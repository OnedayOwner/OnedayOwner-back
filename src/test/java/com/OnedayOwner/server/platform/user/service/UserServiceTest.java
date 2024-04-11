package com.OnedayOwner.server.platform.user.service;

import com.OnedayOwner.server.platform.user.dto.UserDTO;
import com.OnedayOwner.server.platform.user.entity.Customer;
import com.OnedayOwner.server.platform.user.entity.Gender;
import com.OnedayOwner.server.platform.user.repository.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired
    UserService userService;
    @Autowired
    CustomerRepository customerRepository;

    @Test
    public void 회원가입() {
        //given
        String email="1234@naver.com";
        UserDTO.UserJoinRequest userJoinRequest = UserDTO.UserJoinRequest.builder()
                .name("Kim")
                .phoneNumber("010-1234-5678")
                .gender(Gender.MALE)
                .birth(LocalDate.now())
                .email(email)
                .password("1234")
                .build();

        //when
        userService.joinUser(userJoinRequest);

        //then
        Optional<Customer> findCustomer = customerRepository.findCustomerByEmail(email);
        if(findCustomer.isEmpty()){
            throw new IllegalStateException("회원가입이 진행되지 않았습니다.");
        }
        Customer customer=findCustomer.get();
        Assertions.assertEquals(customer.getName(),userJoinRequest.getName());
        Assertions.assertEquals(customer.getPassword(),userJoinRequest.getPassword());
        Assertions.assertEquals(customer.getPhoneNumber(),userJoinRequest.getPhoneNumber());
    }

    @Test
    public void 로그인() {
        //given
        String email="1234@naver.com";
        String password="1234";
        UserDTO.UserJoinRequest userJoinRequest = UserDTO.UserJoinRequest.builder()
                .name("Kim")
                .phoneNumber("010-1234-5678")
                .gender(Gender.MALE)
                .birth(LocalDate.now())
                .email(email)
                .password(password)
                .build();
        userService.joinUser(userJoinRequest);

        //when
        UserDTO.UserLoginRequest userLoginRequest = UserDTO.UserLoginRequest.builder()
                .email(email)
                .password(password)
                .build();
        UserDTO.UserLoginResponse findUser = userService.loginUser(userLoginRequest);

        //then
        if (findUser==null){
            throw new IllegalStateException("로그인이 진행되지 않았습니다.");
        }
        Assertions.assertEquals(findUser.getName(),userJoinRequest.getName());
    }
}