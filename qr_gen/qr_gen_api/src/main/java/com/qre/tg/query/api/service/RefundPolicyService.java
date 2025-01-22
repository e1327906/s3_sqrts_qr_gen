package com.qre.tg.query.api.service;

import com.qre.tg.entity.ticket.TicketMaster;

public interface RefundPolicyService {
    long calculateRefund(TicketMaster ticketMaster);
}
