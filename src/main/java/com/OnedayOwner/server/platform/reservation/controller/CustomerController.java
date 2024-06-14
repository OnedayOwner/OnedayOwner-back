package com.OnedayOwner.server.platform.reservation.controller;

import com.OnedayOwner.server.platform.popup.dto.PopupDto;
import com.OnedayOwner.server.platform.popup.service.PopupService;
import com.OnedayOwner.server.platform.reservation.dto.ReservationDto;
import com.OnedayOwner.server.platform.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customers")
public class CustomerController {

    private final ReservationService reservationService;
    private final PopupService popupService;

    /*
    팝업의 예약 가능 일자 조회
     */
    @GetMapping("/schedule/{popupId}")
    public ResponseEntity<ReservationDto.ReservationTimesDto> getPossibleTimesForReservation(
            @PathVariable("popupId") Long popupId
    ) {
        return ResponseEntity.ok()
                .body(reservationService.getReservationTimes(popupId));
    }

    /*
    새로운 예약 등록
     */
    @PostMapping("/reservation/register")
    public ResponseEntity<ReservationDto.ReservationDetail> registerReservation(
            @RequestBody ReservationDto.ReservationForm reservationForm,
            Long customerId
    ) {
        return ResponseEntity.ok()
                .body(reservationService.registerReservation(reservationForm, customerId));
    }

    /*
    예약 상세 조회
     */
    @GetMapping("/reservation/{reservationId}")
    public ResponseEntity<ReservationDto.ReservationDetail> getReservationDetailForCustomer(
            @PathVariable("reservationId") Long reservationId,
            Long customerId
    ) {
        return ResponseEntity.ok()
                .body(reservationService.getReservationDetailForCustomer(reservationId, customerId));
    }

    /*
    팝업 리스트 조회
     */
    @GetMapping("/popups")
    public ResponseEntity<List<PopupDto.PopupSummaryForCustomer>> getPopupsInBusinessForCustomer(
            Long customerId
    ){
        return  ResponseEntity.ok()
                .body(popupService.getPopupsInBusinessForCustomer());
    }
}
