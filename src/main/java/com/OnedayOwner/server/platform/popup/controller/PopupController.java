package com.OnedayOwner.server.platform.popup.controller;

import com.OnedayOwner.server.global.exception.BusinessException;
import com.OnedayOwner.server.global.security.SecurityUtils;
import com.OnedayOwner.server.platform.popup.dto.PopupDto;
import com.OnedayOwner.server.platform.popup.service.PopupService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/owners/popup")
public class PopupController {

    private final PopupService popupService;

    @Operation(summary = "팝업 등록")
    @PostMapping("/register")
    public ResponseEntity<PopupDto.PopupInBusinessDetail> registerPopup(
        @RequestBody PopupDto.PopupRestaurantForm form,
        SecurityContextHolderAwareRequestWrapper request
    ) {
        Long ownerId = SecurityUtils.extractUserId(request);
        return ResponseEntity.ok()
            .body(popupService.registerPopup(form, ownerId));
    }

    @Operation(summary = "팝업 메뉴 등록")
    @PostMapping("/{popupId}/menu")
    public ResponseEntity<PopupDto.MenuDetail> registerMenu(
            SecurityContextHolderAwareRequestWrapper request,
            PopupDto.MenuForm form,
            @PathVariable("popupId")Long popupId
    ) {
        Long ownerId = SecurityUtils.extractUserId(request);
        return ResponseEntity.ok()
            .body(popupService.registerMenu(ownerId, form, popupId));
    }

    @Operation(summary = "팝업 조회",
            description = "현재 진행중인 팝업 조회," +
                    "조회 시점에 팝업 종료됐을 시 close 처리됨")
    @GetMapping("")
    public ResponseEntity<PopupDto.PopupInBusinessDetail> getPopupInBusiness(
            SecurityContextHolderAwareRequestWrapper request
    ) {
        Long ownerId = SecurityUtils.extractUserId(request);
        return ResponseEntity.ok()
            .body(popupService.getPopupInBusinessDetail(ownerId));
    }

    @Operation(summary = "과거 팝업 상세 조회",
            description = "과거 진행 팝업 list에서 팝업 선택 시 세부정보(detail) qksghks")
    @GetMapping("/history/{popupId}")
    public ResponseEntity<PopupDto.PopupHistoryDetail> getPopupDetail(
            @PathVariable("popupId")Long popupId,
            SecurityContextHolderAwareRequestWrapper request
            ) {
        Long ownerId = SecurityUtils.extractUserId(request);
        return ResponseEntity.ok()
            .body(popupService.getPopupHistoryDetail(ownerId, popupId));
    }

    @Operation(summary = "과거 팝업 조회",
            description = "과거 진행 팝업 summary 정보 반환")
    @GetMapping("/history/list")
    public ResponseEntity<List<PopupDto.PopupSummary>> getPopupHistory(
            SecurityContextHolderAwareRequestWrapper request
    ) {
        Long ownerId = SecurityUtils.extractUserId(request);
        return ResponseEntity.ok()
            .body(popupService.getPopupHistoryByOwner(ownerId));
    }


    /**
     * 팝업 삭제 API
     * @param request
     * @param popupId
     * @return
     */
    @Operation(summary = "팝업 삭제")
    @PostMapping("/{popupId}/delete")
    public ResponseEntity<?> deletePopup(
            SecurityContextHolderAwareRequestWrapper request,
            @PathVariable("popupId")Long popupId
    ){
        Long ownerId = SecurityUtils.extractUserId(request);
        try{
            popupService.deletePopup(ownerId,popupId);
            return ResponseEntity.noContent().build();
        } catch (BusinessException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "팝업 종료")
    @PostMapping("/{popupId}/close")
    public ResponseEntity<?> closePopup(
            SecurityContextHolderAwareRequestWrapper request,
            @PathVariable("popupId")Long popupId
    ){
        Long ownerId = SecurityUtils.extractUserId(request);
        try{
            popupService.closePopup(ownerId,popupId);
            return ResponseEntity.noContent().build();
        } catch (BusinessException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "월 날짜별 예약 정보",
            description = "팝업 정보에서 예약 정보 조회 시 월 날짜별 예약수, 예약인원수 반환")
    @PostMapping("/{popupId}/reservation/month")
    public ResponseEntity<List<PopupDto.ReservationInfoForOwnerSummary>> monthlyReservationInfo(
            SecurityContextHolderAwareRequestWrapper request,
            @PathVariable("popupId")Long popupId,
            @RequestParam("year") int year,
            @RequestParam("month") int month
    ){
        Long ownerId = SecurityUtils.extractUserId(request);
        return ResponseEntity.ok()
                .body(popupService.monthlyReservationInfo(ownerId, popupId, year, month));
    }

    @Operation(summary = "날짜 시간별 메뉴 수량 조회",
            description = "예약 조회 달력에서 날짜 선택 시 해당 날짜의 시간별 메뉴 수량 반환")
    @PostMapping("/{popupId}/reservation/day")
    public ResponseEntity<List<PopupDto.ReservationMenuCount>> dailyMenuCount(
        SecurityContextHolderAwareRequestWrapper request,
        @PathVariable("popupId")Long popupId,
        @RequestParam("year") int year,
        @RequestParam("month") int month,
        @RequestParam("day") int day
    ){
        Long ownerId = SecurityUtils.extractUserId(request);
        LocalDate date = LocalDate.of(year, month, day);

        return ResponseEntity.ok()
                .body(popupService.dailyReservationMenuCount(ownerId, popupId, date));
    }
}
