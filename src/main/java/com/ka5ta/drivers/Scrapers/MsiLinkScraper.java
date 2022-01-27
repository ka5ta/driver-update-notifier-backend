package com.ka5ta.drivers.Scrapers;

import com.ka5ta.drivers.Entities.Driver;
import com.ka5ta.drivers.Records.ScrapedResults;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
public class MsiLinkScraper implements LinkScraper {

    @Override
    public Boolean isLinkSupported(String link) {
        return link.startsWith("https://www.msi.com/");
    }

    @Override
    public ScrapedResults performScrape(String supportLink)
            throws Exception {
        MSIProductDetails msiProductDetails = getMSIProductDetails(supportLink);
        String downloadsHtml = getDownloadsHtml(msiProductDetails.productId());
        List<Element> driverContainers =  getDriverContainers(downloadsHtml);

        List<Driver> drivers = new ArrayList<>();
        for (Element driverContainer: driverContainers) {
            Driver driver = extractDriver(driverContainer);
            //todo does vendorID should be on vendor instead ?
            drivers.add(driver);
        }
//        Element driverInfo = driverContainers.stream().map(this::extractDriverInfo).collect(Collectors.toList());

        return new ScrapedResults(drivers, msiProductDetails.productName(), "MSI", msiProductDetails.productId());
    }


    public String getDownloadsHtml(String productId)
            throws ExecutionException, InterruptedException, IOException {

        String msiLink = "https://www.msi.com/product_ajax/get_support_item";

        //      POST     //
        // Create a Http Client
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // Request parameters and other properties;
        List<NameValuePair> params = new ArrayList<NameValuePair>(3);
        params.add(new BasicNameValuePair("support_type", "download"));
        params.add(new BasicNameValuePair("type", "driver"));
        params.add(new BasicNameValuePair("product_id", productId));
        // Execute GET request
        HttpPost httpPostRequest = new HttpPost(msiLink);
        httpPostRequest.setEntity(new UrlEncodedFormEntity(params));
        // Get response
        CloseableHttpResponse httpResponse = httpClient.execute(httpPostRequest);
        String response = EntityUtils.toString(httpResponse.getEntity());

        return response;
    }

    private MSIProductDetails getMSIProductDetails(String supportLink) throws
            ExecutionException, InterruptedException {

        // Create a GET request
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create(supportLink))
                .GET()
                .build();

        // Send request aSync
        CompletableFuture<HttpResponse<String>> httpFutureResponse = HttpClient
                .newBuilder()
                .build()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString());

        // Get response body as String
        String response = httpFutureResponse.get().body();

        // Find matching text for product_id and find product name
        String regexProductId = "product_id:(\\s+)?(\\d+),";
        String regexProductName = "category:(\\s+)?\'(.*)\',(\\n|\\r|\\s+|\\t)title:(\\s+)?\'(.*)\',";

        String resultProductId = findRegex(regexProductId, 2, response);
        String resultProductName = findRegex(regexProductName, 5, response);

        return new MSIProductDetails(resultProductId, resultProductName);
    }


    private List<Element> getDriverContainers(String downloadsHtml) {
        // Parse HTML
        Document document = Jsoup.parse(downloadsHtml);
        // Find all drivers blocks
        Elements driverSourceBlocks = document.select(".download-box-one-div");
        return driverSourceBlocks;
    }

    private Driver extractDriver (Element driverContainer) throws Exception {
        //Element driverElement = getOnlyMatch(driverContainer, ".row.spec");

        // Initialize Driver
        Driver driver = new Driver();

        // Get download link
        Element downloadLinkElement = getOnlyMatch(driverContainer, ".download-btn");
        String downloadLink = getOnlyMatch(downloadLinkElement, "a[href]").attr("href");
        driver.setDownloadLink(downloadLink);

        // Get driver download link size in MB
        Long downloadSize = getDownloadSizeInMB(downloadLink);
        driver.setFileSizeBytes(downloadSize);


        // Get driver name, version and release date. Add them to driver attributes.
        Map<String, String> driverPairs = getElementPairs(driverContainer);
        String title = driverPairs.get("Title");
        driver.setName(title);
        String version = driverPairs.get("Version");
        driver.setVersion(version);
        LocalDate date = parseStringToDate(driverPairs.get("Release Date"));
        driver.setReleaseDate(date);
        String operatingSys = driverPairs.get("OS");
        driver.setOperatingSys(operatingSys);

        //Generate driver Id
        StringBuilder sb = new StringBuilder();
        String generatedID = sb.append(title).append("-").append(version).append("-").append(downloadLink).toString();
        driver.setDriverId(generatedID);
        return driver;
    }

    private Map<String, String> getElementPairs (Element driverSpec) throws Exception {
    Map<String, String> driverSpecPairs = new HashMap<>();

        Elements theaders = driverSpec.select(".theader");
        for (Element theader:theaders) {
            String headerText = theader.text();
            String bodyText = getOnlyMatch(theader.parent(),".tbody").text();
            driverSpecPairs.put(headerText, bodyText);
        }
        return driverSpecPairs;
    }


    private Element getOnlyMatch(Element element, String query) throws Exception {
        Elements foundElements= element.select(query);
        if(foundElements.size()!=1){
            throw new Exception("There is no result for query " + query + "or multiple elements were selected");
        }
        Element foundOnlyMatch = foundElements.first();
        return foundOnlyMatch;
    }

    private LocalDate parseStringToDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate formattedDate = LocalDate.parse(date, formatter);
        return formattedDate;
    }


}

record MSIProductDetails(String productId, String productName) { }
