
package com.carlt.sesame.protocolstack;

import com.carlt.doride.DorideApplication;
import com.carlt.doride.R;
import com.carlt.doride.model.LoginInfo;
import com.carlt.sesame.data.SesameLoginInfo;
import com.carlt.sesame.preference.TokenInfo;

import org.json.JSONObject;

/**
 * @author user 解析登录信息
 */
public class LoginInfoParser extends BaseParser {

    public LoginInfoParser() {
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void parser() {
        SesameLoginInfo.Last_Login_Time = System.currentTimeMillis();

        JSONObject mJSON_data = mJson.optJSONObject("data");
        if (mJSON_data != null) {
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
            SesameLoginInfo.setExpires_in((member.optString("expires_in", "")));
            SesameLoginInfo.setToken((member.optString("access_token", "")));
            TokenInfo.setToken(member.optString("access_token", ""));
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

            // TODO 测试代码
            // LoginInfo.SLCarLocating = 1;
            // LoginInfo.autoCloseWinSw = 1;
            // LoginInfo.remoteStart = 2;
            // LoginInfo.isSupport = true;

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
    }

    private boolean getFlagResult(String judge) {
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

}
