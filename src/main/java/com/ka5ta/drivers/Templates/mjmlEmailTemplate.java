package com.ka5ta.drivers.Templates;


import es.atrujillo.mjml.config.template.TemplateFactory;
import io.camassia.mjml.MJMLClient;
import org.springframework.stereotype.Controller;
import org.thymeleaf.context.Context;

import java.io.File;

@Controller
public class mjmlEmailTemplate {

    MJMLClient client = MJMLClient.newDefaultClient()
            .withApplicationID("...")
            .withApplicationKey("...");

}
