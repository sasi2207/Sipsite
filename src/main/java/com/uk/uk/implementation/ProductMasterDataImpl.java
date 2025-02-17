package com.uk.uk.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.uk.uk.entity.ShopNameWithUrlDAO;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.uk.uk.entity.ProductMasterDataDAO;
import com.uk.uk.repository.ProductMasterDataRepo;
import com.uk.uk.repository.PricingInsightsRepo;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.uk.uk.entity.ShopWiseUrlDAO;

import com.uk.uk.implementation.CommonUtil;

@Service
public class ProductMasterDataImpl {
    @Autowired
    private ProductMasterDataRepo ProductMasterDataRepo;
    @Autowired
    private PricingInsightsRepo PricingInsightsRepo;
    @Autowired
    private AsdaImpl AsdaImpl;
    @Autowired
    private MorrisonsImpl MorrisonsImpl;
    @Autowired
    private SainsburysImpl SainsburysImpl;
    @Autowired
    private TescoImpl TescoImpl;
    @Autowired
    private WaitRoseImpl WaitRoseImpl;
    @Autowired
    private AmazonImpl AmazonImpl;
    @Autowired
    private AmazonTempImpl AmazonTempImpl;
    @Autowired
    private CoOpImpl CoOpImpl;
    @Autowired
    private OcadoImpl OcadoImpl;
    @Autowired
    private AmazonRobotImpl AmazonRobotImpl;

    @Autowired
    private CommonUtil CommonUtil;

    // Inject the value from the application.properties file
//    @Value("${sipsite.driver.geckodriver.path}")
//    private String geckoDriverPath;


