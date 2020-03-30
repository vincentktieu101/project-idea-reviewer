package edu.ucsb.cs48.s20.demo.controllers;

import edu.ucsb.cs48.s20.demo.entities.Student;
import edu.ucsb.cs48.s20.demo.repositories.StudentRepository;
import edu.ucsb.cs48.s20.demo.services.MembershipService;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class StudentsController {

    private Logger logger = LoggerFactory.getLogger(StudentsController.class);

    @Autowired
    private MembershipService ms;

    private StudentRepository studentRepository;

    @Autowired
    public StudentsController(StudentRepository repo) {
        this.studentRepository = repo;
    }

    @GetMapping("/students")
    public String students(Model model, OAuth2AuthenticationToken token, RedirectAttributes redirAttrs) {
        String role = ms.role(token);
        if (!role.equals("Admin")) {
            redirAttrs.addFlashAttribute("alertDanger", "You do not have permission to access that page");
            return "redirect:/";
        }
        model.addAttribute("students", studentRepository.findAll());
        return "students/index";
    }

    @PostMapping("/students/delete/{id}")
    public String deleteAdmin(@PathVariable("id") Long id, Model model, RedirectAttributes redirAttrs,
            OAuth2AuthenticationToken token) {
        String role = ms.role(token);
        if (!role.equals("Admin")) {
            redirAttrs.addFlashAttribute("alertDanger", "You do not have permission to access that page");
            return "redirect:/";
        }

        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (!optionalStudent.isPresent()) {
            redirAttrs.addFlashAttribute("alertDanger", "User with that id does not exist.");
        } else {
            Student student = optionalStudent.get();
            String email = student.getEmail();
            studentRepository.delete(student);
            redirAttrs.addFlashAttribute("alertSuccess", "Student " + email + "successfully deleted.");
        }
        model.addAttribute("students", studentRepository.findAll());
        return "redirect:/students";
    }

}
