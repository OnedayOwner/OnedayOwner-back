package com.OnedayOwner.server.platform.feedback.repository;

import com.OnedayOwner.server.platform.feedback.entity.Feedback;
import com.OnedayOwner.server.platform.feedback.repository.custom.FeedbackRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<Feedback, Long>, FeedbackRepositoryCustom {

    List<Feedback> findByUserId(Long userId);

}
