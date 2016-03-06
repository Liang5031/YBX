package com.ybx.guider.responses;

/**
 * Created by chenl on 2016/3/2.
 */
public class ResponseUtils {

    public static String TAG_RETURN_CODE = "retcode";
    public static String TAG_RETURN_MSG = "retmsg";
    public static String TAG_SIGN = "sign";
    public static String TAG_ACCOUNT_STATUS = "Status";                     /* 状态‘0’注册待审核中；‘1’启用；‘2’禁用；‘X’注册审核不通过 */


    public static String TAG_PAGE_COUNT = "pagecount";                      /* 在当前查询条件下一共有多少页数据 */
    public static String TAG_PAGE_INDEX = "pageindex";                      /* 当前返回数据的页号 */
    public static String TAG_IS_LASTPAGE = "islastpage";                    /* 当前页是否是当前查询条件的最后一页，“1”表示是最后一页 */

    /* YUNTeam_Query.aspx */
    public static String TAG_ITEM = "item";
    public static String TAG_ITEM_TEAM_INDEX = "TeamIndex";                 /* 团队编号，全局唯一，BIGINT */
    public static String TAG_ITEM_AGENCY_NAME = "AgencyName";               /* 旅行社名称,string(64) */
    public static String TAG_ITEM_DEPARTMENT_NAME = "DepartmentName";       /* 部门名称，string(64) */
    public static String TAG_ITEM_START_DATE = "StartDate";                 /* 行程起始日期，string(8)，格式YYYYMMDD */
    public static String TAG_ITEM_END_DATE = "EndDate";                     /* 行程截止日期，string(8)，格式YYYYMMDD */
    public static String TAG_ITEM_TOURIST_SOURCE = "Touristsource";         /* 客源地，string(16) */
    public static String TAG_ITEM_TEAM_ORDER_NUMBER = "TeamOrderNumber";    /* 行程单号，string(20) */
    public static String TAG_ITEM_PEOPLE_COUNT_1 = "PepleCount1";           /* 成年人数，Int，大于等于零 */
    public static String TAG_ITEM_PEOPLE_COUNT_2 = "PepleCount2";           /* 未成年人数，int，大于等于零 */
    public static String TAG_ITEM_MEMBER_DESC = "MemberDesc";               /* 团队成员说明，string(50)，示例：“几大几小，老人几个，几个有优惠证” */
    public static String TAG_ITEM_TRIP_DESC = "TripDesc";                   /* 行程描述，string(50)，很重要的字段，示例“张+凤四天三晚游” */
    public static String TAG_ITEM_TEAM_FROM = "TeamFrom";                   /* 团队来源，string(20)，如互联网，组团等 */
    public static String TAG_ITEM_TEAM_FROM_DESC = "TeamFromDesc";          /* 团队来源注释（如天猫，去哪儿，或XX组团等） */
    public static String TAG_ITEM_TOTAL_AMOUNT = "TotalAmount";             /* 总应收团款,decimal(18,2)，大于等于零 */
    public static String TAG_ITEM_TOTAL_AMOUNT_DESC = "TotalAmountDesc";    /* 团款描述，string(64)，示例：已收3200，请导游现收4500 */
    public static String TAG_ITEM_CNAME = "CName";                           /* 计调姓名,string(20) */
    public static String TAG_ITEM_STATUS = "Status";                         /* 状态 ,string(1) */
    public static String TAG_ITEM_STATUS_NAME = "StatusName";                /* 状态的文字描述，同sStatus的含义,string(20) */
    public static String TAG_ITEM_C_DATE_TIME = "CDateTime";                 /* 创建这个团队的日期时间，string(14)，YYYYMMDDHHmmss */
    public static String TAG_ITEM_TEAM_MEMO = "memo";                         /* 团队备注 string(200)，对应BZ字段 */
    public static String TAG_ITEM_IDENTITY_COUNT = "IdentityCount";          /* int 团队实名信息的记录条数 */
    public static String TAG_ITEM_TRIP_COUNT = "TripCount";                   /* 行程条目的记录个数，int */
    public static String TAG_ITEM_DEAL_COUNT = "DealCount";                   /* 成交的记录个数,int */
    public static String TAG_ITEM_LOG_COUNT = "LogCount";                     /* 日志的记录条数,int */

