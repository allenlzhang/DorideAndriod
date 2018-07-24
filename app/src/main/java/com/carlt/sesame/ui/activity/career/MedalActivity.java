
package com.carlt.sesame.ui.activity.career;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.sesame.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.data.career.MedalInfo;
import com.carlt.sesame.data.career.UserLicenseInfo;
import com.carlt.sesame.data.career.UserMediaInfo;
import com.carlt.sesame.ui.activity.base.LoadingActivityWithTitle;
import com.carlt.sesame.ui.adapter.MedalAdapter;

import java.util.ArrayList;

/**
 * 我的勋章页面
 * 
 * @author daisy
 */
public class MedalActivity extends LoadingActivityWithTitle {
    private ImageView back;// 头部返回键

    private TextView title;// 标题文字

    private TextView txtRight;// 头部右侧文字

    private GridView mGridView; // 勋章

    private MedalAdapter mAdapter;

    private Intent mIntent;

    private ArrayList<MedalInfo> mArrayList;

    private UserMediaInfo mUserMediaInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_career_medal);
        setTitleView(R.layout.head_back);
        initTitle();
        init();
        UserLicenseInfo licenseInfo = null;
        try {
            licenseInfo = (UserLicenseInfo)getIntent().getSerializableExtra("UserLicenseInfo");
        } catch (Exception e) {
        }
        if (licenseInfo != null) {
            LoadSuccess(licenseInfo);
        } else {
            LoadData();
        }

    }

    private void initTitle() {
        back = (ImageView)findViewById(R.id.head_back_img1);
        title = (TextView)findViewById(R.id.head_back_txt1);
        txtRight = (TextView)findViewById(R.id.head_back_txt2);

        back.setImageResource(R.drawable.arrow_back);
        title.setText("我的勋章(0)");
        txtRight.setText("晒一晒");
        txtRight.setVisibility(View.GONE);

        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void init() {
        mGridView = (GridView)findViewById(R.id.activity_career_medal_gridview);
    }

    private int count;

    @Override
    protected void LoadSuccess(Object data) {
        UserLicenseInfo licenseInfo = (UserLicenseInfo)data;
        mUserMediaInfo = licenseInfo.getmUserMediaInfo();

        mArrayList = mUserMediaInfo.getmList();
        if (mArrayList != null) {
            mAdapter = new MedalAdapter(this, mArrayList);
            mGridView.setAdapter(mAdapter);

            for (int i = 0; i < mArrayList.size(); i++) {
                if (mArrayList.get(i).isIsgot()) {
                    count++;
                }
            }
            title.setText("我的勋章（" + count + "）");
        }

        txtRight.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // 晒一晒
                if (mUserMediaInfo != null) {
//                    ShareControl.share(MedalActivity.class, MedalActivity.this,
//                            mUserMediaInfo.getMediaShareTitle(),
//                            mUserMediaInfo.getMediaShareText(), mUserMediaInfo.getMediaShareLink(),
//                            mGridView);
                }
            }
        });
        super.LoadSuccess(data);
    }

    @Override
    protected void LoadErro(Object erro) {
        super.LoadErro(erro);
    }

    @Override
    protected void LoadData() {
        super.LoadData();
        CPControl.GetMyLicenseResult(listener);
    }

    @Override
    public void OnImgLoadFinished(String url, Bitmap mBitmap) {
        super.OnImgLoadFinished(url, mBitmap);
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

}
