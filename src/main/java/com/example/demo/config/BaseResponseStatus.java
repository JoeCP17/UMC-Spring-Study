package com.example.demo.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),

    // users
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),
    USERS_STATUS_NOT_ACTIVE(false, 2011, "탈퇴한 회원입니다."),

    // [POST] /users
    POST_USERS_EMPTY_PHONENUM(false, 2015, "휴대폰번호를 입력해주세요."),
    POST_USERS_INVALID_PHONENUM(false, 2016, "휴대폰번호 형식을 확인해주세요."),
    POST_USERS_EXISTS_PHONENUM(false,2017,"중복된 휴대폰번호입니다."),

    // products

    // [PATCH] products
    PATCH_PRODUCTS_INVALID_STATUS(false, 2025, "유효하지 않은 상태 값입니다."),

    // Transaction
    // [PATCH] transactions
    PATCH_TRANSACTIONS_INVALID_BUYER(false,2030,"유효하지 않은 구매자 인덱스입니다."),

    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /users
    DUPLICATED_PHONENUM(false, 3013, "중복된 휴대폰번호입니다."),
    FAILED_TO_LOGIN(false,3014,"없는 아이디거나 비밀번호가 틀렸습니다."),



    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),

    //[PUT] /users/{userIdx}
    MODIFY_FAIL_USERNAME(false,4014,"유저네임 수정 실패"),
    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USERDONG(false,4015,"유저동네 수정 실패"),


    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다."),

    // transactions 상태 변경
    MODIFY_FAIL_TRANSACTION_STATUS(false,4034,"거래상태 수정 실패");


    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) { //BaseResponseStatus 에서 각 해당하는 코드를 생성자로 맵핑
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
