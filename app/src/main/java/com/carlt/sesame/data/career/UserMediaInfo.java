package com.carlt.sesame.data.career;

import java.io.Serializable;
import java.util.ArrayList;

public class UserMediaInfo implements Serializable {
	// 用户勋章列表
	private ArrayList<MedalInfo> mList;

	// 分享时标题
	private String mediaShareTitle;

	// 分享时正文
	private String mediaShareText;

	// 分享时链接
	private String mediaShareLink;

	public ArrayList<MedalInfo> getmList() {
		return mList;
	}

	public void setmList(ArrayList<MedalInfo> mList) {
		this.mList = mList;
	}

	public String getMediaShareTitle() {
		return mediaShareTitle;
	}

	public void setMediaShareTitle(String mediaShareTitle) {
		this.mediaShareTitle = mediaShareTitle;
	}

	public String getMediaShareText() {
		return mediaShareText;
	}

	public void setMediaShareText(String mediaShareText) {
		this.mediaShareText = mediaShareText;
	}

	public String getMediaShareLink() {
		return mediaShareLink;
	}

	public void setMediaShareLink(String mediaShareLink) {
		this.mediaShareLink = mediaShareLink;
	}

}
