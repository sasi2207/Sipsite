package com.uk.uk.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uk.uk.entity.ProductMasterDataDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

@Service
public class TescoTempImpl {

    @Autowired
    private RestTemplate restTemplate;

    String url = "https://api.tesco.com/shoppingexperience";

    private HttpHeaders asdaHeader() {
        // Declaring and set Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("X-Apikey", "TvOSZJHlEk0pjniDGQFAc9Q59WGAR4dA");
        headers.set("Origin", "https://www.tesco.com");
        return headers;
    }


    public void insertPricingInsights() {
        String requestBody = "[\n" +
                "    {\n" +
                "        \"operationName\": \"GetProduct\",\n" +
                "        \"variables\": {\n" +
                "            \"tpnc\": \"253237874\",\n" +
                "            \"skipReviews\": false,\n" +
                "            \"offset\": 0,\n" +
                "            \"count\": 10\n" +
                "        },\n" +
                "        \"extensions\": {\n" +
                "            \"mfeName\": \"unknown\"\n" +
                "        },\n" +
                "        \"query\": \"query GetProduct($tpnc: String, $skipReviews: Boolean!, $offset: Int, $count: Int) {\\n  product(tpnc: $tpnc) {\\n    id\\n    baseProductId\\n    isRestrictedOrderAmendment\\n    gtin\\n    tpnb\\n    tpnc\\n    title\\n    description\\n    brandName\\n    isInFavourites\\n    defaultImageUrl\\n    superDepartmentName\\n    superDepartmentId\\n    departmentName\\n    departmentId\\n    aisleName\\n    aisleId\\n    shelfName\\n    displayType\\n    shelfId\\n    averageWeight\\n    bulkBuyLimit\\n    bulkBuyLimitGroupId\\n    bulkBuyLimitMessage\\n    groupBulkBuyLimit\\n    isForSale\\n    isNew\\n    status\\n    productType\\n    charges {\\n      ... on ProductDepositReturnCharge {\\n        __typename\\n        amount\\n      }\\n      __typename\\n    }\\n    __typename\\n    ... on MPProduct {\\n      fulfilment {\\n        __typename\\n        ... on ProductDeliveryType {\\n          cutoff\\n          deliveryType\\n          minDeliveryDays\\n          maxDeliveryDays\\n          charges {\\n            value\\n            __typename\\n            criteria {\\n              __typename\\n              ... on ProductDeliveryCriteria {\\n                deliveryType: type\\n                deliveryvalue: value\\n                __typename\\n              }\\n              ... on ProductDeliveryBasketValueCriteria {\\n                type\\n                value\\n                __typename\\n              }\\n            }\\n          }\\n          __typename\\n        }\\n        ... on ProductReturnType {\\n          __typename\\n          daysToReturn\\n        }\\n      }\\n      __typename\\n    }\\n    seller {\\n      id\\n      name\\n      __typename\\n    }\\n    images {\\n      display {\\n        default {\\n          url\\n          originalUrl\\n          __typename\\n        }\\n        zoom {\\n          url\\n          __typename\\n        }\\n        __typename\\n      }\\n      __typename\\n    }\\n    foodIcons\\n    shelfLife {\\n      url\\n      message\\n      __typename\\n    }\\n    restrictions {\\n      type\\n      isViolated\\n      message\\n      __typename\\n    }\\n    distributorAddress {\\n      ...Address\\n      __typename\\n    }\\n    manufacturerAddress {\\n      ...Address\\n      __typename\\n    }\\n    importerAddress {\\n      ...Address\\n      __typename\\n    }\\n    returnTo {\\n      ...Address\\n      addressLine7\\n      addressLine8\\n      addressLine9\\n      addressLine10\\n      addressLine11\\n      addressLine12\\n      __typename\\n    }\\n    maxWeight\\n    minWeight\\n    catchWeightList {\\n      price\\n      weight\\n      default\\n      __typename\\n    }\\n    multiPackDetails {\\n      ...Multipack\\n      __typename\\n    }\\n    details {\\n      energyEfficiency {\\n        class\\n        energyClassUrl\\n        productInfoDoc\\n        __typename\\n      }\\n      ...Details\\n      components {\\n        ... on CompetitorsInfo {\\n          competitors {\\n            id\\n            priceMatch {\\n              isMatching\\n              __typename\\n            }\\n            __typename\\n          }\\n          __typename\\n        }\\n        ... on AdditionalInfo {\\n          isLowEverydayPricing\\n          isLowPricePromise\\n          __typename\\n        }\\n        __typename\\n      }\\n      __typename\\n    }\\n    price {\\n      actual\\n      unitPrice\\n      unitOfMeasure\\n      __typename\\n    }\\n    promotions {\\n      id\\n      promotionType\\n      startDate\\n      endDate\\n      description\\n      unitSellingInfo\\n      price {\\n        beforeDiscount\\n        afterDiscount\\n        __typename\\n      }\\n      attributes\\n      qualities\\n      __typename\\n    }\\n    reviews(offset: $offset, count: $count) @skip(if: $skipReviews) {\\n      info {\\n        offset\\n        total\\n        page\\n        count\\n        __typename\\n      }\\n      entries {\\n        rating {\\n          value\\n          range\\n          __typename\\n        }\\n        author {\\n          nickname\\n          authoredByMe\\n          __typename\\n        }\\n        status\\n        summary\\n        text\\n        syndicated\\n        syndicationSource {\\n          name\\n          __typename\\n        }\\n        submissionDateTime\\n        reviewId\\n        __typename\\n      }\\n      stats {\\n        noOfReviews\\n        overallRating\\n        __typename\\n      }\\n      __typename\\n    }\\n  }\\n}\\n\\nfragment GDA on GuidelineDailyAmountType {\\n  title\\n  dailyAmounts {\\n    name\\n    value\\n    percent\\n    rating\\n    __typename\\n  }\\n  __typename\\n}\\n\\nfragment Alcohol on AlcoholInfoItemType {\\n  tastingNotes\\n  grapeVariety\\n  vinificationDetails\\n  history\\n  regionalInformation\\n  storageType\\n  storageInstructions\\n  alcoholUnitsOtherText\\n  regionOfOrigin\\n  alcoholType\\n  wineColour\\n  alcoholUnits\\n  percentageAlcohol\\n  currentVintage\\n  producer\\n  typeOfClosure\\n  wineMaker\\n  packQty\\n  packMeasure\\n  country\\n  tasteCategory\\n  alcoholByVolumeOtherText\\n  wineEffervescence\\n  legalNotice {\\n    message\\n    link\\n    __typename\\n  }\\n  __typename\\n}\\n\\nfragment Nutrition on NutritionalInfoItemType {\\n  name\\n  perComp: value1\\n  perServing: value2\\n  referenceIntake: value3\\n  referencePercentage: value4\\n  __typename\\n}\\n\\nfragment CookingInstructions on CookingInstructionsType {\\n  oven {\\n    chilled {\\n      time\\n      instructions\\n      temperature {\\n        value\\n        __typename\\n      }\\n      __typename\\n    }\\n    frozen {\\n      time\\n      instructions\\n      temperature {\\n        value\\n        __typename\\n      }\\n      __typename\\n    }\\n    __typename\\n  }\\n  microwave {\\n    chilled {\\n      detail {\\n        step\\n        T650\\n        T750\\n        T850\\n        __typename\\n      }\\n      instructions\\n      __typename\\n    }\\n    frozen {\\n      detail {\\n        step\\n        T650\\n        T750\\n        T850\\n        __typename\\n      }\\n      instructions\\n      __typename\\n    }\\n    __typename\\n  }\\n  cookingMethods {\\n    name\\n    instructions\\n    time\\n    __typename\\n  }\\n  otherInstructions\\n  cookingGuidelines\\n  cookingPrecautions\\n  __typename\\n}\\n\\nfragment Address on AddressType {\\n  addressLine1\\n  addressLine2\\n  addressLine3\\n  addressLine4\\n  addressLine5\\n  addressLine6\\n  __typename\\n}\\n\\nfragment Multipack on MultipackDetailType {\\n  name\\n  description\\n  sequence\\n  numberOfUses\\n  features\\n  boxContents\\n  storage\\n  nutritionIconInfo\\n  nutritionalClaims\\n  healthClaims\\n  preparationAndUsage\\n  otherInformation\\n  ingredients\\n  cookingInstructions {\\n    ...CookingInstructions\\n    __typename\\n  }\\n  originInformation {\\n    title\\n    value\\n    __typename\\n  }\\n  guidelineDailyAmount {\\n    ...GDA\\n    __typename\\n  }\\n  allergenInfo: allergens {\\n    name\\n    values\\n    __typename\\n  }\\n  nutritionInfo {\\n    name\\n    perComp: value1\\n    perServing: value2\\n    referenceIntake: value3\\n    referencePercentage: value4\\n    __typename\\n  }\\n  __typename\\n}\\n\\nfragment Details on ProductDetailsType {\\n  ingredients\\n  legalLabelling\\n  packSize {\\n    value\\n    units\\n    __typename\\n  }\\n  allergenInfo: allergens {\\n    name\\n    values\\n    __typename\\n  }\\n  marketingTextInfo: marketing\\n  storage\\n  nutritionInfo: nutrition {\\n    ...Nutrition\\n    __typename\\n  }\\n  specifications {\\n    specificationAttributes {\\n      attributeName: name\\n      attributeValue: value\\n      __typename\\n    }\\n    __typename\\n  }\\n  otherNutritionInformation\\n  hazardInfo {\\n    chemicalName\\n    productName\\n    signalWord\\n    statements\\n    symbolCodes\\n    __typename\\n  }\\n  guidelineDailyAmount {\\n    ...GDA\\n    __typename\\n  }\\n  numberOfUses\\n  preparationAndUsage\\n  freezingInstructions {\\n    standardGuidelines\\n    freezingGuidelines\\n    defrosting\\n    __typename\\n  }\\n  manufacturerMarketing\\n  productMarketing\\n  brandMarketing\\n  otherInformation\\n  additives\\n  warnings\\n  netContents\\n  drainedWeight\\n  safetyWarning\\n  lowerAgeLimit\\n  upperAgeLimit\\n  healthmark\\n  recyclingInfo\\n  nappyInfo: nappies {\\n    quantity\\n    nappySize\\n    __typename\\n  }\\n  alcoholInfo: alcohol {\\n    ...Alcohol\\n    __typename\\n  }\\n  cookingInstructions {\\n    ...CookingInstructions\\n    __typename\\n  }\\n  originInformation {\\n    title\\n    value\\n    __typename\\n  }\\n  dosage\\n  preparationGuidelines\\n  directions\\n  features\\n  healthClaims\\n  boxContents\\n  nutritionalClaims\\n  __typename\\n}\\n\"\n" +
                "    }\n" +
                "]";

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
        } catch (Exception ex) {

        }
    }

}
