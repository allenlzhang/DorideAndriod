package com.carlt.doride.ui.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.doride.R;
import com.carlt.doride.base.LoadingActivity;
import com.carlt.doride.control.ActivityControl;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.data.car.CarModeInfo;
import com.carlt.doride.model.LoginInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.protocolparser.DefaultStringParser;
import com.carlt.doride.protocolparser.car.CarTypeInfoListParser;
import com.carlt.doride.systemconfig.URLConfig;
import com.carlt.doride.ui.activity.login.DeviceBindActivity;
import com.carlt.doride.ui.adapter.CarTypeAdapter;
import com.carlt.doride.ui.view.PopBoxCreat;
import com.carlt.doride.ui.view.UUToast;
import com.carlt.sesame.data.SesameLoginInfo;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class CarTypeListActivity extends LoadingActivity {

    private        ListView       car_type_list;//车款列表
    private        CarTypeAdapter adapter;
    private        Intent         intent;
    private        String         optionid;//车型ID
    private static String         brandid = "2579";//车系ID
    private        String         carId;//车款ID
    private        String         carTitle;
    private        String         vinCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_type_list);
        intent = getIntent();
        vinCode = intent.getStringExtra("vin");
        optionid = intent.getStringExtra("optionid");
        loadingDataUI();
        initComponent();
        initData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        super.onNewIntent(intent);
    }

    private void initData() {
        CarTypeInfoListParser parser = new CarTypeInfoListParser(mCallback);
        HashMap<String, String> params = new HashMap<>();
        params.put("optionid", optionid);
        parser.executePost(URLConfig.getM_CAR_TYPE_LIST(), params);
    }

    private void initComponent() {
        initTitle("车款");
        car_type_list = $ViewByID(R.id.car_type_list);
        car_type_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CarModeInfo modeInfo = (CarModeInfo) adapterView.getItemAtPosition(i);
                carId = modeInfo.getId();
                carTitle = modeInfo.getTitle();
                if (carTitle == null) {
                    return;
                }
                if (intent != null && intent.getBooleanExtra("switch", false)) {
                    switchCarType();
                } else {
                    addCarType();
                }
            }
        });
    }


    @Override
    public void loadDataSuccess(Object bInfo) {
        super.loadDataSuccess(bInfo);
        try {
            BaseResponseInfo<ArrayList<CarModeInfo>> baseResponseInfo = (BaseResponseInfo<ArrayList<CarModeInfo>>) bInfo;
            ArrayList<CarModeInfo> carModeInfos = (ArrayList<CarModeInfo>) baseResponseInfo.getValue();
            if (carModeInfos != null && carModeInfos.size() > 0) {
                adapter = new CarTypeAdapter(this, carModeInfos);
                car_type_list.setAdapter(adapter);
            } else {
                loadNodataUI();
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

    private void addCarType() {
        PopBoxCreat.createDialogNotitle(CarTypeListActivity.this, null, "您选择的车型是" + carTitle, "取消", "确定", new PopBoxCreat.DialogWithTitleClick() {
            @Override
            public void onLeftClick() {

            }

            @Override
            public void onRightClick() {
                if (carTitle.startsWith("E20")) {

                    LoginInfo.setApp_type(2);
                    addSesameCar();
                } else if (carTitle.startsWith("大乘")) {
                    LoginInfo.setApp_type(1);
                    addDorideCar();
                }


            }
        });

    }

    private void addSesameCar() {
        // TODO: 2018/8/2  
        //        String dorideToken = LoginInfo.getAccess_token();
        //        String dorideToken = SesameLoginInfo.getAccess_token();
        OkGo.<String>post(com.carlt.sesame.systemconfig.URLConfig.getM_SET_CAR_INFO_URL())
                .params("client_id", com.carlt.sesame.systemconfig.URLConfig.getClientID())
                .params("dealerId", SesameLoginInfo.getDealerId())
                .params("token", SesameLoginInfo.getAccess_token())
                .params("deviceType", "android")
                .params("brandid", brandid)
                .params("optionid", optionid)
                .params("carid", carId)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        LogUtils.e("===" + response.body());
                        String body = response.body();
                        try {
                            JSONObject jo = new JSONObject(body);
                            int code = jo.getInt("code");
                            if (code == 200) {
                                LoginInfo.setCarname(carTitle);
                                Intent intent = new Intent(CarTypeListActivity.this, DeviceBindActivity.class);
                                intent.putExtra("cat_title", carTitle);
                                intent.putExtra("from", "com.carlt.doride.ActivateBindActivity");
                                if (!TextUtils.isEmpty(vinCode)) {
                                    intent.putExtra("vin", vinCode);
                                }
                                SesameLoginInfo.setDeviceActivate(true);
                                CarTypeListActivity.this.startActivity(intent);
                                ActivityControl.finishAllCarSelectActivity();
                            } else {
                                UUToast.showUUToast(CarTypeListActivity.this, " 车型绑定失败");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Response<String> response) {

                        UUToast.showUUToast(CarTypeListActivity.this, " 车型绑定失败");
                    }
                });
    }

    private void addDorideCar() {
        DefaultStringParser parser = new DefaultStringParser(addResult);
        HashMap<String, String> params = new HashMap<>();
        params.put("brandid", brandid);
        params.put("optionid", optionid);
        params.put("carid", carId);
        parser.executePost(URLConfig.getM_CAR_ADD_CAR(), params);
    }

    BaseParser.ResultCallback addResult = new BaseParser.ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            String value = (String) bInfo.getValue();
            LoginInfo.setCarname(carTitle);

            String carid = null;
            try {
                JSONObject object = new JSONObject(value);
                carid = object.getString("carid");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(CarTypeListActivity.this, DeviceBindActivity.class);
            intent.putExtra("cat_title", carTitle);
            intent.putExtra("carid", carid);
            intent.putExtra("from", "com.carlt.doride.ActivateBindActivity");
            if (!TextUtils.isEmpty(vinCode)) {
                intent.putExtra("vin", vinCode);
            }
            if (optionid.equals("2582")) {
                LoginInfo.setDeviceisnew(1);
            } else {
                LoginInfo.setDeviceisnew(0);
            }
            //            if (carTitle.startsWith("E20")) {
            //                LoginInfo.setApp_type(2);
            //            } else if (carTitle.startsWith("大乘")) {
            //                LoginInfo.setApp_type(1);
            //            }
            CarTypeListActivity.this.startActivity(intent);
            ActivityControl.finishAllCarSelectActivity();
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            if (!TextUtils.isEmpty(bInfo.getInfo())) {
                UUToast.showUUToast(CarTypeListActivity.this, bInfo.getInfo());
            } else {
                UUToast.showUUToast(CarTypeListActivity.this, " 车型绑定失败");
            }
        }
    };

    private void switchCarType() {
        PopBoxCreat.createDialogNotitle(CarTypeListActivity.this, null, "您选择的车型是" + carTitle, "取消", "确定", new PopBoxCreat.DialogWithTitleClick() {
            @Override
            public void onLeftClick() {

            }

            @Override
            public void onRightClick() {
                DefaultStringParser parser = new DefaultStringParser(switchResult);
                HashMap<String, String> params = new HashMap<>();
                params.put("brandid", brandid);
                params.put("optionid", optionid);
                params.put("carid", carId);
                parser.executePost(URLConfig.getM_SWITCHCAR_URL(), params);
            }
        });


    }

    BaseParser.ResultCallback switchResult = new BaseParser.ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            LoginInfo.setCarname(carTitle);
            UUToast.showUUToast(CarTypeListActivity.this, " 车型修改成功");
            if (optionid.equals("2582")) {
                LoginInfo.setDeviceisnew(1);
            } else {
                LoginInfo.setDeviceisnew(0);
            }
            ActivityControl.finishAllCarSelectActivity();
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            if (!TextUtils.isEmpty(bInfo.getInfo())) {
                UUToast.showUUToast(CarTypeListActivity.this, bInfo.getInfo());
            } else {
                UUToast.showUUToast(CarTypeListActivity.this, " 车型修改失败");
            }
        }
    };

}
