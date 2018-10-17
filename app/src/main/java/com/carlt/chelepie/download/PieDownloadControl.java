
package com.carlt.chelepie.download;

import android.os.SystemClock;


import com.carlt.chelepie.control.DaoPieDownloadControl;
import com.carlt.chelepie.control.RecorderControl;
import com.carlt.chelepie.data.recorder.PieDownloadInfo;
import com.carlt.chelepie.data.recorder.PieInfo;
import com.carlt.chelepie.manager.DeviceConnectManager;
import com.carlt.chelepie.protocolstack.recorder.RecorderDownloadFileParser;
import com.carlt.chelepie.protocolstack.recorder.VideoCropParser;
import com.carlt.doride.systemconfig.RuningConfig;
import com.carlt.doride.utils.FileUtil;
import com.carlt.doride.utils.Log;
import com.carlt.sesame.control.CPControl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * 车乐拍 下载控件
 * 
 * @author @Y.yun
 * 
 */
public class PieDownloadControl {
	/** 已下载完成 */
	public static final int DOWNLOADED = 0;
	/** 已在下载列表 */
	public static final int EXISTS = 1;
	/**下载成功*/
	public static final int SUCC = 2;
	/**下载shibai*/
	public static final int FAILD = 3;
	/**加入下载*/
	public static final int SUCCADD = 4;

	/**
	 * 文件下载的callback
	 */
	public static RecorderControl.GetTranslateProgressCallback mMediaDownloadCallback;
	
	
	public static List<RecorderControl.GetTranslateProgressCallback> mCropCallbacks = new ArrayList<RecorderControl.GetTranslateProgressCallback>();

	public static final DaoPieDownloadControl DaoPieInstance = DaoPieDownloadControl.getInstance();
	
	//是否在下载
	private static boolean mIsMediaDownloading = false;
	
	private static boolean mIsPauseMediaDownloading = false;
	
	private static RecorderDownloadFileParser mMediaDownParser;// 下载媒体文件的Parser
	private static Thread mMediaDownloader;
	
	public static RecorderControl.GetTranslateProgressCallback mThumbnailTranslaCallback;
	public static CPControl.GetResultListCallback mMediaThumbnailDownloadCallback;

	/** 下载媒体库文件缩略图*/
	private static Timer mTimer;

	private static VideoCropParser mCropParser;
	

	public static void MakeAllLoadingStop() {
		
		ArrayList<PieDownloadInfo> unFinishedList = DaoPieInstance.getUnFinishedList();
		if(null == unFinishedList || unFinishedList.isEmpty()){
			return;
		}
		for (int i = 0; i < unFinishedList.size(); i++) {
			PieDownloadInfo pdj = unFinishedList.get(i);
			pdj.setStatus(PieDownloadInfo.STATUS_STOP);
			DaoPieInstance.update(pdj);
		}
		if (mMediaDownloadCallback != null) {
			mMediaDownloadCallback.onTranslateProgress(unFinishedList);
		}
	}


	// 车乐拍媒体库，文件下载
	/**
	 * 添加到下载队列
	 * 
	 * @param pInfo
	 * @return
	 */
	public static int media_AddToDownload(PieDownloadInfo pInfo) {
		if (!mIsMediaDownloading) {
			media_StartDownload();
		}
		if(null == pInfo){
			return SUCCADD;
		}
		
		//数据库中有相同的条目的，更新
		String srcName = pInfo.getFileSrcName();
		PieDownloadInfo temp = DaoPieDownloadControl.getInstance().getByName(srcName, PieInfo.getInstance().getDeviceName());
		if (temp != null) {
			if (temp.getStatus() == PieDownloadInfo.STATUS_FINISHED) {
				return DOWNLOADED;
			} else if (temp.getStatus() == PieDownloadInfo.STATUS_STOP || temp.getStatus() == PieDownloadInfo.STATUS_UNLOAD) {
				pInfo.setStatus(PieDownloadInfo.STATUS_UNLOAD);
				DaoPieInstance.update(pInfo);
				return EXISTS;
			}else if( temp.getStatus() == PieDownloadInfo.STATUS_LOADING){
				return EXISTS;
			}
		}
		//新的下载条目，添加到数据库
		pInfo.setStatus(PieDownloadInfo.STATUS_UNLOAD);
		DaoPieInstance.insert(pInfo);
		return SUCCADD;
	}

	public static void media_RemoveFromDownload(PieDownloadInfo mInfo) {
		DaoPieInstance.delete(mInfo);
		if (mInfo.getStatus() == PieDownloadInfo.STATUS_LOADING) {
			mMediaDownParser.setContinue(false);
		}
		FileUtil.deleteFile(new File(mInfo.getLocalPath()));
		DaoPieInstance.delete(mInfo);
	}

	public static void media_PauseMediaDownloading() {
		mIsPauseMediaDownloading = true;
		if(mMediaDownParser != null){
			mMediaDownParser.setContinue(false);
		}
	}

	public static void media_ResumeMediaDownloading() {
		mIsPauseMediaDownloading = false;
	}

	static class MediaDownloader extends Thread {

