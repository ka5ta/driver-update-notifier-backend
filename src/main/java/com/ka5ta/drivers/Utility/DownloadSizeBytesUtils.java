package com.ka5ta.drivers.Utility;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class DownloadSizeBytesUtils {

    public static double sizeInMB(String downloadLink) throws IOException, URISyntaxException {

        URI uri = URIUtils.LinkEncoder(downloadLink);

        HttpHead head = new HttpHead(uri);
        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse headResponse = client.execute(head);
        String downloadSize =  headResponse.getHeaders("Content-Length")[0].getValue();

        long sizeInBytes = Long.parseLong(downloadSize);
        double sizeInMB = sizeInBytes / Math.pow(1024,2);

        return round(sizeInMB, 2);
    }

    private static double round(double number, int digits){
        double multiplied = number * Math.pow(10,digits);
        double multipliedRounded = Math.round(multiplied);
        double rounded = multipliedRounded / Math.pow(10,digits);

        return rounded;
    }
}
