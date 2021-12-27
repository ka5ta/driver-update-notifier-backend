package com.ka5ta.drivers.Scrapers;

public class UnsupportedLinkException extends RuntimeException {

    public UnsupportedLinkException(String link){
        super("The link is wrong: "+ link);
    }
}
