package com.qre.tg.dao.ticket;

import com.qre.tg.entity.ticket.JourneyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JourneyTypeRepository extends JpaRepository<JourneyType, Integer> {
}
