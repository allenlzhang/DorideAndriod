
package com.carlt.sesame.ui.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.sesame.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.data.UseInfo;
import com.carlt.sesame.preference.UseInfoLocal;
import com.carlt.sesame.ui.activity.base.BaseActivity;
import com.carlt.sesame.ui.view.GetValidateView;
import com.carlt.sesame.ui.view.ValidateEditText;
import com.carlt.sesame.utility.UUToast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 安全验证页面
 * 
 * @author Administrator
 */
public class ManageCheckActivity extends BaseActivity implements OnClickListener {
	// 头部返回键
	private ImageView back;
	// 标题文字
	private TextView title;
	// 头部右侧文字
	private TextView txtRight;
	// 主机号码
	private ValidateEditText mPhone;
	// 验证码
	private ValidateEditText mCheckCode;
	// 确认按钮
	private TextView mTxtConfirm;
	// 发送按钮
	private TextView mTxtSend;

	private String phoneNum;
	private int count;
	private TimerTask task;
	private Timer mTimer;

	// 验证码相关
	private GetValidateView mValidateView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_check);
		mTimer = new Timer();

		initTitle();
		init();
	}

	private void initTitle() {
		back = (ImageView) findViewById(R.id.head_back_img1);
		title = (TextView) findViewById(R.id.head_back_txt1);
		txtRight = (TextView) findViewById(R.id.head_back_txt2);

		back.setImageResource(R.drawable.arrow_back);
		title.setText("手机验证");

		txtRight.setVisibility(View.GONE);

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	private void init() {

		mPhone = (ValidateEditText) findViewById(R.id.manageCheck_edit1);
		mCheckCode = (ValidateEditText) findViewById(R.id.manageCheck_edit2);
		
		mPhone.setEditHint("请输入手机号码");
		mPhone.setmType(ValidateEditText.TYPE_PON);
		mPhone.setNextEditText(mCheckCode);
		
		mCheckCode.setEditHint("请输入验证码");
		mTxtConfirm = (TextView) findViewById(R.id.manageCheck_btn);
		mTxtSend = (TextView) findViewById(R.id.manageCheck_send);

		mTxtConfirm.setOnClickListener(this);
		mTxtSend.setOnClickListener(this);
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				UUToast.showUUToast(context, msg.obj.toString());
				break;

			case 1:
				// 停止计时
				if (mTimer != null) {
					if (task != null) {
						task.cancel();
					}
				}
				mTxtSend.setClickable(true);
				mTxtSend.setText(R.string.usercenter_push_validate1);
				mTxtSend.setBackgroundResource(R.drawable.btn_code_bg);

				BaseResponseInfo mBaseResponseInfo = (BaseResponseInfo) msg.obj;
				int flag = mBaseResponseInfo.getFlag();
				if (flag == BaseResponseInfo.VALIDATE_LIMIT) {
					if (mValidateView == null) {
						mValidateView = new GetValidateView(context, CPControl.VALIDATE_TYPE_CHANGE, phoneNum);
					}
					mValidateView.setClickStatus(true);
					mValidateView.showMenu();
				} else {
					UUToast.showUUToast(context, "验证码获取失败:" + mBaseResponseInfo.getInfo());
				}
				break;
			case 10:
				count--;
				if (count > 0) {
					mTxtSend.setText(count + "秒后重发");
				} else {
					if (mTimer != null) {
						if (task != null) {
							task.cancel();
						}
					}
					mTxtSend.setClickable(true);
					mTxtSend.setText(R.string.usercenter_push_validate1);
					mTxtSend.setBackgroundResource(R.drawable.btn_code_bg);
				}
				break;
			case 11:
				BaseResponseInfo infoTmp11 = (BaseResponseInfo) msg.obj;
				if (null != infoTmp11 && null != infoTmp11.getInfo() && infoTmp11.getInfo().length() > 0) {
					UUToast.showUUToast(context, infoTmp11.getInfo());
				} else {
					UUToast.showUUToast(context, "验证成功");
				}
				// ---跳转到绑定页面
				Intent mIntent = new Intent(context, ManageReBindActivity.class);
				startActivity(mIntent);
				finish();
				break;
			case 12:
				BaseResponseInfo infoTmp12 = (BaseResponseInfo) msg.obj;
				if (null != infoTmp12 && null != infoTmp12.getInfo() && infoTmp12.getInfo().length() > 0) {
					UUToast.showUUToast(context, infoTmp12.getInfo());
				} else {
					UUToast.showUUToast(context, "验证码错误！");
				}
				break;
			}
		}

	};

	// 获取验证码回掉
	GetResultListCallback mCheckCodeListener = new GetResultListCallback() {

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

	@Override
	public void onClick(View v) {
		// --调用接口
		if (v.equals(mTxtConfirm)) {
			
			mCheckCode.validateEdit();
			mPhone.validateEdit();
			String code = mCheckCode.getText().toString();
			phoneNum = mPhone.getText().toString();
			
			if (TextUtils.isEmpty(phoneNum)) {
				UUToast.showUUToast(context, "请输入手机号");
				return;
			}

			if (TextUtils.isEmpty(code)) {
				UUToast.showUUToast(context, "请输入验证码");
				return;
			}

			CPControl.GetValidateCheckResult(phoneNum, CPControl.VALIDATE_TYPE_CHANGE, code, new GetResultListCallback() {

				@Override
				public void onFinished(Object o) {
					Message msg = new Message();
					msg.obj = o;
					msg.what = 11;
					mHandler.sendMessage(msg);
				}

				@Override
				public void onErro(Object o) {
					Message msg = new Message();
					msg.obj = o;
					msg.what = 12;
					mHandler.sendMessage(msg);
				}
			});

		} else if (v.equals(mTxtSend)) {
			mPhone.validateEdit();
			phoneNum = mPhone.getText().toString();
			if (phoneNum != null && phoneNum.length() == 11) {
			    UseInfo ui = UseInfoLocal.getUseInfo();
	            if(!phoneNum.equals(ui.getAccount())){
	                UUToast.showUUToast(context, "输入的手机号与当前登录账号不匹配");
	                return;
	            }
				CPControl.GetMessageValidateResult(CPControl.VALIDATE_TYPE_CHANGE,
					mPhone.getText().toString(),
					mCheckCodeListener);
				count = 60;
				mTxtSend.setText(count + "秒后重发");
				mTxtSend.setClickable(false);
				mTxtSend.setBackgroundResource(R.drawable.btn_code_gray);

				task = new TimerTask() {

					@Override
					public void run() {
						Message msg = new Message();
						msg.what = 10;
						mHandler.sendMessage(msg);
					}
				};
				mTimer.schedule(task, 1000, 1000);
			} else {
				UUToast.showUUToast(context, "请输入正确的手机号");
			}
		}
	}

}