    public Boolean insertProductMasterData(List<ProductMasterDataDAO> ProductMasterDataDAOList) throws JsonProcessingException, InterruptedException {

//        System.setProperty("webdriver.gecko.driver", geckoDriverPath);

        Integer tagMaxNo;
        tagMaxNo = ProductMasterDataRepo.getMaxTagNo();

        if (null != tagMaxNo)
            tagMaxNo++;
        else
            tagMaxNo = 1;

        Boolean runScrap = false;

        for (ProductMasterDataDAO productMasterData : ProductMasterDataDAOList) {
            if (productMasterData.getShopName().equalsIgnoreCase("Amazon")) {
                String[] validUrl = productMasterData.getUrl().split("/ref");
                productMasterData.setUrl(validUrl[0]);
            }

            ProductMasterDataDAO productMasterDataDAO = new ProductMasterDataDAO();
            productMasterDataDAO = ProductMasterDataRepo.getActiveProductMasterDataByShopNameAndTag(productMasterData.getShopName(), tagMaxNo);

            if (null == productMasterDataDAO || null == productMasterDataDAO.getNo()) {
                ProductMasterDataRepo.insertProductMasterData(productMasterData.getProductName(), productMasterData.getQuantity(),
                        productMasterData.getMeasurement(), productMasterData.getShopName(), productMasterData.getUrl(),
                        productMasterData.getCategory(), tagMaxNo, true);

                runScrap = true;
            }
        }

        System.out.println("** #1 **");
        Integer finalTagMaxNo = tagMaxNo;
        if (runScrap) {
            CompletableFuture.runAsync(() -> {
                try {
                    additionalProcessing(finalTagMaxNo);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        System.out.println("** #2 **");

        return true;
    }

    public Boolean hideProductByTag(Integer tag) {
        try {
            ProductMasterDataRepo.updateTagStatus(tag);
            return true;
        } catch (Exception e) {
            System.out.println("Hide Product By Tag Error : " + e);
            return false;
        }
    }

    public void additionalProcessing(Integer tagMaxNo) throws InterruptedException, IOException {
        // Long running task
        System.out.println("**  MAIN METHOD 1 **" + tagMaxNo);

        List<ProductMasterDataDAO> productMasterDataDAOListByTagNo = new ArrayList<>();

        productMasterDataDAOListByTagNo = ProductMasterDataRepo.getProductMasterDataByTagNo(tagMaxNo);

        System.out.println("**  MAIN METHOD 2 **" + productMasterDataDAOListByTagNo.size());


        Integer idx = 0;
        for (ProductMasterDataDAO productMasterData : productMasterDataDAOListByTagNo) {
            idx++;

            String shopName = productMasterData.getShopName();

            System.out.println(shopName + "SHOP NAME ## 1");

            switch (shopName) {
//                case "Morrisons":
//                    MorrisonsImpl.insertPricingInsights(productMasterData);
//                    break;
                case "Sainsburys":
                case "Tesco":
                case "WaitRose":
                case "CoOp":
                case "Morrisons":

                    System.out.println(" ## 2");
                    FirefoxOptions options = new FirefoxOptions();
                    System.out.println(" ## 3");

                    options.addArguments("-headless"); // Add headless argument
                    System.out.println(" ## 4");


                    WebDriver driver = new FirefoxDriver(options);
                    System.out.println(" ## 5");

                    if (shopName.equalsIgnoreCase("Sainsburys"))
                        SainsburysImpl.insertPricingInsights(productMasterData, driver, 0);
                    else if (shopName.equalsIgnoreCase("Tesco"))
                        TescoImpl.insertPricingInsights(productMasterData, driver);
                    else if (shopName.equalsIgnoreCase("WaitRose"))
                        WaitRoseImpl.insertPricingInsights(productMasterData, driver, 0);
                    else if (shopName.equalsIgnoreCase("CoOp"))
                        CoOpImpl.insertPricingInsightsSelenium(productMasterData, driver);
                    else if (shopName.equalsIgnoreCase("Morrisons"))
                        MorrisonsImpl.insertPricingInsights(productMasterData, driver);

                    driver.close();

                    if (idx == productMasterDataDAOListByTagNo.size())
                        driver.quit();
                    break;
                case "Amazon":
//                    AmazonTempImpl.insertPricingInsights(driver, 0, productMasterData);
                    AmazonRobotImpl.AmazonRobotInsert(productMasterData);
                    break;
                case "Ocado":
                    OcadoImpl.insertPricingInsights(productMasterData);
                    break;
//                case "CoOp":
//                    CoOpImpl.insertPricingInsights(productMasterData);
//                    break;
                case "ASDA":
                    AsdaImpl.insertPricingInsights(productMasterData);
                    break;
            }
        }

        CommonUtil.killDriversProcessesWindows("geckodriver");
        CommonUtil.killDriversProcessesWindows("Firefox");

    }


    public Boolean updateProductMasterByTag(List<ProductMasterDataDAO> ProductMasterDataDAOList) throws InterruptedException {
        Boolean updateStatus = false;
        Boolean runScrap = false;
//        System.setProperty("webdriver.gecko.driver", geckoDriverPath);
        if (ProductMasterDataDAOList.size() > 0) {
            for (ProductMasterDataDAO productMasterData : ProductMasterDataDAOList) {

//                if (productMasterData.getShopName().isEmpty() && productMasterData.getUrl().isEmpty()) {

                ProductMasterDataRepo.updateProductMasterData(productMasterData.getProductName(), productMasterData.getQuantity(),
                        productMasterData.getMeasurement(), productMasterData.getCategory(),
                        productMasterData.getTag());
//                }

                //Check the shop name is existing already if yes call the update query, else call insert query
                ProductMasterDataDAO productMasterDataByShopNameAndTag = ProductMasterDataRepo.getProductMasterDataByShopNameAndTag(
                        productMasterData.getShopName(), productMasterData.getTag()
                );

                if (!productMasterData.getShopName().isEmpty()) {
                    if (null != productMasterDataByShopNameAndTag) {

                        if (productMasterData.getUrl().isEmpty()) {
                            ProductMasterDataRepo.deleteProductMasterByNo(productMasterData.getShopName(), productMasterData.getTag());
//                        PricingInsightsRepo.deletePricingInsightsByNo(productMasterDataByShopNameAndTag.getNo());
                            PricingInsightsRepo.deletePricingInsightsByTagAndShopName(productMasterDataByShopNameAndTag.getTag(), productMasterDataByShopNameAndTag.getShopName());
//                        runScrap = false;
                        } else {
//                        ProductMasterDataRepo.updateProductMasterData(productMasterData.getTag(), productMasterData.getProductName(),
//                                productMasterData.getQuantity(), productMasterData.getMeasurement(), productMasterData.getUrl(),
//                                productMasterData.getCategory(), productMasterData.getShopName());
                            //updateProductMasterDataUrlByShopName

                            ProductMasterDataDAO productMasterDataDAO = new ProductMasterDataDAO();
                            productMasterDataDAO = ProductMasterDataRepo.getProductMasterDataByShopNameAndTag(productMasterData.getShopName(), productMasterData.getTag());

                            if (null != productMasterDataDAO
                                    && (!productMasterDataDAO.getUrl().equalsIgnoreCase(productMasterData.getUrl()) ||
                                    !productMasterDataDAO.getTagStatus())
                            ) {
//                            ProductMasterDataRepo.deleteProductMasterByNo(productMasterData.getShopName(), productMasterData.getTag());
                                PricingInsightsRepo.deletePricingInsightsByTagAndShopName(productMasterDataByShopNameAndTag.getTag(), productMasterDataByShopNameAndTag.getShopName());
                                ProductMasterDataRepo.updateProductMasterDataUrlByShopName(productMasterData.getUrl(), productMasterData.getShopName(), productMasterData.getTag());
                                runScrap = true;
                            }
                        }

                    } else {
                        ProductMasterDataRepo.insertProductMasterData(productMasterData.getProductName(), productMasterData.getQuantity(),
                                productMasterData.getMeasurement(), productMasterData.getShopName(), productMasterData.getUrl(),
                                productMasterData.getCategory(), productMasterData.getTag(), true);
                        runScrap = true;
                    }
                }

                String shopName = productMasterData.getShopName();


                if (runScrap) {
                    CompletableFuture.runAsync(() -> {
                        try {
                            insertPricingInsightsShopWise(shopName, productMasterData);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }

                System.out.println(shopName + "SHOP NAME");

            }
            updateStatus = true;
        }
        return updateStatus;
    }

    public void insertPricingInsightsShopWise(String shopName, ProductMasterDataDAO productMasterData) throws InterruptedException, IOException {
        switch (shopName) {
//            case "Morrisons":
//                MorrisonsImpl.insertPricingInsights(productMasterData);
//                break;
            case "Sainsburys":
            case "Tesco":
            case "WaitRose":
//            case "Amazon":
            case "CoOp":
            case "Morrisons":

                FirefoxOptions options = new FirefoxOptions();
                options.addArguments("-headless"); // Add headless argument

                WebDriver driver = new FirefoxDriver(options);

                if (shopName.equalsIgnoreCase("Sainsburys"))
                    SainsburysImpl.insertPricingInsights(productMasterData, driver, 0);
                else if (shopName.equalsIgnoreCase("Tesco"))
                    TescoImpl.insertPricingInsights(productMasterData, driver);
                else if (shopName.equalsIgnoreCase("WaitRose"))
                    WaitRoseImpl.insertPricingInsights(productMasterData, driver, 0);
                else if (shopName.equalsIgnoreCase("CoOp"))
                    CoOpImpl.insertPricingInsightsSelenium(productMasterData, driver);
                else if (shopName.equalsIgnoreCase("Morrisons"))
                    MorrisonsImpl.insertPricingInsights(productMasterData, driver);

//                else {
//                    String[] validUrl = productMasterData.getUrl().split("/ref");
//                    productMasterData.setUrl(validUrl[0]);
//                    AmazonTempImpl.insertPricingInsights(driver, 0, productMasterData);
//                }
                driver.close();
                driver.quit();
                break;
            case "Amazon":
//                    AmazonTempImpl.insertPricingInsights(driver, 0, productMasterData);
                AmazonRobotImpl.AmazonRobotInsert(productMasterData);
                break;
            case "Ocado":
                OcadoImpl.insertPricingInsights(productMasterData);
                break;
//            case "CoOp":
//                CoOpImpl.insertPricingInsights(productMasterData);
//                break;
            case "ASDA":
                AsdaImpl.insertPricingInsights(productMasterData);
                break;
        }
    }

    public ResponseEntity<String> getProductUrlImpl(Integer tag, String shopName) {
        String productUrl = "";
        if (null != tag && null != shopName) {

            String shopNameFormatted = "";
            switch (shopName) {
                case "asda":
                    shopNameFormatted = "ASDA";
                    break;
                case "morrisons":
                    shopNameFormatted = "Morrisons";
                    break;
                case "sainsburys":
                    shopNameFormatted = "Sainsburys";
                    break;
                case "tesco":
                    shopNameFormatted = "Tesco";
                    break;
                case "waitrose":
                    shopNameFormatted = "WaitRose";
                    break;
                case "ocado":
                    shopNameFormatted = "Ocado";
                    break;
                case "coop":
                    shopNameFormatted = "CoOp";
                    break;
                case "amazon":
                    shopNameFormatted = "Amazon";
                    break;
            }

            if (!shopNameFormatted.isEmpty()) {

                ProductMasterDataDAO productDetails = ProductMasterDataRepo.getProductMasterDataByShopNameAndTag(shopNameFormatted, tag);
                if (null != productDetails)
                    return ResponseEntity.status(HttpStatus.OK).body(productDetails.getUrl());
                else
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).body("URL is not present for this shop and tag");
            } else
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("shopNameFormatted is empty. " +
                        "Please check the shopName is valid in ProductMasterData table");

        } else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(productUrl);
    }

    public String checkProductUrlIsExistsAlready(List<ShopNameWithUrlDAO> ShopNameWithUrlListReq) {
        String shopNames = "";
        List<String> duplicateShopNames = new ArrayList<>();

        for (ShopNameWithUrlDAO shopNameWithUrl : ShopNameWithUrlListReq) {
            if (!shopNameWithUrl.getUrl().isEmpty()) {

                List<ProductMasterDataDAO> productMasterDataDAOList = new ArrayList<>();
                productMasterDataDAOList = ProductMasterDataRepo.getProductMasterDataByUrl(shopNameWithUrl.getUrl());

                if (!productMasterDataDAOList.isEmpty()) {
                    if (shopNames.isEmpty()) {
                        shopNames = shopNameWithUrl.getShopName();
                        duplicateShopNames.add(shopNameWithUrl.getShopName());
//                    } else if (!shopNames.contains(shopNameWithUrl.getShopName())) {
//                        shopNames = shopNames + ", " + shopNameWithUrl.getShopName();
//                    }
                    } else if (!duplicateShopNames.contains(shopNameWithUrl.getShopName())) {
                        shopNames = shopNames + ", " + shopNameWithUrl.getShopName();
                    }
                }
            }
        }
        return shopNames;
    }
}
