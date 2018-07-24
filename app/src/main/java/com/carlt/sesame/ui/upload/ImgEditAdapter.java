package com.carlt.sesame.ui.upload;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.carlt.sesame.R;
import com.carlt.sesame.data.UploadImgInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ImgEditAdapter extends BaseAdapter {

	private Context context;

	private Util util;

	private int index = -1;

	private ArrayList<UploadImgInfo> images;
	private HashMap<String, Bitmap> mBitList;

	private List<View> holderlist;

	private final static int IMG1 = R.drawable.declaration_upload;

	private final static int IMG2 = R.drawable.declaration_takephoto;

	public ImgEditAdapter(Context context, ArrayList<UploadImgInfo> images) {
		this.images = images;
		this.context = context;
		mBitList = new HashMap<String, Bitmap>();
		util = new Util(context);
		holderlist = new ArrayList<View>();
	}

	// public void addData(ArrayList<String> newData) {
	// data.addAll(newData);
	// notifyDataSetChanged();
	// }

	@Override
	public int getCount() {
		return images.size() + 2;
	}

	@Override
	public Object getItem(int arg0) {
		return images.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public View getView(int postion, View contentView, ViewGroup arg2) {
		Holder holder;
		if (postion != index && postion > index) {
			index = postion;
			contentView = LayoutInflater.from(context).inflate(
					R.layout.edit_img_item, null);
			holder = new Holder();
			holder.imageView = (ImageView) contentView
					.findViewById(R.id.edit_img_item_imageView1);
			contentView.setTag(holder);
			holderlist.add(contentView);
		} else {
			holder = (Holder) holderlist.get(postion).getTag();
			contentView = holderlist.get(postion);
		}

		if (postion == (getCount() - 2)) {
			holder.imageView.setImageResource(IMG1);
		} else if (postion == (getCount() - 1)) {
			holder.imageView.setImageResource(IMG2);
		} else {
			String url = images.get(postion).getLocalfilePath();
			if (mBitList.get(url) == null) {
				util.imgExcute(holder.imageView, new ImgClallBackLisner(url),
						url);
			} else {
				holder.imageView.setImageBitmap(mBitList.get(url));
			}
		}

		return contentView;
	}

	class Holder {
		ImageView imageView;
	}

	public class ImgClallBackLisner implements ImgCallBack {
		String num;

		public ImgClallBackLisner(String num) {
			this.num = num;
		}

		@Override
		public void resultImgCall(ImageView imageView, Bitmap bitmap) {
			mBitList.put(num, bitmap);
			imageView.setImageBitmap(bitmap);
		}
	}

	// public interface OnItemClickClass {
	// public void OnItemClick(View v, int Position, CheckBox checkBox);
	// }

	// class OnPhotoClick implements OnClickListener {
	// int position;
	// CheckBox checkBox;
	//
	// public OnPhotoClick(int position, CheckBox checkBox) {
	// this.position = position;
	// this.checkBox = checkBox;
	// }
	//
	// @Override
	// public void onClick(View v) {
	// if (data != null && onItemClickClass != null) {
	// onItemClickClass.OnItemClick(v, position, checkBox);
	// }
	// }
	// }

}
