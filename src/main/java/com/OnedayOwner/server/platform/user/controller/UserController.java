package com.OnedayOwner.server.platform.user.controller;

import com.OnedayOwner.server.global.security.JwtConfig;
import com.OnedayOwner.server.global.security.SecurityUtils;
import com.OnedayOwner.server.platform.auth.entity.AccessToken;
import com.OnedayOwner.server.platform.auth.service.AuthService;
import com.OnedayOwner.server.platform.user.dto.UserDto;
import com.OnedayOwner.server.platform.user.entity.Role;
import com.OnedayOwner.server.platform.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final AuthService authService;
    private final JwtConfig jwtConfig;

    @PostMapping("/join")
    public ResponseEntity<UserDto.UserInfo> join(
            @RequestBody @Valid UserDto.JoinDto joinDto,
            @RequestParam Role role
    ) {
        UserDto.UserInfo userDto = userService.join(joinDto, role);
        return ResponseEntity.ok()
                .body(userDto);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto.UserInfo> login(
            @RequestBody @Valid UserDto.LoginDto loginDto,
            @RequestParam Role role
    ){
        UserDto.UserInfo userDto = userService.login(loginDto, role);
        AccessToken accessToken = authService
                .createAccessTokenByLoginIdAndRole(loginDto.getLoginId(), role);
        String bearerToken = jwtConfig.getPrefix() + accessToken.getToken();
        return ResponseEntity.ok()
                .header(jwtConfig.getHeader(), bearerToken)
                .body(userDto);
    }

    @Operation(summary = "회원 정보 조회")
    @GetMapping
    public ResponseEntity<UserDto.UserInfo> getUser(
            SecurityContextHolderAwareRequestWrapper request
    ) {
        Long userId = SecurityUtils.extractUserId(request);
        return ResponseEntity.ok()
                .body(userService.getUser(userId));
    }
}
