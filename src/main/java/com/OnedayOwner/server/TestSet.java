package com.OnedayOwner.server;

import com.OnedayOwner.server.platform.popup.dto.PopupDto;
import com.OnedayOwner.server.platform.popup.service.PopupService;
import com.OnedayOwner.server.platform.user.entity.Gender;
import com.OnedayOwner.server.platform.user.entity.Role;
import com.OnedayOwner.server.platform.user.entity.User;
import com.OnedayOwner.server.platform.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional
public class TestSet {

    private final PopupService popupService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @PostConstruct
    public void setUp() {
        User owner = User.builder()
                .name("김은학")
                .birth(LocalDate.of(2000, 5, 29))
                .email("acky529@gmail.com")
                .gender(Gender.MALE)
                .role(Role.OWNER)
                .loginId("acky529")
                .phoneNumber("01092099175")
                .password(passwordEncoder.encode("1234"))
                .build();
        User customer = User.builder()
                .name("김은학")
                .birth(LocalDate.of(2000, 5, 29))
                .email("529acky@naver.com")
                .gender(Gender.MALE)
                .role(Role.CUSTOMER)
                .loginId("529acky")
                .phoneNumber("01030079841")
                .password(passwordEncoder.encode("1234"))
                .build();
        userRepository.save(owner);
        userRepository.save(customer);

        // 현재 진행 중인 팝업 3개 생성
        createPopupRestaurant(owner, "스타벅스 논현점", LocalDateTime.now().minusDays(3), LocalDateTime.now().plusDays(3));
        createPopupRestaurant(owner, "홍콩반점 역삼역점", LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(5));
        createPopupRestaurant(owner, "요아정 서초점", LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(7));

        // 진행 예정인 팝업 3개 생성
        createPopupRestaurant(owner, "이디야 이태원점", LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(10));
        createPopupRestaurant(owner, "설빙 명동점", LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(12));
        createPopupRestaurant(owner, "버거킹 종로점", LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(15));
    }

    private void createPopupRestaurant(User owner, String name, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        PopupDto.BusinessTimeForm timeForm = PopupDto.BusinessTimeForm.builder()
                .openTime(LocalTime.of(11, 0))
                .closeTime(LocalTime.of(13, 0))
                .reservationTimeUnit(30)
                .maxPeoplePerTime(10)
                .build();

        PopupDto.MenuForm menuForm1 = PopupDto.MenuForm.builder()
                .price(10000)
                .description("직접 두바이에서 공수한 카다이프를 사용합니다.")
                .name("두바이 초콜릿")
                .build();

        PopupDto.MenuForm menuForm2 = PopupDto.MenuForm.builder()
                .price(20000)
                .description("직접 만든 요거트입니다.")
                .name("요거트 아이스크림")
                .build();

        PopupDto.AddressForm addressForm = PopupDto.AddressForm.builder()
                .street("서울시 강남구 강남대로 512")
                .zipcode("12345")
                .detail("3층")
                .build();

        PopupDto.PopupRestaurantForm restaurantForm = PopupDto.PopupRestaurantForm.builder()
                .name(name)
                .businessTimes(List.of(timeForm))
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .description(name + " 팝업 설명입니다.")
                .address(addressForm)
                .menuForms(List.of(menuForm1, menuForm2))
                .build();

        popupService.registerPopup(restaurantForm, owner.getId());
    }
}
