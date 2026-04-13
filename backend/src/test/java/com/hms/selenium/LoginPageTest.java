package com.hms.selenium;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.*;

@Tag("selenium")
@DisplayName("Selenium: Login Page Tests")
class LoginPageTest extends SeleniumTestBase {

    @Test
    @DisplayName("Login page loads with email and password fields")
    void loginPage_loadsCorrectly() {
        navigateTo("/");

        WebElement emailInput = waitForElement(By.cssSelector("input[type='email']"));
        WebElement passwordInput = waitForElement(By.cssSelector("input[type='password']"));
        WebElement submitBtn = waitForElement(By.cssSelector("button[type='submit']"));

        assertTrue(emailInput.isDisplayed());
        assertTrue(passwordInput.isDisplayed());
        assertTrue(submitBtn.isDisplayed());
        assertEquals("Sign in", submitBtn.getText().trim());
    }

    @Test
    @DisplayName("Login page shows HMS branding")
    void loginPage_showsBranding() {
        navigateTo("/");

        String pageSource = driver.getPageSource();
        assertTrue(pageSource.contains("HMS") || pageSource.contains("Hall Management"));
    }

    @Test
    @DisplayName("Login page shows demo account buttons")
    void loginPage_showsDemoAccounts() {
        navigateTo("/");

        String pageSource = driver.getPageSource();
        assertTrue(pageSource.contains("Demo Accounts") || pageSource.contains("demo"));
    }

    @Test
    @DisplayName("Clicking demo account fills email and password fields")
    void clickDemoAccount_fillsCredentials() {
        navigateTo("/");

        // Click the first demo account button (Student)
        WebElement demoBtn = waitForClickable(
                By.xpath("//button[contains(., 'student@hms.edu') or contains(., 'Student')]"));
        demoBtn.click();

        WebElement emailInput = driver.findElement(By.cssSelector("input[type='email']"));
        WebElement passwordInput = driver.findElement(By.cssSelector("input[type='password']"));

        assertEquals("student@hms.edu", emailInput.getAttribute("value"));
        assertEquals("student123", passwordInput.getAttribute("value"));
    }

    @Test
    @DisplayName("Login with valid student credentials redirects to /student")
    void login_validStudent_redirects() {
        loginAs("student@hms.edu", "student123");
        waitForUrlContains("/student");

        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("/student"));
    }

    @Test
    @DisplayName("Login with valid warden credentials redirects to /warden")
    void login_validWarden_redirects() {
        loginAs("warden@hms.edu", "warden123");
        waitForUrlContains("/warden");

        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("/warden"));
    }

    @Test
    @DisplayName("Login with invalid credentials shows error message")
    void login_invalidCredentials_showsError() {
        loginAs("bad@hms.edu", "wrongpassword");

        WebElement errorMsg = waitForElement(By.cssSelector(".bg-red-50, [class*='red'], [class*='error']"));
        assertTrue(errorMsg.isDisplayed());
    }

    @Test
    @DisplayName("Password visibility toggle works")
    void passwordToggle_switchesType() {
        navigateTo("/");

        WebElement passwordInput = waitForElement(By.cssSelector("input[type='password']"));
        assertEquals("password", passwordInput.getAttribute("type"));

        // Click the toggle button (eye icon)
        WebElement toggleBtn = driver.findElement(
                By.xpath("//input[@type='password']/following-sibling::button | //input[@type='password']/..//button"));
        toggleBtn.click();

        WebElement textInput = waitForElement(By.cssSelector("input[type='text']"));
        assertEquals("text", textInput.getAttribute("type"));
    }
}
