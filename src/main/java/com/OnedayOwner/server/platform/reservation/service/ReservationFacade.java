package com.OnedayOwner.server.platform.reservation.service;

import com.OnedayOwner.server.global.exception.BusinessException;
import com.OnedayOwner.server.global.exception.ErrorCode;
import com.OnedayOwner.server.platform.reservation.dto.ReservationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationFacade {

    private final RedissonClient redissonClient;
    private final ReservationService reservationService;

    /**
     * 예약 등록 (예약의 동시성 제어를 위해서 Redisson을 활용, 본 메소드에서는 동시성 제어만)
     * @param reservationForm 예약 등록 DTO
     * @param customerId 고객 ID
     * @return 예약 정보를 담은 DTO
     */
    public ReservationDto.ReservationDetail registerReservation(
            ReservationDto.ReservationForm reservationForm,
            Long customerId
    ) {
        RLock lock = redissonClient.getLock(reservationForm.getReservationTimeId().toString());
        boolean available = false;
        try {
            // 락 대기 시간 5초, 락 유지 시간 5초 설정
            available = lock.tryLock(5, 5, TimeUnit.SECONDS);
            if (!available) {
                log.info("Redisson GetLock Timeout {}", reservationForm.getReservationTimeId());
                throw new BusinessException(ErrorCode.RESERVATION_TIME_OUT);
            }

            log.info("Redisson GetLock {}", reservationForm.getReservationTimeId());

            return reservationService.executeReservation(reservationForm, customerId);

        } catch (InterruptedException e) {
            throw new BusinessException(ErrorCode.RESERVATION_TIME_OUT);
        } finally {
            if (available) {
                lock.unlock();
                log.info("Redisson UnLock {}", reservationForm.getReservationTimeId());
            }
        }
    }
}
