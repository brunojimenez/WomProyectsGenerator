package cl.wom.rest.controller;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.wom.rest.controller.request.PostRequest;
import cl.wom.rest.controller.request.PutRequest;
import cl.wom.rest.controller.response.PostResponse;
import cl.wom.rest.service.ApiService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Validated
public class ApiController {

	private ApiService apiService;

	public ApiController(ApiService apiService) {
		this.apiService = apiService;
	}

	// GET /rest/?id=123
	@GetMapping
	public ResponseEntity<String> getMappingRequestParam(@RequestParam(value = "id", defaultValue = "123") String id) {
		log.debug("[getMappingRequestParam] {}", id);
		MDC.put("id", id);
		return new ResponseEntity<>(apiService.getProcess(), HttpStatus.OK);
	}

	// GET /rest/123
	@GetMapping("{id}")
	public ResponseEntity<String> getMappingPathParam(@PathParam("id") String id) {
		log.debug("[getMappingPathParam] {}", id);
		MDC.put("id", id);
		return new ResponseEntity<>(apiService.getProcess(), HttpStatus.OK);
	}

	@PostMapping("doPost")
	public ResponseEntity<PostResponse> postMapping(@Valid @RequestBody PostRequest request) {
		log.debug("[postMapping] {}", request);
		MDC.put("id", request.getId());
		MDC.put("name", request.getName());
		return new ResponseEntity<>(apiService.postProcess(), HttpStatus.OK);
	}

	@PutMapping
	public ResponseEntity<String> putMapping(@Valid @RequestBody PutRequest request) {
		log.debug("[putMapping] {}", request);
		MDC.put("id", request.getId());
		return new ResponseEntity<>("Hello", HttpStatus.OK);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<String> deleteMapping(@PathParam("id") String id) {
		log.debug("[postMapping] {}", id);
		MDC.put("id", id);
		return new ResponseEntity<>("Hello", HttpStatus.OK);
	}

}
