package com.uk.uk.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import com.uk.uk.implementation.*;

import java.io.IOException;

@Component
public class ScheduledTaskService {

    @Autowired
    private AsdaImpl AsdaImpl;
    @Autowired
    private MorrisonsImpl MorrisonsImpl;
    @Autowired
    private SainsburysImpl SainsburysImpl;
    @Autowired
    private TescoImpl TescoImpl;
    @Autowired
    private ProductMasterDataImpl ProductMasterDataImpl;
    @Autowired
    private PricingInsightsImpl PricingInsightsImpl;
    @Autowired
    private WaitRoseImpl WaitRoseImpl;
    @Autowired
    private AmazonImpl AmazonImpl;
    @Autowired
    private OcadoImpl OcadoImpl;
    @Autowired
    private CoOpImpl CoOpImpl;
    @Autowired
    private AmazonTempImpl AmazonTempImpl;

    // Method
    // To trigger the scheduler to run every two seconds
//    @Scheduled(fixedRate = 10000)
//    @Scheduled(cron = "0 0 9,15,21 * * *") // Every day at 9:00, 15:00, and 21:00
    public void scheduleTask() throws IOException, InterruptedException {

        System.out.println("Task executed every minute");

        MorrisonsImpl.getProductDetails();
        SainsburysImpl.getProductDetails();
        TescoImpl.getProductDetails();
        WaitRoseImpl.getProductDetails();
//        AmazonImpl.getProductDetails();
        AmazonTempImpl.getProductDetails();
        OcadoImpl.getProductDetails();
        CoOpImpl.getProductDetails();
        AsdaImpl.getProductDetails();

    }

//    @Scheduled(fixedRate = 10000) // Executes every 60,000 milliseconds (1 minute)
//    public void performTask() {
//        System.out.println("Task executed every minute");
//    }
}