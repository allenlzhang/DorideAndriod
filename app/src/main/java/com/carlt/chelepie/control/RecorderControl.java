package com.carlt.chelepie.control;

import android.util.Log;


import com.carlt.chelepie.data.recorder.BaseResponseInfo;
import com.carlt.chelepie.data.recorder.PieDownloadInfo;
import com.carlt.chelepie.data.recorder.PieVersion;
import com.carlt.chelepie.data.recorder.ThumbnailInfo;
import com.carlt.chelepie.download.DownloadBaseInfo;
import com.carlt.chelepie.download.DownloadBaseThread;
import com.carlt.chelepie.download.FtpDownloadThread;
import com.carlt.chelepie.ftp.FtpConnector;
import com.carlt.chelepie.protocolstack.recorder.ContinuePlayBackParser;
import com.carlt.chelepie.protocolstack.recorder.DeleteFileParser;
import com.carlt.chelepie.protocolstack.recorder.EditPieNameParser;
import com.carlt.chelepie.protocolstack.recorder.EditPiePasswordParser;
import com.carlt.chelepie.protocolstack.recorder.PausePlayBackParser;
import com.carlt.chelepie.protocolstack.recorder.PieVersionParser;
import com.carlt.chelepie.protocolstack.recorder.RecorderBindDeviceParser;
import com.carlt.chelepie.protocolstack.recorder.RecorderCaptureRecordParser;
import com.carlt.chelepie.protocolstack.recorder.RecorderDownloadFileParser;
import com.carlt.chelepie.protocolstack.recorder.RecorderFormatSDParser;
import com.carlt.chelepie.protocolstack.recorder.RecorderGetCaptureFileInfoListParser;
import com.carlt.chelepie.protocolstack.recorder.RecorderGetEventFileInfoListParser;
import com.carlt.chelepie.protocolstack.recorder.RecorderGetHVideoFileInfoListParser;
import com.carlt.chelepie.protocolstack.recorder.RecorderSettingParser;
import com.carlt.chelepie.protocolstack.recorder.RecorderStorageParser;
import com.carlt.chelepie.protocolstack.recorder.RecorderTimeParser;
import com.carlt.chelepie.protocolstack.recorder.RecorderUpdateParser;
import com.carlt.chelepie.protocolstack.recorder.RestarPieParser;
import com.carlt.chelepie.protocolstack.recorder.RestoreFactoryParser;
import com.carlt.chelepie.protocolstack.recorder.SetAudioParser;
import com.carlt.chelepie.protocolstack.recorder.SetImgParser;
import com.carlt.chelepie.protocolstack.recorder.SetStreamParser;
import com.carlt.chelepie.protocolstack.recorder.SetVideoParser;
import com.carlt.chelepie.protocolstack.recorder.StartMonitorParser;
import com.carlt.chelepie.protocolstack.recorder.StartPlayBackParser;
import com.carlt.chelepie.protocolstack.recorder.StopMonitorParser;
import com.carlt.chelepie.protocolstack.recorder.StopPlayBackParser;
import com.carlt.chelepie.protocolstack.recorder.TakePhotoParser;
import com.carlt.chelepie.utils.TarAndGz;
import com.carlt.doride.http.FileDownloadThread;
import com.carlt.doride.utils.FileUtil;
import com.carlt.doride.utils.LocalConfig;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;

