package com.OnedayOwner.server.platform.feedback.repository.custom;

import com.OnedayOwner.server.platform.feedback.dto.FeedbackDto;
import com.OnedayOwner.server.platform.feedback.entity.Feedback;

import java.util.List;

public interface FeedbackRepositoryCustom {

    List<Feedback> findByPopupId(Long popupId);

}
