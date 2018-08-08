package com.carlt.doride.ui.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.carlt.doride.R;
import com.carlt.doride.base.LoadingActivity;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.data.car.CarSettingInfo;
import com.carlt.doride.model.LoginInfo;
import com.carlt.doride.protocolparser.BaseParser.ResultCallback;
import com.carlt.doride.protocolparser.DefaultStringParser;
import com.carlt.doride.protocolparser.car.CarSettingInfoParser;
import com.carlt.doride.systemconfig.URLConfig;
import com.carlt.doride.ui.view.UUToast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class CarManagerActivity extends LoadingActivity implements View.OnClickListener {

    private View edit_car_type;//车型
    private View edit_purchase_time;//购车时间
    private View edit_maintenance_mileage;//上次保养里程
    private View edit_maintenance_time;//上次保养时间

    private TextView       car_type_txt;//显示
    private TextView       purchase_time_txt;//显示
    private TextView       maintenance_mileage_txt;//显示
    private TextView       maintenance_time_txt;//显示
    private TimePickerView pvCustomTime;


    private String carDate;
    private String mileage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_manager);
        initComponent();
    }

    private void initComponent() {
        initTitle("车辆管理");
        edit_car_type = findViewById(R.id.edit_car_type);
//        edit_car_type.setOnClickListener(this);
        edit_purchase_time = findViewById(R.id.edit_purchase_time);
        edit_purchase_time.setOnClickListener(this);
        edit_maintenance_mileage = findViewById(R.id.edit_maintenance_mileage);
        edit_maintenance_mileage.setOnClickListener(this);
        edit_maintenance_time = findViewById(R.id.edit_maintenance_time);
        edit_maintenance_time.setOnClickListener(this);

        car_type_txt = findViewById(R.id.car_type_txt);
        if (!TextUtils.isEmpty(LoginInfo.getCarname())) {
            car_type_txt.setText(LoginInfo.getCarname());
        } else {
            car_type_txt.setText("--");
        }
        purchase_time_txt = findViewById(R.id.purchase_time_txt);
        maintenance_mileage_txt = findViewById(R.id.maintenance_mileage_txt);
        maintenance_time_txt = findViewById(R.id.maintenance_time_txt);
    }

    @Override
    protected void onResume() {
        getCarInfo();
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.edit_car_type:
//                Intent switchIntent = new Intent(this, CarModeListActivity.class);
//                switchIntent.putExtra("switch", true);//标记从车辆管理界面跳转
//                startActivity(switchIntent);
                break;
            case R.id.edit_maintenance_mileage:
                Intent editMaintenance = new Intent(this, MaintenanceMileageEditActivity.class);
                startActivityForResult(editMaintenance, 0);
                break;
            case R.id.edit_purchase_time:
                initCustomTimePicker(purchase_time_txt.getText().toString());
                pvCustomTime.show(purchase_time_txt);
                break;
            case R.id.edit_maintenance_time:
                initCustomTimePicker(maintenance_time_txt.getText().toString());
                pvCustomTime.show(maintenance_time_txt);
                break;
        }
    }

    private void modifyCarInfoRequest(HashMap<String, String> params, ResultCallback modifyCallback) {
        DefaultStringParser parser = new DefaultStringParser(modifyCallback);
        parser.executePost(URLConfig.getM_CAR_MODIFY(), params);
    }

    private ResultCallback purchaseCallback = new ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            UUToast.showUUToast(CarManagerActivity.this, "车辆信息修改成功");
            purchase_time_txt.setText(carDate);
            LoginInfo.setBuydate(carDate);
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            if (!TextUtils.isEmpty(bInfo.getInfo())) {
                UUToast.showUUToast(CarManagerActivity.this, bInfo.getInfo());
            } else {
                UUToast.showUUToast(CarManagerActivity.this, "车辆信息修改失败");
            }
        }
    };
    private ResultCallback maintenCallback  = new ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            UUToast.showUUToast(CarManagerActivity.this, "车辆信息修改成功");
            maintenance_time_txt.setText(carDate);
            LoginInfo.setMainten_time(carDate);
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            if (!TextUtils.isEmpty(bInfo.getInfo())) {
                UUToast.showUUToast(CarManagerActivity.this, bInfo.getInfo());
            } else {
                UUToast.showUUToast(CarManagerActivity.this, "车辆信息修改失败");
            }
        }
    };

    private void initCustomTimePicker(String date) {

        /**
         * @description
         *
         * 注意事项：
         * 1.自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针.
         * 具体可参考demo 里面的两个自定义layout布局。
         * 2.因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
         * setRangDate方法控制起始终止时间(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
         */
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(2014, 1, 23);
        Calendar endDate = Calendar.getInstance();
        Date time = endDate.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = dateFormat.format(time);
        String[] split = format.split("-");
        String[]selectedSplit = date.split("-");
        selectedDate.set(Integer.parseInt(selectedSplit[0]),Integer.parseInt(selectedSplit[1])-1,Integer.parseInt(selectedSplit[2]));
        endDate.set(Integer.valueOf(split[0]), Integer.valueOf(split[1]) - 1, Integer.valueOf(split[2]));
        //时间选择器 ，自定义布局
        pvCustomTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                HashMap<String, String> params = null;
                carDate = getTime(date);
                switch (v.getId()) {
                    case R.id.purchase_time_txt:
                        params = new HashMap<>();
                        params.put("buydate", carDate);
                        modifyCarInfoRequest(params, purchaseCallback);
                        break;
                    case R.id.maintenance_time_txt:
                        params = new HashMap<>();
                        params.put("mainten_date", carDate);
                        modifyCarInfoRequest(params, maintenCallback);
                        break;
                }
            }
        })

                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.time_edit_dialog, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView _OK = (TextView) v.findViewById(R.id.sex_change_OK);
                        final TextView _cancel = (TextView) v.findViewById(R.id.sex_change_cancel);
                        _OK.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.returnData();
                                pvCustomTime.dismiss();
                            }
                        });

                        _cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.dismiss();
                            }
                        });

                    }
                })
                .setContentSize(18)
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("年", "月", "日", "时", "分", "秒")
                .setLineSpacingMultiplier(1.2f)
                .setTextXOffset(0, 0, 0, 40, 0, -40)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(0xFF24AD9D)
                .build();
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            mileage = data.getStringExtra("mileage");
            if (!TextUtils.isEmpty(mileage)) {
                HashMap<String, String> params = new HashMap<>();
                params.put("mainten_miles", mileage);
                modifyCarInfoRequest(params, maintenmilesCallback);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private ResultCallback maintenmilesCallback = new ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            UUToast.showUUToast(CarManagerActivity.this, "车辆信息修改成功");
            maintenance_mileage_txt.setText(String.format(getResources().getString(R.string.last_maintenance_mileage), Integer.parseInt(mileage)));
            LoginInfo.setMainten_miles(mileage);
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            if (!TextUtils.isEmpty(bInfo.getInfo())) {
                UUToast.showUUToast(CarManagerActivity.this, bInfo.getInfo());
            } else {
                UUToast.showUUToast(CarManagerActivity.this, "车辆信息修改失败");
            }
        }
    };

    private void getCarInfo() {
        CarSettingInfoParser parser = new CarSettingInfoParser(carSettingCallback);
        HashMap<String, String> params = new HashMap<>();
        parser.executePost(URLConfig.getM_GET_CAR_SETTING(), params);
    }

    ResultCallback carSettingCallback = new ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            CarSettingInfo carSettingInfo = (CarSettingInfo) bInfo.getValue();
            if (!TextUtils.isEmpty(carSettingInfo.getCarname()) && !carSettingInfo.getCarname().equals("0")) {
                car_type_txt.setText((carSettingInfo.getCarname()));
                LoginInfo.setCarname(carSettingInfo.getCarname());
            } else {
                car_type_txt.setText("--");
                LoginInfo.setCarname("--");
            }
            if (!TextUtils.isEmpty(carSettingInfo.getBuydate()) && !carSettingInfo.getBuydate().equals("0")) {
                purchase_time_txt.setText((carSettingInfo.getBuydate()));
                LoginInfo.setBuydate(carSettingInfo.getBuydate());
            } else {
                purchase_time_txt.setText("--");
                LoginInfo.setBuydate("--");
            }
            if (!TextUtils.isEmpty(carSettingInfo.getMainten_miles())) {
                maintenance_mileage_txt.setText(String.format(getResources().getString(R.string.last_maintenance_mileage), Integer.parseInt(carSettingInfo.getMainten_miles())));
                LoginInfo.setMainten_miles(carSettingInfo.getMainten_miles());
            } else {
                maintenance_mileage_txt.setText("--");
                LoginInfo.setMainten_miles("--");
            }
            if (!TextUtils.isEmpty(carSettingInfo.getMainten_date()) && !carSettingInfo.getMainten_date().equals("0")) {
                maintenance_time_txt.setText(carSettingInfo.getMainten_date());
                LoginInfo.setMainten_time(carSettingInfo.getMainten_date());
            } else {
                maintenance_time_txt.setText("--");
                LoginInfo.setMainten_time("--");
            }
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {

        }
    };
}
