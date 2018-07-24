package com.carlt.sesame.data.car;

public class CityInfo {

	private String code;

	private String name;

	private String abbr;

	// 是否需要发动机号0不需要，1需要
	private String engine;
	private String engineno;
	// 是否需要车架号0,不需要 1,需要
	private String classa;
	private String classno;
	// 是否需要登记证书号0,不需要 1,需要
	private String regist;
	private String registno;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAbbr() {
		return abbr;
	}

	public void setAbbr(String abbr) {
		this.abbr = abbr;
	}

	public String getEngine() {
		return engine;
	}

	public void setEngine(String engine) {
		this.engine = engine;
	}

	public String getEngineno() {
		return engineno;
	}

	public void setEngineno(String engineno) {
		this.engineno = engineno;
	}

	public String getClassa() {
		return classa;
	}

	public void setClassa(String classa) {
		this.classa = classa;
	}

	public String getClassno() {
		return classno;
	}

	public void setClassno(String classno) {
		this.classno = classno;
	}

	public String getRegist() {
		return regist;
	}

	public void setRegist(String regist) {
		this.regist = regist;
	}

	public String getRegistno() {
		return registno;
	}

	public void setRegistno(String registno) {
		this.registno = registno;
	}

}
