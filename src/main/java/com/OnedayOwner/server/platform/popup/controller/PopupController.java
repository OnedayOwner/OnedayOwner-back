package com.OnedayOwner.server.platform.popup.controller;

import com.OnedayOwner.server.global.exception.BusinessException;
import com.OnedayOwner.server.global.security.SecurityUtils;
import com.OnedayOwner.server.platform.popup.dto.PopupDto;
import com.OnedayOwner.server.platform.popup.service.PopupService;
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

    @PostMapping("/register")
    public ResponseEntity<PopupDto.PopupInBusinessDetail> registerPopup(
        @RequestBody PopupDto.PopupRestaurantForm form,
        SecurityContextHolderAwareRequestWrapper request
    ) {
        Long ownerId = SecurityUtils.extractUserId(request);
        return ResponseEntity.ok()
            .body(popupService.registerPopup(form, ownerId));
    }

    @PostMapping("/{popupId}/menu")
    public ResponseEntity<PopupDto.MenuDetail> registerMenu(
        PopupDto.MenuForm form,
        @PathVariable("popupId")Long popupId
    ) {
        return ResponseEntity.ok()
            .body(popupService.registerMenu(form, popupId));
    }

    @GetMapping("")
    public ResponseEntity<PopupDto.PopupInBusinessDetail> getPopupInBusiness(
            SecurityContextHolderAwareRequestWrapper request
    ) {
        Long ownerId = SecurityUtils.extractUserId(request);
        return ResponseEntity.ok()
            .body(popupService.getPopupInBusinessDetail(ownerId));
    }

    @GetMapping("/history/{popupId}")
    public ResponseEntity<PopupDto.PopupHistoryDetail> getPopupDetail(
            @PathVariable("popupId")Long popupId
            ) {
        return ResponseEntity.ok()
            .body(popupService.getPopupHistoryDetail(popupId));
    }

    @GetMapping("/popup/history/list")
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
