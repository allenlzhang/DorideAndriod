package com.carlt.yema.ui.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.yema.R;
import com.carlt.yema.base.BaseActivity;

public class AboutYemaActivity extends BaseActivity implements OnClickListener{

    private ImageView back;
    private TextView title;
    private TextView about_yema_ver;
    private TextView about_yema_terms;

    private final static String URL_PROVISION = "http://m.cheler.com/yema.html";// 服务条款URL

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_yema);
        initComponent();
    }

    private void initComponent(){
        back=findViewById(R.id.back);
        back.setOnClickListener(this);

        title=findViewById(R.id.title);
        title.setText(getResources().getString(R.string.about_yema_txt));

        about_yema_ver=findViewById(R.id.about_yema_ver);
        about_yema_ver.setText(String.format(getResources().getString(R.string.version),"1.0.0"));

        about_yema_terms=findViewById(R.id.about_yema_terms);
        about_yema_terms.setOnClickListener(this);
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
