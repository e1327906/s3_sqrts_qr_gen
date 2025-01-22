package com.qre.tg.query.api.service;
import com.qre.tg.dto.route.TrainRouteResponse;
import java.util.List;

public interface TrainRouteService{

    List<TrainRouteResponse> findAll();
}
