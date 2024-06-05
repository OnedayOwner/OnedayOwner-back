package com.OnedayOwner.server.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    /**
     * 400 : Bad Request (클라이언트가 잘못된 요청을 해서 서버가 요청을 처리할 수 없음)
     * 401 : Unauthorized (클라이언트가 해당 리소스에 대한 인증이 필요함)
     * 403 : Forbidden (서버가 요청을 이해했지만 승인을 거부함)
     * 404 : Not Found (요청 리소스를 찾을 수 없음)
     * 500 : Internal Server Error (서버 문제로 오류 발생)
     * 503 : Service Unavailable (서비스 이용 불가)
     */
    INVALID_INPUT_VALUE(400, "C001", " Invalid Input Value"),
    METHOD_NOT_ALLOWED(405, "C002", " Method Not Allowed"),
    NULL_POINT_ERROR(404, "C005", "Null Point Exception"),
    HANDLE_ACCESS_DENIED(403, "C006", "Access is Denied"),
    INTERNAL_SERVER_ERROR(500, "C007", "Internal Server Error"),
    OWNER_NOT_FOUND(404, "C008", "Owner not found"),
    POPUP_NOT_FOUND(404, "C009", "Popup not found"),
    IN_BUSINESS_POPUP_NOT_FOUND(404, "C010", "진행중인 팝업식당이 없습니다."),
    RESERVATION_TIME_NOT_FOUND(404, "C011", "설정된 예약 시간이 없습니다."),
    CAN_NOT_RESERVE_DURING_THAT_TIME(404, "C012", "해당 시간에 예약이 불가합니다."),
    CUSTOMER_NOT_FOUND(404, "C013", "Customer not found"),
    MENU_NOT_FOUND(404, "C014", "Menu not found"),
    RESERVATION_NOT_FOUND(404, "C015", "Reservation not found"),
    CANNOT_ACCESS_RESERVATION(404, "C016", "해당 예약에 접근할 수 없습니다."),
    NUMBER_OF_PEOPLE_EXCEEDED(404, "C017", "예약 가능 인원을 초과하였습니다.")
    ;

    private final String code;
    private final String message;
    private final int status;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }
}
