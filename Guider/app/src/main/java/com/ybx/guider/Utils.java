package com.ybx.guider;

import com.ybx.guider.parameters.Param;

/**
 * Created by chenl on 2016/2/4.
 */
public class Utils {
    public static String SERVER_URL = "http://www.baidu.com/";
    public String url;

    public String generateURL(String pageName, Param param){
        return SERVER_URL + pageName + "?" + param.GetParamString();
    }
}
