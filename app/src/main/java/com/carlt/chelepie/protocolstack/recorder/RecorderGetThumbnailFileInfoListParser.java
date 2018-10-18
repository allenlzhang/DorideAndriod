package com.carlt.chelepie.protocolstack.recorder;


import com.carlt.chelepie.appsdk.AppsdkUtils;
import com.carlt.chelepie.data.recorder.PieDownloadInfo;
import com.carlt.chelepie.data.recorder.PieDownloadListInfo;
import com.carlt.chelepie.data.recorder.PieInfo;
import com.carlt.chelepie.manager.DeviceConnectManager;
import com.carlt.chelepie.systemconfig.ActionConfig;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.preference.UseInfoLocal;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.utils.Log;
import com.carlt.doride.utils.StringUtils;
import com.carlt.sesame.control.CPControl;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 获取缩略图信息列表
 * 
 * @author @Y.yun
 */
public class RecorderGetThumbnailFileInfoListParser extends RecorderBaseParserNew<PieDownloadListInfo> {
	private String startTime = "";
	private String endTime = "";
	
	//0:全天定时录像，1: 命令抓拍触发， 2：碰撞事件触发
	private int type = 0;

	public RecorderGetThumbnailFileInfoListParser(BaseParser.ResultCallback listener, String startTime, String endTime) {
		this(listener, startTime, endTime, 0);
	}

	public RecorderGetThumbnailFileInfoListParser(BaseParser.ResultCallback listener, String startTime, String endTime, int type) {
		super(listener, PieDownloadListInfo.class);
		MSG_FAIL = "获取文件列表信息失败";
		MSG_SUCC = "获取文件列表信息成功";
		mRequestID = ActionConfig.MID_SEARCH_FILE;
		this.startTime = startTime;
		this.endTime = endTime;
		this.type = type;
	}

	@Override
	protected String creatPost() {
		return CreatPost(mMap);
	}

	@Override
	public void run() {
		if (!DeviceConnectManager.isDeviceConnect()) {
			Erro(MSG_ERRO);
			return;
		}

		try {
			// 搜索一整天的缩略图文件
			int num = 0;
			int allNum = 0;
			int time = 0;
			long start = System.currentTimeMillis();
			Log.i(TAG, "开始时间:" + start);
			do {
				long result = AppsdkUtils.CGThumbnail(startTime, endTime, type,mSqnum);
				if (result != 0) {
					Erro(MSG_ERRO);
					return;
				}
				byte[] buf = readResult(ActionConfig.MID_SEARCH_FILE);
				if (buf[0] != 0) {
					Erro(MSG_FAIL);
					return;
				}
				mJson = new JSONObject(new String(buf, 3, buf.length - 3));
				num = mJson.getInt("currentNum");
				allNum = mJson.getInt("allNum");
				JSONArray array = mJson.getJSONArray("list");
				String account = UseInfoLocal.getUseInfo().getAccount();
				String deviceString = PieInfo.getInstance().getDeviceName();
				for (int i = 0; i < array.length(); i++) {
					JSONObject obj = array.getJSONObject(i);
					int fileLen = obj.getInt("fileLength");
					String fileName = obj.getString("filename");
					String bgTime = obj.getString("startTime");
					String endTime = obj.getString("endTime");
					
					int fileNo = obj.getInt("fileNo");
					int recordType = obj.getInt("recordType");
					int minRecordType = obj.getInt("minRecordType");

					PieDownloadInfo info = new PieDownloadInfo();
					info.setFileSrcName(fileName);
					info.setStartTime(bgTime);
					info.setEndTime(endTime);
					info.setFileName(StringUtils.spliteTime(fileName));
					info.setType(PieDownloadInfo.TYPE_JPG);
					info.setStoreType(PieDownloadInfo.STORE_THUMBNAIL);
					info.setLocalPath("");
					info.setTotalLen(fileLen);
					info.setAccout(account);
					info.setDeviceName(deviceString);
					info.setFileNo(fileNo);
					info.setRecordType(recordType);
					info.setMinRecordType(minRecordType);
					mBaseResponseInfo.getArrays().add(info);
					startTime = endTime;
				}
				time++;
			} while (num < allNum);// 如果数目等于64说明还有,且小于总数

			Log.i(TAG, "次数:" + time);
			Log.i(TAG, "结束时间:" + System.currentTimeMillis());
			Log.i(TAG, "共用时间:" + (System.currentTimeMillis() - start));
			mBaseResponseInfo.setFlag(BaseResponseInfo.SUCCESS);
			mBaseResponseInfo.setInfo(MSG_SUCC);
			Success(mBaseResponseInfo);
		} catch (Exception ex) {
			Log.e(TAG, "Socket发送消息失败=" + ex);
			Erro(MSG_ERRO);
		}
	}
	
	public PieDownloadListInfo getReturn() {
		return mBaseResponseInfo;
	}

	@Override
	protected void parser() throws Exception {
	}

	@Override
	protected long sendMsg() {
		return -1;
	}

}
