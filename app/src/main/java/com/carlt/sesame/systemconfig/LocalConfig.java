package com.carlt.sesame.systemconfig;

import android.os.Environment;

import com.carlt.doride.DorideApplication;

public class LocalConfig {

	/**
	 * 记录图片资源保存PATH SD卡
	 */
	public static String mImageCacheSavePath_SD = Environment
			.getExternalStorageDirectory().toString() + "/sesame/img/";

	/**
	 * 记录图片资源保存PATH 内部存储器
	 */
	public static String mImageCacheSavePath_Absolute = DorideApplication.ApplicationContext
			.getCacheDir().getAbsolutePath() + "/sesame/img/";

	/**
	 * 记录下载文件保存PATH SD卡
	 */
	public static String mDownLoadFileSavePath_SD = Environment
			.getExternalStorageDirectory().toString() + "/sesame/down/";

	/**
	 * 记录下载文件保存PATH 内部存储器
	 */
	public static String mDownLoadFileSavePath_Absolute = DorideApplication.ApplicationContext
			.getCacheDir().getAbsolutePath() + "/sesame/down/";

	/**
	 * 记录下载文件保存PATH SD卡
	 */
	public static String mErroLogSavePath_SD = Environment
			.getExternalStorageDirectory().toString() + "/sesame/log/";
	
	/**
	 * 记录轨迹文件保存PATH SD卡
	 */
	public static String mTracksSavePath_SD = Environment
			.getExternalStorageDirectory().toString() + "/sesame/tracks/";

}
