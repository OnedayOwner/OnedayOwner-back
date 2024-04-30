package com.OnedayOwner.server.platform.reservation.service;

import com.OnedayOwner.server.global.exception.BusinessException;
import com.OnedayOwner.server.global.exception.ErrorCode;
import com.OnedayOwner.server.platform.popup.dto.PopupDto;
import com.OnedayOwner.server.platform.popup.entity.PopupRestaurant;
import com.OnedayOwner.server.platform.popup.repository.PopupRestaurantRepository;
import com.OnedayOwner.server.platform.reservation.dto.ReservationDto;
import com.OnedayOwner.server.platform.reservation.entity.ReservationTime;
import com.OnedayOwner.server.platform.reservation.repository.ReservationRepository;
import com.OnedayOwner.server.platform.reservation.repository.ReservationTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final PopupRestaurantRepository popupRestaurantRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    /*
    예약을 위한 팝업 매장 정보 제공
     */
    @Transactional
    public ReservationDto.PopupDetailForReservationDto getPopupDetailForReservation(Long popupId){
        return ReservationDto.PopupDetailForReservationDto.builder()
                .popupRestaurant(popupRestaurantRepository.findById(popupId)
                        .orElseThrow(
                                () -> new BusinessException(ErrorCode.IN_BUSINESS_POPUP_NOT_FOUND)
                        ))
                .reservationPossibleDateTimes(getReservationPossibleDateTimes(popupId))
                .build();
    }

    /*
    팝업 기간중 현재 LocalDateTime 이후로 예약이 가능한 모든 LocalDateTime을 리스트로 반환
     */
    protected List<LocalDateTime> getReservationPossibleDateTimes(Long popupId) {
        List<LocalDateTime> ReservationPossibleDateTimes = new ArrayList<>();
        PopupRestaurant popupRestaurant = popupRestaurantRepository.findById(popupId)
                .orElseThrow(
                        () -> new BusinessException(ErrorCode.IN_BUSINESS_POPUP_NOT_FOUND)
                );
        /*
        팝업 기간 중 이미 maxPeople에 도달한 LocalDateTime 리스트
         */
        List<LocalDateTime> reservationTimesByCustomersGreaterThanMaxPeople = reservationRepository.findReservationTimesByCustomersGreaterThanMaxPeople(popupId, popupRestaurant.getReservationTimes().stream().findFirst()
                .orElseThrow(
                        () -> new BusinessException(ErrorCode.RESERVATION_TIME_NOT_FOUND)
                ).getMaxPeople());

        List<ReservationTime> reservationTimes = reservationTimeRepository.findAllByPopupRestaurantId(popupId);

        LocalDate i = popupRestaurant.getStartDateTime().toLocalDate();
        while(i.isBefore(popupRestaurant.getEndDateTime().toLocalDate().plusDays(1))) {
            for (ReservationTime reservationTime : reservationTimes) {
                LocalDateTime reservationPossibleDateTime = i.atTime(reservationTime.getStartTime());
                if (reservationTimesByCustomersGreaterThanMaxPeople.contains(reservationPossibleDateTime)) {
                    continue;
                }
                if (reservationPossibleDateTime.isAfter(LocalDateTime.now()) && reservationPossibleDateTime.isAfter(popupRestaurant.getStartDateTime()) && reservationPossibleDateTime.isBefore(popupRestaurant.getEndDateTime())) {
                    ReservationPossibleDateTimes.add(reservationPossibleDateTime);
                }
            }
            i.plusDays(1);
        }
        return ReservationPossibleDateTimes;
    }
}
