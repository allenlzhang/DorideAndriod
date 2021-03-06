package com.carlt.doride.ui.activity.carstate;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.doride.base.LoadingActivity;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.http.retrofitnet.ApiRetrofit;
import com.carlt.doride.http.retrofitnet.BaseMvcObserver;
import com.carlt.doride.utils.MyTimeUtils;
import com.carlt.doride.utils.StringUtils;
import com.carlt.sesame.protocolstack.remote.DirectTireIssueRspInfo;

import java.util.HashMap;
import java.util.List;

/**
 * 胎压监测
 */
public class CarTiresStateActivity extends LoadingActivity implements View.OnClickListener {

    private TextView  subHeadTxt;
    private ImageView tirePressureLay0;
    private ImageView tirePressureLay1;
    private ImageView tirePressureLay2;
    private ImageView tirePressureLay3;
    private TextView  pa_tv0;
    private TextView  pa_tv1;
    private TextView  pa_tv2;
    private TextView  pa_tv3;
    private TextView  temp_tv0;
    private TextView  temp_tv1;
    private TextView  temp_tv2;
    private TextView  temp_tv3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_tires_state);
        initTitle("胎压监测");
        initView();
        loadingDataUI();
        initdata();
    }

    private void initView() {
        subHeadTxt = $ViewByID(R.id.layout_sub_head_txt);
        tirePressureLay0 = $ViewByID(R.id.activity_car_tire_pressure_lay0);
        tirePressureLay1 = $ViewByID(R.id.activity_car_tire_pressure_lay1);
        tirePressureLay2 = $ViewByID(R.id.activity_car_tire_pressure_lay2);
        tirePressureLay3 = $ViewByID(R.id.activity_car_tire_pressure_lay3);

        backTV2.setVisibility(View.VISIBLE);
        backTV2.setBackgroundResource(R.drawable.icon_refresh);

        pa_tv0 = $ViewByID(R.id.l_t_pa_tv);
        pa_tv1 = $ViewByID(R.id.r_t_pa_tv);
        pa_tv2 = $ViewByID(R.id.l_b_pa_tv);
        pa_tv3 = $ViewByID(R.id.r_b_pa_tv);

        temp_tv0 = $ViewByID(R.id.l_t_temp_tv);
        temp_tv1 = $ViewByID(R.id.r_t_temp_tv);
        temp_tv2 = $ViewByID(R.id.l_b_temp_tv);
        temp_tv3 = $ViewByID(R.id.r_b_temp_tv);


    }

    private void initdata() {
//        DefaultStringParser parser = new DefaultStringParser(mCallback);
//        HashMap<String, String> param = new HashMap();
//        param.put("move_device_name", GetCarInfo.getInstance().deviceNum);
//        String m_remote_driectrressure = URLConfig.getM_REMOTE_DRIECTRRESSURE();
//        String replace = m_remote_driectrressure.replace("100", "101");
//        parser.executePost(replace, param);
//
//
        HashMap<String, Object> map = new HashMap<>();
        map.put("base", ApiRetrofit.getRemoteCommonParams());
        addDisposable(mApiService.DirectTirePressure(map), new BaseMvcObserver<DirectTireIssueRspInfo>() {

            @Override
            public void onSuccess(DirectTireIssueRspInfo result) {
                loadDataSuccess(result);
            }

            @Override
            public void onError(String msg) {

            }
        });
    }

    public void loadDataSuccess(DirectTireIssueRspInfo bInfo) {
        loadSuccessUI();
        //        String value = (String) ((BaseResponseInfo) bInfo).getValue();
        //        Gson gson = new Gson();
        //        Type type = new TypeToken<List<RemoteDirectPressureInfo>>() {
        //        }.getType();
        //        RemoteDirectPressureInfo remoteDirectPressureInfo = gson.fromJson(value, RemoteDirectPressureInfo.class);
        List<DirectTireIssueRspInfo.PressureItem> list = bInfo.list;

        if (bInfo.err == null) {
            showData(list, bInfo);
        } else {
            loadNodataUI();
        }

        //        if (null == list || list.size() == 0) {
        //            loadNodataUI();
        //        } else {
        //            showData(list, bInfo);
        //        }
    }

    @Override
    public void loadonErrorUI(BaseResponseInfo error) {
        loadSuccessUI();
        if (null == error || StringUtils.isEmpty(error.getInfo())) {
            subHeadTxt.setText("胎压获取失败");
        } else {
            subHeadTxt.setText("胎压获取失败," + error.getInfo());
        }
        tirePressureLay0.setBackgroundResource(R.drawable.tire_fail);
        tirePressureLay1.setBackgroundResource(R.drawable.tire_fail);
        tirePressureLay2.setBackgroundResource(R.drawable.tire_fail);
        tirePressureLay3.setBackgroundResource(R.drawable.tire_fail);
        pa_tv0.setVisibility(View.GONE);
        pa_tv1.setVisibility(View.GONE);
        pa_tv2.setVisibility(View.GONE);
        pa_tv3.setVisibility(View.GONE);
        temp_tv0.setVisibility(View.GONE);
        temp_tv1.setVisibility(View.GONE);
        temp_tv2.setVisibility(View.GONE);
        temp_tv3.setVisibility(View.GONE);
    }

    private void showData(List<DirectTireIssueRspInfo.PressureItem> remoteDirectPressureInfos, DirectTireIssueRspInfo remoteDirectPressureInfo) {
        String nowTimes = MyTimeUtils.formatDateMills(System.currentTimeMillis());
        int rectime = remoteDirectPressureInfo.recTime;
        if (rectime == 0) {
            subHeadTxt.setText("胎压正常。 \n" + nowTimes);
        } else {
            subHeadTxt.setText("胎压正常。 \n" + MyTimeUtils.formatDateGetDaySecend1(rectime));
        }


        // 胎压状态，1：正常；0：异常
        for (int i = 0; i < remoteDirectPressureInfos.size(); i++) {
            int pressure_status = remoteDirectPressureInfos.get(i).pressureState;
            String pa = remoteDirectPressureInfos.get(i).pressureValue + remoteDirectPressureInfos.get(i).pressureUint;
            //            String temp = remoteDirectPressureInfos.get(i).getTemperature_value() + remoteDirectPressureInfos.get(i).getTemperature_unit();
            //四个轮胎,赋值
            if (i == 0) {   //左前
                pa_tv0.setVisibility(View.VISIBLE);
                pa_tv0.setText(pa);
                pa_tv0.setTextColor(getResources().getColor(R.color.black));
                //                temp_tv0.setText(temp);
            } else if (i == 1) {//右前
                pa_tv1.setVisibility(View.VISIBLE);
                pa_tv1.setText(pa);
                pa_tv1.setTextColor(getResources().getColor(R.color.black));
                //                temp_tv1.setText(temp);
            } else if (i == 2) {//左后
                pa_tv2.setVisibility(View.VISIBLE);
                pa_tv2.setText(pa);
                pa_tv2.setTextColor(getResources().getColor(R.color.black));
                //                temp_tv2.setText(temp);
            } else if (i == 3) { //右后
                pa_tv3.setVisibility(View.VISIBLE);
                pa_tv3.setText(pa);
                pa_tv3.setTextColor(getResources().getColor(R.color.black));
                //                temp_tv3.setText(temp);
            } else {
                //么有五个轮胎
            }

            //检测是否正常
            if (pressure_status != 1) {
                //                subHeadTxt.setText("胎压异常!请及时查看! \n" + nowTimes);
                if (rectime == 0) {
                    subHeadTxt.setText("胎压异常!请及时查看! \n" + nowTimes);
                } else {
                    subHeadTxt.setText("胎压异常!请及时查看! \n" + MyTimeUtils.formatDateGetDaySecend1(rectime));
                }
                //四个轮胎
                if (i == 0) {   //左前
                    tirePressureLay0.setBackgroundResource(R.drawable.tire_err);
                    pa_tv0.setTextColor(getResources().getColor(R.color.text_tire_err));
                    temp_tv0.setTextColor(getResources().getColor(R.color.text_tire_err));
                } else if (i == 1) {//右前
                    tirePressureLay1.setBackgroundResource(R.drawable.tire_err);
                    pa_tv1.setTextColor(getResources().getColor(R.color.text_tire_err));
                    temp_tv1.setTextColor(getResources().getColor(R.color.text_tire_err));
                } else if (i == 2) {//左后
                    tirePressureLay2.setBackgroundResource(R.drawable.tire_err);
                    pa_tv2.setTextColor(getResources().getColor(R.color.text_tire_err));
                    temp_tv2.setTextColor(getResources().getColor(R.color.text_tire_err));
                } else if (i == 3) { //右后
                    tirePressureLay3.setBackgroundResource(R.drawable.tire_err);
                    pa_tv3.setTextColor(getResources().getColor(R.color.text_tire_err));
                    temp_tv3.setTextColor(getResources().getColor(R.color.text_tire_err));
                } else {
                    //么有五个轮胎
                }
            } else {
                //四个轮胎
                if (i == 0) {   //左前
                    tirePressureLay0.setBackgroundResource(R.drawable.tire_nol);
                } else if (i == 1) {//右前
                    tirePressureLay1.setBackgroundResource(R.drawable.tire_nol);
                } else if (i == 2) {//左后
                    tirePressureLay2.setBackgroundResource(R.drawable.tire_nol);
                } else if (i == 3) { //右后
                    tirePressureLay3.setBackgroundResource(R.drawable.tire_nol);
                } else {
                    //么有五个轮胎
                }
            }
        }

    }

    @Override
    public void reTryLoadData() {
        super.reTryLoadData();
        initdata();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_title_back_text2:
                loadingDataUI();
                initdata();
                break;

        }
    }

    /**
     * 使用此方法，需要在 setContentView activity 里 加入layout_title
     * 只有 一个文字标题和返回键的标题
     * @param
     */
    //    protected void initTitle(String titleString) {
    //        try {
    //            backTV = $ViewByID(R.id.head_back_img1);
    //            titleTV = $ViewByID(R.id.head_back_txt1);
    //            backTV2 = $ViewByID(R.id.head_back_img2);
    //            backTV2.setVisibility(View.VISIBLE);
    //            backTV2.setBackgroundResource(R.drawable.icon_refresh_bg);
    //        } catch (Exception e) {
    //            //是设置标题出错
    //            return;
    //        }
    //        if (null != backTV) {
    //            backTV.setOnClickListener(new View.OnClickListener() {
    //                @Override
    //                public void onClick(View view) {
    //                   onBackPressed();
    //                }
    //            });
    //        }
    //        if (null != titleTV) {
    //            titleTV.setText(titleString);
    //        }
    //        if (null != backTV2) {
    //            backTV2.setOnClickListener(new View.OnClickListener() {
    //                @Override
    //                public void onClick(View view) {
    //                    onRightClick();
    //                }
    //            });
    //        }
    //    }
    @Override
    public void onRightClick() {
        super.onRightClick();
        loadingDataUI();
        initdata();
    }
}
