package com.OnedayOwner.server.platform.user.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CustomerLoginRequest {
    private final String email;
    private final String password;

    @Builder
    public CustomerLoginRequest(String email, String password) {
        this.email=email;
        this.password=password;
    }
}
