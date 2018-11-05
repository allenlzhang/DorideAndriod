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
import com.carlt.sesame.utility.MyTimeUtil;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 获取抓拍文件列表信息
 * 
 * @author @Y.yun
 */
public class RecorderGetCaptureFileInfoListParser extends RecorderBaseParserNew<PieDownloadListInfo> {

	public static final String DEFAULT_TIME = "1970-01-01 00:00:00";
	public static final int CPATURE = 1;
	public static final int TYPE_VIDEO = 0;
	private static int lastType = TYPE_VIDEO; // 上次请求的文件类型

	private static String lastTime = "";
	private String beginTime = "";
	private String endTime = "";
	private int type = TYPE_VIDEO;
	
	int num = 0;
	
	private int mSqnumpic;

	/**
	 * @param listener
	 * @param beginTime
	 * @param isContinue
	 *            是否接着上次请求 默认先请求三次从视频开始，完了后自动开始请求图片
	 */
	public RecorderGetCaptureFileInfoListParser(BaseParser.ResultCallback listener, String beginTime, boolean isContinue) {
		super(listener, PieDownloadListInfo.class);
		MSG_FAIL = "获取文件列表信息失败";
		MSG_SUCC = "获取文件列表信息成功";
		mRequestID = ActionConfig.MID_SEARCH_FILE;
		// 搜索结束时间，为当前时间
		this.endTime = MyTimeUtil.ENDTIME;
		this.beginTime = DEFAULT_TIME;

		if (isContinue) {
			this.beginTime = lastTime;
			this.type = lastType;
		} else {
			if (beginTime != null) {
				this.beginTime = beginTime;
			}
		}
	}

	@Override
	public void run() {
		if (!DeviceConnectManager.isDeviceConnect()) {
			Erro(MSG_ERRO);
			return;
		}

		try {
			long start = System.currentTimeMillis();
			Log.i(TAG, "开始时间:" + start);
				mSqnum = ActionConfig.getSeqNum();
				long result = AppsdkUtils.CGetFilelist(DEFAULT_TIME, endTime, CPATURE, mSqnum);// 文件类型，事件类型
				if (result != 0) {
					Erro(MSG_ERRO2);
					return;
				}
				readCaptureResult();
				// 如果数目等于64说明还有,最多请求3次
				SystemClock.sleep(100);
			Log.i(TAG, "开始时间:" + start);
				mSqnumpic = ActionConfig.getSeqNum();
				long result2 = AppsdkUtils.CGThumbnail(DEFAULT_TIME, endTime, CPATURE, mSqnumpic);// 文件类型，事件类型
				if (result2 != 0) {
					Erro(MSG_ERRO2);
					return;
				}
				
			readCaptureResult();
				// 如果数目等于64说明还有,最多请求3次
			Log.i(TAG, "结束时间:" + System.currentTimeMillis());
			Log.i(TAG, "共用时间:" + (System.currentTimeMillis() - start));
			mBaseResponseInfo.setFlag(BaseResponseInfo.SUCCESS);
			mBaseResponseInfo.setInfo(MSG_SUCC);

			// 记录最后一次的开始时间，请求的类型
			lastTime = beginTime;
			lastType = type;
			Success(mBaseResponseInfo);
		} catch (Exception ex) {
			Log.e(TAG, "Socket发送消息失败=" + ex);
			Erro(MSG_ERRO2);
		}

	}

	@Override
	protected void parser() throws Exception {}

	public void readCaptureResult() {
		long currentMills = System.currentTimeMillis();
		boolean isTimeout = false;
		byte[] result = null;
		boolean ok = false;

		while (!isTimeout && !ok) {
			if (System.currentTimeMillis() - currentMills > SOTIMEOUT) {
				isTimeout = true;
			}
			synchronized (DeviceConnectManager.Buffers) {
			// 没有读取超时
				for (int i = 0; i < DeviceConnectManager.Buffers.size(); i++) {
					byte[] temp = DeviceConnectManager.Buffers.get(i);
					short action = BitConverter.littleEndianReadShort(temp, 1);
					if (action == ActionConfig.MID_SEARCH_FILE_RES) {
						result = temp;
						ok = analysis(result);
						if (ok) {
							DeviceConnectManager.Buffers.remove(result);
						} else {
							result = null;
							// 没找到 未超时 休息两毫秒 接着找
							SystemClock.sleep(2);
						}
					}
				}
			}

			if (isTimeout) {
				DeviceConnectManager.Buffers.clear();
				break;
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
			if (seqNum == mSqnum || seqNum == mSqnumpic) {
				int iRet = mJson.getInt("iRet");
				if (iRet == 100) {
					num = mJson.getInt("currentNum");
				}

				if (num > 0) {
					JSONArray array = mJson.getJSONArray("list");
					String account = UseInfoLocal.getUseInfo().getAccount();
					String deviceString = PieInfo.getInstance().getDeviceName();
					int len = array.length();
					for (int i = 0; i < len; i++) {
						JSONObject obj1 = array.getJSONObject(i);
						String fileName = obj1.getString("filename");
						
						
						String bgTime = obj1.getString("startTime");
						String endTime = obj1.getString("endTime");
						int fileLen = obj1.getInt("fileLength");
						int fileNo = obj1.getInt("fileNo");
						long streamType = obj1.getLong("streamType");
						long recordType = obj1.getLong("recordType");
						long minRecordType = obj1.getLong("minRecordType");
						PieDownloadInfo info = new PieDownloadInfo();
						info.setFileSrcName(fileName);
						info.setStartTime(bgTime);
						info.setEndTime(endTime);
						info.setFileNo(fileNo);
						info.setFileName(StringUtils.spliteTime(fileName));
						info.setStreamType(streamType);
						info.setRecordType(recordType);
						info.setMinRecordType(minRecordType);
						info.setType(streamType == TYPE_VIDEO ? PieDownloadInfo.TYPE_H264 : PieDownloadInfo.TYPE_JPG);
						info.setStoreType(PieDownloadInfo.STORE_CAPTURE);
						info.setTotalLen(fileLen);
						info.setAccout(account);
						info.setDeviceName(deviceString);

						mBaseResponseInfo.getArrays().add(info);
						if (i == len - 1) {
							beginTime = endTime;
						}
					}
				}

				return true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return false;
	}

	// @Override
	// protected long sendMsg() {
	// return Appsdk.CGetFilelist(beginTime, endTime, type, 2);
	// }

	@Override
	protected String creatPost() {
		return null;
	}

}
