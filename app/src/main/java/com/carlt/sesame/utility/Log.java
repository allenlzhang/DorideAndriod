package com.carlt.sesame.utility;

/**
 * 作者：秋良
 * 
 * 描述：Log类
 * 
 * 规约：所有Log打印应调用此类，以便所有日志信息有统一出口
 */
public class Log {

	public static boolean flag = true;

	public static void e(String tag, String info) {
		if (flag) {
			android.util.Log.e(tag, info);
		}

	}

	public static void v(String tag, String info) {
		if (flag) {
			android.util.Log.v(tag, info);
		}

	}

	public static void i(String tag, String info) {
		if (flag) {
			android.util.Log.i(tag, info);

		}

	}
}
