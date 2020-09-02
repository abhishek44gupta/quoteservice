package com.ticker.controller;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ticker.dto.Quote;
import com.ticker.service.TickerService;


@RestController
public class TickerController {
	private static Logger logger = LoggerFactory.getLogger(TickerController.class);
	
	
	private final TickerService tickerService;

	@Autowired
	public TickerController(TickerService tickerService) {
		this.tickerService = tickerService;
	}
	
	@PostMapping(path = "/quote", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(code = HttpStatus.OK)
	public Quote postQuote(@RequestBody Quote quote) {
		validateQuote(quote);
		logger.info("post quote:"+quote);
		tickerService.saveQuote(quote);
		return quote;
	}
	
	
	@GetMapping(path = "/top5tickers", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Set<Quote>> getTop5Tickers() {
		List<Set<Quote>> topTickers = tickerService.getTopTickers();
		logger.info("getting top 5 tickers:"+topTickers.size());
		return topTickers;
	}

	private void validateQuote(Quote quote) {
		logger.info("validating quote:"+quote);
		LocalDateTime now = LocalDateTime.now();
		
		if(StringUtils.isEmpty(quote.getSymbol())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "symbol can't be blank or null",new RuntimeException());
		}else if(quote.getTimestamp() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "timestamp :"+quote.getTimestamp(),new RuntimeException());
		}else if(now.isBefore(quote.getTimestamp())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "timestamp can't be in future",new RuntimeException());
		}else if(now.minus(10, ChronoUnit.MINUTES).minusSeconds(10).isAfter(quote.getTimestamp())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "timestamp is older than 10 minutes",new RuntimeException());
		}
	}
}
