package com.carlt.sesame.data.car;

public class TirepressureInfo {
	// 车轮位置，用1，2，3，4分别代表左前、右前、左后、右后四个轮胎
	// 获取数据时间
	private String time;
	// 1不正常，0 正常	
	private int state1;//左前胎压
	private int state2;//右前胎压
	private int state3;//左后胎压
	private int state4;//右后胎压
	// 胎压值
	private int coefficient1;//左前胎压
	private int coefficient2;//右前胎压
	private int coefficient3;//左后胎压
	private int coefficient4;//右后胎压
	//胎压单位
	private String unit1;//左前胎压
	private String unit2;//右前胎压
	private String unit3;//左后胎压
	private String unit4;//右后胎压
	
	// 激活状态
	private int tirepressure;
	// 进度
	private int progress;

	public int getState1() {
		return state1;
	}

	public void setState1(int state1) {
		this.state1 = state1;
	}

	public int getState2() {
		return state2;
	}

	public void setState2(int state2) {
		this.state2 = state2;
	}

	public int getState3() {
		return state3;
	}

	public void setState3(int state3) {
		this.state3 = state3;
	}

	public int getState4() {
		return state4;
	}

	public void setState4(int state4) {
		this.state4 = state4;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getTirepressure() {
		return tirepressure;
	}

	public void setTirepressure(int tirepressure) {
		this.tirepressure = tirepressure;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public int getCoefficient1() {
		return coefficient1;
	}

	public void setCoefficient1(int coefficient1) {
		this.coefficient1 = coefficient1;
	}

	public int getCoefficient2() {
		return coefficient2;
	}

	public void setCoefficient2(int coefficient2) {
		this.coefficient2 = coefficient2;
	}

	public int getCoefficient3() {
		return coefficient3;
	}

	public void setCoefficient3(int coefficient3) {
		this.coefficient3 = coefficient3;
	}

	public int getCoefficient4() {
		return coefficient4;
	}

	public void setCoefficient4(int coefficient4) {
		this.coefficient4 = coefficient4;
	}

	public String getUnit1() {
		return unit1;
	}

	public void setUnit1(String unit1) {
		this.unit1 = unit1;
	}

	public String getUnit2() {
		return unit2;
	}

	public void setUnit2(String unit2) {
		this.unit2 = unit2;
	}

	public String getUnit3() {
		return unit3;
	}

	public void setUnit3(String unit3) {
		this.unit3 = unit3;
	}

	public String getUnit4() {
		return unit4;
	}

	public void setUnit4(String unit4) {
		this.unit4 = unit4;
	}

}
