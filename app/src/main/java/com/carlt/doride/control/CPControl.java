package com.carlt.doride.control;


import android.text.TextUtils;
import android.util.Log;

import com.carlt.doride.DorideApplication;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.data.login.UserRegisterParams;
import com.carlt.doride.data.remote.AirMainInfo;
import com.carlt.doride.data.remote.RemoteFunInfo;
import com.carlt.doride.data.set.ModifyCarInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.protocolparser.DefaultParser;
import com.carlt.doride.protocolparser.DefaultStringParser;
import com.carlt.doride.protocolparser.DeviceUpdateInfoParser;
import com.carlt.doride.protocolparser.home.CareerlParser;
import com.carlt.doride.protocolparser.home.InformationCentreInfoListParser;
import com.carlt.doride.protocolparser.home.InformationMessageListParser;
import com.carlt.doride.protocolparser.home.MilesInfoParser;
import com.carlt.doride.protocolparser.home.RemindDefaultParser;
import com.carlt.doride.protocolparser.home.ReportCalendarDayParser;
import com.carlt.doride.protocolparser.home.ReportCalendarMonthParser;
import com.carlt.doride.protocolparser.home.ReportDateParser;
import com.carlt.doride.protocolparser.home.ReportDayLogParser;
import com.carlt.doride.protocolparser.home.ReportDayParser;
import com.carlt.doride.protocolparser.home.ReportGpsParser;
import com.carlt.doride.protocolparser.home.ReportMonthParser;
import com.carlt.doride.protocolparser.home.ReportMonthStatisticParser;
import com.carlt.doride.protocolparser.login.UserRegisterParser;
import com.carlt.doride.protocolparser.remote.CarStateInfoParser;
import com.carlt.doride.systemconfig.URLConfig;
import com.carlt.doride.utils.CipherUtils;
import com.carlt.doride.utils.CreateHashMap;
import com.carlt.doride.utils.FileUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;

public class CPControl {
    public static void GetRemoteAir(BaseParser.ResultCallback mListener, String racoc, String oc) {
        DefaultStringParser remoteStartParser = new DefaultStringParser(mListener);
        HashMap param = new HashMap();
        param.put("racoc", racoc);
        param.put("ratct", oc);
        param.put("move_device_name", DorideApplication.MODEL_NAME);
        remoteStartParser.executePost(URLConfig.getM_DEVICE_REMOTE_AIRCONDITION(), param);
    }

    public static void GetRemoteStart(BaseParser.ResultCallback mListener) {
        DefaultStringParser remoteStartParser = new DefaultStringParser(mListener);
        HashMap param = new HashMap();
        param.put("move_device_name", DorideApplication.MODEL_NAME);
        remoteStartParser.executePost(URLConfig.getM_DEVICE_REMOTE_START(), param);
    }

    public static void GetCancelRemoteStart(BaseParser.ResultCallback mListener) {
        DefaultStringParser remoteStartParser = new DefaultStringParser(mListener);
        HashMap param = new HashMap();
        param.put("move_device_name", DorideApplication.MODEL_NAME);
        remoteStartParser.executePost(URLConfig.getM_DEVICE_REMOTE_STALL(), param);
    }

    public static void GetCarLocating(BaseParser.ResultCallback mListener) {
        DefaultStringParser remoteStartParser = new DefaultStringParser(mListener);
        HashMap param = new HashMap();
        param.put("move_device_name", DorideApplication.MODEL_NAME);
        remoteStartParser.executePost(URLConfig.getM_DEVICE_REMOTE_CARLOCATING(), param);
    }

    //远程开启后背箱
    public static void GetRemoteTrunk(BaseParser.ResultCallback mListener) {
        DefaultStringParser remoteStartParser = new DefaultStringParser(mListener);
        HashMap param = new HashMap();
        param.put("move_device_name", DorideApplication.MODEL_NAME);
        param.put("rtlu", "1");    //1:开启，2：关闭
        remoteStartParser.executePost(URLConfig.getM_DEVICE_REMOTE_TRUNK(), param);
    }

    // 1:解锁，2上锁
    public static void GetRemoteLock(String s, BaseParser.ResultCallback mListener) {
        DefaultStringParser remoteStartParser = new DefaultStringParser(mListener);
        HashMap param = new HashMap();
        param.put("move_device_name", DorideApplication.MODEL_NAME);
        param.put("lock", s);
        remoteStartParser.executePost(URLConfig.getM_DEVICE_REMOTE_LOCK(), param);
    }

