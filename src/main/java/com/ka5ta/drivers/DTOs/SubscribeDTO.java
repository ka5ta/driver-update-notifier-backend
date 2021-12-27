package com.ka5ta.drivers.DTOs;

import com.ka5ta.drivers.Entities.Product;
import org.springframework.stereotype.Component;

import java.io.Serializable;


public class SubscribeDTO {
    String email;
    Long productId;

    public SubscribeDTO(String email, Long product) {
        this.email = email;
        this.productId = product;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getProductId() {
        return productId;
    }


}
