package com.ybx.guider.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.ViewConfiguration;

import java.lang.reflect.Field;

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

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void showOverflowMenu(Context ctx) {
        //devices with hardware menu button (e.g. Samsung Note) don't show action overflow menu
        try {
            ViewConfiguration config = ViewConfiguration.get(ctx);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
        }
    }

    public static String getVersionName(Context context) {
        String versionName = "Version ";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName += pi.versionName;
//            versioncode = pi.versionCode;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
        }
        return versionName;
    }
}
