package com.qre.tg.query.api.service.strategy;

import com.qre.tg.entity.feedback.FeedbackPK;

public interface FeedbackStrategy {

    String generateAcknowledgmentSubject();
    String generateAcknowledgment(FeedbackPK feedback);

}
