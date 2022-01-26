package com.ka5ta.drivers.Scrapers;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ka5ta.drivers.Entities.Driver;
import com.ka5ta.drivers.Records.ScrapedResults;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class AsusRogScraper extends AsusLinkScraper implements LinkScraper {
    @Override
    public Boolean isLinkSupported(String link) {
        return link.startsWith("https://rog.asus.com/");
    }

    @Override
    public ScrapedResults performScrape(String productLink) throws Exception {
        List<Driver> drivers = new ArrayList<>();

        AsusProductDetails asusRogProductDetails = getAsusRogProductDetails(productLink);
        List<JSONObject> downloadsJson = getDownloadsJson(productLink, asusRogProductDetails);

        for (JSONObject downloadJson: downloadsJson) {
            Driver driver = new Driver();
            driver.setName(downloadJson.getString("Title"));
            driver.setVersion(downloadJson.getString("Version"));
            driver.setOperatingSys(downloadJson.getString("OS-Version"));
            driver.setReleaseDate(parseStringToDate(downloadJson.getString("ReleaseDate")));
            driver.setDriverId(downloadJson.getString("Id"));
            String downloadLink = downloadJson.getJSONObject("DownloadUrl").getString("Global");
            driver.setFileSizeBytes(getDownloadSizeInMB(downloadLink));
            driver.setDownloadLink(downloadLink);
            drivers.add(driver);
        }


        return new ScrapedResults(drivers, asusRogProductDetails.productName(), "ASUS ROG", asusRogProductDetails.productId());
    }

    private AsusProductDetails getAsusRogProductDetails(String productLink)
            throws IOException, URISyntaxException, ExecutionException, InterruptedException {

        // Get weburl param from URL
        String webURLParam = productLink.replaceFirst("https://rog.asus.com/", "/");

        // Create a Http GET request
        String link = "https://rog.asus.com/recent-data/api/v1/route/info";
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("weburl", webURLParam));

        String response = createGetRequest(link, params);

        // Get Product ID,
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonResponse = mapper.readTree(response);
        JsonNode result = jsonResponse.get("Result");
        String productId = result.get("ProductId").textValue();
        String productName = result.get("WebPathName").textValue();

        return new AsusProductDetails(productId, productName);
    }
}

