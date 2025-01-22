package com.qre.tg.query.api.service.impl;

import com.qre.tg.query.api.service.TripFareService;
import org.springframework.stereotype.Service;

@Service
public class HolidayTripFareServiceImpl implements TripFareService {

    private static final int DISCOUNT_PERCENTAGE = 3; // discount 3%

    @Override
    public long calculateTripFare(long ticketFare) {
        long discountAmount = ticketFare * DISCOUNT_PERCENTAGE / 100;
        return ticketFare - discountAmount;
    }
}
