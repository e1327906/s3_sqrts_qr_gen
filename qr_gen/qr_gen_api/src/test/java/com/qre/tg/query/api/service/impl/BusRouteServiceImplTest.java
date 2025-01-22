package com.qre.tg.query.api.service.impl;

import com.qre.tg.dao.route.BusRouteRepository;
import com.qre.tg.dto.route.BusRouteResponse;
import com.qre.tg.entity.route.BusStop;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BusRouteServiceImplTest {

    @Mock
    private BusRouteRepository repository;

    @InjectMocks
    private BusRouteServiceImpl service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAll() {
        // Create mock data
        List<BusStop> mockStations = new ArrayList<>();
        BusStop data = BusStop.builder()
                .busStopId(1)
                .busStopCode("bus001")
                .busStopFullName("Bus Stop full name")
                .build();
        mockStations.add(data);

        when(repository.findAll()).thenReturn(mockStations);

        // Call the method to test
        List<BusRouteResponse> responses = service.findAll();

        // Verify the results
        assertEquals(mockStations.size(), responses.size());
        assertEquals(data.getBusStopId(), responses.get(0).getBusStopId());
        assertEquals(data.getBusStopCode(), responses.get(0).getBusStopCode());
        assertEquals(data.getBusStopFullName(), responses.get(0).getBusStopFullName());

        // Verify that findAll() was called on the repository
        verify(repository, times(1)).findAll();
    }
}