package com.OnedayOwner.server.platform.reservation.service;

import com.OnedayOwner.server.global.exception.BusinessException;
import com.OnedayOwner.server.global.exception.ErrorCode;
import com.OnedayOwner.server.platform.feedback.repository.FeedbackRepository;
import com.OnedayOwner.server.platform.popup.repository.MenuRepository;
import com.OnedayOwner.server.platform.popup.repository.PopupRestaurantRepository;
import com.OnedayOwner.server.platform.reservation.dto.ReservationDto;
import com.OnedayOwner.server.platform.reservation.entity.Reservation;
import com.OnedayOwner.server.platform.reservation.entity.ReservationMenu;
import com.OnedayOwner.server.platform.reservation.entity.ReservationTime;
import com.OnedayOwner.server.platform.reservation.repository.ReservationMenuRepository;
import com.OnedayOwner.server.platform.reservation.repository.ReservationRepository;
import com.OnedayOwner.server.platform.reservation.repository.ReservationTimeRepository;
import com.OnedayOwner.server.platform.user.entity.Role;
import com.OnedayOwner.server.platform.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final PopupRestaurantRepository popupRestaurantRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final UserRepository userRepository;
    private final ReservationMenuRepository reservationMenuRepository;
    private final MenuRepository menuRepository;
    private final FeedbackRepository feedbackRepository;
    private final RedissonClient redissonClient;
    private final TransactionTemplate transactionTemplate;

    /**
     * 팝업 예약 정보 조회
     * @param popupId 팝업 ID
     * @return 팝업 예약 가능 일자 및 시간, 예약 가능 인원 등을 담은 DTO
     */
    @Transactional
    public ReservationDto.ReservationInfoDto getReservationInfo(Long popupId){
        return ReservationDto.ReservationInfoDto.builder()
                .popupRestaurant(popupRestaurantRepository.findById(popupId).orElseThrow(
                        () -> new BusinessException(ErrorCode.POPUP_NOT_FOUND)
                ))
                .reservationTimes(reservationTimeRepository.getPossibleReservationTimes(popupId))
                .build();
    }

    /**
     * 팝업 메뉴 조회
     * @param popupId 팝업 ID
     * @return 팝업의 메뉴 정보를 담은 DTO
     */
    public ReservationDto.ReservationMenuDto getReservationMenus(Long popupId){
        return ReservationDto.ReservationMenuDto.builder()
                .popupRestaurant(popupRestaurantRepository.getPopupRestaurantWithMenusById(popupId).orElseThrow(
                        () -> new BusinessException(ErrorCode.POPUP_NOT_FOUND)
                ))
                .build();
    }

    /**
     * 실제 예약을 등록하는 로직
     * @param reservationForm 예약 등록 DTO
     * @param customerId 고객 ID
     * @return 예약 정보를 담은 DTO
     */
    @Transactional
    protected ReservationDto.ReservationDetail executeReservation(
            ReservationDto.ReservationForm reservationForm,
            Long customerId
    ) {
        ReservationTime reservationTime = reservationTimeRepository.findById(reservationForm.getReservationTimeId())
                .orElseThrow(() -> new BusinessException(ErrorCode.RESERVATION_TIME_NOT_FOUND));

        if (!reservationTime.getPopupRestaurant().getId().equals(reservationForm.getPopupId())) {
            throw new BusinessException(ErrorCode.POPUP_NOT_MATCH);
        }

        //예약 등록
        Reservation reservation = reservationRepository.save(Reservation.builder()
                .reservationDateTime(validateReservation(reservationTime, reservationForm.getNumberOfPeople()))
                .numberOfPeople(reservationForm.getNumberOfPeople())
                .popupRestaurant(popupRestaurantRepository.findById(reservationForm.getPopupId()).orElseThrow(
                        () -> new BusinessException(ErrorCode.POPUP_NOT_FOUND)
                ))
                .user(userRepository.findByIdAndRole(customerId, Role.CUSTOMER).orElseThrow(
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

    /**
     * 예약 검증 (예약 시간 및 예약 인원 검증)
     * @param reservationTime 예약 시간
     * @param numberOfPeople 예약 인원
     * @return 예약 가능하다면 예약 시간을 반환 불가능 하다면 에러를 던짐
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

    /**
     * 예약 상세 조회
     * @param reservationId 예약 ID
     * @param customerId 고객 ID
     * @return 예약 상세 DTO
     */
    @Transactional
    public ReservationDto.ReservationDetailForUser getReservationDetailForCustomer(Long reservationId, Long customerId) {
        //조회하는 고객의 예약이 맞는지 검증
        Reservation reservation = reservationRepository.findReservationWithDetails(reservationId).orElseThrow(
                () -> new BusinessException(ErrorCode.RESERVATION_NOT_FOUND)
        );
        if (!reservation.getUser().getId().equals(customerId)) {
            throw new BusinessException(ErrorCode.CANNOT_ACCESS_RESERVATION);
        }
        return feedbackRepository.findByReservationId(reservationId)
                .map(value -> new ReservationDto.ReservationDetailForUser(reservation, value.getId()))
                .orElseGet(() -> new ReservationDto.ReservationDetailForUser(reservation, null));

    }

    /**
     * 방문 예정 예약 조회
     * @param customerId 고객 ID
     * @return 예약 요약 DTO 리스트
     */
    @Transactional
    public List<ReservationDto.ReservationSummary> getUpcomingReservations(Long customerId) {
        return reservationRepository.findUpcomingReservationsByUserId(customerId)
                .stream()
                .map(ReservationDto.ReservationSummary::new)
                .toList();
    }

    /**
     * 방문 완료 예약 조회
     * @param customerId 고객 ID
     * @return 예약 요약 DTO 리스트
     */
    @Transactional
    public List<ReservationDto.ReservationSummary> getCompletedReservations(Long customerId) {
        return reservationRepository.findCompletedReservationsByUserId(customerId)
                .stream()
                .map(ReservationDto.ReservationSummary::new)
                .toList();
    }

    /**
     * 방문 완료 했지만 피드백 작성하지 않은 예약 리스트 반환
     * @param customerId 고객 ID
     * @return 예약 요약 DTO 리스트
     */
    @Transactional
    public List<ReservationDto.ReservationSummary> getUnreviewedReservations(Long customerId) {
        return reservationRepository.findUnreviewedReservationsByUserId(customerId)
                .stream()
                .map(ReservationDto.ReservationSummary::new)
                .toList();
    }
}
