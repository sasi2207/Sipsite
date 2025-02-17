package com.uk.uk.implementation;

import com.uk.uk.entity.ProductMasterDataDAO;
import com.uk.uk.repository.PricingInsightsRepo;
import com.uk.uk.repository.ProductMasterDataRepo;
import com.uk.uk.repository.PriceRepo;
import com.uk.uk.implementation.CommonUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.*;

@Service
public class TescoImpl {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    ProductMasterDataRepo ProductMasterDataRepo;
    @Autowired
    PricingInsightsRepo ProductInsightsRepo;
    @Autowired
    PriceRepo PriceRepo;
    @Autowired
    private CommonUtil CommonUtil;

    @Value("${sipsite.driver.geckodriver.path}")
    private String geckoDriverPath;
    // creating a logger
    Logger logger
            = LoggerFactory.getLogger(TescoImpl.class);


    public void getProductDetails() throws InterruptedException {
        List<ProductMasterDataDAO> productMasterDataList = new ArrayList<>();

        // Filter the ProductMasterData table for "Tesco"
        productMasterDataList = ProductMasterDataRepo.getProductMasterDataByShopName("Tesco");

        CommonUtil.killDriversProcessesWindows("geckodriver32");
        CommonUtil.killDriversProcessesWindows("Firefox");

        System.setProperty("webdriver.gecko.driver", geckoDriverPath);

        // Create a new Firefox profile
        FirefoxProfile profile = new FirefoxProfile();

        // Set the custom User-Agent string
        profile.setPreference("general.useragent.override", "--user-agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");

        // Set up FirefoxOptions for headless mode
        FirefoxOptions options = new FirefoxOptions();
        //options.setProfile(profile);
        options.addArguments("-headless");

        // Open the Firefox Driver
//        WebDriver driver = new FirefoxDriver(options);
        Integer count = 0;
        for (ProductMasterDataDAO productMasterData : productMasterDataList) {

            CommonUtil.killDriversProcessesWindows("geckodriver32");
            CommonUtil.killDriversProcessesWindows("Firefox");

            WebDriver driver = new FirefoxDriver(options);
            if (!productMasterData.getUrl().isEmpty()) {
                insertPricingInsights(productMasterData, driver);
                driver.quit();
//                driver.close();
                count++;
                System.out.println("*********************** " + count);
            }
        }
//        CommonUtil.killDriversProcessesWindows("geckodriver32");
//        CommonUtil.killDriversProcessesWindows("Firefox");
//        driver.close();
    }


    public Boolean fetchPricingInsights(String url, WebDriver driver) throws InterruptedException {
        Boolean status = false;
        // Navigate to the product url
        driver.get(url);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Adjust timeout as needed
        try {
//            WebElement priceElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("html/body/div[1]/div/div[2]/main/div/div/section/div[2]/div/section[2]/div[2]/div[2]/div/div/div[1]/div/p[1]")));
//            System.out.println("$$$$$ - 4");
            // Get the price by using the cssSelector
//            String itemPriceString = priceElement.getText().split("£")[1];

            String itemPriceString = driver.findElement(By.cssSelector(".eNIEDh")).getText().split("£")[1];

            // Convert the price string to double
            Double itemPrice = Double.parseDouble(itemPriceString);

            // XPath of Club Card Price
            String xPathForClubCardPrice = "/html/body/div[1]/div/div[2]/main/div/div/section/div[2]/div/section[2]/div[2]/div[1]/div/div/a/div/div[2]/p[1]";
            Double clubCardPrice = 0.0;
            try {
                WebElement clubCardXPathElement = driver.findElement(By.xpath(xPathForClubCardPrice));
                clubCardPrice = Double.valueOf(clubCardXPathElement.getText().split(" ")[0].split("£")[1]);

            } catch (Exception e) {
                logger.error("Log level: ERROR  - TESCO fetchPricingInsights - 1 : " + e);
                System.out.println("$$$$$ - 5");
            }

            String shopName = "Tesco";
            PriceRepo.insertPricingInsights(itemPrice, shopName, url);
            status = true;
            System.out.println(url + "-->>" + itemPrice);
        } catch (Exception e) {
            System.out.println("Error URL :" + url);
            logger.error("Log level: ERROR - TESCO fetchPricingInsights - 2 : " + e);
            System.out.println("$$$$$ - 6");
        }

        return status;
    }

    public void insertPricingInsights(ProductMasterDataDAO productMasterData, WebDriver driver) throws InterruptedException {

        // Navigate to the product url
        driver.get(productMasterData.getUrl());

        //Current Timestamp
        Timestamp now = new Timestamp(System.currentTimeMillis());

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Adjust timeout as needed

        try {

            WebElement priceElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("html/body/div[1]/div/div[2]/main/div/div/section/div[2]/div/section[2]/div[2]/div[2]/div/div/div[1]/div/p[1]")));
            System.out.println("$$$$$ - 4");
//             Get the price by using the cssSelector
            String itemPriceString = priceElement.getText().split("£")[1];


            // Get the price by using the cssSelector
//            String itemPriceString = driver.findElement(By.cssSelector(".eNIEDh")).getText().split("£")[1];

            // Convert the price string to double
            Double itemPrice = Double.parseDouble(itemPriceString);

            // Locate the image element
            WebElement imageElement = driver.findElement(By.tagName("img"));

            // Get the src attribute value
            String imageRef = imageElement.getAttribute("src");

            // XPath of Club Card Price
            String xPathForClubCardPrice = "/html/body/div[1]/div/div[2]/main/div/div/section/div[2]/div/section[2]/div[2]/div[1]/div/div/a/div/div[2]/p[1]";
            Double clubCardPrice = 0.0;
            try {
                WebElement clubCardXPathElement = driver.findElement(By.xpath(xPathForClubCardPrice));
                clubCardPrice = Double.valueOf(clubCardXPathElement.getText().split(" ")[0].split("£")[1]);

            } catch (Exception e) {
                System.out.println("Club Card Error :" + e);
                logger.error("Log level: ERROR - TESCO insertPricingInsights - 1 : " + e);
            }

            //Insert into PricingInsights table
            ProductInsightsRepo.insertPricingInsights(productMasterData.getNo(), productMasterData.getTag(),
                    productMasterData.getShopName(), itemPrice, productMasterData.getUrl(), true, now, imageRef, clubCardPrice);

        } catch (Exception e) {

            //Insert into PricingInsights table and set price as 0.0 and availability as false.
            ProductInsightsRepo.insertPricingInsights(productMasterData.getNo(), productMasterData.getTag(),
                    productMasterData.getShopName(), 0.0, productMasterData.getUrl(), false, now, "", 0.0);

            System.out.println("Error URL :" + productMasterData.getUrl());
            logger.error("Log level: ERROR - TESCO insertPricingInsights - 2 : " + e);

        }
    }
}
