package com.OnedayOwner.server.platform.feedback.entity;

import com.OnedayOwner.server.platform.reservation.entity.Reservation;
import com.OnedayOwner.server.platform.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feedback {

    @Id
    @GeneratedValue
    @Column(name = "feedback_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private int score;
    private String comment;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @OneToMany(targetEntity = MenuFeedback.class, cascade = CascadeType.ALL, mappedBy = "feedback")
    private List<MenuFeedback> menuFeedbacks;

    public MenuFeedback addMenuFeedback(MenuFeedback menuFeedback){
        this.menuFeedbacks.add(menuFeedback);
        menuFeedback.setFeedback(this);
        return menuFeedback;
    }

    @Builder
    public Feedback(User user, int score, String comment, Reservation reservation) {
        this.user = user;
        this.score = score;
        this.comment = comment;
        this.reservation = reservation;
    }
}
