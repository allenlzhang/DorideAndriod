package com.carlt.chelepie.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;


import com.carlt.chelepie.data.recorder.PieDownloadInfo;
import com.carlt.doride.DorideApplication;
import com.carlt.doride.R;
import com.carlt.doride.http.AsyncImageLoader;
import com.carlt.doride.utils.FileUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * 已下载列表Adapter
 * 
 * @author Daisy
 * 
 */
public class HasdownlistAdapter extends RecyclerView.Adapter<ViewHolder> {
	private LayoutInflater inflater;
	private Context mContext;
	private MedialistAdapter.MyItemClickListener mItemClickListener;
	public ArrayList<PieDownloadInfo> datalist;
	private OnDeleteListener mOnDeleteListener;
	private SparseBooleanArray mSelectedPositions = new SparseBooleanArray();
	private SparseBooleanArray mShowDeletePositions = new SparseBooleanArray();
	private boolean isEditable;// 是否是可以编辑状态
	int width = DorideApplication.dpToPx(62);
	int height = DorideApplication.dpToPx(48);

	private AsyncImageLoader mAsyncImageLoader = AsyncImageLoader.getInstance();

	private final static int TYPE_FOOTER = 1;// 最后一个item
	private final static int TYPE_NORMAL = 2;// 正常item

	public HasdownlistAdapter(Context context,
                              ArrayList<PieDownloadInfo> datalist) {
		mContext = context;
		inflater = LayoutInflater.from(context);
		this.datalist = datalist;
	}

	// 获得选中条目的结果
	public ArrayList<PieDownloadInfo> getSelectedItem() {
		ArrayList<PieDownloadInfo> selectList = new ArrayList<PieDownloadInfo>();
		for (int i = 0; i < datalist.size(); i++) {
			if (isItemChecked(i)) {
				selectList.add(datalist.get(i));
			}
		}
		return selectList;
	}

	// 设置给定位置条目的选择状态
	public void setItemChecked(int position, boolean isChecked) {
		mSelectedPositions.put(position, isChecked);
	}

	// 根据位置判断条目是否选中
	private boolean isItemChecked(int position) {
		return mSelectedPositions.get(position);
	}

	// 设置给定位置条目的选择状态
	public void setItemDeleteShow(int position, boolean isShow) {
		mShowDeletePositions.put(position, isShow);
	}

	// 根据位置判断条目是否展示了删除按钮
	private boolean isItemDeleteShow(int position) {
		return mShowDeletePositions.get(position);
	}

	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
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
		case TYPE_FOOTER:
			//底部item
			MyHodlerFooter itemHolder1 = (MyHodlerFooter) holder;
			break;

