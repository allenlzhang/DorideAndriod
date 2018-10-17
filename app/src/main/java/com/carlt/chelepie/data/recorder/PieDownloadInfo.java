package com.carlt.chelepie.data.recorder;

import android.os.Parcel;
import android.os.Parcelable;


import com.carlt.doride.utils.LocalConfig;
import com.carlt.sesame.utility.MyTimeUtil;

import java.util.Date;

/**
 * 
 * 下载文件信息 0:视频 ,1:图片
 * 
 * @author @Y.yun
 */
public class PieDownloadInfo implements Cloneable,Comparable<PieDownloadInfo>,Parcelable {

	
	
	public PieDownloadInfo() {
	}

	public final static int STATUS_FINISHED = 0;// 已完成
	public final static int STATUS_LOADING = 3;// 下载中
	public final static int STATUS_STOP = 1;// 停止了
	public final static int STATUS_UNLOAD = 2;// 未下载

	public final static int TYPE_JPG = 1;
	public final static int TYPE_H264 = 2;
	public final static int TYPE_FOLDER = 3;// 界面显示需要
	public final static int TYPE_BIN = 4;// 界面显示需要剪裁
	public final static int TYPE_DATE = 5;// 时间item(界面显示需要)
	public final static int TYPE_DATE_YEAR = 7;// 时间item,带有年份(界面显示需要)
	public final static int TYPE_LINE = 6;// 展示竖线(界面显示需要)

	public final static int STORE_EVENT = 2;// 事件
	public final static int STORE_CAPTURE = 1;// 抓拍
	public final static int STORE_THUMBNAIL = 0;// 缩略图，全天定时录制
	public final static int STORE_ALLLISTS = 4;// 视频，全天定时录制
	public final static int STORE_CROP = 3;// 剪裁
	public final static int STORE_TEMP = 5;// 缓存

	private int id = 0;// 记录ID
	private int resolution = 0;// 分辨率 0:1080p ,1:720p
	private int status = STATUS_UNLOAD;// 下载状态
	private int storeType = 0; // 存储类型
	private int totalLen = 0;// 文件大小 k
	private int downloadLen = 0;// 文件已下载大小
	private int type = TYPE_JPG;// 文件类型，图片，视频
	private String fileName = "";// 文件名称
	private String fileSrcName = "";// 文件源文件名--唯一
	private String startTime = "";// 开始时间
	private String endTime = "";// 结束时间
	private String localPath = "";// 本地地址
	private String thumbnailPath = "";// 缩略图本地地址
	private String createTime = "";// 任务创建时间
	private String succTime = ""; // 任务下载完成时间
	private String accout = "";// 账户
	private String deviceName = "";// 设备名称
	/**
	 * 文件号
	 */
	private long fileNo = 0;
	/**
	 * 录像主类型 0:全天定时录制，1: 命令抓拍触发，2：碰撞事件触发
	 */
	private long recordType;
	/**
	 * 子类型：1：表示缩略图（当streamType为 1：图片时有效）
	 */
	private long minRecordType;
	/**
	 * 录像数据流类型 0:视频 ,1:图片
	 */
	private long streamType;

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
	
	public long getStreamType() {
		return streamType;
	}

	public void setStreamType(long streamType) {
		this.streamType = streamType;
	}

	public long getFileNo() {
		return fileNo;
	}

	public void setFileNo(long fileNo) {
		this.fileNo = fileNo;
	}

	public String getAccout() {
		return accout;
	}

	public void setAccout(String accout) {
		this.accout = accout;
	}

	public int getResolution() {
		return resolution;
	}

	public void setResolution(int resolution) {
		this.resolution = resolution;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLocalPath() {
		return localPath;
	}

	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}

	public String getThumbnailPath() {
		return thumbnailPath;
	}

	public void setThumbnailPath(String thumbnailPath) {
		this.thumbnailPath = thumbnailPath;
	}

	public int getStoreType() {
		return storeType;
	}

	public void setStoreType(int storeType) {
		this.storeType = storeType;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		// try {
		// String replaceAll = fileName.replaceAll("video", "");
		// this.fileName = replaceAll;
		// } catch (Exception e) {
		// // e.printStackTrace();
		// this.fileName = fileName;
		// }
		this.fileName = fileName;
	}

	public String getFileSrcName() {
		return fileSrcName;
	}

	public void setFileSrcName(String fileSrcName) {
		this.fileSrcName = fileSrcName;
	}

	public int getTotalLen() {
		return totalLen;
	}

