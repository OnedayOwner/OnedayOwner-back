package com.OnedayOwner.server.platform.feedback.dto;


import com.OnedayOwner.server.platform.feedback.entity.MenuFeedback;
import com.OnedayOwner.server.platform.feedback.entity.Feedback;
import com.OnedayOwner.server.platform.popup.entity.PopupRestaurant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PROTECTED;

public class FeedbackDto {

    @Getter
    @NoArgsConstructor(access = PROTECTED)
    @AllArgsConstructor
    public static class MenuFeedBackForm{
        private Long reservationMenuId;
        private Double score;
        private int desiredPrice;
        private String comment;
    }

    @Getter
    @NoArgsConstructor(access = PROTECTED)
    @AllArgsConstructor
    public static class FeedbackForm {
        private Double score;
        private String comment;

        private List<MenuFeedBackForm> menuFeedBackForms = new ArrayList<>();
    }

    @Getter
    @NoArgsConstructor(access = PROTECTED)
    public static class MenuFeedbackSummary{
        private Long menuFeedbackId;
        private Long reservationMenuId;
        private Double score;
        private int desiredPrice;
        private String comment;
        private String name;
        private String imageUrl;
        private int price;


        public MenuFeedbackSummary(MenuFeedback menuFeedback) {
            this.menuFeedbackId = menuFeedback.getId();
            this.reservationMenuId = menuFeedback.getReservationMenu().getId();
            this.score = menuFeedback.getScore();
            this.desiredPrice = menuFeedback.getDesiredPrice();
            this.comment = menuFeedback.getComment();
            this.name = menuFeedback.getReservationMenu().getMenu().getName();
            this.imageUrl = menuFeedback.getReservationMenu().getMenu().getImageUrl();
            this.price = menuFeedback.getReservationMenu().getMenu().getPrice();
        }
    }

    @Getter
    @NoArgsConstructor(access = PROTECTED)
    public static class FeedbackSummary{
        private Long feedbackId;
        private Double score;
        private String comment;

        public FeedbackSummary(Feedback feedback) {
            this.feedbackId = feedback.getId();
            this.score = feedback.getScore();
            this.comment = feedback.getComment();
        }
    }

    @Getter
    @NoArgsConstructor(access = PROTECTED)
    public static class FeedbackDetail extends FeedbackSummary{

        private List<MenuFeedbackSummary> menuFeedbackSummaries;

        @Builder
        public FeedbackDetail(Feedback feedback){
            super(feedback);
            this.menuFeedbackSummaries = feedback
                    .getMenuFeedbacks()
                    .stream()
                    .map(MenuFeedbackSummary::new)
                    .collect(Collectors.toList());
        }
    }

    @Getter
    @NoArgsConstructor(access = PROTECTED)
    public static class FeedbackSummaryForCustomer {
        private Long feedbackId;
        private Double score;
        private String comment;
        private String popupName;
        private LocalDateTime visitedTime;
        private int numberOfPeople;

        public FeedbackSummaryForCustomer(Feedback feedback,
                                          String popupName,
                                          LocalDateTime visitedTime,
                                          int numberOfPeople) {
            this.feedbackId = feedback.getId();
            this.score = feedback.getScore();
            this.comment = feedback.getComment();
            this.popupName = popupName;
            this.visitedTime = visitedTime;
            this.numberOfPeople = numberOfPeople;
        }
    }

    @Getter
    @NoArgsConstructor(access = PROTECTED)
    public static class FeedbackDetailForCustomer extends FeedbackSummaryForCustomer{

        private List<MenuFeedbackSummary> menuFeedbackSummaries;

        @Builder
        public FeedbackDetailForCustomer(Feedback feedback,
                                         String popupName,
                                         LocalDateTime visitedTime,
                                         int numberOfPeople){
            super(feedback, popupName, visitedTime, numberOfPeople);
            this.menuFeedbackSummaries = feedback
                    .getMenuFeedbacks()
                    .stream()
                    .map(MenuFeedbackSummary::new)
                    .collect(Collectors.toList());
        }
    }

}
