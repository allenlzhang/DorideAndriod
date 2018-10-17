
package com.carlt.chelepie.view;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.carlt.chelepie.control.WIFIControl;
import com.carlt.chelepie.data.ScanResultInfo;
import com.carlt.chelepie.data.recorder.BaseResponseInfo;
import com.carlt.chelepie.view.adapter.WifiInfoAdapter;
import com.carlt.doride.R;
import com.carlt.doride.ui.view.PopBoxCreat;
import com.carlt.doride.ui.view.UUDialogBaseLoading;
import com.carlt.doride.ui.view.UUToast;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取系统wifi列表Dialog
 * 
 * @author Administrator
 */
public class WifiListDialog extends UUDialogBaseLoading {

	protected ListView mListView;

	protected Context mContext;

	private List<ScanResult> mDatalist;

	private PopBoxCreat.DialogWithEditClick mDialogWithEditClick;


	public WifiListDialog(Context context) {
		super(context);
		mContext = context;

		setBodyView(R.layout.dialog_list);
		head.setVisibility(View.GONE);
		foot.setVisibility(View.GONE);
		mListView = (ListView) mainView.findViewById(R.id.dialog_list_listview);
		LoadData();
	}

	private void setData(List<ScanResult> mDatalist) {
		if (mDatalist != null && mDatalist.size() > 0) {
			WifiInfoAdapter mAdapter = new WifiInfoAdapter(mContext, mDatalist);
			mListView.setAdapter(mAdapter);
		}
		if(null == mDatalist || mDatalist.isEmpty() || mDatalist.get(0) == null){
			UUToast.showUUToast(mContext, "暂无可用Wi-Fi");
		}
		this.mDatalist = mDatalist;
	}

	public void setmDialogWithTitleClick(PopBoxCreat.DialogWithEditClick mDialogWithEditClick) {
		this.mDialogWithEditClick = mDialogWithEditClick;
	}

	public void doItemClick() {
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				if (mDatalist != null && mDialogWithEditClick != null) {
					ScanResult mResult = mDatalist.get(position);
					String ssid = mResult.SSID;
					if (ssid != null && !ssid.equals("") && ssid.replace("\"", "").startsWith(WIFIControl.chelepiePrefix)) {
						WIFIControl.SSID_CONNECT = ssid.replace("\"", "");
						dismiss();
						EditDialog2 mDialog = PopBoxCreat.createDialogWithedit2(mContext, mResult.SSID, "请输入",
								InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD, "取消", "确定", mDialogWithEditClick);
						mDialog.show();
					} else {
						UUToast.showUUToast(mContext, "您选择的不是设备Wi-Fi哦！");
					}
				}
			}
		});
	}

	@Override
	protected void LoadData() {
		super.LoadData();
		Thread mThread = new Thread(new Runnable() {

			@Override
			public void run() {
				WifiManager mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
				mWifiManager.startScan();
				// 得到扫描结果
				List<ScanResult> mScanResultList = mWifiManager.getScanResults();
				ScanResultInfo info = new ScanResultInfo();
				List<ScanResult> temp = new ArrayList<ScanResult>();
				for (ScanResult scanResult : mScanResultList) {
					if (scanResult.SSID.replace("\"", "").startsWith(WIFIControl.chelepiePrefix)) {
						temp.add(scanResult);
					}
				}
				info.setScanResults(temp);
				listener.onSuccess(info);
				Log.e("info", "mScanResultList.size==" + mScanResultList.size());
			}
		});
		mThread.start();
	}

	@Override
	protected void LoadError(Object erro) {
		super.LoadError(erro);
	}


	@Override
	public void LoadSuccess(Object data) {
		setData(((ScanResultInfo)data).getScanResults());
		super.LoadSuccess(data);
	}

}
