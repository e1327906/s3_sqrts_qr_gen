package com.qre.tg.query.api.controller.impl;

import com.qre.tg.dto.base.APIResponse;
import com.qre.tg.dto.route.BusRouteResponse;
import com.qre.tg.dto.route.TrainRouteResponse;
import com.qre.tg.query.api.service.BusRouteService;
import com.qre.tg.query.api.service.TrainRouteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RouteControllerImplTest {

    @Mock
    private TrainRouteService trainRouteService;

    @Mock
    private BusRouteService busRouteService;

    @InjectMocks
    private RouteControllerImpl routeController;

    @Test
    void testGetTrainRoutes() {
        // Mock data
        List<TrainRouteResponse> trainRouteList = Collections.singletonList(new TrainRouteResponse());

        // Mock trainRouteService behavior
        when(trainRouteService.findAll()).thenReturn(trainRouteList);

        // Call controller method
        ResponseEntity<APIResponse> responseEntity = routeController.getTrainRoutes();

        // Verify trainRouteService method is called
        verify(trainRouteService).findAll();

        // Verify response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(trainRouteList, responseEntity.getBody().getResponseData());
    }

    @Test
    void testGetBusRoutes() {
        // Mock data
        List<BusRouteResponse> busRouteList = Collections.singletonList(new BusRouteResponse());

        // Mock busRouteService behavior
        when(busRouteService.findAll()).thenReturn(busRouteList);

        // Call controller method
        ResponseEntity<APIResponse> responseEntity = routeController.getBusRoutes();

        // Verify busRouteService method is called
        verify(busRouteService).findAll();

        // Verify response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(busRouteList, responseEntity.getBody().getResponseData());
    }
}
