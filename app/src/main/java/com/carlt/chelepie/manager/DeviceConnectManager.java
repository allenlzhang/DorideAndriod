package com.carlt.chelepie.manager;

import android.util.Log;


import com.carlt.chelepie.appsdk.AppsdkUtils;
import com.carlt.chelepie.control.RecorderControl;
import com.carlt.chelepie.control.WIFIControl;
import com.carlt.chelepie.data.recorder.PieInfo;
import com.carlt.chelepie.download.PieDownloadControl;
import com.carlt.chelepie.systemconfig.ActionConfig;
import com.carlt.chelepie.utils.BitConverter;
import com.carlt.sesame.control.CPControl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Vector;

/**
 * 
 * 车乐拍连接管理类
 * 
 * @author @Y.yun
 * 
 */
public class DeviceConnectManager implements Runnable {

	private static boolean mIsConnect = false;// 是否连接成功
	private static int RESPONSE_COUNT = 60;// 保留的请求回复个数
	public static boolean mIsWIFIConnect = false;// 是否连接上车乐拍WIFI
	public static boolean mIsThreadRunning = false;
	public static WIFIControl mWifiControl = WIFIControl.getInstance();
	private static DeviceConnectManager mInstance = new DeviceConnectManager();
	// public static final String CONNECT_STR = "192.168.1.32:34567";
	public static String s_test = "192.168.1.32:34567";
	public static String s_normal = "172.20.120.1:34567";
	public static String CONNECT_STR = s_normal;
	public static final Vector<byte[]> Buffers = new Vector<byte[]>();// 消息缓存
	public static final int DEVICE_DISCONNECT = 1001;// 设备连接断开
	public static final int DEVICE_CONNECT = 1002;// 设备连接成功
	public static final int DEVICE_CONNECT_TIMEOUT = 1004;// 连接设备三次
	public static final int DEVICE_CONNECT_LOGINERR = 1005;// 登陆出错，有人已连接
	private static ArrayList<NotifyListener> mListeners = new ArrayList<NotifyListener>();
	public static Thread mCurrent = null;

	// 超时取消掉的Action

	private DeviceConnectManager() {
	}

	// 通知接口
	public static interface NotifyListener {
		void notifyAction(int action);

	}

	public static void addNotifyListener(NotifyListener listener) {
		if (!mListeners.contains(listener)) {
			mListeners.add(listener);
		}
	}

	public static void removeNotifyListener(NotifyListener listener) {
		mListeners.remove(listener);
	}

	private static void notifyListeners(int action) {
		for (int i = 0; i < mListeners.size(); i++) {
			mListeners.get(i).notifyAction(action);
		}
	}

	@Override
	public void run() {
		mIsThreadRunning = true;
		long currentMills = 0;
		int times = 0;
		if (!mWifiControl.isConnectCheLe()) {
			notifyListeners(DEVICE_DISCONNECT);
			mIsThreadRunning = false;
			return;
		}

		while (mIsThreadRunning && mWifiControl != null && mWifiControl.isConnectCheLe()) {
			try {
					long result;
					if (!mIsConnect) {
						currentMills = System.currentTimeMillis();
						result = AppsdkUtils.InitCtrlSocket(CONNECT_STR);
						Log.e("C_DEBUG", "网络连接建立消耗时间:" + (System.currentTimeMillis() - currentMills) + "毫秒");
						if (result != 0) {
							mIsConnect = false;
							if (times < 2) {
								Thread.sleep(100);
								times++;
								continue;
							} else {
								mIsThreadRunning = false;
								notifyListeners(DEVICE_CONNECT_TIMEOUT);
								return;
							}
						}

						// 连接建立成功后必须先登录
						if (!loginPie()) {
							mIsConnect = false;
							mIsThreadRunning = false;
							notifyListeners(DEVICE_CONNECT_LOGINERR);
							AppsdkUtils.CloseCtrlSocket();
							return;
						}
						
						RecorderControl.setRecorderTime(listener_time, System.currentTimeMillis());
						/// ---获取系统信息，版本号....设备ID....
						if (!getSysInfo()) {
							mIsConnect = false;
							mIsThreadRunning = false;
							notifyListeners(DEVICE_CONNECT_TIMEOUT);
							AppsdkUtils.CloseCtrlSocket();
							return;
						}

						// 开启心跳
						AppsdkUtils.StartHeartBeat();
						mIsConnect = true;

						// 连接成功--将连接成功的WIFI名称保存下来
						WIFIControl.SaveConnectWIFI();
//						AppsdkUtils.CSetTime(System.currentTimeMillis(),0, ActionConfig.getSeqNum());//系统时间
						// 将下载失败的添加进下载列表里面去
//						PieDownloadControl.AddStopInfoToDownloadList();
//						PieDownloadControl.PrintInfos();
						
						ThumbnailManager.startCheckThumbnail();
//						PieDownloadControl.playback_StartDonwloadThumbnail();
//						PieDownloadControl.media_StartDownload();
						notifyListeners(DEVICE_CONNECT);
					}

				int index = 0;
				byte[] buf = AppsdkUtils.ReadCtrlMsg();
				index++;

				short action = BitConverter.littleEndianReadShort(buf, index);
				index = index + 2;
				// 加入缓存
				Buffers.add(buf);
				Log.e("DEBUG||Action_result:", action + "<<<<<<<<");
				Log.e("DEBUG||Net_Receive:", new String(buf, index, buf.length - 3));

				synchronized (Buffers) {
					// 最多保留60个 , 清除多余未处理的
					if (Buffers.size() > RESPONSE_COUNT) {
						int count = Buffers.size() - RESPONSE_COUNT;
						for (int i = 0; i < count; i++) {
							Buffers.remove(Buffers.lastElement());
						}
					}
				}

			} catch (Exception e) {
				mIsThreadRunning = false;
				notifyListeners(DEVICE_CONNECT_TIMEOUT);
				return;
			}
		}

		// PieDownloadControl.playback_StopDonwloadThumbnail();
		// PieDownloadControl.media_StopMediaDownload();

		// 连接断开提醒
		Buffers.clear();
		PieDownloadControl.MakeAllLoadingStop();
		PieDownloadControl.StopPrint();
		
//		Appsdk.CloseStreamSocket();
//		Appsdk.CloseThumbnailSocket();
//		Appsdk.CloseMonitorSocket();
		
		AppsdkUtils.closeAllSClient();
		
		AppsdkUtils.CloseCtrlSocket();
		notifyListeners(DEVICE_DISCONNECT);
		mIsThreadRunning = false;
		mIsConnect = false;
	}
	
