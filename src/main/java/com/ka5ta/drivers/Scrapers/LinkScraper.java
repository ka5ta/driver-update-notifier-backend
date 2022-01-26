package com.ka5ta.drivers.Scrapers;

import com.ka5ta.drivers.Entities.Driver;
import com.ka5ta.drivers.Entities.Product;
import com.ka5ta.drivers.Records.ScrapedResults;
import com.ka5ta.drivers.Utility.URIUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public interface LinkScraper {
     Boolean isLinkSupported (String link);
     //List<Driver> getDownloads(String productLink, Product product) throws Exception;
     ScrapedResults performScrape (String productLink) throws Exception;


     default Long getDownloadSizeInMB(String downloadLink) throws IOException, URISyntaxException {

          URI uri = URIUtils.LinkEncoder(downloadLink);

          HttpHead head = new HttpHead(uri);
          CloseableHttpClient client = HttpClients.createDefault();
          CloseableHttpResponse headResponse = client.execute(head);
          String downloadSize =  headResponse.getHeaders("Content-Length")[0].getValue();

          return Long.parseLong(downloadSize);
     }

     default String findRegex(String regex, int groupNumber, String searchText) {
          Pattern pattern = Pattern.compile(regex);
          Matcher matcher = pattern.matcher(searchText);
          matcher.find();
          return matcher.group(groupNumber);
     }


}
