package org.example.librarymanagement.common.emailSender;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class EmailSenderService{

    private JavaMailSender mailSender;
    private Configuration freemarkerConfig;

    public void sendConfirmationMail(String token, String otp, String toEmail){
        // Set up FreeMarker configuration
        String mailTemplateFileName = "confirmationMail.ftl";
        String tokenLink = "http://localhost:8080/api/v1/registration/confirm?token="+token;
        Map<String, Object> model = new HashMap<>();
        String emailBody;
        Template freemarkerTemplate;


        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message, true);

            helper.setFrom("caophat113@gmail.com");
            helper.setTo(toEmail);
            helper.setSubject("Confirmation Email");

            model.put("link", tokenLink);
            model.put("otp", otp);

            // Process the FreeMarker template
            freemarkerTemplate = freemarkerConfig.getTemplate(mailTemplateFileName);
            emailBody = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, model);
            helper.setText(emailBody, true);

        } catch (IOException | TemplateException | MessagingException e) {
            throw new RuntimeException(e);
        }


        // Send email
        mailSender.send(message);
    }
}
