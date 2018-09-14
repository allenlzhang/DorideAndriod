package com.carlt.sesame.systemconfig;

import com.carlt.doride.DorideApplication;

import java.util.ArrayList;

public class URLConfig {
    // public static boolean flag = false;

    public final static int VERSION_FORMAL  = 1001;// 正式服
    public final static int VERSION_PREPARE = 1002;// 预发布服
    public final static int VERSION_TEST    = 1003;// 测试服

    // 测试服务器
    private final static String C1   = "0896756ebec5bc62a51b15b9a7541901";
    // 正式服务器
    private final static String C2   = "890ce20d220196ed6dbb0f51793e44ef";
    // 众泰API域名 测试服
    private final static String U5   = "http://electricapi.linewin.cc/";
    // 众泰API域名 测试服_远程下发相关
    private final static String U_R5 = "http://remote-electricapi.linewin.cc/";
    // 众泰API域名 正式服
    private final static String U6   = "https://electricapi.geni4s.com/";
    // 众泰API域名 正式服_远程下发相关
    private final static String U_R6 = "https://remote-electricapi.geni4s.com/";
    // 众泰API域名-预发布
    private final static String U7   = "https://pre-electricapi.geni4s.com/";
    // 众泰API域名-预发布
    private final static String U_R7 = "https://pre-remote-electricapi.geni4s.com/";
    // 众泰API域名-游客版 测试服
    private final static String U8   = "http://zlinkdemoapi.linewin.cc/";
    // 众泰API域名-游客版 正式服
    private final static String U9   = "https://zlinkdemoapi.geni4s.com/";
    // 众泰API域名-游客版 预发布服
    private final static String U10  = "https://pre-zlinkdemoapi.geni4s.com/";

    public final static String CAR_BREAK_URL = "http://v.juhe.cn/wz/query";// 车辆违章查询

    public final static String JUHE_APPKEY = "fa80021879c30853fa80137f1110c2a6";// 聚合AppKey

    public final static String CITYS_INFO_URL = "http://v.juhe.cn/wz/citys";// 聚合获取所有城市信息

    public final static String CITYS2_INFO_URL = "http://test.cx580.com:9000/InputsCondition.aspx";// 车行易

    public final static String VALIDATION_INFO_URL = "http://test.cx580.com:9000/CFTQueryindex.aspx";// 车行易，违章查询接口

    // 天气
    // private static String M_WEATHER_URL =
    // "http://op.juhe.cn/onebox/weather/query"; weather_uri =
    private static String            M_WEATHER_URL = "http://api.map.baidu.com/telematics/v3/weather";
    static         String[]          urlArr        = new String[]{};
    static         ArrayList<String> urlArrayList  = new ArrayList<>();


    // 获取众泰API URL
    private static String getZLinkURL(String s) {
        String version = DorideApplication.Sesame_Version_API + "/";
        String url = "";
        // 正常版
        if (DorideApplication.Formal_Version) {
            url = U6 + version + s;
        } else {
            switch (com.carlt.doride.systemconfig.URLConfig.flag) {
                case VERSION_FORMAL:
                    // 正式服
                    if (urlArrayList.contains(s)) {
                        url = com.carlt.doride.systemconfig.URLConfig.U1_DORIDE + version + s;
                    } else {
                        url = U6 + version + s;
                    }

                    break;

                case VERSION_PREPARE:
                    // 预发布服
                    if (urlArrayList.contains(s)) {
                        url = com.carlt.doride.systemconfig.URLConfig.U1_DORIDE_PRE + version + s;
                    } else {
                        url = U7 + version + s;
                    }

                    break;
                case VERSION_TEST:
                    // 测试服
                    if (urlArrayList.contains(s)) {
                        url = com.carlt.doride.systemconfig.URLConfig.U1_DORIDE_TEST + version + s;
                    } else {
                        url = U5 + version + s;
                    }

                    break;
            }
        }

        return url;
    }


    // 获取远程相关API URL
    private static String getZLinkRemoteURL(String s) {
        if (DorideApplication.Formal_Version) {
            return U_R6 + DorideApplication.VERSION_API_REMOTE + "/" + s;
        } else {
            switch (com.carlt.doride.systemconfig.URLConfig.flag) {
                case VERSION_FORMAL:
                    // 正式服
                    return U_R6 + DorideApplication.VERSION_API_REMOTE + "/" + s;
                case VERSION_PREPARE:
                    // 预发布服
                    return U_R7 + DorideApplication.VERSION_API_REMOTE + "/" + s;
                case VERSION_TEST:
                    // 测试服
                    return U_R5 + DorideApplication.VERSION_API_REMOTE + "/" + s;
                default:
                    return U_R5 + DorideApplication.VERSION_API_REMOTE + "/" + s;
            }
        }
    }

    public static void addUrl() {
        urlArrayList.add("user/accesstoken");
        urlArrayList.add("user/editinfo");
        urlArrayList.add("safe/checkPassword");
        urlArrayList.add("safe/retrievePassword");
        urlArrayList.add("safe/editPassword");
        urlArrayList.add("safe/authMobile");
        urlArrayList.add("safe/editmobile");
        urlArrayList.add("safe/setRemotePwd");
        urlArrayList.add("safe/remotePwdVerify");
        urlArrayList.add("safe/resetRemotePwd");
        urlArrayList.add("safe/forgetRemotePwd");
    }

