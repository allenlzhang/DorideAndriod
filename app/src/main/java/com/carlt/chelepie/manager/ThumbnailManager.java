/**
 * 
 */
package com.carlt.chelepie.manager;

import android.os.SystemClock;


import com.carlt.chelepie.control.DaoPieDownloadControl;
import com.carlt.chelepie.data.recorder.PieDownloadInfo;
import com.carlt.chelepie.protocolstack.recorder.RecorderGetThumbnailFileInfoListParser;
import com.carlt.doride.utils.Log;
import com.carlt.sesame.utility.MyTimeUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * 循环下载 缩略图列表信息
 * 
 * @author @Y.yun
 */
public class ThumbnailManager implements Runnable {
	private static final String TAG = "ThumbnailManager";
	private static boolean mIsRun = false;
	public static final String TIME_START = "1970-01-01 00:00:00";

	private static String lastEndTime = TIME_START;
	private String startTime = "1970-01-01 00:00:00";
	private String endTime = MyTimeUtil.ENDTIME;
	long startLoopTime = 0;
	boolean startLoop = false;
	int loopCount = 0;
	DaoPieDownloadControl mDaoControl = DaoPieDownloadControl.getInstance();
	public static List<PieDownloadInfo> mDatas = Collections.synchronizedList(new ArrayList<PieDownloadInfo>());
	public static int mCurrentNum = 0;
	public static Thread mCurrent = null;
	public static List<PieDownloadInfo> mTheReapt = new ArrayList<PieDownloadInfo>();

	@Override
	public void run() {
		mIsRun = true;
		Log.e(TAG, "进来循环搜索文件~~~~~~~~~" + mDatas.size());
		while (mIsRun && DeviceConnectManager.isDeviceConnect()) {
			//--如果断开重连的话不用从新开始，接着上次开始就好
			if (lastEndTime.equals(TIME_START)) {
				RecorderGetThumbnailFileInfoListParser listParser = new RecorderGetThumbnailFileInfoListParser(null,startTime,endTime);
				listParser.run();
				List<PieDownloadInfo> lists = listParser.getReturn().getArrays();
				if (lists.size() > 0) {
					startTime = lists.get(0).getStartTime();
					lastEndTime = startTime;
					//首次的话要把之前的都清除掉
					mDatas.clear();
					mDatas.addAll(lists);
					sortTheDatas();
					Log.e(TAG, "mDatas1"+lists.toString());
					
				}

				SystemClock.sleep(1000);
				continue;
			}

			startLoop = true;
			if (startLoop) {
				Log.e(TAG, "1分钟循环一次的~~~~~~~~~~" + mDatas.size());

				if (loopCount != 0 && loopCount % 5 == 0) {
					Log.e(TAG, "这个是2分钟进来一次，检测是否有视频被覆盖掉");
					///// ---循环时长大于5分钟,从新获取一下开始时间，把本地图片信息更新一下
					RecorderGetThumbnailFileInfoListParser listParser = new RecorderGetThumbnailFileInfoListParser(null,startTime,endTime);
//					RecorderGetHVideoFileInfoListParser listParser = new RecorderGetHVideoFileInfoListParser(null);
					listParser.run();
					List<PieDownloadInfo> lists = listParser.getReturn().getArrays();
					if (lists.size() > 0) {
						mDatas.clear();
						mDatas.addAll(lists);
						sortTheDatas();
						Log.e(TAG, "mDatasstartLoop"+lists.toString());	
					}
				}
				SystemClock.sleep(1000 * 20 * 1);
				loopCount++;
			}
		}
		Log.e(TAG, "结束循环搜索缩略图~~~~~~~~~~" + mDatas.size());
		mIsRun = false;
		startLoop = false;
		lastEndTime = TIME_START;
	}


	/**
	 * 
	 */
	public void sortTheDatas() {
		try {
			Collections.sort(mDatas, Comparator_BY_TIME);
		} catch (Exception e) {
			Log.e(TAG, "缩略图排序出错~~~~~~~~~~" + mDatas.size());
		}
		
		for (int i = 0; i < mTheReapt.size(); i++) {
			mDatas.remove(mTheReapt.get(i));
		}
		mTheReapt.clear();
	}


	public static void startCheckThumbnail() {
		if (!mIsRun || mCurrent == null || !mCurrent.isAlive()) {
			mCurrent = new Thread(new ThumbnailManager());
			mCurrent.start();
		}
	}

	public static void stopThumbnail() {
		mIsRun = false;
	}

	/**
	 * 
	 * 将缩略图按时间排序
	 * 从小到大排列	 * 
	 * 
	 */
	public static final Comparator<PieDownloadInfo> Comparator_BY_TIME = new Comparator<PieDownloadInfo>() {

		@Override
		public int compare(PieDownloadInfo lhs, PieDownloadInfo rhs) {
			Date d1 = null;
			Date d2 = null;

			try {
				d1 = MyTimeUtil.commonFormat.parse(lhs.getStartTime());
			} catch (Exception e) {
				mTheReapt.add(lhs);
				return 0;
			}

			try {
				d2 = MyTimeUtil.commonFormat.parse(rhs.getStartTime());
			} catch (Exception e) {
				mTheReapt.add(rhs);
				return 0;
			}

			if (d1.before(d2)) {
				return -1;
			} else if (d1.after(d2)) {
				return 1;
			} else {
				mTheReapt.add(lhs);
			}

			return 0;
		}
	};

}