		case TYPE_NORMAL:
			//正常item
			MyHodler itemHolder2 = (MyHodler) holder;
			itemHolder2.bindData(mInfo, position);
			break;
		}

	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		switch (viewType) {
		case TYPE_FOOTER:
			// 底部item
			return new MyHodlerFooter(inflater.inflate(
					R.layout.item_hasdown_footer, parent, false));

		case TYPE_NORMAL:
			// 正常item
			return new MyHodler(inflater.inflate(R.layout.item_hasdown, parent,
					false));
		}

		return new MyHodlerFooter(inflater.inflate(
				R.layout.item_hasdown_footer, parent, false));
	}

	@Override
	public int getItemViewType(int position) {
		if(position==datalist.size()-1){
			return TYPE_FOOTER;
		}else{
			return TYPE_NORMAL;
		}
	}

	/**
	 * 设置Item点击监听
	 * 
	 * @param listener
	 */
	public void setOnItemClickListener(MedialistAdapter.MyItemClickListener listener) {
		this.mItemClickListener = listener;
	}

	/**
	 * 设置删除按钮点击监听
	 * 
	 * @param listener
	 */
	public void setOnDeleteListener(OnDeleteListener listener) {
		this.mOnDeleteListener = listener;
	}

	class MyHodler extends ViewHolder implements OnClickListener {
		private View mViewMian;// 整个item的view
		private TextView mTxtTime, mTxtSize, mTxtDelete;// 时间、大小、删除按钮
		private ImageView mImgThumb, mImgType;// 缩略图、类别icon
		private CheckBox mCheckbox;// 选择框

		private PieDownloadInfo info;
		private int position;

		public MyHodler(View itemView) {
			super(itemView);
			mViewMian = itemView.findViewById(R.id.hasdown_lay_main);
			mTxtTime = (TextView) itemView.findViewById(R.id.hasdown_txt_time);
			mTxtSize = (TextView) itemView.findViewById(R.id.hasdown_txt_size);
			mImgThumb = (ImageView) itemView
					.findViewById(R.id.hasdown_img_thumbnail);
			mImgType = (ImageView) itemView.findViewById(R.id.hasdown_img_type);
			mCheckbox = (CheckBox) itemView
					.findViewById(R.id.hasdown_checkbox_select);
			mTxtDelete = (TextView) itemView
					.findViewById(R.id.hasdown_txt_delete);

			itemView.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					if (isItemDeleteShow(position)) {
						mTxtDelete.setVisibility(View.GONE);
						setItemDeleteShow(position, false);
					} else {
						mTxtDelete.setVisibility(View.VISIBLE);
						setItemDeleteShow(position, true);
					}
					return true;
				}
			});
		}

		public void bindData(final PieDownloadInfo info, final int position) {
			this.info = info;
			this.position = position;
			String s = info.getFileName();
			if (!TextUtils.isEmpty(s)) {
				mTxtTime.setText(s);
			}
			s = FileUtil.getFileShowSize(info.getTotalLen(), new DecimalFormat(
					"0.00"));
			if (!TextUtils.isEmpty(s)) {
				mTxtSize.setText(s);
			}
			if (info.getType() == PieDownloadInfo.TYPE_H264) {
				mImgType.setImageResource(R.drawable.mediatype_video);
			} else if (info.getType() == PieDownloadInfo.TYPE_JPG) {
				mImgType.setImageResource(R.drawable.mediatype_pic);
			}
			if (isEditable) {
				mCheckbox.setVisibility(View.VISIBLE);
			} else {
				mCheckbox.setVisibility(View.GONE);
				mCheckbox.setChecked(false);
			}
			mCheckbox.setOnClickListener(this);

			if (isItemChecked(position)) {
				mCheckbox.setChecked(true);
			} else {
				mCheckbox.setChecked(false);
			}
			if (isItemDeleteShow(position)) {
				mTxtDelete.setVisibility(View.VISIBLE);
			} else {
				mTxtDelete.setVisibility(View.GONE);
			}

			mViewMian.setOnClickListener(this);
			mTxtDelete.setOnClickListener(this);

			String filePathLocal = info.getLocalPath();
			String thumbnailPath = info.getThumbnailPath();

			if (!TextUtils.isEmpty(thumbnailPath)) {
				Bitmap localThumbnail = mAsyncImageLoader
						.getBitmapByUrlLocalThumbnail(thumbnailPath);
				if (null != localThumbnail) {
					mImgThumb.setImageBitmap(localThumbnail);
				} else {
					mImgThumb.setImageResource(R.drawable.def_backgroud_pic);
				}
			} else {
				if (!TextUtils.isEmpty(filePathLocal)) {
					Bitmap mBitmap = null;
					if (info.getType() == PieDownloadInfo.TYPE_H264) {
						mBitmap = mAsyncImageLoader
								.getBitmapByUrlLocalThumbnailVideo(filePathLocal);
					} else if (info.getType() == PieDownloadInfo.TYPE_JPG) {
						mBitmap = mAsyncImageLoader
								.getBitmapByUrlLocalThumbnailPic(filePathLocal,
										width, height);
					}
					if (mBitmap != null) {
						mImgThumb.setImageBitmap(mBitmap);
					} else {
						mImgThumb
								.setImageResource(R.drawable.def_backgroud_pic);
					}
				} else {
					mImgThumb.setImageResource(R.drawable.def_backgroud_pic);
				}
			}
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.hasdown_lay_main:
				// 整个item
				mItemClickListener.onItemClick(itemView, position);
				break;
			case R.id.hasdown_txt_delete:
				// 删除按钮
				mOnDeleteListener.onDelete(info);
				break;

			case R.id.hasdown_checkbox_select:
				// 选择框
				if (isItemChecked(position)) {
					setItemChecked(position, false);
				} else {
					setItemChecked(position, true);
				}
				break;
			}

		}
	}

	class MyHodlerFooter extends ViewHolder {

		private View mViewFooterMian;

		public MyHodlerFooter(View itemView) {
			super(itemView);
			mViewFooterMian = itemView
					.findViewById(R.id.hasdown_footer_lay_main);
		}

	}

	public interface OnDeleteListener {
		void onDelete(PieDownloadInfo pieDownloadInfo);
	}
}
