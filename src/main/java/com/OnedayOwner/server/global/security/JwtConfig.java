package com.OnedayOwner.server.global.security;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class JwtConfig {
    @Value(value = "${security.jwt.header:Authorization}")
    private String header;

    @Value(value = "${security.jwt.prefix:Bearer }")
    private String prefix;

    @Value(value = "${security.jwt.expiration:#{30*60}}")
    private int expiration;

    @Value(value = "${security.jwt.expiration:#{30*24*60*60}}")
    private int renewablePeriod;

    @Value(value = "{security.jwt.secret:JwtSecretKey:3181}")
    private String secret;
}