import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class RecorderControl {

	public interface GetTranslateProgressCallback {
		void onFinished(Object o1);

		void onErro(Object o);

		void onTranslateProgress(Object progress);

		void onUpdateProgress(int progress);
	}

	private static RecorderControl mRecorderControl;

	private FtpConnector mFtpConnector;

	public static RecorderControl getInstance() {
		if (mRecorderControl == null) {
			mRecorderControl = new RecorderControl();
		}
		return mRecorderControl;
	}

	private RecorderControl() {
		mFtpConnector = new FtpConnector();
	}

	// 根目录
	public final static String CATALOG_ROOT = "C_ROOT";

	// 全部
	public final static String CATALOG_ALL = "C_ALL";

	// 车乐拍目录前缀
	public static String CATALOG_PREFIX;

	// 车乐拍目录前缀-缺省值
	public final static String CATALOG_PREFIX_DEFAULT = "/home/mmcdisk/";

	// 抓拍
	public final static String CATALOG_CAPTURE = "chelerpie/user/";

	// 事件
	public final static String CATALOG_EVENT = "chelerpie/event/";

	// 记录
	public final static String RECORD = "chelerpie/record/";

	// 裁剪文件
	public final static String CROP = "chelerpie/temp/crop.mp4";

	// 回放
	public final static String PLAYBACK = "/playback/";

	// 直播
	public final static String LIVE_Stream = "/stream/";

	// 上传固件程序路径
	public final static String UPDATE = "program/";

	private static String getPath(String childPath) {
		if (CATALOG_PREFIX == null || CATALOG_PREFIX.length() <= 0) {
			CATALOG_PREFIX = CATALOG_PREFIX_DEFAULT;
		}
		return CATALOG_PREFIX + childPath;
	}



	private int getFileLength(FTPFile[] mFileList) {
		int count1 = 0;
		if (mFileList != null) {
			for (int i = 0; i < mFileList.length; i++) {
				FTPFile mFTPFile = mFileList[i];
				String name = mFTPFile.getName();
				if (name.endsWith(".mp4")) {
					count1++;
				} else if (name.endsWith(".jpg")) {
					count1++;
				}
			}
		}

		return count1;
	}

	/**
	 * 通知固件升级
	 * 
	 * @param listener
	 *            进度提醒
	 * @param filePath
	 *            升级文件位置
	 */
	public static void GetDeviceUpdate(final GetTranslateProgressCallback listener, final String filePath) {
		if (listener == null)
			return;
		RecorderUpdateParser updateParser = new RecorderUpdateParser(listener, filePath);
		updateParser.start();
	}

	/**
	 * 按天下载预览图接口
	 * 
	 * @param day
	 *            (yyyymmdd) 返回ArrayList<ThumbnailInfo>
	 */
	public static void downloadThumbnailByDay(final String day, final GetResultListCallback listener) {
		if (listener == null)
			return;
		new Thread() {
			@Override
			public void run() {
				Log.e("info", "开始");
				if (!FileUtil.isExCardExist()) {
					BaseResponseInfo mBaseResponseInfo = new BaseResponseInfo();
					mBaseResponseInfo.setFlag(BaseResponseInfo.ERRO);
					mBaseResponseInfo.setInfo("SD卡不存在");
					listener.onErro(mBaseResponseInfo);
					return;
				}
				FtpConnector mFtp = new FtpConnector();
				int code = mFtp.openConnect();
				if (code == FtpConnector.CODE_SUCCESS) {
					// mFtp.getFile(serverPath)
					// SD卡存在
					final String folder = LocalConfig.mDownLoadFileSavePath_SD + "thumbnail/" + day + "/";

					// 判断路径是否存在
					FileUtil.openOrCreatDir(folder);
					FTPFile[] mFileList = mFtp.getFile(RECORD + day + "/");

					// 测试代码
					// String testPath =
					// "ChelerPie/record/20150711/R_20150711112552.tar";
					// FTPFile[] mFileList = mFtp.getFile(testPath);
					// 测试代码结束

					if (mFileList == null) {
						// 硬件上的文件夹被删除后 删除本地的文件夹
						File mFile = new File(folder);
						mFile.delete();
						listener.onFinished(null);
						return;
					}
					DownloadBaseThread.OnDownloadListner downloadListner = new DownloadBaseThread.OnDownloadListner() {
						@Override
						public void onStatusChanged(DownloadBaseInfo mDownloadBaseInfo) {

						}

						@Override
						public void onStart(DownloadBaseInfo mDownloadBaseInfo) {

						}

						@Override
						public void onProgress(int progress) {

						}

						@Override
						public void onPause(DownloadBaseInfo mDownloadBaseInfo) {

						}

						@Override
						public void onFinished(DownloadBaseInfo mDownloadBaseInfo) {
							// 下载完成
							try {
								TarAndGz.unTar(new File(folder + day + ".tar"), folder + "/R_20150711112552/");
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								Log.e("info", "解压出错--==" + e);
							}
							listener.onFinished(getThumbnailInfo(folder + day + ".tar"));
						}

						@Override
						public void onFailed(String error) {

						}
					};
					Log.e("info", "服务器文件个数==" + mFileList.length);
					// 下载当日缩略图文件夹
					DownloadBaseInfo mDownloadBaseInfo = new DownloadBaseInfo();
					mDownloadBaseInfo.setUrl(RECORD + day);
					FtpDownloadThread mFtpDownloadThread = new FtpDownloadThread(mDownloadBaseInfo, downloadListner, "");
					mFtpDownloadThread.setSDPath(folder);
					mFtpDownloadThread.start();

				} else {
					BaseResponseInfo mBaseResponseInfo = new BaseResponseInfo();
					mBaseResponseInfo.setFlag(BaseResponseInfo.ERRO);
					mBaseResponseInfo.setInfo("连接失败:" + code);
					listener.onErro(mBaseResponseInfo);
				}

				Log.e("info", "synchronized==GetRootDirectory");
			}
		}.start();
	}

	/**
	 * 取出文件夹下所有缩略图并按时间排序
	 * 
	 * @param folder
	 *            (yyyymmdd) 返回ArrayList<ThumbnailInfo>
	 */
	private static ArrayList<ThumbnailInfo> getThumbnailInfo(String folder) {

		ArrayList<ThumbnailInfo> mThumbnailInfoList = new ArrayList<ThumbnailInfo>();

		File mFile = new File(folder);

		if (mFile != null) {
			if (mFile.isDirectory()) {
				File[] mFileList = mFile.listFiles();
				for (File file : mFileList) {
					if (file.isDirectory()) {
						mFileList = file.listFiles();
						break;
					} else {
						String name = file.getName();
						if (name.endsWith(".jpg")) {

							ThumbnailInfo mThumbnailInfo = new ThumbnailInfo();
							mThumbnailInfo.setName(name);
							mThumbnailInfo.setThumbLocalPath(file.getAbsolutePath());
							String nameHard = name.substring(name.lastIndexOf("_") + 1, name.lastIndexOf("."));
							mThumbnailInfo.setNameHard(nameHard);
							mThumbnailInfoList.add(mThumbnailInfo);
						}
					}
				}

				for (File file : mFileList) {

					String name = file.getName();
					if (name.endsWith(".jpg")) {

						ThumbnailInfo mThumbnailInfo = new ThumbnailInfo();
						mThumbnailInfo.setName(name);
						mThumbnailInfo.setThumbLocalPath(file.getAbsolutePath());
						String nameHard = name.substring(name.lastIndexOf("_") + 1, name.lastIndexOf("."));
						mThumbnailInfo.setNameHard(nameHard);
						mThumbnailInfoList.add(mThumbnailInfo);
					}
				}

			}
		}

		// 排序
		Collections.sort(mThumbnailInfoList, new Comparator() {
			@Override
			public int compare(Object o1, Object o2) {
				long nameHard1 = Long.parseLong(((ThumbnailInfo) o1).getNameHard());

				long nameHard2 = Long.parseLong(((ThumbnailInfo) o2).getNameHard());
				if (nameHard1 > nameHard2) {
					return 1;
				} else if (nameHard1 == nameHard2) {
					return 0;
				} else {
					return -1;
				}
			}
		});

		return mThumbnailInfoList;
	}

	/**
	 * 获取车乐拍设置信息
	 * 
	 * @param listener
	 * @return null 访问成功后通过PieInfo获取信息
	 */
	public static void getRecorderSetting(final GetResultListCallback listener) {
		new Thread() {
			@Override
			public void run() {

				RecorderSettingParser mParser = new RecorderSettingParser(listener);
				mParser.start();

			}
		}.start();
	}

	/**
	 * 获取硬件SD卡信息
	 * 
	 * @param listener
	 * @return PieSDcardInfo
	 */
	public static void getRecorderSD(final GetResultListCallback listener) {
		RecorderStorageParser parser = new RecorderStorageParser(listener);
		parser.start();
	}

	/**
	 * 格式化硬件SD卡
	 * 
	 * @param listener
	 * @return null
	 */
	public static void formatRecorderSD(final GetResultListCallback listener,RecorderFormatSDParser.JinduResultListCallback jinduListener) {
		RecorderFormatSDParser mParser = new RecorderFormatSDParser(listener, BaseResponseInfo.class,jinduListener);
		mParser.start();
	}

	/**
	 * 修改设备名称
	 * 
	 * @param listener
	 * @return null
	 */
	public static void EditPieName(final String name, final GetResultListCallback listener) {
		EditPieNameParser mParser = new EditPieNameParser(listener);
		mParser.setSsid(name);
		mParser.start();
	}

	/**
	 * 修改设备密码
	 * 
	 * @param listener
	 * @return null
	 */
	public static void EditPiePassword(String psw, GetResultListCallback listener) {
		EditPiePasswordParser mParser = new EditPiePasswordParser(listener);
		mParser.setPassword(psw);
		mParser.start();
	}

	/**
	 * 获取车乐拍版本信息
	 * 
	 * @param listener
	 * @return
	 */
	public static void getPieVersion(final GetResultListCallback listener) {
		PieVersionParser mParser = new PieVersionParser(listener, PieVersion.class);
		mParser.start();
	}
	/**
	 * 设置视频
	 * 
	 * @param listener
	 *            * 以下参数传空视为不改
	 * @param quality
	 *            画质区分低，中，高 0:SD1080,1:HD720,
	 * @return
	 */
	public static void setVideoSize(final int quality, final GetResultListCallback listener) {
		SetVideoParser mParser = new SetVideoParser(listener);
		mParser.setQuality(quality);
		mParser.start();
	}
	public static void setStopMonitorParser(final GetResultListCallback listener) {
		StopMonitorParser mParser = new StopMonitorParser(listener);
		mParser.start();
	}

	/**
	 * 
	 * 设置抓拍是否录制视频
	 * 
	 * @param listener
	 */
	public static void setCaptureRecordVideo(final GetResultListCallback listener) {
		RecorderCaptureRecordParser mParser = new RecorderCaptureRecordParser(listener);
		mParser.start();
	}

	/**
	 * 设置音频
	 * 
	 * @param listener
	 *            * 以下参数传空视为不改
//	 * @param MicEnhance
//	 *            麦克风增强开关 * @param MicEnhance 麦克风增强（谭工说要给他的，具体干啥的不知道）
	 * @return
	 */
	public static void setAudio(final GetResultListCallback listener) {
		SetAudioParser mParser = new SetAudioParser(listener);
		mParser.start();
	}

	/**
	 * 设置录像时是否录音
	 * 
	 * @param listener
	 *            * 以下参数传空视为不改
	 * @param RecordingSound
	 *            录像时录音
	 * @return
	 */
	public static void setStream(final Boolean RecordingSound, final CPControl.GetResultListCallback listener) {
		SetStreamParser mParser = new SetStreamParser(listener, BaseResponseInfo.class);
		if (RecordingSound != null) {
			// mParser.setAudio_enable(RecordingSound);
		}
		mParser.start();
	}

	/**
	 * 设置视频
	 * 
	 * @param listener
	 * @param brightness
	 *            亮度
	 * @param contrast
	 *            对比
	 * @param saturation
	 *            饱和
	 * @return
	 */
	public static void setVideoColor(final int brightness, final int contrast, final int saturation, final CPControl.GetResultListCallback listener) {
		SetImgParser imgParser = new SetImgParser(listener);
		imgParser.setBrightness(brightness);
		imgParser.setContrast(contrast);
		imgParser.setSaturate(saturation);
		imgParser.start();
	}

	public static void getStorageInfo(final CPControl.GetResultListCallback listener) {
		RecorderStorageParser parser = new RecorderStorageParser(listener);
		parser.start();
	}

	/**
	 * 上传固件程序
	 * 
	 * @param filePath
	 *            文件本地路径
	 * @param listener
	 * @return <String> 文件名
	 */
	public static void ftpUpload(final String filePath, final CPControl.GetResultListCallback listener) {
		new Thread() {
			@Override
			public void run() {
				FtpConnector mConnector = new FtpConnector();
				int code = mConnector.openConnect();
				if (code == FtpConnector.CODE_SUCCESS) {
					int result = mConnector.uploadFtp(filePath, getPath(UPDATE));
					if (result == FtpConnector.CODE_SUCCESS) {
						int index = filePath.lastIndexOf("/");
						String fileName = filePath.substring(index);
						listener.onFinished(fileName);
					} else {
						BaseResponseInfo mInfo = new BaseResponseInfo();
						mInfo.setInfo("ftp文件上传失败");
						listener.onErro(mInfo);
					}
				}
			}
		}.start();
	}

	/**
	 * 删除文件
	 * 
	 * @param listener
	 * @return
	 */
	public static void deleteFile(final PieDownloadInfo pieInfo, final GetResultListCallback listener) {
		DeleteFileParser mParser = new DeleteFileParser(listener,pieInfo);
		mParser.start();
	}

	/**
	 * 拍照
	 * 
	 * @param listener
	 * @return
	 */
	public static void takePhoto(final GetResultListCallback listener) {
		TakePhotoParser mParser = new TakePhotoParser(listener);
		mParser.start();
	}

	/**
	 * 重启车乐拍
	 * 
	 */
	public static void getRestarPie(final GetResultListCallback listener) {
		new Thread() {
			@Override
			public void run() {
				RestarPieParser mParser = new RestarPieParser(listener, BaseResponseInfo.class);
				mParser.start();
			}
		}.start();
	}

	/**
	 * 恢复出厂设置
	 * 
	 */
	public static void getRestoreFactory(final GetResultListCallback listener) {
		new Thread() {
			@Override
			public void run() {
				RestoreFactoryParser mParser = new RestoreFactoryParser(listener, BaseResponseInfo.class);
				mParser.start();
			}
		}.start();
	}

	/**
	 * 
	 * 开始直播
	 */
	public static void startMonitor(final GetResultListCallback listener) {
		StartMonitorParser monitorParser = new StartMonitorParser(listener);
		monitorParser.start();
	}

	/**
	 * 
	 * 开始回放
	 */
	public static void startPlayback(final GetResultListCallback listener, final String startTime) {
		StartPlayBackParser parser = new StartPlayBackParser(listener, startTime);
		parser.start();
	}
	/**
	 * 回放停止
	 * @param listener
	 */
	public static void stopPlayback(final GetResultListCallback listener) {
		StopPlayBackParser parser = new StopPlayBackParser(listener);
		parser.start();
	}

	/**
	 * 获取系统信息
	 * 
	 * @param listener
	 */
	public static void getSysInfo(final GetResultListCallback listener) {
		RecorderSettingParser rsp = new RecorderSettingParser(listener);
		rsp.start();
	}
	
	public static void BindDevice(final GetResultListCallback listener, final String uid) {
		RecorderBindDeviceParser bindDeviceParser = new RecorderBindDeviceParser(listener, uid);
		bindDeviceParser.start();
	}

//	/**
//	 * 
//	 * 获取抓拍文件列表
//	 * 
//	 * @param listener
//	 * @param startTime
//	 * @param endTime
//	 * @param type
//	 */
//	public static void getCaptureFilelist(final GetResultListCallback listener, String startTime, String endTime, int type) {
//		RecorderGetCaptureFileInfoListParser parser = new RecorderGetCaptureFileInfoListParser(listener, startTime, endTime, type);
//		parser.start();
//	}
	
	
	public static void PausePlayBack(final GetResultListCallback listener) {
		PausePlayBackParser parser = new PausePlayBackParser(listener);
		parser.start();
	}

	public static void ContinuePlayBack(final GetResultListCallback listener) {
		ContinuePlayBackParser parser = new ContinuePlayBackParser(listener);
		parser.start();
	}	
	
	/**
	 * 获取抓拍文件列表
	 * 
	 * @param listener
	 * @param startTime
	 *            可传null
	 * @param isContinue
	 *            是否接着上次请求
	 */
	public static void getCaptureFilelist(final GetResultListCallback listener, String startTime, boolean isContinue) {
		RecorderGetCaptureFileInfoListParser parser = new RecorderGetCaptureFileInfoListParser(listener, startTime, isContinue);
		parser.start();
	}

	public static void getEventFilelist(final GetResultListCallback listener, String startTime, boolean isContinue) {
		RecorderGetEventFileInfoListParser parser = new RecorderGetEventFileInfoListParser(listener, startTime, isContinue);
		parser.start();
	}
	public static void getAllVedioFilelist(final GetResultListCallback listener, String startTime, boolean isContinue) {
		RecorderGetHVideoFileInfoListParser parser = new RecorderGetHVideoFileInfoListParser(listener, startTime, isContinue);
		parser.start();
	}
	
	
	static RecorderDownloadFileParser recorderDownloadFileParser = null;
	/**
	 * 下载文件
	 * @param listener
	 * @param info
	 */
	public static void getDownLoadFile(final GetTranslateProgressCallback listener,  PieDownloadInfo info) {
		recorderDownloadFileParser = new RecorderDownloadFileParser(listener, info);
		recorderDownloadFileParser.start();
	}
	/**
	 * 停止下载文件
	 * @param info
	 */
	public static void stopDownLoadFile(PieDownloadInfo info) {
		if(null != recorderDownloadFileParser){
			recorderDownloadFileParser.stopDownload();
		}
	}
	
	/**
	 * App向记录仪下发对时时间
	 */
	public static void setRecorderTime(final GetResultListCallback listener,long time) {
		RecorderTimeParser parser = new RecorderTimeParser(listener,time);
		parser.start();
	}

}
