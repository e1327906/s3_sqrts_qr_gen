package com.qre.tg.query.api.service.impl;

import com.qre.tg.dao.route.TrainRouteRepository;
import com.qre.tg.dto.route.TrainRouteResponse;
import com.qre.tg.entity.route.Station;
import com.qre.tg.query.api.service.TrainRouteService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class TrainRouteServiceImpl implements TrainRouteService {

    private final TrainRouteRepository trainRouteRepository;

    @Override
    public List<TrainRouteResponse> findAll() {
        ModelMapper modelMapper = new ModelMapper();

        List<Station> entities = trainRouteRepository.findAll();
        return entities.stream()
                .map(entity -> modelMapper.map(entity, TrainRouteResponse.class))
                .collect(Collectors.toList());
    }
}
