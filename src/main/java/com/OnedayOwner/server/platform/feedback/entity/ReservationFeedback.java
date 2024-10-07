package com.OnedayOwner.server.platform.feedback.entity;

import com.OnedayOwner.server.platform.reservation.entity.Reservation;
import com.OnedayOwner.server.platform.reservation.entity.ReservationMenu;
import com.OnedayOwner.server.platform.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationFeedback {

    @Id
    @GeneratedValue
    @Column(name = "reservation_feedback_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private int score;
    private String comment;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    public ReservationFeedback(User user, int score, String comment, Reservation reservation) {
        this.user = user;
        this.score = score;
        this.comment = comment;
        this.reservation = reservation;
    }
}
