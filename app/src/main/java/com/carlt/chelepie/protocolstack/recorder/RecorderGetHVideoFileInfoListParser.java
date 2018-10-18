package com.carlt.chelepie.protocolstack.recorder;

import android.os.SystemClock;


import com.carlt.chelepie.appsdk.AppsdkUtils;
import com.carlt.chelepie.data.recorder.PieDownloadInfo;
import com.carlt.chelepie.data.recorder.PieDownloadListInfo;
import com.carlt.chelepie.data.recorder.PieInfo;
import com.carlt.chelepie.manager.DeviceConnectManager;
import com.carlt.chelepie.systemconfig.ActionConfig;
import com.carlt.chelepie.utils.BitConverter;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.preference.UseInfoLocal;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.utils.Log;
import com.carlt.doride.utils.StringUtils;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.utility.MyTimeUtil;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 获取全天定时录视频列表
 * 
 * @author @Y.yun
 */
public class RecorderGetHVideoFileInfoListParser extends RecorderBaseParserNew<PieDownloadListInfo> {

	public static final String DEFAULT_TIME = "1970-01-01 00:00:00";
	public static final int MAXLEN = 64;
	public static final int TYPE_VIDEO = 0;
	private static int lastType = TYPE_VIDEO; // 上次请求的文件类型

	private String beginTime = "";
	private String endTime = "";
	private int type = TYPE_VIDEO;
	private int num;

	/**
	 * @param listener
	 * @param beginTime
	 * @param isContinue
	 *            是否接着上次请求 默认先请求三次从视频开始，完了后自动开始请求图片
	 */
	public RecorderGetHVideoFileInfoListParser(BaseParser.ResultCallback listener, String beginTime, boolean isContinue) {
		super(listener, PieDownloadListInfo.class);
		MSG_FAIL = "获取文件列表信息失败";
		MSG_SUCC = "获取文件列表信息成功";
		mRequestID = ActionConfig.MID_SEARCH_FILE;
		// 搜索结束时间，为当前时间
		this.endTime = MyTimeUtil.ENDTIME;
		this.beginTime = DEFAULT_TIME;
		this.type = lastType;
	}

	@Override
	public void run() {
		if (!DeviceConnectManager.isDeviceConnect()) {
			Erro(MSG_ERRO);
			return;
		}

		try {
			num = 0;
			int time = 0;
			long start = System.currentTimeMillis();
			Log.i(TAG, "开始时间:" + start);
			long result = AppsdkUtils.CGetFilelist(beginTime, endTime, 0, mSqnum);// 0:全天定时录像，1: 命令抓拍触发， 2：碰撞事件触发
			if (result != 0) {
				Erro(MSG_ERRO);
				return;
			}
			readVideoResult();
			Log.i(TAG, "次数:" + time);
			Log.i(TAG, "结束时间:" + System.currentTimeMillis());
			Log.i(TAG, "共用时间:" + (System.currentTimeMillis() - start));
			mBaseResponseInfo.setFlag(BaseResponseInfo.SUCCESS);
			mBaseResponseInfo.setInfo(MSG_SUCC);
			mBaseResponseInfo.setHasMore(mBaseResponseInfo.getArrays().size() >= MAXLEN * 3);

			// 记录最后一次的开始时间，请求的类型
			lastType = type;
			Success(mBaseResponseInfo);
		} catch (Exception ex) {
			Log.e(TAG, "Socket发送消息失败=" + ex);
			Erro(MSG_ERRO);
		}

	}

	@Override
	protected void parser() throws Exception {
		getErroCode();
	}

	public void readVideoResult() {
		long currentMills = System.currentTimeMillis();
		boolean isTimeout = false;
		byte[] result = null;
		boolean ok = false;

		while (!isTimeout && !ok) {
			if (System.currentTimeMillis() - currentMills > SOTIMEOUT) {
				isTimeout = true;
			}

			// 没有读取超时
			for (int i = 0; i < DeviceConnectManager.Buffers.size(); i++) {
				byte[] temp = DeviceConnectManager.Buffers.get(i);
				short action = BitConverter.littleEndianReadShort(temp, 1);
				if (action == ActionConfig.MID_SEARCH_FILE_RES) {
					result = temp;
					break;
				}
			}

			if (isTimeout) {
				DeviceConnectManager.Buffers.clear();
				break;
			}

			if (result != null) {
				ok = analysis(result);
				if (ok) {
					DeviceConnectManager.Buffers.remove(result);
				} else {
					result = null;
					// 没找到 未超时 休息两毫秒 接着找
					SystemClock.sleep(50);
				}
			}
		}
	}

	/**
	 * @param temp
	 */
	public boolean analysis(byte[] temp) {
		try {
			mJson = new JSONObject(new String(temp, 3, temp.length - 3));
			int seqNum = mJson.getInt("seqNum");
			if (seqNum != mSqnum) {
				return false;
			}
			
			int iRet = mJson.getInt("iRet");
			if (iRet != 100) {
				return true;
			}
			
			num = mJson.getInt("currentNum");
			JSONArray array = mJson.getJSONArray("list");
			if (num < 1) {
				return true;
			}
			String account = UseInfoLocal.getUseInfo().getAccount();
			String deviceString = PieInfo.getInstance().getDeviceName();
			int len = array.length();
			for (int i = 0; i < len; i++) {
				JSONObject obj1 = array.getJSONObject(i);
				String fileName = obj1.getString("filename");
				String bgTime = obj1.getString("startTime");
				String endTime = obj1.getString("endTime");
				long fileNo = obj1.getLong("fileNo");
				int fileLen = obj1.getInt("fileLength");

				PieDownloadInfo info = new PieDownloadInfo();
				info.setFileSrcName(fileName);
				info.setStartTime(bgTime);
				info.setFileNo(fileNo);
				info.setEndTime(endTime);
				info.setFileName(StringUtils.spliteTime(fileName));
				info.setStoreType(PieDownloadInfo.STORE_ALLLISTS);//全天定时录制视频
				info.setType(type == TYPE_VIDEO ? PieDownloadInfo.TYPE_H264 : PieDownloadInfo.TYPE_JPG);
				info.setTotalLen(fileLen);
				info.setAccout(account);
				info.setDeviceName(deviceString);

				mBaseResponseInfo.getArrays().add(info);
				if (i == len - 1) {
					beginTime = endTime;
				}
			}
//			if(num >= 64){
//				this.run();
//			}

			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return false;
	}

	public PieDownloadListInfo getReturn() {
		return mBaseResponseInfo;
	}

	@Override
	protected String creatPost() {
		return null;
	}

}
