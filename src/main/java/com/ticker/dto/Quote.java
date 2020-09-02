package com.ticker.dto;

import java.time.LocalDateTime;
import java.util.StringJoiner;
public class Quote {
	private LocalDateTime timestamp;
	private String symbol;
	private String sharesTraded;
    private double priceTraded;
    private String changeDirection;
    private double changeInAmount;
    
    public Quote() {
    	
    }
    
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getSharesTraded() {
		return sharesTraded;
	}
	public void setSharesTraded(String sharesTraded) {
		this.sharesTraded = sharesTraded;
	}
	public double getPriceTraded() {
		return priceTraded;
	}
	public void setPriceTraded(double priceTraded) {
		this.priceTraded = priceTraded;
	}
	public String getChangeDirection() {
		return changeDirection;
	}
	public void setChangeDirection(String changeDirection) {
		this.changeDirection = changeDirection;
	}
	
	public double getChangeInAmount() {
		return changeInAmount;
	}

	public void setChangeInAmount(double changeInAmount) {
		this.changeInAmount = changeInAmount;
	}

	public String toString() {
		return new StringJoiner(", ", Quote.class.getSimpleName() + "(", ")")
				.add("symbol="+ symbol )
				.add("timestamp="+ timestamp )
				.add("sharesTraded="+ sharesTraded )
				.add("priceTraded="+ priceTraded )
				.add("changeDirection="+ changeDirection )
				.add("changeInAmount="+ changeInAmount )
				.toString();
	}
	
	
}
