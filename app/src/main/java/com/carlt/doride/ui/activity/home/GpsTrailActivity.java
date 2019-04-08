package com.carlt.doride.ui.activity.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.carlt.doride.R;
import com.carlt.doride.base.LoadingActivity;
import com.carlt.doride.control.CPControl;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.data.ReportGpsInfo;
import com.carlt.doride.data.home.ReportDayLogInfo;
import com.carlt.doride.preference.UseInfoLocal;
import com.carlt.doride.utils.FileUtil;
import com.carlt.doride.utils.LocalConfig;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 轨迹回放页面
 * @author Administrator
 */
public class GpsTrailActivity extends LoadingActivity implements
        AMap.OnMapLoadedListener, OnClickListener {

    private ReportDayLogInfo mCarLogInfo;// 行车日志信息

    public final static String CAR_LOG_INFO = "car_log_info";

    public final static int LINE_WIDTH = 16;// 线条宽度

    public final static int COLOR = Color.parseColor("#ff3FC0EA");// 线条颜色

    private MapView mMapView;

    private AMap mMap;

    LatLngBounds.Builder mBuilder = null;

    PolylineOptions polylineOptions = new PolylineOptions();

    List<PolylineOptions> listLines = Collections
            .synchronizedList(new ArrayList<PolylineOptions>());

    LatLng mStart = null;

    LatLng mEnd = null;

    View mBottom;

    RelativeLayout mRoot;

    TextView txtMileage;// 行驶里程

    TextView txtDate;// 日期

    TextView txtOilAll;

    TextView txtOilAvg;

    TextView txtSpeedMax;

    TextView txtSpeedAvg;

    TextView zoomIn;

    TextView zoomOut;

    View infoLay;

    TextView infoTxt;

    private CoordinateConverter mConverter;

    String dayInitialValue = "";

    String gps_path = "";
    private DrawLine mDl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_trail);
        try {
            mCarLogInfo = (ReportDayLogInfo) getIntent().getSerializableExtra(
                    CAR_LOG_INFO);
            dayInitialValue = getIntent().getStringExtra(
                    ReportActivity.DAY_INITIAL);
        } catch (Exception e) {
            //报错
        }

        initTitle("行车轨迹");
        init(savedInstanceState);
        initData();
    }


    @Override
    public void reTryLoadData() {
        super.reTryLoadData();
        initData();
    }

    private void initData() {
        if (isMapLoaded) {
            if (mCarLogInfo != null) {
                String gpsStartTime = mCarLogInfo.getGpsStartTime();
                String gpsStopTime = mCarLogInfo.getGpsStopTime();
                String runSn = mCarLogInfo.getRunSn();
                String path = LocalConfig.mTracksSavePath_SD
                        + UseInfoLocal.getUseInfo().getAccount();
                String name = gpsStartTime + "-" + gpsStopTime + ".gps";
                gps_path = path + "/" + name;
                if (FileUtil.openOrCreatDir(path)) {
                    File file = new File(path + "/" + name);
                    if (file.exists()) {
                        loadDataSuccess(gps_path);
                        return;
                    }
                }

                CPControl.GetReportGpsResult(mCallback, gpsStartTime, gpsStopTime, runSn);
            }
        } else {
            //
        }
    }

    private void init(Bundle savedInstanceState) {
        mMapView = (MapView) findViewById(R.id.gps_trail_mapView);
        mBottom = findViewById(R.id.gps_trail_lay_bottom);
        mRoot = (RelativeLayout) findViewById(R.id.gps_view_root);

        txtMileage = (TextView) findViewById(R.id.gps_trail_txt_miles);
        txtDate = (TextView) findViewById(R.id.gps_trail_txt_date);

        txtOilAll = (TextView) findViewById(R.id.gps_trail_txt_oilall);
        txtOilAvg = (TextView) findViewById(R.id.gps_trail_txt_oilavg);
        txtSpeedMax = (TextView) findViewById(R.id.gps_trail_txt_speedmax);
        txtSpeedAvg = (TextView) findViewById(R.id.gps_trail_txt_speedavg);

        infoLay = findViewById(R.id.gps_taril_lay_info);
        infoTxt = (TextView) findViewById(R.id.gps_trail_txt_infoText);

        zoomIn = (TextView) findViewById(R.id.gps_zoomin);
        zoomOut = (TextView) findViewById(R.id.gps_zoomout);

        zoomIn.setOnClickListener(this);
        zoomOut.setOnClickListener(this);
        txtMileage.setText(mCarLogInfo.getStarttime() + "至" + mCarLogInfo.getStopTime() + "(" + mCarLogInfo.getTime() + ") " + mCarLogInfo.getMiles());
        //		txtDate.setText(dayInitialValue);
        txtOilAll.setText("总油耗：" + mCarLogInfo.getFuel());
        txtOilAvg.setText("平均油耗：" + mCarLogInfo.getAvgfuel());
        txtSpeedAvg.setText("平均速度：" + mCarLogInfo.getAvgspeed());
        txtSpeedMax.setText("最高速度：" + mCarLogInfo.getMaxspeed());

        mMapView.onCreate(savedInstanceState);
        mMap = mMapView.getMap();
        mMap.setOnMapLoadedListener(this);
        //		mMap.setMapType(AMap.MAP_TYPE_NIGHT);
        UiSettings uiSeting = mMap.getUiSettings();
        uiSeting.setMyLocationButtonEnabled(false);
        uiSeting.setScaleControlsEnabled(true);
        uiSeting.setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);
        // uiSeting.setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);
        uiSeting.setZoomControlsEnabled(false);
        uiSeting.setZoomGesturesEnabled(true);
        uiSeting.setRotateGesturesEnabled(true);
        uiSeting.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_LEFT);
        mConverter = new CoordinateConverter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        if (mDl != null) {
            mDl.cancel(true);
        }
        mHandler.removeCallbacksAndMessages(this);
    }


    class DrawLine extends AsyncTask<Object, Object, Object> {

        public ArrayList<ReportGpsInfo> mGpsInfos;
        public String                   filePath;

        /**
         *
         */
        public DrawLine() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Object doInBackground(Object... arg0) {
            mConverter.from(CoordinateConverter.CoordType.GPS);
            if ((mGpsInfos == null || mGpsInfos.size() < 1) && filePath == null) {
                return null;
            }

            if (filePath != null) {
                // path 不为空
                FileInputStream fin = null;
                try {
                    fin = new FileInputStream(filePath);
                    int count = fin.available();
                    byte[] content = new byte[count];
                    fin.read(content);
                    try {
                        Gson gs = new Gson();
                        Type listType = new TypeToken<ArrayList<ReportGpsInfo>>() {
                        }.getType();
                        ArrayList<ReportGpsInfo> data = gs.fromJson(new String(
                                content), listType);
                        int size = data.size();
                        ReportGpsInfo rtemp = data.get(0);
                        mConverter.coord(new LatLng(rtemp.getLatitude(), rtemp
                                .getLongitude()));
                        mStart = mConverter.convert();
                        rtemp = data.get(size - 1);
                        mConverter.coord(new LatLng(rtemp.getLatitude(), rtemp
                                .getLongitude()));
                        mEnd = mConverter.convert();

                        for (int i = 0; i < size; i++) {
                            if (mBuilder == null) {
                                mBuilder = LatLngBounds.builder();
                            }
                            ReportGpsInfo rinfo = data.get(i);
                            long time1 = System.currentTimeMillis();
                            LatLng latLng = new LatLng(rinfo.getLatitude(),
                                    rinfo.getLongitude());
                            mConverter.coord(latLng);
                            latLng = mConverter.convert();
                            polylineOptions.add(latLng);
                            mBuilder.include(latLng);
                            Log.e("DITU转换坐标：", System.currentTimeMillis()
                                    - time1 + " ms");
                        }

                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                        FileUtil.deleteFile(new File(filePath));
                        return null;
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return null;
                } catch (IOException e) {
                    e.printStackTrace();
                    FileUtil.deleteFile(new File(filePath));
                    return null;
                } finally {
                    try {
                        fin.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                long time = System.currentTimeMillis();
                int size = mGpsInfos.size();
                ReportGpsInfo rtemp = mGpsInfos.get(0);
                LatLng latLngTmp = new LatLng(rtemp.getLatitude(),
                        rtemp.getLongitude());
                mConverter.coord(latLngTmp);
                mStart = mConverter.convert();
                rtemp = mGpsInfos.get(size - 1);
                latLngTmp = new LatLng(rtemp.getLatitude(),
                        rtemp.getLongitude());
                mConverter.coord(latLngTmp);
                mEnd = mConverter.convert();

                for (int i = 0; i < size; i++) {
                    if (mBuilder == null) {
                        mBuilder = LatLngBounds.builder();
                    }
                    ReportGpsInfo rinfo = mGpsInfos.get(i);
                    long time1 = System.currentTimeMillis();
                    LatLng latLng = new LatLng(rinfo.getLatitude(),
                            rinfo.getLongitude());
                    mConverter.coord(latLng);
                    latLng = mConverter.convert();
                    polylineOptions.add(latLng);
                    mBuilder.include(latLng);
                    Log.e("DITU转换坐标：", System.currentTimeMillis() - time1
                            + " ms");
                }

                // 保存轨迹文件
                Gson gs = new Gson();
                String ss = gs.toJson(mGpsInfos);
                if (gps_path != null && gps_path.length() > 0 && ss != null
                        && ss.length() > 0) {
                    try {
                        FileOutputStream fout = new FileOutputStream(gps_path);
                        fout.write(ss.getBytes());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                Log.e("DITU地图添加创建线对象：", System.currentTimeMillis() - time
                        + "ms");
            }

            return null;
        }

        protected View getMyView(String pm_val, int resId) {
            View view = getLayoutInflater().inflate(R.layout.mymarker, null);
            TextView tv_val = (TextView) view.findViewById(R.id.marker_tv_val);
            ImageView iv_val = view.findViewById(R.id.marker_iv_val);
            tv_val.setText(pm_val);
            iv_val.setImageResource(resId);
            return view;
        }


        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            mHandler.removeMessages(0);
            if (polylineOptions.getPoints().size() < 1 || mBuilder == null) {
                BaseResponseInfo mBaseResponseInfo = new BaseResponseInfo();
                mBaseResponseInfo.setInfo("没有轨迹数据！");
                infoTxt.setText("没有轨迹数据 !");
                loadonErrorUI(mBaseResponseInfo);
                return;
            }

            infoLay.setVisibility(View.GONE);
            int height = mBottom.getMeasuredHeight();
            // BitmapDescriptor descriptorStart1 = BitmapDescriptorFactory
            // .fromResource(R.drawable.gps_line_new);
            polylineOptions.color(COLOR);
            polylineOptions.width(LINE_WIDTH);
            // polylineOptions.setCustomTexture(descriptorStart1);
            polylineOptions.useGradient(false);
            polylineOptions.setUseTexture(false);
            polylineOptions.setDottedLine(false);
            polylineOptions.visible(true);
            polylineOptions.setUseTexture(true);

            long time = System.currentTimeMillis();
            mMap.addPolyline(polylineOptions);
            Log.e("DITU地图添加 线条耗时", System.currentTimeMillis() - time + "ms");

            MarkerOptions startOpt = new MarkerOptions();
            //TODO 行车轨迹 起点和终点icon
            BitmapDescriptor descriptorStart =
                    BitmapDescriptorFactory.fromView(getMyView(mCarLogInfo.getStarttime(), R.drawable.gps_icon_start));
            startOpt.icon(descriptorStart);
            startOpt.anchor(0.5f, 0.6f);
            startOpt.position(mStart);
            DisplayMetrics dm = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(dm);
            Marker marker1 = mMap.addMarker(startOpt);
            marker1.setDraggable(false);

            //			TextOptions options1 = new TextOptions();
            //			options1.text(mCarLogInfo.getStarttime());
            //			options1.fontSize((int) (dm.scaledDensity * 12));
            //			options1.backgroundColor(Color.parseColor("#f0f0f0"));
            //			options1.position(mStart);
            //			mMap.addText(options1);

            MarkerOptions endOpt = new MarkerOptions();
            BitmapDescriptor descriptorEnd = BitmapDescriptorFactory
                    .fromView(getMyView(mCarLogInfo.getStopTime(), R.drawable.gps_icon_stop));
            endOpt.icon(descriptorEnd);
            endOpt.anchor(0.5f, 0.6f);
            endOpt.position(mEnd);
            mMap.addMarker(endOpt);
            Marker marker2 = mMap.addMarker(endOpt);
            marker2.setDraggable(false);

            //			TextOptions options2 = new TextOptions();
            //			options2.text(mCarLogInfo.getStopTime());
            //			options2.fontSize((int) (dm.scaledDensity * 12));
            //			options2.backgroundColor(Color.parseColor("#f0f0f0"));
            //			options2.position(mEnd);
            //
            //			mMap.addText(options2);

            LatLngBounds bounds = null;
            try {
                bounds = mBuilder.build();
            } catch (Exception e) {
                e.printStackTrace();
                mBuilder = LatLngBounds.builder();
                mBuilder.include(mStart);
                mBuilder.include(mEnd);
                bounds = mBuilder.build();
            }
            float[] results = new float[4];
            AMapLocation.distanceBetween(bounds.northeast.latitude,
                    bounds.northeast.longitude, bounds.southwest.latitude,
                    bounds.southwest.longitude, results);
            float distance = results[0];
            if (distance < 10) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(bounds.northeast.latitude,
                                bounds.northeast.longitude), 15));
            } else {
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds,
                        height));
            }
        }

    }


    @Override
    public void loadDataSuccess(Object data) {
        super.loadDataSuccess(data);
        infoLay.setVisibility(View.VISIBLE);
        mHandler.sendEmptyMessage(0);
        mDl = new DrawLine();
        if (data instanceof String) {
            mDl.filePath = data.toString();
        } else {
            mDl.mGpsInfos = (ArrayList<ReportGpsInfo>) ((BaseResponseInfo) data).getValue();
        }
        mDl.execute();
    }

    @Override
    public void loadDataError(Object bInfo) {
        super.loadDataError(bInfo);
    }

    boolean isMapLoaded = false;

    @Override
    public void onMapLoaded() {
        if (mCarLogInfo != null) {
            String gpsStartTime = mCarLogInfo.getGpsStartTime();
            String gpsStopTime = mCarLogInfo.getGpsStopTime();
            String runSn = mCarLogInfo.getRunSn();
            String path = LocalConfig.mTracksSavePath_SD
                    + UseInfoLocal.getUseInfo().getAccount();
            String name = gpsStartTime + "-" + gpsStopTime + ".gps";
            gps_path = path + "/" + name;
            if (FileUtil.openOrCreatDir(path)) {
                File file = new File(path + "/" + name);
                if (file.exists()) {
                    loadDataSuccess(gps_path);
                    return;
                }
            }
            CPControl.GetReportGpsResult(mCallback, gpsStartTime, gpsStopTime, runSn);
        }
        isMapLoaded = true;
    }

    @Override
    public void onClick(View arg0) {
        if (arg0.equals(zoomIn)) {
            CameraUpdate zoomInUD = CameraUpdateFactory.zoomIn();
            mMap.animateCamera(zoomInUD);
        } else if (arg0.equals(zoomOut)) {
            CameraUpdate zoomOutUD = CameraUpdateFactory.zoomOut();
            mMap.animateCamera(zoomOutUD);
        }
    }

    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler() {
        int i = 0;

        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0) {
                if (i % 4 == 0) {
                    infoTxt.setText("正在绘制轨迹请等待");
                } else if (i % 4 == 1) {
                    infoTxt.setText("正在绘制轨迹请等待。");
                } else if (i % 4 == 2) {
                    infoTxt.setText("正在绘制轨迹请等待。。");
                } else if (i % 4 == 3) {
                    infoTxt.setText("正在绘制轨迹请等待。。。");
                }

                i++;
                mHandler.sendEmptyMessageDelayed(0, 500);
            }
        }

        ;
    };

}
