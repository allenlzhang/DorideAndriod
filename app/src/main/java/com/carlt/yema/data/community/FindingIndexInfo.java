package com.carlt.yema.data.community;

public class FindingIndexInfo {
	
	//新消息数
	private int hasNewMessage;
	//最新消息内容
	private String latestMessage;
	//车友数
	private int friends;
	//新奖品数
	private int hasNewPrize;
	//发布最新消息的用户头像
	private String avatarImg;
	public int getHasNewMessage() {
		return hasNewMessage;
	}
	public void setHasNewMessage(int hasNewMessage) {
		this.hasNewMessage = hasNewMessage;
	}
	public String getLatestMessage() {
		return latestMessage;
	}
	public void setLatestMessage(String latestMessage) {
		this.latestMessage = latestMessage;
	}
	public int getFriends() {
		return friends;
	}
	public void setFriends(int friends) {
		this.friends = friends;
	}
	public int getHasNewPrize() {
		return hasNewPrize;
	}
	public void setHasNewPrize(int hasNewPrize) {
		this.hasNewPrize = hasNewPrize;
	}
    public String getAvatarImg() {
        return avatarImg;
    }
    public void setAvatarImg(String avatarImg) {
        this.avatarImg = avatarImg;
    }
	
}
