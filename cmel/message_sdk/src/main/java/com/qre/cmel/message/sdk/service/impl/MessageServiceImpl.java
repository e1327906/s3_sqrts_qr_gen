package com.qre.cmel.message.sdk.service.impl;

import com.qre.cmel.message.sdk.service.MessageService;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService {

    /**
     * ACTIVEMQ
     */
    public static final String ACTIVEMQ = "activemq";

    /**
     * producerTemplate
     */
    private final ProducerTemplate producerTemplate;

    /***
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

    /**
     * @param producerTemplate
     */
    public MessageServiceImpl(ProducerTemplate producerTemplate) {
        this.producerTemplate = producerTemplate;
    }

    public void send(String topic, Object message) {

        logger.info("Sending message to topic: {}, message: {}", topic, message);

        this.producerTemplate.asyncSend(ACTIVEMQ.concat(":").concat(topic), (exchange) -> {
            exchange.getIn().setBody(message);
        });
    }
}
