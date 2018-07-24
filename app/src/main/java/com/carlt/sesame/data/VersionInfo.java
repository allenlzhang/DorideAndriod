package com.carlt.sesame.data;

public class VersionInfo {
	// 状态 status:1 不升级，2 强制升级 3 可选升级
	private int status;

	// apk地址
	private String filepath;
	public String latest_version;
	public String info;

	private String remark;//往下个版本更新的日志

	public final static int STATUS_ENABLE = 1;// 1 不升级

	public final static int STATUS_ABLE = 2;// 2 强制升级

	public final static int STATUS_CHOSE = 3;// 3 可选升级

	private String version;

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

	public void setVersion(String version) {
		this.version = version;
	}

	public String getVersion() {
		return version;
	}

	@Override
	public String toString() {
		return "VersionInfo{" +
				"status=" + status +
				", filepath='" + filepath + '\'' +
				", latest_version='" + latest_version + '\'' +
				", info='" + info + '\'' +
				", remark='" + remark + '\'' +
				", version='" + version + '\'' +
				'}';
	}
}
