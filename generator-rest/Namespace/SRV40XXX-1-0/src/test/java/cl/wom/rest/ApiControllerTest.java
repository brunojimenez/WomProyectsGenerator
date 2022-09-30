package cl.wom.rest;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import cl.wom.rest.controller.ApiController;
import cl.wom.rest.controller.request.PostRequest;
import cl.wom.rest.service.ApiService;

@WebMvcTest(ApiController.class)
class ApiControllerTest {

	@MockBean
	private ApiService apiService;

	private MockMvc mockMvc;

	private ObjectMapper objectMapper = new ObjectMapper();

	public ApiControllerTest(MockMvc mockMvc) {
		this.mockMvc = mockMvc;
	}

	@Test
	void getMappingRequestParamTest() throws Exception {

		when(apiService.getProcess()).thenReturn("Hello");

		this.mockMvc.perform(get("/api?id=123")) //
				.andDo(print()) //
				.andExpect(status().isOk()) //
				.andExpect(content().string(containsString("Hello")));

	}

	@Test
	void getMappingPathParamTest() throws Exception {

		when(apiService.getProcess()).thenReturn("Hello");

		this.mockMvc.perform(get("/api/123")) //
				.andDo(print()) //
				.andExpect(status().isOk()) //
				.andExpect(content().string(containsString("Hello")));

	}

	@Test
	void postMappingTest() throws Exception {

		PostRequest input = new PostRequest();
		input.setId("1");
		input.setName("Pedro");
		input.setEmail("pedro@wom.cl");
		input.setAge(33);
		input.setIpAddress("127.0.0.1");

		String body = this.objectMapper.writeValueAsString(input);

		this.mockMvc.perform(post("/api") //
				.contentType("application/json") //
				.content(body)) //
				.andExpect(status().isOk());

	}

	@Test
	void postMappingBadRequestTest() throws Exception {

		PostRequest input = new PostRequest();
		input.setId(""); // @NotBlank
		input.setName("A"); // @Size(min = 2, max = 30)
		input.setEmail(".cl"); // @Email
		input.setAge(null); // @NotNull
		input.setIpAddress("127001"); // @Pattern

		String body = this.objectMapper.writeValueAsString(input);

		this.mockMvc.perform(post("/api") //
				.contentType("application/json") //
				.content(body)) //
				.andExpect(status().isBadRequest());
	}

}
