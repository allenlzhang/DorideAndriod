package com.carlt.chelepie.data.recorder;

import com.carlt.doride.data.BaseResponseInfo;

public class PieVersion extends BaseResponseInfo {

	private String pcb_ver;
	private String kernel_ver;
	private String apps_ver;
	private String sd_mount_point;//sd卡挂载点（获取硬件媒体信息的目录前缀）

	public String getPcb_ver() {
		return pcb_ver;
	}

	public void setPcb_ver(String pcb_ver) {
		this.pcb_ver = pcb_ver;
	}

	public String getKernel_ver() {
		return kernel_ver;
	}

	public void setKernel_ver(String kernel_ver) {
		this.kernel_ver = kernel_ver;
	}

	public String getApps_ver() {
		return apps_ver;
	}

	public void setApps_ver(String apps_ver) {
		this.apps_ver = apps_ver;
	}

	public String getSd_mount_point() {
		return sd_mount_point;
	}

	public void setSd_mount_point(String sd_mount_point) {
		this.sd_mount_point = sd_mount_point;
	}
	
}
