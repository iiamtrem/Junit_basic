package com.example;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {

    WebDriver driver;
    WebDriverWait wait;

    void login(String username, String password) {
        WebElement user = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name")));
        user.clear();
        user.sendKeys(username);

        WebElement pass = driver.findElement(By.id("password"));
        pass.clear();
        pass.sendKeys(password);

        driver.findElement(By.id("login-button")).click();
    }

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get("https://www.saucedemo.com/");
    }

    @AfterEach
    void tearDown() {
        if (driver != null)
            driver.quit();
    }

    // Case 1 — UI basic (có đủ ô/nút)
    @Test
    void loginPage_should_show_username_password_loginButton() {
        assertTrue(driver.findElement(By.id("user-name")).isDisplayed());
        assertTrue(driver.findElement(By.id("password")).isDisplayed());
        assertTrue(driver.findElement(By.id("login-button")).isDisplayed());
    }

    // Case 2 - Login thành công
    @Test
    void login_success_should_go_to_inventory_page() {
        login("standard_user", "secret_sauce");

        wait.until(ExpectedConditions.urlContains("inventory"));
        String actualUrl = driver.getCurrentUrl();

        assertTrue(actualUrl.contains("inventory"));
    }

    // Case 3 — Sai password
    @Test
    void login_fail_wrong_password_should_show_error_message() {
        login("standard_user", "wrong_password");

        WebElement err = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='error']")));

        String actual = err.getText().toLowerCase();
        assertTrue(actual.contains("epic sadface"));
    }

    // Case 4 — Bỏ trống username
    @Test
    void login_fail_empty_username_should_show_error() {
        login("", "secret_sauce");

        WebElement err = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='error']")));

        String actual = err.getText().toLowerCase();
        assertTrue(actual.contains("username"));
    }

}
