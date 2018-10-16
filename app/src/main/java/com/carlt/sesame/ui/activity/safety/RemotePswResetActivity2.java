
package com.carlt.sesame.ui.activity.safety;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.data.SesameLoginInfo;
import com.carlt.sesame.ui.activity.base.BaseActivity;
import com.carlt.sesame.ui.activity.remote.RemoteMainNewActivity;
import com.carlt.sesame.ui.view.GetValidateView;
import com.carlt.sesame.ui.view.PopBoxCreat;
import com.carlt.sesame.ui.view.PwdEditText;
import com.carlt.sesame.ui.view.PwdEditText.OnInputListener;
import com.carlt.sesame.ui.view.ValidateEditText;
import com.carlt.sesame.utility.UUToast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 忘记远程控制密码-step2获取手机验证码
 * 
 * @author Administrator
 */
public class RemotePswResetActivity2 extends BaseActivity implements OnClickListener {
	private ImageView back;// 头部返回键

	private TextView title;// 标题文字

	private ValidateEditText mEdtPhone;// 用户手机号

	private ValidateEditText mEdtCode;// 验证码

	private TextView mTxtCode;// 发送验证码

	private PwdEditText mPwdEdt1;// 密码编辑框-新密码

	private PwdEditText mPwdEdt2;// 密码编辑框-新密码确认

	private TextView mTxtNext;// 修改

	private GetValidateView mValidateView;// 播报语音验证码页面

	private Dialog mDialog;

	public final static String INFO_NAME = "info_name"; // 姓名

	public final static String INFO_IDCARD = "info_idcard"; // 身份证号

	String name;// 姓名

