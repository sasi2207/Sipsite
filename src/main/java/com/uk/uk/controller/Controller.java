package com.uk.uk.controller;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.uk.uk.entity.*;
import com.uk.uk.implementation.*;
import com.uk.uk.repository.AuthRepository;
import com.uk.uk.repository.TempUserRepo;
import com.uk.uk.repository.PriceRepo;
import jakarta.mail.MessagingException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.*;

import com.uk.uk.repository.PricingInsightsRepo;
import com.uk.uk.repository.ProductMasterDataRepo;

import com.uk.uk.implementation.CommonUtil;
import com.uk.uk.implementation.AmazonRobotImpl;
import com.uk.uk.implementation.ContactUsImpl;
//import com.uk.uk.config.FirefoxDriver;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.awt.event.InputEvent;

import io.github.bonigarcia.wdm.WebDriverManager;

//Manually Hit try
//import org.apache.http.HttpHeaders;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.ResponseHandler;
import org.apache.http.impl.client.BasicResponseHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.RequestHeader;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
//@CrossOrigin(origins = "*")

public class Controller {
    @Autowired
    private PricingInsightsRepo ProductInsightsRepo;
    @Autowired
    private ProductMasterDataRepo ProductMasterDataRepo;
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

    @Autowired
    private TescoTempImpl TescoTempImpl;

    @Autowired
    private CommonUtil CommonUtil;
    @Autowired
    private AmazonRobotImpl AmazonRobotImpl;
    @Autowired
    private PriceRepo PriceRepo;

    @Autowired
    EmailService mailService;

    @Autowired
    TempUserRepo TempRepo;

    @Autowired
    AuthRepository authRepository;

    @Autowired
    private ContactUsImpl ContactUsImpl;

//    @Autowired
//    private FirefoxDriver FirefoxDriver;

//    @Value("${sipsite.history.pricingInsights.tablePath}")
    private String historyPricingInsightsTablePath;

    @Value("${sipsite.driver.geckodriver.path}")
    private String geckoDriverPath;


    @GetMapping("/tt")
    public void tt() throws IOException {
        String link = "https://www.amazon.co.uk/Gordons-London-Award-winning-Botanical-Litre/dp/B01HZ6YAUW";
//        List<ProductDetail> products = new ArrayList<>(1000);

        scrapAmazonWrapper(link);

//        while (products.isEmpty())
//            scrapAmazonWrapper(link);
    }

    private static void scrapAmazonWrapper(String link) throws IOException {

//        List<Cookie> requestCookies = new ArrayList<>();
//        requestCookies.add(new Cookie.Builder()
//                .domain("https://www.amazon.co.uk/Gordons-London-Award-winning-Botanical-Litre/dp/B01HZ6YAUW")
//                .name("x-wl-uid")
//                .value("GB")
//                .build()); // Example: GB for United Kingdom


        OkHttpClient client = new OkHttpClient();
        String link2 = "https://www.amazon.co.uk/Gordons-London-Award-winning-Botanical-Litre/dp/B01HZ6YAUW?gl=GB";

        Request request = new Request.Builder().url(link2).build();


        try (Response response = client.newCall(request).execute()) {
            String html = response.body().string();

            // Parse the HTML content with Jsoup
            Document doc = Jsoup.parse(html);

            System.out.println("");
        }
    }


    @GetMapping("/insertPricingInsights")
    public Boolean
    insertPricingInsights() throws IOException, InterruptedException {

        Timestamp now = new Timestamp(System.currentTimeMillis());

        // Define a date format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateWithoutSpace = sdf.format(now);

        Boolean isExported = PricingInsightsImpl.exportDataToFile(historyPricingInsightsTablePath + dateWithoutSpace + ".txt");

        if (isExported) {
            ProductInsightsRepo.truncatePricingInsightsTable();
        }

        SainsburysImpl.getProductDetails(); //driver
        MorrisonsImpl.getProductDetails(); //driver
        TescoImpl.getProductDetails(); //driver
        WaitRoseImpl.getProductDetails();  //driver
        OcadoImpl.getProductDetails();  //Jsoup
        CoOpImpl.getProductDetails(); //driver
        AsdaImpl.getProductDetails(); //webAPI
        CommonUtil.killDriversProcessesWindows("Firefox");
        return true;
    }

