package com.carlt.yema.data.career;

import java.io.Serializable;

public class MedalInfo implements Serializable {
	// id
	private String id;
	// name 勋章名称
	private String name;
	// icon 勋章图标地址 未激活的
	private String iconUrl1;
	// icon 勋章图标地址 激活的
	private String iconUrl2;
	// description 勋章描述
	private String description;
	// level 勋章排序，1最低
	private int level;
	// isgot 当前用户是否获得该勋章
	private boolean isgot;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public boolean isIsgot() {
		return isgot;
	}

	public void setIsgot(boolean isgot) {
		this.isgot = isgot;
	}

}
