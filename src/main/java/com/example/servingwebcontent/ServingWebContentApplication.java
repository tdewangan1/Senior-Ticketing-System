package com.example.servingwebcontent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The entry point of the Spring Boot application.
 * This class contains the main method which uses Spring Boot's SpringApplication.run
 * to launch the application.
 *
 * The @SpringBootApplication annotation marks this class as a Spring Boot application.
 * It enables features like component scanning, autoconfiguration, and property support
 * by encapsulating @Configuration, @EnableAutoConfiguration, and @ComponentScan annotations.
 */
@SpringBootApplication
public class ServingWebContentApplication {

    /**
     * Main method which serves as the entry point for the Spring Boot application.
     * @param args command line arguments passed during the start of the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(ServingWebContentApplication.class, args);
    }

}
