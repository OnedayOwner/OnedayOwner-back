package com.OnedayOwner.server.platform.auth.service;


import com.OnedayOwner.server.global.exception.BusinessException;
import com.OnedayOwner.server.global.exception.ErrorCode;
import com.OnedayOwner.server.global.security.JwtConfig;
import com.OnedayOwner.server.global.security.JwtGenerator;
import com.OnedayOwner.server.global.sms.CoolSms;
import com.OnedayOwner.server.platform.auth.dto.SmsDto;
import com.OnedayOwner.server.platform.auth.dto.VerificationDto;
import com.OnedayOwner.server.platform.auth.entity.AccessToken;
import com.OnedayOwner.server.platform.auth.entity.VerificationCode;
import com.OnedayOwner.server.platform.auth.repositoty.AccessTokenRepository;
import com.OnedayOwner.server.platform.auth.repositoty.VerificationCodeRepository;
import com.OnedayOwner.server.platform.user.entity.Role;
import com.OnedayOwner.server.platform.user.entity.User;
import com.OnedayOwner.server.platform.user.repository.UserRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.RSAKeyProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final JwtGenerator jwtGenerator;
    private final JwtConfig jwtConfig;
    private final AccessTokenRepository accessTokenRepository;
    private final RSAKeyProvider rsaKeyProvider;
    private final UserRepository userRepository;
    private final VerificationCodeRepository verificationCodeRepository;
    private final CoolSms coolSms;

    public Algorithm getAlgorithm() {
        return Algorithm.RSA256(this.rsaKeyProvider);
    }

    @Transactional
    public Boolean passwordAuthenticate(String loginId, String rawPassword, Role role){
        System.out.println(loginId);
        System.out.println(role);
        System.out.println(rawPassword);
        User user = userRepository.findByLoginIdAndRole(loginId, role).orElseThrow(
                () -> new BusinessException(ErrorCode.USER_NOT_FOUND)
        );
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    @Transactional
    public VerificationDto.Response verifyCode(VerificationDto.Request verificationDto) {

        // find the latest verification code by phoneNumber
        VerificationCode verificationCode = verificationCodeRepository
                .findTopByPhoneNumberOrderByIdDesc(verificationDto.getPhoneNumber())
                .orElseThrow(() -> new BusinessException(ErrorCode.VERIFICATION_CODE_NOT_FOUND));

        if (!verificationCode.getId().equals(verificationDto.getCodeId())) {
            throw new BusinessException(ErrorCode.EXPIRED_VERIFICATION_CODE);
        }

        // 인증번호가 틀린 경우
        if (!verificationCode.getCode().equals(verificationDto.getCode())) {
            return new VerificationDto.Response(false, false);
        }

        LocalDateTime createdDateTime = verificationCode.getCreatedDateTime();
        LocalDateTime now = LocalDateTime.now();
        Long minutesElapsed = ChronoUnit.MINUTES.between(createdDateTime, now);

        // 인증 코드 유효 시간 (현재 5분)이 지남
        if (minutesElapsed > 5) {
            throw new BusinessException(ErrorCode.EXPIRED_VERIFICATION_CODE);
        }

        if (userRepository.findByPhoneNumber(verificationDto.getPhoneNumber()).isEmpty()) {
            return new VerificationDto.Response(true, false);
        } else {
            return new VerificationDto.Response(true, true);

        }
    }

    @Transactional
    public SmsDto.Response sendSms(String phoneNumber) throws NoSuchAlgorithmException{
        if (!checkPhoneNumber(phoneNumber)) {
            throw new BusinessException(ErrorCode.INVALID_PHONE_NUMBER);
        }

        if(verificationCodeRepository.findTopByPhoneNumberOrderByIdDesc(phoneNumber).isPresent()){
            if(ChronoUnit.SECONDS.between(verificationCodeRepository.findTopByPhoneNumberOrderByIdDesc(phoneNumber)
                    .get().getCreatedDateTime(), LocalDateTime.now()) < 60){
                throw new BusinessException(ErrorCode.FREQUENT_SMS_REQUEST);
            }
        }
        VerificationCode verificationCode = new VerificationCode(phoneNumber);
        verificationCodeRepository.save(verificationCode);

        coolSms.sendSms(phoneNumber, makeContent(verificationCode.getCode()));

        return new SmsDto.Response(verificationCode.getId());
    }

    public String makeContent(String code){
        return "오늘만 사장 회원가입\n인증번호는 [" + code + "] 입니다.";
    }

    public Boolean checkPhoneNumber(String phoneNumber) {
        if (phoneNumber.length() != 11) {
            System.out.println("phoneNumber.length() = " + phoneNumber.length());
            return false;
        }
        return true;
    }

    @Transactional
    public AccessToken createAccessTokenByLoginIdAndRole(String loginId, Role role) {
        User user = userRepository.findByLoginIdAndRole(loginId, role)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        return createAccessToken(user);
    }


    @Transactional
    public AccessToken createAccessToken(User user) {
        OffsetDateTime now = OffsetDateTime.now();

        String generatedToken = jwtGenerator.generate(
                user.getId(),
                List.of(user.getRole().name()),
                now
        );

        AccessToken accessToken = AccessToken.builder()
                .user(user)
                .token(generatedToken)
                .createdDateTime(now)
                .renewableLimitDateTime(
                        now.plus(Duration.ofSeconds(jwtConfig.getRenewablePeriod()))
                )
                .build();
        return accessTokenRepository.save(accessToken);
    }

    @Transactional
    public AccessToken refreshAccessToken(String bearerToken) {
        Objects.requireNonNull(bearerToken);
        String token = bearerToken.replace(jwtConfig.getPrefix(), "");

        Algorithm algorithm = getAlgorithm();

        DecodedJWT decodedToken = JWT.decode(token);
        algorithm.verify(decodedToken);

        String userId = decodedToken.getSubject();

        // if token has invalid value then valueOf method can throw NumberFormatException
        User user = userRepository
                .findById(Long.valueOf(userId))
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        AccessToken accessToken = accessTokenRepository
                .findFirstByUserAndToken(user, token)
                .orElseThrow(() -> new BusinessException(ErrorCode.ACCESS_TOKEN_NOT_FOUND));

        OffsetDateTime now = OffsetDateTime.now();

        if (accessToken.getRenewableLimitDateTime().compareTo(now) > 0) {
            return createAccessToken(user);
        } else {
            throw new BusinessException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        }
    }


}
