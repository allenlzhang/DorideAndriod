package com.carlt.sesame.ui.activity.safety;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.doride.DorideApplication;
import com.carlt.sesame.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.data.LoginInfo;
import com.carlt.sesame.data.safety.SafetyMainInfo;
import com.carlt.sesame.ui.activity.base.LoadingActivityWithTitle;
import com.carlt.sesame.ui.activity.setting.EditPhoneActivity1;
import com.carlt.sesame.ui.view.PopBoxCreat;
import com.carlt.sesame.ui.view.PopBoxCreat.DialogEditClick;
import com.carlt.sesame.utility.UUToast;

/**
 * 安全中心首页
 * 
 * @author Administrator
 */
public class SafetyMainActivity extends LoadingActivityWithTitle implements
		OnClickListener, OnCheckedChangeListener {
	private ImageView back;// 头部返回键

	private TextView title;// 标题文字

	private View mViewRealName;// 实名认证

	private View mViewPhone;// 手机

	private View mViewPsw;// 登录密码

	private View mViewMobileLog;// 近期登录记录

	private View mViewNoneedpsw;// 远程控制五分钟无需密码

	private View mViewMobileList;// 在线授权设备列表

	private View mViewLineMobileList;// 在线授权设备列表-上方紅線

	private View mViewDot;// 在线授权设备列表-右側紅點

	private View mViewAuther;// 是否允许手机授权

	private View mViewFreeze;// 快速冻结账号

	private TextView mTxtAuthenStatus;// 认证状态

	private TextView mTxtPhone;// 手机号

	private TextView mTxtMobileCurrent;// 当前在线设备

	private CheckBox mCbNoneedpsw;// 是否开启远程控制五分钟无需密码

	private CheckBox mCbAuther;// 是否允许手机授权

	private Dialog mDialog;

	private String mPwd;

	private String status;

	private boolean isReset = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_safetymain);
		setTitleView(R.layout.head_back);
		initTitle();
		init();
	}

	private void initTitle() {
		back = (ImageView) findViewById(R.id.head_back_img1);
		title = (TextView) findViewById(R.id.head_back_txt1);

		title.setText("安全中心");

		back.setImageResource(R.drawable.arrow_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void init() {
		mViewRealName = findViewById(R.id.safetymain_lay_realname);
		mViewPhone = findViewById(R.id.safetymain_lay_phone);
		mViewPsw = findViewById(R.id.safetymain_lay_psw);
		mViewNoneedpsw = findViewById(R.id.safetymain_lay_noneedpsw);
		mViewMobileLog = findViewById(R.id.safetymain_lay_mobilelog);
		mViewMobileList = findViewById(R.id.safetymain_lay_mobilelist);
		mViewLineMobileList = findViewById(R.id.safetymain_line_mobilelist_top);
		mViewDot = findViewById(R.id.safetymain_view_dot);
		mViewAuther = findViewById(R.id.safetymain_lay_authorize);
		mViewFreeze = findViewById(R.id.safetymain_lay_freeze);

		mTxtAuthenStatus = (TextView) findViewById(R.id.safetymain_txt_state);
		mTxtPhone = (TextView) findViewById(R.id.safetymain_txt_phone);
		mTxtMobileCurrent = (TextView) findViewById(R.id.safetymain_txt_mobilecurrent);
		mCbNoneedpsw = (CheckBox) findViewById(R.id.safetymain_checkbox_noneedpsw);
		mCbAuther = (CheckBox) findViewById(R.id.safetymain_checkbox_authorize);

		// 120版本去掉
		// if ("1".equals(LoginInfo.getRemoteControl())) {
		// mViewNoneedpsw.setVisibility(View.VISIBLE);
		// }

		mViewRealName.setOnClickListener(this);
		mViewPhone.setOnClickListener(this);
		mViewPsw.setOnClickListener(this);
		mViewMobileLog.setOnClickListener(this);
		mViewMobileList.setOnClickListener(this);
		mViewFreeze.setOnClickListener(this);

		mTxtMobileCurrent.setOnClickListener(this);
		mCbAuther.setOnClickListener(this);
	}

	@Override
	protected void LoadData() {
		super.LoadData();
		CPControl.GetSafetyMainInfoResult(listener, DorideApplication.NIMEI);
	}

	@Override
	protected void LoadSuccess(Object data) {
		SafetyMainInfo mSafetyMainInfo = (SafetyMainInfo) data;
		String mobileCurrent = DorideApplication.MODEL_NAME;
		mTxtMobileCurrent.setText("当前在线设备:" + mobileCurrent);
		boolean flag = LoginInfo.isAuthen();
		if (flag) {
			// 已经实名认证
			mTxtAuthenStatus.setText("已认证");
		} else {
			// 未实名认证
			mTxtAuthenStatus.setText("未认证");
		}

		String mobile = LoginInfo.getMobile();
		if (mobile != null && mobile.length() == 11) {
			String s1 = mobile.substring(0, 3);
			String s2 = mobile.substring(7);
			StringBuffer mBuffer = new StringBuffer();
			mBuffer.append(s1);
			mBuffer.append("****");
			mBuffer.append(s2);
			mTxtPhone.setText(mBuffer);
		} else {
			mTxtPhone.setText("--");
		}

		if (LoginInfo.isMain()) {
			mViewAuther.setVisibility(View.VISIBLE);
			mViewLineMobileList.setVisibility(View.VISIBLE);
			mViewFreeze.setVisibility(View.VISIBLE);
			String authen_status = LoginInfo.getAuthorize_status();
			if (authen_status != null
					&& authen_status.equals(LoginInfo.AUTHORIZE_STATUS_CLOSE)) {
				mCbAuther.setChecked(false);
			} else if (authen_status != null
					&& authen_status.equals(LoginInfo.AUTHORIZE_STATUS_OPEN)) {
				mCbAuther.setChecked(true);
			}
			mCbAuther.setOnCheckedChangeListener(this);
		} else {
			mViewAuther.setVisibility(View.GONE);
			mViewLineMobileList.setVisibility(View.GONE);
			mViewFreeze.setVisibility(View.GONE);
		}

		if (LoginInfo.isNoneedpsw()) {
			mCbNoneedpsw.setChecked(true);
		} else {
			mCbNoneedpsw.setChecked(false);
		}
		mCbNoneedpsw.setOnCheckedChangeListener(this);

		if (mSafetyMainInfo.isHasAuthorize()) {
			mViewDot.setVisibility(View.VISIBLE);
		} else {
			mViewDot.setVisibility(View.GONE);
		}
		super.LoadSuccess(data);
	}

	@Override
	protected void LoadErro(Object erro) {
		super.LoadErro(erro);
	}

	@Override
	protected void onResume() {
		super.onResume();
		LoadData();
	}

	/**
	 * 更新授权状态开关
	 */
	private GetResultListCallback listener_update = new GetResultListCallback() {

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

	/**
	 * 更新五分钟内是否无需输入远程密码开关
	 */
	private GetResultListCallback listener_noneedpsw = new GetResultListCallback() {

		@Override
		public void onFinished(Object o) {
			Message msg = new Message();
			msg.what = 4;
			msg.obj = o;
			mHandler.sendMessage(msg);

		}

		@Override
		public void onErro(Object o) {
			Message msg = new Message();
			msg.what = 5;
			msg.obj = o;
			mHandler.sendMessage(msg);
		}
	};

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				// 授权状态更新成功
				if (mDialog != null && mDialog.isShowing()) {
					mDialog.dismiss();
				}
				BaseResponseInfo mInfo = (BaseResponseInfo) msg.obj;
				if (mInfo != null) {
					String info = mInfo.getInfo();
					if (info != null && info.length() > 0) {
						UUToast.showUUToast(SafetyMainActivity.this, info);
					} else {
						UUToast.showUUToast(SafetyMainActivity.this, "操作成功！");
					}
				} else {
					UUToast.showUUToast(SafetyMainActivity.this, "操作成功！");
				}
				break;

			case 1:
				// 授权状态更新失败
				if (mDialog != null && mDialog.isShowing()) {
					mDialog.dismiss();
				}
				mCbAuther.setOnCheckedChangeListener(null);
				boolean isChecked = mCbAuther.isChecked();
				isChecked = !isChecked;
				mCbAuther.setChecked(isChecked);
				mCbAuther.setOnCheckedChangeListener(SafetyMainActivity.this);
				BaseResponseInfo mInfo1 = (BaseResponseInfo) msg.obj;
				if (mInfo1 != null) {
					String info = mInfo1.getInfo();
					if (info != null && info.length() > 0) {
						UUToast.showUUToast(SafetyMainActivity.this, info);
					} else {
						UUToast.showUUToast(SafetyMainActivity.this, "操作失败...");
					}
				} else {
					UUToast.showUUToast(SafetyMainActivity.this, "操作失败...");
				}

				break;
			case 2:
				isReset = false;
				mDialog.dismiss();
				editClick.cancle();
				isReset = true;
				mDialog = PopBoxCreat.createDialogWithProgress(
						SafetyMainActivity.this, "提交中...");
				mDialog.show();
				CPControl.GetUpdateAuthorizeStatusResult(status,
						listener_update);
				break;
			case 3:
				isReset = true;
				mDialog.dismiss();
				BaseResponseInfo info = (BaseResponseInfo) msg.obj;
				if (info != null && info.getInfo() != null
						&& info.getInfo().length() > 0) {
					UUToast.showUUToast(SafetyMainActivity.this, info.getInfo());
				} else {
					UUToast.showUUToast(SafetyMainActivity.this, "登录密码错误!");
				}
				break;
			case 4:
				if (mDialog != null && mDialog.isShowing()) {
					mDialog.dismiss();
				}
				BaseResponseInfo mInfo2 = (BaseResponseInfo) msg.obj;
				if (mInfo2 != null) {
					String info2 = mInfo2.getInfo();
					if (info2 != null && info2.length() > 0) {
						UUToast.showUUToast(SafetyMainActivity.this, info2);
					} else {
						UUToast.showUUToast(SafetyMainActivity.this, "操作成功！");
					}
				} else {
					UUToast.showUUToast(SafetyMainActivity.this, "操作成功！");
				}
				break;
			case 5:
				// 授权状态更新失败
				if (mDialog != null && mDialog.isShowing()) {
					mDialog.dismiss();
				}
				mCbNoneedpsw.setOnCheckedChangeListener(null);
				boolean isChecked2 = mCbNoneedpsw.isChecked();
				isChecked2 = !isChecked2;
				mCbAuther.setChecked(isChecked2);
				mCbAuther.setOnCheckedChangeListener(SafetyMainActivity.this);
				BaseResponseInfo mInfo3 = (BaseResponseInfo) msg.obj;
				if (mInfo3 != null) {
					String info3 = mInfo3.getInfo();
					if (info3 != null && info3.length() > 0) {
						UUToast.showUUToast(SafetyMainActivity.this, info3);
					} else {
						UUToast.showUUToast(SafetyMainActivity.this, "操作失败...");
					}
				} else {
					UUToast.showUUToast(SafetyMainActivity.this, "操作失败...");
				}
				break;
			}
		}

	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.safetymain_lay_realname:
			// 实名制认证
			Intent mIntent1 = new Intent(SafetyMainActivity.this,
					RealNameActivity.class);
			boolean flag = LoginInfo.isAuthen();
			// 测试用
			// flag=false;
			// 测试结束
			if (flag) {
				mIntent1.putExtra(RealNameActivity.TYPE,
						RealNameActivity.TYPE_AUTHERED);
			} else {
				mIntent1.putExtra(RealNameActivity.TYPE,
						RealNameActivity.TYPE_UNAUTHER);
			}
			startActivity(mIntent1);
			break;
		case R.id.safetymain_lay_phone:
			// 绑定手机
			Intent mIntent2 = new Intent(SafetyMainActivity.this,
					EditPhoneActivity1.class);
			startActivity(mIntent2);
			break;
		case R.id.safetymain_lay_psw:
			// 密码管理
			Intent mIntent3 = new Intent(SafetyMainActivity.this,
					ManagePswActiviy.class);
			startActivity(mIntent3);
			break;
		case R.id.safetymain_txt_mobilelog:
			// 当前在线设备
			break;
		case R.id.safetymain_lay_mobilelog:
			// 近期登录记录
			Intent mIntent5 = new Intent(SafetyMainActivity.this,
					LoginLogActivity.class);
			startActivity(mIntent5);
			break;
		case R.id.safetymain_checkbox_authorize:
			// 是否允许手机授权
			break;
		case R.id.safetymain_lay_mobilelist:
			// 在线授权设备列表
			Intent mIntent6 = new Intent(SafetyMainActivity.this,
					MobileListActivity.class);
			startActivity(mIntent6);
			break;
		case R.id.safetymain_lay_freeze:
			// 快速冻结账号
			Intent mIntent7 = new Intent(SafetyMainActivity.this,
					FreezeActivity.class);
			mIntent7.putExtra(FreezeActivity.FROM_NAME, SafetyMainActivity.this
					.getClass().getName());
			startActivity(mIntent7);
			break;
		}
	}

	final String ET4_DIGITS = "0123456789abcdefghigklmnopqrstuvwxyz";

	InputFilter mInputFilter2 = new InputFilter() {

		@Override
		public CharSequence filter(CharSequence source, int start, int end,
				Spanned dest, int dstart, int dend) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < source.length(); i++) {
				if (ET4_DIGITS.indexOf(source.charAt(i)) >= 0) {
					sb.append(source.charAt(i));
				}
			}
			return sb;
		}
	};

	InputFilter mInputFilter3 = new InputFilter() {

		@Override
		public CharSequence filter(CharSequence source, int start, int end,
				Spanned dest, int dstart, int dend) {
			if (source.equals(" ")) {
				return "";
			} else {
				return null;
			}
		}
	};

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			status = "1";
		} else {
			status = "0";
		}

		switch (buttonView.getId()) {
		case R.id.safetymain_checkbox_noneedpsw:
			mDialog = PopBoxCreat.createDialogWithProgress(SafetyMainActivity.this, "提交中...");
			mDialog.show();
			CPControl.GetUpdateLessPwdResult(status, listener_noneedpsw);
			break;

		case R.id.safetymain_checkbox_authorize:
			PopBoxCreat.showEditDialog(this, editClick, new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (isReset) {
						boolean state = !mCbAuther.isChecked();
						mCbAuther.setOnCheckedChangeListener(null);
						mCbAuther.setChecked(state);
						mCbAuther.setOnCheckedChangeListener(SafetyMainActivity.this);
					}
				}
			},new InputFilter[]{new InputFilter.LengthFilter(16),mInputFilter2,mInputFilter3});
			break;
		}

	}

	DialogEditClick editClick = new DialogEditClick() {

		@Override
		public void onClick(View v) {
			mPwd = this.mEditText.getText().toString();
			if (null == mPwd || mPwd.length() < 1) {
				UUToast.showUUToast(SafetyMainActivity.this, "密码不能为空哦！");
				return;
			}
			mDialog = PopBoxCreat.createDialogWithProgress(
					SafetyMainActivity.this, "正在验证密码，请稍等...");
			mDialog.show();
			CPControl.GetToCheckPwd(mPwd, check_listener);
		}
	};

	GetResultListCallback check_listener = new GetResultListCallback() {

		@Override
		public void onFinished(Object o) {
			Message msg = new Message();
			msg.what = 2;
			msg.obj = o;
			mHandler.sendMessage(msg);
		}

		@Override
		public void onErro(Object o) {
			Message msg = new Message();
			msg.what = 3;
			msg.obj = o;
			mHandler.sendMessage(msg);
		}
	};

}
