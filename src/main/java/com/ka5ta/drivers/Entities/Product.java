package com.ka5ta.drivers.Entities;

import com.fasterxml.jackson.annotation.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;


@Entity
@Table(name="product")
public class Product  {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String supportLink;
    private String productLink;
    private String vendorId;
    private String name;
    private String manufacturer;
    @OneToMany(mappedBy="product", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    private List<Driver> drivers;
    private Timestamp lastScraped;
    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToMany(mappedBy = "products")
    private List<EmailProfile> emailProfiles;

    public Product(){};

    public Product(long id, String supportLink, String productLink,String vendorId, String name, String manufacturer, List<Driver> drivers, Timestamp lastScraped, List<EmailProfile> emailProfiles) {
        this.id = id;
        this.supportLink = supportLink;
        this.productLink = productLink;
        this.vendorId = vendorId;
        this.name = name;
        this.manufacturer = manufacturer;
        this.drivers = drivers;
        this.lastScraped = lastScraped;
        this.emailProfiles = emailProfiles;
    }

    public Product(String supportLink, String productLink, String vendorId, String name, String manufacturer, List<Driver> drivers, Timestamp lastScraped) {
        this.supportLink = supportLink;
        this.productLink = productLink;
        this.vendorId = vendorId;
        this.name = name;
        this.manufacturer = manufacturer;
        this.drivers = drivers;
        this.lastScraped = lastScraped;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSupportLink() {
        return supportLink;
    }

    public void setSupportLink(String supportLink) {
        this.supportLink = supportLink;
    }

    public String getProductLink() {
        return productLink;
    }

    public void setProductLink(String productLink) {
        this.productLink = productLink;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public List<Driver> getDrivers() {
        return drivers;
    }

    public void setDrivers(List<Driver> drivers) {
        this.drivers = drivers;
    }

    public Timestamp getLastScraped() {
        return lastScraped;
    }

    public void setLastScraped(Timestamp lastScraped) {
        this.lastScraped = lastScraped;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", supportLink='" + supportLink + '\'' +
                ", productLink='" + productLink + '\'' +
                ", name='" + name + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", drivers=" + drivers +
                ", lastScraped=" + lastScraped +
                '}';
    }
}
