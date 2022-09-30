package cl.wom.rest.service;

import org.springframework.stereotype.Service;

import cl.wom.rest.controller.response.PostResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ApiService {

	public String getProcess() {

		log.info("getProcess");

		log.debug("This is a debug message {}{}", "x", "y");
		log.info("This is an info message {}{}", "x", "y");
		log.warn("This is a warn message {}{}", "x", "y");
		log.error("This is an error message");

		return "Hello";

	}

	public PostResponse postProcess() {
		log.info("postProcess");

		PostResponse postResponse = new PostResponse();
		postResponse.setProcessStatus("OK");

		return postResponse;
	}

	public String putProcess() {
		log.info("putProcess");
		return "Hello";
	}

	public void deleteProcess() {
		log.info("deleteProcess");
	}

}
