package com.qre.tg.dao.feedback;

import com.qre.tg.entity.feedback.FeedbackPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FeedbackRepository extends JpaRepository<FeedbackPK, UUID> {
}
