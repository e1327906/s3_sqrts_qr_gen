package com.qre.tg.query.api.service.strategy;

import com.qre.tg.entity.feedback.FeedbackPK;
import org.springframework.stereotype.Component;

@Component
public class SuggestionFeedbackStrategy implements FeedbackStrategy {

    @Override
    public String generateAcknowledgmentSubject() {
        return "SQRTS - Thank you for your Suggestion!";
    }

    @Override
    public String generateAcknowledgment(FeedbackPK feedback) {
        return String.format("""
                Dear %s,
                \nWe want to express our gratitude for your valuable suggestion. Your suggestions plays a crucial role in guiding our development efforts.
                \nTicket ID: %s has been created to track this issue.
                \nThank you for being an active part of our community.
                \nSuggestion given:
                %s
                """, feedback.getName(), feedback.getFeedback_id().toString(), feedback.getMessage());
    }
}
