package com.carlt.chelepie.utils;

import android.graphics.Bitmap;


import com.carlt.doride.http.AsyncImageLoader;

import java.lang.ref.SoftReference;

public class ThumbnailDownloadThread extends Thread {

	private String path;
	private int width;
	private int height;
	
	private int type;
	public final static int TYPE_PIC=1;//图片
	public final static int TYPE_VIDEO=2;//视频

	private AsyncImageLoader mAsyncImageLoader;
	

	public ThumbnailDownloadThread(String path, int width, int height, int type) {
		this.path = path;
		this.width=width;
		this.height=height;
		this.type=type;
		mAsyncImageLoader = AsyncImageLoader.getInstance();
		mAsyncImageLoader.AddmDownloadThreadList(path, this);

	}

	@Override
	public void run() {
		Bitmap mBitmap = null;
		if(type==TYPE_PIC){
			mBitmap = ThumbnailUtil.getImageThumbnail(path, width, height);
		}else if(type==TYPE_VIDEO){
			mBitmap = ThumbnailUtil.getVideoThumbnail(path);
		}
		

		if (mBitmap != null) {
			SoftReference<Bitmap> mReference = new SoftReference<Bitmap>(
					mBitmap);
			mAsyncImageLoader.AddImageCache(path, mReference);
		}
		mAsyncImageLoader.RemovemDownloadThreadList(path);
	}


}
