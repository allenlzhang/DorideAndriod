
package com.carlt.sesame.ui.activity.setting;

import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.carlt.doride.R;
import com.carlt.doride.ui.activity.login.ActivateBindActivity;
import com.carlt.doride.ui.activity.login.UpDateActivity;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.data.SesameLoginInfo;
import com.carlt.sesame.data.car.CarModeInfo;
import com.carlt.sesame.data.set.ModifyCarInfo;
import com.carlt.sesame.ui.activity.base.LoadingActivityWithTitle;
import com.carlt.sesame.ui.activity.car.CarMainActivity;
import com.carlt.sesame.ui.activity.usercenter.SelectCarTypeView;
import com.carlt.sesame.ui.activity.usercenter.SelectCarTypeView.OnCarTypeItemClick;
import com.carlt.sesame.ui.view.PopBoxCreat;
import com.carlt.sesame.ui.view.PopBoxCreat.DialogWithTitleClick;
import com.carlt.sesame.utility.MyParse;
import com.carlt.sesame.utility.MyTimeUtil;
import com.carlt.sesame.utility.UUToast;

import net.simonvt.datepicker.DatePickDialog;
import net.simonvt.datepicker.DatePickDialog.IgetDate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * 设置-车辆管理页面
 * @author daisy
 */
public class ManageCarActivity extends LoadingActivityWithTitle {
    private ImageView back;// 头部返回键

    private TextView title;// 标题文字

    private TextView txtRight;// 头部右侧文字

    private View mView1;// 修改车型、车系、车款

    private View mView3;// 购车时间外层layout

    private View mView4;// 上次保养里程外层layout

    private TextView mTxtCarname;// 用户车name

    private TextView mTxtBuydate;// 购车时间

    private TextView mTxtMaintainMile;// 上传保养里程数

    private TextView mTxtMaintainDate;// 上传保养时间

    private String brandid;// 车型id

    private String optionid;// 车系id

    private String carid;// 车款id

    private String carName;// 车款Name

    private String carLogo;// 车标icon的url

    private String carYear;// 车款年限

    private SelectCarTypeView mCarTypeView;// 选择车型View

    private Dialog mDialog;

    public final static int REQUESTCODE = 2222;

    public final static int NONE = 0;

    public final static String NEWDATA = "newdata";

    private TimePickerView pvCustomTime;

