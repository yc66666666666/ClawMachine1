package com.doll.utils;

public class PrivacyProtectionUtils {
         //java 11 及以上才能用
//        public static String anonymizeNickname(String nickname) {
//            if (nickname == null || nickname.isEmpty()) {
//                return nickname;
//            }
//
//            int length = nickname.length();
//            if (length <= 2) {
//                return "*".repeat(length);
//            }
//            return nickname.charAt(0) + "*".repeat(length - 2) + nickname.charAt(length - 1);
//        }

    public static String anonymizeNickname(String nickname) {
        if (nickname == null || nickname.isEmpty()) {
            return nickname;
        }

        int length = nickname.length();
        if (length <= 2) {
            return repeat("*", length);
        }
        return nickname.charAt(0) + repeat("*", length - 2) + nickname.charAt(length - 1);
    }

    // 自定义 repeat 方法
    public static String repeat(String str, int count) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            builder.append(str);
        }
        return builder.toString();
    }




}
