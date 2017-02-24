package ua.com.brdo.business.constructor.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Service
public class Mailer {

    private JavaMailSender sender;

    @Autowired
    public Mailer(JavaMailSender sender) {
        this.sender = sender;
    }

    public void send(String recipient, String subject, String text) {
        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setTo(new InternetAddress(recipient));
            helper.setSubject(subject);
            helper.setText(text, true);
            sender.send(message);
        } catch (MessagingException e) {
            throw new MailSendException(e.getMessage());
        }
    }

}
