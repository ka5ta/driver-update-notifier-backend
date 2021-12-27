package com.ka5ta.drivers.Services;

import com.ka5ta.drivers.DTOs.SubscribeDTO;
import com.ka5ta.drivers.Entities.EmailProfile;
import com.ka5ta.drivers.Entities.Product;
import com.ka5ta.drivers.Repositories.ProductRepository;
import com.ka5ta.drivers.Repositories.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepo;
    @Autowired
    private ProductRepository productRepository;


    public void setSubscription(SubscribeDTO subDTO) {
        String email = subDTO.getEmail();
        Long productId = subDTO.getProductId();

        Product product = productRepository.findById(productId).orElseThrow();

        //check if email doesn't subscribe already
        EmailProfile emailProfile = subscriptionRepo.findByEmail(email);
        if (emailProfile == null) {
            //create new email subscription
            List<Product> products = new ArrayList<>();
            products.add(product);
            EmailProfile newEmailProfile = new EmailProfile(email, products);
            subscriptionRepo.save(newEmailProfile);
        }

        if (emailProfile != null) {
            List<Product> products = emailProfile.getProducts();
            Optional<Product> foundProduct = products.stream()
                    .filter(existingProduct -> existingProduct.equals(product))
                    .findAny();

            if(foundProduct.isEmpty()){
                products.add(product);
                //update subscription
                subscriptionRepo.save(emailProfile);
            }
        }
    }
}
