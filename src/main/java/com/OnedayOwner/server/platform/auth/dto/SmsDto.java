package com.OnedayOwner.server.platform.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class SmsDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Schema(name = "SmsDto.Request")
    public static class Request{
        private String phoneNumber;

        public Request(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Schema(name = "SmsDto.Response")
    public static class Response{
        private Long codeId;

        public Response(Long codeId) {
            this.codeId = codeId;
        }
    }

}
