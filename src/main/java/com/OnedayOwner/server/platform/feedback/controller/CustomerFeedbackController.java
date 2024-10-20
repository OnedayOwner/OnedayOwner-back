package com.OnedayOwner.server.platform.feedback.controller;

import com.OnedayOwner.server.global.security.SecurityUtils;
import com.OnedayOwner.server.platform.feedback.dto.FeedbackDto;
import com.OnedayOwner.server.platform.feedback.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer/feedbacks")
public class CustomerFeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping("{reservationId}")
    public ResponseEntity<FeedbackDto.FeedbackDetail> registerFeedback(
            @RequestBody FeedbackDto.FeedbackForm form,
            @PathVariable("reservationId")Long reservationId,
            SecurityContextHolderAwareRequestWrapper request
    ){
        Long customerId = SecurityUtils.extractUserId(request);
        return ResponseEntity.ok()
                .body(feedbackService.registerFeedback(customerId, reservationId, form));
    }

    @PostMapping("{reservationId}/menuFeedback")
    public ResponseEntity<FeedbackDto.MenuFeedbackSummary> registerMenuFeedback(
            @RequestBody FeedbackDto.MenuFeedBackForm form,
            @PathVariable("reservationId")Long reservationId,
            SecurityContextHolderAwareRequestWrapper request
    ){
        Long customerId = SecurityUtils.extractUserId(request);
        return ResponseEntity.ok()
                .body(feedbackService.registerMenuFeedback(customerId, reservationId, form));
    }

    @GetMapping("list")
    public ResponseEntity<List<FeedbackDto.FeedbackSummary>> getMyFeedbackList(
            SecurityContextHolderAwareRequestWrapper request
    ){
        Long userId = SecurityUtils.extractUserId(request);
        return ResponseEntity.ok()
                .body(feedbackService.getMyFeedbackList(userId));
    }

    @GetMapping("{reservationId}")
    public ResponseEntity<FeedbackDto.FeedbackSummary> getMyFeedback(
            @PathVariable("reservationId")Long reservationId,
            SecurityContextHolderAwareRequestWrapper request
    ){
        Long userId = SecurityUtils.extractUserId(request);
        return ResponseEntity.ok()
                .body(feedbackService.getMyFeedback(userId, reservationId));
    }

}
