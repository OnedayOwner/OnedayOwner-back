package com.OnedayOwner.server.platform.popup.controller;

import com.OnedayOwner.server.platform.popup.dto.PopupDto;
import com.OnedayOwner.server.platform.popup.service.PopupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/popups")
public class PopupController {

    private final PopupService popupService;

    @PostMapping("/register")
    public ResponseEntity<PopupDto.PopupInBusinessDetail> registerPopup(
        PopupDto.PopupRestaurantForm form,
        PopupDto.AddressForm addressForm,
        List<PopupDto.MenuForm> menuForms,
        Long ownerId
    ) {
        return ResponseEntity.ok()
            .body(popupService.registerPopup(form, addressForm, menuForms, ownerId));
    }

    @PostMapping("/{popupId}/menu")
    public ResponseEntity<PopupDto.MenuDetail> registerMenu(
        PopupDto.MenuForm form,
        @PathVariable("popupId")Long popupId
    ) {
        return ResponseEntity.ok()
            .body(popupService.registerMenu(form, popupId));
    }

    @GetMapping("/popup")
    public ResponseEntity<PopupDto.PopupInBusinessDetail> getPopupInBusiness(
        Long ownerId
    ) {
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

    @GetMapping("/history/list")
    public ResponseEntity<List<PopupDto.PopupSummary>> getPopupHistory(
            Long ownerId
    ) {
        return ResponseEntity.ok()
            .body(popupService.getPopupHistoryByOwner(ownerId));
    }

}