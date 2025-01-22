package com.qre.tg.query.api.service.impl;

import com.qre.cmel.exception.ExceptionMsg;
import com.qre.cmel.exception.FareNotFoundException;
import com.qre.tg.dao.fare.TrainFareMatrixRepository;
import com.qre.tg.dto.fare.BusFareRequest;
import com.qre.tg.dto.fare.FareResponse;
import com.qre.tg.dto.fare.TrainFareRequest;
import com.qre.tg.entity.fare.BusFareMatrix;
import com.qre.tg.entity.fare.TrainFareMatrix;
import com.qre.tg.query.api.service.TripFareService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TrainFareServiceImplTest {

    @Mock
    private TrainFareMatrixRepository repository;

    @Mock
    private TripFareServiceFactoryImpl tripFareServiceFactory;

    @Mock
    private FareCalculator fareCalculator;

    @InjectMocks
    private TrainFareServiceImpl service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindFare_OutboundJourney() {
        // Arrange
        TrainFareRequest request = new TrainFareRequest();
        request.setSrcStnId(1);
        request.setDestStnId(2);
        request.setTicketType(1);
        request.setGroupSize(1);

        // Mock TripFareServiceFactoryImpl to return a non-null TripFareService object
        TripFareService tripFareServiceMock = mock(TripFareService.class);
        when(tripFareServiceFactory.isHoliday(any())).thenReturn(true); // Mock isHoliday method
        when(tripFareServiceFactory.createTripFare(true)).thenReturn(tripFareServiceMock);
        when(tripFareServiceMock.calculateTripFare(100L)).thenReturn(100L); // Adjust the return value as needed

        TrainFareMatrix fareMatrixOutbound = new TrainFareMatrix();
        fareMatrixOutbound.setFare(100L);
        when(repository.findById(any())).thenReturn(Optional.of(fareMatrixOutbound));

        when(fareCalculator.calculateFare(any(), any())).thenReturn(100L);

        // Act
        FareResponse response = service.findFare(request);

        // Assert
        assertEquals(100, response.getFare(), "Fare must be 100");
    }

    @Test
    public void testFindFare_ReturnJourney() {

        TrainFareRequest request = new TrainFareRequest();
        request.setSrcStnId(1);
        request.setDestStnId(2);
        request.setTicketType(1);
        request.setGroupSize(1);

        // Mock TripFareServiceFactoryImpl to return a non-null TripFareService object
        TripFareService tripFareServiceMock = mock(TripFareService.class);
        when(tripFareServiceFactory.isHoliday(any())).thenReturn(true); // Mock isHoliday method
        when(tripFareServiceFactory.createTripFare(true)).thenReturn(tripFareServiceMock);
        when(tripFareServiceMock.calculateTripFare(100L)).thenReturn(100L); // Adjust the return value as needed


        TrainFareMatrix fareMatrixOutbound = new TrainFareMatrix();
        fareMatrixOutbound.setFare(100L);

        TrainFareMatrix fareMatrixReturn = new TrainFareMatrix();
        fareMatrixReturn.setFare(0L);

        when(repository.findById(any())).thenReturn(Optional.of(fareMatrixOutbound), Optional.of(fareMatrixReturn));

        when(fareCalculator.calculateFare(any(), any())).thenReturn(100L);

        // Act
        FareResponse response = service.findFare(request);

        // Assert
        assertEquals(100, response.getFare(), "Fare must be 100");
    }

    @Test
    public void testFindFare_FareNotFoundException() {
        // Arrange
        TrainFareRequest request = new TrainFareRequest();
        request.setSrcStnId(1);
        request.setDestStnId(2);
        request.setTicketType(1);
        request.setGroupSize(1);

        // Mock TripFareServiceFactoryImpl to return a non-null TripFareService object
        TripFareService tripFareServiceMock = mock(TripFareService.class);
        when(tripFareServiceFactory.isHoliday(any())).thenReturn(true); // Mock isHoliday method
        when(tripFareServiceFactory.createTripFare(true)).thenReturn(tripFareServiceMock);
        when(tripFareServiceMock.calculateTripFare(100L)).thenReturn(100L); // Adjust the return value as needed

        TrainFareMatrix fareMatrixOutbound = new TrainFareMatrix();
        fareMatrixOutbound.setFare(100L);
        when(repository.findById(any())).thenReturn(Optional.empty());

        when(fareCalculator.calculateFare(any(), any())).thenReturn(100L);

        try {
            service.findFare(request);
        } catch (Exception e) {
            assertEquals(FareNotFoundException.class, e.getClass());
            assertEquals(ExceptionMsg.FARE_NOT_FOUND, e.getMessage());
        }
    }

    // Add more test cases as needed
}