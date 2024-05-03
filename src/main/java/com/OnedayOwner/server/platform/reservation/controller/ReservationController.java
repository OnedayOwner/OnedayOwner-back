package com.OnedayOwner.server.platform.reservation.controller;

import com.OnedayOwner.server.platform.reservation.dto.ReservationDto;
import com.OnedayOwner.server.platform.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    /*
    팝업의 예약 가능 일자 조회
     */
    @GetMapping("/schedule/{popupId}")
    public ResponseEntity<ReservationDto.ReservationPossibleTimesDto> getPossibleTimesForReservation(
            @PathVariable("popupId")Long popupId
    ) {
        return ResponseEntity.ok()
                .body(reservationService.getPossibleTimesForReservation(popupId));
    }

    /*
    새로운 예약 등록
    예약 상세 내역을 반환
     */
    @PostMapping("/register")
    public ResponseEntity<ReservationDto.ReservationDetailForCustomer> registerReservation(
            @RequestBody ReservationDto.ReservationForm reservationForm,
            Long customerId
    ) {
        return ResponseEntity.ok()
                .body(reservationService.registerReservation(reservationForm, customerId));
    }
}
