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
@RequestMapping("/api/owners/feedbacks")
public class OwnerFeedbackController {

    private final FeedbackService feedbackService;

    @GetMapping("/list/{popupId}")
    public ResponseEntity<List<FeedbackDto.FeedbackSummary>> getFeedbackList(
            @PathVariable("popupId")Long popupId,
            SecurityContextHolderAwareRequestWrapper request
    ){
        Long ownerId = SecurityUtils.extractUserId(request);
        return ResponseEntity.ok()
                .body(feedbackService.getFeedbackList(ownerId, popupId));
    }

    @GetMapping("/{feedbackId}")
    public ResponseEntity<FeedbackDto.FeedbackSummary> getFeedbackDetail(
            @PathVariable("feedbackId")Long feedbackId,
            SecurityContextHolderAwareRequestWrapper request
    ){
        Long ownerId = SecurityUtils.extractUserId(request);
        return ResponseEntity.ok()
                .body(feedbackService.getFeedbackDetail(ownerId, feedbackId));
    }

    @GetMapping("menus/{menuId}")
    public ResponseEntity<List<FeedbackDto.MenuFeedbackSummary>> getMenuFeedback(
            @PathVariable("menuId")Long menuId,
            SecurityContextHolderAwareRequestWrapper request
    ){
        Long ownerId = SecurityUtils.extractUserId(request);
        return ResponseEntity.ok()
                .body(feedbackService.getFeedbackByMenu(ownerId, menuId));
    }
}
