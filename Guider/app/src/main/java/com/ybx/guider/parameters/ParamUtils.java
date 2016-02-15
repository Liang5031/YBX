package com.ybx.guider.parameters;

/**
 * Created by chenl on 2016/2/4.
 */
public class ParamUtils {
    /* Page name for all requests */
    public static String PAGE_LOGIN = "YUNGuider_Login.aspx";
    public static String PAGE_RESET_PASSWORD = "YUNGuider_ChangePwd.aspx";
    public static String PAGE_GET_VERIFY_CODE = "YUNGuider_GetCheckCode.aspx";


    /* Common parameters for all request */
    public static String KEY_SIGN_TYPE = "sign_type";
    public static String KEY_SERVICE_VERSION = "service_version";
    public static String KEY_INPUT_CHARSET ="input_charset";
    public static String KEY_SIGN = "sign";

    /* Parameters for PAGE_LOGIN request */
    public static String KEY_LOGIN_USER = "User";

    /* Parameters for PAGE_RESET_PASSWORD request */
    public static String KEY_NEW_PASSWORD = "sNewPassword";

    /* Parameters for PAGE_GET_VERIFY_CODE  request */
    public static String KEY_PHONE_NUMBER = "sMobile";


    public static String VALUE_SIGN_TYPE_MD5 = "MD5";
    public static String VALUE_DEFAULT_SERVICE_VERSION = "1.0";
    public static String VALUE_DEFAULT_CHAR_SET = "UTF-8";

    public static String generateSign(){
        return "sign";
    }
}
