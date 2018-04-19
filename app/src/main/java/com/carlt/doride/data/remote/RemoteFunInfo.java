package com.carlt.doride.data.remote;

import com.carlt.doride.data.BaseResponseInfo;

import java.util.List;

/**
 * 远程功能item
 * 
 * @author liu
 * 
 */
public class RemoteFunInfo extends BaseResponseInfo {
	private String id;
	private String api_field;// api字段名
	private String name;// 展示名称
	private int icon_id;// 图标id
	private int icon_id_seleced;// 选中图标id
	private int icon_id_seleced_no;// 未选中图标id
	private String state;// 状态 0：不支持 1：支持
	private List<RemoteFunInfo> apiFieldLists;// 子项目
	
	public final static String STATE_SUPPORT = "1";// 支持
	public final static String STATE_NONSUPPORT = "0";// 不支持
	
	public final static String MODE_AUTO="1";//全自动
	public final static String MODE_CLOSE="2";//关闭
	public final static String MODE_FROG="3";//一键除霜
	public final static String MODE_COLD="4";//最大制冷
	public final static String MODE_HEAT="5";//最大制热
	public final static String MODE_ANION="6";//负离子
	public final static String MODE_CLEAN="7";//座舱清洁
	public final static String MODE_TEMPREGULATION="8";//温度调节
	
	private String temperature;//温度
	private boolean isSelect;//是否被选中

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getApi_field() {
		return api_field;
	}

	public void setApi_field(String api_field) {
		this.api_field = api_field;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<RemoteFunInfo> getApiFieldLists() {
		return apiFieldLists;
	}

	public void setApiFieldLists(List<RemoteFunInfo> apiFieldLists) {
		this.apiFieldLists = apiFieldLists;
	}

	public int getIcon_id() {
		return icon_id;
	}

	public void setIcon_id(int icon_id) {
		this.icon_id = icon_id;
	}
	
	public int getIcon_id_seleced() {
		return icon_id_seleced;
	}

	public void setIcon_id_seleced(int icon_id_seleced) {
		this.icon_id_seleced = icon_id_seleced;
	}

	public int getIcon_id_seleced_no() {
		return icon_id_seleced_no;
	}

	public void setIcon_id_seleced_no(int icon_id_seleced_no) {
		this.icon_id_seleced_no = icon_id_seleced_no;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public boolean isSelect() {
		return isSelect;
	}

	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}
}
