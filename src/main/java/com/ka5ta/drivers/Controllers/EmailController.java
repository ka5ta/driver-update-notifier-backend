package com.ka5ta.drivers.Controllers;

import com.ka5ta.drivers.DTOs.SubscribeDTO;
import com.ka5ta.drivers.Entities.Driver;
import com.ka5ta.drivers.Entities.EmailProfile;
import com.ka5ta.drivers.Entities.Product;
import com.ka5ta.drivers.Repositories.SubscriptionRepository;
import com.ka5ta.drivers.Services.EmailService;
import com.ka5ta.drivers.Services.SubscriptionService;
import j2html.tags.ContainerTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static j2html.TagCreator.*;


@CrossOrigin(origins = {"http://192.168.1.54:4200/", "http://localhost:4200/"}, maxAge = 3600)
@RestController
@RequestMapping("api")
public class EmailController {

    final String testEmail = "kasta.urbanska@gmail.com";

    @Autowired
    private SubscriptionService subscriptionService;


    @PostMapping("/subscribe")
    public void createEmailProfile (@RequestBody SubscribeDTO dto)
            throws IOException, ExecutionException, InterruptedException {
        subscriptionService.setSubscription(dto);
        //return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("/unsubscribe")
    public void deleteEmailProfileByProduct (@RequestParam String email, @RequestParam Long productId){
        subscriptionService.unsubscribe(email, productId);
    }



}


