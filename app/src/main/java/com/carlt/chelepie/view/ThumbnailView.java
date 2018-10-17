package com.carlt.chelepie.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.carlt.chelepie.control.RecorderControl;
import com.carlt.chelepie.data.recorder.PieDownloadInfo;
import com.carlt.chelepie.view.activity.FullPlayActivity;
import com.carlt.doride.R;
import com.carlt.doride.http.AsyncImageLoader;
import com.carlt.doride.systemconfig.RuningConfig;
import com.carlt.doride.ui.view.UUToast;
import com.carlt.doride.utils.LocalConfig;
import com.carlt.doride.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;


public class ThumbnailView extends LinearLayout implements RecorderControl.GetTranslateProgressCallback {

	private CoverFlow mGallery;
	private SeekBar mSeekBar;
	private CutSeekBar mCutSeekBar;
	private ImageView play;
	private TextView mTime;
	private Context mContext;
	private List<PieDownloadInfo> mThumbnailInfoList = new ArrayList<PieDownloadInfo>();
	private AsyncImageLoader mImgLoader = AsyncImageLoader.getInstance();
	private ThumbnailAdapter mAdapter;

	private int selectedPos;// 选中的position
	
	private boolean isCut = false;

	/**
	 * 是否是剪裁页面
	 */
	public void setIsCut(boolean flag){
		isCut = flag;
	}
	
	public ThumbnailView(Context context) {
		this(context, null);
	}

	public ThumbnailView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		// 导入布局
		LayoutInflater.from(context).inflate(R.layout.thumbnail_view, this, true);
		mGallery = (CoverFlow) findViewById(R.id.thumbnail_gallery);
		mGallery.setAnimationDuration(1000);
		mSeekBar = (SeekBar) findViewById(R.id.thumbnail_seekbar);
		mCutSeekBar = (CutSeekBar) findViewById(R.id.thumbnail_cutbar);
		mCutSeekBar.setProgressLow(100);
		mCutSeekBar.setProgressHigh(200);

		mTime = (TextView) findViewById(R.id.thumbnail_text_time);
		play = (ImageView) findViewById(R.id.thumbnail_play);
		mSeekBar.setOnSeekBarChangeListener(mOnSeekBarChangeListener);
		mGallery.setOnItemSelectedListener(mOnItemSelectedListener);

