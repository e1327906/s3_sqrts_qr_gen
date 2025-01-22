package com.qre.tg.dao.route;

import com.qre.tg.entity.route.BusStop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusRouteRepository extends JpaRepository<BusStop, Integer> {
}
