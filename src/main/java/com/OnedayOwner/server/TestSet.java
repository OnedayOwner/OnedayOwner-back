//package com.OnedayOwner.server;
//
//import com.OnedayOwner.server.platform.popup.dto.PopupDto;
//import com.OnedayOwner.server.platform.popup.repository.MenuRepository;
//import com.OnedayOwner.server.platform.popup.service.PopupService;
//import com.OnedayOwner.server.platform.user.entity.Gender;
//import com.OnedayOwner.server.platform.user.entity.Role;
//import com.OnedayOwner.server.platform.user.entity.User;
//import com.OnedayOwner.server.platform.user.repository.UserRepository;
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.util.List;
//
//@Component
//@RequiredArgsConstructor
//@Transactional
//public class TestSet {
//
//    private final PopupService popupService;
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final MenuRepository menuRepository;
//
//    @Transactional
//    @PostConstruct
//    public void setUp() {
//        // 사용자 생성
//        User owner = createUser("김은학", "acky529@gmail.com", Gender.MALE, Role.OWNER, "acky529", "01098765432");
//        User customer = createUser("김은학", "529acky@naver.com", Gender.MALE, Role.CUSTOMER, "529acky", "01012345678");
//
//        // 현재 진행 중인 팝업 3개 생성
//        createPopupRestaurant(owner, "스타벅스 논현점", LocalDateTime.now().minusDays(3), LocalDateTime.now().plusDays(3));
//        createPopupRestaurant(owner, "홍콩반점 역삼점", LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(5));
//        createPopupRestaurant(owner, "요아정 서초점", LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(7));
//
//        // 진행 예정인 팝업 3개 생성
//        createPopupRestaurant(owner, "이디야 이태원점", LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(10));
//        createPopupRestaurant(owner, "설빙 명동점", LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(12));
//        createPopupRestaurant(owner, "버거킹 종로점", LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(15));
//    }
//
//    private User createUser(String name, String email, Gender gender, Role role, String loginId, String phoneNumber) {
//        User user = User.builder()
//                .name(name)
//                .birth(LocalDate.of(2000, 5, 29))
//                .email(email)
//                .gender(gender)
//                .role(role)
//                .loginId(loginId)
//                .phoneNumber(phoneNumber)
//                .password(passwordEncoder.encode("1234"))
//                .build();
//        return userRepository.save(user);
//    }
//
//    private void createPopupRestaurant(User owner, String name, LocalDateTime startDateTime, LocalDateTime endDateTime) {
//        PopupDto.BusinessTimeForm timeForm = PopupDto.BusinessTimeForm.builder()
//                .openTime(LocalTime.of(11, 0))
//                .closeTime(LocalTime.of(13, 0))
//                .reservationTimeUnit(30)
//                .maxPeoplePerTime(10)
//                .build();
//
//        // 메뉴 생성 - 팝업마다 고유 메뉴 지정 가능
//        List<PopupDto.MenuForm> menuForms = getMenusForPopup(name);
//
//        PopupDto.AddressForm addressForm = PopupDto.AddressForm.builder()
//                .street("서울시 강남구 강남대로 512")
//                .zipcode("06114")
//                .detail("3층")
//                .build();
//
//        PopupDto.PopupRestaurantForm restaurantForm = PopupDto.PopupRestaurantForm.builder()
//                .name(name)
//                .businessTimes(List.of(timeForm))
//                .startDateTime(startDateTime)
//                .endDateTime(endDateTime)
//                .description(name + " 팝업 설명입니다.")
//                .address(addressForm)
//                .menuForms(menuForms)
//                .build();
//
//        popupService.registerPopup(restaurantForm, owner.getId());
//        updateMenuImages(menuForms);
//    }
//
//    private List<PopupDto.MenuForm> getMenusForPopup(String popupName) {
//        // 팝업 이름에 따라 메뉴를 다르게 지정
//        if ("스타벅스 논현점".equals(popupName)) {
//            return List.of(
//                    PopupDto.MenuForm.builder()
//                            .price(5000)
//                            .description("현대인의 필수품")
//                            .name("아메리카노")
//                            .build(),
//                    PopupDto.MenuForm.builder()
//                            .price(6000)
//                            .description("우유 듬뿍 라떼")
//                            .name("카페 라떼")
//                            .build()
//            );
//        } else if ("홍콩반점 역삼점".equals(popupName)) {
//            return List.of(
//                    PopupDto.MenuForm.builder()
//                            .price(15000)
//                            .description("옛날식 짜장면")
//                            .name("짜장면")
//                            .build(),
//                    PopupDto.MenuForm.builder()
//                            .price(18000)
//                            .description("해물 가득 짬뽕")
//                            .name("짬뽕")
//                            .build()
//            );
//        } else if ("요아정 서초점".equals(popupName)) {
//            return List.of(
//                    PopupDto.MenuForm.builder()
//                            .price(20000)
//                            .description("직접 만든 요거트입니다.")
//                            .name("요거트 아이스크림")
//                            .build(),
//                    PopupDto.MenuForm.builder()
//                            .price(10000)
//                            .description("두바이에서 공수한 카다이프를 사용합니다.")
//                            .name("두바이 초콜릿")
//                            .build()
//            );
//        } else if ("이디야 이태원점".equals(popupName)) {
//            return List.of(
//                    PopupDto.MenuForm.builder()
//                            .price(6500)
//                            .description("초코 듬뿍 초코 라떼")
//                            .name("초코 라떼")
//                            .build(),
//                    PopupDto.MenuForm.builder()
//                            .price(6500)
//                            .description("딸기 듬뿍 딸기 라떼")
//                            .name("딸기 라떼")
//                            .build()
//            );
//        } else if ("설빙 명동점".equals(popupName)) {
//            return List.of(
//                    PopupDto.MenuForm.builder()
//                            .price(12000)
//                            .description("망고 가득 망고 빙수")
//                            .name("망고 빙수")
//                            .build(),
//                    PopupDto.MenuForm.builder()
//                            .price(10000)
//                            .description("고소한 인절미 가득")
//                            .name("인절미 빙수")
//                            .build()
//            );
//        } else{
//            return List.of(
//                    PopupDto.MenuForm.builder()
//                            .price(10000)
//                            .description("횡성 한우 사용")
//                            .name("와퍼")
//                            .build(),
//                    PopupDto.MenuForm.builder()
//                            .price(3000)
//                            .description("강원도 감자 사용")
//                            .name("감자튀김")
//                            .build()
//            );
//        }
//    }
//
//    private void updateMenuImages(List<PopupDto.MenuForm> menuForms) {
//        menuForms.forEach(menu -> {
//            String imageUrl = getImageUrlByName(menu.getName());
//            menuRepository.findAllByName(menu.getName()).forEach(
//                    dbMenu -> {
//                        dbMenu.updateImageUrl(imageUrl);
//                        menuRepository.save(dbMenu);
//                    }
//            );
//        });
//    }
//
//    private String getImageUrlByName(String menuName) {
//        // 메뉴 이름에 따른 이미지 URL 설정
//        switch (menuName) {
//            case "두바이 초콜릿":
//                return "https://www.dailypop.kr/news/photo/202407/79649_141593_5126.jpg";
//            case "요거트 아이스크림":
//                return "https://yoajung.co.kr/data/file/main_menu/e126a58ad07337ea5468a971ca8a6533_mygNdCiF_3dba0dba7ae373393edb7229553d60785dd2b8fb.png";
//            case "짜장면":
//                return "https://dliveimg.kbnc.co.kr/shop/upload/title/637/0000000001027421705389596994_title.png";
//            case "짬뽕":
//                return "https://dliveimg.kbnc.co.kr/shop/upload/title/202/0000000001026981705389595910_title.png";
//            case "아메리카노":
//                return "https://static.megamart.com/product/image/1326/13264314/13264314_1_960.jpg";
//            case "카페 라떼":
//                return "https://item.elandrs.com/upload/prd/orgimg/088/2005488088_0000001.jpg?w=750&h=&q=100";
//            case "망고 빙수":
//                return "https://img.hankyung.com/photo/201911/01.20967758.1.jpg";
//            case "인절미 빙수":
//                return "https://news.nateimg.co.kr/orgImg/ae/2020/01/13/ae_1578876423485_122395_0.jpg";
//            case "와퍼":
//                return "https://cdn.011st.com/11dims/resize/1000x1000/quality/75/11src/product/5590597357/B.jpg?826000000";
//            case "감자튀김":
//                return "https://dispatch.cdnser.be/cms-content/uploads/2019/02/28/f8ed2580-57f2-4e1a-8574-a5c92578226c.jpg";
//            case "초코 라떼":
//                return "https://cdn.011st.com/11dims/resize/600x600/quality/75/11src/product/3122219657/B.jpg?426000000";
//            case "딸기 라떼":
//                return "https://gdimg.gmarket.co.kr/3410442773/still/280?ver=1718961533";
//            default:
//                return "https://example.com/default-menu-image.jpg";
//        }
//    }
//}