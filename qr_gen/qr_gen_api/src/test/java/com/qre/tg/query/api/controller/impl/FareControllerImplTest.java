package com.qre.tg.query.api.controller.impl;

import com.qre.tg.dto.base.APIResponse;
import com.qre.tg.dto.fare.BusFareRequest;
import com.qre.tg.dto.fare.FareResponse;
import com.qre.tg.dto.fare.TrainFareRequest;
import com.qre.tg.entity.fare.BusFareMatrix;
import com.qre.tg.entity.fare.TrainFareMatrix;
import com.qre.tg.query.api.service.TrainFareService;
import com.qre.tg.query.api.service.BusFareService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FareControllerImplTest {

    @Mock
    private TrainFareService trainFareService;

    @Mock
    private BusFareService busFareService;

    @InjectMocks
    private FareControllerImpl fareController;

    private TrainFareRequest trainRequest;
    private BusFareRequest busRequest;

    @BeforeEach
    public void setUp() {
        trainRequest = new TrainFareRequest();
        busRequest = new BusFareRequest();
    }

    @Test
    public void testGetTrainFare() {
        // Mocking
        FareResponse mockedResponse = new FareResponse();
        mockedResponse.setFare(100);
        when(trainFareService.findFare(any(TrainFareRequest.class))).thenReturn(mockedResponse);

        // Test
        ResponseEntity<APIResponse> response = fareController.getTrainFare(trainRequest);
        FareResponse fareResponse = (FareResponse) response.getBody().getResponseData();

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code should be OK");
        assertNotNull(fareResponse, "Response body should not be null");
        assertEquals(100, fareResponse.getFare(), "Fare must be 100");
    }

    @Test
    public void testGetAllTrainFare() {
        // Mocking
        List<TrainFareMatrix> mockedResponse = Collections.singletonList(new TrainFareMatrix());
        when(trainFareService.findAllFare()).thenReturn(mockedResponse);

        // Test
        ResponseEntity<APIResponse> response = fareController.getGetAllTrainFare();
        List<TrainFareMatrix> fareList = (List<TrainFareMatrix>) response.getBody().getResponseData();

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code should be OK");
        assertNotNull(fareList, "List should not be null");
        assertEquals(1, fareList.size(), "List should have one item");
    }

    @Test
    public void testGetAllBusFare() {
        // Mocking
        List<BusFareMatrix> mockedResponse = Collections.singletonList(new BusFareMatrix());
        when(busFareService.findAllFare()).thenReturn(mockedResponse);

        // Test
        ResponseEntity<APIResponse> response = fareController.getGetAllBusFare();
        List<BusFareMatrix> fareList = (List<BusFareMatrix>) response.getBody().getResponseData();

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code should be OK");
        assertNotNull(fareList, "List should not be null");
        assertEquals(1, fareList.size(), "List should have one item");
    }

    @Test
    public void testGetBusFare() {
        // Mocking
        FareResponse mockedResponse = new FareResponse();
        mockedResponse.setFare(100);
        when(busFareService.findFare(any(BusFareRequest.class))).thenReturn(mockedResponse);

        // Test
        ResponseEntity<APIResponse> response = fareController.getBusFare(busRequest);
        FareResponse fareResponse = (FareResponse) response.getBody().getResponseData();

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code should be OK");
        assertNotNull(fareResponse, "Response body should not be null");
        assertEquals(100, fareResponse.getFare(), "Fare must be 100");
    }
}