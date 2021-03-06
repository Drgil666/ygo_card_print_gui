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
public class YuGiOhSeleniumUtil {
    public static final String URL = "https://tools.kooriookami.top/#/yugioh";
    public static final String CARD_NAME_XPATH = "/html/body/div[1]/div/section/main/div/div/div[2]/div/div[1]/div/div/div[2]/form/div[2]/div[2]/div/div/input";
    public static final String CARD_CLICK_XPATH = "/html/body/div[2]/div[2]/div/div/div[1]/ul/li";
    public static final String DOWNLOAD_BUTTON = "/html/body/div[1]/div/section/main/div/div/div[2]/div/div[1]/div/div/div[2]/div[1]/div/div[5]/button";
    public static final String CARD_LANGUAGE = "/html/body/div[1]/div/section/main/div/div/div[2]/div/div[1]/div/div/div[2]/form/div[1]/div[2]/div/div/div/input";
    public static final String JAPANESE_CARD = "/html/body/div[2]/div[1]/div/div/div[1]/ul/li[3]";
    public static final String CHINESE_CARD = "/html/body/div[2]/div[1]/div/div/div[1]/ul/li[1]";
    public static WebDriver driver;
    public static final String CARD_CODE = "/html/body/div[1]/div/section/main/div/div/div[2]/div[1]/div[1]/div/div/div[2]/form/div[16]/div[2]/div/div[1]/div/input";
    public static final String CARD_CODE_CONFIRM = "/html/body/div[1]/div/section/main/div/div/div[2]/div[1]/div[1]/div/div/div[2]/form/div[16]/div[2]/div/div[2]/div/button[1]";
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
        System.setProperty("webdriver.chrome.driver", Main.FILE_PATH+"/chrome/Application/chromedriver.exe");
        driver = new ChromeDriver (desiredCapabilities);
        //driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get(URL);
        Main.addLine("Chrome???????????????!");
        Thread.sleep(3000);
    }

    public static void getImageByCardName(String name) throws InterruptedException {
        driver.navigate().refresh();
        Thread.sleep(2000);
        WebElement cardName = driver.findElement(By.xpath(CARD_NAME_XPATH));
        cardName.clear();
        cardName.sendKeys(name);
        System.out.println("???????????????" + name);
        //????????????
        WebElement cardClick = driver.findElement(By.xpath(CARD_CLICK_XPATH));
        cardClick.click();
        System.out.println("????????????");
        //????????????,????????????
        Thread.sleep(20000);
        System.out.println("????????????...");
        WebElement downloadButton = driver.findElement(By.xpath(DOWNLOAD_BUTTON));
        downloadButton.click();
        System.out.println("??????");
        Thread.sleep(10000);
    }

    public static String getImageByCardCode(String code) throws InterruptedException {
        driver.navigate().refresh();
        Thread.sleep(2000);
        //????????????
        Main.addLine("??????????????????:" + code);
        WebElement cardCode = driver.findElement(By.xpath(CARD_CODE));
        cardCode.clear();
        cardCode.sendKeys(code);
//        Main.addLine("????????????");
        WebElement cardCodeConfirm = driver.findElement(By.xpath(CARD_CODE_CONFIRM));
        cardCodeConfirm.click();
        //????????????,????????????
        Thread.sleep(10000);
//        Main.addLine ("????????????...");
        WebElement downloadButton = driver.findElement(By.xpath(DOWNLOAD_BUTTON));
        downloadButton.click();
//        Main.addLine("??????");
        Thread.sleep(10000);
        WebElement cardName = driver.findElement(By.xpath("/html/body/div[1]/div/section/main/div/div/div[2]/div/div[1]/div/div/div[2]/form/div[2]/div[2]/div/div/div/input"));
        Main.addLine("?????????" + cardName.getAttribute("value"));
        Thread.sleep(5000);
        return cardName.getAttribute("value");
    }

    public static void after() {
        driver.quit();
    }
}
