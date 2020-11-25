package ru.netology.web.data;

import lombok.Value;

public class DataHelper {


    private DataHelper() {
    }

    @Value
    public static class AuthInfo {
        private String login;
        private String password;
    }

    public static AuthInfo getAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    @Value
    public static class VerificationCode {
        private String code;
    }

    public static VerificationCode getVerificationCodeFor(AuthInfo authInfo) {
        return new VerificationCode("12345");
    }

    public static String getCardInfo(int item) {
        String[] cards = {"5559 0000 0000 0001", "5559 0000 0000 0002", "1111 1111 1111"};
        if (item >= 0 && item <= 2) {
            return cards[item];
        }
        return null;
    }

    @Value
    public static class Payment {
        private String payment;
    }

    public static String getPayment() {
        return ("4");
    }

}