package com.ybx.guider;

import com.ybx.guider.parameters.Param;

/**
 * Created by chenl on 2016/2/4.
 */
public class Utils {
    public static String SERVER_URL = "http://www.baidu.com/";
    public static String TEST_SERVER_URL = "http://10.0.2.2:8080/YBX/";
    public static boolean isTestMode = true;


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
}


