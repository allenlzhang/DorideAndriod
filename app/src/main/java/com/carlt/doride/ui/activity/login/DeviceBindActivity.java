package com.carlt.doride.ui.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ReplacementTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.doride.base.BaseActivity;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.model.LoginInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.protocolparser.DefaultStringParser;
import com.carlt.doride.systemconfig.URLConfig;
import com.carlt.doride.ui.activity.setting.CarModeListActivity;
import com.carlt.doride.ui.view.UUToast;
import com.carlt.doride.utils.Log;

import java.util.HashMap;

/**
 * 绑定设备
 */
public class DeviceBindActivity extends BaseActivity implements View.OnClickListener {

    private ImageView back;//返回按钮
    private LinearLayout title_bar;

    private TextView titleText;//页面标题
    private TextView btn_select_car;//选择爱车
    private TextView bind_commit;//绑定提交

    private EditText car_vin_code;//vin码输入框

    private String carTitle;//用户选择的车款

    private String deviceId;//设备ID

    private static String vinCode;

    public static final String TAG = "DeviceBindActivity";

    private static String ACTIVATE = "com.carlt.doride.ActivateBindActivity";

    private Intent intent;

    private String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_bind);
        initComponent();
        intent = getIntent();
        from = intent.getStringExtra("from");
    }

    @Override
    protected void onResume() {
        super.onResume();
        setBindData();
    }

    private void setBindData() {
        carTitle = LoginInfo.getCarname();
        if (!TextUtils.isEmpty(from) && from.equals(ACTIVATE)) {
            if (!TextUtils.isEmpty(carTitle)) {
                btn_select_car.setText(carTitle);
            }
        }else {
            if (intent != null && !TextUtils.isEmpty(intent.getStringExtra("carType"))) {
                btn_select_car.setText(intent.getStringExtra("carType"));
            }
        }
        vinCode =intent.getStringExtra("vin");
        if (!TextUtils.isEmpty(vinCode)) {

            car_vin_code.setText(vinCode);
            car_vin_code.setSelection(vinCode.length());
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        setBindData();
    }

    private void initComponent() {
        back = findViewById(R.id.back);
        back.setOnClickListener(this);
        titleText = findViewById(R.id.title);
        titleText.setText("绑定设备");
        btn_select_car = findViewById(R.id.btn_select_car);
        btn_select_car.setOnClickListener(this);
        bind_commit = findViewById(R.id.bind_commit);
        bind_commit.setOnClickListener(this);
        car_vin_code = findViewById(R.id.car_vin_code);
        car_vin_code.setTransformationMethod(new AutoCaseTransformationMethod());
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                back();
                break;
            case R.id.btn_select_car:
                // 选择车系
                Intent intent = new Intent(this, CarModeListActivity.class);
                if (!TextUtils.isEmpty(car_vin_code.getText().toString())) {
                    intent.putExtra("vin", car_vin_code.getText().toString());
                    vinCode = car_vin_code.getText().toString();
                }
                intent.putExtra("from_bind", TAG);
                startActivity(intent);
                break;
            case R.id.bind_commit:
                deviceId = car_vin_code.getText().toString();
                if (isVinValid()) {
                    bindDevice();
                }
                break;
        }
    }

    private void bindDevice() {
        DefaultStringParser parser = new DefaultStringParser(callback);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("vin", deviceId);
        parser.executePost(URLConfig.getM_DEVICE_BIND_CAR(), map);
    }

    BaseParser.ResultCallback callback = new BaseParser.ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            if (!TextUtils.isEmpty(car_vin_code.getText().toString())) {
                LoginInfo.setVin(LoginInfo.getMobile(), car_vin_code.getText().toString());
            }
            if (!TextUtils.isEmpty(LoginInfo.getCarname()) && !TextUtils.isEmpty(from) && from.equals(ACTIVATE)) {
                btn_select_car.setText(LoginInfo.getCarname());
            }

            Intent activateIntent = new Intent(DeviceBindActivity.this, ActivateBindActivity.class);
            activateIntent.putExtra("vin",vinCode);
            activateIntent.putExtra("carType",carTitle);
            startActivity(activateIntent);
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            if (!TextUtils.isEmpty(bInfo.getInfo())) {
                UUToast.showUUToast(DeviceBindActivity.this, bInfo.getInfo());
            } else {
                UUToast.showUUToast(DeviceBindActivity.this, "车辆绑定成功");
            }
        }
    };

    private boolean isVinValid() {
        if (TextUtils.isEmpty(deviceId)) {
            UUToast.showUUToast(this, " VIN码不能为空");
            return false;
        } else if (deviceId.length() < 17) {
            UUToast.showUUToast(this, " 请输入正确的VIN码");
            return false;
        } else if (btn_select_car.getText().equals(getResources().getString(R.string._car_select_type))) {
            UUToast.showUUToast(this, " 爱车信息不能为空");
            return false;
        }
        return true;
    }

    private void back() {
        Intent loginIntent = new Intent(this, UserLoginActivity.class);
        startActivity(loginIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        back();
        super.onBackPressed();
    }
    public class AutoCaseTransformationMethod extends ReplacementTransformationMethod {
        /**
         * 获取要改变的字符。
         * @return 将你希望被改变的字符数组返回。
         */
        @Override
        protected char[] getOriginal() {
            return new char[]{'a', 'b', 'c', 'd', 'e',
                    'f', 'g', 'h', 'i', 'j', 'k', 'l',
                    'm', 'n', 'o', 'p', 'q', 'r', 's',
                    't', 'u', 'v', 'w', 'x', 'y', 'z'};
        }

        /**
         * 获取要替换的字符。
         * @return 将你希望用来替换的字符数组返回。
         */
        @Override
        protected char[] getReplacement() {
            return new char[]{ 'A', 'B', 'C', 'D', 'E',
                    'F', 'G', 'H', 'I', 'J','K','L','M',
                    'N','O','P','Q','R','S','T','U','V','W','X','Y','Z' };
        }
    }
}
