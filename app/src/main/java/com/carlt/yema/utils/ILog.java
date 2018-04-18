package com.carlt.yema.utils;

import android.text.TextUtils;

/**
 * 描述：Log类
 * 
 * 规约：所有Log打印应调用此类，以便所有日志信息有统一出口
 */
public class ILog {

	public static boolean flag = true;

	public static void e(String tag, String info) {
		if (flag) {
            tag = generateSurrfix(tag);
			android.util.Log.e(tag, info);
		}

	}

	public static void v(String tag, String info) {
		if (flag) {
            tag = generateSurrfix(tag);
			android.util.Log.v(tag, info);
		}

	}

	public static void i(String tag, String info) {
		if (flag) {
            tag = generateSurrfix(tag);
			android.util.Log.i(tag, info);
		}

	}
	
	public static void d(String tag, String info) {
        if (flag) {
            tag = generateSurrfix(tag);
            android.util.Log.d(tag, info);
        }

    }

    /**
     *  给Tag 添加调用者信息，包括类名，方法名，代码行
     * @param tag
     * @return
     */
    private static String generateSurrfix(String tag){
        StackTraceElement caller = new Throwable().getStackTrace()[2];
        String surrfix = "%s.%s(L:%d)";
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        surrfix = String.format(tag, callerClazzName, caller.getMethodName(), caller.getLineNumber());
        surrfix = TextUtils.isEmpty(tag) ? surrfix : tag + ":" + surrfix;
        return surrfix;
    }

}
