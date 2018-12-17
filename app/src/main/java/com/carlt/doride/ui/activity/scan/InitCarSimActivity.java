package com.carlt.doride.ui.activity.scan;

import android.content.Intent;
import android.os.Bundle;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.doride.R;
import com.carlt.doride.base.LoadingActivity;
import com.carlt.doride.data.carflow.CheckBindInfo;
import com.carlt.doride.systemconfig.URLConfig;
import com.carlt.doride.ui.view.UUDialog;
import com.carlt.doride.ui.view.UUToast;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import butterknife.ButterKnife;

public class InitCarSimActivity extends LoadingActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init_car_sim);
        loadSuccessUI();
        ButterKnife.bind(this);
        loadingDialog = new UUDialog(this);

        initTitle("车机初始化");
        initData();
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
                        loadingDialog.dismiss();
                        LogUtils.e(response.body());
                        //                        parseCheckJson(response);
                        String body = response.body();
                        Gson gson = new Gson();
                        CheckBindInfo info = gson.fromJson(body, CheckBindInfo.class);
                        if (info.code == 0) {
                            UUToast.showUUToast(InitCarSimActivity.this, "初始化成功");
                            finish();
                        } else {
                            UUToast.showUUToast(InitCarSimActivity.this, info.error);
                        }

                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        LogUtils.e(response.body());
                        loadingDialog.dismiss();
                        UUToast.showUUToast(InitCarSimActivity.this, "流量数据初始化失败");
                    }
                });
    }


}
