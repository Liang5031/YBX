package com.ybx.guider.parameters;

/**
 * Created by chenl on 2016/2/15.
 */
public class GetVerifyCodeParam extends Param {

    public GetVerifyCodeParam(){
        removeParam(ParamUtils.KEY_SIGN);
    }

    public void setPhoneNumber(String phoneNumber){
        addParam(ParamUtils.KEY_PHONE_NUMBER, phoneNumber);
    }
}