package com.qre.tg.query.api.service.impl;

import com.qre.tg.query.api.service.TripFareService;
import org.springframework.stereotype.Service;

@Service
public class NormalDayTripFareServiceImpl implements TripFareService {
    @Override
    public long calculateTripFare(long ticketFare) {
        return ticketFare;
    }
}
