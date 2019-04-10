package com.carlt.doride.ui.activity.setting;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.carlt.doride.DorideApplication;
import com.carlt.doride.R;
import com.carlt.doride.base.LoadingActivity;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.data.set.MsgManagerInfo;
import com.carlt.doride.http.retrofitnet.ApiRetrofit;
import com.carlt.doride.http.retrofitnet.BaseMvcObserver;
import com.carlt.doride.http.retrofitnet.model.SetUserConfRspInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.protocolparser.DefaultParser;
import com.carlt.doride.systemconfig.URLConfig;
import com.carlt.doride.ui.view.UUToast;
import com.orhanobut.logger.Logger;

import java.util.HashMap;

public class MsgManageActivity extends LoadingActivity implements CompoundButton.OnCheckedChangeListener {

    private CheckBox insurance_expiration_reminder;
    private CheckBox cb_start_reminding;
    private CheckBox driving_report;
    //    private int      checkBox1;
    private int      START_REMINDING;
    private int      DRIVING_REPORT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_manage);
        loadingDataUI();
        initTitle(getResources().getString(R.string.msg_manager_txt));
        initView();
        initData();
    }

    private void initView() {

        insurance_expiration_reminder = $ViewByID(R.id.insurance_expiration_reminder);
        cb_start_reminding = $ViewByID(R.id.cb_start_reminding);
        driving_report = $ViewByID(R.id.driving_report);
        insurance_expiration_reminder.setOnCheckedChangeListener(this);
        cb_start_reminding.setOnCheckedChangeListener(this);
        driving_report.setOnCheckedChangeListener(this);
    }


    private void initData() {
        DefaultParser parser = new DefaultParser(mCallback, MsgManagerInfo.class);
        parser.executePost(URLConfig.getM_PUSH_SET(), new HashMap());
    }

    @Override
    public void loadDataSuccess(Object bInfo) {
        Logger.e(bInfo.toString());
        try {
            MsgManagerInfo info = (MsgManagerInfo) ((BaseResponseInfo) bInfo).getValue();
            //            checkBox1 = info.getReport();
            START_REMINDING = info.startup;
            DRIVING_REPORT = info.getReport();
            //            if (TextUtils.equals("0", checkBox1 + "")) {
            //                insurance_expiration_reminder.setChecked(false);
            //            } else if (TextUtils.equals("1", checkBox1 + "")) {
            //                insurance_expiration_reminder.setChecked(true);
            //            }

            if (TextUtils.equals("0", START_REMINDING + "")) {
                cb_start_reminding.setChecked(false);
            } else if (TextUtils.equals("1", START_REMINDING + "")) {
                cb_start_reminding.setChecked(true);
            }

            if (TextUtils.equals("0", DRIVING_REPORT + "")) {
                driving_report.setChecked(false);
            } else if (TextUtils.equals("1", DRIVING_REPORT + "")) {
                driving_report.setChecked(true);
            }

        } catch (Exception e) {
            loadonErrorUI(null);
        }
    }

    @Override
    public void reTryLoadData() {
        super.reTryLoadData();
        initData();
    }

    @Override
    public void loadDataError(Object bInfo) {
        super.loadDataError(bInfo);
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (!compoundButton.isPressed())
            return;
        String close = b ? "1" : "0";
        switch (compoundButton.getId()) {
            //            case R.id.insurance_expiration_reminder:
            //                checkBox1 = b ? 1 : 0;
            //                DefaultStringParser parser = new DefaultStringParser(mUpdateCallback);
            //                parser.executePost(URLConfig.getM_UPDATE_PUSH_SET(), CreateHashMap.getUpdatePushSet(close, checkBox2 + "", checkBox3 + ""));
            //                break;
            case R.id.cb_start_reminding:
                START_REMINDING = b ? 1 : 0;
                //                DefaultStringParser parser1 = new DefaultStringParser(mUpdateCallback);
                //                parser1.executePost(URLConfig.getM_UPDATE_PUSH_SET(), CreateHashMap.getUpdatePushSet(close, DRIVING_REPORT + ""));
                HashMap<String, Object> map = new HashMap<>();
                HashMap<String, Integer> parms = new HashMap<>();
                parms.put("value", START_REMINDING);
                map.put("startUp", parms);
                setSwitch(map);
                break;
            case R.id.driving_report:
                DRIVING_REPORT = b ? 1 : 0;
//                DefaultStringParser parser2 = new DefaultStringParser(mUpdateCallback);
//                parser2.executePost(URLConfig.getM_UPDATE_PUSH_SET(), CreateHashMap.getUpdatePushSet(START_REMINDING + "", close));
                HashMap<String, Object> map1 = new HashMap<>();
                HashMap<String, Integer> parms1 = new HashMap<>();
                parms1.put("value", DRIVING_REPORT);
                map1.put("dayReport", parms1);
                setSwitch(map1);
                break;
        }

    }

    private void setSwitch(HashMap<String, Object> map) {
        showWaitingDialog(null);
        map.put("base", ApiRetrofit.getRemoteCommonParams());
        map.put("moveDeviceName", DorideApplication.MODEL_NAME);
        addDisposable(mApiService.setUserConfRsp(map), new BaseMvcObserver<SetUserConfRspInfo>() {
            @Override
            public void onSuccess(SetUserConfRspInfo result) {
                dissmissWaitingDialog();
                if (result.err == null) {
                    showToast("设置成功");
                } else {
                    showToast("设置失败");
                }
            }

            @Override
            public void onError(String msg) {
                dissmissWaitingDialog();
                showToast("设置失败");
            }
        });
    }

    BaseParser.ResultCallback mUpdateCallback = new BaseParser.ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            //            initData();
            UUToast.showUUToast(MsgManageActivity.this, "设置成功", Toast.LENGTH_SHORT);
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            UUToast.showUUToast(MsgManageActivity.this, "设置失败");
        }
    };
}
