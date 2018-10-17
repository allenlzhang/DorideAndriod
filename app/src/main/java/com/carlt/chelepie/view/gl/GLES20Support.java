package com.carlt.chelepie.view.gl;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ConfigurationInfo;

public class GLES20Support {

	/**
	 * 判断是否支持软解码
	 * @param context
	 * @return true 表示支持，表示不支持
	 */
	public static boolean detectOpenGLES20(Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		ConfigurationInfo info = am.getDeviceConfigurationInfo();
		return (info.reqGlEsVersion >= 0x20000);
	}

	public static Dialog getNoSupportGLES20Dialog(final Activity activity) {
		AlertDialog.Builder b = new AlertDialog.Builder(activity);
		b.setCancelable(false);
		b.setTitle("not support");
		b.setMessage("不支持");
		b.setNegativeButton("退出", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				activity.finish();
			}
		});
		return b.create();
	}
}
