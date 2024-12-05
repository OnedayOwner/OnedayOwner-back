package com.OnedayOwner.server.platfrom;

import com.OnedayOwner.server.platform.popup.dto.PopupDto;
import com.OnedayOwner.server.platform.popup.service.PopupService;
import com.OnedayOwner.server.platform.reservation.dto.ReservationDto;
import com.OnedayOwner.server.platform.reservation.service.ReservationService;
import com.OnedayOwner.server.platform.user.entity.Gender;
import com.OnedayOwner.server.platform.user.entity.Role;
import com.OnedayOwner.server.platform.user.entity.User;
import com.OnedayOwner.server.platform.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
public class QueryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PopupService popupService;
    @Autowired
    ReservationService reservationService;

    @Test
    public void 쿼리_테스트(){
        //owner 생성
        User owner = userRepository.save(User.builder()
                .email("owner@naver.com")
                .gender(Gender.MALE)
                .role(Role.OWNER)
                .build());

        //customer 생성
        User customer1 = userRepository.save(User.builder()
                .email("customer1@naver.com")
                .gender(Gender.MALE)
                .role(Role.CUSTOMER)
                .build());
        User customer2 = userRepository.save(User.builder()
                .email("customer2@naver.com")
                .gender(Gender.MALE)
                .role(Role.CUSTOMER)
                .build());

        //popup 생성
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
                .street("street1")
                .zipcode("zipcode1")
                .detail("detail1")
                .build();

        PopupDto.PopupRestaurantForm restaurantForm = PopupDto.PopupRestaurantForm.builder()
                .name("popup1")
                .businessTimes(List.of(timeForm))
                .menuForms(List.of(menuForm1, menuForm2))
                .address(addressForm)
                .startDateTime(LocalDateTime.of(2025, 1, 1, 0, 0))
                .endDateTime(LocalDateTime.of(2025, 1, 7, 23, 0))
                .description("popup1 description")
                .build();

        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "restaurantImage",
                "test-image.png",
                "image/png",
                "test image content".getBytes()
        );
        MockMultipartFile mockMenuFile1 = new MockMultipartFile(
                "menuImage",
                "menuImage1.png",
                "image/png",
                "test image content".getBytes()
        );
        MockMultipartFile mockMenuFile2 = new MockMultipartFile(
                "menuImage",
                "menuImage2.png",
                "image/png",
                "test image content".getBytes()
        );

        List<MultipartFile> mockMultipartFileList = new ArrayList<>();
        mockMultipartFileList.add(mockMenuFile1);
        mockMultipartFileList.add(mockMenuFile2);


        PopupDto.PopupInBusinessDetail registerPopup = popupService.registerPopup(restaurantForm, mockMultipartFile, mockMultipartFileList, owner.getId());
//
//        List<ReservationDto.ReservationInfoDto> reservationTimes = reservationService.getReservationInfo(registerPopup.getId()).getReservationTimes();
//        for(ReservationDto.ReservationInfoDto reservationTimeDto : reservationTimes){
//            System.out.println(reservationTimeDto.getReservationDate().atTime(reservationTimeDto.getStartTime()));
//        }
//
//        List<ReservationDto.ReservationMenuForm> reservationMenus = new ArrayList<>();
//        for(PopupDto.MenuDetail menu: registerPopup.getMenus()){
//            reservationMenus.add(ReservationDto.ReservationMenuForm.builder()
//                    .menuId(menu.getId())
//                    .quantity(1)
//                    .build());
//        }
//        for(ReservationDto.ReservationTimeDto reservationTimeDto : reservationTimes) {
//            ReservationDto.ReservationForm reservationForm = ReservationDto.ReservationForm.builder()
//                    .reservationMenus(reservationMenus)
//                    .reservationTimeId(reservationTimeDto.getId())
//                    .popupId(registerPopup.getId())
//                    .numberOfPeople(2)
//                    .build();
//            reservationService.registerReservation(reservationForm, customer1.getId());
//            reservationService.registerReservation(reservationForm, customer1.getId());
//            reservationService.registerReservation(reservationForm, customer2.getId());
//            reservationService.registerReservation(reservationForm, customer2.getId());
//        }
    }
}
