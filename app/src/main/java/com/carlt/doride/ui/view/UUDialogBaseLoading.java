
package com.carlt.doride.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.doride.YemaApplication;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.protocolparser.BaseParser;


public class UUDialogBaseLoading extends Dialog {
    protected RelativeLayout head;

    protected RelativeLayout body;

    protected RelativeLayout foot;

    protected View loading;

    protected ProgressBar progress;

    protected TextView msg;

    protected LayoutInflater inflater;

    protected  int w_dip = 300;
    
    protected int h_dip = 300;
    
    protected View mainView;
    
    protected int getHdip(){
    	return h_dip;
    }
    public UUDialogBaseLoading(Context context) {
        super(context, R.style.dialog);
        inflater = LayoutInflater.from(context);
        mainView= inflater.inflate(R.layout.dialog_baseloading, null);
        body = (RelativeLayout)mainView.findViewById(R.id.dialog_baseloading_lay_body);
        head = (RelativeLayout)mainView.findViewById(R.id.dialog_baseloading_lay_head);
        foot = (RelativeLayout)mainView.findViewById(R.id.dialog_baseloading_lay_foot);
        loading = mainView.findViewById(R.id.dialog_baseloading_lay_loading);
        progress = (ProgressBar)mainView.findViewById(R.id.dialog_baseloading_loading_bar);
        msg = (TextView)mainView.findViewById(R.id.dialog_baseloading_loading_text);

        int w = (int)(YemaApplication.ScreenDensity * w_dip);
        int h = (int)(YemaApplication.ScreenDensity * h_dip);
        setCanceledOnTouchOutside(false);
        ViewGroup.LayoutParams parm = new ViewGroup.LayoutParams(w, h);
        setContentView(mainView, parm);
        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                return true;
            }
        });
    }

    protected void setBodyView(int viewId) {
        View child = inflater.inflate(viewId, null);
        body.addView(child);
    }

    protected void setHeadView(int viewId) {
        View child = inflater.inflate(viewId, null);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        child.setLayoutParams(params);
        head.addView(child);
    }

    protected void setFootView(int viewId) {
        View child = inflater.inflate(viewId, null);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        child.setLayoutParams(params);
        foot.addView(child);
    }

    /**
     * 加载数据
     */
    protected void LoadData() {
        loading.setVisibility(View.VISIBLE);
        msg.setText("加载中...");
    }

    /**
     * 加载失败
     */
    protected void LoadError(Object erro) {
        loading.setVisibility(View.VISIBLE);
        BaseResponseInfo mBaseResponseInfo = (BaseResponseInfo) erro;
        if (null != mBaseResponseInfo && null != mBaseResponseInfo.getInfo()) {
            msg.setText(mBaseResponseInfo.getInfo());
        } else {
            msg.setText("获取数据失败");
        }
        progress.setVisibility(View.GONE);
    }

    /**
     * 加载成功
     */
    public void LoadSuccess(Object data) {
        loading.setVisibility(View.GONE);
    }

    protected BaseParser.ResultCallback listener = new BaseParser.ResultCallback () {

        @Override
        public void onSuccess(BaseResponseInfo o) {
            Message msg = new Message();
            msg.what = 1;
            msg.obj = o;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onError(BaseResponseInfo o) {
            Message msg = new Message();
            msg.what = 0;
            msg.obj = o;
            mHandler.sendMessage(msg);
        }

    };

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case 0:
                LoadError(msg.obj);
                break;
            case 1:
                LoadSuccess(msg.obj);
                break;

            default:
                break;
            }
        }
    };
}
