/**
 * 
 */
package com.carlt.chelepie.control;

import android.content.Context;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;

import com.carlt.chelepie.manager.DeviceConnectManager;
import com.carlt.chelepie.view.EditDialog2;
import com.carlt.chelepie.view.WIFIConnectDialog;
import com.carlt.chelepie.view.WifiListDialog;
import com.carlt.doride.ui.view.PopBoxCreat;
import com.carlt.doride.ui.view.UUToast;


/**
 *
 * @author @Y.yun
 * 
 */
public class DeviceConnControl implements WIFIControl.WIFIConnectListener, DeviceConnectManager.NotifyListener {

	DeviceConnListener mListener;
	WIFIConnectDialog mWIFIDialog;
	Context mCtx;
	private EditDialog2 mDialog;
	private WifiListDialog mWifiListDialog;
	boolean isShowing = false;

	public DeviceConnControl(DeviceConnListener connListener, Context ctx) {
		this.mListener = connListener;
		this.mCtx = ctx;
		isShowing = true;
	}

	public void goConnect() {
		WIFIControl.StartConnectChelePai();
		showDialog();
	}

	public void onResume() {
		WIFIControl.rigisterWIFIConnectListener(this);
		DeviceConnectManager.addNotifyListener(this);
		isShowing = true;
	}
	int wifiErrCount = 0;
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case DeviceConnectManager.DEVICE_DISCONNECT:
				UUToast.showUUToast(mCtx, "网络发生异常中断，请检查您的网络环境并尝试重新连接");
				dissMissDialog();
				if (mListener != null) {
					mListener.connError();
				}
				break;
			case DeviceConnectManager.DEVICE_CONNECT:
				UUToast.showUUToast(mCtx, "设备连接成功");
				if (mListener != null) {
					mListener.connOk();
				}
				dissMissDialog();
				break;
			case DeviceConnectManager.DEVICE_CONNECT_TIMEOUT:
				UUToast.showUUToast(mCtx, "连接设备超时，请检查您的网络环境并尝试重新连接");
				dissMissDialog();
			case DeviceConnectManager.DEVICE_CONNECT_LOGINERR:
				wifiErrCount++;
				if(wifiErrCount > 1){
					WIFIControl.getInstance().Editpwd("");
				}
				UUToast.showUUToast(mCtx, "未能成功连接您的设备，请检查您的网络环境并尝试重新连接");
				dissMissDialog();
				break;
			case WIFIControl.WIFI_CONNECT_OK:
				DeviceConnectManager.StartMessgeLoop();
				break;
			case WIFIControl.WIFI_NOT_OPEN:
				UUToast.showUUToast(mCtx, "网络链接不可用，请先检查您的Wi-Fi是否已开启");
				dissMissDialog();
				break;
			case WIFIControl.WIFI_NOT_FOUND:
				dissMissDialog();
				UUToast.showUUToast(mCtx, "未发现您的设备，请检查设备是否正确安装");
				break;
			case WIFIControl.WIFI_CONNECT_TIMEOUT:
				UUToast.showUUToast(mCtx, "连接您的设备超时，请检查您的网络环境并尝试重新连接");
				dissMissDialog();
				break;
			case WIFIControl.WIFI_CHELE_PWD_ERROR:
				UUToast.showUUToast(mCtx, "设备Wi-Fi密码错误，请重新输入密码");
				dissMissDialog();
				showInputPwd();
				break;
			case WIFIControl.WIFI_CHELE_DISCONNECT:
				break;
			case WIFIControl.WIFI_NO_INFO:
				UUToast.showUUToast(mCtx, "请选择设备");
				dissMissDialog();
				if (mWifiListDialog != null) {
					mWifiListDialog.dismiss();
				}
				mWifiListDialog = PopBoxCreat.createDialogWifilist(mCtx, mDialogWithEditClick);
				mWifiListDialog.show();
				break;
			}
		};
	};

	public void showInputPwd() {
		if (mDialog != null) {
			mDialog.dismiss();
		}

		mDialog = PopBoxCreat.createDialogWithedit2(mCtx, WIFIControl.SSID_CONNECT, "请输入", InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD,
				"取消", "确定", new PopBoxCreat.DialogWithEditClick() {

					@Override
					public void onRightClick(String editContent) {
						if (editContent != null && editContent.length() > 7) {
							WIFIControl.SSID_PWD = editContent;
							goConnect();
						} else {
							UUToast.showUUToast(mCtx, "设备Wi-Fi密码错误，请重新输入密码");
							showInputPwd();
						}
					}

					@Override
					public void onLeftClick(String editContent) {
						WIFIControl.SSID_CONNECT = "";
						WIFIControl.SSID_PWD = "";
					}
				});
		mDialog.show();
	}

	private PopBoxCreat.DialogWithEditClick mDialogWithEditClick = new PopBoxCreat.DialogWithEditClick() {

		@Override
		public void onRightClick(String editContent) {
			// 确定-连接车乐拍wifi--TODO
			// 连接wifi的代码还没有写
			if (WIFIControl.SSID_CONNECT.length() < 1 || WIFIControl.SSID_PWD.length() < 8) {
				WIFIControl.SSID_PWD = "";
				UUToast.showUUToast(mCtx, "连接设备Wi-Fi密码错误，请重新输入密码");
				showInputPwd();
				return;
			}
			showDialog();
			WIFIControl.StartConnectChelePai();
		}

		@Override
		public void onLeftClick(String editContent) {
			// 取消
			WIFIControl.SSID_CONNECT = "";
			WIFIControl.SSID_PWD = "";
		}
	};

	public void showDialog() {
		if (mWIFIDialog != null) {
			mWIFIDialog.dismiss();
		}
		mWIFIDialog = new WIFIConnectDialog(mCtx);
		mWIFIDialog.show();
	}

	public void dissMissDialog() {
		if (mWIFIDialog != null) {
			mWIFIDialog.dismiss();
		}
	}

	public void onStop() {
		WIFIControl.unRigisterWIFIConnectListener(this);
		DeviceConnectManager.removeNotifyListener(this);
		isShowing = false;
	}

	public void onPause() {
		WIFIControl.unRigisterWIFIConnectListener(this);
		DeviceConnectManager.removeNotifyListener(this);
		isShowing = false;
		dissMissDialog();
	}

	public void onDestory() {
		WIFIControl.unRigisterWIFIConnectListener(this);
		DeviceConnectManager.removeNotifyListener(this);
		isShowing = false;
	}

	@Override
	public void notifyAction(int action) {
		if (!isShowing) {
			return;
		}
		mHandler.sendEmptyMessage(action);
	}

	@Override
	public void onWIFIChange(int action) {
		if (!isShowing) {
			return;
		}
		mHandler.sendEmptyMessage(action);
	}

}
