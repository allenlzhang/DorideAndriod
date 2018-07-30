
package com.carlt.sesame.ui.activity.car;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.data.SesameLoginInfo;
import com.carlt.sesame.data.car.CarInfo;
import com.carlt.sesame.data.car.CityInfo;
import com.carlt.sesame.data.car.PostViolationInfo;
import com.carlt.sesame.data.car.ProvinceInfo;
import com.carlt.sesame.preference.CarInfoLocal;
import com.carlt.sesame.ui.activity.base.LoadingActivityWithTitle;
import com.carlt.sesame.ui.activity.car.SelectPlaceView.OnPlaceNameClick;
import com.carlt.sesame.ui.view.AllCapTransformationMethod;
import com.carlt.sesame.ui.view.MenuImageShow;
import com.carlt.sesame.ui.view.PopBoxCreat;
import com.carlt.sesame.utility.UUToast;

/**
 * 违章查询填写车辆信息页面
 * 
 * @author daisy
 */
public class CarFillIllegalActivity extends LoadingActivityWithTitle implements OnClickListener {

	private ImageView back;// 头部返回键

	private TextView title;// 标题文字

	private TextView txtRight;// 头部右侧文字

	private ImageView imgRight;// 头部右侧按钮

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

	private View mView1;// checkbox所在layout

	private View mView2;// 选择上牌城市

	private View mView3;// 车牌号

	private View mView4;// 发动机

	private View mView5;// 车架号

	private View mView6;// 登记证书

	private String code;// 车牌城市id

	private SelectPlaceView mPlaceView;

	private Dialog mDialog;

	PostViolationInfo mInfo;

	private CarInfo mCarInfo;

	private String from;// 记录从何处跳转过来

	public final static String CAR_INFO = "key_carinfo";

	public final static String CLASS_NAME = "class_name";

	// 座驾主页
	private final static String CLASS_CARMAIN = "com.carlt.sesame.ui.activity.car.CarMainActivity";

