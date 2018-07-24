package com.carlt.sesame.ui.activity.friends;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.sesame.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.data.LoginInfo;
import com.carlt.sesame.data.UploadImgInfo;
import com.carlt.sesame.data.community.SubmitSOSInfo;
import com.carlt.sesame.ui.activity.base.BaseActivity;
import com.carlt.sesame.ui.view.PopBoxCreat;
import com.carlt.sesame.utility.UUToast;

import java.util.ArrayList;

//import com.baidu.location.BDLocation;
//import com.baidu.location.BDLocationListener;
//import com.baidu.location.LocationClient;
//import com.baidu.location.LocationClientOption;

public class DeclarationActivity extends BaseActivity /*implements
		BDLocationListener*/ {
	private ImageView back;// 头部返回键

	private TextView title;// 标题文字

	private TextView txtRight;// 头部右侧文字

	private ImageView mImageViewSecretary;// 车秘书头像

	private TextView mTextViewSecretary;// 提醒消息

	private TextView mTxtPic;// 已上传故障照片

	private TextView mTxtPos;// 车辆位置

	private TextView mBtn;// 确认申报按钮

	private CheckBox mCheckBox;// 救援服务开关

	private View mViewPic;// 故障照片

	private View mViewPos;// 故障位置

	private EditText mEditText;// 故障描述

	private Dialog mDialog;

	// private String addr_detail;
	//
	private String addr_point;
	private CheckBox mCheckBox_addr;// 自己填写地址开关

	private EditText mEditText_addr;// 地址Edit
	private SubmitSOSInfo mSubmitSOSInfo = new SubmitSOSInfo();

	public final static String MSG_INFO = "msg_info";

	public final static String POS_INFO = "pos_info";

	private String msgInfo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_declaration);
		try {
			msgInfo = getIntent().getStringExtra(MSG_INFO);
		} catch (Exception e) {
			// TODO: handle exception
		}
