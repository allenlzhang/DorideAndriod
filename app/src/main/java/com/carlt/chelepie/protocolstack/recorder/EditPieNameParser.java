package com.carlt.chelepie.protocolstack.recorder;

import android.content.SharedPreferences;


import com.carlt.chelepie.appsdk.AppsdkUtils;
import com.carlt.chelepie.control.WIFIControl;
import com.carlt.chelepie.systemconfig.ActionConfig;
import com.carlt.doride.DorideApplication;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.data.UseInfo;
import com.carlt.doride.preference.UseInfoLocal;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.utils.StringUtils;
import com.carlt.sesame.control.CPControl;

import java.util.HashMap;

/**
 * @author @Y.yun 修改WIFI名称
 */
public class EditPieNameParser extends RecorderBaseParserNew<BaseResponseInfo> {

	private String ssid;

	public EditPieNameParser(BaseParser.ResultCallback listener) {
		super(listener, BaseResponseInfo.class);
		mRequestID = ActionConfig.MID_CONFIG_WIFI;
	}

	public void setSsid(String ssid) {
		this.ssid = ssid;
	}

	@Override
	protected String creatPost() {
		HashMap<String, String> mMap = new HashMap<String, String>();
		return CreatPost(mMap);
	}

	@Override
	protected void parser() throws Exception {
		getErroCode();
	}

	@Override
	protected void Success(BaseResponseInfo mBaseResponseInfo) {
		WIFIControl mWIFIControl = WIFIControl.getInstance();
		mWIFIControl.EditSSid(WIFIControl.chelepiePrefix + ssid);
		UseInfo uInfo = UseInfoLocal.getUseInfo();
		if (uInfo != null && !StringUtils.isEmpty(uInfo.getAccount())) {
			SharedPreferences sharp = DorideApplication.ApplicationContext.getSharedPreferences("wifi_info", 0);
			String tmp = sharp.getString(uInfo.getAccount() + "_n", "");
			String pwd = sharp.getString(uInfo.getAccount() + "_p", "");
			WIFIControl.SSID_CONNECT = tmp;
			WIFIControl.SSID_PWD = pwd;
		}
		WIFIControl.getInstance().Editpwd("");
		WIFIControl.getInstance().EditSSid("");
		WIFIControl.SSID_CONNECT = "";
		WIFIControl.SSID_PWD = "";
		super.Success(mBaseResponseInfo);
	}

	@Override
	protected long sendMsg() {
		UseInfo uInfo = UseInfoLocal.getUseInfo();
		if (uInfo != null && !StringUtils.isEmpty(uInfo.getAccount())) {
			SharedPreferences sharp = DorideApplication.ApplicationContext.getSharedPreferences("wifi_info", 0);
			String pwd = sharp.getString(uInfo.getAccount() + "_p", "");
			return AppsdkUtils.CSetWIFIInfo(ssid, pwd, pwd, mSqnum);
		}
		return -1;
	}

}
