package com.uk.uk.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import com.uk.uk.entity.PricingInsightsDAO;

import java.sql.Timestamp;
import java.util.List;

public interface PricingInsightsRepo extends JpaRepository<PricingInsightsDAO, Integer> {
    @Query(value = "SELECT * FROM PricingInsights ", nativeQuery = true)
    List<PricingInsightsDAO> getAll();

    @Modifying
    @Query(value = "insert into PricingInsights (ProductMasterDataNo,Tag, ShopName, Price,Url, Availability,CreatedAtDateTime,ImageRef, SpecialPrice) " +
            "values(?1,?2,?3,?4,?5,?6,?7,?8,?9)", nativeQuery = true)
    @Transactional
    void insertPricingInsights(Integer productMasterDataNo, Integer tag, String shopName, Double price, String url, Boolean availability, Timestamp createdAtDateTime,
                               String imageRef, Double specialPrice);

    @Modifying
    @Query(value = "delete from PricingInsights where Tag=?1", nativeQuery = true)
    @Transactional
    void deletePricingInsightsByTag(Integer tag);

    @Modifying
    @Query(value = "truncate table PricingInsights ", nativeQuery = true)
    @Transactional
    void truncatePricingInsightsTable();

    @Modifying
    @Query(value = "delete from PricingInsights where No=?1", nativeQuery = true)
    @Transactional
    void deletePricingInsightsByNo(Integer no);

    @Modifying
    @Query(value = "delete from PricingInsights where Tag=?1 and ShopName=?2", nativeQuery = true)
    @Transactional
    void deletePricingInsightsByTagAndShopName(Integer tag, String shopName);

}
