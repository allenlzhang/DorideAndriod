
package com.carlt.sesame.ui.activity.career;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;
import com.carlt.sesame.R;
import com.carlt.sesame.ui.activity.base.BaseActivity;

import java.util.ArrayList;
//import com.baidu.location.BDLocation;
//import com.baidu.location.BDLocationListener;
//import com.baidu.location.LocationClient;
//import com.baidu.location.LocationClientOption;
//import com.baidu.location.LocationClientOption.LocationMode;

public class LocationActivity extends BaseActivity /*implements BDLocationListener */{
    private ImageView back;// 头部返回键

    private TextView title;// 标题文字

    private TextView txtRight;// 头部右侧文字

    // 定位相关
//    private LocationClient mLocClient;

//    private MapView mMapView;
//
//    private BaiduMap mBaiduMap;

    
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 挑战页面使屏幕常亮
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_location);
        initTitle();
        initMapView();
    }

    /**
     * 定位SDK监听函数
     */
    boolean isFirstLoc = true;// 是否首次定位

    private ArrayList<LatLng> mPointList = new ArrayList<LatLng>();

//    @Override
//    public void onReceiveLocation(BDLocation location) {
//        // map view 销毁后不在处理新接收的位置
//        if (location == null || mMapView == null) {
//            return;
//        }
//        LatLng mLatLng = new LatLng(location.getLatitude(),
//                location.getLongitude());
//
//        if (isFirstLoc) {
//            isFirstLoc = false;
//            Builder mBuilder = new MyLocationData.Builder();
//            mBuilder.accuracy(location.getRadius());
//            mBuilder.latitude(location.getLatitude());
//            mBuilder.longitude(location.getLongitude());
//            MyLocationData locData = mBuilder.build();
//            mBaiduMap.setMyLocationData(locData);
//            MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(mLatLng,
//                    18);
//            mBaiduMap.animateMapStatus(u);
//
//            mPointList.add(mLatLng);
//            return;
//        }
//        if (location.getLocType() != BDLocation.TypeGpsLocation) {
//            return;
//        }
//        mPointList.add(mLatLng);
//
//        Builder mBuilder = new MyLocationData.Builder();
//        mBuilder.accuracy(location.getRadius());
//        mBuilder.direction(location.getDirection());
//        mBuilder.latitude(location.getLatitude());
//        mBuilder.longitude(location.getLongitude());
//        MyLocationData locData = mBuilder.build();
//        mBaiduMap.setMyLocationData(locData);
//    }
    

    private void initTitle() {

        back = (ImageView)findViewById(R.id.head_back_img1);
        title = (TextView)findViewById(R.id.head_back_txt1);
        txtRight = (TextView)findViewById(R.id.head_back_txt2);

        back.setImageResource(R.drawable.arrow_back);
        title.setText("车辆定位");
        txtRight.setVisibility(View.VISIBLE);
        txtRight.setText("");

        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initMapView() {
        // 地图初始化
//        mMapView = (MapView)findViewById(R.id.location_mapview);
//        mBaiduMap = mMapView.getMap();
//        // 传入null则，默认图标
//        BitmapDescriptor marker = BitmapDescriptorFactory.fromResource(R.drawable.location_marker);
//        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(LocationMode.FOLLOWING,
//                true, marker));
//        // 开启定位图层
//        mBaiduMap.setMyLocationEnabled(true);
//        // 定位初始化
//        mLocClient = new LocationClient(this);
//        mLocClient.registerLocationListener(this);
//        LocationClientOption option = new LocationClientOption();
//        option.setOpenGps(true);// 打开gps
//        option.setCoorType("bd09ll"); // 设置坐标类型
//        option.setScanSpan(1000);
//        mLocClient.setLocOption(option);
//        mLocClient.start();
//
//        // 隐藏缩放控件
//        int childCount = mMapView.getChildCount();
//        View zoom = null;
//        for (int i = 0; i < childCount; i++) {
//            View child = mMapView.getChildAt(i);
//            if (child instanceof ZoomControls) {
//                zoom = child;
//                break;
//            }
//
//        }
//        zoom.setVisibility(View.GONE);
//        // 隐藏比例尺控件
//        int count = mMapView.getChildCount();
//        View scale = null;
//        for (int i = 0; i < count; i++) {
//            View child = mMapView.getChildAt(i);
//            if (child instanceof ZoomControls) {
//                scale = child;
//                break;
//            }
//        }
//        scale.setVisibility(View.GONE);
    }
    
    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
//        mMapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 退出时销毁定位
//        mLocClient.stop();
        // 关闭定位图层
//        mBaiduMap.setMyLocationEnabled(false);
//        mMapView.onDestroy();
//        mMapView = null;
    }
}
