package com.qre.tg.query.api.service.impl;

import com.qre.cmel.exception.*;
import com.qre.tg.dao.fare.BusFareMatrixRepository;
import com.qre.tg.dto.fare.BusFareRequest;
import com.qre.tg.dto.fare.FareResponse;
import com.qre.tg.entity.fare.BusFareMatrix;
import com.qre.tg.entity.fare.BusFareMatrixPK;
import com.qre.tg.query.api.common.JourneyTypeEnum;
import com.qre.tg.query.api.service.BusFareService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class BusFareServiceImpl implements BusFareService {

    private final BusFareMatrixRepository busFareMatrixRepository;

    private final TripFareServiceFactoryImpl tripFareServiceFactory;

    @Override
    public FareResponse findFare(BusFareRequest request) {

        BusFareMatrixPK pk = BusFareMatrixPK.builder()
                .destBusStopId(request.getDestBusStopId())
                .srcBusStopId(request.getSrcBusStopId())
                .ticketTypeId(request.getTicketType())
                .build();

        BusFareMatrix fareMatrixOutbound = busFareMatrixRepository.findById(pk)
                .orElseThrow(() -> new FareNotFoundException(ExceptionMsg.FARE_NOT_FOUND));
        BusFareMatrix fareMatrixReturn = null;

        if (JourneyTypeEnum.RETURN_TICKET.equals(JourneyTypeEnum.fromValue(request.getTicketType()))) {
            // For return journey, source and destination are reversed
            pk.setSrcBusStopId(request.getSrcBusStopId());
            pk.setDestBusStopId(request.getDestBusStopId());
            fareMatrixReturn = busFareMatrixRepository.findById(pk).orElse(null);
        }


        Long fareAmountOutbound = fareMatrixOutbound.getFare();
        Long totalFare;

        if (fareMatrixReturn != null) {
            Long fareAmountReturn = fareMatrixReturn.getFare();
            totalFare = fareAmountOutbound + fareAmountReturn;
        }
        else {
            totalFare = fareAmountOutbound; // Only outbound fare
        }

        // Multiply total fare by number of passengers for group fare
        totalFare *= request.getGroupSize();

        return FareResponse.builder()
                .fare(calculateTripFare(totalFare))
                .build();
    }

    private long calculateTripFare(long amount){
        boolean isHoliday = tripFareServiceFactory.isHoliday(new Date());
        return tripFareServiceFactory.createTripFare(isHoliday)
                .calculateTripFare(amount);
    }

    @Override
    public List<BusFareMatrix> findAllFare() {
        return busFareMatrixRepository.findAll();
    }
}