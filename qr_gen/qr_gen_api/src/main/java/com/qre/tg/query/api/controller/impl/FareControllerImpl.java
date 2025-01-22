package com.qre.tg.query.api.controller.impl;

import com.qre.tg.dto.base.APIResponse;
import com.qre.tg.dto.fare.BusFareRequest;
import com.qre.tg.dto.fare.FareResponse;
import com.qre.tg.dto.fare.TrainFareRequest;
import com.qre.tg.query.api.service.TrainFareService;
import com.qre.tg.query.api.controller.FareController;
import com.qre.tg.query.api.service.BusFareService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/fares/")
public class FareControllerImpl implements FareController {

    private final Logger logger = LoggerFactory.getLogger(FareControllerImpl.class);

    private final TrainFareService trainFareService;

    private final BusFareService busFareService;

    @Autowired
    public FareControllerImpl(TrainFareService trainFareService, BusFareService busFareService) {

        this.trainFareService = trainFareService;
        this.busFareService = busFareService;
    }

    @PostMapping("/CalculateTrainFare")
    @Override
    public ResponseEntity<APIResponse> getTrainFare(@RequestBody TrainFareRequest trainFareRequest) {
        FareResponse fareResponse = trainFareService.findFare(trainFareRequest);
        APIResponse apiResponse = new APIResponse(String.valueOf(HttpStatus.OK.value()),
                HttpStatus.OK.getReasonPhrase(), fareResponse);
        logger.info("FareControllerImpl.getTrainFare Method:{}", "get train fare");
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/GetAllTrainFare")
    @Override
    public ResponseEntity<APIResponse> getGetAllTrainFare() {
        APIResponse apiResponse = new APIResponse(String.valueOf(HttpStatus.OK.value()),
                HttpStatus.OK.getReasonPhrase(), trainFareService.findAllFare());
        logger.info("FareControllerImpl.getGetAllTrainFare Method:{}", "get all train fare");
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/GetAllBusFare")
    @Override
    public ResponseEntity<APIResponse> getGetAllBusFare() {
        APIResponse apiResponse = new APIResponse(String.valueOf(HttpStatus.OK.value()),
                HttpStatus.OK.getReasonPhrase(), busFareService.findAllFare());
        logger.info("FareControllerImpl.getGetAllBusFare Method:{}", "get all bus fare");
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/GetBusFare")
    @Override
    public ResponseEntity<APIResponse> getBusFare(@RequestBody BusFareRequest busFareRequest) {
        FareResponse fareResponse = busFareService.findFare(busFareRequest);
        APIResponse apiResponse = new APIResponse(String.valueOf(HttpStatus.OK.value()),
                HttpStatus.OK.getReasonPhrase(), fareResponse);
        logger.info("FareControllerImpl.getBusFare Method:{}", "get bus fare");
        return ResponseEntity.ok(apiResponse);
    }
}
