package com.qre.tg.query.api.controller;

import com.qre.tg.dto.base.APIResponse;
import com.qre.tg.dto.fare.BusFareRequest;
import com.qre.tg.dto.fare.TrainFareRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;


public interface FareController {
    ResponseEntity<APIResponse> getTrainFare(@RequestBody TrainFareRequest trainFareRequest);

    ResponseEntity<APIResponse> getBusFare(@RequestBody BusFareRequest busFareRequest);

    ResponseEntity<APIResponse> getGetAllTrainFare();

    ResponseEntity<APIResponse> getGetAllBusFare();
}