		@Override
		public void run() {
			mIsMediaDownloading = true;
			while (mIsMediaDownloading && DeviceConnectManager.isDeviceConnect()) {
				if (RuningConfig.ISCropRuning || mIsPauseMediaDownloading) {
					SystemClock.sleep(1000);
					continue;
				}

				PieDownloadInfo pie = media_GetNextToDownload();
				if (pie == null) {
					SystemClock.sleep(1000);
					continue;
				}

				try {
					mMediaDownParser = new RecorderDownloadFileParser(mMediaDownloadCallback, pie);
					mMediaDownParser.run();
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				if (pie.getStatus() == PieDownloadInfo.STATUS_FINISHED) {
					// 下载成功了
					// 移除掉
					// --移除要通知调用 Adapter notifyDataSetChanged
					if (mMediaDownloadCallback != null) {
						mMediaDownloadCallback.onFinished(pie);
					} else {
						media_DonwloadFinish(pie);
					}
				} else {
					if (mMediaDownloadCallback != null) {
						mMediaDownloadCallback.onErro(pie);
					} 
				}

//				Log.e("Download", ":======media_download==" + mToDownloadList.size());
			}
			mIsMediaDownloading = false;
			mMediaDownloader = null;
		}

	}

	private static PieDownloadInfo media_GetNextToDownload() {
		try {
			ArrayList<PieDownloadInfo> mToDownloadList = DaoPieInstance.getUnFinishedList();
			if(null == mToDownloadList || mToDownloadList.isEmpty()){
				return null;
			}
			for (int i = 0; i < mToDownloadList.size(); i++) {
				PieDownloadInfo mPieInfo = mToDownloadList.get(i);
				if (mPieInfo.getStatus() == PieDownloadInfo.STATUS_UNLOAD) {
					return mPieInfo;
				}
			}
		}catch (Exception e) {
			Log.e("Download", "error");
		}
		return null;
	}

	public static void media_ReDownload(PieDownloadInfo mDownloadInfo) {
		mDownloadInfo.setStatus(PieDownloadInfo.STATUS_UNLOAD);
		mDownloadInfo.setDownloadLen(0);
		DaoPieInstance.update(mDownloadInfo);
		if (!mIsMediaDownloading) {
			media_StartDownload();
		}
	}

	public static List<PieDownloadInfo> media_GetToDownloadList() {
		
		 ArrayList<PieDownloadInfo> unFinishedList = DaoPieInstance.getUnFinishedList();
		 if(unFinishedList == null){
			 unFinishedList = new ArrayList<PieDownloadInfo>();
		 }
		 return unFinishedList;
	}

	public static void media_StartDownload() {
		if (!mIsMediaDownloading || mMediaDownloader == null || !mMediaDownloader.isAlive()) {
			mIsMediaDownloading = true;
			mMediaDownloader = new MediaDownloader();
			mMediaDownloader.start();
		}
	}

	public static void media_StopMediaDownload() {
		mIsMediaDownloading = false;
		if (mMediaDownParser != null) {
			mMediaDownParser.setContinue(false);
		}
	}

	public static void media_DonwloadFinish(PieDownloadInfo mInfo) {
		mInfo.setStatus(PieDownloadInfo.STATUS_FINISHED);
		ArrayList<PieDownloadInfo> mToDownloadList = DaoPieInstance.getUnFinishedList();
		if(null != mToDownloadList && !mToDownloadList.isEmpty()){
			mToDownloadList.remove(mInfo);
		}
	}

	public static void media_SetMediaDownlistener(RecorderControl.GetTranslateProgressCallback mMediaCallback) {
		mMediaDownloadCallback = mMediaCallback;
		if (mMediaDownParser != null) {
			mMediaDownParser.setListener(mMediaDownloadCallback);
		}
	}

	public static boolean media_IsMediaDownloading() {
		return mIsMediaDownloading;
	}

	/**
	 * 剪裁
	 * @param startTime
	 * @param endTime
	 */


	public static void crop_DownloadCrop(final String startTime, final String endTime) {
		//---开始 剪裁，停止正在下载的任务
		if(mMediaDownParser != null){
			mMediaDownParser.setContinue(false);
		}
		mCropParser = new VideoCropParser(innerCropCallback, startTime, endTime);
		mCropParser.start();
	}

	public static void crop_AddCropListener(RecorderControl.GetTranslateProgressCallback listener) {
		if (!mCropCallbacks.contains(listener)) {
			mCropCallbacks.add(listener);
		}
	}

	public static void crop_RemoveCropListener(RecorderControl.GetTranslateProgressCallback listener) {
		mCropCallbacks.remove(listener);
	}

	public static void crop_StopCrop() {
		if (mCropParser != null) {
			mCropParser.stopCrop();
		}
	}

	static RecorderControl.GetTranslateProgressCallback innerCropCallback = new RecorderControl.GetTranslateProgressCallback() {

		@Override
		public void onUpdateProgress(int progress) {
			if (mCropCallbacks.size() > 0) {
				for (int i = 0; i < mCropCallbacks.size(); i++) {
					mCropCallbacks.get(i).onUpdateProgress(progress);
				}
			}
		}

		@Override
		public void onTranslateProgress(Object progress) {
			if (mCropCallbacks.size() > 0) {
				for (int i = 0; i < mCropCallbacks.size(); i++) {
					mCropCallbacks.get(i).onTranslateProgress(progress);
				}
			}
		}

		@Override
		public void onFinished(Object o1) {
			if (mCropCallbacks.size() > 0) {
				for (int i = 0; i < mCropCallbacks.size(); i++) {
					mCropCallbacks.get(i).onFinished(o1);
				}
			}
		}

		@Override
		public void onErro(Object o) {
			if (mCropCallbacks.size() > 0) {
				for (int i = 0; i < mCropCallbacks.size(); i++) {
					mCropCallbacks.get(i).onErro(o);
				}
			}
		}
	};

	public static void PrintInfos() {
		mTimer = new Timer();
		mTimer.schedule(new TimerTask() {

			@Override
			public void run() {
//				Log.e("CheckNet", ":======the net speed ==" + StringUtils.caculateSpeed());
			}
		}, 1000, 1000);
	}

	public static void StopPrint() {
		if (mTimer != null) {
			mTimer.cancel();
		}
		mTimer = null;
	}

}
