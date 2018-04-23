package com.carlt.doride.ui.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.doride.DorideApplication;
import com.carlt.doride.R;
import com.carlt.doride.base.LoadingActivity;

public class AboutYemaActivity extends LoadingActivity implements OnClickListener{

    private ImageView back;
    private TextView title;
    private TextView about_yema_ver;
    private TextView about_yema_terms;

    private final static String URL_PROVISION = "http://m.cheler.com/yema.html";// 服务条款URL

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_yema);
        loadSuccessUI();
        initTitle("关于");
        initComponent();
    }

    private void initComponent(){
        back=findViewById(R.id.back);
        back.setOnClickListener(this);

        title=findViewById(R.id.title);
        title.setText(getResources().getString(R.string.about_yema_txt));

        about_yema_ver=findViewById(R.id.about_yema_ver);
        about_yema_ver.setText(String.format(getResources().getString(R.string.version), DorideApplication.VersionName));

        about_yema_terms=findViewById(R.id.about_yema_terms);
        about_yema_terms.setOnClickListener(this);
//        about_yema_terms.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
//        about_yema_terms.getPaint().setAntiAlias(true);//抗锯齿
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.about_yema_terms:
                Intent termsDeclare = new Intent(this, TermsDeclareActivity.class);
                termsDeclare.putExtra(TermsDeclareActivity.URL_INFO, URL_PROVISION);
                startActivity(termsDeclare);
                break;
        }

    }
}
