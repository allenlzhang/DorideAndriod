package com.carlt.yema.ui.activity.setting;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.carlt.yema.R;
import com.carlt.yema.base.LoadingActivity;
import com.carlt.yema.model.LoginInfo;
import com.carlt.yema.protocolparser.DefaultStringParser;
import com.carlt.yema.systemconfig.URLConfig;
import com.carlt.yema.ui.view.UUToast;
import com.carlt.yema.utils.CreateHashMap;

public class GeneralManageActivity extends LoadingActivity implements CompoundButton.OnCheckedChangeListener,View.OnClickListener{

    private CheckBox voiceToggle;
    private String sound_switch; // 1:开启; 0:关闭

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_manage);
        initTitle(getResources().getString(R.string.general_manager_txt));
        initView();
    }




    private void initView() {
        voiceToggle=$ViewByID(R.id.btn_remote_voice_affect);
        voiceToggle.setOnCheckedChangeListener(this);
        voiceToggle.setChecked(LoginInfo.isRemoteSoundOpen());

    }

    @Override
    public void loadDataSuccess(Object bInfo) {
        super.loadDataSuccess(bInfo);
        if (TextUtils.equals("0",sound_switch)){
            LoginInfo.setRemoteSoundOpen(false);
        }else if (TextUtils.equals("1",sound_switch)){
            LoginInfo.setRemoteSoundOpen(true);
        }
        voiceToggle.setChecked(LoginInfo.isRemoteSoundOpen());
    }

    @Override
    public void loadDataError(Object bInfo) {
        super.loadDataError(bInfo);
        UUToast.showUUToast(this, "设置失败");
        voiceToggle.setChecked(LoginInfo.isRemoteSoundOpen());
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        sound_switch = b ? "1" : "0";
        DefaultStringParser parser = new DefaultStringParser(mCallback);
        parser.executePost(URLConfig.getM_CONTROL_SOUND(), CreateHashMap.getControlSound(sound_switch));
    }

    @Override
    public void onClick(View view) {
        finish();
    }
}
