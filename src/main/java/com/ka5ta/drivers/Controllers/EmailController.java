package com.ka5ta.drivers.Controllers;

import com.ka5ta.drivers.DTOs.SubscribeDTO;
import com.ka5ta.drivers.Entities.EmailProfile;
import com.ka5ta.drivers.Entities.Product;
import com.ka5ta.drivers.Repositories.SubscriptionRepository;
import com.ka5ta.drivers.Services.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "http://192.168.1.54:4200/", maxAge = 3600)
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

}
