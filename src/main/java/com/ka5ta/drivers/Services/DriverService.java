package com.ka5ta.drivers.Services;

import com.ka5ta.drivers.Entities.Driver;
import com.ka5ta.drivers.Entities.Product;
import com.ka5ta.drivers.Records.ScrapedResults;
import com.ka5ta.drivers.Records.Scraper;
import com.ka5ta.drivers.Repositories.ProductRepository;
import com.ka5ta.drivers.Scrapers.AsusLinkScraper;
import com.ka5ta.drivers.Scrapers.AsusRogScraper;
import com.ka5ta.drivers.Scrapers.MsiLinkScraper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
public class DriverService {

    final String productLink = "https://www.asus.com/Motherboards-Components/Motherboards/";

    @Autowired
    private AsusLinkScraper asusLinkScraper;
    @Autowired
    private MsiLinkScraper msiLinkScraper;
    @Autowired
    private AsusRogScraper asusRogLinkScraper;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private EmailService emailService;


    public Product getProductFromLink(String supportURL) throws Exception {

        Product product = productRepository.findBySupportLink(supportURL);
        if (Objects.isNull(product)) {
            product = new Product(
                    supportURL,
                    "product test id",
                    "product test name",
                    "product test manufacturer",
                    new ArrayList<>(),
                    null
            );
        }

        if (product.isScrapeNeeded()) {
            return scrapeAndUpdateProduct(supportURL, product);
        }

        return product;

    }

    private Product scrapeAndUpdateProduct(String supportURL, Product product) throws Exception {
        Scraper scraper = getScraper(supportURL);
        ScrapedResults scrapedResults = scraper.linkScraper().performScrape(supportURL);
        //Getting drivers list
        List<Driver> scrapedDrivers = scrapedResults.drivers();
        //Updating product
        product.setLastScraped(new Timestamp(System.currentTimeMillis()));
        product.setName(scrapedResults.productName());
        product.setManufacturer(scraper.productManufacturer());
        product.setVendorId(scrapedResults.vendorId());
        Product toUpdateProduct = product;

        List<Driver> newDrivers = new ArrayList<>();

        scrapedDrivers.forEach(scrapedDriver -> {
            List<Driver> existingDrivers = toUpdateProduct.getDrivers();
            String scrapedDriverDriverId = scrapedDriver.getDriverId();


            Optional<Driver> matchingDriver = existingDrivers.stream()
                    .filter(driver -> driver.getDriverId().equals(scrapedDriverDriverId))
                    .findAny();

            if (matchingDriver.isEmpty()) {
                existingDrivers.add(scrapedDriver);
                newDrivers.add(scrapedDriver);

            } else {
                //todo if any information changed
            }
        });

        List<Driver> drivers = toUpdateProduct.getDrivers();
        // The relation has to be updated on Owning side or hibernate will not save changes to database
        drivers.forEach(driver -> driver.setProduct(toUpdateProduct));

        productRepository.save(toUpdateProduct);

        // Send email with new drivers list
        if(newDrivers.size() > 0 ){
            emailService.SendEmailWithNewDriverList(toUpdateProduct, newDrivers);
        }
        return toUpdateProduct;
    }

    private Scraper getScraper(String supportURL) {
        if (asusLinkScraper.isLinkSupported(supportURL)) {
            return new Scraper(asusLinkScraper, "ASUS");
        } else if (msiLinkScraper.isLinkSupported(supportURL)) {
            return new Scraper(msiLinkScraper, "MSI");
        } else if (asusRogLinkScraper.isLinkSupported(supportURL)) {
            return new Scraper(asusRogLinkScraper, "ASUS ROG");
        } else {
            return null;
        }
    }


}
