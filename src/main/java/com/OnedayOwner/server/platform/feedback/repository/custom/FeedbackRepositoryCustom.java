package com.OnedayOwner.server.platform.feedback.repository.custom;

import com.OnedayOwner.server.platform.feedback.dto.FeedbackDto;
import com.OnedayOwner.server.platform.feedback.entity.Feedback;

import java.util.List;
import java.util.Optional;

public interface FeedbackRepositoryCustom {

    Optional<Feedback> findByIdFetchJoin(Long feedbackId);

    List<Feedback> findByPopupId(Long popupId);

    Long countByPopupId(Long popupId);

}
