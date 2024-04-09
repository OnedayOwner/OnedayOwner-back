package com.OnedayOwner.server.platform.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CustomerLoginResponse {
    private final Long id;
    private final String name;
    private final int point;

    @Builder
    public CustomerLoginResponse(Long id, String name, int point) {
        this.id=id;
        this.name=name;
        this.point=point;
    }
}
