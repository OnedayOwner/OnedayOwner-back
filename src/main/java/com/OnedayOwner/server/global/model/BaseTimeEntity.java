package com.newsboy.server.global.model;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDateTime;

    @LastModifiedBy
    private LocalDateTime lastModifiedDateTime;

    @PrePersist
    public void onCreate() {
        this.createdDateTime = LocalDateTime.now();
        this.lastModifiedDateTime = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.lastModifiedDateTime = LocalDateTime.now();
    }
}
