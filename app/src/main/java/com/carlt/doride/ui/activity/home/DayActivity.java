
package com.carlt.doride.ui.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.carlt.doride.R;
import com.carlt.doride.base.LoadingActivity;
import com.carlt.doride.control.CPControl;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.data.home.ReportDayInfo;
import com.carlt.doride.data.home.ReportDayLogInfo;
import com.carlt.doride.model.LoginInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.ui.pull.PullToRefreshListView;
import com.carlt.doride.ui.view.CalendarDay;
import com.carlt.doride.utils.MyParse;
import com.carlt.doride.utils.MyTimeUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class DayActivity extends LoadingActivity implements OnClickListener {

    private ImageView mImgHead;

    private TextView mTxtName;

    private ImageView mImgGender;

    private TextView mTxtDriveInfo;

    private TextView mTxtScore;


    private TextView mTxtMiles;

    private TextView mTxtTime;

    private TextView mTxtOil;

    private TextView mTxtAvgOil;

    private TextView mTxtAvgSpeed;

    private TextView mTxtMaxSpeed;

    private PullToRefreshListView mPullToRefresh;

    private ListView listView;

    LayoutInflater mInflate;

    String dayInitialValue = "";

    private final static String nullDate = "0000-00-00";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_day);
        initTitle("");
        init();
        try {
            dayInitialValue = getIntent().getStringExtra(ReportActivity.DAY_INITIAL);
        } catch (Exception e) {
        }
        if (dayInitialValue == null || dayInitialValue.equals("")) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            dayInitialValue = format.format(Calendar.getInstance().getTime());
        }
        initData();
    }


    private void init() {
        mInflate = LayoutInflater.from(this);
        mImgHead = (ImageView)findViewById(R.id.day_img_headImg);
        mTxtName = (TextView)findViewById(R.id.day_txt_userName);
        mImgGender = (ImageView)findViewById(R.id.day_img_genderImg);
        mTxtDriveInfo = (TextView)findViewById(R.id.day_txt_driveInfo);
        mTxtScore = (TextView)findViewById(R.id.day_txt_driveScore);
        mTxtMiles = (TextView)findViewById(R.id.day_txt_miles);
        mTxtTime = (TextView)findViewById(R.id.day_txt_time);
        mTxtOil = (TextView)findViewById(R.id.day_txt_oils);
        mTxtAvgOil = (TextView)findViewById(R.id.day_txt_avgOils);
        mTxtAvgSpeed = (TextView)findViewById(R.id.day_txt_avgSpeeds);
        mTxtMaxSpeed = (TextView)findViewById(R.id.day_txt_maxSpeeds);
        mPullToRefresh = (PullToRefreshListView)findViewById(R.id.day_refresh_logList);
        listView = mPullToRefresh.getRefreshableView();
        listView.setDividerHeight(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void loadDataSuccess(Object bInfo) {
        mTxtName.setText(mReportDayInfo.getRealname());
        if (!mReportDayInfo.getAvatar_img().equals("")) {
            Glide.with(mContext).load(mReportDayInfo.getAvatar_img()).into(mImgHead);
        } else {
            mImgHead.setImageResource(R.mipmap.ic_launcher);
        }
        if (String.valueOf(mReportDayInfo.getGender()).equals(LoginInfo.GENDER_NAN)) {
            mImgGender.setImageResource(R.drawable.icon_sex_male);
        } else if (String.valueOf(mReportDayInfo.getGender()).equals(LoginInfo.GENDER_NV)) {
            mImgGender.setImageResource(R.drawable.icon_sex_female);
        } else {
            mImgGender.setImageResource(R.drawable.icon_sex_secret);
        }

        int count = 0;
        if (mDayLogInfos != null && mDayLogInfos.size() > 0) {
            count = mDayLogInfos.size();
        }

        mPullToRefresh.setPullLoadEnabled(false);
        mPullToRefresh.setPullRefreshEnabled(false);
        LogAdapter mAdapter = new LogAdapter();
        listView.setAdapter(mAdapter);
        mTxtDriveInfo.setText("今日行车" + count + "次");
//        String avgFuel=mReportDayInfo.getAvgfuel() ;
//        if(avgFuel!=null&&!avgFuel.equals("")&&!avgFuel.equals("--")){
//            mTxtAvgOil.setText(mReportDayInfo.getAvgfuel());
//        }else{
            mTxtAvgOil.setText(mReportDayInfo.getAvgfuel());
//        }
        mTxtOil.setText(mReportDayInfo.getSumfuel());
        mTxtMiles.setText(mReportDayInfo.getSummiles());
        mTxtTime.setText(mReportDayInfo.getSumtime());
        mTxtAvgSpeed.setText(mReportDayInfo.getAvgspeed());
        mTxtMaxSpeed.setText(mReportDayInfo.getMaxspeed());
    }

    protected void initData() {
        if (null != dayInitialValue && dayInitialValue.length() > 0
                && !dayInitialValue.equals(nullDate)) {
        	StringBuffer titleBuffer = new StringBuffer();
        	String[] strings = dayInitialValue.split("-");
        	if (strings.length > 2) {
        		titleBuffer.append(strings[0]);
				titleBuffer.append("-");
				int month= MyParse.parseInt(strings[1]);
				if(month<10){
					titleBuffer.append("0");
				}
				titleBuffer.append(month);
				titleBuffer.append("-");
				int day=MyParse.parseInt(strings[2]);
				if(day<10){
					titleBuffer.append("0");
				}
				titleBuffer.append(day);

        	}
        	titleBuffer.append("行车日报");
            initTitle(titleBuffer.toString());
        }else{
            dayInitialValue= MyTimeUtils.getDateFormat3();
            initTitle(dayInitialValue+ "日行车日报");
        }
        loadingDataUI();
        CPControl.GetDayReportResult(dayReportCallback,dayInitialValue);
        CPControl.GetDayReportLogResult(dayLogReportCallback,dayInitialValue);
    }

    private BaseParser.ResultCallback dayReportCallback = new BaseParser.ResultCallback() {

        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            Message msg = new Message();
            msg.what = 1;
            msg.obj = bInfo;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            Message msg = new Message();
            msg.what = 0;
            msg.obj = bInfo;
            mHandler.sendMessage(msg);
        }

    };

    private BaseParser.ResultCallback dayLogReportCallback = new BaseParser.ResultCallback() {

        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            Message msg = new Message();
            msg.what = 3;
            msg.obj = bInfo;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            Message msg = new Message();
            msg.what = 2;
            msg.obj = bInfo;
            mHandler.sendMessage(msg);
        }

    };

    @Override
    public void reTryLoadData() {
        super.reTryLoadData();
        initData();
    }

    private boolean isSuccessDay;// 日报信息是否拉取成功

    private boolean isSuccessLog;// 行车日志是否拉取成功

    private ReportDayInfo mReportDayInfo;

    private ArrayList<ReportDayLogInfo> mDayLogInfos;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    isSuccessDay = false;
                    loadonErrorUI((BaseResponseInfo) msg.obj);
                    loadDataError(msg.obj);
                    break;
                case 1:
                    isSuccessDay = true;
                    mReportDayInfo = (ReportDayInfo)((BaseResponseInfo)msg.obj).getValue();
                    if (isSuccessDay && isSuccessLog) {
                        loadSuccessUI();
                        loadDataSuccess(null);
                    }
                    break;
                case 2:
                    loadonErrorUI((BaseResponseInfo) msg.obj);
                    loadDataError(msg.obj);
                    break;
                case 3:
                    isSuccessLog = true;
                    mDayLogInfos = (ArrayList<ReportDayLogInfo>)((BaseResponseInfo)msg.obj).getValue();
                    if (isSuccessDay && isSuccessLog) {
                        loadSuccessUI();
                        loadDataSuccess(null);
                    }
                    break;
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == ReportActivity.MENU) {
            selectDate();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 弹出日期选择框
     */
    private void selectDate() {
        CalendarDay mCalendarDay = new CalendarDay(DayActivity.this, mOnCalendarDayClick);

        mCalendarDay.showMenu();
    }

    CalendarDay.OnCalendarDayClick mOnCalendarDayClick = new CalendarDay.OnCalendarDayClick() {

        @Override
        public void onClick(String date) {
            dayInitialValue = date;
            initData();
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.listItem_day_txt_gpsTrack:
                // // 跳转至轨迹回放页面
                Intent mIntent = new Intent(DayActivity.this, GpsTrailActivity.class);
                ReportDayLogInfo mDayLogInfo = (ReportDayLogInfo)v.getTag();
                mIntent.putExtra(GpsTrailActivity.CAR_LOG_INFO, mDayLogInfo);
                mIntent.putExtra(ReportActivity.DAY_INITIAL, dayInitialValue);
                startActivity(mIntent);
                break;
        }

    }

    public class LogAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (mDayLogInfos != null) {
                return mDayLogInfos.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (mDayLogInfos != null) {
                return mDayLogInfos.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder mHolder = null;
            if (convertView == null) {
                mHolder = new ViewHolder();
                convertView = mInflate.inflate(R.layout.list_item_report_day, null);
                mHolder.mDriveInfo = (TextView)convertView
                        .findViewById(R.id.listItem_day_driveInfo);
                mHolder.mScore = (TextView)convertView.findViewById(R.id.listItem_day_driveScore);
                mHolder.mTime = (TextView)convertView
                        .findViewById(R.id.listItem_day_txt_timeAndMiles);
                mHolder.mMiles = convertView.findViewById(R.id.listItem_day_txt_miles);
//                mHolder.mHurrl1 = (TextView)convertView.findViewById(R.id.listItem_day_txt_hurry1);
//                mHolder.mHurrl2 = (TextView)convertView.findViewById(R.id.listItem_day_txt_hurry2);
//                mHolder.mHurrl3 = (TextView)convertView.findViewById(R.id.listItem_day_txt_hurry3);
//                mHolder.mHurrl4 = (TextView)convertView.findViewById(R.id.listItem_day_txt_hurry4);
                mHolder.mInfoOil = (TextView)convertView
                        .findViewById(R.id.listItem_day_txt_infoOil);
                mHolder.mAvgOil = (TextView)convertView
                        .findViewById(R.id.listItem_day_txt_infoAvgOil);
                mHolder.mAvgSpeed = (TextView)convertView
                        .findViewById(R.id.listItem_day_txt_infoSpeed);
                mHolder.mMaxSpeed = (TextView)convertView
                        .findViewById(R.id.listItem_day_txt_infoMaxSpeed);
                mHolder.mGpsTrack = (TextView)convertView
                        .findViewById(R.id.listItem_day_txt_gpsTrack);

                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder)convertView.getTag();
            }

            ReportDayLogInfo cInfo = mDayLogInfos.get(position);
//            mHolder.mScore.setText(cInfo.getPoint());
            String driveInfo = DayActivity.this.getResources().getString(
                    R.string.listItem_day_driveInfo, position + 1 + "/" + mDayLogInfos.size());
            mHolder.mDriveInfo.setText(driveInfo);

            String startTime = cInfo.getStarttime();
            String endTime = cInfo.getStopTime();
            String time =  startTime+"至"+endTime+"("+cInfo.getTime()+")";

            String miles = cInfo.getMiles();
            mHolder.mTime.setText(time);
            mHolder.mMiles.setText(miles);
//            mHolder.mHurrl1.setText(cInfo.getBrake());
//            mHolder.mHurrl2.setText(cInfo.getTurn());
//            mHolder.mHurrl3.setText(cInfo.getSpeedup());
//            mHolder.mHurrl4.setText(cInfo.getOverspeed());
            mHolder.mInfoOil.setText(cInfo.getFuel());
            mHolder.mAvgOil.setText(cInfo.getAvgfuel());
            mHolder.mAvgSpeed.setText(cInfo.getAvgspeed());
            mHolder.mMaxSpeed.setText(cInfo.getMaxspeed());
            mHolder.mGpsTrack.setOnClickListener(DayActivity.this);
            mHolder.mGpsTrack.setTag(cInfo);
            return convertView;
        }

        class ViewHolder {
            TextView mDriveInfo;

            TextView mScore;

            TextView mTime;

            TextView mMiles;

            TextView mHurrl1;

            TextView mHurrl2;

            TextView mHurrl3;

            TextView mHurrl4;

            TextView mInfoOil;

            TextView mAvgOil;

            TextView mAvgSpeed;

            TextView mMaxSpeed;

            TextView mGpsTrack;
        }

    }

}
