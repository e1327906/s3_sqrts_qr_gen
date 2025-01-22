package com.qre.tg.query.api.service.impl;

import com.qre.tg.dao.route.TrainRouteRepository;
import com.qre.tg.dto.route.TrainRouteResponse;
import com.qre.tg.entity.route.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TrainRouteServiceImplTest {

    @Mock
    private TrainRouteRepository trainRouteRepository;

    @InjectMocks
    private TrainRouteServiceImpl trainRouteService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAll() {
        // Create mock data
        List<Station> mockStations = new ArrayList<>();
        Station station = Station.builder()
                .stnId(1)
                .stnCode("stn001")
                .stnFullName("Station 1")
                .build();
        mockStations.add(station);

        when(trainRouteRepository.findAll()).thenReturn(mockStations);

        // Call the method to test
        List<TrainRouteResponse> responses = trainRouteService.findAll();

        // Verify the results
        assertEquals(mockStations.size(), responses.size());
        assertEquals(station.getId(), responses.get(0).getStnId());
        assertEquals(station.getStnCode(), responses.get(0).getStnCode());
        assertEquals(station.getStnFullName(), responses.get(0).getStnName());

        // Verify that findAll() was called on the repository
        verify(trainRouteRepository, times(1)).findAll();
    }
}
