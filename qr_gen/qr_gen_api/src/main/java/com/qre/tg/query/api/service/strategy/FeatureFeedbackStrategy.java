package com.qre.tg.query.api.service.strategy;

import com.qre.tg.entity.feedback.FeedbackPK;
import org.springframework.stereotype.Component;

@Component
public class FeatureFeedbackStrategy implements FeedbackStrategy {

    @Override
    public String generateAcknowledgmentSubject() {
        return "SQRTS - Thank you for your feature request!";
    }

    @Override
    public String generateAcknowledgment(FeedbackPK feedback) {
        return String.format("""
                Dear %s,
                \nThank you for sharing your feature request with us. Your input is instrumental in guiding our development roadmap. We'll carefully evaluate the feasibility of implementing the suggested feature. We appreciate your enthusiasm for enhancing SQRTS.
                \nTicket ID: %s has been created to track this feature.
                \nThank you again for being a part of our journey.
                \nFeedback:
                %s
                """, feedback.getName(), feedback.getFeedback_id().toString(), feedback.getMessage());
    }
}
