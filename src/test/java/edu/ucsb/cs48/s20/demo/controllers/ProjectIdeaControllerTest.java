package edu.ucsb.cs48.s20.demo.controllers;

import edu.ucsb.cs48.s20.demo.advice.AuthControllerAdvice;
import edu.ucsb.cs48.s20.demo.advice.StudentFlowAdvice;
import edu.ucsb.cs48.s20.demo.entities.Student;
import edu.ucsb.cs48.s20.demo.entities.ProjectIdea;
import edu.ucsb.cs48.s20.demo.formbeans.Idea;
import edu.ucsb.cs48.s20.demo.entities.Student;
import edu.ucsb.cs48.s20.demo.entities.Review;
import edu.ucsb.cs48.s20.demo.formbeans.Idea;
import edu.ucsb.cs48.s20.demo.repositories.AppUserRepository;
import edu.ucsb.cs48.s20.demo.repositories.ProjectIdeaRepository;
import edu.ucsb.cs48.s20.demo.repositories.StudentRepository;
import edu.ucsb.cs48.s20.demo.services.GoogleMembershipService;
import edu.ucsb.cs48.s20.demo.services.MembershipService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
import utils.OAuthUtils;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
@RunWith(SpringRunner.class)

public class ProjectIdeaControllerTest{
    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthControllerAdvice aca;

    @MockBean
    private ClientRegistrationRepository crr;

    @MockBean
    private MembershipService ms;

    @Autowired
    private ProjectIdeaController projectIdeaController;

    @MockBean
    private GoogleMembershipService gms;

    @MockBean
    private AppUserRepository aur;

    @MockBean
    private ProjectIdeaRepository pir;

    @MockBean
    private StudentRepository sr;

    @MockBean
    private StudentFlowAdvice sfa;

    private Authentication mockAuthentication;

    @Before
    public void setUp() {
        OAuth2User principal = OAuthUtils.createOAuth2User("Chris Gaucho", "cgaucho@ucsb.edu");
        mockAuthentication = OAuthUtils.getOauthAuthenticationFor(principal);
        when(ms.isMember((OAuth2AuthenticationToken) mockAuthentication)).thenReturn(true);
    }

    /*ADD ACTION TESTS*/
    /*when a student submits a VALID idea (title/deets are proper lengths), it is added to the idea db*/
    /*student attributes are set - submittedIdea = true*/
    /*after submission, there is a redirect to idea reviews page*/
    @Test
    public void studentSubmitsValidIdea_saveToDbAndRedirect() throws Exception{
        Model model = new ExtendedModelMap();
        RedirectAttributes redirAttrs = new RedirectAttributesModelMap();

        Idea idea = new Idea();
        idea.setTitle("TITLE");
        idea.setDetails("This is a valid description because it is more than 30 characters.");

        Student jorbus = new Student();
        jorbus.setId(5);

        when(ms.role((OAuth2AuthenticationToken) mockAuthentication)).thenReturn("Student");
        when(sfa.getStudent((OAuth2AuthenticationToken) mockAuthentication)).thenReturn(jorbus);

        projectIdeaController.addIdea(idea, model, (OAuth2AuthenticationToken) mockAuthentication, null, redirAttrs);

        assert(model.getAttribute("titleHasErrors").equals(false));
        assert(model.getAttribute("detailHasErrors").equals(false));
        final ArgumentCaptor<ProjectIdea> captor = ArgumentCaptor.forClass(ProjectIdea.class);
        verify(pir, times(1)).save(captor.capture());
        verify(sr, times(1)).save(jorbus);
        assert(captor.getValue().getStudent().equals(jorbus));
        assert(captor.getValue().getTitle().equals("TITLE"));
        assert(captor.getValue().getDetails().equals("This is a valid description because it is more than 30 characters."));
        assert(jorbus.getProjectIdea().equals(captor.getValue()));
    }

    /*submitting an invalid idea (title/deets improper lengths) flips model attrs titleHasErrors, detailsHasErrors to true, adds title/deet err strings to model*/
    /*stay on index page (no redirect)*/

    /*DELETE*/
    /*only an admin can perform this action*/
    /*try to delete an idea that DNE- no deletion triggered (look into checking redirectAttrs)*/
    /*try to delete valid idea- delete on idea repo triggered, save on student- check redirectAttrs for success msg*/
    /*redirect to ideas index page*/


    /*INDEX PAGE*/
    /*only an admin can access this page*/
    /*if not admin, then redirected to homepage*/


}