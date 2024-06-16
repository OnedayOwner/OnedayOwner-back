package com.OnedayOwner.server.global.security;


import com.OnedayOwner.server.global.exception.ErrorCode;
import com.OnedayOwner.server.global.exception.ErrorResponse;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.RSAKeyProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtConfig jwtConfig;
    private final RSAKeyProvider keyProvider;

    public JwtFilter(JwtConfig jwtConfig, RSAKeyProvider keyProvider) {
        this.jwtConfig = jwtConfig;
        this.keyProvider = keyProvider;
    }

    public Algorithm getAlgorithm() {
        return Algorithm.RSA256(this.keyProvider);
    }

    public JWTVerifier getVerifier() {
        return JWT.require(getAlgorithm()).build();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String method = request.getMethod();
        String requestURI = request.getRequestURI();
        return SecurityConstant.EXCLUDED_URI.stream()
                .anyMatch(p -> p.match(method, requestURI));
    }

    private void addErrorToResponse(
            HttpServletResponse response,
            ErrorCode errorCode
    ) throws IOException {
        response.setStatus(errorCode.getStatus());
        final ErrorResponse errorResponse = ErrorResponse.of(errorCode);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getWriter(), errorResponse);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader(jwtConfig.getHeader());

        if (authHeader == null || !authHeader.startsWith(jwtConfig.getPrefix())) {
            log.info("no header");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.replace(jwtConfig.getPrefix(), "");
        try {
            DecodedJWT verifiedToken = this.getVerifier().verify(token);
            String accountId = verifiedToken.getSubject();

            logger.info(accountId);
            if (accountId != null) {
                List<GrantedAuthority> authorities = verifiedToken
                        .getClaim("authorities")
                        .asList(String.class)
                        .stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                logger.info(authorities.toString());

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        accountId, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
            filterChain.doFilter(request, response);
        } catch (TokenExpiredException e) {
            SecurityContextHolder.clearContext();
            this.addErrorToResponse(response, ErrorCode.ACCESS_TOKEN_EXPIRED);
            logger.info("token expired");
        } catch (SignatureVerificationException e) {
            this.addErrorToResponse(response, ErrorCode.INVALID_TOKEN_SIGNATURE);
            logger.info("invalid signature");
        } catch (InvalidClaimException e) {
            this.addErrorToResponse(response, ErrorCode.INVALID_CLAIM);
            logger.info("invalid claim");
        }
    }
}
