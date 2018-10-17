package com.carlt.doride.http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.carlt.chelepie.utils.ThumbnailDownloadThread;

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

	private HashMap<String, Thread> mDownloadThreadList = new HashMap<String, Thread>();

	public synchronized void AddmDownloadThreadList(String url,
			Thread mImageDownloadThread) {
		mDownloadThreadList.put(url, mImageDownloadThread);
	}

	public synchronized void RemovemDownloadThreadList(String url) {
		mDownloadThreadList.remove(url);
	}

	public interface AsyncImageLoaderListener {
		void OnImgLoadFinished(String url, Bitmap mBitmap);
	};
	
	public Bitmap getBitmapByUrlLocalThumbnail(String path) {
		if (path == null || path.equals("")) {
			return null;
		}
		Bitmap mBitmap = null;
		try {
			mBitmap = null;
			SoftReference<Bitmap> mReference = imageCache.get(path);
			if (mReference != null) {
				mBitmap = mReference.get();
			}
			if (mBitmap == null) {
				Bitmap bit = BitmapFactory.decodeFile(path);
				if (bit != null) {
					if (bit.getWidth() != 176) {
						bit = Bitmap.createBitmap(bit, 0, 0, 176, 144);
					}
					SoftReference<Bitmap> sr = new SoftReference<Bitmap>(bit);
					imageCache.put(path, sr);
					return sr.get();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return mBitmap;
	}

	public Bitmap getBitmapByUrlLocalThumbnailPic(String path, int width,
												  int height) {
		if (path == null || path.equals("")) {
			return null;
		}

		Bitmap mBitmap = null;
		SoftReference<Bitmap> mReference = imageCache.get(path);
		if (mReference != null) {
			mBitmap = mReference.get();
		}
		if (mBitmap == null) {
			// 加载图片
			if (!mDownloadThreadList.containsKey(path)) {
				// 队列不存在
				ThumbnailDownloadThread mThumbnailDownloadThread = new ThumbnailDownloadThread(
						path,width,height,ThumbnailDownloadThread.TYPE_PIC);
				mThumbnailDownloadThread.start();
			}
		}

		return mBitmap;
	}

	public Bitmap getBitmapByUrlLocalThumbnailVideo(String path) {
		if (path == null || path.equals("")) {
			return null;
		}
		Bitmap mBitmap = null;
		SoftReference<Bitmap> mReference = imageCache.get(path);
		if (mReference != null) {
			mBitmap = mReference.get();
		}
		if (mBitmap == null) {
			// 加载图片
			if (!mDownloadThreadList.containsKey(path)) {
				// 队列不存在
				ThumbnailDownloadThread mThumbnailDownloadThread = new ThumbnailDownloadThread(
						path,0,0,ThumbnailDownloadThread.TYPE_VIDEO);
				mThumbnailDownloadThread.start();
			}
		}
		return mBitmap;
	}
}
