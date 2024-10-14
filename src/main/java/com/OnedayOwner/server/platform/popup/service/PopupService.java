package com.OnedayOwner.server.platform.popup.service;

import com.OnedayOwner.server.global.exception.BusinessException;
import com.OnedayOwner.server.global.exception.ErrorCode;
import com.OnedayOwner.server.platform.Address;
import com.OnedayOwner.server.platform.popup.dto.PopupDto;
import com.OnedayOwner.server.platform.popup.entity.BusinessTime;
import com.OnedayOwner.server.platform.popup.entity.Menu;
import com.OnedayOwner.server.platform.popup.entity.PopupRestaurant;
import com.OnedayOwner.server.platform.popup.repository.BusinessTimeRepository;
import com.OnedayOwner.server.platform.popup.repository.MenuRepository;
import com.OnedayOwner.server.platform.popup.repository.PopupRestaurantRepository;
import com.OnedayOwner.server.platform.reservation.entity.QReservationMenu;
import com.OnedayOwner.server.platform.reservation.entity.ReservationTime;
import com.OnedayOwner.server.platform.reservation.repository.ReservationMenuRepository;
import com.OnedayOwner.server.platform.reservation.repository.ReservationRepository;
import com.OnedayOwner.server.platform.reservation.repository.ReservationTimeRepository;
import com.OnedayOwner.server.platform.user.entity.Role;
import com.OnedayOwner.server.platform.user.repository.UserRepository;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.OnedayOwner.server.platform.reservation.entity.QReservation.reservation;
import static com.OnedayOwner.server.platform.reservation.entity.QReservationMenu.reservationMenu;

@Service
@RequiredArgsConstructor
public class PopupService {

    private final PopupRestaurantRepository popupRestaurantRepository;
    private final MenuRepository menuRepository;
    private final UserRepository userRepository;
    private final BusinessTimeRepository businessTimeRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationMenuRepository reservationMenuRepository;


    @Transactional(noRollbackFor = BusinessException.class)
    public PopupDto.PopupInBusinessDetail registerPopup(
            PopupDto.PopupRestaurantForm restaurantForm,
            Long ownerId){
        Optional<PopupRestaurant> prevRestaurant = popupRestaurantRepository.findByUserIdAndInBusiness(ownerId, true);
        if (prevRestaurant.isPresent()) {
            if (prevRestaurant.get().getEndDateTime().isAfter(LocalDateTime.now())) {//아직 진행중인 팝업이 있는경우
                throw new BusinessException(ErrorCode.POPUP_ALREADY_IN_BUSINESS);
            }
            else{
                prevRestaurant.get().close();
            }
        }
        if (restaurantForm.getStartDateTime().isBefore(LocalDateTime.now())) {
            throw new BusinessException(ErrorCode.POPUP_START_DATETIME_INVALID);
        }
        //레스토랑 등록
        PopupRestaurant restaurant = popupRestaurantRepository.save(PopupRestaurant.builder()
                .name(restaurantForm.getName())
                .description(restaurantForm.getDescription())
                .startDateTime(restaurantForm.getStartDateTime())
                .endDateTime(restaurantForm.getEndDateTime())
                .address(Address.builder()
                        .zipcode(restaurantForm.getAddress().getZipcode())
                        .street(restaurantForm.getAddress().getStreet())
                        .detail(restaurantForm.getAddress().getDetail())
                        .build())
                .user(userRepository.findByIdAndRole(ownerId, Role.OWNER).orElseThrow(
                        () -> new BusinessException(ErrorCode.OWNER_NOT_FOUND)
                ))
                .build());

        registerMenus(restaurantForm.getMenuForms(), restaurant.getId());//메뉴 등록

        restaurantForm.getBusinessTimes().forEach(o -> {
            BusinessTime businessTime = BusinessTime.builder()
                    .openTime(o.getOpenTime())
                    .closeTime(o.getCloseTime())
                    .popupRestaurant(restaurant)
                    .build();
            businessTimeRepository.save(businessTime);//영업시간 등록
            registerReservationTime(o, restaurant);//예약시간 등록
        });

        return PopupDto.PopupInBusinessDetail.builder()
                .popupRestaurant(restaurant)
                .build();

    }

