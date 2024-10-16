package com.OnedayOwner.server.platfrom.feedback;

import com.OnedayOwner.server.global.exception.BusinessException;
import com.OnedayOwner.server.global.exception.ErrorCode;
import com.OnedayOwner.server.platform.feedback.dto.FeedbackDto;
import com.OnedayOwner.server.platform.feedback.service.FeedbackService;
import com.OnedayOwner.server.platform.reservation.dto.ReservationDto;
import com.OnedayOwner.server.platform.reservation.service.ReservationService;
import com.OnedayOwner.server.platform.user.entity.User;
import com.OnedayOwner.server.platform.user.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
public class FeedbackTest {

    @Autowired
    FeedbackService feedbackService;
    @Autowired
    ReservationService reservationService;
    @Autowired
    UserRepository userRepository;

//    @BeforeEach
//    void setup(){
//        List<ReservationDto.ReservationMenuForm> menuList = new ArrayList<>();
//        ReservationDto.ReservationMenuForm menuForm = new ReservationDto.ReservationMenuForm(1,1L);
//        ReservationDto.ReservationMenuForm menuForm2 = new ReservationDto.ReservationMenuForm(2,2L);
//
//        User user = userRepository.findByPhoneNumber("01012345678").orElseThrow(
//                ()->new BusinessException(ErrorCode.USER_NOT_FOUND)
//        );
//
//        menuList.add(menuForm);
//        menuList.add(menuForm2);
//
//        ReservationDto.ReservationForm rform = ReservationDto.ReservationForm.builder()
//                .popupId(1L)
//                .reservationTimeId(1L)
//                .numberOfPeople(2)
//                .reservationMenus(menuList)
//                .build();
//        ReservationDto.ReservationForm rform2 = ReservationDto.ReservationForm.builder()
//                .popupId(1L)
//                .reservationTimeId(2L)
//                .numberOfPeople(2)
//                .reservationMenus(menuList)
//                .build();
//        ReservationDto.ReservationForm rform3 = ReservationDto.ReservationForm.builder()
//                .popupId(1L)
//                .reservationTimeId(3L)
//                .numberOfPeople(2)
//                .reservationMenus(menuList)
//                .build();
//
//        ReservationDto.ReservationDetail reservationDetail1 = reservationService.registerReservation(rform, user.getId());
//        ReservationDto.ReservationDetail reservationDetail2 = reservationService.registerReservation(rform2, user.getId());
//        ReservationDto.ReservationDetail reservationDetail3 = reservationService.registerReservation(rform3, user.getId());
//
//        List<FeedbackDto.MenuFeedBackForm> mfbList1 = new ArrayList<>();
//
//        reservationDetail1.getReservationMenuDetails().forEach(rmd -> {
//            FeedbackDto.MenuFeedBackForm mfb = new FeedbackDto.MenuFeedBackForm(
//                    rmd.getId(),
//                    4,
//                    10000,
//                    "맛있어요."
//            );
//            mfbList1.add(mfb);
//        });
//
//        FeedbackDto.FeedbackForm form = new FeedbackDto.FeedbackForm(
//                4,
//                "맛있어요.",
//                mfbList1
//        );
//
//        List<FeedbackDto.MenuFeedBackForm> mfbList2 = new ArrayList<>();
//
//        reservationDetail2.getReservationMenuDetails().forEach(rmd -> {
//            FeedbackDto.MenuFeedBackForm mfb = new FeedbackDto.MenuFeedBackForm(
//                    rmd.getId(),
//                    4,
//                    10000,
//                    "맛있어요."
//            );
//            mfbList2.add(mfb);
//        });
//        FeedbackDto.FeedbackForm form2 = new FeedbackDto.FeedbackForm(
//                4,
//                "굿.",
//                mfbList2
//        );
//
//        FeedbackDto.FeedbackDetail fb = feedbackService.registerFeedback(
//                user.getId(), reservationDetail1.getId(), form
//        );
//        FeedbackDto.FeedbackDetail fb2 = feedbackService.registerFeedback(
//                user.getId(), reservationDetail2.getId(), form2
//        );
//
//    }

    @Test
    void t3_getFeedbackList() {
        List<FeedbackDto.FeedbackSummary> feedbackList = feedbackService.getFeedbackList(
                1L, 1L
        );
        System.out.println("==============================");
        for(FeedbackDto.FeedbackSummary feedback: feedbackList){
            System.out.println("feedback = " + feedback.getComment());
        }
    }

    @Test
    void t4_getFeedbackDetail(){
        FeedbackDto.FeedbackDetail feedbackDetail = feedbackService.getFeedbackDetail(1L, 1L);
        System.out.println("feedbackDetail = " + feedbackDetail.getComment());

        feedbackDetail.getMenuFeedbackSummaries()
                .forEach(menuFeedbackSummary -> {
                    System.out.println("menuFeedbackSummary = " + menuFeedbackSummary.getComment());
                });
    }

    @Test
    void t5_getFeedbackByMenu() {
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
    void t5_getMyFeedbackList() {
        List<FeedbackDto.FeedbackSummary> myFeedbacks = feedbackService.getMyFeedbackList(2L);

        myFeedbacks.forEach(
                feedbackSummary -> {
                    System.out.println("feedbackSummary = " + feedbackSummary.getComment());
                }
        );
    }
    @Test
    void t6_getMyFeedback() {
        FeedbackDto.FeedbackSummary myFeedback = feedbackService.getMyFeedback(2L, 1L);

        System.out.println("myFeedbacks = " + myFeedback.getComment());

        Assertions.assertThrows(BusinessException.class, () -> feedbackService.getMyFeedback(3L,1L));
    }


}
