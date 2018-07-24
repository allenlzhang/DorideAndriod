package com.carlt.sesame.data.car;

public class MaintainLogInfo {

	private String title;// 建议保养项目标题
	private String remarks;// 建议保养项目说明
	private String nextMiles;// 项目保养里程
	private String nextDate;// 项目保养日期(有值时说明该项目是通过保养周期检索获得)

	
	public final static int COMMEND_NO = 0;
	public final static int COMMEND = 1;
	private int isCommend;// 1芝麻推荐,0不推荐

	public final static int CLOSE = 0;
	public final static int OPEN = 1;
	private int isOpen;// 此标识为仅用于界面显示时 是否展开

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getNextMiles() {
		return nextMiles;
	}

	public void setNextMiles(String nextMiles) {
		this.nextMiles = nextMiles;
	}

	public String getNextDate() {
		return nextDate;
	}

	public void setNextDate(String nextDate) {
		this.nextDate = nextDate;
	}

	public int getIsCommend() {
		return isCommend;
	}

	public void setIsCommend(int isCommend) {
		this.isCommend = isCommend;
	}

	public int getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(int isOpen) {
		this.isOpen = isOpen;
	}

}
