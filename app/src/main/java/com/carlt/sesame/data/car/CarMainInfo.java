package com.carlt.sesame.data.car;

public class CarMainInfo {
	// 是否支持胎压检测
	private boolean tireable;
	// 异常
	public final static int ABNORMAL = 0;
	// 正常
	public final static int NORMAL = 1;
	// 未激活
	public final static int UNACTIVATION = 2;
	// 激活中
	public final static int ACTIVATIONING = 3;
	// 终端抓取胎压数据失败
	public final static int CONNECTERRO = 4;
	// 当前胎压状态
	private int tirepressure;
	// 安防消息条数
	private int safetycount;
	// 最新安防信息内容
	private String safetymsg;
	// 上次体检时间戳
	private String lastchecktime;
	// 上次体检得分
	private String lastcheckscore;
	//是否正在行驶中
	private boolean isRunning;
	//电池电量
	private String soc;
	//电池健康度
    private String soh;

	public boolean isTireable() {
		return tireable;
	}

	public void setTireable(boolean tireable) {
		this.tireable = tireable;
	}

	public int getTirepressure() {
		return tirepressure;
	}

	public void setTirepressure(int tirepressure) {
		this.tirepressure = tirepressure;
	}

	public int getSafetycount() {
		return safetycount;
	}

	public void setSafetycount(int safetycount) {
		this.safetycount = safetycount;
	}

	public String getSafetymsg() {
		return safetymsg;
	}

	public void setSafetymsg(String safetymsg) {
		this.safetymsg = safetymsg;
	}

	public String getLastchecktime() {
		return lastchecktime;
	}

	public void setLastchecktime(String lastchecktime) {
		this.lastchecktime = lastchecktime;
	}

	public String getLastcheckscore() {
		return lastcheckscore;
	}

	public void setLastcheckscore(String lastcheckscore) {
		this.lastcheckscore = lastcheckscore;
	}

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public String getSoc() {
        return soc;
    }

    public void setSoc(String soc) {
        this.soc = soc;
    }

    public String getSoh() {
        return soh;
    }

    public void setSoh(String soh) {
        this.soh = soh;
    }

}
