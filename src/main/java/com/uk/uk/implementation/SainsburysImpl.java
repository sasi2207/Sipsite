package com.uk.uk.implementation;

import com.uk.uk.entity.ProductMasterDataDAO;
import com.uk.uk.repository.PricingInsightsRepo;
import com.uk.uk.repository.ProductMasterDataRepo;
import lombok.AllArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
//import org.openqa.selenium.devtools.v96.browser.Browser;
//import org.openqa.selenium.devtools.v96.browser.model.WindowID;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.Dimension;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.*;
import java.util.List;

import com.uk.uk.implementation.CommonUtil;

@Service
//@AllArgsConstructor
public class SainsburysImpl {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    ProductMasterDataRepo ProductMasterDataRepo;
    @Autowired
    PricingInsightsRepo ProductInsightsRepo;
    @Autowired
    private CommonUtil CommonUtil;

    @Value("${sipsite.driver.geckodriver.path}")
    private String geckoDriverPath;

    Logger logger
            = LoggerFactory.getLogger(SainsburysImpl.class);

    public void getProductDetails() throws InterruptedException {
        List<ProductMasterDataDAO> productMasterDataList = new ArrayList<>();

        // Filter the ProductMasterData table for "Sainsburys"
        productMasterDataList = ProductMasterDataRepo.getProductMasterDataByShopName("Sainsburys");

        CommonUtil.killDriversProcessesWindows("geckodriver32");
        CommonUtil.killDriversProcessesWindows("Firefox");

        System.setProperty("webdriver.gecko.driver", geckoDriverPath);

        // Create a new Firefox profile
        FirefoxProfile profile = new FirefoxProfile();

        // Set the custom User-Agent string
        profile.setPreference("general.useragent.override", "--user-agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");


        // Set up FirefoxOptions for headless mode
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("-headless");// Hide Firefox GUI


        Integer idx = 0;
        Integer count = 0;
        for (ProductMasterDataDAO productMasterData : productMasterDataList) {
            CommonUtil.killDriversProcessesWindows("geckodriver32");
            CommonUtil.killDriversProcessesWindows("Firefox");

            // Open the Firefox Driver
            WebDriver driver = new FirefoxDriver(options);

            if (!productMasterData.getUrl().isEmpty()) {
                insertPricingInsights(productMasterData, driver, idx);
                driver.quit();
//                driver.close();
                count++;
                System.out.println("*********************** " + count);
            }
        }
//        CommonUtil.killDriversProcessesWindows("geckodriver32");
//        CommonUtil.killDriversProcessesWindows("Firefox");
        // driver.close();
    }


