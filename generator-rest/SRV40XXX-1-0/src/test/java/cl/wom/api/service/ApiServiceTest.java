package cl.wom.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.nio.charset.Charset;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import cl.wom.api.controller.response.GetResponse;
import cl.wom.api.controller.response.PostResponse;
import cl.wom.api.exception.BusinessException;
import cl.wom.api.external.publicapis.to.PublicapisResponse;
import cl.wom.api.persistence.MongoDAO;

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

    @BeforeEach
    void setUp() {

    }

    @Test
    void getProcess() {

        // Mocking call
        doNothing().when(mongoDAO).methodWithRepository();

        // Mocking call
        doNothing().when(mongoDAO).methodWithMongoTemplate(isA(String.class), isA(Date.class), isA(Date.class),
                isA(Boolean.class));

        // Mocking call
        when(restTemplate.getForObject(anyString(), any()))
                .thenReturn(new PublicapisResponse());

        GetResponse response = apiService.getProcess();
        assertNotNull(response);
        assertEquals(response.getProcessStatus(), "OK");

    }

    @Test
    void getProcessThrowMongoException() {

        // Mocking call
        doThrow(NullPointerException.class).when(mongoDAO).methodWithRepository();
        assertThrows(BusinessException.class, () -> apiService.getProcess());

    }

    @Test
    void getProcessThrowRestTemplateException() {

        // Mocking call
        doNothing().when(mongoDAO).methodWithRepository();

        // Mocking call
        doNothing().when(mongoDAO).methodWithMongoTemplate(isA(String.class), isA(Date.class), isA(Date.class),
                isA(Boolean.class));

        // Build http client exception (with body)
        byte[] body = "any body".getBytes();

        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR.toString(), body,
                Charset.defaultCharset());

        // Throw HTTP client error
        doThrow(exception).when(restTemplate).getForObject(anyString(), any());
        assertThrows(BusinessException.class, () -> apiService.getProcess());

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

    @Test
    void deleteProcess() {
        // Nothing to test
        apiService.deleteProcess();

    }

}
