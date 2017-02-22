package ua.com.brdo.business.constructor.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Mailer {
    private final static String MAIL_PROPERTIES_FILE = "config/mail.properties";

    public void send(String recipient, String subject, String text) {
        Properties mailProperties = new Properties();
        ClassLoader classLoader = getClass().getClassLoader();
        try (InputStream input = classLoader.getResourceAsStream(MAIL_PROPERTIES_FILE)) {
            mailProperties.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String username = mailProperties.getProperty("mail.username");
        String password = mailProperties.getProperty("mail.password");
        Session session = Session.getInstance(mailProperties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);
            message.setContent(text, "text/html; charset=UTF-8");
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
