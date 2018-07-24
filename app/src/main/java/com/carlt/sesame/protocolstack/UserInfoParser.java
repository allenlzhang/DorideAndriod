package com.carlt.sesame.protocolstack;

import com.carlt.doride.DorideApplication;
import com.carlt.sesame.R;
import com.carlt.sesame.control.DaoControl;
import com.carlt.sesame.data.LoginInfo;
import com.carlt.sesame.data.career.ChallengeInfo;
import com.carlt.sesame.data.career.LicenceLevelInfo;
import com.carlt.sesame.data.career.MedalInfo;
import com.carlt.sesame.preference.TokenInfo;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author user
 *  解析注册信息
 */
public class UserInfoParser extends BaseParser {

	public UserInfoParser() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void parser() {
		JSONObject mJSON_data = mJson.optJSONObject("data");
		JSONObject member = mJSON_data.optJSONObject("member");
		LoginInfo.setUseId((member.optString("id", "")));
		LoginInfo.setDealerId((member.optString("dealerid", "")));
		LoginInfo.setRealname((member.optString("realname", "")));
		LoginInfo.setGender((member.optString("gender", "")));
		LoginInfo.setMobile((member.optString("mobile", "")));
		LoginInfo.setStatus((member.optString("status", "")));
		LoginInfo.setAvatar_img((member.optString("avatar_img", "")));
		LoginInfo.setToken((member.optString("access_token", "")));
		TokenInfo.setToken(member.optString("access_token", ""));
		LoginInfo.setExpiresIn((member.optString("expires_in", "")));
		LoginInfo.setSSID(member.optString("SSID", ""));
		LoginInfo.setSSIDPWD(member.optString("SSIDPWD", ""));

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
		String isUpgradeing=membercar.optString("upgradeing");
        if(isUpgradeing.equals("1")){
            LoginInfo.setUpgradeing(true);
        }else{
            LoginInfo.setUpgradeing(true);
        }
		
		LoginInfo.setBrandid((membercar.optString("brandid", "")));
		LoginInfo.setOptionid((membercar.optString("optionid", "")));
		LoginInfo.setCarid((membercar.optString("carid", "")));
		
		LoginInfo.setDealerId(membercar.optString("dealerid", ""));
		LoginInfo.setDeviceid(membercar.optString("deviceid", ""));
		LoginInfo.setModelid(membercar.optString("modelid", ""));
		LoginInfo.setLicencelevelid(membercar.optString("licencelevelid",""));
		LoginInfo.setMainten_next_miles(membercar.optString("mainten_next_miles",""));
		LoginInfo.setMainten_next_day(membercar.optString("mainten_next_day", ""));
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

		LoginInfo.setCarname((membercar.optString("carname", "")));
		LoginInfo.setCarlogo((membercar.optString("carlogo", "")));

		LoginInfo.setBuydate((membercar.optString("buydate", "")));
		LoginInfo.setSLCarLocating(membercar.optInt("SLCarLocating"));
		LoginInfo.setAutoCloseWinSw(membercar.optInt("autoCloseWinSw"));
		LoginInfo.setRemoteStart(membercar.optInt("remoteStart"));

		String isSupSpecFunc = membercar.optString("isSupSpecFunc", "");
		if (isSupSpecFunc.equals("1")) {
			LoginInfo.setSupport(true);
		} else {
			LoginInfo.setSupport(false);
		}

		// TODO 测试代码
		// LoginInfo.SLCarLocating = 1;
		// LoginInfo.autoCloseWinSw = 1;
		// LoginInfo.remoteStart = 2;
		// LoginInfo.isSupport = true;

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

		String imt = membercar.optString("isNextMain", "");
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
		JSONObject pushset = mJSON_data.optJSONObject("pushset");
		if (pushset != null && pushset.length() > 0) {
			LoginInfo.setPush_prizeinfo_flag((pushset.optInt("dealer", 1)));
		}

		// 勋章入库
		DaoControl mDaoControl = DaoControl.getInstance();
		JSONArray medal = mJSON_data.optJSONArray("medal");
		for (int i = 0; i < medal.length(); i++) {
			MedalInfo mMedalInfo = new MedalInfo();
			JSONObject temp = (JSONObject) medal.opt(i);
			mMedalInfo.setId(temp.optString("id"));
			mMedalInfo.setName(temp.optString("name"));
			mMedalInfo.setIconUrl1(temp.optString("icon"));
			mMedalInfo.setIconUrl2(temp.optString("iconactive"));
			mMedalInfo.setDescription(temp.optString("description"));
			mMedalInfo.setLevel(temp.optInt("level"));
			mDaoControl.insertMedal(mMedalInfo);
		}
		// 挑战入库
		JSONArray challenge = mJSON_data.optJSONArray("challenge");
		for (int i = 0; i < challenge.length(); i++) {
			ChallengeInfo mChallengeInfo = new ChallengeInfo();
			JSONObject temp = (JSONObject) challenge.opt(i);
			mChallengeInfo.setId(temp.optString("id"));
			mChallengeInfo.setType(temp.optInt("type"));
			mChallengeInfo.setFuel(temp.optString("fuel"));
			mChallengeInfo.setMiles(temp.optString("miles"));
			mChallengeInfo.setTime(temp.optString("time"));
			mChallengeInfo.setSpeed(temp.optString("speed"));
			mChallengeInfo.setPoint(temp.optString("point"));
			mChallengeInfo.setSort(temp.optInt("sort"));
			mChallengeInfo.setName(temp.optString("name"));
			mChallengeInfo.setStatus(temp.optString("status"));
			mChallengeInfo.setInfo(temp.optString("info"));
			mDaoControl.insertChallenge(mChallengeInfo);
		}

		// 驾驶证等级信息入库
		JSONArray licence = mJSON_data.optJSONArray("licence");
		for (int i = 0; i < licence.length(); i++) {
			LicenceLevelInfo mLicenceLevelInfo = new LicenceLevelInfo();
			JSONObject temp = (JSONObject) licence.opt(i);
			mLicenceLevelInfo.setId(temp.optString("id"));
			mLicenceLevelInfo.setName(temp.optString("name"));
			mLicenceLevelInfo.setPoint(temp.optInt("point"));
			mLicenceLevelInfo.setIconUrl1(temp.optString("totem"));
			mLicenceLevelInfo.setIconUrl2(temp.optString("totemactive"));
			mLicenceLevelInfo.setLevel(temp.optInt("level"));
			mDaoControl.insertLicenceLevel(mLicenceLevelInfo);
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
