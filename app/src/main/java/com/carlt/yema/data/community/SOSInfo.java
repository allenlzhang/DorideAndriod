package com.carlt.yema.data.community;

public class SOSInfo {

	private String id;
	// 是否需要救援
	private int need_sos;
	// 不需要
	public final static int SOS_NO = 0;
	// 需要
	public final static int SOS_YES = 1;

	// 当前申报状态
	private int state;
	// 等待回复
	public final static int STATE_WAIT = 1;
	// 已回复
	public final static int STATE_REPLYED = 2;

	// 时间
	private String create_time;
	// 描述
	private String info;
	// 图片张数
	private int imagesCount;

	public int getNeed_sos() {
		return need_sos;
	}

	public void setNeed_sos(int need_sos) {
		this.need_sos = need_sos;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public int getImagesCount() {
		return imagesCount;
	}

	public void setImagesCount(int imagesCount) {
		this.imagesCount = imagesCount;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
