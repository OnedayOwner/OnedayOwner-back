package com.OnedayOwner.server.global.security;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.OnedayOwner.server.global.security.MethodUri.*;

public class SecurityConstant {

    public static final Set<MethodUri> EXCLUDED_URI = new HashSet<>(Arrays.asList(
            new MethodUri(Method.ALL, "/error"),
            new MethodUri(Method.ALL, "/swagger-ui/**"),
            new MethodUri(Method.ALL, "/swagger-ui.html"),
            new MethodUri(Method.ALL, "/v3/api-docs/**"),
            new MethodUri(Method.ALL, "/static/**"),

            new MethodUri(Method.ALL, "/api/health"),
            new MethodUri(Method.ALL, "/api/users/join"),
            new MethodUri(Method.ALL, "/api/users/login"),
            new MethodUri(Method.ALL, "/api/auth/**")
    ));
}
