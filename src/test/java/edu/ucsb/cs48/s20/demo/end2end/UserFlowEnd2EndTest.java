package edu.ucsb.cs48.s20.demo.end2end;

import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

/**
 * This test runs a complete end-to-end test on the project as a single test.
 * Starts by logging in as a student, submitting a review, and reviewing 5 ideas
 *
 * If you are trying to duplicate this test in a project, make sure to copy the
 * html template under the test/resources/__files/ directory!
 *
 * NOTE: To run this test you must install ChromeDriver on your machine
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Transactional
public class UserFlowEnd2EndTest {

    /**
     * Sets up custom environment variables for this test class. This is the information for our fake oauth provider.
     */
    static {
        System.setProperty("spring.security.oauth2.client.provider.wiremock.authorization-uri", "http://localhost:8077/oauth/authorize");
        System.setProperty("spring.security.oauth2.client.provider.wiremock.token-uri", "http://localhost:8077/oauth/token");
        System.setProperty("spring.security.oauth2.client.provider.wiremock.user-info-uri", "http://localhost:8077/userinfo");
        System.setProperty("spring.security.oauth2.client.provider.wiremock.user-name-attribute", "sub");
        System.setProperty("spring.security.oauth2.client.registration.wiremock.provider", "wiremock");
        System.setProperty("spring.security.oauth2.client.registration.wiremock.authorization-grant-type", "authorization_code");
        System.setProperty("spring.security.oauth2.client.registration.wiremock.scope", "email");
        System.setProperty("spring.security.oauth2.client.registration.wiremock.redirect-uri", "http://localhost:8080/login/oauth2/code/{registrationId}");
        System.setProperty("spring.security.oauth2.client.registration.wiremock.clientId", "wm");
        System.setProperty("spring.security.oauth2.client.registration.wiremock.clientSecret", "whatever");
    }

    private WebDriver webDriver;

    /**
     * Sets up the api endpoints for the custom mock oauth provider. We have to do this because we can't run automated
     * tests with real Google credentials
     */
    @Rule
    public WireMockRule mockOAuth2Provider = new WireMockRule(wireMockConfig()
            .port(8077)
            .extensions(new ResponseTemplateTransformer(true)));
    @Before
    public void setUp() {
        webDriver = new ChromeDriver();

        mockOAuth2Provider.stubFor(get(urlPathEqualTo("/favicon.ico")).willReturn(notFound()));

        mockOAuth2Provider.stubFor(get(urlPathMatching("/oauth/authorize?.*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/html")
                        .withBodyFile("mocklogin.html")));

        mockOAuth2Provider.stubFor(post(urlPathEqualTo("/login"))
                .willReturn(temporaryRedirect("{{formData request.body 'form' urlDecode=true}}http://localhost:8080/login/oauth2/code/wiremock?code={{{randomValue length=30 type='ALPHANUMERIC'}}}&state={{{form.state}}}")));

        mockOAuth2Provider.stubFor(post(urlPathEqualTo("/oauth/token"))
                .willReturn(okJson("{\"token_type\": \"Bearer\",\"access_token\":\"{{randomValue length=20 type='ALPHANUMERIC'}}\"}")));

        mockOAuth2Provider.stubFor(get(urlPathEqualTo("/userinfo"))
                .willReturn(okJson("{\"sub\":\"my-id\",\"email\":\"joe@ucsb.edu\", \"hd\":\"ucsb.edu\", \"name\":\"Joe\", \"given_name\":\"Joe\", \"family_name\":\"Gaucho\"}")));
    }

    /**
     * Tear down the webDriver (automated clicker)
     */
    @After
    public void reset() {
        if (webDriver != null) {
            webDriver.close();
        }
    }

    static String MEDIUM_TEXT = "This is a long bit of text. This will be reused in some text fields throughout the test.";

    @Test
    public void testTest() {
        // Navigate to login page
        webDriver.get("http://localhost:8080/oauth2/authorization/wiremock");
        // Make sure Spring redirected us to the right place
        assert(webDriver.getCurrentUrl()).startsWith("http://localhost:8077/oauth/authorize");
        webDriver.findElement(By.id("submit")).click();
        // Verify login was successful (and did not result in /error path)
        assert(webDriver.getCurrentUrl().equalsIgnoreCase("http://localhost:8080/"));

        // fill out idea form
        webDriver.findElement(By.id("ideaTitle")).sendKeys("This is a title for a fantastic idea");
        webDriver.findElement(By.id("ideaBody")).sendKeys(MEDIUM_TEXT);
        webDriver.findElement(By.id("submit")).click();

        // Make sure we got redirected to the review review page (lol)
        assert(webDriver.getCurrentUrl().equalsIgnoreCase("http://localhost:8080/reviews/perform"));

        // Enter 5 reviews
        for (int i = 0; i < 5; i++) {
            assert webDriver.findElement(By.id("reviewsNeeded")).getText().contains(String.valueOf(5-i)); //check user sees x remaining
            webDriver.findElement(By.id("radio5Label")).click();
            webDriver.findElement(By.className("form-control")).sendKeys(MEDIUM_TEXT);
            webDriver.findElement(By.id("submit")).click();
        }

        // User has inputted 5 reviews. Make sure the reviewsNeeded banner is gone, and the reviewsSufficient banner shows
        assert(webDriver.findElements(By.id("reviewsNeeded")).isEmpty());
        assert(webDriver.findElements(By.id("reviewsSufficient")).size() == 1);
    }

}
