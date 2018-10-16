package com.carlt.sesame;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;

import com.carlt.doride.utils.LocalConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告
 * 
 * @author Administrator
 * 
 */
public class CrashHandler implements UncaughtExceptionHandler {
	public static final String TAG = "CrashHandler";
	private String s1;
	// CrashHandler 实例
	private static CrashHandler INSTANCE = new CrashHandler();
	// context
	private Context mContext;
	private UncaughtExceptionHandler mDefaultHandler;
	private Map<String, String> infos = new HashMap<String, String>();
	private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

	private CrashHandler() {
	};

	public static CrashHandler getInstance() {
		return INSTANCE;
	}

	public void init(Context context) {
		mContext = context;

		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			// 退出程序,注释下面的重启启动程序代码
			android.os.Process.killProcess(android.os.Process.myPid());
			
			//友盟数据统计
//			MobclickAgent.onKillProcess(mContext);
			
			System.exit(1);
			// 重新启动程序，注释上面的退出程序
			// Intent intent = new Intent();
			// intent.setClass(mContext, SesameMainActivity.class);
			// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// mContext.startActivity(intent);
			// android.os.Process.killProcess(android.os.Process.myPid());
		}
	}

	/**
	 * 自定义错误处理，收集错误信息，发送错误报告等操作均在此完成
	 * 
	 * @param ex
	 * @return true
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return false;
		}
		collectDeviceInfo(mContext);
		saveCrashInfo2File(ex);
		return true;
	}

	/**
	 * 手机设备参数信息
	 * 
	 * @param ctx
	 */
	public void collectDeviceInfo(Context ctx) {
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
					PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				String versionName = pi.versionName == null ? "null"
						: pi.versionName;
				String versionCode = pi.versionCode + "";
				infos.put("versionName", versionName);
				infos.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException e) {

		}
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				infos.put(field.getName(), field.get(null).toString());
			} catch (Exception e) {

			}
		}

	}

	/**
	 * 保存错误信息到文件中
	 * 
	 * @param ex
	 * @return 返回的文件名称，便于将文件传送到服务器
	 */
	private String saveCrashInfo2File(Throwable ex) {
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : infos.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key + "=" + value + "\n");
		}
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		sb.append(result);
		try {
			long timestamp = System.currentTimeMillis();
			String time = formatter.format(new Date());
			String fileName = "Crash-" + time + "-" + timestamp + ".log";
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				String path = LocalConfig.mErroLogSavePath_SD;
				File dir = new File(path);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				FileOutputStream fos = new FileOutputStream(path + fileName);
				fos.write(sb.toString().getBytes());
				fos.close();
			}
			return fileName;
		} catch (Exception e) {

		}
		return null;
	}

}
