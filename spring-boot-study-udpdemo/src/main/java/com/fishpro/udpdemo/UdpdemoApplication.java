package com.fishpro.udpdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class UdpdemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(UdpdemoApplication.class, args);
    }

}
