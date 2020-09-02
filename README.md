# quoteservice
The quote service should have two methods / functions. The first method / function allows a quote to be submitted to the service. The second method / function can be 
used to query the (up to) 5 ticker symbols most frequently received within the last 10 minutes.


An example of a quote in JSON format together with the description of each field / attribute is
shown below. Quotes can be passed directly to the first method / function of the quote service as an
object / struct or individual parameters for each field. JSON parsing / handling is not required to be
part of the solution.
{
"timestamp": "2019-05-05T18:25:43.515Z", // timestamp of the quote
"symbol": "D05.SI", // ticker symbol
"sharesTraded": "5k", // volume for the trade being quoted
"priceTraded": 26.55, // bid price
"changeDirection": "up", // indicates whether stock is trading higher or lower
"changeAmount": 0.17 // difference in price from previous day
}

Steps For running project (Assuming java8 configured on local system):
1. clone the project locally.
2. change to project directory. 
3. Run the below command:
   mvn spring-boot:run
 
 URLs  exposed:
 1. http://localhost:8080/quote (HTTP POST)
 2. http://localhost:8080/top5tickers (HTTP GET)
