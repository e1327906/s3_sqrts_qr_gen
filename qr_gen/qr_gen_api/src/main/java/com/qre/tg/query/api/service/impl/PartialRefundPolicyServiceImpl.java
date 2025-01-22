package com.qre.tg.query.api.service.impl;

import com.qre.tg.entity.ticket.TicketMaster;
import com.qre.tg.query.api.service.RefundPolicyService;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Service
public class PartialRefundPolicyServiceImpl implements RefundPolicyService {
    @Override
    public long calculateRefund(TicketMaster ticketMaster) {
        // Partial refund policy implementation
        long refundPercentage;
        long hoursDifference = hoursDifference(ticketMaster.getEffectiveDateTime());
        if (hoursDifference <= 48) {
            refundPercentage = 50;
        }
        else if (hoursDifference <= 72) {
            refundPercentage = 25;
        }
        else {
            refundPercentage = 0;
        }
        return (ticketMaster.getTransactionData().getAmount() *100) -
                ((ticketMaster.getTransactionData().getAmount() *100) * refundPercentage / 100);
    }

    public long hoursDifference(Date effectiveDateTime) {
        Instant ticketInstant = effectiveDateTime.toInstant();
        Instant nowInstant = Instant.now();
        return Duration.between(ticketInstant, nowInstant).toHours();
    }
}