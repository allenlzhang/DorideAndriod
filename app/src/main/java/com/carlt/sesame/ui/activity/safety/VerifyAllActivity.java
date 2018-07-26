
package com.carlt.sesame.ui.activity.safety;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.doride.DorideApplication;
import com.carlt.doride.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.data.LoginInfo;
import com.carlt.sesame.ui.activity.base.BaseActivity;
import com.carlt.sesame.ui.view.GetValidateView;
import com.carlt.sesame.ui.view.PopBoxCreat;
import com.carlt.sesame.ui.view.ValidateEditText;
import com.carlt.sesame.utility.CheckInfo;
import com.carlt.sesame.utility.UUToast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 丢失手机后-验证所有信息
 * 
 * @author Daisy
 */
public class VerifyAllActivity extends BaseActivity implements OnClickListener {
	private ImageView back;// 头部返回键

	private TextView title;// 标题文字

	private ValidateEditText mEdtPhoneOld;// 旧主机号码

	private ValidateEditText mEdtPswOld;// 旧登录密码

	private ValidateEditText mEdtName;// 用户真实姓名

	private ValidateEditText mEdtIdentity;// 用户身份证号

	private ValidateEditText mEdtPswRemote;// 远程控制密码

	private ValidateEditText mEdtPhoneNew;// 新主机号码

	private ValidateEditText mEdtCode;// 验证码

	private ValidateEditText mEdtPswNew;// 新登录密码

	private ValidateEditText mEdtPswNewSure;// 新登录密码确认

	private TextView mTxtCode;// 发送验证码

	private TextView mBtnOption;// 更换主机按钮

	private View mViewPhoneNew;// 旧主机号码外层view

	private Dialog mDialog;

	private String mobile_name;// 手机名称

	private String mobile_id;// 手机唯一标识id

	public final static String MOBILE_NAME = "mobile_name";

	public final static String MOBILE_ID = "mobile_id";

