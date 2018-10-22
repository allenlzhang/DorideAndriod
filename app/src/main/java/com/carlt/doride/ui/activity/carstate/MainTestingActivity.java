package com.carlt.doride.ui.activity.carstate;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.doride.base.LoadingActivity;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.data.car.WaringLampInfo;
import com.carlt.doride.data.car.WaringLampItemInfo;
import com.carlt.doride.protocolparser.DefaultParser;
import com.carlt.doride.systemconfig.URLConfig;
import com.carlt.doride.ui.adapter.WaringLampAdapter;
import com.carlt.doride.ui.pull.PullToRefreshBase;
import com.carlt.doride.ui.pull.PullToRefreshListView;
import com.orhanobut.logger.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 车况检测报告
 */
public class MainTestingActivity extends LoadingActivity {

    private PullToRefreshListView    mPullListView;
    private TextView                 safyHeadTV;
    private TextView                 tvTitle;
    private WaringLampInfo           waringLampInfo;
    private ListView                 mListView;
    private List<WaringLampItemInfo> dataList;
    private int[] icon = {R.mipmap.abs, R.mipmap.tpms, R.mipmap.esp
            , R.mipmap.engine, R.mipmap.epb, R.mipmap.svs, R.mipmap.srs, R.mipmap.engine, R.mipmap.eps};

    private int[] iconLight = {R.mipmap.abs_light, R.mipmap.tpms_light, R.mipmap.esp_light
            , R.mipmap.engine_light, R.mipmap.epb_light, R.mipmap.svs_light, R.mipmap.srs_light, R.mipmap.engine_light, R.mipmap.eps_light};


    private             int[]    icon1    = {R.drawable.ic_water_temperature, R.drawable.ic_tire_pressure, R.drawable.ic_epb
            , R.drawable.ic_esp, R.drawable.ic_abs,
            R.drawable.ic_safety_air_bag, R.drawable.ic_engine_failure, R.drawable.ic_transmission_case};
    private             String[] iconName = {"水温报警", "胎压", "EPB电子驻车系统故障", "ESP车身稳定系统故障", "ABS"
            , "安全气囊故障", "发动机排放系统故障", "变速箱系统故障"};
    public static final String   txtTitle = "自检得分：";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tain);
        initTitle("车辆故障自检");
        initView();
        //        loadingDataUI();
        initData();
    }

    private void initData() {
        showWaitingDialog(null);
        DefaultParser<WaringLampInfo> defaultParser = new DefaultParser<>(mCallback, WaringLampInfo.class);
        defaultParser.executePost(URLConfig.getM_REMOTE_WARNINGLAMP(), new HashMap());
    }

    @Override
    public void loadDataError(Object bInfo) {
        super.loadDataError(bInfo);
        dissmissWaitingDialog();
    }


    @Override
    public void loadDataSuccess(Object bInfo) {
        dissmissWaitingDialog();
        Logger.e("-----" + bInfo.toString());
        waringLampInfo = (WaringLampInfo) ((BaseResponseInfo) bInfo).getValue();
        if (waringLampInfo != null) {
            //            safyHeadTV.setText(txtTitle.concat(waringLampInfo.Grade + ""));
            safyHeadTV.setText(String.valueOf(waringLampInfo.Grade));
            if (waringLampInfo.ENGINELAMP == 1 || waringLampInfo.ABS == 1 || waringLampInfo.EPB == 1 || waringLampInfo.MTLAMP == 1) {
                safyHeadTV.setTextColor(Color.RED);
            } else if (waringLampInfo.ESP == 1 || waringLampInfo.TPMS == 1 || waringLampInfo.WATERTMP == 1 || waringLampInfo.SRS == 1) {
                safyHeadTV.setTextColor(Color.parseColor("#efa545"));
            } else {
                safyHeadTV.setTextColor(Color.GREEN);
            }


            dataList = new ArrayList<>();
            int[] light = {waringLampInfo.WATERTMP, waringLampInfo.TPMS, waringLampInfo.EPB,
                    waringLampInfo.ESP, waringLampInfo.ABS, waringLampInfo.SRS,
                    waringLampInfo.ENGINELAMP, waringLampInfo.MTLAMP};
            for (int i = 0; i < light.length; i++) {
                addData(light[i], i);
            }

            WaringLampAdapter adapter = new WaringLampAdapter(MainTestingActivity.this, dataList);
            mListView.setAdapter(adapter);
            mPullListView.onPullDownRefreshComplete();
            setLastUpdateTime();

        } else {
            loadNodataUI();
            mPullListView.setVisibility(View.GONE);
        }

    }

    @Override
    public void reTryLoadData() {
        super.reTryLoadData();
        initData();
    }

    private void initView() {
        mPullListView = $ViewByID(R.id.pullRefreshList);
        safyHeadTV = $ViewByID(R.id.layout_sub_head_txt);
        tvTitle = $ViewByID(R.id.tvTitle);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("自检得分:");
        mListView = mPullListView.getRefreshableView();

        mListView.setDivider(getResources().getDrawable(R.drawable.list_divider_bg));
        mListView.setDividerHeight(getResources()
                .getDimensionPixelSize(R.dimen.list_divider_height));
        mListView.setVerticalScrollBarEnabled(false);
        mListView.setSelector(getResources().getDrawable(R.drawable.list_divider_bg));

        mPullListView.setPullLoadEnabled(false);
        mPullListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //                下拉刷新
                initData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //                上拉加载
            }
        });
        safyHeadTV.setText(txtTitle.concat("--"));
    }

    private List<WaringLampItemInfo> addData(int light, int i) {
        WaringLampItemInfo info = new WaringLampItemInfo();
        switch (light) {
            case WaringLampInfo.LIGHT:
                info.setIconState(R.drawable.ic_problem);

                info.setTxt(iconName[i]);
//                info.setColor(R.color.text_color_gray3);
                break;
            case WaringLampInfo.NOT_BRIGHT:
                info.setIconState(R.drawable.ic_no_problem);
                info.setTxt(iconName[i]);
//                info.setColor(R.color.text_color_gray3);
                break;
            default:
                break;
        }
        info.icon = icon1[i];
        dataList.add(info);
        return dataList;
    }

    private void setLastUpdateTime() {
        SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
        String text = mDateFormat.format(new Date(System.currentTimeMillis()));
        mPullListView.setLastUpdatedLabel(text);
    }
}