package com.OnedayOwner.server.platform.feedback.repository;

import com.OnedayOwner.server.platform.feedback.entity.ReservationFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationFeedbackRepository extends JpaRepository<ReservationFeedback, Long> {
}
