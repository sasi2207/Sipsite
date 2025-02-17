package com.uk.uk.implementation;

import com.uk.uk.entity.PricingInsightsDAO;
import com.uk.uk.entity.DashboardGridDataDAO;
import com.uk.uk.entity.DashboardGridPriceUrlDAO;
import com.uk.uk.entity.ProductMasterDataDAO;
import com.uk.uk.repository.ProductMasterDataRepo;
import com.uk.uk.repository.PricingInsightsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class PricingInsightsImpl {
    @Autowired
    private ProductMasterDataRepo ProductMasterDataRepo;
    @Autowired
    private PricingInsightsRepo PricingInsightsRepo;

    public List<ProductMasterDataDAO> getProductMasterByTag(Integer tag) {
        List<ProductMasterDataDAO> productMasterDataDAOList = new ArrayList<>();
        productMasterDataDAOList = ProductMasterDataRepo.getProductMasterDataByTagNo(tag);
        return productMasterDataDAOList;
    }

    public List<DashboardGridDataDAO> getGridData() {
        List<PricingInsightsDAO> productInsightsDAOList = new ArrayList<>();
        productInsightsDAOList = PricingInsightsRepo.getAll();

        Map<Integer, List<PricingInsightsDAO>> productGroupByTag = productInsightsDAOList.stream().collect(Collectors.groupingBy(PricingInsightsDAO::getTag));

        List<DashboardGridDataDAO> gridDataList = new ArrayList<>();

        // Iterate over the map
        for (Map.Entry<Integer, List<PricingInsightsDAO>> entry : productGroupByTag.entrySet()) {
            Integer key = entry.getKey();
            List<PricingInsightsDAO> daoList = entry.getValue();

            DashboardGridDataDAO gridData = new DashboardGridDataDAO();
            List<ProductMasterDataDAO> productMasterDataDAOList = ProductMasterDataRepo.getProductMasterDataByTagNo(key);
            if (!productMasterDataDAOList.isEmpty()) {

                ProductMasterDataDAO productMasterDataDAO = productMasterDataDAOList.getFirst();

                Integer idx = 0;
                Double startingPrice = 0.00;
                List<String> lowestPriceShopNameList = new ArrayList<>();

                for (PricingInsightsDAO pricingInsights : daoList) {

                    if (idx.intValue() == 0) {
                        gridData.setNo(productMasterDataDAO.getNo());
                        gridData.setTag(productMasterDataDAO.getTag());
                        gridData.setCategory(productMasterDataDAO.getCategory());
                        gridData.setProductName(productMasterDataDAO.getProductName());
                        gridData.setQuantity(productMasterDataDAO.getQuantity());
                        gridData.setMeasurement(productMasterDataDAO.getMeasurement());
                        gridData.setSize(productMasterDataDAO.getQuantity().toString() + " " + productMasterDataDAO.getMeasurement());
//                        gridData.setU
//                        gridData.setAmazon(productMasterDataDAO.getUrl());
                    }

                    DashboardGridPriceUrlDAO dashboardGridPriceUrlDAO = new DashboardGridPriceUrlDAO();
                    dashboardGridPriceUrlDAO.setPrice(pricingInsights.getPrice());
                    dashboardGridPriceUrlDAO.setUrl(pricingInsights.getUrl());
                    dashboardGridPriceUrlDAO.setSpecialPrice(pricingInsights.getSpecialPrice());

                    if (startingPrice == 0.00) {
                        startingPrice = pricingInsights.getPrice();
                        lowestPriceShopNameList.add(pricingInsights.getShopName());
                    } else {
                        if (pricingInsights.getPrice() <= startingPrice && pricingInsights.getPrice() != 0) {
                            if (pricingInsights.getPrice() < startingPrice) {
                                lowestPriceShopNameList.clear();
                                lowestPriceShopNameList.add(pricingInsights.getShopName());
                            } else
                                lowestPriceShopNameList.add(pricingInsights.getShopName());
                            startingPrice = pricingInsights.getPrice();
                        }
                    }

                    if (null != pricingInsights.getImageRef() && !pricingInsights.getImageRef().isEmpty() &&
                            !pricingInsights.getShopName().equalsIgnoreCase("Amazon"))
                        gridData.setImageUrl(pricingInsights.getImageRef());

//                    gridData.setImageUrl(pricingInsights.getImageRef());
                    if (daoList.size() == 1 && pricingInsights.getShopName().equalsIgnoreCase("Amazon"))
                        gridData.setImageUrl(pricingInsights.getImageRef());

                    ProductMasterDataDAO productMasterDataDAO1 = new ProductMasterDataDAO();
                    switch (pricingInsights.getShopName()) {

                        case "Morrisons":
                            if (null != productMasterDataDAOList.stream().filter(obj -> obj.getShopName().equalsIgnoreCase("Morrisons")).findFirst().get()) {
                                productMasterDataDAO1 = productMasterDataDAOList.stream().filter(obj -> obj.getShopName().equalsIgnoreCase("Morrisons")).findFirst().get();
                                dashboardGridPriceUrlDAO.setUrl(productMasterDataDAO1.getUrl());
                            }
                            gridData.setMorrisons(dashboardGridPriceUrlDAO);
                            break;
                        case "Tesco":
                            if (null != productMasterDataDAOList.stream().filter(obj -> obj.getShopName().equalsIgnoreCase("Tesco")).findFirst().get()) {
                                productMasterDataDAO1 = productMasterDataDAOList.stream().filter(obj -> obj.getShopName().equalsIgnoreCase("Tesco")).findFirst().get();
                                dashboardGridPriceUrlDAO.setUrl(productMasterDataDAO1.getUrl());
                            }
                            gridData.setTesco(dashboardGridPriceUrlDAO);
                            break;
                        case "Amazon":
                            if (null != productMasterDataDAOList.stream().filter(obj -> obj.getShopName().equalsIgnoreCase("Amazon")).findFirst().get()) {
                                productMasterDataDAO1 = productMasterDataDAOList.stream().filter(obj -> obj.getShopName().equalsIgnoreCase("Amazon")).findFirst().get();
                                dashboardGridPriceUrlDAO.setUrl(productMasterDataDAO1.getUrl());
                            }
                            gridData.setAmazon(dashboardGridPriceUrlDAO);
                            break;
                        case "Sainsburys":
                            if (null != productMasterDataDAOList.stream().filter(obj -> obj.getShopName().equalsIgnoreCase("Sainsburys")).findFirst().get()) {
                                productMasterDataDAO1 = productMasterDataDAOList.stream().filter(obj -> obj.getShopName().equalsIgnoreCase("Sainsburys")).findFirst().get();
                                dashboardGridPriceUrlDAO.setUrl(productMasterDataDAO1.getUrl());
                            }
                            gridData.setSainsburys(dashboardGridPriceUrlDAO);
                            break;
                        case "CoOp":
                            if (null != productMasterDataDAOList.stream().filter(obj -> obj.getShopName().equalsIgnoreCase("CoOp")).findFirst().get()) {
                                productMasterDataDAO1 = productMasterDataDAOList.stream().filter(obj -> obj.getShopName().equalsIgnoreCase("CoOp")).findFirst().get();
                                dashboardGridPriceUrlDAO.setUrl(productMasterDataDAO1.getUrl());
                            }
                            gridData.setCoop(dashboardGridPriceUrlDAO);
                            break;
                        case "Ocado":
                            if (null != productMasterDataDAOList.stream().filter(obj -> obj.getShopName().equalsIgnoreCase("Ocado")).findFirst().get()) {
                                productMasterDataDAO1 = productMasterDataDAOList.stream().filter(obj -> obj.getShopName().equalsIgnoreCase("Ocado")).findFirst().get();
                                dashboardGridPriceUrlDAO.setUrl(productMasterDataDAO1.getUrl());
                            }
                            gridData.setOcado(dashboardGridPriceUrlDAO);
                            break;
                        case "WaitRose":
                            if (null != productMasterDataDAOList.stream().filter(obj -> obj.getShopName().equalsIgnoreCase("WaitRose")).findFirst().get()) {
                                productMasterDataDAO1 = productMasterDataDAOList.stream().filter(obj -> obj.getShopName().equalsIgnoreCase("WaitRose")).findFirst().get();
                                dashboardGridPriceUrlDAO.setUrl(productMasterDataDAO1.getUrl());
                            }
                            gridData.setWaitrose(dashboardGridPriceUrlDAO);
                            break;
                        case "ASDA":
                            if (null != productMasterDataDAOList.stream().filter(obj -> obj.getShopName().equalsIgnoreCase("ASDA")).findFirst().get()) {
                                productMasterDataDAO1 = productMasterDataDAOList.stream().filter(obj -> obj.getShopName().equalsIgnoreCase("ASDA")).findFirst().get();
                                dashboardGridPriceUrlDAO.setUrl(productMasterDataDAO1.getUrl());
                            }
                            gridData.setAsda(dashboardGridPriceUrlDAO);
                            break;

                    }

                    idx++;

                }

                gridData.setLowestPriceShopNameList(lowestPriceShopNameList);
                gridDataList.add(gridData);
            }

            //productMasterDataDAOList.stream().filter(product -> daoList.stream().noneMatch(pricing -> pricing.getNo().equals(product.getNo()))).collect(Collectors.toList());

            List<ProductMasterDataDAO> missingObjects = productMasterDataDAOList.stream().filter(product -> daoList.stream().noneMatch(pricing -> pricing.getShopName().equals(product.getShopName()))).collect(Collectors.toList());

            for (ProductMasterDataDAO missingObj : missingObjects) {
                gridData.setNo(missingObj.getNo());
                gridData.setTag(missingObj.getTag());
                gridData.setCategory(missingObj.getCategory());
                gridData.setProductName(missingObj.getProductName());
                gridData.setQuantity(missingObj.getQuantity());
                gridData.setMeasurement(missingObj.getMeasurement());
                gridData.setSize(missingObj.getQuantity().toString() + " " + missingObj.getMeasurement());

                DashboardGridPriceUrlDAO dashboardGridPriceUrlDAO = new DashboardGridPriceUrlDAO();
                dashboardGridPriceUrlDAO.setPrice(0.00);
                dashboardGridPriceUrlDAO.setUrl(missingObj.getUrl());

                switch (missingObj.getShopName()) {
                    case "Morrisons":
                        gridData.setMorrisons(dashboardGridPriceUrlDAO);
                        break;

                    case "Tesco":
                        gridData.setTesco(dashboardGridPriceUrlDAO);
                        break;

                    case "Amazon":
                        gridData.setAmazon(dashboardGridPriceUrlDAO);
                        break;

                    case "Sainsburys":
                        gridData.setSainsburys(dashboardGridPriceUrlDAO);
                        break;

                    case "CoOp":
                        gridData.setCoop(dashboardGridPriceUrlDAO);
                        break;

                    case "Ocado":
                        gridData.setOcado(dashboardGridPriceUrlDAO);
                        break;

                    case "WaitRose":
                        gridData.setWaitrose(dashboardGridPriceUrlDAO);
                        break;

                    case "ASDA":
                        gridData.setAsda(dashboardGridPriceUrlDAO);
                        break;
                }
            }
        }
        return gridDataList;
    }

    public Boolean deletePricingInsightsByTag(Integer tag) {
        Boolean deleteStatus = false;

        try {
            PricingInsightsRepo.deletePricingInsightsByTag(tag);
            deleteStatus = true;
        } catch (Exception e) {
            System.out.println("Delete Pricing Insights By Tag Error : " + e);
            deleteStatus = false;
        }

        return deleteStatus;

    }


    public Boolean exportDataToFile(String filePath) throws IOException {
        List<PricingInsightsDAO> insights = PricingInsightsRepo.findAll();

        // Prepare the file content
        StringBuilder content = new StringBuilder();
        content.append("No,ProductMasterDataNo,Tag,ShopName,Price,SpecialPrice,URL,Availability,ImageRef,CreatedAtDateTime\n");
        for (PricingInsightsDAO insight : insights) {
            content.append(insight.getNo()).append(",")
                    .append(insight.getProductMasterDataNo()).append(",")
                    .append(insight.getTag()).append(",")
                    .append(insight.getShopName()).append(",")
                    .append(insight.getPrice()).append(",")
                    .append(insight.getSpecialPrice()).append(",")
                    .append(insight.getUrl()).append(",")
                    .append(insight.getAvailability()).append(",")
                    .append(insight.getImageRef()).append(",")
                    .append(insight.getCreatedAtDateTime()).append(",");
        }

        // Write to file
        File file = new File(filePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content.toString());
        }

        return true;
    }
}
