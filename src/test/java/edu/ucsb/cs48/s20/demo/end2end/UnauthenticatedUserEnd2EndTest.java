package edu.ucsb.cs48.s20.demo.end2end;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * This is a frame for an unauthenticated end-to-end test with Spring boot
 * This test doesn't do much as is, but additional clicks/assertions can easily be added
 *
 * NOTE: If your project needs an authenticated user, see test/java/.../end2end/UserFlowEnd2EndTest.java
 */
@RunWith(SpringRunner.class)
@DirtiesContext // prevent spring from caching the context for further tests

// NOTE: properties="spring.datasource.name=XYZ" forces Spring Boot to run this test class with a fresh instance of the database.
// XYZ can be anything - just make it unique from other test classes for a fresh test db (loaded from data.sql)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties="spring.datasource.name=unauthenticateduserend2endtest")
public class UnauthenticatedUserEnd2EndTest {
    private WebDriver webDriver;

    /**
     * Runs before the class.
     * Instantiates the WebDriverManager so we can correctly locate the system's ChromeDriver below
     */
    @BeforeClass
    public static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }
    @Before
    public void setUp() {
        // Setup ChromeDriver (aided by the WebDriverManager pom dependency)
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        webDriver = new ChromeDriver(options);
    }

    /**
     * Tear down the webDriver (the automated clicker)
     */
    @After
    public void reset() {
        if (webDriver != null) {
            webDriver.close();
        }
    }

    @Test
    public void testUnauthenticated() {
        // Navigate to login page
        webDriver.get("http://localhost:8080/");
        // Make sure Spring did not redirect us
        assert(webDriver.getCurrentUrl()).startsWith("http://localhost:8080/");
        /*
            Add more test actions for unauthenticated users here.
            ProjectIdeaReviewer has nothing for unauthenticated users, so there isn't much to test here.
         */
    }
}
