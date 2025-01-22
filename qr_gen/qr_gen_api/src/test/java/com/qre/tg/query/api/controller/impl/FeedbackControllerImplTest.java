package com.qre.tg.query.api.controller.impl;

import com.qre.tg.dto.base.APIResponse;
import com.qre.tg.dto.feedback.Feedback;
import com.qre.tg.query.api.service.FeedbackService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class FeedbackControllerImplTest {

    @Mock
    private FeedbackService feedbackService;

    private FeedbackControllerImpl feedbackController;

    Feedback feedback;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        feedbackController = new FeedbackControllerImpl(feedbackService);
        feedback = new Feedback("user", "user@example.com", "General", "This is a feedback message.", UUID.randomUUID());
    }

    @Test
    void testSaveFeedbackSuccess() {
        when(feedbackService.processFeedback(feedback)).thenReturn(true);

        ResponseEntity<APIResponse> responseEntity = feedbackController.saveFeedback(feedback);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        APIResponse apiResponse = responseEntity.getBody();
        assert apiResponse != null;
        assertEquals(String.valueOf(HttpStatus.OK.value()), apiResponse.getResponseCode());
        assertEquals(HttpStatus.OK.getReasonPhrase(), apiResponse.getResponseMsg());
        assertEquals(true, apiResponse.getResponseData());
        verify(feedbackService, times(1)).processFeedback(feedback);
    }

    @Test
    void testSaveFeedbackFailure() {
        when(feedbackService.processFeedback(feedback)).thenReturn(false);

        ResponseEntity<APIResponse> responseEntity = feedbackController.saveFeedback(feedback);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verify(feedbackService, times(1)).processFeedback(feedback);
    }

    @Test
    void testSaveFeedbackWithNullFeedback() {
        ResponseEntity<APIResponse> responseEntity = feedbackController.saveFeedback(null);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verify(feedbackService, never()).processFeedback(any(Feedback.class));
    }

    @Test
    void testSaveFeedbackWithInvalidFeedback() {
        Feedback invalidFeedback = new Feedback(null, null, null, null, null);

        ResponseEntity<APIResponse> responseEntity = feedbackController.saveFeedback(invalidFeedback);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verify(feedbackService, times(1)).processFeedback(invalidFeedback);
    }

    @Test
    void testSaveFeedbackLogging() {
        when(feedbackService.processFeedback(feedback)).thenReturn(true);

        feedbackController.saveFeedback(feedback);

        verify(feedbackService, times(1)).processFeedback(feedback);
        // Add assertions for log messages if needed
    }
}