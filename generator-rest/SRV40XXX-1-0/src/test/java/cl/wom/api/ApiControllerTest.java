package cl.wom.api;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import cl.wom.api.controller.ApiController;
import cl.wom.api.controller.request.NestedObject;
import cl.wom.api.controller.request.PostRequest;
import cl.wom.api.controller.request.PutRequest;
import cl.wom.api.controller.response.GetResponse;
import cl.wom.api.controller.response.PostResponse;
import cl.wom.api.service.ApiService;

@WebMvcTest(ApiController.class)
@AutoConfigureMockMvc
class ApiControllerTest {

    @MockBean
    private ApiService apiService;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @Test
    void getMappingRequestParamTest() throws Exception {

        // Prepare response
        GetResponse response = new GetResponse();
        response.setProcessStatus("OK");

        // Mock process
        when(apiService.getProcess()).thenReturn(response);

        this.mockMvc.perform(get("/").param("id", "123")) //
                .andDo(print()) //
                .andExpect(status().isOk()) //
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("processStatus").exists())
                .andExpect(jsonPath("processStatus").value("OK"));

    }

    @Test
    void getMappingPathParamTest() throws Exception {

        // Prepare response
        GetResponse response = new GetResponse();
        response.setProcessStatus("OK");

        // Mock process
        when(apiService.getProcess()).thenReturn(response);

        this.mockMvc.perform(get("/").param("id", "123")) //
                .andDo(print()) //
                .andExpect(status().isOk()) //
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("processStatus").exists())
                .andExpect(jsonPath("processStatus").value("OK"));

    }

    @Test
    void postMappingTest() throws Exception {

        // Set valid input
        NestedObject nestedObject = new NestedObject();
        nestedObject.setData("ABC");

        PostRequest input = new PostRequest();
        input.setId("1");
        input.setRut("12345678k");
        input.setName("Pedro");
        input.setEmail("pedro@wom.cl");
        input.setAge(33);
        input.setIpAddress("127.0.0.1");
        input.setNestedObject(nestedObject);

        // Write JSON body as string
        String body = this.objectMapper.writeValueAsString(input);

        // Prepare response
        PostResponse response = new PostResponse();
        response.setProcessStatus("OK");

        // Mock process
        when(apiService.postProcess()).thenReturn(response);

        this.mockMvc.perform(post("/") //
                .contentType(APPLICATION_JSON_UTF8) //
                .content(body)) //
                .andExpect(status().isOk()) //
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("processStatus").exists())
                .andExpect(jsonPath("processStatus").value("OK"));

    }

    @Test
    void postMappingBadRequestTest() throws Exception {

        // Set invalid input
        NestedObject nestedObject = new NestedObject();
        nestedObject.setData("");

        PostRequest input = new PostRequest();
        input.setId(""); // @NotBlank
        input.setRut(""); // @Pattern
        input.setName("A"); // @Size(min = 2, max = 30)
        input.setEmail(".cl"); // @Email
        input.setAge(null); // @NotNull
        input.setIpAddress("127001"); // @Pattern
        input.setNestedObject(nestedObject);

        // Write JSON body as string
        String body = this.objectMapper.writeValueAsString(input);

        // Prepare response
        PostResponse response = new PostResponse();
        response.setProcessStatus("OK");

        // Mock process
        when(apiService.postProcess()).thenReturn(response);

        this.mockMvc.perform(post("/") //
                .contentType(APPLICATION_JSON_UTF8)//
                .content(body)) //
                .andExpect(status().isBadRequest());

    }

    @Test
    void putMappingTest() throws Exception {

        // Set valid input
        PutRequest input = new PutRequest();
        input.setId("123");

        // Write JSON body as string
        String body = this.objectMapper.writeValueAsString(input);

        this.mockMvc.perform(put("/")
                .contentType(APPLICATION_JSON_UTF8) //
                .content(body)) //
                .andExpect(status().isOk()) //
                .andExpect(content().string(containsString("Hello")));

    }

    @Test
    void deleteMappingTest() throws Exception {

        this.mockMvc.perform(delete("/") //
                .param("id", "123") //
                .contentType(APPLICATION_JSON_UTF8)) //
                .andExpect(status().isOk()) //
                .andExpect(content().string(containsString("Hello")));

    }

}
