package cl.wom.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

import cl.wom.api.controller.response.GetResponse;
import cl.wom.api.controller.response.PostResponse;
import cl.wom.api.persistence.MongoDAO;
import cl.wom.api.service.ApiService;

@SpringBootTest
class ApiServiceTest {

    @Mock
    private MongoDAO mongoDAO;

    @Mock
    private RetryTemplate retryTemplate;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ApiService apiService;

    @Test
    void getProcess() {
        GetResponse response = apiService.getProcess();
        assertNotNull(response);
        assertEquals(response.getProcessStatus(), "OK");
    }

    @Test
    void postProcess() {
        PostResponse response = apiService.postProcess();
        assertNotNull(response);
        assertEquals(response.getProcessStatus(), "OK");
    }

    @Test
    void putProcess() {
        String response = apiService.putProcess();
        assertNotNull(response);
        assertEquals(response, "Hello");
    }

}
