package com.ka5ta.drivers.Services;

import com.ka5ta.drivers.Entities.Driver;
import com.ka5ta.drivers.Entities.Product;
import com.ka5ta.drivers.Records.ScrapedResults;
import com.ka5ta.drivers.Records.Scraper;
import com.ka5ta.drivers.Repositories.ProductRepository;
import com.ka5ta.drivers.Scrapers.AsusLinkScraper;
import com.ka5ta.drivers.Scrapers.LinkScraper;
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
    private ProductRepository productRepository;


    public Product getProductFromLink(String supportURL) throws Exception {

        Product product = productRepository.findBySupportLink(supportURL);
        if (Objects.isNull(product)) {
            product = new Product(
                    supportURL,
                    productLink,
                    "product test id",
                    "product test name",
                    "product test manufacturer",
                    new ArrayList<>(),
                    null
            );
        }

        if (product.getLastScraped() == null || !product.getLastScraped().toLocalDateTime().toLocalDate().isEqual(LocalDate.now())) {
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

            scrapedDrivers.forEach(scrapedDriver -> {
                String scrapedDriverDriverId= scrapedDriver.getDriverId();
                List<Driver> existingDrivers = toUpdateProduct.getDrivers();

                Optional<Driver> matchingDriver = existingDrivers.stream()
                        .filter(driver -> driver.getDriverId().equals(scrapedDriverDriverId))
                        .findAny();

                if(matchingDriver.isEmpty()){
                    existingDrivers.add(scrapedDriver);
                }else{
                    //todo if any information changed
                }
            });


            List<Driver> drivers = toUpdateProduct.getDrivers();
            drivers.forEach(driver->driver.setProduct(toUpdateProduct));

            productRepository.save(toUpdateProduct);
            return toUpdateProduct;
        }


        if(product.getLastScraped().toLocalDateTime().toLocalDate().isEqual(LocalDate.now())) {
            return product;
        }

        return product;
    }


/*    public List<Driver> getDriversForProduct(String supportURL) throws Exception {

        Product product = productRepository.findBySupportLink(supportURL);
        if (Objects.isNull(product)) {
            product = new Product(
                    supportURL,
                    productLink,
                    "product test name",
                    "product test manufacturer",
                    new ArrayList<>(),
                    null
            );
        }

        List<Driver> drivers = new ArrayList<>();

        if (product.getLastScraped() == null || !product.getLastScraped().isEqual(LocalDate.now())) {
            List<Driver> scrapedDrivers = getScraper(supportURL).getDownloads(supportURL, product);
            product.setLastScraped(LocalDate.now());
            Product toUpdateProduct = product;

            scrapedDrivers.forEach(scrapedDriver -> {
                String scrapedVendorId = scrapedDriver.getVendorId();
                List<Driver> existingDrivers = toUpdateProduct.getDrivers();

                Optional<Driver> matchingDriver = existingDrivers.stream()
                        .filter(driver -> driver.getVendorId().equals(scrapedVendorId))
                        .findAny();

                if(matchingDriver.isEmpty()){
                    existingDrivers.add(scrapedDriver);
                }else{
                    //todo if any information changed
                }
            });
            productRepository.save(toUpdateProduct);
            drivers = toUpdateProduct.getDrivers();
        }


        if (product.getLastScraped().isEqual(LocalDate.now())) {
            return product.getDrivers();
        }

        return drivers;
    }*/

    private Scraper getScraper(String supportURL) {
        if (asusLinkScraper.isLinkSupported(supportURL)) {
            return new Scraper(asusLinkScraper, "ASUS");
        } else if (msiLinkScraper.isLinkSupported(supportURL)) {
            return new Scraper(msiLinkScraper, "MSI");
        } else {
            return null;
        }
    }


}
