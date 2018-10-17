package com.carlt.chelepie.data.recorder;

import android.util.Log;

/**
 * 盒子列表多媒体信息
 * 
 * @author Administrator
 */
public class ThumbnailInfo {
	private String deviceId;// 设备id

	private String name;// 名称

	private String nameHard;// 硬件上存储的时间戳

	private String thumbLocalPath;// 缩略图本地路径

	private String date;
	private String time;

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;

	}

	public String getNameHard() {
		return nameHard;
	}

	public void setNameHard(String nameHard) {
		this.nameHard = nameHard;

		try {
			StringBuffer sb1 = new StringBuffer(nameHard);
			sb1.insert(4, "-");
			sb1.insert(7, "-");
			sb1.insert(10, ",");
			sb1.insert(13, ":");
			sb1.insert(16, ":");
			String[] dt = sb1.toString().split(",");
			date = dt[0];
			time = dt[1];
		} catch (Exception e) {
			Log.e("info", "NameHard分割错误==" + e);
		}

	}

	public String getThumbLocalPath() {
		return thumbLocalPath;
	}

	public void setThumbLocalPath(String thumbLocalPath) {
		this.thumbLocalPath = thumbLocalPath;
	}

	public String getDate() {
		return date;
	}

	public String getTime() {
		return time;
	}

}
