
package com.carlt.sesame.ui.activity.setting;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.carlt.doride.DorideApplication;
import com.carlt.sesame.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.data.LoginInfo;
import com.carlt.sesame.data.set.FeeMainInfo;
import com.carlt.sesame.data.set.FeeOrderInfo;
import com.carlt.sesame.data.set.FeeTypeInfo;
import com.carlt.sesame.data.set.PayResult;
import com.carlt.sesame.ui.activity.base.LoadingActivityWithTitle;
import com.carlt.sesame.ui.view.PopBoxCreat;
import com.carlt.sesame.utility.MyTimeUtil;
import com.carlt.sesame.utility.UUToast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * 续费管理
 *
 * @author Administrator
 */
public class ManageFeeActivity extends LoadingActivityWithTitle implements OnClickListener {

    private ImageView back;// 头部返回键

    private TextView title;// 标题文字

    private TextView txtRight;// 头部右侧文字

    private TextView mTxtDate;// 有效期至

    private TextView mTxtFee;// 费用金额

    private TextView mTxtSure;// 确认按钮

    private RadioGroup mRgType;// 时长类别

    private RadioButton mRB1;// 1年

    private RadioButton mRB2;// 2年

    private RadioButton mRB3;// 3年

    private CheckBox mCbAlipay;// 支付宝选择按钮

    private View mViewNormal;//正常View
    private View mViewNull;//屏蔽信息View

    private FeeMainInfo mFeeMainInfo;

    private FeeTypeInfo mInfoSelect;// 当前选中的支付类别

    private ArrayList<FeeTypeInfo> mFeeTypeInfos;

    private Dialog mDialog;

