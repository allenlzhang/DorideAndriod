package com.carlt.sesame.ui.activity.usercenter.login;

import android.annotation.SuppressLint;
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
import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.ui.activity.base.BaseActivity;
import com.carlt.sesame.ui.view.ValidateEditText;
import com.carlt.sesame.utility.UUToast;

import java.util.Timer;
import java.util.TimerTask;


public class CheckHostActivity extends BaseActivity implements OnClickListener {

	private ImageView back;// 头部返回键

	private TextView title;// 标题文字

	private TextView txtRight;// 头部右侧文字

	private ValidateEditText mEdit;

	/**
	 * 获取验证码
	 */
	private TextView mSendMsg;
	/**
	 * 确认
	 */
	private TextView tvOK;
	
	private String phoneNum = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_host_phone);
		initTitle();
		init();
	}

	private void init() {
		mEdit = (ValidateEditText)findViewById(R.id.check_host_edit);
		mSendMsg = (TextView) findViewById(R.id.check_host_send);
		tvOK = (TextView) findViewById(R.id.check_host_btn);
		mEdit.setEditHint("请输入验证码");
		mEdit.setmType(ValidateEditText.TYPE_NUM);
		mSendMsg.setOnClickListener(this);
		tvOK.setOnClickListener(this);
		try {
			phoneNum = getIntent().getStringExtra("phoneNum");
		} catch (Exception e) {
			
		}
	
	}

	private void initTitle() {
		back = (ImageView) findViewById(R.id.head_back_img1);
		title = (TextView) findViewById(R.id.head_back_txt1);
		txtRight = (TextView) findViewById(R.id.head_back_txt2);

		back.setImageResource(R.drawable.arrow_back);
		title.setText("验证主机");
		txtRight.setVisibility(View.GONE);

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}
	
    /**
     * 倒计时
     */
    private int count = 60;

    private Timer timer = new Timer();

    private TimerTask task;
	
	// listener-获取验证码
		private CPControl.GetResultListCallback listener_code = new CPControl.GetResultListCallback() {

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
	    switch (v.getId()) {
        case R.id.check_host_send:
         
                CPControl.GetMessageValidateResult(CPControl.VALIDATE_TYPE_AUTHORMAIN,
                		phoneNum, listener_code);
                count = 60;
                mSendMsg.setText(count + "秒后重发");
                mSendMsg.setClickable(false);
                mSendMsg.setBackgroundResource(R.drawable.btn_code_gray);

                task = new TimerTask() {

                    @Override
                    public void run() {
                        Message msg = new Message();
                        msg.what = 100;
                        mHandler.sendMessage(msg);

                    }
                };
                timer.schedule(task, 1000, 1000);
        	break;
        case R.id.check_host_btn:
        	String validate = mEdit.getText().toString().trim();
            if (validate != null && validate.length() > 0) {
            	CPControl.GetUpdateMoveDeviceidResult(DorideApplication.NIMEI, validate, phoneNum,new CPControl.GetResultListCallback() {
					
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
				});
            } else {
                UUToast.showUUToast(CheckHostActivity.this, "验证码不能为空");
                return;
            }

        	break;
	    }
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			BaseResponseInfo mInfo;
			String infoMsg;
			switch (msg.what) {
			case 0:
				// 获取验证码成功
				UUToast.showUUToast(CheckHostActivity.this, msg.obj.toString());
				break;
			case 1:
				// 获取验证码失败
				// 停止计时
				if (timer != null) {
					if (task != null) {
						task.cancel();
					}
				}
				mSendMsg.setClickable(true);
				mSendMsg.setText(R.string.usercenter_push_validate1);
				mSendMsg.setBackgroundResource(R.drawable.btn_code_bg);
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
				UUToast.showUUToast(CheckHostActivity.this, infoMsg);

				break;
			case 2://验证主机成功
				UUToast.showUUToast(CheckHostActivity.this, "验证主机成功");
				setResult(2001);
				finish();
				break;
			case 3:
				mInfo = (BaseResponseInfo) msg.obj;
				if (mInfo != null) {
					String info = mInfo.getInfo();
					if (info != null && info.length() > 0) {
						infoMsg = info;
					} else {
						infoMsg = "验证失败";
					}
				} else {
					infoMsg = "验证失败";
				}
				UUToast.showUUToast(CheckHostActivity.this, infoMsg);
				break;
			case 100:
				count--;
				if (count > 0) {
					mSendMsg.setText(count + "秒后重发");
				} else {
					if (timer != null) {
						if (task != null) {
							task.cancel();
						}
					}
					mSendMsg.setClickable(true);
					mSendMsg.setText(R.string.usercenter_push_validate1);
					mSendMsg.setBackgroundResource(R.drawable.btn_code_bg);
				}
				break;
			}
		}
	};
}
