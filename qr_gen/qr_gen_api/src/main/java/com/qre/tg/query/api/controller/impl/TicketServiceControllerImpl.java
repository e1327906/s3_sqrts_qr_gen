package com.qre.tg.query.api.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qre.cmel.message.sdk.service.MessageService;
import com.qre.tg.dto.base.APIResponse;
import com.qre.tg.dto.base.JsonFieldName;
import com.qre.tg.dto.qr.PurchaseTicketRequest;
import com.qre.tg.dto.qr.RefundRequest;
import com.qre.tg.entity.ticket.TicketMaster;
import com.qre.tg.query.api.common.RefundPolicyTypeEnum;
import com.qre.tg.query.api.common.TicketStatusEnum;
import com.qre.tg.query.api.controller.TicketServiceController;
import com.qre.tg.query.api.service.RefundPolicyService;
import com.qre.tg.query.api.service.TicketService;
import com.qre.tg.query.api.service.impl.RefundPolicyServiceFactoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/tickets/")
@RequiredArgsConstructor
public class TicketServiceControllerImpl implements TicketServiceController {

    private final TicketService ticketService;

    private final RefundPolicyServiceFactoryImpl refundPolicyServiceFactoryImpl;

    private final MessageService messageService;

    @Value("${app.producer.refund.request.queue}")
    private String queue;

    @PostMapping("/PurchaseTicket")
    @Override
    public ResponseEntity<APIResponse> purchaseTicket(@RequestBody PurchaseTicketRequest request) throws Exception {
        ticketService.purchaseTicket(request);
        APIResponse apiResponse = APIResponse.builder()
                .responseCode(String.valueOf(HttpStatus.OK))
                .responseMsg(HttpStatus.OK.getReasonPhrase())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/GetTickets")
    @Override
    public ResponseEntity<APIResponse> getTickets(@RequestParam String email) throws Exception {

        APIResponse apiResponse = APIResponse.builder()
                .responseCode(String.valueOf(HttpStatus.OK))
                .responseMsg(HttpStatus.OK.getReasonPhrase())
                .responseData(ticketService.findAllByEmail(email))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/GetRefundTickets")
    @Override
    public ResponseEntity<APIResponse> getRefundTickets(String email) throws Exception {
        APIResponse apiResponse = APIResponse.builder()
                .responseCode(String.valueOf(HttpStatus.OK))
                .responseMsg(HttpStatus.OK.getReasonPhrase())
                .responseData(ticketService.findAllRefundableTicketByEmail(email))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @RequestMapping(value = "/RefundTicket", method = RequestMethod.POST)
    @Override
    public ResponseEntity<APIResponse> processRefund(@RequestBody Map<String, Object> payLoad) {
        try {

            String serialNumber = payLoad.get(JsonFieldName.SERIAL_NUMBER).toString();
            String paymentIntentId = payLoad.get(JsonFieldName.PAYMENT_INTENT_ID).toString();

            Optional<TicketMaster> ticketMaster = ticketService.findBySerialNumber(serialNumber);
            if (ticketMaster.isEmpty()) {
                APIResponse apiResponse = APIResponse.builder()
                        .responseCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                        .responseMsg(HttpStatus.NOT_FOUND.getReasonPhrase())
                        .responseData(null)
                        .build();
                return ResponseEntity.ok().body(apiResponse);
            }

            // Create policy
            RefundPolicyService refundPolicyService = refundPolicyServiceFactoryImpl.createRefundPolicy(RefundPolicyTypeEnum.PARTIAL_REFUND);
            if (refundPolicyServiceFactoryImpl.hoursDifference(
                    ticketMaster.get().getEffectiveDateTime()) <= 24) {
                refundPolicyService = refundPolicyServiceFactoryImpl.createRefundPolicy(RefundPolicyTypeEnum.FULL_REFUND);
            }

            long refundAmount = refundPolicyService.calculateRefund(ticketMaster.get());
            ticketService.updateRefund(serialNumber, TicketStatusEnum.REFUND_IN_PROGRESS);

            RefundRequest request = RefundRequest.builder()
                    .serialNumber(serialNumber)
                    .refundAmount(refundAmount)
                    .paymentIntentId(paymentIntentId)
                    .build();

            // send refund request to payment service
            messageService.send(queue, new ObjectMapper().writeValueAsString(request));

            APIResponse apiResponse = APIResponse.builder()
                    .responseCode(String.valueOf(HttpStatus.OK.value()))
                    .responseMsg(HttpStatus.OK.getReasonPhrase())
                    .responseData(refundAmount + " " + "SGD" + " refunded successfully")
                    .build();
            return ResponseEntity.ok().body(apiResponse);

        } catch (Exception e) {
            APIResponse apiResponse = APIResponse.builder()
                    .responseCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                    .responseMsg(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                    .responseData(null)
                    .build();
            return ResponseEntity.ok().body(apiResponse);
        }
    }

    @GetMapping("/ServiceStatus")
    public ResponseEntity<APIResponse> ServiceStatus() throws Exception {

        APIResponse apiResponse = APIResponse.builder()
                .responseCode(String.valueOf(HttpStatus.OK))
                .responseMsg(HttpStatus.OK.getReasonPhrase())
                .responseData("Welcome from ticket generating service!")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/Test")
    public ResponseEntity<APIResponse> Test() throws Exception {

        String strEncodeQRData = "ugQq3LrIs5cSlzP5fVpfGaMzBAPvib5e8wd68RyX5MSsoRiKzo6YN918qkk8FNTsRt2he/5dltVv08KErBg4qL1+5tv6ENr18egUVnnQKS9HO6VwvyHKZYxBCVLW5eZ8e5hBPihVBacoU5lcgC8u1l9XdquzcuDio225jA1E0VUohfuubmc0SSw4gt0d6hpIx2dHeCwlDCt6lL81MbpTRL/JsvIacOWn/IlGTP7es1J6iKiPSZcXgZdatVH7RUnXS6AQKKUw0+3LZBVr2ktkAXrEKJGW5g+yLGk0gnEGvQvbnjq1QvCmW0ErSrQiuJ6qK13lBkqZi6RLixu592Ivdj4i9u0ZAA4Y41SrmgnXpY3afukM2dISXCdMUTeQ6ezLIWv3m6jWYVdkb6aHA6Y4cr3koZa1LrwazsuSzsMK0MkoUFZ2DwGt7U1dted7Az6mHsegHj1KZsAR/CvCuA6PtnionNddQDfrGNXXPXdrawCuYMz9T53GF1AeYeJws7sh";
        APIResponse apiResponse = APIResponse.builder()
                .responseCode(String.valueOf(HttpStatus.OK))
                .responseMsg(HttpStatus.OK.getReasonPhrase())
                .responseData(ticketService.generateDigitalSignatureV1(strEncodeQRData))
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
