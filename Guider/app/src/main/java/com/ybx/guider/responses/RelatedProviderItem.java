package com.ybx.guider.responses;

import java.io.Serializable;

/**
 * Created by chenl on 2016/3/5.
 */
public class RelatedProviderItem implements Serializable {
    public String providerid;
    public String providername;
    public String status;
    public String reqdate;
    public String upddate;
    public String disstartdate;
    public String disenddate;


    public String getStatus(){
        if(status.equalsIgnoreCase("R")){
            return "等待供应商审核";
        } else if(status.equalsIgnoreCase("1")){
            return "可以预约";
        }else if(status.equalsIgnoreCase("2")){
            return "停止使用";
        }else if(status.equalsIgnoreCase("U")){
            return "申请被拒绝";
        }
        return "";
    }

}
