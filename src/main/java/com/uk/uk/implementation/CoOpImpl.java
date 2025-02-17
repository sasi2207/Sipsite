package com.uk.uk.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.uk.uk.entity.ProductMasterDataDAO;
import com.uk.uk.repository.PricingInsightsRepo;
import com.uk.uk.repository.ProductMasterDataRepo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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
public class CoOpImpl {
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
            = LoggerFactory.getLogger(CoOpImpl.class);

    public void getProductDetails() throws JsonProcessingException {
        List<ProductMasterDataDAO> productMasterDataList = new ArrayList<>();

        // Filter the ProductMasterData table for "COOP"
        productMasterDataList = ProductMasterDataRepo.getProductMasterDataByShopName("CoOp");

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


        Integer count = 0;
        for (ProductMasterDataDAO productMasterData : productMasterDataList) {
            CommonUtil.killDriversProcessesWindows("geckodriver32");
            CommonUtil.killDriversProcessesWindows("Firefox");

            // Open the Chrome Driver
            WebDriver driver = new FirefoxDriver(options);
            if (!productMasterData.getUrl().isEmpty()) {
                insertPricingInsightsSelenium(productMasterData, driver);
//                driver.close();
                driver.quit();
                count++;
                System.out.println("*********************** " + count);
            }
        }
//        CommonUtil.killDriversProcessesWindows("geckodriver32");
//        CommonUtil.killDriversProcessesWindows("Firefox");
    }

    void insertPricingInsightsSelenium(ProductMasterDataDAO productMasterData, WebDriver driver) {

        System.out.println("** COOP Selenium #1");
        // Navigate to the product url
        driver.get(productMasterData.getUrl());

//        try {

        String itemPriceString = null;

        // Find the element with the class "coop-c-gallery__image"

        String imageRef = "";
        try {
            WebElement galleryImage = driver.findElement(By.className("coop-c-gallery__image"));

            // Within that element, find the picture tag and then the image tag
            WebElement pictureTag = galleryImage.findElement(By.tagName("picture"));
            WebElement imageTag = pictureTag.findElement(By.tagName("img"));

            // Get the src attribute from the image tag
            imageRef = imageTag.getAttribute("src");
        } catch (Exception e) {
            System.out.println("Image className Not in CoOp (ClassName:coop-c-gallery__image");
            logger.error("Log level: ERROR  - COOP fetchPricingInsights - 1 : " + e);

        }
        try {
            itemPriceString = driver.findElement(By.className("coop-u-member-deal-blue")).getText().split("£")[1];
            insertPricingInsights(productMasterData, itemPriceString, imageRef);
        } catch (Exception e) {
            logger.error("Log level: ERROR  - COOP fetchPricingInsights - 2 : " + e);

            try {
                itemPriceString = driver.findElement(By.className("coop-c-card__price")).getText().split("£")[1];
                insertPricingInsights(productMasterData, itemPriceString, imageRef);
            } catch (Exception e2) {
                System.out.println("Image className Not in CoOp (ClassName:coop-c-card__price");
                logger.error("Log level: ERROR  - COOP fetchPricingInsights - 3 : " + e);

            }
        }
    }

    private void insertPricingInsights(ProductMasterDataDAO productMasterData, String itemPriceString, String imageRef) {
        Double itemPrice = Double.parseDouble(itemPriceString);
        //Current Timestamp
        Timestamp now = new Timestamp(System.currentTimeMillis());
        System.out.println(itemPrice + "**" + imageRef);

        ProductInsightsRepo.insertPricingInsights(productMasterData.getNo(), productMasterData.getTag(), productMasterData.getShopName(),
                itemPrice, productMasterData.getUrl(), true, now, imageRef, 0.0);
        System.out.println("** COOP Selenium #2");
    }


//    public void insertPricingInsights(ProductMasterDataDAO productMasterData, Double itemPrice,String imageRef,
//                                      ){
//        
//    }
//    void insertPricingInsights(ProductMasterDataDAO productMasterData) {
//
//        //Current Timestamp
//        Timestamp now = new Timestamp(System.currentTimeMillis());
//
//        try {
//
//
//            //Get the response document
//            Document document = Jsoup.connect(productMasterData.getUrl()).get();
//
//            // Get the price
//            String itemPriceString = document.getElementsByClass("coop-c-card__price")
//                    .get(0).childNode(1).childNode(0)
//                    .toString().split("£")[1];
//
////            String itemPriceString = document.getElementsByClass("bop-price__current ").text().toString().split("£")[1];
//
//            // Convert the price string to double
//            Double itemPrice = Double.parseDouble(itemPriceString);
//
//            // Generate the url for product image
//            String imageRef = document.getElementsByClass("coop-c-gallery__image")
//                    .get(0).getElementsByTag("img").attr("src");
//
////                String imageRef = "https://www.ocado.com/";
////            imageRef = imageRef + document.getElementsByClass("bop-gallery__image").get(0).getElementsByTag("img").attr("src");
//
//
//            //Insert into PricingInsights table
//            ProductInsightsRepo.insertPricingInsights(productMasterData.getNo(), productMasterData.getTag(), productMasterData.getShopName(),
//                    itemPrice, productMasterData.getUrl(), true, now, imageRef);
//        } catch (IOException e) {
//
//            //Insert into PricingInsights table and set price as 0.0 and availability as false.
//            ProductInsightsRepo.insertPricingInsights(productMasterData.getNo(), productMasterData.getTag(), productMasterData.getShopName(),
//                    0.0, productMasterData.getUrl(), false, now, "");
//            e.printStackTrace();
//        }
//    }
}