    @GetMapping("/insertPricingInsightsByShopName")
    public Boolean insertPricingInsights(@RequestParam("shopName") String shopName) throws IOException, InterruptedException {
        switch (shopName) {
            case "Tesco":
                TescoImpl.getProductDetails();
                break;
            case "Morrisons":
                MorrisonsImpl.getProductDetails();
                break;
            case "Sainsburys":
                SainsburysImpl.getProductDetails();
                break;
            case "WaitRose":
                WaitRoseImpl.getProductDetails();
                break;
            case "Ocado":
                OcadoImpl.getProductDetails();
                break;
            case "CoOp":
                CoOpImpl.getProductDetails();
                break;
            case "ASDA":
                AsdaImpl.getProductDetails();
                break;
        }

//        CommonUtil.killDriversProcessesWindows("geckodriver");
        CommonUtil.killDriversProcessesWindows("Firefox");
        return true;
    }


    @GetMapping("/testFetchLogicByShopNameAndUrl")
    public Boolean testFetchLogicByShopNameAndUrl(@RequestParam("shopName") String shopName,
                                                  @RequestParam("url") String url) throws IOException, InterruptedException {

        switch (shopName) {
            case "Tesco":
//                WebDriver driver = FirefoxDriver.FirefoxWebDriver();
//                TescoImpl.fetchPricingInsights(url, driver);
//                driver.close();
                break;
            case "Morrisons":
                MorrisonsImpl.getProductDetails();
                break;
            case "Sainsburys":
                SainsburysImpl.getProductDetails();
                break;
            case "WaitRose":
                WaitRoseImpl.getProductDetails();
                break;
            case "Ocado":
                OcadoImpl.getProductDetails();
                break;
            case "CoOp":
                CoOpImpl.getProductDetails();
                break;
            case "ASDA":
                AsdaImpl.fetchPricingInsights(url);
                break;
        }

        return true;
    }

    @PostMapping("/insertProductMasterData")
    public ResponseEntity<Boolean> insertProductMasterData(@RequestBody List<ProductMasterDataDAO> ProductMasterDataDAOList, @RequestHeader("emailId") String emailId) throws IOException, InterruptedException {

        Boolean isAdminUser = CommonUtil.isAdminUser(emailId);
        if (isAdminUser) {
            ProductMasterDataImpl.insertProductMasterData(ProductMasterDataDAOList);
            return ResponseEntity.status(HttpStatus.OK).body(true);
        } else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
    }

    @GetMapping("/getGridData")
    public List<DashboardGridDataDAO> getUser() {
        List<DashboardGridDataDAO> gridDataList = new ArrayList<>();
        gridDataList = PricingInsightsImpl.getGridData();
        return gridDataList;
    }

    @GetMapping("/getProductMasterByTag")
    public List<ProductMasterDataDAO> getProductMasterByTag(@RequestParam("tag") Integer tag) {
        List<ProductMasterDataDAO> productMasterDataDAOList = new ArrayList<>();
        productMasterDataDAOList = PricingInsightsImpl.getProductMasterByTag(tag);
        return productMasterDataDAOList;
    }

    @PostMapping("/editProductMasterByTag")
    public ResponseEntity<Boolean> editProductMasterByTag(@RequestBody List<ProductMasterDataDAO> ProductMasterDataDAOList, @RequestHeader("emailId") String emailId) throws InterruptedException {
        Boolean isAdminUser = CommonUtil.isAdminUser(emailId);
        if (isAdminUser) {
            Boolean updateStatus = ProductMasterDataImpl.updateProductMasterByTag(ProductMasterDataDAOList);
            return ResponseEntity.status(HttpStatus.OK).body(updateStatus);
        } else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
    }

    @GetMapping("/hideProductByTag")
    public ResponseEntity<Boolean> hideProductByTag(@RequestParam("tag") Integer tag, @RequestHeader("emailId") String emailId) {

        Boolean isAdminUser = CommonUtil.isAdminUser(emailId);
        if (isAdminUser) {
            ProductMasterDataImpl.hideProductByTag(tag);
            PricingInsightsImpl.deletePricingInsightsByTag(tag);
            return ResponseEntity.status(HttpStatus.OK).body(true);
        } else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
    }

    @GetMapping("/getProductUrl")
    public ResponseEntity<String> getProductUrl(@RequestParam("tag") Integer tag, @RequestParam("shopName") String shopName) {
        ResponseEntity<String> getProductUrl = ProductMasterDataImpl.getProductUrlImpl(tag, shopName);
        return getProductUrl;
    }

