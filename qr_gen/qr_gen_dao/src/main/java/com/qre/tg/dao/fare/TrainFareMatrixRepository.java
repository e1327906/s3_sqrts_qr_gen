package com.qre.tg.dao.fare;

import com.qre.tg.entity.fare.TrainFareMatrix;
import com.qre.tg.entity.fare.TrainFareMatrixPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainFareMatrixRepository extends JpaRepository<TrainFareMatrix, TrainFareMatrixPK> {
}
