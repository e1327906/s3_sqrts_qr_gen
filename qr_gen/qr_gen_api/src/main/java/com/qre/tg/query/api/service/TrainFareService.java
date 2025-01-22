package com.qre.tg.query.api.service;


import com.qre.tg.dto.fare.FareResponse;
import com.qre.tg.dto.fare.TrainFareRequest;
import com.qre.tg.entity.fare.TrainFareMatrix;

import java.util.List;

public interface TrainFareService{
    FareResponse findFare(TrainFareRequest request);

    List<TrainFareMatrix> findAllFare();
}
