package com.example;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryTest {
    WebDriver driver;
    WebDriverWait wait;

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get("https://www.saucedemo.com/");
        // login luôn để vào inventory
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name")))
                .sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        wait.until(ExpectedConditions.urlContains("inventory"));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) driver.quit();
    }

    @Test
    void inventory_should_show_products_title_and_items() {
        // Assert title "Products"
        WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".title")));
        assertEquals("Products", title.getText());

        // Assert list items > 0
        List<WebElement> items = driver.findElements(By.cssSelector(".inventory_item"));
        assertTrue(items.size() > 0);
    }
}
