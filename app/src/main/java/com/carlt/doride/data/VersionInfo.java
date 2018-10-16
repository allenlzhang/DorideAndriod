package com.carlt.doride.data;

public class VersionInfo {
	// 状态 status:1 不升级，2 强制升级 3 可选升级
	private int status;

	// apk地址
	private String filepath;

	private String remark;

	public final static int STATUS_ENABLE = 1;// 1 不升级

	public final static int STATUS_ABLE = 2;// 2 强制升级

	public final static int STATUS_CHOSE = 3;// 3 可选升级

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
