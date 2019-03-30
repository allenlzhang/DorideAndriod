
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
//        DorideApplication.Last_Login_Time = System.currentTimeMillis();
//
        JSONObject mJSON_data = mJson.optJSONObject("data");
        if (mJSON_data != null) {
//            JSONObject member = mJSON_data.optJSONObject("member");
//            SesameLoginInfo.setUseId((member.optString("id", "")));
//            SesameLoginInfo.setDealerId((member.optString("dealerid", "")));
//            SesameLoginInfo.setRealname((member.optString("realname", "")));
//            SesameLoginInfo.setUsername(member.optString("username", ""));
//            SesameLoginInfo.setGender((member.optString("gender", "")));
//            String mobile = (member.optString("mobile", ""));
//            SesameLoginInfo.setMobile(mobile);
//            SesameLoginInfo.setAvatar_img((member.optString("avatar_img", "")));
//
//            SesameLoginInfo.setMain(getFlagResult(member.optString("is_main", "")));
//            SesameLoginInfo.setMainDevicename(member.optString("move_device_name", ""));
//            SesameLoginInfo.setFreezing(getFlagResult(member.optString("is_freezing", "")));
//            SesameLoginInfo.setAuthen(getFlagResult(member.optString("is_authen", "")));
//
//            SesameLoginInfo.setNoneedpsw(getFlagResult(member.optString("lesspwd_switch", "")));
//            SesameLoginInfo.setAuthen_name(member.optString("authen_name", ""));
//            SesameLoginInfo.setAuthen_card(member.optString("authen_card", ""));
//
//            TokenInfo.setToken((member.optString("access_token", "")));
//            TokenInfo.setToken(member.optString("access_token", ""));
//            SesameLoginInfo.setToken((member.optString("access_token", "")));
//            TokenInfo.setToken(member.optString("access_token", ""));
//
//            String isSetRemotePwd = member.optString("is_set_remotePwd", "");
//            SesameLoginInfo.setSetRemotePwd(getFlagResult(isSetRemotePwd));
//
//            JSONObject membercar = mJSON_data.optJSONObject("membercar");
//
//            SesameLoginInfo.setDeviceidstring((membercar.optString("deviceidstring", "")));
//            String isGpsDevice = membercar.optString("gps_device");
//            if (isGpsDevice.equals("1")) {
//                SesameLoginInfo.setGpsDevice(true);
//            } else {
//                SesameLoginInfo.setGpsDevice(false);
//            }
//
//            SesameLoginInfo.setBrandid((membercar.optString("brandid", "")));
//            SesameLoginInfo.setOptionid((membercar.optString("optionid", "")));
//            SesameLoginInfo.setCarid((membercar.optString("carid", "")));
//            SesameLoginInfo.setRemoteControl(membercar.optString("remoteControl", ""));
//
//            // LoginInfo.setIsJumptoBind(membercar.optString("isJumpBind", ""));
//
//            String installorder = membercar.optString("installorder", "");
//            SesameLoginInfo.setInstallorder(getFlagResult(installorder));
//            String before_device = membercar.optString("before_device", "0");
//
//            SesameLoginInfo.setDealerId(membercar.optString("dealerid", ""));
//            SesameLoginInfo.setMainten_next_miles(membercar.optString("mainten_next_miles", ""));
//            SesameLoginInfo.setMainten_next_day(membercar.optString("mainten_next_date", ""));
//
//            SesameLoginInfo.setCarno((membercar.optString("carno", "")));
//            SesameLoginInfo.setStandcarno((membercar.optString("standcarno", "")));
//            SesameLoginInfo.setCarcity((membercar.optString("city", "")));
//            SesameLoginInfo.setEngineno((membercar.optString("engineno", "")));
//            SesameLoginInfo.setRegistno((membercar.optString("registno", "")));
//            SesameLoginInfo.setCanQueryVio((membercar.optString("canQueryVio", "")));
//            SesameLoginInfo.setCity_code(membercar.optString("cityCode", ""));
//
//            SesameLoginInfo.setCarname((membercar.optString("carname", "")));
//            SesameLoginInfo.setCarlogo((membercar.optString("carlogo", "")));
//
//            SesameLoginInfo.setBuydate((membercar.optString("buydate", "")));
//            SesameLoginInfo.setAutoCloseWinSw(membercar.optInt("autoCloseWinSw"));
//            SesameLoginInfo.setShortstandcarno(membercar.optString("shortstandcarno"));
//
//            SesameLoginInfo.setCar_year((membercar.optInt("year", 0)));

            // TODO 测试代码
            // LoginInfo.SLCarLocating = 1;
            // LoginInfo.autoCloseWinSw = 1;
            // LoginInfo.remoteStart = 2;
            // LoginInfo.isSupport = true;


//            int secretaryid = membercar.optInt("secretaryid", 1);
//            if (secretaryid == 1) {
//                SesameLoginInfo.setSecretaryImg(R.drawable.secretary_female);
//                SesameLoginInfo.setSecretaryName(DorideApplication.ApplicationContext.getResources().getString(R.string.register_secretary_girl));
//
//            } else {
//                SesameLoginInfo.setSecretaryImg(R.drawable.secretary_male);
//                SesameLoginInfo.setSecretaryName(DorideApplication.ApplicationContext.getResources().getString(R.string.register_secretary_boy));
//
//            }

//            SesameLoginInfo.setMainten_miles((membercar.optString("mainten_miles", "")));
//            SesameLoginInfo.setMainten_time((membercar.optString("mainten_date", "")));
//            SesameLoginInfo.setMainten_next_miles(membercar.optInt("mainten_next_miles") + "");
//            SesameLoginInfo.setMainten_next_day((membercar.optInt("mainten_next_date") + ""));
//
//            String imt = membercar.optString("isMainten", "");
//            if (imt.equals("1")) {
//                SesameLoginInfo.setMainten(true);
//            } else {
//                SesameLoginInfo.setMainten(false);
//            }


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
