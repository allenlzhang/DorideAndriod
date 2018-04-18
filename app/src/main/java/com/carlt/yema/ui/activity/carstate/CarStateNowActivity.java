package com.carlt.yema.ui.activity.carstate;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ListView;
import android.widget.TextView;

import com.carlt.yema.R;
import com.carlt.yema.base.LoadingActivity;
import com.carlt.yema.data.BaseResponseInfo;
import com.carlt.yema.data.car.CarNowStatusInfo;
import com.carlt.yema.protocolparser.DefaultParser;
import com.carlt.yema.systemconfig.URLConfig;
import com.carlt.yema.ui.adapter.CarNowStatusAdapter;

import java.util.HashMap;

/**
 * 实时车况
 *
 */
public class CarStateNowActivity extends LoadingActivity {

    private ListView mListView;

    private TextView mTxtView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_state_now);
        initTitle("实时车况");
        initView();
        loadingDataUI();
        initData();
    }
    @Override
    protected void onResume() {
        super.onResume();

        loadingDataUI();
        initData();
    }
    //初始化view
    private void initView() {
        mListView = $ViewByID(R.id.activity_car_condition_list);
        mTxtView = $ViewByID(R.id.layout_sub_head_txt);
    }

    private void initData() {
        DefaultParser<CarNowStatusInfo> parser = new DefaultParser<>(mCallback,CarNowStatusInfo.class);
        parser.executePost(URLConfig.getM_REMOTE_STATUS(),new HashMap());
    }

    @Override
    public void loadDataSuccess(Object bInfo) {
        try {
            CarNowStatusInfo carNowStatusInfo = (CarNowStatusInfo) ((BaseResponseInfo) bInfo).getValue();

            if(null == carNowStatusInfo){
                loadNodataUI();
            }else{
                showData(carNowStatusInfo);
            }
        }catch (Exception e){
            loadonErrorUI(null);
        }
    }

    private void showData(CarNowStatusInfo carNowStatusInfo) {
        if (TextUtils.equals(carNowStatusInfo.getIsrunning(),"1")){
            mTxtView.setText("您的爱车正在行驶中");
        }else if (TextUtils.equals(carNowStatusInfo.getIsrunning(),"0")){
            mTxtView.setText("您的爱车正在休息");
        }else{
            mTxtView.setText("");
        }
        if (carNowStatusInfo.getList()!=null&&carNowStatusInfo.getList().size() !=0){
            CarNowStatusAdapter adapter = new CarNowStatusAdapter(CarStateNowActivity.this,carNowStatusInfo.getList());
            mListView.setAdapter(adapter);
        }

    }


    @Override
    public void reTryLoadData() {
        super.reTryLoadData();
        initData();
    }
}
