package com.OnedayOwner.server.platform.auth.entity;

import com.OnedayOwner.server.global.model.BaseTimeEntity;
import com.OnedayOwner.server.platform.user.entity.Owner;
import com.OnedayOwner.server.platform.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccessToken{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Long id;

    @Column(length = 1000)
    private String token;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User user;

    private OffsetDateTime createdDateTime;

    private OffsetDateTime renewableLimitDateTime;

    @Builder
    public AccessToken(String token, User user, OffsetDateTime createdDateTime, OffsetDateTime expiredDateTime, OffsetDateTime renewableLimitDateTime) {
        this.token = token;
        this.user = user;
        this.createdDateTime = createdDateTime;
        this.renewableLimitDateTime = renewableLimitDateTime;
    }
}