    private String carDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_car);
        setTitleView(R.layout.head_back);
        mDialog = PopBoxCreat.createDialogWithProgress(ManageCarActivity.this, "努力加载中...");
        initTitle();
        init();
        LoadData();

    }

    private void initTitle() {
        back = (ImageView) findViewById(R.id.head_back_img1);
        title = (TextView) findViewById(R.id.head_back_txt1);
        txtRight = (TextView) findViewById(R.id.head_back_txt2);

        back.setImageResource(R.drawable.arrow_back);
        title.setText("车辆管理");
        txtRight.setVisibility(View.GONE);

        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void init() {

        mView1 = findViewById(R.id.manage_car_layout1);
        mView3 = findViewById(R.id.manage_car_layout3);
        mView4 = findViewById(R.id.manage_car_layout4);

        mTxtCarname = (TextView) findViewById(R.id.manage_car_txt_carname);
        mTxtBuydate = (TextView) findViewById(R.id.manage_car_txt_buydate);
        mTxtMaintainMile = (TextView) findViewById(R.id.manage_car_txt_maintainmile);
        mTxtMaintainDate = (TextView) findViewById(R.id.manage_car_txt_maintaindate);

//        mView1.setOnClickListener(mClickListener);
//        mTxtCarname.setOnClickListener(mClickListener);
        mTxtBuydate.setOnClickListener(mClickListener);
        mView4.setOnClickListener(mClickListener);
        mTxtMaintainDate.setOnClickListener(mClickListener);

//        mTxtBuydate.setOnFocusChangeListener(mChangeListener);

    }

    @Override
    protected void LoadSuccess(Object data) {

        if (SesameLoginInfo.getCarname() != null) {
            mTxtCarname.setText(SesameLoginInfo.getCarname());
        }

        if (SesameLoginInfo.getBuydate() != null) {
            mTxtBuydate.setText(SesameLoginInfo.getBuydate());
        } else {
            mTxtBuydate.setText("");
        }

        if (SesameLoginInfo.getMainten_miles() != null) {
            mTxtMaintainMile.setText(SesameLoginInfo.getMainten_miles());
        }

        if (SesameLoginInfo.getMainten_time() != null) {
            mTxtMaintainDate.setText(SesameLoginInfo.getMainten_time());
        }
        carLogo = SesameLoginInfo.getCarlogo();
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
        CPControl.getCarMangerInfo(listener);
    }

    private OnFocusChangeListener mChangeListener = new OnFocusChangeListener() {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            switch (v.getId()) {
                case R.id.manage_car_txt_carname:
                    if (hasFocus) {
                        mView3.setBackgroundResource(R.drawable.edittext_bg_focused);
                    } else {
                        mView3.setBackgroundResource(R.drawable.edittext_bg_focused_no);
                    }
                    break;
            }

        }
    };

    private OnClickListener mClickListener = new OnClickListener() {

        @SuppressWarnings("deprecation")
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.manage_car_layout1:
                    // 选择车型
                    // if (mCarTypeView == null) {
                    // mCarTypeView = new
                    // SelectCarTypeView(ManageCarActivity.this,
                    // mOnCarTypeItemClick);
                    // }
                    // mCarTypeView.pullDataFirst(SelectCarTypeView.TYPE_MODEL);
                    // mCarTypeView.showMenu();

                    // 选择车款
                    //                    if (mCarTypeView == null) {
                    //                        mCarTypeView = new SelectCarTypeView(ManageCarActivity.this,
                    //                                mOnCarTypeItemClick);
                    //                    }
                    //                    CarModeInfo carModeInfo1 = new CarModeInfo();
                    //                    carModeInfo1.setTitle(SelectCarTypeView.TITLE);
                    //                    carModeInfo1.setId(SelectCarTypeView.OPTIONID);
                    //                    mCarTypeView.pullDataThird(carModeInfo1, SelectCarTypeView.TYPE_CAR);
                    //                    mCarTypeView.showMenu();
                    break;
                case R.id.manage_car_txt_carname:
                    // 选择车型
                    // if (mCarTypeView == null) {
                    // mCarTypeView = new
                    // SelectCarTypeView(ManageCarActivity.this,
                    // mOnCarTypeItemClick);
                    // }
                    // mCarTypeView.pullDataFirst(SelectCarTypeView.TYPE_MODEL);
                    // mCarTypeView.showMenu();

                    // 选择车款
                    if (mCarTypeView == null) {
                        mCarTypeView = new SelectCarTypeView(ManageCarActivity.this,
                                mOnCarTypeItemClick);
                    }
                    CarModeInfo carModeInfo2 = new CarModeInfo();
                    carModeInfo2.setTitle(SelectCarTypeView.TITLE);
                    carModeInfo2.setId(SelectCarTypeView.OPTIONID);
                    mCarTypeView.pullDataThird(carModeInfo2, SelectCarTypeView.TYPE_CAR);
                    mCarTypeView.showMenu();
                    break;
                case R.id.manage_car_txt_buydate:
                    // 选择日期
//                    Calendar mCalendar = Calendar.getInstance();
//                    String buyDate = SesameLoginInfo.getBuydate();
//                    int year;
//                    int month;
//                    int day;
//                    if (buyDate != null && buyDate.length() > 0) {
//                        String date[] = buyDate.split("-");
//                        year = MyParse.parseInt(date[0]);
//                        month = (MyParse.parseInt(date[1]) - 1);
//                        day = MyParse.parseInt(date[2]);
//                    } else {
//                        year = mCalendar.get(Calendar.YEAR);
//                        month = mCalendar.get(Calendar.MONTH);
//                        day = mCalendar.get(Calendar.DAY_OF_MONTH);
//                    }
//                    DatePickDialog datePickDialog = new DatePickDialog(ManageCarActivity.this,
//                            mIgetDate, "", year, month, day, "确定", "取消");
//                    datePickDialog.show();
                    initCustomTimePicker(mTxtBuydate.getText().toString());
                        pvCustomTime.show(mTxtBuydate);
                    break;

                case R.id.manage_car_layout4:
                    // 修改上次保养里程
                    Intent mIntent2 = new Intent(ManageCarActivity.this,
                            EditMileageMaintainActivity.class);
                    startActivityForResult(mIntent2, REQUESTCODE);
                    break;
                case R.id.manage_car_txt_maintaindate:
                    // 修改上次保养日期
//                    Calendar mCalendar2 = Calendar.getInstance();
//                    String maintainDate = SesameLoginInfo.getMainten_time();
//                    int year2;
//                    int month2;
//                    int day2;
//                    if (maintainDate != null && maintainDate.length() > 0) {
//                        String date[] = maintainDate.split("-");
//                        year2 = MyParse.parseInt(date[0]);
//                        month2 = (MyParse.parseInt(date[1]) - 1);
//                        day2 = MyParse.parseInt(date[2]);
//                    } else {
//                        year2 = mCalendar2.get(Calendar.YEAR);
//                        month2 = mCalendar2.get(Calendar.MONTH);
//                        day2 = mCalendar2.get(Calendar.DAY_OF_MONTH);
//                    }
//                    DatePickDialog datePickDialog2 = new DatePickDialog(ManageCarActivity.this,
//                            mIgetDate2, "", year2, month2, day2, "确定", "取消");
//                    datePickDialog2.show();
                    initCustomTimePicker(mTxtMaintainDate.getText().toString());
                    pvCustomTime.show(mTxtMaintainDate);
                    break;
            }
        }
    };

    private void initCustomTimePicker(String date) {

        /**
         * @description
         *
         * 注意事项：
         * 1.自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针.
         * 具体可参考demo 里面的两个自定义layout布局。
         * 2.因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
         * setRangDate方法控制起始终止时间(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
         */
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(2014, 0, 1);
        Calendar endDate = Calendar.getInstance();
        Date time = endDate.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = dateFormat.format(time);
        String[] split = format.split("-");
        endDate.set(Integer.valueOf(split[0]), Integer.valueOf(split[1]) - 1, Integer.valueOf(split[2]));
        if (!TextUtils.isEmpty(date)&&!TextUtils.equals(date,"--")&&!TextUtils.equals(date,"-")) {
            String[] selectedSplit = date.split("-");
            selectedDate.set(Integer.valueOf(selectedSplit[0]), Integer.valueOf(selectedSplit[1]) - 1, Integer.valueOf(selectedSplit[2]));
        }
        //时间选择器 ，自定义布局
        pvCustomTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                HashMap<String, String> params = null;
                carDate = getTime(date);
                switch (v.getId()) {
                    case R.id.manage_car_txt_buydate:
                        params = new HashMap<>();
                        params.put("buydate", carDate);
                        CPControl.GetUpdateCarDateResult(carDate,listener_date);
//                        modifyCarInfoRequest(params, purchaseCallback);
                        break;
                    case R.id.manage_car_txt_maintaindate:
                        params = new HashMap<>();
                        params.put("mainten_date", carDate);
                        CPControl.GetUpdateCarMaintenDateResult(carDate,listener_date_maintain);
//                        modifyCarInfoRequest(params, maintenCallback);
                        break;
                }
            }
        })

                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.time_edit_dialog, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView _OK = (TextView) v.findViewById(R.id.sex_change_OK);
                        final TextView _cancel = (TextView) v.findViewById(R.id.sex_change_cancel);
                        _OK.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.returnData();
                                pvCustomTime.dismiss();
                            }
                        });

                        _cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.dismiss();
                            }
                        });

                    }
                })
                .setContentSize(18)
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("年", "月", "日", "时", "分", "秒")
                .setLineSpacingMultiplier(1.2f)
                .setTextXOffset(0, 0, 0, 40, 0, -40)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(0xFF24AD9D)
                .build();
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    private GetResultListCallback listener_car = new GetResultListCallback() {

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

    private GetResultListCallback listener_date = new GetResultListCallback() {

        @Override
        public void onFinished(Object o) {
            Message msg = new Message();
            msg.what = 2;
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

    private GetResultListCallback listener_date_maintain = new GetResultListCallback() {

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

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    // 修改车型成功
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                    carName = SesameLoginInfo.getCarname();
                    mTxtCarname.setText(carName);
                    Intent mIntent = new Intent();
                    mIntent.setAction(CarMainActivity.CARMAIN_CARYEAR);
                    ManageCarActivity.this.sendBroadcast(mIntent);
                    UUToast.showUUToast(ManageCarActivity.this, "修改车型成功！");
                    com.carlt.doride.control.CPControl.GetCarConfigResult(null);
                    //SesameMainActivity.setRemoteVisible();
                    break;

                case 1:
                    // 修改车型失败
                    BaseResponseInfo mBaseResponseInfo1 = (BaseResponseInfo) msg.obj;

                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                    if (mBaseResponseInfo1 != null) {
                        if (mBaseResponseInfo1.getFlag() == 1020) {
                            Intent intent  = new Intent(ManageCarActivity.this,UpDateActivity.class);
                            startActivity(intent);
//                            PopBoxCreat.showUUUpdateDialog(context, null);
                            return;
                        }

                        String info = mBaseResponseInfo1.getInfo();
                        if (info != null && info.length() > 0) {
                            UUToast.showUUToast(ManageCarActivity.this, info);
                        } else {
                            UUToast.showUUToast(ManageCarActivity.this, "修改车型失败...");
                        }
                    } else {
                        UUToast.showUUToast(ManageCarActivity.this, "修改车型失败...");
                    }
                    break;

                case 2:
                    // 修改购车日期成功
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                    String buyDate = SesameLoginInfo.getBuydate();
                    mTxtBuydate.setText(buyDate);
                    UUToast.showUUToast(ManageCarActivity.this, "修改购车日期成功！");
                    break;

                case 3:
                    // 修改购车日期失败
                    BaseResponseInfo mBaseResponseInfo2 = (BaseResponseInfo) msg.obj;
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                    if (mBaseResponseInfo2 != null) {
                        String info = mBaseResponseInfo2.getInfo();
                        if (info != null && info.length() > 0) {
                            UUToast.showUUToast(ManageCarActivity.this, info);
                        } else {
                            UUToast.showUUToast(ManageCarActivity.this, "修改购车日期失败...");
                        }
                    } else {
                        UUToast.showUUToast(ManageCarActivity.this, "修改购车日期失败...");
                    }
                    break;
                case 4:
                    // 修改上次保养日期成功
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                    String maintainDate = SesameLoginInfo.getMainten_time();
                    Log.e("info", "maintainDate==" + maintainDate);
                    mTxtMaintainDate.setText(maintainDate);
                    UUToast.showUUToast(ManageCarActivity.this, "修改上次保养日期成功！");
                    break;

                case 5:
                    // 修改上次保养日期失败
                    BaseResponseInfo mBaseResponseInfo3 = (BaseResponseInfo) msg.obj;
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                    if (mBaseResponseInfo3 != null) {
                        String info = mBaseResponseInfo3.getInfo();
                        if (info != null && info.length() > 0) {
                            UUToast.showUUToast(ManageCarActivity.this, info);
                        } else {
                            UUToast.showUUToast(ManageCarActivity.this, "修改上次保养日期失败...");
                        }
                    } else {
                        UUToast.showUUToast(ManageCarActivity.this, "修改上次保养日期失败...");
                    }
                    break;
            }
        }

        ;
    };

    /**
     * 选择车型、车系、车款的回调
     */
    private OnCarTypeItemClick mOnCarTypeItemClick = new OnCarTypeItemClick() {

        @Override
        public void onClick(CarModeInfo carModeInfo, int type) {
            switch (type) {
                case SelectCarTypeView.TYPE_MODEL:
                    // 车型
                    brandid = carModeInfo.getId();
                    carLogo = carModeInfo.getCarlogo();
                    mCarTypeView.pullDataSecond(carModeInfo, SelectCarTypeView.TYPE_SERIES);
                    break;
                case SelectCarTypeView.TYPE_SERIES:
                    // 车系
                    optionid = carModeInfo.getId();
                    break;
                case SelectCarTypeView.TYPE_CAR:
                    // 车款
                    if (mCarTypeView != null) {
                        // 让第三级选车型popwindow消失
                        carid = carModeInfo.getId();
                        carName = carModeInfo.getTitle();
                        carYear = carModeInfo.getYear();

                        DialogWithTitleClick click = new DialogWithTitleClick() {

                            @Override
                            public void onRightClick() {
                                // 取消

                            }

                            @Override
                            public void onLeftClick() {
                                // 确定
                                ModifyCarInfo modifyCarInfo = new ModifyCarInfo();
                                optionid = SelectCarTypeView.OPTIONID;
                                brandid = SelectCarTypeView.BRANDID;
                                if (brandid != null && brandid.length() > 0 && optionid != null
                                        && optionid.length() > 0 && carid != null
                                        && carid.length() > 0) {
                                    modifyCarInfo.setBrandid(brandid);
                                    modifyCarInfo.setOptionid(optionid);
                                    modifyCarInfo.setCarid(carid);

                                    modifyCarInfo.setCarname(carName);
                                    modifyCarInfo.setCarlogo(carLogo);
                                    modifyCarInfo.setYear(carYear);
                                } else {
                                    UUToast.showUUToast(ManageCarActivity.this, "您还没有选择爱车车型哦！");
                                    return;
                                }
                                mCarTypeView.dissmiss();
                                mDialog = PopBoxCreat.createDialogWithProgress(
                                        ManageCarActivity.this, "数据提交中...");
                                mDialog.show();
                                CPControl.GetUpdateCarTypeResult(modifyCarInfo, listener_car);
                            }
                        };
                        PopBoxCreat.createDialogWithTitle(ManageCarActivity.this, "提示", "您选择的车型是\n"
                                + carName, "", "确定", "取消", click);
                    }
                    break;
            }

        }
    };

    private boolean isPositive = false;// 是否点击了确定按钮

    protected OnDateSetListener mDateSetListener = new OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        }
    };

    private boolean isPositive2 = false;// 是否点击了确定按钮

    protected OnDateSetListener mDateSetListener2 = new OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        }
    };

    private final IgetDate mIgetDate = new IgetDate() {

        @Override
        public void getDate(int year, int month, int day) {
            month = month + 1;
            StringBuffer mStringBuffer = new StringBuffer();
            mStringBuffer.append(year + "-");
            if (month < 10) {
                mStringBuffer.append("0" + month + "-");
            } else {
                mStringBuffer.append(month + "-");
            }
            if (day < 10) {
                mStringBuffer.append("0" + day);
            } else {
                mStringBuffer.append(day);
            }
            String buyDate = SesameLoginInfo.getBuydate();
            if (buyDate.equals(mStringBuffer.toString())) {
                // 是否改变了日期
                UUToast.showUUToast(ManageCarActivity.this, "您还没有更改购车时间哦");
            } else {
                String current = MyTimeUtil.getDateFormat1();
                String[] dates = current.split("-");
                int year_cur = MyParse.parseInt(dates[0]);
                int month_cur = MyParse.parseInt(dates[1]);
                int day_cur = MyParse.parseInt(dates[2]);
                if (year_cur < year) {
                    UUToast.showUUToast(ManageCarActivity.this, "您选择的日期有误...");
                    return;
                } else if (year_cur == year) {
                    if (month_cur < month) {
                        UUToast.showUUToast(ManageCarActivity.this, "您选择的日期有误...");
                        return;
                    } else if (month_cur == month) {
                        if (day_cur < day) {
                            UUToast.showUUToast(ManageCarActivity.this, "您选择的日期有误...");
                            return;
                        } else {
                            if (mDialog != null) {
                                mDialog.show();
                            }
                            CPControl.GetUpdateCarDateResult(mStringBuffer.toString(),
                                    listener_date);
                        }
                    } else {
                        mDialog = PopBoxCreat.createDialogWithProgress(ManageCarActivity.this,
                                "数据提交中...");
                        mDialog.show();
                        CPControl.GetUpdateCarDateResult(mStringBuffer.toString(), listener_date);
                    }
                } else {
                    mDialog = PopBoxCreat.createDialogWithProgress(ManageCarActivity.this,
                            "数据提交中...");
                    mDialog.show();
                    CPControl.GetUpdateCarDateResult(mStringBuffer.toString(), listener_date);
                }
            }
        }

    };

    private final IgetDate mIgetDate2 = new IgetDate() {

        @Override
        public void getDate(int year, int month, int day) {

            month = month + 1;
            StringBuffer mStringBuffer = new StringBuffer();
            mStringBuffer.append(year + "-");
            if (month < 10) {
                mStringBuffer.append("0" + month + "-");
            } else {
                mStringBuffer.append(month + "-");
            }
            if (day < 10) {
                mStringBuffer.append("0" + day);
            } else {
                mStringBuffer.append(day);
            }
            String maintainDate = SesameLoginInfo.getMainten_time();
            if (maintainDate.equals(mStringBuffer.toString())) {
                // 是否改变了日期
                UUToast.showUUToast(ManageCarActivity.this, "您还没有更改日期哦");
            } else {
                String current = MyTimeUtil.getDateFormat1();
                String[] dates = current.split("-");
                int year_cur = MyParse.parseInt(dates[0]);
                int month_cur = MyParse.parseInt(dates[1]);
                int day_cur = MyParse.parseInt(dates[2]);

                if (year_cur < year) {
                    UUToast.showUUToast(ManageCarActivity.this, "您选择的日期有误...");
                    return;
                } else if (year_cur == year) {
                    if (month_cur < month) {
                        UUToast.showUUToast(ManageCarActivity.this, "您选择的日期有误...");
                        return;
                    } else if (month_cur == month) {
                        if (day_cur < day) {
                            UUToast.showUUToast(ManageCarActivity.this, "您选择的日期有误...");
                            return;
                        } else {
                            if (mDialog != null) {
                                mDialog.show();
                            }
                            CPControl.GetUpdateCarMaintenDateResult(mStringBuffer.toString(),
                                    listener_date_maintain);
                        }
                    } else {
                        mDialog = PopBoxCreat.createDialogWithProgress(ManageCarActivity.this,
                                "数据提交中...");
                        mDialog.show();
                        CPControl.GetUpdateCarMaintenDateResult(mStringBuffer.toString(),
                                listener_date_maintain);
                    }
                } else {
                    mDialog = PopBoxCreat.createDialogWithProgress(ManageCarActivity.this,
                            "数据提交中...");
                    mDialog.show();
                    CPControl.GetUpdateCarMaintenDateResult(mStringBuffer.toString(),
                            listener_date_maintain);
                }
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == NONE)
            return;
        if (resultCode == REQUESTCODE + 1) {
            // String newData = data.getStringExtra(NEWDATA);
            // mTxtMileage.setText(newData);
        } else if (resultCode == REQUESTCODE + 2) {
            String newData = data.getStringExtra(NEWDATA);
            mTxtMaintainMile.setText(newData);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void OnImgLoadFinished(String url, Bitmap mBitmap) {
        if (url != null && url.length() > 0 && mBitmap != null) {
            mCarTypeView.refreshCarlogo();
        }

        super.OnImgLoadFinished(url, mBitmap);
    }

    ;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {
                    0, 0
            };
            // 获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right && event.getY() > top
                    && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

}
