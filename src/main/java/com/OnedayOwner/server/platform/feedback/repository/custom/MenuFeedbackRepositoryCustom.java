package com.OnedayOwner.server.platform.feedback.repository.custom;

import com.OnedayOwner.server.platform.feedback.entity.MenuFeedback;

import java.util.List;

public interface MenuFeedbackRepositoryCustom {

    List<MenuFeedback> findByMenuId(Long menuId);

}
