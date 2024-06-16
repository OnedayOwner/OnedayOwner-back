package com.OnedayOwner.server.global.security;

import lombok.Getter;
import org.springframework.util.AntPathMatcher;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public class MethodUri {

    private final Method method;
    private final String pattern;

    public MethodUri(Method method, String pattern) {
        this.method = method;
        this.pattern = pattern;
    }

    public Boolean match(String method, String uri) {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        return pathMatcher.match(this.pattern, uri) &&
                (this.method.equals(Method.ALL) || this.method.isSameMethod(method));
    }

    @Getter
    enum Method {
        GET("GET"),
        POST("POST"),
        PUT("PUT"),
        ALL("ALL");

        private final String name;

        private static final Map<String, Method> stringToEnum =
                Stream.of(Method.values()).collect(Collectors.toMap(Objects::toString, e -> e));

        public Boolean isSameMethod(String methodStr) {
            return this.name.equals(methodStr);
        }

        Method(String name) {
            this.name = name;
        }
    }
}
