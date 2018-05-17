package com.carlt.doride.ui.activity.home;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.doride.base.LoadingActivity;
import com.carlt.doride.control.CPControl;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.data.car.CarSettingInfo;
import com.carlt.doride.data.home.InformationMessageInfo;
import com.carlt.doride.data.home.InformationMessageInfoList;
import com.carlt.doride.model.LoginInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.protocolparser.DefaultStringParser;
import com.carlt.doride.protocolparser.car.CarSettingInfoParser;
import com.carlt.doride.systemconfig.URLConfig;
import com.carlt.doride.ui.activity.setting.CarManagerActivity;
import com.carlt.doride.ui.activity.setting.MsgManageActivity;
import com.carlt.doride.ui.adapter.InformationCentreTipsAdapter;
import com.carlt.doride.ui.pull.PullToRefreshBase;
import com.carlt.doride.ui.pull.PullToRefreshListView;
import com.carlt.doride.ui.view.PopBoxCreat;
import com.carlt.doride.ui.view.UUDialog;
import com.carlt.doride.ui.view.UUToast;
import com.carlt.doride.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Marlon on 2018/3/16.
 */

public class RemindActivity extends LoadingActivity {

    private TextView mTextViewSecretary;// 车秘书文字

    private PullToRefreshListView mPullListView;

    private ListView mListView;

    private TextView mTxtEmpty;// 没有消息时的提示文字

    private ImageView mImgEmpty;// 没有消息时的提示UI

    private InformationMessageInfoList mInfoLists;// 接口返回的数据结构

    private InformationCentreTipsAdapter mAdapter;

    private ArrayList<InformationMessageInfo> mList = new ArrayList<InformationMessageInfo>();// 数据list

    private String title_s;// 标题文字

    private int type;// 列表类型

    public final static String TIPS_TITLE = "tips_title";

    public final static String TIPS_TYPE = "tips_type";

    private final static int LIMIT = 10;

    private View MaintenanceTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_centre_tips);

        try {
            Intent mIntent = getIntent();
            title_s = mIntent.getStringExtra(TIPS_TITLE);
            type = mIntent.getIntExtra(TIPS_TYPE, -1);
        } catch (Exception e) {
            // TODO: handle exception
        }

        initSubTitle();
        initTitle(title_s);
        init();
