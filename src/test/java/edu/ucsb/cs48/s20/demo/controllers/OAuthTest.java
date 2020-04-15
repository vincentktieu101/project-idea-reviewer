package edu.ucsb.cs48.s20.demo.controllers;

import edu.ucsb.cs48.s20.demo.advice.AuthControllerAdvice;
import edu.ucsb.cs48.s20.demo.advice.StudentFlowAdvice;
import edu.ucsb.cs48.s20.demo.repositories.AppUserRepository;
import edu.ucsb.cs48.s20.demo.repositories.StudentRepository;
import edu.ucsb.cs48.s20.demo.services.GoogleMembershipService;
import edu.ucsb.cs48.s20.demo.services.MembershipService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import utils.OAuthUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

@RunWith(SpringRunner.class)
@WebMvcTest(ApplicationController.class)
public class OAuthTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthControllerAdvice aca;

    @MockBean
    private ClientRegistrationRepository crr;

    @MockBean
    private MembershipService ms;

    @MockBean
    private GoogleMembershipService gms;

    @MockBean
    private AppUserRepository aur;

    @MockBean
    private StudentRepository sr;

    @MockBean
    private StudentFlowAdvice studentFlowAdvice;

    /**
     * This tests the authenticated "guest" user to make sure there is no Google login button in the menu bar,
     * but instead has the user's name with (Guest) and the proper logout button.
     * Also tests to make sure Guests do not have links to admin/student resources
     * @throws Exception
     */
    @Test
    public void testGuestNavbar() throws Exception {
        when(aca.getFirstName(any())).thenReturn("Joe");
        when(aca.getLastName(any())).thenReturn("Gaucho");
        when(aca.getEmail(any())).thenReturn("joegaucho@ucsb.edu");
        when(aca.getRole(any())).thenReturn("Guest");
        when(aca.getIsLoggedIn(any())).thenReturn(true);
        // Check name in header is Joe
        mvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(xpath("/html/body/div[@class='container']/nav[@class='navbar navbar-expand-lg navbar-light bg-light']/div[@id='navbarTogglerDemo03']/ul[@class='nav navbar-nav navbar-right']/li[1]/a")
                        .string("Joe"));

        // check role in header is (Guest)
        mvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(xpath("/html/body/div[@class='container']/nav[@class='navbar navbar-expand-lg navbar-light bg-light']/div[@id='navbarTogglerDemo03']/ul[@class='nav navbar-nav navbar-right']/li[2]")
                        .string("(Guest)"));

        // Check login button is NOT present
        mvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(xpath("/html/body/div[@class='container']/nav[@class='navbar navbar-expand-lg navbar-light bg-light']/div[@id='navbarTogglerDemo03']/ul[@class='nav navbar-nav navbar-right']/li/form[@class='form-inline my-2 my-lg-0']/button[@class='navbar-btn']")
                        .doesNotExist());

        //TODO: Check logout button is present
        //TODO: Make sure the other admin/student buttons are NOT present
    }
}
