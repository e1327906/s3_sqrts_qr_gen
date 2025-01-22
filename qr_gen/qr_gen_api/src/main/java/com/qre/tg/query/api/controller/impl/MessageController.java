package com.qre.tg.query.api.controller.impl;

import com.qre.cmel.message.sdk.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/message")
public class MessageController {

    private static Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Value("${app.producer.purchase.queue}")
    private String queue;

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> send(@RequestBody String message) {
        messageService.send(queue, message);
        return ResponseEntity.ok("Message sent successfully");
    }

    @GetMapping("/loadTest")
    public ResponseEntity<String> loadTest(){
        logger.info("Ticket Management Microservice load test performed");
        return ResponseEntity.ok("Ticket Management Microservice load test performed");
    }
}
