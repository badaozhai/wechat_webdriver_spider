package yun.spider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import yun.helper.Browser;
import yun.model.Article;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Administrator on 2015/9/25.
 */
public class ArticleDetailSpider {
    public WebDriver driver;

    public Article spider(String url){
        Browser.init();
        Article art = parse();
        return art;
    }


    public Article parse(){
        String title = driver.getTitle();
        System.out.println("文章标题---" + title);
        WebElement postDate = driver.findElement(By.cssSelector("#post-date"));
        System.out.println("发布时间---"+postDate.getText());
        WebElement postUser = driver.findElement(By.cssSelector("#post-user"));
        System.out.println("作者---"+postUser.getText());

//        WebElement cnt = driver.findElement(By.cssSelector("#js_content"));
//        System.out.println("内容---" + cnt.getText());

        String html = driver.getPageSource();
        Document doc = Jsoup.parse(html);
        Element content = doc.select("#js_content").first();
        String contentHtml = content.html();
        Document conDoc = Jsoup.parse(contentHtml);
        Elements imgList = conDoc.select("img");
        for (Element img : imgList) {
            String data_src = img.attr("data-src");
            System.out.println("data_src---"+data_src);
            String src = img.attr("src");
            System.out.println("src---"+src);
            String url = img.attr("url");
            System.out.println("url---"+url);
        }
//        System.out.println("内容---"+contentHtml);
        Article art = new Article();
        return art;
    }

}
