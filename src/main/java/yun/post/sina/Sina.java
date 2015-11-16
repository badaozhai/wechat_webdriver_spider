package yun.post.sina;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

/**
 * Created by Administrator on 2015/10/12.
 */
public class Sina {
    public WebDriver driver;
    public String initUrl = "http://weibo.com/";
    public Boolean loadAll = false;
    public void init(){
        System.setProperty("webdriver.chrome.driver",
                "D:\\selenium\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver(options);
//        initUrl = "http://dzs.qisuu.com/txt/%E9%BE%99%E5%8D%B0%E6%88%98%E7%A5%9E.txt";
        driver.get(initUrl);

    }
    public void login(final String username,String password){
        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                WebElement usernameInput = driver.findElement(By.name("username"));
                WebElement passInput = driver.findElement(By.name("password"));
                if (usernameInput != null && passInput != null) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        WebElement usernameInput = driver.findElement(By.name("username"));
        usernameInput.sendKeys(username);
        WebElement passInput = driver.findElement(By.name("password"));
        passInput.sendKeys(password);
        WebElement button = driver.findElement(By.cssSelector(".W_btn_a.btn_40px"));
        button.click();
    }
    public void post(String title,String content){
        //点击进入首页
        WebElement indexA = driver.findElement(By.cssSelector(".S_txt1.home"));
        indexA.click();
        //点击长微博
        WebElement longWebA = driver.findElements(By.cssSelector(".kind a ")).get(4);
        longWebA.click();
        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
//                List divs = driver.findElements(By.cssSelector("body>div"));
//                WebElement lastDiv = (WebElement) divs.get(divs.size()-1);
                WebElement iframes = driver.findElement(By.cssSelector("iframe"));
                if (iframes != null ) {

                    return true;
                } else {
                    return false;
                }
            }
        });
//        List divs = driver.findElements(By.cssSelector("body>div"));
//        WebElement lastDiv = (WebElement) divs.get(divs.size()-1);
        List iframes = driver.findElements(By.cssSelector("iframe"));
//        driver.switchTo().frame(iframes);
        //输入标题
//        WebElement titleInput = driver.findElement(By.cssSelector(".con_tit input"));
//        titleInput.sendKeys(Keys.chord(Keys.CONTROL+"a"));
//        titleInput.sendKeys(Keys.chord(Keys.CONTROL+"x"));
//        titleInput.sendKeys(title);
//        //输入内容
////        WebElement div = driver.findElement(By.cssSelector("WB_layer_longwb_iframe.WB_layer_longwb_iframe_v3"));
//
//        //点击提交
//        WebElement buttons = driver.findElement(By.cssSelector("W_layer_btn.S_bg1"));
//        WebElement subButton = buttons.findElement(By.partialLinkText("发布"));
//        subButton.click();

    }
}
