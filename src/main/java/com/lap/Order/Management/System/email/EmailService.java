package com.lap.Order.Management.System.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${email.from.name}")
    private String fromName;

    @Value("${email.reply-to}")
    private String replyTo;

    @Value("${email.enabled:true}")
    private boolean emailEnabled;

    @Value("${email.charset:UTF-8}")
    private String charset;



    @Async
    public void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            System.out.println("Email sent to " + to);
        } catch (Exception e) {
            System.err.println("Error sending email: " + e.getMessage());
        }
    }



    @Async
    public void sendHtmlEmail(String to, String subject, String htmlBody) {
        if (!emailEnabled) return;

        try {
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, charset);

            helper.setFrom(fromEmail, fromName);
            helper.setReplyTo(replyTo);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);

            mailSender.send(message);

        } catch (MessagingException e) {
            System.err.println("Error sending HTML email: " + e.getMessage());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }



}