package edu.ucsb.cs48.s20.demo.controllers;

import edu.ucsb.cs48.s20.demo.advice.AuthControllerAdvice;
import edu.ucsb.cs48.s20.demo.advice.StudentFlowAdvice;
import edu.ucsb.cs48.s20.demo.entities.Student;
import edu.ucsb.cs48.s20.demo.repositories.AppUserRepository;
import edu.ucsb.cs48.s20.demo.repositories.StudentRepository;
import edu.ucsb.cs48.s20.demo.services.GoogleMembershipService;
import edu.ucsb.cs48.s20.demo.services.MembershipService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
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
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
import utils.OAuthUtils;

import java.util.ArrayList;
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
public class StudentsControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private StudentsController studentsController;

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

    private Authentication mockAuthentication;

    @Before
    public void setUp() {
        OAuth2User principal = OAuthUtils.createOAuth2User("Chris Gaucho", "cgaucho@ucsb.edu");
        mockAuthentication = OAuthUtils.getOauthAuthenticationFor(principal);
        when(ms.isMember((OAuth2AuthenticationToken) mockAuthentication)).thenReturn(true);
    }

    /**
     * This test makes sure admins can access the /students endpoint
     */
    @Test
    public void testAdminPageAccessForAdmins() throws Exception {
        when(ms.role(any())).thenReturn("Admin");
        mvc.perform(MockMvcRequestBuilders.get("/students").with(authentication(mockAuthentication)).accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk());
    }

    /**
     * This test makes sure students can not access the /students endpoint
     */
    @Test
    public void testAdminPageAccessForStudents() throws Exception {
        when(ms.role(any())).thenReturn("Student");
        mvc.perform(MockMvcRequestBuilders.get("/students").with(authentication(mockAuthentication)).accept(MediaType.TEXT_HTML))
                .andExpect(status().is3xxRedirection());
        // Note: with(authentication(mockAuthentication)) is required to bypass Spring's HttpSecurity that would otherwise redirect guest users to the login page
    }

    /**
     * This test directly tests the information the /students endpoint injects into the Model
     * The method studentsController.students is called directly vs fully rendering html and using xpath
     *
     * This tests the case where no students are enrolled
     * @throws Exception
     */
    @Test
    public void testEmptyStudentsModel() throws Exception {
        // Begin by creating a new Model that we can pass to the controller to populate
        Model model = new ExtendedModelMap();

        // Mock the user role as an Admin
        when(ms.role(any())).thenReturn("Admin");

        // Mock the database response
        List<Student> someStudents = Arrays.asList();
        when(sr.findAll()).thenReturn(someStudents);

        // Call the controller
        studentsController.students(model, (OAuth2AuthenticationToken) mockAuthentication, null);

        // Make sure model has correct attribute
        assert(model.getAttribute("students").equals(someStudents));
    }

    /**
     * This test directly tests the information the /students endpoint injects into the Model
     * The method studentsController.students is called directly vs fully rendering html and using xpath
     *
     * This tests the case where some students are enrolled
     * @throws Exception
     */
    @Test
    public void testNonEmptyStudentsModel() throws Exception {
        // Begin by creating a new Model that we can pass to the controller to populate
        Model model = new ExtendedModelMap();

        // Mock the user role as an Admin
        when(ms.role(any())).thenReturn("Admin");

        // Mock the database response
        List<Student> someStudents = Arrays.asList(new Student(), new Student(), new Student());
        when(sr.findAll()).thenReturn(someStudents);

        // Call the controller
        studentsController.students(model, (OAuth2AuthenticationToken) mockAuthentication, null);

        // Make sure model has correct attribute
        assert(model.getAttribute("students").equals(someStudents));
    }

    /**
     * This tests the /students/delete/{id} endpoint by mocking two students, deleting one, and
     * asserting that the controller's model returns only one student in the students list
     *
     * A very comprehensive test
     */
    @Test
    public void testDeleteValidStudent() throws Exception {
        // Begin by creating a new Model that we can pass to the controller to populate
        Model model = new ExtendedModelMap();
        RedirectAttributes redirAttrs = new RedirectAttributesModelMap();

        // Mock the user role as an Admin
        when(ms.role(any())).thenReturn("Admin");

        // Create our two students
        Student joe = new Student();
        joe.setId(5);
        Student carl = new Student();
        carl.setId(10);

        // Mock the database response
        when(sr.findById((long)5)).thenReturn(java.util.Optional.of(joe));
        when(sr.findAll()).thenReturn(Arrays.asList(carl));

        // Call the controller
        studentsController.deleteAdmin((long) 5, model, redirAttrs, (OAuth2AuthenticationToken) mockAuthentication);

        // Make sure delete was actually called (once)
        verify(sr, times(1)).delete(joe);

        // Make sure we display a success message
        assert(redirAttrs.getFlashAttributes().containsKey("alertSuccess"));

        // Assert model has correct attribute
        assert(model.getAttribute("students").equals(Arrays.asList(carl)));

        // Note: with(authentication(mockAuthentication)) is required to bypass Spring's HttpSecurity that would otherwise redirect guest users to the login page
    }


}
