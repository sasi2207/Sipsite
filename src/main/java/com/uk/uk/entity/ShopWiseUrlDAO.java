package com.uk.uk.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.cache.annotation.Cacheable;

@Getter
@Setter
@Cacheable
@NoArgsConstructor

public class ShopWiseUrlDAO {
    public String asdaUrl;
    public String morissonUrl;
    public String sainsburyUrl;
    public String tescoUrl;
    public String waitRoseUrl;
    public String ocadoUrl;
    public String coOpUrl;
    public String amazonUrl;

    public String getAsdaUrl() {
        return asdaUrl;
    }

    public void setAsdaUrl(String asdaUrl) {
        this.asdaUrl = asdaUrl;
    }

    public String getMorissonUrl() {
        return morissonUrl;
    }

    public void setMorissonUrl(String morissonUrl) {
        this.morissonUrl = morissonUrl;
    }

    public String getSainsburyUrl() {
        return sainsburyUrl;
    }

    public void setSainsburyUrl(String sainsburyUrl) {
        this.sainsburyUrl = sainsburyUrl;
    }

    public String getTescoUrl() {
        return tescoUrl;
    }

    public void setTescoUrl(String tescoUrl) {
        this.tescoUrl = tescoUrl;
    }

    public String getWaitRoseUrl() {
        return waitRoseUrl;
    }

    public void setWaitRoseUrl(String waitRoseUrl) {
        this.waitRoseUrl = waitRoseUrl;
    }

    public String getOcadoUrl() {
        return ocadoUrl;
    }

    public void setOcadoUrl(String ocadoUrl) {
        this.ocadoUrl = ocadoUrl;
    }

    public String getCoOpUrl() {
        return coOpUrl;
    }

    public void setCoOpUrl(String coOpUrl) {
        this.coOpUrl = coOpUrl;
    }

    public String getAmazonUrl() {
        return amazonUrl;
    }

    public void setAmazonUrl(String amazonUrl) {
        this.amazonUrl = amazonUrl;
    }
}
