package com.ka5ta.drivers.Entities;

import com.fasterxml.jackson.annotation.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name="profile")
public class EmailProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String email;
    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToMany
    private List<Product> products;
    @CreationTimestamp
    private Timestamp subscribeDate;


    public EmailProfile() {}

    public EmailProfile(String email, List<Product> products) {
        this.email = email;
        this.products = products;
    }

    public EmailProfile(Long id, String email, List<Product> products, Timestamp subscribeDate) {
        this.id = id;
        this.email = email;
        this.products = products;
        this.subscribeDate = subscribeDate;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Timestamp getSubscribeDate() {
        return subscribeDate;
    }

    public void setSubscribeDate(Timestamp subscribeDate) {
        this.subscribeDate = subscribeDate;
    }
}
