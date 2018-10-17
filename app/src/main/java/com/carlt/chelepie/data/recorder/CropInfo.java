package com.carlt.chelepie.data.recorder;

/**
 * 剪裁文件信息
 * @author zhanghuiqian
 *
 */
public class CropInfo {
	private static CropInfo mCropInfo;

	public static CropInfo getInstance() {
		if (mCropInfo == null) {
			mCropInfo = new CropInfo();
		}
		return mCropInfo;
	}
	
	String fileSize;//文件大小
	String fileName;//文件名
	public String getFileSize() {
		return fileSize;
	}
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
}
