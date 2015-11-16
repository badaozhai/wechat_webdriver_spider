package yun.demo;

import yun.model.Article;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Spider {
    public static Boolean loadAll = false;
    //获取第一页数据
    public List<Article> getFirstPageArticlesByOpenid(String openid) {
        String weChatUrl = "http://weixin.sogou.com/gzh?openid=" + openid;
        System.setProperty("webdriver.chrome.driver",
                "D:\\selenium\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();

        driver.get(weChatUrl);
        //等待10秒判定为超时  直到找到元素
        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {

            public Boolean apply(WebDriver d) {
                // 获取一页的数据
                // 查找最后一个元素
                WebElement lastA = d.findElement(By
                        .id("sogou_vr_11002601_title_9"));
                WebElement lastDiv = d.findElement(By
                        .id("sogou_vr_11002601_box_9"));
                // 知道最后一个元素找到时才去执行业务逻辑
                if (lastA != null && lastDiv != null) {
                    System.out.println("加载好了");
                    return true;
                } else {
                    System.out.println("没加载完");
                    return false;
                }

            }
        });

        List<?> divEles = driver.findElements(By.cssSelector(".wx-rb.wx-rb3"));//所有div

        System.out.println(divEles.size());
        List articles = new ArrayList();
        for (int i = 0; i < divEles.size(); i++) {
            Article article = new Article();
            WebElement divElement = (WebElement) divEles.get(i);

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


            String docid = divElement.getAttribute("d");// docid 文档id
            System.out.println("docid:" + docid);
            article.setDocid(docid);
            String currentHandle = driver.getWindowHandle();

            aElement.click();

            // 获取所有浏览器窗口标识
            Set<String> handles = driver.getWindowHandles();
            for (String handle : handles) {
                // System.out.println(handle);
                if (handle.equals(currentHandle)) {
                    continue;
                } else {
                    driver.switchTo().window(handle);
                    aUrl = driver.getCurrentUrl();
                    article.setUrl(aUrl);
                    System.out.println("重定向后的:" + aUrl);
                    System.out.println("\n");
                    driver.close();
                }
                // 获取详细内容
//				String htmlStr = d.getPageSource();
//				System.out.println(htmlStr);
            }
            driver.switchTo().window(currentHandle);
            articles.add(article);
        }
        System.out.println(articles);
        driver.quit();
        return articles;
    }
    //抓取所有发布过的文章 此处有bug，就是当文章数量小于10的时候 无法判定页面是否全部加载完毕
    public List<Article> getAllArticlesByOpenid(String openid) {
        String weChatUrl = "http://weixin.sogou.com/gzh?openid=" + openid;
        System.setProperty("webdriver.chrome.driver",
                "D:\\selenium\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get(weChatUrl);
        while (!Spider.loadAll) {
            (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver d) {
                    WebElement loadMoreDiv = d.findElement(By.cssSelector("#wxmore"));
                    String style = loadMoreDiv.getAttribute("style");
                    System.out.println(style);
                    if (style.contains("hidden")) {
                        System.out.println("加载完毕");
                        Spider.loadAll = true;
                        return true;
                    } else if (style.contains("none")) {
                        System.out.println("未完全加载");
                        return false;
                    } else {
                        System.out.println("还有下一页");
                        WebElement loadMoreWe = d.findElement(By
                                .cssSelector("#wxmore a"));
                        loadMoreWe.click();
                        return false;
                    }
                }
            });
        }
//        System.exit(0);
        List<?> divEles = driver.findElements(By.cssSelector(".wx-rb.wx-rb3"));//所有div
        //避免被遮住 点击回顶部
        WebElement goToTop = driver.findElement(By.id("quan_twitter_gotop_0"));
        if(goToTop != null){
            goToTop.click();
        }
        System.out.println(divEles.size());
        List articles = new ArrayList();
        for (int i = 0; i < divEles.size(); i++) {
            Article article = new Article();
            WebElement divElement = (WebElement) divEles.get(i);

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


            String docid = divElement.getAttribute("d");// docid 文档id
            System.out.println("docid:" + docid);
            article.setDocid(docid);
            String currentHandle = driver.getWindowHandle();
            //为了避免被遮住
//            if(i>10){
//                WebElement divElement = (WebElement) divEles.get(i);
//                WebElement aElement = divElement.findElement(By.cssSelector(".news_lst_tab"));//标题a
//                Actions actions=new Actions(driver);
//                actions.moveToElement(foot).perform();
//                actions.moveToElement(aElement).perform();
//            }
            aElement.click();
            // 获取所有浏览器窗口标识
            Set<String> handles = driver.getWindowHandles();
            for (String handle : handles) {
                // System.out.println(handle);
                if (handle.equals(currentHandle)) {
                    continue;
                } else {
                    driver.switchTo().window(handle);
                    aUrl = driver.getCurrentUrl();
                    article.setUrl(aUrl);
                    System.out.println("重定向后的:" + aUrl);
                    System.out.println("\n");
                    driver.close();
                }
                // 获取详细内容
//				String htmlStr = d.getPageSource();
//				System.out.println(htmlStr);
            }
            driver.switchTo().window(currentHandle);
            articles.add(article);
        }
        System.out.println(articles);
        driver.quit();
        return articles;
    }
    public List<Article> getAllArticlesByUrl(String url) {
        String weChatUrl = url;
        System.setProperty("webdriver.chrome.driver",
                "D:\\selenium\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get(weChatUrl);

        while (!Spider.loadAll) {
            (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver d) {
                    WebElement loadMoreDiv = d.findElement(By.cssSelector("#wxmore"));
                    String style = loadMoreDiv.getAttribute("style");
                    System.out.println(style);
                    if (style.contains("hidden")) {
                        System.out.println("加载完毕");
                        Spider.loadAll = true;
                        return true;
                    } else if (style.contains("none")) {
                        System.out.println("未完全加载");
                        return false;
                    } else {
                        System.out.println("还有下一页");
                        WebElement loadMoreWe = d.findElement(By
                                .cssSelector("#wxmore a"));
                        loadMoreWe.click();
                        return false;
                    }
                }
            });
        }
