package com.uk.uk.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.uk.uk.entity.ProductMasterDataDAO;
import com.uk.uk.repository.PricingInsightsRepo;
import com.uk.uk.repository.PriceRepo;
import com.uk.uk.repository.ProductMasterDataRepo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.uk.uk.implementation.CommonUtil;

@Service
public class WaitRoseImpl {
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

    Logger logger
            = LoggerFactory.getLogger(WaitRoseImpl.class);

    public void getProductDetails() throws JsonProcessingException, InterruptedException {
        List<ProductMasterDataDAO> productMasterDataList = new ArrayList<>();

        // Filter the ProductMasterData table for "WaitRose"
        productMasterDataList = ProductMasterDataRepo.getProductMasterDataByShopName("WaitRose");

        CommonUtil.killDriversProcessesWindows("geckodriver32");
        CommonUtil.killDriversProcessesWindows("Firefox");

        System.setProperty("webdriver.gecko.driver", geckoDriverPath);

        // Create a new Firefox profile
        FirefoxProfile profile = new FirefoxProfile();

        // Set the custom User-Agent string
        profile.setPreference("general.useragent.override", "--user-agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");


        // Set up FirefoxOptions for headless mode
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("-headless");


        Integer idx = 0;
        Integer count = 0;
        for (ProductMasterDataDAO productMasterData : productMasterDataList) {
            CommonUtil.killDriversProcessesWindows("geckodriver32");
            CommonUtil.killDriversProcessesWindows("Firefox");

            // Open the Chrome Driver
            WebDriver driver = new FirefoxDriver(options);

            if (!productMasterData.getUrl().isEmpty()) {
                insertPricingInsights(productMasterData, driver, idx);
                count++;
                driver.quit();
                System.out.println("*********************** " + count);
            }
        }
//        driver.close();
//        CommonUtil.killDriversProcessesWindows("geckodriver32");
//        CommonUtil.killDriversProcessesWindows("Firefox");
    }

    public Boolean fetchPricingInsights(String url, WebDriver driver) throws InterruptedException {

        Boolean status = false;

        // Navigate to the product url
        driver.get(url);

        try {

            // Get the price by using the className
            String itemPriceString = driver.findElement(By.xpath("//*[@data-test='product-pod-price']")).getText().split("£")[1];
            // Convert the price string to double
            Double itemPrice = Double.parseDouble(itemPriceString);

            String shopName = "WaitRose";
            PriceRepo.insertPricingInsights(itemPrice, shopName, url);
            status = true;
            System.out.println(url + "-->>" + itemPrice);
        } catch (Exception e) {
            System.out.println("Error URL :" + url);
            logger.error("Log level: ERROR - WAITROSE fetchPricingInsights - 1 : " + e);

        }

        return status;
    }

    void insertPricingInsights(ProductMasterDataDAO productMasterData, WebDriver driver, Integer idx) throws InterruptedException {
        // Navigate to the product url
        driver.get(productMasterData.getUrl());

        //Current Timestamp
        Timestamp now = new Timestamp(System.currentTimeMillis());

        // If the idx value is 0, then we need to click the "I Accept" cookie button
        if (idx == 0) {
            // Wait for 3 sec to load the Cookie tag in browser
            Thread.sleep(3000);

            try {
                // Locate the cookie button by Id
                WebElement cookieButton = driver.findElement(By.xpath("//*[@data-testid=\"reject-all\"]"));
                // Click on the cookie button
                cookieButton.click();
            } catch (Exception e) {
                System.out.println("Error in Cookie Button");
                logger.error("Log level: ERROR - WAITROSE insertPricingInsights - 1 : " + e);

            }
        }

        // Wait for 1 sec for closing the Cookie tag
        Thread.sleep(1000);

        try {

            // Get the price by using the className
            String itemPriceString = driver.findElement(By.xpath("//*[@data-test='product-pod-price']")).getText().split("£")[1];
            // Convert the price string to double
            Double itemPrice = Double.parseDouble(itemPriceString);

            // Locate the image element
            WebElement imageElement = driver.findElement(By.id("productImage")).findElement(By.tagName("img"));

            // Get the src attribute value
            String imageRef = imageElement.getAttribute("src");

            //Insert into PricingInsights table
            ProductInsightsRepo.insertPricingInsights(productMasterData.getNo(), productMasterData.getTag(), productMasterData.getShopName(),
                    itemPrice, productMasterData.getUrl(), true, now, imageRef, 0.0);

        } catch (Exception e) {

            //Insert into PricingInsights table and set price as 0.0 and availability as false.
            ProductInsightsRepo.insertPricingInsights(productMasterData.getNo(), productMasterData.getTag(), productMasterData.getShopName(),
                    0.0, productMasterData.getUrl(), true, now, "", 0.0);

            System.out.println("Error URL :" + productMasterData.getUrl());
            logger.error("Log level: ERROR - WAITROSE insertPricingInsights - 2 : " + e);

        }
        idx++;
    }
}