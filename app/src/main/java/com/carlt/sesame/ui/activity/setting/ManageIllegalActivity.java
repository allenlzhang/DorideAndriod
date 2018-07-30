package com.carlt.sesame.ui.activity.setting;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultList2Callback;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.data.SesameLoginInfo;
import com.carlt.sesame.data.car.CityInfo;
import com.carlt.sesame.data.car.PostViolationInfo;
import com.carlt.sesame.data.car.ProvinceInfo;
import com.carlt.sesame.ui.activity.base.LoadingActivityWithTitle;
import com.carlt.sesame.ui.activity.car.SelectPlaceView;
import com.carlt.sesame.ui.activity.car.SelectPlaceView.OnPlaceNameClick;
import com.carlt.sesame.ui.view.AllCapTransformationMethod;
import com.carlt.sesame.ui.view.MenuImageShow;
import com.carlt.sesame.ui.view.PopBoxCreat;
import com.carlt.sesame.utility.UUToast;

import java.util.HashMap;

/**
 * 违章管理页面
 * 
 * @author daisy
 */
public class ManageIllegalActivity extends LoadingActivityWithTitle implements OnClickListener {

	private ImageView back;// 头部返回键

	private TextView title;// 标题文字

	private TextView txtRight;// 头部右侧文字

	private ImageView mImageViewSecretary;// 车秘书头像

	private ImageView mImageView1;// 发动机号填写提示

	private ImageView mImageView2;// 车架号填写提示

	private ImageView mImageView3;// 登记证书

	private TextView mTextViewSecretary;// 车秘书提示文字

	private TextView mTextView1;// 选择城市

	private TextView mTextView2;// 选择城市后出现的车牌号缩略文字（如：陕A）

	private TextView mTextView3;// 立即查询

	private EditText mEditText1;// 车牌号

	private EditText mEditText2;// 发动机号

	private EditText mEditText3;// 车架号

	private EditText mEditText4;// 登记证书号

	private CheckBox mCheckBox;// 推送消息勾选框

	private View mView2;// 选择上牌城市

	private View mView3;// 车牌号

	private View mView4;// 发动机号

	private View mView5;// 车架号

	private View mView6;// 登记证书

	private String code;// 车牌城市id

	private SelectPlaceView mPlaceView;

	private Dialog mDialog;

