package com.el;

import lombok.extern.log4j.Log4j;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

@Log4j
public class Email {
    private static final String DIRECT_SERVER_HOST = "smtp.gmail.com";
    private static final String DIRECT_SERVER_PORT = "587";
    private static final String SENDER_EMAILID = "EMAILID";
    private static final String SENDER_PASSWORD = "PASSWORD";

    public static void send() throws Exception {
        log.info("sending message started");
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", DIRECT_SERVER_HOST);
        properties.put("mail.smtp.port", DIRECT_SERVER_PORT);
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.auth", "true");
        Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(SENDER_EMAILID, SENDER_PASSWORD); }
        });

        try {
            MimeMessage mimeMessage = new MimeMessage(session);
            Multipart multipart = new MimeMultipart("mixed");
            mimeMessage.setFrom(new InternetAddress(SENDER_EMAILID));
            mimeMessage.addRecipients(Message.RecipientType.TO, new Address[]{new InternetAddress(SENDER_EMAILID)});
            mimeMessage.setSubject("email lambda subject");
            MimeBodyPart bodyContent = new MimeBodyPart();
            bodyContent.setContent("Hi, content body. Email triggered by Lambda", "text/html; charset=utf-8");
            multipart.addBodyPart(bodyContent);
            mimeMessage.setContent(multipart);
            log.info("triggering send email to " + SENDER_EMAILID);
            Transport.send(mimeMessage);
            log.info("email sent (success)");
        } catch (Exception e) {
            // AddressException | MessagingException
            log.error("unable to send email, encountered exception -> (message) " + e.getMessage());
                throw new Exception("unable to send email, encountered exception -> (message) " + e.getMessage());
        }
    }
}
