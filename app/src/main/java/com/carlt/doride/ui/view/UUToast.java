package com.carlt.doride.ui.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.carlt.doride.R;



/**
 * 作者：秋良
 * 
 * 描述：此类用于用户提示信息显示 均为静态方法，可直接调用
 */
public class UUToast extends Toast {

	private static LayoutInflater inflater;

	private static Toast toast;

	private static UUToast uuTo;

	private static TextView text;
	private static CharSequence oldContent ="";
	private static  long oldTime ;

	/**
	 *  500 毫秒后吐司 消失
	 */
	private static  int hideTime = 800;
	private static Handler toastHideHandle = new Handler(Looper.getMainLooper());


	@SuppressLint("WrongConstant")
	private UUToast(Context context) {
		super(context);
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.toast, null);
		text = (TextView) layout.findViewById(R.id.toast_message);
		toast = new Toast(context);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(layout);
		toast.setGravity(Gravity.CENTER, 0, 0);
	}

	@SuppressLint("WrongConstant")
	public static void showUUToast(Context context, CharSequence tex,
								   int duration) {
		if (uuTo == null) {
			uuTo = new UUToast(context);
		} else {
			uuTo.cancel();
//			uuTo = new UUToast(context);
		}
		text.setText(tex);
		toast.show();
//		ToastUtils.setGravity(Gravity.CENTER,0,0);
//		ToastUtils.showShort(tex);
//		toastHideHandle.postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				ToastUtils.cancel();
//			}
//		}, hideTime);
	}

	public static void showUUToast(Context context, CharSequence tex) {
		// 优化吐司,机制..相同吐司30秒内不弹
		showUUToast(context, tex, Toast.LENGTH_SHORT);

	;
	}


}
