package com.OnedayOwner.server.platform.auth.repositoty;

import com.OnedayOwner.server.platform.auth.entity.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {

    Optional<VerificationCode> findById(Long id);

    Optional<VerificationCode> findTopByPhoneNumberOrderByIdDesc(String phoneNumber);

}
