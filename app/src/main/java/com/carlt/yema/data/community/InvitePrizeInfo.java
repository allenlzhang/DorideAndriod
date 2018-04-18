package com.carlt.yema.data.community;

import java.util.ArrayList;

public class InvitePrizeInfo {
	private ArrayList<info> mInvitePrizeList = new ArrayList<info>();

	public ArrayList<info> getmInvitePrizeList() {
		return mInvitePrizeList;
	}

	public void AddmInvitePrizeList(String name, String url) {
		info object = new info(name, url);
		mInvitePrizeList.add(object);
	}

	public class info {
		private String name;
		private String url;

		public info(String name, String url) {
			this.name = name;
			this.url = url;
		}

		public String getName() {
			return name;
		}

		public String getUrl() {
			return url;
		}

	}
}
