package com.carlt.sesame.data.set;

import java.io.Serializable;

public class CityStringInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	// id
	private int id;
	// 内容
	private String txt;
	// 更新时间
	private String time;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTxt() {
		return txt;
	}

	public void setTxt(String txt) {
		this.txt = txt;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
