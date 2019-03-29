
package com.carlt.sesame.ui.activity.car;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.doride.http.retrofitnet.model.GetCarInfo;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.data.SesameLoginInfo;
import com.carlt.sesame.data.car.CheckFaultInfo;
import com.carlt.sesame.data.car.PhysicalExaminationInfo;
import com.carlt.sesame.ui.activity.base.BaseActivity;
import com.carlt.sesame.ui.activity.career.SecretaryRemoteActivity;
import com.carlt.sesame.ui.adapter.CarTestAdapter;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 座驾-爱车体检页面
 * 
 * @author daisy
 */
public class CarTestActivity extends BaseActivity {
    private ImageView back;// 头部返回键

    private TextView title;// 标题文字

    private TextView txtRight;// 头部右侧文字

    private ImageView mImageViewSecretary;// 车秘书头像

    private TextView mTextViewSecretary;// 提醒消息

    private ListView mListView;// 故障码列表

    private View mView; // 底部远程诊断，排除故障按钮

    private CarTestAdapter mAdapter;

    private ArrayList<CheckFaultInfo> mListP;

    private ArrayList<CheckFaultInfo> mListC;

    private ArrayList<CheckFaultInfo> mListB;

    private ArrayList<CheckFaultInfo> mListU;

    private int initValue = 3;// count的初始值

    private int count = initValue;// 第一次放入3个item

    private int addValue = 2;// count每次增加的数值

    private int index = 0;// 计算随机变量的索引值

    private final static int sizeTrueP = 4760;// 故障码真实条数-P

    private final static int sizeTrueC = 178;// 故障码真实条数-C

    private final static int sizeTrueB = 146;// 故障码真实条数-B

    private final static int sizeTrueU = 554;// 故障码真实条数-U

    private int sizeTrueRealP = sizeTrueP - initValue;// 故障码真实条数-P（实际）

    private int sizeTrueRealC = sizeTrueC - initValue;// 故障码真实条数-C（实际）

    private int sizeTrueRealB = sizeTrueB - initValue;// 故障码真实条数-B（实际）

    private int sizeTrueRealU = sizeTrueU - initValue;// 故障码真实条数-U（实际）

    private int sizeFakeP;// 故障码虚假条数-P

    private int sizeFakeC;// 故障码虚假条数-C

    private int sizeFakeB;// 故障码虚假条数-B

    private int sizeFakeU;// 故障码虚假条数-U

    private int sizeFakeRealP;// 故障码虚假条数-P（实际）

    private int sizeFakeRealC;// 故障码虚假条数-C（实际）

    private int sizeFakeRealB;// 故障码虚假条数-B（实际）

