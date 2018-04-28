package com.carlt.doride.systemconfig;


import com.carlt.doride.DorideApplication;

public class URLConfig {
    public final static int VERSION_FORMAL  = 1001;// 正式服
    public final static int VERSION_PREPARE = 1002;// 预发布服
    public final static int VERSION_TEST    = 1003;// 测试服
    public static       int flag            = VERSION_TEST;

    // 车乐测试服务器
    private final static String C1 = "0896756ebec5bc62a51b15b9a7541901";

    // 车乐正式服务器
    private final static String C2 = "890ce20d220196ed6dbb0f51793e44ef";

    //大乘域名 测试服
    private final static String U1_DORIDE_TEST = "http://dorideapi.linewin.cc/";

    //大乘域名 预发布服
    private final static String U1_DORIDE_PRE = "http://pre-dorideapi.geni4s.com/";

    // 众泰大乘API域名 正式服
    private final static String U1_DORIDE = "http://dorideapi.geni4s.com/";

    // 远程下发-正式服务器
    private final static String U_R1 = "https://remote-wildhorse.geni4s.com/";
    // 远程下发-预发布服务器
    private final static String U_R2 = "https://pre-remote-wildhorse.geni4s.com/";
    // 大乘下发-测试服务器
    private final static String U_R3 = "http://remote-doride.linewin.cc/";

    // 获取DORIDE API URL
    private static String getDorideURL(String s) {
        String version = DorideApplication.Version_API + "/";
        String url = "";
        // 正常版
        if (DorideApplication.Formal_Version) {
            url = U1_DORIDE + version + s;
        } else {
            switch (flag) {
                case VERSION_FORMAL:
                    // 正式服
                    url = U1_DORIDE + version + s;
                    break;

                case VERSION_PREPARE:
                    // 预发布服
                    url = U1_DORIDE_PRE + version + s;
                    break;
                case VERSION_TEST:
                    // 测试服
                    url = U1_DORIDE_TEST + version + s;
                    break;
            }
        }
        return url;
    }

    ;

    // 生成和远程下发相关的Url
    private static String getUrlRemote(String s) {
        if (DorideApplication.Formal_Version) {
            return U_R1 + DorideApplication.VERSION_API_REMOTE + "/" + s;
        } else {
            switch (flag) {
                case VERSION_FORMAL:
                    // 正式服
                    return U_R1 + DorideApplication.VERSION_API_REMOTE + "/" + s;
                case VERSION_PREPARE:
                    // 预发布服
                    return U_R2 + DorideApplication.VERSION_API_REMOTE + "/" + s;
                case VERSION_TEST:
                    // 测试服
                    return U_R3 + DorideApplication.VERSION_API_REMOTE + "/" + s;
                default:
                    return U_R3 + DorideApplication.VERSION_API_REMOTE + "/" + s;
            }
        }
    }

    public static String getClientID() {
        String clientId = "";
        if (DorideApplication.Formal_Version) {
            clientId = C2;
        } else {
            switch (flag) {
                case VERSION_FORMAL:
                    // 正式服
                    clientId = C2;
                    break;

                case VERSION_PREPARE:
                    // 预发布服
                    clientId = C2;
                    break;
                case VERSION_TEST:
                    // 测试服
                    clientId = C1;
                    break;
            }
        }
        return clientId;
    }

    ;

    // 新版登录
    private static String M_LOGIN_NEW_URL = "user/login";

    // 新版注册
    private static String M_REGISTER_NEW_URL = "user/register";

    // 获取设备升级状态
    private static String M_DEVICEUPDATE_URL = "user/checkIsUpgrade";

    // 新版发送验证码
    private static String M_VALIDATE_NEW_URL = "user/setValidate";

    // 车秘书分类
    private static String M_SECRETARY_CATEGORY_URL_NEW = "life/messageCategory";

    // 删除车秘书消息
    private static String M_SECRETARY_DELETE_URL_NEW = "life/deleteMessage";
    // 获取大迈车系（针对车款列表-二级列表）
    private static String M_OPTIONLIST_URL           = "comm/getDomyOptionList";