    /* YUNTeamTripInfo_Query.aspx */
    public static String TAG_DATE = "Date";                     /* 日期，显示时的第一排序条件，string(8)，YYYYMMDD */
    public static String TAG_TRIP_INDEX = "TripIndex";         /* 行程序号，全局唯一，bigint，显示时的第二排序条件 */
    public static String TAG_TIME_REQUIRE = "TimeRequire";     /* 时间要求，string(20)，如“上午9:00” */
    public static String TAG_TRIP_TYPE = "TripType";            /* 行程类型，“1”行程内，“2”加点，String(1) */
    public static String TAG_TRAN_TYPE = "TranType";            /* 事务的类型，string(1)  “1”吃，“2”住，“3”行，“4”游，“5”购(或采点)，“6”娱 */
    public static String TAG_TRAN_NAME = "TranName";            /* 事务名称，string(30)，如：天门山；中餐；晚餐等 */
    public static String TAG_PROVIDER_NUMBER = "ProviderNumber";     /* 供应商ID，bigint */
    public static String TAG_PROVIDER_NAME = "ProviderName";     /* 供应商名称，string(64) */
    public static String TAG_DESC = "Desc";                       /* 说明，对当前行程的说明 */
    public static String TAG_STATUS = "status";     /* 状态，1：初始状态，待执行；2：预约中；3：预约成功；4：成交(已完成)；5：预约被撤消；6：行程取消 */
    public static String TAG_APPO_NUMBER = "AppoNumber";     /* 预约单号或编号，string(36) */
    public static String TAG_APPO_DESC = "AppoDesc";     /* 预约情况的描述，string(64) */
    public static String TAG_PROVIDER_APP_MODE = "ProviderAppMode";     /* 供应商支持的预约形式，‘0’不支持预约，‘1’云平台预约，‘2’远程预约 */

    /* YUNTeamIdentityInfo_Query.aspx */
    public static String TAG_NUMBER = "Number";     /* 证件号码,string(20) */
    public static String TAG_NAME = "Name";     /* 姓名,string(20) */
    public static String TAG_TYPE = "Type";     /* 证件类型，string(1)，‘1’身份证，‘2’护照，‘3’其他 */

    /* YUNTeamDeal_Query.aspx */
    public static String TAG_REC_NUMBER = "recordnumber";     /* 成交记录编号	对应CJJLBH字段，	Bigint  */
    public static String TAG_PROVIDER_ID = "providerid";     /* 供应商ＩＤ 对应QYDM字段，Bigint */
    public static String TAG_BILL_NUMBER = "billnumber";     /* 单据单号	对应DJDH字段，string(20) */
    public static String TAG_APPO_NUM = "appointmentnum";     /* 预约单号/编号	对应YYBH字段，string(36) */
    public static String TAG_PRODUCT_NAME = "productname";     /* 产品名称	对应CPMC字段，string(64) */
    public static String TAG_PRODUCT_ID = "productid";     /* 产品ＩＤ 对应CPID字段，string(36) */
    public static String TAG_PRICE = "price";           /* 单价，对应DJ字段，Decimal(18,2) */
    public static String TAG_COUNT = "count";          /* 数量，对应SL字段，decimal(18,2) */
    public static String TAG_REC_DATE = "recdate";        /* 成交日期，对应CJRQ字段，string(8) */


    public static String RESULT_OK = "0";
    public static String RESULT_FAIL = "1";
    public static String ACCOUNT_STATUS_CHECKING = "0";
    public static String ACCOUNT_STATUS_ACTIVE = "1";
    public static String ACCOUNT_STATUS_INACTIVE = "2";

    /* 证件类型 */
    public static String TYPE_IDENTITY_ID = "1";
    public static String TYPE_PASSPORT = "2";
    public static String TYPE_OTHERS = "3";

    /**
     * YYYYMMDD to YYYY-MM-DD
     */
    public static String formatDate(String date) {
        if (date != null && date.length() == 8) {
            return date.substring(0, 4) + "/" + date.substring(4, 6) + "/" + date.substring(6, 8);
        }
        return "";
    }

    public static String getDuration(String startDate, String endDate) {
        if (startDate == null || startDate.isEmpty() || endDate == null || endDate.isEmpty()) {
            return "";
        }
        return formatDate(startDate) + "-" + formatDate(endDate);
    }

    public static String getTeamOrderNumber(String orderNumber) {
        if (orderNumber == null || orderNumber.isEmpty()) {
            return "[无行程单号]";
        }
        return "[" + orderNumber + "]";
    }

    public static String getIdentidyType(String type) {
        if (type.equalsIgnoreCase(TYPE_IDENTITY_ID)) {
            return "身份证";
        } else if (type.equalsIgnoreCase(TYPE_PASSPORT)) {
            return "护照";
        } else if (type.equalsIgnoreCase(TYPE_OTHERS)) {
            return "其他";
        }
        return "其他";
    }

}
