package com.example;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CartTest {
    WebDriver driver;
    WebDriverWait wait;

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get("https://www.saucedemo.com/");
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
    void cart_should_show_added_item() {
        // Add 1 item
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();

        // Go to cart
        driver.findElement(By.cssSelector(".shopping_cart_link")).click();
        wait.until(ExpectedConditions.urlContains("cart"));

        // Assert item exists
        List<WebElement> names = driver.findElements(By.cssSelector(".inventory_item_name"));
        assertTrue(names.size() > 0);

        boolean found = names.stream().anyMatch(e -> e.getText().equals("Sauce Labs Backpack"));
        assertTrue(found);
    }

    @Test
    void remove_in_cart_should_remove_item() {
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();
        driver.findElement(By.cssSelector(".shopping_cart_link")).click();
        wait.until(ExpectedConditions.urlContains("cart"));

        driver.findElement(By.id("remove-sauce-labs-backpack")).click();

        List<WebElement> items = driver.findElements(By.cssSelector(".cart_item"));
        assertEquals(0, items.size());
    }
}
