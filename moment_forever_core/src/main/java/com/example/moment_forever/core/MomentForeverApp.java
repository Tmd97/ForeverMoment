package com.example.moment_forever.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
		"com.example.moment_forever"
})
@EntityScan(basePackages = {
		"com.example.moment_forever.data.entities"
})

public class MomentForeverApp {

    public static void main(String[] args) {
        SpringApplication.run(MomentForeverApp.class, args);
    }
}