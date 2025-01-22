package com.qre.tg.query.api.service.impl;

import com.qre.tg.dto.fare.FareRequest;
import com.qre.tg.entity.ticket.TicketMaster;
import com.qre.tg.query.api.service.TransportModeStrategy;
import org.springframework.stereotype.Component;

@Component
public class BusFareStrategy implements TransportModeStrategy {
    @Override
    public long calculateFare(FareRequest fareRequest) {
        return 0;
    }
}
