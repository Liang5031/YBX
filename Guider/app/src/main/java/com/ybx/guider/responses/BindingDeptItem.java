package com.ybx.guider.responses;

import java.io.Serializable;

/**
 * Created by chenl on 2016/3/5.
 */
public class BindingDeptItem implements Serializable {
    public String customerid;
    public String customertype;
    public String customername;
    public String YYXCXK;
    public String WPTDXK;
    public String CJTDXK;
    public String RQSJ;
    public static String ALLOW = "是";
    public static String NOT_ALLOW = "否";


    public boolean isAllowAppointment() {
        if (YYXCXK != null) {
            return YYXCXK.equals("1");
        }
        return true;
    }

    public String getAllowAppointment() {
        if (YYXCXK == null) {
            return ALLOW;
        }
        if (YYXCXK.equals("1")) {
            return ALLOW;
        } else {
            return NOT_ALLOW;
        }
    }

    public void setAllowAppointment(boolean allow) {
        if (allow) {
            YYXCXK = "1";
        } else {
            YYXCXK = "0";
        }
    }

    public boolean isAllowAssignTeam() {
        if (WPTDXK != null) {
            return WPTDXK.equals("1");
        }
        return true;
    }

    public String getAllowAssignTeam() {
        if (WPTDXK == null) {
            return ALLOW;
        }
        if (WPTDXK.equals("1")) {
            return ALLOW;
        } else {
            return NOT_ALLOW;
        }
    }

    public void setAllowAssignTeam(boolean allow) {
        if (allow) {
            WPTDXK = "1";
        } else {
            WPTDXK = "0";
        }
    }

}
