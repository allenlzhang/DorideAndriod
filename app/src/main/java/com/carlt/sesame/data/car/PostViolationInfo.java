
package com.carlt.sesame.data.car;

import java.io.Serializable;

public class PostViolationInfo implements Serializable {
	/** */
	private static final long serialVersionUID = 1L;

	// 上线城市ID
	private String cityCodeId;
	// 车牌号码 7位
	private String carno;
	// 发动机号
	private String engineno;
	// 车架号
	private String standcarno;
	// 登记证书号
	private String registno;
	/** id */
	private int id = -1;
	/** 违章信息 */
	private String infos = "";
	/** 更新时间 */
	private long time = -1;

	public static final int CARNOSIZE = 7;

	public static final int ENGINENOSIZE = 8;

	public static final int STANDCARNOSIZE = 8;

	public static final int REGISTNOSIZE = 8;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getInfos() {
		return infos;
	}

	public void setInfos(String infos) {
		this.infos = infos;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getCityCodeId() {
		return cityCodeId;
	}

	public void setCityCodeId(String cityCodeId) {
		this.cityCodeId = cityCodeId;
	}

	public String getCarno() {
		return carno;
	}

	public void setCarno(String carno) {
		this.carno = carno;
	}

	public String getEngineno() {
		return engineno;
	}

	public void setEngineno(String engineno) {
		this.engineno = engineno;
	}

	public String getStandcarno() {
		return standcarno;
	}

	public void setStandcarno(String standcarno) {
		this.standcarno = standcarno;
	}

	public String getRegistno() {
		return registno;
	}

	public void setRegistno(String registno) {
		this.registno = registno;
	}
}
