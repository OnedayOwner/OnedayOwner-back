package com.OnedayOwner.server.platform.popup.service;

import com.OnedayOwner.server.global.exception.BusinessException;
import com.OnedayOwner.server.global.exception.ErrorCode;
import com.OnedayOwner.server.platform.Address;
import com.OnedayOwner.server.platform.popup.dto.PopupDto;
import com.OnedayOwner.server.platform.popup.entity.BusinessTime;
import com.OnedayOwner.server.platform.popup.entity.Menu;
import com.OnedayOwner.server.platform.popup.entity.PopupRestaurant;
import com.OnedayOwner.server.platform.popup.repository.BusinessTimeRepository;
import com.OnedayOwner.server.platform.popup.repository.MenuRepository;
import com.OnedayOwner.server.platform.popup.repository.PopupRestaurantRepository;
import com.OnedayOwner.server.platform.reservation.entity.ReservationTime;
import com.OnedayOwner.server.platform.reservation.repository.ReservationTimeRepository;
import com.OnedayOwner.server.platform.user.entity.Role;
import com.OnedayOwner.server.platform.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PopupService {

    private final PopupRestaurantRepository popupRestaurantRepository;
    private final MenuRepository menuRepository;
    private final UserRepository userRepository;
    private final BusinessTimeRepository businessTimeRepository;
    private final ReservationTimeRepository reservationTimeRepository;


    @Transactional
    public PopupDto.PopupInBusinessDetail registerPopup(
            PopupDto.PopupRestaurantForm restaurantForm,
            Long ownerId){
        //레스토랑 등록
        PopupRestaurant restaurant = popupRestaurantRepository.save(PopupRestaurant.builder()
                .name(restaurantForm.getName())
                .startDateTime(restaurantForm.getStartDateTime())
                .endDateTime(restaurantForm.getEndDateTime())
                .address(Address.builder()
                        .zipcode(restaurantForm.getAddress().getZipcode())
                        .street(restaurantForm.getAddress().getStreet())
                        .detail(restaurantForm.getAddress().getDetail())
                        .build())
                .user(userRepository.findByIdAndRole(ownerId, Role.OWNER).orElseThrow(
                        () -> new BusinessException(ErrorCode.OWNER_NOT_FOUND)
                ))
                .build());

        registerMenus(restaurantForm.getMenuForms(), restaurant.getId());//메뉴 등록

        restaurantForm.getBusinessTimes().forEach(o -> {
            BusinessTime businessTime = BusinessTime.builder()
                    .openTime(o.getOpenTime())
                    .closeTime(o.getCloseTime())
                    .popupRestaurant(restaurant)
                    .build();
            businessTimeRepository.save(businessTime);//영업시간 등록
            registerReservationTime(o, restaurant);//예약시간 등록
        });

        return PopupDto.PopupInBusinessDetail.builder()
                .popupRestaurant(restaurant)
                .build();

    }

    @Transactional
    public PopupDto.PopupInBusinessDetail getPopupInBusinessDetail(Long ownerId){
        return popupRestaurantRepository.getInBusinessPopupRestaurantWithMenusAndReservationTimesAndBusinessTimesByUserId(ownerId)
                .map(PopupDto.PopupInBusinessDetail::new)
                .orElseThrow(
                        () -> new BusinessException(ErrorCode.IN_BUSINESS_POPUP_NOT_FOUND)
                );
    }

    @Transactional
    public PopupDto.PopupHistoryDetail getPopupHistoryDetail(Long popupId){
        return popupRestaurantRepository.getPopupRestaurantWithMenusById(popupId)
                .map(PopupDto.PopupHistoryDetail::new)
                .orElseThrow(
                        () -> new BusinessException(ErrorCode.POPUP_NOT_FOUND)
                );
    }

    @Transactional
    public void registerReservationTime(PopupDto.BusinessTimeForm form, PopupRestaurant restaurant){
        LocalDate startDate = restaurant.getStartDateTime().toLocalDate();
        LocalDate endDate = restaurant.getEndDateTime().toLocalDate();

        Stream.iterate(startDate, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(startDate, endDate) + 1)
                .forEach(date -> {
                    long minutesBetween = ChronoUnit.MINUTES.between(form.getOpenTime(), form.getCloseTime());
                    long numberOfSlots = minutesBetween / form.getReservationTimeUnit();

                    for (int i = 0; i < numberOfSlots; i++) {
                        LocalTime slotStartTime = form.getOpenTime().plusMinutes((i * form.getReservationTimeUnit()));
                        LocalTime slotEndTime = slotStartTime.plusMinutes(form.getReservationTimeUnit());

                        LocalDateTime startDateTime = LocalDateTime.of(date, slotStartTime);
                        LocalDateTime endDateTime = LocalDateTime.of(date, slotEndTime);
                        if(startDateTime.isBefore(restaurant.getStartDateTime())){
                            continue;
                        }
                        if(endDateTime.isAfter(restaurant.getEndDateTime())){
                            break;
                        }

                        reservationTimeRepository.save(ReservationTime.builder()
                                .popupRestaurant(restaurant)
                                .reservationDate(date)
                                .startTime(slotStartTime)
                                .endTime(slotEndTime)
                                .maxPeople(form.getMaxPeoplePerTime())
                                .build());
                    }
                });
    }

    @Transactional
    public PopupDto.MenuDetail registerMenu(PopupDto.MenuForm menuForm, Long popupId){
        return new PopupDto.MenuDetail(menuRepository.save(Menu.builder()
                .name(menuForm.getName())
                .price(menuForm.getPrice())
                .description(menuForm.getDescription())
                .popupRestaurant(popupRestaurantRepository.findById(popupId).orElseThrow(
                        () -> new BusinessException(ErrorCode.POPUP_NOT_FOUND)
                ))
                .build()));
    }

    @Transactional
    public List<PopupDto.MenuDetail> registerMenus(List<PopupDto.MenuForm> menuForms, Long popupId){
        return menuForms.stream()
                .map(menuForm -> menuRepository.save(Menu.builder()
                    .name(menuForm.getName())
                    .price(menuForm.getPrice())
                    .description(menuForm.getDescription())
                    .popupRestaurant(popupRestaurantRepository.findById(popupId).orElseThrow(
                            () -> new BusinessException(ErrorCode.POPUP_NOT_FOUND)
                    ))
                    .build()))
                .toList()
                .stream()
                .map(PopupDto.MenuDetail::new)
                .toList();
    }

    @Transactional
    public List<PopupDto.PopupSummary> getPopupHistoryByOwner(Long ownerId){
        return popupRestaurantRepository.findAllByUserIdAndInBusiness(ownerId, false)
                .stream()
                .map(PopupDto.PopupSummary::new)
                .toList();
    }

    @Transactional
    public PopupRestaurant getPopupInBusinessByOwner(Long ownerId){
        return popupRestaurantRepository.findByUserIdAndInBusiness(ownerId, true)
                .orElseThrow(
                        () -> new BusinessException(ErrorCode.IN_BUSINESS_POPUP_NOT_FOUND)
                );
    }

    //팝업 리스트 조회
    @Transactional
    public List<PopupDto.PopupSummaryForCustomer> getPopupsInBusinessForCustomer(){
        return popupRestaurantRepository.findAllByInBusiness(true)
                .stream()
                .map(PopupDto.PopupSummaryForCustomer::new)
                .toList();
    }
}
