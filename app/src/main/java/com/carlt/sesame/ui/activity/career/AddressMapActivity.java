
package com.carlt.sesame.ui.activity.career;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.OnMapLoadedListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.carlt.doride.R;
import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.data.SesameLoginInfo;
import com.carlt.sesame.ui.activity.base.LoadingActivityWithTitle;

/**
 * 4s店地址map
 * 
 * @author daisy
 */
public class AddressMapActivity extends LoadingActivityWithTitle implements OnMapLoadedListener
/* OnGetPoiSearchResultListener */ {

	private ImageView back;// 头部返回键

	private TextView title;// 标题文字

	private TextView txtRight;// 头部右侧文字

	private TextView mTextView1;// 4s店名称

	private TextView mTextView2;// 4s店地址

	private MapView mMapView;

	// private PoiSearch mPoiSearch = null;
	private AMap mMap = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_career_map_4s);
		setTitleView(R.layout.head_back);
		// mPoiSearch = PoiSearch.newInstance();
		// mPoiSearch.setOnGetPoiSearchResultListener(this);
		mMapView = (MapView) findViewById(R.id.activity_career_map_4s_map);
		mMapView.onCreate(savedInstanceState);
		mMap = mMapView.getMap();
		mMap.setOnMapLoadedListener(this);
//		mMap.getUiSettings().setAllGesturesEnabled(true);
//		mMap.getUiSettings().setZoomControlsEnabled(true);

		// getIntent().getStringExtra("address");
		initTitle();
		init();

		// String dealerAddress = getIntent().getStringExtra("ADDR");
		// if (dealerAddress == null || dealerAddress.equals("")) {
		// dealerAddress = LoginInfo.dealerAddres;
		// }
		// search("", dealerAddress);
		// LoadSuccess(null);
		// LoadData();
	}

	@Override
	protected void LoadData() {
		super.LoadData();
		if (SesameLoginInfo.getDealerLat() > 0 && SesameLoginInfo.getDealerLon() > 0) {
			mMap.clear();
			LoadSuccess(null);
			// // 定义Maker坐标点
			LatLng point = new LatLng(SesameLoginInfo.getDealerLat(), SesameLoginInfo.getDealerLon());
			MarkerOptions options = new MarkerOptions();
			BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.arrow_top_selected);
			options.icon(bitmap);
			options.anchor(0.5f, 1f);
			options.position(point);
			mMap.addMarker(options);

			mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 13));
		} else {
			BaseResponseInfo mBaseResponseInfo = new BaseResponseInfo();
			mBaseResponseInfo.setFlag(0);
			mBaseResponseInfo.setInfo("未查询到地点");
			LoadErro(mBaseResponseInfo);
		}
	}

	private void initTitle() {
		back = (ImageView) findViewById(R.id.head_back_img1);
		title = (TextView) findViewById(R.id.head_back_txt1);
		txtRight = (TextView) findViewById(R.id.head_back_txt2);

		back.setImageResource(R.drawable.arrow_back);
		if (SesameLoginInfo.getDealerUsername() != null) {
			title.setText(SesameLoginInfo.getDealerUsername());
		}

		txtRight.setVisibility(View.GONE);

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void init() {
		mTextView1 = (TextView) findViewById(R.id.activity_career_map_4s_txt1);
		mTextView2 = (TextView) findViewById(R.id.activity_career_map_4s_txt2);

		if (SesameLoginInfo.getDealerUsername() != null) {
			mTextView1.setText(SesameLoginInfo.getDealerUsername());
		}

		if (SesameLoginInfo.getDealerAddres() != null) {
			mTextView2.setText(SesameLoginInfo.getDealerAddres());
		}
	}

	@Override
	protected void LoadSuccess(Object data) {
		super.LoadSuccess(data);
	}

	@Override
	protected void LoadErro(Object erro) {
		super.LoadErro(erro);
	}

	private void search(String city, String dealerAddress) {
		// mPoiSearch.searchInCity((new PoiCitySearchOption()).city(city)
		// .keyword(dealerAddress).pageNum(0));

	}

	// public void onGetPoiResult(PoiResult result) {
	// if (result == null
	// || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
	// BaseResponseInfo mBaseResponseInfo = new BaseResponseInfo();
	// mBaseResponseInfo.setFlag(0);
	// mBaseResponseInfo.setInfo("未查询到地点");
	// LoadErro(mBaseResponseInfo);
	// return;
	// }
	// if (result.error == SearchResult.ERRORNO.NO_ERROR) {
	// mBaiduMap.clear();
	// PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
	// mBaiduMap.setOnMarkerClickListener(overlay);
	// overlay.setData(result);
	// overlay.addToMap();
	// overlay.zoomToSpan();
	// LoadSuccess(null);
	// return;
	// }
	// if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {
	//
	// BaseResponseInfo mBaseResponseInfo = new BaseResponseInfo();
	// mBaseResponseInfo.setFlag(0);
	// mBaseResponseInfo.setInfo("未查询到地点");
	// LoadErro(mBaseResponseInfo);
	// // // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
	// // String strInfo = "在";
	// // for (CityInfo cityInfo : result.getSuggestCityList()) {
	// // strInfo += cityInfo.city;
	// // strInfo += ",";
	// // }
	// // strInfo += "找到结果";
	// // UUToast.showUUToast(CarQueryIllegalDetailActivity.this, strInfo,
	// // Toast.LENGTH_LONG);
	// }
	// }

	// public void onGetPoiDetailResult(PoiDetailResult result) {
	// if (result.error != SearchResult.ERRORNO.NO_ERROR) {
	// Toast.makeText(PoiSearchDemo.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
	// .show();
	// } else {
	// Toast.makeText(PoiSearchDemo.this,
	// result.getName() + ": " + result.getAddress(),
	// Toast.LENGTH_SHORT).show();
	// }
	// }

	// private class MyPoiOverlay extends PoiOverlay {
	//
	// public MyPoiOverlay(BaiduMap baiduMap) {
	// super(baiduMap);
	// }
	//
	// @Override
	// public boolean onPoiClick(int index) {
	// return super.onPoiClick(index);
	// // PoiInfo poi = getPoiResult().getAllPoi().get(index);
	// // // if (poi.hasCaterDetails) {
	// // mPoiSearch.searchPoiDetail((new PoiDetailSearchOption())
	// // .poiUid(poi.uid));
	// // // }
	// // return true;
	// }
	// }

	@Override
	protected void onDestroy() {
		super.onDestroy();
		 mMapView.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
		 mMapView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		 mMapView.onResume();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);
	}

	@Override
	public void onMapLoaded() {
		LoadData();
	}
}
