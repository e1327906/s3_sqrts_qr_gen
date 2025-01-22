package com.qre.tg.query.api.controller;

import com.qre.tg.dto.base.APIResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

public interface StripePaymentController {

    ResponseEntity<APIResponse> createPaymentIntent(@RequestBody Map<String, Object> payLoad);

    ResponseEntity<APIResponse> fetchCustomerCards (@RequestBody Map<String, Object> payLoad);

    ResponseEntity<APIResponse> deletePaymentMethod (@RequestBody Map<String, Object> payLoad);

    ResponseEntity<APIResponse> createSetupIntent(@RequestBody Map<String, Object> payLoad);

    ResponseEntity<APIResponse> createPaymentIntentByNewCard(@RequestBody Map<String, Object> payLoad);

    ResponseEntity<APIResponse> processRefund(@RequestBody Map<String, Object> payLoad);

    ResponseEntity<String> webhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader);
}
