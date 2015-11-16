package yun.spider;


import org.apache.bcel.generic.IF_ACMPEQ;
import org.apache.xpath.operations.Bool;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import yun.helper.Browser;
import yun.helper.Util;
import yun.model.Article;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import yun.model.Log;
import yun.model.WeApp;
import yun.mysql.dao.ArticleDao;
import yun.mysql.dao.LogDao;
import yun.mysql.dao.WeAppDao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 通过公众号昵称 公众号号 从搜索页进入进行抓取
 */

public class ArticlesByAppSpider {
    public long startTime = System.currentTimeMillis();
    public WebDriver driver;
    public Boolean goToNext = false;
    public Boolean loadAll = false;
    public Boolean isOnePage = false;
    public Boolean hasArticle = true;
    public String openid;
    public String last_article_docid;
    public Boolean findLastArticle = false;

    public static void main(String[] args) throws SQLException {
        List apps = WeAppDao.getAll();
        System.out.println("公有："+apps.size()+"个公众号需要去抓取文章");
        //遍历所有公众号
        int appsNum = apps.size();
        for (int i = 200; i < appsNum; i++) {
            Map<String, Object> _app = (Map<String, Object>) apps.get(i);
            String appName = (String) _app.get("nick_name");
            String appCode = (String) _app.get("alias");
            String openid = (String) _app.get("openid");
            String last_docid = (String) _app.get("last_docid");
            if (!appCode.equals("") && !appCode.equals("") && !appCode.equals(appName)){
                WeApp app = new WeApp(appName,appCode,openid,last_docid);
                ArticlesByAppSpider sp = new ArticlesByAppSpider();
                List<Article> articles = sp.crawlArticles(app);
                if(articles !=null){
                if (articles.size() > 0) {
                    //文章存入数据库
                    for (Article article : articles) {
                        ArticleDao articleDao = new ArticleDao();
                        article.setApp_name(appName);
                        article.setApp_code(appCode);
                        article.setOpenid(openid);
                        int res = articleDao.save(article);
                        System.out.println(res);
                    }
                    //记录日志
                    Log log = new Log();
                    log.setApp_name(appName);
                    log.setApp_code(appCode);
                    log.setDocid(articles.get(0).getDocid());
                    log.setCol_num(articles.size());
                    LogDao.save(log);
                    //更新最后docid
                    String  newLastDocid = articles.get(0).getDocid();
                    WeAppDao.updateLastDocid(openid,newLastDocid);
                }
                }
            }else {
                System.out.println(appName + appCode + "appName或者appCode不合格");
            }
        }
    }
    /**
     * 根据公众号爬取文章
     *
     * @param app
     * @return
     */
    public List<Article> crawlArticles(WeApp app) {
        List allArticles = new ArrayList();
        AppByNickSpider sp = new AppByNickSpider();
        //搜公众号，定位到公众号页面
        WeApp appInfo = sp.crawlApp(app);
        if (appInfo == null) {
            return null;
        } else {
            driver = Browser.driver;
            openid = app.openid;
            last_article_docid = app.last_docid;
            //切换到公众号列表页
            Browser.switchToWindow(app.nick + "的公众号详情页");
            //关闭其它窗口
            System.out.println(driver.getTitle());
            Browser.closeOtherWindowByTitle(app.nick + "的相关微信公众号");
            Util.sleepRandom();
            List<WebElement> allElements = new ArrayList<WebElement>();
            //解析第一页文章的元素
            List<WebElement> firstPageElements = parseArticleElements();
            allElements = firstPageElements;
            //其它页的所有元素 不好判断 不去抓剩下的页面了
//            while(findLastArticle == false&&goToNextPage()){
//                List<WebElement> otherPageElements = parseArticleElements();
//                allElements = otherPageElements;
//            }
            System.out.println("本次抓取的所有文章元素数量:"+allElements.size());
            allArticles = processArticles(allElements);
            return allArticles;
        }
    }
    /**
     * 爬取所有文章
     *
     * @return
     */
    public List<Article> parseAllArticles() {
        Util.sleepRandom();
        System.out.println("开始时间" + startTime);
        while (!loadAll) {
            (new WebDriverWait(driver, 100)).until(new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver d) {
                    WebElement loadMoreDiv = d.findElement(By.cssSelector("#wxmore"));
                    String style = loadMoreDiv.getAttribute("style");
                    System.out.println(style);
                    if (style.contains("hidden")) {
                        System.out.println("所有页加载完毕");
                        loadAll = true;
                        return true;
                    } else if (style.contains("none")) {
                        if (System.currentTimeMillis() - startTime > 9000) {
                            if (Browser.ElementExist(By.cssSelector("#wxmore")) == false) {
                                hasArticle = false;
                                System.out.println("一篇文章都没");
                                return true;
                            }
                            System.out.println("结束时间" + System.currentTimeMillis());
                            isOnePage = true;
                            loadAll = true;
                            System.out.println("只有一页");
                            return true;
                        } else {
                            loadAll = false;
                            System.out.println("最开始的状态");
                            return false;
                        }
                    } else if (style.contains("visible")) {
                        System.out.println("还有下一页");
                        WebElement loadMoreWe = d.findElement(By
                                .cssSelector("#wxmore a"));
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        loadMoreWe.click();
                        return false;
                    } else {
                        return true;
                    }
                }
            });
        }
        List articles = new ArrayList();
        //如果一篇文章都没发布
        if (hasArticle == false) {
            return articles;
        }
        List<?> divEles = driver.findElements(By.cssSelector(".wx-rb.wx-rb3"));//所有div
        //避免被遮住 点击回顶部
        WebElement goToTop = driver.findElement(By.id("quan_twitter_gotop_0"));
        if (goToTop.isDisplayed() == true) {
            goToTop.click();
        }
        System.out.println("文章div数量" + divEles.size());
        for (int i = 0; i < divEles.size(); i++) {
            WebElement divElement = (WebElement) divEles.get(i);
            Article article = new Article();
            article.setOpenid(openid);
            String docid = divElement.getAttribute("d");// docid 文档id
            System.out.println("docid:" + docid);
            article.setDocid(docid);
            if (docid.equals(last_article_docid)) {
                System.out.println("找到最后的文章了");
                break;
            }
            WebElement aElement = divElement.findElement(By.cssSelector(".news_lst_tab"));//标题a
            String title = aElement.getText().toString();        //获取标题
            String aUrl = aElement.getAttribute("href");//获取跳转前的url
            System.out.println("标题:" + title);
            System.out.println("链接:" + aUrl);
            article.setTitle(title);

            WebElement pElement = divElement.findElement(By.cssSelector(".txt-box p"));//描述p
            String description = pElement.getText().toString();
            System.out.println("描述:" + description);
            article.setDescription(description);

            WebElement tDivElement = divElement.findElement(By.cssSelector(".s-p"));//时间div
            int last_modified = Integer.parseInt(tDivElement.getAttribute("t"));
            System.out.println("最后修改时间:" + last_modified);
            article.setLast_modified(last_modified);

            WebElement imgElement = divElement.findElement(By.cssSelector("img"));//图片img
            String imgSrc = imgElement.getAttribute("src");
            System.out.println("图片src:" + imgSrc);
            article.setImglink(imgSrc);


            String currentHandle = driver.getWindowHandle();
            Util.sleepRandom();
            aElement.click();
            // 获取所有浏览器窗口标识
            Set<String> handles = driver.getWindowHandles();
            for (String handle : handles) {
                // System.out.println(handle);
                if (handle.equals(currentHandle)) {
                    continue;
                } else {
                    driver.switchTo().window(handle);
                    Util.sleepRandom();
                    aUrl = driver.getCurrentUrl();
                    if (aUrl != null || aUrl.length() > 0) {
                        article.setUrl(aUrl);
                        String htmlStr = driver.getPageSource();
                        Document doc = Jsoup.parse(htmlStr);
                        Element content = doc.select("#js_content").first();
                        String contentHtml = "空，没找到js_content的元素";
                        if (content != null) {
                            contentHtml = content.html();
                        }
                        article.setContent(contentHtml);
                        System.out.println("重定向后的:" + aUrl);
                        System.out.println("\n");
                        driver.close();
                    }
                }
                // 获取详细内容
//				String htmlStr = d.getPageSource();
//				System.out.println(htmlStr);
            }
            driver.switchTo().window(currentHandle);
            articles.add(article);
        }
        return articles;
    }

    /**
     * 解析当前页面文章 返回元素
     * @return
     */
    public List<WebElement> parseArticleElements() {
        Util.sleepRandom();
        List<WebElement> articlesWelemets = new ArrayList();
        //如果一篇文章都没发布
        if (hasArticle == false) {
            return articlesWelemets;
        }
        List<?> divEles = driver.findElements(By.cssSelector(".wx-rb.wx-rb3"));//所有div
        //避免被遮住 点击回顶部
        try {
            WebElement goToTop = driver.findElement(By.id("quan_twitter_gotop_0"));
            if (goToTop.isDisplayed() == true) {
                goToTop.click();
            }
        } catch (org.openqa.selenium.NoSuchElementException ex) {
            System.out.println("回顶部按钮没找到");
        }
        for (int i = 0; i < divEles.size(); i++) {
            WebElement divElement = (WebElement) divEles.get(i);
            String docid = divElement.getAttribute("d");// docid 文档id
            System.out.println("docid:" + docid);
            if (docid.equals(last_article_docid)) {
                findLastArticle = true;
                System.out.println("找到最后的文章了");
                break;
            }
            articlesWelemets.add(divElement);
        }
        System.out.println("本页文章div数量" + articlesWelemets.size());
        return articlesWelemets;
    }

    public List<Article> processArticles(List divEles){

        List articles = new ArrayList();
        //如果一篇文章都没发布
        if (hasArticle == false) {
            return articles;
        }
        for (int i = 0; i < divEles.size(); i++) {

            Util.sleepRandom();
            WebElement divElement = (WebElement) divEles.get(i);
            Article article = new Article();
            article.setOpenid(openid);
            String docid = divElement.getAttribute("d");// docid 文档id
            System.out.println("docid:" + docid);
            article.setDocid(docid);
            if (docid.equals(last_article_docid)) {
                System.out.println("找到最后的文章了");
                break;
            }
            WebElement aElement = divElement.findElement(By.cssSelector(".news_lst_tab"));//标题a
            String title = aElement.getText().toString();        //获取标题
            String aUrl = aElement.getAttribute("href");//获取跳转前的url
            System.out.println("标题:" + title);
            System.out.println("链接:" + aUrl);
            article.setTitle(title);

            WebElement pElement = divElement.findElement(By.cssSelector(".txt-box p"));//描述p
            String description = pElement.getText().toString();
            System.out.println("描述:" + description);
            article.setDescription(description);

            WebElement tDivElement = divElement.findElement(By.cssSelector(".s-p"));//时间div
            int last_modified = Integer.parseInt(tDivElement.getAttribute("t"));
            System.out.println("最后修改时间:" + last_modified);
            article.setLast_modified(last_modified);

            WebElement imgElement = divElement.findElement(By.cssSelector("img"));//图片img
            String imgSrc = imgElement.getAttribute("src");
            System.out.println("图片src:" + imgSrc);
            article.setImglink(imgSrc);
            String currentHandle = driver.getWindowHandle();
            if(4<i) {
                JavascriptExecutor JS = (JavascriptExecutor) driver;
                String high = "scroll(0,1000);";//滚动到Y值10000像素的位置，一般10000就到页面的底部了，可以根据自己需要调试
                JS.executeScript(high);
            }
            aElement.click();
            System.out.println(driver.getTitle());
            // 获取所有浏览器窗口标识
            Set<String> handles = driver.getWindowHandles();
            for (String handle : handles) {
                // System.out.println(handle);
                if (handle.equals(currentHandle)) {
                    continue;
                } else {
                    Util.sleepRandom();
                    driver.switchTo().window(handle);
                    aUrl = driver.getCurrentUrl();
                    if (aUrl != null || aUrl.length() > 0) {
                        article.setUrl(aUrl);
                        String htmlStr = driver.getPageSource();
                        Document doc = Jsoup.parse(htmlStr);
                        Element content = doc.select("#js_content").first();
                        String contentHtml = "空，没找到js_content的元素";
                        if (content != null) {
                            contentHtml = content.html();
                        }
                        article.setContent(contentHtml);
                        System.out.println("重定向后的:" + aUrl);
                        System.out.println("\n");
                        driver.close();
                    }
                }
                // 获取详细内容
//				String htmlStr = d.getPageSource();
//				System.out.println(htmlStr);
            }
            driver.switchTo().window(currentHandle);
            articles.add(article);
        }
        driver.quit();
        return articles;
    }
    /**
     * 进入下一页
     * @return
     */
    public Boolean goToNextPage() {
        Util.sleepRandom();
        System.out.println("开始时间" + startTime);
        (new WebDriverWait(driver, 100)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                WebElement loadMoreDiv = d.findElement(By.cssSelector("#wxmore"));
                String style = loadMoreDiv.getAttribute("style");
                System.out.println(style);
                if (style.contains("hidden")) {
                    System.out.println("所有页加载完毕");
                    goToNext = false;
                    return true;
                } else if (style.contains("none")) {
                    if (System.currentTimeMillis() - startTime > 9000) {
                        if (Browser.ElementExist(By.cssSelector("#wxmore")) == false) {
                            goToNext = false;
                            hasArticle = false;
                            System.out.println("一篇文章都没");
                            return true;
                        }
                        System.out.println("结束时间" + System.currentTimeMillis());
                        System.out.println("只有一页");
                        return true;
                    } else {
                        loadAll = false;
                        System.out.println("最开始的状态");
                        return false;
                    }
                } else if (style.contains("visible")) {
                    System.out.println("还有下一页");
                    WebElement loadMoreWe = d.findElement(By
                            .cssSelector("#wxmore a"));
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    loadMoreWe.click();
                    goToNext = true;
                    return false;
                } else {
                    goToNext = false;
                    return true;
                }
            }
        });
        return goToNext;
    }

    /**
     * 进一步进入详情页获取详细信息
     * @return
     */
    public Article getDetail(Article article, WebElement articleElement) {
        String currentHandle = driver.getWindowHandle();
        Util.sleepRandom();
        articleElement.click();
        // 获取所有浏览器窗口标识
        Set<String> handles = driver.getWindowHandles();
        for (String handle : handles) {
            // System.out.println(handle);
            if (handle.equals(currentHandle)) {
                continue;
            } else {
                driver.switchTo().window(handle);
                Util.sleepRandom();
                String aUrl = driver.getCurrentUrl();
                if (aUrl != null || aUrl.length() > 0) {
                    article.setUrl(aUrl);
                    String htmlStr = driver.getPageSource();
                    Document doc = Jsoup.parse(htmlStr);
                    Element content = doc.select("#js_content").first();
                    String contentHtml = "空，没找到js_content的元素";
                    if (content != null) {
                        contentHtml = content.html();
                    }
                    article.setContent(contentHtml);
                    System.out.println("重定向后的:" + aUrl);
                    System.out.println("\n");
                    driver.close();
                }
            }
            // 获取详细内容
//				String htmlStr = d.getPageSource();
//				System.out.println(htmlStr);
        }
        driver.switchTo().window(currentHandle);
        return article;
    }

}
