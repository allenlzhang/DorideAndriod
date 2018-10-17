package com.carlt.chelepie.data.recorder;

public class FileInfo {

	/**
	 * 录像数据流类型 0:视频 ,1:图片
	 */
	private long streamType;
	
	/**
	 * 录像主类型 0:全天定时录制，1: 命令抓拍触发，2：碰撞事件触发
	 */
	private long recordType;
	
	/**
	 * 子类型：1：表示缩略图（当streamType为 1：图片时有效）
	 */
	private long minRecordType;

	/**
	 * 文件号
	 */
	private long fileNo;
	
	/**
	 * 文件长度
	 */
	private long fileLength;

	/**
	 * 文件名
	 */
	private String filename;
	
	/**
	 * 开始时间
	 */
	private String startTime;
	
	/**
	 * 结束时间
	 */
	private String endTime;

	public long getStreamType() {
		return streamType;
	}

	public void setStreamType(long streamType) {
		this.streamType = streamType;
	}

	public long getRecordType() {
		return recordType;
	}

	public void setRecordType(long recordType) {
		this.recordType = recordType;
	}

	public long getMinRecordType() {
		return minRecordType;
	}

	public void setMinRecordType(long minRecordType) {
		this.minRecordType = minRecordType;
	}

	public long getFileNo() {
		return fileNo;
	}

	public void setFileNo(long fileNo) {
		this.fileNo = fileNo;
	}

	public long getFileLength() {
		return fileLength;
	}

	public void setFileLength(long fileLength) {
		this.fileLength = fileLength;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

}
