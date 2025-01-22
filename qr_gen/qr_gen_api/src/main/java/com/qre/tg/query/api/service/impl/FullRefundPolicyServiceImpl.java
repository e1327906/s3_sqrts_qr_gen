package com.qre.tg.query.api.service.impl;

import com.qre.tg.entity.ticket.TicketMaster;
import com.qre.tg.query.api.service.RefundPolicyService;
import org.springframework.stereotype.Service;

@Service
public class FullRefundPolicyServiceImpl implements RefundPolicyService {
    @Override
    public long calculateRefund(TicketMaster ticketMaster) {

        long refundPercentage = 6; //Service charges 3 percentage
        // Full refund policy implementation
        return ((ticketMaster.getTransactionData().getAmount() *100))
                - ((ticketMaster.getTransactionData().getAmount() *100) * refundPercentage / 100);
    }
}