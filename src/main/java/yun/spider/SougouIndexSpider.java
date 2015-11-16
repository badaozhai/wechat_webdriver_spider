package yun.spider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import yun.helper.Browser;
import yun.model.Article;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Thread.sleep;

/**
 * 搜狗首页抓取
 */
public class SougouIndexSpider {
    public WebDriver driver;
    public  String weChatUrl = "http://weixin.sogou.com";
    public  Boolean hasParsed = false;
    public  Boolean loadAll = false;
    public int typeindex ;
    public List<Article> spider() throws UnsupportedEncodingException {
        driver = Browser.init();
//        getHotSearch();
//        getHotWords();
//        List<WebElement> types = getTypes();
//        typeindex = 15;
//        if(typeindex>8){
//            driver.findElement(By.cssSelector("#more_anchor")).click();
//        }
//        getArticles();
//        driver.quit();
        int index = 3;
//        getArticlesByUrl(index);
        List articles = null;
        return articles;
    }
    //搜索页面第一步初始化

    //获取微信热搜榜
    public void getHotSearch(){
        List<WebElement> hotList = driver.findElements(By.cssSelector(".wx-ph li a"));
        for(WebElement hotA :hotList){
            System.out.println("首页微信热搜榜---"+hotA.getText());
        }
    }
    //获取订阅热词
    public void getHotWords(){
        List<WebElement> hotList = driver.findElements(By.cssSelector(".re-box a"));

        for(WebElement hotA :hotList){
            System.out.println("订阅热词---"+hotA.getText());
        }

    }
    //获取类别
    public List<WebElement> getTypes(){
        List<WebElement> TypeListAll = new ArrayList<WebElement>();
        //本身就有的类别
        List<WebElement> TypeList = driver.findElements(By.cssSelector("#wx-tabbox-ul a"));
        for(WebElement typeA :TypeList){
            if(typeA.getAttribute("id").equals("more_anchor")){
                typeA.click();
                break;
            }
            System.out.print("类别标签a的id---" + typeA.getAttribute("id"));
            System.out.println("首页类别---" + typeA.getText());
            TypeListAll.add(typeA);
        }
        //点击更多的类别
        List<WebElement> TypeListMore = driver.findElements(By.cssSelector("#gd-tab a"));
        for(WebElement typeA :TypeListMore){
            TypeListAll.add(typeA);
            System.out.print("点击跟多加载后的id---"+typeA.getAttribute("id"));
            System.out.println("点击跟多加载后的名称---" + typeA.getText());
        }
        return TypeListAll;
    }
    //todo
    public void getArticles(){
        //根据索引找到元素并点击
        String typeid = String.format("#pc_%s",typeindex);
        WebElement typeElement = driver.findElement(By.cssSelector(typeid));
        typeElement.click();
        //获取文章div
        ExpectedCondition<Boolean> isloaded = new ExpectedCondition<Boolean>(){
            public Boolean apply(WebDriver d) {

                String divID =  String.format("#pc_%s_d",typeindex);
                System.out.println(divID);
                //获取文章div
                WebElement div = driver.findElement(By.cssSelector(divID));
                WebElement logoEle = div.findElement(By.cssSelector(".pos-wxrw p img"));

                if (div != null && logoEle !=null) {
                    for(int scrollTime =1;scrollTime<11;scrollTime++){
                        System.out.println(888);
                        String setscroll = "document.documentElement.scrollTop=document.body.scrollHeight";
                        JavascriptExecutor jse=(JavascriptExecutor) driver;
                        jse.executeScript(setscroll);
                    }
//                    Actions actions = new Actions(driver);
//                    actions.moveToElement(foot).perform();
                    System.out.println("div内容"+div.getText());
                    String logo = logoEle.getAttribute("src");
                    System.out.println(logo);
                    return true;
                } else {
                    return false;
                }
            }
        };
        (new WebDriverWait(driver, 10)).until(isloaded);
    }
    //拼接url的方式
    public List<Article> getArticlesByUrl(int index,String lastDocid) throws IOException, InterruptedException, ParseException {
        String url = "";
        List articles = new ArrayList();
        Boolean hasSpider = false;
        //最多15页
        for (int i = 0; i <= 10; i++) {
            if(hasSpider == false){
                if (i == 0) {
                    url = String.format("http://weixin.sogou.com/pcindex/pc/pc_%s/pc_%s.html", index, index);
                } else {
                    url = String.format("http://weixin.sogou.com/pcindex/pc/pc_%s/%s.html", index, i);
                }
                Thread.sleep(500);
                Document doc = null;
                long pageListStartTime = System.currentTimeMillis();
                try{
                    doc = Jsoup.connect(url).get();
                }
                catch(Exception e){
                    System.out.println("超时文章列表url:"+url);
                    e.printStackTrace();
                }
                finally{
                    System.out.println("请求文章列表页"+url+"----耗时:"+(System.currentTimeMillis()-pageListStartTime) + "ms");
                }
//                Document doc = Jsoup.connect(url).get();
                //获取一页文章的列表
                Elements lis = doc.select("li");
                //遍历文章元素
                for(Element li :lis){

                    Article article = new Article();
                    //设置类别
                    article.setTypeid(index+1);
                    //docid
                    // 判断采集的断点  跳出循环
                    String docid = li.attr("id");
                    if(docid.equals(lastDocid)){
                        hasSpider = true;
                        System.out.println("类别id"+(index+1)+"跳出遍历一页中文章元素循环");
                        break;
                    }
                    System.out.println(docid);
                    article.setDocid(docid);
                    //标题
                    Element titleNode = li.select(".wx-news-info2 h4 a").first();
                    String title = titleNode.text();
                    System.out.println(title);
                    article.setTitle(title);
                    //描述
                    Element descNode = li.select(".wx-news-info2 a").get(1);
                    String desc = descNode.text();
                    System.out.println(desc);
                    article.setDescription(desc);
                    //文章url
                    String articleUrl = titleNode.attr("href");
                    System.out.println(articleUrl);
                    article.setUrl(articleUrl);
                    //内容
                    long articleDetaiStartlTime = System.currentTimeMillis();
                    Document contentDoc = null;
                    try{
                        contentDoc = Jsoup.connect(articleUrl).get();
                    }
                    catch(Exception e){
                        System.out.println("超时文章url:"+articleUrl);
                        e.printStackTrace();
                    }
                    finally{
                        System.out.println("请求该文章详情页耗时:"+(System.currentTimeMillis()-articleDetaiStartlTime) + "ms");
                    }
                    Element content = contentDoc.select("#js_content").first();
                    String contentHtml = "空，没找到js_content的元素";
                    if(content != null){
                        contentHtml = content.html();
                    }else{
                        System.out.println(contentHtml);
                        System.out.println("当前url:"+articleUrl);

                    }
                    article.setContent(contentHtml);
                    //文章logo
                    Element logoNode = li.select(".wx-img-box a img").first();
                    String imgLink = logoNode.attr("src");
                    System.out.println(imgLink);
                    article.setImglink(imgLink);
                    //阅读数
                    Element readNode = li.select(".wx-news-info2 .s-p").first();

                    //正则提取阅读数 发布时间
                    Pattern p = Pattern.compile("\\d\\d*");
                    String readText = readNode.text();
                    Matcher m = p.matcher(readText);
                    m.find();
                    int readNum = Integer.parseInt(m.group());
                    System.out.println("阅读数"+readNum);
                    article.setRead_num(readNum);
                    //发布时间
                    Pattern p2 = Pattern.compile("\\d{2}:\\d{2}");
                    Matcher m2 = p2.matcher(readText);
                    int time = 0;
                    if(m2.find()){
                        String t = m2.group();
                        System.out.println("时间:" + t);
                        Calendar now = Calendar.getInstance();
                        int year = now.get(Calendar.YEAR);
                        int month = (now.get(Calendar.MONTH) + 1);
                        int day = now.get(Calendar.DAY_OF_MONTH);
                        String timeString = String.format("%s-%s-%s %s", year, month,day,t);
                        System.out.println(timeString);
                        String format = "yyyy-MM-dd HH:mm";
                        SimpleDateFormat sdf = new SimpleDateFormat(format);
                        Date date = sdf.parse(timeString);
                        String strTime = date.getTime() + "";
                        strTime = strTime.substring(0, 10);
                        time = Integer.parseInt(strTime);
                        System.out.println(time);
                    }else{
                        System.out.println("发布时间没找到");
                        System.out.println("当前url:"+articleUrl);
                    }
                    article.setLast_modified(time);
                    //公众号名
                    Element appNameNode = li.select(".pos-wxrw a p").get(1);
                    String appName = appNameNode.text();
                    article.setApp_name(appName);
                    //公众号openid
                    Element appNode = li.select(".pos-wxrw a").first();
                    String appUrl = appNode.attr("href");
                    Pattern p3 = Pattern.compile("(?<=openid=).*(?=&)");
                    Matcher m3 = p3.matcher(appUrl);
                    String openid = "";
                    if(m3.find()){
                        openid = m3.group();
                    }else{
                        System.out.println("openid not found");
                        System.out.println("当前url:"+articleUrl);
                    }
                    System.out.println("");
                    System.out.println("");
                    article.setOpenid(openid);
                    articles.add(article);
                }
            }else{
                System.out.println("该类别"+(index+1)+"已经最新,进入下一个类别，跳出此次循环");
                break;
            }
        }
        return articles;
    }
    /**
     * 最热收藏
     */
}
