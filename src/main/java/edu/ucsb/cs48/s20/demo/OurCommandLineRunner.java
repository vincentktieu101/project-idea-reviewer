package edu.ucsb.cs48.s20.demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import edu.ucsb.cs48.s20.demo.entities.Student;
import edu.ucsb.cs48.s20.demo.entities.Admin;

import edu.ucsb.cs48.s20.demo.services.CSVToObjectService;
import edu.ucsb.cs48.s20.demo.repositories.AdminRepository;
import edu.ucsb.cs48.s20.demo.repositories.StudentRepository;

@Component
public class OurCommandLineRunner implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(OurCommandLineRunner.class);

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    CSVToObjectService<Student> csvToObjectService;

    // Note: you can pass commandline arguments to spring boot with the following
    // format:
    // mvn spring-boot:run -Dspring-boot.run.arguments="--load-students=filename"
    // mvn spring-boot:run -Dspring-boot.run.arguments="--load-students=file1 --load-students=file2"
    // mvn spring-boot:run -Dspring-boot.run.arguments="--add-admin=cgaucho@example.org"

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if (args.getOptionNames().size() == 0) {
            logger.info("No command line arguments to OurCommandLineRunner");
            return;
        }

        if (args.containsOption("load-student-file")) {
            loadStudentFiles(args.getOptionValues("load-student-file"));
        }

        if (args.containsOption("add-admin")) {
            setAdmins(args.getOptionValues("add-admin"));
        }

    }

    public void loadStudentFiles(List<String> filenames) {
        for (var filename : filenames) {
            loadStudentFile(filename);
        }
    }

    public void setAdmins(List<String> admins) {
        for (var admin : admins) {
            setAdmin(admin);
        }
    }

    public void loadStudentFile(String filename) {
        logger.warn("Loading data from {}", filename);

        InputStream targetStream = null;

        try {
            File initialFile = new File(filename);
            targetStream = new FileInputStream(initialFile);
        } catch (FileNotFoundException fnfe) {
            logger.error("File not found: {}, no data loaded.", filename);
            return;
        }

        try (Reader reader = new InputStreamReader(targetStream)) {
            List<Student> students = csvToObjectService.parse(reader, Student.class);
            studentRepository.saveAll(students);
        } catch (IOException ioe) {
            logger.error("Exception: ", ioe);
        }

    }

    public void setAdmin(String adminEmail) {
        logger.info("Adding admin {}", adminEmail);
        Admin newAdmin = new Admin();
        newAdmin.setEmail(adminEmail);
        newAdmin = adminRepository.save(newAdmin);
        logger.info("Admin {} added successfully", newAdmin);
    }

}