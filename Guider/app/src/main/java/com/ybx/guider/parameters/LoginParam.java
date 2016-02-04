package com.ybx.guider.parameters;

/**
 * Created by chenl on 2016/2/4.
 */
public class LoginParam extends Param {

    public void setSignType(String type){
        addParam(ParamUtils.LOGIN_SIGN_TYPE, type);
    }

    public void setServiceVersion(String version){
        addParam(ParamUtils.LOGIN_SERVICE_VERSION, version);
    }

    public void setSign(String sign){
        addParam(ParamUtils.LOGIN_SIGN, sign);
    }

    public void setUser(String user){
        addParam(ParamUtils.LOGIN_USER, user);
    }
}
