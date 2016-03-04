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




    public static String RESULT_OK = "0";
    public static String RESULT_FAIL = "1";
    public static String ACCOUNT_STATUS_CHECKING = "0";
    public static String ACCOUNT_STATUS_ACTIVE = "1";
    public static String ACCOUNT_STATUS_INACTIVE = "2";


}
