package com.ka5ta.drivers.Scrapers;

import com.ka5ta.drivers.Entities.Driver;
import com.ka5ta.drivers.Entities.Product;
import com.ka5ta.drivers.Records.ScrapedResults;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface LinkScraper {
     Boolean isLinkSupported (String link);
     //List<Driver> getDownloads(String productLink, Product product) throws Exception;
     ScrapedResults performScrape (String productLink) throws Exception;


     default Long getDownloadSizeInMB(String downloadLink) throws IOException {
          // Get the url of web
          URI link = URI.create(downloadLink);
          HttpHead head = new HttpHead(link);

          CloseableHttpClient client = HttpClients.createDefault();
          CloseableHttpResponse headResponse = client.execute(head);
          String downloadSize =  headResponse.getHeaders("Content-Length")[0].getValue();

          return Long.parseLong(downloadSize);
     }


}
