package com.qre.tg.query.api.service;

import com.qre.tg.dto.qr.ValidationRequest;

public interface QRValidatorService{

    void validate(ValidationRequest request) throws Exception;
}
