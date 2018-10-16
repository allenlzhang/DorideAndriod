package com.carlt.doride.ui.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.doride.base.LoadingActivity;


/**
 * 上次保养里程修改Activity
 *
 * */
public class MaintenanceMileageEditActivity extends LoadingActivity implements OnClickListener{

    private EditText maintenance_mileage_input;//里程输入框
    private TextView maintenance_mileage_commit;//确认修改按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_mlieage_edit);
        initTitle("修改上次保养里程");
        initComponent();
    }

    private void initComponent(){
        maintenance_mileage_input=findViewById(R.id.maintenance_mileage_input);

        maintenance_mileage_commit=findViewById(R.id.maintenance_mileage_commit);
        maintenance_mileage_commit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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
