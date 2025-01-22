package com.qre.tg.query.api.service.impl;

import com.qre.cmel.exception.ExceptionMsg;
import com.qre.cmel.exception.FareNotFoundException;
import com.qre.tg.dao.fare.BusFareMatrixRepository;
import com.qre.tg.dto.fare.BusFareRequest;
import com.qre.tg.dto.fare.FareResponse;
import com.qre.tg.entity.fare.BusFareMatrix;
import com.qre.tg.query.api.service.TripFareService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BusFareServiceImplTest {

    @Mock
    private BusFareMatrixRepository busFareMatrixRepository;

    @InjectMocks
    private BusFareServiceImpl busFareService;

    @Mock
    private TripFareServiceFactoryImpl tripFareServiceFactory;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindFare_OutboundJourney() {
        BusFareRequest request = new BusFareRequest();
        request.setSrcBusStopId(1);
        request.setDestBusStopId(2);
        request.setTicketType(1);
        request.setGroupSize(1);

        BusFareMatrix fareMatrixOutbound = new BusFareMatrix();
        fareMatrixOutbound.setFare(100L);
        TripFareService tripFareServiceMock = mock(TripFareService.class);
        when(tripFareServiceFactory.isHoliday(any())).thenReturn(true); // Mock isHoliday method
        when(tripFareServiceFactory.createTripFare(true)).thenReturn(tripFareServiceMock);
        when(tripFareServiceMock.calculateTripFare(100L)).thenReturn(100L);

        when(busFareMatrixRepository.findById(any())).thenReturn(Optional.of(fareMatrixOutbound));
        FareResponse response = busFareService.findFare(request);
        assertEquals(100, response.getFare(), "Fare must be 100");
    }

    @Test
    public void testFindFare_ReturnJourney() {
        BusFareRequest request = new BusFareRequest();
        request.setSrcBusStopId(1);
        request.setDestBusStopId(2);
        request.setTicketType(1);
        request.setGroupSize(1);

        BusFareMatrix fareMatrixOutbound = new BusFareMatrix();
        fareMatrixOutbound.setFare(100L);

        BusFareMatrix fareMatrixReturn = new BusFareMatrix();
        fareMatrixReturn.setFare(0L);

        TripFareService tripFareServiceMock = mock(TripFareService.class);
        when(tripFareServiceFactory.isHoliday(any())).thenReturn(true); // Mock isHoliday method
        when(tripFareServiceFactory.createTripFare(true)).thenReturn(tripFareServiceMock);
        when(tripFareServiceMock.calculateTripFare(100L)).thenReturn(100L);

        when(busFareMatrixRepository.findById(any())).thenReturn(Optional.of(fareMatrixOutbound), Optional.of(fareMatrixReturn));

        FareResponse response = busFareService.findFare(request);

        assertEquals(100, response.getFare(), "Fare must be 100");
    }

    @Test
    public void testFindFare_FareNotFoundException() {
        BusFareRequest request = new BusFareRequest();
        request.setSrcBusStopId(1);
        request.setDestBusStopId(2);
        request.setTicketType(1);
        request.setGroupSize(1);

        when(busFareMatrixRepository.findById(any())).thenReturn(Optional.empty());

        try {
            busFareService.findFare(request);
        } catch (Exception e) {
            assertEquals(FareNotFoundException.class, e.getClass());
            assertEquals(ExceptionMsg.FARE_NOT_FOUND, e.getMessage());
        }
    }

    // Add more test cases as needed

}