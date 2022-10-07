package cl.wom.rest.service;

import org.springframework.stereotype.Service;

import cl.wom.rest.controller.response.GetResponse;
import cl.wom.rest.controller.response.PostResponse;
import cl.wom.rest.exception.BusinessException;
import cl.wom.rest.mongo.MongoDAO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ApiService {

	private MongoDAO mongoDAO;

	public ApiService(MongoDAO mongoDAO) {
		this.mongoDAO = mongoDAO;
	}

	// Read method
	public GetResponse getProcess() {
		log.info("[getProcess]");

		GetResponse getResponse = new GetResponse();
		getResponse.setProcessStatus("OK");

		try {

			// Review mongoDB examples
			mongoDAO.methodWithMongoTemplate();
			mongoDAO.methodWithMongoTemplateQuery(null, null, null, null);
			mongoDAO.methodWithRepository();

		} catch (Exception e) {

			log.error("[getProcess] Error={}", e.getMessage());
			throw new BusinessException("MONGO", "Mongo Error");

		}

		return getResponse;
	}

	// Create method
	public PostResponse postProcess() {
		log.info("[postProcess]");

		PostResponse postResponse = new PostResponse();
		postResponse.setProcessStatus("OK");

		return postResponse;
	}

	// Update method
	public String putProcess() {
		log.info("[putProcess]");
		return "Hello";
	}

	// Delete method don't have response
	public void deleteProcess() {
		log.info("[deleteProcess]");
	}

}