	String idcard;// 身份证号

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_remotepsw_reset2);

		try {
			name = getIntent().getStringExtra(INFO_NAME);
			idcard = getIntent().getStringExtra(INFO_IDCARD);
		} catch (Exception e) {
			// TODO: handle exception
		}

		initTitle();
		init();
	}

	private void initTitle() {
		back = (ImageView) findViewById(R.id.head_back_img1);
		title = (TextView) findViewById(R.id.head_back_txt1);

		back.setImageResource(R.drawable.arrow_back);
		title.setText("重置远程控制密码");

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				back();
			}
		});

	}

	private void init() {
		mEdtPhone = (ValidateEditText) findViewById(R.id.remotepsw_reset_edt_phone);
		mEdtCode = (ValidateEditText) findViewById(R.id.remotepsw_reset_edt_code);
		
		mEdtPhone.setEditHint("请填写手机号码");
		mEdtPhone.setmType(ValidateEditText.TYPE_PON);
		mEdtPhone.setNextEditText(mEdtCode);
		
		mEdtCode.setEditHint("请输入验证码");
		mEdtCode.setmType(ValidateEditText.TYPE_CODE);

		mTxtCode = (TextView) findViewById(R.id.remotepsw_reset_txt_code);
		mTxtNext = (TextView) findViewById(R.id.remotepsw_reset_txt_next);

		mPwdEdt1 = (PwdEditText) findViewById(R.id.remotepsw_reset2_pwdedt1);
		mPwdEdt2 = (PwdEditText) findViewById(R.id.remotepsw_reset2_pwdedt2);

		mTxtCode.setOnClickListener(this);
		mTxtNext.setOnClickListener(this);
		mPwdEdt1.setOnInputListener(mInputListener);
		mPwdEdt2.setOnInputListener(mInputListener);
	}

	// 获取短信验证码的回调
	GetResultListCallback listener_code = new GetResultListCallback() {

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

	GetResultListCallback listener_verify = new GetResultListCallback() {

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

	// 忘记密码
	private GetResultListCallback listener_forget = new GetResultListCallback() {

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
			BaseResponseInfo mBaseResponseInfo = null;
			switch (msg.what) {
			case 0:
				UUToast.showUUToast(RemotePswResetActivity2.this, msg.obj.toString());
				mTxtCode.setClickable(true);
				break;

			case 1:
				// 停止计时
				if (timer != null) {
					if (task != null) {
						task.cancel();
					}
				}
				mTxtCode.setClickable(true);
				mTxtCode.setText(R.string.usercenter_push_validate1);
				mTxtCode.setBackgroundResource(R.drawable.btn_code_bg);

				mBaseResponseInfo = (BaseResponseInfo) msg.obj;
				int flag = mBaseResponseInfo.getFlag();
				if (flag == BaseResponseInfo.VALIDATE_LIMIT) {
					if (mValidateView == null) {
						mValidateView = new GetValidateView(RemotePswResetActivity2.this,
								CPControl.VALIDATE_TYPE_FINDPASSWORD, phoneNum);
					}
					mValidateView.showMenu();
				} else {
					UUToast.showUUToast(RemotePswResetActivity2.this, "获取验证码失败：" + mBaseResponseInfo.getInfo());
				}
				break;
			case 2:
				// 验证码验证成功
				if (mDialog != null && mDialog.isShowing()) {
					mDialog.dismiss();
				}
				mBaseResponseInfo = (BaseResponseInfo) msg.obj;
				if (mBaseResponseInfo != null) {
					String info = mBaseResponseInfo.getInfo();
					if (info != null && info.length() > 0) {
						UUToast.showUUToast(RemotePswResetActivity2.this, info);
					} else {
						UUToast.showUUToast(RemotePswResetActivity2.this, "验证成功！");
					}
				} else {
					UUToast.showUUToast(RemotePswResetActivity2.this, "验证成功！");
				}
				String code = mEdtCode.getText().toString();
				Intent mIntent = new Intent(RemotePswResetActivity2.this, RemotePswResetActivity3.class);
				mIntent.putExtra(RemotePswResetActivity3.TYPE, RemotePswResetActivity3.TYPE_FORGET);
				mIntent.putExtra(RemotePswResetActivity3.VALIDATE, code);
				startActivity(mIntent);
				finish();
				break;
			case 3:
				// 验证码验证失败
				if (mDialog != null && mDialog.isShowing()) {
					mDialog.dismiss();
				}
				mBaseResponseInfo = (BaseResponseInfo) msg.obj;
				if (mBaseResponseInfo != null) {
					String info = mBaseResponseInfo.getInfo();
					if (info != null && info.length() > 0) {
						UUToast.showUUToast(RemotePswResetActivity2.this, info);
					} else {
						UUToast.showUUToast(RemotePswResetActivity2.this, "验证失败...");
					}
				} else {
					UUToast.showUUToast(RemotePswResetActivity2.this, "验证失败...");
				}
				break;
			case 4:
				// 重置密码成功
				if (mDialog != null && mDialog.isShowing()) {
					mDialog.dismiss();
				}
				BaseResponseInfo mInfo2 = (BaseResponseInfo) msg.obj;
				if (mInfo2 != null) {
					String info = mInfo2.getInfo();
					if (info != null && info.length() > 0) {
						UUToast.showUUToast(RemotePswResetActivity2.this, info);
					} else {
						UUToast.showUUToast(RemotePswResetActivity2.this, "重置远程密码成功！");
					}
				} else {
					UUToast.showUUToast(RemotePswResetActivity2.this, "重置远程密码成功！");
				}
				Intent mIntent2 = new Intent();
				mIntent2.setAction(RemoteMainNewActivity.ACTION_REMOTE_FORGETPSW);
				sendBroadcast(mIntent2);
				finish();
				break;
			case 5:
				// 重置密码失败
				if (mDialog != null && mDialog.isShowing()) {
					mDialog.dismiss();
				}
				BaseResponseInfo mInfo3 = (BaseResponseInfo) msg.obj;
				if (mInfo3 != null) {
					String info = mInfo3.getInfo();
					if (info != null && info.length() > 0) {
						UUToast.showUUToast(RemotePswResetActivity2.this, info);
					} else {
						UUToast.showUUToast(RemotePswResetActivity2.this, "重置远程密码失败...");
					}
				} else {
					UUToast.showUUToast(RemotePswResetActivity2.this, "重置远程密码失败...");
				}

				break;

			case 10:
				count--;
				if (count > 0) {
					mTxtCode.setText(count + "秒后重发");
				} else {
					if (timer != null) {
						if (task != null) {
							task.cancel();
						}
					}
					mTxtCode.setClickable(true);
					mTxtCode.setText(R.string.usercenter_push_validate1);
					mTxtCode.setBackgroundResource(R.drawable.btn_code_bg);
				}
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 倒计时
	 */
	private int count = 60;

	private Timer timer = new Timer();

	private TimerTask task;

	private String phoneNum = null;// 手机号

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.remotepsw_reset_txt_code:
			mEdtPhone.validateEdit();
			phoneNum = mEdtPhone.getText().toString();
			String phoneLocal = SesameLoginInfo.getMobile();
			if (phoneNum != null && phoneNum.length() == 11 && phoneNum.equals(phoneLocal)) {
				CPControl.GetMessageValidateResult(CPControl.VALIDATE_TYPE_FINDPASSWORDREMOTE, phoneNum, listener_code);
				count = 60;
				mTxtCode.setText(count + "秒后重发");
				mTxtCode.setClickable(false);
				mTxtCode.setBackgroundResource(R.drawable.btn_code_gray);

				task = new TimerTask() {

					@Override
					public void run() {
						Message msg = new Message();
						msg.what = 10;
						mHandler.sendMessage(msg);

					}
				};
				timer.schedule(task, 1000, 1000);
			} else {
				UUToast.showUUToast(RemotePswResetActivity2.this, "请输入正确的手机号");
			}
			break;

		case R.id.remotepsw_reset_txt_next:
			// 调用校验验证码接口
			// String phone = mEdtPhone.getText().toString();
			// String code = mEdtCode.getText().toString();
			// if (code != null && code.length() > 0) {
			// if (mDialog == null) {
			// mDialog = PopBoxCreat.createDialogWithProgress(
			// RemotePswResetActivity2.this, "提交中...");
			// }
			// mDialog.show();
			// CPControl.GetValidateCheckResult(phone,
			// CPControl.VALIDATE_TYPE_FINDPASSWORDREMOTE, code,
			// listener_verify);
			// } else {
			// UUToast.showUUToast(RemotePswResetActivity2.this,
			// "您的验证码不正确哦！");
			// return;
			// }
			// 跳转至验证第三步
			// String phone = mEdtPhone.getText().toString();
			// String code = mEdtCode.getText().toString();
			// String phoneLocal2 = LoginInfo.getMobile();
			// if (phoneLocal2 == null || phoneLocal2.length() < 1 ||
			// !phone.equals(phoneLocal2)) {
			// UUToast.showUUToast(RemotePswResetActivity2.this,
			// "请输入正确的手机号");
			// return;
			// } else if (code == null || code.length() < 1) {
			// UUToast.showUUToast(RemotePswResetActivity2.this,
			// "您的验证码不正确哦！");
			// return;
			// } else {
			// Intent mIntent = new Intent(RemotePswResetActivity2.this,
			// RemotePswResetActivity3.class);
			// mIntent.putExtra(RemotePswResetActivity3.TYPE,
			// RemotePswResetActivity3.TYPE_FORGET);
			// mIntent.putExtra(RemotePswResetActivity3.VALIDATE, code);
			// mIntent.putExtra(RemotePswResetActivity3.INFO_NAME, name);
			// mIntent.putExtra(RemotePswResetActivity3.INFO_IDCARD,
			// idcard);
			// startActivity(mIntent);
			// finish();
			// }

			mEdtPhone.validateEdit();
			mEdtCode.validateEdit();
			// 调用重置远程密码接口
			String phone = mEdtPhone.getText().toString();
			String code = mEdtCode.getText().toString();
			String mobile = SesameLoginInfo.getMobile();
			String pswNew1 = mPwdEdt1.getText().toString();
			String pswNew2 = mPwdEdt2.getText().toString();
			if (mobile == null || mobile.length() < 1 || !phone.equals(mobile)) {
				UUToast.showUUToast(RemotePswResetActivity2.this, "请输入正确的手机号");
				return;
			} else if (code == null || code.length() < 1) {
				UUToast.showUUToast(RemotePswResetActivity2.this, "您的验证码不正确哦！");
				return;
			} else if (pswNew1 == null || pswNew1.length() != 6) {
				UUToast.showUUToast(RemotePswResetActivity2.this, "您的新密码应为6位，请重新输入...");
				return;
			} else if (pswNew2 == null || pswNew2.length() != 6) {
				UUToast.showUUToast(RemotePswResetActivity2.this, "您再次输入的密码应为6位，请重新输入...");
				return;
			} else if (!pswNew1.equals(pswNew2)) {
				UUToast.showUUToast(RemotePswResetActivity2.this, "您两次输入的密码不一致，请重新输入...");
				return;
			} else {
				mDialog = PopBoxCreat.createDialogWithProgress(RemotePswResetActivity2.this, "数据提交中...");
				mDialog.show();
				CPControl.GetForgetRemotePwdResult(name, idcard, mobile, pswNew1, code, listener_forget);
			}

			break;
		}
	}

	private OnInputListener mInputListener = new OnInputListener() {

		@Override
		public void onInputChange(int viewID, int length, String password, boolean isFinished) {
			if (isFinished) {
				switch (viewID) {
				case R.id.remotepsw_reset2_pwdedt1:
					mPwdEdt1.clearFocus();
					mPwdEdt2.requestFocus();
					mPwdEdt2.setFocusable(true);
					mPwdEdt2.setFocusableInTouchMode(true);
					break;

				case R.id.remotepsw_reset2_pwdedt2:
					// mPwdEdt2.clearFocus();
					// mPwdEdt1.requestFocus();
					// mPwdEdt1.setFocusable(true);
					// mPwdEdt1.setFocusableInTouchMode(true);
					break;
				}
			}

		}
	};

	private void back() {
		Intent mIntent = new Intent(RemotePswResetActivity2.this, RemotePswResetActivity1.class);
		startActivity(mIntent);
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			back();
			return true;
		}
		return super.onKeyDown(keyCode, event);

	}

	@Override
	protected void onDestroy() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		if (task != null) {
			task.cancel();
			task = null;
		}
		super.onDestroy();
	}

}
