package com.example;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class LogoutTest {
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
    void logout_should_return_to_login_page() {
        // Open burger menu
        driver.findElement(By.id("react-burger-menu-btn")).click();

        // Wait logout link visible then click
        WebElement logout = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("logout_sidebar_link"))
        );
        logout.click();

        // Assert back to login page
        WebElement loginBtn = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("login-button"))
        );
        assertTrue(loginBtn.isDisplayed());

        assertTrue(driver.getCurrentUrl().contains("saucedemo.com"));
        assertFalse(driver.getCurrentUrl().contains("inventory"));
    }
}
