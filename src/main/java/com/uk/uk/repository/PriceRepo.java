package com.uk.uk.repository;

import com.uk.uk.entity.PriceDAO;
import com.uk.uk.entity.PricingInsightsDAO;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

public interface PriceRepo extends JpaRepository<PriceDAO, Integer> {
    @Query(value = "SELECT * FROM price ", nativeQuery = true)
    List<PriceDAO> getAll();

    @Query(value = "SELECT * FROM price where ShopName = ?1", nativeQuery = true)
    List<PriceDAO> getAllByShopName(String shopName);

    @Modifying
    @Query(value = "insert into price (Price,ShopName,Url) values(?1,?2,?3)", nativeQuery = true)
    @Transactional
    void insertPricingInsights(Double price, String shopName, String url);

}
