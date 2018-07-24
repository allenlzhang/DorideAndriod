
package com.carlt.sesame.ui.activity.career;

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

import com.carlt.doride.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.data.LoginInfo;
import com.carlt.sesame.data.career.SecretaryMessageInfo;
import com.carlt.sesame.data.career.SecretaryMessageInfoList;
import com.carlt.sesame.ui.activity.base.LoadingActivityWithTitle;
import com.carlt.sesame.ui.activity.car.CarFillIllegalActivity;
import com.carlt.sesame.ui.activity.car.CarQueryIllegalActivity;
import com.carlt.sesame.ui.activity.car.CarTestActivity;
import com.carlt.sesame.ui.activity.car.CarTirePressureActivity;
import com.carlt.sesame.ui.activity.car.TestFirstView;
import com.carlt.sesame.ui.activity.car.TestFirstView.OnTestBtnClick;
import com.carlt.sesame.ui.activity.career.report.ReportActivity;
import com.carlt.sesame.ui.activity.setting.ManageCarActivity;
import com.carlt.sesame.ui.activity.setting.ManageMessageActivity;
import com.carlt.sesame.ui.adapter.SecretaryTipsAdapterNew;
import com.carlt.sesame.ui.adapter.SecretaryTipsAdapterNew.OnBottomClickListner;
import com.carlt.sesame.ui.pull.PullToRefreshBase;
import com.carlt.sesame.ui.pull.PullToRefreshBase.OnRefreshListener;
import com.carlt.sesame.ui.pull.PullToRefreshListView;
import com.carlt.sesame.ui.view.PopBoxCreat;
import com.carlt.sesame.ui.view.PopBoxCreat.DialogWithTitleClick;
import com.carlt.sesame.ui.view.UUDialog;
import com.carlt.sesame.utility.UUToast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 车秘书消息列表
 * @author daisy
 */
public class SecretaryTipsActivity extends LoadingActivityWithTitle {
    private ImageView back;// 头部返回键

    private TextView title;// 标题文字

    private TextView txtRight;// 头部右侧文字

    private TextView mTextViewSecretary;// 车秘书文字

    private PullToRefreshListView mPullListView;

    private ListView mListView;

    private TextView mTxtEmpty;// 没有消息时的提示文字

    private SecretaryMessageInfoList mInfoLists;// 接口返回的数据结构

    private SecretaryTipsAdapterNew mAdapter;

    private ArrayList<SecretaryMessageInfo> mList = new ArrayList<SecretaryMessageInfo>();// 数据list

    private String title_s;// 标题文字

    private int type;// 列表类型

    public final static String TIPS_TITLE = "tips_title";

    public final static String TIPS_TYPE = "tips_type";

    private final static int LIMIT = 10;