	private CPControl.GetResultListCallback listener_time = new CPControl.GetResultListCallback() {

		@Override
		public void onFinished(Object o) {
		}

		@Override
		public void onErro(Object o) {
		}
	};

	/**
	 * 设备登录流程
	 * 
	 * @return
	 */
	private boolean loginPie() {
		// 请求登录
		long result = AppsdkUtils.CLogin("Carlt", "Carlt12345",1);
		if (result != 0) {
			return false;
		}

		byte[] lresult = AppsdkUtils.ReadCtrlMsg();
		int index = 0;
		result = lresult[index];
		index++;
		if (result != 0) {
			return false;
		}
		// short action = BitConverter.littleEndianReadShort(lresult, index);
		index += 2;
		// 是否登录成功
		String json = new String(lresult, index, lresult.length - 3);
		try {
			JSONObject mJObject = new JSONObject(json);
			int code = mJObject.getInt("iRet");
			Log.e("info", "chelerpie  login code 。。。" + code);
			return ActionConfig.RetCode.OK == code;
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return false;
	}

	private boolean getSysInfo() {
		boolean flag = false;
		try {
			long result = AppsdkUtils.CGetSystemInfo(ActionConfig.getSeqNum());
			if (result == 0) {
				byte[] bufs = AppsdkUtils.ReadCtrlMsg();
				if (bufs[0] == 0) {
					JSONObject mJson = new JSONObject(new String(bufs, 3, bufs.length - 3));
					int ok = mJson.getInt("iRet");
					if (ok == 100) {
						flag = true;
						String buildTime = mJson.getString("buildTime");
						String serialNo = mJson.getString("serialNo");
						String serialNo2 = null;
						if(serialNo.length()>16){
							 serialNo2 = serialNo.substring(0, 16);
						}else{
							serialNo2 = serialNo;
						}
						String softVersion = mJson.getString("softVersion");
						String hardVersion = mJson.getString("hardVersion");
						String encryptVersion = mJson.getString("encryptVersion");
						
						PieInfo.getInstance().setBuildTime(buildTime);
						PieInfo.getInstance().setDeviceName(serialNo2);
						PieInfo.getInstance().setSoftVersion(softVersion);
						PieInfo.getInstance().setHardVersion(hardVersion);
						PieInfo.getInstance().setEncryptVersion(encryptVersion);
						return flag;
					}
					Log.i("DeviceConnectManager", "获取版本号信息 " + new String(bufs, 3, bufs.length - 3));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return flag;
	}

	public static boolean isDeviceConnect() {
		return mIsConnect = mIsConnect && mCurrent != null && mCurrent.isAlive();
	}

	public static void Exit() {
		long cLogout = AppsdkUtils.CLogout();
		AppsdkUtils.CloseCtrlSocket();
		mIsConnect = false;
	}

	public static void StartMessgeLoop() {
		if (mIsConnect) {
			notifyListeners(DEVICE_CONNECT);
			return;
		}

		synchronized (DeviceConnectManager.class) {
//			if (!mIsThreadRunning || mCurrent == null || !mCurrent.isAlive()) {
			if (!mIsThreadRunning ) {
				mCurrent = new Thread(mInstance);
				mCurrent.start();
			}
		}
	}

	public static void remove(byte[] temp) {
		synchronized (Buffers) {
			Buffers.remove(temp);
		}
	}
}