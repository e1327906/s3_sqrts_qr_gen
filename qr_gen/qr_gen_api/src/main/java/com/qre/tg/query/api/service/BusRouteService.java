
package com.qre.tg.query.api.service;

import com.qre.tg.dto.route.BusRouteResponse;

import java.util.List;

public interface BusRouteService {

    List<BusRouteResponse> findAll();
}
