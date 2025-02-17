package com.uk.uk.implementation;

import com.uk.uk.entity.ProductMasterDataDAO;
import com.uk.uk.repository.PricingInsightsRepo;
import com.uk.uk.repository.ProductMasterDataRepo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.uk.uk.implementation.MouseControl;

@Service
public class AmazonRobotImpl {

    //    public String AmazonRobotInsert(@RequestParam("x") Integer xValue, @RequestParam("y") Integer yValue, @RequestParam("url") String url) {
    @Autowired
    private PricingInsightsRepo productInsightsRepo;

    @Autowired
    private ProductMasterDataRepo productMasterDataRepo;

    @Autowired
    private MouseControl MouseControl;

    public void getProductDetails() throws IOException, InterruptedException {
        List<ProductMasterDataDAO> productMasterDataList = productMasterDataRepo.getProductMasterDataByShopName("Amazon");
        for (ProductMasterDataDAO productMasterData : productMasterDataList) {
            AmazonRobotInsert(productMasterData);
        }
    }

    public String AmazonRobotInsert(ProductMasterDataDAO productMasterData) {
        try {

            // Current Timestamp
            Timestamp now = new Timestamp(System.currentTimeMillis());

            System.setProperty("java.awt.headless", "false");


            // Create a Robot instance
            Robot robot = new Robot();

            robot.delay(5000);

//            Runtime.getRuntime().exec("powershell -Command Start-Process 'C:\\Windows\\System32\\cmd.exe' -ArgumentList '/c echo hello'");
            String url = productMasterData.getUrl().split("//")[1];

            try {
                // Path to your PowerShell script
                String scriptPath = "D:\\Pixmonks Backup\\Uk\\Git\\script.ps1";  // Update with actual script path

                // Construct the PowerShell command
                String command = "powershell.exe -ExecutionPolicy Bypass -File \"" + scriptPath + "\" \"" + url + "\"";

                // Execute the command
                Process process = Runtime.getRuntime().exec(command);

                // Read the output from the PowerShell script
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);  // Print the output (or handle it)
                }

                // Wait for the process to exit
                process.waitFor();
            } catch (Exception e) {
                e.printStackTrace();
            }


//            try {
//                // Command to open Firefox
//                // String command = "cmd /c start firefox";
//                String url = productMasterData.getUrl().split("//")[1];
//                //view-source:
//                url = "view-source:" + url;
//                String command = "cmd /c start firefox \"" + url + "\"";
//
//                // Execute the command
//                Process process = Runtime.getRuntime().exec(command);
//                process.waitFor();
//
//                // Wait for a few seconds to ensure the page loads (adjust as needed)
//                Thread.sleep(5000);
//
//                // Get screen dimensions
//                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//                int screenWidth = screenSize.width;
//                int screenHeight = screenSize.height;
//
//                // Calculate the center of the screen
//                int centerX = screenWidth / 2;
//                int centerY = screenHeight / 2;
//
//                // Simulate a mouse click at the center of the screen
//                Robot robotNew = new Robot();
//                robotNew.mouseMove(centerX, centerY); // Move the mouse to the center
//                robotNew.mousePress(java.awt.event.InputEvent.BUTTON1_DOWN_MASK); // Press the left mouse button
//                robotNew.mouseRelease(java.awt.event.InputEvent.BUTTON1_DOWN_MASK); // Release the left mouse button
//
//
//            } catch (IOException | InterruptedException e) {
//                e.printStackTrace();
//                return "Failed to open Firefox: " + e.getMessage();
//            }


            // Open a browser using Run dialog (Windows + R)
//            robot.keyPress(KeyEvent.VK_WINDOWS);
//            robot.keyPress(KeyEvent.VK_R);
//            robot.keyRelease(KeyEvent.VK_R);
//            robot.keyRelease(KeyEvent.VK_WINDOWS);


            robot.delay(10000);

//            MouseControl.moveMouseToCenterAndClick();
            // Type the name of the browser ("firefox")
//            typeText(robot, "firefox");


