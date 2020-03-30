package edu.ucsb.cs48.s20.demo.advice;

import org.springframework.web.bind.annotation.ModelAttribute;

import edu.ucsb.cs48.s20.demo.controllers.AppUsersController;
import edu.ucsb.cs48.s20.demo.entities.AppUser;
import edu.ucsb.cs48.s20.demo.entities.Student;
import edu.ucsb.cs48.s20.demo.repositories.AppUserRepository;
import edu.ucsb.cs48.s20.demo.repositories.StudentRepository;
import edu.ucsb.cs48.s20.demo.services.MembershipService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class StudentFlowAdvice {

    @Autowired   
    private MembershipService membershipService;

    @Autowired   
    private AppUserRepository appUserRepository;

    @Autowired   
    private StudentRepository studentRepository;

    
    @ModelAttribute("needsToSubmitProjectIdea")
    public boolean needsToSubmitProjectIdea(OAuth2AuthenticationToken token){
        Student student = getStudent(token);
        if (student==null)
          return false;
        return (student.getProjectIdea()==null);
    }

    @ModelAttribute("student")
    public Student getStudent(OAuth2AuthenticationToken token){
        if (!(membershipService.isStudent(token))) {
            return null;
        }
        String email = membershipService.email(token);
        List<Student> students = studentRepository.findByEmail(email);
        if (students.size()==0) {
            return null;
        }
        Student student = students.get(0);
        return student;
    }



}