package com.carlt.sesame.ui.view.general;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.carlt.sesame.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.data.LoginInfo;
import com.carlt.sesame.ui.view.PopBoxCreat;
import com.carlt.sesame.utility.UUToast;

/**
 * 通用座驾-寻车功能View
 * 
 * @author daisy
 */
public class GeneralFindView extends GeneralBaseView {

	private Dialog mDialog;

	private long click_time;// 点击View时的时间

	private final static long DURATION = 10 * 1000;// 超时判断时长

	private final static long DURATION_EVERY = 5 * 1000;// 轮询间隔

	public GeneralFindView(Context context) {
		super(context);
		int[] imgIds = { R.drawable.general_find_gray,
				R.drawable.general_find_light, R.drawable.general_find_light,
				R.drawable.general_find_dark };
		setImgIds(imgIds);
		String[] stateStrings = { "声光寻车/暂不支持", "声光寻车", "声光寻车", "声光寻车" };
		setStateStrings(stateStrings);
		setState(LoginInfo.getSLCarLocating());
	}

	@Override
	public void setState(int state) {
		if (LoginInfo.getSLCarLocating() == LoginInfo.SLCar_NONE) {
			state = GeneralBaseView.STATE_UNAVAILABLE;
		} else if (LoginInfo.getSLCarLocating() == LoginInfo.SLCar_SUPPORT) {
			state = GeneralBaseView.STATE_AVAILABLE_SINGLE;
		}
		super.setState(state);
	}

	@Override
	protected void onViewClick() {
		if (mDialog == null) {
			mDialog = PopBoxCreat
					.createDialogWithProgress(context, "正在连接爱车...");
		}

		mDialog.show();
		// TODO 调用接口
		click_time = System.currentTimeMillis();
		mHandler.sendEmptyMessage(2);

	}

	private GetResultListCallback listener = new GetResultListCallback() {

		@Override
		public void onFinished(Object o) {
			mHandler.sendEmptyMessage(0);

		}

		@Override
		public void onErro(Object o) {
			Message msg = new Message();
			msg.what = 1;
			msg.obj = o;
			mHandler.sendMessage(msg);

		}
	};

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				// 成功
				if (mDialog != null && mDialog.isShowing()) {
					mDialog.dismiss();
				}
				UUToast.showUUToast(context, "操作成功");
				break;
			case 1:
				// 失败
				long current_time = System.currentTimeMillis();
				long d_valuew = current_time - click_time;
				if (d_valuew < DURATION) {

					BaseResponseInfo mInfo = (BaseResponseInfo) msg.obj;
					if (mInfo.getFlag() > 3000) {
						// 提示失败信息
						if (mDialog != null && mDialog.isShowing()) {
							mDialog.dismiss();
						}
						if (mInfo != null) {
							UUToast.showUUToast(context, mInfo.getInfo());
						}
					} else {
						// 继续调用启动接口
						mHandler.sendEmptyMessageDelayed(2, DURATION_EVERY);
					}

				} else {
					// 提示失败信息
					if (mDialog != null && mDialog.isShowing()) {
						mDialog.dismiss();
					}
					BaseResponseInfo mInfo = (BaseResponseInfo) msg.obj;
					if (mInfo != null) {
						UUToast.showUUToast(context, mInfo.getInfo());
					}

				}
				break;
			case 2:
				// 调用接口
				CPControl.GetCarLocating(listener);
				break;
			}
		}
	};
}
