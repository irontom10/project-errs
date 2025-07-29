package net.errs.ProjectErrs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("net.errs.ProjectErrs.Model")
public class ProjectErrsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectErrsApplication.class, args);
    }

}