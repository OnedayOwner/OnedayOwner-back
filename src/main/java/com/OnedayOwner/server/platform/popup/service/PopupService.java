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
import com.OnedayOwner.server.platform.user.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PopupService {

    private final PopupRestaurantRepository popupRestaurantRepository;
    private final MenuRepository menuRepository;
    private final OwnerRepository ownerRepository;
    private final BusinessTimeRepository businessTimeRepository;
    private final ReservationTimeRepository reservationTimeRepository;


    @Transactional
    public PopupDto.PopupInBusinessDetail registerPopup(
            PopupDto.PopupRestaurantForm restaurantForm,
            PopupDto.AddressForm addressForm,
            List<PopupDto.MenuForm> menuForms,
            Long ownerId){
        //레스토랑 등록
        PopupRestaurant restaurant = popupRestaurantRepository.save(PopupRestaurant.builder()
                .name(restaurantForm.getName())
                .address(Address.builder()
                        .city(addressForm.getCity())
                        .street(addressForm.getStreet())
                        .zipcode(addressForm.getZipcode())
                        .detail(addressForm.getDetail())
                        .build())
                .owner(ownerRepository.findById(ownerId).orElseThrow(
                        () -> new BusinessException(ErrorCode.OWNER_NOT_FOUND)
                ))
                .build());

        registerMenus(menuForms, restaurant.getId());//메뉴 등록

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
        return popupRestaurantRepository.getInBusinessPopupRestaurantWithMenusAndReservationTimesAndBusinessTimes(ownerId)
                .map(PopupDto.PopupInBusinessDetail::new)
                .orElseThrow(
                        () -> new BusinessException(ErrorCode.IN_BUSINESS_POPUP_NOT_FOUND)
                );
    }

    @Transactional
    public PopupDto.PopupHistoryDetail getPopupHistoryDetail(Long popupId){
        return popupRestaurantRepository.getPopupRestaurantWithMenus(popupId)
                .map(PopupDto.PopupHistoryDetail::new)
                .orElseThrow(
                        () -> new BusinessException(ErrorCode.POPUP_NOT_FOUND)
                );
    }

    @Transactional
    public void registerReservationTime(PopupDto.BusinessTimeForm form, PopupRestaurant restaurant){
        long minutesBetween = ChronoUnit.MINUTES.between(form.getOpenTime(), form.getCloseTime());
        long numberOfSlots = minutesBetween / form.getReservationTimeUnit();

        for(int i = 0; i<numberOfSlots; i++){
            LocalTime slotStartTime = form.getOpenTime().plusMinutes((i * form.getReservationTimeUnit()));
            LocalTime slotEndTime = slotStartTime.plusMinutes(form.getReservationTimeUnit());

            reservationTimeRepository.save(ReservationTime.builder()
                    .popupRestaurant(restaurant)
                    .startTime(slotStartTime)
                    .endTime(slotEndTime)
                    .maxPeople(form.getMaxPeoplePerTime())
                    .build());
        }
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
        return popupRestaurantRepository.findAllByOwnerIdAndInBusiness(ownerId, false)
                .stream()
                .map(PopupDto.PopupSummary::new)
                .toList();
    }

    @Transactional
    public PopupRestaurant getPopupInBusinessByOwner(Long ownerId){
        return popupRestaurantRepository.findByOwnerIdAndInBusiness(ownerId, true)
                .orElseThrow(
                        () -> new BusinessException(ErrorCode.IN_BUSINESS_POPUP_NOT_FOUND)
                );
    }


}