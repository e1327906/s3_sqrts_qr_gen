package com.qre.tg.query.api.service.impl;

import com.qre.tg.dto.fare.FareRequest;
import com.qre.tg.query.api.common.TransportMode;
import com.qre.tg.query.api.service.FareCalculationStrategy;
import com.qre.tg.query.api.service.TransportModeStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class TicketTypeStrategy implements FareCalculationStrategy {

    protected TransportModeStrategy modeStrategy;

    @Autowired
    private TrainFareStrategy trainFareStrategy;

    @Autowired
    private BusFareStrategy busFareStrategy;

    @Autowired
    private TramFareStrategy tramFareStrategy;


    public void setTicketTypeStrategy(TransportMode mode) {
        switch (mode) {
            case TRAIN:
                this.modeStrategy = trainFareStrategy;
                break;
            case BUS:
                this.modeStrategy = busFareStrategy;
                break;
            case TRAM:
                this.modeStrategy = tramFareStrategy;
                break;
            default:
                throw new IllegalArgumentException("Invalid transport mode: " + mode);
        }
    }



    @Override
    public long calculateFare(FareRequest fareRequest) {
        return modeStrategy.calculateFare(fareRequest);  // Delegate to mode strategy
    }
}
