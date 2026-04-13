package com.hms.selenium;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.*;

@Tag("selenium")
@DisplayName("Selenium: Warden Dashboard Tests")
class WardenDashboardTest extends SeleniumTestBase {

    @BeforeEach
    void loginAsWarden() {
        loginAs("warden@hms.edu", "warden123");
        waitForUrlContains("/warden");
    }

    @Test
    @DisplayName("Warden dashboard loads successfully")
    void dashboard_loadsSuccessfully() {
        String pageSource = driver.getPageSource();
        assertTrue(pageSource.contains("Warden") || pageSource.contains("Dashboard") || pageSource.contains("warden"));
    }

    @Test
    @DisplayName("Warden sidebar shows all navigation links")
    void sidebar_showsAllNavLinks() {
        String pageSource = driver.getPageSource();
        assertTrue(pageSource.contains("Students") || pageSource.contains("students"));
        assertTrue(pageSource.contains("Complaints") || pageSource.contains("complaints"));
        assertTrue(pageSource.contains("Staff") || pageSource.contains("staff"));
    }

    @Test
    @DisplayName("Navigate to warden students page")
    void navigate_toStudentsPage() {
        WebElement link = waitForClickable(
                By.xpath("//a[contains(@href, '/warden/students')]"));
        link.click();
        waitForUrlContains("/warden/students");

        assertTrue(driver.getCurrentUrl().contains("/warden/students"));
    }

    @Test
    @DisplayName("Navigate to warden complaints page")
    void navigate_toComplaintsPage() {
        WebElement link = waitForClickable(
                By.xpath("//a[contains(@href, '/warden/complaints')]"));
        link.click();
        waitForUrlContains("/warden/complaints");

        assertTrue(driver.getCurrentUrl().contains("/warden/complaints"));
    }

    @Test
    @DisplayName("Navigate to warden occupancy page")
    void navigate_toOccupancyPage() {
        WebElement link = waitForClickable(
                By.xpath("//a[contains(@href, '/warden/occupancy')]"));
        link.click();
        waitForUrlContains("/warden/occupancy");

        assertTrue(driver.getCurrentUrl().contains("/warden/occupancy"));
    }

    @Test
    @DisplayName("Navigate to warden salary page")
    void navigate_toSalaryPage() {
        WebElement link = waitForClickable(
                By.xpath("//a[contains(@href, '/warden/salary')]"));
        link.click();
        waitForUrlContains("/warden/salary");

        assertTrue(driver.getCurrentUrl().contains("/warden/salary"));
    }

    @Test
    @DisplayName("Navigate to warden expenditures page")
    void navigate_toExpendituresPage() {
        WebElement link = waitForClickable(
                By.xpath("//a[contains(@href, '/warden/expenditures')]"));
        link.click();
        waitForUrlContains("/warden/expenditures");

        assertTrue(driver.getCurrentUrl().contains("/warden/expenditures"));
    }

    @Test
    @DisplayName("Navigate to annual accounts page")
    void navigate_toAccountsPage() {
        WebElement link = waitForClickable(
                By.xpath("//a[contains(@href, '/warden/accounts')]"));
        link.click();
        waitForUrlContains("/warden/accounts");

        assertTrue(driver.getCurrentUrl().contains("/warden/accounts"));
    }
}
