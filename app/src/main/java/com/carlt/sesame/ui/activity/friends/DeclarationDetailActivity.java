package com.carlt.sesame.ui.activity.friends;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.sesame.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.data.community.SOSDetialInfo;
import com.carlt.sesame.data.community.SOSInfo;
import com.carlt.sesame.ui.activity.base.LoadingActivityWithTitle;
import com.carlt.sesame.ui.activity.career.order.RiLiActivity;
import com.carlt.sesame.ui.adapter.DeclarationImgAdapter;
import com.carlt.sesame.ui.upload.ImgDetialActivity;
import com.carlt.sesame.ui.view.HorizontalListView;

import java.util.ArrayList;

public class DeclarationDetailActivity extends LoadingActivityWithTitle {
	private ImageView back;// 头部返回键

	private TextView title;// 标题文字

	private TextView txtRight;// 头部右侧文字

	private TextView mTxtPos;// 故障地点

	private TextView mTxtStatus;// 故障状态

	private TextView mTxtContent;// 故障描述

	private TextView mTxtReply;// 技师回复

	private HorizontalListView mList;// 故障图片gallery

	private DeclarationImgAdapter mAdapter;

	private ArrayList<String> mImgList;

	private String id;

	private int length;

	public final static String MSG_ID = "msg_id";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_declaration_detail);
		setTitleView(R.layout.head_back);

		try {
			id = getIntent().getStringExtra(MSG_ID);
		} catch (Exception e) {
			// TODO: handle exception
		}

		initTitle();
		init();
		LoadData();
	}

	private void initTitle() {
		back = (ImageView) findViewById(R.id.head_back_img1);
		title = (TextView) findViewById(R.id.head_back_txt1);
		txtRight = (TextView) findViewById(R.id.head_back_txt2);

		back.setImageResource(R.drawable.arrow_back);
		title.setText("申报详情");
		txtRight.setText("维修预约");

		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		txtRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 跳转至维修预约选择日历页面
				Intent intent = new Intent(DeclarationDetailActivity.this,
						RiLiActivity.class);
				intent.putExtra("type", RiLiActivity.TYPE_FIX);
				startActivity(intent);
			}
		});
	}

	private void init() {

		mTxtPos = (TextView) findViewById(R.id.declaration_detail_txt_pos);
		mTxtStatus = (TextView) findViewById(R.id.declaration_detail_txt_status);
		mTxtContent = (TextView) findViewById(R.id.declaration_detail_txt_content);
		mTxtReply = (TextView) findViewById(R.id.declaration_detail_txt_reply);

		mList = (HorizontalListView) findViewById(R.id.declaration_detail_list);
	}

	@Override
	protected void LoadSuccess(Object data) {
		SOSDetialInfo mInfo = (SOSDetialInfo) data;
		if (mInfo != null) {
			String s;

			mImgList = mInfo.getmImgList();
			length = mImgList.size();
			if (length > 0) {
				mAdapter = new DeclarationImgAdapter(
						DeclarationDetailActivity.this, mImgList);
				mList.setAdapter(mAdapter);
				mList.setOnItemClickListener(mItemClickListener);
			} else {
			    mList.setVisibility(View.GONE);
			}

			s = mInfo.getAddress();
			if (s != null && s.length() > 0) {
				mTxtPos.setText(s);
			}

			s = mInfo.getInfo();
			if (s != null && s.length() > 0) {
				mTxtContent.setText(s);
			}

			s = mInfo.getStore_reply();
			if (s != null && s.length() > 0) {
				mTxtReply.setText(s);
			}

			int i;
			i = mInfo.getNeed_sos();
			if (i == SOSInfo.SOS_YES) {
				mTxtPos.setVisibility(View.VISIBLE);
				mTxtStatus.setVisibility(View.VISIBLE);
				s = mInfo.getAddress();
				if (s != null && s.length() > 0) {
					mTxtPos.setText(s);
				}
			} else if (i == SOSInfo.SOS_NO) {
				mTxtPos.setVisibility(View.GONE);
				mTxtStatus.setVisibility(View.GONE);
			}

		}
		super.LoadSuccess(data);
	}

	OnItemClickListener mItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			// 跳转至大图页面
			Intent mIntent = new Intent(DeclarationDetailActivity.this,
					ImgDetialActivity.class);
			mIntent.putExtra(ImgDetialActivity.INDEX, position);
			mIntent.putStringArrayListExtra(ImgDetialActivity.IMGLIST, mImgList);
			startActivity(mIntent);
		}
	};

	@Override
	protected void LoadErro(Object erro) {
		// TODO Auto-generated method stub
		super.LoadErro(erro);
	}

	@Override
	protected void LoadData() {
		super.LoadData();
		if (id != null && id.length() > 0) {
			CPControl.GetSOSDetialResult(id, listener);
		}
	}

	@Override
	public void OnImgLoadFinished(String url, Bitmap mBitmap) {
		super.OnImgLoadFinished(url, mBitmap);
		if (mAdapter != null) {
			mAdapter.notifyDataSetChanged();
		}
	}

}
