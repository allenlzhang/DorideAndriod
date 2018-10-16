
package com.carlt.sesame.ui.activity.car;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.data.SesameLoginInfo;
import com.carlt.sesame.data.car.CityInfo;
import com.carlt.sesame.data.set.ModifyCarInfo;
import com.carlt.sesame.ui.activity.career.report.MenuCalendar;
import com.carlt.sesame.ui.view.PopBoxCreat;
import com.carlt.sesame.utility.UUToast;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * 第一次体检View
 * 
 * @author daisy
 */
public class TestFirstView extends MenuCalendar {
    private ImageView mImageViewSecretary;// 车秘书头像

    private TextView mTextViewSecretary;// 提醒消息

    private ArrayList<CityInfo> mList = new ArrayList<CityInfo>();

    private Context mContext;

    private OnTestBtnClick mOnTestBtnClick;

    private View mView1;// 行驶总里程的外层layout

    private View mView2;// 购车时间的外层layout

    private EditText mEditText;// 行驶总里程

    private TextView mTextView;// 日期显示

    private TextView mBtnTest;// 开始体检按钮

    private Dialog mDialog;

    public TestFirstView(Context context, OnTestBtnClick onTestBtnClick) {
        super(context);

        mContext = context;
        mOnTestBtnClick = onTestBtnClick;
        mDialog = PopBoxCreat.createDialogWithProgress(mContext, "努力加载中...");

        View child = LayoutInflater.from(mContext).inflate(R.layout.layout_car_test_first, null);
        init(child);
        setTitle("爱车体检");

        mView1 = child.findViewById(R.id.layout_car_test_first_layout1);
        mView2 = child.findViewById(R.id.layout_car_test_first_layout2);
        mEditText = (EditText)child.findViewById(R.id.layout_car_test_first_edit);
        mTextView = (TextView)child.findViewById(R.id.layout_car_test_first_txt);
        mBtnTest = (TextView)child.findViewById(R.id.layout_car_test_first_btn);

        mEditText.setOnFocusChangeListener(mFocusChangeListener);
        mView2.setOnClickListener(mClickListener);
        mBtnTest.setOnClickListener(mClickListener);

        initSubTitle(child);
    }

    private void initSubTitle(View v) {
        mImageViewSecretary = (ImageView)v.findViewById(R.id.layout_sub_head_img);
        mTextViewSecretary = (TextView)v.findViewById(R.id.layout_sub_head_txt);

        mImageViewSecretary.setImageResource(SesameLoginInfo.getSecretaryImg());
        String s = "第一次体检需填写行驶里程，系统会根据公里数匹配相应的体检模式！";
        mTextViewSecretary.setText(s);
    }

    @Override
    protected void onPopCreat() {
        // TODO Auto-generated method stub

    };

    private OnFocusChangeListener mFocusChangeListener = new OnFocusChangeListener() {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                mView1.setBackgroundResource(R.drawable.edittext_bg_focused);
            } else {
                mView1.setBackgroundResource(R.drawable.edittext_bg_focused_no);
            }

        }
    };

    private OnClickListener mClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.layout_car_test_first_layout2:
                    // 选择日期view
                    isSecond = false;
                    Calendar mCalendar = Calendar.getInstance();
                    final DatePickerDialog mPickerDialog = new DatePickerDialog(mContext,
                            mDateSetListener, mCalendar.get(Calendar.YEAR),
                            mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));

                    DialogInterface.OnClickListener mClickListener = new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    isPositive = true;
                                    if (mPickerDialog.isShowing()) {
                                        mPickerDialog.dismiss();
                                    }
                                    // 针对魅族系统添加的该段代码
                                    DatePicker datePicker = mPickerDialog.getDatePicker();
                                    mDateSetListener.onDateSet(datePicker, datePicker.getYear(),
                                            datePicker.getMonth(), datePicker.getDayOfMonth());
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    isPositive = false;
                                    if (mPickerDialog.isShowing()) {
                                        mPickerDialog.dismiss();
                                    }
                                    break;
                            }

                        }
                    };
                    mPickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", mClickListener);
                    mPickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", mClickListener);

                    mPickerDialog.show();
                    break;
                case R.id.layout_car_test_first_btn:
                    // 确定按钮
                    ModifyCarInfo mModifyCarInfo = new ModifyCarInfo();

                    String brandid = SesameLoginInfo.getBrandid();
                    String optionid = SesameLoginInfo.getOptionid();
                    String carid = SesameLoginInfo.getCarid();
                    String carLogo = SesameLoginInfo.getCarlogo();
                    String carName = SesameLoginInfo.getCarname();

                    mModifyCarInfo.setBrandid(brandid);
                    mModifyCarInfo.setOptionid(optionid);
                    mModifyCarInfo.setCarid(carid);
                    mModifyCarInfo.setCarlogo(carLogo);
                    mModifyCarInfo.setCarname(carName);

                    String summiles = mEditText.getText().toString();
                    if (summiles != null && summiles.length() > 0) {
                        mModifyCarInfo.setSummiles(summiles);
                    } else {
                        UUToast.showUUToast(mContext, "您还没有填写行驶里程");
                        return;
                    }
                    String buydate = mTextView.getText().toString();
                    if (buydate != null && buydate.length() > 0) {
                        mModifyCarInfo.setBuydate(buydate);
                    } else {
                        UUToast.showUUToast(mContext, "您还没有填写购车日期");
                        return;
                    }

                    mDialog.show();
                    CPControl.GetUpdateCarInfoResult(mModifyCarInfo, listener);
                    break;
            }

        }
    };

    private GetResultListCallback listener = new GetResultListCallback() {

        @Override
        public void onFinished(Object o) {
            Message msg = new Message();
            msg.what = 0;
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

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    dissmiss();
                    UUToast.showUUToast(mContext, "信息提交成功");
                    mOnTestBtnClick.onClick();
                    break;

                case 1:
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    BaseResponseInfo mInfo = (BaseResponseInfo)msg.obj;
                    if (mInfo != null) {
                        String info = mInfo.getInfo();
                        if (info != null && info.length() > 0) {
                            UUToast.showUUToast(mContext, "信息提交失败:" + info);
                        } else {
                            UUToast.showUUToast(mContext, "信息提交失败，请稍候再试...");
                        }
                    } else {
                        UUToast.showUUToast(mContext, "信息提交失败，请稍候再试...");
                    }
                    break;
            }
        };
    };

    private boolean isSecond = false;// 4.0以上系统Bug,会调用两次OnDateSetListener，故用此做标识

    private boolean isPositive = false;// 是否点击了确定按钮

    protected OnDateSetListener mDateSetListener = new OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
            if (isSecond) {

            } else {
                isSecond = true;
                StringBuffer mStringBuffer = new StringBuffer();
                mStringBuffer.append(year + "-");
                mStringBuffer.append((month + 1) + "-");
                mStringBuffer.append(dayOfMonth);
                if (isPositive) {
                    mTextView.setText(mStringBuffer.toString());
                } else {
                    mTextView.setHint("请选择购车时间");
                }
            }
        }
    };

    public interface OnTestBtnClick {

        void onClick();
    }

}
