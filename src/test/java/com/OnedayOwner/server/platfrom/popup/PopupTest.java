package com.OnedayOwner.server.platfrom.popup;

import com.OnedayOwner.server.platform.popup.dto.PopupDto;
import com.OnedayOwner.server.platform.popup.entity.PopupRestaurant;
import com.OnedayOwner.server.platform.popup.service.PopupService;
import com.OnedayOwner.server.platform.user.entity.Owner;
import com.OnedayOwner.server.platform.user.repository.OwnerRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@SpringBootTest
public class PopupTest {

    @Autowired
    PopupService popupService;

    @Autowired
    OwnerRepository ownerRepository;


    @Test
    @Transactional
    public void test() {
        Owner user1 = Owner.builder()
                .phoneNumber("01094219784")
                .birth(LocalDate.of(2000, 01, 15))
                .build();

        ownerRepository.save(user1);

        PopupDto.BusinessTimeForm timeForm = PopupDto.BusinessTimeForm.builder()
                .openTime(LocalTime.of(11, 0))
                .closeTime(LocalTime.of(13, 0))
                .reservationTimeUnit(30)
                .maxPeoplePerTime(10)
                .build();

        PopupDto.MenuForm menuForm1 = PopupDto.MenuForm.builder()
                .name("menu1")
                .price(10000)
                .description("menu1 description")
                .build();
        PopupDto.MenuForm menuForm2 = PopupDto.MenuForm.builder()
                .name("menu2")
                .price(20000)
                .description("menu2 description")
                .build();

        PopupDto.AddressForm addressForm = PopupDto.AddressForm.builder()
                .city("city1")
                .street("street1")
                .zipcode("zipcode1")
                .detail("detail1")
                .build();

        PopupDto.PopupRestaurantForm restaurantForm = PopupDto.PopupRestaurantForm.builder()
                .name("popup1")
                .businessTimes(List.of(timeForm))
                .menuForms(List.of(menuForm1, menuForm2))
                .address(addressForm)
                .startDateTime(LocalDateTime.of(2024, 1, 1, 0, 0))
                .endDateTime(LocalDateTime.of(2024, 1, 15, 23, 0))
                .description("popup1 description")
                .build();


        PopupDto.PopupInBusinessDetail registerPopup = popupService.registerPopup(restaurantForm, user1.getId());

        PopupRestaurant findPopup = popupService.getPopupInBusinessByOwner(user1.getId());


        Assertions.assertThat(registerPopup.getId()).isEqualTo(findPopup.getId());


        registerPopup.getBusinessTimes().forEach(o -> {
            System.out.println("business time : " + o.getOpenTime() + " ~ " + o.getCloseTime());
        });

        registerPopup.getReservationTimes().forEach(o -> {
            System.out.println("reservation time : " + o.getStartTime() + " ~ " + o.getEndTime());
        });


    }



}
