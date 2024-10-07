package com.OnedayOwner.server.platform.feedback.dto;


import com.OnedayOwner.server.platform.feedback.entity.MenuFeedback;
import com.OnedayOwner.server.platform.feedback.entity.Feedback;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PROTECTED;

public class FeedbackDto {

    @Getter
    @NoArgsConstructor(access = PROTECTED)
    public static class MenuFeedBackForm{
        private Long reservationMenuId;
        private int score;
        private int desiredPrice;
        private String comment;
    }

    @Getter
    @NoArgsConstructor(access = PROTECTED)
    public static class FeedbackForm {
        private int score;
        private String comment;

        private List<MenuFeedBackForm> menuFeedBackForms = new ArrayList<>();
    }

    @Getter
    @NoArgsConstructor(access = PROTECTED)
    public static class MenuFeedbackSummary{
        private Long menuFeedbackId;
        private Long reservationMenuId;
        private int score;
        private int desiredPrice;
        private String comment;


        public MenuFeedbackSummary(MenuFeedback menuFeedback) {
            this.menuFeedbackId = menuFeedback.getId();
            this.reservationMenuId = menuFeedback.getReservationMenu().getId();
            this.score = menuFeedback.getScore();
            this.desiredPrice = menuFeedback.getDesiredPrice();
            this.comment = menuFeedback.getComment();
        }
    }

    @Getter
    @NoArgsConstructor(access = PROTECTED)
    public static class FeedbackSummary{
        private Long feedbackId;
        private int score;
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

}
