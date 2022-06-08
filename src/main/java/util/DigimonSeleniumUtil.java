package util;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import view.Main;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author Gilbert
 * @date 2022/3/25 19:01
 */
public class DigimonSeleniumUtil {
    public static final String URL = "https://wikimon.net/";
    public static WebDriver driver;
    public static String CARD_POSITION="/html/body/div/div[2]/div[2]/div[3]/div[3]/div[1]/a/img";
    public static String downloadPath = Main.FILE_PATH+"\\card";
    public static void pre() throws InterruptedException {
        preChrome();
    }

    private static void preChrome() throws InterruptedException {
        HashMap<String, Object> hashMap = new HashMap<String, Object>(10);
        hashMap.put("download.default_directory", downloadPath);
        ChromeOptions chromeOptions = new ChromeOptions ();
        chromeOptions.setExperimentalOption("prefs", hashMap);
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
        System.setProperty("webdriver.chrome.driver", "Y:/Users/DrGilbert/Desktop/chromedriver.exe");
        driver = new ChromeDriver (desiredCapabilities);
        //driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        System.out.println("Chrome初始化完成!");
        Thread.sleep(3000);
    }

    public static String getImageByCardName(String name) throws InterruptedException {
        System.out.println("卡片密码:"+name);
        driver.get(URL+"File:Dcg-"+name+".jpg");
        System.out.println("准备获取链接...");
        WebElement cardPosition = driver.findElement(By.xpath(CARD_POSITION));
        String cardURL=cardPosition.getAttribute("src");
        System.out.println(cardURL);
        return cardURL;
    }

    public static void after() {
        driver.quit();
    }
}