    //关窗
    public static void GetRemoteClosewin(BaseParser.ResultCallback mListener) {
        DefaultStringParser remoteStartParser = new DefaultStringParser(mListener);
        HashMap param = new HashMap();
        param.put("move_device_name", DorideApplication.MODEL_NAME);
        param.put("rwoc", "2");
        remoteStartParser.executePost(URLConfig.getM_DEVICE_REMOTE_WINDOW(), param);
    }

    //开窗
    public static void GetRemoteOpenwin(BaseParser.ResultCallback mListener) {
        DefaultStringParser remoteStartParser = new DefaultStringParser(mListener);
        HashMap param = new HashMap();
        param.put("move_device_name", DorideApplication.MODEL_NAME);
        param.put("rwoc", "1");
        remoteStartParser.executePost(URLConfig.getM_DEVICE_REMOTE_WINDOW(), param);
    }

    public static void GetRemoteSkylight(String s, BaseParser.ResultCallback mListener) {
        DefaultStringParser remoteStartParser = new DefaultStringParser(mListener);
        HashMap param = new HashMap();
        param.put("move_device_name", DorideApplication.MODEL_NAME);
        param.put("rwoc", s);
        remoteStartParser.executePost(URLConfig.getM_DEVICE_REMOTE_SKYLIGHT(), param);
    }

