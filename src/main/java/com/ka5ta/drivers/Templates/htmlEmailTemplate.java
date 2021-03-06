package com.ka5ta.drivers.Templates;

import com.ka5ta.drivers.Entities.Driver;
import com.ka5ta.drivers.Entities.EmailProfile;
import com.ka5ta.drivers.Entities.Product;
import j2html.tags.ContainerTag;


import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

import static j2html.TagCreator.*;
import static j2html.TagCreator.body;

public class htmlEmailTemplate {

    public static String createEmail(Product product, List<Driver> newDrivers, String distributionEmail) {

        // Body CSS style
        StringBuilder bodyStylesBuilder = new StringBuilder();
        String bodyStyle = bodyStylesBuilder
                .append("color: #000001; font-size: 13px; margin: auto; text-align: center; ").toString();

        // Body CSS style
        StringBuilder tableStylesBuilder = new StringBuilder();
        String tableStyle = tableStylesBuilder
                .append("color: #000001; font-size: 13px; margin-left: auto; margin-right: auto; margin-top: 40px; margin-bottom: 50px; ").toString();

        // Logo CSS style
        StringBuilder logoBuilder = new StringBuilder();
        String logoStyle = logoBuilder
                .append("color: #051937; " +
                       "font-size: 30px; " +
                       "letter-spacing: 5px; " +
                       "background-color: #eaf4fa; " +
                      "text-align: center; " +
                       "margin: auto; " +
                        "padding: 30px 60px; " +
                       "font-weight: 800;" +
                       "width: fit-content; " +
                        "line-height: 1; ")
                .toString();

        // Button CSS style
        StringBuilder buttonStylesBuilder = new StringBuilder();
        String button = buttonStylesBuilder
                .append("background-color: #bbe0ec; " +
                        "color: rgb(255, 255, 255); " +
                        "font-size: 13px; " +
                        "text-align: center; " +
                        "cursor: pointer; " +
                        "border-radius: 5px;" +
                        "text-decoration: none;" +
                        "font-family: Helvetica, Arial, sans-serif; " +
                        "padding: 14px 20px; " +
                        "display: block; " +
                        "margin: auto; " +
                        "width: fit-content; ")
                .toString();

        // Email title
        String title = "New drivers for product: " + product.getName();

        // User name from email
        String name = distributionEmail.toUpperCase().substring(0, distributionEmail.indexOf("@"));

        // Email container template
        ContainerTag htmlTextContainer = body(
                div("DRIVERS SUBSCRIPTION").withStyle(logoStyle),
                div(h3("Hello " + name).withStyle("color: #000001; font-size: 23px; text-align: center; font-weight: 600; ")),
                div(
                        span("I would like to inform you that there are a new drivers for "),
                        span(product.getName()).withStyle("font-weight:bold; color: rgba(0,153,204,0.8); "),
                        span(" please see below list:")
                ).withStyle(bodyStyle),
                table().withStyle(tableStyle).with(
                        tr().withStyle("padding: 8px; ").with(
                                th("Driver name"),
                                th("Driver Download link"),
                                th("File Size")
                        ),
                        each(newDrivers, driver ->
                                tr().withStyle("padding: 8px; ").with(
                                        td(driver.getName()),
                                        td(a().withText(String.format("*%s*", driver.getDownloadLink()))),
                                        td(driver.getFileSizeMB() + " MB")
                                )
                        )
                ),
                a("UNSUBSCRIBE").withHref("http://localhost:8080/api/unsubscribe?" + "email=" + distributionEmail + "&" + "productId=" + product.getId()).withStyle(button)
        );


        // HTML email body formatted
        String htmlEmailTemplate = htmlTextContainer.renderFormatted();
        System.out.println(htmlEmailTemplate);

        return htmlEmailTemplate;
    }
    //todo create object that will hold title and template + unsubscribe button
}



