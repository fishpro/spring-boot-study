package com.fishpro.helloworld;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HelloworldApplication {

	public static void main(String[] args) {

		SpringApplication.run(HelloworldApplication.class, args);
		//增加输出窗口打印成功信息 方便用户看到启动成功后的标志
		System.out.println("helloworld application success...");
	}

}
