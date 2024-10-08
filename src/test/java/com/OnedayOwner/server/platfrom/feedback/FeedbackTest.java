package com.OnedayOwner.server.platfrom.feedback;

import com.OnedayOwner.server.platform.feedback.dto.FeedbackDto;
import com.OnedayOwner.server.platform.feedback.service.FeedbackService;
import com.OnedayOwner.server.platform.reservation.dto.ReservationDto;
import com.OnedayOwner.server.platform.reservation.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class FeedbackTest {

    @Autowired
    FeedbackService feedbackService;
    @Autowired
    ReservationService reservationService;

    @BeforeEach
    @Transactional
    public void reservation(){
        List<ReservationDto.ReservationMenuForm> menuList = new ArrayList<>();
        ReservationDto.ReservationMenuForm menuForm = new ReservationDto.ReservationMenuForm(1,1L);
        ReservationDto.ReservationMenuForm menuForm2 = new ReservationDto.ReservationMenuForm(2,2L);

        menuList.add(menuForm);
        menuList.add(menuForm2);

        ReservationDto.ReservationForm form = ReservationDto.ReservationForm.builder()
                .popupId(1L)
                .reservationTimeId(1L)
                .numberOfPeople(2)
                .reservationMenus(menuList)
                .build();

        reservationService.registerReservation(form, 2L);
    }

    @Test
    @Transactional
    public void registerFeedback(){
        List<FeedbackDto.MenuFeedBackForm> mfbList = new ArrayList<>();

        FeedbackDto.MenuFeedBackForm mfb1 = new FeedbackDto.MenuFeedBackForm(
                1L,
                4,
                10000,
                "청경채의 익힘이 타이트 하네요."
        );
        FeedbackDto.MenuFeedBackForm mfb2 = new FeedbackDto.MenuFeedBackForm(
                2L,
                3,
                7000,
                "짜요 탈락입니다."
        );
        mfbList.add(mfb1);
        mfbList.add(mfb2);

        FeedbackDto.FeedbackForm form = new FeedbackDto.FeedbackForm(
                4,
                "맛있어요.",
                mfbList
        );

        FeedbackDto.FeedbackDetail fb = feedbackService.registerFeedback(
                2L, 1L, form
        );

    }
}
