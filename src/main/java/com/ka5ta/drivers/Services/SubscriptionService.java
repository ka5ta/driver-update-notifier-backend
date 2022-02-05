package com.ka5ta.drivers.Services;

import com.ka5ta.drivers.DTOs.SubscribeDTO;
import com.ka5ta.drivers.Entities.EmailProfile;
import com.ka5ta.drivers.Entities.Product;
import com.ka5ta.drivers.Repositories.ProductRepository;
import com.ka5ta.drivers.Repositories.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;


@Service
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepo;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private EmailService emailService;


    public void setSubscription(SubscribeDTO subDTO) throws IOException, ExecutionException, InterruptedException {
        String email = subDTO.getEmail();
        Long productId = subDTO.getProductId();

        Product product = productRepository.findById(productId).orElseThrow();
        EmailProfile emailProfile = subscriptionRepo.findByEmail(email);

        //check if email doesn't subscribe already
        if (emailProfile == null) {
            //create new email subscription
            List<Product> products = new ArrayList<>();
            products.add(product);
            EmailProfile newEmailProfile = new EmailProfile(email, products);
            subscriptionRepo.save(newEmailProfile);
            emailService.sendWelcomeEmail(email);
        }else{

            List<Product> profileProducts = emailProfile.getProducts();
                    /*
                    Optional<Product> foundProduct = profileProducts.stream()
                    .filter(existingProduct -> existingProduct.equals(product))
                    .findAny();*/

            if( ! profileProducts.contains(product)){
                profileProducts.add(product);
                //update subscription
                subscriptionRepo.save(emailProfile);
            }
        }
    }

    public void unsubscribe(String email, Long productId){
        Product productToRemove = productRepository.findById(productId).orElseThrow();
        EmailProfile profile = subscriptionRepo.findByEmail(email);

        List<Product> profileProducts = profile.getProducts();
        if(profileProducts.contains(productToRemove)) {
            profileProducts.remove(productToRemove);
            System.out.println("product "+ productToRemove.getId() + " is removed.");
            subscriptionRepo.save(profile);
            System.out.println("Email profile saved");
        }else{
            System.out.println("There is no such product in profile");
        }


    }
}
