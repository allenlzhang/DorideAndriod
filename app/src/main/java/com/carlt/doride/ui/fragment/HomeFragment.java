package com.carlt.doride.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.weather.LocalDayWeatherForecast;
import com.amap.api.services.weather.LocalWeatherForecast;
import com.amap.api.services.weather.LocalWeatherForecastResult;
import com.amap.api.services.weather.LocalWeatherLiveResult;
import com.amap.api.services.weather.WeatherSearch;
import com.amap.api.services.weather.WeatherSearchQuery;
import com.blankj.utilcode.util.LogUtils;
import com.carlt.chelepie.control.WIFIControl;
import com.carlt.doride.DorideApplication;
import com.carlt.doride.R;
import com.carlt.doride.base.BaseFragment;
import com.carlt.doride.control.CPControl;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.data.PictrueInfo;
import com.carlt.doride.data.home.InformationCategoryInfo;
import com.carlt.doride.data.home.InformationCategoryInfoList;
import com.carlt.doride.data.home.MilesInfo;
import com.carlt.doride.data.home.WeatherInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.protocolparser.DefaultStringParser;
import com.carlt.doride.systemconfig.URLConfig;
import com.carlt.doride.ui.activity.home.InformationCentreActivity;
import com.carlt.doride.ui.activity.home.ReportActivity;
import com.carlt.doride.utils.LoadLocalImageUtil;
import com.carlt.doride.utils.StringUtils;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Marlon on 2018/3/15.
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener, WeatherSearch.OnWeatherSearchListener,WIFIControl.WIFIConnectListener {
    private ImageView      mIvReport;    //行车报告
    private RelativeLayout mRlInformationCentre;    //大乘助手
    private TextView       mTxtDate;  //日期
    private String         currentDate;
    private TextView       mTextView3;    //红点
    private TextView       mTextView4;    //助手消息
    private TextView       mTxtRunningTime;   //行驶时间
    private TextView       mTxtMile; //总里程
    private TextView       mTxtAvgSpeed;  //平均速度
    private TextView       mTxtAvgFuel;   //平均油耗
    private MilesInfo      milesInfo;    //大乘远程 读取里程实体类
    private TextView       mTxtCity;      //城市
    private TextView       mTxtWeather;   //天气
    private TextView       mTxtTemputre;  //温度
    long   mMills   = 0;
    String cityName = null;
    AMapLocationClient mClient;
    AMapLocation       mLocation;
    private ImageView ivHomeBg;

    @Override
    protected View inflateView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        return view;
    }


    @Override
    public void init(View view) {
        getActivateStatus("激活会在24小时内完成。激活未完成前，无法使用APP全部功能，请耐心等候。");
        mIvReport = $ViewByID(R.id.activity_home_iv_report);
        ivHomeBg = $ViewByID(R.id.ivHomeBg);
        mRlInformationCentre = $ViewByID(R.id.activity_home_relative2);
        mTxtDate = $ViewByID(R.id.home_txt_date);
        mTextView3 = $ViewByID(R.id.activity_home_txt3);
        mTextView4 = $ViewByID(R.id.activity_home_txt4);
        mTxtRunningTime = $ViewByID(R.id.layout_report_grid_linear2_txt2);
        mTxtMile = $ViewByID(R.id.layout_report_grid_linear1_txt2);
        mTxtAvgSpeed = $ViewByID(R.id.layout_report_grid_linear3_txt2);
        mTxtAvgFuel = $ViewByID(R.id.layout_report_grid_linear4_txt2);
        mTxtCity = $ViewByID(R.id.home_txt_city);
        mTxtWeather = $ViewByID(R.id.home_txt_weather);
        mTxtTemputre = $ViewByID(R.id.home_txt_temputre);
        mIvReport.setOnClickListener(this);
        mRlInformationCentre.setOnClickListener(this);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = format.format(new Date(System.currentTimeMillis()));
        mTxtDate.setText(currentDate);
        mClient = new AMapLocationClient(DorideApplication.getInstanse().getApplicationContext());
        mClient.setLocationListener(new AMapLocationListener() {

            @Override
            public void onLocationChanged(AMapLocation arg0) {
                if (mLocation == null) {
                    if (null != arg0.getCity()) {
                        mMills = System.currentTimeMillis();
                        if (null != arg0.getCity()
                                && arg0.getCity().length() > 0) {
                            mLocation = arg0;
                            cityName = arg0.getCity();
                            mTxtCity.setText(cityName);
                            Log.e("info", "loadWeather onLocationChanged1==");
                            loadWeather();
                        }
                    }
                } else {
                    if (null != mLocation.getCity() && null != arg0.getCity()
                            && !arg0.getCity().equals(mLocation.getCity())) {
                        mLocation = arg0;
                        // 通知更新天气，城市
                        Log.e("info", "loadWeather onLocationChanged2==");
                        loadWeather();
                    } else {
                        long duration = (System.currentTimeMillis() - mMills)
                                % (1000 * 60 * 5);
                        if (duration == 0) {
                            mMills = System.currentTimeMillis();
                            mLocation = arg0;
                            Log.e("info", "loadWeather onLocationChanged3==");
                            loadWeather();
                        }
                    }
                }
            }
        });

        // 初始化定位参数
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        // 设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        // 设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        // 设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        // 设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        // 设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        // 设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(5000);
        // 给定位客户端对象设置定位参数
        mClient.setLocationOption(mLocationOption);
        mClient.startLocation();
    }


    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            WIFIControl.rigisterWIFIConnectListener(this);
            WIFIControl.DisConnectChelePai();
            loadData();
        }else {
            WIFIControl.unRigisterWIFIConnectListener(this);
        }
    }

    @Override
    public void loadData() {
        upgradeProgram();
        loadingDataUI();
        CPControl.GetInformationCentreInfoListResult(callback);
        CPControl.GetMilesInfoResult(remoteCallback);
        getBgImage();
    }

    private void getBgImage() {
        BaseParser parser1 = new DefaultStringParser(new BaseParser.ResultCallback() {
            @Override
            public void onSuccess(BaseResponseInfo bInfo) {
                LogUtils.e("parser1=======" + bInfo.toString());
                String json = bInfo.getValue().toString();
                Gson gson = new Gson();
                PictrueInfo pictrueInfo = gson.fromJson(json, PictrueInfo.class);
                LoadLocalImageUtil.getInstance().displayFromWeb(pictrueInfo.filePath, ivHomeBg, R.drawable.home_bg);
            }

            @Override
            public void onError(BaseResponseInfo bInfo) {

            }
        });
        HashMap params1 = new HashMap();
        params1.put("app_softtype", "1");
        params1.put("position", "10");
        parser1.executePost(URLConfig.getM_GET_APPSPICS_URL(), params1);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_home_iv_report:  //跳转行车报告
                Intent mIntent = new Intent(getActivity(), ReportActivity.class);
                mIntent.putExtra("c", 0);
                mIntent.putExtra(ReportActivity.DAY_INITIAL, currentDate);
                startActivity(mIntent);
                break;
            case R.id.activity_home_relative2:  //跳转信息中心
                Intent mIntent1 = new Intent(getActivity(), InformationCentreActivity.class);
                getActivity().startActivityForResult(mIntent1, REQUESTCODE);
                break;
        }
    }

    private final static int REQUESTCODE = 1; // 返回的结果码

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESTCODE) {
            Log.e(TAG, "loadData  onActivityResult");
            loadData();
        }
    }

    private WeatherSearchQuery mquery;
    private WeatherSearch      mweathersearch;

    @SuppressLint("NewApi")
    public void loadWeather() {
        Log.e("info", "loadWeather cityName==" + cityName);
        if (null == cityName) {
            return;
        }
        // CPControl.GetWeatherListResult(cityName,
        // getResources().getString(R.string.baidu_map_key),
        // Weather_listener);
        if (mquery == null) {
            mquery = new WeatherSearchQuery(cityName,
                    WeatherSearchQuery.WEATHER_TYPE_FORECAST);
        }
        if (mweathersearch == null) {
            mweathersearch = new WeatherSearch(getActivity());
            mweathersearch.setOnWeatherSearchListener(this);
            mweathersearch.setQuery(mquery);
        }

        mweathersearch.searchWeatherAsyn();
    }

    BaseParser.ResultCallback callback = new BaseParser.ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            Message message = new Message();
            message.what = 0;
            message.obj = bInfo;
            mHandler.sendMessage(message);
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            Message message = new Message();
            message.what = 1;
            message.obj = bInfo;
            mHandler.sendMessage(message);
        }
    };

    BaseParser.ResultCallback remoteCallback = new BaseParser.ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            Message message = new Message();
            message.what = 2;
            message.obj = bInfo;
            mHandler.sendMessage(message);
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            Message message = new Message();
            message.what = 3;
            message.obj = bInfo;
            mHandler.sendMessage(message);
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    loadSuccessUI();
                    actLoadSuccess((BaseResponseInfo) msg.obj);
                    break;
                case 1:
                    loadonErrorUI((BaseResponseInfo) msg.obj);
                    break;
                case 2:
                    loadRemoteSuccess((BaseResponseInfo) msg.obj);
                    break;
                case 3:
                    loadRemoteError((BaseResponseInfo) msg.obj);
                    break;
            }
        }
    };

    @Override
    public void reTryLoadData() {
        loadData();
    }

    private void loadRemoteError(BaseResponseInfo bInfo) {
        //        UUToast.showUUToast(getContext(),bInfo.getInfo());
        mTxtRunningTime.setText("--");
        mTxtMile.setText("--");
        mTxtAvgSpeed.setText("--");
        mTxtAvgFuel.setText("--");
    }

    protected void loadRemoteSuccess(BaseResponseInfo bInfo) {
        milesInfo = (MilesInfo) (bInfo.getValue());
        if (milesInfo != null) {
            if (StringUtils.isEmpty(milesInfo.getRunningTime())) {
                mTxtRunningTime.setText("--");
            } else {
                mTxtRunningTime.setText(milesInfo.getRunningTime());
            }
            if (StringUtils.isEmpty(milesInfo.getObd())) {
                mTxtMile.setText("--");
            } else {
                mTxtMile.setText(milesInfo.getObd());
            }
            if (StringUtils.isEmpty(milesInfo.getAvgSpeed())) {
                mTxtAvgSpeed.setText("--");
            } else {
                mTxtAvgSpeed.setText(milesInfo.getAvgSpeed());
            }
            if (StringUtils.isEmpty(milesInfo.getAvgFuel())) {
                mTxtAvgFuel.setText("--");
            } else {
                mTxtAvgFuel.setText(milesInfo.getAvgFuel());
            }
        }
    }

    protected void actLoadSuccess(BaseResponseInfo binfo) {
        InformationCategoryInfoList infoList = (InformationCategoryInfoList) binfo.getValue();
        if (infoList != null) {
            // 车秘书未读信息条数
            String unreadmessage = "";
            // 车秘书最新消息内容
            String latestmessage = "";

            ArrayList<InformationCategoryInfo> mInformationCategoryInfos = infoList.getmAllList();
            ArrayList<InformationCategoryInfo> unReadMsgInfos = new ArrayList<>();
            ArrayList<InformationCategoryInfo> readMsgInfos = new ArrayList<>();
            for (int i = 0; i < mInformationCategoryInfos.size(); i++) {
                if (Integer.parseInt(mInformationCategoryInfos.get(i).getMsgcount()) > 0) {
                    unReadMsgInfos.add(mInformationCategoryInfos.get(i));
                } else {
                    readMsgInfos.add(mInformationCategoryInfos.get(i));
                }
            }
            if (unReadMsgInfos.size() > 0) {
                lastTime(unReadMsgInfos);
                latestmessage = unReadMsgInfos.get(0).getLastmsg();
                unreadmessage = unReadMsgInfos.get(0).getMsgcount();
            } else {
                lastTime(readMsgInfos);
                latestmessage = readMsgInfos.get(0).getLastmsg();
                unreadmessage = readMsgInfos.get(0).getMsgcount();
            }


            if (unreadmessage != null && unreadmessage.length() > 0) {
                if (unreadmessage.equals("0")) {
                    mTextView3.setVisibility(View.GONE);
                    if (latestmessage != null && latestmessage.length() > 0
                            && !latestmessage.equals("null")) {
                        mTextView4.setText("最后一条：" + latestmessage + "");
                    } else {
                        mTextView4.setText("最后一条：");
                    }
                } else {
                    mTextView3.setVisibility(View.VISIBLE);
                    if (latestmessage != null && latestmessage.length() > 0
                            && !latestmessage.equals("null")) {
                        mTextView4.setText("新消息：" + latestmessage + "");
                    } else {
                        mTextView4.setText("新消息：");
                    }
                }
            } else {
                mTextView3.setVisibility(View.GONE);
                if (latestmessage != null && latestmessage.length() > 0
                        && !latestmessage.equals("null")) {
                    mTextView4.setText(latestmessage + "");
                } else {
                    mTextView4.setText("");
                }
            }
        }
    }

    private void lastTime(ArrayList<InformationCategoryInfo> list) {
        Collections.sort(list, new Comparator<InformationCategoryInfo>() {
            @Override
            public int compare(InformationCategoryInfo o1, InformationCategoryInfo o2) {
                Date date1 = new Date(Long.parseLong(o1.getMsgdate()));
                Date date2 = new Date(Long.parseLong(o2.getMsgdate()));

                return date2.compareTo(date1);
            }
        });
    }

    @Override
    public void onWeatherLiveSearched(LocalWeatherLiveResult localWeatherLiveResult, int i) {

    }

    @Override
    public void onWeatherForecastSearched(LocalWeatherForecastResult localWeatherForecastResult, int i) {
        if (i == 1000) {
            LocalWeatherForecast mLocalWeatherForecast = localWeatherForecastResult
                    .getForecastResult();
            List<LocalDayWeatherForecast> mDayWeatherForecasts = mLocalWeatherForecast
                    .getWeatherForecast();
            LocalDayWeatherForecast mDayWeatherForecast = mDayWeatherForecasts
                    .get(0);
            String date = mDayWeatherForecast.getDate();
            String dayWeather = mDayWeatherForecast.getDayWeather();
            String dayTemp = mDayWeatherForecast.getDayTemp();
            String nightWeather = mDayWeatherForecast.getNightWeather();
            String nightTemp = mDayWeatherForecast.getNightTemp();
            WeatherInfo mWeatherInfo = new WeatherInfo();
            mWeatherInfo.setTemperature(nightTemp + "~" + dayTemp + "℃");
            if (nightWeather.equals(dayWeather)) {
                mWeatherInfo.setWeather(dayWeather);
            } else {
                mWeatherInfo.setWeather(dayWeather + "转" + nightWeather);
            }

            weatherLoaded(mWeatherInfo);
        } else {
            Log.e("info", "loadWeather onWeatherForecastSearched==");
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.e("info", "loadWeather handleMessage==");
                    loadWeather();
                }
            }, 5000);
        }
    }

    public void weatherLoaded(Object obj) {
        WeatherInfo info = (WeatherInfo) obj;
        mTxtWeather.setText(info.getWeather());
        mTxtTemputre.setText(info.getTemperature());
    }

    @Override
    public void onWIFIChange(int action) {
        mHandler.sendEmptyMessage(action);
    }
}