    // 获取大迈车款（针对车款列表-三级列表）
    private static String M_CARLIST_URL = "comm/getDomyCarList";

    // 获取生涯首页
    private static String M_CAREER_URL = "life/lifeIndex";

    // 月报
    private static String M_MONTHREPORT_URL = "life/monthReport";

    // 行车报告日期
    private static String M_REPORTDATE_URL = "life/reportdate";

    // 某年月报统计数据
    private static String M_MONTHREPORTSTATISTIC_URL = "life/monthReportStatistic";

    // 轨迹回放
    private static String M_GETCOOR_URL = "gps/getCoor";

    // 日历信息--月报
    private static String M_USER_MONTH_POINT_URL = "life/usermonthpoint";

    // 获取TOKEN
    private static String M_USER_ACCESSTOKEN = "user/accesstoken";

    // 日历信息--日报
    private static String M_USER_DAY_POINT_URL = "life/userdaypoint";

    // 获取用户绑定车款配置
    private static String M_CAR_CURCARCONFIG_URL = "car/curCarConfig";

    // 修改车辆信息--切换车型
    private static String M_SWITCHCAR_URL = "remote/switchCar";

    // 座驾首页
    private static String M_CAR_MAIN_URL       = "car/carIndex";
    private static String M_REMOTE_CAR_MILES_INFO_URL = "remote/getMilesInfos";

    // 日报
    private static String M_REPORTDAY_URL    = "life/dayReport";
    // 日志
    private static String M_REPORTDAYLOG_URL = "life/daylogreport";
    //读取里程
    private static String M_MILESINFO        = "remote/getMilesInfos";

    private static String M_MAINTAIN_LOG = "car/maintainLog";

    private static String M_REMOTE_WARNINGLAMP    = "remote/warningLamp";
    //实时车况
    private static String M_REMOTE_STATUS         = "remote/status";
    //车辆状态
    private static String M_REMOTE_STATE          = "remote/state";
    //胎压监测
    private static String M_REMOTE_DRIECTRRESSURE = "remote/directPressure";

    // 车秘书提醒
    private static String M_SAFETY_MESSAGE_URL         = "life/message";
    // 安防提醒
    private static String M_SECURITY_MESSAGE_URL       = "life/securityMessage";
    //获取车型
    private static String M_CAR_MODE_LIST              = "comm/getModelList";
    //获取车款
    private static String M_CAR_TYPE_LIST              = "comm/getCarList";
    //添加车款
    private static String M_CAR_ADD_CAR                = "car/addCar";
    //绑定设备
    private static String M_DEVICE_BIND_CAR            = "car/bindVinDevice";
    //远程操作日志
    private static String M_CAR_REMOTE_LOG_OPERATION   = "carRelated/getRemoteOperationLog";
    //密码找回
    private static String M_PASSWORD_RETRIEVE          = "safe/retrievePassword";
    //绑定手机老手机提交
    private static String M_AUTH_MOBILE                = "safe/authMobile";
    //绑定手机新手机提交
    private static String M_EDIT_MOBILE                = "safe/editmobile";
    //密码找回
    private static String M_AUTH_SET_VALIDATE          = "user/setValidate";
    //设备激活
    private static String M_DEVICE_ACTIVATE            = "remote/deviceActive";
    //远程启动
    private static String M_DEVICE_REMOTE_START        = "remote/start";
    //远程熄火
    private static String M_DEVICE_REMOTE_STALL        = "remote/stall";
    //声光寻车
    private static String M_DEVICE_REMOTE_CARLOCATING  = "remote/carLocating";
    //远程开关窗
    private static String M_DEVICE_REMOTE_WINDOW       = "remote/window";
    //远程开闭锁
    private static String M_DEVICE_REMOTE_LOCK         = "remote/lock";
    //开启关闭后备箱
    private static String M_DEVICE_REMOTE_TRUNK        = "remote/trunk";
    //远程天窗
    private static String M_DEVICE_REMOTE_SKYLIGHT     = "remote/skyLight";
    //远程开关空调
    private static String M_DEVICE_REMOTE_AIRCONDITION = "remote/aircondition";
    //验证登录密码
    private static String M_USERCENTER_CHECK_PWD       = "safe/checkPassword";
    //修改登录密码
    private static String M_USERCENTER_EDIT_PWD        = "safe/editPassword";
    //校验远程密码
    private static String M_REMOTEPWDVERIFY            = "safe/remotePwdVerify";
    //修改远程密码
    private static String M_RESET_REMOTE_PWD           = "safe/resetRemotePwd";
    //重置远程密码
    private static String M_FORGET_REMOTE_PWD          = "safe/forgetRemotePwd";
    //消息中心开关
    private static String M_USER_REMOTE_SWITCH         = "safe/userSwitch";
    //消息中心开关
    private static String M_CAR_MODIFY                 = "car/editCarInfo";
    //远程控制音效开关
    private static String M_CONTROL_SOUND              = "safe/remoteControlsound";
    //获取推送设置
    private static String M_GET_PUSH_SET               = "user/getPushSet";
    //更新推送设置
    private static String M_UPDATE_PUSH_SET            = "user/updatePushSet";

