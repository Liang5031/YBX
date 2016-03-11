package com.ybx.guider.parameters;

/**
 * Created by chenl on 2016/2/4.
 */
public class ParamUtils {
    /* Page name for all requests */
    public static String PAGE_GUIDER_LOGIN = "YUNGuider_Login.aspx";
    public static String PAGE_GUIDER_LOGOUT = "YUNGuider_Logout.aspx";
    public static String PAGE_GUIDER_RESET_PASSWORD = "YUNGuider_ResetPwd.aspx";
    public static String PAGE_GUIDER_CHANGE_PASSWORD = "YUNGuider_ChangePwd.aspx";
    public static String PAGE_GET_CHECK_CODE = "YUNGuider_GetCheckCode.aspx";
    public static String PAGE_GUIDER_REGISTER = "YUNGuider_Register.aspx";
    public static String PAGE_APPOINTMENT_CHANGE = "YUNAppointment_Change.aspx";
    public static String PAGE_APPOINTMENT_CANCEL = "YUNAppointment_Cancel.aspx";
    public static String PAGE_APPOINTMENT_PERMISSION_QUERY = "YUNAppointmentPermision_Query.aspx";
    public static String PAGE_DEAL_INFO_QUERY = "YUNDealInfo_Query.aspx";
    public static String PAGE_GUIDER_QUERY = "YUNGuider_Query.aspx";
    public static String PAGE_TEAM_QUERY = "YUNTeam_Query.aspx";
    public static String PAGE_TEAM_REAL_NAME_QUERY = "YUNTeamIdentityInfo_Query.aspx";
    public static String PAGE_TEAM_SCHEDULE_QUERY = "YUNTeamTripInfo_Query.aspx";
    public static String PAGE_TEAM_DEAL_QUERY = "YUNTeamDeal_Query.aspx";
    public static String PAGE_TEAM_ACCEPT = "YUNTeamOper_Accept.aspx";
    public static String PAGE_TEAM_COMPLETE = "YUNTeamOper_Complete.aspx";
    public static String PAGE_TEAM_LOG_QUERY = "YUNTeamLog_Query.aspx";
    public static String PAGE_GUIDER_UPDATE_INFO = "YUNGuider_Update.aspx";
    public static String PAGE_DEPT_QUERY = "YUNRelationObjects_Query.aspx";
    public static String PAGE_DEPT_ADD = "YUNRelationObjects_Add.aspx";
    public static String PAGE_PROVIDER_QUERY = "YUNProvider_Query.aspx";
    public static String PAGE_CUSTOMER_QUERY = "YUNCustomer_Query.aspx";
    public static String PAGE_SERVICE_ITEM_QUERY = "YUNServiceItem_Query.aspx";
    public static String PAGE_SERVICE_TIME_SPAN_QUERY = "YUNServiceTimeSpan_Query.aspx";
    public static String PAGE_START_APPOINTMENT = "YUNAppointment_Add.aspx";

