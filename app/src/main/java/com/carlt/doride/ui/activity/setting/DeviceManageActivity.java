package com.carlt.doride.ui.activity.setting;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.doride.base.BaseActivity;
import com.carlt.doride.http.retrofitnet.model.GetCarInfo;
import com.carlt.doride.model.LoginInfo;

public class DeviceManageActivity extends BaseActivity implements View.OnClickListener {

    private ImageView back;
    private TextView  title;

    private TextView device_binded_index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_manage);
        initComponent();
    }

    private void initComponent() {
        back = $ViewByID(R.id.back);
        back.setOnClickListener(this);

        title = $ViewByID(R.id.title);
        title.setText(getResources().getString(R.string.device_manager_txt));

        device_binded_index = $ViewByID(R.id.device_binded_index);
        if (TextUtils.isEmpty(GetCarInfo.getInstance().deviceidstring)) {
            device_binded_index.setText("--");
        } else {
            device_binded_index.setText(String.format(getResources().getString(R.string.device_binded_txt), GetCarInfo.getInstance().deviceidstring));
        }
    }

    @Override
    public void onClick(View view) {
        finish();
    }
}
