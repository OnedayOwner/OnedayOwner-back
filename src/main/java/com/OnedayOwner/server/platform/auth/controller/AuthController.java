package com.OnedayOwner.server.platform.auth.controller;

import com.OnedayOwner.server.global.security.JwtConfig;
import com.OnedayOwner.server.platform.auth.dto.SmsDto;
import com.OnedayOwner.server.platform.auth.dto.VerificationDto;
import com.OnedayOwner.server.platform.auth.entity.AccessToken;
import com.OnedayOwner.server.platform.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtConfig jwtConfig;

    @PostMapping("/verification-sms")
    public ResponseEntity<SmsDto.Response> sendSms(
            @RequestBody @Valid SmsDto.Request smsRequestDto
    ) throws NoSuchAlgorithmException {
        return ResponseEntity.ok()
                .body(authService.sendSms(smsRequestDto.getPhoneNumber()));
    }


    @PostMapping("token/refresh")
    private ResponseEntity<Object> refresh(
            @RequestHeader HttpHeaders headers
    ) {
        String bearerToken = headers.getFirst(jwtConfig.getHeader());
        AccessToken accessToken = authService.refreshAccessToken(bearerToken);
        String refreshedBearerToken = jwtConfig.getPrefix() + accessToken.getToken();
        return ResponseEntity.ok()
                .header(jwtConfig.getHeader(), refreshedBearerToken)
                .build();
    }

}
