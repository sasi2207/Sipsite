package com.uk.uk.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.springframework.stereotype.Service;

@Service
public class FirefoxDriver {

    public WebDriver FirefoxWebDriver(){
        // Create a new Firefox profile
        FirefoxProfile profile = new FirefoxProfile();

        // Set the custom User-Agent string
        profile.setPreference("general.useragent.override", "--user-agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");

        // Set up FirefoxOptions for headless mode
        FirefoxOptions options = new FirefoxOptions();
        options.setProfile(profile);
        options.addArguments("-headless");

        // Open the Firefox Driver
        WebDriver driver = new org.openqa.selenium.firefox.FirefoxDriver(options);

        return driver;
    }
}
