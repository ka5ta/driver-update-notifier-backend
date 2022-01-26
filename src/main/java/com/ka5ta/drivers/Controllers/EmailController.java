package com.ka5ta.drivers.Controllers;

import com.ka5ta.drivers.DTOs.SubscribeDTO;
import com.ka5ta.drivers.Entities.Driver;
import com.ka5ta.drivers.Entities.EmailProfile;
import com.ka5ta.drivers.Entities.Product;
import com.ka5ta.drivers.Repositories.SubscriptionRepository;
import com.ka5ta.drivers.Services.EmailService;
import com.ka5ta.drivers.Services.SubscriptionService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;




@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("api")
public class EmailController {

    final String testEmail = "kasta.urbanska@gmail.com";

    @Autowired
    private SubscriptionService subscriptionService;


    @PostMapping("/subscribe")
    public void createEmailProfile (@RequestBody SubscribeDTO dto){
        subscriptionService.setSubscription(dto);
        //return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("/unsubscribe")
    public void deleteEmailProfileByProduct (@RequestParam String email, @RequestParam Long productId){
        subscriptionService.unsubscribe(email, productId);
    }



}