    public Boolean fetchPricingInsights(String url, WebDriver driver) throws InterruptedException {
        Boolean status = false;
        // Navigate to the product url
        driver.get(url);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2)); // Adjust timeout as needed


        try {

            Thread.sleep(5000);
            // Get the price by using the cssSelector
            String itemPriceString = driver.findElement(By.className("pd__cost__retail-price")).getText().split("£")[1];

            // Wait for the price element to be visible
//            WebElement priceElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("pd__cost__retail-price")));
//            String itemPriceString = priceElement.getText().split("£")[1];

            // Convert the price string to double
            Double itemPrice = Double.parseDouble(itemPriceString);
            String xPathForNectarPrice = "/html/body/div[1]/div[2]/div[2]/div[2]/div/div/div/div/section[1]/div/div/div[2]/div[5]/div[1]/div/div[1]/span[1]";
            Double NectarPrice = 0.0;
            try {
                WebElement clubCardXPathElement = driver.findElement(By.xpath(xPathForNectarPrice));
                NectarPrice = Double.valueOf(clubCardXPathElement.getText().split("£")[1]);
            } catch (Exception e) {
                System.out.println("Error Nectar Price " + e);
                logger.error("Log level: ERROR - SAINSBURYS fetchPricingInsights - 1 : " + e);
            }

            System.out.println(url + ">>" + itemPriceString + ">>" + NectarPrice);

            String shopName = "Tesco";
//            PriceRepo.insertPricingInsights(itemPrice, clubCardPrice, shopName, url);
            status = true;
        } catch (Exception e) {
            System.out.println("Error URL :" + url);
            logger.error("Log level: ERROR - SAINSBURYS fetchPricingInsights - 2 : " + e);
        }
        return status;
    }


    public void insertPricingInsights(ProductMasterDataDAO productMasterData, WebDriver driver, Integer idx) throws InterruptedException {

        // Navigate to the product url
        driver.get(productMasterData.getUrl());

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2)); // Adjust timeout as needed

        //Current Timestamp
        Timestamp now = new Timestamp(System.currentTimeMillis());

        // If the idx value is 0, then we need to click the "I Accept" cookie button
        if (idx == 0) {
            // Wait for 3 sec to load the Cookie tag in browser
            Thread.sleep(3000);

            try {
                // Locate the cookie button by Id
                WebElement cookieButton = driver.findElement(By.id("onetrust-accept-btn-handler"));

                // Click on the cookie button
                cookieButton.click();
                Thread.sleep(3000);

            } catch (Exception e) {
                System.out.println("Error in Cookie Button");
                logger.error("Log level: ERROR - SAINSBURYS insertPricingInsights - 1 : " + e);

            }
        }

        // Wait for 3 sec for closing the Cookie tag
        Thread.sleep(3000);

        try {

            // Get the price by using the className
//            WebElement priceElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("pd__cost__retail-price")));
//            String itemPriceString = priceElement.getText().split("£")[1];

            Thread.sleep(5000);
            // Get the price by using the cssSelector
            String itemPriceString = driver.findElement(By.className("pd__cost__retail-price")).getText().split("£")[1];


            // Convert the price string to double
            Double itemPrice = Double.parseDouble(itemPriceString);

            String xPathForNectarPrice = "/html/body/div[1]/div[2]/div[2]/div[2]/div/div/div/div/section[1]/div/div/div[2]/div[5]/div[1]/div/div[1]/span[1]";
            Double NectarPrice = 0.0;
            try {
                WebElement clubCardXPathElement = driver.findElement(By.xpath(xPathForNectarPrice));
                NectarPrice = Double.valueOf(clubCardXPathElement.getText().split("£")[1]);
                if (NectarPrice.equals(itemPrice)) {
                    NectarPrice = 0.0;
                }
            } catch (Exception e) {
                System.out.println("Error Nectar Price " + e);
                logger.error("Log level: ERROR - SAINSBURYS insertPricingInsights - 2 : " + e);

            }

            // Locate the image element
            WebElement imageElement = driver.findElement(By.cssSelector(".pd__image"));

            // Get the src attribute value
            String imageRef = imageElement.getAttribute("src");

            //Insert into PricingInsights table
            ProductInsightsRepo.insertPricingInsights(productMasterData.getNo(), productMasterData.getTag(),
                    productMasterData.getShopName(), itemPrice, productMasterData.getUrl(), true, now, imageRef, NectarPrice);

        } catch (Exception e) {
            //Insert into PricingInsights table
            ProductInsightsRepo.insertPricingInsights(productMasterData.getNo(), productMasterData.getTag(),
                    productMasterData.getShopName(), 0.0, productMasterData.getUrl(), false, now, "", 0.0);

            System.out.println("Error URL :" + productMasterData.getUrl());
            logger.error("Log level: ERROR - SAINSBURYS insertPricingInsights - 3 : " + e);

        }
        idx++;
    }

    public void test() throws InterruptedException {

//        System.setProperty("webdriver.gecko.driver", geckoDriverPath);

//        FirefoxOptions options = new FirefoxOptions();
//        options.setHeadless(true); // Enable headless mode

        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("-headless"); // Add headless argumen

        WebDriver driver = new FirefoxDriver(options);

        // Navigate to a URL
        driver.get("https://www.tesco.com/groceries/en-GB/products/257546202");

        Thread.sleep(3000);

        // Use WebDriverWait to wait for the element to be present
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".eNIEDh")));
//        System.out.println("Element Text: " + element.getText());

        String itemPriceString = driver.findElement(By.cssSelector(".eNIEDh")).getText().split("£")[1];

//        WebElement textBox = driver.findElement(By.id("SIvCob"));
        System.out.println("#### >> " + itemPriceString);
//
//        // Fetch page title or other data
//        System.out.println("##### >>Page Title: " + driver.getTitle());

        // Quit the driver
    }

}
