package com.OnedayOwner.server.platform.reservation.service;

import com.OnedayOwner.server.global.exception.BusinessException;
import com.OnedayOwner.server.global.exception.ErrorCode;
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

import java.time.LocalDateTime;
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
    public ReservationDto.ReservationTimesDto getReservationTimes(Long popupId){
        return ReservationDto.ReservationTimesDto.builder()
                .reservationTimes(reservationTimeRepository.getPossibleReservationTimes(popupId))
                .build();
    }

    /*
    예약 생성
     */
    @Transactional
    public ReservationDto.ReservationDetail registerReservation(
            ReservationDto.ReservationForm reservationForm,
            Long customerId
    ) {
        //예약 등록
        Reservation reservation = reservationRepository.save(Reservation.builder()
                .reservationDateTime(validateReservation(reservationTimeRepository
                        .findById(reservationForm.getReservationTimeId()).orElseThrow(
                                () -> new BusinessException(ErrorCode.RESERVATION_TIME_NOT_FOUND)
                        ), reservationForm.getNumberOfPeople()))
                .numberOfPeople(reservationForm.getNumberOfPeople())
                .popupRestaurant(popupRestaurantRepository.findById(reservationForm.getPopupId()).orElseThrow(
                        () -> new BusinessException(ErrorCode.POPUP_NOT_FOUND)
                ))
                .customer(customerRepository.findById(customerId).orElseThrow(
                        () -> new BusinessException(ErrorCode.CUSTOMER_NOT_FOUND)
                ))
                .build());

        //예약 메뉴 등록
        reservationForm.getReservationMenus().forEach(o -> {
            reservationMenuRepository.save(ReservationMenu.builder()
                            .reservation(reservation)
                            .quantity(o.getQuantity())
                            .menu(menuRepository.findById(o.getMenuId()).orElseThrow(
                                    () -> new BusinessException(ErrorCode.MENU_NOT_FOUND)
                            ))
                    .build());
        });

        return ReservationDto.ReservationDetail.builder()
                .reservation(reservation)
                .build();
    }

    /*
    예약 검증
     */
    private LocalDateTime validateReservation(ReservationTime reservationTime, int numberOfPeople){
        LocalDateTime reservationDateTime = reservationTime.getReservationDate().atTime(reservationTime.getStartTime());
        //예약 시간이 현재 이후인지 검사
        if(reservationDateTime.isBefore(LocalDateTime.now())){
            throw new BusinessException(ErrorCode.CAN_NOT_RESERVE_DURING_THAT_TIME);
        }
        //예약 가능 인원을 초과하진 않는지 검사
        if(reservationTime.getMaxPeople() < numberOfPeople){
            throw new BusinessException(ErrorCode.NUMBER_OF_PEOPLE_EXCEEDED);
        }
        reservationTime.minusMaxPeople(numberOfPeople);
        return  reservationDateTime;
    }

    /*
    예약 상세 조회-고객
     */
    @Transactional
    public ReservationDto.ReservationDetail getReservationDetailForCustomer(Long reservationId, Long customerId) {
        //조회하는 고객의 예약이 맞는지 체크
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(
                () -> new BusinessException(ErrorCode.RESERVATION_NOT_FOUND)
        );
        if (!reservation.getCustomer().getId().equals(customerId)) {
            throw new BusinessException(ErrorCode.CANNOT_ACCESS_RESERVATION);
        }

        return ReservationDto.ReservationDetail.builder()
                .reservation(reservation)
                .build();
    }

    /*
    예약 리스트 조회
     */
    @Transactional
    public List<ReservationDto.ReservationSummary> getReservationsByCustomer(Long customerId) {
        return reservationRepository.findAllByCustomerId(customerId)
                .stream()
                .map(ReservationDto.ReservationSummary::new)
                .toList();
    }
}
