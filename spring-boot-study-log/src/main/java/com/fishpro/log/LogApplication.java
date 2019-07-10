package com.fishpro.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class LogApplication {

    public static void main(String[] args) {
        Logger logger =LoggerFactory.getLogger(LogApplication.class);
        SpringApplication.run(LogApplication.class, args);
        logger.debug("This is a debug message");//注意 spring 默认日志输出级别为 info 所以默认情况下 这句不会打印到控制台
        logger.info("This is an info message");
        logger.warn("This is a warn message");
        logger.error("This is an error message");
    }

}
