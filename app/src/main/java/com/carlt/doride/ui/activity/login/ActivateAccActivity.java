package com.carlt.doride.ui.activity.login;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.doride.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivateAccActivity extends BaseActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView  title;
    @BindView(R.id.btnOpt)
    TextView  btnOpt;
    @BindView(R.id.ivHelp)
    ImageView ivHelp;
    @BindView(R.id.btnACCNext)
    TextView  btnACCNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate_acc);
        ButterKnife.bind(this);
        title.setText("设备激活");
    }

    @OnClick({R.id.back, R.id.btnACCNext})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.btnACCNext:
                break;
        }
    }
}
