package com.qre.tg.query.api.service.impl;

import com.qre.tg.entity.feedback.FeedbackPK;
import com.qre.tg.query.api.service.strategy.BugFeedbackStrategy;
import com.qre.tg.query.api.service.strategy.FeedbackStrategy;
import com.qre.tg.query.api.service.strategy.GeneralFeedbackStrategy;
import com.qre.tg.query.api.service.strategy.OtherFeedbackStrategy;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeedbackServiceImplTest {

    @Mock
    private Map<String, FeedbackStrategy> strategies;

    @Mock
    private GeneralFeedbackStrategy generalStrategy;

    @Mock
    private OtherFeedbackStrategy otherStrategy;

    @Mock
    private BugFeedbackStrategy bugStrategy;

    @Before("")
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        strategies = new HashMap<>();
        strategies.put("general", generalStrategy);
        strategies.put("others", otherStrategy);
        strategies.put("bug", bugStrategy);

        when(generalStrategy.generateAcknowledgment(any(FeedbackPK.class))).thenReturn("Acknowledgment message");
        when(generalStrategy.generateAcknowledgmentSubject()).thenReturn("Acknowledgment subject");
    }

    @Test
    void testGenerateAcknowledgmentOfSpecificStrategy() {
        FeedbackPK feedbackPK = FeedbackPK.builder().name("John Doe").email("john@example.com").category("General").message("I need help with my order").build();

        when(generalStrategy.generateAcknowledgment(feedbackPK)).thenReturn("Thank you for your feedback");

        String acknowledgment = generalStrategy.generateAcknowledgment(feedbackPK);

        assertEquals("Thank you for your feedback", acknowledgment);
    }

    @Test
    void testGenerateAcknowledgmentSubjectOfSpecificStrategy() {
        when(generalStrategy.generateAcknowledgmentSubject()).thenReturn("Feedback Acknowledgment");

        String subject = generalStrategy.generateAcknowledgmentSubject();

        assertEquals("Feedback Acknowledgment", subject);
    }

    @Test
    void testGenerateAcknowledgment() {
        FeedbackPK mockFeedback = mock(FeedbackPK.class);
        FeedbackStrategy mockStrategy = mock(FeedbackStrategy.class);
        when(mockStrategy.generateAcknowledgment(mockFeedback)).thenReturn("Acknowledgment message");

        String acknowledgment = mockStrategy.generateAcknowledgment(mockFeedback);

        assertEquals("Acknowledgment message", acknowledgment);
    }

    @Test
    void testGenerateAcknowledgmentSubject() {
        FeedbackStrategy mockStrategy = mock(FeedbackStrategy.class);
        when(mockStrategy.generateAcknowledgmentSubject()).thenReturn("Acknowledgment subject");

        String subject = mockStrategy.generateAcknowledgmentSubject();

        assertEquals("Acknowledgment subject", subject);
    }
}