package com.carlt.sesame.data.career;

public class LicenceLevelInfo {

	// 主键
	private String id;
	// 等级名称
	private String name;
	// 升级积分条件
	private int point;
	// 图片地址--未激活状态
	private String iconUrl1;
	// 图片地址--激活状态
	private String iconUrl2;
	// 等级，1最低
	private int level;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}


	public String getIconUrl1() {
		return iconUrl1;
	}

	public void setIconUrl1(String iconUrl1) {
		this.iconUrl1 = iconUrl1;
	}

	public String getIconUrl2() {
		return iconUrl2;
	}

	public void setIconUrl2(String iconUrl2) {
		this.iconUrl2 = iconUrl2;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
