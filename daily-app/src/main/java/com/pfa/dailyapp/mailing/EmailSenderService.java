package com.pfa.dailyapp.mailing;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class EmailSenderService {
    private JavaMailSender mailSender;

    public void sendEmail(String toEmail,String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@dailymeeting.ma");
        message.setTo(toEmail);
        message.setSubject("Vérification OTP");
        message.setText("Votre code de vérification expire dans 10 minutes : " + body);

        mailSender.send(message);

        log.info("Mail sent successfully...");
    }

    public void sendDailyReportWithAttachment(String to, String subject, String text, byte[] pdfContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);

        helper.addAttachment("rapport-taches.pdf", new ByteArrayDataSource(pdfContent, "application/pdf"));

        mailSender.send(message);
    }

}