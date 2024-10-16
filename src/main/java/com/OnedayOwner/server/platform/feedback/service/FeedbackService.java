package com.OnedayOwner.server.platform.feedback.service;

import com.OnedayOwner.server.global.exception.BusinessException;
import com.OnedayOwner.server.global.exception.ErrorCode;
import com.OnedayOwner.server.platform.feedback.dto.FeedbackDto;
import com.OnedayOwner.server.platform.feedback.entity.MenuFeedback;
import com.OnedayOwner.server.platform.feedback.entity.Feedback;
import com.OnedayOwner.server.platform.feedback.repository.MenuFeedbackRepository;
import com.OnedayOwner.server.platform.feedback.repository.FeedbackRepository;
import com.OnedayOwner.server.platform.popup.entity.Menu;
import com.OnedayOwner.server.platform.popup.entity.PopupRestaurant;
import com.OnedayOwner.server.platform.popup.repository.MenuRepository;
import com.OnedayOwner.server.platform.popup.repository.PopupRestaurantRepository;
import com.OnedayOwner.server.platform.reservation.entity.Reservation;
import com.OnedayOwner.server.platform.reservation.repository.ReservationMenuRepository;
import com.OnedayOwner.server.platform.reservation.repository.ReservationRepository;
import com.OnedayOwner.server.platform.user.entity.Role;
import com.OnedayOwner.server.platform.user.entity.User;
import com.OnedayOwner.server.platform.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final MenuFeedbackRepository menuFeedbackRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationMenuRepository reservationMenuRepository;
    private final FeedbackRepository feedbackRepository;
    private final MenuRepository menuRepository;
    private final PopupRestaurantRepository popupRestaurantRepository;

    @Transactional
    public FeedbackDto.FeedbackDetail registerFeedback(Long userId, Long reservationId, FeedbackDto.FeedbackForm form){
        User user = userRepository.findByIdAndRole(userId, Role.CUSTOMER)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESERVATION_NOT_FOUND));
        if(feedbackRepository.findByReservationId(reservationId).isPresent()){
            throw new BusinessException(ErrorCode.FEEDBACK_ALREADY_EXIST);
        }

        Feedback feedback = Feedback.builder()
                .reservation(reservation)
                .score(form.getScore())
                .comment(form.getComment())
                .user(user)
                .build();

        form.getMenuFeedBackForms()
                .forEach(
                        menuFeedBackForm -> {
                            MenuFeedback menuFeedback = MenuFeedback.builder()
                                    .score(menuFeedBackForm.getScore())
                                    .comment(menuFeedBackForm.getComment())
                                    .reservationMenu(reservationMenuRepository.findById(
                                            menuFeedBackForm.getReservationMenuId()).orElseThrow(
                                            () -> new BusinessException(ErrorCode.RESERVATION_MENU_NOT_FOUND)
                                    ))
                                    .desiredPrice(menuFeedBackForm.getDesiredPrice())
                                    .build();
                            feedback.addMenuFeedback(menuFeedback);
                        }
                );
        feedbackRepository.save(feedback);

        return new FeedbackDto.FeedbackDetail(feedback);
    }

    @Transactional
    public List<FeedbackDto.FeedbackSummary> getMyFeedbackList(Long customerId){
        return feedbackRepository.findByUserId(customerId)
                .stream().map(FeedbackDto.FeedbackSummary::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public FeedbackDto.FeedbackDetail getMyFeedback(Long customerId, Long feedbackId) {
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new BusinessException(ErrorCode.FEEDBACK_NOT_FOUND));

        if(!feedback.getUser().getId().equals(customerId)){
            throw new BusinessException(ErrorCode.FEEDBACK_USER_NOT_MATCH);
        }

        return new FeedbackDto.FeedbackDetail(feedback);
    }

    @Transactional
    public List<FeedbackDto.FeedbackSummary> getFeedbackList(Long ownerId, Long popupId){
        PopupRestaurant popupRestaurant = popupRestaurantRepository.findById(popupId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POPUP_NOT_FOUND));
        if(!popupRestaurant.getUser().getId().equals(ownerId)){
            throw new BusinessException(ErrorCode.POPUP_AND_USER_NOT_MATCH);
        }
        List<Feedback> feedbacks = feedbackRepository.findByPopupId(popupId);

        List<FeedbackDto.FeedbackSummary> feedbackSummaryList = new ArrayList<>();

        feedbacks.forEach(
                feedback -> {
                    FeedbackDto.FeedbackSummary feedbackSummary = new FeedbackDto.FeedbackSummary(feedback);
                    feedbackSummaryList.add(feedbackSummary);
                }
        );
        return feedbackSummaryList;
    }

    @Transactional
    public FeedbackDto.FeedbackDetail getFeedbackDetail(Long ownerId, Long feedbackId){
        Feedback feedback = feedbackRepository.findByIdFetchJoin(feedbackId).orElseThrow(
                () -> new BusinessException(ErrorCode.FEEDBACK_NOT_FOUND));
        if(!feedback.getReservation().getPopupRestaurant().getUser().getId().equals(ownerId)){
            throw new BusinessException(ErrorCode.POPUP_AND_USER_NOT_MATCH);
        }
        return new FeedbackDto.FeedbackDetail(feedback);
    }

    @Transactional
    public List<FeedbackDto.MenuFeedbackSummary> getFeedbackByMenu (Long ownerId, Long menuId){
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MENU_NOT_FOUND));
        if (!menu.getPopupRestaurant().getUser().getId().equals(ownerId)) {
            throw new BusinessException(ErrorCode.POPUP_AND_USER_NOT_MATCH);
        }
        List<MenuFeedback> menuFeedbacks = menuFeedbackRepository.findByMenuId(menuId);

        List<FeedbackDto.MenuFeedbackSummary> menuFeedbackSummaryList = new ArrayList<>();

        menuFeedbacks.forEach(
                menuFeedback -> {
                    FeedbackDto.MenuFeedbackSummary menuFeedbackSummary =
                            new FeedbackDto.MenuFeedbackSummary(menuFeedback);
                    menuFeedbackSummaryList.add(menuFeedbackSummary);
                }
        );

        return menuFeedbackSummaryList;
    }



}

