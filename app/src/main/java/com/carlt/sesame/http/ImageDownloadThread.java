package com.carlt.sesame.http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.carlt.doride.utils.LocalConfig;
import com.carlt.sesame.utility.FileUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;

public class ImageDownloadThread extends Thread {

	private String imageUrl;

	private AsyncImageLoader mAsyncImageLoader;

	private final String SUFFIX = "";
	private String iconFileURL_sdcard;
	private String iconFileURL_internal;

	public ImageDownloadThread(String imageUrl) {
		this.imageUrl = imageUrl;
		mAsyncImageLoader = AsyncImageLoader.getInstance();
		mAsyncImageLoader.AddmDownloadThreadList(imageUrl, this);

	}

	@Override
	public void run() {
		Bitmap mBitmap = null;
		if (imageUrl.startsWith("htt")) {
			// 网络图片
			int tmp = imageUrl.lastIndexOf(".");
			String houzhui = imageUrl.substring(tmp);
			String imageUrlMd5 = FileUtil.stringToMD5(imageUrl);
			String fileURL = null;
			iconFileURL_sdcard = LocalConfig.mImageCacheSavePath_SD
					+ imageUrlMd5 + houzhui + SUFFIX;
			iconFileURL_internal = LocalConfig.mImageCacheSavePath_Absolute
					+ imageUrlMd5 + houzhui + SUFFIX;
			if (FileUtil.isExist(iconFileURL_sdcard)) {
				// 首先判断本地SD卡上文件是否存在
				fileURL = ReDownImg(iconFileURL_sdcard);
			} else if (FileUtil.isExist(iconFileURL_internal)) {
				// 然后判断内部存储路径文件是否存在
				fileURL = ReDownImg(iconFileURL_internal);

			} else {
				fileURL = DownImg();
			}
			mBitmap = BitmapFactory.decodeFile(fileURL);
		} else {
			// 本地图片
			mBitmap = BitmapFactory.decodeFile(imageUrl);
		}

		if (mBitmap != null) {
			SoftReference<Bitmap> mReference = new SoftReference<Bitmap>(
					mBitmap);
			mAsyncImageLoader.AddImageCache(imageUrl, mReference);
		}
		mAsyncImageLoader.RemovemDownloadThreadList(imageUrl);
	}

	private String DownImg() {
		// 本地文件不存在通过网络下载
		InputStream data = null;
		String fileURL = "";
		HttpConnector hc = new HttpConnector();
		hc.setTimeout(10000);
		int state = HttpConnector.CONNECT_UNAVAILABLE;
		state = hc.connect(imageUrl, null);
		if (state == HttpConnector.CONNECT_OK) {
			data = hc.getInputStream();
		}
		if (null != data) {
			// 优先使用SD卡
			if (FileUtil.isExCardExist()) {
				fileURL = iconFileURL_sdcard;
			} else {

				fileURL = iconFileURL_internal;
			}
			FileUtil.saveToFile(data, fileURL);
			try {
				data.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fileURL;
	}

	private String ReDownImg(final String localPath) {
		File mLocalImg = new File(localPath);
		long localSize = mLocalImg.length();
		// 本地文件不存在通过网络下载
		InputStream data = null;
		HttpConnector hc = new HttpConnector();
		hc.setTimeout(10000);
		int state = HttpConnector.CONNECT_UNAVAILABLE;
		state = hc.connect(imageUrl, null);
		if (state == HttpConnector.CONNECT_OK) {
			long serviceSize = hc.getDataLength();
			if (localSize == serviceSize) {

			} else {
				data = hc.getInputStream();
				if (null != data) {
					// 优先使用SD卡
					mLocalImg.delete();
					FileUtil.saveToFile(data, localPath);
					try {
						data.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		return localPath;
	}

}
