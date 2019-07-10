package com.fishpro.helloworld;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HelloworldApplication {

	public static void main(String[] args) {

		SpringApplication.run(HelloworldApplication.class, args);
		System.out.println("helloworld application success...");
	}

}
