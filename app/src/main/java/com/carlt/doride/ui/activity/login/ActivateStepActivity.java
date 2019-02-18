package com.carlt.doride.ui.activity.login;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.doride.base.BaseActivity;
import com.shuhart.stepview.StepView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate_step);
        ButterKnife.bind(this);
        title.setText("设备激活");
    }

    @OnClick({R.id.back, R.id.tvRetry})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tvRetry:
                break;
        }
    }
}
