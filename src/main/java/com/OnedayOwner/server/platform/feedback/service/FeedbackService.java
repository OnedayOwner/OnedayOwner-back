package com.OnedayOwner.server.platform.feedback.service;

import com.OnedayOwner.server.platform.feedback.repository.MenuFeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final MenuFeedbackRepository menuFeedbackRepository;

    @Transactional
    public void registerFeedback(Long regi)
}

