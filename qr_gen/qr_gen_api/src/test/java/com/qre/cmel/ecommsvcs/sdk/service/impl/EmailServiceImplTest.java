package com.qre.cmel.ecommsvcs.sdk.service.impl;

import com.qre.cmel.ecommsvcs.sdk.config.ECommSvcSdkProperties;
import com.qre.cmel.ecommsvcs.sdk.dto.MessageDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

import static org.mockito.Mockito.*;

public class EmailServiceImplTest {

    @Mock
    private JavaMailSender mockMailSender;

    @Mock
    private ECommSvcSdkProperties mockProperties;

    private EmailServiceImpl emailService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        emailService = new EmailServiceImpl(mockMailSender, mockProperties);
    }

    @Test
    public void testSendWithImage() throws MessagingException, IOException {
        MessageDto messageDto = new MessageDto();
        messageDto.setTo("test@example.com");
        messageDto.setSubject("Test Subject");
        messageDto.setMessage("Test Message");
        messageDto.setImageBytes(new byte[0]);
        messageDto.setFileNameWithExtension("test.png");

        MimeMessage mimeMessage = mock(MimeMessage.class);

        when(mockMailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(mockProperties.getFromEmail()).thenReturn("from@example.com");

        emailService.send(messageDto);

        verify(mockMailSender, times(1)).send(mimeMessage);
    }

    @Test
    public void testSendWithoutImage() throws MessagingException, IOException {
        MessageDto messageDto = new MessageDto();
        messageDto.setTo("test@example.com");
        messageDto.setSubject("Test Subject");
        messageDto.setMessage("Test Message");

        MimeMessage mimeMessage = mock(MimeMessage.class);

        when(mockMailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(mockProperties.getFromEmail()).thenReturn("from@example.com");

        emailService.send(messageDto);

        verify(mockMailSender, times(1)).send(mimeMessage);
    }

    // You can add more test cases as needed
}
