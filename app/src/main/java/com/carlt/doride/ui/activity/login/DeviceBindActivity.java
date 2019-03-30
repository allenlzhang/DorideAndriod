package com.carlt.doride.ui.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ReplacementTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.doride.MainActivity;
import com.carlt.doride.R;
import com.carlt.doride.base.BaseActivity;
import com.carlt.doride.control.LoginControl;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.http.retrofitnet.model.GetCarInfo;
import com.carlt.doride.http.retrofitnet.model.UserInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.protocolparser.DefaultStringParser;
import com.carlt.doride.systemconfig.URLConfig;
import com.carlt.doride.ui.activity.setting.CarModeListActivity;
import com.carlt.doride.ui.view.PopBoxCreat;
import com.carlt.doride.ui.view.UUToast;
import com.carlt.sesame.data.SesameBindDeviceInfo;
import com.carlt.sesame.preference.TokenInfo;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * 绑定车辆
 */
public class DeviceBindActivity extends BaseActivity implements View.OnClickListener {

    private ImageView    back;//返回按钮
    private ImageView    ivHelp;//返回按钮
    private LinearLayout title_bar;

    private TextView titleText;//页面标题
    private TextView btn_select_car;//选择爱车
    private TextView bind_commit;//绑定提交

    private EditText car_vin_code;//vin码输入框

    private String carTitle;//用户选择的车款

    private String deviceId;//设备ID

    private static String vinCode;

    public static final String TAG = "DeviceBindActivity";

    private static String ACTIVATE        = "com.carlt.doride.ActivateBindActivity";
    private static String SESAME_ACTIVATE = "com.carlt.sesame.ui.activity.usercenter.login.SesameActivateActivity";

    private Intent intent;

