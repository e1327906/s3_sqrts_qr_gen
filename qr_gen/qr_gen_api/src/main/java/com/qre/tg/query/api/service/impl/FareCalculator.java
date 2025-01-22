package com.qre.tg.query.api.service.impl;

import com.qre.tg.dto.fare.FareRequest;
import com.qre.tg.query.api.common.JourneyTypeEnum;
import com.qre.tg.query.api.common.TransportMode;
import org.springframework.stereotype.Component;

@Component
public class FareCalculator {

    private final FareStrategyFactory factory;

    public FareCalculator(FareStrategyFactory factory) {
        this.factory = factory;
    }

    public long calculateFare(FareRequest fareRequest, TransportMode mode) {
        TicketTypeStrategy strategy = factory.getStrategy(JourneyTypeEnum.fromValue(fareRequest.getTicketType()), mode);
        return strategy.calculateFare(fareRequest);
    }
}
