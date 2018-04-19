package com.carlt.doride.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.carlt.doride.YemaApplication;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.data.car.CarMainFunInfo;
import com.carlt.doride.data.remote.RemoteMainInfo;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/10/10 0010.
 */

public class LoginInfo extends BaseResponseInfo {
    public static final int Error_Message = 0;// 设备出现异常的推送

    public static final int Author_Message = 1;// 授权推送

    public static final int Feetip_Message = 2;// 续费提醒推送

    public static final String PREF_USER = "usre_pref";

    public static final String PREF_CAR = "car_pref";

    public static final String PREF_EXT = "user_ext_pref";

    public static final int SERVICE_TIME_TNORMAL = 0;// 正常
    public static final int SERVICE_TIME_TNEAR = 1;// 即将到期
    public static final int SERVICE_TIME_TARRAIVE = 2;// 已到期
    public static final int SERVICE_TIME_TOVER = 3;// 透支了
    public static final int SERVICE_TIME_CANCEL = 4;// 已销户(不支持续费)

    // 车乐拍
    public static String ssid = "";

    public static String ssidpwd = "";

    private static SharedPreferences user_pref = YemaApplication.getInstanse()
            .getSharedPreferences(PREF_USER, Context.MODE_PRIVATE);

    private static SharedPreferences car_pref = YemaApplication.getInstanse()
            .getSharedPreferences(PREF_CAR, Context.MODE_PRIVATE);

    private static SharedPreferences user_ext_pref = YemaApplication.getInstanse().getSharedPreferences(PREF_EXT,
                    Context.MODE_PRIVATE);

    /** 城市名 缓存 非登录下发 */
    private static String cityName = "";

    public static String getCityName() {
        cityName = user_pref.getString("cityName", null);
        return cityName;
    }

    public static void setCityName(String cityName) {
        LoginInfo.cityName = cityName;
        user_pref.edit().putString("cityName", cityName).apply();
    }

    // 最后一次登录时间
    public static long Last_Login_Time = -1;

    // TOKEN生命周期
    private static String expiresIn = "";
    // 经销商id
    private static String dealerId = "";
    // 用户ID
    private static String useId = "";
    // 手机号
    private static String mobile = "";
    // 是否是游客
    private static boolean isVisitor;

    private static String username = "";

    private static String SSID = "";

    private static String SSIDPWD = "";

    private static String lifetime = "";// 注册截止今天第几日

    private static String weixinbind = "";// 是否绑定微信: 1 微信账户绑定

    private static String clwbind = "";// 是否绑定设备：2 APP设备未绑定,1 APP设备绑定

    private static String regip = "";// 注册IP

    private static String avatar_id = "";// 头像ID

    private static String originate = "";// 注册来源，0：网站，1，APP-安卓，2：APP-IOS，3：微信

    private static String lastlogin = "";// 上次登录时间

    private static String loginoauth = "";// 登录授权AUTH

    private static String logintimes = "";// 登陆次数

    private static String createdate = "";// 注册时间

    @SerializedName("access_token")
    private static String access_token = "";// 授权登陆token

    private static String expires_in = "";// 授权生命周期

    /** 车辆信息 **/
    private static String cId = "";// 车辆ID

    // 品牌ID
    private static String brandid = "";

    // 颜色
    private static String color = "";

    //
    private static String province = "";

    // 城市
    private static String city = "";

    // 车型ID
    private static String modelid = "";

    // 登陆设备类型，， 0 正常登陆，1 更换设备
    private static String devicetype = "";

    private static String VC = "";// 排量

    private static String protocol = "";// 协议类型

    private static String accePark = "";// 加速度计驻车阈值

    private static String acceRun = "";// 加速度计行驶阈值

    private static String acceDir = "";// 加速度计安装方向

    private static String VBatTh = "";// 电瓶电压阈值

    private static String Vbat = "";// 电瓶电压，心跳包的电压值考虑保存在这里。因为心跑包上报频率很高。每条都记录数据量较大

    private static String credit = "";

    private static String licencenumber = "";

    private static String licencedate = "";

    private static String isrunning = "";

    private static String tag = "";

    private static String updatedate = "";

    private static String city_code_id = "";

    private static String city_code = "";

    // 车牌号
    private static String carno = "";

    // 激活时走前装还是后装， 1前装 0非前装
    private static boolean isInstallorder;

    public final static String needJumptoBind = "1";// 1 需要

    public final static String noJumptoBind = "0";// 0 不需要

    public final static String DEVICECATEGORY_BEFORE = "1";// 前装

    public final static String DEVICECATEGORY_AFTER = "0";// 后装

    public final static String DEVICECATEGORY_AFTER2016 = "2";// 前装2016款

    public final static String DEVICECATEGORY_X7 = "3";// 大迈X7
    // 设备真实顺序 0后装，1前装，2后装2016款，3大迈X7(v1.3.0版本添加默认值 0)
    private static String deviceCategory = "0";

    private static String tbox_type;

    // 短位车架号
    private static String shortstandcarno = "";

    /** 4S店信息 **/
    private static String dealerName = "";

    // 车系id
    private static String optionid = "";

    // 车款id
    private static String carid = "";

    // 车辆名称
    private static String carname = "";

    // 车标
    private static String carlogo = "";

    /** 以下信息是违章查询使用到的信息 **/
    // 能否进行违章查询
    private static String canQueryVio = "";

    // 车牌城市
    private static String carcity = "";

    // 车架号
    private static String standcarno = "";

    // 发动机号
    private static String engineno = "";

    // 证书号
    private static String registno = "";

    // 盒子记录里程数
    private static String summileage = "";

