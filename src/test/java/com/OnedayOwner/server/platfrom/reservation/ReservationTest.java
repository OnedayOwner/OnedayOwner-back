package com.OnedayOwner.server.platfrom.reservation;

import com.OnedayOwner.server.global.exception.BusinessException;
import com.OnedayOwner.server.platform.popup.dto.PopupDto;
import com.OnedayOwner.server.platform.popup.service.PopupService;
import com.OnedayOwner.server.platform.reservation.dto.ReservationDto;
import com.OnedayOwner.server.platform.reservation.service.ReservationService;
import com.OnedayOwner.server.platform.user.entity.Gender;
import com.OnedayOwner.server.platform.user.entity.Role;
import com.OnedayOwner.server.platform.user.entity.User;
import com.OnedayOwner.server.platform.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
public class ReservationTest {

    @Autowired
    ReservationService reservationService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PopupService popupService;

    Long ownerId;
    Long customer1Id;
    Long customer2Id;
    Long popupId;
    List<Long> menuId = new ArrayList<>();

    @BeforeEach
    void initData(){
        //owner 생성
        User owner = User.builder()
                .email("owner@naver.com")
                .gender(Gender.MALE)
                .role(Role.OWNER)
                .build();

        userRepository.save(owner);
        ownerId = owner.getId();

        //customer 생성
        User customer1 = User.builder()
                .email("customer1@naver.com")
                .gender(Gender.MALE)
                .role(Role.CUSTOMER)
                .build();
        User customer2 = User.builder()
                .email("customer2@naver.com")
                .gender(Gender.MALE)
                .role(Role.CUSTOMER)
                .build();

        userRepository.save(customer1);
        userRepository.save(customer2);
        customer1Id = customer1.getId();
        customer2Id = customer2.getId();

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
                .endDateTime(LocalDateTime.of(2025, 1, 15, 23, 0))
                .description("popup1 description")
                .build();

        PopupDto.PopupInBusinessDetail registerPopup = popupService.registerPopup(restaurantForm, owner.getId());
        popupId = registerPopup.getId();
        registerPopup.getMenus().forEach(o -> menuId.add(o.getId()));
    }

    @Test
    public void 팝업_예약_가능_일자_조회() {
        //given, when
        List<ReservationDto.ReservationTimeDto> reservationTimes = reservationService.getReservationTimes(popupId);

        //then
        Assertions.assertEquals(reservationTimes.size(), 15 * 4);
    }

    @Test
    public void 예약_생성() {
        //given
        List<ReservationDto.ReservationMenuForm> reservationMenus = new ArrayList<>();
        for(Long id : menuId){
            reservationMenus.add(ReservationDto.ReservationMenuForm.builder()
                    .menuId(id)
                    .quantity(2)
                    .build());
        }

        ReservationDto.ReservationForm reservationForm = ReservationDto.ReservationForm.builder()
                .popupId(popupId)
                .numberOfPeople(2)
                .reservationMenus(reservationMenus)
                .reservationTimeId(reservationService.getReservationTimes(popupId)
                        .stream()
                        .findFirst()
                        .get()
                        .getId())
                .build();

        //when
        ReservationDto.ReservationDetail reservationDetail = reservationService.registerReservation(reservationForm, customer1Id);

        //then
        Assertions.assertEquals(reservationDetail.getNumberOfPeople(), reservationForm.getNumberOfPeople());
        Assertions.assertEquals(reservationDetail.getReservationDateTime(), LocalDateTime.of(2025,1,1,11,0));
        Assertions.assertEquals(reservationDetail.getPopupSummaryForReservation().getId(), popupId);
        Assertions.assertEquals(reservationDetail.getReservationMenuDetails().size(), 2);
        Assertions.assertEquals(reservationService.getReservationTimes(popupId)
                .stream()
                .findFirst()
                .get()
                .getMaxPeople(), 8);
    }

    @Test
    public void 예약_생성_실패_인원_초과() {
        //given
        List<ReservationDto.ReservationMenuForm> reservationMenus = new ArrayList<>();
        for(Long id : menuId){
            reservationMenus.add(ReservationDto.ReservationMenuForm.builder()
                    .menuId(id)
                    .quantity(2)
                    .build());
        }

        ReservationDto.ReservationForm reservationForm = ReservationDto.ReservationForm.builder()
                .popupId(popupId)
                .numberOfPeople(11)
                .reservationMenus(reservationMenus)
                .reservationTimeId(reservationService.getReservationTimes(popupId)
                        .stream()
                        .findFirst()
                        .get()
                        .getId())
                .build();

        //when, then
        Assertions.assertThrows(BusinessException.class, () ->
                reservationService.registerReservation(reservationForm, customer1Id));
    }

    @Test
    public void 예약_조회() {
        //given
        List<ReservationDto.ReservationMenuForm> reservationMenus = new ArrayList<>();
        for(Long id : menuId){
            reservationMenus.add(ReservationDto.ReservationMenuForm.builder()
                    .menuId(id)
                    .quantity(2)
                    .build());
        }

        ReservationDto.ReservationForm reservationForm = ReservationDto.ReservationForm.builder()
                .popupId(popupId)
                .numberOfPeople(2)
                .reservationMenus(reservationMenus)
                .reservationTimeId(reservationService.getReservationTimes(popupId)
                        .stream()
                        .findFirst()
                        .get()
                        .getId())
                .build();

        ReservationDto.ReservationDetail reservationDetail = reservationService.registerReservation(reservationForm, customer1Id);

        //when
        ReservationDto.ReservationDetail reservationDetailForCustomer = reservationService.getReservationDetailForCustomer(reservationDetail.getId(), customer1Id);

        //then
        Assertions.assertEquals(reservationDetailForCustomer.getId(), reservationDetail.getId());
        Assertions.assertEquals(reservationDetailForCustomer.getNumberOfPeople(), 2);
        Assertions.assertEquals(reservationDetailForCustomer.getReservationDateTime(), LocalDateTime.of(2025,1,1,11,0));
        Assertions.assertEquals(reservationDetailForCustomer.getReservationMenuDetails().size(), 2);
        Assertions.assertEquals(reservationDetailForCustomer.getPopupSummaryForReservation().getId(), popupId);
        Assertions.assertEquals(reservationDetailForCustomer.getPopupSummaryForReservation().getName(), "popup1");
    }

    @Test
    public void 예약_리스트_조회() {
        //given
        for(int i=0;i<2;i++){
            List<ReservationDto.ReservationMenuForm> reservationMenus = new ArrayList<>();
            for(Long id : menuId){
                reservationMenus.add(ReservationDto.ReservationMenuForm.builder()
                        .menuId(id)
                        .quantity(2)
                        .build());
            }

            ReservationDto.ReservationForm reservationForm = ReservationDto.ReservationForm.builder()
                    .popupId(popupId)
                    .numberOfPeople(2)
                    .reservationMenus(reservationMenus)
                    .reservationTimeId(reservationService.getReservationTimes(popupId)
                            .get(i)
                            .getId())
                    .build();

            reservationService.registerReservation(reservationForm, customer1Id);
        }

        //when
        List<ReservationDto.ReservationSummary> reservationsByCustomer = reservationService.getReservationsByCustomer(customer1Id);

        //then
        Assertions.assertEquals(reservationsByCustomer.size(), 2);
        Assertions.assertEquals(reservationsByCustomer.get(0).getReservationDateTime(), LocalDateTime.of(2025,1,1,11,0));
        Assertions.assertEquals(reservationsByCustomer.get(1).getReservationDateTime(), LocalDateTime.of(2025,1,1,11,30));

    }
}
