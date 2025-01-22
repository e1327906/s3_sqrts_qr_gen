package com.qre.tg.query.api.controller;

import com.qre.tg.dto.base.APIResponse;
import com.qre.tg.dto.feedback.Feedback;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface FeedbackController {

    ResponseEntity<APIResponse> saveFeedback(@RequestBody Feedback feedback);

}
