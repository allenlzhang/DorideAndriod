
package com.carlt.doride.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.carlt.doride.DorideApplication;
import com.carlt.doride.R;

import java.util.Timer;
import java.util.TimerTask;

public class UUAuthorDialog extends Dialog implements android.view.View.OnClickListener {

	protected TextView content1;

	protected TextView content2;

	protected TextView watch;

	protected TextView btnLeft;// 左侧按钮

	protected TextView btnRight;// 右侧按钮

	private Timer mTimer;

	private int t;

	private final static int COUNT = 60;

	private final static int w_dip = 300;

	private PopBoxCreat.DialogWithTitleClick mDialogWithTitleClick;

	private OnTimeOutListener mTimeOutListener;

	public UUAuthorDialog(Context context) {
		super(context, R.style.dialog);
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.dialog_author, null);
		content1 = (TextView) view.findViewById(R.id.dialog_author_text_content1);
		content2 = (TextView) view.findViewById(R.id.dialog_author_text_content2);

		btnLeft = (TextView) view.findViewById(R.id.dialog_author_text_btnleft);
		watch = (TextView) view.findViewById(R.id.dialog_author_text_watch);
		int w = (int) (DorideApplication.ScreenDensity * w_dip);
		setCanceledOnTouchOutside(true);
		ViewGroup.LayoutParams parm = new ViewGroup.LayoutParams(w, LayoutParams.WRAP_CONTENT);
		setContentView(view, parm);
		setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		
		setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				if(mTimeOutListener != null){
					mTimeOutListener.onTimeOut();
				}
			}
		});
	}

	private TimerTask mTask;

	public void setTime(int t) {
		this.t = t;
	}

	@Override
	public void show() {
		super.show();
		createTimer();
		visibleWatch();
	}

	@Override
	public void dismiss() {
		destoryTimer();
		super.dismiss();

	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				// 秒表刷新
				if (watch != null) {
					if (msg.arg1 > -1) {
						watch.setText("(" + msg.arg1 + "S)");
					} else {
						if (mTimeOutListener != null) {
							mTimeOutListener.onTimeOut();
						}
						btnLeft.setTextColor(Color.parseColor("#666666"));
						btnLeft.setClickable(true);
						destoryTimer();
					}

				}
				break;
			}
		}
	};

	private void createTimer() {
		t = COUNT;
		if (mTimer == null) {
			mTimer = new Timer();
			mTask = new TimerTask() {
				@Override
				public void run() {
					t--;
					Message msg = new Message();
					msg.what = 0;
					msg.arg1 = t;
					mHandler.sendMessage(msg);
				}
			};
			btnLeft.setTextColor(Color.parseColor("#999999"));
			btnLeft.setClickable(false);
			mTimer.schedule(mTask, 0, 1000);
		}
	}

	private void destoryTimer() {
		if (mTimer != null) {
			if (mTask != null) {
				mTask.cancel();
			}
			mTimer.cancel();
			mTimer = null;
		}
	}

	public void stopTimer() {
		watch.setText("(0S)");
		btnLeft.setTextColor(Color.parseColor("#666666"));
		btnLeft.setClickable(true);
		if (mTimer != null) {
			if (mTask != null) {
				mTask.cancel();
			}
			mTimer.cancel();
			mTimer = null;
		}
	}

	public void goneWatch() {
		if (watch != null) {
			watch.setVisibility(View.INVISIBLE);
		}
	}

	public void visibleWatch() {
		if (watch != null) {
			watch.setVisibility(View.VISIBLE);
		}
	}

	public void setContentText(String content1, String content2) {
		if (this.content1 != null) {
			this.content1.setText(content1);
		}

		if (this.content2 != null) {
			this.content2.setText(content2);
		}
	}

	public void setBtnText(String btnLeft, String btnRight) {
		if (this.btnLeft != null) {
			this.btnLeft.setText(btnLeft);
		}

		if (this.btnRight != null) {
			this.btnRight.setText(btnRight);
		}
	}

	public void setmDialogWithTitleClick(PopBoxCreat.DialogWithTitleClick mDialogWithTitleClick) {
		if (mDialogWithTitleClick != null) {
			this.mDialogWithTitleClick = mDialogWithTitleClick;
			this.btnLeft.setOnClickListener(this);
//			this.btnRight.setOnClickListener(this);
		}
	}

	public void setmTimeOutListener(OnTimeOutListener mTimeOutListener) {
		this.mTimeOutListener = mTimeOutListener;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_author_text_btnleft:
			mDialogWithTitleClick.onLeftClick();
			createTimer();
			break;

		// case R.id.dialog_author_text_btnright:
		// mDialogWithTitleClick.onRightClick();
		// createTimer();
		// break;
		}
	}
	
	

	public interface OnTimeOutListener {
		void onTimeOut();
	};

}
