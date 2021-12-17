package com.ionutbaranga.ptapp;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class PtAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(PtAppApplication.class, args);
    }

}
