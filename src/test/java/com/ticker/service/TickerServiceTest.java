package com.ticker.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ticker.dto.Quote;

public class TickerServiceTest {
	private static final Logger log = LoggerFactory.getLogger(TickerServiceTest.class);
	private static TickerService service;
	private static ScheduledExecutorService executor;
	

	@BeforeAll
	static void setupData() {
		executor = Executors.newSingleThreadScheduledExecutor();
		executor.scheduleAtFixedRate(new TickerService.TickerCacheCleaner(), 1, 1, TimeUnit.SECONDS);
		service = new TickerService(executor);
	}
	
	@Test
	void testInit() {
		assertNotNull(service);
	}

	
	

	@Test
	public void testSaveQuote() {		
		Quote q = new Quote();
		q.setSymbol("A");
		q.setChangeDirection("up");
		q.setPriceTraded(3300);
		q.setChangeInAmount(23.04);
		q.setSharesTraded("22k");
		q.setTimestamp(LocalDateTime.now().minusSeconds(2));		
		service.saveQuote(q);

		assertEquals(1, service.getTopTickers().size());
		service.cleanUp();
	}
	
	@Test
	public void testSaveQuote_TimePast20Min() throws Exception{
		Quote q = new Quote();
		q.setSymbol("A");
		q.setChangeDirection("up");
		q.setPriceTraded(3300);
		q.setChangeInAmount(23.04);
		q.setSharesTraded("22k");
		q.setTimestamp(LocalDateTime.now().minusMinutes(20));
	
		service.saveQuote(q);
		try {
			Thread.sleep(1000);
		}catch(Exception e) {
			log.error(e.getMessage());
		}
		assertEquals(0, service.getTopTickers().size());
		service.cleanUp();
	}
	
	@Test
	public void testSaveQuote_TimePast10Min1sec() throws Exception{
		Quote q = new Quote();
		q.setSymbol("A");
		q.setChangeDirection("up");
		q.setPriceTraded(3300);
		q.setChangeInAmount(23.04);
		q.setSharesTraded("22k");
		q.setTimestamp(LocalDateTime.now().minusMinutes(10).minusSeconds(1));

		service.saveQuote(q);
		try {
			Thread.sleep(1000);
		}catch(Exception e) {
			log.error(e.getMessage());
		}
		assertEquals(0, service.getTopTickers().size());
		service.cleanUp();
	}
	
	@Test
	public void testSaveQuote_TimePast9Min58Sec() throws Exception{
		Quote q = new Quote();
		q.setSymbol("A");
		q.setChangeDirection("up");
		q.setPriceTraded(3300);
		q.setChangeInAmount(23.04);
		q.setSharesTraded("22k");
		q.setTimestamp(LocalDateTime.now().minusMinutes(10).plusSeconds(2));

		service.saveQuote(q);
		try {
			Thread.sleep(1000);
		}catch(Exception e) {
			log.error(e.getMessage());
		}
		assertEquals(1, service.getTopTickers().size());
		service.cleanUp();
	}
	
}
