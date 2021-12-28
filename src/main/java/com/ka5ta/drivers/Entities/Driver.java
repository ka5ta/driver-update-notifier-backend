package com.ka5ta.drivers.Entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;


@Entity
@Table(name="driver")
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne
    private Product product;
    private String name;
    private String vendorId;
    private String version;
    private String operatingSys;
    private String downloadLink;
    private LocalDate releaseDate;
    private long fileSizeBytes;
    @CreatedDate
    private Timestamp updatedOn;

    @PrePersist
    protected void onCreate() {
        updatedOn = new Timestamp(System.currentTimeMillis());;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedOn = new Timestamp(System.currentTimeMillis());;
    }


    public Driver(){
    };

    public Driver(long id, Product product, String vendorId, String name, String version, String operatingSys, LocalDate releaseDate, Long fileSizeBytes, String downloadLink) {
        this.id = id;
        this.product = product;
        this.vendorId = vendorId;
        this.name = name;
        this.version = version;
        this.operatingSys = operatingSys;
        this.downloadLink = downloadLink;
        this.releaseDate = releaseDate;
        this.fileSizeBytes = fileSizeBytes;
    }


    public Driver(String name, String vendorId, Product product, String version, String operatingSys, LocalDate releaseDate, Long fileSizeBytes, String downloadLink, Timestamp createdOn) {
        this.name = name;
        this.vendorId = vendorId;
        this.product = product;
        this.version = version;
        this.operatingSys = operatingSys;
        this.releaseDate = releaseDate;
        this.fileSizeBytes = fileSizeBytes;
        this.downloadLink = downloadLink;
        this.updatedOn = createdOn;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Long getFileSizeBytes() {
        return fileSizeBytes;
    }

    public void setFileSizeBytes(Long fileSizeBytes) {
        this.fileSizeBytes = fileSizeBytes;
    }

    public String getOperatingSys() {
        return operatingSys;
    }

    public void setOperatingSys(String operatingSys) {
        this.operatingSys = operatingSys;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "Driver{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", operatingSys='" + operatingSys + '\'' +
                ", downloadLink='" + downloadLink + '\'' +
                ", releaseDate=" + releaseDate +
                ", fileSizeBytes=" + fileSizeBytes +
                ", createdOn=" + updatedOn +
                '}';
    }
}





