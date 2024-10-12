package com.OnedayOwner.server.platfrom.feedback;

import com.OnedayOwner.server.global.exception.BusinessException;
import com.OnedayOwner.server.platform.feedback.dto.FeedbackDto;
import com.OnedayOwner.server.platform.feedback.service.FeedbackService;
import com.OnedayOwner.server.platform.reservation.dto.ReservationDto;
import com.OnedayOwner.server.platform.reservation.service.ReservationService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@Transactional
public class FeedbackTest {

    @Autowired
    FeedbackService feedbackService;
    @Autowired
    ReservationService reservationService;

    @Test
    @Order(1)
    public void t1_reservation(){
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
        ReservationDto.ReservationForm form2 = ReservationDto.ReservationForm.builder()
                .popupId(1L)
                .reservationTimeId(2L)
                .numberOfPeople(2)
                .reservationMenus(menuList)
                .build();
        ReservationDto.ReservationForm form3 = ReservationDto.ReservationForm.builder()
                .popupId(1L)
                .reservationTimeId(3L)
                .numberOfPeople(2)
                .reservationMenus(menuList)
                .build();

        reservationService.registerReservation(form, 2L);
        reservationService.registerReservation(form2, 2L);
        reservationService.registerReservation(form3, 2L);
    }

    @Test
    @Order(2)
    public void t2_registerFeedback(){
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

        List<FeedbackDto.MenuFeedBackForm> mfbList2 = new ArrayList<>();

        FeedbackDto.MenuFeedBackForm mfb3 = new FeedbackDto.MenuFeedBackForm(
                3L,
                4,
                10000,
                "청경채의 익힘이 타이트 하네요."
        );
        FeedbackDto.MenuFeedBackForm mfb4 = new FeedbackDto.MenuFeedBackForm(
                4L,
                3,
                7000,
                "짜요 탈락입니다."
        );
        mfbList2.add(mfb3);
        mfbList2.add(mfb4);

        FeedbackDto.FeedbackForm form2 = new FeedbackDto.FeedbackForm(
                4,
                "맛있어요.",
                mfbList2
        );

        List<FeedbackDto.MenuFeedBackForm> mfbList3 = new ArrayList<>();

        FeedbackDto.MenuFeedBackForm mfb5 = new FeedbackDto.MenuFeedBackForm(
                5L,
                4,
                10000,
                "청경채의 익힘이 타이트 하네요."
        );
        FeedbackDto.MenuFeedBackForm mfb6 = new FeedbackDto.MenuFeedBackForm(
                6L,
                3,
                7000,
                "짜요 탈락입니다."
        );
        mfbList3.add(mfb5);
        mfbList3.add(mfb6);

        FeedbackDto.FeedbackForm form3 = new FeedbackDto.FeedbackForm(
                4,
                "맛있어요.",
                mfbList3
        );



        FeedbackDto.FeedbackDetail fb = feedbackService.registerFeedback(
                2L, 1L, form
        );
        FeedbackDto.FeedbackDetail fb2 = feedbackService.registerFeedback(
                2L, 2L, form2
        );
        FeedbackDto.FeedbackDetail fb3 = feedbackService.registerFeedback(
                2L, 3L, form3
        );

    }

    @Test
    @Order(3)
    public void t3_getFeedbackList() {
        List<FeedbackDto.FeedbackSummary> feedbackList = feedbackService.getFeedbackList(
                1L, 1L
        );
        System.out.println("==============================");
        for(FeedbackDto.FeedbackSummary feedback: feedbackList){
            System.out.println("feedback = " + feedback.getComment());
        }
    }

    @Test
    @Order(4)
    public void t4_getFeedbackDetail(){
        FeedbackDto.FeedbackDetail feedbackDetail = feedbackService.getFeedbackDetail(1L, 1L);
        System.out.println("feedbackDetail = " + feedbackDetail.getComment());

        feedbackDetail.getMenuFeedbackSummaries()
                .forEach(menuFeedbackSummary -> {
                    System.out.println("menuFeedbackSummary = " + menuFeedbackSummary.getComment());
                });
    }

    @Test
    @Order(5)
    public void t5_getFeedbackByMenu() {
        List<FeedbackDto.MenuFeedbackSummary> menuFeedbacks = feedbackService.getFeedbackByMenu(
                1L, 1L
        );

        menuFeedbacks.forEach(
                menuFeedbackSummary -> {
                    System.out.println("menuFeedbackSummary = " + menuFeedbackSummary.getComment());
                }
        );
    }

    @Test
    @Order(6)
    public void t5_getMyFeedbackList() {
        List<FeedbackDto.FeedbackSummary> myFeedbacks = feedbackService.getMyFeedbackList(2L);

        myFeedbacks.forEach(
                feedbackSummary -> {
                    System.out.println("feedbackSummary = " + feedbackSummary.getComment());
                }
        );
    }
    @Test
    @Order(6)
    public void t6_getMyFeedback() {
        FeedbackDto.FeedbackSummary myFeedback = feedbackService.getMyFeedback(2L, 1L);

        System.out.println("myFeedbacks = " + myFeedback.getComment());

        Assertions.assertThrows(BusinessException.class, () -> feedbackService.getMyFeedback(3L,1L));
    }

}
