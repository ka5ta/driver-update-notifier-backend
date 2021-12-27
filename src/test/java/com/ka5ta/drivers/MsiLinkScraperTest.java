package com.ka5ta.drivers;

import com.ka5ta.drivers.Scrapers.MsiLinkScraper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class MsiLinkScraperTest {

    @Test
    void contextLoads() {
    }

    MsiLinkScraper msi = new MsiLinkScraper();

    @Test
    void isDownloadsHtmlString () throws IOException, ExecutionException, InterruptedException {
        String downloadsHtml = msi.getDownloadsHtml("https://www.msi.com/Motherboard/B450-TOMAHAWK-MAX/support");
        System.out.println(downloadsHtml);
        assertTrue(downloadsHtml != null);
    }




}