	private boolean isMain;// 当前手机是否是主机

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_verify_all);

		isMain = LoginInfo.isMain();
		try {
			mobile_name = getIntent().getStringExtra(MOBILE_NAME);
			mobile_id = getIntent().getStringExtra(MOBILE_ID);
		} catch (Exception e) {
			// TODO: handle exception
		}

		initTitle();
		init();
	}

	private void initTitle() {
		back = (ImageView) findViewById(R.id.head_back_img1);
		title = (TextView) findViewById(R.id.head_back_txt1);

		title.setText("安全验证");

		back.setImageResource(R.drawable.arrow_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	private void init() {

		mEdtPhoneOld = (ValidateEditText) findViewById(R.id.verify_all_edt_phoneold);
		mEdtPswOld = (ValidateEditText) findViewById(R.id.verify_all_edt_pswold);
		mEdtName = (ValidateEditText) findViewById(R.id.verify_all_edt_name);
		mEdtIdentity = (ValidateEditText) findViewById(R.id.verify_all_edt_identify);
		mEdtCode = (ValidateEditText) findViewById(R.id.verify_all_edt_code);
		mEdtPswRemote = (ValidateEditText) findViewById(R.id.verify_all_edt_pswremote);
		mEdtPhoneNew = (ValidateEditText) findViewById(R.id.verify_all_edt_phonenew);
		mEdtPswNewSure = (ValidateEditText) findViewById(R.id.verify_all_edt_pswnew_sure);
		mEdtPswNew = (ValidateEditText) findViewById(R.id.verify_all_edt_pswnew);
		
		mEdtPhoneOld.setmLength(11);
		mEdtPhoneOld.setEditHint("请输入旧手机号码");
		mEdtPhoneOld.setmType(ValidateEditText.TYPE_NUM);
		mEdtPhoneOld.setNextEditText(mEdtPswOld);
		
		
		mEdtPswOld.setEditHint("请输入旧登录密码");
		mEdtPswOld.setmType(ValidateEditText.TYPE_PDT);
		mEdtPswOld.setNextEditText(mEdtName);
		
		mEdtName.setEditHint("请输入姓名");
		mEdtName.setNextEditText(mEdtIdentity);
		
		mEdtIdentity.setmType(ValidateEditText.TYPE_IDN);
		mEdtIdentity.setEditHint("请输入身份证号");
		mEdtIdentity.setNextEditText(mEdtPswRemote);
		
		mEdtPswRemote.setEditHint("请输入远程控制密码");
		mEdtPswRemote.setmLength(6);
		mEdtPswRemote.setmType(ValidateEditText.TYPE_PDN);
		mEdtPswRemote.setNextEditText(mEdtPhoneNew);
		
		mEdtPhoneNew.setEditHint("请输入新主机手机号码");
		mEdtPhoneNew.setmLength(11);
		mEdtPhoneNew.setmType(ValidateEditText.TYPE_NUM);
		mEdtPhoneNew.setNextEditText(mEdtCode);
		
		mEdtCode.setEditPaddingRightDP(100);
		mEdtCode.setmType(ValidateEditText.TYPE_NUM);
		mEdtCode.setEditHint("请输入验证码");
		mEdtCode.setNextEditText(mEdtPswNew);
		
		mEdtPswNew.setEditHint("请输入新登录密码");
		mEdtPswNew.setmType(ValidateEditText.TYPE_PDT);
		mEdtPswNew.setNextEditText(mEdtPswNewSure);
		
		mEdtPswNewSure.setEditHint("请再次输入新登录密码");
		mEdtPswNewSure.setmType(ValidateEditText.TYPE_COFT);
		mEdtPswNewSure.setmConfirmEdit(mEdtPswNew.getmEditText());
		
		
		
		
		mTxtCode = (TextView) findViewById(R.id.verify_all_txt_code);
		mBtnOption = (TextView) findViewById(R.id.verify_all_txt_option);

		mTxtCode.setOnClickListener(this);
		mBtnOption.setOnClickListener(this);

	}

	/**
	 * 倒计时
	 */
	private int count = 60;

	private Timer timer = new Timer();

	private TimerTask task;

	// 语音播报view
	private GetValidateView mValidateView;

	private String phoneNum;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.verify_all_txt_code:
			// 获取验证码
			mEdtPhoneNew.validateEdit();
			phoneNum = mEdtPhoneNew.getText().toString();
			if (phoneNum != null && phoneNum.length() == 11) {
				CPControl.GetMessageValidateResult(CPControl.VALIDATE_TYPE_AUTHORMAIN, phoneNum, listener_code);
				count = 60;
				mTxtCode.setText(count + "秒后重发");
				mTxtCode.setClickable(false);
				mTxtCode.setBackgroundResource(R.drawable.btn_code_gray);

				task = new TimerTask() {

					@Override
					public void run() {
						Message msg = new Message();
						msg.what = 100;
						mHandler.sendMessage(msg);

					}
				};
				timer.schedule(task, 1000, 1000);
			} else {
				UUToast.showUUToast(VerifyAllActivity.this, "请输入正确的手机号");
			}
			break;
		case R.id.verify_all_txt_option:
			// 更换主机
			String phoneOld = mEdtPhoneOld.getmEditText().getText().toString();
			mEdtPhoneOld.validateEdit();
			String pswOld = mEdtPswOld.getText().toString();
			mEdtPswOld.validateEdit();
			String authen_name = mEdtName.getText().toString();
			mEdtName.validateEdit();
			String authen_identity = mEdtIdentity.getText().toString();
			mEdtIdentity.validateEdit();
			//暂时屏蔽掉
//			String psw_remote = mEdtPswRemote.getText().toString();
//			mEdtPswRemote.validateEdit();
			String psw_remote = "";
			//
			String phoneNew = mEdtPhoneNew.getText().toString();
			mEdtPhoneNew.validateEdit();
			String phoneCode = mEdtCode.getText().toString();
			mEdtCode.validateEdit();
			String pswNew = mEdtPswNew.getText().toString();
			mEdtPswNew.validateEdit();
			String pswNewSure = mEdtPswNewSure.getText().toString();
			mEdtPswNewSure.validateEdit();
			String checkPsw = CheckInfo.checkPassword(pswNew);
			
			if (phoneOld == null || phoneOld.length() != 11) {
				UUToast.showUUToast(VerifyAllActivity.this, "请输入正确的旧主机号码...");
				return;
			} else if (pswOld == null || pswOld.equals("")) {
				UUToast.showUUToast(VerifyAllActivity.this, "您还没有填写您的旧登录密码哦...");
				return;
			} else if (authen_name == null || authen_name.equals("")) {
				UUToast.showUUToast(VerifyAllActivity.this, "您还没有填写您的姓名哦...");
				return;
			} else if (authen_identity == null || authen_identity.equals("")) {
				UUToast.showUUToast(VerifyAllActivity.this, "您还没有填写您的身份证号哦...");
				return;
			} /*else if (psw_remote == null || psw_remote.equals("")) { // 先屏蔽掉
				UUToast.showUUToast(VerifyAllActivity.this, "您还没有填写您的远程密码哦...");
				return;
			} */else if (phoneNew == null || phoneNew.length() != 11) {
				UUToast.showUUToast(VerifyAllActivity.this, "请输入正确的新主机电话号码...");
				return;
			} else if (phoneCode == null || phoneCode.equals("")) {
				UUToast.showUUToast(VerifyAllActivity.this, "您还没有填写验证码哦...");
				return;
			} else if (!checkPsw.equals(CheckInfo.CORRECT_PSWLENTH)) {
				UUToast.showUUToast(VerifyAllActivity.this, checkPsw);
				return;
			} else if (pswNewSure == null || pswNewSure.equals("")) {
				UUToast.showUUToast(VerifyAllActivity.this, "请再次填写您的新登录密码...");
				return;
			} else if (!pswNew.equals(pswNewSure)) {
				UUToast.showUUToast(VerifyAllActivity.this, "您两次填写的新登录密码不一致，请重新输入...");
			} else {
				if (mDialog == null) {
					mDialog = PopBoxCreat.createDialogWithProgress(VerifyAllActivity.this, "信息提交中...");
				}
				mDialog.show();
				// 调用安全验证登录
				CPControl.GetNoAuthorizeChangeDeviceResult(phoneOld,
					pswOld,
					authen_name,
					authen_identity,
					psw_remote,
					phoneNew,
					phoneCode,
					pswNew,
					mobile_id,
					mobile_name,
						DorideApplication.MODEL,
					listener_change);
			}
			break;

		}
	}

	// listener-获取验证码
	private GetResultListCallback listener_code = new GetResultListCallback() {

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

	// listener-更换主机
	private GetResultListCallback listener_change = new GetResultListCallback() {

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

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			BaseResponseInfo mInfo;
			String infoMsg;
			switch (msg.what) {
			case 0:
				// 获取验证码成功
				UUToast.showUUToast(VerifyAllActivity.this, msg.obj.toString());
				break;
			case 1:
				// 获取验证码失败
				// 停止计时
				if (timer != null) {
					if (task != null) {
						task.cancel();
					}
				}
				mTxtCode.setClickable(true);
				mTxtCode.setText(R.string.usercenter_push_validate1);
				mTxtCode.setBackgroundResource(R.drawable.btn_code_bg);
				mInfo = (BaseResponseInfo) msg.obj;
				if (mInfo != null) {
					String info = mInfo.getInfo();
					if (info != null && info.length() > 0) {
						infoMsg = info;
					} else {
						infoMsg = "获取验证码失败...";
					}
				} else {
					infoMsg = "获取验证码失败...";
				}

				BaseResponseInfo mBaseResponseInfo = (BaseResponseInfo) msg.obj;
				int flag = mBaseResponseInfo.getFlag();
				if (flag == BaseResponseInfo.VALIDATE_LIMIT) {
					if (mValidateView == null) {
						mValidateView = new GetValidateView(context, CPControl.VALIDATE_TYPE_AUTHORMAIN, phoneNum);
					}
					mValidateView.setClickStatus(true);
					mValidateView.showMenu();
				} else {
					UUToast.showUUToast(VerifyAllActivity.this, infoMsg);
				}

				break;
			case 2:
				if (mDialog != null) {
					mDialog.dismiss();
				}
//				setResult(LoginActivity.REQUEST_CODE + 1);
				finish();
				break;

			case 3:
				if (mDialog != null) {
					mDialog.dismiss();
				}
				mInfo = (BaseResponseInfo) msg.obj;
				if (mInfo != null) {
					String info = mInfo.getInfo();
					if (info != null && info.length() > 0) {
						infoMsg = info;
					} else {
						infoMsg = "主机更换失败...";
					}
				} else {
					infoMsg = "主机更换失败...";
				}

				UUToast.showUUToast(VerifyAllActivity.this, infoMsg);
				break;
			case 100:
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
			}
		}
	};

}
