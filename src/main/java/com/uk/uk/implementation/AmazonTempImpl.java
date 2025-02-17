package com.uk.uk.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.uk.uk.entity.ProductMasterDataDAO;
import com.uk.uk.repository.PricingInsightsRepo;
import com.uk.uk.repository.ProductMasterDataRepo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

@Service
public class AmazonTempImpl {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ProductMasterDataRepo productMasterDataRepo;

    @Autowired
    private PricingInsightsRepo productInsightsRepo;

//    @Value("${sipsite.driver.geckodriver.path}")
//    private String geckoDriverPath;

    public void getProductDetails() throws IOException, InterruptedException {

        List<ProductMasterDataDAO> productMasterDataList = productMasterDataRepo.getProductMasterDataByShopName("Amazon");

        Integer idx = 0;

        //LOCAL
//        System.setProperty("webdriver.gecko.driver", "D:/Pixmonks Backup/Uk/Git/Uk-Backend/src/main/resources/drivers/geckodriver.exe");
//        System.setProperty("webdriver.gecko.driver", geckoDriverPath);
        //PROD
//        System.setProperty("webdriver.gecko.driver", "C:/Program Files/Apache Software Foundation/Tomcat 10.1/api.sipsite.co.uk/ROOT/WEB-INF/classes/drivers/geckodriver.exe");

        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("-headless");

        // Open the Chrome Driver
        WebDriver driver = new FirefoxDriver(options);

        for (ProductMasterDataDAO productMasterData : productMasterDataList) {
            insertPricingInsights(driver, idx, productMasterData);
            idx++;
        }

        driver.close();
    }

    public void insertPricingInsights(WebDriver driver, Integer idx, ProductMasterDataDAO productMasterData) throws InterruptedException, IOException {

        if (idx == 0) {

            //Get the response document
           // Document document = Jsoup.connect(productMasterData.getUrl()).get();

//            driver.get("https://www.amazon.co.uk/");
            System.out.println(productMasterData.getUrl() + " ## 6.1");
            driver.get(productMasterData.getUrl());
            System.out.println(productMasterData.getUrl() + " ## 6.2");
            Thread.sleep(2000);
            System.out.println(productMasterData.getUrl() + " ## 6.03");

            driver.navigate().refresh();

            Thread.sleep(2000);


            boolean elementFound = false;

            while (!elementFound) {
                try {
                    WebElement cookieButton = driver.findElement(By.id("sp-cc-accept"));
                    if (cookieButton != null) {
                        elementFound = true;
                        // Perform actions on the element if needed
                        System.out.println("Element found!");


//                        WebElement cookieButton = driver.findElement(By.id("sp-cc-accept"));
//            // Click on the cookie button
                        cookieButton.click();
                        System.out.println(driver.findElement(By.id("nav-global-location-popover-link")) + " ## 6.4");

                        WebElement locationButton = driver.findElement(By.id("nav-global-location-popover-link"));
                        System.out.println(productMasterData.getUrl() + " ## 6.5");
                        // Click on the cookie button
                        locationButton.click();

                        System.out.println(" ## 7");

                        Thread.sleep(1000);

                        WebElement addressBox = driver.findElement(By.id("Condo"));
                        // Click on the cookie button
                        WebElement textBox = addressBox.findElement(By.id("GLUXZipUpdateInput"));
                        textBox.clear();
                        textBox.sendKeys("W1A 1AA");

                        System.out.println(" ## 8");
                        Thread.sleep(2000);

                        WebElement applyButton = driver.findElement(By.id("GLUXZipUpdate"));

                        // Click on the cookie button
                        applyButton.click();

                        System.out.println(" ## 9");

                        Thread.sleep(1000);

                        driver.navigate().refresh();

                        Thread.sleep(1500);

                        System.out.println(" ## 10");

                        try {
                            // Locate the cookie button by XPath
                            WebElement cookieButtonXPath = driver.findElement(By.xpath("//*[@data-testid=\"reject-all\"]"));
                            // Click on the cookie button
                            System.out.println(" ## 11");
                            cookieButtonXPath.click();
                            System.out.println(" ## 12");
                        } catch (Exception e) {
                            System.out.println("Error in Cookie Button");
                        }

                        System.out.println(" ## 13");

                    }


                    // Navigate to the product URL
//        driver.get(productMasterData.getUrl());

                    // Current Timestamp
                    Timestamp now = new Timestamp(System.currentTimeMillis());

                    // If the idx value is 0, then we need to click the "I Accept" cookie button
//        if (idx == 0) {
//            // Wait for 3 sec to load the Cookie tag in browser
//            Thread.sleep(3000);
//
//            try {
//                // Locate the cookie button by XPath
//                WebElement cookieButton = driver.findElement(By.xpath("//*[@data-testid=\"reject-all\"]"));
//                // Click on the cookie button
//                cookieButton.click();
//            } catch (Exception e) {
//                System.out.println("Error in Cookie Button");
//            }
//        }

                    // Wait for 1 sec for closing the Cookie tag
                    Thread.sleep(1000);
//                    ((JavascriptExecutor) driver).executeScript("location.reload();");
                    System.out.println(" ## 14");

                    try {
                        // Get the price by using the XPath
                        String itemPriceString = null != driver.findElements(By.className("a-price-whole")) && null != driver.findElements(By.className("a-price-whole")).get(0) ?
                                driver.findElements(By.className("a-price-whole")).get(0).getText() : "0";

                        System.out.println(itemPriceString + " ## 15");
                        String fractionalValue = null != driver.findElements(By.className("a-price-fraction")) && null != driver.findElements(By.className("a-price-fraction")).get(0) ?
                                driver.findElements(By.className("a-price-fraction")).get(0).getText() : String.valueOf('0');
                        itemPriceString = itemPriceString + '.' + fractionalValue;

                        System.out.println(itemPriceString + " ## 16");

                        // Convert the price string to double
                        Double itemPrice = Double.parseDouble(itemPriceString);

                        System.out.println(itemPrice + " ## 17");


                        // Locate the image element
                        WebElement imageElement = driver.findElement(By.id("landingImage"));

                        System.out.println(imageElement + " ## 18");


                        // Get the src attribute value
                        String imageRef = imageElement.getAttribute("src");

                        System.out.println(imageRef + " ## 19");


                        // Insert into PricingInsights table
//                        productInsightsRepo.insertPricingInsights(productMasterData.getNo(), productMasterData.getTag(), productMasterData.getShopName(),
//                                itemPrice, productMasterData.getUrl(), true, now, imageRef);

                    } catch (Exception e) {
                        // Insert into PricingInsights table
//                        productInsightsRepo.insertPricingInsights(productMasterData.getNo(), productMasterData.getTag(), productMasterData.getShopName(),
//                                0.0, productMasterData.getUrl(), true, now, "");
                        System.out.println("Error URL :" + productMasterData.getUrl());
                    }
                    idx++;


                } catch (Exception e) {
                    // Element not found, refresh the page and try again
                    System.out.println("Element not found, refreshing the page...");
//                    driver.navigate().refresh();
                    ((JavascriptExecutor) driver).executeScript("location.reload();");

                    // Optional: Add a sleep interval to avoid rapid refreshing
                    try {
                        Thread.sleep(2000); // 2 seconds pause

                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }
}