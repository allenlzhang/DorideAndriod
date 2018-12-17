package com.carlt.doride.ui.activity.scan;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.carlt.doride.R;
import com.carlt.doride.base.LoadingActivity;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.data.carflow.CheckBindInfo;
import com.carlt.doride.data.carflow.CheckCodeInfo;
import com.carlt.doride.model.LoginInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.protocolparser.DefaultStringParser;
import com.carlt.doride.systemconfig.URLConfig;
import com.carlt.doride.ui.view.UUToast;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CheckPhoneActivity extends LoadingActivity {

    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.etCode)
    EditText etCode;
    @BindView(R.id.tvSendCode)
    TextView tvSendCode;
    @BindView(R.id.tvConfirm)
    TextView tvConfirm;
    private int count = 60;

    private Timer timer = new Timer();

    private TimerTask task;
    private int    carid;
    private String    ccid;
    private String    phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_phone);
        ButterKnife.bind(this);
        initTitle("安全验证");
        phone = LoginInfo.getMobile();
        etPhone.setText(phone);
        Intent intent = getIntent();
        carid = intent.getIntExtra("carid",0);
        ccid = intent.getStringExtra("ccid");
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10:
                    count--;
                    if (count > 0) {
                        tvSendCode.setText(count + "秒后重发");
                    } else {
                        if (timer != null) {
                            if (task != null) {
                                task.cancel();
                            }
                        }
                        tvSendCode.setClickable(true);
                        tvSendCode.setText("重发验证码");
                        tvSendCode.setBackgroundResource(R.drawable.verification_send_pressed_no);
                    }
                    break;
            }
        }

    };

    @OnClick({R.id.tvSendCode, R.id.tvConfirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvSendCode:
                String phoneNum = etPhone.getText().toString();
                getVCodeRequest(phoneNum);
                count = 60;
                tvSendCode.setText(count + "秒后重发");
                tvSendCode.setClickable(false);
                tvSendCode.setBackgroundResource(R.drawable.verification_send_pressed_no);

                task = new TimerTask() {

                    @Override
                    public void run() {
                        Message msg = new Message();
                        msg.what = 10;
                        mHandler.sendMessage(msg);

                    }
                };
                timer.schedule(task, 1000, 1000);
                break;
            case R.id.tvConfirm:
                checkCode();
                break;
        }
    }

    private void checkCode() {
        String code = etCode.getText().toString().trim();
        loadingDialog.show();
        if (TextUtils.isEmpty(code)) {
            UUToast.showUUToast(this, "验证码不能为空");
            return;
        }
        OkGo.<String>post(URLConfig.getM_CHECK_VALIDATE())
                .params("client_id", URLConfig.getClientID())
                .params("dealerId", LoginInfo.getDealerId())
                .params("token", LoginInfo.getAccess_token())
                .params("mobile", phone)
                .params("type", "15")
                .params("code", code)
                .params("deviceType", "android")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        LogUtils.e(response.body());
                        String body = response.body();
                        Gson gson = new Gson();
                        CheckCodeInfo info = gson.fromJson(body, CheckCodeInfo.class);
                        if (info.code == 200) {
                            bindSim();
                        } else {
                            UUToast.showUUToast(CheckPhoneActivity.this, info.msg);
                            loadingDialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        loadingDialog.dismiss();
                        UUToast.showUUToast(CheckPhoneActivity.this, "验证出错，请稍后重试");
                    }
                });
    }

    private void bindSim() {
        OkGo.<String>post(URLConfig.getCAR_BIND_SIM_URL())
                .params("carid", carid)
                .params("ccid", ccid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        LogUtils.e(response.body());
                        parseBindJson(response);
                        loadingDialog.dismiss();
                        //                        parseCheckJson(response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        loadingDialog.dismiss();
                        //                        LogUtils.e(response);
                    }
                });
    }

    private void parseBindJson(Response<String> response) {
        String body = response.body();
        Gson gson = new Gson();
        CheckBindInfo info = gson.fromJson(body, CheckBindInfo.class);
        if (info.code == 0) {
            //成功
            Intent intent = new Intent(this, InitCarSimActivity.class);
            intent.putExtra("carid", carid);
            intent.putExtra("ccid", ccid);
            startActivity(intent);
            finish();
        } else {
            ToastUtils.showShort(info.error);
        }


    }

    /**
     * 获取验证码接口请求
     */
    private void getVCodeRequest(String mobile) {
        DefaultStringParser parser = new DefaultStringParser(vCodeCallback);
        HashMap<String, String> params = new HashMap<>();
        params.put("mobile", mobile);
        params.put("type", "15");
        params.put("voiceVerify", "0");
        parser.executePost(URLConfig.getM_AUTH_SET_VALIDATE(), params);
    }

    private BaseParser.ResultCallback vCodeCallback = new BaseParser.ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            // 获取验证码成功
            UUToast.showUUToast(CheckPhoneActivity.this, "验证码已发送成功！");
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            // 获取验证码失败
            // 停止计时
            if (timer != null) {
                if (task != null) {
                    task.cancel();
                }
            }
            tvSendCode.setClickable(true);
            tvSendCode.setText("重发验证码");
            tvSendCode.setBackgroundResource(R.drawable.verification_send_pressed_no);
            int flag = bInfo.getFlag();
            UUToast.showUUToast(CheckPhoneActivity.this, "验证码获取失败:" + bInfo.getInfo());
        }
    };
}