	PostViolationInfo mInfo;
	HashMap<String, CityInfo> cityInfos = null;
	CityInfo tempCity = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_illegal);
		setTitleView(R.layout.head_back);

		initTitle();
		initSubTitle();
		init();
		LoadData();

	}

	private void initTitle() {
		back = (ImageView) findViewById(R.id.head_back_img1);
		title = (TextView) findViewById(R.id.head_back_txt1);
		txtRight = (TextView) findViewById(R.id.head_back_txt2);

		back.setImageResource(R.drawable.arrow_back);
		title.setText("违章管理");
		txtRight.setVisibility(View.GONE);

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

		//mImageViewSecretary.setImageResource(LoginInfo.getSecretaryImg());
		if (!SesameLoginInfo.getCanQueryVio().equals("1")) {
			mTextViewSecretary.setText("第一次查询违章需要补充以下信息");
		}else{
			mTextViewSecretary.setText("请填写违章查询车辆的信息");
		}
		
	}

	private void init() {

		mImageView1 = (ImageView) findViewById(R.id.activity_manage_illegal_img1);
		mImageView2 = (ImageView) findViewById(R.id.activity_manage_illegal_img2);
		mImageView3 = (ImageView) findViewById(R.id.activity_manage_illegal_img3);

		mTextView1 = (TextView) findViewById(R.id.activity_manage_illegal_select);
		mTextView2 = (TextView) findViewById(R.id.activity_manage_illegal_abbr);
		mTextView3 = (TextView) findViewById(R.id.activity_manage_illegal_query);

		mEditText1 = (EditText) findViewById(R.id.activity_manage_illegal_edit1);// 车牌号
		mEditText2 = (EditText) findViewById(R.id.activity_manage_illegal_edit2);// 发动机号
		mEditText3 = (EditText) findViewById(R.id.activity_manage_illegal_edit3);// 车架号
		mEditText4 = (EditText) findViewById(R.id.activity_manage_illegal_edit4);// 登记证书号

		mView2 = findViewById(R.id.activity_manage_illegal_layout2);
		mView3 = findViewById(R.id.activity_manage_illegal_layout3);
		mView4 = findViewById(R.id.activity_manage_illegal_layout4);
		mView5 = findViewById(R.id.activity_manage_illegal_layout5);
		mView6 = findViewById(R.id.activity_manage_illegal_layout6);

		mImageView1.setOnClickListener(this);
		mImageView2.setOnClickListener(this);
		mImageView3.setOnClickListener(this);

		mTextView3.setOnClickListener(this);

		mView2.setOnClickListener(this);

		mMenuImageShow = new MenuImageShow(ManageIllegalActivity.this);
		mEditText1.setTransformationMethod(new AllCapTransformationMethod());
		mEditText1.setOnFocusChangeListener(mChangeListener);
		mEditText2.setOnFocusChangeListener(mChangeListener);
		mEditText2.setTransformationMethod(new AllCapTransformationMethod());
		mEditText3.setOnFocusChangeListener(mChangeListener);
		mEditText3.setTransformationMethod(new AllCapTransformationMethod());
		mEditText4.setOnFocusChangeListener(mChangeListener);
	}

	private MenuImageShow mMenuImageShow;

	@Override
	protected void LoadSuccess(Object data) {
		super.LoadSuccess(data);
		// 初始化数据
		CityInfo mInfo = cityInfos.get(SesameLoginInfo.getCity_code());
		if (null == mInfo) {
			return;
		}

		code = mInfo.getCode();

		mTextView1.setText("");
		mEditText1.setText("");
		mEditText2.setText("");
		mEditText3.setText("");
		mEditText4.setText("");

		mTextView1.setText(mInfo.getName());
		mTextView2.setVisibility(View.VISIBLE);
		mTextView2.setText(mInfo.getAbbr());

		if (SesameLoginInfo.getCarno().length() > 0) {
			mEditText1.setText(SesameLoginInfo.getCarno().substring(1));
		}

		String string;
		// 是否需要发动机号
		string = mInfo.getEngine();
		if (string.equals("0")) {
			mView4.setVisibility(View.GONE);
		} else {
			mView4.setVisibility(View.VISIBLE);
			String num = mInfo.getEngineno();
			if (num.equals("0")) {
				mEditText2.setHint("请输入全部发动机号");
			} else {
				mEditText2.setHint(String.format("请输入后%s位发动机号", num));
			}
			if (SesameLoginInfo.getEngineno().length() > 0) {
				mEditText2.setText(SesameLoginInfo.getEngineno());
			}
		}
		// 是否需要车架号
		string = mInfo.getClassa();
		if (string.equals("0")) {
			mView5.setVisibility(View.GONE);
		} else {
			mView5.setVisibility(View.VISIBLE);
			String num = mInfo.getClassno();
			if (num.equals("0")) {
				mEditText3.setHint("请输入全部车架号");
			} else {
				mEditText3.setHint(String.format("请输入后%s位车架号", num));
			}
			if (SesameLoginInfo.getShortstandcarno().length() > 0) {
				mEditText3.setText(SesameLoginInfo.getShortstandcarno());
			}
		}
		// 是否需要登记证书号
		string = mInfo.getRegist();
		if (string.equals("0")) {
			mView6.setVisibility(View.GONE);
		} else {
			mView6.setVisibility(View.VISIBLE);
			String num = mInfo.getRegistno();
			if (num.equals("0")) {
				mEditText2.setHint("请输入全部证书号");
			} else {
				mEditText2.setHint(String.format("请输入后%s位证书号", num));
			}
			if (SesameLoginInfo.getRegistno().length() > 0) {
				mEditText3.setText(SesameLoginInfo.getRegistno());
			}
		}
	}

	@Override
	protected void LoadErro(Object erro) {

		super.LoadErro(erro);
	}

	@Override
	protected void LoadData() {
		super.LoadData();
		CPControl.GetAllCityInfoResult(cityListener);
	}

	private GetResultList2Callback cityListener = new GetResultList2Callback() {

		@Override
		public void onFinished(Object o1, Object o2, Object o3) {
			Message msg = new Message();
			msg.what = 1002;
			msg.obj = o2;
			cityInfos = (HashMap<String, CityInfo>) o2;
			mHandler.sendMessage(msg);
		}

		@Override
		public void onErro(Object o) {
			Message msg = new Message();
			msg.what = 2002;
			msg.obj = o;
			mHandler.sendMessage(msg);
		}
	};

	private GetResultListCallback myListener = new GetResultListCallback() {

		@Override
		public void onFinished(Object o) {
			Message msg = new Message();
			msg.what = 0;
			msg.obj = o;
			mHandler.sendMessage(msg);
		}

		@Override
		public void onErro(Object o) {
			Message msg = new Message();
			msg.what = 1;
			msg.obj = o;
			mHandler.sendMessage(msg);
		}
	};

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				// 信息提交成功
				if (mDialog != null && mDialog.isShowing()) {
					mDialog.dismiss();
				}
				
				UUToast.showUUToast(ManageIllegalActivity.this, "信息提交成功！");
				finish();
				break;
			case 1:
				// 信息提交失败
				if (mDialog != null && mDialog.isShowing()) {
					mDialog.dismiss();
				}

				BaseResponseInfo mBaseResponseInfo = (BaseResponseInfo) msg.obj;
				if (mBaseResponseInfo != null && mBaseResponseInfo.getInfo() != null) {
					UUToast.showUUToast(ManageIllegalActivity.this, "信息提交失败：" + mBaseResponseInfo.getInfo());
				} else {
					UUToast.showUUToast(ManageIllegalActivity.this, "信息提交失败,请稍候再试!");
				}

				break;
			case 1002:
				LoadSuccess(null);
				break;
			case 2002:
				LoadErro(null);
				break;
			}
		};
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.activity_manage_illegal_img1:
			// 弹出发动机号输入提醒
			mMenuImageShow.showMenu(R.drawable.pop_pic_fadongji, mView4, "");
			break;
		case R.id.activity_manage_illegal_img2:
			// 弹出车架号输入提醒
			mMenuImageShow.showMenu(R.drawable.pop_pic_chejia, mView5, "");
			break;
		case R.id.activity_manage_illegal_img3:
			// 弹出登记证书号输入提醒
			mMenuImageShow.showMenu(R.drawable.pop_pic_dengjizhengshu, mView6, "");
			break;
		case R.id.activity_manage_illegal_query:
			// 确定（提交填写信息）
			mInfo = new PostViolationInfo();
			if (code != null && !code.equals("")) {
				mInfo.setCityCodeId(code);
			} else {
				UUToast.showUUToast(ManageIllegalActivity.this, "请选择您的上牌城市");
				return;
			}

			String string;
			// 车牌号
			string = mEditText1.getText().toString().toUpperCase();
			if (string != null && (string.length() + 1) == PostViolationInfo.CARNOSIZE) {
				mInfo.setCarno(mTextView2.getText().toString() + string);
			} else {
				UUToast.showUUToast(ManageIllegalActivity.this, "您的车牌号填写有误");
				return;
			}

			// 发动机号
			if (mView4.getVisibility() == View.VISIBLE) {
				string = mEditText2.getText().toString();
				if (string != null && string.length() > 0) {
					mInfo.setEngineno(string);
				} else {
					UUToast.showUUToast(ManageIllegalActivity.this, "您的发动机号填写有误");
					return;
				}
			}

			// 车架号
			if (mView5.getVisibility() == View.VISIBLE) {

				string = mEditText3.getText().toString();
				if (string != null && string.length() > 0) {
					mInfo.setStandcarno(string);
				} else {
					UUToast.showUUToast(ManageIllegalActivity.this, "您的车架号填写有误");
					return;
				}
			}

			// 登记证号
			if (mView6.getVisibility() == View.VISIBLE) {
				string = mEditText4.getText().toString();
				if (string != null && string.length() > 0) {
					mInfo.setRegistno(string);
				} else {
					UUToast.showUUToast(ManageIllegalActivity.this, "您的登记证号填写有误");
					return;
				}
			}
			if (mDialog == null) {
				mDialog = PopBoxCreat.createDialogWithProgress(ManageIllegalActivity.this, "信息提交中...");
			}
			mDialog.show();
			// CPControl.GetViolationListResult(mInfo, myListener);
			// CPControl.GetViolationListNewResult(mInfo,
			// myListener);//新版，直接调用聚合接口
			CPControl.InitViolation(mInfo, myListener);// 初始化查询信息
			break;
		case R.id.activity_manage_illegal_layout2:
			// 弹出选择城市view
			if (mPlaceView == null) {
				mPlaceView = new SelectPlaceView(ManageIllegalActivity.this, mNameClick);
			}
			mPlaceView.pullDataFirst(SelectPlaceView.TYPE_PROVINCE);
			mPlaceView.showMenu();
			break;
		}
	}

	private OnPlaceNameClick mNameClick = new OnPlaceNameClick() {

		@Override
		public void onClick(Object info, int type) {

			if (type == SelectPlaceView.TYPE_PROVINCE) {
				// 省份
				ProvinceInfo mProvinceInfo = (ProvinceInfo) info;
				mPlaceView.pullDataSecond(mProvinceInfo, SelectPlaceView.TYPE_CITY);

			} else if (type == SelectPlaceView.TYPE_CITY) {
				// 城市
				if (mPlaceView != null) {
					mPlaceView.dissmiss();
				}

				CityInfo mCityInfo = (CityInfo) info;
				code = mCityInfo.getCode();
				tempCity = mCityInfo;

				mTextView1.setText("");
				mEditText1.setText("");
				mEditText2.setText("");
				mEditText3.setText("");
				mEditText4.setText("");

				mTextView1.setText(mCityInfo.getName());
				mTextView2.setVisibility(View.VISIBLE);
				mTextView2.setText(mCityInfo.getAbbr());

//				if (LoginInfo.carno.length() > 0) {
//					mEditText1.setText(LoginInfo.carno.substring(1));
//				}
				String string;
				// 是否需要发动机号
				string = mCityInfo.getEngine();
				if (string.equals("0")) {
					mView4.setVisibility(View.GONE);
				} else {
					mView4.setVisibility(View.VISIBLE);
					String num = mCityInfo.getEngineno();
					if (num.equals("0")) {
						mEditText2.setHint("请输入全部发动机号");
					} else {
						mEditText2.setHint(String.format("请输入后%s位发动机号", num));
					}

//					if (!TextUtils.isEmpty(LoginInfo.engineno)) {
//						int len = Integer.parseInt(num);
//						String res = "";
//						if (LoginInfo.engineno.length() > len && len > 0) {
//							res = LoginInfo.engineno.substring(LoginInfo.engineno.length() - len);
//						} else if (LoginInfo.engineno.length() == len) {
//							res = LoginInfo.engineno;
//						}
//						mEditText2.setText(res);
//					}
				}
				// 是否需要车架号
				string = mCityInfo.getClassa();
				if (string.equals("0")) {
					mView5.setVisibility(View.GONE);
				} else {
					mView5.setVisibility(View.VISIBLE);
					String num = mCityInfo.getClassno();
					if (num.equals("0")) {
						mEditText3.setHint("请输入全部车架号");
					} else {
						mEditText3.setHint(String.format("请输入后%s位车架号", num));
					}

					if (!TextUtils.isEmpty(SesameLoginInfo.getStandcarno())) {
						if (!SesameLoginInfo.getCanQueryVio().equals("1")) {
							int len = Integer.parseInt(num);
							String res = "";
							if (SesameLoginInfo.getStandcarno().length() > len && len > 0) {
								res = SesameLoginInfo.getStandcarno().substring(SesameLoginInfo.getStandcarno().length() - len);
							} else{
								res = SesameLoginInfo.getStandcarno();
							}
							mEditText3.setText(res);
						}
					}
				}
				// 是否需要登记证书号
				string = mCityInfo.getRegist();
				if (string.equals("0")) {
					mView6.setVisibility(View.GONE);
				} else {
					mView6.setVisibility(View.VISIBLE);
					String num = mCityInfo.getRegistno();
					if (num.equals("0")) {
						mEditText4.setHint("请输入全部证书号");
					} else {
						mEditText4.setHint(String.format("请输入后%s位证书号", num));
					}

//					if (!TextUtils.isEmpty(LoginInfo.registno)) {
//						int len = Integer.parseInt(num);
//						String res = "";
//						if (LoginInfo.registno.length() > len && len > 0) {
//							res = LoginInfo.registno.substring(LoginInfo.registno.length() - len);
//						} else if (LoginInfo.registno.length() == len) {
//							res = LoginInfo.registno;
//						}
//						mEditText4.setText(res);
//					}
				}

			}

		}
	};

	private OnFocusChangeListener mChangeListener = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			switch (v.getId()) {
			case R.id.activity_manage_illegal_edit1:
				if (hasFocus) {
					mView3.setBackgroundResource(R.drawable.edittext_bg_focused);
				} else {
					mView3.setBackgroundResource(R.drawable.edittext_bg);
				}
				break;

			case R.id.activity_manage_illegal_edit2:

				if (hasFocus) {
					mView4.setBackgroundResource(R.drawable.edittext_bg_focused);
				} else {
					mView4.setBackgroundResource(R.drawable.edittext_bg);
				}
				break;
			case R.id.activity_manage_illegal_edit3:
				if (hasFocus) {
					mView5.setBackgroundResource(R.drawable.edittext_bg_focused);
				} else {
					mView5.setBackgroundResource(R.drawable.edittext_bg);
				}
				break;
			case R.id.activity_manage_illegal_edit4:
				if (hasFocus) {
					mView6.setBackgroundResource(R.drawable.edittext_bg_focused);
				} else {
					mView6.setBackgroundResource(R.drawable.edittext_bg);
				}
				break;

			}

		}
	};

}
