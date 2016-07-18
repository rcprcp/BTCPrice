# BTCPrice
Java program to gather Bitcoin prices from various public sources.  Each data collector runs in it's own thread.  I think the result is remarkable CPU-efficient.  this program uses less than 1% CPU on a reasonably powered laptop. 

Some of these prices come from various Bitcoin exchange APIs, other prices come from scraping web pages. 

This program requires gson (tested with version 2.6.1), and Jsoup (tested with version 1.9.21).

It also needs some work.  
* the UI is not so nice - probably should delete some columns.  i was over optimistic about what data is available without accounts on the various exchanges.
* we probably should find additional API-style data and not scrape web pages.  :) 
