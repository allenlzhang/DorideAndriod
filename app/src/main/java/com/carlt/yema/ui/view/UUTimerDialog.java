package com.carlt.yema.ui.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

public class UUTimerDialog extends UUDialog {

	private Timer mTimer;

	private int t = 60;

	private TimerTask mTask = new TimerTask() {
		@Override
		public void run() {
			t--;
			Message msg = new Message();
			msg.what = 0;
			msg.arg1 = t;
			mHandler.sendMessage(msg);
		}
	};

	public UUTimerDialog(Context context) {
		super(context);

	}

	public void setTime(int t) {
		this.t = t;
	}

	@Override
	public void show() {
		super.show();
		if (mTimer == null) {
			mTimer = new Timer();
			mTimer.schedule(mTask, 0, 1000);
		}
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
						watch.setText("(" + msg.arg1 + ")");
					} else {
						watch.setText("(" + "再等等" + ")");
						destoryTimer();
					}

				}
				break;
			}
		}
	};

	private void destoryTimer() {
		if (mTimer != null) {
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

}
