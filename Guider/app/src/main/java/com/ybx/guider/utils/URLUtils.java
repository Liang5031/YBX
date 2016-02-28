package com.ybx.guider.utils;

import com.ybx.guider.parameters.Param;

/**
 * Created by chenl on 2016/2/4.
 */
public class URLUtils {

//    "http://10.0.2.2:8080/TomcatPro/MyFirstServlet"

    public static String SERVER_URL = "http://121.40.94.228:8081/source/";
    public static String TEST_SERVER_URL = "http://10.0.2.2:8080/";
    public static boolean isTestMode = false;
    public static boolean isDebug = true;
    public static String DEBUG_TAG = "chenl";

    public static String generateURL(String pageName, Param param) {
        String params = param.getParamString();
        if (isTestMode) {
            return TEST_SERVER_URL + pageName;
        } else if (params != null && params.length() > 0) {
            return SERVER_URL + pageName + "?" + params;
        } else {
            return SERVER_URL + pageName;
        }
    }

    public static String getServerUrl() {
        if (isTestMode) {
            return TEST_SERVER_URL;
        } else {
            return SERVER_URL;
        }
    }
}

