package com.carlt.sesame.data.car;

public class HelpPhoneInfo {
	// flag 为true时代表标签 为false时代表电话
	private boolean flag;
	private String name;
	private String phone;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

}
