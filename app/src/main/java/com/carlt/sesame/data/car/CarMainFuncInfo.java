package com.carlt.sesame.data.car;

/**
 * 座驾首页底部支持功能的Info
 * 
 * @author Daisy
 * 
 */
public class CarMainFuncInfo {
	private int id;// 功能id
	private String name;// 功能名称
	private int icon;// 功能图标
	private boolean isShowDot;// 是否展示红点
	public boolean hasPermissions ;// s是否有使用权限
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getIcon() {
		return icon;
	}
	public void setIcon(int icon) {
		this.icon = icon;
	}
	public boolean isShowDot() {
		return isShowDot;
	}
	public void setShowDot(boolean isShowDot) {
		this.isShowDot = isShowDot;
	}
	
}
