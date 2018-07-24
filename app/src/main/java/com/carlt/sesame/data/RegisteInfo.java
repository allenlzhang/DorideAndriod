package com.carlt.sesame.data;

public class RegisteInfo {
	// public final static String SECRETARY1 = "1";
	// public final static String SECRETARY2 = "2";
	// // 车秘书ID
	// private String secretaryid;
	// // 真实姓名
	// private String realname;
	// // 性别
	// private String gender;
	// // 头像
	// private String avatar_path;
	// // 车型id
	// private String modelid;
	// // 车系id
	// private String optionid;
	// // 车款id
	// private String carid;
	// 手机号
	private String mobile;
	// 密码
	private String passWord;
	// 手机验证码
	private String validate;
	// 邀请码，可选参数
	private String invite;

	//android或IOS
	private final static String originate = "1";

	// public String getSecretaryid() {
	// return secretaryid;
	// }
	//
	// public void setSecretaryid(String secretaryid) {
	// this.secretaryid = secretaryid;
	// }
	//
	// public String getRealname() {
	// return realname;
	// }
	//
	// public void setRealname(String realname) {
	// this.realname = realname;
	// }
	//
	// public String getGender() {
	// return gender;
	// }
	//
	// public void setGender(String gender) {
	// this.gender = gender;
	// }
	//
	//
	// public String getAvatar_path() {
	// return avatar_path;
	// }
	//
	// public void setAvatar_path(String avatar_path) {
	// this.avatar_path = avatar_path;
	// }
	//
	// public String getModelid() {
	// return modelid;
	// }
	//
	// public void setModelid(String modelid) {
	// this.modelid = modelid;
	// }
	//
	// public String getOptionid() {
	// return optionid;
	// }
	//
	// public void setOptionid(String optionid) {
	// this.optionid = optionid;
	// }
	//
	// public String getCarid() {
	// return carid;
	// }
	//
	// public void setCarid(String carid) {
	// this.carid = carid;
	// }

	public static String getOriginate() {
		return originate;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public String getValidate() {
		return validate;
	}

	public void setValidate(String validate) {
		this.validate = validate;
	}

	public String getInvite() {
		return invite;
	}

	public void setInvite(String invite) {
		this.invite = invite;
	}

}
