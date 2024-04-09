package com.OnedayOwner.server.platform.user.service;

import com.OnedayOwner.server.platform.user.dto.request.CustomerJoinRequest;
import com.OnedayOwner.server.platform.user.dto.request.CustomerLoginRequest;
import com.OnedayOwner.server.platform.user.dto.response.CustomerLoginResponse;
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
public class CustomerServiceTest {

    @Autowired
    CustomerService customerService;
    @Autowired
    CustomerRepository customerRepository;

    @Test
    public void 회원가입() {
        //given
        String email="1234@naver.com";
        CustomerJoinRequest customerJoinRequest = CustomerJoinRequest.builder()
                .name("Kim")
                .phoneNumber("010-1234-5678")
                .gender(Gender.MALE)
                .birth(LocalDate.now())
                .email(email)
                .password("1234")
                .build();

        //when
        customerService.joinCustomer(customerJoinRequest);

        //then
        Optional<Customer> findCustomer = customerRepository.findCustomerByEmail(email);
        if(findCustomer.isEmpty()){
            throw new IllegalStateException("회원가입이 진행되지 않았습니다.");
        }
        Customer customer=findCustomer.get();
        Assertions.assertEquals(customer.getName(),customerJoinRequest.getName());
        Assertions.assertEquals(customer.getPassword(),customerJoinRequest.getPassword());
        Assertions.assertEquals(customer.getPhoneNumber(),customerJoinRequest.getPhoneNumber());
    }

    @Test
    public void 로그인() {
        //given
        String email="1234@naver.com";
        String password="1234";
        CustomerJoinRequest customerJoinRequest = CustomerJoinRequest.builder()
                .name("Kim")
                .phoneNumber("010-1234-5678")
                .gender(Gender.MALE)
                .birth(LocalDate.now())
                .email(email)
                .password(password)
                .build();
        customerService.joinCustomer(customerJoinRequest);

        //when
        CustomerLoginRequest customerLoginRequest = CustomerLoginRequest.builder()
                .email(email)
                .password(password)
                .build();
        CustomerLoginResponse findCustomer = customerService.loginCustomer(customerLoginRequest);

        //then
        if (findCustomer==null){
            throw new IllegalStateException("로그인이 진행되지 않았습니다.");
        }
        Assertions.assertEquals(findCustomer.getName(),customerJoinRequest.getName());
    }
}