	public void setTotalLen(int totalLen) {
		this.totalLen = totalLen;
	}

	public int getDownloadLen() {
		return downloadLen;
	}

	public void setDownloadLen(int downloadLen) {
		this.downloadLen = downloadLen;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getSuccTime() {
		return succTime;
	}

	public void setSuccTime(String succTime) {
		this.succTime = succTime;
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the deviceName
	 */
	public String getDeviceName() {
		return deviceName;
	}

	/**
	 * @param deviceName
	 *            the deviceName to set
	 */
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	
	

	@Override
	public String toString() {
		return "PieDownloadInfo [id=" + id + ", resolution=" + resolution
				+ ", status=" + status + ", storeType=" + storeType
				+ ", totalLen=" + totalLen + ", downloadLen=" + downloadLen
				+ ", type=" + type + ", fileName=" + fileName
				+ ", fileSrcName=" + fileSrcName + ", startTime=" + startTime
				+ ", endTime=" + endTime + ", localPath=" + localPath
				+ ", thumbnailPath=" + thumbnailPath + ", createTime="
				+ createTime + ", succTime=" + succTime + ", accout=" + accout
				+ ", deviceName=" + deviceName + ", fileNo=" + fileNo
				+ ", recordType=" + recordType + ", minRecordType="
				+ minRecordType + ", streamType=" + streamType + "]";
	}

	@Override
	public int compareTo(PieDownloadInfo another) {
        Date timeAnother = MyTimeUtil.parseDate(another.startTime);
        Date timeThis = MyTimeUtil.parseDate(this.startTime);
        int i=timeAnother.compareTo(timeThis);
		return i;
	}
	
	/**
	 * 获取存储最后一级目录
	 */
	public String getDir(int mStoreType){
		String dir = "";
		if (mStoreType == PieDownloadInfo.STORE_CROP) {
			dir = LocalConfig.DIR_CROP;
		} else if (mStoreType == PieDownloadInfo.STORE_CAPTURE) {
			dir = LocalConfig.DIR_CAPTURE;
		} else if (mStoreType == PieDownloadInfo.STORE_EVENT) {
			dir = LocalConfig.DIR_EVENT;
		} else if (mStoreType == PieDownloadInfo.STORE_THUMBNAIL) {
			dir = LocalConfig.DIR_THUMBNAIL;
		}else if (mStoreType == PieDownloadInfo.STORE_ALLLISTS) {
			dir = LocalConfig.DIR_ALLLISTS;
		}else if (mStoreType == PieDownloadInfo.STORE_TEMP) {
			dir = LocalConfig.DIR_TEMP;
		}
		return dir;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeInt(resolution);
		dest.writeInt(status);
		dest.writeInt(storeType);
		dest.writeInt(totalLen);
		dest.writeInt(downloadLen);
		dest.writeInt(type);
		dest.writeString(fileName);
		dest.writeString(fileSrcName);
		dest.writeString(startTime);
		dest.writeString(endTime);
		dest.writeString(localPath);
		dest.writeString(thumbnailPath);
		dest.writeString(createTime);
		dest.writeString(succTime);
		dest.writeString(accout);
		dest.writeString(deviceName);
		dest.writeLong(fileNo);
		dest.writeLong(recordType);
		dest.writeLong(minRecordType);
		dest.writeLong(streamType);
	}

	public PieDownloadInfo(Parcel in) {
		this.id = in.readInt();
		this.resolution = in.readInt();
		this.status = in.readInt();
		this.storeType = in.readInt();
		this.totalLen = in.readInt();
		this.downloadLen = in.readInt();
		this.type = in.readInt();
		this.fileName = in.readString();
		this.fileSrcName = in.readString();
		this.startTime = in.readString();
		this.endTime = in.readString();
		this.localPath = in.readString();
		this.thumbnailPath = in.readString();
		this.createTime = in.readString();
		this.succTime = in.readString();
		this.accout = in.readString();
		this.deviceName = in.readString();
		this.fileNo = in.readLong();
		this.recordType = in.readLong();
		this.minRecordType = in.readLong();
		this.streamType = in.readLong();
	}
	
	public static final Creator<PieDownloadInfo> CREATOR = new Creator<PieDownloadInfo>() {

		@Override
		public PieDownloadInfo[] newArray(int size) {
			return new PieDownloadInfo[size];
		}

		@Override
		public PieDownloadInfo createFromParcel(Parcel in) {
			return new PieDownloadInfo(in);
		}
	};
	
}
