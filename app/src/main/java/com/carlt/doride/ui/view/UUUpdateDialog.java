package com.carlt.doride.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.doride.YemaApplication;
import com.carlt.doride.control.CPControl;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.data.DeviceUpdateInfo;
import com.carlt.doride.model.LoginInfo;
import com.carlt.doride.protocolparser.BaseParser;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 升级提示框
 * 
 * @author Daisy
 */
public class UUUpdateDialog extends Dialog {

	protected TextView txtProgress;// 进度展示

	private final static int w_dip = 154;

	private Timer mTimer;

	private int progressValue = 0;

	private Context mContext;

	private DialogUpdateListener mDialogUpdateListener;

	private TimerTask mTask = new TimerTask() {
		@Override
		public void run() {
			progressValue++;
			Message msg = new Message();
			msg.what = 100;
			msg.arg1 = progressValue;
			mHandler.sendMessage(msg);
		}
	};

	public UUUpdateDialog(Context context,
                          DialogUpdateListener dialogUpdateListener) {
		super(context, R.style.dialog);
		mContext = context;
		this.mDialogUpdateListener = dialogUpdateListener;
		progressValue = 0;
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.dialog_update, null);
		txtProgress = (TextView) view
				.findViewById(R.id.update_loading_txt_progress);
		Animation operatingAnim = AnimationUtils.loadAnimation(context,
				R.anim.dialog_rotate);
		LinearInterpolator lin = new LinearInterpolator();
		operatingAnim.setInterpolator(lin);
		int w = (int) (YemaApplication.ScreenDensity * w_dip);
		setCanceledOnTouchOutside(false);
		setCancelable(false);
		ViewGroup.LayoutParams parm = new ViewGroup.LayoutParams(w, w);
		setContentView(view, parm);
		setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
				return true;
			}
		});
	}

	public void setDialogUpdateListener(
			DialogUpdateListener dialogUpdateListener) {
		this.mDialogUpdateListener = dialogUpdateListener;
	}

	@Override
	public void show() {
		super.show();
		if (mTimer == null) {
			mTimer = new Timer();
			mTimer.schedule(mTask, 0, 3000);
			polling();
		}
	}

	// 轮询
	private void polling() {
		String s = LoginInfo.getDeviceidstring();
		CPControl.GetDeviceUpdateResult(s, listener_polling);
	}

	protected BaseParser.ResultCallback listener_polling = new BaseParser.ResultCallback() {

		@Override
		public void onSuccess(BaseResponseInfo bInfo) {
			Message msg = new Message();
			msg.what = 0;
			msg.obj = bInfo;
			mHandler.sendMessage(msg);
		}

		@Override
		public void onError(BaseResponseInfo bInfo) {
			Message msg = new Message();
			msg.what = 1;
			msg.obj = bInfo;
			mHandler.sendMessage(msg);

		}
	};

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				// 升级成功
				if (msg.obj == null) {
					return;
				}
				DeviceUpdateInfo mInfo = (DeviceUpdateInfo) msg.obj;
				if (mInfo != null) {
					if (mInfo.isUpgrading()) {
						// LoginInfo.setUpgradeing(true);
						// 升级失败
						if (progressValue > -1 && progressValue < 121) {
							// 六分钟内继续轮询
							polling();
						} else {
							destoryTimer();
							UUToast.showUUToast(mContext, "大乘盒子升级失败...");
							dismiss();
							if (mDialogUpdateListener != null) {
								mDialogUpdateListener.onFailed();
							}
						}
						return;
					} else {
						// LoginInfo.setUpgradeing(false);
						String s = LoginInfo.getDeviceidstring();
						txtProgress.setText("升级中(100%)升级成功");
					}
				}
				mHandler.sendEmptyMessageAtTime(101, 1 * 1000);
				break;
			case 1:
				// 升级失败
				if (progressValue > -1 && progressValue < 121) {
					// 六分钟内继续轮询
					polling();
				} else {
					destoryTimer();
					UUToast.showUUToast(mContext, "大乘盒子升级失败...");
					dismiss();
					if (mDialogUpdateListener != null) {
						mDialogUpdateListener.onFailed();
					}
				}
				break;
			case 100:
				// 进度刷新
				if (txtProgress != null) {
					StringBuffer mBuffer = new StringBuffer("升级中(");
					if (msg.arg1 > -1 && msg.arg1 < 100) {
						mBuffer.append(msg.arg1);
						mBuffer.append("%");
						mBuffer.append(")");
						txtProgress.setText(mBuffer.toString());
					} else {
						mBuffer.append("99%)");
						txtProgress.setText(mBuffer.toString());
					}
				}
				break;
			case 101:
				// 升级成功，dialog消失
				destoryTimer();
				dismiss();
				if (mDialogUpdateListener != null) {
					mDialogUpdateListener.onSuccess();
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

	public interface DialogUpdateListener {
		// 升级失败
		void onFailed();

		// 升级成功
		void onSuccess();
	}
}