    @GetMapping("/test")
    public Boolean test() throws IOException, InterruptedException {
//        AmazonTempImpl.getProductDetails();
        SainsburysImpl.test();
        return true;
    }

    @GetMapping("/message")
    public String message() throws IOException, InterruptedException {
        TescoTempImpl.insertPricingInsights();
        return "TEST MESSAGE";
    }

    @GetMapping("/TestEmail")
    public void sendSimpleEmail(String to, String subject, String text) throws MessagingException {

        mailService.sendSimpleEmail("jailanijohn@gmail.com", "TEST SUBJECT", "TEST MESSAGE");
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(to);
//        message.setSubject(subject);
//        message.setText(text);
//        message.setFrom("your_email@example.com");
//
//        mailSender.send(message);
    }

    @GetMapping("/isRegisteredUser")
    public Boolean isRegisteredUser(@RequestParam("emailId") String emailId) {
        AuthEntity userDetails = authRepository.findByUserEmail(emailId);
        return null != userDetails ? true : false;
    }

    @GetMapping("/checkPointerPosition")
    public void checkPointerPosition(@RequestParam("x") Integer xValue, @RequestParam("y") Integer yValue) throws AWTException {
        System.setProperty("java.awt.headless", "false");

        // Create a Robot instance
        Robot robot = new Robot();

        // Delay to allow the action to be visible (in milliseconds)
        robot.delay(2000);

        //600,848
        robot.mouseMove(xValue, yValue); // Move the mouse to the location
        robot.delay(1000); // Wait for 1 second

    }

    @GetMapping("/click-browser")
    public String clickOnBrowser(@RequestParam("x") Integer xValue, @RequestParam("y") Integer yValue, @RequestParam("url") String url) {
        try {

            System.setProperty("java.awt.headless", "false");


            // Create a Robot instance
            Robot robot = new Robot();

            // Delay to allow the action to be visible (in milliseconds)
            robot.delay(2000);

            // Move the mouse to the coordinates of the browser icon on the taskbar
            int x = xValue;  // Adjust this value to the X coordinate of the browser icon
            int y = yValue; // Adjust this value to the Y coordinate of the taskbar (based on screen resolution)

            //600,848
            robot.mouseMove(600, 848); // Move the mouse to the location
            robot.delay(1000); // Wait for 1 second


            // Perform a left mouse click
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

            //wait to open firebox browser
            robot.delay(2000);

            //click on browser's search bar
            robot.mouseMove(330, 65); // Move the mouse to the location
            robot.delay(1000); // Wait for 1 second

            // Perform a left mouse click
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

            // Type "Hello"
//            typeText(robot, "https://www.amazon.co.uk/Gordons-London-Award-winning-Botanical-Litre/dp/B01HZ6YAUW");
            typeText(robot, url.split("//")[1]);

            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);

            //Wait until page loads
            robot.delay(3000);

            /*
             -- To change location - START
            //click on deliver to button
            robot.mouseMove(220, 120); // Move the mouse to the location
            robot.delay(1000); // Wait for 1 second

            // Perform a left mouse click
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);


            //click to type PINCODE
            robot.mouseMove(650, 470); // Move the mouse to the location
            robot.delay(1000); // Wait for 1 second

            // Perform a left mouse click
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

            robot.keyPress(KeyEvent.VK_CONTROL);  // Hold Control key
            robot.keyPress(KeyEvent.VK_A);        // Press A key
            robot.keyRelease(KeyEvent.VK_A);      // Release A key
            robot.keyRelease(KeyEvent.VK_CONTROL);// Release Control key

            // Delay to ensure text is selected
            robot.delay(500);

            // 5. Clear the text by pressing the Delete or Backspace key
            robot.keyPress(KeyEvent.VK_DELETE);   // Press Delete key (use KeyEvent.VK_BACK_SPACE if needed)
            robot.keyRelease(KeyEvent.VK_DELETE); // Release Delete key

            // Wait for the delete action to complete
            robot.delay(500);

            typeText(robot, "W1A 1AA");

            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);

            //click to continue
            robot.mouseMove(xValue, yValue); // Move the mouse to the location
            robot.delay(1000); // Wait for 1 second

            -- To change location - END
            */


            //click on deliver to button
            robot.mouseMove(220, 120); // Move the mouse to the location
            robot.delay(1000); // Wait for 1 second


            // Perform a right mouse click
            robot.mousePress(InputEvent.BUTTON3_DOWN_MASK); // Press the right mouse button
            robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK); // Release the right mouse button