    private boolean feeSuccess;// 续费成功

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_managefee);
        setTitleView(R.layout.head_back);

        initTitle();
        init();
        LoadData();
    }

    private void initTitle() {
        back = (ImageView) findViewById(R.id.head_back_img1);
        title = (TextView) findViewById(R.id.head_back_txt1);
        txtRight = (TextView) findViewById(R.id.head_back_txt2);

        title.setText("服务购买");
        txtRight.setText("购买记录");
        txtRight.setVisibility(View.VISIBLE);
        back.setImageResource(R.drawable.arrow_back);

        back.setOnClickListener(this);
        txtRight.setOnClickListener(this);

        txtRight.setVisibility(View.VISIBLE);

    }

    private void init() {
        mTxtDate = (TextView) findViewById(R.id.managefee_txt_date);
        mTxtFee = (TextView) findViewById(R.id.managefee_txt_feevalue);
        mTxtSure = (TextView) findViewById(R.id.managefee_txt_sure);
        mRgType = (RadioGroup) findViewById(R.id.managefee_rg_type);
        mCbAlipay = (CheckBox) findViewById(R.id.managefee_cb_alipay);

        mViewNormal = findViewById(R.id.managefee_lay_normal);
        mViewNull = findViewById(R.id.managefee_view_null);

        mCbAlipay.setClickable(false);
        mTxtSure.setOnClickListener(this);

        mViewNormal.setVisibility(View.VISIBLE);
        mViewNull.setVisibility(View.GONE);

    }

    @Override
    protected void LoadSuccess(Object data) {
        mFeeMainInfo = (FeeMainInfo) data;
        if (mFeeMainInfo != null) {
            mFeeTypeInfos = mFeeMainInfo.getmFeeTypeInfos();
            if (mFeeTypeInfos != null && mFeeTypeInfos.size() > 0) {
                for (int i = 0; i < mFeeTypeInfos.size(); i++) {
                    RadioButton mRadioButton = (RadioButton) mRgType.getChildAt(i);
                    FeeTypeInfo mFeeTypeInfo = mFeeTypeInfos.get(i);
                    mRadioButton.setText(mFeeTypeInfo.getName() + "年服务");
                }

                mRgType.setOnCheckedChangeListener(mChangeListener);
            }
            if (mFeeMainInfo.isTimeIsRed()) {
                mTxtDate.setTextColor(Color.parseColor("#fa7c44"));
            } else {
                mTxtDate.setTextColor(Color.parseColor("#333333"));
            }

            mTxtDate.setText("有效期至  " + mFeeMainInfo.getServiceEndDate());

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
        CPControl.GetFeeMainResult(listener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (feeSuccess) {
            LoadData();
            feeSuccess = false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back_img1:
                // 返回键
                finish();
                break;

            case R.id.head_back_txt2:
                // 跳转至购买记录
                Intent mIntent = new Intent(ManageFeeActivity.this, FeeLogActivity.class);
                startActivity(mIntent);
                break;
            case R.id.managefee_txt_sure:
                // 提交订单信息
                if (mInfoSelect != null) {
                    CPControl.GetFeeOrderResult(orderListener, mInfoSelect.getName(),
                            mInfoSelect.getCost());
                }
                break;
        }
    }

    private GetResultListCallback orderListener = new GetResultListCallback() {

        @Override
        public void onFinished(Object o) {
            Message msg = new Message();
            msg.what = 0;
            msg.obj = o;
            mGoHandler.sendMessage(msg);
        }

        @Override
        public void onErro(Object o) {
            Message msg = new Message();
            msg.what = 1;
            msg.obj = o;
            mGoHandler.sendMessage(msg);
        }
    };

    private OnCheckedChangeListener mChangeListener = new OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.managefee_rb_type1:
                    mInfoSelect = mFeeTypeInfos.get(0);
                    break;
                case R.id.managefee_rb_type2:
                    mInfoSelect = mFeeTypeInfos.get(1);
                    break;
                case R.id.managefee_rb_type3:
                    mInfoSelect = mFeeTypeInfos.get(2);
                    break;
            }

            mTxtFee.setText(mInfoSelect.getCost());
        }
    };

    public Handler mPayHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                case 1: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/
                     * doc2/ detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=
                     * 103665& docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();

                    if (resultStatus.equals("9000") || resultStatus.equals("8000")) {
                        String[] result = resultInfo.split("&");
                        StringBuffer json = new StringBuffer("{");
                        for (int i = 0; i < result.length; i++) {
                            String temp = result[i];
                            String key = temp.substring(0, temp.indexOf("="));
                            String value = temp.substring(temp.indexOf("=") + 1);
                            json.append("\"" + key + "\"");
                            json.append(":");
                            json.append(value);
                            json.append(",");
                        }
                        json.deleteCharAt(json.length() - 1);
                        json.append("}");
                        mDialog = PopBoxCreat.createDialogWithProgress(ManageFeeActivity.this,
                                "正在验证支付结果请稍等。。。");
                        mDialog.show();
                        Log.d("C_DEBUG", json.toString());
                        Log.d("C_DEBUG", resultInfo);
                        CPControl.GetFeeCheckResult(pay_listener, resultStatus, json.toString());
                    } else {
                        UUToast.showUUToast(ManageFeeActivity.this, "续费失败!");
                    }
                    break;
                }
                default:
                    UUToast.showUUToast(ManageFeeActivity.this, "续费失败!");
                    break;
            }
        }

        ;
    };

    GetResultListCallback pay_listener = new GetResultListCallback() {

        @Override
        public void onFinished(Object o) {
            Message msg = new Message();
            msg.what = 3;
            msg.obj = o;
            mGoHandler.sendMessage(msg);
        }

        @Override
        public void onErro(Object o) {
            Message msg = new Message();
            msg.what = 4;
            msg.obj = o;
            mGoHandler.sendMessage(msg);
        }
    };

    public Handler mGoHandler = new Handler() {
        public void handleMessage(Message msg) {
            BaseResponseInfo mInfo = null;
            switch (msg.what) {
                case 0:
                    FeeOrderInfo fee = (FeeOrderInfo) msg.obj;
                    String orderInfo = fee.getParam();
                    Log.d("DEBUG", "orderinfo ====" + orderInfo);
                    CPControl.GetToPay(mPayHandler, ManageFeeActivity.this, orderInfo, false);
                    break;
                case 1:
                    mInfo = (BaseResponseInfo) msg.obj;
                    String meesg = "订单信息获取失败";
                    if (mInfo != null) {
                        meesg = mInfo.getInfo();
                    }
                    UUToast.showUUToast(ManageFeeActivity.this, meesg);
                    break;
                case 3:
                    mDialog.dismiss();
                    feeSuccess = true;
                    LoginInfo.setServiceExpire(false);
                    mDialog = PopBoxCreat.createUUImgInfoDialog(ManageFeeActivity.this, null, null,
                            "续费成功", new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    mDialog.dismiss();
                                    Intent intent = new Intent(ManageFeeActivity.this,
                                            FeeLogActivity.class);
                                    startActivity(intent);
                                }
                            });
                    mDialog.show();

                    String date = mFeeMainInfo.getServiceEndDate();
                    try {
                        Date d = MyTimeUtil.DateFormat.parse(date);
                        Calendar c = Calendar.getInstance();
                        c.setTime(d);
                        c.add(Calendar.YEAR, Integer.parseInt(mInfoSelect.getName()));

                        mFeeMainInfo.setServiceEndDate(MyTimeUtil.DateFormat.format(c.getTime()));
                        mTxtDate.setText(mFeeMainInfo.getServiceEndDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    mDialog.dismiss();
                    mInfo = (BaseResponseInfo) msg.obj;
                    if (mInfo != null && mInfo.getInfo() != null) {
                        UUToast.showUUToast(ManageFeeActivity.this, mInfo.getInfo());
                    } else {
                        UUToast.showUUToast(ManageFeeActivity.this, "续费失败");
                    }
                    break;
            }
        }

        ;
    };

}