    //获取经销商信息
    private static String M_GET_DEALER_INFO = "dealer/getDealerInfo";
    //修改用户信息
    private static String M_USER_EDIT_INFO  = "user/editinfo";

    // 安全-设置远程密码
    private static String M_SAFE_SETREMOTEPWD_URL = "safe/setRemotePwd";
    // 获取车辆位置信息
    private static String M_CAR_GETCAREXTINFO     = "car/getCarExtInfo";

    // 获取导航同步到车--改为远程相关接口
    private static String M_NAVIGATION_URL  = "remote/navigation";
    // 获取导航同步到车--改为远程相关接口
    private static String M_OSS_UPLOAD_URL  = "oss/upload";
    // 获取车辆详情
    private static String M_GET_CAR_SETTING = "car/getCarSetting";

    // 查询新版本
    private static String M_GET_APP_UPDATE = "comm/appUpdate";

    //信鸽token保存
    private static String M_REGISTERXGPUSH_URL = "user/saveXingeToken";

    //注销信鸽token
    private static String M_REMOVERXGPUSH_URL = "user/clearXingeToken";

    public static String getM_REMOVERXGPUSH_URL() {
        return getDorideURL(M_REMOVERXGPUSH_URL);
    }

    public static String getM_REGISTERXGPUSH_URL() {
        return getDorideURL(M_REGISTERXGPUSH_URL);
    }


    public static String getM_NAVIGATION_URL() {
        return getUrlRemote(M_NAVIGATION_URL);
    }

    public static String getM_CAR_GETCAREXTINFO_URL() {
        return getDorideURL(M_CAR_GETCAREXTINFO);
    }

    public static String getM_SAFETY_MESSAGE_URL() {
        return getDorideURL(M_SAFETY_MESSAGE_URL);
    }
    public static String getM_REMOTE_CAR_MILES_INFO_URL() {
        return getUrlRemote(M_REMOTE_CAR_MILES_INFO_URL);
    }
    /**
     * 获取安防提醒
     */
    public static String getM_SECURITY_MESSAGE_URL() {
        return getDorideURL(M_SECURITY_MESSAGE_URL);
    }

    public static String getM_REPORTDAY_URL() {
        return getDorideURL(M_REPORTDAY_URL);
    }

    public static String getM_REPORTDAYLOG_URL() {
        return getDorideURL(M_REPORTDAYLOG_URL);
    }

    public static String getM_MILESINFO_URL() {
        return getUrlRemote(M_MILESINFO);
    }

    public static String getM_MAINTAIN_LOG() {
        return getDorideURL(M_MAINTAIN_LOG);
    }

    public static String getM_GET_DEALER_INFO() {
        return getDorideURL(M_GET_DEALER_INFO);
    }

    public static String getM_USER_EDIT_INFO() {
        return getDorideURL(M_USER_EDIT_INFO);
    }

    public static String getM_LOGIN_URL() {
        return getDorideURL(M_LOGIN_NEW_URL);
    }

    public static String getM_REGISTER_NEW_URL() {
        return getDorideURL(M_REGISTER_NEW_URL);
    }

