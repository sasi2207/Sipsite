package com.uk.uk.implementation;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.uk.uk.controller.Controller;

import java.io.IOException;

@Component
public class PricingInsightsScheduler {

    private final Controller Controller;

    public PricingInsightsScheduler(com.uk.uk.controller.Controller controller) {
        Controller = controller;
    }

//    @Scheduled(cron = "0 8,21 18 * * *")
    /*
        0: At the start of the minute.
        0: At the start of the hour.
        2: At 2 AM (2nd hour of the day, 24-hour format).
        *: Every day of the month.
        *: Every month.
        *: Every day of the week.
    * */
@Scheduled(cron = "0 0 2 * * *")
    public void scheduleInsertPricingInsights() throws IOException, InterruptedException {
        Controller.insertPricingInsights();
    }
}
