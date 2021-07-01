package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class DemoApplicationTests {

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Test
	public void whenFileUploaded_thenVerifyStatus()
			throws Exception {
		MockMultipartFile file
				= new MockMultipartFile(
				"file",
				"hello.txt",
				MediaType.TEXT_PLAIN_VALUE,
				"Hello, World!".getBytes()
		);

		MockMvc mockMvc
				= MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		mockMvc.perform(multipart("/hello").file(file))
				.andExpect(status().isOk());
	}

}
