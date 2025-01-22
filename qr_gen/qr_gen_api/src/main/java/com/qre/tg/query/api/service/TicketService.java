package com.qre.tg.query.api.service;

import com.qre.tg.dto.qr.PurchaseTicketRequest;
import com.qre.tg.dto.qr.TicketDetailResponse;
import com.qre.tg.dto.qr.TicketUpdateStatusRequest;
import com.qre.tg.entity.ticket.TicketMaster;
import com.qre.tg.query.api.common.TicketStatusEnum;

import java.util.List;
import java.util.Optional;

public interface TicketService{

    TicketDetailResponse purchaseTicket(PurchaseTicketRequest request) throws Exception;

    List<TicketDetailResponse> findAllByEmail(String email);

    String generateSerialNumber(PurchaseTicketRequest request);

    List<TicketDetailResponse> findAllRefundableTicketByEmail(String email);

    Optional<TicketMaster> findByTransactionDataPaymentRefNo(String paymentRefNo);

    void updateRefund(String serialNumber, TicketStatusEnum ticketStatusEnum);

    Optional<TicketMaster> findBySerialNumber(String serialNumber);

    void updateTicketStatus(TicketUpdateStatusRequest request);
}
