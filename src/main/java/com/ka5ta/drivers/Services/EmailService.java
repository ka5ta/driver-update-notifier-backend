package com.ka5ta.drivers.Services;

import com.ka5ta.drivers.Entities.Driver;
import com.ka5ta.drivers.Entities.EmailProfile;
import com.ka5ta.drivers.Entities.Product;
import com.ka5ta.drivers.Repositories.SubscriptionRepository;
import com.ka5ta.drivers.Templates.htmlEmailTemplate;
import j2html.tags.ContainerTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static j2html.TagCreator.*;
import static j2html.TagCreator.body;

@Service
public class EmailService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private JavaMailSender emailSender;

    public void sendEmail(String to, String title, String body) {
        String from = "drivers.subscription@gmail.com";

        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(title);
            helper.setText(body, true);
            emailSender.send(message);

        } catch (MessagingException exception) {
            exception.printStackTrace();
            System.out.println("Email was not sent");
        }
    }

    public void SendEmailWithNewDriverList(Product product, List<Driver> newDrivers) throws IOException {

        // Get all distribution emails for the product
        List<EmailProfile> subscriptionProfilesForProduct = subscriptionRepository.findByProducts(product);
        List<String> distributionEmails = new ArrayList<>();
        subscriptionProfilesForProduct.forEach(emailProfile -> distributionEmails.add(emailProfile.getEmail()));

        // Create HTML template
        String title = "New drivers for product: " + product.getName();

        for (String distributionEmail : distributionEmails) {
            String htmlText = htmlEmailTemplate.createEmail(product, newDrivers, distributionEmail);

            this.sendEmail(distributionEmail, title, htmlText);
        }
    }
}
