package com.OnedayOwner.server.platform.feedback.entity;

import com.OnedayOwner.server.platform.popup.entity.Menu;
import com.OnedayOwner.server.platform.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_id")
    private Long id;

    private String score;
    private int desiredPrice;
    private String feedback;
    private Boolean show;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @Builder
    public Feedback(String score, int desiredPrice , String feedback, Boolean show, User user, Menu menu) {
        this.score = score;
        this.desiredPrice=desiredPrice;
        this.feedback=feedback;
        this.show=show;
        this.user = user;
        this.menu=menu;
    }
}