    /* 签名方式 -- String(8) 取值：MD5，默认：MD5 暂时只支持MD5 */
    public static String KEY_SIGN_TYPE = "sign_type";
    /* 接口版本 -- String(8) 默认为1.0 */
    public static String KEY_SERVICE_VERSION = "service_version";
    /* 字符集 -- String(8) 默认 UTF-8*/
    public static String KEY_INPUT_CHARSET ="input_charset";
    /* 手机验证码的明文 */
    public static String KEY_VERIFY_CODE ="sSMSCode";
    /* 签名 -- String(32) */
    public static String KEY_SIGN = "sign";
    /* 用户名 -- String(64) 导游证号 */
    public static String KEY_USER = "User";
    /* 新密码 -- String(64) TripleDes(MD5(newpassword),sOldpassword)。newpassword是用户输入的新密码（明文) sOldPassword是旧密码的MD5值 */
    public static String KEY_NEW_PASSWORD = "sNewPassword";
    /* 用户手机号码 -- String(20) 此手机号将用于接收验证码，注册后可以修改 */
    public static String KEY_PHONE_NUMBER = "sMobile";
    /* 供应商ID -- Bigint */
    public static String KEY_PROVIDER_ID = "nProviderID";
    /* 供应商服务项目ID  -- String(2) 通过调用接口‘YUNServiceItem_Query.aspx’获取 */
    public static String KEY_SERVICE_ID = "sServiceCode";
    /* 日期 -- String(8) YYYYMMDD 一次仅能检索一天的数据，日期必须大于等于今天，小于等于今天起15个日历日内的日期 */
    public static String KEY_DATE = "sDate";
    /* 行程序号 -- bigint 存储在T_YY_TD_XC表里，通过接口可以获取 */
    public static String KEY_TRIP_ITEM_INDEX = "nTripItemIndex";
    /* 行程序号 -- bigint YUNTeamTripInfo_Query.aspx*/
    public static String KEY_TRIP_INDEX = "nTripIndex";
    /* 时段号 -- int */
    public static String KEY_TIME_SPAN_INDEX = "nTimeSpanIndex";
    /* 时间段 -- String(20) 当对应供应商的YYJKLX为“1”时，此值不能为空，由导游输入。如“12：00-12：30”等 */
    public static String KEY_TIME_DESC = "nTimeDesc";
    /* 人数 -- Int 总人数，默认情况下，使用团队的“成年人数”+“未成年人数”的和，导游也可以自行输入此值（但这个值必须小于等于团队的总人数）*/
    public static String KEY_TEAM_MEMBER_COUNT = "nCount";
    /* 团队成员说明 -- String(50) 默认情况下，使用T_YY_TD中的“TYMS”字段值；导游也可以另行输入 */
    public static String KEY_TEAM_MEMBER_DESC = "sMemberDesc";
    /* 随团手机1 -- String(20) 默认情况下，使用导游注册时留下的手机，此值存放在表T_KH_DYY表上的主要手机“ZYLXRSJ”中，也可以由导游自行输入 */
    public static String KEY_GUIDER_PHONE_NUMBER_1 = "sMobile1";
    /* 随团手机2 -- String(20) 可以由导游自行输入 */
    public static String KEY_GUIDER_PHONE_NUMBER_2 = "sMobile2";
    /* 预约备注 */
    public static String KEY_RESERVATION_MEMO = "sMemo";
    /* 预约编号 -- String(20) 在发起预约时由接口返回，保存在云数据库的T_YY_TD_SC的YYBH字段 */
    public static String KEY_RESERVATION_NUMBER = "sAppointmentNumber";
    /* 交易单据号 -- String(20) 此值通过Appointment_Query.aspx查询返回 此参数若提供，则必须进行确认，防止检索到非指定预约的售票单数据 */
    public static String KEY_BUSINESS_ID = "sBusinessID";
    /* 导游证号 -- String(20) 一旦注册后，此值不能更改 YUNGuider_Register.aspx YUNGuider_GetCheckCode.aspx YUNGuider_Update.aspx YUNTeamLog_Query.aspx */
    public static String KEY_GUIDER_NUMBER = "sGuiderNumber";
    /* 姓名 -- String(20) 一旦注册后，此值不能更改，且内容必须与实际导游证上的姓名一致，否则不能通过实名认证 */
    public static String KEY_GUIDER_NAME = "sName";
    /* 身份证号 -- String(20) 个人身份证件号码，外籍人士使用护照号码，此值允许注册之后修改 */
    public static String KEY_GUIDER_IDENTITY_NUMBER = "sIdentityNumber";
    /* 主要语种 -- String(20) */
    public static String KEY_FIRST_LANGUAGE = "sLanguage";
    /* 次要语种 -- String(20) */
    public static String KEY_SECOND_LANGUAGE = "sLanguage2";
    /* 登录密码 -- String(64) TripleDes(MD5(密码),MD5(验证码)) 验证码的MD5值为密钥。密码是指用户输入的登陆密码。验证码是通过手机发送的验证码 */
    public static String KEY_PASSWORD = "sPassword";
    /* 照片 */
    public static String KEY_PHOTO = "sPhoto";
    /* 页号 -- Int 此值必须大于等于零。此值不指定与输入零是等价的，均表示返回第一页的数据。每页的数据为5条 */
    public static String KEY_PAGE_INDEX = "nPageIndex";
    /* 团队编号 -- Bigint 团队的编号，当此值被指定时，nPageIndex没有意义，总是为0 */
    public static String KEY_TEAM_INDEX = "nTeamIndex";
    /* 团队状态 -- String(1) 1-初始状态，团队创建中；2-确认待委派；3-已委派待接团；4-导游已接团；5-带团完成待结算；6-已结算。已结算是最终状态，不再容许修改。*/
    public static String KEY_TEAM_STATUS = "sStatus";
    /* 行程单号/团队自编号 -- String(20) */
    public static String KEY_TEAM_ORDER_NUMBER = "sTeamOrderNumber";
    /* 户口所在地行政区划  -- string(6) 请参照国家行政区划代码 */
    public static String KEY_ADDRESS_ZONE_1 = "sAddZone1";
    /* 户籍地址  -- string(64) */
    public static String KEY_ADDRESS_1 = "sAddress1";
    /* 常住地行政区划  -- string(6) 请参照国家行政区划代码 */
    public static String KEY_ADDRESS_ZONE_2 = "sAddZone2";
    /* 常住地址  -- string(64) */
    public static String KEY_ADDRESS_2 = "sAddress2";
    /* 学历  -- string(20) */
    public static String KEY_EDUCATION = "sEducation";
    /* 毕业时间  -- string(14) 可以是年份 */
    public static String KEY_GRADUATION = "sGraducation";
    /* 电话  -- String(20) */
    public static String KEY_PHONE = "sPhone";
    /* QQ  -- String(20) */
    public static String KEY_QQ = "sQQ";
    /* 微信  -- String(20) */
    public static String KEY_WECHAT = "sWeChart";


    public static String VALUE_SIGN_TYPE_MD5 = "MD5";
    public static String VALUE_DEFAULT_SERVICE_VERSION = "1.0";
    public static String VALUE_DEFAULT_CHAR_SET = "UTF-8";
    public static int VALUE_FIRST_PAGE_INDEX = 0;
}
