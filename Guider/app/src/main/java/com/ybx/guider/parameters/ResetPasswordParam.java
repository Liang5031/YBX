package com.ybx.guider.parameters;

/**
 * Created by chenl on 2016/2/15.
 */
public class ResetPasswordParam extends Param{

    public void setGuiderNumber(String user){
        addParam(ParamUtils.KEY_GUIDER_NUMBER, user);
    }

    public void setNewPassword(String password){
        addParam(ParamUtils.KEY_NEW_PASSWORD, password);
    }
}
