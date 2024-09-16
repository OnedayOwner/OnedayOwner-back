package com.OnedayOwner.server.platform.reservation.controller;

import com.OnedayOwner.server.global.security.SecurityUtils;
import com.OnedayOwner.server.platform.popup.dto.PopupDto;
import com.OnedayOwner.server.platform.popup.service.PopupService;
import com.OnedayOwner.server.platform.reservation.dto.ReservationDto;
import com.OnedayOwner.server.platform.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
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
    public ResponseEntity<List<ReservationDto.ReservationTimeDto>> getPossibleTimesForReservation(
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
            SecurityContextHolderAwareRequestWrapper request
    ) {
        Long customerId = SecurityUtils.extractUserId(request);
        return ResponseEntity.ok()
                .body(reservationService.registerReservation(reservationForm, customerId));
    }

    /*
    예약 상세 조회
     */
    @GetMapping("/reservation/{reservationId}")
    public ResponseEntity<ReservationDto.ReservationDetail> getReservationDetailForCustomer(
            @PathVariable("reservationId") Long reservationId,
            SecurityContextHolderAwareRequestWrapper request
    ) {
        Long customerId = SecurityUtils.extractUserId(request);
        return ResponseEntity.ok()
                .body(reservationService.getReservationDetailForCustomer(reservationId, customerId));
    }

    /*
    현재 진행중인 팝업 리스트 조회
     */
    @GetMapping("/popups/active")
    public ResponseEntity<List<PopupDto.PopupSummaryForCustomer>> getActivePopupsInBusinessForCustomer(){
        return  ResponseEntity.ok()
                .body(popupService.getActivePopupsInBusinessForCustomer());
    }

    /*
    현재 진행중인 팝업 리스트 조회
     */
    @GetMapping("/popups/future")
    public ResponseEntity<List<PopupDto.PopupSummaryForCustomer>> getFuturePopupsInBusinessForCustomer(){
        return  ResponseEntity.ok()
                .body(popupService.getFuturePopupsInBusinessForCustomer());
    }

    /*
    예약 리스트 조회
     */
    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationDto.ReservationSummary>> getReservationsByCustomer(
            SecurityContextHolderAwareRequestWrapper request
    ){
        Long customerId = SecurityUtils.extractUserId(request);
        return  ResponseEntity.ok()
                .body(reservationService.getReservationsByCustomer(customerId));
    }
}
