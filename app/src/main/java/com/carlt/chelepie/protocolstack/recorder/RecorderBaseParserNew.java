package com.carlt.chelepie.protocolstack.recorder;

import android.os.SystemClock;


import com.carlt.chelepie.manager.DeviceConnectManager;
import com.carlt.chelepie.systemconfig.ActionConfig;
import com.carlt.chelepie.utils.BitConverter;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.utils.Log;
import com.carlt.doride.utils.MyParse;
import com.carlt.sesame.control.CPControl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class RecorderBaseParserNew<T extends BaseResponseInfo> extends Thread {
	protected HashMap<String, String> mMap = new HashMap<String, String>();
	public final static String MSG_ERRO = "未能连接到设备";
	public final static String MSG_ERRO2 = "请求失败！";
	public final static String MSG_ERRO3 = "网络问题！";
	public final static int NOT_FOUND = 0x10;

	/** 搜索事件，抓拍文件默认 开始时间 */
	public final static String DEFAULT_STIME = "1970-01-01 00:00:00";
	public String MSG_FAIL = "开启直播失败";
	public String MSG_SUCC = "开启直播成功";
	protected final static long SOTIMEOUT = 15000;// 等待返回超时时间

	protected long SOTIMEOUT2 = -1;
	protected T mBaseResponseInfo;
	protected JSONObject mJson;
	protected int mSqnum = 0;
	private BaseParser.ResultCallback listener;
	public short mRequestID = 0;
	public short mResponseID = 0;
	public String TAG = "LDEBUG||" + this.getClass().getName();

	RecorderBaseParserNew(BaseParser.ResultCallback listener, Class<T> mClass) {
		this.listener = listener;
		mSqnum = ActionConfig.getSeqNum();
		try {
			mBaseResponseInfo = mClass.newInstance();
		} catch (Exception e) {
			Log.e("info", "初始化mBaseResponseInfo出错");
		}
	}

	@Override
	public void run() {
		if (!DeviceConnectManager.isDeviceConnect()) {
			mBaseResponseInfo.setFlag(BaseResponseInfo.ERRO);
			mBaseResponseInfo.setInfo(MSG_ERRO);
			Erro(mBaseResponseInfo);
			return;
		}
		try {
			// 发送命令
			long flag = sendMsg();
			if (flag != 0) {
				mBaseResponseInfo.setFlag(BaseResponseInfo.ERRO_DISCONNECT);
				mBaseResponseInfo.setInfo(MSG_ERRO3);
				Erro(mBaseResponseInfo);
				return;
			}
		} catch (Exception e1) {
			Log.e("info", "Socket发送消息失败=" + e1);
			mBaseResponseInfo.setFlag(BaseResponseInfo.ERRO);
			mBaseResponseInfo.setInfo(MSG_ERRO);
			Erro(mBaseResponseInfo);
			return;
		}

		try {
			byte[] result = null;
			result = readResult(mRequestID);
			if (result == null || result[0] == NOT_FOUND) {
				Erro(MSG_ERRO2);
				return;
			}

			if (result[0] != 0) {
				Erro(MSG_ERRO3);
				return;
			}
			
			// 得到结果
			String s1 = new String(new String(result, 3, result.length - 3));
			Log.e("info", "Socket接收到的数据==" + s1);
			mJson = new JSONObject(s1);
			mBaseResponseInfo.setFlag(BaseResponseInfo.SUCCESS);
			mBaseResponseInfo.setInfo("OK");
			
		} catch (Exception e) {
			Log.e("info", "Socket接收数据出错==" + e);
			mBaseResponseInfo.setFlag(BaseResponseInfo.ERRO);
			mBaseResponseInfo.setInfo("解析出错");
		}
		
		try {
			parser();
		} catch (Exception e) {
			mBaseResponseInfo.setFlag(BaseResponseInfo.ERRO);
			mBaseResponseInfo.setInfo("解析出错");
			Log.e("http", "解析错误--e==" + e);
		}
		
		if (mBaseResponseInfo.getFlag() == BaseResponseInfo.SUCCESS) {
			Success(mBaseResponseInfo);
		} else {
			Erro(mBaseResponseInfo);
		}
	}

	protected abstract void parser() throws Exception;

	protected abstract String creatPost();

	/**
	 * 通用发送控制命令
	 * 
	 * @return
	 */
	protected long sendMsg() {
		return -1;
	}

	protected void Success(BaseResponseInfo mBaseResponseInfo) {
		Log.e("info", "Success");
		if (listener == null) {
			return;
		}
		listener.onSuccess(mBaseResponseInfo);
	}

	protected void Success(String msg) {
		Log.e("info", "Success");
		mBaseResponseInfo.setFlag(BaseResponseInfo.SUCCESS);
		mBaseResponseInfo.setInfo(msg);
		Success(mBaseResponseInfo);
	}

	protected void Erro(BaseResponseInfo mBaseResponseInfo) {
		Log.e("info", "Erro");
		if (listener == null) {
			return;
		}
		listener.onError(mBaseResponseInfo);
	}

	protected void Erro(String msg) {
		Log.e("info", "Erro");
		mBaseResponseInfo.setFlag(BaseResponseInfo.ERRO);
		mBaseResponseInfo.setInfo(msg);
		Erro(mBaseResponseInfo);
	}

	
	protected String CreatPost(HashMap<String, String> mMap) {
		Iterator<Map.Entry<String, String>> iter = mMap.entrySet().iterator();
		JSONObject mObject = new JSONObject();
		while (iter.hasNext()) {
			Map.Entry<String, String> entry = iter.next();
			String key = entry.getKey().toString();
			String value = entry.getValue().toString();
			try {
				mObject.put(key, value);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		byte[] b = null;
		String s = mObject.toString();

		return s;

	}

	protected void getErroCode() {

		String errcode;
		Log.e("DEBUG","记录仪返回："+ mJson.toString());
		if (mJson != null) {
			// 有返回值的
			errcode = mJson.optString("iRet");
		} else {
			// 没有返回值的默认成功
			errcode = "0";
		}
		if (errcode != null && errcode.length() > 0) {
			int errcode_int = MyParse.parseInt(errcode);
			if (errcode_int == BaseResponseInfo.RECORD_OK || errcode_int == BaseResponseInfo.RECORD_RESTART) {// 成功，或成功要重启
				mBaseResponseInfo.setFlag(BaseResponseInfo.SUCCESS);
				mBaseResponseInfo.setInfo("OK");
			} else {
				mBaseResponseInfo.setFlag(BaseResponseInfo.ERRO);
				mBaseResponseInfo.setInfo("操作失败");
			}
		} else {
			mBaseResponseInfo.setFlag(BaseResponseInfo.ERRO);
			mBaseResponseInfo.setInfo("获取设备消息返回值失败...");
		}

	}

	protected boolean getJsonBoolean(String key) {
		String b = mJson.optString(key);
		if (b.equals("1")) {
			return true;
		} else {
			return false;
		}
	}

	protected String setJsonBoolean(boolean b) {
		if (b) {
			return "1";
		} else {
			return "0";
		}
	}

	/**
	 *  通过iRet判断 相应结果是否正确
	 * @param buf
	 */
	public boolean isCommandOk(byte[] buf) {
		try {
			mJson = new JSONObject(new String(buf, 3, buf.length - 3));
			int errcode = mJson.optInt("iRet");
			if (ActionConfig.RetCode.OK == errcode) {
				return true;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		return false;
	}
	/**
	 * 从返回命令池中查找结果
	 * 
	 * @param res
	 * @return 未找到或超过10s 返回null 找到返回结果
	 */
	public byte[] readResult(short res) {
		long currentMills = System.currentTimeMillis();
		boolean isTimeout = false;
		boolean flag = false;
		byte[] result = null;

		long timeOut = SOTIMEOUT2 == -1 ? SOTIMEOUT : SOTIMEOUT2;
		while (DeviceConnectManager.isDeviceConnect() && !isTimeout) {

			if (System.currentTimeMillis() - currentMills > timeOut) {
				isTimeout = true;
			}
			synchronized (DeviceConnectManager.Buffers) {
				// 没有读取超时
				for (int i = 0; i < DeviceConnectManager.Buffers.size(); i++) {
					try {
						byte[] temp = DeviceConnectManager.Buffers.get(i);
						int index = 1;
						short action = BitConverter.littleEndianReadShort(temp, index);
						String str = new String(temp, 3, temp.length - 3);
						JSONObject job = new JSONObject(str);
						int seqNum = job.optInt("seqNum");
						if (action == res + 1 && seqNum == mSqnum) {
							result = temp;
							flag = true;
							DeviceConnectManager.Buffers.remove(temp);
							break;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					flag = false;
				}
			}
			if (flag || isTimeout) {
				break;
			}
				// 没找到 未超时 休息两毫秒 接着找
				SystemClock.sleep(4);
		}

		if (!flag || isTimeout) {
			DeviceConnectManager.Buffers.clear();
			return new byte[] { NOT_FOUND };
		}
		return result;
	}

}
