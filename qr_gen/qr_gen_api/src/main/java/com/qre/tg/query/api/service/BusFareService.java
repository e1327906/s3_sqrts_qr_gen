package com.qre.tg.query.api.service;


import com.qre.tg.dto.fare.BusFareRequest;
import com.qre.tg.dto.fare.FareResponse;
import com.qre.tg.entity.fare.BusFareMatrix;

import java.util.List;

public interface BusFareService{
    FareResponse findFare(BusFareRequest busFareRequest);

    List<BusFareMatrix> findAllFare();
}
