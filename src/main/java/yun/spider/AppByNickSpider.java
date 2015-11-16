package yun.spider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import yun.helper.Browser;
import yun.helper.Util;
import yun.model.Article;
import yun.model.WeApp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 通过公众号昵称 公众号号 抓取公众号
 */

public class AppByNickSpider {
	public long startTime = System.currentTimeMillis();
	public  WebDriver driver;
	public  String weChatUrl = "http://weixin.sogou.com/";
	public  WeApp weAppInfo;//抓取到的全的公众号信息
    public  Boolean hasFindApp = false;
    public  Boolean hasNextPage = true;
	public String openid = "";
	public String appQuerySelector = "#public-num";
	public String searchInputSelector = "#query";
    public String nextPageSelector = "#sogou_next";
	public WeApp crawlApp(WeApp app){
		String appNick = app.nick;
		String appCode = app.appcode;
        weAppInfo = new WeApp(appNick,appCode);
        //初始化浏览器
		driver = Browser.init();
		//初始化搜狗首页
		driver.get(weChatUrl);
        Util.sleepRandom(2000,3000);
		//切换搜公众号按钮
		Browser.clickBySelector(appQuerySelector);
		Util.sleepRandom();
		//在搜索框中写入搜索词并且提交
		Browser.inputAndSubmit(searchInputSelector, appNick);
        Util.sleepRandom();
        // 第一页搜索解析公众号 res 0 没有找到 1已经找到 2法律法规不显示
		int res = parseAppByCode(appCode);
        System.out.println("parseAppByCode结果:"+res);
		if(res > 1){
            //出错了 404 验证码等问题
			driver.quit();
			return null;
		}
        if(res == 1){
            //第一页找到该公众号了
            return weAppInfo;
        }
        //如果文章没有找到 从第二页开始循环 获取剩下页面的文章
        while(hasNextPage==true && hasFindApp==false){
            //进入下一页
            Boolean gotoNext = gotoNextPage();
            if(gotoNext == true){
                res = parseAppByCode(appCode);
            }else{
                hasNextPage = false;
            }
        }
        if(res == 1){
            //剩余的页面找到该公众号了
            return weAppInfo;
        }else if(res == 0){
            //翻遍所有页面都没找到该公众号
            driver.quit();
            return null;
        }else if(res > 1){
            //出错了 404 验证码等问题
            driver.quit();
            return null;
        }
        driver.quit();
		return weAppInfo;
	}

    /**
     * 通过appCode找公众号
     * @param appCode
     * @return
     */
    public int parseAppByCode(String appCode){
        int res = 0;
        Util.sleepRandom();
        if (Browser.ElementExist(By.id("noresult_frontbl_container")) == true || Browser.ElementExist(By.id("noresult_part1_container"))) {
            //法律法规 or 找不到公众号
            System.out.println("法律法规 or 找不到公众号"+driver.getCurrentUrl());
            res = 2;
            return res;
        }
        if(Browser.ElementExist(By.id("seccodeInput")) == true){
            //出现了验证码
            System.out.println("出现了验证码"+driver.getCurrentUrl());
            res = 3;
            return res;
        }
        List<?> appElements = driver.findElements(By.cssSelector(".txt-box h4 span"));//获取所有公众号的元素
        List<?> appA = driver.findElements(By.cssSelector(".txt-box h3"));
        for (int i = 0; i < appElements.size(); i++) {
            WebElement appEle = (WebElement) appElements.get(i);
            String _appCode = appEle.getText().toString();
            String[] strarray = _appCode.split("：");
            System.out.println("本页appCode:"+strarray[1]);
            if (appCode.equals(strarray[1])) {
                System.out.println("找到该公众号了");
                WebElement appDiv = driver.findElements(By.cssSelector(".wx-rb.bg-blue.wx-rb_v1._item")).get(i);
                String appUrl = appDiv.getAttribute("href");
                Pattern p3 = Pattern.compile("(?<=openid=).*(?=&)");
                Matcher m3 = p3.matcher(appUrl);
                m3.find();
                openid = m3.group();
                WebElement h3 = (WebElement)appA.get(i);
                //找到该公众号了
                hasFindApp = true;
                res = 1;
                h3.click();
                break;
            }
        }
        return res;
    }
    /**
     * 进入下一页
     * @return
     */
    public Boolean gotoNextPage(){
        try {
            //如果出现没有结果 获取到的页面数量为0
            Util.sleepRandom();
            WebElement nextButton = driver.findElement(By.cssSelector(nextPageSelector));
            if(nextButton != null){
                nextButton.click();
                return true;
            }else{
                hasNextPage = false;
                return false;
            }
        }catch (org.openqa.selenium.NoSuchElementException ex) {
            System.out.println("公众号列表页没有下一页了");
            hasNextPage = false;
            return false;
        }
    }

}
