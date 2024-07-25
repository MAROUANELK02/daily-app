package com.pfa.dailyapp.security.mailing;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class EmailSenderService {
    private JavaMailSender mailSender;

    public void sendEmail(String toEmail,String subject,String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("marouanelk02@gmail.com");
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);

        log.info("Mail sent successfully...");
    }
}
