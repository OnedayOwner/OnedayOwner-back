package com.OnedayOwner.server.platform.auth.entity;

import com.OnedayOwner.server.global.model.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VerificationCode extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "verification_code_id")
    private Long id;

    private String phoneNumber;
    private String code;

    public VerificationCode(String phoneNumber) throws NoSuchAlgorithmException {
        this.phoneNumber = phoneNumber;
        String code = createCode();
        this.code = code;
    }

    public VerificationCode(String phoneNumber, String code) {
        this.phoneNumber = phoneNumber;
        this.code = code;
    }

    private String createCode() throws NoSuchAlgorithmException{
        int length = 6;
        Random random = SecureRandom.getInstanceStrong();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(random.nextInt(10));
        }
        return builder.toString();
    }

}
