package com.OnedayOwner.server.platform.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class VerificationDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Schema(name = "VerificationDto.Request")
    public static class Request{
        private Long codeId;
        private String phoneNumber;
        private String code;

        @Builder
        public Request(Long codeId, String phoneNumber, String code) {
            this.codeId = codeId;
            this.phoneNumber = phoneNumber;
            this.code = code;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Schema(name = "VerificationDto.Response")
    public static class Response{
        private Boolean isVerified;
        private Boolean isUser;

        @Builder
        public Response(Boolean isVerified, Boolean isUser) {
            this.isVerified = isVerified;
            this.isUser = isUser;
        }
    }

}
