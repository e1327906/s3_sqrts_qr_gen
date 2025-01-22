package com.qre.tg.dao.route;

import com.qre.tg.entity.route.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

;

@Repository
public interface TrainRouteRepository extends JpaRepository<Station, Integer> {
}
