package com.ybx.guider.parameters;

/**
 * Created by chenl on 2016/2/4.
 */
public class LoginParam extends Param {

    public LoginParam(){
        mPageName = ParamUtils.PAGE_LOGIN;
    }

    public void setUser(String user){
        addParam(ParamUtils.KEY_USER, user);
    }
}
