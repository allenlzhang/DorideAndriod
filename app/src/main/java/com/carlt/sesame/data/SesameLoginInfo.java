package com.carlt.sesame.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.carlt.doride.DorideApplication;
import com.carlt.doride.R;
import com.carlt.sesame.data.car.CarMainFunInfo;
import com.carlt.sesame.data.remote.RemoteMainInfo;
import com.carlt.sesame.preference.TokenInfo;

import org.json.JSONException;
import org.json.JSONObject;

public class SesameLoginInfo {
    public static final int Author_Message = 1;// 授权推送

    public static final int Feetip_Message = 2;// 续费提醒推送

    public static final String PREF_USER = "sesame_usre_pref";

    public static final String PREF_CAR = "sesame_car_pref";

    public static final String PREF_EXT = "sesame_user_ext_pref";

    private static SharedPreferences user_pref = DorideApplication.getAppContext()
            .getSharedPreferences(PREF_USER, Context.MODE_PRIVATE);

    private static SharedPreferences car_pref = DorideApplication.getAppContext()
            .getSharedPreferences(PREF_CAR, Context.MODE_PRIVATE);

    private static SharedPreferences user_ext_pref = DorideApplication
            .getAppContext().getSharedPreferences(PREF_EXT,
                    Context.MODE_PRIVATE);

    /**
     * 城市名 缓存 非登录下发
     */
    private static String cityName = "";

    public static String getCityName() {
        cityName = user_pref.getString("cityName", null);
        return cityName;
    }

    public static void setCityName(String cityName) {
        SesameLoginInfo.cityName = cityName;
        user_pref.edit().putString("cityName", cityName).commit();
    }

    // 最后一次登录时间
    public static long Last_Login_Time = -1;

    // TOKEN
    private static String token = "";

    // TOKEN生命周期
    private static String expiresIn = "";

    private static String dealerId = "";

    // 用户ID
    private static String useId  = "";
    // 手机号
    private static String mobile = "";
    // 是否是游客
    private static boolean isVisitor;

    private static String remoteControl = "";

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

    private static String access_token = "";// 授权登陆token

    private static String expires_in = "";// 授权生命周期

    /**
     * 车辆信息
     **/
    private static String cId = "";// 车辆ID

    // 品牌ID
    private static String brandid         = "";
    private static int    activate_status = -1;

    // 颜色
    private static String color = "";

    //
    private static String province = "";

    // 城市
    private static String city = "";

    // 设备ID
    private static String deviceid = "";

    // 车型ID
    private static String modelid = "";

    //
    private static String licencelevelid = "";

    // 登陆设备类型，， 0 正常登陆，，1 更换设备
    private static String devicetype = "";

    // 判定是否为网销设备，1=网销设备
    private static String is_net_sale = "";

    private static String fixed_miles = "";

    private static String powerontime = "";//

    private static String VC = "";// 排量

    private static String protocol = "";// 协议类型

    private static String accePark = "";// 加速度计驻车阈值

    private static String acceRun = "";// 加速度计行驶阈值

    private static String acceDir = "";// 加速度计安装方向

    private static String VBatTh = "";// 电瓶电压阈值

    private static String Vbat = "";// 电瓶电压，心跳包的电压值考虑保存在这里。因为心跑包上报频率很高。每条都记录数据量较大

    private static String secretaryid = "";// 车秘书ID

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

    // 是否需要跳转到绑定设备页面， 1需要 0不需要
    private static String isJumptoBind = "";

    // 激活时走前装还是后装， 1前装 0非前装
    private static boolean isInstallorder;

    public final static String needJumptoBind = "1";// 1 需要

    public final static String noJumptoBind = "0";// 0 不需要

    // 设备真实顺序 1前装，0后装，2后装2016款
    private static String deviceType;

    public final static String DEVICETYPE_BEFORE = "1";// 前装

    public final static String DEVICETYPE_AFTER = "0";// 后装

    public final static String DEVICETYPE_AFTER2016 = "2";// 前装2016款

    // 短位车架号
    private static String shortstandcarno = "";

    /**
     * 4S店信息
     **/
    private static String dealerName = "";

    // 车系id
    private static String optionid = "";

    // 车款id
    private static String carid = "";

    // 车辆名称
    private static String carname = "";

    // 车标
    private static String carlogo = "";

    /**
     * 以下信息是违章查询使用到的信息
     **/
    // 能否进行违章查询
    private static String canQueryVio = "";

    // 车牌省
    private static String carprovice = "";

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

    /**
     * 用户信息
     **/
    // 真实姓名
    private static String realname = "";

    // 设备串号
    private static String deviceidstring = "";

    // 车辆PIN码
    private static String vpin = "";

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
    private static boolean isRemoteSoundOpen;

    // 状态：-1禁用,0未验证,1已验证
    private static String status = "";

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

    // 是否支持通用的三个新功能
    private static boolean isSupport = false;

    /**
     * 秘书信息
     **/
    // 车秘书名称
    private static String secretaryName = "";

    // 车秘书图标
    private static int secretaryImg = R.drawable.secretary_male;

    /**
     * 4S店信息
     **/
    private static String dealerUsername = "";

    private static String dealerAddres = "";

    private static double dealerLat = -1;

    private static double dealerLon = -1;

    private static int dealerZoom = 18;

    private static String dealerTel = "";

    private static String serviceTel = "";// 客服电话

    private static int push_prizeinfo_flag = 1;

    // 声光寻车状态 0：不支持此功能 1：支持
    public final static int SLCar_NONE = 0;

    public final static int SLCar_SUPPORT = 1;

    private static int SLCarLocating;

    // 自动升窗状态 0：不支持此功能 ,1：已开启自动升窗 ,2 未开启自动升窗
    public final static int WIN_NONE = 0;

    public final static int WIN_ON = 1;

    public final static int WIN_OFF = 2;

    private static int autoCloseWinSw;

    // 远程启动状态 0：不支持此功能 ,1：可以发起启动 ,2 可以发起取消启动
    public final static int START_NONE = 0;

