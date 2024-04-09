package com.OnedayOwner.server.platform.user.service;

import com.OnedayOwner.server.platform.user.dto.request.CustomerJoinRequest;
import com.OnedayOwner.server.platform.user.dto.request.CustomerLoginRequest;
import com.OnedayOwner.server.platform.user.dto.response.CustomerLoginResponse;
import com.OnedayOwner.server.platform.user.entity.Customer;
import com.OnedayOwner.server.platform.user.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Transactional
    public void joinCustomer(CustomerJoinRequest customerJoinRequest) {
        Customer customer=Customer.builder()
                .name(customerJoinRequest.getName())
                .phoneNumber(customerJoinRequest.getPhoneNumber())
                .gender(customerJoinRequest.getGender())
                .birth(customerJoinRequest.getBirth())
                .password(customerJoinRequest.getPassword())
                .email(customerJoinRequest.getEmail())
                .build();
        customerRepository.save(customer);
    }

    public CustomerLoginResponse loginCustomer(CustomerLoginRequest customerLoginRequest) {
        Optional<Customer> findCustomer = customerRepository.findCustomerByEmail(customerLoginRequest.getEmail());
        if(findCustomer.isEmpty()){
            return null;
        }
        Customer customer=findCustomer.get();
        if(!customer.getPassword().equals(customerLoginRequest.getPassword())){
            return null;
        }
        CustomerLoginResponse customerLoginResponse=CustomerLoginResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .point(customer.getPoint())
                .build();
        return customerLoginResponse;
    }
}
