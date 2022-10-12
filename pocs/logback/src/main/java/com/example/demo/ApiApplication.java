package com.example.demo;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiApplication implements ApplicationRunner {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

	public void run(ApplicationArguments arg0) throws Exception {

		// Mapped Diagnostic Context
		// https://howtodoinjava.com/logback/logging-with-mapped-diagnostic-context/
		MDC.put("trace-id", UUID.randomUUID().toString());

		log.debug("This is a debug message {}", "x");
		log.info("This is an info message {}", "x");
		log.warn("This is a warn message {}", "x");
		log.error("This is an error message {}", "x");

		MDC.clear();

	}
}