    public static String getM_OPTIONLIST_URL() {
        return getDorideURL(M_OPTIONLIST_URL);
    }

    public static String getM_CARLIST_URL() {
        return getDorideURL(M_CARLIST_URL);
    }

    public static String getM_CAREER_URL() {
        return getDorideURL(M_CAREER_URL);
    }

    public static String getM_VALIDATE_NEW_URL() {
        return getDorideURL(M_VALIDATE_NEW_URL);
    }

    public static String getM_MONTHREPORT_URL() {
        return getDorideURL(M_MONTHREPORT_URL);
    }

    public static String getM_REPORTDATE_URL() {
        return getDorideURL(M_REPORTDATE_URL);
    }

    public static String getM_MONTHREPORTSTATISTIC_URL() {
        return getDorideURL(M_MONTHREPORTSTATISTIC_URL);
    }

    public static String getM_GETCOOR_URL() {
        return getDorideURL(M_GETCOOR_URL);
    }

    public static String getM_USER_MONTH_POINT_URL() {
        return getDorideURL(M_USER_MONTH_POINT_URL);
    }

    public static String getM_USER_DAY_POINT_URL() {
        return getDorideURL(M_USER_DAY_POINT_URL);
    }

    public static String getM_SECRETARY_CATEGORY_URL() {
        return getDorideURL(M_SECRETARY_CATEGORY_URL_NEW);
    }

    public static String getM_SECRETARY_DELETE_URL() {
        return getDorideURL(M_SECRETARY_DELETE_URL_NEW);
    }

    public static String getM_SAFE_SETREMOTEPWD_URL() {
        return getDorideURL(M_SAFE_SETREMOTEPWD_URL);
    }

    public static String getM_CAR_CURCARCONFIG_URL() {
        return getDorideURL(M_CAR_CURCARCONFIG_URL);
    }

    public static String getM_SWITCHCAR_URL() {
        return getUrlRemote(M_SWITCHCAR_URL);
    }

    public static String getM_REMOTE_WARNINGLAMP() {
        return getUrlRemote(M_REMOTE_WARNINGLAMP);
    }

    public static String getM_CAR_MAIN_URL() {
        return getDorideURL(M_CAR_MAIN_URL);
    }

    public static String getM_DEVICEUPDATE_URL() {
        return getDorideURL(M_DEVICEUPDATE_URL);
    }

    public static String getM_USER_ACCESSTOKEN() {
        return getDorideURL(M_USER_ACCESSTOKEN);
    }

    public static String getM_REMOTE_STATUS() {
        return getUrlRemote(M_REMOTE_STATUS);
    }

    public static String getM_REMOTE_STATE() {
        return getUrlRemote(M_REMOTE_STATE);
    }

    public static String getM_REMOTE_DRIECTRRESSURE() {
        return getUrlRemote(M_REMOTE_DRIECTRRESSURE);
    }

    public static String getM_CAR_MODE_LIST() {
        return getDorideURL(M_CAR_MODE_LIST);
    }

    public static String getM_CAR_TYPE_LIST() {
        return getDorideURL(M_CAR_TYPE_LIST);
    }

    public static String getM_CAR_ADD_CAR() {
        return getDorideURL(M_CAR_ADD_CAR);
    }

    public static String getM_DEVICE_BIND_CAR() {
        return getDorideURL(M_DEVICE_BIND_CAR);
    }

    public static String getM_CAR_REMOTE_LOG_OPERATION() {
        return getDorideURL(M_CAR_REMOTE_LOG_OPERATION);
    }

    public static String getM_PASSWORD_RETRIEVE() {
        return getDorideURL(M_PASSWORD_RETRIEVE);
    }

    public static String getM_DEVICE_ACTIVATE() {
        return getUrlRemote(M_DEVICE_ACTIVATE);
    }

    public static String getM_AUTH_MOBILE() {
        return getDorideURL(M_AUTH_MOBILE);
    }

    public static String getM_AUTH_SET_VALIDATE() {
        return getDorideURL(M_AUTH_SET_VALIDATE);
    }

    public static String getM_DEVICE_REMOTE_START() {
        return getUrlRemote(M_DEVICE_REMOTE_START);
    }

