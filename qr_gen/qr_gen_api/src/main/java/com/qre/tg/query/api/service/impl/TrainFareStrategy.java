package com.qre.tg.query.api.service.impl;

import com.qre.cmel.exception.ExceptionMsg;
import com.qre.cmel.exception.FareNotFoundException;
import com.qre.tg.dao.fare.TrainFareMatrixRepository;
import com.qre.tg.dto.fare.FareRequest;
import com.qre.tg.entity.fare.TrainFareMatrix;
import com.qre.tg.entity.fare.TrainFareMatrixPK;
import com.qre.tg.entity.ticket.TicketMaster;
import com.qre.tg.query.api.service.TransportModeStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TrainFareStrategy implements TransportModeStrategy {

    @Autowired
    private TrainFareMatrixRepository trainFareMatrixRepository;

    @Override
    public long calculateFare(FareRequest fareRequest) {

        TrainFareMatrixPK pk = TrainFareMatrixPK.builder()
                .destStnId(fareRequest.getEndPoint())
                .srcStnId(fareRequest.getStartPoint())
                .ticketTypeId(fareRequest.getTicketType())
                .build();

        /*TrainFareMatrix fareMatrix = trainFareMatrixRepository.findById(pk)
                .orElseThrow(() -> new FareNotFoundException(ExceptionMsg.FARE_NOT_FOUND));*/

        Optional<TrainFareMatrix> fareMatrix = trainFareMatrixRepository.findById(pk);
        if (fareMatrix.isEmpty()){
            return 10;
        }

        return fareMatrix.get().getFare();
    }
}
