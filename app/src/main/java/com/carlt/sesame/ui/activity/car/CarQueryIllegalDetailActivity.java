package com.carlt.sesame.ui.activity.car;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.doride.http.retrofitnet.model.GetCarInfo;
import com.carlt.sesame.data.SesameLoginInfo;
import com.carlt.sesame.data.car.ViolationInfo;
import com.carlt.sesame.ui.activity.base.LoadingActivityWithTitle;
import com.carlt.sesame.ui.adapter.QueryIllegalAdapter;



/**
 * 座驾-违章查询详情页面
 * 
 * @author daisy
 */
public class CarQueryIllegalDetailActivity extends LoadingActivityWithTitle
		implements /*OnGetPoiSearchResultListener,*/ OnClickListener {

	private ImageView back;// 头部返回键

	private TextView title;// 标题文字

	private TextView txtRight;// 头部右侧文字

	private TextView mTextView1;// 违章地点

	private TextView mTextView2;// 违章描述

	private TextView mTextView3;// 违章日期

	private TextView mTextView4;// 违章扣分

	private TextView mTextView5;// 违章罚款

	private View mView;// 吐槽layout

	private TextView mTextViewState;// 处理状态

	private QueryIllegalAdapter mAdapter;

	public final static String VIOLATION_INFO = "violation_info";

	private ViolationInfo mViolationInfo;

//	private MapView mMapView;
//	private PoiSearch mPoiSearch = null;
//	private BaiduMap mBaiduMap = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_car_query_illegal_detail);
		setTitleView(R.layout.head_back);
//		mPoiSearch = PoiSearch.newInstance();
//		mPoiSearch.setOnGetPoiSearchResultListener(this);
//		mMapView = (MapView) findViewById(R.id.activity_car_query_illegal_detail_map);
//		mBaiduMap = mMapView.getMap();

		if (getIntent().getBundleExtra("Bundle") != null) {
			mViolationInfo = (ViolationInfo) getIntent().getBundleExtra(
					"Bundle").getSerializable(VIOLATION_INFO);
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
		title.setText("违章详情");
		txtRight.setVisibility(View.GONE);

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void init() {
		mTextView1 = (TextView) findViewById(R.id.activity_car_query_illegal_detail_txt1);
		mTextView2 = (TextView) findViewById(R.id.activity_car_query_illegal_detail_txt2);
		mTextView3 = (TextView) findViewById(R.id.activity_car_query_illegal_detail_txt3);
		mTextView4 = (TextView) findViewById(R.id.activity_car_query_illegal_detail_txt4);
		mTextView5 = (TextView) findViewById(R.id.activity_car_query_illegal_detail_txt5);
		mView = findViewById(R.id.activity_car_query_illegal_detail_layout);

		mTextViewState = (TextView) findViewById(R.id.activity_car_query_illegal_detail_txt_state);

		mView.setOnClickListener(this);
	}

	@Override
	protected void LoadSuccess(Object data) {
		if (mViolationInfo.getArea() != null
				&& !mViolationInfo.getArea().equals("")) {
			mTextView1.setText(mViolationInfo.getArea());
		}
		if (mViolationInfo.getAct() != null
				&& !mViolationInfo.getAct().equals("")) {
			mTextView2.setText(mViolationInfo.getAct());
		}
		if (mViolationInfo.getDate() != null
				&& !mViolationInfo.getDate().equals("")) {
			mTextView3.setText(mViolationInfo.getDate());
		}
		if (mViolationInfo.getFen() != null
				&& !mViolationInfo.getFen().equals("")) {
			StringBuffer mStringBuffer = new StringBuffer();
			mStringBuffer.append("扣");
			mStringBuffer.append(mViolationInfo.getFen() + "分");
			mTextView4.setText(mStringBuffer.toString());
		}
		if (mViolationInfo.getMoney() != null
				&& !mViolationInfo.getMoney().equals("")) {
			StringBuffer mStringBuffer = new StringBuffer();
			mStringBuffer.append("罚");
			mStringBuffer.append(mViolationInfo.getMoney() + "元");
			mTextView5.setText(mStringBuffer.toString());
		}
		if (mViolationInfo.getHandled().equals(ViolationInfo.HANDLED)) {
			// 已处理
			mTextViewState.setBackgroundResource(R.drawable.icon_handled);
		} else if (mViolationInfo.getHandled().equals(ViolationInfo.HANDLED_NO)) {
			// 未处理
			mTextViewState.setBackgroundResource(R.drawable.icon_unhandled);
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
		search(GetCarInfo.getInstance().city);

	}

	@Override
	public void onClick(View v) {
		// 吐槽
		View shareView = findViewById(R.id.activity_car_query_illegal_detail);
//		ShareControl.share(CarQueryIllegalDetailActivity.class,
//				CarQueryIllegalDetailActivity.this,
//				mViolationInfo.getShareTitle(), mViolationInfo.getShareText(),
//				mViolationInfo.getShareLink(), shareView);
	}

	private void search(String city) {

//		mPoiSearch.searchInCity((new PoiCitySearchOption()).city(city)
//				.keyword(mViolationInfo.getArea()).pageNum(0));
	}

//	public void onGetPoiResult(PoiResult result) {
//		if (result == null
//				|| result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
//			BaseResponseInfo mBaseResponseInfo = new BaseResponseInfo();
//			mBaseResponseInfo.setFlag(0);
//			mBaseResponseInfo.setInfo("未查询到地点");
//			LoadErro(mBaseResponseInfo);
//			return;
//		}
//		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
//			mBaiduMap.clear();
//			PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
//			mBaiduMap.setOnMarkerClickListener(overlay);
//			overlay.setData(result);
//			overlay.addToMap();
//			overlay.zoomToSpan();
//			LoadSuccess(null);
//			return;
//		}
//		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {
//
//			BaseResponseInfo mBaseResponseInfo = new BaseResponseInfo();
//			mBaseResponseInfo.setFlag(0);
//			mBaseResponseInfo.setInfo("未查询到地点");
//			LoadErro(mBaseResponseInfo);
//			// // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
//			// String strInfo = "在";
//			// for (CityInfo cityInfo : result.getSuggestCityList()) {
//			// strInfo += cityInfo.city;
//			// strInfo += ",";
//			// }
//			// strInfo += "找到结果";
//			// UUToast.showUUToast(CarQueryIllegalDetailActivity.this, strInfo,
//			// Toast.LENGTH_LONG);
//		}
//	}

//	public void onGetPoiDetailResult(PoiDetailResult result) {
//		// if (result.error != SearchResult.ERRORNO.NO_ERROR) {
//		// Toast.makeText(PoiSearchDemo.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
//		// .show();
//		// } else {
//		// Toast.makeText(PoiSearchDemo.this,
//		// result.getName() + ": " + result.getAddress(),
//		// Toast.LENGTH_SHORT).show();
//		// }
//	}

//	private class MyPoiOverlay extends PoiOverlay {
//
//		public MyPoiOverlay(BaiduMap baiduMap) {
//			super(baiduMap);
//		}
//
//		@Override
//		public boolean onPoiClick(int index) {
//			return super.onPoiClick(index);
//			// PoiInfo poi = getPoiResult().getAllPoi().get(index);
//			// // if (poi.hasCaterDetails) {
//			// mPoiSearch.searchPoiDetail((new PoiDetailSearchOption())
//			// .poiUid(poi.uid));
//			// // }
//			// return true;
//		}
//	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

}
