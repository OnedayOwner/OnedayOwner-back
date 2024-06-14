package com.OnedayOwner.server.global.security;

import com.auth0.jwt.interfaces.RSAKeyProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtConfig jwtConfig;
    private final RSAKeyProvider keyProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(
                        httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(
                                corsConfigurationSource())
                )
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(
                        httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(
                        new JwtFilter(jwtConfig, keyProvider),
                        UsernamePasswordAuthenticationFilter.class
                )
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers(SecurityUtils.pathsSetToArray(SecurityConstant.EXCLUDED_URI)).permitAll()
                                //.requestMatchers(HttpMethod.POST, "/api/news").hasAuthority(Role.ADMIN.toString())
                                //.requestMatchers(HttpMethod.POST, "/api/user/attendance/reset").hasAuthority(Role.ADMIN.toString())
//                    .requestMatchers("/api/users/token-for-test").permitAll()
                                .anyRequest().authenticated()
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        // TODO: need to change cors setting to allow only required origin - 2024.03.19 sehyeona
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addExposedHeader("Authorization");
        corsConfiguration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}