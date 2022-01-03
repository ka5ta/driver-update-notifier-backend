package com.ka5ta.drivers.Records;

import com.ka5ta.drivers.Entities.Driver;

import java.util.List;

public record ScrapedResults(List<Driver> drivers, String productName, String productManufacturer, String vendorId) {
}
