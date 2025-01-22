package com.qre.tg.query.api.controller.impl;

import com.qre.cmel.exception.EntryExitMismatchException;
import com.qre.cmel.exception.InvalidJourneyException;
import com.qre.cmel.exception.InvalidTicketException;
import com.qre.tg.dto.base.APIResponse;
import com.qre.tg.dto.qr.ValidationRequest;
import com.qre.tg.query.api.controller.QRValidatorController;
import com.qre.tg.query.api.service.QRValidatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/validator")
@RequiredArgsConstructor
public class QRValidatorControllerImpl implements QRValidatorController {

    private final QRValidatorService qrValidatorService;

    @PostMapping("/Validate")
    @Override
    public ResponseEntity<APIResponse> validate(@RequestBody ValidationRequest request) {
        APIResponse apiResponse = null;
        try {
            qrValidatorService.validate(request);
            apiResponse = APIResponse.builder()
                    .responseCode(String.valueOf(HttpStatus.OK.value()))
                    .responseMsg(HttpStatus.OK.getReasonPhrase())
                    .build();
        } catch (InvalidTicketException ex) {

            apiResponse = APIResponse.builder()
                    .responseCode("800")
                    .responseMsg(ex.getMessage())
                    .build();

        } catch (InvalidJourneyException ex) {
            apiResponse = APIResponse.builder()
                    .responseCode("801")
                    .responseMsg(ex.getMessage())
                    .build();
        } catch (EntryExitMismatchException ex) {
            apiResponse = APIResponse.builder()
                    .responseCode("802")
                    .responseMsg(ex.getMessage())
                    .build();
        } catch (Exception ex) {
            apiResponse = APIResponse.builder()
                    .responseCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                    .responseMsg(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                    .build();
        }

        return ResponseEntity.ok(apiResponse);
    }
}
