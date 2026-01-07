package com.example;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class CheckoutTest {
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
    void checkout_success_should_show_complete_page() {
        // Add item
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();

        // Cart
        driver.findElement(By.cssSelector(".shopping_cart_link")).click();
        wait.until(ExpectedConditions.urlContains("cart"));

        // Checkout
        driver.findElement(By.id("checkout")).click();
        wait.until(ExpectedConditions.urlContains("checkout-step-one"));

        // Fill info
        driver.findElement(By.id("first-name")).sendKeys("Test");
        driver.findElement(By.id("last-name")).sendKeys("User");
        driver.findElement(By.id("postal-code")).sendKeys("70000");

        driver.findElement(By.id("continue")).click();
        wait.until(ExpectedConditions.urlContains("checkout-step-two"));

        // Finish
        driver.findElement(By.id("finish")).click();
        wait.until(ExpectedConditions.urlContains("checkout-complete"));

        String thankYou = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".complete-header"))
        ).getText();

        assertEquals("Thank you for your order!", thankYou);
    }

    @Test
void checkout_fail_missing_first_name_should_show_error() {
    // Add item
    driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();

    // Cart
    driver.findElement(By.cssSelector(".shopping_cart_link")).click();
    wait.until(ExpectedConditions.urlContains("cart"));

    // Checkout
    driver.findElement(By.id("checkout")).click();
    wait.until(ExpectedConditions.urlContains("checkout-step-one"));

    // Missing first name
    driver.findElement(By.id("last-name")).sendKeys("User");
    driver.findElement(By.id("postal-code")).sendKeys("70000");
    driver.findElement(By.id("continue")).click();

    WebElement err = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='error']"))
    );
    String msg = err.getText().toLowerCase();

    assertTrue(msg.contains("first name"));
}

}
