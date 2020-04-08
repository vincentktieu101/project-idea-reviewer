package edu.ucsb.cs48.s20.demo.controllers;

import edu.ucsb.cs48.s20.demo.services.MembershipService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import utils.OAuthUtils;

import org.junit.runner.RunWith;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AdminController.class)
public class OAuthTest {

    private OAuth2User principal;

    @Autowired
    public static MembershipService ms;

    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser
    public void testOauth() throws Exception {
        //OAuth2AuthenticationToken token =  OAuthUtils.getOauthAuthenticationFor(principal);
        principal = OAuthUtils.createOAuth2User("Chris Gaucho", "cgaucho@example.com");
        mvc.perform(MockMvcRequestBuilders.get("/")
                .with(authentication(OAuthUtils.getOauthAuthenticationFor(principal))).accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk());
    }
}
