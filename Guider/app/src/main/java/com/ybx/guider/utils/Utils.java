package com.ybx.guider.utils;

/**
 * Created by chenl on 2016/3/2.
 */
public class Utils {
    public static final int MIN_LENGTH_OF_PASSWORD = 6;

    public static boolean checkPassword(String password) {
        boolean hasNumber = false;
        boolean hasChar = false;

        for (int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);
            if (ch >= 'a' && ch <= 'z') {
                hasChar = true;
            }
            if (ch >= 'A' && ch <= 'Z') {
                hasChar = true;
            }
            if (ch >= '0' && ch <= '9') {
                hasNumber = true;
            }
        }
        return hasChar && hasNumber;
    }
}
