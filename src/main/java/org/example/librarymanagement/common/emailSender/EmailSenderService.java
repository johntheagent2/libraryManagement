package org.example.librarymanagement.common.emailSender;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailSenderService{

    private final JavaMailSender mailSender;
    private final Configuration freemarkerConfig;

    @Value("${spring.mail.username}")
    private String MAIL_SENDER;

    @Value("${verify-link}")
    private String VERIFY_TOKEN_LINK;

    @Value("${confirmation-template}")
    private String CONFIRMATION_TEMPLATE;

    @Value("${mail-subject-confirmation}")
    private String MAIL_SUBJECT;

    public void sendConfirmationMail(String token, String otp, String toEmail){
        // Set up FreeMarker configuration
        String tokenLink = VERIFY_TOKEN_LINK + token;
        Map<String, Object> model = new HashMap<>();
        String emailBody;
        Template freemarkerTemplate;


        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message, true);

            helper.setFrom(MAIL_SENDER);
            helper.setTo(toEmail);
            helper.setSubject(MAIL_SUBJECT);

            model.put("link", tokenLink);
            model.put("otp", otp);

            // Process the FreeMarker template
            freemarkerTemplate = freemarkerConfig.getTemplate(CONFIRMATION_TEMPLATE);
            emailBody = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, model);
            helper.setText(emailBody, true);

        } catch (IOException | TemplateException | MessagingException e) {
            throw new RuntimeException(e);
        }


        // Send email
        mailSender.send(message);
    }
}
