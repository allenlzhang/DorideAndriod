package com.carlt.doride.ui.activity.carstate;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.AoiItem;
import com.amap.api.services.geocoder.BusinessArea;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.geocoder.RegeocodeRoad;
import com.amap.api.services.road.Crossroad;
import com.carlt.doride.R;
import com.carlt.doride.base.BaseActivity;
import com.carlt.doride.control.CPControl;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.ui.view.PopBoxCreat;
import com.carlt.doride.ui.view.UUToast;
import com.carlt.doride.utils.StringUtils;
import com.carlt.doride.utils.map.PositionEntity;
import com.carlt.doride.utils.map.SensorEventHelper;

import java.util.List;

/**
 * 导航同步到车
 */
public class LocationSynchronizeActivity extends BaseActivity implements
        LocationSource, AMapLocationListener, AMap.OnMapLoadedListener,
        AMap.OnCameraChangeListener, View.OnClickListener {

    private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
    private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);

    private ImageView back;// 头部返回键
    private TextView title;// 标题文字
    private TextView txtRight;// 头部右侧文字

    private View mViewInput;// 输入框
    private View mViewAddr;// 目的地详细信息展示框
    private View mViewSend;// 导航同步到车整个view
    private TextView mTxtTip;// 顶部提示信息
    private TextView mTxtAddrinfo;// 目的地文字信息
    private TextView mTxtAddrName;// 目的地名称
    private TextView mTxtAddrDetail;// 目的地详情
    private TextView mTxtSend;// 导航同步到车按钮
    private ImageView mImgBack;// 顶部框中的返回按钮
    private ImageView mImgCha;// 叉号
    private ImageView mImgDestination;// 目的地的位置

    private MapView mMapView;
    private AMap mMap;
    private AMapLocationClient mLocationClient;
    private AMapLocation mCurrentLoc;
    private LatLng mFirstCarLoc;
    private LatLng mDestinationLoc;// 目的地坐标
    private LocationSource.OnLocationChangedListener mListener;
    private String destinationName = "";// 目的地名称
    private String destinationAddr = "";// 目的地地址
    private String fromLocName;// 从搜索页跳转回来的值
    private String fromLocAddr;// 从搜索页跳转回来的值
    private Marker mLocMarker;
    private Circle mLocCircle;
    private boolean isMyLocenable = true;
    private SensorEventHelper mSensorHelper;

    private final static int TYPE_LOCATION = 1;// 定位UI
    private final static int TYPE_DESTINATION = 2;// 目的地UI

    private boolean isShowDestination;// 是否将目的地移动到地图中心
    private boolean isFromSearch;// 是否是从搜索页面跳转回来的

    private final static int ZOOM = 17;// 缩放级别

    private Dialog mDialog;// loading

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_synchronize);
        init(savedInstanceState);
        initMap(savedInstanceState);
    }

    private void initMap(Bundle savedInstanceState) {
        mMapView.onCreate(savedInstanceState);
        mMap = mMapView.getMap();
        mMap.setLocationSource(this);
        mMap.setMapType(AMap.MAP_TYPE_NIGHT);
        // 定位图标样式
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();
        Bitmap bMap = BitmapFactory.decodeResource(this.getResources(),
                R.mipmap.icon_loc);
        BitmapDescriptor des = BitmapDescriptorFactory.fromBitmap(bMap);
        myLocationStyle.myLocationIcon(des);
        myLocationStyle.strokeColor(STROKE_COLOR);
        myLocationStyle.radiusFillColor(FILL_COLOR);
        myLocationStyle.strokeWidth(1.0f);
        myLocationStyle.anchor(0.5f, 0.5f);
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW);
        myLocationStyle.showMyLocation(true);
        mMap.setMyLocationStyle(myLocationStyle);

        mMap.getUiSettings().setMyLocationButtonEnabled(false);// 是否显示定位按钮
        mMap.setMyLocationEnabled(true); // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // mMap.setMyLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);
        mMap.setOnMapLoadedListener(this);
        mMap.setOnCameraChangeListener(this);

        mSensorHelper = new SensorEventHelper(this);
        if (mSensorHelper != null) {
            mSensorHelper.registerSensorListener();
        }

        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(this);
            AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
            mLocationOption.setInterval(1000);
            // 设置定位监听
            mLocationClient.setLocationListener(this);
            // 设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            // 设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mLocationClient.startLocation();
        }
    }

    private void init(Bundle savedInstanceState) {
        mMapView = (MapView) findViewById(R.id.navigationtocar_mapView);
        mViewInput = findViewById(R.id.navigationtocar_lay_input);
        mViewAddr = findViewById(R.id.navigationtocar_lay_addr);
        mViewSend = findViewById(R.id.navigationtocar_lay_send);
        mTxtTip = (TextView) findViewById(R.id.navigationtocar_txt_input);
        mTxtAddrinfo = (TextView) findViewById(R.id.navigationtocar_txt_addrinfo);
        mTxtAddrName = (TextView) findViewById(R.id.navigationtocar_txt_addrname);
        mTxtAddrDetail = (TextView) findViewById(R.id.navigationtocar_txt_addrdetail);
        mTxtSend = (TextView) findViewById(R.id.navigationtocar_txt_send);
        mImgBack = (ImageView) findViewById(R.id.navigationtocar_img_back);
        mImgCha = (ImageView) findViewById(R.id.navigationtocar_img_cha);
        mImgDestination = (ImageView) findViewById(R.id.navigationtocar_img_destination);

        mTxtTip.setOnClickListener(this);
        mTxtAddrinfo.setOnClickListener(this);
        mTxtSend.setOnClickListener(this);
        mImgBack.setOnClickListener(this);
        mImgCha.setOnClickListener(this);

        mViewAddr.setVisibility(View.GONE);
        mViewSend.setVisibility(View.GONE);

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

                if (mSensorHelper.getCurrentMarker() == null
                        && mLocMarker != null) {
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
            case R.id.navigationtocar_txt_input:
                // 跳转至智能搜索页面
                Intent mIntent1 = new Intent(this,
                        SearchAddrActivity.class);
                if (mCurrentLoc == null) {
                    UUToast.showUUToast(this, "未获取到当前位置!");
                    return;
                }
                fromLocName = null;
                mIntent1.putExtra("latlng", mCurrentLoc.getLatitude() + ","
                        + mCurrentLoc.getLongitude());
                mIntent1.putExtra("cityCode", mCurrentLoc.getCityCode());
                startActivityForResult(mIntent1, 0);
                break;
            case R.id.navigationtocar_txt_addrinfo:
                // 跳转至智能搜索页面
                Intent mIntent2 = new Intent(this,
                        SearchAddrActivity.class);
                if (mCurrentLoc == null) {
                    UUToast.showUUToast(this, "未获取到当前位置!");
                    return;
                }
                fromLocName = null;
                mIntent2.putExtra("latlng", mCurrentLoc.getLatitude() + ","
                        + mCurrentLoc.getLongitude());
                mIntent2.putExtra("cityCode", mCurrentLoc.getCityCode());
                startActivityForResult(mIntent2, 0);
                break;
            case R.id.navigationtocar_img_back:
                // 跳转至智能搜索页面
                Intent mIntent3 = new Intent(this,
                        SearchAddrActivity.class);
                if (mCurrentLoc == null) {
                    UUToast.showUUToast(this, "未获取到当前位置!");
                    return;
                }
                fromLocName = null;
                mIntent3.putExtra("latlng", mCurrentLoc.getLatitude() + ","
                        + mCurrentLoc.getLongitude());
                mIntent3.putExtra("cityCode", mCurrentLoc.getCityCode());
                startActivityForResult(mIntent3, 0);
                break;
            case R.id.navigationtocar_img_cha:
                // 回到初始页面
                setUILayout(TYPE_LOCATION);
                isShowDestination = false;
                LatLng mLatLng = new LatLng(mCurrentLoc.getLatitude(),
                        mCurrentLoc.getLongitude());
                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(mLatLng,
                        ZOOM);
                mMap.moveCamera(update);
                break;
            case R.id.navigationtocar_txt_send:
                // 导航同步到车按钮
                double latitude = 0;// 纬度
                double longtitude = 0;// 经度
                if (mDestinationLoc != null) {
                    latitude = mDestinationLoc.latitude;
                    longtitude = mDestinationLoc.longitude;

                }
                String position = "";
                if (latitude >= 0 && longtitude >= 0) {
                    position = latitude + "," + longtitude;
                } else {
                    UUToast.showUUToast(this, "您还没有输入目的地哦");
                    return;
                }

                String location = mTxtAddrName.getText().toString();
                if (StringUtils.isEmpty(location)) {
                    UUToast.showUUToast(this, "您还没有输入目的地哦");
                    return;
                }
                if (mDialog == null) {
                    mDialog = PopBoxCreat.createDialogWithProgress(
                            this, "数据提交中...");
                }
                mDialog.show();
                CPControl.GetNavigationResult(position, location, listener_navigation);
                break;
        }
    }

    private BaseParser.ResultCallback listener_navigation = new BaseParser.ResultCallback() {

        @Override
        public void onSuccess(BaseResponseInfo o) {
            Message msg = new Message();
            msg.what = 2;
            msg.obj = o;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onError(BaseResponseInfo o) {
            Message msg = new Message();
            msg.what = 3;
            msg.obj = o;
            mHandler.sendMessage(msg);
        }
    };
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (mDialog != null) {
                mDialog.dismiss();
            }
            switch (msg.what) {
                case 2:
                    // 导航同步到车成功
                    BaseResponseInfo mInfo1 = (BaseResponseInfo) msg.obj;
                    String info1 = "";
                    if (mInfo1 != null) {
                        info1 = mInfo1.getInfo();
                        if (TextUtils.isEmpty(info1)) {
                            info1 = "目的地信息已上传成功！";
                        } else {

                        }
                    }
                    UUToast.showUUToast(LocationSynchronizeActivity.this, info1);
                    LocationSynchronizeActivity.this.finish();
                    break;

                case 3:
                    // 导航同步到车失败
                    BaseResponseInfo mInfo2 = (BaseResponseInfo) msg.obj;
                    String info2 = "";
                    if (mInfo2 != null) {
                        info2 = mInfo2.getInfo();
                        if (TextUtils.isEmpty(info2)) {
                            info2 = "目的地信息上传失败...";
                        } else {

                        }
                    }
                    UUToast.showUUToast(LocationSynchronizeActivity.this, info2);
                    break;
            }
        }

    };


    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null && amapLocation.getErrorCode() == 0) {

            if (mCurrentLoc != null) {
                if (mCurrentLoc.getLatitude() == amapLocation.getLatitude()
                        && mCurrentLoc.getLongitude() == amapLocation
                        .getLongitude()) {
                    isMyLocenable = false;
                } else {
                    isMyLocenable = true;
                }
            } else {
                isMyLocenable = true;
            }
            mCurrentLoc = amapLocation;
            LatLng location = new LatLng(amapLocation.getLatitude(),
                    amapLocation.getLongitude());
            if (isMyLocenable) {
                if (mLocMarker != null) {
                    mLocMarker.remove();
                    mLocMarker = null;
                }

                if (mLocCircle != null) {
                    mLocCircle.remove();
                    mLocMarker = null;
                }
                addCircle(location, amapLocation.getAccuracy());// 添加定位精度圆
                addMarker(location);// 添加定位图标
                mSensorHelper.setCurrentMarker(mLocMarker);// 定位图标旋转
                if (!isShowDestination) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,
                            ZOOM));
                }
            } else {
                // mLocCircle.setCenter(location);
                // mLocCircle.setRadius(accuracy);
                // mLocMarker.setPosition(location);
                // mMap.moveCamera(CameraUpdateFactory.changeLatLng(location));
            }

        } else {
            String errText = "定位失败," + amapLocation.getErrorCode() + ": "
                    + amapLocation.getErrorInfo();
            Log.e("AmapErr", errText);
        }
    }


    /**
     * 坐标转位置信息 位置信息转坐标
     */
    public void getAddress(LatLng ll) {
        GeocodeSearch geocoderSearch = new GeocodeSearch(this);
        geocoderSearch
                .setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {

                    @Override
                    public void onRegeocodeSearched(RegeocodeResult arg0,
                                                    int arg1) {
                        if (arg1 == 1000) {
                            if (arg0 != null
                                    && arg0.getRegeocodeAddress() != null
                                    && arg0.getRegeocodeAddress()
                                    .getFormatAddress() != null) {
                                RegeocodeAddress raddress = arg0
                                        .getRegeocodeAddress();
                                String building = raddress.getBuilding();
                                List<BusinessArea> businessAreas = raddress
                                        .getBusinessAreas();
                                String adCode = raddress.getAdCode();
                                String towncode = raddress.getTowncode();
                                String township = raddress.getTownship();
                                String formatAddress = raddress
                                        .getFormatAddress();
                                List<AoiItem> aos = raddress.getAois();
                                List<Crossroad> crossroads = raddress
                                        .getCrossroads();
                                List<PoiItem> pois = raddress.getPois();
                                List<RegeocodeRoad> regeocodeRoads = raddress
                                        .getRoads();

                                destinationAddr = formatAddress;


                                if (pois.size() > 0) {
                                    destinationName = pois.get(0).getTitle();
                                    Log.e("info", "PoiAdName=="
                                            + destinationName);
                                }
                                Log.e("info", "formatAddress==" + formatAddress);
                                mTxtAddrinfo.setText(Html
                                        .fromHtml(destinationName
                                                + "  <font color='#666666'>(点击修改)</font>"));
                                mTxtAddrName.setText(destinationName);
                                mTxtAddrDetail.setText(destinationAddr);

                            }
                        }
                    }

                    @Override
                    public void onGeocodeSearched(GeocodeResult arg0, int arg1) {
                    }

                });
        // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(ll.latitude,
                ll.longitude), 100, GeocodeSearch.AMAP);
        geocoderSearch.getFromLocationAsyn(query);
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
        Bitmap bMap = BitmapFactory.decodeResource(this.getResources(),
                R.mipmap.icon_loc);
        BitmapDescriptor des = BitmapDescriptorFactory.fromBitmap(bMap);
        MarkerOptions options = new MarkerOptions();
        options.icon(des);
        options.anchor(0.5f, 0.5f);
        options.position(latlng);
        mLocMarker = mMap.addMarker(options);
    }


    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {
        mListener = null;
    }

    public void clearMarkers() {
        mMap.clear();
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        LatLng target = cameraPosition.target;
        Log.e("info", "target.longitude==" + target.longitude);
        Log.e("info", "target.latitude==" + target.latitude);
        Log.e("info", "isFromSearch==" + isFromSearch);
        if (isFromSearch) {
            isFromSearch = false;
        } else {
            getAddress(target);
        }
        mDestinationLoc = target;
    }

    @Override
    public void onMapLoaded() {

    }

    private void setUILayout(int type) {
        switch (type) {
            case TYPE_LOCATION:
                // 定位UI
                mViewInput.setVisibility(View.VISIBLE);
                mViewAddr.setVisibility(View.GONE);
                mViewSend.setVisibility(View.GONE);
                mImgDestination.setVisibility(View.GONE);
                break;
            case TYPE_DESTINATION:
                // 目的地展示UI
                mViewInput.setVisibility(View.GONE);
                mViewAddr.setVisibility(View.VISIBLE);
                mViewSend.setVisibility(View.VISIBLE);
                mImgDestination.setVisibility(View.VISIBLE);
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (data != null) {
                setUILayout(TYPE_DESTINATION);
                isShowDestination = true;
                isFromSearch = true;
                String latlng = data.getStringExtra("latlng");
                if (!TextUtils.isEmpty(latlng)) {
                    fromLocName = data.getStringExtra("name");
                    fromLocAddr = data.getStringExtra("address");
                    mTxtAddrinfo.setText(Html.fromHtml(fromLocName
                            + "  <font color='#666666'>(点击修改)</font>"));
                    mTxtAddrName.setText(fromLocName);
                    mTxtAddrDetail.setText(fromLocAddr);
                    PositionEntity pe = new PositionEntity();
                    pe.longitude = Double.parseDouble(latlng.split(",")[1]);
                    pe.latitue = Double.parseDouble(latlng.split(",")[0]);
                    mFirstCarLoc = new LatLng(pe.latitue, pe.longitude);
                    CameraUpdate update = CameraUpdateFactory.newLatLngZoom(
                            mFirstCarLoc, ZOOM);
                    // mMap.animateCamera(update);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            mFirstCarLoc, ZOOM));

                    clearMarkers();
                }
            }
        }
    }

}
