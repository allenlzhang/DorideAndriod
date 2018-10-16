package com.carlt.sesame.http;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.lang.ref.SoftReference;
import java.util.HashMap;

public class AsyncImageLoader {
	public static AsyncImageLoader mLoader;
	private static AsyncImageLoaderListener mListener;

	private AsyncImageLoader() {
		// TODO Auto-generated constructor stub
	}

	public void setmListener(AsyncImageLoaderListener mListener) {
		this.mListener = mListener;
	}

	public static synchronized AsyncImageLoader getInstance() {
		if (mLoader == null) {
			mLoader = new AsyncImageLoader();
		}
		return mLoader;
	}

	public Bitmap getBitmapByUrl(String url) {
		if (url == null || url.equals("")) {
			return null;
		}
		Bitmap mBitmap = null;
		SoftReference<Bitmap> mReference = imageCache.get(url);
		if (mReference != null) {
			mBitmap = mReference.get();
		}
		if (mBitmap == null) {
			// 加载图片
			if (!mDownloadThreadList.containsKey(url)) {
				// 队列不存在
				ImageDownloadThread mImageDownloadThread = new ImageDownloadThread(
						url);
				mImageDownloadThread.start();
			}
		}
		return mBitmap;
	}

	private HashMap<String, SoftReference<Bitmap>> imageCache = new HashMap<String, SoftReference<Bitmap>>();

	public synchronized void AddImageCache(String url,
			SoftReference<Bitmap> mReference) {
		imageCache.put(url, mReference);
		if (mListener != null) {
			Message msg = new Message();
			msg.what = 0;
			msg.obj = url;
			mHandler.sendMessage(msg);

		}

	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			try {
				mListener.OnImgLoadFinished(msg.obj.toString(),
						imageCache.get(msg.obj.toString()).get());
			} catch (Exception e) {
				Log.e("info", "e==" + e);
			}

			super.handleMessage(msg);
		}
	};

	private HashMap<String, ImageDownloadThread> mDownloadThreadList = new HashMap<String, ImageDownloadThread>();

	public synchronized void AddmDownloadThreadList(String url,
			ImageDownloadThread mImageDownloadThread) {
		mDownloadThreadList.put(url, mImageDownloadThread);
	}

	public synchronized void RemovemDownloadThreadList(String url) {
		mDownloadThreadList.remove(url);
	}

	public interface AsyncImageLoaderListener {
		void OnImgLoadFinished(String url, Bitmap mBitmap);
	};

}
