package com.carlt.yema.ui.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.yema.R;
import com.carlt.yema.base.BaseActivity;


/**
 * 上次保养里程修改Activity
 *
 * */
public class MaintenanceMileageEditActivity extends BaseActivity implements OnClickListener{

    private ImageView back;
    private TextView title;

    private EditText maintenance_mileage_input;//里程输入框
    private TextView maintenance_mileage_commit;//确认修改按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_mlieage_edit);
        initComponent();
    }

    private void initComponent(){
        back=findViewById(R.id.back);
        back.setOnClickListener(this);
        title=findViewById(R.id.title);
        title.setText("修改上次保养里程");

        maintenance_mileage_input=findViewById(R.id.maintenance_mileage_input);

        maintenance_mileage_commit=findViewById(R.id.maintenance_mileage_commit);
        maintenance_mileage_commit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.maintenance_mileage_commit:
                String mileage=maintenance_mileage_input.getText().toString();
                Intent intent=new Intent();
                intent.putExtra("mileage",mileage);
                setResult(RESULT_OK,intent);
                finish();
                break;
        }
    }
}
