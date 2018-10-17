package com.carlt.chelepie.protocolstack.recorder;

import android.content.SharedPreferences;


import com.carlt.chelepie.appsdk.AppsdkUtils;
import com.carlt.chelepie.control.WIFIControl;
import com.carlt.chelepie.data.recorder.BaseResponseInfo;
import com.carlt.chelepie.systemconfig.ActionConfig;
import com.carlt.doride.DorideApplication;
import com.carlt.doride.data.UseInfo;
import com.carlt.doride.preference.UseInfoLocal;
import com.carlt.doride.utils.StringUtils;
import com.carlt.sesame.control.CPControl;

import java.util.HashMap;

/**
 * 修改WIFI密码
 * 
 * @author @Y.yun
 * 
 */
public class EditPiePasswordParser extends RecorderBaseParserNew<BaseResponseInfo> {

	private String psw;

	public EditPiePasswordParser(CPControl.GetResultListCallback listener) {
		super(listener, BaseResponseInfo.class);
		mRequestID = ActionConfig.MID_CONFIG_WIFI;
	}

	public void setPassword(String psw) {
		this.psw = psw;
	}

	@Override
	protected String creatPost() {
		HashMap<String, String> mMap = new HashMap<String, String>();
//		mMap.put("action", "set_passwd");
//		mMap.put("passwd", psw);
		return CreatPost(mMap);
	}

	@Override
	protected void parser() throws Exception {
		getErroCode();
	}

	@Override
	protected void Success(BaseResponseInfo mBaseResponseInfo) {
		WIFIControl.getInstance().Editpwd(psw);
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
			String tmp = sharp.getString(uInfo.getAccount() + "_n", "");
			String pwd = sharp.getString(uInfo.getAccount() + "_p", "");//旧密码
			int i = tmp.indexOf("-");
			if (i == -1) {
				i = 0;
			}
			return AppsdkUtils.CSetWIFIInfo(tmp.substring(i + 1), pwd, psw, mSqnum);
		}
		return -1;
	}
}
