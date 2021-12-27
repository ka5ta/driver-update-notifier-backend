package com.ka5ta.drivers.Entities;

import com.fasterxml.jackson.annotation.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
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
    @ManyToMany
    private List<Product> products;
    @CreatedDate
    private LocalDate subscribeDate;


    public EmailProfile() {}

    public EmailProfile(String email, List<Product> products) {
        this.email = email;
        this.products = products;
    }

    public EmailProfile(Long id, String email, List<Product> products, LocalDate subscribeDate) {
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

    public LocalDate getSubscribeDate() {
        return subscribeDate;
    }

    public void setSubscribeDate(LocalDate subscribeDate) {
        this.subscribeDate = subscribeDate;
    }
}
