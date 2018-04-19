package com.carlt.doride.ui.activity.setting;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.carlt.doride.R;
import com.carlt.doride.base.LoadingActivity;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.data.set.MsgManagerInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.protocolparser.DefaultParser;
import com.carlt.doride.protocolparser.DefaultStringParser;
import com.carlt.doride.systemconfig.URLConfig;
import com.carlt.doride.ui.view.UUToast;
import com.carlt.doride.utils.CreateHashMap;

import java.util.HashMap;

public class MsgManageActivity extends LoadingActivity implements CompoundButton.OnCheckedChangeListener{

    private CheckBox insurance_expiration_reminder;
    private CheckBox annual_inspection_reminder;
    private CheckBox driving_report;
    private int checkBox1;
    private int checkBox2;
    private int checkBox3;

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

        insurance_expiration_reminder=$ViewByID(R.id.insurance_expiration_reminder);
        annual_inspection_reminder=$ViewByID(R.id.annual_inspection_reminder);
        driving_report=$ViewByID(R.id.driving_report);
        insurance_expiration_reminder.setOnCheckedChangeListener(this);
        annual_inspection_reminder.setOnCheckedChangeListener(this);
        driving_report.setOnCheckedChangeListener(this);
    }



    private void initData() {
        DefaultParser parser = new DefaultParser(mCallback, MsgManagerInfo.class);
        parser.executePost(URLConfig.getM_PUSH_SET(),new HashMap());
    }

    @Override
    public void loadDataSuccess(Object bInfo) {
        try {
            MsgManagerInfo info = (MsgManagerInfo) ((BaseResponseInfo)bInfo).getValue();
            checkBox1 = info.getReport();
            checkBox2 = info.getClass2_6201();
            checkBox3 = info.getClass2_6202();
            if (TextUtils.equals("0",checkBox1+"")){
                insurance_expiration_reminder.setChecked(false);
            }else if (TextUtils.equals("1",checkBox1+"")){
                insurance_expiration_reminder.setChecked(true);
            }

            if (TextUtils.equals("0",checkBox2+"")){
                annual_inspection_reminder.setChecked(false);
            }else if (TextUtils.equals("1",checkBox2+"")){
                annual_inspection_reminder.setChecked(true);
            }

            if (TextUtils.equals("0",checkBox3+"")){
                driving_report.setChecked(false);
            }else if (TextUtils.equals("1",checkBox3+"")){
                driving_report.setChecked(true);
            }

        }catch (Exception e){
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
        if(!compoundButton.isPressed())return;
        String close = b?"1":"0";
        switch (compoundButton.getId()){
            case R.id.insurance_expiration_reminder:
                DefaultStringParser parser = new DefaultStringParser(mUpdateCallback);
                parser.executePost(URLConfig.getM_UPDATE_PUSH_SET(), CreateHashMap.getUpdatePushSet(close,checkBox2+"",checkBox3+""));
                break;
            case R.id.annual_inspection_reminder:
                DefaultStringParser parser1 = new DefaultStringParser(mUpdateCallback);
                parser1.executePost(URLConfig.getM_UPDATE_PUSH_SET(), CreateHashMap.getUpdatePushSet(checkBox1+"",close,checkBox3+""));
                break;
            case R.id.driving_report:
                DefaultStringParser parser2 = new DefaultStringParser(mUpdateCallback);
                parser2.executePost(URLConfig.getM_UPDATE_PUSH_SET(), CreateHashMap.getUpdatePushSet(checkBox1+"",checkBox2+"",close));
                break;
        }

    }

    BaseParser.ResultCallback mUpdateCallback = new BaseParser.ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            initData();
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            UUToast.showUUToast(MsgManageActivity.this, "设置失败");
        }
    };
}