//		mLocClient = new LocationClient(this);
//		mLocClient.registerLocationListener(this);
//		LocationClientOption option = new LocationClientOption();
//		option.setOpenGps(true);// 打开gps
//		option.setCoorType("bd09ll"); // 设置坐标类型
//		option.setScanSpan(1000 * 30);
//		option.setIsNeedAddress(true);
//		mLocClient.setLocOption(option);
//		mLocClient.start();
		initTitle();
		initSubTitle();
		init();
	}

	private void initTitle() {
		back = (ImageView) findViewById(R.id.head_back_img1);
		title = (TextView) findViewById(R.id.head_back_txt1);
		txtRight = (TextView) findViewById(R.id.head_back_txt2);

		back.setImageResource(R.drawable.arrow_back);
		title.setText("故障申报");
		txtRight.setText("申报纪录");

		txtRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 跳转至申报纪录页面
				Intent mIntent = new Intent(DeclarationActivity.this,
						DeclarationListActivity.class);
				startActivity(mIntent);
			}
		});

		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void initSubTitle() {
		mImageViewSecretary = (ImageView) findViewById(R.id.layout_sub_head_img);
		mTextViewSecretary = (TextView) findViewById(R.id.layout_sub_head_txt);

		mImageViewSecretary.setImageResource(LoginInfo.getSecretaryImg());
		mTextViewSecretary.setText("申报车辆故障,可第一时间获得专家的维修建议以及大致的费用！");
	}

	private void init() {
		mTxtPic = (TextView) findViewById(R.id.declaration_txt_pic);
		mTxtPos = (TextView) findViewById(R.id.declaration_position_txt_pos);
		mBtn = (TextView) findViewById(R.id.declaration_btn);

		mCheckBox = (CheckBox) findViewById(R.id.declaration_checkbox);

		mViewPic = findViewById(R.id.declaration_lay1);
		mViewPos = findViewById(R.id.declaration_lay3);

		mEditText = (EditText) findViewById(R.id.declaration_edit);

		mCheckBox.setOnCheckedChangeListener(mChangeListener);

		mCheckBox.setChecked(false);
		mViewPic.setOnClickListener(mClickListener);
		mBtn.setOnClickListener(mClickListener);

		mTxtPic.setText("已上传0张");

		if (msgInfo != null && msgInfo.length() > 0) {
			mEditText.setText(msgInfo);
		}

		mCheckBox_addr = (CheckBox) findViewById(R.id.declaration_position_checkbox);
		mEditText_addr = (EditText) findViewById(R.id.declaration_position_edit);
		OnCheckedChangeListener mChangeListener = new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton view, boolean isCkeck) {
				if (isCkeck) {
					mEditText_addr.setVisibility(View.VISIBLE);
				} else {
					mEditText_addr.setVisibility(View.GONE);
				}

			}
		};
		mCheckBox_addr.setOnCheckedChangeListener(mChangeListener);
	}

	private OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.declaration_lay1:
				// 跳转至故障照片页面
				Intent mIntent1 = new Intent(DeclarationActivity.this,
						DeclarationPhotoActivity.class);
				ArrayList<UploadImgInfo> images = mSubmitSOSInfo.getImages();
				mIntent1.putParcelableArrayListExtra("images", images);
				startActivityForResult(mIntent1, REQ_UPLOADIMG);
				break;

			case R.id.declaration_btn:
				// 提交
				boolean b1 = false;
				boolean b2 = false;
				boolean b3 = false;

				if (mSubmitSOSInfo.getImages().size() > 0) {
					b1 = true;
				}
				if (mCheckBox.isChecked()) {
					mSubmitSOSInfo.setNeed_sos(true);

					String addr = "";
					if (mCheckBox_addr.isChecked()) {
						addr = mEditText_addr.getText().toString();
					} else {
						addr = mTxtPos.getText().toString();
					}
					if (!addr.equals("")) {
						b2 = true;
					}
					mSubmitSOSInfo.setAddr_detail(addr);
					mSubmitSOSInfo.setAddr_point(addr_point);
				} else {
					mSubmitSOSInfo.setNeed_sos(false);
				}

				if (mEditText.getText() != null) {
					if (mEditText.getText().toString().length() > 0) {
						b3 = true;
					}

					mSubmitSOSInfo.setInfo(mEditText.getText().toString());
				}

				if (b1 || b2 || b3) {
					if (mDialog == null) {
						mDialog = PopBoxCreat.createDialogWithProgress(
								DeclarationActivity.this, "数据提交中...");
					}
					mDialog.show();
					CPControl.GeSubmitSOSResult(mSubmitSOSInfo, listener);
				} else {
					UUToast.showUUToast(DeclarationActivity.this,
							"请至少填写一项数据再提交");
				}

				break;
			}

		}
	};

	private GetResultListCallback listener = new GetResultListCallback() {

		@Override
		public void onFinished(Object o) {
			mHandler.sendEmptyMessage(0);
		}

		@Override
		public void onErro(Object o) {
			Message msg = new Message();
			msg.what = 1;
			msg.obj = o;
			mHandler.sendMessage(msg);

		}
	};

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				if (mDialog != null && mDialog.isShowing()) {
					mDialog.dismiss();
				}
				UUToast.showUUToast(DeclarationActivity.this, "故障申报成功！");
				Intent mIntent = new Intent(DeclarationActivity.this,
						DeclarationListActivity.class);
				startActivity(mIntent);
				finish();
				break;

			case 1:
				if (mDialog != null && mDialog.isShowing()) {
					mDialog.dismiss();
				}
				BaseResponseInfo mInfo = (BaseResponseInfo) msg.obj;
				if (mInfo != null && mInfo.getInfo() != null
						&& mInfo.getInfo().length() > 0) {
					UUToast.showUUToast(DeclarationActivity.this,
							mInfo.getInfo());
				} else {
					UUToast.showUUToast(DeclarationActivity.this,
							"故障申报失败，请稍后再试...");
				}
				break;
			}
		}

	};

	private OnCheckedChangeListener mChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton view, boolean isCkeck) {
			if (isCkeck) {
				mViewPos.setVisibility(View.VISIBLE);
			} else {
				mViewPos.setVisibility(View.GONE);
			}
		}
	};

	public static final int REQ_UPLOADIMG = 1;// 新增上传图片

	public static final int REQ_POS = 2;// 手动更改位置

	private final static int RESULT_CODE = 200;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_CODE) {
			if (requestCode == REQ_UPLOADIMG) {
				Bundle bundle = data.getExtras();
				if (bundle != null) {

					ArrayList<UploadImgInfo> images = bundle
							.getParcelableArrayList("images");
					if (images != null) {
						mSubmitSOSInfo.setImages(images);
						mTxtPic.setText("已上传" + images.size() + "张");
					}
				}
			}

			// else if (requestCode == REQ_POS) {
			// // 地址回调
			// if (data != null) {
			// mLocClient.stop();
			// addr_detail = data.getStringExtra(POS_INFO);
			// mTxtPos.setText(addr_detail);
			// }
			// }

			super.onActivityResult(requestCode, resultCode, data);
		}

	}

	// 定位相关
//	private LocationClient mLocClient;

//	@Override
//	public void onReceiveLocation(BDLocation arg0) {
//		String s = arg0.getAddrStr();
//		if (s != null && s.length() > 0) {
//			addr_point = arg0.getLatitude() + "," + arg0.getLongitude();
//			mTxtPos.setText(arg0.getAddrStr());
//		}
//	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 退出时销毁定位
//		mLocClient.stop();
	}
}