//        System.exit(0);
        List<?> divEles = driver.findElements(By.cssSelector(".wx-rb.wx-rb3"));//所有div
        //避免被遮住 点击回顶部
        WebElement goToTop = driver.findElement(By.id("quan_twitter_gotop_0"));
        if(goToTop != null){
            goToTop.click();
        }
        System.out.println(divEles.size());
        List articles = new ArrayList();
        for (int i = 0; i < divEles.size(); i++) {
            Article article = new Article();
            WebElement divElement = (WebElement) divEles.get(i);

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


            String docid = divElement.getAttribute("d");// docid 文档id
            System.out.println("docid:" + docid);
            article.setDocid(docid);
            String currentHandle = driver.getWindowHandle();
            //为了避免被遮住
//            if(i>10){
//                WebElement divElement = (WebElement) divEles.get(i);
//                WebElement aElement = divElement.findElement(By.cssSelector(".news_lst_tab"));//标题a
//                Actions actions=new Actions(driver);
//                actions.moveToElement(foot).perform();
//                actions.moveToElement(aElement).perform();
//            }
            aElement.click();
            // 获取所有浏览器窗口标识
            Set<String> handles = driver.getWindowHandles();
            for (String handle : handles) {
                // System.out.println(handle);
                if (handle.equals(currentHandle)) {
                    continue;
                } else {
                    driver.switchTo().window(handle);
                    aUrl = driver.getCurrentUrl();
                    article.setUrl(aUrl);
                    System.out.println("重定向后的:" + aUrl);
                    System.out.println("\n");
                    driver.close();
                }
                // 获取详细内容
//				String htmlStr = d.getPageSource();
//				System.out.println(htmlStr);
            }
            driver.switchTo().window(currentHandle);
            articles.add(article);
        }
        System.out.println(articles);
        driver.quit();
        return articles;
    }
}
