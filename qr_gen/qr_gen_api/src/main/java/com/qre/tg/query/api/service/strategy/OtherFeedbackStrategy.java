package com.qre.tg.query.api.service.strategy;

import com.qre.tg.entity.feedback.FeedbackPK;
import org.springframework.stereotype.Component;

@Component
public class OtherFeedbackStrategy implements FeedbackStrategy {

    @Override
    public String generateAcknowledgmentSubject() {
        return "SQRTS - Thank you for your feedback!";
    }

    @Override
    public String generateAcknowledgment(FeedbackPK feedback) {
        return String.format("""
                Dear %s,
                \nThank you for reaching out to us. We've received your feedback, and our team will review it promptly. If you have any further questions or concerns, please feel free to contact us. Your engagement with our platform is highly valued.
                \nTicket ID: %s has been created to track this feature.
                \nThank you again for being a part of our journey.
                \nFeedback:
                %s
                """, feedback.getName(), feedback.getFeedback_id().toString(), feedback.getMessage());
    }
}
