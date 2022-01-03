package com.ka5ta.drivers.Scrapers;

import com.ka5ta.drivers.Entities.Driver;
import com.ka5ta.drivers.Entities.Product;
import com.ka5ta.drivers.Records.ScrapedResults;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AsusLinkScraper implements LinkScraper {
    @Override
    public Boolean isLinkSupported(String link) {
        return link.startsWith("https://www.asus.com/");
    }

    @Override
    public ScrapedResults performScrape(String productLink) throws Exception {
        List<Driver> drivers = new ArrayList<>();

        AsusProductDetails asusProductDetails = getAsusProductDetails(productLink);
        List<JSONObject> downloadsJson = getDownloadsJson(productLink, asusProductDetails);

        for (JSONObject downloadJson:downloadsJson) {
            Driver driver = new Driver();
            driver.setName(downloadJson.getString("Title"));
            driver.setVersion(downloadJson.getString("Version"));
            driver.setOperatingSys(downloadJson.getString("OS-Version"));
            driver.setReleaseDate(parseStringToDate(downloadJson.getString("ReleaseDate")));
            driver.setDriverId(downloadJson.getString("Id"));
            //driver.setProduct(product);
            drivers.add(driver);
            //todo Encode download link
            String downloadLink = downloadJson.getJSONObject("DownloadUrl").getString("Global");
            //String encodedDownloadLink = URLEncoder.encode(downloadLink, StandardCharsets.UTF_8.name());
            driver.setFileSizeBytes(getDownloadSizeInMB(downloadLink));
            driver.setDownloadLink(downloadLink);
        }
        return new ScrapedResults(drivers, asusProductDetails.productName(),"ASUS", asusProductDetails.productId());
    }

    private List<JSONObject> getDownloadsJson (String productLink, AsusProductDetails asusProductDetails)
            throws JSONException, URISyntaxException, IOException, ExecutionException, InterruptedException, ParseException {
        //Get OS systems by specific card

        Map<String, String> operatingSystemIDs = getOperatingSystemIDs(asusProductDetails);
        List<JSONObject> downloadsJson = new ArrayList<>();

        for(Map.Entry<String, String> entry:operatingSystemIDs.entrySet() ){
            String key = entry.getKey();
            String value = entry.getValue();
            List<JSONObject> downloadsJSONByOS = getDownloadsJSONByOS(productLink, key, value);
            downloadsJson.addAll(downloadsJSONByOS);
        }

        return downloadsJson;
    }


    private List<JSONObject> getDownloadsJSONByOS(String productLink, String operatingSystemKey, String operatingSystemValue)
            throws IOException, ExecutionException, InterruptedException, URISyntaxException, ParseException, JSONException {

        String productId = getAsusProductDetails(productLink).productId();
        String productModel = getAsusProductDetails(productLink).productName();


        //      Execute GET request    //
        //Create parameters list
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("website", "global"));
        params.add(new BasicNameValuePair("osid", operatingSystemKey));
        params.add(new BasicNameValuePair("pdid", productId));
        params.add(new BasicNameValuePair("model", productModel));

        String allDriversLink = "https://www.asus.com/support/api/product.asmx/GetPDDrivers";

        String response = createGetRequest(allDriversLink, params);

        //Create JSON response
        JSONObject responseJson = new JSONObject(response);

        //Extract drivers list in Json to new list
        List<JSONObject> driversJsonList = new ArrayList<>();
        JSONArray driversJsonContainer = responseJson.getJSONObject("Result").getJSONArray("Obj");
        for (int groupNumber = 0; groupNumber < driversJsonContainer.length(); groupNumber++) {
            JSONObject driverJsonType = driversJsonContainer.getJSONObject(groupNumber);
            JSONArray driversJson = driverJsonType.getJSONArray("Files");
            for (int i = 0; i < driversJson.length(); i++) {
                JSONObject jsonDriver = driversJson.getJSONObject(i);
                jsonDriver.put("OS-Version",operatingSystemValue);
                driversJsonList.add(jsonDriver);
            }
        }
        return driversJsonList;
    }

    private String createGetRequest(String link, List<NameValuePair> params)
            throws URISyntaxException, ExecutionException, InterruptedException, IOException {
        // Create a Http Client
        CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();

        //Start the client
        httpclient.start();

        HttpGet httpGetRequest = new HttpGet(link);

        URI uri = new URIBuilder(httpGetRequest.getURI())
                .addParameters(params)
                .build();
        httpGetRequest.setURI(uri);

        Future<HttpResponse> future = httpclient.execute(httpGetRequest, null);
        HttpResponse futureResponse = future.get();
        String response = EntityUtils.toString(futureResponse.getEntity());
        httpclient.close();

        return response;
    }

    private AsusProductDetails getAsusProductDetails(String productLink)
            throws URISyntaxException, IOException, ExecutionException, InterruptedException {

        String TestProductLink = "https://www.asus.com/Motherboards-Components/Motherboards/PRIME/PRIME-Z690-A-CSM/";
        String response = Jsoup.connect(productLink).get().html();

        String regexProductId = "\"sku\":(\\s+)?(\\d+),";
        String regexProductName = "\"@type\": \"Product\",(\\n|\\r|\\s+|\\t)\"@id\": \"(.*?)\",(\\n|\\r|\\s+|\\t)\"name\":(\\s+)?\"(.*?)\"";


        String productId = findRegex(regexProductId, 2, response);
        String productName = findRegex(regexProductName, 5, response);

        return new AsusProductDetails(productId,productName);
    }


    private Map<String, String> getOperatingSystemIDs(AsusProductDetails asusProductDetails)
            throws URISyntaxException, IOException, ExecutionException, InterruptedException, JSONException {
        Map<String, String> operatingSystemOptions = new HashMap<>();

        String OSlink = "https://www.asus.com/support/api/product.asmx/GetPDOS";

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("website", "global"));
        params.add(new BasicNameValuePair("model", asusProductDetails.productName() ));
        params.add(new BasicNameValuePair("pdid", asusProductDetails.productId() ));

        String response = createGetRequest(OSlink, params);

        final JSONObject responseJson = new JSONObject(response);
        JSONArray OSjsonPairs = responseJson.getJSONObject("Result").getJSONArray("Obj");
        for(int j=0; j < OSjsonPairs.length(); j++){
            JSONObject o = (JSONObject) OSjsonPairs.get(j);
            String id = o.getString("Id");
            String name = o.getString("Name");
            operatingSystemOptions.put(id, name);
        }

        return operatingSystemOptions;
    }

    private String getDriverHashedID(String asusDriverID) {
        //todo how to get hashed id ?

        return "7JylTwYxnZk7gfUT";
    }

    private String getDriverModel(String productLink) {
        //productLink = "https://www.asus.com/Motherboards-Components/Motherboards/PRIME/PRIME-Z690-A-CSM/";
        String[] splitedURL = productLink.split("/");
        return splitedURL[splitedURL.length-1];
    }

    private List<JSONObject> getMotherboardsGroups ()
            throws IOException, URISyntaxException, ExecutionException, InterruptedException, JSONException {

        //todo Get typeID for product: motherboard from asus dropdown list
        Jsoup.connect("https://www.asus.com/support/Download-Center/").get().select(".selectDropdown.select2");
        String typeId = "8644";

        // Get list of motherboard groups with their Id's
        String motherboardGroupsLink = "https://www.asus.com/support/api/product.asmx/GetPDLevel";
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("typeid", typeId));
        params.add(new BasicNameValuePair("productflag", "0"));
        params.add(new BasicNameValuePair("type", "1"));
        params.add(new BasicNameValuePair("website", "global"));
        String responseMotherboardGroupsHtml = createGetRequest(motherboardGroupsLink, params);

        //Create JSON from HTML response
        final JSONObject responseJson = new JSONObject(responseMotherboardGroupsHtml);

        //Extract motherboard groups ID's list in JSON to new list
        List<JSONObject> motherboardJsonPairs = new ArrayList<>();
        JSONArray motherboardsJsonContainer = responseJson
                .getJSONObject("Result")
                .getJSONObject("ProductLevel")
                .getJSONObject("Products")
                .getJSONArray("Items");
        for(int groupNumber = 0; groupNumber < motherboardsJsonContainer.length(); groupNumber++ ){
            JSONObject motherboardJsonPair = motherboardsJsonContainer.getJSONObject(groupNumber);
            //System.out.println("driverJsonType.optString(\"Id\") = " + driverJsonType.optString("Id"));
            //System.out.println("driverJsonType.optString(\"Name\") = " + driverJsonType.optString("Name"));
            motherboardJsonPairs.add(motherboardJsonPair);
        }
        return motherboardJsonPairs;
    }

    private LocalDate parseStringToDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate formattedDate = LocalDate.parse(date, formatter);
        return formattedDate;
    }

}

record AsusProductDetails(String productId, String productName){};
