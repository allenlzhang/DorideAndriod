package com.carlt.chelepie.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.provider.MediaStore.Images;

import com.carlt.chelepie.data.recorder.PieDownloadInfo;
import com.carlt.doride.utils.LocalConfig;


public class ThumbnailUtil {
	/**
	 * 计算图片的缩小比例
	 * 
	 * @param options
	 * @param reqWidth
	 *            (只拿宽度比对，宽度>600 则按比例压缩；否则传原图)
	 * @return
	 */
	private static float calculate(BitmapFactory.Options options, float reqWidth) {
		// Raw height and width of image
		final float width = options.outWidth;
		float inSampleSize = 1;

		if (width > reqWidth) {
			float widthRatio = (reqWidth / width);
			inSampleSize = widthRatio;
		}

		return inSampleSize;
	}

	/**
	 * 根据路径获得压缩Bitmap
	 * 
	 * @param filePath
	 * @return
	 */
	public static Bitmap getImgThumbnail(String filePath) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
		float f = calculate(options, 183);
		Matrix matrix = new Matrix();
		matrix.postScale(f, f); // 长和宽放大缩小的比例
		Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, options.outWidth,
				options.outHeight, matrix, true);
		return resizeBmp;
	}

	/**
	 * 根据指定的图像路径和大小来获取缩略图 此方法有两点好处： 1.
	 * 使用较小的内存空间，第一次获取的bitmap实际上为null，只是为了读取宽度和高度，
	 * 第二次读取的bitmap是根据比例压缩过的图像，第三次读取的bitmap是所要的缩略图。 2.
	 * 缩略图对于原图像来讲没有拉伸，这里使用了2.2版本的新工具ThumbnailUtils，使 用这个工具生成的图像不会被拉伸。
	 * 
	 * @param imagePath
	 *            图像的路径
	 * @param width
	 *            指定输出图像的宽度
	 * @param height
	 *            指定输出图像的高度
	 * @return 生成的缩略图
	 */
	public static Bitmap getImageThumbnail(String imagePath, int width,
                                           int height) {
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// 获取这个图片的宽和高，注意此处的bitmap为null
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		options.inJustDecodeBounds = false; // 设为 false
		// 计算缩放比
		int h = options.outHeight;
		int w = options.outWidth;
		int beWidth = w / width;
		int beHeight = h / height;
		int be = 1;
		if (beWidth < beHeight) {
			be = beWidth;
		} else {
			be = beHeight;
		}
		if (be <= 0) {
			be = 1;
		}
		options.inSampleSize = be;
		// 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		// 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}

	public static Bitmap getImageThumbnail(Context context, ContentResolver cr,
                                           String Imagepath) {
		ContentResolver testcr = context.getContentResolver();
		String[] projection = { Images.Media.DATA,
				Images.Media._ID, };
		String whereClause = Images.Media.DATA + " = '" + Imagepath
				+ "'";
		Cursor cursor = testcr.query(
				Images.Media.EXTERNAL_CONTENT_URI, projection,
				whereClause, null, null);
		int _id = 0;
		String imagePath = "";
		if (cursor == null || cursor.getCount() == 0) {
			return null;
		}
		if (cursor.moveToFirst()) {
			int _idColumn = cursor.getColumnIndex(Images.Media._ID);
			int _dataColumn = cursor
					.getColumnIndex(Images.Media.DATA);
			do {
				_id = cursor.getInt(_idColumn);
				imagePath = cursor.getString(_dataColumn);
			} while (cursor.moveToNext());
		}
		cursor.close();
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inDither = false;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		Bitmap bitmap = Images.Thumbnails.getThumbnail(cr, _id,
				Images.Thumbnails.MINI_KIND, options);
		return bitmap;
	}

	// 获取视频缩略图--方法1
	public static Bitmap getVideoThumbnail(String filePath, int width,
                                           int height, int kind) {
		// 定義一個Bitmap對象bitmap；
		Bitmap bitmap = null;

		// ThumbnailUtils類的截取的圖片是保持原始比例的，但是本人發現顯示在ImageView控件上有时候有部分沒顯示出來；
		// 調用ThumbnailUtils類的靜態方法createVideoThumbnail獲取視頻的截圖；
		bitmap = ThumbnailUtils.createVideoThumbnail(filePath, kind);

		// 調用ThumbnailUtils類的靜態方法extractThumbnail將原圖片（即上方截取的圖片）轉化為指定大小；
		// 最後一個參數的具體含義我也不太清楚，因為是閉源的；
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);

		// 放回bitmap对象；
		return bitmap;
	}

	// 获取视频缩略图--方法2
	public static Bitmap getVideoThumbnail(String filePath) {
		Bitmap b = null;
		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		try {
			retriever.setDataSource(filePath);
			b = retriever.getFrameAtTime();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (RuntimeException e) {
			e.printStackTrace();

		} finally {
			try {
				retriever.release();
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
		}
		return b;
	}
	
	public static String getThumbnailPath(PieDownloadInfo pInfo) {
		String fileName =  pInfo.getFileName().replace("mp4", "jpg").replace("video", "thm");
		String path = LocalConfig.GetMediaPath(pInfo.getAccout(), pInfo.getDeviceName(), LocalConfig.DIR_THUMBNAIL) + fileName;
		return path;
	}
}
