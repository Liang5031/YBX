package com.ybx.guider.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by chenl on 2016/2/18.
 */
public class PreferencesUtils {
    public static String TAG = "PreferencesUtils";

    public static String PREFS_NAME = "guider_prefs";
    public static String PREFS_KEY_GUIDER_NUMBER = "guider_number";
    public static String PREFS_KEY_PASSWORD = "password";
    public static String PREFS_KEY_AUTO_LOGIN = "auto_login";
    public static String PREFS_KEY_PAGE_INDEX = "page_index";
    public static String PREFS_KEY_PHONE_NUMBER = "phone_number";

    public static void clearLoginInfo(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(PREFS_KEY_GUIDER_NUMBER, "");
        editor.putString(PREFS_KEY_PASSWORD, "");
        editor.putBoolean(PREFS_KEY_AUTO_LOGIN, false);
        editor.commit();
    }

    public static void saveLoginInfo(Context ctx, String guiderNumber, String password, boolean isAutoLogin) {
        SharedPreferences sp = ctx.getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(PREFS_KEY_GUIDER_NUMBER, guiderNumber);
        editor.putString(PREFS_KEY_PASSWORD, password);
        editor.putBoolean(PREFS_KEY_AUTO_LOGIN, isAutoLogin);
        editor.commit();
        if (URLUtils.isTestMode) {
            Log.d(TAG, "Guider number: " + guiderNumber);
            Log.d(TAG, "Password: " + password);
            Log.d(TAG, "Auto login: " + isAutoLogin);
        }
    }

    public static void setIsAutoLogin(Context ctx, boolean isAutoLogin) {
        SharedPreferences sp = ctx.getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(PREFS_KEY_AUTO_LOGIN, isAutoLogin);
        editor.commit();
    }

    public static boolean getIsAutoLogin(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE);
        return sp.getBoolean(PREFS_KEY_AUTO_LOGIN, false);
    }

    public static void setGuiderNumber(Context ctx, String guiderNumber) {
        SharedPreferences sp = ctx.getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(PREFS_KEY_GUIDER_NUMBER, guiderNumber);
        editor.commit();
    }

    public static String getGuiderNumber(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE);
        return sp.getString(PREFS_KEY_GUIDER_NUMBER, "");
    }

    public static void setPassword(Context ctx, String password) {
        SharedPreferences sp = ctx.getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(PREFS_KEY_PASSWORD, password);
        editor.commit();
    }

    public static String getPassword(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE);
        return sp.getString(PREFS_KEY_PASSWORD, "");
    }

    public static void setLastPageIndex(Context ctx, int index) {
        SharedPreferences sp = ctx.getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(PREFS_KEY_PAGE_INDEX, index);
        editor.commit();
    }

    public static int getLastPageIndex(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE);
        return sp.getInt(PREFS_KEY_PAGE_INDEX, 0);
    }

    public static void setPhoneNumber(Context ctx, String number) {
        SharedPreferences sp = ctx.getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(PREFS_KEY_PHONE_NUMBER, number);
        editor.commit();
    }

    public static String getPhoneNumber(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE);
        return sp.getString(PREFS_KEY_PHONE_NUMBER, "");
    }
}