    // 购车日期
    private static String buydate = "";

    /** 用户信息 **/
    // 真实姓名
    private static String realname = "";

    // 设备串号
    private static String deviceidstring = "";
    // 车辆PIN码
    private static String pin = "";
    // 车辆VIN码
    private static String vin = "";

    // 服务时间是否即将到期
    private static boolean isServiceExpire;

    // 设备绑定了车
    private static boolean isBindCar = false;

    // 设备是否激活了
    private static boolean isDeviceActivate = false;

    // 设备是否正在升级
    private static boolean isUpgradeing = false;

    // 是否为gps设备
    private static boolean isGpsDevice = false;

    // 头像
    private static String avatar_img = "";

    // 性别(1:男 2:女 3:保密)
    private static String gender = "";

    public final static String GENDER_NAN = "1";

    public final static String GENDER_NV = "2";

    public final static String GENDER_MI = "3";

    // 是否已设置远程密码 1：是 0：否
    private static boolean isSetRemotePwd;

    // 远程音效开关是否打开
    private static boolean isRemoteSoundOpen = true;

    // 是否是主机
    private static boolean isMain;

    // 主机名称
    private static String mainDevicename;

    // 主机唯一标识ID
    private static String mainDeviceid;

    // 授权开关状态（主机）
    private static String authorize_status = "";

    public final static String AUTHORIZE_STATUS_CLOSE = "0";

    public final static String AUTHORIZE_STATUS_OPEN = "1";

    // 是否存在授权请求（主机）
    private static boolean hasAuthorize;

    // 是否需要授权登录（子机）
    private static boolean needAuthorize;

    // 账户是否冻结（主机）
    private static boolean isFreezing;

    // 是否实名认证（主机）
    private static boolean isAuthen;

    // 认证名字
    private static String authen_name = "";

    // 是否打开五分钟内无需输入密码开关
    private static boolean isNoneedpsw;

    // 认证身份证号
    private static String authen_card = "";

    /** 秘书信息 **/
    // 车秘书名称
    private static String secretaryName = "";

    /** 4S店信息 **/
    private static String dealerUsername = "";
    private static String dealerAddres = "";
    private static double dealerLat = -1;
    private static double dealerLon = -1;
    private static int dealerZoom = 18;
    private static String dealerTel = "";
    private static String serviceTel = "";// 客服电话

    private static int push_prizeinfo_flag = 1;

    // 最近一条日报、周报、月报信息
    private static String lately_day = "";

    private static String lately_week = "";

    private static String lately_month = "";

    private static String insurance_time;//上次投保时间

    private static String mainten_miles = "";// 上次保养里程

    private static String mainten_time = "";// 上次保养日期

    private static String register_time = "";// 注册时间

    private static String mainten_next_miles = "";// 距离下次保养里程

    private static String mainten_next_day = "";// 距离下次保养天数

    private static boolean isMainten = false;// “我已保养过”按钮
    // 是否可点击 1能点击，剩余不可点击
    public static int service_time_type = 0;
    public static String service_time_end = "";// 到期日期
    public static String service_time_days = "";// 可用天数

    private static RemoteMainInfo remoteMainInfo;// 远程首页数据
    private static CarMainFunInfo carMainFunInfo;// 座驾首页支持的功能数据

    private static boolean isChangedCar;// 用户是否更换了车型（用进行修改车型操作后，该值为true，重新拉取修改车辆车型信息变为false）

    private static boolean isDemoAccount;// 是否是演示账号

    private static boolean isTachograph;//是否是带记录仪设备

