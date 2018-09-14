package com.carlt.doride.control;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.doride.DorideApplication;
import com.carlt.doride.MainActivity;
import com.carlt.doride.R;
import com.carlt.doride.model.LoginInfo;
import com.carlt.doride.ui.activity.login.DeviceBindActivity;
import com.carlt.doride.ui.view.UUUpdateDialog;
import com.carlt.sesame.data.SesameLoginInfo;
import com.carlt.sesame.preference.TokenInfo;
import com.carlt.sesame.ui.SesameMainActivity;
import com.carlt.sesame.ui.activity.safety.FreezeActivity;

import org.json.JSONObject;


/**
 * 登录控制
 * @author daisy
 */
public class LoginControl {
    public static Activity                            mCtx;
    public static UUUpdateDialog.DialogUpdateListener mDialogUpdateListener;

    public static void logic(final Activity mContext) {
        mCtx = mContext;
        DorideApplication.getInstanse().setIsshowupdata(false);
        String className = mContext.getClass().getName();
        // 判断是否绑定设备
        String s = LoginInfo.getDeviceidstring();
        String s2 = SesameLoginInfo.getDeviceidstring();
        Log.e("info", "deviceidstring==" + s);
        Log.e("info", "deviceidstring==" + s2);

        if (!TextUtils.isEmpty(s) || !TextUtils.isEmpty(s2)) {
            // 已绑定设备,判断是否激活设备
            boolean isDeviceActivate = LoginInfo.isDeviceActivate();
            boolean deviceActivate = SesameLoginInfo.isDeviceActivate();
            Log.e("info", "isDeviceActivate==" + isDeviceActivate);
            Log.e("info", "deviceActivate==" + deviceActivate);
            if (isDeviceActivate || deviceActivate) {
                Intent mainIntent;
                if (LoginInfo.getApp_type() == 1) {
                    // 大乘绑定和激活合并，如果激活直接进入主页
                    mainIntent = new Intent(mContext, MainActivity.class);
                } else {
                    if (SesameLoginInfo.isFreezing()) {
                        // 处在冻结状态
                        mainIntent = new Intent(mContext, FreezeActivity.class);
                        mainIntent.putExtra(FreezeActivity.FROM_NAME, mContext.getClass().getName());
//                        mContext.startActivity(mIntent4);
                    } else {
                        mainIntent = new Intent(mContext, SesameMainActivity.class);
                    }

                }
                mContext.startActivity(mainIntent);
                mContext.finish();
            } else {
                // 未激活设备
                String vin = LoginInfo.getVin(LoginInfo.getMobile());
                String vin2 = LoginInfo.getVin(SesameLoginInfo.getMobile());
                if (vin == null || vin.equals("")) {
                    Intent loginIntent = new Intent(mContext, DeviceBindActivity.class);

                    mContext.startActivity(loginIntent);
                } else {
                    boolean isUpdating = LoginInfo.isUpgradeing();
                    // 是否需要升级
                    if (isUpdating) {
                        // 设备正在升级，跳转至升级页面
//                        PopBoxCreat.showUUUpdateDialog(mContext,
//                                new UUUpdateDialog.DialogUpdateListener() {
//
//                                    @Override
//                                    public void onSuccess() {
//                                        LoginInfo.setUpgradeing(false);
//                                        LoginControl.logic(mCtx);
//                                        if (mDialogUpdateListener != null) {
//                                            mDialogUpdateListener.onSuccess();
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onFailed() {
//                                        if (mDialogUpdateListener != null) {
//                                            mDialogUpdateListener.onFailed();
//                                        }
//                                    }
//                                });
                    } else {
                        // 设备不需要升级，跳转绑定 回填Vin码
                        Intent loginIntent = new Intent(mContext, DeviceBindActivity.class);
                        //                        loginIntent.putExtra("vin", LoginInfo.getVin(LoginInfo.getMobile()));
                        //                        loginIntent.putExtra("carType", LoginInfo.getCarname());
                        mContext.startActivity(loginIntent);
                    }
                }

            }
        } else {
            Intent loginIntent = new Intent(mContext, DeviceBindActivity.class);
            //            loginIntent.putExtra("vin", LoginInfo.getVin(LoginInfo.getMobile()));
            //            loginIntent.putExtra("carType", LoginInfo.getCarname());
            mContext.startActivity(loginIntent);
        }
    }

    public static void parseLoginInfo(JSONObject data) {
        if (data != null) {
            int app_type = data.optInt("app_type", 1);
            LoginInfo.setApp_type(app_type);
            if (app_type == 2) {
                // 大乘
                parseSesameLoginInfo(data);
            } else {
                // 芝麻
                parseDorideLoginInfo(data);
            }


        }
    }

    private static void parseSesameLoginInfo(JSONObject mJSON_data) {
        JSONObject member = mJSON_data.optJSONObject("member");
        SesameLoginInfo.setUseId((member.optString("id", "")));
        SesameLoginInfo.setDealerId((member.optString("dealerid", "")));
        SesameLoginInfo.setRealname((member.optString("realname", "")));
        SesameLoginInfo.setUsername(member.optString("username", ""));
        SesameLoginInfo.setGender((member.optString("gender", "")));
        String mobile = (member.optString("mobile", ""));
        SesameLoginInfo.setMobile(mobile);
        SesameLoginInfo.setStatus((member.optString("status", "")));
        SesameLoginInfo.setWeixinbind(member.optString("weixinbind", "0"));
        SesameLoginInfo.setClwbind(member.optString("clwbind", "0"));
        SesameLoginInfo.setRegip(member.optString("regip", ""));
        SesameLoginInfo.setAvatar_id(member.optString("avatar_id", "-1"));
        SesameLoginInfo.setOriginate(member.optString("originate", "0"));
        SesameLoginInfo.setLastlogin(member.optString("lastlogin", "0"));
        SesameLoginInfo.setLoginoauth(member.optString("loginoauth", ""));
        SesameLoginInfo.setLogintimes(member.optString("logintimes", "0"));
        SesameLoginInfo.setCreatedate(member.optString("createdate", ""));
        SesameLoginInfo.setAvatar_img((member.optString("avatar_img", "")));
        SesameLoginInfo.setLifetime((member.optString("lifetime", "")));

        SesameLoginInfo.setMain(getFlagResult(member.optString("is_main", "")));
        SesameLoginInfo.setMainDevicename(member.optString("move_device_name", ""));
        SesameLoginInfo.setMainDeviceid(member.optString("move_deviceid", ""));
        SesameLoginInfo.setAuthorize_status(member.optString("authorize_switch", ""));
        SesameLoginInfo.setHasAuthorize(getFlagResult(member.optString("has_authorize", "")));
        SesameLoginInfo.setNeedAuthorize(getFlagResult(member.optString("need_authorize", "")));
        SesameLoginInfo.setFreezing(getFlagResult(member.optString("is_freezing", "")));
        SesameLoginInfo.setAuthen(getFlagResult(member.optString("is_authen", "")));

        SesameLoginInfo.setNoneedpsw(getFlagResult(member.optString("lesspwd_switch", "")));
        SesameLoginInfo.setAuthen_name(member.optString("authen_name", ""));
        SesameLoginInfo.setAuthen_card(member.optString("authen_card", ""));

        SesameLoginInfo.setAccess_token((member.optString("access_token", "")));
        LoginInfo.setAccess_token(member.optString("access_token", ""));
        SesameLoginInfo.setToken((member.optString("access_token", "")));
        TokenInfo.setToken(member.optString("access_token", ""));
        SesameLoginInfo.setExpires_in((member.optString("expires_in", "")));
        SesameLoginInfo.setExpiresIn((member.optString("expires_in", "")));
        SesameLoginInfo.setSSID(member.optString("SSID", ""));
        SesameLoginInfo.setSSIDPWD(member.optString("SSIDPWD", ""));

        String isSetRemotePwd = member.optString("is_set_remotePwd", "");
        SesameLoginInfo.setSetRemotePwd(getFlagResult(isSetRemotePwd));

        JSONObject membercar = mJSON_data.optJSONObject("membercar");

        int membercarId = membercar.optInt("id");
        if (membercarId > 0) {
            SesameLoginInfo.setBindCar(true);
        } else {
            SesameLoginInfo.setBindCar(false);
        }
        SesameLoginInfo.setDeviceidstring((membercar.optString("deviceidstring", "")));
        int isDeviceActivate = membercar.optInt("isDeviceActivate");
        if (isDeviceActivate == 1) {
            SesameLoginInfo.setDeviceActivate(true);
        } else {
            SesameLoginInfo.setDeviceActivate(false);
        }
        String isUpgradeing = membercar.optString("upgradeing");
        if (isUpgradeing.equals("1")) {
            SesameLoginInfo.setUpgradeing(true);
        } else {
            SesameLoginInfo.setUpgradeing(false);
        }
        String isGpsDevice = membercar.optString("gps_device");
        if (isGpsDevice.equals("1")) {
            SesameLoginInfo.setGpsDevice(true);
        } else {
            SesameLoginInfo.setGpsDevice(false);
        }

        SesameLoginInfo.setBrandid((membercar.optString("brandid", "")));
        SesameLoginInfo.setDevicetype((membercar.optString("devicetype", "")));
        SesameLoginInfo.setOptionid((membercar.optString("optionid", "")));
        SesameLoginInfo.setCarid((membercar.optString("carid", "")));
        SesameLoginInfo.setRemoteControl(membercar.optString("remoteControl", ""));
        String car_pin = membercar.optString("car_pin", "");
        if (car_pin != null && !car_pin.equals("")) {
            SesameLoginInfo.setVpin(mobile, membercar.optString("car_pin", ""));
        }

        // LoginInfo.setIsJumptoBind(membercar.optString("isJumpBind", ""));

        String installorder = membercar.optString("installorder", "");
        SesameLoginInfo.setInstallorder(getFlagResult(installorder));
        String before_device = membercar.optString("before_device", "0");
        SesameLoginInfo.setDeviceType(before_device);

        SesameLoginInfo.setDealerId(membercar.optString("dealerid", ""));
        SesameLoginInfo.setDeviceid(membercar.optString("deviceid", ""));
        SesameLoginInfo.setModelid(membercar.optString("modelid", ""));
        SesameLoginInfo.setLicencelevelid(membercar.optString("licencelevelid", ""));
        SesameLoginInfo.setMainten_next_miles(membercar.optString("mainten_next_miles", ""));
        SesameLoginInfo.setMainten_next_day(membercar.optString("mainten_next_date", ""));
        SesameLoginInfo.setIs_net_sale(membercar.optString("is_net_sale", ""));
        SesameLoginInfo.setFixed_miles(membercar.optString("fixed_miles", ""));
        SesameLoginInfo.setPowerontime(membercar.optString("powerontime", ""));

        SesameLoginInfo.setCarprovice((membercar.optString("carprovice", "")));
        SesameLoginInfo.setCarno((membercar.optString("carno", "")));
        SesameLoginInfo.setStandcarno((membercar.optString("standcarno", "")));
        SesameLoginInfo.setCarcity((membercar.optString("city", "")));
        SesameLoginInfo.setEngineno((membercar.optString("engineno", "")));
        SesameLoginInfo.setRegistno((membercar.optString("registno", "")));
        SesameLoginInfo.setCanQueryVio((membercar.optString("canQueryVio", "")));
        SesameLoginInfo.setCity_code(membercar.optString("cityCode", ""));

        SesameLoginInfo.setCarname((membercar.optString("carname", "")));
        SesameLoginInfo.setCarlogo((membercar.optString("carlogo", "")));

        SesameLoginInfo.setBuydate((membercar.optString("buydate", "")));
        SesameLoginInfo.setSLCarLocating(membercar.optInt("SLCarLocating"));
        SesameLoginInfo.setAutoCloseWinSw(membercar.optInt("autoCloseWinSw"));
        SesameLoginInfo.setRemoteStart(membercar.optInt("remoteStart"));
        SesameLoginInfo.setShortstandcarno(membercar.optString("shortstandcarno"));
        SesameLoginInfo.setNeed_pin(membercar.optString("need_pin"));
        String isSupSpecFunc = membercar.optString("isSupSpecFunc", "");
        if (isSupSpecFunc.equals("1")) {
            SesameLoginInfo.setSupport(true);
        } else {
            SesameLoginInfo.setSupport(false);
        }

        SesameLoginInfo.setCar_year((membercar.optInt("year", 0)));



        String isTireable = membercar.optString("tireable", "");

        if (isTireable.equals("1")) {
            SesameLoginInfo.setTireable(true);
        } else {
            SesameLoginInfo.setTireable(false);
        }

        int secretaryid = membercar.optInt("secretaryid", 1);
        if (secretaryid == 1) {
            SesameLoginInfo.setSecretaryImg(R.drawable.secretary_female);
            SesameLoginInfo.setSecretaryName(DorideApplication.ApplicationContext.getResources().getString(R.string.register_secretary_girl));

        } else {
            SesameLoginInfo.setSecretaryImg(R.drawable.secretary_male);
            SesameLoginInfo.setSecretaryName(DorideApplication.ApplicationContext.getResources().getString(R.string.register_secretary_boy));

        }

        SesameLoginInfo.setMainten_miles((membercar.optString("mainten_miles", "")));
        SesameLoginInfo.setMainten_time((membercar.optString("mainten_date", "")));
        SesameLoginInfo.setMainten_next_miles(membercar.optInt("mainten_next_miles") + "");
        SesameLoginInfo.setMainten_next_day((membercar.optInt("mainten_next_date") + ""));

        String imt = membercar.optString("isMainten", "");
        if (imt.equals("1")) {
            SesameLoginInfo.setMainten(true);
        } else {
            SesameLoginInfo.setMainten(false);
        }

        // dealer
        JSONObject dealer = mJSON_data.optJSONObject("dealer");
        SesameLoginInfo.setDealerUsername((dealer.optString("name", "")));
        SesameLoginInfo.setDealerAddres((dealer.optString("addres", "")));
        String m = dealer.optString("map", "");
        String[] map = dealer.optString("map", "").split(",");
        if (map != null && map.length > 1) {
            SesameLoginInfo.setDealerLat(Double.parseDouble(map[0]));
            SesameLoginInfo.setDealerLon(Double.parseDouble(map[1]));
        }
        if (map.length > 2) {
            SesameLoginInfo.setDealerZoom(Integer.parseInt(map[2]));
        }
        SesameLoginInfo.setDealerTel(dealer.optString("tel", ""));
        JSONObject pushset = mJSON_data.optJSONObject("pushset");
        if (pushset != null && pushset.length() > 0) {
            SesameLoginInfo.setPush_prizeinfo_flag((pushset.optInt("dealer", 1)));
        }

        // 最近一个日报、周报、月报日期
        JSONObject reportdate = mJSON_data.optJSONObject("reportdate");
        String nullDate = "0000-00-00";
        if (reportdate != null) {
            String s = reportdate.optString("day");
            if (s.equals(nullDate)) {
                SesameLoginInfo.setLately_day("");
            } else {
                SesameLoginInfo.setLately_day(s);
            }
            s = reportdate.optString("week");
            if (s.equals(nullDate)) {
                SesameLoginInfo.setLately_week("");
            } else {
                SesameLoginInfo.setLately_week(s);
            }

            s = reportdate.optString("month");
            if (s.equals(nullDate)) {
                SesameLoginInfo.setLately_month("");
            } else {
                SesameLoginInfo.setLately_month(s);
            }
        }

    }

    private static void parseDorideLoginInfo(JSONObject data) {
        JSONObject member = data.optJSONObject("member");
        LoginInfo.setUseId((member.optString("id", "")));
        LoginInfo.setRealname((member.optString("realname", "")));
        LoginInfo.setUsername(member.optString("username", ""));
        LoginInfo.setGender((member.optString("gender", "")));

        String mobile = (member.optString("mobile", ""));
        LoginInfo.setMobile(mobile);
        if (!TextUtils.isEmpty(mobile)) {
            LoginInfo.setDemoAccount(true);
        } else {
            LoginInfo.setDemoAccount(false);
        }
        LoginInfo.setWeixinbind(member.optString("weixinbind", "0"));
        LoginInfo.setClwbind(member.optString("clwbind", "0"));
        LoginInfo.setRegip(member.optString("regip", ""));
        LoginInfo.setAvatar_id(member.optString("avatar_id", "-1"));
        LoginInfo.setOriginate(member.optString("originate", "0"));
        LoginInfo.setLastlogin(member.optString("lastlogin", "0"));
        LoginInfo.setLoginoauth(member.optString("loginoauth", ""));
        LoginInfo.setLogintimes(member.optString("logintimes", "0"));
        LoginInfo.setCreatedate(member.optString("createdate", ""));
        LoginInfo.setAvatar_img((member.optString("avatar_img", "")));
        LoginInfo.setLifetime((member.optString("lifetime", "")));
        LoginInfo.setAccess_token(member.optString("access_token", ""));
        SesameLoginInfo.setAccess_token((member.optString("access_token", "")));
        LoginInfo.setMain(getFlagResult(member.optString("is_main", "")));
        LoginInfo.setMainDevicename(member
                .optString("move_device_name", ""));
        LoginInfo.setAuthorize_status(member.optString("authorize_switch",
                ""));
        LoginInfo.setHasAuthorize(getFlagResult(member.optString(
                "has_authorize", "")));
        LoginInfo.setNeedAuthorize(getFlagResult(member.optString(
                "need_authorize", "")));
        LoginInfo.setFreezing(getFlagResult(member.optString("is_freezing",
                "")));
        LoginInfo
                .setAuthen(getFlagResult(member.optString("is_authen", "")));
        LoginInfo.setAuthen_name(member.optString("authen_name", ""));
        LoginInfo.setAuthen_card(member.optString("authen_card", ""));

        LoginInfo.setExpires_in((member.optString("expires_in", "")));
        LoginInfo.setExpiresIn((member.optString("expires_in", "")));
        LoginInfo.setSSID(member.optString("SSID", ""));
        LoginInfo.setSSIDPWD(member.optString("SSIDPWD", ""));

        String isSetRemotePwd = member.optString("is_set_remotePwd", "");
        LoginInfo.setSetRemotePwd(getFlagResult(isSetRemotePwd));

        JSONObject membercar = data.optJSONObject("membercar");
        int deviceisnew = membercar.optInt("deviceisnew");
        LogUtils.e("parseLoginInfodeviceisnew------" + deviceisnew);
        LoginInfo.setDeviceisnew(deviceisnew);
        int membercarId = membercar.optInt("id");
        if (membercarId > 0) {
            LoginInfo.setBindCar(true);
        } else {
            LoginInfo.setBindCar(false);
        }
        LoginInfo.setDeviceidstring((membercar.optString("deviceidstring",
                "")));
        int isDeviceActivate = membercar.optInt("isDeviceActivate");
        if (isDeviceActivate == 1) {
            LoginInfo.setDeviceActivate(true);
        } else {
            LoginInfo.setDeviceActivate(false);
        }
        String isUpgradeing = membercar.optString("upgradeing");
        if (isUpgradeing.equals("1")) {
            LoginInfo.setUpgradeing(true);
        } else {
            LoginInfo.setUpgradeing(false);
        }
        String isGpsDevice = membercar.optString("gps_device");
        if (isGpsDevice.equals("1")) {
            LoginInfo.setGpsDevice(true);
        } else {
            LoginInfo.setGpsDevice(false);
        }

        LoginInfo.setBrandid((membercar.optString("brandid", "")));
        LoginInfo.setDevicetype((membercar.optString("devicetype", "")));
        LoginInfo.setOptionid((membercar.optString("optionid", "")));
        LoginInfo.setCarid((membercar.optString("carid", "")));
        String installorder = membercar.optInt("installorder") + "";
        LoginInfo.setInstallorder(getFlagResultOther(installorder));

        LoginInfo.setDealerId(membercar.optString("dealerid", ""));
        LoginInfo.setModelid(membercar.optString("modelid", ""));

        LoginInfo.setCarno((membercar.optString("carno", "")));
        LoginInfo.setStandcarno((membercar.optString("standcarno", "")));
        LoginInfo.setCarcity((membercar.optString("city", "")));
        LoginInfo.setEngineno((membercar.optString("engineno", "")));
        LoginInfo.setRegistno((membercar.optString("registno", "")));
        LoginInfo.setCanQueryVio((membercar.optString("canQueryVio", "")));
        LoginInfo.setCity_code(membercar.optString("cityCode", ""));

        LoginInfo.setCarname((membercar.optString("carname", "")));
        LoginInfo.setCarlogo((membercar.optString("carlogo", "")));

        LoginInfo.setBuydate((membercar.optString("buydate", "")));
        String pin = membercar.optString("bindpin", "");
        if (pin != null && !pin.equals("")) {
            LoginInfo.setPin(member.optString("mobile", ""), pin);
        }
        String vin = membercar.optString("bindvin", "");
        if (vin != null && !vin.equals("")) {
            LoginInfo.setVin(member.optString("mobile", ""), vin);
        }
        LoginInfo
                .setShortstandcarno(membercar.optString("shortstandcarno"));

        int secretaryid = membercar.optInt("secretaryid", 1);

        LoginInfo.setSecretaryName("大乘小秘书");

        LoginInfo.setMainten_miles((membercar
                .optString("mainten_miles", "")));
        LoginInfo
                .setMainten_time((membercar.optString("mainten_date", "")));
        LoginInfo.setMainten_next_miles(membercar
                .optInt("mainten_next_miles") + "");
        LoginInfo.setMainten_next_day((membercar
                .optInt("mainten_next_date") + ""));

        String imt = membercar.optString("isMainten", "");
        if (imt.equals("1")) {
            LoginInfo.setMainten(true);
        } else {
            LoginInfo.setMainten(false);
        }

        int is_tachograph = membercar.optInt("is_tachograph", 0);
        LoginInfo.setTachograph(getFlagResult(is_tachograph + ""));
        LoginInfo.setTbox_type(membercar.optString("tbox_type"));
    }

    private static boolean getFlagResult(String judge) {
        boolean flag = false;
        if (judge != null) {
            if (judge.equals("0")) {
                flag = false;
            } else if (judge.equals("1")) {
                flag = true;
            }
        }
        return flag;
    }

    private static boolean getFlagResultOther(String judge) {
        boolean flag = false;
        if (judge != null) {
            if (judge.equals("2")) {
                flag = false;
            } else if (judge.equals("1")) {
                flag = true;
            }
        }
        return flag;
    }
}
