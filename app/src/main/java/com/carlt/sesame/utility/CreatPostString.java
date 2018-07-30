package com.carlt.sesame.utility;

import android.text.TextUtils;

import com.carlt.doride.DorideApplication;
import com.carlt.sesame.data.SesameLoginInfo;
import com.carlt.sesame.data.RegisteInfo;
import com.carlt.sesame.data.UploadImgInfo;
import com.carlt.sesame.data.car.PostViolationInfo;
import com.carlt.sesame.data.career.SecretaryMessageInfo;
import com.carlt.sesame.data.community.SubmitSOSInfo;
import com.carlt.sesame.data.set.PushSetInfo;
import com.carlt.sesame.systemconfig.URLConfig;
import com.carlt.sesame.ui.activity.setting.ManageMessageActivity;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 作者：秋良 描述：此类用于Post参数生成
 */

public class CreatPostString {

    public static String getEmptyParams() {
        HashMap<String, String> mMap = new HashMap<String, String>();
        return CreatString(mMap);
    }

    public static String getReBindParams(String deviceid) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("deviceid", deviceid);
        return CreatString(mMap);
    }

    // 生成版本更新参数
    public static String getVersion() {
        HashMap<String, String> mMap = new HashMap<String, String>();
        // mMap.put("clienttype", "app");
        mMap.put("softtype", "android");

        mMap.put("version", DorideApplication.Sesame_Version_API + "");
        return CreatString(mMap);
    }

    // 生成版本更新参数
    public static String getCurVersion() {
        HashMap<String, String> mMap = new HashMap<String, String>();
        // mMap.put("clienttype", "app");
        mMap.put("softtype", "android");

        mMap.put("version", DorideApplication.Sesame_Version_API + "");
        return CreatString(mMap);
    }

    public static String getCitys() {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("key", URLConfig.JUHE_APPKEY);
        return CreatString(mMap);
    }

    // 使用车行易
    public static String getCitys2() {
        HashMap<String, String> mMap = new HashMap<String, String>();
        return CreatStringClean(mMap);
    }

    // 生成登录参数
    public static String getLogin(String userName, String psWord) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("version", DorideApplication.Sesame_Version_API + "");
        mMap.put("mobile", userName);
        mMap.put("password", psWord);
        //		mMap.put("app_type", "z");
        //		mMap.put("new_move_deviceid", DorideApplication.NIMEI);
        if (TextUtils.isEmpty(DorideApplication.NIMEI)) {
            mMap.put("move_deviceid", "");
        } else {
            mMap.put("move_deviceid", DorideApplication.NIMEI);
        }

        mMap.put("move_device_name", DorideApplication.MODEL_NAME);
        mMap.put("move_model", DorideApplication.MODEL);
        mMap.put("softtype", "android");
        StringBuffer sysinfo = new StringBuffer(DorideApplication.ANDROID_VERSION);
        sysinfo.append(",");
        sysinfo.append(DorideApplication.DISPLAY);
        sysinfo.append(",");
        sysinfo.append(DorideApplication.MODEL_NAME);
        mMap.put("sysinfo", sysinfo.toString());
        return CreatString(mMap);
    }

    // 生成登录参数
    public static String getCareer() {
        HashMap<String, String> mMap = new HashMap<String, String>();
        return CreatString(mMap);
    }

    // 生成激活设备参数
    public static String getDeviceidActivte(String pin) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("pin", pin);
        return CreatString(mMap);
    }

    // 生成获取设备升级状态参数
    public static String getDeviceUpdate(String deviceid) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("deviceid", deviceid);
        return CreatString(mMap);
    }

    // 生成检测PIN码参数
    public static String getCheckVPin(String vpin) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("vpin", vpin);
        return CreatString(mMap);
    }

    // 生成获取用户相关信息参数
    public static String getExtInfo() {
        HashMap<String, String> mMap = new HashMap<String, String>();
        return CreatString(mMap);
    }

    // 生成激活设备参数
    public static String getDeviceidIsOnLine() {
        HashMap<String, String> mMap = new HashMap<String, String>();
        return CreatString(mMap);
    }

    // 获取经销商车型列表
    public static String getDealerModel(int index, String id) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        switch (index) {
            case 1:

                break;
            case 2:
                mMap.put("pid", id);
                break;
            case 3:
                mMap.put("optionid", id);
                break;
        }
        return CreatString(mMap);
    }

    // 获取经销商车型列表(针对获取车款级别列表)
    public static String getDealerModelV1(String id) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("brandid", id);
        return CreatString(mMap);
    }

    // 获取经销商车型列表(针对获取车系级别列表)
    public static String getDealerModelV2(String id, String is_before) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("optionid", id);
        mMap.put("is_before", is_before);
        return CreatString(mMap);
    }

    // 生成登录参数
    public static String getUserInfo() {
        HashMap<String, String> mMap = new HashMap<String, String>();
        return CreatString(mMap);
    }

    // 生成注册参数
    public static String getRegiste(RegisteInfo mRegisteInfo) {
        HashMap<String, String> mMap = new HashMap<String, String>();

        mMap.put("mobile", mRegisteInfo.getMobile());
        mMap.put("password", mRegisteInfo.getPassWord());
        mMap.put("validate", mRegisteInfo.getValidate());
        if (TextUtils.isEmpty(DorideApplication.NIMEI)) {
            mMap.put("move_deviceid", "");
        } else {
            mMap.put("move_deviceid", DorideApplication.NIMEI);
        }
        //		mMap.put("new_move_deviceid", DorideApplication.NIMEI);
        mMap.put("move_device_name", DorideApplication.MODEL_NAME);
        if (mRegisteInfo.getInvite() != null
                & !mRegisteInfo.getInvite().equals("")) {
            mMap.put("invite", mRegisteInfo.getInvite());
        }
        mMap.put("originate", mRegisteInfo.getOriginate());
        return CreatString(mMap);
    }

    // 生成勋章参数
    public static String getMyLicence() {
        HashMap<String, String> mMap = new HashMap<String, String>();
        return CreatString(mMap);
    }

    // 生成我的奖品列表参数
    public static String getPrize() {
        HashMap<String, String> mMap = new HashMap<String, String>();
        return CreatString(mMap);
    }

    // 生成违章查询列表参数
    public static String getTrafficViolation() {
        HashMap<String, String> mMap = new HashMap<String, String>();
        return CreatString(mMap);
    }

    // 生成首次违章查询参数
    public static String getinitViolation(PostViolationInfo mPostViolationInfo) {
        HashMap<String, String> mMap = new HashMap<String, String>();

        String cityCode = mPostViolationInfo.getCityCodeId();
        if (cityCode != null && !cityCode.equals("")) {
            mMap.put("cityCode", cityCode);
        }
        String carno = mPostViolationInfo.getCarno();
        if (carno != null && !carno.equals("")) {
            mMap.put("carno", carno);
        }
        String engineno = mPostViolationInfo.getEngineno();
        if (engineno != null && !engineno.equals("")) {
            mMap.put("engineno", engineno);
        }
        String standcarno = mPostViolationInfo.getStandcarno();
        if (standcarno != null && !standcarno.equals("")) {
            mMap.put("shortstandcarno", standcarno);
        }
        String registno = mPostViolationInfo.getRegistno();
        if (registno != null && !registno.equals("")) {
            mMap.put("cityCode", registno);
        }
        return CreatString(mMap);
    }

    // 生成首次违章查询参数
    public static String getViolationParams(PostViolationInfo mPostViolationInfo) {
        HashMap<String, String> mMap = new HashMap<String, String>();

        mMap.put("dtype", "json");
        mMap.put("key", URLConfig.JUHE_APPKEY);
        mMap.put("hpzl", "02");

        if (mPostViolationInfo != null) {
            String cityCode = mPostViolationInfo.getCityCodeId();
            if (cityCode != null && !cityCode.equals("")) {
                mMap.put("city", cityCode);
            }
            String carno = mPostViolationInfo.getCarno();
            if (carno != null && !carno.equals("")) {
                mMap.put("hphm", carno);
            }
            String engineno = mPostViolationInfo.getEngineno();
            if (engineno != null && !engineno.equals("")) {
                mMap.put("engineno", engineno);
            }
            String standcarno = mPostViolationInfo.getStandcarno();
            if (standcarno != null && !standcarno.equals("")) {
                mMap.put("classno", standcarno);
            }
            String registno = mPostViolationInfo.getRegistno();
            if (registno != null && !registno.equals("")) {
                mMap.put("registno", registno);
            }
        }
        return CreatString(mMap);
    }

    // 生成车易行违章信息
    public static String getViolation2Params(
            PostViolationInfo mPostViolationInfo) {
        HashMap<String, String> mMap = new HashMap<String, String>();

        mMap.put("userid", "lianyun2014");
        mMap.put("userpwd", "tMXQ3l604mEpciEq2Xgq0A==");

        if (mPostViolationInfo != null) {
            // String cityCode = mPostViolationInfo.getCityCodeId();
            // if (cityCode != null && !cityCode.equals("")) {
            // mMap.put("city", cityCode);
            // }
            String carno = mPostViolationInfo.getCarno();
            if (carno != null && !carno.equals("")) {
                mMap.put("carnumber", carno);
            }
            String engineno = mPostViolationInfo.getEngineno();
            if (engineno != null && !engineno.equals("")) {
                mMap.put("cardrivenumber", engineno);
            }
            String standcarno = mPostViolationInfo.getStandcarno();
            if (standcarno != null && !standcarno.equals("")) {
                mMap.put("carcode", standcarno);
            }
            // String registno = mPostViolationInfo.getRegistno();
            // if (registno != null && !registno.equals("")) {
            // mMap.put("registno", registno);
            // }
        }
        return CreatStringClean(mMap);
    }

    // 生成保存违章车辆信息参数
    public static String getSaveCar(String cityCode, String cityName,
                                    String carno, String engineno, String standcarno, String registno) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        if (cityCode != null && !cityCode.equals("")) {
            mMap.put("cityCode", cityCode);
        }
        if (cityName != null && !cityName.equals("")) {
            mMap.put("cityName", cityName);
        }
        if (carno != null && !carno.equals("")) {
            mMap.put("carno", carno);
        }
        if (engineno != null && !engineno.equals("")) {
            mMap.put("engineno", engineno);
        }
        if (standcarno != null && !standcarno.equals("")) {
            mMap.put("standcarno", standcarno);
        }
        if (registno != null && !registno.equals("")) {
            mMap.put("registno", registno);
        }
        return CreatString(mMap);
    }

    // 生成获取违章车辆列表参数
    public static String getCarList() {
        HashMap<String, String> mMap = new HashMap<String, String>();
        return CreatString(mMap);
    }

    // 生成获取违章车辆参数
    public static String getCarInfo(String id) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("id", id);
        return CreatString(mMap);
    }

    // 生成删除违章车辆参数
    public static String getDelCar(String id) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("id", id);
        return CreatString(mMap);
    }

    // 生成设置为我的车辆参数
    public static String getSetMyCar(String id) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("id", id);
        return CreatString(mMap);
    }

    // 生成实时车况列表参数
    public static String getCarStatus() {
        HashMap<String, String> mMap = new HashMap<String, String>();
        return CreatString(mMap);
    }

    // 生成导航同步到车参数
    public static String getNavigation(String position, String location,
                                       String move_device_name) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("position", position);
        mMap.put("location", location);
        mMap.put("move_device_name", move_device_name);
        return CreatString(mMap);
    }

    // 生成用户绑定车款配置参数
    public static String getOperationConfig() {
        HashMap<String, String> mMap = new HashMap<String, String>();
        return CreatString(mMap);
    }

    // 生成获取故障码列表参数
    public static String getCheckFault() {
        HashMap<String, String> mMap = new HashMap<String, String>();
        return CreatString(mMap);
    }

    // 生成获取求援电话列表参数
    public static String getHelpPhone() {
        HashMap<String, String> mMap = new HashMap<String, String>();
        return CreatString(mMap);
    }

    // 生成获取胎压数据参数
    public static String getTirepressure(boolean isrefresh) {
        HashMap<String, String> mMap = new HashMap<String, String>();

        mMap.put("isrefresh", isrefresh + "");
        return CreatString(mMap);
    }

    // 生成获取直接式胎压数据参数
    public static String getTiredirect() {
        HashMap<String, String> mMap = new HashMap<String, String>();

        return CreatString(mMap);
    }

    // 生成获取胎压进度数据参数
    public static String getTireprogress() {
        HashMap<String, String> mMap = new HashMap<String, String>();

        return CreatString(mMap);
    }

    // 生成获取胎压数据参数
    public static String getTirepreslearn() {
        HashMap<String, String> mMap = new HashMap<String, String>();

        return CreatString(mMap);
    }

    // 生成获取远程诊断列表参数
    public static String getDiagnoseList(String id) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("carbrandid", SesameLoginInfo.getBrandid());
        mMap.put("id", id);
        return CreatString(mMap);
    }

    // 生成获取挑战列表
    public static String getChallengeList() {
        HashMap<String, String> mMap = new HashMap<String, String>();
        return CreatString(mMap);
    }

    // 生成开始挑战参数
    public static String getStartChallenge(String challengeid) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("challengeid", challengeid + "");
        return CreatString(mMap);
    }

    // 生成终止挑战参数
    public static String getEndChallenge(String id) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("id", id + "");
        return CreatString(mMap);
    }

    // 生成当次挑战成绩参数
    public static String getChallengeScore(String id) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("id", id);
        return CreatString(mMap);
    }

    // 生成我的好友列表参数
    public static String getMyFriendList() {
        HashMap<String, String> mMap = new HashMap<String, String>();
        return CreatString(mMap);
    }

    // 生成好友详情
    public static String getMyFriendDetial(String id) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("userid", id);
        return CreatString(mMap);
    }

    // 生成好友详情
    public static String getSearchFriend(String phoneNum) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("mobile", phoneNum);
        return CreatString(mMap);
    }

    // 添加解除好友
    public static String getUpdataFriend(String userid, String action) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("userid", userid);
        mMap.put("action", action);
        return CreatString(mMap);
    }

    // 生成好友动态列表参数
    public static String getFriendsShareList(int limit, int offset) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("limit", limit + "");
        mMap.put("offset", offset + "");
        return CreatString(mMap);
    }

    // 生成动态详情参数
    public static String getFriendsShareList(String id) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("feedid", id);
        return CreatString(mMap);
    }

    // 生成删除动态参数
    public static String getDeleteFeed(String id) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("feedid", id);
        return CreatString(mMap);
    }

    // 提交评价
    public static String getSubmitEvaluation(String id, int star,
                                             String content, String spent) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("orderid", id);
        mMap.put("star", star + "");
        mMap.put("content", content);
        mMap.put("price", spent);
        return CreatString(mMap);
    }

    // 提交救援请求
    public static String getSubmitSOS(SubmitSOSInfo mSubmitSOSInfo) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        if (mSubmitSOSInfo.isNeed_sos()) {
            mMap.put("need_sos", "1");
            String addr_detail = mSubmitSOSInfo.getAddr_detail();
            if (addr_detail != null && !addr_detail.equals("")) {
                mMap.put("addr_detail", mSubmitSOSInfo.getAddr_detail());
            }
            String addr_point = mSubmitSOSInfo.getAddr_point();
            if (addr_point != null && !addr_point.equals("")) {
                mMap.put("addr_point", mSubmitSOSInfo.getAddr_point());
            }

        } else {
            mMap.put("need_sos", "0");
        }
        mMap.put("info", mSubmitSOSInfo.getInfo());
        ArrayList<UploadImgInfo> mImages = mSubmitSOSInfo.getImages();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mImages.size(); i++) {
            String id = mImages.get(i).getId();
            sb.append(",");
            sb.append(id);
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(0);
            mMap.put("images", sb.toString());
        }

        return CreatString(mMap);
    }

    // 生成我的申报记录
    public static String getMySOS(int limit, int offset) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("limit", limit + "");
        mMap.put("offset", offset + "");
        return CreatString(mMap);
    }

    // 生成预约列表
    public static String getMyOrder(int limit, int offset) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("limit", limit + "");
        mMap.put("offset", offset + "");
        return CreatString(mMap);
    }

    // 生成申报詳情
    public static String getSOSDetial(String id) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("sosid", id);
        return CreatString(mMap);
    }

    // 生成预约詳情
    public static String getAppiontDetial(String id) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("orderid", id);
        return CreatString(mMap);
    }

    // 生成点赞参数
    public static String getFriendPoke(String id) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("id", id);
        return CreatString(mMap);
    }

    // 生成好友pk参数
    public static String getFriendspk(String userid) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("userid", userid);
        return CreatString(mMap);
    }

    // 生成挑战的历史最佳成绩参数
    public static String getChallengeBest(String challengeid) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("challengeid", challengeid + "");
        return CreatString(mMap);
    }

    // 生获取某月预约状态列表参数
    public static String getMonthOrderState(String date) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("date", date);
        return CreatString(mMap);
    }

    // 生获取某天预约状态列表参数
    public static String getDayOrderState(String date) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("date", date);
        return CreatString(mMap);
    }

    // 车秘书分类列表参数
    public static String getSecretaryCategory() {
        HashMap<String, String> mMap = new HashMap<String, String>();
        return CreatString(mMap);
    }

    // 车秘书分类列表参数
    public static String getSecretaryById(String id) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("id", id);

        return CreatString(mMap);
    }

    // 车秘书消息列表参数
    public static String getSecretaryDelete(int class1, String id) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("class1", class1 + "");
        mMap.put("messageid", id);
        return CreatString(mMap);
    }

    // 车秘书消息列表参数
    public static String getSecretaryMessage(int class1) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        if (class1 != SecretaryMessageInfo.C1_ALL) {
            mMap.put("class1", class1 + "");
        }

        return CreatString(mMap);
    }

    // 生获取某天预约状态列表参数
    public static String getSaftyMessage(int class1) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        if (class1 != SecretaryMessageInfo.C1_ALL) {
            mMap.put("class1", class1 + "");
        }
        return CreatString(mMap);
    }

    // 生获取某天预约状态列表参数
    public static String getSecretaryMessage(int class1, int limit, int offset) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        if (class1 != SecretaryMessageInfo.C1_ALL) {
            mMap.put("class1", class1 + "");
        }

        mMap.put("limit", limit + "");
        mMap.put("offset", offset + "");
        return CreatString(mMap);
    }

    // 提交预约
    public static String getSubmitOrder(String date, String time, int type) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("arrivedate", date);
        mMap.put("arrivetime", time);
        mMap.put("type", type + "");
        return CreatString(mMap);
    }

    // 领取奖品
    public static String getReceivePrize(String prizeid) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("prizeid", prizeid);
        return CreatString(mMap);
    }

    // 活动报名
    public static String getActivitySign(String activityid) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("activityid", activityid);
        return CreatString(mMap);
    }

    // 生成我的奖品详情参数
    public static String getPrizeDetail(String id) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("prizeid", id);
        return CreatString(mMap);
    }

    // 获取推荐顾问参数
    public static String getRecommendSales(String id) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        if (id != null && id.length() > 0) {
            mMap.put("salesid", id);
        }
        return CreatString(mMap);
    }

    // 生成日报月报周报参数
    public static String getReport(String reportType, String date) {
        HashMap<String, String> mMap = new HashMap<String, String>();

        mMap.put("reporttype", reportType);
        mMap.put("cartypeid", SesameLoginInfo.getCarid());
        if (date != null && date.length() > 0 && !date.equals("null")) {
            mMap.put("date", date);
        }

        return CreatString(mMap);
    }

    // 生成获取行车报告日期参数
    public static String getReportdate() {
        HashMap<String, String> mMap = new HashMap<String, String>();
        return CreatString(mMap);
    }

    // 生成行车日志参数
    public static String getCarLog(String date) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("date", date);
        return CreatString(mMap);
    }

    // 生成获取行驶轨迹参数
    public static String getCoor(String gpsStartTime, String gpsStopTime,
                                 String runSn) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("gpsStartTime", gpsStartTime);
        mMap.put("gpsStopTime", gpsStopTime);
        mMap.put("runSn", runSn);
        return CreatString(mMap);
    }

    // 生成行车报告日历参数
    public static String getReportCalendar(String date) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("date", date);
        return CreatString(mMap);
    }

    // 生成修改密码参数
    public static String getEditPassword(String oldPassword, String newPassword) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("oldpassword", oldPassword);
        mMap.put("newspassword", newPassword);
        return CreatString(mMap);
    }

    // 生成修改手机-第一步验证旧手机号参数
    public static String getAuthPhone(String oldPhone, String oldValidate) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("oldMobile", oldPhone);
        mMap.put("oldValidate", oldValidate);

        return CreatString(mMap);
    }

    // 生成修改手机-第二步新手机修改参数
    public static String getEditPhone(String oldValidate, String newPhone,
                                      String newValidate) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("oldValidate", oldValidate);
        mMap.put("newMobile", newPhone);
        mMap.put("newValidate", newValidate);

        return CreatString(mMap);
    }

    // 生成发送验证码参数
    public static String getValidate(String type, String phoneNum,
                                     String voiceVerify) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("mobile", phoneNum);
        mMap.put("type", type);
        mMap.put("voiceVerify", voiceVerify);

        return CreatString(mMap);
    }

    // 生成发送验证码参数
    public static String getRetrievePassword(String phoneNum,
                                             String newPassWord, String validate) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("mobile", phoneNum);
        mMap.put("validate", validate);
        mMap.put("newpassword", newPassWord);

        return CreatString(mMap);
    }

    // 生成校验验证码参数
    public static String getValidateCheck(String phoneNum, String type,
                                          String code) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("mobile", phoneNum);
        mMap.put("type", type);
        mMap.put("code", code);

        return CreatString(mMap);
    }

    // 生成发送故障问题
    public static String getSubmitproblem(String id, String salesid, int diagway) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("id", id);
        mMap.put("salesid", salesid);
        mMap.put("diagway", diagway + "");
        return CreatString(mMap);
    }

    // 获取推送设置
    public static String getPushSet() {
        HashMap<String, String> mMap = new HashMap<String, String>();
        return CreatString(mMap);
    }

    // 更新推送设置
    public static String getUpdatePushSet(final String pushSetItemName,
                                          final String pushSetItemValue) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        if (pushSetItemName != null && pushSetItemName.length() > 0) {

            if (pushSetItemName.equals(ManageMessageActivity.KEY_NAMES[1])) {
                mMap.put("weekreport", pushSetItemValue);
                mMap.put("monthreport", pushSetItemValue);
            } else if (pushSetItemName
                    .equals(ManageMessageActivity.KEY_NAMES[7])) {
                mMap.put("gotmedal", pushSetItemValue);
                mMap.put("newrecord", pushSetItemValue);
                mMap.put("license", pushSetItemValue);
            } else {
                mMap.put(pushSetItemName, pushSetItemValue);
            }
        }
        return CreatString(mMap);
    }

    // 更新推送设置
    public static String getFirstUpdatePushSet(PushSetInfo mPushSetInfo) {
        HashMap<String, String> mMap = new HashMap<String, String>();

        mMap.put("startup", mPushSetInfo.getStartup() + "");

        // int security = mPushSetInfo.getSecurity();
        // if (security == 0) {
        //
        // } else {
        // mMap.put("vibstrength", mPushSetInfo.getVibstrength() + "");
        // }
        // mMap.put("security", security + "");
        mMap.put("startup", mPushSetInfo.getStartup() + "");

        // mMap.put("lostconnect", mPushSetInfo.getLostconnect() + "");

        mMap.put("dayreport", mPushSetInfo.getDayreport() + "");

        mMap.put("gotmedal", mPushSetInfo.getGotmedal() + "");
        mMap.put("newrecord", mPushSetInfo.getNewrecord() + "");
        mMap.put("license", mPushSetInfo.getLicense() + "");
        return CreatString(mMap);
    }

    // 更新推送设置
    public static String getBindDevice(String deviceId) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("deviceid", deviceId);
        Log.e("info", "deviceId==" + deviceId.length());
        return CreatString(mMap);
    }

    // 更新车辆信息
    public static String getModifyCarInfo(String summiles, String buydate) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("summiles", summiles);
        mMap.put("buydate", buydate);
        return CreatString(mMap);
    }

    // 更新车辆车型信息
    public static String getModifyCarType(String brandid, String optionid,
                                          String carid) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("brandid", brandid);
        mMap.put("optionid", optionid);
        mMap.put("carid", carid);

        return CreatString(mMap);
    }

    // 更新车辆行驶里程信息
    public static String getModifyCarMileage(String summiles) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("summiles", summiles);

        return CreatString(mMap);
    }

    // 更新车辆购买日期信息
    public static String getModifyCarDate(String buydate) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("buydate", buydate);

        return CreatString(mMap);
    }

    // 更新车辆上次保养里程
    public static String getMaintenMiles(String miles) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("mainten_miles", miles);

        return CreatString(mMap);
    }

    // 更新车辆上次保养日期
    public static String getMaintenDate(String date) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("mainten_date", date);

        return CreatString(mMap);
    }

    // 记录养护日志
    public static String getMaintainLog() {
        HashMap<String, String> mMap = new HashMap<String, String>();
        return CreatString(mMap);
    }

    // 行车报告分享回调
    public static String getReportShare(String sharetitle, String sharetext,
                                        String sharelink, String date, final String reportType) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("reporttype", reportType);
        mMap.put("date", date);

        mMap.put("sharetitle", sharetitle);
        mMap.put("sharetext", sharetext);
        Log.e("info", "==" + URLEncoder.encode(sharelink));
        mMap.put("sharelink", URLEncoder.encode(sharelink));

        return CreatString(mMap);
    }

    // 驾驶证等级分享回调
    public static String getLicenseShare(String licenseId) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("licencelevelid", licenseId);

        return CreatString(mMap);
    }

    // 邀请好友奖品列表
    public static String getInvitePrize() {
        HashMap<String, String> mMap = new HashMap<String, String>();

        return CreatString(mMap);
    }

    // 座驾首页
    public static String getCarMain() {
        HashMap<String, String> mMap = new HashMap<String, String>();
        return CreatString(mMap);
    }

    // 远程首页
    public static String getRemoteMain() {
        HashMap<String, String> mMap = new HashMap<String, String>();
        return CreatString(mMap);
    }

    // 远程校验远程密码
    public static String getRemotePswVerify(String remote_pwd) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("remote_pwd", remote_pwd);
        return CreatString(mMap);
    }

    // 远程声光寻车
    public static String getCarLocating(String move_device_name) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("move_device_name", move_device_name);
        return CreatString(mMap);
    }

    // 远程自动升窗
    public static String getAutoCloseWin(String autoCloseWinSw,
                                         String move_device_name) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("autoCloseWinSw", autoCloseWinSw);
        mMap.put("move_device_name", move_device_name);
        return CreatString(mMap);
    }

    // 远程远程启动
    public static String getRemoteStart(String move_device_name) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("move_device_name", move_device_name);
        return CreatString(mMap);
    }

    // 远程取消远程启动
    public static String getCancelRemoteStart(String move_device_name) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("move_device_name", move_device_name);
        return CreatString(mMap);
    }

    // 远程打开后备箱
    public static String getRemoteTrunk(String move_device_name) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("rtlu", "1");
        mMap.put("move_device_name", move_device_name);
        return CreatString(mMap);
    }

    // 远程启动空调
    public static String getRemoteAir(String racoc,
                                      String move_device_name) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("racoc", racoc);
        //		mMap.put("ratct", oc);
        mMap.put("move_device_name", move_device_name);
        return CreatString(mMap);
    }

    // 开启座椅加热
    public static String getSeatHeatOpen(String move_device_name) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("rhsoc", "1");
        mMap.put("move_device_name", move_device_name);
        return CreatString(mMap);
    }

    // 远程关闭座椅加热
    public static String getSeatHeatClose(String move_device_name) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("rhsoc", "2");
        mMap.put("move_device_name", move_device_name);
        return CreatString(mMap);
    }

    // 远程开启空气净化
    public static String getAirCylinderOpen(String move_device_name) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("rpoc", "1");
        mMap.put("move_device_name", move_device_name);
        return CreatString(mMap);
    }

    // 远程关闭空气净化
    public static String getAirCylinderClose(String move_device_name) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("rpoc", "2");
        mMap.put("move_device_name", move_device_name);
        return CreatString(mMap);
    }

    // 远程开窗
    public static String getRemoteOpenwin(String move_device_name) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("rwoc", "1");
        mMap.put("move_device_name", move_device_name);
        return CreatString(mMap);
    }

    // 远程关窗
    public static String getRemoteClosewin(String move_device_name) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("rwoc", "2");
        mMap.put("move_device_name", move_device_name);
        return CreatString(mMap);
    }

    // 远程落锁、解锁
    public static String getRemoteLock(String lock, String move_device_name) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("lock", lock);
        mMap.put("move_device_name", move_device_name);
        return CreatString(mMap);
    }

    // 远程充电
    public static String getRemoteCharge(String command, String chargeTime,
                                         String move_device_name) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("command", command);
        if (command.equals("2")) {
            mMap.put("chargeTime", chargeTime);
        }
        mMap.put("move_device_name", move_device_name);
        return CreatString(mMap);
    }

    // 取消定时充电
    public static String getRemoteCancelTimeCharge(String move_device_name) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("move_device_name", move_device_name);
        return CreatString(mMap);
    }

    // 远程充电状态
    public static String getRemoteChargeStatus() {
        HashMap<String, String> mMap = new HashMap<String, String>();
        return CreatString(mMap);
    }

    // 远程车辆状态
    public static String getRemoteCarStates() {
        HashMap<String, String> mMap = new HashMap<String, String>();
        return CreatString(mMap);
    }

    // 远程操作记录
    public static String getRemoteLog(String limit, String offset) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("limit", limit);
        mMap.put("offset", offset);
        return CreatString(mMap);
    }

    // 车辆管理信息
    public static String getCarMangerInfo() {
        HashMap<String, String> mMap = new HashMap<String, String>();
        return CreatString(mMap);
    }

    // 发现首页
    public static String getFindingIndex() {
        HashMap<String, String> mMap = new HashMap<String, String>();
        return CreatString(mMap);
    }

    // 更新用户信息
    public static String getUpdateUserInfo(String action, String newValue) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put(action, newValue);
        return CreatString(mMap);
    }

    // 设置车型
    public static String getSetCarInfo(String brandid, String optionid,
                                       String carid) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("brandid", brandid);
        mMap.put("optionid", optionid);
        mMap.put("carid", carid);
        return CreatString(mMap);
    }

    /**
     * 绑定设备
     * @param vin
     * @return
     */
    public static String getCarBindInfo(String vin) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("vin", vin);
        return CreatString(mMap);
    }

    // 养护建议
    public static String getRecommendMaintain() {
        HashMap<String, String> mMap = new HashMap<String, String>();

        return CreatString(mMap);
    }

    // 获取天气信息
    public static String getWeatherInfo(String cityName, String key) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        // try {
        // mMap.put("location", "杭州"/*URLEncoder.encode(cityName, "UTF-8")*/);
        mMap.put("location", cityName);
        // } catch (UnsupportedEncodingException e) {
        // e.printStackTrace();
        // }
        mMap.put("ak", key);
        mMap.put("output", "json");
        mMap.put("mcode", "F7:4C:08:E9:4E:48:37:4E:83:07:C5:6E:14:7B:A2:E1:10:3F:97:1D;com.hz17car.zotye");
        // mMap.put("mcode",
        // "F7:4C:08:E9:4E:48:37:4E:83:07:C5:6E:14:7B:A2:E1:10:3F:97:1D;com.carlt.sesame");
        // mMap.put("mcode",
        // "9C:59:85:88:9D:7B:27:6C:F7:D1:CB:47:1A:7B:F8:E4:26:4E:8B:FF;com.hz17car.zotye");
        return CreatStringClean(mMap);
    }

    // 安全-安全中心首页
    public static String getSafetyIndex(String move_deviceid) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("move_deviceid", move_deviceid);
        return CreatString(mMap);
    }

    // 安全-获取登录日志
    public static String getLoginLog(String limit, String offset) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("limit", limit);
        mMap.put("offset", offset);
        return CreatString(mMap);
    }

    // 安全-获取授权设备列表
    public static String getAuthorizeDevice(String limit, String offset,
                                            String move_deviceid) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("limit", limit);
        mMap.put("offset", offset);
        mMap.put("move_deviceid", move_deviceid);
        return CreatString(mMap);
    }

    // 安全-删除授权设备
    public static String getDelAuthorizeDevice(String id) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("authorize_deviceid", id);
        return CreatString(mMap);
    }

    // 安全-冻结、解冻账户
    public static String getFreezing(String is_freezing, String password) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("is_freezing", is_freezing);
        if (password != null && password.length() > 0) {
            mMap.put("password", password);
        }
        return CreatString(mMap);
    }

    // 安全-发送授权请求
    public static String getSendAuthorize(String mobile, String move_deviceid,
                                          String move_device_name, String move_model) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("mobile", mobile);
        mMap.put("move_deviceid", move_deviceid);
        mMap.put("move_device_name", move_device_name);
        mMap.put("move_model", move_model);
        return CreatString(mMap);
    }

    // 安全-获取授权页面信息
    public static String getAuthorizePageInfo() {
        HashMap<String, String> mMap = new HashMap<String, String>();
        return CreatString(mMap);
    }

    // 安全-授权处理
    public static String getDealAuthorize(String isallow, String move_deviceid) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("isallow", isallow);
        mMap.put("move_deviceid", move_deviceid);
        return CreatString(mMap);
    }

    // 安全-获取授权状态
    public static String getAuthorizeStatus(String mobile, String move_deviceid) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("mobile", mobile);
        mMap.put("move_deviceid", move_deviceid);
        return CreatString(mMap);
    }

    // 安全-更改授权状态
    public static String getUpdateAuthorizeStatus(String authorize_switch) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("authorize_switch", authorize_switch);
        return CreatString(mMap);
    }

    // 安全-实名认证
    public static String getRealauthen(String authen_name, String authen_card) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("authen_name", authen_name);
        mMap.put("authen_card", authen_card);
        return CreatString(mMap);
    }

    // 安全-设置远程密码
    public static String getSetRomotePsw(String remote_pwd) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("remote_pwd", remote_pwd);
        return CreatString(mMap);
    }

    // 安全-修改远程密码
    public static String getResetRomotePsw(String old_remote_pwd,
                                           String new_remote_pwd) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("old_remote_pwd", old_remote_pwd);
        mMap.put("new_remote_pwd", new_remote_pwd);
        return CreatString(mMap);
    }

    // 安全-重置远程密码
    public static String getForgetRomotePsw(String authen_name,
                                            String authen_card, String mobile, String remote_pwd,
                                            String validate_code) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("authen_name", authen_name);
        mMap.put("authen_card", authen_card);
        mMap.put("mobile", mobile);
        mMap.put("remote_pwd", remote_pwd);
        mMap.put("validate_code", validate_code);
        return CreatString(mMap);
    }

    // 安全-更新远程控制五分钟无需密码开关状态
    public static String getUpdateLessPwd(String lesspwd_switch) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("lesspwd_switch", lesspwd_switch);
        return CreatString(mMap);
    }

    // 安全-更换主机
    public static String getChangeMainDevice(String type, String authen_name,
                                             String authen_card, String old_mobile, String new_mobile,
                                             String validate, String move_deviceid, String move_device_name) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("type", type);
        String value;
        value = authen_name;
        if (value != null && !value.equals("")) {
            mMap.put("authen_name", authen_name);
        }
        value = authen_card;
        if (value != null && !value.equals("")) {
            mMap.put("authen_card", authen_card);
        }
        value = old_mobile;
        if (value != null && !value.equals("")) {
            mMap.put("old_mobile", old_mobile);
        }
        value = new_mobile;
        if (value != null && !value.equals("")) {
            mMap.put("new_mobile", new_mobile);
        }
        value = validate;
        if (value != null && !value.equals("")) {
            mMap.put("validate", validate);
        }
        mMap.put("move_deviceid", move_deviceid);
        mMap.put("move_device_name", move_device_name);
        return CreatString(mMap);
    }

    // 安全-无授权登录
    public static String getNoAuthorizeLogin(String mobile, String authen_name,
                                             String authen_card, String remote_pwd, String move_deviceid,
                                             String move_device_name, String move_model) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("mobile", mobile);
        mMap.put("authen_name", authen_name);
        mMap.put("authen_card", authen_card);
        mMap.put("remote_pwd", remote_pwd);
        mMap.put("move_deviceid", move_deviceid);
        mMap.put("move_device_name", move_device_name);
        mMap.put("move_model", move_model);
        return CreatString(mMap);
    }

    // 安全-安全验证登录
    public static String getNoAuthorizeChangeDevice(String old_mobile,
                                                    String old_password, String authen_name, String authen_card,
                                                    String remote_pwd, String new_mobile, String validate,
                                                    String new_password, String move_deviceid, String move_device_name,
                                                    String move_model) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("old_mobile", old_mobile);
        mMap.put("old_password", old_password);
        mMap.put("authen_name", authen_name);
        mMap.put("authen_card", authen_card);
        mMap.put("remote_pwd", remote_pwd);
        mMap.put("new_mobile", new_mobile);
        mMap.put("validate", validate);
        mMap.put("new_password", new_password);
        mMap.put("move_deviceid", move_deviceid);
        mMap.put("move_device_name", move_device_name);
        mMap.put("move_model", move_model);

        return CreatString(mMap);
    }

    // 安全-更换主机-主机认证
    public static String getMainDeviceAuthorize(String old_mobile,
                                                String new_mobile, String validate) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("old_mobile", old_mobile);
        mMap.put("new_mobile", new_mobile);
        mMap.put("validate", validate);
        return CreatString(mMap);
    }

    // 续费-获取续费管理主页数据
    public static String getFeeMain() {
        HashMap<String, String> mMap = new HashMap<String, String>();
        return CreatString(mMap);
    }

    /**
     * 获取TOKEN
     * @param pwdMd5
     * @return
     */
    public static String getToken(String mobile, String pwdMd5) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("password", pwdMd5);
        mMap.put("mobile", mobile);
        return CreatString(mMap);
    }

    /**
     * 将信鸽TOKEN推送至后台
     * @param xgtoken
     *         信鸽token
     * @param move_deviceid
     *         登录设备ID(手机唯一标识码)
     * @return
     */
    public static String getPushXgToken(String xgtoken, String move_deviceid) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("xgtoken", xgtoken);
        if (TextUtils.isEmpty(move_deviceid)) {
            mMap.put("move_deviceid", "");
        } else {
            mMap.put("move_deviceid", move_deviceid);
        }
