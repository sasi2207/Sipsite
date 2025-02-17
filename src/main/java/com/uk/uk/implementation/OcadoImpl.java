package com.uk.uk.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.uk.uk.entity.ProductMasterDataDAO;
import com.uk.uk.repository.PricingInsightsRepo;
import com.uk.uk.repository.ProductMasterDataRepo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class OcadoImpl {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    ProductMasterDataRepo ProductMasterDataRepo;

    @Autowired
    PricingInsightsRepo ProductInsightsRepo;

    Logger logger
            = LoggerFactory.getLogger(OcadoImpl.class);

    public void getProductDetails() throws JsonProcessingException {
        List<ProductMasterDataDAO> productMasterDataList = new ArrayList<>();

        // Filter the ProductMasterData table for "OCADO"
        productMasterDataList = ProductMasterDataRepo.getProductMasterDataByShopName("Ocado");

        Integer count = 0;
        for (ProductMasterDataDAO productMasterData : productMasterDataList) {
            if (!productMasterData.getUrl().isEmpty())
                insertPricingInsights(productMasterData);
            count++;
            System.out.println("*********************** " + count);
        }
    }

    public Boolean fetchPricingInsights(String url, WebDriver driver) throws InterruptedException {

        Boolean status = false;

        try {
            //Get the response document
            Document document = Jsoup.connect(url).get();


            try {
                // Get the price
                String itemPriceString = document.getElementsByClass("bop-price__current")
                        .get(0).childNode(0).attributes().get("content");

                // Convert the price string to double
                Double itemPrice = Double.parseDouble(itemPriceString);

                // Generate the url for product image
                String imageRef = "https://www.ocado.com/" + document.getElementsByClass("bop-galleryWrapper")
                        .get(0).getElementsByTag("img").attr("src");

                System.out.println(itemPrice + "@@@@" + imageRef);
                status = true;
            } catch (Exception e) {
                System.out.println("ERROR");
                logger.error("Log level: ERROR - OCADO fetchPricingInsights - 1 : " + e);

            }


        } catch (IOException e) {

        }
        ;

        return status;
    }

    void insertPricingInsights(ProductMasterDataDAO productMasterData) {

        //Current Timestamp
        Timestamp now = new Timestamp(System.currentTimeMillis());
        try {

            //Get the response document
            Document document = Jsoup.connect(productMasterData.getUrl()).get();

            try {
                // Get the price
                String itemPriceString = document.getElementsByClass("bop-price__current")
                        .get(0).childNode(0).attributes().get("content");

                // Convert the price string to double
                Double itemPrice = Double.parseDouble(itemPriceString);


//                try {
//
//                }
//                catch (Exception e){
//
//                }

                // Generate the url for product image
                String imageRef = "https://www.ocado.com/" + document.getElementsByClass("bop-galleryWrapper")
                        .get(0).getElementsByTag("img").attr("src");

                //Insert into PricingInsights table
                ProductInsightsRepo.insertPricingInsights(productMasterData.getNo(), productMasterData.getTag(), productMasterData.getShopName(),
                        itemPrice, productMasterData.getUrl(), true, now, imageRef, 0.0);
            } catch (Exception e) {
                System.out.println("ERROR URL : " + productMasterData.getUrl() + "Reason : Price");
                logger.error("Log level: ERROR - OCADO insertPricingInsights - 1 : " + e);

            }


        } catch (IOException e) {

            //Insert into PricingInsights table and set price as 0.0 and availability as false.
            ProductInsightsRepo.insertPricingInsights(productMasterData.getNo(), productMasterData.getTag(), productMasterData.getShopName(),
                    0.0, productMasterData.getUrl(), true, now, "", 0.0);
            logger.error("Log level: ERROR - OCADO insertPricingInsights - 2 : " + e);

            e.printStackTrace();
        }
    }
}