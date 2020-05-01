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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)

public class UserFlowEnd2EndTest {
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

    static final String APP_BASE_URL = "http://localhost:8080";

    private WebDriver webDriver;

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
                        .withBodyFile("login.html")));

        mockOAuth2Provider.stubFor(post(urlPathEqualTo("/login"))
                .willReturn(temporaryRedirect("{{formData request.body 'form' urlDecode=true}}http://localhost:8080/login/oauth2/code/wiremock?code={{{randomValue length=30 type='ALPHANUMERIC'}}}&state={{{form.state}}}")));

        mockOAuth2Provider.stubFor(post(urlPathEqualTo("/oauth/token"))
                .willReturn(okJson("{\"token_type\": \"Bearer\",\"access_token\":\"{{randomValue length=20 type='ALPHANUMERIC'}}\"}")));

        mockOAuth2Provider.stubFor(get(urlPathEqualTo("/userinfo"))
                .willReturn(okJson("{\"sub\":\"my-id\",\"email\":\"joe@ucsb.edu\", \"hd\":\"ucsb.edu\", \"name\":\"Joe\", \"given_name\":\"Joe\", \"family_name\":\"Gaucho\"}")));
    }

    @After
    public void reset() {
        if (webDriver != null) {
            webDriver.close();
        }
    }

    @Test
    public void testTest() {
        webDriver.get(APP_BASE_URL + "/oauth2/authorization/wiremock");
        assert(webDriver.getCurrentUrl()).startsWith("http://localhost:8077/oauth/authorize");

        webDriver.findElement(By.name("username")).sendKeys("joe@ucsb.edu");
        webDriver.findElement(By.name("password")).sendKeys("password");
        webDriver.findElement(By.id("submit")).click();
        assert(webDriver.getCurrentUrl().equalsIgnoreCase("http://localhost:8080/"));
    }

}