            // Press Enter to open the browser

//            robot.keyPress(KeyEvent.VK_ENTER);
//            robot.keyRelease(KeyEvent.VK_ENTER);

//            robot.delay(10000); // Wait for the browser to open

//            String url = productMasterData.getUrl().split("//")[1];
//            System.out.println("%%  START URL %%");
//            typeText(robot, url);
//            System.out.println("%%  END URL %%");
            // Press Enter to navigate to the URL

//            System.out.println("%%  BEFORE ENTER %%");
//            robot.keyPress(KeyEvent.VK_ENTER);
//            robot.keyRelease(KeyEvent.VK_ENTER);
//            System.out.println("%%  AFTER ENTER %%");
//            // Wait for the page to load
//            robot.delay(10000);  // Adjust as per the website load time

//            System.out.println("%%  BEFORE CTRL + U %%");
//            // Simulate pressing Ctrl + U to open the page source
//            robot.keyPress(KeyEvent.VK_CONTROL);
//            robot.keyPress(KeyEvent.VK_U);
//            robot.keyRelease(KeyEvent.VK_U);
//            robot.keyRelease(KeyEvent.VK_CONTROL);
//            System.out.println("%%  AFTER CTRL + U %%");

            // Wait for the source code to appear
//            robot.delay(10000);


            System.out.println("%%  BEFORE CTRL + A %%");
            // Simulate pressing Ctrl + A to select all text
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_A);
            robot.keyRelease(KeyEvent.VK_A);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            System.out.println("%%  AFTER CTRL + A %%");

            System.out.println("%%  BEFORE CTRL + C %%");
            // Simulate pressing Ctrl + C to copy the selected text
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_C);
            robot.keyRelease(KeyEvent.VK_C);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            System.out.println("%%  AFTER CTRL + C %%");

            // Wait for the copy operation to complete
            robot.delay(2000);

            System.out.println("%%  BEFORE PASTE CODE %%");
            // Retrieve the copied text (HTML source code) from the clipboard
            String htmlSource = getClipboardText();
            System.out.println("HTML Source Code:\n" + htmlSource);

            // Parse the HTML
            Document document = Jsoup.parse(htmlSource);

            // Select elements by class name
//            String wholePrice = document.getElementsByClass("a-price-whole").get(0).text();
//            String fractionPrice = document.getElementsByClass("a-price-fraction").get(0).text();

            //document.getElementsByClass("a-price aok-align-center").get(0).getElementsByClass("a-offscreen").get(0).text()

            String finalPrice = "";

//            if (fractionPrice.equalsIgnoreCase("00"))
//                finalPrice = wholePrice.replace(".", "");
//            else
//                finalPrice = wholePrice + fractionPrice;

            finalPrice = document.getElementsByClass("a-offscreen").get(0).text().split("£")[1];
//            finalPrice = document.getElementsByClass("a-price aok-align-center").get(0).getElementsByClass("a-offscreen").get(0).text().split("£")[1];
            String imgUrl = document.getElementById("landingImage").attr("src");

            // Close the browser using Alt + F4 for Windows/Linux
            closeBrowser(robot);


            System.out.println(imgUrl + "  ** END **  " + finalPrice);

//            productInsightsRepo.insertPricingInsights(productMasterData.getNo(), productMasterData.getTag(), productMasterData.getShopName(),
//                    Double.valueOf(finalPrice), productMasterData.getUrl(), true, now, imgUrl);


