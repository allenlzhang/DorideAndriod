
package com.carlt.sesame.ui.activity.safety;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.carlt.doride.DorideApplication;
import com.carlt.sesame.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.data.LoginInfo;
import com.carlt.sesame.data.safety.MobileInfo;
import com.carlt.sesame.data.safety.MobileListInfo;
import com.carlt.sesame.ui.activity.base.LoadingActivityWithTitle;
import com.carlt.sesame.ui.adapter.safety.MobileListAdapter;
import com.carlt.sesame.ui.adapter.safety.MobileListAdapter.OnMobileBtnListener;
import com.carlt.sesame.ui.pull.PullToRefreshBase;
import com.carlt.sesame.ui.pull.PullToRefreshBase.OnRefreshListener;
import com.carlt.sesame.ui.pull.PullToRefreshListView;
import com.carlt.sesame.ui.view.PopBoxCreat;
import com.carlt.sesame.ui.view.PopBoxCreat.DialogWithTitleClick;
import com.carlt.sesame.utility.UUToast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 在线授权设备列表
 * 
 * @author Administrator
 */
public class MobileListActivity extends LoadingActivityWithTitle implements OnClickListener {

    private ImageView back;// 头部返回键

    private TextView title;// 标题文字

    private TextView txtRight;// 头部右侧文字

    private TextView mTxtHostName;// 主机设备名

    private TextView mTxtOption;// 主机后面的操作按钮

    private PullToRefreshListView mPullListView;

    private ListView mListView;

    private TextView mTxtEmpty;// 没有消息时的提示文字

    private final static int LIMIT = 20;

    private Dialog mDialog;

    private ArrayList<MobileInfo> mList = new ArrayList<MobileInfo>();// 数据list

    private MobileListInfo mInfoLists;

    private MobileListAdapter mAdapter;

    private int optionPosition;

    private final static String TYPE_CHANGE = "更换主机";

