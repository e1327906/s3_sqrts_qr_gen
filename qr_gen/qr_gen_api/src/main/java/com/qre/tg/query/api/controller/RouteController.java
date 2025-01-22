package com.qre.tg.query.api.controller;

import com.qre.tg.dto.base.APIResponse;
import org.springframework.http.ResponseEntity;

public interface RouteController {
    ResponseEntity<APIResponse> getTrainRoutes();

    ResponseEntity<APIResponse> getBusRoutes();

}
