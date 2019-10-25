package com.diabolo.util;

import com.diabolo.db.ConnectionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;

public class EmailUtil {
    private static final Logger LOGGER = LogManager.getLogger(EmailUtil.class.getName());

    public static String createValidationString(int length) {
        String validationString = "";
        do {
            validationString = PasswordUtil.generatePassword(length);
        } while (isValidationStringInDatabase(validationString));
        return validationString;
    }

    public static boolean isValidationStringInDatabase(String validationString) {
        boolean returnValue = false;
        String query = "SELECT id FROM user_info WHERE validation = ?";
        try (Connection con = ConnectionManager.getConnection("login", "login_read")) {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, validationString);
            ResultSet rs = pst.executeQuery();
            if (rs.last()) {
                returnValue = true;
            }
            rs.close();
            pst.close();
        } catch (SQLException ex) {
            LOGGER.error(EmailUtil.class.getName() + " SQL Error Message Logged !!!", ex.getMessage(), ex);
        }
        return returnValue;
    }

    public static void disableValidationInDatabase(String validationString) {
        updateValidationInDatabase(null, false, validationString);
    }

    public static void updateValidationInDatabase(String newValidString, boolean isNotValid, String currentValidString) {
        String query = "UPDATE user_info SET `validation` = ?, email_not_valid = ? WHERE `validation` = ?";
        try (Connection con = ConnectionManager.getConnection("login", "login_rw")) {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, newValidString);
            pst.setBoolean(2, isNotValid);
            pst.setString(3, currentValidString);
            pst.executeUpdate();
            pst.close();
        } catch (SQLException ex) {
            LOGGER.error(EmailUtil.class.getName() + " SQL Error Message Logged !!!", ex.getMessage(), ex);
        }
    }

    public static boolean isEmailInDatabase(String email) {
        boolean returnValue = false;
        String query = "SELECT id FROM user_info WHERE email = ?";
        try (Connection con = ConnectionManager.getConnection("login", "login_read")) {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();
            if (rs.last()) {
                returnValue = true;
            }
            rs.close();
            pst.close();
        } catch (SQLException ex) {
            LOGGER.error(EmailUtil.class.getName() + " SQL Error Message Logged !!!", ex.getMessage(), ex);
        }
        return returnValue;
    }

    public static void sendPasswordEmail(String email, String name, String tempPassword) throws IOException {
        try {
            Message message = new MimeMessage(getDefaultSession());
            message.setFrom(new InternetAddress("no-reply@dondiabolo.com","Don Diabolo"));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(email, name));
            message.setSubject("Don Diabolo Password Reset");
            message.setSentDate(new Date());

            String msg = "<!DOCTYPE html><html lang=\"en\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/><title>Don Diabolo Password Reset</title></head><body><b>Hello " + name + ",</b><br>Your new password is <i>" + tempPassword + "</i></body></html>";
            String msg2 = "Hello " + name + System.lineSeparator() + "Your new password is " + tempPassword;

            MimeBodyPart plainPart = new MimeBodyPart();
            plainPart.setContent(msg2, "text/plain; charset=US-ASCII");
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(msg, "text/html; charset=UTF-8");

            Multipart multipart = new MimeMultipart("alternative");
            multipart.addBodyPart(plainPart);
            multipart.addBodyPart(htmlPart);

            message.setContent(multipart);

            Transport.send(message);
        } catch (MessagingException ex) {
            LOGGER.error(EmailUtil.class.getName() + " Mail Error Message Logged !!!", ex.getMessage(), ex);
        }
    }

    public static void sendNewUserEmail(String email, String name, String validation) throws IOException {
        try {
            Message message = new MimeMessage(getDefaultSession());
            message.setFrom(new InternetAddress("no-reply@dondiabolo.com","Don Diabolo"));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(email, name));
            message.setSubject("Don Diabolo Confirm Email");
            message.setSentDate(new Date());

            String msg = "<!DOCTYPE html><html lang=\"en\"><head><meta http-equiv=\"Content-Type\" " +
                    "content=\"text/html; charset=UTF-8\"/><title>Don Diabolo Confirm Email</title></head>" +
                    "<body><b>Hello " + name + ",</b><br>" +
                    "Please confirm your email address by using the following validation string: " +
                    "<i>" +validation + "</i></body></html>";
            String msg2 = "Hello " + name + System.lineSeparator() +
                    "Please confirm your email address by using the following validation string: " + validation;

            MimeBodyPart plainPart = new MimeBodyPart();
            plainPart.setContent(msg2, "text/plain; charset=US-ASCII");
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(msg, "text/html; charset=UTF-8");

            Multipart multipart = new MimeMultipart("alternative");
            multipart.addBodyPart(plainPart);
            multipart.addBodyPart(htmlPart);

            message.setContent(multipart);

            Transport.send(message);
        } catch (MessagingException ex) {
            LOGGER.error(EmailUtil.class.getName() + " Mail Error Message Logged !!!", ex.getMessage(), ex);
        }
    }

    private static Session getDefaultSession() {
        Properties prop = new Properties();
        //prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "localhost");
        prop.put("mail.smtp.port", "25");
        prop.put("mail.smtp.ssl.trust", "localhost");

        return Session.getDefaultInstance(prop);
    }
}
