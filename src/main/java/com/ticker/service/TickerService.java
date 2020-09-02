package com.ticker.service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.ticker.dto.Quote;


@Service
public class TickerService {
	
	private static Logger logger = LoggerFactory.getLogger(TickerService.class);
	private static final Map<String,Set<Quote>> tickerCache = new ConcurrentHashMap<>();
	private static final DelayQueue<TickerEvent> tickerQueue = new DelayQueue<>();
	private final ScheduledExecutorService executor; 

	@Autowired
	public TickerService() {
		executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(new TickerCacheCleaner(), 1, 10, TimeUnit.SECONDS);
	}
	
	
	public void cleanUp() {
		tickerCache.clear();
		tickerQueue.clear();
	}

	public TickerService(ScheduledExecutorService executor) {
		this.executor = executor;
	}
	
	public void saveQuote(@NonNull Quote quote) {	
		tickerCache.computeIfAbsent(quote.getSymbol(), k -> new LinkedHashSet<Quote>()).add(quote);
		tickerQueue.add(new TickerEvent(quote));
	}
	
	public List<Set<Quote>> getTopTickers(){
		logger.info("getting top 5 ticker quotes");
		List<String> topKeys = tickerCache.entrySet().stream()
				.collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().size())).entrySet().stream()
				.sorted(Map.Entry.<String, Integer>comparingByValue().reversed()).limit(5).map(Map.Entry::getKey)
				.collect(Collectors.toList());
		return topKeys.stream().map( key -> tickerCache.get(key)).collect(Collectors.toList());
	}
	
	public static class TickerCacheCleaner implements Runnable 
	{ 
	    @Override
	    public void run() 
	    {        
	        logger.debug("queue before drain:"+tickerQueue);
	        int size = tickerQueue.size();
	        List<TickerEvent> events = new ArrayList<TickerEvent>();
	        tickerQueue.drainTo(events);
	        logger.info(events.size()+" elements are drained from queue of size:"+size);
	        events.stream().forEach( te -> {
	        	Quote quote = te.getQuote();
	        	logger.info("removing quote from tickercache:"+quote);
	        	tickerCache.get(quote.getSymbol()).remove(quote);
	        	if(tickerCache.get(quote.getSymbol()).size() == 0) {
	        		tickerCache.remove(quote.getSymbol());
	        	}
	        });
	    }
	}
}
