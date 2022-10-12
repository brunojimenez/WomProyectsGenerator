package cl.wom.api.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.websocket.server.PathParam;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cl.wom.api.controller.request.PostRequest;
import cl.wom.api.controller.request.PutRequest;
import cl.wom.api.controller.response.GetResponse;
import cl.wom.api.controller.response.PostResponse;

@Validated
@RequestMapping("${server.servlet.context-path}")
public interface ApiControllerInterface {

	@GetMapping
	public ResponseEntity<GetResponse> getMappingRequestParam( //
			@RequestParam(value = "id", defaultValue = "123") String id //
	);

	@GetMapping("{id}")
	public ResponseEntity<GetResponse> getMappingPathParam( //
			@NotNull @NotEmpty @PathParam("id") String id //
	);

	@PostMapping("doPost")
	public ResponseEntity<PostResponse> postMapping( //
			@Valid @RequestBody PostRequest request //
	);

	@PutMapping
	public ResponseEntity<String> putMapping( //
			@Valid @RequestBody PutRequest request //
	);

	@DeleteMapping("{id}")
	public ResponseEntity<String> deleteMapping(@PathParam("id") String id);

}
