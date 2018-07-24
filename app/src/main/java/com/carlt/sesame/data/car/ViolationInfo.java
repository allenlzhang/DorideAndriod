package com.carlt.sesame.data.car;

import java.io.Serializable;

public class ViolationInfo implements Serializable{
	/** */
	private static final long serialVersionUID = 1L;
	// // ID
	// public int id;
	// 时间
	public String date;
	// 违章地点
	public String area;
	// 违章行为
	public String act;
	// 违章代码
	public String code;
	// 违章扣分
	public String fen;
	// 违章罚款
	public String money;
	// 是否处理,1处理 0未处理 空未知
	public String handled;
	
	public final static String HANDLED="1";//已处理
	public final static String HANDLED_NO="0";//未处理
	
	//分享需要的属性
    private String shareLink;
    private String shareTitle;
    private String shareText;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getAct() {
		return act;
	}

	public void setAct(String act) {
		this.act = act;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getFen() {
		return fen;
	}

	public void setFen(String fen) {
		this.fen = fen;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getHandled() {
		return handled;
	}

	public void setHandled(String handled) {
		this.handled = handled;
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
