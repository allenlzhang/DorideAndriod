package com.carlt.sesame.data.car;

import java.util.ArrayList;

public class PhysicalExaminationInfo {
	private String id;
	private String point;
	// 车辆是否在行驶，1 行驶中，0 未行驶，
	// 在未行驶时,APP不显示实时数据，提示"请先启动车辆，再重新发起诊断"。
	private int isrunning;
	private ArrayList<CheckFaultInfo> listP = new ArrayList<CheckFaultInfo>();
	private ArrayList<CheckFaultInfo> listB = new ArrayList<CheckFaultInfo>();
	private ArrayList<CheckFaultInfo> listC = new ArrayList<CheckFaultInfo>();
	private ArrayList<CheckFaultInfo> listU = new ArrayList<CheckFaultInfo>();

	private ArrayList<CheckFaultInfo> mistakeListP = new ArrayList<CheckFaultInfo>();
	private ArrayList<CheckFaultInfo> mistakeListB = new ArrayList<CheckFaultInfo>();
	private ArrayList<CheckFaultInfo> mistakeListC = new ArrayList<CheckFaultInfo>();
	private ArrayList<CheckFaultInfo> mistakeListU = new ArrayList<CheckFaultInfo>();

	private String shareLink;
    private String shareTitle;
    private String shareText;
    
	public ArrayList<CheckFaultInfo> getListP() {
		return listP;
	}

	public void setListP(ArrayList<CheckFaultInfo> listP) {
		this.listP = listP;
	}

	public ArrayList<CheckFaultInfo> getListB() {
		return listB;
	}

	public void setListB(ArrayList<CheckFaultInfo> listB) {
		this.listB = listB;
	}

	public ArrayList<CheckFaultInfo> getListC() {
		return listC;
	}

	public void setListC(ArrayList<CheckFaultInfo> listC) {
		this.listC = listC;
	}

	public ArrayList<CheckFaultInfo> getListU() {
		return listU;
	}

	public void setListU(ArrayList<CheckFaultInfo> listU) {
		this.listU = listU;
	}

	public ArrayList<CheckFaultInfo> getMistakeListP() {
		return mistakeListP;
	}

	public void setMistakeListP(ArrayList<CheckFaultInfo> mistakeListP) {
		this.mistakeListP = mistakeListP;
	}

	public ArrayList<CheckFaultInfo> getMistakeListB() {
		return mistakeListB;
	}

	public void setMistakeListB(ArrayList<CheckFaultInfo> mistakeListB) {
		this.mistakeListB = mistakeListB;
	}

	public ArrayList<CheckFaultInfo> getMistakeListC() {
		return mistakeListC;
	}

	public void setMistakeListC(ArrayList<CheckFaultInfo> mistakeListC) {
		this.mistakeListC = mistakeListC;
	}

	public ArrayList<CheckFaultInfo> getMistakeListU() {
		return mistakeListU;
	}

	public void setMistakeListU(ArrayList<CheckFaultInfo> mistakeListU) {
		this.mistakeListU = mistakeListU;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPoint() {
		return point;
	}

	public void setPoint(String point) {
		this.point = point;
	}

	public int getIsrunning() {
		return isrunning;
	}

	public void setIsrunning(int isrunning) {
		this.isrunning = isrunning;
	}

    public String getShareLink() {
        return shareLink;
    }

    public void setShareLink(String shareLink) {
        this.shareLink = shareLink;
    }

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public String getShareText() {
        return shareText;
    }

    public void setShareText(String shareText) {
        this.shareText = shareText;
    }

}