    private String   from;
    private String   mCarid;
    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_bind);
        userInfo = UserInfo.getInstance();
        initComponent();
        intent = getIntent();
        from = intent.getStringExtra("from");
        mCarid = intent.getStringExtra("carid");
        LogUtils.e("====" + mCarid);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setBindData();
    }

    private void setBindData() {
        //        carTitle = GetCarInfo.getInstance().carName;
        if (!TextUtils.isEmpty(from) && from.equals(ACTIVATE)) {
            String cat_title = intent.getStringExtra("cat_title");
            if (!TextUtils.isEmpty(cat_title)) {
                btn_select_car.setText(cat_title);
            }
        } else {
            if (intent != null && !TextUtils.isEmpty(intent.getStringExtra("carType"))) {
                btn_select_car.setText(intent.getStringExtra("carType"));
            }
        }
        vinCode = intent.getStringExtra("vin");
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
        ivHelp = findViewById(R.id.ivHelp);
        back.setOnClickListener(this);
        titleText = findViewById(R.id.title);
        titleText.setText("绑定车辆");
        btn_select_car = findViewById(R.id.btn_select_car);
        btn_select_car.setOnClickListener(this);
        bind_commit = findViewById(R.id.bind_commit);
        bind_commit.setOnClickListener(this);
        car_vin_code = findViewById(R.id.car_vin_code);
        car_vin_code.setTransformationMethod(new AutoCaseTransformationMethod());


        ivHelp.setVisibility(View.VISIBLE);
        ivHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DeviceBindActivity.this, ActivateHelpActivity.class));
            }
        });
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
                //立即激活
                if (isVinValid()) {
                    if (GetCarInfo.getInstance().carType == 1) {
                        //  大乘
                        bindDevice();
                    } else if (GetCarInfo.getInstance().carType == 2) {
                        // 芝麻
                        bindSesameDevice();

                    }

                }



                break;
        }
    }

    public final static String FROM_NAME = "from_name";


    private void bindSesameDevice() {
        OkGo.<String>post(com.carlt.sesame.systemconfig.URLConfig.getM_CAR_BINDDEVICE_URL())
                .params("client_id", com.carlt.sesame.systemconfig.URLConfig.getClientID())
                .params("dealerId", userInfo.dealerId)
                .params("token", TokenInfo.getToken())
                .params("deviceType", "android")
                .params("vin", deviceId)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        LogUtils.e("===" + response.body());
                        String body = response.body();
                        Gson gson = new Gson();

                        try {
                            JSONObject jo = new JSONObject(body);
                            int code = jo.getInt("code");
                            String msg = jo.getString("msg");
                            if (code == 200) {
                                SesameBindDeviceInfo info = gson.fromJson(body, SesameBindDeviceInfo.class);
                                UUToast.showUUToast(DeviceBindActivity.this, "绑定成功!");
                                // 跳转芝麻的激活页面
//                                Intent sesameActivateIntent = new Intent(DeviceBindActivity.this, SesameActivateActivity.class);
//                                sesameActivateIntent.putExtra("need_pin", info.data.need_pin);
//                                startActivity(sesameActivateIntent);


//                                Intent activateIntent = new Intent(DeviceBindActivity.this, ActivateAccActivity.class);
//                                activateIntent.putExtra("vin", vinCode);
//                                activateIntent.putExtra("carType", carTitle);
//                                activateIntent.putExtra("carID", mCarid);

//                                startActivity(activateIntent);
                                justActivate();
                            } else {
                                UUToast.showUUToast(DeviceBindActivity.this, msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        UUToast.showUUToast(DeviceBindActivity.this, "绑定失败，请检查网络!");
                    }
                });
    }

    private void bindDevice() {
        DefaultStringParser parser = new DefaultStringParser(callback);
        HashMap<String, String> map = new HashMap<>();
        map.put("vin", deviceId);
        parser.executePost(URLConfig.getM_DEVICE_BIND_CAR(), map);
    }

    BaseParser.ResultCallback callback = new BaseParser.ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            String value = (String) bInfo.getValue();
            try {
                JSONObject object = new JSONObject(value);

                String deviceidstring = object.getString("deviceidstring");
                GetCarInfo.getInstance().deviceNum = deviceidstring;
            } catch (JSONException e) {
                e.printStackTrace();
            }


            if (!TextUtils.isEmpty(car_vin_code.getText().toString())) {
                GetCarInfo.getInstance().vin = car_vin_code.getText().toString();
            }
            if (!TextUtils.isEmpty(GetCarInfo.getInstance().carName) && !TextUtils.isEmpty(from) && from.equals(ACTIVATE)) {
                btn_select_car.setText(GetCarInfo.getInstance().carName);
            }

            justActivate();

            //  大乘
            //            Intent activateIntent = new Intent(DeviceBindActivity.this, ActivateBindActivity.class);



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
    private void justActivate() {
        PopBoxCreat.createDialogNotitle(this,
                "温馨提示",
                "您的爱车已添加，您现在可以去激活设备啦，激活成功后，就能使用全部功能啦！",
                "稍后激活", "立即激活", new PopBoxCreat.DialogWithTitleClick() {
                    @Override
                    public void onLeftClick() {
                        //稍后激活
                        Intent intent2 = new Intent(DeviceBindActivity.this, MainActivity.class);
                        startActivity(intent2);
                        finish();
                    }

                    @Override
                    public void onRightClick() {
                        //立即激活
                        Intent activateIntent = new Intent(DeviceBindActivity.this, ActivateAccActivity.class);
                        activateIntent.putExtra("vin", vinCode);
                        activateIntent.putExtra("carType", carTitle);
                        activateIntent.putExtra("carID", mCarid);
                        startActivity(activateIntent);
                    }
                });
        LoginControl.getCarInfo(new HashMap<>());
    }
    private boolean isVinValid() {
        if (TextUtils.isEmpty(deviceId)) {
            UUToast.showUUToast(this, " VIN码不能为空");
            return false;
        } else if (deviceId.length() < 17) {
            UUToast.showUUToast(this, " 请输入正确的VIN码");
            return false;
        } else if (btn_select_car.getText().equals(getResources().getString(R.string._car_select_type))) {
            UUToast.showUUToast(this, "爱车信息不能为空");
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
            return new char[]{'A', 'B', 'C', 'D', 'E',
                    'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
                    'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        }
    }
}
