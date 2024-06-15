package com.OnedayOwner.server;

import com.OnedayOwner.server.platform.popup.service.PopupService;
import com.OnedayOwner.server.platform.user.entity.Customer;
import com.OnedayOwner.server.platform.user.entity.Owner;
import com.OnedayOwner.server.platform.user.repository.CustomerRepository;
import com.OnedayOwner.server.platform.user.repository.OwnerRepository;
import com.OnedayOwner.server.platform.user.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Transactional
public class TestSet {

    private final UserService userService;
    private final OwnerRepository ownerRepository;
    private final PopupService popupService;
    private final CustomerRepository customerRepository;

    @Transactional
    @PostConstruct
    public void setUp() {
        Owner user1 = Owner.builder()
                .phoneNumber("01094219784")
                .birth(LocalDate.of(2000, 01, 15))
                .build();

        ownerRepository.save(user1);

        Customer customer1 = Customer.builder()
                .phoneNumber("01094219784")
                .birth(LocalDate.of(2000, 01, 15))
                .build();

        customerRepository.save(customer1);

//        PopupDto.BusinessTimeForm timeForm = PopupDto.BusinessTimeForm.builder()
//                .openTime(LocalTime.of(11, 0))
//                .closeTime(LocalTime.of(13, 0))
//                .reservationTimeUnit(30)
//                .maxPeoplePerTime(10)
//                .build();
//
//
//        PopupDto.PopupRestaurantForm restaurantForm = PopupDto.PopupRestaurantForm.builder()
//                .name("test")
//                .businessTimes(List.of(timeForm))
//                .startDateTime(LocalDateTime.of(2024, 1, 1, 0, 0))
//                .endDateTime(LocalDateTime.of(2024, 1, 15, 23, 0))
//                .description("test")
//                .build();
//
//        PopupDto.AddressForm addressForm = PopupDto.AddressForm.builder()
//                .city("city1")
//                .street("street1")
//                .zipcode("zipcode1")
//                .detail("detail1")
//                .build();
//
//        popupService.registerPopup(restaurantForm, user1.getId());
    }
}
