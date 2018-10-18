package com.carlt.chelepie.protocolstack.recorder;


import com.carlt.chelepie.appsdk.AppsdkUtils;
import com.carlt.chelepie.control.RecorderControl;
import com.carlt.chelepie.data.recorder.PieVersion;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.sesame.control.CPControl;

import java.util.HashMap;

/**
 * 获取设备摄像头版本号
 * @author Administrator
 *
 */
public class PieVersionParser extends RecorderBaseParserNew {

	public PieVersionParser(BaseParser.ResultCallback listener, Class mClass) {
		super(listener, mClass);
	}

	@Override
	protected String creatPost() {
		HashMap<String, String> mMap = new HashMap<String, String>();
		mMap.put("action", "firmware_version");
		return CreatPost(mMap);
	}

	@Override
	protected void parser() throws Exception {

		PieVersion mPieVersion = (PieVersion) mBaseResponseInfo;
		mPieVersion.setApps_ver(mJson.optString("apps_ver"));
		mPieVersion.setPcb_ver(mJson.optString("pcb_ver"));
		mPieVersion.setKernel_ver(mJson.optString("kernel_ver"));
		mPieVersion.setSd_mount_point(mJson.optString("SD_MountPoint"));
		
		//测试用
		RecorderControl.getInstance().CATALOG_PREFIX=mJson.optString("SD_MountPoint");
	}
	
	@Override
	protected long sendMsg() {
		return AppsdkUtils.CGetSystemInfo(mSqnum);
	}
}
