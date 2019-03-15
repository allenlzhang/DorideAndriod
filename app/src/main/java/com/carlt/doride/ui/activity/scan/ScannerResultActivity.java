package com.carlt.doride.ui.activity.scan;

import android.os.Bundle;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.doride.base.LoadingActivity;

public class ScannerResultActivity extends LoadingActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner_result);
        initTitle("扫描结果");
        loadSuccessUI();
        TextView tvResult = findViewById(R.id.tvResult);
        String codeResult = getIntent().getStringExtra("codeResult");
        tvResult.setText(codeResult);
    }
}
