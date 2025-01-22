package com.qre.tg.query.api.route.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qre.tg.dto.qr.RefundResponse;
import com.qre.tg.dto.qr.TicketUpdateStatusRequest;
import com.qre.tg.query.api.common.TicketStatusEnum;
import com.qre.tg.query.api.service.TicketService;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UpdateStatusMessageProcessor implements Processor {

    /**
     * logger
     */
    private Logger logger = LoggerFactory.getLogger(UpdateStatusMessageProcessor.class);

    /**
     * ticketService
     */
    private final TicketService ticketService;

    /**
     * objectMapper
     */
    private final ObjectMapper objectMapper;

    public UpdateStatusMessageProcessor(TicketService ticketService, ObjectMapper objectMapper) {
        this.ticketService = ticketService;
        this.objectMapper = objectMapper;
    }

    /**
     * @param exchange
     * @throws Exception
     */
    @Override
    public void process(Exchange exchange) throws Exception {
        try {
            String strMessage = exchange.getIn().getBody(String.class);
            logger.info("Processing message: {}", strMessage);

            TicketUpdateStatusRequest ticketUpdateStatusRequest =
                    objectMapper.readValue(strMessage, TicketUpdateStatusRequest.class);
            ticketService.updateTicketStatus(ticketUpdateStatusRequest);


        } catch (Exception ex) {
            logger.error("Error processing message", ex);
            throw new Exception("Failed to process message", ex);
        }
    }

}
