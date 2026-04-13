package com.hms.selenium;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.*;

@Tag("selenium")
@DisplayName("Selenium: Student Dashboard Tests")
class StudentDashboardTest extends SeleniumTestBase {

    @BeforeEach
    void loginAsStudent() {
        loginAs("student@hms.edu", "student123");
        waitForUrlContains("/student");
    }

    @Test
    @DisplayName("Student dashboard loads and shows welcome content")
    void dashboard_loadsSuccessfully() {
        String pageSource = driver.getPageSource();
        assertTrue(pageSource.contains("Student") || pageSource.contains("Dashboard") || pageSource.contains("student"));
    }

    @Test
    @DisplayName("Student sidebar navigation links are visible")
    void sidebar_showsNavLinks() {
        // Should have nav links for dues, complaints, payments
        String pageSource = driver.getPageSource();
        assertTrue(pageSource.contains("Dues") || pageSource.contains("dues"));
        assertTrue(pageSource.contains("Complaints") || pageSource.contains("complaints"));
        assertTrue(pageSource.contains("Payments") || pageSource.contains("payments"));
    }

    @Test
    @DisplayName("Navigate to student dues page")
    void navigate_toDuesPage() {
        WebElement duesLink = waitForClickable(
                By.xpath("//a[contains(@href, '/student/dues')]"));
        duesLink.click();
        waitForUrlContains("/student/dues");

        assertTrue(driver.getCurrentUrl().contains("/student/dues"));
    }

    @Test
    @DisplayName("Navigate to student complaints page")
    void navigate_toComplaintsPage() {
        WebElement link = waitForClickable(
                By.xpath("//a[contains(@href, '/student/complaints')]"));
        link.click();
        waitForUrlContains("/student/complaints");

        assertTrue(driver.getCurrentUrl().contains("/student/complaints"));
    }

    @Test
    @DisplayName("Navigate to student payments page")
    void navigate_toPaymentsPage() {
        WebElement link = waitForClickable(
                By.xpath("//a[contains(@href, '/student/payments')]"));
        link.click();
        waitForUrlContains("/student/payments");

        assertTrue(driver.getCurrentUrl().contains("/student/payments"));
    }

    @Test
    @DisplayName("Sign out returns to login page")
    void signOut_returnsToLogin() {
        WebElement signOutBtn = waitForClickable(
                By.xpath("//button[contains(text(), 'Sign') or contains(text(), 'sign') or contains(text(), 'Logout') or contains(text(), 'logout') or contains(text(), 'Log out')]"));
        signOutBtn.click();

        waitForUrlContains("/");
        WebElement emailInput = waitForElement(By.cssSelector("input[type='email']"));
        assertTrue(emailInput.isDisplayed());
    }
}
