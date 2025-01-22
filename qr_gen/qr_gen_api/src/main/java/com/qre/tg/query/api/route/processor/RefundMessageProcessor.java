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
public class RefundMessageProcessor implements Processor {

    /**
     * logger
     */
    private Logger logger = LoggerFactory.getLogger(RefundMessageProcessor.class);

    /**
     * ticketService
     */
    private final TicketService ticketService;

    /**
     * objectMapper
     */
    private final ObjectMapper objectMapper;

    public RefundMessageProcessor(TicketService ticketService, ObjectMapper objectMapper) {
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

            RefundResponse refundResponse = objectMapper.readValue(strMessage, RefundResponse.class);

            TicketStatusEnum ticketStatus = mapRefundStatusToTicketStatus(refundResponse.getRefundStatus());
            ticketService.updateRefund(refundResponse.getSerialNumber(), ticketStatus);

            TicketUpdateStatusRequest ticketUpdateStatusRequest = TicketUpdateStatusRequest.builder()
                    .serialNumber(refundResponse.getSerialNumber())
                    .status(refundResponse.getRefundStatus())
                    .build();

            // Optionally, set the TicketUpdateStatusRequest back to the exchange for further processing
            exchange.getIn().setBody(objectMapper.writeValueAsString(ticketUpdateStatusRequest));

        } catch (Exception ex) {
            logger.error("Error processing message", ex);
            throw new Exception("Failed to process message", ex);
        }
    }

    private TicketStatusEnum mapRefundStatusToTicketStatus(int refundStatus) {
        return switch (refundStatus) {
            case 1 -> TicketStatusEnum.REFUND_APPROVED;
            case 2 -> TicketStatusEnum.REFUND_REJECT;
            default -> TicketStatusEnum.REFUND_IN_PROGRESS;
        };
    }

}
