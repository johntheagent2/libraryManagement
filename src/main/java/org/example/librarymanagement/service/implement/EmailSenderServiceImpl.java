package org.example.librarymanagement.service.implement;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.RequiredArgsConstructor;
import org.example.librarymanagement.entity.Book;
import org.example.librarymanagement.exception.exception.MessageException;
import org.example.librarymanagement.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

@Service
@RequiredArgsConstructor
public class EmailSenderServiceImpl implements EmailSenderService {

    private final JavaMailSender mailSender;
    private final Configuration freemarkerConfig;
    private final ResourceBundle resourceBundle;

    @Value("${spring.mail.username}")
    private String sender;

    @Value("${verify-link}")
    private String verifyTokenLink;

    @Value("${confirmation-template}")
    private String confirmationTemplate;

    //    @Value(("${invoice-template}"))
    private String invoiceTemplate = "invoiceTemplate.ftl";

    private String maintenanceTemplate = "maintenanceTemplate.ftl";

    @Value("${mail-subject-confirmation}")
    private String confirmMailSubject;

    @Value("${mail-subject-reset-password}")
    private String resetPasswordSubject;

    @Value("${mail-subject-reset-password}")
    private String invoice;

    @Value("${mail-subject-maintenance}")
    private String maintenance;

    @Override
    public void sendConfirmationMail(String token, String otp, String toEmail) {
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
        } catch (TemplateException e) {
            throw new org.example.librarymanagement.exception.exception.TemplateException("freemarker.template.exception",
                    resourceBundle.getString("freemarker.template.exception"));
        } catch (MessagingException e) {
            throw new MessageException("jarkarta.mail.exception",
                    resourceBundle.getString("jarkarta.mail.exception"));
        }

        // Send email
        mailSender.send(message);
    }

    @Override
    public void sendChangeRequest(String token, String toEmail) {
        String tokenLink = verifyTokenLink + token;
        changeResetSender(toEmail, tokenLink);
    }

    @Override
    public void sendResetRequest(String token, String toEmail) {
        String tokenLink = verifyTokenLink + token + "&mail=" + toEmail;
        changeResetSender(toEmail, tokenLink);
    }

    @Override
    public void changeResetSender(String toEmail, String tokenLink) {
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
        } catch (TemplateException e) {
            throw new org.example.librarymanagement.exception.exception.TemplateException("freemarker.template.exception",
                    resourceBundle.getString("freemarker.template.exception"));
        } catch (MessagingException e) {
            throw new MessageException("jarkarta.mail.exception",
                    resourceBundle.getString("jarkarta.mail.exception"));
        }

        // Send email
        mailSender.send(message);
    }

    @Override
    public void sendInvoiceEmail(String toEmail, List<Book> bookList, BigDecimal total) {
        Map<String, Object> model = new HashMap<>();
        Template freemarkerTemplate;
        String emailBody;

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        MimeMultipart mimeMultipart;

        try {
            mimeMultipart = new MimeMultipart("related");

            helper = new MimeMessageHelper(message, true);
            helper.setFrom(sender);
            helper.setTo(toEmail);
            helper.setSubject(invoice);

            model.put("email", toEmail);
            model.put("books", bookList);
            model.put("total", total.doubleValue());

            // Process the FreeMarker template
            freemarkerTemplate = freemarkerConfig.getTemplate(invoiceTemplate);
            emailBody = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, model);

            // Create a new MimeBodyPart for the HTML content
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(emailBody, "text/html; charset=utf-8");

            // Add the MimeMultipart with inline images to the message
            mimeMultipart.addBodyPart(htmlPart);
            message.setContent(mimeMultipart);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read image file", e);
        } catch (TemplateException e) {
            throw new RuntimeException("Error processing FreeMarker template", e);
        } catch (MessagingException e) {
            throw new RuntimeException("Error creating MimeMessage", e);
        }

        // Send email
        mailSender.send(message);
    }

    @Override
    public void sendMaintenanceMailToAllUser(List<String> toEmails) {
        Map<String, Object> model = new HashMap<>();
        Template freemarkerTemplate;
        String emailBody;

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper;

        try {
            helper = new MimeMessageHelper(message, true);
            helper.setFrom(sender);
            // Set recipients in "To" or "bcc" field based on your preference
            helper.setBcc(toEmails.toArray(new String[0])); // "bcc" field
            helper.setSubject(maintenance);

            freemarkerTemplate = freemarkerConfig.getTemplate(maintenanceTemplate);
            emailBody = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, model);
            helper.setText(emailBody, true);

            // Send email
            mailSender.send(message);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read image file", e);
        } catch (TemplateException e) {
            throw new RuntimeException("Error processing FreeMarker template", e);
        } catch (MessagingException e) {
            throw new RuntimeException("Error creating MimeMessage", e);
        }
    }
}
