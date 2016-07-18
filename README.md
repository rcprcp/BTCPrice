# BTCPrice
Java program to gather Bitcoin prices from various public sources.  Each data source runs in it's own thread.  I think the result is remarkable CPU-efficient.  There is a lot of wait time, each data source has delays between 20 and 60 seconds between requests.

Some of these prices come from various Bitcoin exchange public APIs, other prices come from scraping web pages.  Bing, Google and QuadrigaHTTP all make HTTP/HTML requests and use Jsoup to parse the returned HTML.  CoinBase, BTCe and OKCoin use their standard, public API, which is HTTP/JOSN requests returning JSON data - subsequently parsed by Gson.  The BitcoinCharts data source uses their experimental streaming API, returning JSON in a record-by-record format.

This program requires gson (tested with version 2.6.1), and Jsoup (tested with version 1.9.21).

It also needs some work.  
* the UI is not so nice - probably should delete some columns.  i was over optimistic about what data is available without accounts on the various exchanges.
* we probably should find additional API-style data and not scrape web pages.  :) 
