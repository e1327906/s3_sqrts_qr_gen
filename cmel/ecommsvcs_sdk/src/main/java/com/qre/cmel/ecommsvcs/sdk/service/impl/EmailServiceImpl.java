
package com.qre.cmel.ecommsvcs.sdk.service.impl;

import com.qre.cmel.ecommsvcs.sdk.config.ECommSvcSdkProperties;
import com.qre.cmel.ecommsvcs.sdk.dto.MessageDto;
import com.qre.cmel.ecommsvcs.sdk.service.EmailService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Service
public class EmailServiceImpl implements EmailService {

    /**
     * javaMailSender
     */
    private final JavaMailSender javaMailSender;

    /**
     * eCommSvcSdkProperties
     */
    private final ECommSvcSdkProperties eCommSvcSdkProperties;

    /**
     * @param javaMailSender
     * @param eCommSvcSdkProperties
     */
    public EmailServiceImpl(JavaMailSender javaMailSender, ECommSvcSdkProperties eCommSvcSdkProperties) {
        this.javaMailSender = javaMailSender;
        this.eCommSvcSdkProperties = eCommSvcSdkProperties;
    }

    /**
     * @param messageDto
     */
    @Override
    public void send(MessageDto messageDto) throws MessagingException, IOException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        if (messageDto.getCc() != null && !messageDto.getCc().isEmpty() && !messageDto.getCc().isBlank()) {
            helper.setCc(messageDto.getCc());
        }

        if (messageDto.getBcc() != null && !messageDto.getBcc().isEmpty() && !messageDto.getBcc().isBlank()) {
            helper.setBcc(messageDto.getBcc());
        }

        helper.setFrom(eCommSvcSdkProperties.getFromEmail());
        helper.setTo(messageDto.getTo());
        helper.setSubject(messageDto.getSubject());
        helper.setText(messageDto.getMessage());

        // Attach image file
        if(messageDto.getImageBytes() !=null) {
            ByteArrayResource imageResource = new ByteArrayResource(messageDto.getImageBytes());
            helper.addAttachment(messageDto.getFileNameWithExtension(), imageResource); // Specify the file name and resource
        }
        javaMailSender.send(mimeMessage);
    }
}
