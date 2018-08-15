package com.carlt.doride.ui.view;


import android.content.Context;
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

	private UUToast(Context context) {
		super(context);
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.toast, null);
		text = (TextView) layout.findViewById(R.id.toast_message);
		toast = new Toast(context);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(layout);
		toast.setGravity(Gravity.CENTER, 0, 0);
	}

	public static void showUUToast(Context context, CharSequence tex,
                                   int duration) {
		if (uuTo == null) {
			uuTo = new UUToast(context);
		} else {
			uuTo.cancel();
			uuTo = new UUToast(context);
		}
		text.setText(tex);
		toast.show();
	}

	public static void showUUToast(Context context, CharSequence tex) {
		showUUToast(context, tex, Toast.LENGTH_LONG);
	}

}
