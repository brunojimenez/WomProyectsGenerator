package cl.wom.api.controller;

import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import cl.wom.api.controller.request.PostRequest;
import cl.wom.api.controller.request.PutRequest;
import cl.wom.api.controller.response.GetResponse;
import cl.wom.api.controller.response.PostResponse;
import cl.wom.api.service.ApiService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@RestController
public class ApiController implements ApiControllerInterface {

    private final ApiService apiService;

    // GET /?id=123
    public ResponseEntity<GetResponse> getMappingRequestParam(String id) {
        log.debug("id={}", id);
        MDC.put("id", id);
        return new ResponseEntity<>(apiService.getProcess(), HttpStatus.OK);
    }

    // GET /123
    public ResponseEntity<GetResponse> getMappingPathParam(String id) {
        log.debug("id={}", id);
        MDC.put("id", id);
        return new ResponseEntity<>(apiService.getProcess(), HttpStatus.OK);
    }

    // POST
    public ResponseEntity<PostResponse> postMapping(PostRequest request) {
        log.debug("request={}", request);
        MDC.put("id", request.getId());
        MDC.put("name", request.getName());
        return new ResponseEntity<>(apiService.postProcess(), HttpStatus.OK);
    }

    // PUT
    public ResponseEntity<String> putMapping(PutRequest request) {
        log.debug("request={}", request);
        MDC.put("id", request.getId());
        return new ResponseEntity<>("Hello", HttpStatus.OK);
    }

    // DELETE /?id=123
    public ResponseEntity<String> deleteMapping(String id) {
        log.debug("request={}", id);
        MDC.put("id", id);
        return new ResponseEntity<>("Hello", HttpStatus.OK);
    }

}
