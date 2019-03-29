package com.carlt.sesame.ui.activity.career;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.weather.LocalDayWeatherForecast;
import com.amap.api.services.weather.LocalWeatherForecast;
import com.amap.api.services.weather.LocalWeatherForecastResult;
import com.amap.api.services.weather.LocalWeatherLiveResult;
import com.amap.api.services.weather.WeatherSearch;
import com.amap.api.services.weather.WeatherSearch.OnWeatherSearchListener;
import com.amap.api.services.weather.WeatherSearchQuery;
import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.carlt.doride.R;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.data.PictrueInfo;
import com.carlt.doride.http.retrofitnet.model.OtherInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.protocolparser.DefaultStringParser;
import com.carlt.sesame.control.ActivityControl;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.SesameLoginInfo;
import com.carlt.sesame.data.career.CareerInfo;
import com.carlt.sesame.data.career.ChallengeInfo;
import com.carlt.sesame.data.career.WeatherInfo;
import com.carlt.sesame.http.AsyncImageLoader;
import com.carlt.sesame.ui.activity.base.LoadingActivityWithTitle;
import com.carlt.sesame.ui.activity.career.report.ReportActivity;
import com.carlt.sesame.utility.Log;
import com.carlt.sesame.utility.MyTimeUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 生涯主页面
 * @author daisy
 */
