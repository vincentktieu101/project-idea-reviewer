package edu.ucsb.cs48.s20.demo.controllers;

import edu.ucsb.cs48.s20.demo.advice.StudentFlowAdvice;
import edu.ucsb.cs48.s20.demo.entities.ProjectIdea;
import edu.ucsb.cs48.s20.demo.entities.Student;
import edu.ucsb.cs48.s20.demo.formbeans.Idea;
import edu.ucsb.cs48.s20.demo.repositories.ProjectIdeaRepository;
import edu.ucsb.cs48.s20.demo.repositories.StudentRepository;
import edu.ucsb.cs48.s20.demo.services.CSVToObjectService;
import edu.ucsb.cs48.s20.demo.services.MembershipService;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProjectIdeaController {

    private Logger logger = LoggerFactory.getLogger(StudentsController.class);

    @Autowired
    private MembershipService ms;

    @Autowired
    private StudentFlowAdvice studentFlowAdvice;

    private StudentRepository studentRepository;

    private ProjectIdeaRepository projectIdeaRepository;

    @Autowired
    CSVToObjectService<Student> csvToObjectService;

    @Autowired
    public ProjectIdeaController(ProjectIdeaRepository projectIdeaRepository, StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
        this.projectIdeaRepository = projectIdeaRepository;
    }

    @GetMapping("/ideas")
    public String ideas(Model model, OAuth2AuthenticationToken token, RedirectAttributes redirAttrs) {
        String role = ms.role(token);
        if (!role.equals("Admin")) {
            redirAttrs.addFlashAttribute("alertDanger", "You do not have permission to access that page");
            return "redirect:/";
        }
        model.addAttribute("ideas", projectIdeaRepository.findAll());
        return "ideas/index";
    }

    @PostMapping("/ideas/delete/{id}")
    public String deleteIdea(@PathVariable("id") Long id, Model model, RedirectAttributes redirAttrs,
            OAuth2AuthenticationToken token) {
        String role = ms.role(token);
        if (!role.equals("Admin")) {
            redirAttrs.addFlashAttribute("alertDanger", "You do not have permission to access that page");
            return "redirect:/";
        }

        Optional<ProjectIdea> optionalIdea = projectIdeaRepository.findById(id);
        if (!optionalIdea.isPresent()) {
            redirAttrs.addFlashAttribute("alertDanger", "ProjectIdea with that id does not exist.");
        } else {
            ProjectIdea idea = optionalIdea.get();
            String title = idea.getTitle();
            Student student = idea.getStudent();
            String name = idea.getStudent().getFname() + " " + idea.getStudent().getLname();
            projectIdeaRepository.delete(idea);
            studentRepository.save(student);
            redirAttrs.addFlashAttribute("alertSuccess",
                    "Idea " + title + " for student " + name + " successfully deleted.");
        }
        model.addAttribute("ideas", projectIdeaRepository.findAll());
        return "redirect:/ideas";
    }

    @PostMapping("/ideas/add")
    public String addIdea(@ModelAttribute("idea") Idea idea, Model model, OAuth2AuthenticationToken token,
            BindingResult bindingResult, RedirectAttributes redirAttrs) {
        String role = ms.role(token);
        if (!role.equals("Admin") && !role.equals("Student")) {
            redirAttrs.addFlashAttribute("alertDanger", "You do not have permission to access that page");
            return "redirect:/";
        }

        boolean errors =false;

        model.addAttribute("titleHasErrors", false);
        model.addAttribute("detailHasErrors", false);

        if (idea.getTitle() == null || idea.getTitle().length() < 15) {
            model.addAttribute("titleErrors", "Title is too short (15 characters minimum)");
            model.addAttribute("titleHasErrors", true);
            errors = true;

        }

        if (idea.getTitle() != null && idea.getTitle().length() > 255) {
            model.addAttribute("titleErrors", "Title is too long");
            model.addAttribute("titleHasErrors", true);
            errors = true;

        }

        if (idea.getDetails() == null || idea.getDetails().length() < 200) {
            model.addAttribute("detailErrors", "Please add some more detail");
            model.addAttribute("detailHasErrors", true);
            errors = true;
        }

        if (!errors) {
            Student student = studentFlowAdvice.getStudent(token);
            ProjectIdea projectIdea = new ProjectIdea();
            projectIdea.setStudent(student);
            student.setProjectIdea(projectIdea);
            projectIdea.setTitle(idea.getTitle());
            projectIdea.setDetails(idea.getDetails());
            projectIdeaRepository.save(projectIdea);
            studentRepository.save(student);
            return "redirect:/";

        }

        model.addAttribute("idea", idea);
    
        logger.info("leaving ProjectIdeaController addIdea:");
        logger.info("idea"+idea);

        return "index";
        
    }

}
