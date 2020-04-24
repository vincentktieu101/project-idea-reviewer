package edu.ucsb.cs48.s20.demo.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import edu.ucsb.cs48.s20.demo.advice.AuthControllerAdvice;
import edu.ucsb.cs48.s20.demo.advice.StudentFlowAdvice;
import edu.ucsb.cs48.s20.demo.repositories.AppUserRepository;
import edu.ucsb.cs48.s20.demo.repositories.StudentRepository;
import edu.ucsb.cs48.s20.demo.services.MembershipService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.security.oauth2.core.user.OAuth2User;
import utils.OAuthUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.junit.Before;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;

@RunWith(SpringRunner.class)
@WebMvcTest(ApplicationController.class)
public class HomePageTest {

    @Autowired
	private MockMvc mvc;

	@MockBean
    private AuthControllerAdvice aca;
    
    @MockBean
	private StudentFlowAdvice sfa;
	
	@MockBean
    private ClientRegistrationRepository crr;

    @MockBean
    private MembershipService ms;

    @MockBean
    private AppUserRepository aur;

    @MockBean
    private StudentRepository sr;

    private Authentication mockAuthentication;

    @Before
    public void setUp() {
        final OAuth2User principal = OAuthUtils.createOAuth2User("Chris Gaucho", "cgaucho@ucsb.edu");
        mockAuthentication = OAuthUtils.getOauthAuthenticationFor(principal);
        when(ms.isMember((OAuth2AuthenticationToken) mockAuthentication)).thenReturn(true);
    }

    /*generic test to check that any request to home page succeeds*/
    @Test
    public void getHomePage_ContentType() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }
 
    /*test content of the page- title should be "CS48 demo"*/
    @Test
    public void getHomePage_hasCorrectTitle() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
                .andExpect(xpath("//title").exists())
                .andExpect(xpath("//title").string("CS48 demo"));
    }

    /*test that student can access home page when logged in
    since this is a new student, they should have no submitted ideas and should see the text box*/
    @Test
    public void studentWithNoSubmissions_hasTextBox() throws Exception{
        when(sfa.needsToSubmitProjectIdea((OAuth2AuthenticationToken) mockAuthentication)).thenReturn(true);

        mvc.perform(MockMvcRequestBuilders.get("/").with(authentication(mockAuthentication)).accept(MediaType.TEXT_HTML))
        .andExpect(status().isOk()).andExpect(xpath("/html/body/div/div[1]/p[1]").string("To get started, submit your project idea below."))
        .andExpect(xpath("//*[@id='details']").exists());

    }
}
