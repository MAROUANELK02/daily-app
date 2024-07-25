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

    public void sendEmail(String toEmail,String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("marouanelk02@gmail.com");
        message.setTo(toEmail);
        message.setSubject("Vérification OTP");
        message.setText("Votre code de vérification expire dans 10 minutes : " + body);

        mailSender.send(message);

        log.info("Mail sent successfully...");
    }
}