    public static String getM_DEVICE_REMOTE_STALL() {
        return getUrlRemote(M_DEVICE_REMOTE_STALL);
    }

    public static String getM_DEVICE_REMOTE_CARLOCATING() {
        return getUrlRemote(M_DEVICE_REMOTE_CARLOCATING);
    }

    public static String getM_DEVICE_REMOTE_WINDOW() {
        return getUrlRemote(M_DEVICE_REMOTE_WINDOW);
    }

    public static String getM_DEVICE_REMOTE_TRUNK() {
        return getUrlRemote(M_DEVICE_REMOTE_TRUNK);
    }

    public static String getM_DEVICE_REMOTE_SKYLIGHT() {
        return getUrlRemote(M_DEVICE_REMOTE_SKYLIGHT);
    }

    public static String getM_DEVICE_REMOTE_AIRCONDITION() {
        return getUrlRemote(M_DEVICE_REMOTE_AIRCONDITION);
    }

    public static String getM_DEVICE_REMOTE_LOCK() {
        return getUrlRemote(M_DEVICE_REMOTE_LOCK);
    }

    public static String getM_EDIT_MOBILE() {
        return getDorideURL(M_EDIT_MOBILE);
    }

    public static String getM_USERCENTER_CHECK_PWD() {
        return getDorideURL(M_USERCENTER_CHECK_PWD);
    }

    public static String getM_USERCENTER_EDIT_PWD() {
        return getDorideURL(M_USERCENTER_EDIT_PWD);
    }

    public static String getM_REMOTEPWDVERIFY() {
        return getDorideURL(M_REMOTEPWDVERIFY);
    }

    public static String getM_RESET_REMOTE_PWD() {
        return getDorideURL(M_RESET_REMOTE_PWD);
    }

    public static String getM_FORGET_REMOTE_PWD() {
        return getDorideURL(M_FORGET_REMOTE_PWD);
    }

    public static String getM_USER_REMOTE_SWITCH() {
        return getDorideURL(M_USER_REMOTE_SWITCH);
    }

    public static String getM_CAR_MODIFY() {
        return getDorideURL(M_CAR_MODIFY);
    }

    public static String getM_CONTROL_SOUND() {
        return getDorideURL(M_CONTROL_SOUND);
    }

    public static String getM_PUSH_SET() {
        return getDorideURL(M_GET_PUSH_SET);
    }

    public static String getM_UPDATE_PUSH_SET() {
        return getDorideURL(M_UPDATE_PUSH_SET);
    }

    public static String getM_OSS_UPLOAD_URL() {
        return getDorideURL(M_OSS_UPLOAD_URL);
    }

    public static String getM_GET_CAR_SETTING() {
        return getDorideURL(M_GET_CAR_SETTING);
    }

    public static String getM_GET_APP_UPDATE() {
        return getDorideURL(M_GET_APP_UPDATE);
    }

    /***********************旅行相册（其他接口在上面添加）***********************/
    //测试服
    private static final String ALBUM_TEST_URL   = "http://imgcloud.linewin.cc/";
    //预发服
    private static final String ALBUM_PRE_URL    = "http://imgcloud.geni4s.com/";
    //正式服
    private static final String ALBUM_FORMAL_URL = "http://imgcloud.geni4s.com/";
    //相册列表查询
    public static final  String ALBUM_QUERY      = "query";
    //相册列表查询
    public static final  String ALBUM_DELETE     = "delete";

    public static String getAlbumUrl(String path) {
        String version = DorideApplication.Version_API + "/";
        String url = "";
        // 正常版
        if (DorideApplication.Formal_Version) {
            url = ALBUM_FORMAL_URL + version + path;
        } else {
            switch (flag) {
                case VERSION_FORMAL:
                    // 正式服
                    url = ALBUM_FORMAL_URL + version + path;
                    break;
                case VERSION_PREPARE:
                    // 预发布服
                    url = ALBUM_PRE_URL + version + path;
                    break;
                case VERSION_TEST:
                    // 测试服
                    url = ALBUM_TEST_URL + version + path;
                    break;
            }
        }

        return url;
    }
}
