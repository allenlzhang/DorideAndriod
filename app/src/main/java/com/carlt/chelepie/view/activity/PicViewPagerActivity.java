/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.carlt.chelepie.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.carlt.chelepie.control.DaoPieDownloadControl;
import com.carlt.chelepie.control.RecorderControl;
import com.carlt.chelepie.data.recorder.PieDownloadInfo;
import com.carlt.chelepie.data.recorder.PieInfo;
import com.carlt.chelepie.utils.CodecPlayerUtil;
import com.carlt.doride.R;
import com.carlt.doride.preference.UseInfoLocal;
import com.carlt.doride.ui.view.UUToast;
import com.carlt.doride.utils.FileUtil;
import com.carlt.doride.utils.LocalConfig;
import com.carlt.sesame.ui.activity.base.LoadingActivityWithTitle;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public class PicViewPagerActivity extends LoadingActivityWithTitle {

	public final static String FILEPIC = "filepic";

	public PieDownloadInfo pieInfo;

	private RelativeLayout picLay;

	private int storeType;
	private DaoPieDownloadControl mDaoPieDownloadControl = DaoPieDownloadControl
			.getInstance();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_bigpic);
		setTitleView(R.layout.head_back);
		picLay = (RelativeLayout) findViewById(R.id.pic_lay);
		picDownloadBtn = (ImageView) findViewById(R.id.pic_download_btn);
		Intent intent = getIntent();
		if (intent != null) {
			pieInfo = (PieDownloadInfo) intent.getParcelableExtra(FILEPIC);
		}
		initTitle();
		if (pieInfo != null) {
			storeType = pieInfo.getStoreType();
			initData();
		}
	}

	private void initTitle() {
		ImageView back = (ImageView) findViewById(R.id.head_back_img1);
		TextView title = (TextView) findViewById(R.id.head_back_txt1);
		ImageView imgRight = (ImageView) findViewById(R.id.head_back_img2);
		back.setImageResource(R.drawable.arrow_back);
		if (null != pieInfo && !TextUtils.isEmpty(pieInfo.getFileName())) {
			String name = pieInfo.getFileName();
			title.setText(name);
		}
		imgRight.setVisibility(View.INVISIBLE);

		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void initData() {
		photoView = new PhotoView(this);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		photoView.setLayoutParams(params);
		picLay.addView(photoView);
		//1.如果是本地文件直接展示
		//2.如果是设备文件先下载缓存，在播放		
		if (mDaoPieDownloadControl.isDownLoad(pieInfo)) {
			pieInfo = mDaoPieDownloadControl.getFinishedInfo(pieInfo);
			if (pieInfo != null && pieInfo.getStatus() == PieDownloadInfo.STATUS_FINISHED) {
				picDownloadBtn
						.setImageResource(R.drawable.player_download_succes);
				picDownloadBtn.setOnClickListener(null);
				LoadSuccess(null);
				showPic(pieInfo.getLocalPath());
			} else {
				showCheckLocal();
			}
		} else {
			showCheckLocal();
		
		}

	}

	/**
	 * 再次检查本地有没有
	 */
	private void showCheckLocal() {
		picDownloadBtn.setImageResource(R.drawable.player_download_selector);
		picDownloadBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				downloadPic();
			}
		});
		
		//检查缓存有没有
		String tempPath = LocalConfig.GetMediaPath(UseInfoLocal
				.getUseInfo().getAccount(), PieInfo.getInstance()
				.getDeviceName(),LocalConfig.DIR_TEMP) + pieInfo.getFileName();
		if(FileUtil.isExist(pieInfo.getLocalPath())){
			LoadSuccess(null);
			showPic(pieInfo.getLocalPath());
		}else if (FileUtil.isExist(tempPath)){
			LoadSuccess(null);
			pieInfo.setLocalPath(tempPath);
			showPic(tempPath);
		}else{
			LoadData();
			pieInfo.setStoreType(PieDownloadInfo.STORE_TEMP);
			RecorderControl.getDownLoadFile(downLoadLisener, pieInfo);
		}
	}

	public void showPic(String path) {
		Bitmap bm = BitmapFactory.decodeFile(path);
		photoView.setImageBitmap(bm);
	}

	/**
	 * 下载文件监听
	 */
	RecorderControl.GetTranslateProgressCallback downLoadLisener = new RecorderControl.GetTranslateProgressCallback() {
		@Override
		public void onFinished(Object o1) {

			Message msg = new Message();
			msg.what = 101;
			msg.obj = o1;
			mHandler.sendMessage(msg);
		}

		@Override
		public void onErro(Object o) {
			Message msg = new Message();
			msg.what = 102;
			msg.obj = o;
			mHandler.sendMessage(msg);
		}

		@Override
		public void onTranslateProgress(Object progress) {
		}

		@Override
		public void onUpdateProgress(int progress) {
		}

	};

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case 101: // 缓冲完成
				pieInfo = (PieDownloadInfo) msg.obj;
				LoadSuccess(null);
				showPic(pieInfo.getLocalPath());
				break;
			case 102: // 缓冲错误
				LoadSuccess(null);
				UUToast.showUUToast(PicViewPagerActivity.this,
						"获取图片失败");
				break;
			case CodecPlayerUtil.TYPE_DOWNLOAD_SUCCSE: // 下载成功
				UUToast.showUUToast(PicViewPagerActivity.this, "下载图片成功");
				picDownloadBtn
						.setImageResource(R.drawable.player_download_succes);
				picDownloadBtn.setClickable(false);
				pieInfo.setStatus(PieDownloadInfo.STATUS_FINISHED);
				pieInfo.setStoreType(storeType);
				DaoPieDownloadControl.getInstance().insert(pieInfo);
				break;
			case CodecPlayerUtil.TYPE_DOWNLOAD_ERR: // 下载失败
				UUToast.showUUToast(PicViewPagerActivity.this, "下载图片失败");
				break;
			}
		};
	};

	private PhotoView photoView;

	private ImageView picDownloadBtn;

	/**
	 * 下载
	 */
	private void downloadPic() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO TEST
				String oldPath = pieInfo.getLocalPath();

				String newPath = LocalConfig.GetMediaPath(UseInfoLocal
						.getUseInfo().getAccount(), PieInfo.getInstance()
						.getDeviceName(), pieInfo.getDir(storeType))
						+ pieInfo.getFileName();
				try {
					int bytesum = 0;
					int byteread = 0;
					File oldfile = new File(oldPath);
					File newfile = new File(newPath);
					if (oldfile.exists()) { // 文件存在时
						if (newfile.exists()) {
							mHandler.sendEmptyMessage(CodecPlayerUtil.TYPE_DOWNLOAD_SUCCSE);
							pieInfo.setLocalPath(newPath);
							return;
						}
						InputStream inStream = new FileInputStream(oldPath); // 读入原文件
						FileOutputStream fs = new FileOutputStream(newPath);
						byte[] buffer = new byte[1444];
						int length;
						while ((byteread = inStream.read(buffer)) != -1) {
							bytesum += byteread; // 字节数 文件大小
							System.out.println(bytesum);
							fs.write(buffer, 0, byteread);
						}
						inStream.close();
						mHandler.sendEmptyMessage(CodecPlayerUtil.TYPE_DOWNLOAD_SUCCSE);
						pieInfo.setLocalPath(newPath);
					} else {
						// 文件不存在
						mHandler.sendEmptyMessage(CodecPlayerUtil.TYPE_DOWNLOAD_ERR);
					}
				} catch (Exception e) {
					e.printStackTrace();
					FileUtil.deleteFile(new File(newPath));
					mHandler.sendEmptyMessage(CodecPlayerUtil.TYPE_DOWNLOAD_ERR);
				}
			}
		}).start();

	}
}