            //click on Inspect option
            robot.mouseMove(400, 490); // Move the mouse to the location
            robot.delay(1000); // Wait for 1 second

            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

            //click on Inspected Code
            robot.mouseMove(1100, 210); // Move the mouse to the location
            robot.delay(1000); // Wait for 1 second

            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

            robot.delay(5000); // Wait for 1 second

            robot.keyPress(KeyEvent.VK_CONTROL);  // Hold Control key
            robot.keyPress(KeyEvent.VK_F);        // Press A key
            robot.keyRelease(KeyEvent.VK_F);      // Release A key
            robot.keyRelease(KeyEvent.VK_CONTROL);// Release Control key

            robot.delay(2000); // Wait for 1 second
            System.out.println("**");
            typeText(robot, "a-section a-spacing-none aok-align-center aok-relative");

            robot.delay(2000); // Wait for 1 second

            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);


            //click on Inspected Code
            robot.mouseMove(1100, 235); // Move the mouse to the location
            robot.delay(1000); // Wait for 1 second

            // Perform a right mouse click
            robot.mousePress(InputEvent.BUTTON3_DOWN_MASK); // Press the right mouse button
            robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK); // Release the right mouse button


            //click on Copy Option
            robot.mouseMove(1180, 650); // Move the mouse to the location
            robot.delay(1000); // Wait for 1 second

            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

            robot.delay(1000); // Wait for 1 second

            //click on Inner HTML Option
            robot.mouseMove(1320, 650); // Move the mouse to the location
            robot.delay(1000); // Wait for 1 second

            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

            robot.delay(1000); // Wait for 1 second

            // Get the system clipboard
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

            try {
                // Get the content from the clipboard
                Transferable data = clipboard.getContents(null);

                // Check if the clipboard contains text
                if (data != null && data.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                    // Get the text from the clipboard
                    String text = (String) data.getTransferData(DataFlavor.stringFlavor);

                    // Parse the HTML
                    Document document = Jsoup.parse(text);

                    // Select elements by class name
                    Elements elements = document.getElementsByClass("aok-offscreen");

                    // Print the values
                    Integer idx = 0;
                    String priceString = "";
                    for (Element element : elements) {

                        if (idx == 0) {
                            System.out.println("Extracted Value: " + element.text());
                            if (element.text().contains("with")) {
                                String[] sp = element.text().split(" with");
                                priceString = sp[0].replaceAll("\\s+", "");
                            } else
                                priceString = element.text().replaceAll("\\s+", "");
                        }
                        idx++;
                    }


                    if (priceString.contains("£")) {
                        String[] priceDouble = priceString.split("£");
                        priceString = priceDouble[1];
                    }

                    System.out.println("PRICE X: " + priceString);

                } else {
                    System.out.println("No text data found on the clipboard.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            robot.keyPress(KeyEvent.VK_CONTROL);  // Hold Control key
            robot.keyPress(KeyEvent.VK_F);        // Press A key
            robot.keyRelease(KeyEvent.VK_F);      // Release A key
            robot.keyRelease(KeyEvent.VK_CONTROL);// Release Control key

            robot.delay(2000); // Wait for 1 second

            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_A);
            robot.keyRelease(KeyEvent.VK_A);
            robot.keyRelease(KeyEvent.VK_CONTROL);

//            typeText(robot, "a-dynamic-image a-stretch-vertical");
            typeText(robot, "a-dynamic-image a-stretch-vertical");

            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);

            //click on Inspected Code
            robot.mouseMove(1100, 235); // Move the mouse to the location
            robot.delay(1000); // Wait for 1 second

            // Perform a right mouse click
            robot.mousePress(InputEvent.BUTTON3_DOWN_MASK); // Press the right mouse button
            robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK); // Release the right mouse button


            //click on Copy Option
            robot.mouseMove(1180, 650); // Move the mouse to the location
            robot.delay(1000); // Wait for 1 second

            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

            robot.delay(1000); // Wait for 1 second

            //click on Inner HTML Option
            robot.mouseMove(1320, 665); // Move the mouse to the location
            robot.delay(1000); // Wait for 1 second

            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

            robot.delay(1000); // Wait for 1 second

            // Get the system clipboard
            clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

            // Get the clipboard contents as a Transferable object
            Transferable contents = clipboard.getContents(null);

            // Check if the clipboard has text data
            if (contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                // Get the clipboard data as a string and print it
                String clipboardText = (String) contents.getTransferData(DataFlavor.stringFlavor);

                // Parse the HTML content
                Document document = Jsoup.parse(clipboardText);
                Element imgElement = document.select("img").first();

                if (imgElement != null) {
                    String src = imgElement.attr("src");
                    System.out.println("Image source: " + src);
                } else {
                    System.out.println("No image found!");
                }

            } else {
                System.out.println("Clipboard does not contain text data.");
            }

            //wait to open firebox browser
            robot.delay(2000);

            //click on browser's search bar
            robot.mouseMove(1510, 10); // Move the mouse to the location
            robot.delay(1000); // Wait for 1 second

            // Perform a left mouse click
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

            // Return success message
            return "Browser icon clicked successfully!";
        } catch (
                AWTException e) {
            e.printStackTrace();
            return "Failed to move the mouse or click due to: " + e.getMessage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedFlavorException e) {
            throw new RuntimeException(e);
        }
    }

    // Helper method to type text character by character
    public static void typeText(Robot robot, String text) {
        for (char c : text.toCharArray()) {
            // Convert character to uppercase if needed and get corresponding KeyEvent
            int keyCode = KeyEvent.getExtendedKeyCodeForChar(c);
            if (KeyEvent.CHAR_UNDEFINED == keyCode) {
                throw new IllegalArgumentException("Cannot type character: " + c);
            }

            // Handle lowercase and uppercase letters
            if (Character.isUpperCase(c)) {
                robot.keyPress(KeyEvent.VK_SHIFT); // Hold Shift for uppercase
                robot.keyPress(keyCode);
                robot.keyRelease(keyCode);
                robot.keyRelease(KeyEvent.VK_SHIFT); // Release Shift
            } else {
                robot.keyPress(keyCode);
                robot.keyRelease(keyCode);
            }

            // Add small delay between keystrokes for better simulation
//            robot.delay(500);


        }
    }


    @GetMapping("/sendEmail")
    public ResponseEntity<String> sendEmail(@RequestParam String to, @RequestParam String password) throws MessagingException {
//        mailService.sendSimpleEmail(to, subject, text);

        String otp = generateOTP(6);
        String subject = "Your OTP Code";
        String text = "Hello " + to + ", " + "Your One-Time Password (OTP) code is: [" + otp + "] " + "Please do not share this OTP with anyone for security reasons. " + "Thanks.";
        Boolean alreadyExist = PersonValidation(to);
        if (alreadyExist) {
            return new ResponseEntity<>("already_exist", HttpStatus.OK);
        } else {
            mailService.sendSimpleEmail(to, subject, text);
        }
        List<TempUser> user = TempRepo.findByUserEmail(to);
        if (user.isEmpty()) {
            TempUser user1 = new TempUser();
            user1.setOtp(otp);
            user1.setUserEmail(to);
            user1.setPassword(password);
            TempRepo.save(user1);
        } else {
            List<TempUser> user2 = TempRepo.findByUserEmail(to);
            user2.get(0).setOtp(otp);
            user2.get(0).setPassword(password);
            TempRepo.save(user2.get(0));
        }
        return new ResponseEntity<>("success", HttpStatus.OK);
    }


    public static String generateOTP(int length) {
        if (length < 1 || length > 9) {
            throw new IllegalArgumentException("OTP length must be between 1 and 9 digits.");
        }
        // Generate a random OTP with the specified length
        StringBuilder otp = new StringBuilder();
        Random random = new Random();

        for (int i = 1; i <= length; i++) {
            int digit = random.nextInt(10); // Generates a random digit between 0 and 9
            otp.append(digit);
        }

        return otp.toString();
    }

    @GetMapping("/Verification")
    public boolean Otpverification(@RequestParam String to, @RequestParam String otp) {
        System.out.println("Hello");
        Iterable<TempUser> user = TempRepo.findByUserEmail(to);
        var ref = new Object() {
            boolean isvalid = false;
        };
        user.forEach(x -> {
            if (x.getUserEmail().equals(to) && x.getOtp().equals(otp)) {
                ref.isvalid = true;
                AuthEntity insertUser = new AuthEntity();
                insertUser.setUsername(x.getUsername());
                insertUser.setUserEmail(x.getUserEmail());
                insertUser.setPassword(x.getPassword());
                authRepository.save(insertUser);
                TempRepo.deleteById(x.getId());
            }
        });
        return ref.isvalid;
    }

    @GetMapping("/Personvalidation")
    public boolean PersonValidation(@RequestParam String to) {
        System.out.println("Hello");
        List<AuthEntity> user = (List<AuthEntity>) authRepository.findAll();
        return user.stream().anyMatch(x -> x.getUserEmail().equals(to));
    }

    @GetMapping("/Allowuserlogin")
    public String Allowuserlogin(@RequestParam String to, @RequestParam String password) {
        List<AuthEntity> user = (List<AuthEntity>) authRepository.findAll();
        if (user == null || user.size() == 0) {
            return "no_user";
        }
        AuthEntity userDetails = user.stream().filter(obj -> obj.getUserEmail().equalsIgnoreCase(to) && obj.getPassword().equalsIgnoreCase(password)).findFirst().get();

        if (null != userDetails && null != userDetails.getUserEmail()) {

            return "success$@$" + userDetails.getUserPrivilege();
        } else return "";


//        return user.stream().anyMatch(x -> x.getUserEmail().equals(to) && x.getPassword().equals(password)) ? "success" : "";
    }

    @GetMapping("/Updatepassword")
    public String Updatepassword(@RequestParam String name, @RequestParam String password) {
        AuthEntity user = authRepository.findByUserEmail(name);
        user.setPassword(password);
        user.setTempOtp(null);
        authRepository.save(user);
        return "success$@$" + user.getUserPrivilege();
    }

    @GetMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestParam String to) throws MessagingException {
//        mailService.sendSimpleEmail(to, subject, text)
        String otp = generateOTP(6);
        String subject = "Your OTP Code";
        String text = "Hello " + to + ", " + "Your One-Time Password (OTP) code is: [" + otp + "] " + "Please do not share this OTP with anyone for security reasons. " + "Thanks.";
        mailService.sendSimpleEmail(to, subject, text);
        AuthEntity user = authRepository.findByUserEmail(to);
        user.setTempOtp(otp);
        authRepository.save(user);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @GetMapping("/reVerification")
    public boolean ReOtpverification(@RequestParam String to, @RequestParam String otp) {
        System.out.println("Hello");
        AuthEntity user = authRepository.findByUserEmail(to);
        boolean valid = false;
        if (user.getUserEmail().equals(to) && user.getTempOtp().equals(otp)) {
            valid = true;
        }
        return valid;
    }

    @PostMapping("/checkProductUrlIsExistsAlready")
    public String checkProductUrlIsExistsAlready(@RequestBody List<ShopNameWithUrlDAO> ShopNameWithUrlList) {
        String shopNames = "";
        shopNames = ProductMasterDataImpl.checkProductUrlIsExistsAlready(ShopNameWithUrlList);
        return shopNames;
    }

    @PostMapping("/contactUs")
    public ResponseEntity<Boolean> contactUs(@RequestBody ContactUsDAO contactUsDAORequest) {
        ContactUsImpl.contactUsSendEmail(contactUsDAORequest);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @GetMapping("/sample/getProductsByShop")
    public ResponseEntity<List<PriceDAO>> getProductsByShop(@RequestParam String shopName) {
        List<PriceDAO> priceDAOS = new ArrayList<>();
        priceDAOS = PriceRepo.getAllByShopName(shopName);
        return new ResponseEntity<>(priceDAOS, HttpStatus.OK);
    }

    @PostMapping("/sample/postTescoUrl")
    public ResponseEntity<Boolean> postTescoUrl(@RequestParam String url, @RequestParam String shopName) throws InterruptedException {

        // Automatically download the latest compatible ChromeDriver
//        WebDriverManager.chromedriver().setup();
        CommonUtil.killDriversProcessesWindows("geckodriver32");
        System.out.println("##### - 10");
        System.setProperty("webdriver.gecko.driver", geckoDriverPath);
        System.out.println("##### - 20 " + geckoDriverPath);

        // Create a new Firefox profile
        FirefoxProfile profile = new FirefoxProfile();
        System.out.println("##### - 30");

        // Set the custom User-Agent string
        profile.setPreference("general.useragent.override", "--user-agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");
        System.out.println("##### - 40");

        FirefoxOptions options = new FirefoxOptions();
//        options.setProfile(profile);
        System.out.println("##### - 50");
        options.addArguments("-headless");
//        options.addArguments("--verbose"); // Enable verbose logging
//        options.addArguments("--disable-gpu"); // Disable GPU hardware acceleration
//        options.addArguments("--start-maximized"); // Start maximized
//        options.addArguments("--disable-infobars"); // Disable infobars
        System.out.println("##### - 60");

        WebDriver driver = new FirefoxDriver(options);
        System.out.println("##### - 70");

//        WebDriver driver new
        Boolean status = false;
        switch (shopName) {
            case "Tesco":
                status = TescoImpl.fetchPricingInsights(url, driver);
                break;
            case "WaitRose":
                status = WaitRoseImpl.fetchPricingInsights(url, driver);
                break;
            case "Ocado":
                status = OcadoImpl.fetchPricingInsights(url, driver);
                break;
            case "Sainsbury":
                status = SainsburysImpl.fetchPricingInsights(url, driver);
                break;
            case "Morrison":
                status = MorrisonsImpl.fetchPricingInsights(url, driver);
                break;
        }
//        CommonUtil.killDriversProcessesWindows("geckodriver32");
        driver.close();
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @GetMapping("/playwright")
    public String playwright() throws IOException {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.firefox().launch();
            Page page = browser.newPage();
            page.navigate("https://www.tesco.com/groceries/en-GB/products/256563839");
            String title = page.title();
            System.out.println(page.title());
            browser.close();
            return title;
        }
    }

    @GetMapping("/test2")
    public String test2() throws IOException {
        try {
            // Increase timeout to 60 seconds (60000 ms)
            Document document = Jsoup.connect("https://www.tesco.com/groceries/en-GB/products/256563839")
                    .timeout(10000) // 10 seconds timeout
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                    .header("Accept-Language", "en-US,en;q=0.9")
                    .header("Accept-Encoding", "gzip, deflate, br")
                    .header("Connection", "keep-alive")
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                    .get();

            // Return the HTML content as a string
            return document.html(); // Returns the HTML of the webpage

        } catch (IOException e) {
            // Handle exceptions and return error message
            return "ERROR: " + e.getMessage();
        }
    }

    @GetMapping("/fetch-product")
    public String fetchProduct(
            @RequestHeader HttpHeaders headers,
            @CookieValue(value = "sessionid", defaultValue = "") String sessionidCookie,
            HttpServletRequest request,
            HttpServletResponse response) {

        // Capture incoming cookies from the request
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                // Optionally, set a cookie in the response (e.g., session cookie)
                if (cookie.getName().equals("sessionid")) {
                    Cookie newCookie = new Cookie("sessionid", cookie.getValue());
                    newCookie.setPath("/");  // Ensure it's sent to the correct path
                    newCookie.setHttpOnly(true);  // Make it HTTP only for security
                    newCookie.setMaxAge(60 * 60);  // Set expiration (1 hour for example)
                    response.addCookie(newCookie);
                }
            }
        }

        // Make the request to Amazon with the session cookies
        return fetchProductDetails(sessionidCookie);
    }

    // Service method to make the outbound request to Amazon
    public String fetchProductDetails(String sessionidCookie) {
        // Use RestTemplate for making the request
        RestTemplate restTemplate = new RestTemplate();

        // Prepare headers to simulate a browser request
        HttpHeaders outgoingHeaders = new HttpHeaders();
        outgoingHeaders.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
        outgoingHeaders.set("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        outgoingHeaders.set("Accept-Encoding", "gzip, deflate, br");
        outgoingHeaders.set("Accept-Language", "en-US,en;q=0.5");
        outgoingHeaders.set("Connection", "keep-alive");
        outgoingHeaders.set("Upgrade-Insecure-Requests", "1");

        // If the session cookie is available, add it to the request headers
        if (sessionidCookie != null && !sessionidCookie.isEmpty()) {
            outgoingHeaders.set("Cookie", "sessionid=" + sessionidCookie);
        }

        // Prepare the HttpEntity to make the GET request
        HttpEntity<String> entity = new HttpEntity<>(outgoingHeaders);

        // Use RestTemplate to perform the GET request to Amazon
        ResponseEntity<String> response = restTemplate.exchange("https://www.waitrose.com/ecom/products/bacardi-carta-blanca-superior-white-rum/063302-32157-32158", org.springframework.http.HttpMethod.GET, entity, String.class);

        // Return the response body (HTML content from Amazon)
        return response.getBody();
    }
}
