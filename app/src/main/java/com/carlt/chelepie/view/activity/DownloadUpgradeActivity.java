
package com.carlt.chelepie.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.chelepie.data.recorder.UpgradeInfo;
import com.carlt.chelepie.download.DownloadBaseInfo;
import com.carlt.chelepie.download.DownloadBaseThread;
import com.carlt.chelepie.download.HttpDownloadThread;
import com.carlt.chelepie.view.gif.GifView;
import com.carlt.doride.MainActivity;
import com.carlt.doride.R;
import com.carlt.doride.base.BaseActivity;
import com.carlt.doride.ui.view.UUToast;
import com.carlt.doride.utils.FileUtil;
import com.carlt.doride.utils.LocalConfig;

import java.text.DecimalFormat;

/**
 * 选车型和绑定设备合二为一页面
 * 
 * @author daisy
 */
public class DownloadUpgradeActivity extends BaseActivity implements OnClickListener, DownloadBaseThread.OnDownloadListner {
	TextView mNextTime;
	TextView mDonwNow;
	TextView mTips1;
	TextView mTips2;
	ImageView mImg;
//	GifView mGif;
	UpgradeInfo minfo;
	private float size;
	boolean isGon = true;

	String tip1 = "是否立即更新您的设备";
	String tip2 = "正在下载更新内容";
	DecimalFormat mDF = new DecimalFormat("0.00");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_download);
		minfo = (UpgradeInfo) getIntent().getSerializableExtra("info");
		if (minfo == null || !minfo.isUpgrade) {
			onBackPressed();
			return;
		}

		String path = getLoaclSavePath();
		long len = FileUtil.getFileLength(path);

		init();
	}

	private void init() {
		mNextTime = (TextView) findViewById(R.id.download_txt_nextTime);
		mDonwNow = (TextView) findViewById(R.id.download_txt_downNow);
		mTips1 = (TextView) findViewById(R.id.download_txt_Tip1);
		mTips2 = (TextView) findViewById(R.id.download_txt_Tip2);
		mImg = (ImageView) findViewById(R.id.download_img_bg);
//		mGif = (GifView) findViewById(R.id.download_gif_bg);

		mTips1.setText(tip1);
		size = (float) minfo.size / 1024 / 1024;
		mTips2.setText("更新大小约" + mDF.format(size) + "MB，请尽量在Wi-Fi下进行");
//		mGif.setVisibility(View.GONE);
		mNextTime.setOnClickListener(this);
		mDonwNow.setOnClickListener(this);
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				mTips2.setText("已下载完成:" + msg.obj + "%");
				break;
			case 1:
				if(isGon){
					DownloadBaseInfo dbi = (DownloadBaseInfo) msg.obj;
					Intent intent = new Intent(DownloadUpgradeActivity.this, MainActivity.class);
					intent.putExtra("filePath", dbi.getPathLocal());
					startActivity(intent);
				}
				break;
			case 2:
				UUToast.showUUToast(DownloadUpgradeActivity.this, "下载失败" + msg.obj);
				if(isGon){
					onBackPressed();
				}
				break;
			default:
				break;
			}
		}

	};

	public void onBackPressed() {
		super.onBackPressed();
		isGon = false;
		finish();
	}

	@Override
	public void onClick(View v) {
		if (v.equals(mNextTime)) {
			onBackPressed();
		} else if (v.equals(mDonwNow)) {
			mTips1.setText(tip2);
			mTips2.setText("已下载完成:0%");
			mImg.setVisibility(View.GONE);
//			mGif.setVisibility(View.VISIBLE);
//			mGif.setStart(this);
			mNextTime.setVisibility(View.GONE);
			mDonwNow.setVisibility(View.GONE);
			DownloadBaseInfo mBaseInfo = new DownloadBaseInfo();
			mBaseInfo.setUrl(minfo.url);
			mBaseInfo.setLengthTotal(minfo.size+"");
			HttpDownloadThread mThread = new HttpDownloadThread(mBaseInfo, this);
			mThread.setSDPath(LocalConfig.mDownLoadFileSavePath_SD);
			mThread.start();
		}
	}



	@Override
	public void onStatusChanged(DownloadBaseInfo mDownloadBaseInfo) {
	}

	@Override
	public void onStart(DownloadBaseInfo mDownloadBaseInfo) {
	}

	@Override
	public void onProgress(int progress) {
		Message msg = new Message();
		msg.what = 0;
		msg.obj = progress;
		mHandler.sendMessage(msg);
	}

	@Override
	public void onPause(DownloadBaseInfo mDownloadBaseInfo) {
	}

	@Override
	public void onFinished(DownloadBaseInfo mDownloadBaseInfo) {
		Message msg = new Message();
		msg.what = 1;
		msg.obj = mDownloadBaseInfo;
		mHandler.sendMessage(msg);
	}

	@Override
	public void onFailed(String error) {
		Message msg = new Message();
		msg.what = 2;
		msg.obj = error;
		mHandler.sendMessage(msg);
	}

	protected String getLoaclSavePath() {
		int tmp = minfo.url.lastIndexOf("/");
		String fileURL = null;
		String tempFileURL_sdcard = LocalConfig.mDownLoadFileSavePath_SD + minfo.url.substring(tmp + 1);
		String tempFileURL_internal = LocalConfig.mDownLoadFileSavePath_Absolute + minfo.url.substring(tmp + 1);
		if (FileUtil.isExCardExist()) {
			// SD卡存在
			fileURL = tempFileURL_sdcard;
		} else {
			// SD卡不存在
			fileURL = tempFileURL_internal;
		}
		return fileURL;
	}
}
