package yun.helper;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

/**
 * Created by Administrator on 2015/9/23.
 * 浏览器类
 */
public class Browser {
    public static WebDriver driver;
    /**
     * 获取随机的代理
     * @return
     * @throws IOException
     */
    public static String getRandomProxy() throws IOException {
        String apiUrl =
                "http://svip.kuaidaili.com/api/getproxy" +
                        "?orderid=924720991061426" +
                        "&sort=2" +
                        "&num=1" +
                        "&quality=2" +
                        "&b_pcchrome=1" +
                        "&sp1=1" +
//                        "&carrier=2"+       //电信线路
                        "&an_ha=1" +      //高度匿名
//                "&an_an=1" +        //匿名
//                "&an_tr=1" +        //透明
                        "&area=广东,北京,上海,深圳,江苏,浙江";
        URL proxyApi = new URL(apiUrl);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        proxyApi.openStream()));
        String inputLine;
        String res = "";
        while ((inputLine = in.readLine()) != null) {
            res = res + inputLine;
        }
        in.close();
        return res;
    }
    /**
     * 通过代理初始化浏览器
     * @param proxy
     * @return
     */
    public static WebDriver initByProxy(String proxy){
        String os = System.getProperty("os.name");
        if(os.contains("Windows")){
            System.setProperty("webdriver.chrome.driver",
                    "D:\\selenium\\chromedriver.exe");
            org.openqa.selenium.Proxy p = new org.openqa.selenium.Proxy();
            p.setHttpProxy(proxy).setFtpProxy(proxy).setSslProxy(proxy);
            ChromeOptions co = new ChromeOptions();
            DesiredCapabilities cap = DesiredCapabilities.chrome();
            cap.setCapability(CapabilityType.PROXY, p);
            cap.setCapability(ChromeOptions.CAPABILITY, co);
            driver = new ChromeDriver(cap);
            System.out.println("切换代理成功");
        }else{
            driver = new FirefoxDriver();
        }
        return driver;
    }
    /**
     * 初始化浏览器
     * @return
     */
    public static WebDriver init(){
        String os = System.getProperty("os.name");
        System.out.println(os);
        //windows 下使用chrome linux下使用firefox
        if(os.contains("Windows")){
//            DesiredCapabilities capabilities = DesiredCapabilities.chrome();
//            Proxy proxy = new Proxy();
//            proxy.setHttpProxy("121.34.190.253:9999");
//            capabilities.setCapability("proxy", proxy);
            System.setProperty("webdriver.chrome.driver",
                    "D:\\selenium\\chromedriver.exe");
            driver = new ChromeDriver();
//            driver.get("http://www.ip138.com");
        }else{
            driver = new FirefoxDriver();
        }
        return driver;
    }
    /**
     * 通过选择器，往input中写入词语
     */
    public static void inputAndSubmit(String selector,String word){
        WebElement we = driver.findElement(By.cssSelector(selector));
        if(we != null){
            for (int i = 0; i < word.length(); i++) {
                char  item =  word.charAt(i);
                we.sendKeys(String.valueOf(item));
                Util.sleepRandom(500,1000);
            }
            we.submit();
        }else{
            System.out.println("当前页面:"+driver.getCurrentUrl());
            System.out.println("元素没有找到"+selector);
            System.exit(0);
        }
    }
    /**
     * 通过选择器找到元素并点击
     */
    public static void clickBySelector(String selector){
        WebElement we = driver.findElement(By.cssSelector(selector));
        if(we != null){
            we.click();
        }else{
            System.out.println("当前页面:"+driver.getCurrentUrl());
            System.out.println("元素没有找到"+selector);
            System.exit(0);
        }
    }
    //根据标题切换窗口
    public  static boolean switchToWindow(String windowTitle){
        boolean flag = false;
        try {
            String currentHandle = driver.getWindowHandle();
            Set<String> handles = driver.getWindowHandles();
            for (String s : handles) {
                if (s.equals(currentHandle))
                    continue;
                else {
                    driver.switchTo().window(s);
                    if (driver.getTitle().contains(windowTitle)) {
                        flag = true;
                        System.out.println("Switch to window: "
                                + windowTitle + " successfully!");
                        break;
                    } else
                        continue;
                }
            }
        } catch (NoSuchWindowException e) {
            System.out.println("Window: " + windowTitle+ " cound not found!"+ e.fillInStackTrace());
            flag = false;
        }
        return flag;
    }

    /**
     * 关闭其它窗口
     * @return
     */
    public  static boolean closeOtherWindowByTitle(String title){
        boolean flag = false;
        try {
            String currentHandle = driver.getWindowHandle();
            Set<String> handles = driver.getWindowHandles();
            for (String s : handles) {
                if (s.equals(currentHandle))
                    continue;
                else {
                    driver.switchTo().window(s);
                    if (driver.getTitle().contains(title)) {
                        flag = true;
                        driver.close();
                        break;
                    } else
                        continue;
                }
            }
            driver.switchTo().window(currentHandle);
        } catch (NoSuchWindowException e) {
            System.out.println("Window: " + title+ " cound not found!"+ e.fillInStackTrace());
            flag = false;
        }

        return flag;
    }
    /**
     * 判断元素是否存在
     * @param Locator
     * @return
     */
    public static boolean ElementExist(By Locator) {
        try {
            driver.findElement(Locator);
            return true;
        } catch (org.openqa.selenium.NoSuchElementException ex) {
            return false;
        }
    }

    public static String getNextProxy() throws IOException {
        String apiUrl =
                "http://svip.kuaidaili.com/api/getproxy" +
                        "?orderid=924720991061426" +
                        "&sort=1" +
                        "&num=1" +
                        "&quality=2" +
                        "&b_pcchrome=1" +
                        "&sp1=1" +
                        "&protocol=1" +
                        "&dedup=1" +
//                        "&carrier=2"+       //电信线路
                        "&an_ha=1" +      //高度匿名
//                "&an_an=1" +        //匿名
//                "&an_tr=1" +        //透明
                        "&area=广东,北京,上海,深圳,江苏,浙江";
        URL proxyApi = new URL(apiUrl);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        proxyApi.openStream()));
        String inputLine;
        String res = "";
        while ((inputLine = in.readLine()) != null) {
            res = res + inputLine;
        }
        in.close();
        return res;
    }


}