    private View MaintenanceTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_career_secretary_tips);
        setTitleView(R.layout.head_back);

        try {
            Intent mIntent = getIntent();
            title_s = mIntent.getStringExtra(TIPS_TITLE);
            type = mIntent.getIntExtra(TIPS_TYPE, -1);
        } catch (Exception e) {
            // TODO: handle exception
        }

        initSubTitle();
        initTitle();
        init();
        LoadData();
    }

    private int count_onstart = 0;

    @Override
    protected void onStart() {

        count_onstart++;
        if (count_onstart > 1) {
            super.LoadData();
            long l = 5000;
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    LoadData();

                }
            }, 1000 * 5);
        }
        super.onStart();
    }

    private void initTitle() {
        back = (ImageView) findViewById(R.id.head_back_img1);
        title = (TextView) findViewById(R.id.head_back_txt1);
        txtRight = (TextView) findViewById(R.id.head_back_txt2);

        back.setImageResource(R.drawable.arrow_back);
        title.setText(title_s);
        txtRight.setText("消息设置");

        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtRight.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent mIntent = new Intent(SecretaryTipsActivity.this, ManageMessageActivity.class);
                startActivity(mIntent);
            }
        });

    }

    private UUDialog mUUDialog;

    private TextView havemainten;

    @Override
    protected void onResume() {
        super.onResume();
        if (mTextViewSecretary != null) {
            StringBuffer sb1 = new StringBuffer();
            sb1.append("您的爱车距下次保养还有 ");
            sb1.append(LoginInfo.getMainten_next_miles());
            sb1.append("公里/");
            sb1.append(LoginInfo.getMainten_next_day());
            sb1.append("天建议您及时带TA进行保养，让TA重新焕发活力");
            mTextViewSecretary.setText(sb1.toString());

            if (havemainten != null) {
                if (LoginInfo.isMainten()) {
                    havemainten.setTextColor(getResources().getColor(R.color.cheng1));
                    havemainten
                            .setBackgroundResource(R.drawable.activity_career_secretary_tips_kuang);
                } else {
                    havemainten
                            .setBackgroundResource(R.drawable.activity_career_secretary_tips_kuang_noclick);
                    havemainten.setTextColor(getResources().getColor(R.color.text_color_gray1));
                }
            }
        }

    }

    // 养护提醒专有功能
    private void initSubTitle() {
        mUUDialog = PopBoxCreat.createDialogWithProgress(SecretaryTipsActivity.this, "正在加载");
        ImageView mImageViewSecretary = (ImageView) findViewById(R.id.layout_sub_head_img);
        mTextViewSecretary = (TextView) findViewById(R.id.layout_sub_head_txt);

        havemainten = (TextView) findViewById(R.id.activity_career_secretary_tips_havemainten);

        View lookmainten = findViewById(R.id.activity_career_secretary_tips_lookmainten);

        View setmainten = findViewById(R.id.activity_career_secretary_tips_setmainten);

        OnClickListener l1 = new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                switch (arg0.getId()) {
                    case R.id.activity_career_secretary_tips_havemainten:
                        // 已经保养过
                        if (LoginInfo.isMainten()) {
                            mUUDialog.show();
                            CPControl.GetMaintainLogResult(listener_MaintainLog);
                        }

                        break;
                    case R.id.activity_career_secretary_tips_lookmainten:
                        // 查看保养建议
                        Intent mIntent2 = new Intent(SecretaryTipsActivity.this,
                                MaintainLogActivity.class);
                        startActivity(mIntent2);
                        break;
                    case R.id.activity_career_secretary_tips_setmainten:
                        // 设置保养时间
                        Intent mIntent3 = new Intent(SecretaryTipsActivity.this,
                                ManageCarActivity.class);
                        startActivity(mIntent3);
                        break;
                }

            }
        };
        havemainten.setOnClickListener(l1);
        lookmainten.setOnClickListener(l1);
        setmainten.setOnClickListener(l1);

    }

    private GetResultListCallback listener_MaintainLog = new GetResultListCallback() {

        @Override
        public void onFinished(Object o) {
            Message msg = new Message();
            msg.what = 7;
            msg.obj = o;
            mHandler.sendMessage(msg);

        }

        @Override
        public void onErro(Object o) {
            Message msg = new Message();
            msg.what = 8;
            msg.obj = o;
            mHandler.sendMessage(msg);

        }
    };

    private void init() {
        MaintenanceTitle = findViewById(R.id.activity_career_secretary_tips_title);
        if (type == SecretaryMessageInfo.C1_T6) {
            MaintenanceTitle.setVisibility(View.VISIBLE);
        } else {
            MaintenanceTitle.setVisibility(View.GONE);
        }
        mPullListView = (PullToRefreshListView) findViewById(R.id.activity_career_secretary_tips_list);
        mTxtEmpty = (TextView) findViewById(R.id.activity_career_secretary_tips_empty);

        mListView = mPullListView.getRefreshableView();
        mListView.setDivider(getResources().getDrawable(R.drawable.list_divider_bg));
        mListView.setDividerHeight(getResources()
                .getDimensionPixelSize(R.dimen.list_divider_height));
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
    }

    @Override
    protected void LoadSuccess(Object data) {
        if (data != null) {
            mInfoLists = (SecretaryMessageInfoList) data;
            if (mInfoLists != null) {
                mList = mInfoLists.getmAllList();
                if (mAdapter == null) {
                    mAdapter = new SecretaryTipsAdapterNew(SecretaryTipsActivity.this, mList,
                            mBottomClickListner);
                    mListView.setAdapter(mAdapter);
                } else {
                    mAdapter.setmList(mList);
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
        } else {

        }
        super.LoadSuccess(data);
    }

    @Override
    protected void LoadErro(Object erro) {
        // TODO Auto-generated method stub
        super.LoadErro(erro);
    }

    @Override
    protected void LoadData() {
        super.LoadData();
        if (type > 0) {
            CPControl.GetSecretaryMessageResult(LIMIT, 0, type, listener);
        } else {
            LoadSuccess(null);
        }

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
            CPControl.GetSecretaryMessageResult(LIMIT, 0, type, listener);
        }
    }

    /**
     * 上拉获取更多数据
     */
    private void PullUp() {
        if (type > 0) {
            int offset = mInfoLists.getOffset();
            CPControl.GetSecretaryMessageResult(LIMIT, offset, type, listener_loadmore);
        }

    }

    private Dialog mDialog;

    private TestFirstView mTestFirstView;

    private OnTestBtnClick mOnTestBtnClick = new OnTestBtnClick() {

        @Override
        public void onClick() {
            Intent mIntent = new Intent(SecretaryTipsActivity.this, CarTestActivity.class);
            startActivity(mIntent);

        }
    };

    private int dele_position;

    private OnBottomClickListner mBottomClickListner = new OnBottomClickListner() {

        @Override
        public void onDelete(final SecretaryMessageInfo info, final int position) {
            DialogWithTitleClick click = new DialogWithTitleClick() {

                @Override
                public void onLeftClick() {
                    // 确定
                    if (mDialog == null) {
                        mDialog = PopBoxCreat.createDialogWithProgress(SecretaryTipsActivity.this,
                                "数据提交中...");
                    }
                    int class1 = info.getClass1();
                    String id = info.getId();
                    if (class1 > 0 && id != null && id.length() > 0) {
                        mDialog.show();
                        CPControl.GetSecretaryDeleteResult(class1, id, listener_tip);
                        dele_position = position;
                    }
                }

                @Override
                public void onRightClick() {
                    // 取消
                }

            };
            PopBoxCreat.createDialogWithTitle(SecretaryTipsActivity.this, "提示", "您确定要删除该消息吗？", "",
                    "确定", "取消", click);
        }

        @Override
        public void onAction(SecretaryMessageInfo info) {
            SecretaryMessageInfo mInfo = info;
            int class1 = mInfo.getClass1();
            int class2 = mInfo.getClass2();
            switch (class1) {
                case 11:
                    // 11 用车提醒
                    if (class2 == SecretaryMessageInfo.C1_T1_T1) {
                        // 预约
                        Intent mIntent1 = new Intent(SecretaryTipsActivity.this,
                                SecretaryAppointmentActivity.class);
                        startActivity(mIntent1);
                    } else if (class2 == SecretaryMessageInfo.C1_T1_T2) {
                        Intent mIntent1 = new Intent(SecretaryTipsActivity.this,
                                AddressMapActivity.class);
                        startActivity(mIntent1);
                    } else if (class2 == SecretaryMessageInfo.C1_T1_T3) {
                        // 违章详情
                        if (LoginInfo.getCanQueryVio() == "0") {
                            // 暂无车辆信息
                            Intent mIntent1 = new Intent(SecretaryTipsActivity.this,
                                    CarFillIllegalActivity.class);
                            startActivity(mIntent1);
                        } else {
                            // 已有车辆信息
                            Intent mIntent1 = new Intent(SecretaryTipsActivity.this,
                                    CarQueryIllegalActivity.class);
                            startActivity(mIntent1);
                        }
                    } else if (class2 == SecretaryMessageInfo.C1_T1_T4) {
                        // 激活盒子，跳转至爱车体检页面
                        if (LoginInfo.getBuydate().equals("")) {
                            if (mTestFirstView == null) {
                                mTestFirstView = new TestFirstView(SecretaryTipsActivity.this,
                                        mOnTestBtnClick);
                            }
                            mTestFirstView.showMenu();
                        } else {
                            Intent mIntent1 = new Intent(SecretaryTipsActivity.this,
                                    CarTestActivity.class);
                            startActivity(mIntent1);
                        }
                    }

                    break;
                case 21:
                    // 21 安防故障
                    // String relid = mInfo.getRelid();
                    // int isGot = mInfo.getIsgot();
                    // if (class2 == SecretaryMessageInfo.C1_T2_T2) {
                    // // 故障提醒
                    // Intent mIntent2 = new Intent(SecretaryTipsActivity.this,
                    // SecretaryRemoteActivity.class);
                    // mIntent2.putExtra(SecretaryRemoteActivity.ID, relid);
                    // mIntent2.putExtra(SecretaryRemoteActivity.SEND_STATUS,
                    // isGot);
                    // startActivity(mIntent2);
                    // }
                    if (class2 == SecretaryMessageInfo.C1_T2_T3) {
                        // 跳转至胎压监测
                        Intent mIntent23 = new Intent(SecretaryTipsActivity.this,
                                CarTirePressureActivity.class);
                        startActivity(mIntent23);
                    }
                    break;
                case 31:
                    // 31 奖品活动
                    if (class2 == SecretaryMessageInfo.C1_T3_T1) {
                        // 奖品
                        if (mInfo.getIsgot() == SecretaryMessageInfo.GOT_NO) {
                            mDialog = PopBoxCreat.createDialogWithProgress(
                                    SecretaryTipsActivity.this, "正在领取");
                            mDialog.show();

                            CPControl.GetReceivePrizeResult(mInfo, listener_Prize);
                        } else {
                            Intent intent31 = new Intent(SecretaryTipsActivity.this,
                                    RewardDetailActivity.class);
                            intent31.putExtra(RewardDetailActivity.REWARDID, mInfo.getRelid());
                            startActivity(intent31);
                        }
                    } else if (class2 == SecretaryMessageInfo.C1_T3_T2) {
                        // 活动

                        if (mInfo.getIsgot() == SecretaryMessageInfo.GOT_NO) {
                            mDialog = PopBoxCreat.createDialogWithProgress(
                                    SecretaryTipsActivity.this, "正在报名");
                        } else {
                            mDialog = PopBoxCreat.createDialogWithProgress(
                                    SecretaryTipsActivity.this, "正在取消报名");
                        }

                        mDialog.show();
                        CPControl.GetActivitySignResult(mInfo, listener_Prize);

                    }

                    break;
                case 41:
                    // 41 行车信息

                    Intent mIntent4;
                    switch (class2) {
                        case SecretaryMessageInfo.C1_T4_T1:
                            mIntent4 = new Intent(SecretaryTipsActivity.this, ReportActivity.class);
                            mIntent4.putExtra("c", 2);
                            mIntent4.putExtra(ReportActivity.DAY_INITIAL, mInfo.getDate());
                            startActivity(mIntent4);
                            break;
                        case SecretaryMessageInfo.C1_T4_T2:
                            mIntent4 = new Intent(SecretaryTipsActivity.this, ReportActivity.class);
                            mIntent4.putExtra("c", 1);
                            mIntent4.putExtra(ReportActivity.WEEK_INITIAL, mInfo.getDate());
                            startActivity(mIntent4);
                            break;
                        case SecretaryMessageInfo.C1_T4_T3:
                            mIntent4 = new Intent(SecretaryTipsActivity.this, ReportActivity.class);
                            mIntent4.putExtra("c", 0);
                            mIntent4.putExtra(ReportActivity.MONTH_INITIAL, mInfo.getDate());
                            startActivity(mIntent4);
                            break;

                        case SecretaryMessageInfo.C1_T4_T4:
                            // 解锁勋章

                            //                            mIntent4 = new Intent(SecretaryTipsActivity.this, MedalActivity.class);
                            //                            startActivity(mIntent4);
                            break;

                        case SecretaryMessageInfo.C1_T4_T5:
                            // 创造新记录
                            break;

                        case SecretaryMessageInfo.C1_T4_T6:
                            // 获得驾驶证

                            break;
                    }

                    break;
                case 51:
                    // 故障
                    String relid = mInfo.getRelid();
                    int isGot = mInfo.getIsgot();
                    if (class2 == SecretaryMessageInfo.C1_T2_T2) {
                        // 故障提醒
                        Intent mIntent2 = new Intent(SecretaryTipsActivity.this,
                                SecretaryRemoteActivity.class);
                        mIntent2.putExtra(SecretaryRemoteActivity.ID, relid);
                        mIntent2.putExtra(SecretaryRemoteActivity.SEND_STATUS, isGot);
                        startActivity(mIntent2);
                    }
                    break;

                case 61:
                    // 故障
                    String id = mInfo.getId();
                    // 故障提醒
                    Intent mIntent6 = new Intent(SecretaryTipsActivity.this,
                            MaintainLogDetialActivity.class);
                    mIntent6.putExtra(MaintainLogDetialActivity.ID, id);
                    startActivity(mIntent6);
                    break;

            }

        }

    };

    // 奖品领取和活动报名回调
    GetResultListCallback listener_Prize = new GetResultListCallback() {

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

    // 拉取更多数据
    GetResultListCallback listener_loadmore = new GetResultListCallback() {

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

    // 删除车秘书提醒回调
    GetResultListCallback listener_tip = new GetResultListCallback() {

        @Override
        public void onFinished(Object o) {
            Message msg = new Message();
            msg.what = 4;
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
            BaseResponseInfo mBaseResponseInfo;
            switch (msg.what) {
                case 0:
                    // 获取奖品、活动报名成功
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    mAdapter.notifyDataSetChanged();
                    String info_success = (String) msg.obj;
                    if (info_success != null) {
                        UUToast.showUUToast(SecretaryTipsActivity.this, info_success);
                    }

                    break;

                case 1:
                    // 获取奖品、活动报名失败
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    String info_failed = (String) msg.obj;
                    if (info_failed != null) {
                        UUToast.showUUToast(SecretaryTipsActivity.this, info_failed);
                    }
                    break;
                case 2:
                    SecretaryMessageInfoList mMore = (SecretaryMessageInfoList) msg.obj;
                    if (mInfoLists != null) {
                        mInfoLists.setOffset(mMore.getOffset());
                        mInfoLists.addmAllList(mMore.getmAllList());
                        if (mMore.getmAllList().size() == 0) {
                            mPullListView.setPullLoadEnabled(false);
                        }
                    }
                    LoadSuccess(mInfoLists);
                    break;
                case 3:
                    LoadErro(msg.obj);
                    break;
                case 4:
                    // 删除一条消息成功
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    mList.remove(dele_position);
                    mAdapter.setmList(mList);
                    mAdapter.notifyDataSetChanged();

                    UUToast.showUUToast(SecretaryTipsActivity.this, "删除成功！");
                    break;
                case 5:
                    // 删除一条消息失败
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    mBaseResponseInfo = (BaseResponseInfo) msg.obj;
                    if (mBaseResponseInfo != null) {
                        UUToast.showUUToast(SecretaryTipsActivity.this, mBaseResponseInfo.getInfo());
                    }
                    break;

                case 7:
                    // 点击保养过了成功

                    if (LoginInfo.isMainten()) {

                        havemainten.setTextColor(getResources().getColor(R.color.cheng1));
                        havemainten
                                .setBackgroundResource(R.drawable.activity_career_secretary_tips_kuang);
                    } else {

                        havemainten
                                .setBackgroundResource(R.drawable.activity_career_secretary_tips_kuang_noclick);
                        havemainten.setTextColor(getResources().getColor(R.color.text_color_gray1));
                    }
                    if (mUUDialog != null && mUUDialog.isShowing()) {
                        mUUDialog.dismiss();
                    }

                    mBaseResponseInfo = (BaseResponseInfo) msg.obj;
                    if (mBaseResponseInfo != null) {
                        UUToast.showUUToast(SecretaryTipsActivity.this, mBaseResponseInfo.getInfo());
                    }
                    break;

                case 8:
                    if (mUUDialog != null && mUUDialog.isShowing()) {
                        mUUDialog.dismiss();
                    }

                    mBaseResponseInfo = (BaseResponseInfo) msg.obj;
                    if (mBaseResponseInfo != null) {
                        UUToast.showUUToast(SecretaryTipsActivity.this, mBaseResponseInfo.getInfo());
                    }
                    break;

            }
        }

    };

}
