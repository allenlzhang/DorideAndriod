package com.carlt.doride.ui.activity.scan;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.doride.R;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

// 扫码 Activity
public class ScanActivity extends AppCompatActivity {
    CaptureManager       capture;
    DecoratedBarcodeView barcodeView ;
    ImageView            back ;
    TextView             tittle ;
    Bundle mBundle ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        mBundle = savedInstanceState;
        initView();

    }

    private void initView() {
        tittle  = findViewById(R.id.layout_title_back_txt1);
        back  = findViewById(R.id.layout_title_back_img1);
        barcodeView  = findViewById(R.id.dbv_custom);
//        backTv  = findViewById(R.id.back_tv);
        tittle.setText("扫描二维码");
        capture = new CaptureManager(this, barcodeView);
        capture.initializeFromIntent(getIntent(), mBundle);
        capture.decode();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();


    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        capture.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}
