package com.carlt.doride.data.community;


public class PKItemInfo {
	private String title;

	private String left;

	private String right;
	
	private String unit;

	// 状态
	private int status;
	
	public final static int STATUS_LEFT=1;// 1.左
	public final static int STATUS_RIGHR=2;// 2.右
	public final static int STATUS_EQUAL=3;// 3.相等无皇冠

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLeft() {
		return left;
	}

	public void setLeft(String left) {
		this.left = left;
	}

	public String getRight() {
		return right;
	}

	public void setRight(String right) {
		this.right = right;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	
}
