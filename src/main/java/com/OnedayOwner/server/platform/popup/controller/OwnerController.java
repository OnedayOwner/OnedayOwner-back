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

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/owners")
public class OwnerController {

    private final PopupService popupService;

    @PostMapping("/popup/register")
    public ResponseEntity<PopupDto.PopupInBusinessDetail> registerPopup(
        @RequestBody PopupDto.PopupRestaurantForm form,
        SecurityContextHolderAwareRequestWrapper request
    ) {
        Long ownerId = SecurityUtils.extractUserId(request);
        return ResponseEntity.ok()
            .body(popupService.registerPopup(form, ownerId));
    }

    @PostMapping("/popup/{popupId}/menu")
    public ResponseEntity<PopupDto.MenuDetail> registerMenu(
        PopupDto.MenuForm form,
        @PathVariable("popupId")Long popupId
    ) {
        return ResponseEntity.ok()
            .body(popupService.registerMenu(form, popupId));
    }

    @GetMapping("/popup")
    public ResponseEntity<PopupDto.PopupInBusinessDetail> getPopupInBusiness(
            SecurityContextHolderAwareRequestWrapper request
    ) {
        Long ownerId = SecurityUtils.extractUserId(request);
        return ResponseEntity.ok()
            .body(popupService.getPopupInBusinessDetail(ownerId));
    }

    @GetMapping("/popup/history/{popupId}")
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
    @PostMapping("/popup/{popupId}/delete")
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

}
