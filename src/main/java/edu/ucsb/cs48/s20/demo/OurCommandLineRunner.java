package edu.ucsb.cs48.s20.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import edu.ucsb.cs48.s20.demo.entities.Admin;
import edu.ucsb.cs48.s20.demo.repositories.AdminRepository;

@Component
public class OurCommandLineRunner implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(OurCommandLineRunner.class);

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("Running application with the following args: {}", args.getOptionNames());
        for (String arg : args.getOptionNames()) {
            logger.info("---- {} : {}", arg, args.getOptionValues(arg));
        }
        Admin newAdmin = new Admin();
        newAdmin.setEmail("test@test.org");
        newAdmin = adminRepository.save(newAdmin);
        for (Admin admin : adminRepository.findAll()) {
            logger.info("---- {} : {}", admin.getId(), admin.getEmail());
        }
        adminRepository.delete(newAdmin);
        for (Admin admin : adminRepository.findAll()) {
            logger.info("---- {} : {}", admin.getId(), admin.getEmail());
        }
    }

}