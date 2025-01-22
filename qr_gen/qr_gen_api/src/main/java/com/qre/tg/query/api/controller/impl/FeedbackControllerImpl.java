package com.qre.tg.query.api.controller.impl;

import com.qre.tg.dto.base.APIResponse;
import com.qre.tg.dto.feedback.Feedback;
import com.qre.tg.query.api.controller.FeedbackController;
import com.qre.tg.query.api.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/general/")
@RequiredArgsConstructor
public class FeedbackControllerImpl implements FeedbackController {
    private final Logger logger = LoggerFactory.getLogger(FeedbackControllerImpl.class);

    private final FeedbackService feedbackService;

    @PostMapping("/Feedback")
    @Override
    public ResponseEntity<APIResponse> saveFeedback(@RequestBody Feedback feedback) {
        boolean feedbackResp = feedbackService.processFeedback(feedback);
        if (!feedbackResp) {
            return ResponseEntity.badRequest().build();
        }
        APIResponse apiResponse = new APIResponse(String.valueOf(HttpStatus.OK.value()),
                HttpStatus.OK.getReasonPhrase(), feedbackResp);

        logger.info("FeedbackControllerImpl.saveFeedback Method");
        return ResponseEntity.ok(apiResponse);
    }
}