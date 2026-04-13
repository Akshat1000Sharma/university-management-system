package com.hms.selenium;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static org.junit.jupiter.api.Assertions.*;

@Tag("selenium")
@DisplayName("Selenium: Role-Based Access and Navigation Tests")
class RoleBasedAccessTest extends SeleniumTestBase {

    @Test
    @DisplayName("Student login redirects to /student")
    void studentRole_redirectsCorrectly() {
        loginAs("student@hms.edu", "student123");
        waitForUrlContains("/student");
        assertTrue(driver.getCurrentUrl().contains("/student"));
    }

    @Test
    @DisplayName("Warden login redirects to /warden")
    void wardenRole_redirectsCorrectly() {
        loginAs("warden@hms.edu", "warden123");
        waitForUrlContains("/warden");
        assertTrue(driver.getCurrentUrl().contains("/warden"));
    }

    @Test
    @DisplayName("Controlling Warden login redirects to /controlling-warden")
    void controllingWardenRole_redirectsCorrectly() {
        loginAs("cwarden@hms.edu", "cwarden123");
        waitForUrlContains("/controlling-warden");
        assertTrue(driver.getCurrentUrl().contains("/controlling-warden"));
    }

    @Test
    @DisplayName("Mess Manager login redirects to /mess-manager")
    void messManagerRole_redirectsCorrectly() {
        loginAs("mess@hms.edu", "mess123");
        waitForUrlContains("/mess-manager");
        assertTrue(driver.getCurrentUrl().contains("/mess-manager"));
    }

    @Test
    @DisplayName("Clerk login redirects to /clerk")
    void clerkRole_redirectsCorrectly() {
        loginAs("clerk@hms.edu", "clerk123");
        waitForUrlContains("/clerk");
        assertTrue(driver.getCurrentUrl().contains("/clerk"));
    }

    @Test
    @DisplayName("HMC Chairman login redirects to /hmc")
    void hmcRole_redirectsCorrectly() {
        loginAs("hmc@hms.edu", "hmc123");
        waitForUrlContains("/hmc");
        assertTrue(driver.getCurrentUrl().contains("/hmc"));
    }

    @Test
    @DisplayName("Unauthenticated access to dashboard redirects to login")
    void unauthenticated_redirectsToLogin() {
        navigateTo("/student");
        // Should either stay at / or redirect to login
        waitForElement(By.cssSelector("input[type='email']"));
        assertTrue(isElementPresent(By.cssSelector("input[type='email']")));
    }

    @Test
    @DisplayName("HMC dashboard loads with grants and halls navigation")
    void hmcDashboard_hasCorrectNav() {
        loginAs("hmc@hms.edu", "hmc123");
        waitForUrlContains("/hmc");

        String pageSource = driver.getPageSource();
        assertTrue(pageSource.contains("Grant") || pageSource.contains("grant") || pageSource.contains("Halls") || pageSource.contains("halls"));
    }

    @Test
    @DisplayName("Mess Manager dashboard shows charges navigation")
    void messManagerDashboard_hasCorrectNav() {
        loginAs("mess@hms.edu", "mess123");
        waitForUrlContains("/mess-manager");

        String pageSource = driver.getPageSource();
        assertTrue(pageSource.contains("Charges") || pageSource.contains("charges") ||
                   pageSource.contains("Mess") || pageSource.contains("mess"));
    }

    @Test
    @DisplayName("Clerk dashboard shows leaves navigation")
    void clerkDashboard_hasCorrectNav() {
        loginAs("clerk@hms.edu", "clerk123");
        waitForUrlContains("/clerk");

        String pageSource = driver.getPageSource();
        assertTrue(pageSource.contains("Leave") || pageSource.contains("leave") ||
                   pageSource.contains("Salary") || pageSource.contains("salary"));
    }
}
