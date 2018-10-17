package com.carlt.chelepie.protocolstack.recorder;


import com.carlt.chelepie.appsdk.AppsdkUtils;
import com.carlt.chelepie.data.recorder.BaseResponseInfo;
import com.carlt.chelepie.systemconfig.ActionConfig;
import com.carlt.doride.utils.MyParse;
import com.carlt.sesame.control.CPControl;

/**
 * *绑定激活
 * 
 * @author @Y.yun
 * 
 */
public class RecorderBindDeviceParser extends RecorderBaseParserNew<BaseResponseInfo> {
	/**
	 * 手机号码
	 */
	private String uid = "";

	public RecorderBindDeviceParser(CPControl.GetResultListCallback listener, String uid) {
		super(listener, BaseResponseInfo.class);
		mRequestID = ActionConfig.MID_BIND;
		MSG_FAIL = "绑定激活设备失败";
		MSG_SUCC = "绑定激活设备成功";
		SOTIMEOUT2 = 1000 * 30;
		this.uid = uid;
		mSqnum = 0;
	}

	@Override
	protected String creatPost() {
		return CreatPost(mMap);
	}

	@Override
	protected long sendMsg() {
		return AppsdkUtils.CBindDevice(uid,mSqnum);
	}

	// 具体原因保存在reserve
	// 0成功，1账号已经绑定，2设备已经绑定，3激活失败，10经销商不匹配
	@Override
	protected void parser() throws Exception {
		String errcode;
		if (mJson != null) {
			// 有返回值的
			errcode = mJson.optString("iRet");
		} else {
			// 没有返回值的默认成功
			errcode = "0";
		}
//		/*
//		 * 车乐拍绑定激活的时候 App上给用户展示的话术
//0.激活成功；
//1.激活失败，您的账户已绑定过其他车辆；
//2.激活失败，您的设备已绑定过其他帐号
//3.激活失败，该设备不支持您的爱车品牌。
//4.激活失败，请联系您的经销商。
//10.当前设备与您的账户不匹配，请联系您的销商
//-1.激活失败
//		 */
		if (errcode != null && errcode.length() > 0) {
			int errcode_int = MyParse.parseInt(errcode);
			if (errcode_int == BaseResponseInfo.RECORD_OK) {
				int reas = mJson.getInt("reserve");
				String resl = "激活失败";
				if(0 == reas){
					mBaseResponseInfo.setFlag(BaseResponseInfo.SUCCESS);
					mBaseResponseInfo.setInfo("OK");
					return;
				}else if (reas == 1) {
					resl = "激活失败，您的账户已绑定过其他车辆";
				} else if (reas == 2) {
					resl = "激活失败，您的设备已绑定过其他帐号";
				} else if (reas == 3) {
					resl = "激活失败，该设备不支持您的爱车品牌";
				}else if (reas == 4) {
					resl = "激活失败，请联系您的经销商";
				}else if(reas == 10){
					resl = "当前设备与您的账户不匹配，请联系您的销商";
				}else if (reas == -1) {
					resl = "激活失败";
				}
				mBaseResponseInfo.setFlag(BaseResponseInfo.ERRO);
				mBaseResponseInfo.setInfo(resl);
			} else {
				int reason = mJson.getInt("reserve");
				String res = "激活失败";
				if (reason == 1) {
					res = "激活失败，您的账户已绑定过其他车辆";
				} else if (reason == 2) {
					res = "激活失败，您的设备已绑定过其他帐号";
				} else if (reason == 3) {
					res = "激活失败，该设备不支持您的爱车品牌";
				} else if (reason == 4) {
					res = "激活失败，请联系您的经销商";
				}else if(reason == 10){
					res = "当前设备与您的账户不匹配，请联系您的销商";
				} else if (reason == -1) {
					res = "激活失败";
				}
				mBaseResponseInfo.setFlag(BaseResponseInfo.ERRO);
				mBaseResponseInfo.setInfo(res);
			}
		} else {
			mBaseResponseInfo.setFlag(BaseResponseInfo.ERRO);
			mBaseResponseInfo.setInfo("激活失败");
		}
	}
}
