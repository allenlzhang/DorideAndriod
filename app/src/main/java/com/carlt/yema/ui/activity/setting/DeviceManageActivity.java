package com.carlt.yema.ui.activity.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.yema.R;
import com.carlt.yema.base.BaseActivity;
import com.carlt.yema.model.LoginInfo;

public class DeviceManageActivity extends BaseActivity implements View.OnClickListener{

    private ImageView back;
    private TextView title;

    private TextView device_binded_index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_manage);
        initComponent();
    }

    private void initComponent() {
        back=$ViewByID(R.id.back);
        back.setOnClickListener(this);

        title = $ViewByID(R.id.title);
        title.setText(getResources().getString(R.string.device_manager_txt));

        device_binded_index= $ViewByID(R.id.device_binded_index);
        device_binded_index.setText(String.format(getResources().getString(R.string.device_binded_txt), LoginInfo.getDeviceidstring()));

    }

    @Override
    public void onClick(View view) {
        finish();
    }
}