    public static void Destroy() {
        JSONObject destroy = null;
        try {
            destroy = new JSONObject("{}");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        LoginInfo.setRealname((destroy.optString("realname", "")));
        LoginInfo.setGender((destroy.optString("gender", "")));
        LoginInfo.setAvatar_img((destroy.optString("avatar_img", "")));
        LoginInfo.setMobile((destroy.optString("mobile", "")));
        LoginInfo.setVisitor(destroy.optBoolean("isVisitor"));
//        YemaApplication.TOKEN = "";
        LoginInfo.setExpiresIn((destroy.optString("expires_in", "")));
        LoginInfo.setDealerId((destroy.optString("expires_in", "")));
        LoginInfo.setUseId((destroy.optString("uid", "")));
        LoginInfo.setOptionid((destroy.optString("optionid", "")));
        LoginInfo.setCarid((destroy.optString("carid", "")));
        LoginInfo.setCarcity((destroy.optString("city", "")));
        LoginInfo.setDeviceidstring((destroy.optString("deviceidstring", "")));
        LoginInfo.setPin(mobile, (destroy.optString("pin", "")));
        LoginInfo.setServiceExpire((destroy.optBoolean("service_time_expire")));
        LoginInfo.setCarno((destroy.optString("carno", "")));
        LoginInfo.setCarname((destroy.optString("carname", "")));
        LoginInfo.setCarlogo((destroy.optString("carlogo", "")));
        LoginInfo.setBrandid((destroy.optString("brandid", "")));
        LoginInfo.setSecretaryName((destroy.optString("name", "")));
        LoginInfo.setDealerUsername((destroy.optString("username", "")));
        LoginInfo.setDealerAddres((destroy.optString("addres", "")));
        LoginInfo.setDealerTel(destroy.optString("tel"));

        LoginInfo.setLately_day((destroy.optString("day", "")));
        LoginInfo.setLately_week((destroy.optString("week", "")));
        LoginInfo.setLately_month((destroy.optString("month", "")));

        LoginInfo.setMainten_miles((destroy.optString("mainten_miles", "")));
        LoginInfo.setMainten_time((destroy.optString("mainten_time", "")));
        LoginInfo.setMainten_next_miles((destroy.optString(
                "mainten_next_miles", "")));
        LoginInfo.setMainten_next_day((destroy.optString("mainten_next_date",
                "")));
        LoginInfo.setMainten((destroy.optBoolean("isMainten")));

        LoginInfo.setMain(destroy.optBoolean("is_main"));
        LoginInfo.setMainDevicename(destroy.optString("move_device_name", ""));
        LoginInfo
                .setAuthorize_status(destroy.optString("authorize_switch", ""));
        LoginInfo.setHasAuthorize(destroy.optBoolean("has_authorize"));
        LoginInfo.setNeedAuthorize(destroy.optBoolean("need_authorize"));
        LoginInfo.setFreezing(destroy.optBoolean("is_freezing"));
        LoginInfo.setAuthen(destroy.optBoolean("is_authen"));
        LoginInfo.setAuthen_name(destroy.optString("authen_name", ""));
        LoginInfo.setAuthen_card(destroy.optString("authen_card", ""));
        LoginInfo.setInsurance_time(destroy.optString("insurance_time", ""));
        LoginInfo.setRegister_time(destroy.optString("register_time", ""));
        LoginInfo.setAccess_token(destroy.optString("access_token", ""));
        LoginInfo.setVin(LoginInfo.getMobile(),"");
    }

    public static String getExpiresIn() {
        expiresIn = user_pref.getString("expiresIn", expiresIn);
        return expiresIn;
    }

    public static void setExpiresIn(String expiresIn) {
        LoginInfo.expiresIn = expiresIn;
        user_pref.edit().putString("expiresIn", expiresIn).apply();
    }

    public static String getDealerId() {
        dealerId = user_pref.getString("dealerId", dealerId);
        return dealerId;
    }

    public static void setDealerId(String dealerId) {
        LoginInfo.dealerId = dealerId;
        user_pref.edit().putString("dealerId", dealerId).apply();
    }

    public static String getUseId() {
        useId = user_pref.getString("useId", useId);
        return useId;
    }

    public static void setUseId(String useId) {
        LoginInfo.useId = useId;
        user_pref.edit().putString("useId", useId).apply();
    }

    public static String getMobile() {
        mobile = user_pref.getString("mobile", mobile);
        return mobile;
    }

    public static void setMobile(String mobile) {
        LoginInfo.mobile = mobile;
        user_pref.edit().putString("mobile", mobile).apply();
    }

    public static boolean isVisitor() {
        isVisitor = user_pref.getBoolean("isVisitor", isVisitor);
        return isVisitor;
    }

    public static void setVisitor(boolean isVisitor) {
        LoginInfo.isVisitor = isVisitor;
        user_pref.edit().putBoolean("isVisitor", isVisitor).apply();
    }

    public static String getUsername() {
        username = user_pref.getString("username", username);
        return username;
    }

    public static void setUsername(String username) {
        LoginInfo.username = username;
        user_pref.edit().putString("username", username).apply();
    }

    public static String getSSID() {
        SSID = user_pref.getString("SSID", SSID);
        return SSID;
    }

    public static void setSSID(String sSID) {
        SSID = sSID;
        user_pref.edit().putString("SSID", SSID).apply();
    }

    public static String getSSIDPWD() {
        SSIDPWD = user_pref.getString("SSIDPWD", SSIDPWD);
        return SSIDPWD;
    }

    public static void setSSIDPWD(String sSIDPWD) {
        SSIDPWD = sSIDPWD;
        user_pref.edit().putString("SSIDPWD", SSIDPWD).apply();
    }

    public static String getLifetime() {
        lifetime = user_pref.getString("lifetime", lifetime);
        return lifetime;
    }

    public static void setLifetime(String lifetime) {
        LoginInfo.lifetime = lifetime;
        user_pref.edit().putString("lifetime", lifetime).apply();
    }

    public static String getWeixinbind() {
        lifetime = user_pref.getString("weixinbind", weixinbind);
        return weixinbind;
    }

    public static void setWeixinbind(String weixinbind) {
        LoginInfo.weixinbind = weixinbind;
        user_pref.edit().putString("weixinbind", weixinbind).apply();
    }

    public static String getClwbind() {
        clwbind = user_pref.getString("clwbind", clwbind);
        return clwbind;
    }

    public static void setClwbind(String clwbind) {
        LoginInfo.clwbind = clwbind;
        user_pref.edit().putString("clwbind", clwbind).apply();
    }

    public static String getRegip() {
        regip = user_pref.getString("regip", regip);
        return regip;
    }

    public static void setRegip(String regip) {
        LoginInfo.regip = regip;
        user_pref.edit().putString("regip", regip).apply();
    }

    public static String getAvatar_id() {
        avatar_id = user_pref.getString("avatar_id", avatar_id);
        return avatar_id;
    }

    public static void setAvatar_id(String avatar_id) {
        LoginInfo.avatar_id = avatar_id;
        user_pref.edit().putString("avatar_id", avatar_id).apply();
    }

    public static String getOriginate() {
        originate = user_pref.getString("originate", originate);
        return originate;
    }

    public static void setOriginate(String originate) {
        LoginInfo.originate = originate;
        user_pref.edit().putString("originate", originate).apply();
    }

    public static String getLastlogin() {
        lastlogin = user_pref.getString("lastlogin", lastlogin);
        return lastlogin;
    }

    public static void setLastlogin(String lastlogin) {
        LoginInfo.lastlogin = lastlogin;
        user_pref.edit().putString("lastlogin", lastlogin).apply();
    }

    public static String getLoginoauth() {
        loginoauth = user_pref.getString("loginoauth", loginoauth);
        return loginoauth;
    }

    public static void setLoginoauth(String loginoauth) {
        LoginInfo.loginoauth = loginoauth;
        user_pref.edit().putString("loginoauth", loginoauth).apply();
    }

    public static String getLogintimes() {
        logintimes = user_pref.getString("logintimes", logintimes);
        return logintimes;
    }

    public static void setLogintimes(String logintimes) {
        LoginInfo.logintimes = logintimes;
        user_pref.edit().putString("logintimes", logintimes).apply();
    }

    public static String getCreatedate() {
        createdate = user_pref.getString("createdate", createdate);
        return createdate;
    }

    public static void setCreatedate(String createdate) {
        LoginInfo.createdate = createdate;
        user_pref.edit().putString("createdate", createdate).apply();
    }

    public static String getAccess_token() {
        if(TextUtils.isEmpty(access_token)){
            access_token = user_pref.getString("access_token", access_token);
        }
        return access_token;
    }

    public static void setAccess_token(String token) {
        access_token = token;
        user_pref.edit().putString("access_token", access_token).apply();
    }

    public static String getExpires_in() {
        expires_in = user_pref.getString("expires_in", expires_in);
        return expires_in;
    }

    public static void setExpires_in(String expires_in) {
        LoginInfo.expires_in = expires_in;
        user_pref.edit().putString("expires_in", expires_in).apply();
    }

    public static String getcId() {
        cId = car_pref.getString("cId", cId);
        return cId;
    }

    public static void setcId(String cId) {
        LoginInfo.cId = cId;
        car_pref.edit().putString("cId", cId).apply();
    }

    public static String getBrandid() {
        brandid = car_pref.getString("brandid", brandid);
        return brandid;
    }

    public static void setBrandid(String brandid) {
        LoginInfo.brandid = brandid;
        car_pref.edit().putString("brandid", brandid).apply();
    }

    public static String getColor() {
        color = car_pref.getString("color", color);
        return color;
    }

    public static void setColor(String color) {
        LoginInfo.color = color;
        car_pref.edit().putString("color", color).apply();
    }

    public static String getProvince() {
        province = car_pref.getString("province", province);
        return province;
    }

    public static void setProvince(String province) {
        LoginInfo.province = province;
        car_pref.edit().putString("province", province).apply();
    }

    public static String getCity() {
        city = car_pref.getString("city", city);
        return city;
    }

    public static void setCity(String city) {
        LoginInfo.city = city;
        car_pref.edit().putString("city", city).apply();
    }

    public static String getModelid() {
        modelid = car_pref.getString("modelid", modelid);
        return modelid;
    }

    public static void setModelid(String modelid) {
        LoginInfo.modelid = modelid;
        car_pref.edit().putString("modelid", modelid).apply();
    }

    public static String getVC() {
        VC = car_pref.getString("VC", VC);
        return VC;
    }

    public static void setVC(String vC) {
        VC = vC;
        car_pref.edit().putString("VC", VC).apply();
    }

    public static String getProtocol() {
        protocol = car_pref.getString("protocol", protocol);
        return protocol;
    }

    public static void setProtocol(String protocol) {
        LoginInfo.protocol = protocol;
        car_pref.edit().putString("protocol", protocol).apply();
    }

    public static String getAccePark() {
        accePark = car_pref.getString("accePark", accePark);
        return accePark;
    }

    public static void setAccePark(String accePark) {
        LoginInfo.accePark = accePark;
        car_pref.edit().putString("accePark", accePark).apply();
    }

    public static String getAcceRun() {
        acceRun = car_pref.getString("acceRun", acceRun);
        return acceRun;
    }

    public static void setAcceRun(String acceRun) {
        LoginInfo.acceRun = acceRun;
        car_pref.edit().putString("acceRun", acceRun).apply();
    }

    public static String getAcceDir() {
        acceDir = car_pref.getString("acceDir", acceDir);
        return acceDir;
    }

    public static void setAcceDir(String acceDir) {
        LoginInfo.acceDir = acceDir;
        car_pref.edit().putString("acceDir", acceDir).apply();
    }

    public static String getVBatTh() {
        VBatTh = car_pref.getString("VBatTh", VBatTh);
        return VBatTh;
    }

    public static void setVBatTh(String vBatTh) {
        VBatTh = vBatTh;
        car_pref.edit().putString("VBatTh", VBatTh).apply();
    }

    public static String getVbat() {
        Vbat = car_pref.getString("Vbat", Vbat);
        return Vbat;
    }

    public static void setVbat(String vbat) {
        Vbat = vbat;
        car_pref.edit().putString("Vbat", Vbat).apply();
    }

    public static String getCredit() {
        credit = car_pref.getString("credit", credit);
        return credit;
    }

    public static void setCredit(String credit) {
        LoginInfo.credit = credit;
        car_pref.edit().putString("credit", credit).apply();
    }

    public static String getLicencenumber() {
        licencenumber = car_pref.getString("licencenumber", licencenumber);
        return licencenumber;
    }

    public static void setLicencenumber(String licencenumber) {
        LoginInfo.licencenumber = licencenumber;
        car_pref.edit().putString("licencenumber", licencenumber).apply();
    }

    public static String getLicencedate() {
        licencedate = car_pref.getString("licencedate", licencedate);
        return licencedate;
    }

    public static void setLicencedate(String licencedate) {
        LoginInfo.licencedate = licencedate;
        car_pref.edit().putString("licencedate", licencedate).apply();
    }

    public static String getIsrunning() {
        isrunning = car_pref.getString("isrunning", isrunning);
        return isrunning;
    }

    public static void setIsrunning(String isrunning) {
        LoginInfo.isrunning = isrunning;
        car_pref.edit().putString("isrunning", isrunning).apply();
    }

    public static String getTag() {
        tag = car_pref.getString("tag", tag);
        return tag;
    }

    public static void setTag(String tag) {
        LoginInfo.tag = tag;
        car_pref.edit().putString("tag", tag).apply();
    }

    public static String getUpdatedate() {
        updatedate = car_pref.getString("updatedate", updatedate);
        return updatedate;
    }

    public static void setUpdatedate(String updatedate) {
        LoginInfo.updatedate = updatedate;
        car_pref.edit().putString("updatedate", updatedate).apply();
    }

    public static String getCity_code_id() {
        city_code_id = car_pref.getString("city_code_id", city_code_id);
        return city_code_id;
    }

    public static void setCity_code_id(String city_code_id) {
        LoginInfo.city_code_id = city_code_id;
        car_pref.edit().putString("city_code_id", city_code_id).apply();
    }

    public static String getCity_code() {
        city_code = car_pref.getString("city_code", city_code);
        return city_code;
    }

    public static void setCity_code(String city_code) {
        LoginInfo.city_code = city_code;
        car_pref.edit().putString("city_code", city_code).apply();
    }

    public static String getCarno() {
        carno = car_pref.getString("carno", carno);
        return carno;
    }

    public static void setCarno(String carno) {
        LoginInfo.carno = carno;
        car_pref.edit().putString("carno", carno).apply();
    }

    public static String getShortstandcarno() {
        shortstandcarno = car_pref
                .getString("shortstandcarno", shortstandcarno);
        return shortstandcarno;
    }

    public static void setShortstandcarno(String shortstandcarno) {
        LoginInfo.shortstandcarno = shortstandcarno;
        car_pref.edit().putString("shortstandcarno", shortstandcarno).apply();
    }

    public static String getDealerName() {
        dealerName = car_pref.getString("dealerName", dealerName);
        return dealerName;
    }

    public static void setDealerName(String dealerName) {
        LoginInfo.dealerName = dealerName;
        car_pref.edit().putString("dealerName", dealerName).apply();
    }

    public static String getOptionid() {
        optionid = car_pref.getString("optionid", optionid);
        return optionid;
    }

    public static void setOptionid(String optionid) {
        LoginInfo.optionid = optionid;
        car_pref.edit().putString("optionid", optionid).apply();
    }

    public static String getCarid() {
        carid = car_pref.getString("carid", carid);
        return carid;
    }

    public static void setCarid(String carid) {
        LoginInfo.carid = carid;
        car_pref.edit().putString("carid", carid).apply();
    }

    public static String getCarname() {
        carname = car_pref.getString("carname", carname);
        return carname;
    }

    public static void setCarname(String carname) {
        LoginInfo.carname = carname;
        car_pref.edit().putString("carname", carname).apply();
    }

    public static String getCarlogo() {
        carlogo = car_pref.getString("carlogo", carlogo);
        return carlogo;
    }

    public static void setCarlogo(String carlogo) {
        LoginInfo.carlogo = carlogo;
        car_pref.edit().putString("carlogo", carlogo).apply();
    }

    public static String getCanQueryVio() {
        canQueryVio = car_pref.getString("canQueryVio", canQueryVio);
        return canQueryVio;
    }

    public static void setCanQueryVio(String canQueryVio) {
        LoginInfo.canQueryVio = canQueryVio;
        car_pref.edit().putString("canQueryVio", canQueryVio).apply();
    }

    public static String getCarcity() {
        carcity = car_pref.getString("carcity", carcity);
        return carcity;
    }

    public static void setCarcity(String carcity) {
        LoginInfo.carcity = carcity;
        car_pref.edit().putString("carcity", carcity).apply();
    }

    public static String getStandcarno() {
        standcarno = car_pref.getString("standcarno", standcarno);
        return standcarno;
    }

    public static void setStandcarno(String standcarno) {
        LoginInfo.standcarno = standcarno;
        car_pref.edit().putString("standcarno", standcarno).apply();
    }

    public static String getEngineno() {
        engineno = car_pref.getString("engineno", engineno);
        return engineno;
    }

    public static void setEngineno(String engineno) {
        LoginInfo.engineno = engineno;
        car_pref.edit().putString("engineno", engineno).apply();
    }

    public static String getRegistno() {
        registno = car_pref.getString("registno", registno);
        return registno;
    }

    public static void setRegistno(String registno) {
        LoginInfo.registno = registno;
        car_pref.edit().putString("registno", registno).apply();
    }

    public static String getSummileage() {
        summileage = car_pref.getString("summileage", summileage);
        return summileage;
    }

    public static void setSummileage(String summileage) {
        LoginInfo.summileage = summileage;
        car_pref.edit().putString("summileage", summileage).apply();
    }

    public static String getBuydate() {
        buydate = car_pref.getString("buydate", buydate);
        return buydate;
    }

    public static void setBuydate(String buydate) {
        LoginInfo.buydate = buydate;
        car_pref.edit().putString("buydate", buydate).apply();
    }

    public static String getRealname() {
        realname = car_pref.getString("realname", realname);
        return realname;
    }

    public static void setRealname(String realname) {
        LoginInfo.realname = realname;
        car_pref.edit().putString("realname", realname).apply();
    }

    public static String getDeviceidstring() {
        deviceidstring = car_pref.getString("deviceidstring", deviceidstring);
        return deviceidstring;
    }

    public static void setDeviceidstring(String deviceidstring) {
        LoginInfo.deviceidstring = deviceidstring;
        car_pref.edit().putString("deviceidstring", deviceidstring).apply();
    }

    public static String getPin(String mobile) {
        pin = car_pref.getString(mobile + "_pin", "");
        return pin;
    }

    public static void setPin(String mobile, String pin) {
        LoginInfo.pin = pin;
        car_pref.edit().putString(mobile + "_pin", pin).apply();
    }

    public static void setVin(String mobile, String vin) {
        LoginInfo.vin = vin;
        car_pref.edit().putString(mobile + "_vin", vin).apply();
    }

    public static String getVin(String mobile) {
        vin = car_pref.getString(mobile + "_vin", "");
        return vin;
    }

    public static boolean isServiceExpire() {
        isServiceExpire = user_ext_pref
                .getBoolean("service_time_expire", false);
        return isServiceExpire;
    }

    public static void setServiceExpire(boolean isServiceExpire) {
        LoginInfo.isServiceExpire = isServiceExpire;
        user_ext_pref.edit().putBoolean("service_time_expire", isServiceExpire)
                .apply();
    }

    public static boolean isBindCar() {
        isBindCar = car_pref.getBoolean("isBindCar", false);
        return isBindCar;
    }

    public static void setBindCar(boolean isBindCar) {
        LoginInfo.isBindCar = isBindCar;
        car_pref.edit().putBoolean("isBindCar", isBindCar).apply();
    }

    public static boolean isDeviceActivate() {
        isDeviceActivate = car_pref.getBoolean("isDeviceActivate", false);
        return isDeviceActivate;
    }

    public static void setDeviceActivate(boolean isDeviceActivate) {
        LoginInfo.isDeviceActivate = isDeviceActivate;
        car_pref.edit().putBoolean("isDeviceActivate", isDeviceActivate)
                .apply();
    }

    public static boolean isUpgradeing() {
        isUpgradeing = car_pref.getBoolean("isUpgradeing", false);
        return isUpgradeing;
    }

    public static void setUpgradeing(boolean isUpgradeing) {
        LoginInfo.isUpgradeing = isUpgradeing;
        car_pref.edit().putBoolean("isUpgradeing", isUpgradeing).apply();
    }

    public static boolean isGpsDevice() {
        isGpsDevice = car_pref.getBoolean("isGpsDevice", false);
        return isGpsDevice;
    }

    public static void setGpsDevice(boolean isGpsDevice) {
        LoginInfo.isGpsDevice = isGpsDevice;
        car_pref.edit().putBoolean("isGpsDevice", isGpsDevice).apply();
    }

    public static String getAvatar_img() {
        avatar_img = car_pref.getString("avatar_img", avatar_img);
        return avatar_img;
    }

    public static void setAvatar_img(String avatar_img) {
        LoginInfo.avatar_img = avatar_img;
        car_pref.edit().putString("avatar_img", avatar_img).apply();
    }

    public static String getGender() {
        gender = car_pref.getString("gender", gender);
        return gender;
    }

    public static void setGender(String gender) {
        LoginInfo.gender = gender;
        car_pref.edit().putString("gender", gender).apply();
    }

    public static boolean isMain() {
        isMain = car_pref.getBoolean("isMain", false);
        return isMain;
    }

    public static void setMain(boolean isMain) {
        LoginInfo.isMain = isMain;
        car_pref.edit().putBoolean("isMain", isMain).apply();
    }

    public static String getMainDevicename() {
        mainDevicename = car_pref.getString("mainDevicename", mainDevicename);
        return mainDevicename;
    }

    public static void setMainDevicename(String mainDevicename) {
        LoginInfo.mainDevicename = mainDevicename;
        car_pref.edit().putString("mainDevicename", mainDevicename).apply();
    }

    public static String getAuthorize_status() {
        authorize_status = user_pref.getString("authorize_status",
                authorize_status);
        return authorize_status;
    }

    public static void setAuthorize_status(String authorize_status) {
        LoginInfo.authorize_status = authorize_status;
        user_pref.edit().putString("authorize_status", authorize_status)
                .apply();
    }

    public static boolean isHasAuthorize() {
        hasAuthorize = user_pref.getBoolean("hasAuthorize", false);
        return hasAuthorize;
    }

    public static void setHasAuthorize(boolean hasAuthorize) {
        LoginInfo.hasAuthorize = hasAuthorize;
        user_pref.edit().putBoolean("hasAuthorize", hasAuthorize).apply();
    }

    public static boolean isNeedAuthorize() {
        needAuthorize = user_pref.getBoolean("needAuthorize", false);
        return needAuthorize;
    }

    public static void setNeedAuthorize(boolean needAuthorize) {
        LoginInfo.needAuthorize = needAuthorize;
        user_pref.edit().putBoolean("needAuthorize", needAuthorize).apply();
    }

    public static boolean isFreezing() {
        isFreezing = user_pref.getBoolean("isFreezing", false);
        return isFreezing;
    }

    public static void setFreezing(boolean isFreezing) {
        LoginInfo.isFreezing = isFreezing;
        user_pref.edit().putBoolean("isFreezing", isFreezing).apply();
    }

    public static boolean isAuthen() {
        isAuthen = user_pref.getBoolean("isAuthen", false);
        return isAuthen;
    }

    public static void setAuthen(boolean isAuthen) {
        LoginInfo.isAuthen = isAuthen;
        user_pref.edit().putBoolean("isAuthen", isAuthen).apply();
    }

    public static boolean isNoneedpsw() {
        isNoneedpsw = user_pref.getBoolean("isNoneedpsw", false);
        return isNoneedpsw;
    }

    public static void setNoneedpsw(boolean isNoneedpsw) {
        LoginInfo.isNoneedpsw = isNoneedpsw;
        user_pref.edit().putBoolean("isNoneedpsw", isNoneedpsw).apply();
    }

    public static String getAuthen_name() {
        authen_name = user_pref.getString("authen_name", authen_name);
        return authen_name;
    }

    public static void setAuthen_name(String authen_name) {
        LoginInfo.authen_name = authen_name;
        user_pref.edit().putString("authen_name", authen_name).apply();
    }

    public static String getAuthen_card() {
        authen_card = user_pref.getString("authen_card", authen_card);
        return authen_card;
    }

    public static void setAuthen_card(String authen_card) {
        LoginInfo.authen_card = authen_card;
        user_pref.edit().putString("authen_card", authen_card).apply();
    }

    public static String getSecretaryName() {
        secretaryName = car_pref.getString("secretaryName", secretaryName);
        return secretaryName;
    }

    public static void setSecretaryName(String secretaryName) {
        LoginInfo.secretaryName = secretaryName;
        car_pref.edit().putString("secretaryName", secretaryName).apply();
    }

    public static String getDealerUsername() {
        dealerUsername = car_pref.getString("dealerUsername", dealerUsername);
        return dealerUsername;
    }

    public static void setDealerUsername(String dealerUsername) {
        LoginInfo.dealerUsername = dealerUsername;
        car_pref.edit().putString("dealerUsername", dealerUsername).apply();
    }

    public static String getDealerAddres() {
        dealerAddres = car_pref.getString("dealerAddres", dealerAddres);
        return dealerAddres;
    }

    public static void setDealerAddres(String dealerAddres) {
        LoginInfo.dealerAddres = dealerAddres;
        car_pref.edit().putString("dealerAddres", dealerAddres).apply();
    }

    public static double getDealerLat() {
        String dealer = car_pref.getString("dealerLat", "0.0");
        try {
            dealerLat = Double.parseDouble(dealer);
        } catch (Exception ex) {
            ex.printStackTrace();
            dealerLat = 0.0;
        }
        return dealerLat;
    }

    public static void setDealerLat(double dealerLat) {
        LoginInfo.dealerLat = dealerLat;
        car_pref.edit().putString("dealerLat", Double.toString(dealerLat))
                .apply();
    }

    public static double getDealerLon() {
        String dealer = car_pref.getString("dealerLon", "0.0");
        try {
            dealerLon = Double.parseDouble(dealer);
        } catch (Exception ex) {
            ex.printStackTrace();
            dealerLon = 0.0;
        }
        return dealerLon;
    }

    public static void setDealerLon(double dealerLon) {
        LoginInfo.dealerLon = dealerLon;
        car_pref.edit().putString("dealerLon", Double.toString(dealerLon))
                .apply();
    }

    public static int getDealerZoom() {
        dealerZoom = car_pref.getInt("dealerZoom", dealerZoom);
        return dealerZoom;
    }

    public static void setDealerZoom(int dealerZoom) {
        LoginInfo.dealerZoom = dealerZoom;
        car_pref.edit().putInt("dealerZoom", dealerZoom).apply();
    }

    public static String getDealerTel() {
        LoginInfo.dealerTel = car_pref.getString("dealerTel", dealerTel);
        return dealerTel;
    }

    public static void setDealerTel(String dealerTel) {
        LoginInfo.dealerTel = dealerTel;
        car_pref.edit().putString("dealerTel", dealerTel).apply();
    }

    public static String getServiceTel() {
        serviceTel = car_pref.getString("serviceTel", serviceTel);
        return serviceTel;
    }

    public static void setServiceTel(String serviceTel) {
        LoginInfo.serviceTel = serviceTel;
        car_pref.edit().putString("serviceTel", serviceTel).apply();
    }

    public static int getPush_prizeinfo_flag() {
        push_prizeinfo_flag = car_pref.getInt("push_prizeinfo_flag",
                push_prizeinfo_flag);
        return push_prizeinfo_flag;
    }

    public static void setPush_prizeinfo_flag(int push_prizeinfo_flag) {
        LoginInfo.push_prizeinfo_flag = push_prizeinfo_flag;
        car_pref.edit().putInt("push_prizeinfo_flag", push_prizeinfo_flag)
                .apply();
    }

    public static String getLately_day() {
        lately_day = car_pref.getString("lately_day", lately_day);
        return lately_day;
    }

    public static void setLately_day(String lately_day) {
        LoginInfo.lately_day = lately_day;
        car_pref.edit().putString("lately_day", lately_day).apply();
    }

    public static String getLately_week() {
        lately_week = car_pref.getString("lately_week", lately_week);
        return lately_week;
    }

    public static void setLately_week(String lately_week) {
        LoginInfo.lately_week = lately_week;
        car_pref.edit().putString("lately_week", lately_week).apply();
    }

    public static String getLately_month() {
        lately_month = car_pref.getString("lately_month", lately_month);
        return lately_month;
    }

    public static void setLately_month(String lately_month) {
        LoginInfo.lately_month = lately_month;
        car_pref.edit().putString("lately_month", lately_month).apply();
    }

    public static String getMainten_miles() {
        mainten_miles = car_pref.getString("mainten_miles", mainten_miles);
        return mainten_miles;
    }

    public static void setMainten_miles(String mainten_miles) {
        LoginInfo.mainten_miles = mainten_miles;
        car_pref.edit().putString("mainten_miles", mainten_miles).apply();
    }

    public static String getInsurance_time() {
        insurance_time = car_pref.getString("insurance_time", insurance_time);
        return insurance_time;
    }

    public static void setInsurance_time(String insurance_time) {
        LoginInfo.insurance_time = insurance_time;
        car_pref.edit().putString("insurance_time", insurance_time).apply();
    }

    public static String getRegister_time() {
        register_time = car_pref.getString("register_time", register_time);
        return register_time;
    }

    public static void setRegister_time(String register_time) {
        LoginInfo.register_time = register_time;
        car_pref.edit().putString("register_time", register_time).apply();
    }

    public static String getMainten_time() {
        mainten_time = car_pref.getString("mainten_time", mainten_time);
        return mainten_time;
    }

    public static void setMainten_time(String mainten_time) {
        LoginInfo.mainten_time = mainten_time;
        car_pref.edit().putString("mainten_time", mainten_time).apply();
    }

    public static String getMainten_next_miles() {
        mainten_next_miles = car_pref.getString("mainten_next_miles",
                mainten_next_miles);
        return mainten_next_miles;
    }

    public static void setMainten_next_miles(String mainten_next_miles) {
        LoginInfo.mainten_next_miles = mainten_next_miles;
        car_pref.edit().putString("mainten_next_miles", mainten_next_miles)
                .apply();
    }

    public static String getMainten_next_day() {
        mainten_next_day = car_pref.getString("mainten_next_day",
                mainten_next_day);
        return mainten_next_day;
    }

    public static String getDevicetype() {
        devicetype = car_pref.getString("devicetype", devicetype);
        return devicetype;
    }

    public static void setDevicetype(String devicetype) {
        LoginInfo.devicetype = devicetype;
        car_pref.edit().putString("devicetype", devicetype).apply();
    }

    public static void setMainten_next_day(String mainten_next_day) {
        LoginInfo.mainten_next_day = mainten_next_day;
        car_pref.edit().putString("mainten_next_day", mainten_next_day)
                .apply();
    }

    public static boolean isMainten() {
        isMainten = car_pref.getBoolean("isMainten", isMainten);
        return isMainten;
    }

    public static void setMainten(boolean isMainten) {
        LoginInfo.isMainten = isMainten;
        car_pref.edit().putBoolean("isMainten", isMainten).apply();
    }

    public static boolean isInstallorder() {
        isInstallorder = car_pref.getBoolean("installorder", false);
        return isInstallorder;
    }

    public static void setInstallorder(boolean isInstallorder) {
        LoginInfo.isInstallorder = isInstallorder;
        car_pref.edit().putBoolean("installorder", isInstallorder).apply();
    }

    public static String getDeviceCategory() {
        deviceCategory = car_pref.getString("before_device", deviceCategory);
        return deviceCategory;
    }

    public static void setDeviceCategory(String deviceCategory) {
        LoginInfo.deviceCategory = deviceCategory;
        car_pref.edit().putString("before_device", deviceCategory).apply();
    }

    public static boolean isSetRemotePwd() {
        isSetRemotePwd = user_pref.getBoolean("isSetRemotePwd", isSetRemotePwd);
        return isSetRemotePwd;
    }

    public static void setSetRemotePwd(boolean isSetRemotePwd) {
        LoginInfo.isSetRemotePwd = isSetRemotePwd;
        user_pref.edit().putBoolean("isSetRemotePwd", isSetRemotePwd).apply();
    }

    public static boolean isRemoteSoundOpen() {
        return isRemoteSoundOpen = user_pref.getBoolean("isRemoteSoundOpen",
                isRemoteSoundOpen);
    }

    public static void setRemoteSoundOpen(boolean isRemoteSoundOpen) {
        LoginInfo.isRemoteSoundOpen = isRemoteSoundOpen;
        user_pref.edit().putBoolean("isRemoteSoundOpen", isRemoteSoundOpen)
                .apply();
    }

    public static RemoteMainInfo getRemoteMainInfo() {
        return remoteMainInfo;
    }

    public static void setRemoteMainInfo(RemoteMainInfo remoteMainInfo) {
        LoginInfo.remoteMainInfo = remoteMainInfo;
    }

    public static CarMainFunInfo getCarMainFunInfo() {
        return carMainFunInfo;
    }

    public static void setCarMainFunInfo(CarMainFunInfo carMainFunInfo) {
        LoginInfo.carMainFunInfo = carMainFunInfo;
    }

    public static boolean isChangedCar() {
        return isChangedCar;
    }

    public static void setChangedCar(boolean isChangedCar) {
        LoginInfo.isChangedCar = isChangedCar;
    }

    public static boolean isDemoAccount() {
        isDemoAccount = user_pref.getBoolean("isDemoAccount",
                isDemoAccount);
        return isDemoAccount;
    }

    public static void setDemoAccount(boolean isDemoAccount) {
        LoginInfo.isDemoAccount = isDemoAccount;
        user_pref.edit().putBoolean("isDemoAccount", isDemoAccount)
                .apply();
    }

    public static boolean isTachograph() {
        isTachograph = user_pref.getBoolean("isTachograph",
                isTachograph);
        return isTachograph;
    }

    public static void setTachograph(boolean isTachograph) {
        LoginInfo.isTachograph = isTachograph;
        user_pref.edit().putBoolean("isTachograph", isTachograph)
                .apply();
    }

    public static String getTbox_type() {
        tbox_type = user_pref.getString("tbox_type", tbox_type);
        return tbox_type;
    }

    public static void setTbox_type(String tbox_type) {
        LoginInfo.tbox_type = tbox_type;
        user_pref.edit().putString("tbox_type", tbox_type).commit();
    }

}
