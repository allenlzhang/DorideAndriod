package com.carlt.yema.control;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.carlt.yema.MainActivity;
import com.carlt.yema.YemaApplication;
import com.carlt.yema.model.LoginInfo;
import com.carlt.yema.ui.activity.login.DeviceBindActivity;
import com.carlt.yema.ui.view.PopBoxCreat;
import com.carlt.yema.ui.view.UUUpdateDialog;

import org.json.JSONObject;


/**
 * 登录控制
 *
 * @author daisy
 */
public class LoginControl {
    public static Activity mCtx;
    public static UUUpdateDialog.DialogUpdateListener mDialogUpdateListener;

    public static void logic(final Activity mContext) {
        mCtx = mContext;
        YemaApplication.getInstanse().setIsshowupdata(false);
        String className = mContext.getClass().getName();
        // 判断是否绑定设备
        String s = LoginInfo.getDeviceidstring();
        Log.e("info", "deviceidstring==" + s);
        // 测试代码开始
        // s="1234567890123456";
        // 测试代码结束
        if (s != null && s.length() > 0 && !s.equals("null")) {
            // 已绑定设备,判断是否激活设备
            boolean isDeviceActivate = LoginInfo.isDeviceActivate();
            Log.e("info", "isDeviceActivate==" + isDeviceActivate);
            if (isDeviceActivate) {

                // 野马绑定和激活合并，如果激活直接进入主页
                Intent mainIntent = new Intent(mContext,
                        MainActivity.class);
                mContext.startActivity(mainIntent);

            } else {
                // 未激活设备
                String vin = LoginInfo.getVin(LoginInfo.getMobile());
                if (vin == null || vin.equals("")) {
                    Intent loginIntent = new Intent(mContext,
                            DeviceBindActivity.class);

                    mContext.startActivity(loginIntent);
                } else {
                    boolean isUpdating = LoginInfo.isUpgradeing();
                    // 是否需要升级
                    if (isUpdating) {
                        // 设备正在升级，跳转至升级页面
                        PopBoxCreat.showUUUpdateDialog(mContext,
                                new UUUpdateDialog.DialogUpdateListener() {

                                    @Override
                                    public void onSuccess() {
                                        LoginInfo.setUpgradeing(false);
                                        LoginControl.logic(mCtx);
                                        if (mDialogUpdateListener != null) {
                                            mDialogUpdateListener.onSuccess();
                                        }
                                    }

                                    @Override
                                    public void onFailed() {
                                        if (mDialogUpdateListener != null) {
                                            mDialogUpdateListener.onFailed();
                                        }
                                    }
                                });
                    } else {
                        // 设备不需要升级，跳转绑定 回填Vin码
                        Intent loginIntent = new Intent(mContext,
                                DeviceBindActivity.class);
                        loginIntent.putExtra("vin", LoginInfo.getVin(LoginInfo.getMobile()));
                        loginIntent.putExtra("carType", LoginInfo.getCarname());
                        mContext.startActivity(loginIntent);
                    }
                }

            }
        } else {
            Intent loginIntent = new Intent(mContext,
                    DeviceBindActivity.class);
            loginIntent.putExtra("vin", LoginInfo.getVin(LoginInfo.getMobile()));
            loginIntent.putExtra("carType", LoginInfo.getCarname());
            mContext.startActivity(loginIntent);
        }
    }

    public static void parseLoginInfo(JSONObject data) {
        if (data != null) {
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

            // 测试代码
            // LoginInfo.setAuthen(getFlagResult("0"));
            // 测试代码结束
            LoginInfo.setAuthen_name(member.optString("authen_name", ""));
            LoginInfo.setAuthen_card(member.optString("authen_card", ""));

            LoginInfo.setExpires_in((member.optString("expires_in", "")));
//            YemaApplication.TOKEN = member.optString("access_token", "");
            LoginInfo.setExpiresIn((member.optString("expires_in", "")));
            LoginInfo.setSSID(member.optString("SSID", ""));
            LoginInfo.setSSIDPWD(member.optString("SSIDPWD", ""));

            String isSetRemotePwd = member.optString("is_set_remotePwd", "");
            // 测试代码
            // isSetRemotePwd = "0";
            // 测试代码结束
            LoginInfo.setSetRemotePwd(getFlagResult(isSetRemotePwd));

            JSONObject membercar = data.optJSONObject("membercar");

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
            // 测试代码
            // installorder="0";
            // 测试代码结束
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

                LoginInfo.setSecretaryName("野马小秘书");

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
