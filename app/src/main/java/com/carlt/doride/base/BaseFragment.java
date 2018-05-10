package com.carlt.doride.base;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.doride.data.BaseResponseInfo;

/**
 * Created by liu on 2016/12/23.
 */

public abstract class BaseFragment extends Fragment {
    protected View mView;
    private boolean isDestory = false;
    protected Activity mCtx;

    protected TextView       mTxtDes;// 描述文字
    protected TextView       mTxtEorrorSub;//错误信息副标题
    protected TextView       mTxtNodata;// 没有数据时的描述文字
    protected Button         mTxtRetryError;// 重试（刷新）
    private   ProgressBar    mPBar;
    private   RelativeLayout mLayMain;
    private   View           mMainView;
    private   View           mViewLoading;// 加载View
    private   View           mViewError;// 错误提示View
    private   View           mViewNodata;// 没有数据View

    protected String TAG = getClass().getSimpleName();
    public BaseFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isDestory = false;
        if (mView == null) {
            mCtx = getActivity();
            mView = inflateRootView(inflater);
            init(mView);
        }
        Log.e("BaseFragment",this.getClass().getSimpleName()+"onCreateView");
        return mView;
    }

    /**
     * 添加主View
     */
    private void setMainView(View view) {
        mMainView =view;
        RelativeLayout.LayoutParams mLayoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        mMainView.setLayoutParams(mLayoutParams);
        mLayMain.addView(mMainView);
    }


    /**
     *  添加整体跟布局，返回 子页面布局
     * @param inflater
     * @return 子页面跟布局
     */
    protected View inflateRootView(LayoutInflater inflater){
        View view = inflater.inflate(R.layout.fragment_loading, null, false);
        mLayMain = (RelativeLayout) view.findViewById(R.id.loading_lay_mainlayout);
        mViewLoading = view.findViewById(R.id.laoding_lay_main);
        mViewError = view.findViewById(R.id.error_lay_main);
        mViewNodata = view.findViewById(R.id.nodata_lay_main);

        mTxtDes = (TextView) view.findViewById(R.id.laoding_txt_des);
        mTxtEorrorSub = (TextView) view.findViewById(R.id.error_txt_des_sub);
        mTxtNodata = (TextView) view.findViewById(R.id.nodata_txt_des);
        mTxtRetryError = (Button) view.findViewById(R.id.error_txt_retry);
        mPBar = (ProgressBar) view.findViewById(R.id.loading_bar_loading);
        mTxtRetryError.setOnClickListener(mClickListener);
        View inflateView = inflateView(inflater);
        setMainView(inflateView);
        return view;
    }

    /**
     * 填充子页面
     * @param inflater
     * @return
     */
    protected abstract View inflateView(LayoutInflater inflater);

    private View.OnClickListener mClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.error_txt_retry:
                    loadingDataUI();
                    reTryLoadData();
                    break;
            }

        }
    };

    /**
     * 数据加载成功
     */
    public void loadSuccessUI() {
        mViewLoading.setBackgroundResource(R.drawable.transparent_bg);
        mViewLoading.setVisibility(View.GONE);
        mViewError.setVisibility(View.GONE);
        mViewNodata.setVisibility(View.GONE);
        mMainView.setVisibility(View.VISIBLE);
    }

    /**
     * 加载数据
     */
    protected void loadingDataUI() {
        mTxtRetryError.setVisibility(View.GONE);
        mViewLoading.setBackgroundResource(R.drawable.transparent_bg);
        mViewLoading.setVisibility(View.VISIBLE);
        mViewError.setVisibility(View.GONE);
        mViewNodata.setVisibility(View.GONE);
        mMainView.setVisibility(View.GONE);
        mPBar.setVisibility(View.VISIBLE);
    }

    public void reTryLoadData() {
        //子类选择可以实现，重新加载
    }


    /**
     * 加载失败
     * @param error
     */
    public void loadonErrorUI(BaseResponseInfo error) {
        //并不需要
        mViewLoading.setVisibility(View.GONE);
        mViewError.setVisibility(View.VISIBLE);
        mViewNodata.setVisibility(View.GONE);
        mMainView.setVisibility(View.GONE);
        mPBar.setVisibility(View.GONE);

        BaseResponseInfo mInfo = (BaseResponseInfo) error;
        String info = "";
        if (mInfo != null) {
            info = mInfo.getInfo();
        } else {
            info = "数据加载失败，请重试...";
        }
        mTxtRetryError.setVisibility(View.VISIBLE);
        mTxtEorrorSub.setText(info);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

    }

    @Override
    public void onResume() {
        super.onResume();

        Log.e("BaseFragment",this.getClass().getSimpleName()+"onResume");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("BaseFragment",this.getClass().getSimpleName()+"onDestroy");
        isDestory = true;
        ViewGroup vg = ((ViewGroup) mView.getParent());
        if (vg != null) {
            vg.removeView(mView);
        }
    }

    public void init(View view) {
    }

    /***
     * 加载数据调用
     */
    public abstract void loadData() ;

    /**
     * 简化按 ID 查找
     *
     * @param id
     * @param <T>
     * @return
     */
    public <T> T $ViewByID(int id) {
        return mView == null ? null : (T) mView.findViewById(id);
    }

    /**
     * 简化按 ID 查找
     *
     * @param id
     * @param <T>
     * @return
     */
    public <T> T $ViewByID(View v, int id) {
        return v == null ? null : (T) v.findViewById(id);
    }



}
