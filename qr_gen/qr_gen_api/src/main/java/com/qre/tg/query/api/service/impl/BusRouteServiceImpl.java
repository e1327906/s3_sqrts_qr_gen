package com.qre.tg.query.api.service.impl;

import com.qre.cmel.exception.ExceptionMsg;
import com.qre.cmel.exception.FareNotFoundException;
import com.qre.tg.dao.route.BusRouteRepository;
import com.qre.tg.dto.fare.FareResponse;
import com.qre.tg.dto.route.BusRouteResponse;
import com.qre.tg.dto.route.TrainRouteResponse;
import com.qre.tg.entity.fare.TrainFareMatrix;
import com.qre.tg.entity.fare.TrainFareMatrixPK;
import com.qre.tg.entity.route.BusStop;
import com.qre.tg.entity.route.Station;
import com.qre.tg.query.api.service.BusRouteService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class BusRouteServiceImpl implements BusRouteService {

    private final BusRouteRepository busRouteRepository;

    @Override
    public List<BusRouteResponse> findAll() {
        ModelMapper modelMapper = new ModelMapper();

        List<BusStop> entities = busRouteRepository.findAll();
        return entities.stream()
                .map(entity -> modelMapper.map(entity, BusRouteResponse.class))
                .collect(Collectors.toList());
    }
}
