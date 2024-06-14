package com.OnedayOwner.server.global.security;

import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;

import java.util.Set;

public class SecurityUtils {

    public static Long extractUserId(SecurityContextHolderAwareRequestWrapper request) {
        return Long.valueOf(request.getUserPrincipal().getName());
    }

    public static String[] pathsSetToArray(Set<MethodUri> paths) {
        return paths
                .stream()
                .map(MethodUri::getPattern)
                .toList()
                .toArray(new String[]{});
    }
}