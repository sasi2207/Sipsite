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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import com.uk.uk.implementation.CommonUtil;
import com.uk.uk.repository.PriceRepo;

@Service
public class MorrisonsImpl {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    ProductMasterDataRepo ProductMasterDataRepo;
    @Autowired
    PricingInsightsRepo ProductInsightsRepo;
    @Autowired
    private CommonUtil CommonUtil;
    @Autowired
    private PriceRepo PriceRepo;

    @Value("${sipsite.driver.geckodriver.path}")
    private String geckoDriverPath;
    Logger logger
            = LoggerFactory.getLogger(TescoImpl.class);

    public void getProductDetails() throws JsonProcessingException {
        List<ProductMasterDataDAO> productMasterDataList = new ArrayList<>();

        // Filter the ProductMasterData table for "Morrisons"
        productMasterDataList = ProductMasterDataRepo.getProductMasterDataByShopName("Morrisons");

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
            CommonUtil.killDriversProcessesWindows("Firefox");


            WebDriver driver = new FirefoxDriver(options);

            if (!productMasterData.getUrl().isEmpty()) {
                insertPricingInsights(productMasterData, driver);
                driver.quit();
//                driver.close();
                // driver.close();
                count++;
                System.out.println("*********************** " + count);
            }

        }
//        CommonUtil.killDriversProcessesWindows("geckodriver32");
//        CommonUtil.killDriversProcessesWindows("Firefox");
    }

    public void insertPricingInsights(ProductMasterDataDAO productMasterData, WebDriver driver) {

        //Current Timestamp
        Timestamp now = new Timestamp(System.currentTimeMillis());

        // Split the url with urlSplitByHyphen (-)
        String[] urlSplitByHyphen = productMasterData.getUrl().split("/");

        // Get the pid value from splitted array
        String pid = urlSplitByHyphen[urlSplitByHyphen.length - 1];

        String updatedPid = pid.replaceFirst("-(?=[^-]*$)", "/");

        // Generate the url with the dynamic pid value
        String url = "https://groceries.morrisons.com/products/" + updatedPid;

        driver.get(productMasterData.getUrl());
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5)); // Adjust timeout as needed

//        try {

        //Get the response document
//            Document document = Jsoup.connect(url).get();

        // Get the price
//            String itemPriceString = document.getElementsByClass("bop-price__current").get(0).childNode(2)
//                    .toString().split("£")[1];

//            String itemPriceString = document.getElementsByClass("_text_f6lbl_1 _text--bold_f6lbl_7 _text--xl_f6lbl_31 _text--promotion_f6lbl_35 sc-1ft5imw-0 fJrHje").
//                    first().toString().split("£")[1].replace("</span>", "");

//document.selectFirst("div[data-test=price-container]").text().;

        try {
            WebElement priceElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div/div[1]/main/div/div[3]/div/div/div[2]")));
            String itemPriceString = priceElement.getText().split("£")[1];

            // Convert the price string to double
            Double itemPrice = Double.parseDouble(itemPriceString);
            String imageRef = driver.findElement(By.xpath("/html/body/div[1]/div/div[1]/main/div/div[2]/div[1]/ul/li[1]/button/img")).getAttribute("src");


            //Insert into PricingInsights table
            ProductInsightsRepo.insertPricingInsights(productMasterData.getNo(), productMasterData.getTag(), productMasterData.getShopName(),
                    itemPrice, productMasterData.getUrl(), true, now, imageRef, 0.0);

            System.out.println("MORRISONS INSERTED " + url);
        } catch (Exception e) {
            System.out.println("Page Not Found URL : " + url);
            logger.error("Log level: ERROR  - MORRISONS insertPricingInsights - 1 : " + e);

        }

//        } catch (IOException e) {
//
//            //Insert into PricingInsights table and set price as 0.0 and availability as false.
//            ProductInsightsRepo.insertPricingInsights(productMasterData.getNo(), productMasterData.getTag(), productMasterData.getShopName(),
//                    0.0, productMasterData.getUrl(), false, now, "", 0.0);
//
//            e.printStackTrace();
//        }
    }

    public Boolean fetchPricingInsights(String productUrl, WebDriver driver) throws InterruptedException {
        Boolean status = false;

        // Split the url with urlSplitByHyphen (-)
        String[] urlSplitByHyphen = productUrl.split("/");

        // Get the pid value from splitted array
        String pid = urlSplitByHyphen[urlSplitByHyphen.length - 1];

        String updatedPid = pid.replaceFirst("-(?=[^-]*$)", "/");

        // Generate the url with the dynamic pid value
        String url = "https://groceries.morrisons.com/products/" + updatedPid;

        driver.get(productUrl);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5)); // Adjust timeout as needed


        try {

            WebElement priceElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div/div[1]/main/div/div[3]/div/div/div[2]")));
            String itemPriceString = priceElement.getText().split("£")[1];

            // Convert the price string to double
            Double itemPrice = Double.parseDouble(itemPriceString);

            String shopName = "Morrisons";
            PriceRepo.insertPricingInsights(itemPrice, shopName, url);
            status = true;
            System.out.println(url + "-->>" + itemPrice);

        } catch (Exception e) {
            System.out.println("Error URL :" + url);
            logger.error("Log level: ERROR - Morrisons fetchPricingInsights - 2 : " + e);
        }
        return status;
    }

}
