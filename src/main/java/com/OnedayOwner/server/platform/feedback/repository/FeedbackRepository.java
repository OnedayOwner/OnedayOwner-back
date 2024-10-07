package com.OnedayOwner.server.platform.feedback.repository;

import com.OnedayOwner.server.platform.feedback.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}
