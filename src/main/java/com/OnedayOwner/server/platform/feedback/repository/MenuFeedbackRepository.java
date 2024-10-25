package com.OnedayOwner.server.platform.feedback.repository;

import com.OnedayOwner.server.platform.feedback.entity.MenuFeedback;
import com.OnedayOwner.server.platform.feedback.repository.custom.MenuFeedbackRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuFeedbackRepository extends JpaRepository<MenuFeedback, Long>, MenuFeedbackRepositoryCustom {

//    MenuFeedback findById(Long id);

}