//        initData();
    }

    private int count_onstart = 0;

    @Override
    protected void onStart() {
        count_onstart++;
        if (count_onstart > 1) {
            loadSuccessUI();
            long l = 5000;
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    initData();
                }
            }, 1000 * 5);
        }
        super.onStart();
    }

    @Override
    public void reTryLoadData() {
        super.reTryLoadData();
        initData();
    }

    @Override
    protected void initTitle(String titleString) {
        super.initTitle(titleString);
        backTV2.setBackgroundResource(R.drawable.icon_message_manager_bg);
        backTV2.setVisibility(View.VISIBLE);
    }

    private UUDialog mUUDialog;

    private TextView havemainten;

    @Override
    public void onRightClick() {
        Intent intent = new Intent(RemindActivity.this, MsgManageActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();

    }

    // 养护提醒专有功能
    private void initSubTitle() {
        mUUDialog = PopBoxCreat.createDialogWithProgress(RemindActivity.this, "正在加载");
        ImageView mImageViewSecretary = (ImageView) findViewById(R.id.layout_sub_head_img);
        mTextViewSecretary = (TextView) findViewById(R.id.layout_sub_head_txt);

        havemainten = (TextView) findViewById(R.id.activity_career_secretary_tips_havemainten);

        //        View lookmainten = findViewById(R.id.activity_career_secretary_tips_lookmainten);

        View setmainten = findViewById(R.id.activity_career_secretary_tips_car);

        View.OnClickListener l1 = new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                switch (arg0.getId()) {
                    case R.id.activity_career_secretary_tips_havemainten:
                        // 已经保养过
                        if (LoginInfo.isMainten()) {
                            mUUDialog.show();
                            DefaultStringParser parser = new DefaultStringParser(listener_MaintainLog);
                            parser.executePost(URLConfig.getM_MAINTAIN_LOG(), new HashMap());
                        }

                        break;
                    //                    case R.id.activity_career_secretary_tips_lookmainten:
                    //                        // 查看保养建议
                    //                        Intent mIntent2 = new Intent(RemindActivity.this,
                    //                                MaintainLogActivity.class);
                    //                        startActivity(mIntent2);
                    //                        break;
                    case R.id.activity_career_secretary_tips_car:
                        // 不准确点这里
                        Intent mIntent3 = new Intent(RemindActivity.this,
                                CarManagerActivity.class);
                        startActivity(mIntent3);
                        break;
                }

            }
        };
        havemainten.setOnClickListener(l1);
        setmainten.setOnClickListener(l1);

    }

    private BaseParser.ResultCallback listener_MaintainLog = new BaseParser.ResultCallback() {

        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            Message msg = new Message();
            msg.what = 7;
            msg.obj = bInfo;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            Message msg = new Message();
            msg.what = 8;
            msg.obj = bInfo;
            mHandler.sendMessage(msg);
        }
    };

    private void init() {
        MaintenanceTitle = findViewById(R.id.activity_career_secretary_tips_title);
        if (type == InformationMessageInfo.C1_T6) {
            MaintenanceTitle.setVisibility(View.VISIBLE);
        } else {
            MaintenanceTitle.setVisibility(View.GONE);
        }
        mPullListView = (PullToRefreshListView) findViewById(R.id.activity_career_secretary_tips_list);
        mTxtEmpty = (TextView) findViewById(R.id.activity_career_secretary_tips_empty);
        mImgEmpty = (ImageView) findViewById(R.id.activity_career_secretary_img_empty);

        mListView = mPullListView.getRefreshableView();
        mListView.setDivider(getResources().getDrawable(R.drawable.list_divider_bg));
        mListView.setDividerHeight(getResources()
                .getDimensionPixelSize(R.dimen.list_divider_height));
        mListView.setVerticalScrollBarEnabled(false);
        mListView.setSelector(getResources().getDrawable(R.drawable.list_divider_bg));

        mPullListView.setPullLoadEnabled(true);
        mPullListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

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
    }


    @Override
    public void loadDataSuccess(Object data) {
        super.loadDataSuccess(data);
        dissmissWaitingDialog();
        if (data != null) {
            InformationMessageInfoList mLists = (InformationMessageInfoList) ((BaseResponseInfo) data).getValue();
            if (mInfoLists != null) {
                mList = mInfoLists.getmAllList();

                if (mAdapter == null) {
                    mAdapter = new InformationCentreTipsAdapter(RemindActivity.this, mList,
                            mBottomClickListner);
                    mListView.setAdapter(mAdapter);
                } else {
                    mAdapter.setmList(mList);
                    mAdapter.notifyDataSetChanged();
                }

                if (mList.size() == 0) {
                    mPullListView.setVisibility(View.GONE);
                    if (type == InformationMessageInfo.C1_T6) {
                        mImgEmpty.setVisibility(View.GONE);
                    } else {
                        mImgEmpty.setVisibility(View.VISIBLE);
                    }
                    mTxtEmpty.setVisibility(View.VISIBLE);
                } else {
                    mPullListView.setVisibility(View.VISIBLE);
                    mImgEmpty.setVisibility(View.GONE);
                    mTxtEmpty.setVisibility(View.GONE);
                }

                mPullListView.onPullDownRefreshComplete();
                mPullListView.onPullUpRefreshComplete();
                setLastUpdateTime();
            }
        } else {

        }
    }


    protected void initData() {
        showWaitingDialog(null);
        if (type > 0) {
            if (type == InformationMessageInfo.C1_T6) {
                getCarInfo();
            } else {
            }
            CPControl.GetInformationMessageResult(mCallback, type);
        } else {
            loadSuccessUI();
        }

    }

    @Override
    public void loadDataError(Object bInfo) {
        super.loadDataError(bInfo);
        dissmissWaitingDialog();
    }

    @Override
    public void loadonErrorUI(BaseResponseInfo error) {
        super.loadonErrorUI(error);
        dissmissWaitingDialog();
    }

    private void getCarInfo() {
        CarSettingInfoParser parser = new CarSettingInfoParser(listener_secretary);
        HashMap<String, String> params = new HashMap<>();
        parser.executePost(URLConfig.getM_GET_CAR_SETTING(), params);
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
        if (type > 0) {
            CPControl.GetInformationMessageResult(mCallback, type);
        }
    }

    /**
     * 上拉获取更多数据
     */
    private void PullUp() {
        if (type > 0) {
            int offset = mList.size();
            CPControl.GetInformationMessageResult(mCallback, type);
        }

    }

    private Dialog mDialog;

    /**
     * 保养提醒 车秘书需要下次保养里程和时间
     */
    BaseParser.ResultCallback listener_secretary = new BaseParser.ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            Message msg = new Message();
            msg.what = 9;
            msg.obj = bInfo;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            Message msg = new Message();
            msg.what = 10;
            msg.obj = bInfo;
            mHandler.sendMessage(msg);
        }
    };

    private int dele_position;

    private InformationCentreTipsAdapter.OnBottomClickListner mBottomClickListner = new InformationCentreTipsAdapter.OnBottomClickListner() {

        @Override
        public void onDelete(final InformationMessageInfo info, final int position) {
            PopBoxCreat.DialogWithTitleClick click = new PopBoxCreat.DialogWithTitleClick() {

                @Override
                public void onLeftClick() {
                    // 确定
                    if (mDialog == null) {
                        mDialog = PopBoxCreat.createDialogWithProgress(RemindActivity.this,
                                "数据提交中...");
                    }
                    int class1 = info.getClass1();
                    String id = info.getId();
                    if (class1 > 0 && id != null && id.length() > 0) {
                        mDialog.show();
                        CPControl.GetRemindDeleteResult(listener_tip, class1, Integer.parseInt(id));
                        dele_position = position;
                    }
                }

                @Override
                public void onRightClick() {
                    // 取消
                }

            };
            PopBoxCreat.createDialogWithTitle(RemindActivity.this, "提示", "您确定要删除该消息吗？", "",
                    "确定", "取消", click);
        }

        //
        @Override
        public void onAction(InformationMessageInfo info) {
            InformationMessageInfo mInfo = info;
            int class1 = mInfo.getClass1();
            int class2 = mInfo.getClass2();
            switch (class1) {

                case 41:
                    // 41 行车信息

                    Intent mIntent4;
                    switch (class2) {
                        case InformationMessageInfo.C1_T4_T1:
                            mIntent4 = new Intent(RemindActivity.this, ReportActivity.class);
                            mIntent4.putExtra("c", 0);
                            mIntent4.putExtra(ReportActivity.DAY_INITIAL, mInfo.getDate());
                            startActivity(mIntent4);
                            break;
                        case InformationMessageInfo.C1_T4_T3:
                            mIntent4 = new Intent(RemindActivity.this, ReportActivity.class);
                            mIntent4.putExtra("c", 1);
                            mIntent4.putExtra(ReportActivity.MONTH_INITIAL, mInfo.getDate());
                            startActivity(mIntent4);
                            break;
                    }

            }

        }
    };


    //删除车秘书提醒回调
    BaseParser.ResultCallback listener_tip = new BaseParser.ResultCallback() {

        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            Message msg = new Message();
            msg.what = 4;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            Message msg = new Message();
            msg.what = 5;
            msg.obj = bInfo;
            mHandler.sendMessage(msg);
        }

    };


    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            BaseResponseInfo mBaseResponseInfo;
            switch (msg.what) {
                case 4:
                    // 删除一条消息成功
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
//                    mList.remove(dele_position);
//                    mAdapter.setmList(mList);
//                    mAdapter.notifyDataSetChanged();
                    if (type > 0) {
                        CPControl.GetInformationMessageResult(mCallback, type);
                    }
                    UUToast.showUUToast(RemindActivity.this, "删除成功！");
                    break;
                case 5:
                    // 删除一条消息失败
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    mBaseResponseInfo = (BaseResponseInfo) msg.obj;
                    if (mBaseResponseInfo != null) {
                        UUToast.showUUToast(RemindActivity.this, mBaseResponseInfo.getInfo());
                    }
                    break;

                case 7:
                    // 点击保养过了成功
                    LoginInfo.setMainten(false);
                    if (LoginInfo.isMainten()) {

                        havemainten.setEnabled(true);
                    } else {
                        havemainten.setEnabled(false);
                    }
                    if (mUUDialog != null && mUUDialog.isShowing()) {
                        mUUDialog.dismiss();
                    }

                    mBaseResponseInfo = (BaseResponseInfo) msg.obj;
                    if (mBaseResponseInfo != null) {
                        UUToast.showUUToast(RemindActivity.this, mBaseResponseInfo.getInfo());
                    }
                    break;

                case 8:
                    if (mUUDialog != null && mUUDialog.isShowing()) {
                        mUUDialog.dismiss();
                    }

                    mBaseResponseInfo = (BaseResponseInfo) msg.obj;
                    if (mBaseResponseInfo != null) {
                        UUToast.showUUToast(RemindActivity.this, mBaseResponseInfo.getInfo());
                    }
                    break;
                case 9:
                    CarSettingInfo info = (CarSettingInfo) ((BaseResponseInfo) msg.obj).getValue();
                    if (type == InformationMessageInfo.C1_T6) {
                        String mainten_next_miles = "";
                        String mainten_next_date = "";
                        if (!StringUtils.isEmpty(info.getMainten_next_miles())) {
                            mainten_next_miles = info.getMainten_next_miles();
                        } else {
                            mainten_next_miles = "--";
                        }

                        if (!StringUtils.isEmpty(info.getMainten_next_date())) {
                            mainten_next_date = info.getMainten_next_date();
                        } else {
                            mainten_next_date = "--";
                        }
                        if (mTextViewSecretary != null) {
                            StringBuffer sb1 = new StringBuffer();
                            sb1.append("您的爱车距下次保养还有 ");
                            sb1.append(mainten_next_miles);
                            sb1.append("公里/");
                            sb1.append(mainten_next_date);
                            sb1.append("天建议您及时带TA进行保养，让TA重新焕发活力");
                            mTextViewSecretary.setText(sb1.toString());
                            if (havemainten != null) {
                                if (LoginInfo.isMainten()) {
                                    havemainten.setEnabled(true);
                                } else {
                                    havemainten.setEnabled(false);
                                }
                            }
                        }

                    }
                    break;

                case 10:
                    if (type == InformationMessageInfo.C1_T6) {
                        if (mTextViewSecretary != null) {
                            StringBuffer sb1 = new StringBuffer();
                            sb1.append("您的爱车距下次保养还有 ");
                            sb1.append("--");
                            sb1.append("公里/");
                            sb1.append("--");
                            sb1.append("天建议您及时带TA进行保养，让TA重新焕发活力");
                            mTextViewSecretary.setText(sb1.toString());
                            if (havemainten != null) {
                                if (LoginInfo.isMainten()) {
                                    havemainten.setEnabled(true);
                                } else {
                                    havemainten.setEnabled(false);
                                }
                            }
                        }

                    }
                    break;

            }
        }

    };
}


