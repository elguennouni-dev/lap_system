package com.lap.Order.Management.System.email;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

@Service
public class EmailTemplateService {

    public String loadTemplate(String templateName, Map<String, String> variables) {
        try {
            ClassPathResource resource = new ClassPathResource("templates/email/" + templateName);
            String content = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

            for (Map.Entry<String, String> entry : variables.entrySet()) {
                content = content.replace("{{" + entry.getKey() + "}}", entry.getValue());
            }

            return content;
        } catch (IOException e) {
            throw new RuntimeException("Template not found: " + templateName, e);
        }
    }


}
