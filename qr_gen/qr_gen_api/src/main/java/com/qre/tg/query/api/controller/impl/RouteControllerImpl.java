
package com.qre.tg.query.api.controller.impl;

import com.qre.tg.dto.base.APIResponse;
import com.qre.tg.dto.route.BusRouteResponse;
import com.qre.tg.dto.route.TrainRouteResponse;
import com.qre.tg.query.api.controller.RouteController;
import com.qre.tg.query.api.service.BusRouteService;
import com.qre.tg.query.api.service.TrainRouteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.qre.tg.query.api.common.Constants.BASE_URL;

@RestController
@RequestMapping("/api/v1/routes")
public class RouteControllerImpl implements RouteController {

    private final Logger logger = LoggerFactory.getLogger(RouteControllerImpl.class);
    private final TrainRouteService trainRouteService;
    private final BusRouteService busRouteService;

    @Autowired
    public RouteControllerImpl(TrainRouteService trainRouteService, BusRouteService busRouteService) {
        this.trainRouteService = trainRouteService;
        this.busRouteService = busRouteService;
    }

    @GetMapping("/GetTrainRoutes")
    @Override
    public ResponseEntity<APIResponse> getTrainRoutes() {
        APIResponse apiResponse;
        List<TrainRouteResponse> trainRouteList = trainRouteService.findAll();
        apiResponse = new APIResponse(String.valueOf(HttpStatus.OK.value()),
                HttpStatus.OK.getReasonPhrase(), trainRouteList);
        logger.info("RouteControllerImpl.getTrainRoutes Method:{}", "get train routes");
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/GetBusRoutes")
    @Override
    public ResponseEntity<APIResponse> getBusRoutes() {
        APIResponse apiResponse;
        List<BusRouteResponse> busRouteList = busRouteService.findAll();
        apiResponse = new APIResponse(String.valueOf(HttpStatus.OK.value()),
                HttpStatus.OK.getReasonPhrase(), busRouteList);
        logger.info("RouteControllerImpl.getBusRoutes Method:{}", "get bus routes");
        return ResponseEntity.ok(apiResponse);
    }
}