		mCutSeekBar.setOnSeekBarChangeListener(mCutSeekBarListener);
		play.setOnClickListener(palylistener);
		mAdapter = new ThumbnailAdapter();
		mGallery.setAdapter(mAdapter);
	}

	/**
	 * 刷新页面
	 */
	public void notifydata(){
		mAdapter.notifyDataSetChanged();
	}
	
	/**
	 *  是否显示播放按钮
	 * @param flag
	 */
	public void setPlayViewShow(boolean flag){
		if (null != play) {
			if (flag) {
				play.setVisibility(View.VISIBLE);
			} else {
				play.setVisibility(View.GONE);
			}
		}
	}
	
	
	private OnClickListener palylistener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// 播放按钮
			if (RuningConfig.ISCropRuning) {
				UUToast.showUUToast(mContext, "暂时无法观看回放，请等待剪裁完成");
			} else {
				if(null != mThumbnailInfoList && mThumbnailInfoList.size() > 0){
				PieDownloadInfo mInfo = mThumbnailInfoList.get(selectedPos);
				Intent mIntent = new Intent(mContext, FullPlayActivity.class);
				mIntent.putExtra(FullPlayActivity.Time, mInfo.getStartTime());
				mContext.startActivity(mIntent);
				}else{
					UUToast.showUUToast(mContext, "暂无回放数据");
				}
			}
		}
	};

	public void changeCutMode() {
		setPlayViewShow(false);
		RelativeLayout.LayoutParams layparams = (RelativeLayout.LayoutParams) mSeekBar.getLayoutParams();
		if (mCutSeekBar.getVisibility() != View.GONE) {
			mCutSeekBar.setVisibility(View.GONE);
			layparams.bottomMargin = 0;

		} else {
			mCutSeekBar.setVisibility(View.VISIBLE);
			layparams.bottomMargin = (int) mContext.getResources().getDimension(R.dimen.thumbnail_seekbar_margin_bottom);
		}
		mSeekBar.setLayoutParams(layparams);
	}

	public void changePlayMode() {
		setPlayViewShow(true);
		RelativeLayout.LayoutParams layparams = (RelativeLayout.LayoutParams) mSeekBar.getLayoutParams();

			mCutSeekBar.setVisibility(View.GONE);
			layparams.bottomMargin = 0;
		mSeekBar.setLayoutParams(layparams);
	}
	
	private OnItemSelectedListener mOnItemSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			selectedPos = arg2;
			try {
				mSeekBar.setProgress(arg2);
				mTime.setText(mThumbnailInfoList.get(arg2).getStartTime());
			} catch (Exception e) {
				Log.e("info", "mSeekBar切换失败");
			}

		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}

	};
	private OnSeekBarChangeListener mOnSeekBarChangeListener = new OnSeekBarChangeListener() {

		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
		}

		@Override
		public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
			try {
				mGallery.setSelection(arg1);
				mTime.setText(mThumbnailInfoList.get(arg1).getStartTime());
			} catch (Exception e) {
				Log.e("info", "mGallery切换失败");
			}

		}
	};

	CutSeekBar.OnSeekBarChangeListener mCutSeekBarListener = new CutSeekBar.OnSeekBarChangeListener() {

		@Override
		public void onProgressChanged(CutSeekBar seekBar, double progressLow, double progressHigh, double max, double min) {
		}

	};

	public double getMax() {
		return mCutSeekBar.getMax();
	}

	public double getMin() {
		return mCutSeekBar.getMin();
	}

	public double getProgressLow() {
		return mCutSeekBar.getProgressLow();
	}

	public double getProgressHigh() {
		return mCutSeekBar.getProgressHigh();
	}

	public String getTime() {
		return mTime.getText().toString();
	}

	public void loadData(List<PieDownloadInfo> mThumbnailInfoList) {
		this.mThumbnailInfoList = mThumbnailInfoList;
		mAdapter.notifyDataSetChanged();
		int size = mThumbnailInfoList.size();
		if (isCut) {
			play.setVisibility(View.GONE);
			mSeekBar.setMax(0);
			if(size == 0){
				mSeekBar.setMax(0);
			}else if(size > 0){
				mSeekBar.setMax(this.mThumbnailInfoList.size() - 1);
			}
		} else {
			play.setVisibility(View.VISIBLE);
			mSeekBar.setMax(this.mThumbnailInfoList.size() - 1);
		}
	}

	public void ImgLoadFinished(String url, Bitmap mBitmap) {
		if (mAdapter != null) {
			mAdapter.notifyDataSetChanged();
		}

	}

	public class ThumbnailAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public ThumbnailAdapter() {
			mInflater = LayoutInflater.from(mContext);
		}

		@Override
		public int getCount() {
			if (mThumbnailInfoList != null) {
				return mThumbnailInfoList.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {

			if (convertView == null) {
				convertView = new ImageView(mContext);
			}

			ImageView imgV = (ImageView) convertView;
			PieDownloadInfo mThumbnailInfo = mThumbnailInfoList.get(position);
			Bitmap bit = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.main_live_bg);
			String path = LocalConfig.GetMediaPath(mThumbnailInfo.getAccout(), mThumbnailInfo.getDeviceName(), LocalConfig.DIR_THUMBNAIL)
					+ mThumbnailInfo.getFileName();
			Log.e("ThumbnailView", "回放显示缩略图。。。" + path);
			Bitmap bitMap = mImgLoader.getBitmapByUrlLocalThumbnail(path);
			if (bitMap != null) {
				Log.e("info", " 获取图片成功。。。。。");
				bitMap = createReflectedImages(mContext, bitMap);
				imgV.setImageBitmap(bitMap);
			} else {
				bit = createReflectedImages(mContext, bit);
				imgV.setImageBitmap(bit);
			}

			DisplayMetrics dm = new DisplayMetrics();
			WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
			windowManager.getDefaultDisplay().getMetrics(dm);
			int ScreenWith = dm.widthPixels;
			int ScreenHeight = dm.heightPixels;
			float ScreenDensity = dm.density;
			imgV.setLayoutParams(new CoverFlow.LayoutParams(100 * (int) ScreenDensity, 100 * (int) ScreenDensity));
			imgV.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
			// 设置的抗锯齿
			BitmapDrawable drawable = (BitmapDrawable) imgV.getDrawable();
			drawable.setAntiAlias(true);

			return imgV;
		}

		class Holder {
			private ImageView mImageView;
		}

		/**
		 * 设置镜像图像
		 * 
		 * @param mContext
		 * @param imageId
		 * @return
		 */
		public Bitmap createReflectedImages(Context mContext, Bitmap originalImage) {
			final int reflectionGap = 4;
			final int border = 2;
			int width = originalImage.getWidth();
			int height = originalImage.getHeight();

			Matrix matrix = new Matrix();
			matrix.preScale(1, -1);

			Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0, height / 2, width, height / 2, matrix, false);
			Bitmap bitmapWithReflection = Bitmap.createBitmap(width + 2 * border, (height + height / 2 + reflectionGap + 2 * border), Config.ARGB_8888);

			Canvas canvas = new Canvas(bitmapWithReflection);
			canvas.drawBitmap(originalImage, border, border, null);
			Paint deafaultPaint = new Paint();
			canvas.drawRect(border, 2 * border + height, width + border, height + 2 * border + reflectionGap, deafaultPaint);

			deafaultPaint.setColor(Color.WHITE);
			deafaultPaint.setStrokeWidth(border);
			deafaultPaint.setStyle(Style.STROKE);
			canvas.drawRect(0, 0, width + 2 * border, height + 2 * border, deafaultPaint);
			canvas.drawBitmap(reflectionImage, 0, height + reflectionGap + border, null);
			deafaultPaint.reset();
			LinearGradient shader = new LinearGradient(0, originalImage.getHeight(), 0, bitmapWithReflection.getHeight() + reflectionGap + border, 0x70ffffff,
					0x00ffffff, TileMode.MIRROR);
			deafaultPaint.setShader(shader);
			deafaultPaint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
			canvas.drawRect(0, height + 2 * border + reflectionGap, width + border * 2, bitmapWithReflection.getHeight() + reflectionGap + 2 * border + height,
					deafaultPaint);

			return bitmapWithReflection;
		}

	}

	@Override
	public void onFinished(Object o1) {
		final PieDownloadInfo pInfo = (PieDownloadInfo) o1;
		Message msg = new Message();
		msg.what = 0;
		msg.obj = pInfo;
		mHandler.sendMessage(msg);
	}

	@Override
	public void onErro(Object o) {
		mHandler.sendEmptyMessage(1);
	}

	@Override
	public void onTranslateProgress(Object progress) {
		mHandler.sendEmptyMessage(2);
	}

	@Override
	public void onUpdateProgress(int progress) {
	}

	public Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
			case 1:
			case 2:
				if (mAdapter != null) {
					mAdapter.notifyDataSetChanged();
					mSeekBar.setMax(mThumbnailInfoList.size() - 1);
					mSeekBar.setProgress(selectedPos);
				}
				break;
			}
		};
	};
	
	/**
	 * 设置seekbar 选中的时间进度
	 * @param time
	 */
	public void setPickedTime(String time){
		if(StringUtils.isEmpty(time)){
			return;
		}
		int j = 0;
		for (int i = 0; i < mThumbnailInfoList.size(); i++) {
			if(TextUtils.equals(mThumbnailInfoList.get(i).getStartTime(),time)){
				j = i;
				break;
			}
		}
		mSeekBar.setProgress(j);
		mTime.setText(mThumbnailInfoList.get(j).getStartTime());
	}

}
