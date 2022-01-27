package com.ka5ta.drivers.Utility;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class URIUtils {

    /** LinkEncoder encodes provided string URL white spaces to ASCII character UTF-8: %20
     *
     * @param link a string URL
     * @return URL with encoded white space to "%20"
     * @throws MalformedURLException
     * @throws URISyntaxException
     */

    public static URI LinkEncoder(String link) throws MalformedURLException, URISyntaxException {

        URL url = new URL(link);
        return new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
    }
}
