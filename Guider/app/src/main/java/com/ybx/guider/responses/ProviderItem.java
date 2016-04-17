package com.ybx.guider.responses;

import java.io.Serializable;

/**
 * Created by chenl on 2016/3/5.
 */
public class ProviderItem implements Serializable {
    public String providerid;
    public String providername;
    public String type;
    public String appointmentable;

    public String getAppointmentType(){
        if(appointmentable.equals("0")){
            return "不允许预约";
        } else if(appointmentable.equals("1")){
            return "云平台预约";
        }else if(appointmentable.equals("2")){
            return "远程预约";
        }
        return "";
    }
}
