package com.qre.tg.query.api.controller;

import com.qre.tg.dto.base.APIResponse;
import com.qre.tg.dto.qr.PurchaseTicketRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

public interface TicketServiceController {

    ResponseEntity<APIResponse> purchaseTicket(@RequestBody PurchaseTicketRequest purchaseTicketRequest) throws Exception;

    ResponseEntity<APIResponse> getTickets(@RequestParam String email) throws Exception;

    ResponseEntity<APIResponse> getRefundTickets(@RequestParam String email) throws Exception;

    ResponseEntity<APIResponse> processRefund(@RequestBody Map<String, Object> payLoad) throws Exception;
}
