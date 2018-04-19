
package com.carlt.doride.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.doride.YemaApplication;
import com.carlt.doride.data.remote.RemoteFunInfo;

import java.util.ArrayList;


/**
 * 座驾部分相关Dialog
 * 
 * @author Administrator
 */
public class UUDialogRemote extends UUDialogBaseLoading implements
        android.view.View.OnClickListener, OnItemClickListener {
    private TextView title;

    private ImageView imgOption;

    private CarGridView grid;

    private TextView txtNodata;// 没有数据

    private Context mContext;

    private Dialog mDialog;

    private ArrayList<RemoteFunInfo> mDataList;// 远程控制list
//    protected int mh_dip = 540;
//    protected int mh_dip = 300;
    protected int mh_dip = 200;
//    protected int mh_dip = 420;

    protected int[] mh_dips = {210,mh_dip,420,540};


    public UUDialogRemote(Context context) {
        super(context);
        inflater = LayoutInflater.from(context);
        mainView= inflater.inflate(R.layout.dialog_baseloading, null);
        body = (RelativeLayout)mainView.findViewById(R.id.dialog_baseloading_lay_body);
        head = (RelativeLayout)mainView.findViewById(R.id.dialog_baseloading_lay_head);
        foot = (RelativeLayout)mainView.findViewById(R.id.dialog_baseloading_lay_foot);
        loading = mainView.findViewById(R.id.dialog_baseloading_lay_loading);
        progress = (ProgressBar)mainView.findViewById(R.id.dialog_baseloading_loading_bar);
        msg = (TextView)mainView.findViewById(R.id.dialog_baseloading_loading_text);

        int w = (int)(YemaApplication.ScreenDensity * w_dip);
        int h = (int)(YemaApplication.ScreenDensity * mh_dip);
        setCanceledOnTouchOutside(false);
        ViewGroup.LayoutParams parm = new ViewGroup.LayoutParams(w, h);
        setContentView(mainView, parm);
        setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                return true;
            }
        });
        mContext = context;
        setHeadView(R.layout.dialog_head);
        setFootView(R.layout.dialog_foot);
        setBodyView(R.layout.dialog_car);

        mDialog = PopBoxCreat.createDialogWithProgress(mContext, "正在操作，请稍等...");

        title = (TextView)mainView.findViewById(R.id.dialog_head_txt_title);
        grid = (CarGridView)mainView.findViewById(R.id.dialog_car_grid);
        txtNodata = (TextView)mainView.findViewById(R.id.dialog_car_txt_nodata);
        grid.setScrollable(false);
        imgOption = (ImageView)mainView.findViewById(R.id.dialog_foot_img_option);
        imgOption.setOnClickListener(this);

        LoadData();
    }

    public void refushView(int mh){
        int w = (int)(YemaApplication.ScreenDensity * w_dip);
        int h = (int)(YemaApplication.ScreenDensity * mh);
        setCanceledOnTouchOutside(false);
        ViewGroup.LayoutParams parm = new ViewGroup.LayoutParams(w, h);
        setContentView(mainView, parm);
    }

    @Override
    protected void LoadData() {
        super.LoadData();
    }

    @Override
    protected void LoadError(Object erro) {
        // TODO Auto-generated method stub
        super.LoadError(erro);
    }

    @Override
    public void LoadSuccess(Object data) {
        mDataList = (ArrayList<RemoteFunInfo>)data;
        if (mDataList != null) {
            int size = mDataList.size();
            if (size > 0) {
                setGridAttr(mDataList, 3);
            } else {
                txtNodata.setVisibility(View.VISIBLE);
                txtNodata.setText("您的爱车暂不支持该功能...");
            }

            if(size <= 2){
            	refushView(mh_dips[0]);
            }else if(size >2 && size <= 4){
            	refushView(mh_dips[1]);
            }else if(size >4 && size <= 6){
            	refushView(mh_dips[2]);
            }else if(size >6 && size <= 8){
            	refushView(mh_dips[3]);
            }

            grid.setNumColumns(size);

        } else {
            txtNodata.setVisibility(View.VISIBLE);
            txtNodata.setText("您的爱车暂不支持该功能...");
        }

        super.LoadSuccess(data);
    }

    public void setTitleMsg(String titleMsg) {
        if (titleMsg != null && titleMsg.length() > 0) {
            title.setText(titleMsg);
        }
    }

    public void setOptionImg(int resId) {
        if (resId > 0) {
            imgOption.setImageResource(resId);
        }
    }

    CarRemoteAdapter  adapter;

    public void setGridAttr(ArrayList<RemoteFunInfo> mDataList, int columnNum) {
        this.mDataList = mDataList;
        adapter = new CarRemoteAdapter(mContext, mDataList);
        grid.setAdapter((ListAdapter) adapter);
        grid.setNumColumns(columnNum);
        grid.setOnItemClickListener(this);
    }

    private void refreshData(ArrayList<RemoteFunInfo> mDataList) {
        adapter.setmRemoteFunInfos(mDataList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        // 关闭dialog
        dismiss();
    }

    android.view.View.OnClickListener mItemClick1;
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    	if(null != mItemClick1){
    		view.setTag(mDataList.get(position));
    		mItemClick1.onClick(view);
    	}
    }

	public void setItemClick(android.view.View.OnClickListener mItemClick12) {
		this.mItemClick1 = mItemClick12;
	}


}