//            -------------------------------------------------------


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
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // Helper method to type text character by character
//    public static void typeText(Robot robot, String text) {
//
//        String tets = "";
//        System.out.println("URL >>" + text);
//        for (char c : text.toCharArray()) {
//            // Convert character to uppercase if needed and get corresponding KeyEvent
//            int keyCode = KeyEvent.getExtendedKeyCodeForChar(c);
//            if (KeyEvent.CHAR_UNDEFINED == keyCode) {
//                throw new IllegalArgumentException("Cannot type character: " + c);
//            }
//
//            // Handle lowercase and uppercase letters
//            if (Character.isUpperCase(c)) {
//                robot.keyPress(KeyEvent.VK_SHIFT); // Hold Shift for uppercase
//                robot.keyPress(keyCode);
//                robot.keyRelease(keyCode);
//                robot.keyRelease(KeyEvent.VK_SHIFT); // Release Shift
//            } else {
//                robot.keyPress(keyCode);
//                robot.keyRelease(keyCode);
//            }
//
//            System.out.println("CURRENT CHAR >> " + c);
//
//            tets = tets + c;
//
//            System.out.println("TOTAL CHAR >> " + tets);
//
//
//            // Add small delay between keystrokes for better simulation
////            robot.delay(500);
//
//
//        }
//
//        System.out.println("%%  LEAVE %%");
////        System.out.println(tets +" --------------- $$");
//    }

    public static void typeText(Robot robot, String text) {
        String tets = "";
        System.out.println("URL >>" + text);
        for (char c : text.toCharArray()) {
            // Convert character to uppercase if needed and get corresponding KeyEvent
            int keyCode = KeyEvent.getExtendedKeyCodeForChar(c);

            if (keyCode == KeyEvent.CHAR_UNDEFINED) {
                // If the character is not mapped, handle it manually (for special characters)
                if (c == '+') { // Requires Shift + =
                    keyCode = KeyEvent.VK_EQUALS;
                    robot.keyPress(KeyEvent.VK_SHIFT);
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                    robot.keyRelease(KeyEvent.VK_SHIFT);
                } else if (c == '-') { // Just the '-' key
                    keyCode = KeyEvent.VK_MINUS;
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                } else if (c == ':') { // Requires Shift + ;
                    keyCode = KeyEvent.VK_SEMICOLON;
                    robot.keyPress(KeyEvent.VK_SHIFT);
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                    robot.keyRelease(KeyEvent.VK_SHIFT);
                } else if (c == ';') { // Just the ';' key
                    keyCode = KeyEvent.VK_SEMICOLON;
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                } else if (c == ',') { // Just the ',' key
                    keyCode = KeyEvent.VK_COMMA;
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                } else if (c == '<') { // Requires Shift + ,
                    keyCode = KeyEvent.VK_COMMA;
                    robot.keyPress(KeyEvent.VK_SHIFT);
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                    robot.keyRelease(KeyEvent.VK_SHIFT);
                } else if (c == '>') { // Requires Shift + .
                    keyCode = KeyEvent.VK_PERIOD;
                    robot.keyPress(KeyEvent.VK_SHIFT);
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                    robot.keyRelease(KeyEvent.VK_SHIFT);
                } else if (c == '.') { // Just the '.' key
                    keyCode = KeyEvent.VK_PERIOD;
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                } else if (c == '/') { // Just the '/' key
                    keyCode = KeyEvent.VK_SLASH;
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                } else if (c == '\\') { // Just the '\' key
                    keyCode = KeyEvent.VK_BACK_SLASH;
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                } else if (c == '`') { // Just the '`' key
                    keyCode = KeyEvent.VK_BACK_QUOTE;
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                } else if (c == '\'') { // Just the '\'' key
                    keyCode = KeyEvent.VK_QUOTE;
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                } else if (c == '"') { // Requires Shift + '
                    keyCode = KeyEvent.VK_QUOTE;
                    robot.keyPress(KeyEvent.VK_SHIFT);
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                    robot.keyRelease(KeyEvent.VK_SHIFT);
                } else if (c == '?') { // Requires Shift + /
                    keyCode = KeyEvent.VK_SLASH;
                    robot.keyPress(KeyEvent.VK_SHIFT);
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                    robot.keyRelease(KeyEvent.VK_SHIFT);
                } else if (c == '=') { // Just the '=' key
                    keyCode = KeyEvent.VK_EQUALS;
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                } else if (c == '!') { // Requires Shift + 1
                    keyCode = KeyEvent.VK_1;
                    robot.keyPress(KeyEvent.VK_SHIFT);
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                    robot.keyRelease(KeyEvent.VK_SHIFT);
                } else if (c == '@') { // Requires Shift + 2
                    keyCode = KeyEvent.VK_2;
                    robot.keyPress(KeyEvent.VK_SHIFT);
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                    robot.keyRelease(KeyEvent.VK_SHIFT);
                } else if (c == '#') { // Requires Shift + 3
                    keyCode = KeyEvent.VK_3;
                    robot.keyPress(KeyEvent.VK_SHIFT);
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                    robot.keyRelease(KeyEvent.VK_SHIFT);
                } else if (c == '$') { // Requires Shift + 4
                    keyCode = KeyEvent.VK_4;
                    robot.keyPress(KeyEvent.VK_SHIFT);
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                    robot.keyRelease(KeyEvent.VK_SHIFT);
                } else if (c == '%') { // Requires Shift + 5
                    keyCode = KeyEvent.VK_5;
                    robot.keyPress(KeyEvent.VK_SHIFT);
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                    robot.keyRelease(KeyEvent.VK_SHIFT);
                } else if (c == '^') { // Requires Shift + 6
                    keyCode = KeyEvent.VK_6;
                    robot.keyPress(KeyEvent.VK_SHIFT);
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                    robot.keyRelease(KeyEvent.VK_SHIFT);
                } else if (c == '&') { // Requires Shift + 7
                    keyCode = KeyEvent.VK_7;
                    robot.keyPress(KeyEvent.VK_SHIFT);
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                    robot.keyRelease(KeyEvent.VK_SHIFT);
                } else if (c == '*') { // Requires Shift + 8
                    keyCode = KeyEvent.VK_8;
                    robot.keyPress(KeyEvent.VK_SHIFT);
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                    robot.keyRelease(KeyEvent.VK_SHIFT);
                } else if (c == '(') { // Requires Shift + 9
                    keyCode = KeyEvent.VK_9;
                    robot.keyPress(KeyEvent.VK_SHIFT);
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                    robot.keyRelease(KeyEvent.VK_SHIFT);
                } else if (c == ')') { // Requires Shift + 0
                    keyCode = KeyEvent.VK_0;
                    robot.keyPress(KeyEvent.VK_SHIFT);
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                    robot.keyRelease(KeyEvent.VK_SHIFT);
                } else if (c == '_') { // Requires Shift + -
                    keyCode = KeyEvent.VK_MINUS;
                    robot.keyPress(KeyEvent.VK_SHIFT);
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                    robot.keyRelease(KeyEvent.VK_SHIFT);
                } else {
                    // For other characters, you can throw an error or handle them as needed
                    throw new IllegalArgumentException("Cannot type character: " + c);
                }
            } else {

                if (c == '+') { // Requires Shift + =
                    keyCode = KeyEvent.VK_EQUALS;
                    robot.keyPress(KeyEvent.VK_SHIFT);
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                    robot.keyRelease(KeyEvent.VK_SHIFT);
                } else if (c == '-') { // Just the '-' key
                    keyCode = KeyEvent.VK_MINUS;
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                } else if (c == ':') { // Requires Shift + ;
                    keyCode = KeyEvent.VK_SEMICOLON;
                    robot.keyPress(KeyEvent.VK_SHIFT);
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                    robot.keyRelease(KeyEvent.VK_SHIFT);
                } else if (c == ';') { // Just the ';' key
                    keyCode = KeyEvent.VK_SEMICOLON;
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                } else if (c == ',') { // Just the ',' key
                    keyCode = KeyEvent.VK_COMMA;
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                } else if (c == '<') { // Requires Shift + ,
                    keyCode = KeyEvent.VK_COMMA;
                    robot.keyPress(KeyEvent.VK_SHIFT);
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                    robot.keyRelease(KeyEvent.VK_SHIFT);
                } else if (c == '>') { // Requires Shift + .
                    keyCode = KeyEvent.VK_PERIOD;
                    robot.keyPress(KeyEvent.VK_SHIFT);
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                    robot.keyRelease(KeyEvent.VK_SHIFT);
                } else if (c == '.') { // Just the '.' key
                    keyCode = KeyEvent.VK_PERIOD;
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                } else if (c == '/') { // Just the '/' key
                    keyCode = KeyEvent.VK_SLASH;
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                } else if (c == '\\') { // Just the '\' key
                    keyCode = KeyEvent.VK_BACK_SLASH;
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                } else if (c == '`') { // Just the '`' key
                    keyCode = KeyEvent.VK_BACK_QUOTE;
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                } else if (c == '\'') { // Just the '\'' key
                    keyCode = KeyEvent.VK_QUOTE;
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                } else if (c == '"') { // Requires Shift + '
                    keyCode = KeyEvent.VK_QUOTE;
                    robot.keyPress(KeyEvent.VK_SHIFT);
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                    robot.keyRelease(KeyEvent.VK_SHIFT);
                } else if (c == '?') { // Requires Shift + /
                    keyCode = KeyEvent.VK_SLASH;
                    robot.keyPress(KeyEvent.VK_SHIFT);
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                    robot.keyRelease(KeyEvent.VK_SHIFT);
                } else if (c == '=') { // Just the '=' key
                    keyCode = KeyEvent.VK_EQUALS;
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                } else if (c == '!') { // Requires Shift + 1
                    keyCode = KeyEvent.VK_1;
                    robot.keyPress(KeyEvent.VK_SHIFT);
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                    robot.keyRelease(KeyEvent.VK_SHIFT);
                } else if (c == '@') { // Requires Shift + 2
                    keyCode = KeyEvent.VK_2;
                    robot.keyPress(KeyEvent.VK_SHIFT);
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                    robot.keyRelease(KeyEvent.VK_SHIFT);
                } else if (c == '#') { // Requires Shift + 3
                    keyCode = KeyEvent.VK_3;
                    robot.keyPress(KeyEvent.VK_SHIFT);
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                    robot.keyRelease(KeyEvent.VK_SHIFT);
                } else if (c == '$') { // Requires Shift + 4
                    keyCode = KeyEvent.VK_4;
                    robot.keyPress(KeyEvent.VK_SHIFT);
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                    robot.keyRelease(KeyEvent.VK_SHIFT);
                } else if (c == '%') { // Requires Shift + 5
                    keyCode = KeyEvent.VK_5;
                    robot.keyPress(KeyEvent.VK_SHIFT);
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                    robot.keyRelease(KeyEvent.VK_SHIFT);
                } else if (c == '^') { // Requires Shift + 6
                    keyCode = KeyEvent.VK_6;
                    robot.keyPress(KeyEvent.VK_SHIFT);
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                    robot.keyRelease(KeyEvent.VK_SHIFT);
                } else if (c == '&') { // Requires Shift + 7
                    keyCode = KeyEvent.VK_7;
                    robot.keyPress(KeyEvent.VK_SHIFT);
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                    robot.keyRelease(KeyEvent.VK_SHIFT);
                } else if (c == '*') { // Requires Shift + 8
                    keyCode = KeyEvent.VK_8;
                    robot.keyPress(KeyEvent.VK_SHIFT);
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                    robot.keyRelease(KeyEvent.VK_SHIFT);
                } else if (c == '(') { // Requires Shift + 9
                    keyCode = KeyEvent.VK_9;
                    robot.keyPress(KeyEvent.VK_SHIFT);
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                    robot.keyRelease(KeyEvent.VK_SHIFT);
                } else if (c == ')') { // Requires Shift + 0
                    keyCode = KeyEvent.VK_0;
                    robot.keyPress(KeyEvent.VK_SHIFT);
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                    robot.keyRelease(KeyEvent.VK_SHIFT);
                } else if (c == '_') { // Requires Shift + -
                    keyCode = KeyEvent.VK_MINUS;
                    robot.keyPress(KeyEvent.VK_SHIFT);
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                    robot.keyRelease(KeyEvent.VK_SHIFT);
                } else {


                    // Handle normal characters (letters, numbers, and other symbols)
                    if (Character.isUpperCase(c)) {
                        robot.keyPress(KeyEvent.VK_SHIFT); // Hold Shift for uppercase
                        robot.keyPress(keyCode);
                        robot.keyRelease(keyCode);
                        robot.keyRelease(KeyEvent.VK_SHIFT); // Release Shift
                    } else {
                        robot.keyPress(keyCode);
                        robot.keyRelease(keyCode);
                    }
                }
            }

            System.out.println("CURRENT CHAR >> " + c);
            tets = tets + c;
            System.out.println("TOTAL CHAR >> " + tets);

            // Add small delay between keystrokes for better simulation
            // robot.delay(500);
        }

        System.out.println("%% LEAVE %%");
        // System.out.println(tets +" --------------- $$");
    }

    // Function to get text from the system clipboard
    private static String getClipboardText() throws UnsupportedFlavorException, IOException {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable contents = clipboard.getContents(null);
        if (contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            return (String) contents.getTransferData(DataFlavor.stringFlavor);
        }
        return "";
    }

    // Function to close the browser using Alt + F4 (Windows/Linux)
    private static void closeBrowser(Robot robot) throws InterruptedException {
        // Simulate pressing Alt + F4 to close the browser
        robot.keyPress(KeyEvent.VK_ALT);
        robot.keyPress(KeyEvent.VK_F4);
        robot.keyRelease(KeyEvent.VK_F4);
        robot.keyRelease(KeyEvent.VK_ALT);
    }
}
