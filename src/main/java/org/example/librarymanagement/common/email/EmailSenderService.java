package org.example.librarymanagement.common.email;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.example.librarymanagement.exception.exception.MessageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

@Service
@RequiredArgsConstructor
public class EmailSenderService{

    private final JavaMailSender mailSender;
    private final Configuration freemarkerConfig;
    private final ResourceBundle resourceBundle;

    @Value("${spring.mail.username}")
    private String sender;

    @Value("${verify-link}")
    private String verifyTokenLink;

    @Value("${confirmation-template}")
    private String confirmationTemplate;

    @Value("${mail-subject-confirmation}")
    private String confirmMailSubject;

    @Value("${mail-subject-reset-password}")
    private String resetPasswordSubject;

    public void sendConfirmationMail(String token, String otp, String toEmail){
        // Set up FreeMarker configuration
        String tokenLink = verifyTokenLink + token;
        Map<String, Object> model = new HashMap<>();
        String emailBody;
        Template freemarkerTemplate;


        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message, true);

            helper.setFrom(sender);
            helper.setTo(toEmail);
            helper.setSubject(confirmMailSubject);

            model.put("link", tokenLink);
            model.put("otp", otp);

            // Process the FreeMarker template
            freemarkerTemplate = freemarkerConfig.getTemplate(confirmationTemplate);
            emailBody = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, model);
            helper.setText(emailBody, true);

        } catch (IOException e) {
            throw new org.example.librarymanagement.exception.exception.IOException("util.io.exception",
                    resourceBundle.getString("util.io.exception"));
        }catch (TemplateException e){
            throw new org.example.librarymanagement.exception.exception.TemplateException("freemarker.template.exception",
                    resourceBundle.getString("freemarker.template.exception"));
        }catch (MessagingException e){
            throw new MessageException("jarkarta.mail.exception",
                    resourceBundle.getString("jarkarta.mail.exception"));
        }

        // Send email
        mailSender.send(message);
    }

    public void sendChangeRequest(String token, String toEmail){
        String tokenLink = verifyTokenLink + token;
        Map<String, Object> model = new HashMap<>();
        String emailBody;
        Template freemarkerTemplate;


        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message, true);

            helper.setFrom(sender);
            helper.setTo(toEmail);
            helper.setSubject(resetPasswordSubject);

            model.put("link", tokenLink);

            // Process the FreeMarker template
            freemarkerTemplate = freemarkerConfig.getTemplate(confirmationTemplate);
            emailBody = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, model);
            helper.setText(emailBody, true);

        } catch (IOException e) {
            throw new org.example.librarymanagement.exception.exception.IOException("util.io.exception",
                    resourceBundle.getString("util.io.exception"));
        }catch (TemplateException e){
            throw new org.example.librarymanagement.exception.exception.TemplateException("freemarker.template.exception",
                    resourceBundle.getString("freemarker.template.exception"));
        }catch (MessagingException e){
            throw new MessageException("jarkarta.mail.exception",
                    resourceBundle.getString("jarkarta.mail.exception"));
        }

        // Send email
        mailSender.send(message);
    }

    public void sendResetRequest(String token, String toEmail){
        String tokenLink = verifyTokenLink + token + "&mail=" + toEmail;
        Map<String, Object> model = new HashMap<>();
        String emailBody;
        Template freemarkerTemplate;


        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message, true);

            helper.setFrom(sender);
            helper.setTo(toEmail);
            helper.setSubject(resetPasswordSubject);

            model.put("link", tokenLink);

            // Process the FreeMarker template
            freemarkerTemplate = freemarkerConfig.getTemplate(confirmationTemplate);
            emailBody = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, model);
            helper.setText(emailBody, true);

        } catch (IOException e) {
            throw new org.example.librarymanagement.exception.exception.IOException("util.io.exception",
                    resourceBundle.getString("util.io.exception"));
        }catch (TemplateException e){
            throw new org.example.librarymanagement.exception.exception.TemplateException("freemarker.template.exception",
                    resourceBundle.getString("freemarker.template.exception"));
        }catch (MessagingException e){
            throw new MessageException("jarkarta.mail.exception",
                    resourceBundle.getString("jarkarta.mail.exception"));
        }

        // Send email
        mailSender.send(message);
    }
}