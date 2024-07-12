package com.ilusha.marketplaceAPI.configS3;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class SendEmailService {
    private JavaMailSender mailSender;


    public void sendEmail(String recipient, String resetCode){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("ilialimits222@gmail.com");
        message.setTo(recipient);
        message.setText("Your code to reset the password is: " + resetCode);
        message.setSubject("Code for resetting your password");

        mailSender.send(message);

        System.out.println("Sent successfully");

    }

}
