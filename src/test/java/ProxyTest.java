import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import yun.helper.Browser;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Administrator on 2015/9/22.
 */
public class ProxyTest {

    public static void main(String[] args) throws SQLException, IOException {
        String hello = Browser.getRandomProxy();
        System.out.println(hello);
//        System.out.println(hello.length());
        System.setProperty("webdriver.chrome.driver",
                "D:\\selenium\\chromedriver.exe");
        String proxy = "203.148.12.132:8000";
        proxy = hello;
        org.openqa.selenium.Proxy p = new org.openqa.selenium.Proxy();
        p.setHttpProxy(proxy).setFtpProxy(proxy).setSslProxy(proxy);
        ChromeOptions co = new ChromeOptions();
        DesiredCapabilities cap = DesiredCapabilities.chrome();
        cap.setCapability(CapabilityType.PROXY, p);
        cap.setCapability(ChromeOptions.CAPABILITY, co);
        WebDriver driver = new ChromeDriver(cap);
//        driver.get("http://www.ip138.com");
        driver.get("http://www.baidu.com/s?wd=ip");
        driver.get("http://weixin.sogou.com/?p=73141200&kw=");
    }

}
