package com.uk.uk.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.uk.uk.repository.ProductMasterDataRepo;
import com.uk.uk.repository.PricingInsightsRepo;
import com.uk.uk.entity.ProductMasterDataDAO;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class AsdaImpl {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    ProductMasterDataRepo ProductMasterDataRepo;
    @Autowired
    PricingInsightsRepo ProductInsightsRepo;

    // Url for the POST Api
    String url = "https://groceries.asda.com/api/bff/graphql";

    Logger logger
            = LoggerFactory.getLogger(AsdaImpl.class);

    public void getProductDetails() throws JsonProcessingException {
        List<ProductMasterDataDAO> productMasterDataList = new ArrayList<>();

        // Filter the ProductMasterData table for "ASDA"
        productMasterDataList = ProductMasterDataRepo.getProductMasterDataByShopName("ASDA");

        Integer count = 0;
        for (ProductMasterDataDAO productMasterData : productMasterDataList) {
            if (!productMasterData.getUrl().isEmpty())
                insertPricingInsights(productMasterData);
            count++;
            System.out.println("*********************** " + count);
        }
    }

    private HttpHeaders asdaHeader() {
        // Declaring and set Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Request-Origin", "gi");
        return headers;
    }


    public void fetchPricingInsights(String url) {

        //Current Timestamp
        Timestamp now = new Timestamp(System.currentTimeMillis());

        // Split the url with slash
        String[] urlSplitBySlash = url.split("/");
//        String productId = urlSplitBySlash[6].contains("?") ? urlSplitBySlash[6].split("\\?")[0] : urlSplitBySlash[6];
        String productId = null;
        if (urlSplitBySlash.length > 6) {
            if (urlSplitBySlash[6].contains("?")) {
                if (urlSplitBySlash[6].split("\\?").length > 0)
                    productId = urlSplitBySlash[6].split("\\?")[0];
            } else {
                productId = urlSplitBySlash[6];
            }
        }


        if (null != productId) {
            // Get the pid value from splitted array
//        String pid = urlSplitBySlash[urlSplitBySlash.length - 1];
            String pid = productId;

            String requestBody = "{\n" +
                    "    \"requestorigin\": \"gi\",\n" +
                    "    \"contract\": \"web/cms/product-details-page\",\n" +
                    "    \"variables\": {\n" +
                    "        \"is_eat_and_collect\": false,\n" +
                    "        \"store_id\": \"4565\",\n" +
                    "        \"type\": \"content\",\n" +
                    "        \"request_origin\": \"gi\",\n" +
                    "        \"payload\": {\n" +
                    "            \"page_id\": \"" + pid + "\",\n" +
                    "            \"page_type\": \"productDetailsPage\",\n" +
                    "            \"page_meta_info\": true\n" +
                    "        },\n" +
                    "        \"user_segments\": [\n" +
                    "            \"1007\",\n" +
                    "            \"1019\",\n" +
                    "            \"1020\",\n" +
                    "            \"1023\",\n" +
                    "            \"1024\",\n" +
                    "            \"1027\",\n" +
                    "            \"1038\",\n" +
                    "            \"1041\",\n" +
                    "            \"1042\",\n" +
                    "            \"1043\",\n" +
                    "            \"1047\",\n" +
                    "            \"1053\",\n" +
                    "            \"1055\",\n" +
                    "            \"1057\",\n" +
                    "            \"1059\",\n" +
                    "            \"1067\",\n" +
                    "            \"1070\",\n" +
                    "            \"1082\",\n" +
                    "            \"1087\",\n" +
                    "            \"1097\",\n" +
                    "            \"1098\",\n" +
                    "            \"1099\",\n" +
                    "            \"1100\",\n" +
                    "            \"1102\",\n" +
                    "            \"1105\",\n" +
                    "            \"1107\",\n" +
                    "            \"1109\",\n" +
                    "            \"1110\",\n" +
                    "            \"1111\",\n" +
                    "            \"1112\",\n" +
                    "            \"1116\",\n" +
                    "            \"1117\",\n" +
                    "            \"1119\",\n" +
                    "            \"1123\",\n" +
                    "            \"1124\",\n" +
                    "            \"1126\",\n" +
                    "            \"1128\",\n" +
                    "            \"1130\",\n" +
                    "            \"1140\",\n" +
                    "            \"1141\",\n" +
                    "            \"1144\",\n" +
                    "            \"1147\",\n" +
                    "            \"1150\",\n" +
                    "            \"1152\",\n" +
                    "            \"1157\",\n" +
                    "            \"1159\",\n" +
                    "            \"1160\",\n" +
                    "            \"1165\",\n" +
                    "            \"1166\",\n" +
                    "            \"1167\",\n" +
                    "            \"1169\",\n" +
                    "            \"1170\",\n" +
                    "            \"1172\",\n" +
                    "            \"1173\",\n" +
                    "            \"1174\",\n" +
                    "            \"1176\",\n" +
                    "            \"1177\",\n" +
                    "            \"1178\",\n" +
                    "            \"1179\",\n" +
                    "            \"1180\",\n" +
                    "            \"1182\",\n" +
                    "            \"1183\",\n" +
                    "            \"1184\",\n" +
                    "            \"1186\",\n" +
                    "            \"1189\",\n" +
                    "            \"1190\",\n" +
                    "            \"1191\",\n" +
                    "            \"1194\",\n" +
                    "            \"1196\",\n" +
                    "            \"1197\",\n" +
                    "            \"1198\",\n" +
                    "            \"1201\",\n" +
                    "            \"1202\",\n" +
                    "            \"1204\",\n" +
                    "            \"1206\",\n" +
                    "            \"1207\",\n" +
                    "            \"1208\",\n" +
                    "            \"1209\",\n" +
                    "            \"1210\",\n" +
                    "            \"1213\",\n" +
                    "            \"1214\",\n" +
                    "            \"1216\",\n" +
                    "            \"1217\",\n" +
                    "            \"1219\",\n" +
                    "            \"1220\",\n" +
                    "            \"1221\",\n" +
                    "            \"1222\",\n" +
                    "            \"1224\",\n" +
                    "            \"1225\",\n" +
                    "            \"1227\",\n" +
                    "            \"1231\",\n" +
                    "            \"1233\",\n" +
                    "            \"1236\",\n" +
                    "            \"1237\",\n" +
                    "            \"1238\",\n" +
                    "            \"1239\",\n" +
                    "            \"1241\",\n" +
                    "            \"1242\",\n" +
                    "            \"1245\",\n" +
                    "            \"1247\",\n" +
                    "            \"1249\",\n" +
                    "            \"1256\",\n" +
                    "            \"1259\",\n" +
                    "            \"1260\",\n" +
                    "            \"1262\",\n" +
                    "            \"1263\",\n" +
                    "            \"1264\",\n" +
                    "            \"1269\",\n" +
                    "            \"1271\",\n" +
                    "            \"1278\",\n" +
                    "            \"1279\",\n" +
                    "            \"1283\",\n" +
                    "            \"1284\",\n" +
                    "            \"1285\",\n" +
                    "            \"1288\",\n" +
                    "            \"1291\",\n" +
                    "            \"test_4565\",\n" +
                    "            \"4565_test\",\n" +
                    "            \"1293\",\n" +
                    "            \"1294\",\n" +
                    "            \"1295\",\n" +
                    "            \"1296\",\n" +
                    "            \"1298\",\n" +
                    "            \"1299\",\n" +
                    "            \"1301\",\n" +
                    "            \"1302\",\n" +
                    "            \"1303\",\n" +
                    "            \"1308\",\n" +
                    "            \"1304\",\n" +
                    "            \"1305\",\n" +
                    "            \"1306\",\n" +
                    "            \"1309\",\n" +
                    "            \"1310\",\n" +
                    "            \"1311\",\n" +
                    "            \"1312\",\n" +
                    "            \"dp-false\",\n" +
                    "            \"wapp\",\n" +
                    "            \"store_4565\",\n" +
                    "            \"vp_M\",\n" +
                    "            \"anonymous\",\n" +
                    "            \"clothing_store_enabled\",\n" +
                    "            \"checkoutOptimization\",\n" +
                    "            \"NAV_UI\",\n" +
                    "            \"T003\",\n" +
                    "            \"T014\",\n" +
                    "            \"rmp_enabled_user\"\n" +
                    "        ]\n" +
                    "    }\n" +
                    "}";

            // Generate the HttpEntity
            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, asdaHeader());

            try {
                // Hit the external Api and get the response
                ResponseEntity<String> response = restTemplate.exchange(
                        url,
                        HttpMethod.POST,
                        requestEntity,
                        String.class
                );

                // Get the response body
                String responseBody = response.getBody();

                // Map the response with the object mapper
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> map = mapper.readValue(responseBody, Map.class);

                // Search for the "price" in the response
                Map<String, Object> dataMap = (Map<String, Object>) map.get("data");
                Map<String, Object> tempoCmsContentMap = (Map<String, Object>) dataMap.get("tempo_cms_content");
                ArrayList zonesList = (ArrayList) tempoCmsContentMap.get("zones");
                Map<String, Object> zonesFirstIdx = (Map<String, Object>) zonesList.getFirst();
                Map<String, Object> configsMap = (Map<String, Object>) zonesFirstIdx.get("configs");
                Map<String, Object> productsMap = (Map<String, Object>) configsMap.get("products");
                ArrayList itemsMap = (ArrayList) productsMap.get("items");
                Map<String, Object> itemsFirstIdx = (Map<String, Object>) itemsMap.getFirst();
                Map<String, Object> priceMap = (Map<String, Object>) itemsFirstIdx.get("price");
                Map<String, Object> priceInfoMap = (Map<String, Object>) priceMap.get("price_info");

                // Get the price
                String itemPriceString = priceInfoMap.get("price").toString().split("£")[1];

                Map<String, Object> itemMap = (Map<String, Object>) itemsFirstIdx.get("item");
                ArrayList upcNumberList = (ArrayList) itemMap.get("upc_numbers");

                // Generate the url for product image
                String imageRef = "https://ui.assets-asda.com/dm/asdagroceries/" + upcNumberList.getFirst().toString() +
                        "_T1?defaultImage=asdagroceries/noImage&resMode=sharp2&id=InbSJ1&fmt=webp&dpr=off&fit=constrain,1&wid=188&hei=188";

                // Convert the price string to double
                Double itemPrice = Double.parseDouble(itemPriceString);

                System.out.println("URL :" + url + ">>Price :" + itemPrice + ">> Img: " + imageRef);

            } catch (Exception e) {

                //Insert into PricingInsights table and set price as 0.0 and availability as false.

                System.out.println("Error URL :" + url);
                logger.error("Log level: ERROR  - ASDA fetchPricingInsights - 1 : " + e);

            }
        }
    }


    public void insertPricingInsights(ProductMasterDataDAO productMasterData) {

        //Current Timestamp
        Timestamp now = new Timestamp(System.currentTimeMillis());

        // Split the url with slash
        String[] urlSplitBySlash = productMasterData.getUrl().split("/");
//        String productId = urlSplitBySlash[6].contains("?") ? urlSplitBySlash[6].split("\\?")[0] : urlSplitBySlash[6];


        String productId = null;
        if (urlSplitBySlash.length > 6) {
            if (urlSplitBySlash[6].contains("?")) {
                if (urlSplitBySlash[6].split("\\?").length > 0)
                    productId = urlSplitBySlash[6].split("\\?")[0];
            } else {
                productId = urlSplitBySlash[6];
            }
        }

        if (null != productId) {
            // Get the pid value from splitted array
//        String pid = urlSplitBySlash[urlSplitBySlash.length - 1];
            String pid = productId;

            String requestBody = "{\n" +
                    "    \"requestorigin\": \"gi\",\n" +
                    "    \"contract\": \"web/cms/product-details-page\",\n" +
                    "    \"variables\": {\n" +
                    "        \"is_eat_and_collect\": false,\n" +
                    "        \"store_id\": \"4565\",\n" +
                    "        \"type\": \"content\",\n" +
                    "        \"request_origin\": \"gi\",\n" +
                    "        \"payload\": {\n" +
                    "            \"page_id\": \"" + pid + "\",\n" +
                    "            \"page_type\": \"productDetailsPage\",\n" +
                    "            \"page_meta_info\": true\n" +
                    "        },\n" +
                    "        \"user_segments\": [\n" +
                    "            \"1007\",\n" +
                    "            \"1019\",\n" +
                    "            \"1020\",\n" +
                    "            \"1023\",\n" +
                    "            \"1024\",\n" +
                    "            \"1027\",\n" +
                    "            \"1038\",\n" +
                    "            \"1041\",\n" +
                    "            \"1042\",\n" +
                    "            \"1043\",\n" +
                    "            \"1047\",\n" +
                    "            \"1053\",\n" +
                    "            \"1055\",\n" +
                    "            \"1057\",\n" +
                    "            \"1059\",\n" +
                    "            \"1067\",\n" +
                    "            \"1070\",\n" +
                    "            \"1082\",\n" +
                    "            \"1087\",\n" +
                    "            \"1097\",\n" +
                    "            \"1098\",\n" +
                    "            \"1099\",\n" +
                    "            \"1100\",\n" +
                    "            \"1102\",\n" +
                    "            \"1105\",\n" +
                    "            \"1107\",\n" +
                    "            \"1109\",\n" +
                    "            \"1110\",\n" +
                    "            \"1111\",\n" +
                    "            \"1112\",\n" +
                    "            \"1116\",\n" +
                    "            \"1117\",\n" +
                    "            \"1119\",\n" +
                    "            \"1123\",\n" +
                    "            \"1124\",\n" +
                    "            \"1126\",\n" +
                    "            \"1128\",\n" +
                    "            \"1130\",\n" +
                    "            \"1140\",\n" +
                    "            \"1141\",\n" +
                    "            \"1144\",\n" +
                    "            \"1147\",\n" +
                    "            \"1150\",\n" +
                    "            \"1152\",\n" +
                    "            \"1157\",\n" +
                    "            \"1159\",\n" +
                    "            \"1160\",\n" +
                    "            \"1165\",\n" +
                    "            \"1166\",\n" +
                    "            \"1167\",\n" +
                    "            \"1169\",\n" +
                    "            \"1170\",\n" +
                    "            \"1172\",\n" +
                    "            \"1173\",\n" +
                    "            \"1174\",\n" +
                    "            \"1176\",\n" +
                    "            \"1177\",\n" +
                    "            \"1178\",\n" +
                    "            \"1179\",\n" +
                    "            \"1180\",\n" +
                    "            \"1182\",\n" +
                    "            \"1183\",\n" +
                    "            \"1184\",\n" +
                    "            \"1186\",\n" +
                    "            \"1189\",\n" +
                    "            \"1190\",\n" +
                    "            \"1191\",\n" +
                    "            \"1194\",\n" +
                    "            \"1196\",\n" +
                    "            \"1197\",\n" +
                    "            \"1198\",\n" +
                    "            \"1201\",\n" +
                    "            \"1202\",\n" +
                    "            \"1204\",\n" +
                    "            \"1206\",\n" +
                    "            \"1207\",\n" +
                    "            \"1208\",\n" +
                    "            \"1209\",\n" +
                    "            \"1210\",\n" +
                    "            \"1213\",\n" +
                    "            \"1214\",\n" +
                    "            \"1216\",\n" +
                    "            \"1217\",\n" +
                    "            \"1219\",\n" +
                    "            \"1220\",\n" +
                    "            \"1221\",\n" +
                    "            \"1222\",\n" +
                    "            \"1224\",\n" +
                    "            \"1225\",\n" +
                    "            \"1227\",\n" +
                    "            \"1231\",\n" +
                    "            \"1233\",\n" +
                    "            \"1236\",\n" +
                    "            \"1237\",\n" +
                    "            \"1238\",\n" +
                    "            \"1239\",\n" +
                    "            \"1241\",\n" +
                    "            \"1242\",\n" +
                    "            \"1245\",\n" +
                    "            \"1247\",\n" +
                    "            \"1249\",\n" +
                    "            \"1256\",\n" +
                    "            \"1259\",\n" +
                    "            \"1260\",\n" +
                    "            \"1262\",\n" +
                    "            \"1263\",\n" +
                    "            \"1264\",\n" +
                    "            \"1269\",\n" +
                    "            \"1271\",\n" +
                    "            \"1278\",\n" +
                    "            \"1279\",\n" +
                    "            \"1283\",\n" +
                    "            \"1284\",\n" +
                    "            \"1285\",\n" +
                    "            \"1288\",\n" +
                    "            \"1291\",\n" +
                    "            \"test_4565\",\n" +
                    "            \"4565_test\",\n" +
                    "            \"1293\",\n" +
                    "            \"1294\",\n" +
                    "            \"1295\",\n" +
                    "            \"1296\",\n" +
                    "            \"1298\",\n" +
                    "            \"1299\",\n" +
                    "            \"1301\",\n" +
                    "            \"1302\",\n" +
                    "            \"1303\",\n" +
                    "            \"1308\",\n" +
                    "            \"1304\",\n" +
                    "            \"1305\",\n" +
                    "            \"1306\",\n" +
                    "            \"1309\",\n" +
                    "            \"1310\",\n" +
                    "            \"1311\",\n" +
                    "            \"1312\",\n" +
                    "            \"dp-false\",\n" +
                    "            \"wapp\",\n" +
                    "            \"store_4565\",\n" +
                    "            \"vp_M\",\n" +
                    "            \"anonymous\",\n" +
                    "            \"clothing_store_enabled\",\n" +
                    "            \"checkoutOptimization\",\n" +
                    "            \"NAV_UI\",\n" +
                    "            \"T003\",\n" +
                    "            \"T014\",\n" +
                    "            \"rmp_enabled_user\"\n" +
                    "        ]\n" +
                    "    }\n" +
                    "}";

            // Generate the HttpEntity
            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, asdaHeader());

            try {
                // Hit the external Api and get the response
                ResponseEntity<String> response = restTemplate.exchange(
                        url,
                        HttpMethod.POST,
                        requestEntity,
                        String.class
                );

                // Get the response body
                String responseBody = response.getBody();

                // Map the response with the object mapper
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> map = mapper.readValue(responseBody, Map.class);

                // Search for the "price" in the response
                Map<String, Object> dataMap = (Map<String, Object>) map.get("data");
                Map<String, Object> tempoCmsContentMap = (Map<String, Object>) dataMap.get("tempo_cms_content");
                ArrayList zonesList = (ArrayList) tempoCmsContentMap.get("zones");
                Map<String, Object> zonesFirstIdx = (Map<String, Object>) zonesList.getFirst();
                Map<String, Object> configsMap = (Map<String, Object>) zonesFirstIdx.get("configs");
                Map<String, Object> productsMap = (Map<String, Object>) configsMap.get("products");
                ArrayList itemsMap = (ArrayList) productsMap.get("items");
                Map<String, Object> itemsFirstIdx = (Map<String, Object>) itemsMap.getFirst();
                Map<String, Object> priceMap = (Map<String, Object>) itemsFirstIdx.get("price");
                Map<String, Object> priceInfoMap = (Map<String, Object>) priceMap.get("price_info");

                // Get the price
                String itemPriceString = priceInfoMap.get("price").toString().split("£")[1];

                Map<String, Object> itemMap = (Map<String, Object>) itemsFirstIdx.get("item");
                ArrayList upcNumberList = (ArrayList) itemMap.get("upc_numbers");

                // Generate the url for product image
                String imageRef = "https://ui.assets-asda.com/dm/asdagroceries/" + upcNumberList.getFirst().toString() +
                        "_T1?defaultImage=asdagroceries/noImage&resMode=sharp2&id=InbSJ1&fmt=webp&dpr=off&fit=constrain,1&wid=188&hei=188";

                // Convert the price string to double
                Double itemPrice = Double.parseDouble(itemPriceString);

                //Insert into PricingInsights table
                ProductInsightsRepo.insertPricingInsights(productMasterData.getNo(), productMasterData.getTag(), productMasterData.getShopName(),
                        itemPrice, productMasterData.getUrl(), true, now, imageRef, 0.0);

            } catch (Exception e) {

                //Insert into PricingInsights table and set price as 0.0 and availability as false.
                ProductInsightsRepo.insertPricingInsights(productMasterData.getNo(), productMasterData.getTag(), productMasterData.getShopName(),
                        0.0, productMasterData.getUrl(), false, now, "", 0.0);

                System.out.println("Error URL :" + productMasterData.getUrl());
                logger.error("Log level: ERROR  - ASDA insertPricingInsights - 1 : " + e);

            }
        }
    }
}
