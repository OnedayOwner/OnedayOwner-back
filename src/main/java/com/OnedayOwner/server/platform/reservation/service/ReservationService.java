package com.OnedayOwner.server.platform.reservation.service;

import com.OnedayOwner.server.global.exception.BusinessException;
import com.OnedayOwner.server.global.exception.ErrorCode;
import com.OnedayOwner.server.platform.popup.entity.Menu;
import com.OnedayOwner.server.platform.popup.entity.PopupRestaurant;
import com.OnedayOwner.server.platform.popup.repository.MenuRepository;
import com.OnedayOwner.server.platform.popup.repository.PopupRestaurantRepository;
import com.OnedayOwner.server.platform.reservation.dto.ReservationDto;
import com.OnedayOwner.server.platform.reservation.entity.Reservation;
import com.OnedayOwner.server.platform.reservation.entity.ReservationMenu;
import com.OnedayOwner.server.platform.reservation.entity.ReservationTime;
import com.OnedayOwner.server.platform.reservation.repository.ReservationMenuRepository;
import com.OnedayOwner.server.platform.reservation.repository.ReservationRepository;
import com.OnedayOwner.server.platform.reservation.repository.ReservationTimeRepository;
import com.OnedayOwner.server.platform.user.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final PopupRestaurantRepository popupRestaurantRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final CustomerRepository customerRepository;
    private final ReservationMenuRepository reservationMenuRepository;
    private final MenuRepository menuRepository;

    /*
    팝업의 예약 가능 일자 조회
     */
    @Transactional
    public ReservationDto.ReservationPossibleTimesDto getPossibleTimesForReservation(Long popupId){
        return ReservationDto.ReservationPossibleTimesDto.builder()
                .reservationPossibleDateTimes(checkPossibleTimesForReservation(popupId))
                .build();
    }

    /*
    팝업 기간중 현재 LocalDateTime 이후로 예약이 가능한 모든 LocalDateTime을 리스트로 반환
     */
    private List<LocalDateTime> checkPossibleTimesForReservation(Long popupId) {
        List<LocalDateTime> ReservationPossibleTimes = new ArrayList<>();
        PopupRestaurant popupRestaurant = popupRestaurantRepository.findById(popupId)
                .orElseThrow(
                        () -> new BusinessException(ErrorCode.IN_BUSINESS_POPUP_NOT_FOUND)
                );

        LocalDate i = popupRestaurant.getStartDateTime().toLocalDate();
        while(i.isBefore(popupRestaurant.getEndDateTime().toLocalDate().plusDays(1))) {
            for (ReservationTime reservationTime : reservationTimeRepository.findAllByPopupRestaurantId(popupId)) {
                LocalDateTime reservationPossibleTime = i.atTime(reservationTime.getStartTime());
                if (reservationRepository.findReservationTimesByCustomersGreaterThanMaxPeople(popupId, popupRestaurant.getReservationTimes().stream().findFirst()
                        .orElseThrow(
                                () -> new BusinessException(ErrorCode.RESERVATION_TIME_NOT_FOUND)
                        ).getMaxPeople()).contains(reservationPossibleTime)) {
                    continue;
                }
                if (reservationPossibleTime.isAfter(LocalDateTime.now()) && reservationPossibleTime.isAfter(popupRestaurant.getStartDateTime()) && reservationPossibleTime.isBefore(popupRestaurant.getEndDateTime())) {
                    ReservationPossibleTimes.add(reservationPossibleTime);
                }
            }
            i = i.plusDays(1);
        }
        return ReservationPossibleTimes;
    }

    /*
    예약 생성
    예약 상세 내역을 반환
     */
    @Transactional
    public ReservationDto.ReservationDetailForCustomer registerReservation(
            ReservationDto.ReservationForm reservationForm,
            Long customerId
    ) {
        //예약 시간 검증
        if(!validateReservationTime(reservationForm.getReservationTime(), reservationForm.getNumberOfPeople(), reservationForm.getPopupId())) {
            throw new BusinessException(ErrorCode.CAN_NOT_RESERVE_DURING_THAT_TIME);
        }

        //예약 저장
        Reservation reservation = reservationRepository.save(Reservation.builder()
                .numberOfPeople(reservationForm.getNumberOfPeople())
                .customer(customerRepository.findById(customerId).orElseThrow(
                        () -> new BusinessException(ErrorCode.CUSTOMER_NOT_FOUND)
                ))
                .popupRestaurant(popupRestaurantRepository.findById(reservationForm.getPopupId()).orElseThrow(
                        () -> new BusinessException(ErrorCode.POPUP_NOT_FOUND)
                ))
                .reservationTime(reservationForm.getReservationTime())
                .build());

        //예약 메뉴 저장
        List<ReservationDto.ReservationMenuDetail> reservationMenuDetails = new ArrayList<>();
        for(ReservationDto.ReservationMenuForm reservationMenuForm : reservationForm.getReservationMenus()) {
            Menu menu = menuRepository.findById(reservationMenuForm.getMenuId()).orElseThrow(
                    () -> new BusinessException(ErrorCode.MENU_NOT_FOUND)
            );
            ReservationMenu reservationMenu = reservationMenuRepository.save(ReservationMenu.builder()
                    .menu(menu)
                    .reservation(reservation)
                    .quantity(reservationMenuForm.getQuantity())
                    .build());
            reservationMenuDetails.add(ReservationDto.ReservationMenuDetail.builder()
                    .reservationMenu(reservationMenu)
                    .menu(menu)
                    .build());
        }
        return ReservationDto.ReservationDetailForCustomer.builder()
                .reservation(reservation)
                .popupRestaurant(reservation.getPopupRestaurant())
                .reservationMenuDetails(reservationMenuDetails)
                .address(reservation.getPopupRestaurant().getAddress())
                .build();

    }

    /*
    예약 시간 검증 로직
    예약 시간이 등록된 시간과 맞는지, 현재시간 이후인지, (예약인원 + 누적인원)이 Max인원보다 작거나 같은지 check
     */
    private boolean validateReservationTime(LocalDateTime reservationTime, int numberOfPeople, Long popupId) {
        PopupRestaurant findPopup = popupRestaurantRepository.findById(popupId).orElseThrow(
                () -> new BusinessException(ErrorCode.IN_BUSINESS_POPUP_NOT_FOUND)
        );
        return reservationTimeRepository.existsByStartTimeAndPopupRestaurantId(reservationTime.toLocalTime(), popupId)
                && reservationTime.isAfter(LocalDateTime.now())
                && reservationTime.isAfter(findPopup.getStartDateTime())
                && reservationTime.isBefore(findPopup.getEndDateTime())
                && reservationRepository.findNumberOfPeopleByReservationTimeAndPopupId(popupId, reservationTime).orElse(0) + numberOfPeople
                <= findPopup.getReservationTimes().stream().findFirst().orElseThrow(
                () -> new BusinessException(ErrorCode.RESERVATION_TIME_NOT_FOUND)
        ).getMaxPeople();
    }

    /*
    예약 상세 조회-고객
     */
    @Transactional
    public ReservationDto.ReservationDetailForCustomer getReservationDetailByCustomer(Long reservationId, Long customerId) {
        //조회하는 고객의 예약이 맞는지 체크
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(
                () -> new BusinessException(ErrorCode.RESERVATION_NOT_FOUND)
        );
        if (!reservation.getCustomer().getId().equals(customerId)) {
            throw new BusinessException(ErrorCode.CANNOT_ACCESS_RESERVATION);
        }

        List<ReservationDto.ReservationMenuDetail> reservationMenuDetails = new ArrayList<>();
        for(ReservationMenu reservationMenu : reservation.getReservationMenus()) {
            reservationMenuDetails.add(ReservationDto.ReservationMenuDetail.builder()
                    .menu(reservationMenu.getMenu())
                    .reservationMenu(reservationMenu)
                    .build());
        }
        return ReservationDto.ReservationDetailForCustomer.builder()
                .reservation(reservation)
                .popupRestaurant(reservation.getPopupRestaurant())
                .reservationMenuDetails(reservationMenuDetails)
                .address(reservation.getPopupRestaurant().getAddress())
                .build();
    }
}
