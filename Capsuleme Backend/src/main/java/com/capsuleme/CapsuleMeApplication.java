package com.capsuleme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CapsuleMeApplication {

    public static void main(String[] args) {
        SpringApplication.run(CapsuleMeApplication.class, args);

    }
}