	// 违章车辆列表页
	private final static String CLASS_CARLIST = "com.carlt.sesame.ui.activity.car.CarListActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_car_fill_illegal);
		setTitleView(R.layout.head_back);
		mCarInfo = (CarInfo) getIntent().getSerializableExtra(CAR_INFO);
		from = getIntent().getStringExtra(CLASS_NAME);
		initTitle();
		initSubTitle();
		init();
		LoadData();

	}

	private void initTitle() {
		back = (ImageView) findViewById(R.id.head_back_img1);
		title = (TextView) findViewById(R.id.head_back_txt1);
		txtRight = (TextView) findViewById(R.id.head_back_txt2);
		imgRight = (ImageView) findViewById(R.id.head_back_img2);

		back.setImageResource(R.drawable.arrow_back);
		title.setText("违章查询");
		txtRight.setVisibility(View.GONE);
		imgRight.setVisibility(View.VISIBLE);
		imgRight.setImageResource(R.drawable.violation_menu);

		back.setOnClickListener(CarFillIllegalActivity.this);
		imgRight.setOnClickListener(CarFillIllegalActivity.this);
	}

	private void initSubTitle() {
		mImageViewSecretary = (ImageView) findViewById(R.id.layout_sub_head_img);
		mTextViewSecretary = (TextView) findViewById(R.id.layout_sub_head_txt);

		mTextViewSecretary.setText("第一次查询违章需要补充以下信息");
		mTextViewSecretary.setText("请确认查询车辆的基本信息");
	}

	private void init() {

		mImageView1 = (ImageView) findViewById(R.id.car_fill_illegal_img1);
		mImageView2 = (ImageView) findViewById(R.id.car_fill_illegal_img2);
		mImageView3 = (ImageView) findViewById(R.id.car_fill_illegal_img3);

		mTextView1 = (TextView) findViewById(R.id.car_fill_illegal_select);
		mTextView2 = (TextView) findViewById(R.id.car_fill_illegal_abbr);
		mTextView3 = (TextView) findViewById(R.id.car_fill_illegal_query);

		mEditText1 = (EditText) findViewById(R.id.car_fill_illegal_edit1);
		mEditText2 = (EditText) findViewById(R.id.car_fill_illegal_edit2);
		mEditText3 = (EditText) findViewById(R.id.car_fill_illegal_edit3);
		mEditText4 = (EditText) findViewById(R.id.car_fill_illegal_edit4);

		mCheckBox = (CheckBox) findViewById(R.id.car_fill_illegal_checkbox);

		mView1 = findViewById(R.id.car_fill_illegal_layout1);
		mView2 = findViewById(R.id.car_fill_illegal_layout2);
		mView3 = findViewById(R.id.car_fill_illegal_layout3);
		mView4 = findViewById(R.id.car_fill_illegal_layout4);
		mView5 = findViewById(R.id.car_fill_illegal_layout5);
		mView6 = findViewById(R.id.car_fill_illegal_layout6);

		mImageView1.setOnClickListener(this);
		mImageView2.setOnClickListener(this);
		mImageView3.setOnClickListener(this);

		mTextView3.setOnClickListener(this);

		mView1.setOnClickListener(this);
		mView2.setOnClickListener(this);

		mMenuImageShow = new MenuImageShow(CarFillIllegalActivity.this);
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
		String cityName = "";
		String cityCode = "";
		String carno = "";
		String engineno = "";
		String standcarno = "";
		String registno = "";
		mCarInfo = (CarInfo) getIntent().getSerializableExtra("result");
		if (mCarInfo != null) {
			cityName = mCarInfo.getCityName();
			cityCode = mCarInfo.getCityCode();
			carno = mCarInfo.getCarNo();
			engineno = mCarInfo.getEngineNo();
			standcarno = mCarInfo.getStandcarNo();
			registno = mCarInfo.getRegistNo();
			if(!TextUtils.isEmpty(carno) && carno.equals(CarListActivity.CAR_ADD)){
				carno = "";
			}
		} else {
			cityName = SesameLoginInfo.getCarcity();
			cityCode = SesameLoginInfo.getCity_code();
			carno = SesameLoginInfo.getCarno();
			engineno = SesameLoginInfo.getEngineno();
			standcarno = SesameLoginInfo.getShortstandcarno();
			registno = SesameLoginInfo.getRegistno();
			if (carno == null || carno.equals("")) {
				mCarInfo = CarInfoLocal.getCarInfo(SesameLoginInfo.getMobile());
				if (mCarInfo != null) {
					cityName = mCarInfo.getCityName();
					cityCode = mCarInfo.getCityCode();
					carno = mCarInfo.getCarNo();
					engineno = mCarInfo.getEngineNo();
					standcarno = mCarInfo.getStandcarNo();
					registno = mCarInfo.getRegistNo();
				}
			}
		}
		if (cityName != null && !cityName.equals("")) {
			mTextView1.setText(cityName);
		} else {
			mTextView1.setText("");
		}
		if (cityCode != null && !cityCode.equals("")) {
			code = cityCode;
		} else {
			code = "";
		}
		if (carno != null && !carno.equals("")) {
			String province = carno.substring(0, 1);
			String number = carno.substring(1);
			mEditText1.setText(number);
			mTextView2.setVisibility(View.VISIBLE);
			mTextView2.setText(province);
		} else {
//			mTextView2.setVisibility(View.GONE);
			mTextView2.setText("");
			mEditText1.setText("");
		}
		if (engineno != null && !engineno.equals("")) {
			mView4.setVisibility(View.VISIBLE);
			mEditText2.setText(engineno);
		} else {
			mView4.setVisibility(View.GONE);
			mEditText2.setText("");
		}
		if (standcarno != null && !standcarno.equals("")) {
			mView5.setVisibility(View.VISIBLE);
			mEditText3.setText(standcarno);
		} else {
			mView5.setVisibility(View.GONE);
			mEditText3.setText("");
		}
		if (registno != null && !registno.equals("")) {
			mView6.setVisibility(View.VISIBLE);
			mEditText4.setText(registno);
		} else {
			mView6.setVisibility(View.GONE);
			mEditText4.setText("");
		}
		super.LoadSuccess(data);
	}

	@Override
	protected void LoadErro(Object erro) {
		super.LoadErro(erro);
	}

	@Override
	protected void LoadData() {
		super.LoadData();
		LoadSuccess(null);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.head_back_img1:
			// 返回键
			finish();
			break;
		case R.id.head_back_img2:
			// 跳转至违章车辆列表-TODO
			Intent mIntent = new Intent(CarFillIllegalActivity.this, CarListActivity.class);
			startActivityForResult(mIntent, 0);
			break;
		case R.id.car_fill_illegal_img1:
			// 弹出发动机号输入提醒
			mMenuImageShow.showMenu(R.drawable.pop_pic_fadongji, mView4, "");
			break;
		case R.id.car_fill_illegal_img2:
			// 弹出车架号输入提醒
			mMenuImageShow.showMenu(R.drawable.pop_pic_chejia, mView5, "");
			break;
		case R.id.car_fill_illegal_img3:
			// 弹出登记证书号输入提醒
			mMenuImageShow.showMenu(R.drawable.pop_pic_dengjizhengshu, mView6, "");
			break;
		case R.id.car_fill_illegal_query:
			// 立即查询（跳转至查询页面）
			mInfo = new PostViolationInfo();
			mCarInfo = new CarInfo();
			if (code != null && !code.equals("")) {
				mInfo.setCityCodeId(code);
				mCarInfo.setCityCode(code);
				mCarInfo.setCityName(mTextView1.getText().toString());
			} else {
				UUToast.showUUToast(CarFillIllegalActivity.this, "请选择您的上牌城市");
				return;
			}
			if (mEditText1.getText().toString() != null && (mEditText1.getText().toString().length()
					+ mTextView2.getText().toString().length()) == PostViolationInfo.CARNOSIZE) {
				mInfo.setCarno(mTextView2.getText().toString() + mEditText1.getText().toString().toUpperCase());
				mCarInfo.setCarNo(mTextView2.getText().toString() + mEditText1.getText().toString().toUpperCase());
			} else {
				UUToast.showUUToast(CarFillIllegalActivity.this, "您的车牌号填写有误");
				return;
			}
			if (mView4.getVisibility() == View.VISIBLE) {
				if (mEditText2.getText().toString() != null && mEditText2.getText().toString().length() > 0) {
					mInfo.setEngineno(mEditText2.getText().toString());
					mCarInfo.setEngineNo(mEditText2.getText().toString());
				} else {
					UUToast.showUUToast(CarFillIllegalActivity.this, "您的发动机号填写有误");
					return;
				}
			}

			if (mView5.getVisibility() == View.VISIBLE) {
				if (mEditText3.getText().toString() != null && mEditText3.getText().toString().length() > 0) {
					mInfo.setStandcarno(mEditText3.getText().toString());
					mCarInfo.setStandcarNo(mEditText3.getText().toString());
				} else {
					UUToast.showUUToast(CarFillIllegalActivity.this, "您的车架号填写有误");
					return;
				}
			}
			if (mView6.getVisibility() == View.VISIBLE) {
				if (mEditText4.getText().toString() != null && mEditText4.getText().toString().length() > 0) {
					mInfo.setRegistno(mEditText4.getText().toString());
					mCarInfo.setRegistNo(mEditText4.getText().toString());
				} else {
					UUToast.showUUToast(CarFillIllegalActivity.this, "您的登记证号填写有误");
					return;
				}
			}

			if (mDialog == null) {
				mDialog = PopBoxCreat.createDialogWithProgress(CarFillIllegalActivity.this, "提交中...");
			}
			mDialog.show();
			CPControl.GetSaveCarResult(mCarInfo, listener_save);

			break;
		case R.id.car_fill_illegal_layout2:
			// 弹出选择城市view
			if (mPlaceView == null) {
				mPlaceView = new SelectPlaceView(CarFillIllegalActivity.this, mNameClick);
			}
			mPlaceView.pullDataFirst(SelectPlaceView.TYPE_PROVINCE);
			mPlaceView.showMenu();
			break;
		case R.id.car_fill_illegal_layout1:
			mCheckBox.setChecked(!mCheckBox.isChecked());
			break;

		default:
			break;
		}
	}

	private GetResultListCallback myListener = new GetResultListCallback() {

		@Override
		public void onFinished(Object o) {
			Message msg = new Message();
			msg.what = 0;
			msg.obj = o;
			mHandler2.sendMessage(msg);
		}

		@Override
		public void onErro(Object o) {
			Message msg = new Message();
			msg.what = 1;
			msg.obj = o;
			mHandler2.sendMessage(msg);
		}
	};

	private GetResultListCallback listener_save = new GetResultListCallback() {

		@Override
		public void onFinished(Object o) {
			Message msg = new Message();
			msg.what = 0;
			msg.obj = o;
			mHandler2.sendMessage(msg);
		}

		@Override
		public void onErro(Object o) {
			Message msg = new Message();
			msg.what = 1;
			msg.obj = o;
			mHandler2.sendMessage(msg);
		}
	};

	private Handler mHandler2 = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				// 信息提交成功
				if (mDialog != null && mDialog.isShowing()) {
					mDialog.dismiss();
				}

				if (mCarInfo != null && mCarInfo.getCarNo() != null
						&& mCarInfo.getCarNo().equals(SesameLoginInfo.getCarno())) {
					SesameLoginInfo.setCarcity(mCarInfo.getCityName());
					SesameLoginInfo.setCity_code(mCarInfo.getCityCode());
					SesameLoginInfo.setCarno(mCarInfo.getCarNo());
					SesameLoginInfo.setEngineno(mCarInfo.getEngineNo());
					SesameLoginInfo.setShortstandcarno(mCarInfo.getStandcarNo());
					SesameLoginInfo.setRegistno(mCarInfo.getRegistNo());
				}

				CarInfoLocal.setCarInfo(mCarInfo, SesameLoginInfo.getMobile());
				Intent mIntent = new Intent(CarFillIllegalActivity.this, CarQueryIllegalActivity.class);
				Bundle mBundle = new Bundle();
				mBundle.putSerializable(CarQueryIllegalActivity.POST_VIOLATION_INFO, mInfo);
				mIntent.putExtra("Bundle", mBundle);
				startActivity(mIntent);
				break;
			case 1:
				// 信息提交失败
				if (mDialog != null && mDialog.isShowing()) {
					mDialog.dismiss();
				}
				BaseResponseInfo mBaseResponseInfo = (BaseResponseInfo) msg.obj;
				if (mBaseResponseInfo != null && mBaseResponseInfo.getInfo() != null) {
					UUToast.showUUToast(CarFillIllegalActivity.this, "信息提交失败：" + mBaseResponseInfo.getInfo());
				} else {
					UUToast.showUUToast(CarFillIllegalActivity.this, "信息提交失败,请稍候再试!");
				}
				break;
			}
		};
	};

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

				mTextView1.setText("");
				mEditText1.setText("");
				mEditText2.setText("");
				mEditText3.setText("");
				mEditText4.setText("");

				mTextView1.setText(mCityInfo.getName());
				mTextView2.setVisibility(View.VISIBLE);
				mTextView2.setText(mCityInfo.getAbbr());

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
				}

			}

		}
	};

	private OnFocusChangeListener mChangeListener = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			switch (v.getId()) {
			case R.id.car_fill_illegal_edit1:
				if (hasFocus) {
					mView3.setBackgroundResource(R.drawable.edittext_bg_focused);
				} else {
					mView3.setBackgroundResource(R.drawable.edittext_bg);
				}
				break;

			case R.id.car_fill_illegal_edit2:

				if (hasFocus) {
					mView4.setBackgroundResource(R.drawable.edittext_bg_focused);
				} else {
					mView4.setBackgroundResource(R.drawable.edittext_bg);
				}
				break;
			case R.id.car_fill_illegal_edit3:
				if (hasFocus) {
					mView5.setBackgroundResource(R.drawable.edittext_bg_focused);
				} else {
					mView5.setBackgroundResource(R.drawable.edittext_bg);
				}
				break;
			case R.id.car_fill_illegal_edit4:
				if (hasFocus) {
					mView6.setBackgroundResource(R.drawable.edittext_bg_focused);
				} else {
					mView6.setBackgroundResource(R.drawable.edittext_bg);
				}
				break;

			}

		}
	};

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		mCarInfo = (CarInfo) intent.getSerializableExtra(CAR_INFO);
		from = intent.getStringExtra(CLASS_NAME);
		if (mCarInfo != null) {
			Log.e("info", "mCarInfo.getCarNo()==" + mCarInfo.getCarNo());
		}
		LoadData();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == 0 && data != null) {
			CarInfo mcInfo = (CarInfo) data.getSerializableExtra(CarFillIllegalActivity.CAR_INFO);
			if (mcInfo != null) {
				String cityName = "";
				String cityCode = "";
				String carno = "";
				String engineno = "";
				String standcarno = "";
				String registno = "";

				cityName = mcInfo.getCityName();
				cityCode = mcInfo.getCityCode();
				carno = mcInfo.getCarNo();
				if (TextUtils.isEmpty(carno)) {
					return;
				}
				
				if(carno.equals(CarListActivity.CAR_ADD)){
					carno = "";
				}
				
				engineno = mcInfo.getEngineNo();
				standcarno = mcInfo.getStandcarNo();
				registno = mcInfo.getRegistNo();
				if (cityName != null && !cityName.equals("")) {
					mTextView1.setText(cityName);
				} else {
					mTextView1.setText("");
				}
				if (cityCode != null && !cityCode.equals("")) {
					code = cityCode;
				} else {
					code = "";
				}
				if (carno != null && !carno.equals("")) {
					String province = carno.substring(0, 1);
					String number = carno.substring(1);
					mEditText1.setText(number);
					mTextView2.setVisibility(View.VISIBLE);
					mTextView2.setText(province);
				} else {
//					mTextView2.setVisibility(View.GONE);
					mEditText1.setText("");
					mTextView2.setText("");
				}
				if (engineno != null && !engineno.equals("")) {
					mView4.setVisibility(View.VISIBLE);
					mEditText2.setText(engineno);
				} else {
					mView4.setVisibility(View.GONE);
					mEditText2.setText("");
				}
				if (standcarno != null && !standcarno.equals("")) {
					mView5.setVisibility(View.VISIBLE);
					mEditText3.setText(standcarno);
				} else {
					mView5.setVisibility(View.GONE);
					mEditText3.setText("");
				}
				if (registno != null && !registno.equals("")) {
					mView6.setVisibility(View.VISIBLE);
					mEditText4.setText(registno);
				} else {
					mView6.setVisibility(View.GONE);
					mEditText4.setText("");
				}
			}

		}

	}

}
