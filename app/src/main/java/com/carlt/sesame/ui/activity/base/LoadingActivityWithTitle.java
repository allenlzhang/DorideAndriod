
package com.carlt.sesame.ui.activity.base;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.doride.ui.view.UUUpdateDialog;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.ui.view.PopBoxCreat;
import com.carlt.sesame.utility.UUToast;

public abstract class LoadingActivityWithTitle extends BaseActivity {

    private RelativeLayout mMainLayout;

    private View mMainView;

    private View mLoadingLayout;

    private TextView mLoadingTextView;

    private View mLoadingBar;

    private RelativeLayout mTitleLay;
    
    private View mLayError;//错误信息展示layout
    
    private TextView mTextError;//错误信息展示text
    
    private TextView mTextRetry;//重试按钮

    private boolean hasData = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.sesame_loading_activity_with_title);
        mMainLayout = (RelativeLayout)findViewById(R.id.loading_activity_with_title_mainlayout);
        mTitleLay = (RelativeLayout)findViewById(R.id.loading_activity_with_title_title);
        mLoadingLayout = findViewById(R.id.loading_activity_with_title_loading_lay);
        mLoadingTextView = (TextView)findViewById(R.id.loading_activity_with_title_loading_text);
        mLoadingBar = findViewById(R.id.loading_activity_with_title_loading_bar);
        
        mLayError=findViewById(R.id.loading_activity_lay_error);
        mTextError=(TextView)findViewById(R.id.loading_activity_text_error);
        mTextRetry=(TextView)findViewById(R.id.loading_activity_text_retry);
        
        mTextRetry.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                LoadData();
            }
        });
        
        mMainView = LayoutInflater.from(this).inflate(layoutResID, null);
        mLoadingLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

            }
        });
        mMainLayout.addView(mMainView, 1);
        // RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
        // mMainView.getLayoutParams());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        int h = (int)getResources().getDimension(R.dimen.head_height) + 1;
        params.setMargins(0, h, 0, 0);
        mMainView.setLayoutParams(params);

    }

    public void setTitleView(int layoutResID) {

        // View mView = LayoutInflater.from(FoodCookBook.this).inflate(
        // R.layout.food_cookbook_childlayout, null);
        mTitleLay.addView(LayoutInflater.from(this).inflate(layoutResID, null));

    }

    public void setTitleView(View title) {
        mTitleLay.addView(title);
    }

    protected void LoadSuccess(Object data) {
        hasData = true;
        mMainView.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.GONE);
        mLayError.setVisibility(View.GONE);

    }

    protected void ManualLoadSuccess() {
        hasData = true;
        mMainView.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.GONE);
        mLayError.setVisibility(View.GONE);
    }

    protected void LoadErro(Object erro) {
        mMainView.setVisibility(View.GONE);
        mLoadingLayout.setVisibility(View.GONE);
        
        BaseResponseInfo mBaseResponseInfo = (BaseResponseInfo)erro;
        hasData = false;
        if (null != mBaseResponseInfo && null != mBaseResponseInfo.getInfo()) {
            mTextError.setText(mBaseResponseInfo.getInfo());
            UUToast.showUUToast(this, mBaseResponseInfo.getInfo());
        } else {
            mTextError.setText("获取数据失败");
            UUToast.showUUToast(this, "获取数据失败");
        }
        mLayError.setVisibility(View.VISIBLE);

    }

    protected GetResultListCallback listener = new GetResultListCallback() {

        @Override
        public void onFinished(Object o) {
            Message msg = new Message();
            msg.what = 1;
            msg.obj = o;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onErro(Object o) {
            Message msg = new Message();
            msg.what = 0;
            msg.obj = o;
            mHandler.sendMessage(msg);

        }
    };

    private UUUpdateDialog mUpdateDialog;// 硬件升级提示框

    private UUUpdateDialog.DialogUpdateListener mDialogUpdateListener = new UUUpdateDialog.DialogUpdateListener() {

        @Override
        public void onFailed() {
            // 升级失败，重新调用当前接口
            LoadData();

        }

        @Override
        public void onSuccess() {
            // 升级成功，重新调用当前接口
            LoadData();
        }
    };
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    BaseResponseInfo mInfo = (BaseResponseInfo)msg.obj;
                    if (mInfo.getFlag() == BaseResponseInfo.DEVICE_UPDATE) {
                        // 盒子升级中
                        hasData = false;
                        mLoadingBar.setVisibility(View.GONE);
                        PopBoxCreat.showUUUpdateDialog(context, mDialogUpdateListener);
                    } else {
                        LoadErro(msg.obj);
                    }
                    break;
                case 1:
                    LoadSuccess(msg.obj);
                    break;

                default:
                    break;
            }
        }
    };

    protected void LoadData() {
        mLoadingBar.setVisibility(View.VISIBLE);
//        mMainView.setVisibility(View.GONE);
        mLoadingTextView.setText("等待中");
        mLoadingLayout.setVisibility(View.VISIBLE);
        mLayError.setVisibility(View.GONE);
    };

    // protected void LoadData(Object o) {
    //
    // mLoadingBar.setVisibility(View.VISIBLE);
    // mLoadingTextView.setText("等待中");
    // mLoadingLayout.setVisibility(View.VISIBLE);
    // };

    @Override
    protected void onResume() {
        Log.e("info", "bbbbb");
        if (!hasData) {
            Log.e("info", "aaaaaaaaaa");
            LoadData();
        }
        super.onResume();
    }

}
