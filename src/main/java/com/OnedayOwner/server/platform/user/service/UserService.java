package com.OnedayOwner.server.platform.user.service;

import com.OnedayOwner.server.global.exception.BusinessException;
import com.OnedayOwner.server.global.exception.ErrorCode;
import com.OnedayOwner.server.platform.Address;
import com.OnedayOwner.server.platform.auth.dto.VerificationDto;
import com.OnedayOwner.server.platform.auth.service.AuthService;
import com.OnedayOwner.server.platform.user.dto.UserDto;
import com.OnedayOwner.server.platform.user.entity.Customer;
import com.OnedayOwner.server.platform.user.entity.Role;
import com.OnedayOwner.server.platform.user.entity.User;
import com.OnedayOwner.server.platform.user.repository.CustomerRepository;
import com.OnedayOwner.server.platform.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserDto.UserInfo join(UserDto.JoinDto joinDto, Role role){

        VerificationDto.Request verificationDto = new VerificationDto.Request(
                joinDto.getCodeId(), joinDto.getPhoneNumber(), joinDto.getVerificationCode()
        );

        if(userRepository.findByPhoneNumber(joinDto.getPhoneNumber()).isPresent()){
            throw new BusinessException(ErrorCode.USER_ALREADY_EXIST);
        }

        if(!authService.verifyCode(verificationDto).getIsVerified()){
            throw new BusinessException(ErrorCode.VERIFICATION_CODE_NOT_MATCH);
        }

        if(duplicateIdCheck(joinDto.getLoginId(), role)){
            throw new BusinessException(ErrorCode.DUPLICATE_ID);
        }

        String hashedPassword = passwordEncoder.encode(joinDto.getPassword());

        if(role == Role.OWNER) {
            User joinUser = User.builder()
                    .name(joinDto.getName())
                    .birth(joinDto.getBirth())
                    .email(joinDto.getEmail())
                    .gender(joinDto.getGender())
                    .role(Role.OWNER)
                    .loginId(joinDto.getLoginId())
                    .phoneNumber(joinDto.getPhoneNumber())
                    .password(hashedPassword)
                    .build();
            userRepository.save(joinUser);
            return new UserDto.OwnerInfo(joinUser);
        }
        else {
            User joinUser = User.builder()
                    .name(joinDto.getName())
                    .birth(joinDto.getBirth())
                    .email(joinDto.getEmail())
                    .gender(joinDto.getGender())
                    .role(Role.OWNER)
                    .loginId(joinDto.getLoginId())
                    .phoneNumber(joinDto.getPhoneNumber())
                    .password(hashedPassword)
                    .address(new Address(joinDto.getAddressForm()))
                    .build();
            userRepository.save(joinUser);
            return new UserDto.CustomerInfo(joinUser);
        }

    }

    @Transactional
    public Boolean duplicateIdCheck(String loginId, Role role){
        return userRepository.findByLoginIdAndRole(loginId, role).isPresent();//아이다 중복이면 true 반환
    }

    @Transactional
    public UserDto.UserInfo login(UserDto.LoginDto loginDto){
        if(userRepository.findByLoginId(loginDto.getLoginId()).isEmpty()){
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        if(userRepository.findByLoginId(loginDto.getLoginId()).get().getRole() == Role.OWNER){
            return new UserDto.OwnerInfo(userRepository.findByLoginId(loginDto.getLoginId()).get());
        }
        else{
            return new UserDto.UserInfo(userRepository.findByLoginId(loginDto.getLoginId()).get());
        }
    }
}