public class CareerMainActivity extends LoadingActivityWithTitle implements
        OnClickListener, OnWeatherSearchListener {

    private TextView mTextView2; // 车秘书姓名

    private TextView mTextView3; // 车秘书推送信息条数

    private TextView mTextView4; // 车秘书推送信息内容

    private TextView mTextReportGrid1; // 总油耗

    private TextView mTextReportGrid2; // 总油耗描述

    private TextView mTextReportGrid3; // 总里程

    private TextView mTextReportGrid4; // 总里程描述

    private TextView mTextReportGrid5; // 最高时速

    private TextView mTextReportGrid6; // 最高时速描述

    private TextView mTextReportGrid7; // 百公里电耗

    private TextView mTextReportGrid8; // 百公里电耗描述

    private TextView mTextTime;// 时间

    private TextView     mTextWeather;// 天气
    private TextView     mTextCity;// 天气
    private TextView     mTextTemputre;// 天气
    private LinearLayout lltop;
    private View         mViewReport;// 行车日志layout

    private RelativeLayout mRelativeLayout2;// 车秘书

    private View mLinearReportGrid1;// 总油耗

    private View mLinearReportGrid2;// 总里程

    private View mLinearReportGrid3;// 最高时速

    private View mLinearReportGrid4;// 平均油耗

    private ImageView mImageView1;// 车秘书头像

    private CareerInfo mCareerInfo;// 生涯主页面数据结构

    private ArrayList<ChallengeInfo> mArrayList;

    private AsyncImageLoader mAsyncImageLoader;// 异步加载图片

    AMapLocationClient mClient;
    AMapLocation       mLocation;

    long   mMills      = 0;
    String cityName    = null;
    String weatherInfo = "--";

    private int count = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_career_main);

        mAsyncImageLoader = AsyncImageLoader.getInstance();
        setTitleView(R.layout.head_home);
        init();
        LoadData();
    }

    private void init() {
        mTextView2 = (TextView) findViewById(R.id.activity_career_main_txt2);
        mTextView3 = (TextView) findViewById(R.id.activity_career_main_txt3);
        mTextView4 = (TextView) findViewById(R.id.activity_career_main_txt4);

        mTextTime = (TextView) findViewById(R.id.domay_txt_topDate);
        lltop = findViewById(R.id.lltop);
        mTextCity = (TextView) findViewById(R.id.domay_txt_topCity);
        mTextTemputre = (TextView) findViewById(R.id.domay_txt_topTemputre);

        mTextTime.setText(MyTimeUtil.getDateFormat3());
        mTextWeather = (TextView) findViewById(R.id.domay_txt_topWeather);

        mViewReport = findViewById(R.id.domay_txt_carLog);
        mRelativeLayout2 = (RelativeLayout) findViewById(R.id.activity_career_main_relative2);

        mImageView1 = (ImageView) findViewById(R.id.activity_career_main_img1);

        mViewReport.setOnClickListener(this);
        mRelativeLayout2.setOnClickListener(this);


        getLoction();
    }
    private void getBgImage() {
        BaseParser parser1 = new DefaultStringParser(new BaseParser.ResultCallback() {
            @Override
            public void onSuccess(BaseResponseInfo bInfo) {
                LogUtils.e("parser1=======" + bInfo.toString());
                String json = bInfo.getValue().toString();
                Gson gson = new Gson();
                PictrueInfo pictrueInfo = gson.fromJson(json, PictrueInfo.class);
//                LoadLocalImageUtil.getInstance().displayFromWeb(pictrueInfo.filePath, ivHomeBg, R.drawable.home_bg);
                Glide.with(CareerMainActivity.this).load(pictrueInfo.filePath).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnmation) {
                        Drawable drawable = new BitmapDrawable(resource);
                        lltop.setBackgroundDrawable(drawable);
                    }
                });
            }

            @Override
            public void onError(BaseResponseInfo bInfo) {

            }
        });
        HashMap params1 = new HashMap();
        params1.put("app_softtype", "1");
        params1.put("position", "10");
        parser1.executePost(com.carlt.sesame.systemconfig.URLConfig.getM_GET_APPSPICS_URL(), params1);
    }
    private void getLoction() {
        mArrayList = new ArrayList<ChallengeInfo>();
        mClient = new AMapLocationClient(getApplicationContext());
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
                            OtherInfo.getInstance().setCityName(cityName);
                            mTextCity.setText(cityName);
                            loadWeather();
                        }
                    }
                } else {
                    if (null != mLocation.getCity() && null != arg0.getCity()
                            && !arg0.getCity().equals(mLocation.getCity())) {
                        mLocation = arg0;
                        // 通知更新天气，城市
                        loadWeather();
                    } else {
                        long duration = (System.currentTimeMillis() - mMills)
                                % (1000 * 60 * 5);
                        if (duration == 0) {
                            mMills = System.currentTimeMillis();
                            mLocation = arg0;
                            loadWeather();
                        }
                    }
                }
            }
        });

        // 初始化定位参数
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        // 设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
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

    private void initReporGrid() {
        mTextReportGrid1 = (TextView) findViewById(R.id.layout_report_grid_linear1_txt1);
        mTextReportGrid2 = (TextView) findViewById(R.id.layout_report_grid_linear1_txt2);
        mTextReportGrid3 = (TextView) findViewById(R.id.layout_report_grid_linear2_txt1);
        mTextReportGrid4 = (TextView) findViewById(R.id.layout_report_grid_linear2_txt2);
        mTextReportGrid5 = (TextView) findViewById(R.id.layout_report_grid_linear3_txt1);
        mTextReportGrid6 = (TextView) findViewById(R.id.layout_report_grid_linear3_txt2);
        mTextReportGrid7 = (TextView) findViewById(R.id.layout_report_grid_linear4_txt1);
        mTextReportGrid8 = (TextView) findViewById(R.id.layout_report_grid_linear4_txt2);

        mLinearReportGrid1 = findViewById(R.id.layout_report_grid_linear1);
        mLinearReportGrid2 = findViewById(R.id.layout_report_grid_linear2);
        mLinearReportGrid3 = findViewById(R.id.layout_report_grid_linear3);
        mLinearReportGrid4 = findViewById(R.id.layout_report_grid_linear4);

        mLinearReportGrid1.setClickable(false);
        mLinearReportGrid2.setClickable(false);
        mLinearReportGrid3.setClickable(false);
        mLinearReportGrid4.setClickable(false);

        if (mCareerInfo != null) {

            // 行驶时间
            String s1 = mCareerInfo.getSumtime();
            mTextReportGrid2.setText(s1);
            // // 总里程
            s1 = mCareerInfo.getSummiles();
            mTextReportGrid4.setText(s1);
            // // 平均速度
            s1 = mCareerInfo.getAvgspeed();
            mTextReportGrid6.setText(s1);
            // // 平均油耗
            s1 = mCareerInfo.getAvgfuel();
            mTextReportGrid8.setText(s1);

            String s2;
            // // 总油耗描述
            s2 = mCareerInfo.getSumtimedesc();
            if (s2 != null && s2.length() > 0) {
                mTextReportGrid1.setText(s2);
            }
            //
            // // 总里程描述
            s2 = mCareerInfo.getSummilesdesc();
            if (s2 != null && s2.length() > 0) {
                mTextReportGrid3.setText(s2);
            }
            //
            // // 平均速度描述
            s2 = mCareerInfo.getAvgspeeddesc();
            if (s2 != null && s2.length() > 0) {
                mTextReportGrid5.setText(s2);
            }
            //
            // // 平均油耗描述
            s2 = mCareerInfo.getAvgfueldesc();
            if (s2 != null && s2.length() > 0) {
                mTextReportGrid7.setText(s2);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_career_main_relative2:
                // 跳转至车秘书提醒分类列表页面
                Intent mIntent2 = new Intent(this, SecretaryActivityNew.class);
                startActivity(mIntent2);
                break;
            case R.id.domay_txt_carLog: // 行车日志
                // 跳转至日报页面
                Intent mIntent9 = new Intent(CareerMainActivity.this,
                        ReportActivity.class);
                mIntent9.putExtra("c", 2);
                startActivity(mIntent9);
                break;
        }

    }

    //	private OnItemClickListener mItemClickListener = new OnItemClickListener() {
    //
    //		@Override
    //		public void onItemClick(AdapterView<?> parent, View view, int position,
    //				long id) {
    //			int size = mArrayList.size();
    //			if (position >= mArrayList.size()) {
    //				position = position % size;
    //			}
    //			ChallengeInfo mChallengeInfo = mArrayList.get(position);
    //			if (mChallengeInfo != null) {
    //				String status = mChallengeInfo.getStatus();
    //				if (status.equals(ChallengeInfo.STATUS_FINISHED)) {
    //					// 1.已完成挑战，跳转至挑战结果（最佳纪录）
    //					Intent mIntent1 = new Intent(CareerMainActivity.this,
    //							ChallengeHistoryBestActivity.class);
    //					mIntent1.putExtra("ChallengeInfo", mChallengeInfo);
    //					startActivity(mIntent1);
    //
    //				} else if (status.equals(ChallengeInfo.STATUS_UNFINISHED)) {
    //					// 2.未完成挑战，跳转至挑战页面
    //					Intent mIntent2 = new Intent(CareerMainActivity.this,
    //							ChallengeMapActivity.class);
    //					mIntent2.putExtra("ChallengeInfo", mChallengeInfo);
    //					startActivity(mIntent2);
    //				} else if (status.equals(ChallengeInfo.STATUS_UNLOCKED)) {
    //					// 3.未解锁挑战，不可点击
    //
    //				}
    //			}
    //
    //		}
    //	};

    @Override
    protected void LoadSuccess(Object data) {
        mCareerInfo = (CareerInfo) data;
        initReporGrid();
        if (mCareerInfo != null) {
            // 登录天数
            String lefttime = mCareerInfo.getLefttime();
            // if (lefttime != null && lefttime.length() > 0) {
            // titleTxt1.setText(lefttime);
            // titleTxt1.setTypeface(GetTypeFace.typefaceBold(this.getResources()));
            // } else {
            // titleTxt1.setText("");
            // }

            // 用户姓名
            // titleTxt2.setText("嗨，" + LoginInfo.realname);

            // 挑战信息
            mArrayList = mCareerInfo.getmChallengeInfoList();

            // 驾驶证等级
            String licenceLevel = mCareerInfo.getLicenceLevel();
            if (licenceLevel == null || licenceLevel.length() < 1) {
                licenceLevel = "0";
            }
            SpannableStringBuilder spannable = new SpannableStringBuilder(
                    getResources().getString(R.string.career_main_diverlevel));
            spannable.append(licenceLevel);
            spannable.setSpan(new AbsoluteSizeSpan((int) getResources()
                            .getDimension(R.dimen.text_size_middle)),
                    spannable.length() - licenceLevel.length(), spannable
                            .length(),
                    SpannableStringBuilder.SPAN_INCLUSIVE_INCLUSIVE);
            // mTextView1.setText(spannable);

            String total = mCareerInfo.getChallengeTotal();
            String finished = mCareerInfo.getChallengeFinished();

            SpannableStringBuilder mStringBuilder = new SpannableStringBuilder();
            mStringBuilder.append("共有");
            mStringBuilder.append(total);
            mStringBuilder.append("关,已完成");
            mStringBuilder.append(finished);
            mStringBuilder.append("关");

            int index2 = mStringBuilder.length() - 1;
            int index1 = index2 - finished.length();

            // mStringBuilder.setSpan(new ForegroundColorSpan(Color.rgb(241, 61,
            // 109)), index1, index2, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            // mTextTime.setText(mStringBuilder);

            String daypoint = mCareerInfo.getDaypoint();
            if (daypoint != null && daypoint.length() > 0) {

            } else {
                daypoint = "0";
            }

            mStringBuilder = new SpannableStringBuilder("上次驾驶得分：");
            mStringBuilder.append(daypoint);
            mStringBuilder.append("分");
            index2 = mStringBuilder.length() - 1;
            index1 = index2 - daypoint.length();

            mStringBuilder.setSpan(
                    new ForegroundColorSpan(Color.rgb(62, 192, 234)), index1,
                    index2, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            // mTextScore.setText(mStringBuilder);

            // 驾驶证图腾
            String licenceImg = mCareerInfo.getLicenceImg();
            if (licenceImg != null && licenceImg.length() > 0) {
                if (mAsyncImageLoader.getBitmapByUrl(licenceImg) != null) {
                    // mImageView.setImageBitmap(mAsyncImageLoader.getBitmapByUrl(licenceImg));
                }
            } else {
                // mImageView.setImageResource(R.drawable.level_default_s);
            }

            // 驾驶证升级进度
            int licencePercent = mCareerInfo.getLicencePercent();
            if (licencePercent < 1) {
                licencePercent = 0;
            }
            // mProgressBar.setProgress(licencePercent);

            // 车秘书未读信息条数
            String unreadmessage = mCareerInfo.getUnreadmessage();
            // 车秘书最新消息内容
            String latestmessage = mCareerInfo.getLatestmessage();

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

        // 车秘书姓名
        // String secretaryName = LoginInfo.getSecretaryName();
        // if (secretaryName != null) {
        // mTextView2.setText(secretaryName + "：");
        // }

        super.LoadSuccess(data);
    }

    public void weatherLoaded(Object obj) {
        WeatherInfo info = (WeatherInfo) obj;
        mTextWeather.setText(info.getWeather());
        mTextTemputre.setText(info.getTemperature());
    }

    private WeatherSearchQuery mquery;
    private WeatherSearch      mweathersearch;

    public void loadWeather() {
        //		Log.e("info", "cityName==" + cityName);
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
            mweathersearch = new WeatherSearch(this);
            mweathersearch.setOnWeatherSearchListener(this);
            mweathersearch.setQuery(mquery);
        }

        mweathersearch.searchWeatherAsyn();
    }

    @Override
    protected void LoadErro(Object erro) {

        super.LoadErro(erro);
    }

    @Override
    protected void LoadData() {
        super.LoadData();
        CPControl.GetCareerResult(listener);
        cityName = OtherInfo.getInstance().getCityName();
        if (cityName == null) {
            cityName = "北京市";
        }
        mTextCity.setText(cityName);
        //		loadWeather();
        getLoction();
        getBgImage();
    }

    GetResultListCallback Tel_listener = new GetResultListCallback() {

        @Override
        public void onFinished(Object o) {
            Message msg = new Message();
            msg.what = 4;
            msg.obj = o;
            handler.sendMessage(msg);
        }

        @Override
        public void onErro(Object o) {
            Message msg = new Message();
            msg.what = 5;
            msg.obj = o;
            handler.sendMessage(msg);
        }
    };

    /**
     * 异步加载图片
     */
    @Override
    public void OnImgLoadFinished(String url, Bitmap mBitmap) {
        super.OnImgLoadFinished(url, mBitmap);
        if (url.equals(mCareerInfo.getLicenceImg())) {
            // mImageView.setImageBitmap(mBitmap);
        }
    }

    @Override
    protected void onResume() {
        Log.e("info", "onResume");
        super.onResume();
        if (count > 2) {
            CPControl.GetCareerResult(listener);
        }
        count++;
        CPControl.GetDealerInfoResult(Tel_listener);

        mTextWeather.requestFocus();

    }

    GetResultListCallback Weather_listener = new GetResultListCallback() {

        @Override
        public void onFinished(Object o) {
            Message msg = new Message();
            msg.what = 1;
            msg.obj = o;
            handler.sendMessage(msg);
        }

        @Override
        public void onErro(Object o) {
            Message msg = new Message();
            msg.what = 0;
            msg.obj = o;
            handler.sendMessage(msg);
        }
    };

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            //						loadWeather();
                            getLoction();
                        }
                    }, 5000);
                    break;
                case 1:
                    weatherLoaded(msg.obj);
                    break;
                case 4:
                    break;
                case 5:
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ActivityControl.exit(CareerMainActivity.this);
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mClient.onDestroy();
    }

    // OnWeatherSearchListener
    @Override
    public void onWeatherForecastSearched(
            LocalWeatherForecastResult weatherForecastResult, int rCode) {
        if (rCode == 1000) {
            LocalWeatherForecast mLocalWeatherForecast = weatherForecastResult
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
            loadWeather();
        }

    }

    @Override
    public void onWeatherLiveSearched(LocalWeatherLiveResult arg0, int arg1) {
        // TODO Auto-generated method stub

    }

}
