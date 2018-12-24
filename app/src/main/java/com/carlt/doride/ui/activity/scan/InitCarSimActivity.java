package com.carlt.doride.ui.activity.scan;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.carlt.doride.DorideApplication;
import com.carlt.doride.R;
import com.carlt.doride.base.LoadingActivity;
import com.carlt.doride.control.ActivityControl;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.data.carflow.CheckBindInfo;
import com.carlt.doride.data.carflow.CheckInitInfo;
import com.carlt.doride.model.LoginInfo;
import com.carlt.doride.systemconfig.URLConfig;
import com.carlt.doride.ui.view.UUDialog;
import com.carlt.doride.ui.view.UUToast;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONObject;

import butterknife.ButterKnife;

public class InitCarSimActivity extends LoadingActivity {

    private TextView mTxtWarning;
    private Button mBtnReTry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init_car_sim);
        loadSuccessUI();
        ButterKnife.bind(this);
        loadingDialog = new UUDialog(this);
        mTxtWarning = findViewById(R.id.sim_txt_warning);
        mBtnReTry = findViewById(R.id.sim_btn);
        initTitle("车机初始化");
        initData();
        mBtnReTry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
    }


    private void initData() {
        Intent intent = getIntent();
        int carid = intent.getIntExtra("carid", 0);
        String ccid = intent.getStringExtra("ccid");
        loadingDialog.show();
        OkGo.<String>post(URLConfig.getCAR_INIT_SIM_URL())
                .params("carid", carid)
                .params("ccid", ccid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
//                        loadingDialog.dismiss();
                        LogUtils.e(response.body());
                        //                        parseCheckJson(response);
                        String body = response.body();
                        Gson gson = new Gson();
                        CheckBindInfo info = gson.fromJson(body, CheckBindInfo.class);
                        if (info.code == 0) {
//                            UUToast.showUUToast(InitCarSimActivity.this, "初始化成功");
//                            finish();
                            countDataPackage();
                        } else {
                            loadingDialog.dismiss();
                            UUToast.showUUToast(InitCarSimActivity.this, info.error);
//                            finish();
                            mTxtWarning.setVisibility(View.VISIBLE);
                            mBtnReTry.setVisibility(View.VISIBLE);
                        }

                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        LogUtils.e(response.body());
                        loadingDialog.dismiss();
                        UUToast.showUUToast(InitCarSimActivity.this, "流量数据初始化失败");
                        mTxtWarning.setVisibility(View.VISIBLE);
                        mBtnReTry.setVisibility(View.VISIBLE);
//                        finish();
                    }
                });
    }

    /**
     * 判断T-box、车机是否配置流量产品（V140）
     */
    private void countDataPackage(){
        String countDataPackageUrl = URLConfig.getM_COUNTDATAPACKGE();
        OkGo.<String>post(countDataPackageUrl)
                .params("client_id", URLConfig.getClientID())
                .params("token", LoginInfo.getAccess_token())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        loadingDialog.dismiss();
                        parseCountDataPackage(response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        loadingDialog.dismiss();
                        super.onError(response);
                        LogUtils.e(response);
                    }
                });
    }
    private void parseCountDataPackage(Response<String> response){
        String body = response.body();
        try {
            JSONObject jsonObject = new JSONObject(body);
            int code = jsonObject.optInt("code",-1);
            String msg = jsonObject.optString("msg","");
            if (code == BaseResponseInfo.NO_TOKEN){
                ActivityControl.onTokenDisable();
                ToastUtils.setGravity(Gravity.CENTER, 0, 0);
                ToastUtils.setBackgroundColor(R.drawable.toast_bg);
                ToastUtils.setMessageColor(Color.WHITE);
                ToastUtils.showLong(msg);
                return;
            }
            JSONObject data = jsonObject.getJSONObject("data");
            if (data!=null){
                int tboxDataNum = data.optInt("tboxDataNum",0);
                int machineDataNum = data.optInt("machineDataNum",0);
                int tboxRenewNum = data.optInt("tboxRenewNum",0);
                if (machineDataNum!=0){
                    Intent intent = new Intent(this,CarFlowPackageRechargeActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    ToastUtils.showShort("暂未获取到商品列表");
                    finish();
                }
            }
        }catch (Exception e){
            LogUtils.e(e);
        }
    }

}