    @Transactional(noRollbackFor = BusinessException.class)
    public PopupDto.PopupInBusinessDetail getPopupInBusinessDetail(Long ownerId){
        PopupRestaurant popupRestaurant = popupRestaurantRepository.getInBusinessPopupRestaurantWithMenusAndReservationTimesAndBusinessTimesByUserId(ownerId)
                .orElseThrow(
                        ()-> new BusinessException(ErrorCode.IN_BUSINESS_POPUP_NOT_FOUND)
                );
        if (popupRestaurant.getEndDateTime().isBefore(LocalDateTime.now())){
            popupRestaurant.close();
            throw new BusinessException(ErrorCode.POPUP_CLOSED);
        }
        List<Long[]> list = reservationRepository.sumNumberOfPeopleByPopupRestaurantId(popupRestaurant.getId());

        Long[] result = list.get(0);
        Long totalReservation = result[0];
        Long totalReservationPeople = result[1];

        return new PopupDto.PopupInBusinessDetail(popupRestaurant, totalReservation, totalReservationPeople);
    }

    @Transactional
    public PopupDto.PopupHistoryDetail getPopupHistoryDetail(Long ownerId, Long popupId){
        PopupRestaurant popupRestaurant = popupRestaurantRepository.getPopupRestaurantWithMenusById(popupId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POPUP_NOT_FOUND));
        if (!popupRestaurant.getUser().getId().equals(ownerId)) {
            throw new BusinessException(ErrorCode.POPUP_AND_USER_NOT_MATCH);
        }
        return new PopupDto.PopupHistoryDetail(popupRestaurant);
    }

    @Transactional
    public void registerReservationTime(PopupDto.BusinessTimeForm form, PopupRestaurant restaurant){
        LocalDate startDate = restaurant.getStartDateTime().toLocalDate();
        LocalDate endDate = restaurant.getEndDateTime().toLocalDate();

        Stream.iterate(startDate, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(startDate, endDate) + 1)
                .forEach(date -> {
                    long minutesBetween = ChronoUnit.MINUTES.between(form.getOpenTime(), form.getCloseTime());
                    long numberOfSlots = minutesBetween / form.getReservationTimeUnit();

                    for (int i = 0; i < numberOfSlots; i++) {
                        LocalTime slotStartTime = form.getOpenTime().plusMinutes((i * form.getReservationTimeUnit()));
                        LocalTime slotEndTime = slotStartTime.plusMinutes(form.getReservationTimeUnit());

                        LocalDateTime startDateTime = LocalDateTime.of(date, slotStartTime);
                        LocalDateTime endDateTime = LocalDateTime.of(date, slotEndTime);
                        if(startDateTime.isBefore(restaurant.getStartDateTime())){
                            continue;
                        }
                        if(endDateTime.isAfter(restaurant.getEndDateTime())){
                            break;
                        }

                        reservationTimeRepository.save(ReservationTime.builder()
                                .popupRestaurant(restaurant)
                                .reservationDate(date)
                                .startTime(slotStartTime)
                                .endTime(slotEndTime)
                                .maxPeople(form.getMaxPeoplePerTime())
                                .build());
                    }
                });
    }

    @Transactional
    public PopupDto.MenuDetail registerMenu(Long ownerId, PopupDto.MenuForm menuForm, Long popupId){
        PopupRestaurant popupRestaurant = popupRestaurantRepository.findById(popupId).orElseThrow(
                () -> new BusinessException(ErrorCode.POPUP_NOT_FOUND));
        if (!popupRestaurant.getUser().getId().equals(ownerId)) {
            throw new BusinessException(ErrorCode.POPUP_AND_USER_NOT_MATCH);
        }
        return new PopupDto.MenuDetail(menuRepository.save(Menu.builder()
                .name(menuForm.getName())
                .price(menuForm.getPrice())
                .description(menuForm.getDescription())
                .popupRestaurant(popupRestaurant)
                .build()));
    }

    @Transactional
    public List<PopupDto.MenuDetail> registerMenus(List<PopupDto.MenuForm> menuForms, Long popupId){
        return menuForms.stream()
                .map(menuForm -> menuRepository.save(Menu.builder()
                    .name(menuForm.getName())
                    .price(menuForm.getPrice())
                    .description(menuForm.getDescription())
                    .popupRestaurant(popupRestaurantRepository.findById(popupId).orElseThrow(
                            () -> new BusinessException(ErrorCode.POPUP_NOT_FOUND)
                    ))
                    .build()))
                .toList()
                .stream()
                .map(PopupDto.MenuDetail::new)
                .toList();
    }

    @Transactional
    public List<PopupDto.PopupSummary> getPopupHistoryByOwner(Long ownerId){
        return popupRestaurantRepository.findAllByUserIdAndInBusiness(ownerId, false)
                .stream()
                .map(PopupDto.PopupSummary::new)
                .toList();
    }


    //팝업 리스트 조회
    @Transactional
    public PopupRestaurant getPopupInBusinessByOwner(Long ownerId){
        return popupRestaurantRepository.findByUserIdAndInBusiness(ownerId, true)
                .orElseThrow(
                        () -> new BusinessException(ErrorCode.IN_BUSINESS_POPUP_NOT_FOUND)
                );
    }

    //고객의 현재 진행중인 팝업 리스트 조회
    @Transactional
    public List<PopupDto.PopupSummaryForCustomer> getActivePopupsInBusinessForCustomer(){
        return popupRestaurantRepository.findActivePopupRestaurantsWithMenus()
                .stream()
                .map(PopupDto.PopupSummaryForCustomer::new)
                .toList();
    }


    /**
     * 팝업 삭제
     * @param ownerId
     * @param popupId
     */
    @Transactional
    public void deletePopup(Long ownerId, Long popupId){
        PopupRestaurant popupRestaurant = popupRestaurantRepository.findById(popupId)
                .orElseThrow(
                        () -> new BusinessException(ErrorCode.POPUP_NOT_FOUND)
                );

        if(!popupRestaurant.getUser().getId().equals(ownerId)) {
            throw new BusinessException(ErrorCode.POPUP_AND_USER_NOT_MATCH);
        }

        popupRestaurantRepository.deleteByUserIdAndId(ownerId, popupId);
    }

    @Transactional
    public void closePopup(Long ownerId, Long popupId){
        PopupRestaurant popupRestaurant = popupRestaurantRepository.findById(popupId)
                .orElseThrow(
                        () -> new BusinessException(ErrorCode.POPUP_NOT_FOUND)
                );

        if(!popupRestaurant.getUser().getId().equals(ownerId)) {
            throw new BusinessException(ErrorCode.POPUP_AND_USER_NOT_MATCH);
        }
        popupRestaurant.close();
    }

    @Transactional
    public List<PopupDto.ReservationInfoForOwnerSummary> monthlyReservationInfo(
            Long ownerId, Long popupId, int year, int month
    ){
        if(!popupRestaurantRepository.findById(popupId).orElseThrow(
                () -> new BusinessException(ErrorCode.POPUP_NOT_FOUND)
        ).getUser().getId().equals(ownerId)){
            throw new BusinessException(ErrorCode.POPUP_AND_USER_NOT_MATCH);
        }
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        List<Tuple> results = reservationRepository.getMonthlyReservationInfo(startDate, endDate, popupId);

        List<PopupDto.ReservationInfoForOwnerSummary> info = new ArrayList<>();

        for (Tuple tuple : results) {
            int dayOfMonth = tuple.get(reservation.reservationDateTime.dayOfMonth()); // "일(day)" 값
            LocalDate date = LocalDate.of(year, month, dayOfMonth);
            long reservationCount = tuple.get(reservation.count()); // 예약 수
            long totalPeople = tuple.get(reservation.numberOfPeople.sum()); // 총 인원 수

            info.add(new PopupDto.ReservationInfoForOwnerSummary(date, reservationCount, totalPeople));
        }

        return info;
    }

    @Transactional
    public List<PopupDto.ReservationMenuCount> dailyReservationMenuCount(
        Long ownerId,
        Long popupId,
        LocalDate date
    ){
        if(!popupRestaurantRepository.findById(popupId).orElseThrow(
                () -> new BusinessException(ErrorCode.POPUP_NOT_FOUND)
        ).getUser().getId().equals(ownerId)){
            throw new BusinessException(ErrorCode.POPUP_AND_USER_NOT_MATCH);
        }

        List<Tuple> results = reservationMenuRepository.getDailyMenuCountGroupByReservationTime(popupId,date);
        List<PopupDto.ReservationMenuCount> menuCountList = new ArrayList<>();
        for(Tuple tuple : results){
            LocalDateTime dateTime = tuple.get(reservationMenu.reservation.reservationDateTime);
            String menuName = tuple.get(reservationMenu.menu.name);
            int quantity = tuple.get(reservationMenu.quantity.sum());

            menuCountList.add(new PopupDto.ReservationMenuCount(dateTime, menuName, quantity));
        }

        return menuCountList;
    }

  
    //고객의 진행 예정인 팝업 리스트 조회
    @Transactional
    public List<PopupDto.PopupSummaryForCustomer> getFuturePopupsInBusinessForCustomer(){
        return popupRestaurantRepository.findFuturePopupRestaurantsWithMenus()
                .stream()
                .map(PopupDto.PopupSummaryForCustomer::new)
                .toList();
    }

    //고객의 팝업 상세 조회
    @Transactional
    public PopupDto.PopupDetailForCustomer getPopupDetailForCustomer(Long popupId){
        return popupRestaurantRepository.getPopupRestaurantWithMenusById(popupId)
                .map(PopupDto.PopupDetailForCustomer::new)
                .orElseThrow(
                        () -> new BusinessException(ErrorCode.POPUP_NOT_FOUND)
                );
    }

}