    private final static String TYPE_CANCEL = "取消";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobilelist);
        setTitleView(R.layout.head_back);

        init();
        initTitle();
        LoadData();
    }

    private void initTitle() {
        back = (ImageView)findViewById(R.id.head_back_img1);
        title = (TextView)findViewById(R.id.head_back_txt1);
        txtRight = (TextView)findViewById(R.id.head_back_txt2);

        back.setImageResource(R.drawable.arrow_back);
        title.setText("授权设备列表");
        txtRight.setVisibility(View.GONE);
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init() {
        mTxtHostName = (TextView)findViewById(R.id.mobilelist_txt_host);
        mTxtOption = (TextView)findViewById(R.id.mobilelist_txt_option);
        mTxtEmpty = (TextView)findViewById(R.id.mobilelist_txt_empty);

        mPullListView = (PullToRefreshListView)findViewById(R.id.mobilelist_list);

        mListView = mPullListView.getRefreshableView();
        mListView.setDivider(getResources().getDrawable(R.drawable.list_divider_bg_line));
        mListView.setDividerHeight(0);
        mListView.setVerticalScrollBarEnabled(false);
        mListView.setSelector(getResources().getDrawable(R.drawable.list_divider_bg));

        mPullListView.setPullLoadEnabled(true);
        mPullListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                // 下拉刷新
                PullDown();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                // 上拉刷新
                PullUp();
            }
        });

        mTxtHostName.setText(LoginInfo.getMainDevicename());
        mTxtOption.setOnClickListener(this);
        mTxtOption.setTag(mTxtOption.getText().toString());
    }

    private void setLastUpdateTime() {
        SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
        String text = mDateFormat.format(new Date(System.currentTimeMillis()));
        mPullListView.setLastUpdatedLabel(text);
    }

    /**
     * 下拉刷新
     */
    private void PullDown() {
        // 调用接口
        CPControl.GetMoblieListResult(LIMIT, 0, DorideApplication.NIMEI, listener);
    }

    /**
     * 上拉获取更多数据
     */
    private void PullUp() {
        // 调用接口
        int offset = mInfoLists.getOffset();
        CPControl.GetMoblieListResult(LIMIT, offset, DorideApplication.NIMEI, listener_loadmore);
    }

    private String isallow = "";

    private OnMobileBtnListener mBtnListener = new OnMobileBtnListener() {
        @Override
        public void onChange(String id, int position) {
            // 更换主机
            
            final MobileInfo mobileInfo= mList.get(position);
            optionPosition = position;
            String name = mobileInfo.getName();
            StringBuffer mBuffer = new StringBuffer();
            mBuffer.append("是否将手机");
            mBuffer.append("\"");
            mBuffer.append(name);
            mBuffer.append("\"");
            mBuffer.append("更换为主机");

            DialogWithTitleClick click = new DialogWithTitleClick() {

                @Override
                public void onRightClick() {
                    // 确定
                    Intent mIntent = new Intent(MobileListActivity.this, VerifyActivity.class);
                    mIntent.putExtra(VerifyActivity.MOBILE_NAME, mobileInfo.getName());
                    mIntent.putExtra(VerifyActivity.MOBILE_ID, mobileInfo.getMobile_id());
                    startActivity(mIntent);
                }

                @Override
                public void onLeftClick() {
                    // 取消

                }
            };
            PopBoxCreat.createDialogNotitle(MobileListActivity.this, "更换主机", mBuffer.toString(),
                    "取消", "确认", click);
        }

        @Override
        public void onDelete(final String id, int position) {
            optionPosition = position;

            // 删除授权设备
            DialogWithTitleClick click = new DialogWithTitleClick() {

                @Override
                public void onRightClick() {
                    // 删除
                    if (mDialog == null) {
                        mDialog = PopBoxCreat.createDialogWithProgress(MobileListActivity.this,
                                "处理中...");
                    }
                    CPControl.GetDelMobileResult(id, listener_del);
                }

                @Override
                public void onLeftClick() {
                    // 取消
                }
            };

            PopBoxCreat.createDialogNotitle(MobileListActivity.this, "删除授权", "删除后该设备登录需重新授权", "取消",
                    "确定", click);

        }

        @Override
        public void onAuthorAgree(final String id, int position) {
            // 同意授权
            optionPosition = position;

            DialogWithTitleClick click = new DialogWithTitleClick() {

                @Override
                public void onRightClick() {
                    // 同意授权
                    isallow = "1";

                    if (mDialog == null) {
                        mDialog = PopBoxCreat.createDialogWithProgress(MobileListActivity.this,
                                "授权中...");
                    }
                    mDialog.show();
                    CPControl.GetDealAuthorize(isallow, id, listener_deal);
                }

                @Override
                public void onLeftClick() {
                    // 取消
                }
            };

            PopBoxCreat.createDialogNotitle(MobileListActivity.this, "同意授权", "同意该设备的授权请求", "取消",
                    "确定", click);

        }

        @Override
        public void onAuthorRefuse(final String id, int position) {
            // 拒绝授权
            optionPosition = position;

            DialogWithTitleClick click = new DialogWithTitleClick() {

                @Override
                public void onRightClick() {
                    // 拒绝授权
                    isallow = "2";

                    if (mDialog == null) {
                        mDialog = PopBoxCreat.createDialogWithProgress(MobileListActivity.this,
                                "处理中...");
                    }
                    mDialog.show();
                    CPControl.GetDealAuthorize(isallow, id, listener_deal);
                }

                @Override
                public void onLeftClick() {
                    // 取消
                }
            };

            PopBoxCreat.createDialogNotitle(MobileListActivity.this, "拒绝授权", "拒绝该设备的授权请求", "取消",
                    "确定", click);
        }
    };

    @Override
    protected void LoadSuccess(Object data) {
        mInfoLists = (MobileListInfo)data;
        if (mInfoLists != null) {
            mList = mInfoLists.getmMobileInfoList();
            if (LoginInfo.isMain()) {
                if(mList!=null&&mList.size()>0){
                    mTxtOption.setVisibility(View.VISIBLE);
                }else{
                    mTxtOption.setVisibility(View.GONE);
                }
            } else {
                mTxtOption.setVisibility(View.GONE);
            }
            String type = (String)mTxtOption.getTag();
            
            if (mAdapter == null) {
                mAdapter = new MobileListAdapter(MobileListActivity.this, mList, mBtnListener);
                if(type.equals(TYPE_CANCEL)){
                    mAdapter.setInChangeStatus(true);
                }else if(type.equals(TYPE_CHANGE)){
                    mAdapter.setInChangeStatus(false);
                }
                mListView.setAdapter(mAdapter);
            } else {
                mAdapter.setmList(mList);
                if(type.equals(TYPE_CANCEL)){
                    mAdapter.setInChangeStatus(true);
                }else if(type.equals(TYPE_CHANGE)){
                    mAdapter.setInChangeStatus(false);
                }
                mAdapter.notifyDataSetChanged();
            }

            if (mList.size() == 0) {
                mPullListView.setVisibility(View.GONE);
                mTxtEmpty.setVisibility(View.VISIBLE);
            } else {
                mPullListView.setVisibility(View.VISIBLE);
                mTxtEmpty.setVisibility(View.GONE);
            }

            mPullListView.onPullDownRefreshComplete();
            mPullListView.onPullUpRefreshComplete();
            setLastUpdateTime();
        }
        super.LoadSuccess(data);

    }

    @Override
    protected void LoadErro(Object erro) {
        super.LoadErro(erro);
    }

    @Override
    protected void LoadData() {
        super.LoadData();
        CPControl.GetMoblieListResult(LIMIT, 0, DorideApplication.NIMEI, listener);
    }

    // 拉取更多数据
    GetResultListCallback listener_loadmore = new GetResultListCallback() {

        @Override
        public void onFinished(Object o) {
            Message msg = new Message();
            msg.what = 0;
            msg.obj = o;
            mHandler.sendMessage(msg);

        }

        @Override
        public void onErro(Object o) {
            Message msg = new Message();
            msg.what = 1;
            msg.obj = o;
            mHandler.sendMessage(msg);

        }
    };

    // 删除授权设备
    GetResultListCallback listener_del = new GetResultListCallback() {

        @Override
        public void onFinished(Object o) {
            Message msg = new Message();
            msg.what = 2;
            msg.obj = o;
            mHandler.sendMessage(msg);

        }

        @Override
        public void onErro(Object o) {
            Message msg = new Message();
            msg.what = 3;
            msg.obj = o;
            mHandler.sendMessage(msg);

        }
    };

    /**
     * 处理授权请求
     */
    private GetResultListCallback listener_deal = new GetResultListCallback() {

        @Override
        public void onFinished(Object o) {
            Message msg = new Message();
            msg.what = 4;
            msg.obj = o;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onErro(Object o) {
            Message msg = new Message();
            msg.what = 5;
            msg.obj = o;
            mHandler.sendMessage(msg);
        }
    };

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    MobileListInfo mMore = (MobileListInfo)msg.obj;
                    if (mInfoLists != null) {
                        mInfoLists.setOffset(mMore.getOffset());
                        mInfoLists.addmMobileInfoList(mMore.getmMobileInfoList());
                        if (mMore.getmMobileInfoList().size() == 0) {
                            mPullListView.setPullLoadEnabled(false);
                        }
                    }
                    LoadSuccess(mInfoLists);
                    break;

                case 1:
                    LoadErro(msg.obj);
                    break;
                case 2:
                    // 删除授权设备成功
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    BaseResponseInfo mInfo2 = (BaseResponseInfo)msg.obj;
                    if (mInfo2 != null) {
                        String info = mInfo2.getInfo();
                        if (info != null && info.length() > 0) {
                            UUToast.showUUToast(MobileListActivity.this, info);
                        } else {
                            UUToast.showUUToast(MobileListActivity.this, "操作成功!");
                        }
                    } else {
                        UUToast.showUUToast(MobileListActivity.this, "操作成功!");
                    }
                    mList.remove(optionPosition);
                    if(mList.size()==0){
                        mTxtOption.setVisibility(View.GONE);
                        mPullListView.setVisibility(View.GONE);
                        mTxtEmpty.setVisibility(View.VISIBLE);
                    }
                    if (mAdapter != null) {
                        mAdapter.setmList(mList);
                        mAdapter.notifyDataSetChanged();
                    }
                    break;
                case 3:
                    // 删除授权设备失败
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    BaseResponseInfo mInfo3 = (BaseResponseInfo)msg.obj;
                    if (mInfo3 != null) {
                        String info = mInfo3.getInfo();
                        if (info != null && info.length() > 0) {
                            UUToast.showUUToast(MobileListActivity.this, info);
                        } else {
                            UUToast.showUUToast(MobileListActivity.this, "操作成功!");
                        }
                    } else {
                        UUToast.showUUToast(MobileListActivity.this, "操作成功!");
                    }
                    break;
                case 4:
                    // 授权成功
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    BaseResponseInfo mInfo = (BaseResponseInfo)msg.obj;
                    if (mInfo != null) {
                        String info = mInfo.getInfo();
                        if (info != null && info.length() > 0) {
                            UUToast.showUUToast(MobileListActivity.this, info);
                        } else {
                            UUToast.showUUToast(MobileListActivity.this, "处理成功！");
                        }
                    } else {
                        UUToast.showUUToast(MobileListActivity.this, "处理成功！");
                    }
                    if (isallow != null) {
                        if (isallow.equals("1")) {
                            // 允许授权-将这一条信息变为已授权
                            MobileInfo mobileInfo = mList.get(optionPosition);
                            mobileInfo.setAuthorize_type(MobileInfo.AUTHORIZE_TYPE_DONE);
                            if (mAdapter != null) {
                                mAdapter.setmList(mList);
                                mAdapter.notifyDataSetChanged();
                            }
                        } else if (isallow.equals("2")) {
                            // 拒绝授权-将这一条信息从列表删除
                            mList.remove(optionPosition);
                            if (mAdapter != null) {
                                mAdapter.setmList(mList);
                                mAdapter.notifyDataSetChanged();
                            }
                            mList = mInfoLists.getmMobileInfoList();
                            if (LoginInfo.isMain()) {
                                if(mList!=null&&mList.size()>0){
                                    mTxtOption.setVisibility(View.VISIBLE);
                                }else{
                                    mTxtOption.setVisibility(View.GONE);
                                }
                            } else {
                                mTxtOption.setVisibility(View.GONE);
                            }
                            if (mList.size() == 0) {
                                mPullListView.setVisibility(View.GONE);
                                mTxtEmpty.setVisibility(View.VISIBLE);
                            } else {
                                mPullListView.setVisibility(View.VISIBLE);
                                mTxtEmpty.setVisibility(View.GONE);
                            }
                        }
                    }

                    break;
                case 5:
                    // 授权失败
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    BaseResponseInfo mInfo1 = (BaseResponseInfo)msg.obj;
                    if (mInfo1 != null) {
                        String info = mInfo1.getInfo();
                        if (info != null && info.length() > 0) {
                            UUToast.showUUToast(MobileListActivity.this, info);
                        } else {
                            UUToast.showUUToast(MobileListActivity.this, "处理失败...");
                        }
                    } else {
                        UUToast.showUUToast(MobileListActivity.this, "处理失败...");
                    }

                    break;
            }
        }

    };

    @Override
    public void onClick(View v) {
        // TODO 更换主机按钮
        switch (v.getId()) {
            case R.id.mobilelist_txt_option:
                String type = (String)v.getTag();
                if (type.equals(TYPE_CHANGE)) {
                    mTxtOption.setText(TYPE_CANCEL);
                    mTxtOption.setTag(TYPE_CANCEL);
                    if (mAdapter != null) {
                        mAdapter.setInChangeStatus(true);
                        mAdapter.notifyDataSetChanged();
                    }
                } else if (type.equals(TYPE_CANCEL)) {
                    mTxtOption.setText(TYPE_CHANGE);
                    mTxtOption.setTag(TYPE_CHANGE);
                    if (mAdapter != null) {
                        mAdapter.setInChangeStatus(false);
                        mAdapter.notifyDataSetChanged();
                    }
                }
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mTxtHostName.setText(LoginInfo.getMainDevicename());
        LoadData();
        
    }

}
