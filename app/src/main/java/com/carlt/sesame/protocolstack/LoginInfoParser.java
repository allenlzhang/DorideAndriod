
package com.carlt.sesame.protocolstack;

import com.carlt.doride.DorideApplication;
import com.carlt.sesame.R;
import com.carlt.sesame.data.LoginInfo;
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
		LoginInfo.Last_Login_Time = System.currentTimeMillis();

		JSONObject mJSON_data = mJson.optJSONObject("data");
		if (mJSON_data != null) {
			JSONObject member = mJSON_data.optJSONObject("member");
			LoginInfo.setUseId((member.optString("id", "")));
			LoginInfo.setDealerId((member.optString("dealerid", "")));
			LoginInfo.setRealname((member.optString("realname", "")));
			LoginInfo.setUsername(member.optString("username", ""));
			LoginInfo.setGender((member.optString("gender", "")));
			String mobile = (member.optString("mobile", ""));
			LoginInfo.setMobile(mobile);
			LoginInfo.setStatus((member.optString("status", "")));
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

			LoginInfo.setMain(getFlagResult(member.optString("is_main", "")));
			LoginInfo.setMainDevicename(member.optString("move_device_name", ""));
			LoginInfo.setMainDeviceid(member.optString("move_deviceid", ""));
			LoginInfo.setAuthorize_status(member.optString("authorize_switch", ""));
			LoginInfo.setHasAuthorize(getFlagResult(member.optString("has_authorize", "")));
			LoginInfo.setNeedAuthorize(getFlagResult(member.optString("need_authorize", "")));
			LoginInfo.setFreezing(getFlagResult(member.optString("is_freezing", "")));
			LoginInfo.setAuthen(getFlagResult(member.optString("is_authen", "")));

			LoginInfo.setNoneedpsw(getFlagResult(member.optString("lesspwd_switch", "")));
			LoginInfo.setAuthen_name(member.optString("authen_name", ""));
			LoginInfo.setAuthen_card(member.optString("authen_card", ""));

			LoginInfo.setAccess_token((member.optString("access_token", "")));
			LoginInfo.setExpires_in((member.optString("expires_in", "")));
			LoginInfo.setToken((member.optString("access_token", "")));
			TokenInfo.setToken(member.optString("access_token", ""));
			LoginInfo.setExpiresIn((member.optString("expires_in", "")));
			LoginInfo.setSSID(member.optString("SSID", ""));
			LoginInfo.setSSIDPWD(member.optString("SSIDPWD", ""));

			String isSetRemotePwd = member.optString("is_set_remotePwd", "");
			LoginInfo.setSetRemotePwd(getFlagResult(isSetRemotePwd));

			JSONObject membercar = mJSON_data.optJSONObject("membercar");

			int membercarId = membercar.optInt("id");
			if (membercarId > 0) {
				LoginInfo.setBindCar(true);
			} else {
				LoginInfo.setBindCar(false);
			}
			LoginInfo.setDeviceidstring((membercar.optString("deviceidstring", "")));
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
			LoginInfo.setRemoteControl(membercar.optString("remoteControl", ""));
			String car_pin = membercar.optString("car_pin", "");
			if (car_pin != null && !car_pin.equals("")) {
				LoginInfo.setVpin(mobile, membercar.optString("car_pin", ""));
			}

			// LoginInfo.setIsJumptoBind(membercar.optString("isJumpBind", ""));

			String installorder = membercar.optString("installorder", "");
			LoginInfo.setInstallorder(getFlagResult(installorder));
			String before_device = membercar.optString("before_device", "0");
			LoginInfo.setDeviceType(before_device);

			LoginInfo.setDealerId(membercar.optString("dealerid", ""));
			LoginInfo.setDeviceid(membercar.optString("deviceid", ""));
			LoginInfo.setModelid(membercar.optString("modelid", ""));
			LoginInfo.setLicencelevelid(membercar.optString("licencelevelid", ""));
			LoginInfo.setMainten_next_miles(membercar.optString("mainten_next_miles", ""));
			LoginInfo.setMainten_next_day(membercar.optString("mainten_next_date", ""));
			LoginInfo.setIs_net_sale(membercar.optString("is_net_sale", ""));
			LoginInfo.setFixed_miles(membercar.optString("fixed_miles", ""));
			LoginInfo.setPowerontime(membercar.optString("powerontime", ""));

			LoginInfo.setCarprovice((membercar.optString("carprovice", "")));
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
			LoginInfo.setSLCarLocating(membercar.optInt("SLCarLocating"));
			LoginInfo.setAutoCloseWinSw(membercar.optInt("autoCloseWinSw"));
			LoginInfo.setRemoteStart(membercar.optInt("remoteStart"));
			LoginInfo.setShortstandcarno(membercar.optString("shortstandcarno"));
			LoginInfo.setNeed_pin(membercar.optString("need_pin"));
			String isSupSpecFunc = membercar.optString("isSupSpecFunc", "");
			if (isSupSpecFunc.equals("1")) {
				LoginInfo.setSupport(true);
			} else {
				LoginInfo.setSupport(false);
			}
			
			LoginInfo.setCar_year((membercar.optInt("year", 0)));

			// TODO 测试代码
			// LoginInfo.SLCarLocating = 1;
			// LoginInfo.autoCloseWinSw = 1;
			// LoginInfo.remoteStart = 2;
			// LoginInfo.isSupport = true;

			String isTireable = membercar.optString("tireable", "");

			if (isTireable.equals("1")) {
				LoginInfo.setTireable(true);
			} else {
				LoginInfo.setTireable(false);
			}

			int secretaryid = membercar.optInt("secretaryid", 1);
			if (secretaryid == 1) {
				LoginInfo.setSecretaryImg(R.drawable.secretary_female);
				LoginInfo.setSecretaryName(DorideApplication.ApplicationContext.getResources().getString(R.string.register_secretary_girl));

			} else {
				LoginInfo.setSecretaryImg(R.drawable.secretary_male);
				LoginInfo.setSecretaryName(DorideApplication.ApplicationContext.getResources().getString(R.string.register_secretary_boy));

			}

			LoginInfo.setMainten_miles((membercar.optString("mainten_miles", "")));
			LoginInfo.setMainten_time((membercar.optString("mainten_date", "")));
			LoginInfo.setMainten_next_miles(membercar.optInt("mainten_next_miles") + "");
			LoginInfo.setMainten_next_day((membercar.optInt("mainten_next_date") + ""));

			String imt = membercar.optString("isMainten", "");
			if (imt.equals("1")) {
				LoginInfo.setMainten(true);
			} else {
				LoginInfo.setMainten(false);
			}

			// dealer
			JSONObject dealer = mJSON_data.optJSONObject("dealer");
			LoginInfo.setDealerUsername((dealer.optString("name", "")));
			LoginInfo.setDealerAddres((dealer.optString("addres", "")));
			String m = dealer.optString("map", "");
			String[] map = dealer.optString("map", "").split(",");
			if (map != null && map.length > 1) {
				LoginInfo.setDealerLat(Double.parseDouble(map[0]));
				LoginInfo.setDealerLon(Double.parseDouble(map[1]));
			}
			if (map.length > 2) {
				LoginInfo.setDealerZoom(Integer.parseInt(map[2]));
			}
			LoginInfo.setDealerTel(dealer.optString("tel", ""));
			JSONObject pushset = mJSON_data.optJSONObject("pushset");
			if (pushset != null && pushset.length() > 0) {
				LoginInfo.setPush_prizeinfo_flag((pushset.optInt("dealer", 1)));
			}

			// 最近一个日报、周报、月报日期
			JSONObject reportdate = mJSON_data.optJSONObject("reportdate");
			String nullDate = "0000-00-00";
			if (reportdate != null) {
				String s = reportdate.optString("day");
				if (s.equals(nullDate)) {
					LoginInfo.setLately_day("");
				} else {
					LoginInfo.setLately_day(s);
				}
				s = reportdate.optString("week");
				if (s.equals(nullDate)) {
					LoginInfo.setLately_week("");
				} else {
					LoginInfo.setLately_week(s);
				}

				s = reportdate.optString("month");
				if (s.equals(nullDate)) {
					LoginInfo.setLately_month("");
				} else {
					LoginInfo.setLately_month(s);
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
