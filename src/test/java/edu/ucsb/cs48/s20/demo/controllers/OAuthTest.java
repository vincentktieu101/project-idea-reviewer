package edu.ucsb.cs48.s20.demo.controllers;

import edu.ucsb.cs48.s20.demo.Application;
import edu.ucsb.cs48.s20.demo.advice.AuthControllerAdvice;
import edu.ucsb.cs48.s20.demo.advice.StudentFlowAdvice;
import edu.ucsb.cs48.s20.demo.services.GoogleMembershipService;
import edu.ucsb.cs48.s20.demo.services.MembershipService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import utils.OAuthUtils;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

import org.junit.runner.RunWith;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ApplicationController.class)
@ContextConfiguration(classes={Application.class, AuthControllerAdvice.class, GoogleMembershipService.class})

public class OAuthTest {

    @Autowired
    public static MembershipService ms;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private StudentFlowAdvice sfa;

    @MockBean
    private AuthControllerAdvice aca;

    @MockBean
    private ClientRegistrationRepository crr;

    @Configuration
    public static class TestConf {
        @Bean
        public ApplicationController applicationController() {
            return new ApplicationController();
        }
    }


    //@Test
    /**
     * Tests that we can successfully authenticate with an oauth user

    public void testOauth() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/")
                .with(authentication(OAuthUtils.getOauthAuthenticationFor(guest_principal))).accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk());
    }
    */

    @Test
    /**
     * Test that a guest account can properly access the app as a guest
     */
    public void testUnauthenticated() throws Exception {
        OAuth2User guest = OAuthUtils.createOAuth2User("Chris Gaucho", "pamplona@ucsb.edu");
        System.out.println(mvc.perform(MockMvcRequestBuilders.get("/")).toString());
        assert(false);
    }
}
