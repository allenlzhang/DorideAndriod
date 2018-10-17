package com.carlt.chelepie.protocolstack.recorder;

import android.os.SystemClock;


import com.carlt.chelepie.appsdk.AppsdkUtils;
import com.carlt.chelepie.control.DaoPieDownloadControl;
import com.carlt.chelepie.data.recorder.BaseResponseInfo;
import com.carlt.chelepie.data.recorder.PieInfo;
import com.carlt.chelepie.manager.DeviceConnectManager;
import com.carlt.chelepie.manager.ThumbnailManager;
import com.carlt.chelepie.systemconfig.ActionConfig;
import com.carlt.chelepie.utils.BitConverter;
import com.carlt.doride.preference.UseInfoLocal;
import com.carlt.doride.utils.LocalConfig;
import com.carlt.doride.utils.Log;
import com.carlt.sesame.control.CPControl;

import org.json.JSONObject;

import java.io.File;

/**
 * *格式化SDCard
 * 
 * @author @Y.yun
 * 
 */
public class RecorderFormatSDParser extends RecorderBaseParserNew<BaseResponseInfo> {

	
	
	private JinduResultListCallback jinduListener;
	public interface JinduResultListCallback {
		void onFinished(int o);
	}
	
	public RecorderFormatSDParser(CPControl.GetResultListCallback listener, Class mClass, JinduResultListCallback jinduListener) {
		super(listener, mClass);
		mRequestID = 1203;
		SOTIMEOUT2 = 1000 * 10;
		this.jinduListener = jinduListener;
		MSG_FAIL = "格式化SD卡失败";
		MSG_SUCC = "格式化SD卡成功";
	}

	@Override
	protected String creatPost() {
		return CreatPost(mMap);
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
			short action =  ActionConfig.MID_FORMATSD_RES;
			boolean flag = true;
			while (flag) {
				result = readResult(action);
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
				int jindu = mJson.getInt("reserve");
				if (jindu == 100) {
					mBaseResponseInfo.setFlag(BaseResponseInfo.SUCCESS);
					mBaseResponseInfo.setInfo("OK");
					jinduListener.onFinished(100);
					flag = false;
					break;
				}else{
					jinduListener.onFinished(jindu);
					continue;
				}
			}
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
						if (action == res) {
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

	@Override
	protected void Success(BaseResponseInfo mBaseResponseInfo) {
		// --删掉无用文件
		DaoPieDownloadControl.getInstance().deleteAllThumbnails();
		ThumbnailManager.mDatas.clear();
		String account = UseInfoLocal.getUseInfo().getAccount();
		String deviceString = PieInfo.getInstance().getDeviceName();
		String path = LocalConfig.GetMediaPath(account, deviceString, LocalConfig.DIR_THUMBNAIL);
		File file = new File(path);
		if (file.exists()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].exists()) {
					files[i].delete();
				}
			}
		}
		super.Success(mBaseResponseInfo);
	}

	@Override
	protected long sendMsg() {
		return AppsdkUtils.CFormatStorage(mSqnum);
	}

	@Override
	protected void parser() throws Exception {
		getErroCode();
	}
}
