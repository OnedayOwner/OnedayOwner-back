package com.OnedayOwner.server.platform.feedback.entity;

import com.OnedayOwner.server.platform.reservation.entity.ReservationMenu;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MenuFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_feedback_id")
    private Long id;

    private Double score;
    private int desiredPrice;
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedback_id")
    private Feedback feedback;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_menu_id")
    private ReservationMenu reservationMenu;

    public void setFeedback(Feedback feedback){
        this.feedback = feedback;
    }

    @Builder
    public MenuFeedback(Double score, int desiredPrice , String comment, ReservationMenu reservationMenu) {
        this.score = score;
        this.desiredPrice = desiredPrice;
        this.comment = comment;
        this.reservationMenu = reservationMenu;
    }
}
