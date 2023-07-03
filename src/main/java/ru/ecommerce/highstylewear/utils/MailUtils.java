package ru.ecommerce.highstylewear.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

public class MailUtils {
    private MailUtils() {}

    public static SimpleMailMessage crateMailMessage(final String email,
                                                     final String from,
                                                     final String subject,
                                                     final String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom(from);
        message.setSubject(subject);
        message.setText(text);
        return message;
    }
}
