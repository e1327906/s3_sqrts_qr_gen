package com.qre.tg.query.api.service;

import com.qre.tg.dto.fare.FareRequest;

public interface FareCalculationStrategy {
    long calculateFare(FareRequest fareRequest);
}
