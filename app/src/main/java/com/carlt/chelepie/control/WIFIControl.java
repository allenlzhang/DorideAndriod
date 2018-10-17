package com.carlt.chelepie.control;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.SystemClock;
import android.util.Log;

import com.carlt.chelepie.appsdk.AppsdkUtils;
import com.carlt.chelepie.data.recorder.CameraWifiInfo;
import com.carlt.doride.DorideApplication;
import com.carlt.doride.data.UseInfo;
import com.carlt.doride.preference.UseInfoLocal;
import com.carlt.doride.utils.StringUtils;
import com.carlt.sesame.utility.ObjectIO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WIFIControl {
	// 定义一个WifiManager对象
	private static WifiManager mWifiManager;
	// 当前连接的信息
	private WifiInfo mCurrentWifiInfo;
	// 范围内列表
	private List<ScanResult> mScanResultList;
	// 保存列表
	private List<WifiConfiguration> mWifiConfigurationList;
	private static WIFIControl mWIFIControl;
	public static final String chelepiePrefix = "A3-";
//	public static final String chelepiePrefix = "Domy-DVR-";
	// 当前需要连接的摄像头信息
	private CameraWifiInfo mCameraWifiInfo;
	private static final String TAG = "DEBUG||WIFIControl";
	private final static String bindCameraInfoPath = DorideApplication.ApplicationContext.getCacheDir().getAbsolutePath() + "/defaultCarmera.ini";
	public static String SSID_CONNECT = "";
	public static String SSID_PWD = "";

	public static WifiReceiver mWifiReceiver = new WifiReceiver();

	public static final int WIFI_NOT_OPEN = 10011;// WIFI 打开失败
	public static final int WIFI_CONNECT_OK = 10012;// WIFI连接成功
	public static final int WIFI_CONNECT_TIMEOUT = 10013;// WIFI连接三次都没连接上,提醒手动连接
	public static final int WIFI_CHELE_DISCONNECT = 10014;// WIFI断开连接
	public static final int WIFI_CHELE_PWD_ERROR = 10015;// WIFI密码错误
	public static final int WIFI_NOT_FOUND = 10016;// WIFI连接没发现
	public static final int WIFI_NO_INFO = 10017;// 本地没有WIFI名称或者密码
	public static boolean mIsFoundWIFI = false;
	
	/** 尝试连接wifi次数 */
	private static  int tryConnectCount = 0;
	
	/**
	 * 登录后是否已经连接过车乐拍
	 */
	private static boolean isconnect_chele_wifi = false;

	public static List<WIFIConnectListener> mConnectListeners = Collections.synchronizedList(new ArrayList<WIFIConnectListener>());
	
	/**
	 * 连接车乐拍wifi的循环
	 */
	public static boolean isWIFIConnectRuning = false;
	/**
	 * 断开车乐拍wifi的循环
	 */
	public static boolean isWIFIDisConnectRuning = false;
	private static Connector mConnector;
	public static boolean isPwdError = false;

	private WIFIControl() {
		IntentFilter mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
		DorideApplication.ApplicationContext.registerReceiver(mWifiReceiver, mIntentFilter);

		UseInfo uInfo = UseInfoLocal.getUseInfo();
		if (uInfo != null && !StringUtils.isEmpty(uInfo.getAccount())) {
			SharedPreferences sharp = DorideApplication.ApplicationContext.getSharedPreferences("wifi_info", 0);
			SSID_CONNECT = sharp.getString(uInfo.getAccount() + "_n", null);
			SSID_PWD = sharp.getString(uInfo.getAccount() + "_p", null);
		}
	}

	public List<ScanResult> getmScanResultList() {
		return mScanResultList;
	}

	public static WIFIControl getInstance() {
		if (mWIFIControl == null) {
			mWIFIControl = new WIFIControl();
		}
		return mWIFIControl;
	}

	public void init(Context context) {
		mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		mCameraWifiInfo = (CameraWifiInfo) ObjectIO.readObject(bindCameraInfoPath);
		startScan();
	}

	// 打开wifi
	public void openWifi() {
		if (!mWifiManager.isWifiEnabled()) {
			mWifiManager.setWifiEnabled(true);
		}
	}

	// 关闭wifi
	public void closeWifi() {
		if (mWifiManager.isWifiEnabled()) {
			mWifiManager.setWifiEnabled(false);
		}
	}

	// 检查当前wifi状态
	public int checkState() {
		return mWifiManager.getWifiState();
	}

	public void startScan() {
		mWifiManager.startScan();
		// 得到扫描结果
		mScanResultList = mWifiManager.getScanResults();
		Log.e("info", "mScanResultList.size==" + mScanResultList.size());
		// 得到配置好的网络连接
		mWifiConfigurationList = mWifiManager.getConfiguredNetworks();
		mCurrentWifiInfo = mWifiManager.getConnectionInfo();
	}

	public boolean ConnectChelePie() {
		return connetionWifi(SSID_CONNECT, SSID_PWD, null);
	}

	public void disConnectChelePie() {
		WifiInfo wInfo = mWifiManager.getConnectionInfo();
		String wifiName = wInfo.getSSID().replace("\"", "");
		if (wifiName.startsWith(chelepiePrefix)) {
			mWifiManager.removeNetwork(wInfo.getNetworkId());
		}
	}

	public void EditSSid(String ssid) {
		Log.e("info", "ssid==" + ssid);
		if (ssid != null) {
			UseInfo uInfo = UseInfoLocal.getUseInfo();
			if (uInfo != null && !StringUtils.isEmpty(uInfo.getAccount())) {
				SharedPreferences sharp = DorideApplication.ApplicationContext.getSharedPreferences("wifi_info", 0);
				sharp.edit().putString(uInfo.getAccount() + "_n", ssid).apply();
			}
		}
	}

	public void Editpwd(String psw) {
		Log.e("info", "ssid==" + psw);
		if (psw != null) {
			UseInfo uInfo = UseInfoLocal.getUseInfo();
			if (uInfo != null && !StringUtils.isEmpty(uInfo.getAccount())) {
				SharedPreferences sharp = DorideApplication.ApplicationContext.getSharedPreferences("wifi_info", 0);
				sharp.edit().putString(uInfo.getAccount() + "_p", psw).apply();
			}
		}
	}

	private void saveBindDevice(CameraWifiInfo mCameraWifiInfo) {
		this.mCameraWifiInfo = mCameraWifiInfo;
		ObjectIO.saveObject(mCameraWifiInfo, bindCameraInfoPath);
	}

	public void saveBindDevice(String ssid, String psw) {
		CameraWifiInfo mCameraWifiInfo = new CameraWifiInfo();
		if (ssid != null && ssid.length() > 0) {
			mCameraWifiInfo.setSsid(ssid);
		}
		if (psw != null && psw.length() > 0) {
			mCameraWifiInfo.setPsw(psw);
		}
		saveBindDevice(mCameraWifiInfo);
	}

	public static void SaveConnectWIFI() {
		WifiInfo wInfo = mWifiManager.getConnectionInfo();
		if (wInfo != null) {
			String wname = wInfo.getSSID().replace("\"", "");
			mWIFIControl.EditSSid(wname);
			mWIFIControl.Editpwd(SSID_PWD);
		}
	}

	/**
	 * 
	 * @param wifiname
	 * @param pwd
	 * @param bssid
	 * @return 指定网络是否在附近 w
	 */
	private boolean connetionWifi(String wifiname, String pwd, String bssid) {
		openWifi();
		startScan();
		if (mCurrentWifiInfo != null && mCurrentWifiInfo.getSSID().equals("\"" + wifiname + "\"")) {
			return true;
		}
		if (mScanResultList == null) {
			return false;
		}

		List<WifiConfiguration> mWcfs = mWifiManager.getConfiguredNetworks();
		for (WifiConfiguration wc : mWcfs) {
			if (wc.SSID.replace("\"", "").equals(wifiname)) {
				mWifiManager.removeNetwork(wc.networkId);
			}
		}

		Log.e("info", "wifiname==" + wifiname);
		for (int i = 0; i < mScanResultList.size(); i++) {
			ScanResult mScanResult = mScanResultList.get(i);
			Log.e("info", "mScanResult.SSID==" + mScanResult.SSID);
			if (mScanResult != null && mScanResult.SSID.equals(wifiname)) {
				int wifiId = -1;
				WifiConfiguration wc = new WifiConfiguration();
				wc.allowedAuthAlgorithms.clear();
				wc.allowedGroupCiphers.clear();
				wc.allowedKeyManagement.clear();
				wc.allowedPairwiseCiphers.clear();
				wc.allowedProtocols.clear();
				if (bssid != null) {
					if (bssid.equals(mScanResult.BSSID)) {
						wc.BSSID = "\"" + bssid + "\"";
					} else {
						continue;
					}
				}
				wc.SSID = "\"" + wifiname + "\"";
				if (pwd == null || pwd.length() <= 0) {
					wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
					wc.wepTxKeyIndex = 0;
				} else {
					wc.preSharedKey = "\"" + pwd + "\"";
					wc.status = WifiConfiguration.Status.ENABLED;
				}

				wifiId = mWifiManager.addNetwork(wc);
				Log.e("info", "wifiId==" + wifiId);
				// if (wifiId > -1) {
				// mIsFoundWIFI = true;
				mWifiManager.enableNetwork(wifiId, true);
				return true;
				// }
			}
		}

		mIsFoundWIFI = false;
		return false;
	}

	/**
	 *  连接日常用的wifi
	 * @return
	 */
	public boolean connetionOffenUse() {
		openWifi();
		startScan();
		if (mScanResultList == null) {
			return false;
		}

		List<WifiConfiguration> mConfigs = mWifiManager.getConfiguredNetworks();
		for (int i = 0; i < mScanResultList.size(); i++) {
			ScanResult mScanResult = mScanResultList.get(i);
			Log.e("info", "mScanResult.SSID==" + mScanResult.SSID);
			if (mScanResult != null && !mScanResult.SSID.replace("\"", "").startsWith(chelepiePrefix) && mConfigs.size() > 0) {
				for (int j = 0; j < mConfigs.size(); j++) {
					WifiConfiguration wc = mConfigs.get(j);
					if (wc.SSID.replace("\"", "").equals(mScanResult.SSID.replace("\"", ""))) {
						mWifiManager.enableNetwork(wc.networkId, true);
						return true;
					}
				}
			}
		}

		return false;
	}

	/**
	 * 是否连接车乐拍的wifi
	 * @return
	 */
	public boolean isConnectCheLe() {

		if (mWifiManager.isWifiEnabled()) {
			WifiInfo wInfo = mWifiManager.getConnectionInfo();
			if (wInfo != null && wInfo.getIpAddress() != 0) {
				String wifiName = wInfo.getSSID().replace("\"", "");
				if (wifiName.startsWith(chelepiePrefix)) {
					return true;
				}
			}
		}

		return false;
	}

	public CameraWifiInfo getmCameraWifiInfo() {
		return mCameraWifiInfo;
	}

	public static interface WIFIConnectListener {
		public void onWIFIChange(int action);
	};

	/**
	 * WIFI是否可用
	 * 
	 * @return
	 */
	public static boolean isWifiEnalble() {
		return mWifiManager.isWifiEnabled();
	}

	/**
	 * 使所有车乐拍连接不可用
	 */
	private static final void disableChelepai() {
		try {
			for (int i = 0; i < mWifiManager.getConfiguredNetworks().size(); i++) {
				WifiConfiguration configuration = mWifiManager.getConfiguredNetworks().get(i);
				if (configuration.SSID.replace("\"", "").startsWith(chelepiePrefix)) {
					int id = configuration.networkId;
					mWifiManager.removeNetwork(id);
				}
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 使车乐拍WIFI可用
	 */

	/**
	 * 当程序退出时 调用
	 */
	public static final void exitWifiControl() {
		DorideApplication.ApplicationContext.unregisterReceiver(mWifiReceiver);
		mWIFIControl = null;
	}

	/**
	 * 连接车乐拍
	 */
	public static synchronized void StartConnectChelePai() {
		if (!isWIFIConnectRuning) {
			Log.e("onResume", "开始连接设备WIFI");
			mConnector = new Connector();
			mConnector.start();
		}
	}

	/**
	 * 断开车乐拍链接
	 */
	public static synchronized void DisConnectChelePai() {
		if (!isWIFIDisConnectRuning) {
			new DisConnect().start();
		}
	}

	/**
	 * 连接车乐拍
	 * 
	 * @author @Y.yun
	 * 
	 */
	public static class Connector extends Thread {

		public static final int STATE_DIS = 0;
		public static final int STATE_COMPLETE = 1;
		public static final int STATE_PWD_ERROR = 2;

		public int STATE = -1;

		@Override
		public void run() {
			isWIFIConnectRuning = true;
			isPwdError = false;
			Log.e("WIFI_INFO", "1  ____" + SSID_CONNECT + "||" + SSID_PWD);
			if (mWIFIControl == null) {
				isWIFIConnectRuning = false;
				notifyWIFIState(WIFI_NOT_OPEN);
				return;
			}

			if (!mWifiManager.isWifiEnabled()) {
				isWIFIConnectRuning = false;
				notifyWIFIState(WIFI_NOT_OPEN);
				return;
			}

			if (mWIFIControl.isConnectCheLe()) {
				isWIFIConnectRuning = false;
				isconnect_chele_wifi = true;
				notifyWIFIState(WIFI_CONNECT_OK);
				tryConnectCount = 0;
				return;
			}

			String tmp = null;
			String pwd = null;

			UseInfo uInfo = UseInfoLocal.getUseInfo();
			if (uInfo != null && !StringUtils.isEmpty(uInfo.getAccount())) {
				SharedPreferences sharp = DorideApplication.ApplicationContext.getSharedPreferences("wifi_info", 0);
				tmp = sharp.getString(uInfo.getAccount() + "_n", null);
				pwd = sharp.getString(uInfo.getAccount() + "_p", null);
			}

//			if (tmp == null || tmp.length() < 1 || pwd == null || pwd.length() < 1) {
			if (StringUtils.isEmpty(tmp) || StringUtils.isEmpty(pwd)) {
				if(StringUtils.isEmpty(SSID_CONNECT) || StringUtils.isEmpty(SSID_PWD)){
					isWIFIConnectRuning = false;
					notifyWIFIState(WIFI_NO_INFO);
					return;
				}else{
					WIFIControl.getInstance().Editpwd(SSID_PWD);
					WIFIControl.getInstance().EditSSid(SSID_CONNECT);
				}
			} else {
				SSID_CONNECT = tmp;
				SSID_PWD = pwd;
			}

			try {
				boolean isFound = mWIFIControl.ConnectChelePie();
				if (!isFound) {
					// --没找到要链接的WIFI
					isWIFIConnectRuning = false;
					notifyWIFIState(WIFI_NOT_FOUND);
					tryConnectCount++;
					if(tryConnectCount > 2){
					SSID_CONNECT = "";
					SSID_PWD = "";
					WIFIControl.getInstance().Editpwd("");
					WIFIControl.getInstance().EditSSid("");
					}
					return;
				}

				Log.e("WIFI_INFO", "2  ____" + SSID_CONNECT + "||" + SSID_PWD);
				long currentMills = System.currentTimeMillis();
				while (System.currentTimeMillis() - currentMills < 15 * 1000) {

					WifiInfo wInfo = mWifiManager.getConnectionInfo();
					SupplicantState state = wInfo.getSupplicantState();
					if (!wInfo.getSSID().replace("\"", "").startsWith(chelepiePrefix) && state == SupplicantState.COMPLETED) {
						mWIFIControl.ConnectChelePie();
					}

					if (wInfo.getSSID().replace("\"", "").startsWith(chelepiePrefix)) {
						if (state == SupplicantState.COMPLETED && mWIFIControl.isConnectCheLe()) {
							isconnect_chele_wifi = true;
							notifyWIFIState(WIFI_CONNECT_OK);
							isWIFIConnectRuning = false;
							return;
						} else if (state == SupplicantState.DISCONNECTED) {
							while (System.currentTimeMillis() - currentMills < 10 * 1000) {
								WifiInfo wInfo2 = mWifiManager.getConnectionInfo();
								if (wInfo2 == null || !wInfo2.getSSID().replace("\"", "").startsWith(chelepiePrefix)) {
									SSID_CONNECT = "";
									SSID_PWD = "";
									WIFIControl.getInstance().Editpwd("");
									WIFIControl.getInstance().EditSSid("");
									disableChelepai();
									notifyWIFIState(WIFI_CONNECT_TIMEOUT);
									isWIFIConnectRuning = false;
									return;
								}

								if (isPwdError) {
									disableChelepai();
									SSID_CONNECT = "";
									SSID_PWD = "";
									WIFIControl.getInstance().Editpwd("");
									WIFIControl.getInstance().EditSSid("");
									notifyWIFIState(WIFI_CHELE_PWD_ERROR);
									isWIFIConnectRuning = false;
									return;
								}
								SystemClock.sleep(500);
							}
						}
					}

					SystemClock.sleep(500);

				}
			} catch (Exception e) {
				String msg = e.getMessage();
				Log.e(TAG, msg + "");
			}

			SSID_CONNECT = "";
			SSID_PWD = "";
			disableChelepai();
			notifyWIFIState(WIFI_CONNECT_TIMEOUT);
			isWIFIConnectRuning = false;
		}
	}

	/**
	 *
	 * 断开车乐拍连接
	 * 
	 * @author @Y.yun
	 * 
	 */
	public static class DisConnect extends Thread {

		@Override
		public void run() {
			isWIFIDisConnectRuning = true;
			if (mWIFIControl == null) {
				notifyWIFIState(WIFI_CHELE_DISCONNECT);
				isWIFIDisConnectRuning = false;
				return;
			}

			if (!mWifiManager.isWifiEnabled()) {
				notifyWIFIState(WIFI_CHELE_DISCONNECT);
				isWIFIDisConnectRuning = false;
				return;
			}
			//没有连接车乐拍
			if (!mWIFIControl.isConnectCheLe()) {
				//没连接过车乐拍直接通知断开连接
				if(!isconnect_chele_wifi){
					notifyWIFIState(WIFI_CHELE_DISCONNECT);
				}
				disableChelepai();
				if (mWifiManager.getConnectionInfo().getSupplicantState() != SupplicantState.COMPLETED) {
					if (mWIFIControl.connetionOffenUse()) {
						long mills = System.currentTimeMillis();
						int ok = -1;
						while (System.currentTimeMillis() - mills < 8000) {

							if (System.currentTimeMillis() - mills > 4000 && ok == -1) {
								mWIFIControl.connetionOffenUse();
								ok = 0;
							}

							WifiInfo wInfo = mWifiManager.getConnectionInfo();
							if (wInfo != null && wInfo.getIpAddress() != 0) {
								Log.e("info", "已连接上常用WIFI");
								break;
							}
							try {
								Thread.sleep(400);
							} catch (Exception e) {
							}
						}
					}
				}

				notifyWIFIState(WIFI_CHELE_DISCONNECT);
				isWIFIDisConnectRuning = false;
				return;
			}
			//本身连着车乐拍
			AppsdkUtils.CloseCtrlSocket();
			mWIFIControl.disConnectChelePie();
			long currentMills = System.currentTimeMillis();
			while (System.currentTimeMillis() - currentMills < 6000) {
				if (!mWIFIControl.isConnectCheLe()) {
					Log.e("info", "没有连接设备");
					disableChelepai();
					long mills = System.currentTimeMillis();
					Log.e("info", "连接常用WIFI");
					int ok = -1;
					if (mWIFIControl.connetionOffenUse()) {
						while (System.currentTimeMillis() - mills < 9000) {

							if (System.currentTimeMillis() - mills > 4000 && ok == -1) {
								mWIFIControl.connetionOffenUse();
								ok = 0;
							}

							WifiInfo wInfo = mWifiManager.getConnectionInfo();
							if (wInfo != null && wInfo.getIpAddress() != 0) {
								Log.e("info", "已连接上常用WIFI");
								break;
							}
							SystemClock.sleep(500);
						}
					}
					notifyWIFIState(WIFI_CHELE_DISCONNECT);
					isWIFIDisConnectRuning = false;
					return;
				}

				SystemClock.sleep(500);
			}

			notifyWIFIState(WIFI_CHELE_DISCONNECT);
			isWIFIDisConnectRuning = false;
		}
	}

	public static void notifyWIFIState(int action) {
		for (int i = 0; i < mConnectListeners.size(); i++) {
			mConnectListeners.get(i).onWIFIChange(action);
		}
	}

	public static void rigisterWIFIConnectListener(WIFIConnectListener listener) {
		if (listener == null) {
			return;
		}
		if (!mConnectListeners.contains(listener)) {
			mConnectListeners.add(listener);
		}
	}

	public static void unRigisterWIFIConnectListener(WIFIConnectListener listener) {
		if (listener == null) {
			return;
		}
		mConnectListeners.remove(listener);
	}
	
	public static String getLocalWIFIName() {
		String name = "";
		UseInfo uInfo = UseInfoLocal.getUseInfo();
		if (uInfo != null && !StringUtils.isEmpty(uInfo.getAccount())) {
			SharedPreferences sharp = DorideApplication.ApplicationContext.getSharedPreferences("wifi_info", 0);
			name = sharp.getString(uInfo.getAccount() + "_n", "");
		}
		return name;
	}

	public static class WifiReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context c, Intent intent) {
			String action = intent.getAction();
			if (mWifiManager == null) {
				return;
			}


			if (action.equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)) {

				Log.d("WifiReceiver", ">>>>SUPPLICANT_STATE_CHANGED_ACTION<<<<<<");
				int supl_error = intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, -1);
				if (supl_error == WifiManager.ERROR_AUTHENTICATING) {
					if (!isPwdError) {
						isPwdError = true;
					}
					Log.i("SupplicantState", "ERROR_AUTHENTICATING!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				}
			}
			WifiInfo mInfo = mWifiManager.getConnectionInfo();
			if (mInfo == null) {
				return;
			}
			
			if (!mInfo.getSSID().replace("\"", "").startsWith(chelepiePrefix)) {
				return;
			}
		}
	}

}