    private int sizeFakeRealU;// 故障码虚假条数-U（实际）

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_test);
        initTitle();
        initSubTitle();
        init();
        LoadData();
    }

    private void initTitle() {
        back = (ImageView)findViewById(R.id.head_back_img1);
        title = (TextView)findViewById(R.id.head_back_txt1);
        txtRight = (TextView)findViewById(R.id.head_back_txt2);

        back.setImageResource(R.drawable.arrow_back);
        title.setText("爱车体检");
        txtRight.setVisibility(View.GONE);
        txtRight.setText("晒一晒");

        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txtRight.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (mPhysicalExaminationInfo != null) {
                    View shareView = findViewById(R.id.activity_car_test);
//                    ShareControl.share(CarTestActivity.class, CarTestActivity.this,
//                            mPhysicalExaminationInfo.getShareTitle(),
//                            mPhysicalExaminationInfo.getShareText(),
//                            mPhysicalExaminationInfo.getShareLink(), shareView);
                }

            }
        });
    }

    private void initSubTitle() {
        mImageViewSecretary = (ImageView)findViewById(R.id.layout_sub_head_img);
        mTextViewSecretary = (TextView)findViewById(R.id.layout_sub_head_txt);

        mImageViewSecretary.setImageResource(GetCarInfo.getInstance().secretaryID == 1?R.drawable.secretary_female:R.drawable.secretary_male);
        mTextViewSecretary.setText("正在连接您的爱车，请稍候...");
    }

    private void init() {
        mListView = (ListView)findViewById(R.id.activity_car_test_list);
        mView = findViewById(R.id.activity_car_test_layout);

        mListP = new ArrayList<CheckFaultInfo>();
        mListC = new ArrayList<CheckFaultInfo>();
        mListB = new ArrayList<CheckFaultInfo>();
        mListU = new ArrayList<CheckFaultInfo>();

        mAdapter = new CarTestAdapter(CarTestActivity.this, mListP);
        mListView.setAdapter(mAdapter);
    }

    private PhysicalExaminationInfo mPhysicalExaminationInfo;

    private void LoadSuccess(Object data) {
        mPhysicalExaminationInfo = (PhysicalExaminationInfo)data;

        if (mPhysicalExaminationInfo.getIsrunning() == 0) {
            // 0:未行驶 不能拿到数据
            mTextViewSecretary.setText("您的爱车没有在行驶中，无法进行体检哦...");
        } else if (mPhysicalExaminationInfo.getIsrunning() == 1) {
            // 1:行驶中 能拿到数据
            mTextViewSecretary.setText("爱车检测中，体检报告马上生成...");

            mListP = mPhysicalExaminationInfo.getListP();
            mListC = mPhysicalExaminationInfo.getListC();
            mListB = mPhysicalExaminationInfo.getListB();
            mListU = mPhysicalExaminationInfo.getListU();

            sizeFakeP = mListP.size();
            sizeFakeC = mListC.size();
            sizeFakeB = mListB.size();
            sizeFakeU = mListU.size();

            sizeFakeRealP = sizeFakeP - initValue;
            sizeFakeRealC = sizeFakeP - initValue;
            sizeFakeRealB = sizeFakeP - initValue;
            sizeFakeRealU = sizeFakeP - initValue;

            if (mAdapter != null) {
                mAdapter.AddListP(mListP.get(0));
                mAdapter.setTestNumListP(0);
                mAdapter.AddListP(mListP.get(1));
                mAdapter.setTestNumListP(0);
                mAdapter.AddListP(mListP.get(2));
                mAdapter.setTestNumListP(0);

                mAdapter.setTitleState1(CarTestAdapter.TITLE_STATE0);
                mAdapter.notifyDataSetChanged();
                refreshTestItem();
            }

        }
    }

    private void LoadErro(Object erro) {
        BaseResponseInfo mBaseResponseInfo = (BaseResponseInfo)erro;
        txtRight.setTextColor(getResources().getColor(R.color.text_color_gray0));
        txtRight.setOnClickListener(null);
        mTextViewSecretary.setText(mBaseResponseInfo.getInfo());
    }

    private void LoadData() {
        CPControl.GetPhysicalExaminationResult(listener);
    }

    private GetResultListCallback listener = new GetResultListCallback() {

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

    private Timer timer;

    private final static long time = 70;

    private int type;

    private boolean f2 = true; // 定时器中的任务是否执行

    TimerTask task;

    /**
     * 刷新检测数据
     */
    private void refreshTestItem() {
        if (timer == null) {
            timer = new Timer();
        }
        if (task == null) {
            task = new TimerTask() {

                @Override
                public void run() {

                    if (f2) {
                        Message msg = new Message();
                        switch (type) {
                            case 0:
                                msg.what = 5;
                                break;
                            case 1:
                                msg.what = 7;
                                break;
                            case 2:
                                msg.what = 9;
                                break;
                            case 3:
                                msg.what = 11;
                                break;
                        }

                        mHandler.sendMessage(msg);
                    }
                }
            };
            if (timer != null && task != null) {
                timer.schedule(task, time, time);
            }
        }
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    // 拉取检测数据成功
                    LoadSuccess(msg.obj);
                    break;
                case 1:
                    // 拉取检测数据失败
                    LoadErro(msg.obj);
                    break;
                case 5:
                    if (count < sizeFakeP) {
                        mAdapter.setStateListP(1);
                        mAdapter.AddListP(mListP.get(count));
                        int randomNum = getTestNum(index, 0);
                        mAdapter.setTestNumListP(randomNum);
                        mAdapter.RemoveListP();
                        mAdapter.notifyDataSetChanged();
                        count = count + addValue;
                        index++;
                    } else {
                        f2 = false;
                        count = initValue;
                        index = 0;
                        type = 1;

                        mAdapter.setTestNumListP(sizeTrueP);
                        mAdapter.clearListP();
                        if (mPhysicalExaminationInfo.getMistakeListP().size() == 0) {
                            mAdapter.setTitleState1(CarTestAdapter.TITLE_STATE1);
                        } else {
                            mAdapter.setTitleState1(CarTestAdapter.TITLE_STATE2);
                        }

                        mAdapter.AddListC(mListC.get(0));
                        mAdapter.AddListC(mListC.get(1));
                        mAdapter.AddListC(mListC.get(2));
                        mAdapter.setTestNumListC(count);
                        mAdapter.setTitleState2(CarTestAdapter.TITLE_STATE0);

                        mAdapter.notifyDataSetChanged();
                        f2 = true;

                    }
                    break;

                case 7:
                    if (count < sizeFakeC) {
                        mAdapter.setStateListC(1);
                        mAdapter.AddListC(mListC.get(count));
                        int randomNum = getTestNum(index, 1);
                        mAdapter.setTestNumListC(randomNum);
                        mAdapter.RemoveListC();
                        mAdapter.notifyDataSetChanged();
                        count = count + addValue;
                        index++;
                    } else {
                        f2 = false;
                        count = initValue;
                        index = 0;
                        type = 2;

                        mAdapter.setTestNumListC(sizeTrueC);
                        mAdapter.clearListC();
                        if (mPhysicalExaminationInfo.getMistakeListC().size() == 0) {
                            mAdapter.setTitleState2(CarTestAdapter.TITLE_STATE1);
                        } else {
                            mAdapter.setTitleState2(CarTestAdapter.TITLE_STATE2);
                        }

                        mAdapter.AddListB(mListB.get(0));
                        mAdapter.AddListB(mListB.get(1));
                        mAdapter.AddListB(mListB.get(2));
                        mAdapter.setTestNumListB(count);

                        mAdapter.setTitleState3(CarTestAdapter.TITLE_STATE0);

                        mAdapter.notifyDataSetChanged();
                        f2 = true;
                    }
                    break;
                case 9:
                    if (count < sizeFakeB) {
                        mAdapter.setStateListB(1);
                        mAdapter.AddListB(mListB.get(count));
                        int randomNum = getTestNum(index, 2);
                        mAdapter.setTestNumListB(randomNum);
                        mAdapter.RemoveListB();
                        mAdapter.notifyDataSetChanged();
                        count = count + addValue;
                        index++;
                    } else {
                        f2 = false;
                        count = initValue;
                        index = 0;
                        type = 3;

                        mAdapter.setTestNumListB(sizeTrueB);
                        mAdapter.clearListB();
                        if (mPhysicalExaminationInfo.getMistakeListB().size() == 0) {
                            mAdapter.setTitleState3(CarTestAdapter.TITLE_STATE1);
                        } else {
                            mAdapter.setTitleState3(CarTestAdapter.TITLE_STATE2);
                        }

                        mAdapter.AddListU(mListU.get(0));
                        mAdapter.AddListU(mListU.get(1));
                        mAdapter.AddListU(mListU.get(2));
                        mAdapter.setTestNumListU(count);

                        mAdapter.setTitleState4(CarTestAdapter.TITLE_STATE0);

                        mAdapter.notifyDataSetChanged();
                        f2 = true;
                    }
                    break;

                case 11:
                    if (count < sizeFakeU) {
                        mAdapter.setStateListU(1);
                        mAdapter.AddListU(mListU.get(count));
                        int randomNum = getTestNum(index, 3);
                        mAdapter.setTestNumListU(randomNum);
                        mAdapter.RemoveListU();
                        mAdapter.notifyDataSetChanged();
                        count = count + addValue;
                        index++;
                    } else {
                        timer.cancel();

                        mAdapter.setTestNumListU(sizeTrueU);
                        mAdapter.clearListU();
                        if (mPhysicalExaminationInfo.getMistakeListU().size() == 0) {
                            mAdapter.setTitleState4(CarTestAdapter.TITLE_STATE1);
                        } else {
                            mAdapter.setTitleState4(CarTestAdapter.TITLE_STATE2);
                        }

                        mAdapter.notifyDataSetChanged();

                        int sizeMistakeP = mPhysicalExaminationInfo.getMistakeListP().size();
                        int sizeMistakeC = mPhysicalExaminationInfo.getMistakeListC().size();
                        int sizeMistakeB = mPhysicalExaminationInfo.getMistakeListB().size();
                        int sizeMistakeU = mPhysicalExaminationInfo.getMistakeListU().size();

                        for (int i = 0; i < sizeMistakeP; i++) {
                            mAdapter.AddListP(mPhysicalExaminationInfo.getMistakeListP().get(i));
                        }
                        for (int i = 0; i < sizeMistakeC; i++) {
                            mAdapter.AddListC(mPhysicalExaminationInfo.getMistakeListC().get(i));
                        }
                        for (int i = 0; i < sizeMistakeB; i++) {
                            mAdapter.AddListB(mPhysicalExaminationInfo.getMistakeListB().get(i));
                        }
                        for (int i = 0; i < sizeMistakeU; i++) {
                            mAdapter.AddListU(mPhysicalExaminationInfo.getMistakeListU().get(i));
                        }

                        mAdapter.setTestFinishAll(true);
                        if (sizeMistakeP > 0) {
                            mAdapter.setTestNumListP(sizeMistakeP);
                        }
                        if (sizeMistakeC > 0) {
                            mAdapter.setTestNumListC(sizeMistakeC);
                        }
                        if (sizeMistakeB > 0) {
                            mAdapter.setTestNumListB(sizeMistakeB);
                        }
                        if (sizeMistakeU > 0) {
                            mAdapter.setTestNumListU(sizeMistakeU);
                        }

                        if (sizeMistakeP > 0 || sizeMistakeC > 0 || sizeMistakeB > 0
                                || sizeMistakeU > 0) {
                            mView.setVisibility(View.VISIBLE);
                            mView.setOnClickListener(mOnClickListener);
                        } else {
                            mView.setVisibility(View.GONE);
                        }

                        String point = mPhysicalExaminationInfo.getPoint();
                        if (point != null && point.length() > 0) {
                            if (point.equals("100")) {
                                mTextViewSecretary.setText("爱车健康指数：100分,请继续保持哦");
                            } else {
                                mTextViewSecretary.setText("爱车健康指数："
                                        + mPhysicalExaminationInfo.getPoint() + "分,存在安全隐患");
                            }
                        } else {
                            mTextViewSecretary.setText("爱车健康指数：？分");
                        }
                    }
                    break;
            }
        }

    };

    /**
     * 获取已检测XX项的随机数
     * 
     * @param index 索引码，用于计算随机区间的最小值
     * @param type（故障类型：0-P，0-C，0-B，0-U）
     * @return
     */
    private int getTestNum(int index, int type) {
        int randomNum = 0;// 获取到的随机数

        int sumCount;// 循环总次数
        int rangeLength;// 随机区间的长度
        int rangeMin;// 随机区间的最小者

        Random mRandom = new Random();

        switch (type) {
            case 0:
                // 故障P
                sumCount = (int)Math.ceil(sizeFakeRealP / addValue);// 向上取整
                rangeLength = sizeTrueRealP / sumCount;

                rangeMin = sizeTrueRealP - rangeLength * (sumCount - index);
                randomNum = mRandom.nextInt(rangeLength) + rangeMin;
                break;

            case 1:
                // 故障C

                sumCount = (int)Math.ceil(sizeFakeRealC / addValue);// 向上取整
                rangeLength = sizeTrueRealC / sumCount;

                rangeMin = sizeTrueRealC - rangeLength * (sumCount - index);
                randomNum = mRandom.nextInt(rangeLength) + rangeMin;
                break;
            case 2:
                // 故障B
                sumCount = (int)Math.ceil(sizeFakeRealB / addValue);// 向上取整
                rangeLength = sizeTrueRealB / sumCount;

                rangeMin = sizeTrueRealB - rangeLength * (sumCount - index);
                randomNum = mRandom.nextInt(rangeLength) + rangeMin;
                break;
            case 3:
                // 故障U
                sumCount = (int)Math.ceil(sizeFakeRealU / addValue);// 向上取整
                rangeLength = sizeTrueRealU / sumCount;

                rangeMin = sizeTrueRealU - rangeLength * (sumCount - index);
                randomNum = mRandom.nextInt(rangeLength) + rangeMin;
                break;
        }
        return randomNum;
    }

    private OnClickListener mOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent mIntent = new Intent(CarTestActivity.this, SecretaryRemoteActivity.class);
            if (mPhysicalExaminationInfo != null) {
                mIntent.putExtra(SecretaryRemoteActivity.ID, mPhysicalExaminationInfo.getId());
                mIntent.putExtra(SecretaryRemoteActivity.SEND_STATUS, 0);
            }
            startActivity(mIntent);
        }
    };

    @Override
    protected void onDestroy() {
        if (timer != null) {
            timer.cancel();
            timer = null;

        }
        if (task != null) {
            task.cancel();
            task = null;
        }
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
