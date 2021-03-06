
package com.carlt.sesame.ui.activity.car;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.CoordinateConverter.CoordType;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.model.Text;
import com.amap.api.maps.model.TextOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.AoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.geocoder.RegeocodeRoad;
import com.amap.api.services.road.Crossroad;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.amap.api.services.route.WalkStep;
import com.carlt.doride.DorideApplication;
import com.carlt.doride.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.ui.activity.base.LoadingActivityWithTitle;
import com.carlt.sesame.ui.activity.car.map.PositionEntity;
import com.carlt.sesame.ui.activity.car.map.RouteTask;
import com.carlt.sesame.ui.activity.car.map.RouteTask.OnRouteResult;
import com.carlt.sesame.ui.view.PopBoxCreat;
import com.carlt.sesame.utility.SensorEventHelper;
import com.carlt.sesame.utility.UUToast;
import com.carlt.sesame.utility.WalkRouteOverlay;

import java.util.List;

/**
 * 
 * @author @Y.yun
 * 
 */
public class FindCarActivity extends LoadingActivityWithTitle implements LocationSource, AMapLocationListener, OnClickListener {
	private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
	private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);

	private ImageView back;// 头部返回键
	private TextView title;// 标题文字
	private TextView txtRight;// 头部右侧文字

	private View mViewInput;// 输入框
	private TextView mTxtPos;// 爱车位置
	private ImageView mImgCha;// 叉号按钮
	private ImageView mImgPLoc;// che的位置
	private TextView mTxtPLoc;

	private MapView mMapView;
	private AMap mMap;
	private Marker mPositionMark;
	private LatLng mStartPosition;
	private AMapLocationClient mLocationClient;
	private AMapLocation mFirstLoc;
	private LatLng mFirstCarLoc;
	private LatLng mCurrentCarLoc;
	private OnLocationChangedListener mListener;
	private RouteTask mRouteTask;
	private Polyline mRouteLine;
	private Marker startMarker;
	private Marker endMarker;
	private Text txtStart;
	private Text txtEnd;
	private Dialog mPDialog;
	private PolylineOptions mlineOption;
	private WalkPath mPath;
	private WalkRouteResult mResult;
	private String locName;// 终点位置
	private String fromLocName;// 从另一页跳转
	private Marker mLocMarker;
	private Circle mLocCircle;
	private boolean isMyLocenable = false;
	private boolean isNeedRefresh=false;//是否需要刷新
	private AMapLocation mCurrentLoc;
	private SensorEventHelper mSensorHelper;
	private final static int ZOOM = 20;// 缩放级别

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_findcar);
		setTitleView(R.layout.head_back);

		initTitle();
		init(savedInstanceState);
		LoadData();
	}

	private void initTitle() {
		back = (ImageView) findViewById(R.id.head_back_img1);
		title = (TextView) findViewById(R.id.head_back_txt1);
		txtRight = (TextView) findViewById(R.id.head_back_txt2);

		back.setImageResource(R.drawable.arrow_back);
		title.setText("定位寻车");
		txtRight.setVisibility(View.VISIBLE);
		txtRight.setText("终点变更");

		txtRight.setOnClickListener(this);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void init(Bundle savedInstanceState) {
		mMapView = (MapView) findViewById(R.id.findcar_mapView);
		mViewInput = findViewById(R.id.findcar_lay_input);
		mTxtPos = (TextView) findViewById(R.id.findcar_txt_carpos);
		mImgCha = (ImageView) findViewById(R.id.findcar_img_cha);
		mImgPLoc = (ImageView) findViewById(R.id.findCar_img_persion);
		mTxtPLoc = (TextView) findViewById(R.id.findCar_img_persion_txt);

		mImgPLoc.setVisibility(View.GONE);
		mTxtPLoc.setVisibility(View.GONE);
		mViewInput.setVisibility(View.GONE);

		mMapView.onCreate(savedInstanceState);
		mMap = mMapView.getMap();
		mMap.setLocationSource(this);
//		mMap.getUiSettings().setMyLocationButtonEnazbled(false);
		mMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
		setMylocEnable(false);
		mMap.getUiSettings().setScaleControlsEnabled(true);
		mTxtPos.setOnClickListener(this);
		mImgCha.setOnClickListener(this);

		mRouteTask = RouteTask.getInstance(getApplicationContext());

		mSensorHelper = new SensorEventHelper(this);
			mSensorHelper.registerSensorListener();

		// mRegeocodeTask = new RegeocodeTask(getApplicationContext());
		if (mLocationClient == null) {
			mLocationClient = new AMapLocationClient(this);
			AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
			mLocationOption.setInterval(2000);
			// 设置定位监听
			mLocationClient.setLocationListener(this);
			// 设置为高精度定位模式
			mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
			// 设置定位参数
			mLocationClient.setLocationOption(mLocationOption);
			// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
			// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
			// 在定位结束后，在合适的生命周期调用onDestroy()方法
			// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
			mLocationClient.startLocation();
		}
	}

	@Override
	protected void LoadSuccess(Object data) {
//		 data = "108.95224799262152,34.19749321831597";
		if (data != null && !TextUtils.isEmpty(data.toString())) {
			String ss = data.toString();
			String[] ll = ss.split(",");
			if (ll.length > 1) {
				LatLng latLng = new LatLng(Double.valueOf(ll[1]), Double.valueOf(ll[0]));
				CoordinateConverter converter = new CoordinateConverter(context);
				converter.from(CoordType.GPS);
				converter.coord(latLng);
				mFirstCarLoc = converter.convert();
				getAddress(mFirstCarLoc);
			} else {
				super.LoadSuccess(null);
				UUToast.showUUToast(context, "暂未获取到车辆位置");
			}
			drawFirstLine();
		} else {
			super.LoadSuccess(null);
			UUToast.showUUToast(context, "暂未获取到车辆位置");
		}

	}

	/**
	 * 
	 */
	public void getAddress(LatLng ll) {
		GeocodeSearch geocoderSearch = new GeocodeSearch(this);
		geocoderSearch.setOnGeocodeSearchListener(new OnGeocodeSearchListener() {

			@Override
			public void onRegeocodeSearched(RegeocodeResult arg0, int arg1) {
				if (arg1 == 1000) {
					if (arg0 != null && arg0.getRegeocodeAddress() != null && arg0.getRegeocodeAddress().getFormatAddress() != null) {
						// locName =
						// arg0.getRegeocodeAddress().getFormatAddress();

						RegeocodeAddress raddress = arg0.getRegeocodeAddress();
						String building = raddress.getBuilding();
						String bname = raddress.getBusinessAreas().size() > 0 ? raddress.getBusinessAreas().get(0).getName() : "";
						String adCode = raddress.getAdCode();
						String towncode = raddress.getTowncode();
						String township = raddress.getTownship();
						List<AoiItem> aos = raddress.getAois();
						List<Crossroad> cRoads = raddress.getCrossroads();
						List<PoiItem> pois = raddress.getPois();
						List<RegeocodeRoad> roads = raddress.getRoads();

						locName = township;

						// if(aos.size()>0){
						// locName += aos.get(0).getAoiName();
						// }

						if (pois.size() > 0) {
							locName += pois.get(0).getTitle();
						}

					}
				}
			}

			@Override
			public void onGeocodeSearched(GeocodeResult arg0, int arg1) {
			}

		});
		// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
		RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(ll.latitude, ll.longitude), 200, GeocodeSearch.AMAP);
		geocoderSearch.getFromLocationAsyn(query);
	}

	@Override
	protected void LoadErro(Object erro) {
		// super.LoadErro(erro);
		super.LoadSuccess(null);
		UUToast.showUUToast(context, "暂未获取到车辆位置");
	}

	@Override
	protected void LoadData() {
		super.LoadData();
		CPControl.GetCarExtInfo(listener);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mMapView.onResume();
		if (mSensorHelper != null) {
			mSensorHelper.registerSensorListener();
		} else {
			mSensorHelper = new SensorEventHelper(this);
			if (mSensorHelper != null) {
				mSensorHelper.registerSensorListener();

				if (mSensorHelper.getCurrentMarker() == null && mLocMarker != null) {
					mSensorHelper.setCurrentMarker(mLocMarker);
				}
			}
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		mMapView.onPause();
		if (mSensorHelper != null) {
			mSensorHelper.unRegisterSensorListener();
			mSensorHelper.setCurrentMarker(null);
			mSensorHelper = null;
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mMapView.onDestroy();
		if (mLocationClient != null) {
			mLocationClient.stopLocation();
			mLocationClient.onDestroy();
		}
		mLocationClient = null;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.findcar_txt_carpos:
			// 跳转至智能搜索页面
			Intent mIntent = new Intent(FindCarActivity.this, SearchAddrActivity.class);
			if (mFirstLoc == null) {
				UUToast.showUUToast(context, "未获取到当前位置!");
				return;
			}
			fromLocName = null;
			mIntent.putExtra("latlng", mFirstLoc.getLatitude() + "," + mFirstLoc.getLongitude());
			mIntent.putExtra("cityCode", mFirstLoc.getCityCode());
			startActivityForResult(mIntent, 0);
			break;

		case R.id.findcar_img_cha:
			// 隐藏输入框
			clearMarkers();
			reSet();
			break;
		case R.id.head_back_txt2:
			if (mViewInput.getVisibility() == View.VISIBLE) {
				mViewInput.setVisibility(View.GONE);
				mImgPLoc.setVisibility(View.GONE);
				mTxtPLoc.setVisibility(View.GONE);
				txtRight.setText("终点变更");
				// 点击确定开始导航画线
				BitmapDescriptor bd = BitmapDescriptorFactory.fromResource(R.drawable.icon_loaction_car);
				int w = mMapView.getWidth() / 2 + bd.getWidth() / 2;
				int h = mMapView.getHeight() / 2 + bd.getHeight();
				Point p = new Point(w, h);
				LatLng ll = mMap.getProjection().fromScreenLocation(p);

				if (mFirstLoc == null) {
					UUToast.showUUToast(context, "定位失败");
					return;
				}

				PositionEntity ps = new PositionEntity();
				ps.longitude = mFirstLoc.getLongitude();
				ps.latitue = mFirstLoc.getLatitude();

				PositionEntity pe = new PositionEntity();
				pe.longitude = ll.longitude;
				pe.latitue = ll.latitude;
				clearMarkers();
				mCurrentLoc=null;
				drawLine(ps, pe);
				locName = "";
				getAddress(ll);
				mFirstCarLoc = ll;
			} else {
				if (mFirstCarLoc == null) {
					UUToast.showUUToast(context, "暂未获取到车辆位置");
					return;
				}

				if (mFirstLoc == null) {
					UUToast.showUUToast(context, "未获取到当前位置!");
					return;
				}

				setMylocEnable(false);
				mViewInput.setVisibility(View.VISIBLE);
				txtRight.setText("确定");
				clearMarkers();

				if (mFirstLoc != null) {
					LatLng ll = null;
					if (mlineOption == null || mlineOption.getPoints().size() < 1) {
						ll = new LatLng(mFirstLoc.getLatitude(), mFirstLoc.getLongitude());
					} else {
						ll = mlineOption.getPoints().get(0);
					}

					MarkerOptions startOpt = new MarkerOptions();
					BitmapDescriptor descriptorStart = BitmapDescriptorFactory.fromResource(R.drawable.icon_loaction_user);
					startOpt.icon(descriptorStart);
					startOpt.anchor(0.5f, 1.1f);
					startOpt.position(ll);
					startMarker = mMap.addMarker(startOpt);

					TextOptions toptStart = new TextOptions();
					toptStart.position(ll);
					toptStart.fontSize((int) (DorideApplication.ScaledDensity * 14));
					toptStart.fontColor(Color.parseColor("#FFFFFF"));
					toptStart.text("您的位置");
					toptStart.backgroundColor(Color.parseColor("#404040"));
					mMap.addText(toptStart);
					CameraUpdate update = CameraUpdateFactory.newLatLngZoom(mFirstCarLoc, 18);
					mMap.animateCamera(update);
				}
				mImgPLoc.setVisibility(View.VISIBLE);
				mTxtPLoc.setVisibility(View.VISIBLE);

				if (!TextUtils.isEmpty(locName)) {
					mTxtPos.setText(Html.fromHtml(locName + "  <font color='#666666'>(点击修改)</font>"));
				} else {
					mTxtPos.setText(Html.fromHtml("暂无车辆位置信息" + "  <font color='#666666'>(点击修改)</font>"));
					getAddress(mFirstCarLoc);
				}
			}
			break;
		}
	}

	// LocationSource
	@Override
	public void activate(OnLocationChangedListener arg0) {
		mListener = arg0;
	}

	// LocationSource
	@Override
	public void deactivate() {
		mListener = null;
		// if (mLocationClient != null) {
		// mLocationClient.stopLocation();
		// mLocationClient.onDestroy();
		// }
		// mLocationClient = null;
	}

	public void clearMarkers() {
		mMap.clear();
	}

	// AMapLocationListener
	@Override
	public void onLocationChanged(AMapLocation arg0) {
		if (arg0 != null && arg0.getErrorCode() == 0) {

			if (mFirstLoc == null) {
				mFirstLoc = arg0;
				// 开始导航画线
				drawFirstLine();
			}

			mFirstLoc = arg0;
			
			if (isMyLocenable) {
				if (mCurrentLoc != null) {
					if (mCurrentLoc.getLatitude() == arg0.getLatitude()
							&& mCurrentLoc.getLongitude() == arg0
									.getLongitude()) {
						isNeedRefresh = false;
					} else {
						isNeedRefresh = true;
					}
				} else {
					isNeedRefresh = true;
				}
				if(isNeedRefresh){
					// 显示更新 定位蓝点
					if (mLocMarker != null) {
						mLocMarker.remove();
						mLocMarker = null;
					}

					if (mLocCircle != null) {
						mLocCircle.remove();
						mLocCircle = null;
					}

					addCircle(new LatLng(mFirstLoc.getLatitude(), mFirstLoc.getLongitude()), mFirstLoc.getAccuracy());
					addMarker(new LatLng(mFirstLoc.getLatitude(), mFirstLoc.getLongitude()));
					mSensorHelper.setCurrentMarker(mLocMarker);// 定位图标旋转
				}
				mCurrentLoc = arg0;
			}

		} else {
			String errText = "定位失败," + arg0.getErrorCode() + ": " + arg0.getErrorInfo();
			Log.e("AmapErr", errText);
		}
	}

	public void drawFirstLine() {
		if (mFirstCarLoc != null) {
			super.LoadSuccess(null);
			if (mFirstLoc != null) {
				PositionEntity ps = new PositionEntity();
				ps.longitude = mFirstLoc.getLongitude();
				ps.latitue = mFirstLoc.getLatitude();
				PositionEntity pe = new PositionEntity();
				pe.longitude = mFirstCarLoc.longitude;
				pe.latitue = mFirstCarLoc.latitude;
				clearMarkers();
				drawLine(ps, pe);
			} else {
				UUToast.showUUToast(context, "正在定位您的位置");
				TextOptions toptEnd = new TextOptions();
				toptEnd.position(mFirstCarLoc);
				toptEnd.fontSize((int) (DorideApplication.ScaledDensity * 14));
				toptEnd.fontColor(Color.parseColor("#FFFFFF"));
				toptEnd.text("爱车位置");
				toptEnd.backgroundColor(Color.parseColor("#404040"));
				txtEnd = mMap.addText(toptEnd);

				MarkerOptions endOpt = new MarkerOptions();
				BitmapDescriptor descriptorEnd = BitmapDescriptorFactory.fromResource(R.drawable.icon_loaction_car);
				endOpt.icon(descriptorEnd);
				endOpt.anchor(0.5f, 1.1f);
				endOpt.position(mFirstCarLoc);
				endMarker = mMap.addMarker(endOpt);
			}
		}
	}

	void showProgressDialog() {
		dissmissDialog();
		mPDialog = PopBoxCreat.createDialogWithProgress(context, "正在处理请稍等。。。");
		mPDialog.show();
	}

	void dissmissDialog() {
		if (mPDialog != null && mPDialog.isShowing()) {
			mPDialog.dismiss();
		}
	}

	/**
	 * @param ps
	 * @param pe
	 */
	public void drawLine(final PositionEntity ps, final PositionEntity pe) {
		showProgressDialog();
		setMylocEnable(true);
		mRouteTask.setStartPoint(ps);
		mRouteTask.setEndPoint(pe);
		mRouteTask.removeAllResultListener();
		mRouteTask.addRouteResultListener(new OnRouteResult() {

			@Override
			public void onRoutePath(WalkPath path, WalkRouteResult result) {
				dissmissDialog();
				LatLng lls = null;
				LatLng lle = null;
				if (path == null || result == null) {
					UUToast.showUUToast(context, "路径规划失败，请检查您的网络和定位设置；（当您的爱车离您过远时，无法规划您的步行路径）");
					lls = new LatLng(ps.latitue, ps.longitude);
					lle = new LatLng(pe.latitue, pe.longitude);
					CameraUpdate update = CameraUpdateFactory.newLatLngZoom(lle, 18);
					mMap.animateCamera(update);
				} else {
					mPath = path;
					mResult = result;

					List<WalkStep> steps = path.getSteps();
					mlineOption = new PolylineOptions();
					for (WalkStep walkStep : steps) {
						for (LatLonPoint ll : walkStep.getPolyline()) {
							LatLng l2 = new LatLng(ll.getLatitude(), ll.getLongitude());
							mlineOption.add(l2);
						}
					}

					mlineOption.color(Color.parseColor("#3D93FD"));
					mlineOption.width(13);
					mRouteLine = mMap.addPolyline(mlineOption);
					lls = mlineOption.getPoints().get(0);
					lle = mlineOption.getPoints().get(mlineOption.getPoints().size() - 1);

					WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(context, mMap, path, result.getStartPos(), result.getTargetPos());
					walkRouteOverlay.zoomToSpan();
					walkRouteOverlay.removeFromMap();
				}

				MarkerOptions startOpt = new MarkerOptions();
				BitmapDescriptor descriptorStart = BitmapDescriptorFactory.fromResource(R.drawable.icon_loaction_user);
				startOpt.icon(descriptorStart);
				startOpt.anchor(0.5f, 1.1f);
				startOpt.position(lls);
				startMarker = mMap.addMarker(startOpt);

				TextOptions toptStart = new TextOptions();
				toptStart.position(lls);
				toptStart.fontSize((int) (DorideApplication.ScaledDensity * 14));
				toptStart.fontColor(Color.parseColor("#FFFFFF"));
				toptStart.text("您的位置");
				toptStart.backgroundColor(Color.parseColor("#404040"));
				txtStart = mMap.addText(toptStart);

				
				
				TextOptions toptEnd = new TextOptions();
				toptEnd.position(lle);
				toptEnd.fontSize((int) (DorideApplication.ScaledDensity * 14));
				toptEnd.fontColor(Color.parseColor("#FFFFFF"));
				toptEnd.text("爱车位置");
				toptEnd.backgroundColor(Color.parseColor("#404040"));
				txtEnd = mMap.addText(toptEnd);

				MarkerOptions endOpt = new MarkerOptions();
				BitmapDescriptor descriptorEnd = BitmapDescriptorFactory.fromResource(R.drawable.icon_loaction_car);
				endOpt.icon(descriptorEnd);
				endOpt.anchor(0.5f, 1.1f);
				endOpt.position(lle);
				endMarker = mMap.addMarker(endOpt);

			}
		});
		mRouteTask.search();
	}

	public void reSet() {
		mViewInput.setVisibility(View.GONE);
		txtRight.setText("终点变更");
		setMylocEnable(true);
		mImgPLoc.setVisibility(View.GONE);
		mTxtPLoc.setVisibility(View.GONE);
		if (mPath != null) {
			WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(context, mMap, mPath, mResult.getStartPos(), mResult.getTargetPos());
			walkRouteOverlay.removeFromMap();
			walkRouteOverlay.zoomToSpan();

			mlineOption = new PolylineOptions();
			for (WalkStep walkStep : mPath.getSteps()) {
				for (LatLonPoint ll : walkStep.getPolyline()) {
					LatLng l2 = new LatLng(ll.getLatitude(), ll.getLongitude());
					mlineOption.add(l2);
				}
			}

			mlineOption.color(Color.parseColor("#3D93FD"));
			mlineOption.width(13);
			mRouteLine = mMap.addPolyline(mlineOption);

			TextOptions toptEnd = new TextOptions();
			toptEnd.position(mlineOption.getPoints().get(mlineOption.getPoints().size() - 1));
			toptEnd.fontSize((int) (DorideApplication.ScaledDensity * 14));
			toptEnd.fontColor(Color.parseColor("#FFFFFF"));
			toptEnd.text("爱车位置");
			toptEnd.backgroundColor(Color.parseColor("#404040"));
			txtEnd = mMap.addText(toptEnd);

			MarkerOptions endOpt = new MarkerOptions();
			BitmapDescriptor descriptorEnd = BitmapDescriptorFactory.fromResource(R.drawable.icon_loaction_car);
			endOpt.icon(descriptorEnd);
			endOpt.anchor(0.5f, 1.1f);
			endOpt.position(mlineOption.getPoints().get(mlineOption.getPoints().size() - 1));
			endMarker = mMap.addMarker(endOpt);

			MarkerOptions startOpt = new MarkerOptions();
			BitmapDescriptor descriptorStart = BitmapDescriptorFactory.fromResource(R.drawable.icon_loaction_user);
			startOpt.icon(descriptorStart);
			startOpt.anchor(0.5f, 1.1f);
			startOpt.position(new LatLng(mFirstLoc.getLatitude(), mFirstLoc.getLongitude()));
			startMarker = mMap.addMarker(startOpt);

			TextOptions toptStart = new TextOptions();
			toptStart.position(new LatLng(mFirstLoc.getLatitude(), mFirstLoc.getLongitude()));
			toptStart.fontSize((int) (DorideApplication.ScaledDensity * 14));
			toptStart.fontColor(Color.parseColor("#FFFFFF"));
			toptStart.text("您的位置");
			toptStart.backgroundColor(Color.parseColor("#404040"));
			txtStart = mMap.addText(toptStart);

		} else {
			if (mFirstCarLoc != null) {
				TextOptions toptEnd = new TextOptions();
				toptEnd.position(mFirstCarLoc);
				toptEnd.fontSize((int) (DorideApplication.ScaledDensity * 14));
				toptEnd.fontColor(Color.parseColor("#FFFFFF"));
				toptEnd.text("爱车位置");
				toptEnd.backgroundColor(Color.parseColor("#404040"));
				txtEnd = mMap.addText(toptEnd);

				MarkerOptions endOpt = new MarkerOptions();
				BitmapDescriptor descriptorEnd = BitmapDescriptorFactory.fromResource(R.drawable.icon_loaction_car);
				endOpt.icon(descriptorEnd);
				endOpt.anchor(0.5f, 1.1f);
				endOpt.position(mFirstCarLoc);
				endMarker = mMap.addMarker(endOpt);
				
				
				MarkerOptions startOpt = new MarkerOptions();
				BitmapDescriptor descriptorStart = BitmapDescriptorFactory.fromResource(R.drawable.icon_loaction_user);
				startOpt.icon(descriptorStart);
				startOpt.anchor(0.5f, 1.1f);
				startOpt.position(new LatLng(mFirstLoc.getLatitude(), mFirstLoc.getLongitude()));
				startMarker = mMap.addMarker(startOpt);

				TextOptions toptStart = new TextOptions();
				toptStart.position(new LatLng(mFirstLoc.getLatitude(), mFirstLoc.getLongitude()));
				toptStart.fontSize((int) (DorideApplication.ScaledDensity * 14));
				toptStart.fontColor(Color.parseColor("#FFFFFF"));
				toptStart.text("您的位置");
				toptStart.backgroundColor(Color.parseColor("#404040"));
				txtStart = mMap.addText(toptStart);
				
				
				
				CameraUpdate update = CameraUpdateFactory.newLatLngZoom(mFirstCarLoc, 18);
				mMap.animateCamera(update);
			} else if (mFirstLoc != null) {
				MarkerOptions startOpt = new MarkerOptions();
				BitmapDescriptor descriptorStart = BitmapDescriptorFactory.fromResource(R.drawable.icon_loaction_user);
				startOpt.icon(descriptorStart);
				startOpt.anchor(0.5f, 1.1f);
				startOpt.position(new LatLng(mFirstLoc.getLatitude(), mFirstLoc.getLongitude()));
				startMarker = mMap.addMarker(startOpt);

				TextOptions toptStart = new TextOptions();
				toptStart.position(new LatLng(mFirstLoc.getLatitude(), mFirstLoc.getLongitude()));
				toptStart.fontSize((int) (DorideApplication.ScaledDensity * 14));
				toptStart.fontColor(Color.parseColor("#FFFFFF"));
				toptStart.text("您的位置");
				toptStart.backgroundColor(Color.parseColor("#404040"));
				txtStart = mMap.addText(toptStart);
				
				CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(mFirstLoc.getLatitude(), mFirstLoc.getLongitude()), 18);
				mMap.animateCamera(update);
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0) {
			if (data != null) {
				String latlng = data.getStringExtra("latlng");
				if (!TextUtils.isEmpty(latlng)) {
					fromLocName = data.getStringExtra("name");
					mTxtPos.setText(Html.fromHtml(fromLocName + "  <font color='#666666'>(点击修改)</font>"));
					PositionEntity pe = new PositionEntity();
					pe.longitude = Double.parseDouble(latlng.split(",")[1]);
					pe.latitue = Double.parseDouble(latlng.split(",")[0]);
					Log.e("info", "pe.longitude=="+pe.longitude);
					Log.e("info", "pe.latitue=="+pe.latitue);
					mFirstCarLoc = new LatLng(pe.latitue, pe.longitude);
					CameraUpdate update = CameraUpdateFactory.newLatLngZoom(mFirstCarLoc, ZOOM);
					//mMap.animateCamera(update);
					mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
							mFirstCarLoc, ZOOM));
					clearMarkers();
					MarkerOptions startOpt = new MarkerOptions();
					BitmapDescriptor descriptorStart = BitmapDescriptorFactory.fromResource(R.drawable.icon_loaction_user);
					startOpt.icon(descriptorStart);
					startOpt.anchor(0.5f, 1.1f);
					startOpt.position(new LatLng(mFirstLoc.getLatitude(), mFirstLoc.getLongitude()));
					startMarker = mMap.addMarker(startOpt);

					TextOptions toptStart = new TextOptions();
					toptStart.position(new LatLng(mFirstLoc.getLatitude(), mFirstLoc.getLongitude()));
					toptStart.fontSize((int) (DorideApplication.ScaledDensity * 14));
					toptStart.fontColor(Color.parseColor("#FFFFFF"));
					toptStart.text("您的位置");
					toptStart.backgroundColor(Color.parseColor("#404040"));
					txtStart = mMap.addText(toptStart);
				}
			}
		}
	}

	private void addCircle(LatLng latlng, double radius) {
		CircleOptions options = new CircleOptions();
		options.strokeWidth(1f);
		options.fillColor(FILL_COLOR);
		options.strokeColor(STROKE_COLOR);
		options.center(latlng);
		options.radius(radius);
		mLocCircle = mMap.addCircle(options);
	}

	private void addMarker(LatLng latlng) {
		if (mLocMarker != null) {
			return;
		}
		Bitmap bMap = BitmapFactory.decodeResource(this.getResources(), R.drawable.icon_loc);
		BitmapDescriptor des = BitmapDescriptorFactory.fromBitmap(bMap);
		MarkerOptions options = new MarkerOptions();
		options.icon(des);
		options.anchor(0.5f, 0.5f);
		options.position(latlng);
		mLocMarker = mMap.addMarker(options);
	}

	public void setMylocEnable(boolean enable) {
		this.isMyLocenable = enable;
		if (!enable) {
			if (mLocMarker != null) {
				mLocMarker.remove();
			}

			if (mLocCircle != null) {
				mLocCircle.remove();
			}

		}
	}

}
