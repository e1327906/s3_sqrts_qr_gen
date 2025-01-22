package com.qre.tg.dao.fare;

import com.qre.tg.entity.fare.BusFareMatrix;
import com.qre.tg.entity.fare.BusFareMatrixPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusFareMatrixRepository extends JpaRepository<BusFareMatrix, BusFareMatrixPK> {
}
