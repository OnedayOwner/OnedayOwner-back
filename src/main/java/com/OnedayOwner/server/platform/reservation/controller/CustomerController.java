package com.OnedayOwner.server.platform.reservation.controller;

import com.OnedayOwner.server.global.security.SecurityUtils;
import com.OnedayOwner.server.platform.popup.dto.PopupDto;
import com.OnedayOwner.server.platform.popup.service.PopupService;
import com.OnedayOwner.server.platform.reservation.dto.ReservationDto;
import com.OnedayOwner.server.platform.reservation.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "팝업 예약 정보 조회",
        description = "팝업 예약 가능 일자 및 시간 조회")
    @GetMapping("/info/{popupId}")
    public ResponseEntity<ReservationDto.ReservationInfoDto> getReservationInfo(
            @PathVariable("popupId") Long popupId
    ) {
        return ResponseEntity.ok()
                .body(reservationService.getReservationInfo(popupId));
    }

    @Operation(summary = "팝업 메뉴 조회")
    @GetMapping("/menu/{popupId}")
    public ResponseEntity<ReservationDto.ReservationMenuDto> getReservationMenus(
            @PathVariable("popupId") Long popupId
    ) {
        return ResponseEntity.ok()
                .body(reservationService.getReservationMenus(popupId));
    }

    @Operation(summary = "예약 등록")
    @PostMapping("/reservation/register")
    public ResponseEntity<ReservationDto.ReservationDetail> registerReservation(
            @RequestBody ReservationDto.ReservationForm reservationForm,
            SecurityContextHolderAwareRequestWrapper request
    ) {
        Long customerId = SecurityUtils.extractUserId(request);
        return ResponseEntity.ok()
                .body(reservationService.registerReservation(reservationForm, customerId));
    }

    @Operation(summary = "예약 상세 조회")
    @GetMapping("/reservation/{reservationId}")
    public ResponseEntity<ReservationDto.ReservationDetailForUser> getReservationDetailForCustomer(
            @PathVariable("reservationId") Long reservationId,
            SecurityContextHolderAwareRequestWrapper request
    ) {
        Long customerId = SecurityUtils.extractUserId(request);
        return ResponseEntity.ok()
                .body(reservationService.getReservationDetailForCustomer(reservationId, customerId));
    }

    @Operation(summary = "진행중인 팝업 조회")
    @GetMapping("/popups/active")
    public ResponseEntity<List<PopupDto.PopupSummaryForCustomer>> getActivePopupsInBusinessForCustomer(){
        return  ResponseEntity.ok()
                .body(popupService.getActivePopupsInBusinessForCustomer());
    }

    @Operation(summary = "진행 예정 팝업 조회")
    @GetMapping("/popups/future")
    public ResponseEntity<List<PopupDto.PopupSummaryForCustomer>> getFuturePopupsInBusinessForCustomer(){
        return  ResponseEntity.ok()
                .body(popupService.getFuturePopupsInBusinessForCustomer());
    }

    @Operation(summary = "방문 예정 예약 조회")
    @GetMapping("/reservations/upcoming")
    public ResponseEntity<List<ReservationDto.ReservationSummary>> getUpcomingReservations(
            SecurityContextHolderAwareRequestWrapper request
    ){
        Long customerId = SecurityUtils.extractUserId(request);
        return  ResponseEntity.ok()
                .body(reservationService.getUpcomingReservations(customerId));
    }

    @Operation(summary = "방문 완료 예약 조회",
            description = "")
    @GetMapping("/reservations/completed")
    public ResponseEntity<List<ReservationDto.ReservationSummary>> getCompletedReservations(
            SecurityContextHolderAwareRequestWrapper request
    ){
        Long customerId = SecurityUtils.extractUserId(request);
        return  ResponseEntity.ok()
                .body(reservationService.getCompletedReservations(customerId));
    }

    @Operation(summary = "피드백 작성하지 않은 예약 조회",
            description = "방문했지만 피드백 작성하지 않은 예약 리스트")
    @GetMapping("/reservations/completed/unreviewed")
    public ResponseEntity<List<ReservationDto.ReservationSummary>> getUnreviewedReservations(
            SecurityContextHolderAwareRequestWrapper request
    ){
        Long customerId = SecurityUtils.extractUserId(request);
        return  ResponseEntity.ok()
                .body(reservationService.getUnreviewedReservations(customerId));
    }

    @Operation(summary = "팝업 상세 조회")
    @GetMapping("/popup/{popupId}")
    public ResponseEntity<PopupDto.PopupDetailForCustomer> getPopupDetailForCustomer(
            @PathVariable("popupId")Long popupId
    ) {
        return ResponseEntity.ok()
                .body(popupService.getPopupDetailForCustomer(popupId));
    }
}
