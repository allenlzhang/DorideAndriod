package com.carlt.sesame.data.community;

public class AppointmentDetialInfo extends AppointmentInfo {
	// 售后顾问名字
	protected String consultantName;
	// 售后顾问电话
	protected String consultantPhone;
	// 4S店地址
	private String address_4s;
	// 4S店名称
	private String name_4s;
	// 评价内容
	private String evaluation;
    public String getConsultantName() {
        return consultantName;
    }
    public void setConsultantName(String consultantName) {
        this.consultantName = consultantName;
    }
    public String getConsultantPhone() {
        return consultantPhone;
    }
    public void setConsultantPhone(String consultantPhone) {
        this.consultantPhone = consultantPhone;
    }
    public String getAddress_4s() {
        return address_4s;
    }
    public void setAddress_4s(String address_4s) {
        this.address_4s = address_4s;
    }
    public String getName_4s() {
        return name_4s;
    }
    public void setName_4s(String name_4s) {
        this.name_4s = name_4s;
    }
    public String getEvaluation() {
        return evaluation;
    }
    public void setEvaluation(String evaluation) {
        this.evaluation = evaluation;
    }
	
}
