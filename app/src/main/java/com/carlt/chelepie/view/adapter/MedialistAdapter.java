package com.carlt.chelepie.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.carlt.chelepie.control.DaoPieDownloadControl;
import com.carlt.chelepie.data.recorder.PieDownloadInfo;
import com.carlt.chelepie.utils.ThumbnailUtil;
import com.carlt.doride.DorideApplication;
import com.carlt.doride.R;
import com.carlt.doride.http.AsyncImageLoader;
import com.carlt.doride.utils.FileUtil;
import com.carlt.sesame.utility.MyTimeUtil;
import com.orhanobut.logger.Logger;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MedialistAdapter extends RecyclerView.Adapter<ViewHolder> {
	private LayoutInflater inflater;
	private Context mContext;
	public static final int TYPE_TIME_TOP = 1;
	public static final int TYPE_TIME = 2;
	public static final int TYPE_NORMAL = 3;
	public static final int TYPE_LINE = 4;
	public List<PieDownloadInfo> datalist;

	private MyItemClickListener mItemClickListener;
	
	private DaoPieDownloadControl mDaoPieDownloadControl = DaoPieDownloadControl.getInstance();

	public MedialistAdapter(Context context, ArrayList<PieDownloadInfo> datalist) {
		if(context!=null){
			mContext = context;
        }else{
        	mContext= DorideApplication.ApplicationContext;
        }
		inflater = LayoutInflater.from(context);
		this.datalist = datalist;
	}

	public void setData(ArrayList<PieDownloadInfo> datalist){
		this.datalist = datalist;
	}
	
	@Override
	public int getItemCount() {
		if (datalist != null) {
			return datalist.size();
		}
		return 0;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		PieDownloadInfo mInfo = datalist.get(position);
		switch (getItemViewType(position)) {

		case TYPE_TIME_TOP:
			// 第一行头的竖线不显示
			MyHodlerHeader itemHolderHeader = (MyHodlerHeader) holder;
			itemHolderHeader.mViewLineTop.setVisibility(View.GONE);
			itemHolderHeader.bindData(mInfo);
			break;
		case TYPE_TIME:
			MyHodlerHeader itemHolderHeader2 = (MyHodlerHeader) holder;
			itemHolderHeader2.mViewLineTop.setVisibility(View.VISIBLE);
			itemHolderHeader2.bindData(mInfo);
			break;
		case TYPE_LINE:
			MyHodler itemHolder = (MyHodler) holder;
			itemHolder.mViewBottomline.setVisibility(View.VISIBLE);
			itemHolder.mViewBottomline.getLayoutParams().height = 351;
			itemHolder.mLayMediainfo.setVisibility(View.GONE);
			itemHolder.bindData(mInfo);
			break;
		case TYPE_NORMAL:
			MyHodler itemHolder2 = (MyHodler) holder;
			itemHolder2.mViewBottomline.setVisibility(View.GONE);
			itemHolder2.mLayMediainfo.setVisibility(View.VISIBLE);
			itemHolder2.bindData(mInfo);
			break;
		}

	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		switch (viewType) {
		case TYPE_TIME_TOP:
			return new MyHodlerHeader(inflater.inflate(
					R.layout.item_medialist_header, parent, false));

		case TYPE_TIME:
			return new MyHodlerHeader(inflater.inflate(
					R.layout.item_medialist_header, parent, false));
		case TYPE_NORMAL:
			return new MyHodler(inflater.inflate(R.layout.item_medialist,
					parent, false));
		case TYPE_LINE:
			return new MyHodler(inflater.inflate(R.layout.item_medialist,
					parent, false));
		}
		return null;
	}

	@Override
	public int getItemViewType(int position) {
		PieDownloadInfo mInfo = datalist.get(position);
		if (position == 0) {
			return TYPE_TIME_TOP;
		} else {
			if (mInfo.getType() == PieDownloadInfo.TYPE_DATE||mInfo.getType() == PieDownloadInfo.TYPE_DATE_YEAR) {
				return TYPE_TIME;
			} else if (mInfo.getType() == PieDownloadInfo.TYPE_LINE) {
				return TYPE_LINE;
			} else {
				return TYPE_NORMAL;
			}
		}
	}

	/**
	 * 设置Item点击监听
	 * 
	 * @param listener
	 */
	public void setOnItemClickListener(MyItemClickListener listener) {
		this.mItemClickListener = listener;
	}

	public interface MyItemClickListener {
		public void onItemClick(View view, int postion);
	}

	class MyHodler extends ViewHolder implements OnClickListener {
		private View mViewBottomline, mLayMediainfo;// 左侧竖线,多媒体数据
		private ImageView mImgThumbnail, mImgType, mImgHasdown;// 缩略图,类别icon(视频、图片)、已经下载标记
		private TextView mTxtTime, mTxtSize;// 时间、大小

		public MyHodler(View itemView) {
			super(itemView);
			mViewBottomline = itemView
					.findViewById(R.id.medialist_view_bottomLine);
			mLayMediainfo = itemView.findViewById(R.id.medialist_lay_mediainfo);
			mImgType = (ImageView) itemView
					.findViewById(R.id.medialist_img_type);
			mImgHasdown = (ImageView) itemView
					.findViewById(R.id.medialist_img_hasdown);
			mImgThumbnail = (ImageView) itemView
					.findViewById(R.id.medialist_img_thumbnail);
			mTxtTime = (TextView) itemView
					.findViewById(R.id.medialist_txt_time);
			mTxtSize = (TextView) itemView
					.findViewById(R.id.medialist_txt_size);
			mImgThumbnail.setOnClickListener(MyHodler.this);
			
		}

		public void bindData(PieDownloadInfo info) {
			if (info.getType() == PieDownloadInfo.TYPE_H264) {
				mImgType.setImageDrawable(mContext.getResources().getDrawable(
						R.drawable.mediatype_video));
			} else if (info.getType() == PieDownloadInfo.TYPE_JPG) {
				mImgType.setImageDrawable(mContext.getResources().getDrawable(
						R.drawable.mediatype_pic));
			}

			String s = MyTimeUtil.parseDate3(info.getStartTime());
			if (!TextUtils.isEmpty(s)) {
				mTxtTime.setText(s);
			}
			
			if (info.getType() == PieDownloadInfo.TYPE_H264 || info.getType() == PieDownloadInfo.TYPE_JPG) {
				String thumbLocalPath = ThumbnailUtil.getThumbnailPath(info);
				Bitmap bit = AsyncImageLoader.getInstance().getBitmapByUrlLocalThumbnail(thumbLocalPath);
				if (bit == null) {
					mImgThumbnail.setImageResource(R.drawable.def_backgroud_pic);
					// --加入下载队列去下载
				} else {
					mImgThumbnail.setImageBitmap(bit);
				}
			}
			
			mTxtSize.setText(FileUtil.getFileShowSize(info.getTotalLen(), new DecimalFormat("0.00")));
			
			if(mDaoPieDownloadControl.isDownLoad(info)){
				mImgHasdown.setVisibility(View.VISIBLE);
			}else{
				mImgHasdown.setVisibility(View.GONE);
			}
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.medialist_img_thumbnail:
				// 跳转至播放页面
				mItemClickListener.onItemClick(v, getPosition());
				break;

			default:
				break;
			}

		}
	}

	class MyHodlerHeader extends ViewHolder {
		private TextView mTxtDate;// 日期
		private View mViewLineTop;// 日期轴线
		private TextView mTxtYear;//年份

		public MyHodlerHeader(View itemView) {
			super(itemView);
			mTxtDate = (TextView) itemView.findViewById(R.id.medialist_txt_date);
			mViewLineTop = itemView.findViewById(R.id.medialist_view_lineTop);
			mTxtYear=(TextView) itemView.findViewById(R.id.medialist_txt_year);
		}

		public void bindData(PieDownloadInfo info) {
			String s = MyTimeUtil.parseDate2(info.getStartTime());
			// 2018-10-20
			String dates [] = s.split("-");
			if(dates.length < 3){
				return;
			}
			if (!TextUtils.isEmpty(s)) {
//				StringBuffer date = new StringBuffer();
//				String dateMonth = s.substring(4, 6);
//				String dateDay = s.substring(6);
//				date.append(dateMonth);
//				date.append("/");
//				date.append(dateDay);
				mTxtDate.setText(dates[1]+"/"+ dates[2]);
			}
			Logger.e(info.getType() + "=================================");
			if(info.getType()==PieDownloadInfo.TYPE_DATE_YEAR){
				//需要展示年份
				mTxtYear.setVisibility(View.VISIBLE);
				String year=s.substring(0,4);
				mTxtYear.setText(dates[0]);
			}else if(info.getType()==PieDownloadInfo.TYPE_DATE){
				//不需要展示年份
				mTxtYear.setVisibility(View.GONE);
			}
		}
	}
}
