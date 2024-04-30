package com.OnedayOwner.server.platform.user.service;

import com.OnedayOwner.server.platform.user.dto.UserDTO;
import com.OnedayOwner.server.platform.user.entity.Customer;
import com.OnedayOwner.server.platform.user.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final CustomerRepository customerRepository;

    @Transactional
    public void joinUser(UserDTO.UserJoinRequest userJoinRequest) {
        Customer customer=Customer.builder()
                .name(userJoinRequest.getName())
                .phoneNumber(userJoinRequest.getPhoneNumber())
                .gender(userJoinRequest.getGender())
                .birth(userJoinRequest.getBirth())
                .password(userJoinRequest.getPassword())
                .email(userJoinRequest.getEmail())
                .build();
        customerRepository.save(customer);  //유저별 저장 로직 구현 필요
    }



    public UserDTO.UserLoginResponse loginUser(UserDTO.UserLoginRequest userLoginRequest) {
        /*
        유저별 로그인 로직 구현 필요
         */
        Optional<Customer> findCustomer = customerRepository.findCustomerByEmail(userLoginRequest.getEmail());
        if(findCustomer.isEmpty()){
            return null;
        }
        Customer customer=findCustomer.get();
        if(!customer.getPassword().equals(userLoginRequest.getPassword())){
            return null;
        }
        UserDTO.UserLoginResponse userLoginResponse=UserDTO.UserLoginResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .point(customer.getPoint())
                .build();
        return userLoginResponse;
    }
}
