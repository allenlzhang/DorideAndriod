package com.carlt.doride.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.doride.control.ActivityControl;
import com.carlt.doride.data.ActivateInfo;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.model.LoginInfo;
import com.carlt.doride.systemconfig.URLConfig;
import com.carlt.sesame.ui.view.PopBoxCreat;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by liu on 2016/12/23.
 */

public abstract class BaseFragment extends Fragment {
    protected boolean mIsShowing = false;
    protected View mView;
    private boolean isDestory = false;
    protected Activity mCtx;

    protected TextView       mTxtDes;// 描述文字
    protected TextView       mTxtEorrorSub;//错误信息副标题
    protected TextView       mTxtNodata;// 没有数据时的描述文字
    protected Button         mTxtRetryError;// 重试（刷新）
    private   ProgressBar    mPBar;
    private   RelativeLayout mLayMain;
    public   View           mMainView;
    public    View           mViewLoading;// 加载View
    public    View           mViewError;// 错误提示View
    public    View           mViewNodata;// 没有数据View
    private   ImageView      mIvErrorIcon;  //错误图片
    protected String TAG = getClass().getSimpleName();
    /** 进入后台之前 要干的事的集合 */
    protected static ArrayList<BeforeGoToBackground> mBackDoList = new ArrayList<BeforeGoToBackground>();
    public BaseFragment() {
    }
    protected void registerBeforeGoToBackGround(BeforeGoToBackground listener) {
        if(!mBackDoList.contains(listener)){
            mBackDoList.add(listener);
        }
    }

    protected void unRegisterBeforeGoToBackGround(BeforeGoToBackground listener) {
        mBackDoList.remove(listener);
    }

    private static void HandleBeforeGoToBackGround() {
        Log.e("info", "baseactivity____doBeforeGoToBackGround()>>>>>>>>>>>>>>>没有页面显示了");
        for (int i = 0; i < mBackDoList.size(); i++) {
            try {
                mBackDoList.get(i).doBeforeGoToBackground();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
        Log.e("BaseFragment", this.getClass().getSimpleName() + "onCreateView");
        //        getActivateStatus();
        return mView;
    }

    protected void getActivateStatus(final String info) {
        OkGo.<String>post(URLConfig.getCheckIsActivate_URL())
                .params("client_id", URLConfig.getClientID())
                .params("token", LoginInfo.getAccess_token())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Logger.e(response.body());
                        String body = response.body();
                        Gson gson = new Gson();
                        JSONObject jo = null;
                        try {
                            jo = new JSONObject(response.body());
                            int code = jo.getInt("code");
//                            String msg = jo.getString("msg");
                            if (code == 200) {
                                ActivateInfo activateInfo = gson.fromJson(body, ActivateInfo.class);
                                if (activateInfo.data.activate_status == 1) {
                                    showTipDialog(info);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //                        activateInfo.data.activate_status = 1;

                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                    }
                });
    }

    private void showTipDialog(String info) {
        PopBoxCreat.createDialogNotitleOneBtn(mCtx, "温馨提示", info, "确定", new PopBoxCreat.DialogWithTitleClick() {
            @Override
            public void onLeftClick() {
            }

            @Override
            public void onRightClick() {
            }
        });
    }

    /**
     * 添加主View
     */
    private void setMainView(View view) {
        mMainView = view;
        FrameLayout.LayoutParams mLayoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        mMainView.setLayoutParams(mLayoutParams);
        mLayMain.addView(mMainView);
    }

    public FrameLayout fl_base_content;

    /**
     * 添加整体跟布局，返回 子页面布局
     * @param inflater
     * @return 子页面跟布局
     */
    protected View inflateRootView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_loading, null, false);
        fl_base_content = view.findViewById(R.id.fl_base_content);
        mLayMain = (RelativeLayout) view.findViewById(R.id.loading_lay_mainlayout);
        mViewLoading = view.findViewById(R.id.laoding_lay_main);
        mViewError = view.findViewById(R.id.error_lay_main);
        mViewNodata = view.findViewById(R.id.nodata_lay_main);
        mIvErrorIcon = view.findViewById(R.id.info_icon);
        mTxtDes = (TextView) view.findViewById(R.id.laoding_txt_des);
        mTxtEorrorSub = (TextView) view.findViewById(R.id.error_txt_des_sub);
        mTxtNodata = (TextView) view.findViewById(R.id.nodata_txt_des);
        mTxtRetryError = (Button) view.findViewById(R.id.error_txt_retry);
        mPBar = (ProgressBar) view.findViewById(R.id.loading_bar_loading);
        mTxtRetryError.setOnClickListener(mClickListener);
        //        View inflateView = inflateView(inflater);
        //        setMainView(inflateView);
        fl_base_content.removeAllViews();
        fl_base_content.addView(inflateView(inflater));
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
//                mMainView.setVisibility(View.VISIBLE);
    }
    /**
     * 没有数据时调用
     */
    protected void loadNodata() {
        mViewLoading.setVisibility(View.GONE);
        mView.setVisibility(View.GONE);
        mViewNodata.setVisibility(View.VISIBLE);
//        mMainView.setVisibility(View.GONE);
    }
    /**
     * 加载数据
     */
    protected void loadingDataUI() {
        mTxtRetryError.setVisibility(View.GONE);
        mViewLoading.setBackgroundResource(R.drawable.transparent_bg);
        mViewLoading.setVisibility(View.VISIBLE);
//                mViewError.setVisibility(View.GONE);
//                mViewNodata.setVisibility(View.GONE);
//                mMainView.setVisibility(View.GONE);
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
//                mViewError.setVisibility(View.VISIBLE);
//                mViewNodata.setVisibility(View.GONE);
//                mMainView.setVisibility(View.GONE);
        mPBar.setVisibility(View.GONE);

        BaseResponseInfo mInfo = (BaseResponseInfo) error;
        String info = "";
        if (mInfo != null) {
            info = mInfo.getInfo();
        } else {
            info = "数据加载失败，请重试...";
        }
        if (mInfo.getFlag() == BaseResponseInfo.ERRO) {
            mIvErrorIcon.setImageResource(R.mipmap.icon_error);
        } else {
            mIvErrorIcon.setImageResource(R.mipmap.icon_error_bg);
        }
        mTxtRetryError.setVisibility(View.VISIBLE);
        mTxtEorrorSub.setText(info);
                mViewError.setVisibility(View.VISIBLE);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

    }

    @Override
    public void onResume() {
        super.onResume();
        mIsShowing = true;
        Log.e("BaseFragment", this.getClass().getSimpleName() + "onResume");
    }

    @Override
    public void onStop() {
        super.onStop();
        mIsShowing = false;
        if (!ActivityControl.anyActivtyShowing()) {
            HandleBeforeGoToBackGround();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("BaseFragment", this.getClass().getSimpleName() + "onDestroy");
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
    public abstract void loadData();

    /**
     * 简化按 ID 查找
     * @param id
     * @param <T>
     * @return
     */
    public <T> T $ViewByID(int id) {
        return mView == null ? null : (T) mView.findViewById(id);
    }

    /**
     * 简化按 ID 查找
     * @param id
     * @param <T>
     * @return
     */
    public <T> T $ViewByID(View v, int id) {
        return v == null ? null : (T) v.findViewById(id);
    }


    public void hideView(){
        mViewLoading.setVisibility(View.GONE);
    }

}
