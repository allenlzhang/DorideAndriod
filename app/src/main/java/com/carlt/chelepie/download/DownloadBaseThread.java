package com.carlt.chelepie.download;

import android.util.Log;


import com.carlt.doride.utils.FileUtil;
import com.carlt.doride.utils.LocalConfig;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

public abstract class DownloadBaseThread extends Thread {

	protected String sourceUrl;// 下载URL

	protected long haveDownSize;// 已经下载的大小

	protected long totalLength;// 下载文件的总大小

	protected RandomAccessFile savedFile;

	protected OnDownloadListner mDownloadListner;

	protected DownloadBaseInfo mDownloadBaseInfo;

	private final String SUFFIX = ".temp";

	private String SDPath = LocalConfig.mDownLoadFileSavePath_SD;

	private String AbsolutePath = LocalConfig.mDownLoadFileSavePath_Absolute;

	public DownloadBaseThread(DownloadBaseInfo mDownloadBaseInfo, OnDownloadListner downloadListner) {
		mDownloadListner = downloadListner;
		this.mDownloadBaseInfo = mDownloadBaseInfo;
		mDownloadBaseInfo.setStatus(DownloadBaseInfo.STATUS_UNLOAD);
		sourceUrl = mDownloadBaseInfo.getUrl();
	}

	protected String getLoaclSavePath() {
		int tmp = sourceUrl.lastIndexOf("/");

		String fileURL = null;

		String tempFileURL_sdcard = SDPath + sourceUrl.substring(tmp + 1);

		String tempFileURL_internal = AbsolutePath + sourceUrl.substring(tmp + 1);

		if (FileUtil.isExCardExist()) {
			// SD卡存在
			fileURL = tempFileURL_sdcard;

		} else {
			// SD卡不存在
			fileURL = tempFileURL_internal;
		}

		return fileURL;
	}

	@Override
	public void run() {
		String filePath = getLoaclSavePath() + SUFFIX;
		haveDownSize = FileUtil.getFileLength(filePath);
		InputStream data = requestData(sourceUrl, haveDownSize);

		if (null != data) {
			int defaultPro = (int) (haveDownSize * 100 / totalLength);
			mDownloadBaseInfo.setStatus(DownloadBaseInfo.STATUS_LOADING);
			mDownloadListner.onProgress(defaultPro);
			mDownloadListner.onStatusChanged(mDownloadBaseInfo);
			mDownloadListner.onStart(mDownloadBaseInfo);

			try {
				savedFile = new RandomAccessFile(filePath, "rw");
				byte[] buffer = new byte[1024];
				int len = 0;
				int progress = 0;
				savedFile.seek(haveDownSize);
				while ((len = data.read(buffer, 0, buffer.length)) != -1 && haveDownSize < totalLength) {
					if (mDownloadBaseInfo.getStatus() == DownloadBaseInfo.STATUS_LOADING) {
						savedFile.write(buffer, 0, len);
						haveDownSize += len;
						if (totalLength != 0) {
							progress = (int) (haveDownSize * 100 / totalLength);
							mDownloadBaseInfo.setPersent(progress);
							mDownloadListner.onProgress(progress);
						}
					} else {
						break;
					}
				}

				if (savedFile.length() == totalLength) {

					// 下载文件完整，重命名
					int pos = filePath.lastIndexOf(".");
					String newPath = filePath.substring(0, pos);
					if (FileUtil.isExist(newPath)) {
						// 若本地存在该文件，则覆盖
						FileUtil.deleteFile(new File(newPath));
					}
					File mFile = FileUtil.renameFile(filePath, newPath);
					if (mFile != null && mFile.length() > 0) {
						mDownloadBaseInfo.setPathLocal(mFile.getPath());
						mDownloadBaseInfo.setStatus(DownloadBaseInfo.STATUS_FINISHED);
						mDownloadListner.onStatusChanged(mDownloadBaseInfo);
					}

					Log.e("info", "下载完成==" + mFile.getPath());
				}

				haveDownSize = savedFile.length();
				mDownloadBaseInfo.setLength(haveDownSize + "");
				mDownloadBaseInfo.setPersent(progress);
				if (mDownloadBaseInfo.getStatus() == DownloadBaseInfo.STATUS_PAUSE) {
					mDownloadListner.onPause(mDownloadBaseInfo);
					mDownloadListner.onStatusChanged(mDownloadBaseInfo);
				} else if (mDownloadBaseInfo.getStatus() == DownloadBaseInfo.STATUS_FINISHED) {
					mDownloadListner.onFinished(mDownloadBaseInfo);
					mDownloadListner.onStatusChanged(mDownloadBaseInfo);

				}
				savedFile.close();
				data.close();
			} catch (IOException e) {
				Log.e("down", "down--e--1==" + e);
				int persent = 0;
				if (totalLength != 0) {
					persent = (int) (haveDownSize * 100 / totalLength);
				}
				mDownloadBaseInfo.setStatus(DownloadBaseInfo.STATUS_PAUSE);
				mDownloadBaseInfo.setPersent(persent);
				mDownloadListner.onStatusChanged(mDownloadBaseInfo);
				mDownloadListner.onFailed(e + "");
			}
		} else {
			mDownloadListner.onFailed("连接失败-数据不存在");
		}

	}

	protected abstract InputStream requestData(final String URL, long startPos);

	public interface OnDownloadListner {
		void onStatusChanged(DownloadBaseInfo mDownloadBaseInfo);// 状态改变

		void onStart(DownloadBaseInfo mDownloadBaseInfo);// 下载开始

		void onProgress(int progress);// 下载中

		void onPause(DownloadBaseInfo mDownloadBaseInfo);// 暂停

		void onFinished(DownloadBaseInfo mDownloadBaseInfo);// 下载完成

		void onFailed(String error);// 下载出错
	}

	public void setSDPath(String sDPath) {
		if (sDPath != null && sDPath.length() > 0) {
			SDPath = sDPath;
		}
	}

	public void setAbsolutePath(String absolutePath) {
		if (absolutePath != null && absolutePath.length() > 0) {
			AbsolutePath = absolutePath;
		}
	}

	public DownloadBaseInfo getmDownloadBaseInfo() {
		return mDownloadBaseInfo;
	};
}
