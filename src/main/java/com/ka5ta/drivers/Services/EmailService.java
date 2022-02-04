package com.ka5ta.drivers.Services;

import com.ka5ta.drivers.Entities.Driver;
import com.ka5ta.drivers.Entities.EmailProfile;
import com.ka5ta.drivers.Entities.Product;
import com.ka5ta.drivers.Repositories.SubscriptionRepository;
import com.ka5ta.drivers.Templates.htmlEmailTemplate;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.*;
import java.util.concurrent.Future;

@Service
public class EmailService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private JavaMailSender emailSender;

    public void sendEmail(String to, String title, String body) {
        String from = "drivers.subscription@gmail.com";
        File file = new File("src/main/resources/email/drawing-logo.png");

        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(title);
            helper.setText(body, true);
            helper.addAttachment("drawing-logo.png", file);
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
            String htmlText = htmlEmailTemplate.generate(product, newDrivers, distributionEmail);

            this.sendEmail(distributionEmail, title, htmlText);
        }
    }

    public void sendWelcomeEmail(String email) throws ExecutionException, InterruptedException, IOException {

        /*// Get path to welcome email template
        Path path = Path.of("src/main/resources/email/mjmlDraft.mjml");
        String template = Files.readString(path, StandardCharsets.UTF_8);*/

        // Create mjml template with Thymeleaf - replace email var with user email
        String mjmlBody = this.parseThymeleafTemplate(email);

        // Convert mjml email template to html
        CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();
        httpclient.start();

        HttpPost httpPost = new HttpPost("http://127.0.0.1:1410");
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("mjml", mjmlBody));

        httpPost.setEntity(new UrlEncodedFormEntity(params));
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");

        Future<HttpResponse> futureResponse = httpclient.execute(httpPost, null);
        HttpResponse response = futureResponse.get();
        HttpEntity responseEntity = response.getEntity();
        String htmlBody = EntityUtils.toString(responseEntity);

        // Create HTML template title
        String title = "Thank you for subscribing to our service";

        //Send email
        this.sendEmail(email, title, htmlBody);
        httpclient.close();
    }

    private static String parseThymeleafTemplate(String email)  {

        // Get name from email
        String name = email.substring(0, email.indexOf("@"));

        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("email/");

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        // Search for var name on template and replace it with user's name
        Context context = new Context();
        context.setVariable("name", name);
        return templateEngine.process("mjmlDraft.mjml", context);
    }
}
