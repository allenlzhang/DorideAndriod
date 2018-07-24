
package com.carlt.sesame.ui.activity.setting;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.data.LoginInfo;
import com.carlt.sesame.ui.activity.base.BaseActivity;
import com.carlt.sesame.ui.activity.usercenter.login.ActivateActivity;
import com.carlt.sesame.ui.activity.usercenter.scan.ScanActivity;
import com.carlt.sesame.ui.view.PopBoxCreat;
import com.carlt.sesame.utility.UUToast;

/**
 * 通用管理
 * 
 * @author Administrator
 */
public class ManageReBindActivity extends BaseActivity implements OnClickListener {

	/** 扫描二维码 */
	private TextView mScanCode;

	/** 输入二维码 */
	private EditText mDeviceId;

	private TextView mBtn;// 确定按钮

	private View mViewScan;// 扫描外层view

	Dialog mDialog;
	
	String DeviceId;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acrivity_manage_rebind);

		initTitle();
		init();

	}

	private void initTitle() {
		ImageView back = (ImageView) findViewById(R.id.head_back_img1);
		TextView title = (TextView) findViewById(R.id.head_back_txt1);
		TextView txtRight = (TextView) findViewById(R.id.head_back_txt2);

		back.setImageResource(R.drawable.arrow_back);
		title.setText("绑定盒子");
		txtRight.setVisibility(View.GONE);

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				back();
			}
		});
	}

	private void init() {
		mScanCode = (TextView) findViewById(R.id.activity_rebind_btnscan);
		mDeviceId = (EditText) findViewById(R.id.activity_rebind_editnum);
		mBtn = (TextView) findViewById(R.id.activity_rebind_btnbind);

		mViewScan = findViewById(R.id.activity_rebind_scan);

		// 后装设备
	    mViewScan.setVisibility(View.VISIBLE);

		mScanCode.setOnClickListener(this);
		mViewScan.setClickable(false);
		mBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.activity_rebind_btnscan:
			// 跳转至二维码扫描页面
			Intent intent = new Intent();
			intent.setClass(context, ScanActivity.class);
			startActivityForResult(intent, 0);
			break;

		case R.id.activity_rebind_btnbind:
			// 绑定设备(调用初始化设置车型绑定设备接口)
			String deviceCode = mDeviceId.getText().toString();
			String isJumpTobind = LoginInfo.getIsJumptoBind();
			if (isJumpTobind.equals(LoginInfo.noJumptoBind)) {
				// 前装设备
			} else {
				// 后装设备
				if (deviceCode == null || deviceCode.equals("")) {
					UUToast.showUUToast(context, "您还没有输入您的芝麻乐园序列号...");
					return;
				} else if (deviceCode.length() != 16) {
					UUToast.showUUToast(context, "您输入的芝麻乐园序列号有误，请重新输入...");
					return;
				}
			}
			if (mDialog == null) {
				mDialog = PopBoxCreat.createDialogWithProgress(context, "绑定中...");
			}
			mDialog.show();
			DeviceId = deviceCode;
			CPControl.GetCarChangeDevice(deviceCode, listener);
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0) {

			if (data == null) {
				UUToast.showUUToast(context, "二维码不正确");
				return;
			}

			String code = data.getStringExtra("Code");
			if (code == null || code.length() != 16) {
				UUToast.showUUToast(context, "二维码不正确");
				return;
			}

			mDeviceId.setText(code);
		}

		super.onActivityResult(requestCode, resultCode, data);
	}


	GetResultListCallback listener = new GetResultListCallback() {

		@Override
		public void onFinished(Object o) {
			Message msg = new Message();
			msg.what = 0;
			msg.obj = o;
			mHandler.sendMessage(msg);
		}

		@Override
		public void onErro(Object o) {
			Message msg = new Message();
			msg.what = 1;
			msg.obj = o;
			mHandler.sendMessage(msg);
		}
	};

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			BaseResponseInfo rinfo = null;
			rinfo = (BaseResponseInfo) msg.obj;
			mDialog.dismiss();
			switch (msg.what) {
			case 0:
				if (rinfo != null && rinfo.getInfo() != null && rinfo.getInfo().length() > 0) {
					UUToast.showUUToast(context, rinfo.getInfo());
				} else {
					UUToast.showUUToast(context, "绑定盒子成功!");
				}
				Intent intent = new Intent(context, ActivateActivity.class);
				intent.putExtra(ActivateActivity.ID, DeviceId);
				intent.putExtra(ActivateActivity.FROM_NAME, ActivateActivity.CLASS_DEVICE_REBIND);
				startActivity(intent);
				finish();
				break;
			case 1:
				if (rinfo != null && rinfo.getInfo() != null && rinfo.getInfo().length() > 0) {
					UUToast.showUUToast(context, rinfo.getInfo());
				} else {
					UUToast.showUUToast(context, "绑定盒子失败!");
				}
				break;
			}
		};
	};

	private void back() {
		finish();
	}

	@Override
	public void onBackPressed() {
		back();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

}
