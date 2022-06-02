package com.codefellows.salmonCookies2;

import com.codefellows.salmonCookies2.models.SalmonCookiesStore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Comment this out for now since it is an integration test and we'll be talking about that later
@SpringBootTest
@AutoConfigureMockMvc
class SalmonCookies2ApplicationTests {

	@Autowired
	MockMvc mockMvc;

	@Test
	public void Test_salmon_cookies_store()
	{
		SalmonCookiesStore sut = new SalmonCookiesStore("Test Store Name", 10);

		assertEquals("Test Store Name", sut.getName());
		assertEquals(10, sut.getAverageCookiesPerDay());
	}

	@Test
	void testSalmonCookiesGet() throws Exception{

		mockMvc.perform(get("/"))
				.andDo(print())// shows output on server console
				.andExpect(content().string(containsString("<h1>Salmon Cookie Store Manager</h1>")))
				.andExpect(status().isOk());
	}
}