    public static String getClientID() {
        String clientId = "";
        if (DorideApplication.Formal_Version) {
            clientId = C2;
        } else {
            switch (com.carlt.doride.systemconfig.URLConfig.flag) {
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


    // 新版登录
    private static String M_LOGIN_NEW_URL = "user/login";

    // 超级登录接口
    private static String M_LOGIN_SUPER_URL = "user/superLogin";

    // 新版注册
    private static String M_REGISTER_NEW_URL = "user/register";

    // 新版获取用户信息
    private static String M_USERINFO_NEW_URL = "user/getuserinfo";

    /**
     * 验证登录密码
     */
    private static String M_USERCENTER_CHECK_PWD = "safe/checkPassword";

    /**
     * 远程音效控制开关 1,开启 0 关闭
     */
    private static String M_SAFE_REMOTE_CTL_SOUND_URL = "safe/remoteControlsound";

    /**
     * 通用，，，应用设置
     */
    private static String M_SAFE_APPSETTING_URL = "safe/appSetting";

    // 新版发送验证码
    private static String M_VALIDATE_NEW_URL = "user/setValidate";

    // 新版校验验证码
    private static String M_VALIDATE_CHECK_URL = "user/checkValidate";

    // 获取用户其他信息
    private static String M_USEROTHERINFO_URL = "user/getUserOtherInfo";

    // 新版初始化违章信息
    private static String M_INITVIOLATION_NEW_URL = "car/initViolation";

    // 车辆更换设备
    private static String M_CAR_CHANGEDEVICE_URL = "car/changeDevice";

    // 新版获取推送设置
    private static String M_PUSH_SET_URL_NEW = "user/getPushSet";

    // 新版更新推送设置
    //private static String M_UPDATE_PUSH_URL_NEW = "user/updatePushSet";
    // 新版更新推送设置-v1.2.2版本改为杨鋆远程接口
    private static String M_UPDATE_PUSH_URL_NEW = "remote/updatePushSet";

    // 车秘书分类
    private static String M_SECRETARY_CATEGORY_URL_NEW = "life/messageCategory";

    // 删除车秘书消息
    private static String M_SECRETARY_DELETE_URL_NEW = "life/deleteMessage";

    // 车秘书消息
    private static String M_SECRETARY_MESSAGE_URL_NEW = "life/message";

    // 版本更新
    private static String M_VERSION_URL     = "comm/appupdate";
    //    获取最新版本信息
    private static String M_CUR_VERSION_URL = "comm/getCurVersion";

    // 获取经销商车型列表
    private static String M_BRANDLLIST_URL = "comm/getBrandList";

    // 获取经销商车型列表(针对车款列表)
    private static String M_MODELLIST_URL = "comm/getModelList";

    // 获取经销商车型列表（针对车系列表-二级列表）
    // private static String M_CARLIST_URL = "comm/getCarList";

    // 获取芝麻车款（针对车系列表-二级列表）
    private static String M_CARLIST_URL = "comm/getDomyCarList";

    // 获取生涯首页
    private static String M_CAREER_URL = "life/lifeIndex";

    // 挑战模式列表
    private static String M_CHALLENGELIST_URL = "life/challengeList";

    // 修改密码
    private static String M_EDITPASSWORD_URL = "safe/editPassword";

    // 修改手机-第一步验证旧手机
    private static String M_AUTHMOBILE_URL = "safe/authMobile";

    // 修改手机-第二步新手机修改
    private static String M_EDITMOBILE_URL = "safe/editmobile";

    // 找回密码
    private static String M_RETRIEVEPASSWORD_URL = "safe/retrievePassword";

    // 我的驾驶证信息
    private static String M_MYLICENCE_URL = "life/licenseIndex";

    // 月报周报日报
    private static String M_REPORT_URL = "life/report";

    // 行车报告日期
    private static String M_REPORTDATE_URL = "life/reportdate";

    // 行车日志
    private static String M_CARLOG_URL = "life/daylogreport";

    // 轨迹回放
    private static String M_GETCOOR_URL = "gps/getCoor";

    // 日历信息--月报
    private static String M_USER_MONTH_POINT_URL = "life/usermonthpoint";

    // 日历信息--周报
    private static String M_USER_WEEK_POINT_URL = "life/userweekpoint";

    // 获取TOKEN
    private static String M_USER_ACCESSTOKEN = "user/accesstoken";

    // 用户 - 信鸽token保存
    private static String M_USER_SAVEXINGETOKEN_URL = "user/saveXingeToken";

    // 日历信息--日报
    private static String M_USER_DAY_POINT_URL = "life/userdaypoint";

    // 绑定设备
    private static String M_CAR_BINDDEVICE = "car/bindDevice";

    // 获取二维码
    private static String M_TRANSFER_GET_QRCODE_URL = "caroutting/sweepMake";

    // 新车主取消
    private static String M_TRANSFER_NEW_CANCLE_URL = "caroutting/cancelOutting";

    // 旧车主操作过户
    private static String M_TRANSFER_OLD_OPERATION = "caroutting/dealOutting";

    // 新车主操作过户
    private static String M_TRANSFER_NEW_OPERATION = "caroutting/sweepQrcode";

    // 旧车主检测有没有过户请求
    private static String M_TRANSFER_OLD_CHECKING = "caroutting/hasOutting";

    // 新车主检测过户结果
    private static String M_TRANSFER_NEW_CHECKING = "caroutting/outtingStatus";

    // 我的奖品列表
    private static String M_PRIZELIST_URL = "life/prize";

    // 我的奖品详情
    private static String M_PRIZEDETAIL_URL = "life/prizedetail";

    // 获取推荐顾问
    private static String M_RECOMMENDSALES_URL = "life/recommendsales";

    // 获取车秘书消息详情
    private static String M_SECRETARY_GETBIID_URL = "life/getByIdMessage";

    // 安防消息消息
    private static String M_SAFETY_MESSAGE_URL = "life/message";

    // 获取违章信息列表
    private static String M_TRAFFICVIOLATION_URL = "car/getTrafficViolation";

    // 保存违章车辆信息
    private static String M_SAVECAR_URL = "violation/saveCar";

    // 获取违章车辆列表
    private static String M_GETCARLIST_URL = "violation/getCarList";

    // 获取违章车辆
    private static String M_GETCARINFO_URL = "violation/getCarInfo";

    // 删除违章车辆
    private static String M_DELCAR_URL = "violation/delCar";

    // 设置为我的车辆
    private static String M_SETMYCAR_URL = "violation/setMyCar";

    // 获取首次违章查询信息列表
    private static String M_INITVIOLATION_URL = "car/initViolation";

    // 获取实时车况列表
    // private static String M_CAR_STATU_URL = "car/status";
    // 获取实时车况列表-v1.2.2版本改为杨鋆远程接口
    private static String M_CAR_STATU_URL = "remote/status";

    // 获取导航同步到车
    // private static String M_CARRELATED_NAVIGATION_URL =
    // "carRelated/navigation";
    // 获取导航同步到车-v1.2.2版本改为杨鋆远程接口
    private static String M_CARRELATED_NAVIGATION_URL = "remote/navigation";

    //获取用户绑定车款配置
    private static String M_OPERATIONCONFIG_URL = "car/operationConfig";

    // 获取某月预约状态
    private static String M_MONTHORDERSTATE_URL = "finding/monthorderstate";

    // 获取某天预约状态
    private static String M_DAYORDERSTATE_URL = "finding/dayorderstate";

    // 提交预约
    private static String M_SUBMITORDER_URL = "finding/submitorder";

    // 领取奖品
    private static String M_RECEIVEPRIZE_URL = "life/getprize";

    // 活动报名
    private static String M_ACTIVITYSIGN_URL = "life/sign";

    // 获取故障码列表
    private static String M_CHECKFAULT_URL = "car/check";

    // 获取远程诊断列表
    private static String M_DIAGNOSE_URL = "carRelated/remoteDiagnose";

    // 发送问题
    private static String M_SUBMITPROBLEM_URL = "life/submitproblem";

    // 获取故障码列表
    private static String M_HELPPHONE_URL = "car/helpphone";

    // 获取胎压数据
    // private static String M_TIREPRESSURE_URL = "car/tirepressure";
    // 获取胎压数据-v1.2.2版本改为杨鋆远程接口
    private static String M_TIREPRESSURE_URL = "remote/tirepressure";

    // 获取直接式胎压检测数据
    // private static String M_DIRECTPRESSURE_URL = "carRelated/directPressure";
    // 获取直接式胎压检测数据-v1.2.2版本改为杨鋆远程接口
    private static String M_DIRECTPRESSURE_URL = "remote/directPressure";

    // 获取胎压激活进度
    private static String M_TIREPROGRESS_URL = "car/tireprogress";

    // 胎压自学习
    // private static String M_TIREPRESLEARN_URL = "car/startlearn";
    // 胎压自学习-v1.2.2版本改为杨鋆远程接口
    private static String M_TIREPRESLEARN_URL = "remote/startlearn";

    // 获取开始挑战信息
    private static String M_STARTCHALLENGE_URL = "life/startchallenge";

    // 获取终止挑战信息
    private static String M_ENDCHALLENGE_URL = "life/endchallenge";

    // 获取当次挑战成绩信息
    private static String M_CHALLENGESCORE_URL = "life/challengeinfo";

    // 获取挑战的历史最佳成绩
    private static String M_CHALLENGEBEST_URL = "life/challengebest";

    // 获取我的好友列表
    private static String M_FRIEND_LIST_URL = "group/friend";

    // 获取添加或者解除好友
    private static String M_UPDATEFRIEND_URL = "group/updatefriend";

    // 获取根据手机号搜索好友
    private static String M_SEARCHFRIEND_URL = "group/searchFriend";

    // 获取我的好友详情
    private static String M_FRIEND_DETIAL_URL = "group/getFriendUserinfo";

    // 获取我好友动态列表
    private static String M_FRIENDS_SHARE_LIST_URL = "group/feed";

    // 获取我的动态列表
    private static String M_FRIENDS_MYFEED_LIST_URL = "finding/myFeed";

    // 获取动态详情
    private static String M_FEED_DETIAL_URL = "finding/myFeedDetail";

    // 删除动态
    private static String M_DELETE_FEED_URL = "finding/deleteFeed";

    // 获取车友动态点赞
    private static String M_FRIENDS_POKE_URL = "group/poke";

    // 获取我好友PK
    private static String M_FRIENDS_PK_URL = "group/pk";

    // 更新推送设置
    private static String M_BIND_DEVICE_URL = "user/bind";

    // 上传图片接口
    private static String M_UPLOAD_IMG_URL = "oss/upload";

    // 更新用户信息
    private static String M_EDITINFO_URL = "user/editinfo";

    // 修改车辆信息-车型信息
    //private static String M_MODIFYCAR_URL = "car/modifyCar";
    // 修改车辆信息-车型信息-v1.2.2版本改为杨鋆远程接口
    private static String M_MODIFYCAR_URL = "remote/switchCar";

    // 修改车辆信息-购车时间、上次保养里程、上次保养时间
    private static String M_EDITCARINFO_URL = "car/editCarInfo";

    // 记录养护日志
    private static String M_MAINTAINLOG_URL = "car/maintainLog";

    // 初始化设置车型
    private static String M_SET_CAR_INFO_URL = "car/addCar";

    // 获取养护建议
    private static String M_RECOMMEND_MAINTAIN_URL = "car/recommendMaintain";

    // 邀请好友奖品列表
    private static String M_INVITE_PRIZE_URL = "group/invitePrize";

    // 座驾首页
    private static String M_CAR_MAIN_URL = "car/carIndex";

    // 分享行车报告回调
    private static String M_REPORTSHARE_URL = "life/reportShare";

    // 分享驾驶证等级回调
    private static String M_LICENSESHARE_URL = "life/licenseShare";

    // 激活设备
    // private static String M_DEVICEACTIVATE_URL = "user/deviceActivate";
    // 激活设备-v1.2.2版本改为杨鋆远程接口
    private static String M_DEVICEACTIVATE_URL = "remote/deviceActive";

    // 获取设备升级状态
    private static String M_DEVICEUPDATE_URL = "user/checkIsUpgrade";

    // 设备是否在线
    private static String M_CAROBD_URL = "user/carobd";

    // 检测PIN码
    private static String M_CHECKPIN_URL = "user/checkPin";

    // 获取用户相关信息
    private static String M_GETEXTINFO_URL = "user/getExtInfo";

    // 远程首页
    private static String M_REMOTE_INDEX_URL = "carRelated/remoteIndex";

    // 远程校验远程密码
    private static String M_SAFE_REMOTEPWDVERIFY_URL = "safe/remotePwdVerify";

    // 远程声光寻车
    // private static String M_CAR_LOCATING_URL =
    // "carRelated/remoteCarLocating";
    // 远程声光寻车-v1.2.2版本改为杨鋆远程接口
    private static String M_CAR_LOCATING_URL = "remote/carLocating";

    // 远程自动升窗
    // private static String M_AUTOCLOSEWIN_URL = "car/autoCloseWinSw";
    // 远程自动升窗-v1.2.2版本改为杨鋆远程接口
    private static String M_AUTOCLOSEWIN_URL = "remote/autoCloseWinSw";

    // 远程启动
    // private static String M_REMOTESTART_URL = "carRelated/remoteStart";
    // 远程启动-v1.2.2版本改为杨鋆远程接口
    private static String M_REMOTESTART_URL = "remote/start";

    // 取消远程启动
    // private static String M_CANCEL_REMOTESTART_URL =
    // "carRelated/cancelRemoteStart";
    // 取消远程启动-v1.2.2版本改为杨鋆远程接口
    private static String M_CANCEL_REMOTESTART_URL = "remote/stall";

    // 远程开关后备箱
    // private static String M_OPENTRUNK_URL = "carRelated/opentrunk";
    // private static String M_OPENTRUNK_URL = "carRelated/remoteTrunk";
    // 远程开关后备箱-v1.2.2版本改为杨鋆远程接口
    private static String M_OPENTRUNK_URL = "remote/trunk";

    // 远程开关空调
    // private static String M_AIRCONDITIONER_URL =
    // "carRelated/remoteAirconditioner";
    // 远程开关空调-v1.2.2版本改为杨鋆远程接口
    private static String M_AIRCONDITIONER_URL = "remote/aircondition";

    // 远程开关窗
    // private static String M_WINDOW_URL = "carRelated/remoteWindow";
    // 远程开关窗-v1.2.2版本改为杨鋆远程接口
    private static String M_WINDOW_URL = "remote/window";

    // 远程开闭锁
    // private static String M_LOCKING_URL = "carRelated/remoteLock";
    // 远程开闭锁-v1.2.2版本改为杨鋆远程接口
    private static String M_LOCKING_URL = "remote/lock";

    // 远程充电
    private static String M_CHARGE_URL = "remote/charge";

    // 取消定时充电
    private static String M_CANCLETIMECHARGE_URL = "remote/cancleTimeCharge";

    // 远程充电状态
    private static String M_CHARGESTATUS_URL = "carRelated/chargeStatus";

    // 远程车辆状态
    //private static String M_STATUS_URL = "carRelated/getVehicleStatus";
    // 远程车辆状态-v1.2.2版本改为杨鋆远程接口
    private static String M_STATUS_URL = "remote/state";

    // 远程操作记录
    private static String M_REMOTEOPERATION_URL = "carRelated/getRemoteOperationLog";

    // 获取车辆管理信息
    private static String M_GETCARSETTING_URL = "car/getCarSetting";

    // 获取发现首页
    private static String M_FINDING_URL = "finding/findingIndex";

    // 申报列表
    private static String M_SOS_LIST_URL = "finding/mySOS";

    // 申報詳情
    private static String M_SOS_DETIAL_URL = "finding/mySOSDetail";

    // 预约列表
    private static String M_ORDER_LIST_URL = "finding/myOrder";

    // 预约详情
    private static String M_ORDER_DETIAL_URL = "finding/myOrderDetail";

    // 预约评价提交
    private static String M_SUBMIT_EVALUATION_URL = "finding/submitOrderComment";

    // 提交救援请求
    private static String M_SUBMIT_SOS_URL = "finding/submitSOS";

    // 空气净化器
    // private static String M_AIRCYLINDER_URL = "carRelated/remoteAirPurify";
    // 空气净化器-v1.2.2版本改为杨鋆远程接口
    private static String M_AIRCYLINDER_URL = "remote/airPurify";

    // 座椅加热
    // private static String M_HEATSEAT_URL = "carRelated/remoteChairHeating";
    // 座椅加热-v1.2.2版本改为杨鋆远程接口
    private static String M_HEATSEAT_URL = "remote/chairHeating";

    // 安全-安全中心首页
    private static String M_SAFE_SAFEINDEX_URL = "safe/safeIndex";

    // 安全-获取登录日志
    private static String M_SAFE_GETLOGINLOG_URL = "safe/getLoginLog";

    // 安全-获取授权设备列表
    private static String M_SAFE_GETAUTHORIZEDEVICE_URL = "safe/getAuthorizeDevice";

    // 安全-删除授权设备
    private static String M_SAFE_DELAUTHORIZEDEVICE_URL = "safe/delAuthorizeDevice";

    // 安全-冻结、解冻账户
    private static String M_SAFE_FREEZING_URL = "safe/freezing";

    // 安全-发送授权请求
    private static String M_SAFE_SENDAUTHORIZE_URL = "safe/sendAuthorize";

    // 安全-授权页面
    private static String M_SAFE_AUTHORIZEPAGE_URL = "safe/authorizePage";

    // 安全-授权处理
    private static String M_SAFE_DEALAUTHORIZE_URL = "safe/dealAuthorize";

    // 安全-获取授权状态
    private static String M_SAFE_GETAUTHORIZESTATUS_URL = "safe/getAuthorizeStatus";
    //修改主机唯一标识（安卓专用）
    private static String M_UPDATEMOVEDEVICEID          = "user/updateMoveDeviceid";
    // 安全-更改授权状态                               user/updateMoveDeviceid
    private static String M_SAFE_UPDATESWITCH_URL       = "safe/updateSwitch";

    // 安全-实名认证
    private static String M_SAFE_REALAUTHEN_URL = "safe/realauthen";

    // 安全-身份验证
    private static String M_SAFE_AUTHENTICATION_URL = "safe/authentication";

    // 安全-设置远程密码
    private static String M_SAFE_SETREMOTEPWD_URL = "safe/setRemotePwd";

    // 安全-修改远程密码
    private static String M_SAFE_RESETREMOTEPWD_URL = "safe/resetRemotePwd";

    // 安全-重置远程密码
    private static String M_SAFE_FORGETREMOTEPWD_URL = "safe/forgetRemotePwd";

    // 安全-远程控制五分钟无需密码开关
    private static String M_SAFE_UPDATELESSPWDSWITCH_URL = "safe/updateLessPwdSwitch";

    // 安全-更换主机
    private static String M_SAFE_CHANGEMAINDEVICE_URL = "safe/changeMainDevice";

    // 安全-无授权登录
    private static String M_SAFE_NOAUTHORIZELOGIN_URL = "safe/noAuthorizeLogin";

    // 安全-安全验证登录
    private static String M_SAFE_NOAUTHORIZECHANGEDEVICE_URL = "safe/noAuthorizeChangeDevice";

    // 安全-更换主机-主机认证
    private static String M_SAFE_MAINDEVICEAUTHORIZE_URL = "safe/mainDeviceAuthorize";

    // 续费-续费管理首页
    private static String M_RENEW_FEELIST_URL = "renew/feeList";

    // 续费-订单参数
    private static String M_RENEW_ALIPAY_URL = "renew/aliPay";

    // 续费-支付回调验证
    private static String M_RENEW_SYNCAPI_URL = "renew/syncApi";

    // 续费-续费记录列表
    private static String M_RENEW_PAYLOG_URL = "renew/payLog";

    // 获取广告
    private static String M_ADVERT_GETADVERT_URL = "advert/getAdvert";

    // 获取车辆位置信息
    private static String M_CAR_GETCAREXTINFO = "car/getCarExtInfo";

    // 获取经销商信息
    private static String M_DEALER_GETDEALERINFO = "dealer/getDealerInfo";
    //版本更新日志
    private static String M_CAR_GETVERSIONLOG    = "car/getVersionLog";

    public static String getM_SUBMIT_SOS_URL_URL() {
        return getZLinkURL(M_SUBMIT_SOS_URL);
    }

    public static String getM_SUBMIT_EVALUATION_URL() {
        return getZLinkURL(M_SUBMIT_EVALUATION_URL);
    }

    public static String getM_ORDER_LIST_URL() {
        return getZLinkURL(M_ORDER_LIST_URL);
    }

    public static String getM_ORDER_DETIAL_URL() {
        return getZLinkURL(M_ORDER_DETIAL_URL);
    }

    public static String getM_LOGIN_URL() {
        return getZLinkURL(M_LOGIN_NEW_URL);
    }

    public static String getM_VERSION_URL() {
        return getZLinkURL(M_VERSION_URL);
    }

    public static String getM_CUR_VERSION_URL() {
        return getZLinkURL(M_CUR_VERSION_URL);
    }

    public static String getM_REGISTER_NEW_URL() {
        return getZLinkURL(M_REGISTER_NEW_URL);
    }

    public static String getM_BRANDLLIST_URL() {
        return getZLinkURL(M_BRANDLLIST_URL);
    }

    public static String getM_MODELLIST_URL() {
        return getZLinkURL(M_MODELLIST_URL);
    }

    public static String getM_CARLIST_URL() {
        return getZLinkURL(M_CARLIST_URL);
    }

    public static String getM_CAREER_URL() {
        return getZLinkURL(M_CAREER_URL);
    }

    public static String getM_USERINFO_NEW_URL() {
        return getZLinkURL(M_USERINFO_NEW_URL);
    }

    public static String getM_CHALLENGELIST_URL() {
        return getZLinkURL(M_CHALLENGELIST_URL);
    }

    public static String getM_EDITPASSWORD_URL() {
        return getZLinkURL(M_EDITPASSWORD_URL);
    }

    public static String getM_AUTHMOBILE_URL() {
        return getZLinkURL(M_AUTHMOBILE_URL);
    }

    public static String getM_EDITMOBILE_URL() {
        return getZLinkURL(M_EDITMOBILE_URL);
    }

    public static String getM_VALIDATE_NEW_URL() {
        return getZLinkURL(M_VALIDATE_NEW_URL);
    }

    public static String getM_VALIDATE_CHECK_URL() {
        return getZLinkURL(M_VALIDATE_CHECK_URL);
    }

    public static String getM_USEROTHERINFO_URL() {
        return getZLinkURL(M_USEROTHERINFO_URL);
    }

    public static String getM_RETRIEVEPASSWORD_URL() {
        return getZLinkURL(M_RETRIEVEPASSWORD_URL);
    }

    public static String getM_MYLICENCE_URL() {
        return getZLinkURL(M_MYLICENCE_URL);
    }

    public static String getM_REPORT_URL() {
        return getZLinkURL(M_REPORT_URL);
    }

    public static String getM_REPORTDATE_URL() {
        return getZLinkURL(M_REPORTDATE_URL);
    }

    public static String getM_CARLOG_URL() {
        return getZLinkURL(M_CARLOG_URL);
    }

    public static String getM_GETCOOR_URL() {
        return getZLinkURL(M_GETCOOR_URL);
    }

    public static String getM_USER_MONTH_POINT_URL() {
        return getZLinkURL(M_USER_MONTH_POINT_URL);
    }

    public static String getM_USER_WEEK_POINT_URL() {
        return getZLinkURL(M_USER_WEEK_POINT_URL);
    }

    public static String getM_USER_DAY_POINT_URL() {
        return getZLinkURL(M_USER_DAY_POINT_URL);
    }

    public static String getM_PRIZELIST_URL() {
        return getZLinkURL(M_PRIZELIST_URL);
    }

    public static String getM_PRIZEDETAIL_URL() {
        return getZLinkURL(M_PRIZEDETAIL_URL);
    }

    public static String getM_RECOMMENDSALES_URL() {
        return getZLinkURL(M_RECOMMENDSALES_URL);
    }

    public static String getM_SECRETARY_CATEGORY_URL() {
        return getZLinkURL(M_SECRETARY_CATEGORY_URL_NEW);
    }

    public static String getM_SECRETARY_DELETE_URL() {
        return getZLinkURL(M_SECRETARY_DELETE_URL_NEW);
    }

    public static String getM_SECRETARY_GETBIID_URL() {
        return getZLinkURL(M_SECRETARY_GETBIID_URL);
    }

    public static String getM_SECRETARY_MESSAGE_URL() {
        return getZLinkURL(M_SECRETARY_MESSAGE_URL_NEW);
    }

    public static String getM_SAFETY_MESSAGE_URL() {
        return getZLinkURL(M_SAFETY_MESSAGE_URL);
    }

    public static String getM_TRAFFICVIOLATION_URL() {
        return getZLinkURL(M_TRAFFICVIOLATION_URL);
    }

    public static String getM_SAVECAR_URL() {
        return getZLinkURL(M_SAVECAR_URL);
    }

    public static String getM_GETCARLIST_URL() {
        return getZLinkURL(M_GETCARLIST_URL);
    }

    public static String getM_GETCARINFO_URL() {
        return getZLinkURL(M_GETCARINFO_URL);
    }

    public static String getM_DELCAR_URL() {
        return getZLinkURL(M_DELCAR_URL);
    }

    public static String getM_SETMYCAR_URL() {
        return getZLinkURL(M_SETMYCAR_URL);
    }

    public static String getM_INITVIOLATION_URL() {
        return getZLinkURL(M_INITVIOLATION_URL);
    }

    public static String getM_INITVIOLATION_NEW_URL() {
        return getZLinkURL(M_INITVIOLATION_NEW_URL);
    }

    public static String getM_CAR_STATU_URL() {
        return getZLinkRemoteURL(M_CAR_STATU_URL);
    }

    public static String getM_CARRELATED_NAVIGATION_URL() {
        return getZLinkRemoteURL(M_CARRELATED_NAVIGATION_URL);
    }

    public static String getM_OPERATIONCONFIG_URL() {
        return getZLinkURL(M_OPERATIONCONFIG_URL);
    }

    public static String getM_MONTHORDERSTATE_URL() {
        return getZLinkURL(M_MONTHORDERSTATE_URL);
    }

    public static String getM_DAYORDERSTATE_URL() {
        return getZLinkURL(M_DAYORDERSTATE_URL);
    }

    public static String getM_SUBMITORDER_URL() {
        return getZLinkURL(M_SUBMITORDER_URL);
    }

    public static String getM_RECEIVEPRIZE_URL() {
        return getZLinkURL(M_RECEIVEPRIZE_URL);
    }

    public static String getM_ACTIVITYSIGN_URL() {
        return getZLinkURL(M_ACTIVITYSIGN_URL);
    }

    public static String getM_CHECKFAULT_URL() {
        return getZLinkURL(M_CHECKFAULT_URL);
    }

    public static String getM_DIAGNOSE_URL() {
        return getZLinkURL(M_DIAGNOSE_URL);
    }

    public static String getM_SUBMITPROBLEM_URL() {
        return getZLinkURL(M_SUBMITPROBLEM_URL);
    }

    public static String getM_HELPPHONE_URL() {
        return getZLinkURL(M_HELPPHONE_URL);
    }

    public static String getM_TIREPRESSURE_URL() {
        return getZLinkRemoteURL(M_TIREPRESSURE_URL);
    }

    public static String getM_DIRECTPRESSURE_URL() {
        return getZLinkRemoteURL(M_DIRECTPRESSURE_URL);
    }

    public static String getM_TIREPROGRESS_URL() {
        return getZLinkURL(M_TIREPROGRESS_URL);
    }

    public static String getM_TIREPRESLEARN_URL() {
        return getZLinkRemoteURL(M_TIREPRESLEARN_URL);
    }

    public static String getM_STARTCHALLENGE_URL() {
        return getZLinkURL(M_STARTCHALLENGE_URL);
    }

    public static String getM_ENDCHALLENGE_URL() {
        return getZLinkURL(M_ENDCHALLENGE_URL);
    }

    public static String getM_CHALLENGESCORE_URL() {
        return getZLinkURL(M_CHALLENGESCORE_URL);
    }

    public static String getM_CHALLENGEBEST_URL() {
        return getZLinkURL(M_CHALLENGEBEST_URL);
    }

    public static String getM_FRIEND_LIST_URL() {
        return getZLinkURL(M_FRIEND_LIST_URL);
    }

    public static String getM_UPDATEFRIEND_URL() {
        return getZLinkURL(M_UPDATEFRIEND_URL);
    }

    public static String getM_SEARCHFRIEND_URL() {
        return getZLinkURL(M_SEARCHFRIEND_URL);
    }

    public static String getM_FRIEND_DETIAL_URL() {
        return getZLinkURL(M_FRIEND_DETIAL_URL);
    }

    public static String getM_FRIENDS_SHARE_LIST_URL() {
        return getZLinkURL(M_FRIENDS_SHARE_LIST_URL);
    }

    public static String getM_FRIENDS_MYFEED_LIST_URL() {
        return getZLinkURL(M_FRIENDS_MYFEED_LIST_URL);
    }

    public static String getM_FEED_DETIAL_URL() {
        return getZLinkURL(M_FEED_DETIAL_URL);
    }

    public static String getM_DELETE_FEED_URL() {
        return getZLinkURL(M_DELETE_FEED_URL);
    }

    public static String getM_FRIENDS_POKE_URL() {
        return getZLinkURL(M_FRIENDS_POKE_URL);
    }

    public static String getM_FRIENDS_PK_URL() {
        return getZLinkURL(M_FRIENDS_PK_URL);
    }

    public static String getM_PUSH_SET_URL() {
        return getZLinkURL(M_PUSH_SET_URL_NEW);
    }

    public static String getM_UPDATE_PUSH_URL() {
        return getZLinkRemoteURL(M_UPDATE_PUSH_URL_NEW);
    }

    public static String getM_BIND_DEVICE_URL() {
        return getZLinkURL(M_BIND_DEVICE_URL);
    }

    public static String getM_UPLOAD_IMG_URL() {
        return getZLinkURL(M_UPLOAD_IMG_URL);
    }

    public static String getM_EDITINFO_URL() {
        return getZLinkURL(M_EDITINFO_URL);
    }

    public static String getM_MODIFYCAR_URL() {
        return getZLinkRemoteURL(M_MODIFYCAR_URL);
    }

    public static String getM_EDITCARINFO_URL() {
        return getZLinkURL(M_EDITCARINFO_URL);
    }

    public static String getM_MAINTAINLOG_URL() {
        return getZLinkURL(M_MAINTAINLOG_URL);
    }

    public static String getM_INVITE_PRIZE_URL() {
        return getZLinkURL(M_INVITE_PRIZE_URL);
    }

    public static String getM_CAR_MAIN_URL() {
        return getZLinkURL(M_CAR_MAIN_URL);
    }

    public static String getM_REPORTSHARE_URL() {
        return getZLinkURL(M_REPORTSHARE_URL);
    }

    public static String getM_LICENSESHARE_URL() {
        return getZLinkURL(M_LICENSESHARE_URL);
    }

    public static String getM_DEVICEACTIVATE_URL() {
        return getZLinkRemoteURL(M_DEVICEACTIVATE_URL);
    }

    public static String getM_DEVICEUPDATE_URL() {
        return getZLinkURL(M_DEVICEUPDATE_URL);
    }

    public static String getM_CAROBD_URL() {
        return getZLinkURL(M_CAROBD_URL);
    }

    public static String getM_CHECKPIN_URL() {
        return getZLinkURL(M_CHECKPIN_URL);
    }

    public static String getM_GETEXTINFO_URL() {
        return getZLinkURL(M_GETEXTINFO_URL);
    }

    public static String getM_REMOTE_INDEX_URL() {
        return getZLinkURL(M_REMOTE_INDEX_URL);
    }

    public static String getM_SAFE_REMOTEPWDVERIFY_URL() {
        return getZLinkURL(M_SAFE_REMOTEPWDVERIFY_URL);
    }

    public static String getM_CAR_LOCATING_URL() {
        return getZLinkRemoteURL(M_CAR_LOCATING_URL);
    }

    public static String getM_AUTOCLOSEWIN_URL() {
        return getZLinkRemoteURL(M_AUTOCLOSEWIN_URL);
    }

    public static String getM_REMOTESTART_URL() {
        return getZLinkRemoteURL(M_REMOTESTART_URL);
    }

    public static String getM_CANCEL_REMOTESTART_URL() {
        return getZLinkRemoteURL(M_CANCEL_REMOTESTART_URL);
    }

    public static String getM_OPENTRUNK_URL() {
        return getZLinkRemoteURL(M_OPENTRUNK_URL);
    }

    public static String getM_AIRCONDITIONER_URL() {
        return getZLinkRemoteURL(M_AIRCONDITIONER_URL);
    }

    public static String getM_WINDOW_URL() {
        return getZLinkRemoteURL(M_WINDOW_URL);
    }

    public static String getM_LOCKING_URL() {
        return getZLinkRemoteURL(M_LOCKING_URL);
    }

    public static String getM_CHARGE_URL() {
        return getZLinkRemoteURL(M_CHARGE_URL);
    }

    public static String getM_CANCLETIMECHARGE_URL() {
        return getZLinkRemoteURL(M_CANCLETIMECHARGE_URL);
    }

    public static String getM_CHARGESTATUS_URL() {
        return getZLinkURL(M_CHARGESTATUS_URL);
    }

    public static String getM_STATUS_URL() {
        return getZLinkRemoteURL(M_STATUS_URL);
    }

    public static String getM_REMOTEOPERATION_URL() {
        return getZLinkURL(M_REMOTEOPERATION_URL);
    }

    public static String getM_GETCARSETTING_URL() {
        return getZLinkURL(M_GETCARSETTING_URL);
    }

    public static String getM_FINDING_URL() {
        return getZLinkURL(M_FINDING_URL);
    }

    public static String getM_SOS_LIST_URL() {
        return getZLinkURL(M_SOS_LIST_URL);
    }

    public static String getM_SOS_DETIAL_URL() {
        return getZLinkURL(M_SOS_DETIAL_URL);
    }

    public static String getM_SET_CAR_INFO_URL() {
        return getZLinkURL(M_SET_CAR_INFO_URL);
    }

    public static String getM_RECOMMEND_MAINTAIN_URL() {
        return getZLinkURL(M_RECOMMEND_MAINTAIN_URL);
    }

    public static String getM_WHEATER_URL() {
        return M_WEATHER_URL;
    }

    public static String getM_AIRCYLINDER_URL() {
        return getZLinkRemoteURL(M_AIRCYLINDER_URL);
    }

    public static String getM_SEATHEAT_URL() {
        return getZLinkRemoteURL(M_HEATSEAT_URL);
    }

    public static String getM_SAFE_SAFEINDEX_URL() {
        return getZLinkURL(M_SAFE_SAFEINDEX_URL);
    }

    public static String getM_SAFE_GETLOGINLOG_URL() {
        return getZLinkURL(M_SAFE_GETLOGINLOG_URL);
    }

    public static String getM_SAFE_GETAUTHORIZEDEVICE_URL() {
        return getZLinkURL(M_SAFE_GETAUTHORIZEDEVICE_URL);
    }

    public static String getM_SAFE_DELAUTHORIZEDEVICE_URL() {
        return getZLinkURL(M_SAFE_DELAUTHORIZEDEVICE_URL);
    }

    public static String getM_SAFE_FREEZING_URL() {
        return getZLinkURL(M_SAFE_FREEZING_URL);
    }

    public static String getM_SAFE_SENDAUTHORIZE_URL() {
        return getZLinkURL(M_SAFE_SENDAUTHORIZE_URL);
    }

    public static String getM_SAFE_AUTHORIZEPAGE_URL() {
        return getZLinkURL(M_SAFE_AUTHORIZEPAGE_URL);
    }

    public static String getM_SAFE_DEALAUTHORIZE_URL() {
        return getZLinkURL(M_SAFE_DEALAUTHORIZE_URL);
    }

    public static String getM_SAFE_GETAUTHORIZESTATUS_URL() {
        return getZLinkURL(M_SAFE_GETAUTHORIZESTATUS_URL);
    }

    public static String getM_SAFE_UPDATESWITCH_URL() {
        return getZLinkURL(M_SAFE_UPDATESWITCH_URL);
    }

    public static String getM_SAFE_REALAUTHEN_URL() {
        return getZLinkURL(M_SAFE_REALAUTHEN_URL);
    }

    public static String getM_SAFE_AUTHENTICATION_URL() {
        return getZLinkURL(M_SAFE_AUTHENTICATION_URL);
    }

    public static String getM_SAFE_SETREMOTEPWD_URL() {
        return getZLinkURL(M_SAFE_SETREMOTEPWD_URL);
    }

    public static String getM_SAFE_RESETREMOTEPWD_URL() {
        return getZLinkURL(M_SAFE_RESETREMOTEPWD_URL);
    }

    public static String getM_SAFE_FORGETREMOTEPWD_URL() {
        return getZLinkURL(M_SAFE_FORGETREMOTEPWD_URL);
    }

    public static String getM_SAFE_UPDATELESSPWDSWITCH_URL() {
        return getZLinkURL(M_SAFE_UPDATELESSPWDSWITCH_URL);
    }

    public static String getM_SAFE_CHANGEMAINDEVICE_URL() {
        return getZLinkURL(M_SAFE_CHANGEMAINDEVICE_URL);
    }

    public static String getM_SAFE_NOAUTHORIZELOGIN_URL() {
        return getZLinkURL(M_SAFE_NOAUTHORIZELOGIN_URL);
    }

    public static String getM_SAFE_NOAUTHORIZECHANGEDEVICE_URL() {
        return getZLinkURL(M_SAFE_NOAUTHORIZECHANGEDEVICE_URL);
    }

    public static String getM_SAFE_MAINDEVICEAUTHORIZE_URL() {
        return getZLinkURL(M_SAFE_MAINDEVICEAUTHORIZE_URL);
    }

    public static String getM_RENEW_FEELIST_URL() {
        return getZLinkURL(M_RENEW_FEELIST_URL);
    }

    public static String getM_RENEW_ALIPAY_URL() {
        return getZLinkURL(M_RENEW_ALIPAY_URL);
    }

    public static String getM_RENEW_SYNCAPI_URL() {
        return getZLinkURL(M_RENEW_SYNCAPI_URL);
    }

    public static String getM_RENEW_PAYLOG_URL() {
        return getZLinkURL(M_RENEW_PAYLOG_URL);
    }

    public static String getM_CAR_BINDDEVICE_URL() {
        return getZLinkURL(M_CAR_BINDDEVICE);
    }

    public static String getM_USERCENTER_CHECK_PWD() {
        return getZLinkURL(M_USERCENTER_CHECK_PWD);
    }

    public static String getM_USER_ACCESSTOKEN() {
        return getZLinkURL(M_USER_ACCESSTOKEN);
    }

    public static String getM_USER_SAVEXINGETOKEN_URL() {
        return getZLinkURL(M_USER_SAVEXINGETOKEN_URL);
    }

    public static String getM_TRANSFER_GET_QRCODE_URL() {
        return getZLinkURL(M_TRANSFER_GET_QRCODE_URL);
    }

    public static String getM_TRANSFER_OLD_OPERATION() {
        return getZLinkURL(M_TRANSFER_OLD_OPERATION);
    }

    public static String getM_TRANSFER_NEW_OPERATION() {
        return getZLinkURL(M_TRANSFER_NEW_OPERATION);
    }

    public static String getM_TRANSFER_OLD_CHECKING() {
        return getZLinkURL(M_TRANSFER_OLD_CHECKING);
    }

    public static String getM_TRANSFER_NEW_CHECKING() {
        return getZLinkURL(M_TRANSFER_NEW_CHECKING);
    }

    public static String getM_SAFE_REMOTE_CTL_SOUND_URL() {
        return getZLinkURL(M_SAFE_REMOTE_CTL_SOUND_URL);
    }

    public static String getM_SAFE_APPSETTING_URL() {
        return getZLinkURL(M_SAFE_APPSETTING_URL);
    }

    public static String getM_CAR_CHANGEDEVICE_URL() {
        return getZLinkURL(M_CAR_CHANGEDEVICE_URL);
    }

    public static String getM_TRANSFER_NEW_CANCLE_URL() {
        return getZLinkURL(M_TRANSFER_NEW_CANCLE_URL);
    }

    public static String getM_ADVERT_GETADVERT_URL() {
        return getZLinkURL(M_ADVERT_GETADVERT_URL);
    }

    public static String getM_CAR_GETCAREXTINFO() {
        return getZLinkURL(M_CAR_GETCAREXTINFO);
    }

    public static String getM_DEALER_GETDEALERINFO() {
        return getZLinkURL(M_DEALER_GETDEALERINFO);
    }

    public static String getM_CAR_GETVERSIONLOG() {
        return getZLinkURL(M_CAR_GETVERSIONLOG);
    }

    public static String getM_UPDATEMOVEDEVICEID() {
        return getZLinkURL(M_UPDATEMOVEDEVICEID);
    }
}
