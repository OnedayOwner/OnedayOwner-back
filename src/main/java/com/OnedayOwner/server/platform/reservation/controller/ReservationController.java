package com.OnedayOwner.server.platform.reservation.controller;

import com.OnedayOwner.server.platform.reservation.dto.ReservationDto;
import com.OnedayOwner.server.platform.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    /*
    고객 입장에서 팝업 상세 및 예약 화면
     */
    @GetMapping("/{popupId}")
    public ResponseEntity<ReservationDto.PopupDetailForReservationDto> getPopupDetailForReservation(
            @PathVariable("popupId")Long popupId
    ) {
        return ResponseEntity.ok()
                .body(reservationService.getPopupDetailForReservation(popupId));
    }
}
