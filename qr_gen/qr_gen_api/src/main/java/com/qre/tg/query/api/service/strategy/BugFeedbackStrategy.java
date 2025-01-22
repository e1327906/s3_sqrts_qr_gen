package com.qre.tg.query.api.service.strategy;

import com.qre.tg.entity.feedback.FeedbackPK;
import org.springframework.stereotype.Component;

@Component
public class BugFeedbackStrategy implements FeedbackStrategy {

    @Override
    public String generateAcknowledgmentSubject() {
        return "SQRTS - Thank you for catching Our Bugs";
    }

    @Override
    public String generateAcknowledgment(FeedbackPK feedback) {
        return String.format("""
                Dear %s,
                \nWe sincerely appreciate you bringing the issue to our attention. Our team is currently investigating the reported bug. Your input is invaluable as we continually strive to improve SQRTS, we'll work diligently to resolve it as soon as possible.
                \nTicket ID: %s has been created to track this issue. A Customer Service Representative will be in contact with you within 3 - 5 working days.
                \nThank you for being a part of our journey.
                \nBug Reported:
                %s
                """, feedback.getName(), feedback.getFeedback_id().toString(), feedback.getMessage());
    }
}
