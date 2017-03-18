package ch.eike.springer.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * The Entry point for the application.
 * NOTE pun intended
 */
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = "ch.eike.springer.configuration")
public class SpringerSpringBootApp {

    public static void main(final String... args) {
        SpringApplication.run(SpringerSpringBootApp.class, args);
    }

}
