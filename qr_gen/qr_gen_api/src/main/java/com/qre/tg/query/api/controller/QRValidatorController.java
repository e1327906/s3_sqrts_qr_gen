package com.qre.tg.query.api.controller;

import com.qre.tg.dto.base.APIResponse;
import com.qre.tg.dto.qr.ValidationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface QRValidatorController {

    ResponseEntity<APIResponse> validate(@RequestBody ValidationRequest request) throws Exception;
}
