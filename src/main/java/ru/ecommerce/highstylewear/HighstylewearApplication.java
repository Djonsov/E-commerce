package ru.ecommerce.highstylewear;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HighstylewearApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(HighstylewearApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Swagger runs at http://localhost:8080/swagger-ui/index.html");

    }
}
