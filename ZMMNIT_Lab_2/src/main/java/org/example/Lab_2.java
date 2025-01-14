package org.example;


import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

public class Lab_2 {
    private WebDriver chromeDriver;
    private static final String baseUrl = "https://www.nmu.org.ua/ua/";

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        //Run driver
        WebDriverManager.chromedriver().setup();
        ChromeOptions chromeOptions = new ChromeOptions();
        //set fullscreen
        chromeOptions.addArguments("--start-fullscreen");
        //setup wait for loading elements
        chromeOptions.setImplicitWaitTimeout(Duration.ofSeconds(15));
        this.chromeDriver = new ChromeDriver(chromeOptions);
    }

    @BeforeMethod
    public void preconditions() {
        //open main page
        chromeDriver.get(baseUrl);
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        chromeDriver.quit();
    }

    @Test
    //will be failed - fix it
    public void testHeaderExists() {
        //find element by id
        WebElement header = chromeDriver.findElement(By.id("header"));
        //verification
        Assert.assertNotNull(header);
    }

    @Test
    public void testClickOnForStudent() {
        //find element by xpath
        WebElement forStudentButton = chromeDriver.findElement(By.xpath("//*[@id=\"main_vertikal2\"]/li[1]/a/span"));
        //verification
        Assert.assertNotNull(forStudentButton);
        forStudentButton.click();
        //verification page changed
        Assert.assertNotEquals(chromeDriver.getCurrentUrl(), baseUrl);
    }
    @Test
    //will be failed - fix it
    public void testSearchFieldOnForStudentPage() {
        String studentPageUrl = "content/students/";
        chromeDriver.get(baseUrl + studentPageUrl);
        //find element by tagName
        WebElement searchField = chromeDriver.findElement(By.tagName("input"));
        //verification
        Assert.assertNotNull(searchField);
        //different params of searchField
        System.out.println(String.format("Name attribute: %s", searchField.getAttribute("name"))+
                String.format("\n attribute: %s", searchField.getAttribute("id")) +
                String.format("\nType attribute: %s", searchField.getAttribute("type")) +
                String.format("\nValue attribute: %s", searchField.getAttribute("value")) +
                String.format("\nPosition: (%d;%d)", searchField.getLocation().x, searchField.getLocation().y) +
                String.format("\nSize: %dx%d", searchField.getSize().width, searchField.getSize().height)
        );
        //input value
        String inputValue = "I need info";
        searchField.sendKeys(inputValue);
        //verification text
        Assert.assertEquals(searchField.getText(), inputValue);
        //click enter
        searchField.sendKeys(Keys.ENTER);
        //verification page changed
        Assert.assertNotEquals(chromeDriver.getCurrentUrl(), studentPageUrl);
    }
}