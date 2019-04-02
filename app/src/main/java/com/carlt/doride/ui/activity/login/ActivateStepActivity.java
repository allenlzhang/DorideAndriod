package com.carlt.doride.ui.activity.login;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.doride.MainActivity;
import com.carlt.doride.R;
import com.carlt.doride.base.BaseActivity;
import com.carlt.doride.http.retrofitnet.BaseMvcObserver;
import com.carlt.doride.http.retrofitnet.model.ActivateStepInfo;
import com.carlt.doride.http.retrofitnet.model.GetCarInfo;
import com.carlt.sesame.control.ActivityControl;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ActivateStepActivity extends BaseActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView  title;
    @BindView(R.id.btnOpt)
    TextView  btnOpt;
    @BindView(R.id.ivHelp)
    ImageView ivHelp;
    @BindView(R.id.step_view)
    StepView  stepView;
    @BindView(R.id.ivLoading)
    ImageView ivLoading;
    @BindView(R.id.ivState)
    ImageView ivState;
    @BindView(R.id.tvState)
    TextView  tvState;
    @BindView(R.id.tvDes)
    TextView  tvDes;
    @BindView(R.id.tvErr)
    TextView  tvErr;
    @BindView(R.id.tvRetry)
    TextView  tvRetry;
    @BindView(R.id.tvTip)
    TextView  tvTip;
    private int carId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate_step);
        ButterKnife.bind(this);
        title.setText("设备激活");
        back.setVisibility(View.GONE);
        initActivateLogs();
        initAnimator();
    }

    Disposable disposable;
    int        time = 1800;

    @SuppressLint("CheckResult")
    private void initActivateLogs() {

        requestLogInfo(true);
        disposable = Observable.interval(5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(aLong -> requestLogInfo(false))
                .subscribe(this::accept, throwable -> LogUtils.e(throwable.getMessage()));


    }

    private void requestLogInfo(boolean isLoading) {
        //        carId = getIntent().getIntExtra("carId", 0);
        carId = GetCarInfo.getInstance().id;
        LogUtils.e(carId);
        final Map<String, Object> params = new HashMap<>();
        params.put("carID", carId);
        if (isLoading) {
            showWaitingDialog("");
        }
        addDisposable(mApiService.getLogs(params), new BaseMvcObserver<ActivateStepInfo>() {
            @Override
            public void onSuccess(ActivateStepInfo result) {
                initStepView(result);
                dissmissWaitingDialog();
            }

            @Override
            public void onError(String msg) {
                showToast(msg);
                dissmissWaitingDialog();
                if (disposable != null) {
                    disposable.dispose();
                }
            }
        });
    }

    private void initStepView(ActivateStepInfo info) {
        if (info.err == null) {
            List<ActivateStepInfo.StepsBean> steps = info.steps;
            ArrayList<ActivateStepInfo.StepsBean> logs = new ArrayList<>();
            for (ActivateStepInfo.StepsBean step : steps) {
                if (step.isSuccess == 1) {
                    //                    tvRetry.setVisibility(View.GONE);
                    //                    激活中
                    tvRetry.setText("进入首页");
                    ERR_TYPE = 3;
                    tvTip.setVisibility(View.GONE);
                    logs.add(step);
                } else if (step.isSuccess == 2) {
                    tvRetry.setVisibility(View.VISIBLE);
                    tvTip.setVisibility(View.VISIBLE);
                    disposable.dispose();
                    if (step.failCode == 2226) {
                        ERR_TYPE = 1;
                        tvRetry.setText("去修改");
                    } else if (step.failCode == 2201) {
                        ERR_TYPE = 0;
                        tvRetry.setText("去修改");
                    } else {
                        ERR_TYPE = 0;
                        tvRetry.setText("重试");

                    }
                    break;
                }
            }

            ActivateStepInfo.StepsBean secondStep = steps.get(1);
            if (secondStep.isSuccess == 2) {
                tvTip.setVisibility(View.GONE);
            }


            stepView.setStepsNumber(steps.size());
            ActivateStepInfo.StepsBean stepsBean;
            LogUtils.e(logs.size() + steps.size());
            if (logs.size() == steps.size()) {
                //激活成功
                tvRetry.setText("进入首页");
                tvRetry.setVisibility(View.VISIBLE);
                ERR_TYPE = 2;
                disposable.dispose();
                stepView.go(logs.size() - 1, true);
                stepView.done(true);
                stepsBean = steps.get(logs.size() - 1);
            } else {
                stepView.go(logs.size(), true);
                stepsBean = steps.get(logs.size());
            }


            if (stepsBean.isSuccess == -1) {
                tvState.setText(stepsBean.title);
                ivLoading.setVisibility(View.VISIBLE);
                tvDes.setText(stepsBean.description);
                tvErr.setText(stepsBean.failReason);
                //                tvErr.setVisibility(View.VISIBLE);
                //                tvTip.setVisibility(View.VISIBLE);
                //                ivState.setImageResource(R.mipmap.iv_activate_ing);
            } else if (stepsBean.isSuccess == 2) {
                tvState.setText(stepsBean.failReason);
                //                tvErr.setVisibility(View.GONE);
                //                tvTip.setVisibility(View.GONE);
                //                tvErr.setText(stepsBean.failReason);
                //                tvState.setText(stepsBean.title);
                mAnimator.cancel();
                ivLoading.setVisibility(View.GONE);
                ivState.setImageResource(R.mipmap.iv_activate_err);
            } else if (stepsBean.isSuccess == 1) {
                tvDes.setText(stepsBean.description);
                tvErr.setText(stepsBean.failReason);
                tvState.setText(stepsBean.title);
                //                tvErr.setVisibility(View.VISIBLE);
                //                tvTip.setVisibility(View.VISIBLE);
                mAnimator.cancel();
                ivLoading.setVisibility(View.GONE);
                ivState.setImageResource(R.mipmap.iv_activate_success);
            }
        } else {
            showToast(info.err.msg);
            tvState.setText(info.err.msg);
            if (disposable != null) {
                disposable.dispose();
            }
        }


    }

    private int ERR_TYPE;

    @OnClick({R.id.back, R.id.tvRetry})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tvRetry:

                switch (ERR_TYPE) {
                    case 0:
                        GetCarInfo.getInstance().remoteStatus = 3;
                        Intent intent = new Intent();
                        //                        intent.putExtra("withTbox", withTbox);
                        intent.putExtra("carId", carId);
                        intent.setClass(this, ActivateAccActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        GetCarInfo.getInstance().remoteStatus = 3;
                        Intent intent1 = new Intent();
                        //                        intent1.putExtra("withTbox", withTbox);
                        intent1.putExtra("carId", carId);
                        intent1.setClass(this, DeviceBindActivity.class);
                        startActivity(intent1);
                        break;
                    case 2:
                        GetCarInfo.getInstance().remoteStatus = 2;
                        closeActivity();
                        break;
                    case 3:
                        GetCarInfo.getInstance().remoteStatus = 1;
                        closeActivity();
                        break;
                    default:
                        break;
                }


                finish();

        }
    }

    private void closeActivity() {
        for (Activity activity : ActivityControl.mActivityList) {
            if (activity instanceof AutoGoActivateActivity) {
                activity.finish();
            }
            if (activity instanceof DeviceBindActivity)
                activity.finish();
            if (activity instanceof UserLoginActivity) {
                activity.finish();
            }

        }

        Intent intent2 = new Intent(this, MainActivity.class);
        startActivity(intent2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAnimator != null) {
            mAnimator.cancel();
        }
        if (disposable != null) {
            disposable.dispose();
        }
    }

    public ObjectAnimator mAnimator;

    private void initAnimator() {
        mAnimator = ObjectAnimator.ofFloat(ivLoading, "rotation", 0f, 359f);
        mAnimator.setRepeatCount(-1);
        mAnimator.setDuration(2000);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.start();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //            showToast("请点击“进入首页”");
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void accept(Long aLong) {
        time--;
        if (time <= 0) {
            disposable.dispose();
        }
    }
}
