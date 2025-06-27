package com.healthcare.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Map;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public void sendAppointmentConfirmation(String to, Map<String, Object> templateModel) {
        try {
            String html = processTemplate("appointment-confirmation", templateModel);
            sendHtmlEmail(to, "Appointment Confirmation", html);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send appointment confirmation email", e);
        }
    }

    public void sendAppointmentReminder(String to, Map<String, Object> templateModel) {
        try {
            String html = processTemplate("appointment-reminder", templateModel);
            sendHtmlEmail(to, "Appointment Reminder", html);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send appointment reminder email", e);
        }
    }

    public void sendAppointmentCancellation(String to, Map<String, Object> templateModel) {
        try {
            String html = processTemplate("appointment-cancellation", templateModel);
            sendHtmlEmail(to, "Appointment Cancellation", html);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send appointment cancellation email", e);
        }
    }

    private String processTemplate(String templateName, Map<String, Object> templateModel) {
        Context context = new Context();
        context.setVariables(templateModel);
        return templateEngine.process(templateName, context);
    }

    private void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        
        mailSender.send(message);
    }
}