    public final static int START_OFF = 1;

    public final static int START_ON = 2;

    private static int remoteStart;

    // 是否支持胎压监测
    private static boolean isTireable = false;

    // 最近一条日报、周报、月报信息
    private static String lately_day = "";

    private static String lately_week = "";

    private static String lately_month = "";

    private static String mainten_miles = "";// 上次保养里程

    private static String mainten_time = "";// 上次保养日期

    private static String mainten_next_miles = "";// 距离下次保养里程

    private static String mainten_next_day = "";// 距离下次保养天数

    private static String need_pin = "";// 	是否需要输入pin码 1不需要 0需要

    private static boolean isMainten = false;// “我已保养过”按钮
    // 是否可点击 1能点击，剩余不可点击

    private static int car_year;// 车款年限
    public final static int CAR_YEAR_2016 = 2016;
    public final static int CAR_YEAR_2018 = 2018;

    private static RemoteMainInfo remoteMainInfo;// 远程首页数据
    private static CarMainFunInfo carMainFunInfo;// 座驾首页支持的功能数据

    public static void Destroy() {
        JSONObject destroy = null;
        try {
            destroy = new JSONObject("{}");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        SesameLoginInfo.setRealname((destroy.optString("realname", "")));
        SesameLoginInfo.setGender((destroy.optString("gender", "")));
        SesameLoginInfo.setStatus((destroy.optString("status", "")));
        SesameLoginInfo.setAvatar_img((destroy.optString("avatar_img", "")));
        SesameLoginInfo.setMobile((destroy.optString("mobile", "")));
        SesameLoginInfo.setVisitor(destroy.optBoolean("isVisitor"));
        SesameLoginInfo.setToken((destroy.optString("access_token", "")));
        TokenInfo.setToken("");
        SesameLoginInfo.setExpiresIn((destroy.optString("expires_in", "")));
        SesameLoginInfo.setDealerId((destroy.optString("expires_in", "")));
        SesameLoginInfo.setUseId((destroy.optString("uid", "")));
        SesameLoginInfo.setOptionid((destroy.optString("optionid", "")));
        SesameLoginInfo.setCarid((destroy.optString("carid", "")));
        SesameLoginInfo.setCarprovice((destroy.optString("carprovice", "")));
        SesameLoginInfo.setCarcity((destroy.optString("city", "")));
        SesameLoginInfo.setDeviceidstring((destroy.optString("deviceidstring", "")));
        SesameLoginInfo.setVpin(mobile, (destroy.optString("vpin", "")));
        SesameLoginInfo.setServiceExpire((destroy.optBoolean("service_time_expire")));
        SesameLoginInfo.setCarno((destroy.optString("carno", "")));
        SesameLoginInfo.setCarname((destroy.optString("carname", "")));
        SesameLoginInfo.setCarlogo((destroy.optString("carlogo", "")));
        SesameLoginInfo.setBrandid((destroy.optString("brandid", "")));
        SesameLoginInfo.setSecretaryName((destroy.optString("name", "")));
        SesameLoginInfo.setSecretaryImg(0);
        SesameLoginInfo.setSLCarLocating(0);
        SesameLoginInfo.setAutoCloseWinSw(0);
        SesameLoginInfo.setRemoteStart(0);
        SesameLoginInfo.setTireable((destroy.optBoolean("tireable")));
        SesameLoginInfo.setDealerUsername((destroy.optString("username", "")));
        SesameLoginInfo.setDealerAddres((destroy.optString("addres", "")));
        SesameLoginInfo.setDealerTel(destroy.optString("tel"));
        SesameLoginInfo.setServiceTel(destroy.optString("tel1"));

        SesameLoginInfo.setLately_day((destroy.optString("day", "")));
        SesameLoginInfo.setLately_week((destroy.optString("week", "")));
        SesameLoginInfo.setLately_month((destroy.optString("month", "")));

        SesameLoginInfo.setMainten_miles((destroy.optString("mainten_miles", "")));
        SesameLoginInfo.setMainten_time((destroy.optString("mainten_time", "")));
        SesameLoginInfo.setMainten_next_miles((destroy.optString(
                "mainten_next_miles", "")));
        SesameLoginInfo.setMainten_next_day((destroy.optString("mainten_next_date",
                "")));
        SesameLoginInfo.setNeed_pin((destroy.optString("need_pin",
                "")));
        SesameLoginInfo.setMainten((destroy.optBoolean("isMainten")));

        SesameLoginInfo.setMain(destroy.optBoolean("is_main"));
        SesameLoginInfo.setMainDevicename(destroy.optString("move_device_name", ""));
        SesameLoginInfo.setMainDeviceid(destroy.optString("move_deviceid", ""));
        SesameLoginInfo
                .setAuthorize_status(destroy.optString("authorize_switch", ""));
        SesameLoginInfo.setHasAuthorize(destroy.optBoolean("has_authorize"));
        SesameLoginInfo.setNeedAuthorize(destroy.optBoolean("need_authorize"));
        SesameLoginInfo.setFreezing(destroy.optBoolean("is_freezing"));
        SesameLoginInfo.setAuthen(destroy.optBoolean("is_authen"));
        SesameLoginInfo.setAuthen_name(destroy.optString("authen_name", ""));
        SesameLoginInfo.setAuthen_card(destroy.optString("authen_card", ""));
        SesameLoginInfo.setStandcarno(destroy.optString("standcarno", ""));

        SesameLoginInfo.setCar_year(destroy.optInt("authen_card", 0));

    }

    public static String getToken() {
        token = user_pref.getString("token", token);
        return token;
    }

    public static void setToken(String token) {
        SesameLoginInfo.token = token;
        user_pref.edit().putString("token", token).commit();
    }

    public static String getNeed_pin() {
        need_pin = user_pref.getString("need_pin", need_pin);
        return need_pin;
    }

    public static void setNeed_pin(String need_pin) {
        SesameLoginInfo.need_pin = need_pin;
        user_pref.edit().putString("need_pin", need_pin).commit();
    }

    public static String getExpiresIn() {
        expiresIn = user_pref.getString("expiresIn", expiresIn);
        return expiresIn;
    }

    public static void setExpiresIn(String expiresIn) {
        SesameLoginInfo.expiresIn = expiresIn;
        user_pref.edit().putString("expiresIn", expiresIn).commit();
    }

    public static String getDealerId() {
        dealerId = user_pref.getString("dealerId", dealerId);
        return dealerId;
    }

    public static void setDealerId(String dealerId) {
        SesameLoginInfo.dealerId = dealerId;
        user_pref.edit().putString("dealerId", dealerId).commit();
    }

    public static String getUseId() {
        useId = user_pref.getString("useId", useId);
        return useId;
    }

    public static void setUseId(String useId) {
        SesameLoginInfo.useId = useId;
        user_pref.edit().putString("useId", useId).commit();
    }

    public static String getMobile() {
        mobile = user_pref.getString("mobile", mobile);
        return mobile;
    }

    public static void setMobile(String mobile) {
        SesameLoginInfo.mobile = mobile;
        user_pref.edit().putString("mobile", mobile).commit();
    }

    public static boolean isVisitor() {
        isVisitor = user_pref.getBoolean("isVisitor", isVisitor);
        return isVisitor;
    }

    public static void setVisitor(boolean isVisitor) {
        SesameLoginInfo.isVisitor = isVisitor;
        user_pref.edit().putBoolean("isVisitor", isVisitor).commit();
    }

    public static String getUsername() {
        username = user_pref.getString("username", username);
        return username;
    }

    public static void setUsername(String username) {
        SesameLoginInfo.username = username;
        user_pref.edit().putString("username", username).commit();
    }

    public static String getSSID() {
        SSID = user_pref.getString("SSID", SSID);
        return SSID;
    }

    public static void setSSID(String sSID) {
        SSID = sSID;
        user_pref.edit().putString("SSID", SSID).commit();
    }

    public static String getSSIDPWD() {
        SSIDPWD = user_pref.getString("SSIDPWD", SSIDPWD);
        return SSIDPWD;
    }

    public static void setSSIDPWD(String sSIDPWD) {
        SSIDPWD = sSIDPWD;
        user_pref.edit().putString("SSIDPWD", SSIDPWD).commit();
    }

    public static String getLifetime() {
        lifetime = user_pref.getString("lifetime", lifetime);
        return lifetime;
    }

    public static void setLifetime(String lifetime) {
        SesameLoginInfo.lifetime = lifetime;
        user_pref.edit().putString("lifetime", lifetime).commit();
    }

    public static String getWeixinbind() {
        lifetime = user_pref.getString("weixinbind", weixinbind);
        return weixinbind;
    }

    public static void setWeixinbind(String weixinbind) {
        SesameLoginInfo.weixinbind = weixinbind;
        user_pref.edit().putString("weixinbind", weixinbind).commit();
    }

    public static String getClwbind() {
        clwbind = user_pref.getString("clwbind", clwbind);
        return clwbind;
    }

    public static void setClwbind(String clwbind) {
        SesameLoginInfo.clwbind = clwbind;
        user_pref.edit().putString("clwbind", clwbind).commit();
    }

    public static String getRegip() {
        regip = user_pref.getString("regip", regip);
        return regip;
    }

    public static void setRegip(String regip) {
        SesameLoginInfo.regip = regip;
        user_pref.edit().putString("regip", regip).commit();
    }

    public static String getAvatar_id() {
        avatar_id = user_pref.getString("avatar_id", avatar_id);
        return avatar_id;
    }

    public static void setAvatar_id(String avatar_id) {
        SesameLoginInfo.avatar_id = avatar_id;
        user_pref.edit().putString("avatar_id", avatar_id).commit();
    }

    public static String getOriginate() {
        originate = user_pref.getString("originate", originate);
        return originate;
    }

    public static void setOriginate(String originate) {
        SesameLoginInfo.originate = originate;
        user_pref.edit().putString("originate", originate).commit();
    }

    public static String getLastlogin() {
        lastlogin = user_pref.getString("lastlogin", lastlogin);
        return lastlogin;
    }

    public static void setLastlogin(String lastlogin) {
        SesameLoginInfo.lastlogin = lastlogin;
        user_pref.edit().putString("lastlogin", lastlogin).commit();
    }

    public static String getLoginoauth() {
        loginoauth = user_pref.getString("loginoauth", loginoauth);
        return loginoauth;
    }

    public static void setLoginoauth(String loginoauth) {
        SesameLoginInfo.loginoauth = loginoauth;
        user_pref.edit().putString("loginoauth", loginoauth).commit();
    }

    public static String getLogintimes() {
        logintimes = user_pref.getString("logintimes", logintimes);
        return logintimes;
    }

    public static void setLogintimes(String logintimes) {
        SesameLoginInfo.logintimes = logintimes;
        user_pref.edit().putString("logintimes", logintimes).commit();
    }

    public static String getCreatedate() {
        createdate = user_pref.getString("createdate", createdate);
        return createdate;
    }

    public static void setCreatedate(String createdate) {
        SesameLoginInfo.createdate = createdate;
        user_pref.edit().putString("createdate", createdate).commit();
    }

    public static String getAccess_token() {
        access_token = user_pref.getString("access_token", access_token);
        return access_token;
    }

    public static void setAccess_token(String access_token) {
        SesameLoginInfo.access_token = access_token;
        user_pref.edit().putString("access_token", access_token).commit();
    }

    public static String getExpires_in() {
        expires_in = user_pref.getString("expires_in", expires_in);
        return expires_in;
    }

    public static void setExpires_in(String expires_in) {
        SesameLoginInfo.expires_in = expires_in;
        user_pref.edit().putString("expires_in", expires_in).commit();
    }

    public static String getcId() {
        cId = car_pref.getString("cId", cId);
        return cId;
    }

    public static void setcId(String cId) {
        SesameLoginInfo.cId = cId;
        car_pref.edit().putString("cId", cId).commit();
    }

    public static String getBrandid() {
        brandid = car_pref.getString("brandid", brandid);
        return brandid;
    }

    public static int getActivate_status() {
        activate_status = car_pref.getInt("activate_status", -1);
        return activate_status;
    }

    public static void setActivate_status(int activate_status) {
        SesameLoginInfo.activate_status = activate_status;
        car_pref.edit().putInt("activate_status", activate_status).commit();
    }

    public static void setBrandid(String brandid) {
        SesameLoginInfo.brandid = brandid;
        car_pref.edit().putString("brandid", brandid).commit();
    }

    public static String getColor() {
        color = car_pref.getString("color", color);
        return color;
    }

    public static void setColor(String color) {
        SesameLoginInfo.color = color;
        car_pref.edit().putString("color", color).commit();
    }

    public static String getProvince() {
        province = car_pref.getString("province", province);
        return province;
    }

    public static void setProvince(String province) {
        SesameLoginInfo.province = province;
        car_pref.edit().putString("province", province).commit();
    }

    public static String getCity() {
        city = car_pref.getString("city", city);
        return city;
    }

    public static void setCity(String city) {
        SesameLoginInfo.city = city;
        car_pref.edit().putString("city", city).commit();
    }

    public static String getDeviceid() {
        deviceid = car_pref.getString("deviceid", deviceid);
        return deviceid;
    }

    public static void setDeviceid(String deviceid) {
        SesameLoginInfo.deviceid = deviceid;
        car_pref.edit().putString("deviceid", deviceid).commit();
    }

    public static String getModelid() {
        modelid = car_pref.getString("modelid", modelid);
        return modelid;
    }

    public static void setModelid(String modelid) {
        SesameLoginInfo.modelid = modelid;
        car_pref.edit().putString("modelid", modelid).commit();
    }

    public static String getLicencelevelid() {
        licencelevelid = car_pref.getString("licencelevelid", licencelevelid);
        return licencelevelid;
    }

    public static void setLicencelevelid(String licencelevelid) {
        SesameLoginInfo.licencelevelid = licencelevelid;
        car_pref.edit().putString("licencelevelid", licencelevelid).commit();
    }

    public static String getIs_net_sale() {
        is_net_sale = car_pref.getString("is_net_sale", is_net_sale);
        return is_net_sale;
    }

    public static void setIs_net_sale(String is_net_sale) {
        SesameLoginInfo.is_net_sale = is_net_sale;
        car_pref.edit().putString("is_net_sale", is_net_sale).commit();
    }

    public static String getFixed_miles() {
        fixed_miles = car_pref.getString("fixed_miles", fixed_miles);
        return fixed_miles;
    }

    public static void setFixed_miles(String fixed_miles) {
        SesameLoginInfo.fixed_miles = fixed_miles;
        car_pref.edit().putString("fixed_miles", fixed_miles).commit();
    }

    public static String getPowerontime() {
        powerontime = car_pref.getString("powerontime", powerontime);
        return powerontime;
    }

    public static void setPowerontime(String powerontime) {
        SesameLoginInfo.powerontime = powerontime;
        car_pref.edit().putString("powerontime", powerontime).commit();
    }

    public static String getVC() {
        VC = car_pref.getString("VC", VC);
        return VC;
    }

    public static void setVC(String vC) {
        VC = vC;
        car_pref.edit().putString("VC", VC).commit();
    }

    public static String getProtocol() {
        protocol = car_pref.getString("protocol", protocol);
        return protocol;
    }

    public static void setProtocol(String protocol) {
        SesameLoginInfo.protocol = protocol;
        car_pref.edit().putString("protocol", protocol).commit();
    }

    public static String getAccePark() {
        accePark = car_pref.getString("accePark", accePark);
        return accePark;
    }

    public static void setAccePark(String accePark) {
        SesameLoginInfo.accePark = accePark;
        car_pref.edit().putString("accePark", accePark).commit();
    }

    public static String getAcceRun() {
        acceRun = car_pref.getString("acceRun", acceRun);
        return acceRun;
    }

    public static void setAcceRun(String acceRun) {
        SesameLoginInfo.acceRun = acceRun;
        car_pref.edit().putString("acceRun", acceRun).commit();
    }

    public static String getAcceDir() {
        acceDir = car_pref.getString("acceDir", acceDir);
        return acceDir;
    }

    public static void setAcceDir(String acceDir) {
        SesameLoginInfo.acceDir = acceDir;
        car_pref.edit().putString("acceDir", acceDir).commit();
    }

    public static String getVBatTh() {
        VBatTh = car_pref.getString("VBatTh", VBatTh);
        return VBatTh;
    }

    public static void setVBatTh(String vBatTh) {
        VBatTh = vBatTh;
        car_pref.edit().putString("VBatTh", VBatTh).commit();
    }

    public static String getVbat() {
        Vbat = car_pref.getString("Vbat", Vbat);
        return Vbat;
    }

    public static void setVbat(String vbat) {
        Vbat = vbat;
        car_pref.edit().putString("Vbat", Vbat).commit();
    }

    public static String getSecretaryid() {
        secretaryid = car_pref.getString("secretaryid", secretaryid);
        return secretaryid;
    }

    public static void setSecretaryid(String secretaryid) {
        SesameLoginInfo.secretaryid = secretaryid;
        car_pref.edit().putString("secretaryid", secretaryid).commit();
    }

    public static String getCredit() {
        credit = car_pref.getString("credit", credit);
        return credit;
    }

    public static void setCredit(String credit) {
        SesameLoginInfo.credit = credit;
        car_pref.edit().putString("credit", credit).commit();
    }

    public static String getLicencenumber() {
        licencenumber = car_pref.getString("licencenumber", licencenumber);
        return licencenumber;
    }

    public static void setLicencenumber(String licencenumber) {
        SesameLoginInfo.licencenumber = licencenumber;
        car_pref.edit().putString("licencenumber", licencenumber).commit();
    }

    public static String getLicencedate() {
        licencedate = car_pref.getString("licencedate", licencedate);
        return licencedate;
    }

    public static void setLicencedate(String licencedate) {
        SesameLoginInfo.licencedate = licencedate;
        car_pref.edit().putString("licencedate", licencedate).commit();
    }

    public static String getIsrunning() {
        isrunning = car_pref.getString("isrunning", isrunning);
        return isrunning;
    }

    public static void setIsrunning(String isrunning) {
        SesameLoginInfo.isrunning = isrunning;
        car_pref.edit().putString("isrunning", isrunning).commit();
    }

    public static String getTag() {
        tag = car_pref.getString("tag", tag);
        return tag;
    }

    public static void setTag(String tag) {
        SesameLoginInfo.tag = tag;
        car_pref.edit().putString("tag", tag).commit();
    }

    public static String getUpdatedate() {
        updatedate = car_pref.getString("updatedate", updatedate);
        return updatedate;
    }

    public static void setUpdatedate(String updatedate) {
        SesameLoginInfo.updatedate = updatedate;
        car_pref.edit().putString("updatedate", updatedate).commit();
    }

    public static String getCity_code_id() {
        city_code_id = car_pref.getString("city_code_id", city_code_id);
        return city_code_id;
    }

    public static void setCity_code_id(String city_code_id) {
        SesameLoginInfo.city_code_id = city_code_id;
        car_pref.edit().putString("city_code_id", city_code_id).commit();
    }

    public static String getCity_code() {
        city_code = car_pref.getString("city_code", city_code);
        return city_code;
    }

    public static void setCity_code(String city_code) {
        SesameLoginInfo.city_code = city_code;
        car_pref.edit().putString("city_code", city_code).commit();
    }

    public static String getCarno() {
        carno = car_pref.getString("carno", carno);
        return carno;
    }

    public static void setCarno(String carno) {
        SesameLoginInfo.carno = carno;
        car_pref.edit().putString("carno", carno).commit();
    }

    public static String getShortstandcarno() {
        shortstandcarno = car_pref
                .getString("shortstandcarno", shortstandcarno);
        return shortstandcarno;
    }

    public static void setShortstandcarno(String shortstandcarno) {
        SesameLoginInfo.shortstandcarno = shortstandcarno;
        car_pref.edit().putString("shortstandcarno", shortstandcarno).commit();
    }

    public static String getDealerName() {
        dealerName = car_pref.getString("dealerName", dealerName);
        return dealerName;
    }

    public static void setDealerName(String dealerName) {
        SesameLoginInfo.dealerName = dealerName;
        car_pref.edit().putString("dealerName", dealerName).commit();
    }

    public static String getOptionid() {
        optionid = car_pref.getString("optionid", optionid);
        return optionid;
    }

    public static void setOptionid(String optionid) {
        SesameLoginInfo.optionid = optionid;
        car_pref.edit().putString("optionid", optionid).commit();
    }

    public static String getCarid() {
        carid = car_pref.getString("carid", carid);
        return carid;
    }

    public static void setCarid(String carid) {
        SesameLoginInfo.carid = carid;
        car_pref.edit().putString("carid", carid).commit();
    }

    public static String getCarname() {
        carname = car_pref.getString("carname", carname);
        return carname;
    }

    public static void setCarname(String carname) {
        SesameLoginInfo.carname = carname;
        car_pref.edit().putString("carname", carname).commit();
    }

    public static String getCarlogo() {
        carlogo = car_pref.getString("carlogo", carlogo);
        return carlogo;
    }

    public static void setCarlogo(String carlogo) {
        SesameLoginInfo.carlogo = carlogo;
        car_pref.edit().putString("carlogo", carlogo).commit();
    }

    public static String getCanQueryVio() {
        canQueryVio = car_pref.getString("canQueryVio", canQueryVio);
        return canQueryVio;
    }

    public static void setCanQueryVio(String canQueryVio) {
        SesameLoginInfo.canQueryVio = canQueryVio;
        car_pref.edit().putString("canQueryVio", canQueryVio).commit();
    }

    public static String getCarprovice() {
        carprovice = car_pref.getString("carprovice", carprovice);
        return carprovice;
    }

    public static void setCarprovice(String carprovice) {
        SesameLoginInfo.carprovice = carprovice;
        car_pref.edit().putString("carprovice", carprovice).commit();
    }

    public static String getCarcity() {
        carcity = car_pref.getString("carcity", carcity);
        return carcity;
    }

    public static void setCarcity(String carcity) {
        SesameLoginInfo.carcity = carcity;
        car_pref.edit().putString("carcity", carcity).commit();
    }

    public static String getStandcarno() {
        standcarno = car_pref.getString("standcarno", standcarno);
        return standcarno;
    }

    public static void setStandcarno(String standcarno) {
        SesameLoginInfo.standcarno = standcarno;
        car_pref.edit().putString("standcarno", standcarno).commit();
    }

    public static String getEngineno() {
        engineno = car_pref.getString("engineno", engineno);
        return engineno;
    }

    public static void setEngineno(String engineno) {
        SesameLoginInfo.engineno = engineno;
        car_pref.edit().putString("engineno", engineno).commit();
    }

    public static String getRegistno() {
        registno = car_pref.getString("registno", registno);
        return registno;
    }

    public static void setRegistno(String registno) {
        SesameLoginInfo.registno = registno;
        car_pref.edit().putString("registno", registno).commit();
    }

    public static String getSummileage() {
        summileage = car_pref.getString("summileage", summileage);
        return summileage;
    }

    public static void setSummileage(String summileage) {
        SesameLoginInfo.summileage = summileage;
        car_pref.edit().putString("summileage", summileage).commit();
    }

    public static String getBuydate() {
        buydate = car_pref.getString("buydate", buydate);
        return buydate;
    }

    public static void setBuydate(String buydate) {
        SesameLoginInfo.buydate = buydate;
        car_pref.edit().putString("buydate", buydate).commit();
    }

    public static String getRealname() {
        realname = car_pref.getString("realname", realname);
        return realname;
    }

    public static void setRealname(String realname) {
        SesameLoginInfo.realname = realname;
        car_pref.edit().putString("realname", realname).commit();
    }

    public static String getDeviceidstring() {
        deviceidstring = car_pref.getString("deviceidstring", deviceidstring);
        return deviceidstring;
    }

    public static void setDeviceidstring(String deviceidstring) {
        SesameLoginInfo.deviceidstring = deviceidstring;
        car_pref.edit().putString("deviceidstring", deviceidstring).commit();
    }

    public static String getVpin(String mobile) {
        vpin = car_pref.getString(mobile, "");
        return vpin;
    }

    public static void setVpin(String mobile, String vpin) {
        SesameLoginInfo.vpin = vpin;
        car_pref.edit().putString(mobile, vpin).commit();
    }

    public static boolean isServiceExpire() {
        isServiceExpire = user_ext_pref
                .getBoolean("service_time_expire", false);
        return isServiceExpire;
    }

    public static void setServiceExpire(boolean isServiceExpire) {
        SesameLoginInfo.isServiceExpire = isServiceExpire;
        user_ext_pref.edit().putBoolean("service_time_expire", isServiceExpire)
                .commit();
    }

    public static boolean isBindCar() {
        isBindCar = car_pref.getBoolean("isBindCar", false);
        return isBindCar;
    }

    public static void setBindCar(boolean isBindCar) {
        SesameLoginInfo.isBindCar = isBindCar;
        car_pref.edit().putBoolean("isBindCar", isBindCar).commit();
    }

    public static boolean isDeviceActivate() {
        isDeviceActivate = car_pref.getBoolean("isDeviceActivate", false);
        return isDeviceActivate;
    }

    public static void setDeviceActivate(boolean isDeviceActivate) {
        SesameLoginInfo.isDeviceActivate = isDeviceActivate;
        car_pref.edit().putBoolean("isDeviceActivate", isDeviceActivate)
                .commit();
    }

    public static boolean isUpgradeing() {
        isUpgradeing = car_pref.getBoolean("isUpgradeing", false);
        return isUpgradeing;
    }

    public static void setUpgradeing(boolean isUpgradeing) {
        SesameLoginInfo.isUpgradeing = isUpgradeing;
        car_pref.edit().putBoolean("isUpgradeing", isUpgradeing).commit();
    }

    public static boolean isGpsDevice() {
        isGpsDevice = car_pref.getBoolean("isGpsDevice", false);
        return isGpsDevice;
    }

    public static void setGpsDevice(boolean isGpsDevice) {
        SesameLoginInfo.isGpsDevice = isGpsDevice;
        car_pref.edit().putBoolean("isGpsDevice", isGpsDevice).commit();
    }

    public static String getAvatar_img() {
        avatar_img = car_pref.getString("avatar_img", avatar_img);
        return avatar_img;
    }

    public static void setAvatar_img(String avatar_img) {
        SesameLoginInfo.avatar_img = avatar_img;
        car_pref.edit().putString("avatar_img", avatar_img).commit();
    }

    public static String getGender() {
        gender = car_pref.getString("gender", gender);
        return gender;
    }

    public static void setGender(String gender) {
        SesameLoginInfo.gender = gender;
        car_pref.edit().putString("gender", gender).commit();
    }

    public static String getStatus() {
        status = car_pref.getString("status", status);
        return status;
    }

    public static void setStatus(String status) {
        SesameLoginInfo.status = status;
        car_pref.edit().putString("status", status).commit();
    }

    public static boolean isMain() {
        isMain = car_pref.getBoolean("isMain", false);
        return isMain;
    }

    public static void setMain(boolean isMain) {
        SesameLoginInfo.isMain = isMain;
        car_pref.edit().putBoolean("isMain", isMain).commit();
    }

    public static String getMainDevicename() {
        mainDevicename = car_pref.getString("mainDevicename", mainDevicename);
        return mainDevicename;
    }

    public static void setMainDevicename(String mainDevicename) {
        SesameLoginInfo.mainDevicename = mainDevicename;
        car_pref.edit().putString("mainDevicename", mainDevicename).commit();
    }

    public static String getMainDeviceid() {
        mainDeviceid = car_pref.getString("mainDeviceid", mainDeviceid);
        return mainDeviceid;
    }

    public static void setMainDeviceid(String mainDeviceid) {
        SesameLoginInfo.mainDeviceid = mainDeviceid;
        car_pref.edit().putString("mainDeviceid", mainDeviceid).commit();
    }

    public static String getAuthorize_status() {
        authorize_status = user_pref.getString("authorize_status",
                authorize_status);
        return authorize_status;
    }

    public static void setAuthorize_status(String authorize_status) {
        SesameLoginInfo.authorize_status = authorize_status;
        user_pref.edit().putString("authorize_status", authorize_status)
                .commit();
    }

    public static boolean isHasAuthorize() {
        hasAuthorize = user_pref.getBoolean("hasAuthorize", false);
        return hasAuthorize;
    }

    public static void setHasAuthorize(boolean hasAuthorize) {
        SesameLoginInfo.hasAuthorize = hasAuthorize;
        user_pref.edit().putBoolean("hasAuthorize", hasAuthorize).commit();
    }

    public static boolean isNeedAuthorize() {
        needAuthorize = user_pref.getBoolean("needAuthorize", false);
        return needAuthorize;
    }

    public static void setNeedAuthorize(boolean needAuthorize) {
        SesameLoginInfo.needAuthorize = needAuthorize;
        user_pref.edit().putBoolean("needAuthorize", needAuthorize).commit();
    }

    public static boolean isFreezing() {
        isFreezing = user_pref.getBoolean("isFreezing", false);
        return isFreezing;
    }

    public static void setFreezing(boolean isFreezing) {
        SesameLoginInfo.isFreezing = isFreezing;
        user_pref.edit().putBoolean("isFreezing", isFreezing).commit();
    }

    public static boolean isAuthen() {
        isAuthen = user_pref.getBoolean("isAuthen", false);
        return isAuthen;
    }

    public static void setAuthen(boolean isAuthen) {
        SesameLoginInfo.isAuthen = isAuthen;
        user_pref.edit().putBoolean("isAuthen", isAuthen).commit();
    }

    public static boolean isNoneedpsw() {
        isNoneedpsw = user_pref.getBoolean("isNoneedpsw", false);
        return isNoneedpsw;
    }

    public static void setNoneedpsw(boolean isNoneedpsw) {
        SesameLoginInfo.isNoneedpsw = isNoneedpsw;
        user_pref.edit().putBoolean("isNoneedpsw", isNoneedpsw).commit();
    }

    public static String getAuthen_name() {
        authen_name = user_pref.getString("authen_name", authen_name);
        return authen_name;
    }

    public static void setAuthen_name(String authen_name) {
        SesameLoginInfo.authen_name = authen_name;
        user_pref.edit().putString("authen_name", authen_name).commit();
    }

    public static String getAuthen_card() {
        authen_card = user_pref.getString("authen_card", authen_card);
        return authen_card;
    }

    public static void setAuthen_card(String authen_card) {
        SesameLoginInfo.authen_card = authen_card;
        user_pref.edit().putString("authen_card", authen_card).commit();
    }

    public static boolean isSupport() {
        isSupport = car_pref.getBoolean("isSupport", false);
        return isSupport;
    }

    public static void setSupport(boolean isSupport) {
        SesameLoginInfo.isSupport = isSupport;
        car_pref.edit().putBoolean("isSupport", isSupport).commit();
    }

    public static String getSecretaryName() {
        secretaryName = car_pref.getString("secretaryName", secretaryName);
        return secretaryName;
    }

    public static void setSecretaryName(String secretaryName) {
        SesameLoginInfo.secretaryName = secretaryName;
        car_pref.edit().putString("secretaryName", secretaryName).commit();
    }

    public static int getSecretaryImg() {
        secretaryImg = car_pref.getInt("secretaryImg", secretaryImg);
        return secretaryImg;
    }

    public static void setSecretaryImg(int secretaryImg) {
        SesameLoginInfo.secretaryImg = secretaryImg;
        car_pref.edit().putInt("secretaryImg", secretaryImg).commit();
    }

    public static String getDealerUsername() {
        dealerUsername = car_pref.getString("dealerUsername", dealerUsername);
        return dealerUsername;
    }

    public static void setDealerUsername(String dealerUsername) {
        SesameLoginInfo.dealerUsername = dealerUsername;
        car_pref.edit().putString("dealerUsername", dealerUsername).commit();
    }

    public static String getDealerAddres() {
        dealerAddres = car_pref.getString("dealerAddres", dealerAddres);
        return dealerAddres;
    }

    public static void setDealerAddres(String dealerAddres) {
        SesameLoginInfo.dealerAddres = dealerAddres;
        car_pref.edit().putString("dealerAddres", dealerAddres).commit();
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
        SesameLoginInfo.dealerLat = dealerLat;
        car_pref.edit().putString("dealerLat", Double.toString(dealerLat))
                .commit();
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
        SesameLoginInfo.dealerLon = dealerLon;
        car_pref.edit().putString("dealerLon", Double.toString(dealerLon))
                .commit();
    }

    public static int getDealerZoom() {
        dealerZoom = car_pref.getInt("dealerZoom", dealerZoom);
        return dealerZoom;
    }

    public static void setDealerZoom(int dealerZoom) {
        SesameLoginInfo.dealerZoom = dealerZoom;
        car_pref.edit().putInt("dealerZoom", dealerZoom).commit();
    }

    public static String getDealerTel() {
        dealerTel = car_pref.getString("dealerTel", dealerTel);
        return dealerTel;
    }

    public static void setDealerTel(String dealerTel) {
        SesameLoginInfo.dealerTel = dealerTel;
        car_pref.edit().putString("dealerTel", dealerTel).commit();
    }

    public static String getServiceTel() {
        serviceTel = car_pref.getString("serviceTel", serviceTel);
        return serviceTel;
    }

    public static void setServiceTel(String serviceTel) {
        SesameLoginInfo.serviceTel = serviceTel;
        car_pref.edit().putString("serviceTel", serviceTel).commit();
    }

    public static int getPush_prizeinfo_flag() {
        push_prizeinfo_flag = car_pref.getInt("push_prizeinfo_flag",
                push_prizeinfo_flag);
        return push_prizeinfo_flag;
    }

    public static void setPush_prizeinfo_flag(int push_prizeinfo_flag) {
        SesameLoginInfo.push_prizeinfo_flag = push_prizeinfo_flag;
        car_pref.edit().putInt("push_prizeinfo_flag", push_prizeinfo_flag)
                .commit();
    }

    public static int getSLCarLocating() {
        SLCarLocating = car_pref.getInt("SLCarLocating", SLCarLocating);
        return SLCarLocating;
    }

    public static void setSLCarLocating(int sLCarLocating) {
        SLCarLocating = sLCarLocating;
        car_pref.edit().putInt("SLCarLocating", SLCarLocating).commit();
    }

    public static int getAutoCloseWinSw() {
        autoCloseWinSw = car_pref.getInt("autoCloseWinSw", autoCloseWinSw);
        return autoCloseWinSw;
    }

    public static void setAutoCloseWinSw(int autoCloseWinSw) {
        SesameLoginInfo.autoCloseWinSw = autoCloseWinSw;
        car_pref.edit().putInt("autoCloseWinSw", autoCloseWinSw).commit();
    }

    public static int getRemoteStart() {
        remoteStart = car_pref.getInt("remoteStart", remoteStart);
        return remoteStart;
    }

    public static void setRemoteStart(int remoteStart) {
        SesameLoginInfo.remoteStart = remoteStart;
        car_pref.edit().putInt("remoteStart", remoteStart).commit();
    }

    public static boolean isTireable() {
        isTireable = car_pref.getBoolean("isTireable", isTireable);
        return isTireable;
    }

    public static void setTireable(boolean isTireable) {
        SesameLoginInfo.isTireable = isTireable;
        car_pref.edit().putBoolean("isTireable", isTireable).commit();
    }

    public static String getLately_day() {
        lately_day = car_pref.getString("lately_day", lately_day);
        return lately_day;
    }

    public static void setLately_day(String lately_day) {
        SesameLoginInfo.lately_day = lately_day;
        car_pref.edit().putString("lately_day", lately_day).commit();
    }

    public static String getLately_week() {
        lately_week = car_pref.getString("lately_week", lately_week);
        return lately_week;
    }

    public static void setLately_week(String lately_week) {
        SesameLoginInfo.lately_week = lately_week;
        car_pref.edit().putString("lately_week", lately_week).commit();
    }

    public static String getLately_month() {
        lately_month = car_pref.getString("lately_month", lately_month);
        return lately_month;
    }

    public static void setLately_month(String lately_month) {
        SesameLoginInfo.lately_month = lately_month;
        car_pref.edit().putString("lately_month", lately_month).commit();
    }

    public static String getMainten_miles() {
        mainten_miles = car_pref.getString("mainten_miles", mainten_miles);
        return mainten_miles;
    }

    public static void setMainten_miles(String mainten_miles) {
        SesameLoginInfo.mainten_miles = mainten_miles;
        car_pref.edit().putString("mainten_miles", mainten_miles).commit();
    }

    public static String getMainten_time() {
        mainten_time = car_pref.getString("mainten_time", mainten_time);
        return mainten_time;
    }

    public static void setMainten_time(String mainten_time) {
        SesameLoginInfo.mainten_time = mainten_time;
        car_pref.edit().putString("mainten_time", mainten_time).commit();
    }

    public static String getMainten_next_miles() {
        mainten_next_miles = car_pref.getString("mainten_next_miles",
                mainten_next_miles);
        return mainten_next_miles;
    }

    public static void setRemoteControl(String control) {
        SesameLoginInfo.remoteControl = control;
        car_pref.edit().putString("remoteControl", remoteControl).commit();
    }

    public static String getRemoteControl() {
        remoteControl = car_pref.getString("remoteControl", remoteControl);
        return remoteControl;
    }

    public static void setMainten_next_miles(String mainten_next_miles) {
        SesameLoginInfo.mainten_next_miles = mainten_next_miles;
        car_pref.edit().putString("mainten_next_miles", mainten_next_miles)
                .commit();
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
        SesameLoginInfo.devicetype = devicetype;
        car_pref.edit().putString("devicetype", devicetype).commit();
    }

    public static void setMainten_next_day(String mainten_next_day) {
        SesameLoginInfo.mainten_next_day = mainten_next_day;
        car_pref.edit().putString("mainten_next_day", mainten_next_day)
                .commit();
    }

    public static boolean isMainten() {
        isMainten = car_pref.getBoolean("isMainten", isMainten);
        return isMainten;
    }

    public static void setMainten(boolean isMainten) {
        SesameLoginInfo.isMainten = isMainten;
        car_pref.edit().putBoolean("isMainten", isMainten).commit();
    }

    public static String getIsJumptoBind() {
        isJumptoBind = car_pref.getString("isJumptoBind", "0");
        return isJumptoBind;
    }

    public static void setIsJumptoBind(String isJumptoBind) {
        SesameLoginInfo.isJumptoBind = isJumptoBind;
        car_pref.edit().putString("isJumptoBind", isJumptoBind).commit();
    }

    public static boolean isInstallorder() {
        isInstallorder = car_pref.getBoolean("installorder", false);
        return isInstallorder;
    }

    public static void setInstallorder(boolean isInstallorder) {
        SesameLoginInfo.isInstallorder = isInstallorder;
        car_pref.edit().putBoolean("installorder", isInstallorder).commit();
    }

    public static String getDeviceType() {
        deviceType = car_pref.getString("deviceType", deviceType);
        return deviceType;
    }

    public static void setDeviceType(String deviceType) {
        SesameLoginInfo.deviceType = deviceType;
        car_pref.edit().putString("deviceType", deviceType).commit();
    }

    public static boolean isSetRemotePwd() {
        isSetRemotePwd = user_pref.getBoolean("isSetRemotePwd", isSetRemotePwd);
        return isSetRemotePwd;
    }

    public static void setSetRemotePwd(boolean isSetRemotePwd) {
        SesameLoginInfo.isSetRemotePwd = isSetRemotePwd;
        user_pref.edit().putBoolean("isSetRemotePwd", isSetRemotePwd).commit();
    }

    public static boolean isRemoteSoundOpen() {
        return isRemoteSoundOpen = user_pref.getBoolean("isRemoteSoundOpen", isRemoteSoundOpen);
    }

    public static void setRemoteSoundOpen(boolean isRemoteSoundOpen) {
        SesameLoginInfo.isRemoteSoundOpen = isRemoteSoundOpen;
        user_pref.edit().putBoolean("isRemoteSoundOpen", isRemoteSoundOpen)
                .commit();
    }

    public static int getCar_year() {
        car_year = car_pref.getInt("year", CAR_YEAR_2016);
        return car_year;
    }

    public static void setCar_year(int car_year) {
        SesameLoginInfo.car_year = car_year;
        car_pref.edit().putInt("year", car_year).commit();
    }

    public static RemoteMainInfo getRemoteMainInfo() {
        return remoteMainInfo;
    }

    public static void setRemoteMainInfo(RemoteMainInfo remoteMainInfo) {
        SesameLoginInfo.remoteMainInfo = remoteMainInfo;
    }

    public static CarMainFunInfo getCarMainFunInfo() {
        return carMainFunInfo;
    }

    public static void setCarMainFunInfo(CarMainFunInfo carMainFunInfo) {
        SesameLoginInfo.carMainFunInfo = carMainFunInfo;
    }

}
