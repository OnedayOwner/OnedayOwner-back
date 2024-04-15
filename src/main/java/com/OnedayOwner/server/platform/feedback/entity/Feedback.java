package com.OnedayOwner.server.platform.feedback.entity;

import com.OnedayOwner.server.platform.popup.entity.Menu;
import com.OnedayOwner.server.platform.user.entity.Customer;
import com.OnedayOwner.server.platform.user.entity.Gender;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @Builder
    public Feedback(String score, int desiredPrice ,String feedback, Boolean show, Customer customer, Menu menu) {
        this.score = score;
        this.desiredPrice=desiredPrice;
        this.feedback=feedback;
        this.show=show;
        this.customer=customer;
        this.menu=menu;
    }
}