    /**
     * 远程-车辆实时温度 成功返回 AirMainInfo
     */
    public static void GetRemoteCarTemp(final BaseParser.ResultCallback mListener_temp, final AirMainInfo airMainInfo) {
        DefaultStringParser paser = new DefaultStringParser(new BaseParser.ResultCallback() {
            @Override
            public void onSuccess(BaseResponseInfo mBaseResponseInfo) {
                String value = (String) mBaseResponseInfo.getValue();
                JsonParser jsonParser = new JsonParser();
                JsonObject mParser = jsonParser.parse(value).getAsJsonObject();
                String temp = "";
                String airState = "";
                if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
                    temp = mParser.get("ACTemp").getAsString();
                    boolean isGetCurrentTempSuccess;
                    if (TextUtils.isEmpty(temp)) {
                        temp = "26";
                        isGetCurrentTempSuccess = false;
                    } else {
                        if (temp.equals("0")) {
                            temp = "26";
                            isGetCurrentTempSuccess = false;
                        } else {
                            isGetCurrentTempSuccess = true;
                        }
                    }
                    // 测试数据
                    // temp = "35";
                    // 测试数据结束
                    airMainInfo.setCurrentTemp(temp);
                    Log.e("info", "temp==------------" + temp);
                    airMainInfo.setGetCurrentTempSuccess(isGetCurrentTempSuccess);

                    airState = mParser.get("AC").getAsString();
                    ArrayList<RemoteFunInfo> remoteFunInfos = airMainInfo
                            .getmRemoteFunInfos();
                    for (int i = 0; i < remoteFunInfos.size(); i++) {
                        RemoteFunInfo item = remoteFunInfos.get(i);
                        String id = item.getId();
                        if (id.equals(airState)) {
                            item.setSelect(true);
                            break;
                        }
                    }
                    airMainInfo.setState(airState);
                    mListener_temp.onSuccess(airMainInfo);
                } else {
                    airMainInfo.setCurrentTemp("26");
                    airMainInfo.setGetCurrentTempSuccess(false);
                    airMainInfo.setState("-1");
                    mListener_temp.onError(mBaseResponseInfo);
                }
            }

            @Override
            public void onError(BaseResponseInfo bInfo) {
                mListener_temp.onError(bInfo);
            }
        });
        HashMap mapParam = new HashMap();
        paser.executePost(URLConfig.getM_REMOTE_STATE(), mapParam);
    }

    //
    public static void GetCarExtInfo(BaseParser.ResultCallback mCallback) {
        DefaultStringParser paser = new DefaultStringParser(mCallback);
        HashMap mapParam = new HashMap();
        paser.executePost(URLConfig.getM_CAR_GETCAREXTINFO_URL(), mapParam);
    }

    public static void GetRemoteCarState(final BaseParser.ResultCallback mListener_states) {
        CarStateInfoParser paser = new CarStateInfoParser(mListener_states);
        HashMap mapParam = new HashMap();
        paser.executePost(URLConfig.getM_REMOTE_STATE(), mapParam);
    }

    public static void GetRemotePswVerify(String password, BaseParser.ResultCallback mListener_verify) {
        DefaultStringParser paser = new DefaultStringParser(mListener_verify);
        HashMap mapParam = new HashMap();
        mapParam.put("remote_pwd", FileUtil.stringToMD5(password));
        paser.executePost(URLConfig.getM_REMOTEPWDVERIFY(), mapParam);
    }

    public static void GetSetRemotePwdResult(String pswNew1, BaseParser.ResultCallback listener_set) {
        DefaultStringParser paser = new DefaultStringParser(listener_set);
        HashMap mapParam = new HashMap();
        mapParam.put("remote_pwd", FileUtil.stringToMD5(pswNew1));
        paser.executePost(URLConfig.getM_SAFE_SETREMOTEPWD_URL(), mapParam);
    }

    public static void GetForgetRemotePwdResult(String name, String idcard, String mobile, String pswNew1, String validate, BaseParser.ResultCallback listener_forget) {
        DefaultStringParser parser = new DefaultStringParser(listener_forget);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("authen_card", idcard);
        params.put("authen_name", name);
        params.put("mobile", mobile);
        params.put("validate", validate);
        params.put("remote_pwd", CipherUtils.md5(pswNew1));
        parser.executePost(URLConfig.getM_FORGET_REMOTE_PWD(), params);
    }

    /**
     * 注册接口 onFinished表示成功
     */
    public static void GetRegisteResult(final UserRegisterParams mRegisteParams,
                                        final BaseParser.ResultCallback listener) {

        if (listener == null)
            return;

        // 链接地址
        // String url = URLConfig.getM_REGISTER_URL();
        String url = URLConfig.getM_REGISTER_NEW_URL();
        // Post参数
        UserRegisterParser mUserInfoParser = new UserRegisterParser(listener);
        HashMap<String, String> mMap = new HashMap<String, String>();

        mMap.put("mobile", mRegisteParams.getMobile());
        mMap.put("password", mRegisteParams.getPassword());
        mMap.put("validate", mRegisteParams.getValidate());
        mMap.put("move_deviceid", DorideApplication.NIMEI);
        mMap.put("move_device_name", DorideApplication.MODEL_NAME);
        mMap.put("originate", mRegisteParams.getOriginate());
        mUserInfoParser
                .executePost(url, mMap);
    }

    public static void GetMessageValidateResult(final String type,
                                                final String phoneNum, final BaseParser.ResultCallback listener) {
        GetValidateResult(type, phoneNum, "0", listener);
    }

    private static void GetValidateResult(final String type,
                                          final String phoneNum, final String voiceVerify,
                                          final BaseParser.ResultCallback listener) {

        if (listener == null)
            return;
        // 链接地址
        // String url = URLConfig.getM_VALIDATE_URL();
        String url = URLConfig.getM_VALIDATE_NEW_URL();
        // Post参数
        HashMap<String, String> params = new HashMap<>();
        params.put("mobile", phoneNum);
        params.put("type", type);
        params.put("voiceVerify", voiceVerify);
        DefaultStringParser mParser = new DefaultStringParser(listener);
        mParser.executePost(url, params);

    }

    /**
     * 修改车辆车型信息 调用成功执行onFinished() 无返回值
     */

    public static void GetUpdateCarTypeResult(
            final ModifyCarInfo modifyCarInfo,
            final BaseParser.ResultCallback listener) {

        if (listener == null)
            return;

        String brandid = modifyCarInfo.getBrandid();
        String optionid = modifyCarInfo.getOptionid();
        String carid = modifyCarInfo.getCarid();
        // 链接地址
        String url = URLConfig.getM_SWITCHCAR_URL();
        // Post参数
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("brandid", brandid);
        mMap.put("optionid", optionid);
        mMap.put("carid", carid);

        DefaultParser mParser = new DefaultParser(listener, ModifyCarInfo.class);
        mParser.executePost(url, mMap);

    }

    /**
     * 获取设备升级状态 onFinished表示成功
     */
    public static void GetDeviceUpdateResult(final String deviceid,
                                             final BaseParser.ResultCallback listener) {

        if (listener == null)
            return;

        // 链接地址
        String url = URLConfig.getM_DEVICEUPDATE_URL();
        // Post参数
        DeviceUpdateInfoParser mParser = new DeviceUpdateInfoParser(listener);
        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("deviceid", deviceid);
        mParser.executePost(url, mMap);
    }

    /**
     * 获取信息中心列表
     * @param callback
     */
    public static void GetInformationCentreInfoListResult(BaseParser.ResultCallback callback) {
        HashMap mHashMap = CreateHashMap.getNullData();
        InformationCentreInfoListParser parser = new InformationCentreInfoListParser(callback);
        parser.setTest(false);
        parser.executePost(URLConfig.getM_SECRETARY_CATEGORY_URL(), mHashMap);
    }


    /**
     * 行车报告日期
     * @param callback
     */
    public static void GetReportdateResult(BaseParser.ResultCallback callback) {
        HashMap mHashMap = CreateHashMap.getNullData();
        ReportDateParser parser = new ReportDateParser(callback);
        parser.setTest(false);
        parser.executePost(URLConfig.getM_REPORTDATE_URL(), mHashMap);
    }

    /**
     * 指定用户行车日报
     * @param callback
     * @param date
     */
    public static void GetDayReportResult(BaseParser.ResultCallback callback, String date) {
        HashMap mHashMap = CreateHashMap.getDayReportMap(date);
        ReportDayParser parser = new ReportDayParser(callback);
        parser.setTest(false);
        parser.executePost(URLConfig.getM_REPORTDAY_URL(), mHashMap);
    }

    /**
     * 某一天的行车日志
     * @param callback
     * @param date
     */
    public static void GetDayReportLogResult(BaseParser.ResultCallback callback, String date) {
        HashMap mHashMap = CreateHashMap.getDayReportMap(date);
        ReportDayLogParser parser = new ReportDayLogParser(callback);
        parser.setTest(false);
        parser.executePost(URLConfig.getM_REPORTDAYLOG_URL(), mHashMap);
    }

    /**
     * 指定用户行车月报
     * @param callback
     * @param date
     */
    public static void GetMonthReportResult(BaseParser.ResultCallback callback, String date) {
        HashMap mHashMap = CreateHashMap.getDayReportMap(date);
        ReportMonthParser parser = new ReportMonthParser(callback);
        parser.setTest(false);
        parser.executePost(URLConfig.getM_MONTHREPORT_URL(), mHashMap);
    }

    /**
     * 获取某年月报统计数据
     * @param callback
     * @param date
     */
    public static void GetMonthReportLogResult(BaseParser.ResultCallback callback, String date) {
        HashMap mHashMap = CreateHashMap.getDayReportMap(date);
        ReportMonthStatisticParser parser = new ReportMonthStatisticParser(callback);
        parser.setTest(false);
        parser.executePost(URLConfig.getM_MONTHREPORTSTATISTIC_URL(), mHashMap);
    }

    /**
     * 车秘书提醒
     * @param callback
     */
    public static void GetInformationMessageResult(BaseParser.ResultCallback callback, int class1) {
        HashMap mHashMap = CreateHashMap.getMessageMap(class1);
        InformationMessageListParser parser = new InformationMessageListParser(callback);
        parser.setTest(false);
        parser.executePost(URLConfig.getM_SAFETY_MESSAGE_URL(), mHashMap);
    }

    /**
     * 获取一年中每月平均得分
     * @param callback
     * @param date
     */
    public static void GetUserMonthPointResult(BaseParser.ResultCallback callback, String date) {
        HashMap mHashMap = CreateHashMap.getDayReportMap(date);
        ReportCalendarMonthParser parser = new ReportCalendarMonthParser(callback);
        parser.setTest(false);
        parser.executePost(URLConfig.getM_USER_MONTH_POINT_URL(), mHashMap);
    }

    /**
     * 用户一月中日平均得分
     * @param callback
     * @param date
     */
    public static void GetUserDayPointResult(BaseParser.ResultCallback callback, String date) {
        HashMap mHashMap = CreateHashMap.getDayReportMap(date);
        ReportCalendarDayParser parser = new ReportCalendarDayParser(callback);
        parser.setTest(false);
        parser.executePost(URLConfig.getM_USER_DAY_POINT_URL(), mHashMap);
    }

    /**
     * 获取生涯首页数据
     * @param callback
     */
    public static void GetCareerResult(BaseParser.ResultCallback callback) {
        HashMap mHashMap = CreateHashMap.getNullData();
        CareerlParser parser = new CareerlParser(callback);
        parser.setTest(false);
        parser.executePost(URLConfig.getM_CAREER_URL(), mHashMap);
    }

    /**
     * 读取里程
     * @param callback
     */
    public static void GetMilesInfoResult(BaseParser.ResultCallback callback) {
        HashMap mHashMap = CreateHashMap.getNullData();
        MilesInfoParser parser = new MilesInfoParser(callback);
        parser.setTest(false);
        parser.executePost(URLConfig.getM_MILESINFO_URL(), mHashMap);
    }

    /**
     * GPS行车轨迹
     * @param callback
     * @param gpsStartTime
     * @param gpsStopTime
     * @param runSn
     */
    public static void GetReportGpsResult(BaseParser.ResultCallback callback,
                                          String gpsStartTime, String gpsStopTime, String runSn) {
        HashMap mHashMap = CreateHashMap.getReportGpsMap(gpsStartTime, gpsStopTime, runSn);
        ReportGpsParser parser = new ReportGpsParser(callback);
        parser.setTest(false);
        parser.executePost(URLConfig.getM_GETCOOR_URL(), mHashMap);
    }

    /**
     * 提醒项 删除
     * @param callback
     * @param class1
     * @param messageid
     */
    public static void GetRemindDeleteResult(BaseParser.ResultCallback callback, int class1, int messageid) {
        HashMap mHashMap = CreateHashMap.getRemindDefaultMap(class1, messageid);
        RemindDefaultParser parser = new RemindDefaultParser(callback);
        parser.setTest(false);
        parser.executePost(URLConfig.getM_SECRETARY_DELETE_URL(), mHashMap);
    }

    public static void GetNavigationResult(String position, String location, BaseParser.ResultCallback listener_navigation) {

        DefaultStringParser paser = new DefaultStringParser(listener_navigation);
        HashMap mapParam = new HashMap();
        mapParam.put("position", position);
        mapParam.put("location", location);
        mapParam.put("move_device_name", DorideApplication.MODEL_NAME);
        paser.executePost(URLConfig.getM_NAVIGATION_URL(), mapParam);
    }

    public static void GetPushXgTokenResult(String xgtoken, String move_deviceid, BaseParser.ResultCallback listener) {
        DefaultStringParser paser = new DefaultStringParser(listener);
        HashMap mapParam = new HashMap();
        mapParam.put("move_deviceid", move_deviceid);
        mapParam.put("xgtoken", xgtoken);
        paser.executePost(URLConfig.getM_REGISTERXGPUSH_URL(), mapParam);
    }


    public static void GetUnRigisterXgTokenResult(String move_deviceid, BaseParser.ResultCallback listener) {
        DefaultStringParser paser = new DefaultStringParser(listener);
        HashMap mapParam = new HashMap();
        mapParam.put("move_deviceid", move_deviceid);
        paser.executePost(URLConfig.getM_REMOVERXGPUSH_URL(), mapParam);
    }

    public static void getMilesInfos(BaseParser.ResultCallback listener) {
        DefaultStringParser paser = new DefaultStringParser(listener);
        HashMap mapParam = new HashMap();
        Logger.e("-----" + URLConfig.getM_REMOTE_CAR_MILES_INFO_URL());
        paser.executePost(URLConfig.getM_REMOTE_CAR_MILES_INFO_URL(), mapParam);
    }

    public static void GetLogin(String account, String password, BaseParser.ResultCallback listener_login) {

        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("version", DorideApplication.Version + "");
        mMap.put("mobile", account);
        mMap.put("password", CipherUtils.md5(password));
        mMap.put("move_deviceid", DorideApplication.NIMEI);
        mMap.put("move_device_name", DorideApplication.MODEL_NAME);
        mMap.put("move_model", DorideApplication.MODEL);
        mMap.put("softtype", "android");
        StringBuffer sysinfo = new StringBuffer(DorideApplication.ANDROID_VERSION);
        sysinfo.append(",");
        sysinfo.append(DorideApplication.DISPLAY);
        sysinfo.append(",");
        sysinfo.append(DorideApplication.MODEL_NAME);
        mMap.put("sysinfo", sysinfo.toString());
        String url = URLConfig.getM_LOGIN_URL();
//        Logger.e("url---" + url);
        DefaultStringParser parser = new DefaultStringParser(listener_login);
        parser.executePost(url, mMap);

    }
}
