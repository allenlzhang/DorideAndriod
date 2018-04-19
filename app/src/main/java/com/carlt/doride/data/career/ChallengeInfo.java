package com.carlt.doride.data.career;

import java.io.Serializable;

public class ChallengeInfo implements Serializable {
	// Id
	private String id;

	// 挑战类型
	// 11 省油模式:定油量,挑战里程;
	// 21 加速模式:定车速,挑战加速时间;
	// 31 省时模式:定行程,挑战行驶时间;
	// 41 评分模式:定行程,挑战驾驶评分
	private int type;

	public final static int TYPE_OIL = 11;// 11 省油模式:定油量,挑战里程;

	public final static int TYPE_ACCELERATE = 21;// 21 加速模式:定车速,挑战加速时间;

	public final static int TYPE_TIME = 31;// 31 省时模式:定行程,挑战行驶时间;

	public final static int TYPE_SCORE = 41;// 41 评分模式:定行程,挑战驾驶评分

	// fuel：在省油挑战中使用
	private String fuel;

	// miles:在省油 省时 评分挑战中使用
	private String miles;

	// time:在加速省时挑战中使用,加速挑战中单位是秒,省时挑战中单位是分
	private String time;

	// speed:加速挑战中使用
	private String speed;

	// point:评分挑战中使用
	private String point;

	// sort:排序，小靠前
	private int sort;

	private String info;

	// name:挑战名称
	private String name;

	// status:1 挑战已成功 2 挑战中,3 未解锁
	private String status="";

	public final static String STATUS_FINISHED = "1";// status:1 挑战已成功

	public final static String STATUS_UNFINISHED = "2";// status:2 挑战中

	public final static String STATUS_UNLOCKED = "3";// status:3 未解锁
	
	private boolean isNull;//是否是因界面整齐加入的空项

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getFuel() {
		return fuel;
	}

	public void setFuel(String fuel) {
		this.fuel = fuel;
	}

	public String getMiles() {
		return miles;
	}

	public void setMiles(String miles) {
		this.miles = miles;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public String getPoint() {
		return point;
	}

	public void setPoint(String point) {
		this.point = point;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

    public boolean isNull() {
        return isNull;
    }

    public void setNull(boolean isNull) {
        this.isNull = isNull;
    }
	
}
