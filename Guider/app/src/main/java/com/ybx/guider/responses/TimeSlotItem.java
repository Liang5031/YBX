package com.ybx.guider.responses;

import java.io.Serializable;

/**
 * Created by chenl on 2016/3/9.
 */
public class TimeSlotItem implements Serializable {
    public String ServiceCode;
    public String Date;
    public String TimeSpanIndex;
    public String StartTime;
    public String EndTime;
    public String Capacity;
    public String AppointmentAble;
    public String AppointmentMaxRate;
    public String AppointmentCount;
    public String AppointmentUseCount;
    public String OtherUseCount;
    public String UseToAppointmentAble;

    public Integer getRemainCount(){
        if( AppointmentAble.equalsIgnoreCase("0")){
            return 0;
        }

        if( Integer.valueOf(Capacity) - Integer.valueOf(OtherUseCount) > Integer.valueOf(Capacity)*Integer.valueOf(AppointmentMaxRate)/100){
            return Integer.valueOf(Capacity)*Integer.valueOf(AppointmentMaxRate)/100 - Integer.valueOf(AppointmentCount);
        }

        return Integer.valueOf(Capacity) - Integer.valueOf(OtherUseCount) - Integer.valueOf(AppointmentCount);
    }

}
