package com.ticker.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;

import com.ticker.service.TickerService;

@SpringJUnitConfig(classes = {TickerControllerTest.Config.class})
@WebMvcTest
public class TickerControllerTest {
	private static final Logger log = LoggerFactory.getLogger(TickerControllerTest.class);
	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private TickerController controller;

	
	@Configuration
	static class Config{
			
		@Bean 
		public TickerController getController() {
			return new TickerController(getService());
		}
		
		private TickerService getService() {
			return mock(TickerService.class);
		}
	}
	
	@Test
	void testInit() {
		assertNotNull(controller);
	}
	
	@Test
	void testPostQuote() throws Exception {
		String jsonRequest = "{\r\n" + 
				"\"timestamp\": \""+LocalDateTime.now().minusSeconds(2)+"\", \r\n" + 
				"\"symbol\": \"D06.SI\",\r\n" + 
				"\"sharesTraded\": \"5k\", \r\n" + 
				"\"priceTraded\": 27.55, \r\n" + 
				"\"changeDirection\": \"up\", \r\n" + 
				"\"changeAmount\": 0.18 \r\n" + 
				"}";
		mvc.perform(post("/quote")
									   .contentType(MediaType.APPLICATION_JSON_VALUE)
									   .content(jsonRequest)).andExpect(status().isOk())
				.andReturn();
	}
	
	
	
	@Test
	void testPostQuote_symboleblank() throws Exception {
		String jsonRequest = "{\r\n" + 
				"\"timestamp\": \""+LocalDateTime.now().minusSeconds(2)+"\", \r\n" + 
				"\"symbol\": \"\",\r\n" + 
				"\"sharesTraded\": \"5k\", \r\n" + 
				"\"priceTraded\": 27.55, \r\n" + 
				"\"changeDirection\": \"up\", \r\n" + 
				"\"changeAmount\": 0.18 \r\n" + 
				"}";
	
		mvc.perform(post("/quote")
									   .contentType(MediaType.APPLICATION_JSON_VALUE)
									   .content(jsonRequest)).andExpect(status().isBadRequest())
				.andReturn();
	}
	
	@Test
	void testPostQuote_timestamp_future() throws Exception {
		String jsonRequest = "{\r\n" + 
				"\"timestamp\": \""+LocalDateTime.now().plusSeconds(2)+"\", \r\n" + 
				"\"symbol\": \"SI5.01\",\r\n" + 
				"\"sharesTraded\": \"5k\", \r\n" + 
				"\"priceTraded\": 27.55, \r\n" + 
				"\"changeDirection\": \"up\", \r\n" + 
				"\"changeAmount\": 0.18 \r\n" + 
				"}";
		log.info(jsonRequest);
		mvc.perform(post("/quote")
									   .contentType(MediaType.APPLICATION_JSON_VALUE)
									   .content(jsonRequest)).andExpect(status().isBadRequest())
				.andReturn();
	}
	
	@Test
	void testPostQuote_timestamp_past_10min() throws Exception {
		String jsonRequest = "{\r\n" + 
				"\"timestamp\": \""+LocalDateTime.now().minusMinutes(20)+"\", \r\n" + 
				"\"symbol\": \"SI5.01\",\r\n" + 
				"\"sharesTraded\": \"5k\", \r\n" + 
				"\"priceTraded\": 27.55, \r\n" + 
				"\"changeDirection\": \"up\", \r\n" + 
				"\"changeAmount\": 0.18 \r\n" + 
				"}";
		log.info(jsonRequest);
		mvc.perform(post("/quote")
									   .contentType(MediaType.APPLICATION_JSON_VALUE)
									   .content(jsonRequest)).andExpect(status().isBadRequest())
				.andReturn();
	}
	
	
	
}