//        mMap.put("move_deviceid", move_deviceid);
        return CreatString(mMap);
    }

    public static String getQrCode(String realName, String idNumber,
                                   String code, String phone) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        // mMap.put("ownerid", ownerid);
        mMap.put("mobile", phone);
        mMap.put("validate", code);
        mMap.put("authen_name", realName);
        mMap.put("authen_card", idNumber);
        return CreatString(mMap);
    }

    /**
     * @param outtingId
     * @param isAgree
     * @return
     */
    public static String getTransferOldOpt(String outtingId, String isAgree) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("outtingid", outtingId);
        mMap.put("is_agree", isAgree);
        return CreatString(mMap);
    }

    /**
     * @param outtingId
     * @return
     */
    public static String getTransferOldCheck() {
        HashMap<String, String> mMap = new HashMap<String, String>();
        return CreatString(mMap);
    }

    /**
     * @param outtingId
     * @return
     */
    public static String getTransferNewCheck(String outtingId) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("outtingid", outtingId);
        return CreatString(mMap);
    }

    public static String getTransferNewOpt(String outtingId) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("outtingid", outtingId);
        return CreatString(mMap);
    }

    public static String getTransferNewCancle(String outtingId) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("outtingid", outtingId);
        return CreatString(mMap);
    }

    /**
     * 验证密码 MD5加密过后
     * @param pwdMd5
     * @return
     */
    public static String getChekcPwd(String pwdMd5) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("password", pwdMd5);
        return CreatString(mMap);
    }

    // 续费-订单参数
    public static String getFeeOrder(String order_name, String order_money) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("order_year", order_name);
        mMap.put("order_money", order_money);
        mMap.put("softtype", "android");
        mMap.put("version", DorideApplication.Sesame_Version_API + "");
        return CreatString(mMap);
    }

    // 续费-支付回调验证
    public static String getFeeCheck(String resultStatus, String result) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("resultStatus", resultStatus);
        mMap.put("result", result);
        return CreatString(mMap);
    }

    // 续费-续费记录
    public static String getFeeLog(int limit, int offset) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("limit", limit + "");
        mMap.put("offset", offset + "");
        return CreatString(mMap);
    }

    // 远程音效开关
    public static String getSoundSwitchControl(String sound_switch) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("sound_switch", sound_switch);
        return CreatString(mMap);
    }

    // 获取通用设置信息
    public static String getCommSetInfo() {
        HashMap<String, String> mMap = new HashMap<String, String>();
        return CreatString(mMap);
    }

    // 获取广告
    public static String getAdvert(String advert_type) {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("advert_type", advert_type);
        return CreatString(mMap);
    }

    private static String CreatString(HashMap<String, String> mMap) {
        // mMap.put("version", DorideApplication.Sesame_Version_API + "");
        mMap.put("client_id", URLConfig.getClientID());

        if (SesameLoginInfo.getDealerId() != null
                && SesameLoginInfo.getDealerId().length() > 0) {
            mMap.put("dealerId", SesameLoginInfo.getDealerId());
        }
        String token = SesameLoginInfo.getToken();
        if (token != null && !token.equals("")) {
            mMap.put("token", token);
        }
        mMap.put("deviceType", "android");
        StringBuffer sb = new StringBuffer();
        Iterator iter = mMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();
            sb.append("&");
            sb.append(key);
            sb.append("=");
            sb.append(value);
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(0);
        }
        return sb.toString();
    }

    private static String CreatStringClean(HashMap<String, String> mMap) {
        StringBuffer sb = new StringBuffer();
        Iterator iter = mMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();
            sb.append("&");
            sb.append(key);
            sb.append("=");
            sb.append(value);
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(0);
        }
        return sb.toString();
    }

    public static String getUpdateMoveDeviceid(String move_deviceid,
                                               String validate, String mobile) {
        HashMap<String, String> mMap = new HashMap<String, String>();
//        mMap.put("move_deviceid", move_deviceid);
        if (TextUtils.isEmpty(move_deviceid)) {
            mMap.put("move_deviceid", "");
        } else {
            mMap.put("move_deviceid", move_deviceid);
        }
        mMap.put("validate", validate);
        mMap.put("mobile", mobile);

        mMap.put("move_device_name", DorideApplication.MODEL_NAME);
        mMap.put("move_model", DorideApplication.MODEL);
        mMap.put("softtype", "android");
        StringBuffer sysinfo = new StringBuffer(DorideApplication.ANDROID_VERSION);
        sysinfo.append(",");
        sysinfo.append(DorideApplication.DISPLAY);
        sysinfo.append(",");
        sysinfo.append(DorideApplication.MODEL_NAME);
        mMap.put("sysinfo", sysinfo.toString());
        return CreatString(mMap);
    }

    /**
     * 获取经销商信息
     */
    public static String getDealerInfo() {
        HashMap<String, String> mMap = new HashMap<String, String>();
        return CreatString(mMap);
    }

    public static String getVersionLogInfo() {
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("softtype", "1");
        return CreatString(mMap);
    }
}
