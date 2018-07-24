
package com.carlt.sesame.ui.activity.usercenter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.sesame.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.data.LoginInfo;
import com.carlt.sesame.data.UseInfo;
import com.carlt.sesame.data.car.BindDeviceInfo;
import com.carlt.sesame.data.car.CarModeInfo;
import com.carlt.sesame.data.set.TransferNewCheckInfo;
import com.carlt.sesame.preference.UseInfoLocal;
import com.carlt.sesame.ui.MainActivity;
import com.carlt.sesame.ui.activity.base.BaseActivity;
import com.carlt.sesame.ui.activity.usercenter.login.ActivateActivity;
import com.carlt.sesame.ui.activity.usercenter.login.LoginActivity;
import com.carlt.sesame.ui.activity.usercenter.scan.ScanActivity;
import com.carlt.sesame.ui.view.PopBoxCreat;
import com.carlt.sesame.ui.view.PopBoxCreat.DialogWithTitleClick;
import com.carlt.sesame.ui.view.UUTransferDialog;
import com.carlt.sesame.utility.StringUtils;
import com.carlt.sesame.utility.UUToast;

/**
 * 输入VPIN码页面
 *
 * @author daisy
 */
public class SelectCarBindActivity extends BaseActivity {

    private TextView mBtn;// 确定按钮

    private Dialog mDialog;

    // 等待过户对话框
    private UUTransferDialog mTDialog;

    public final static String FROM_NAME = "from_name";

    public final static String CLASS_START = "com.carlt.sesame.ui.StartActivity";// 从启动页跳转过来的

    public final static String CLASS_LOGIN = "com.carlt.sesame.ui.activity.usercenter.login.LoginActivity";// 从登录页跳转过来的

    public final static String CLASS_SCAN = "com.carlt.sesame.ui.activity.usercenter.TwoDemisonCodeActivity";// 从二维码扫描页跳转过来的

    public final static String CLASS_ACTIVATE = "com.carlt.sesame.ui.activity.usercenter.login.ActivateActivity";// 从激活页面跳转过来

    public final static int TO_BIND_DEVICEID = 2707;

    private String fromName;

    String code = "";

    boolean isCancle = false;

    private SelectCarTypeView mCarTypeView;// 选择车型View


    private String brandid;// 车型id

    private String optionid;// 车系id

    private String carid;// 车款Name

    private String carName;// 车款id

    private View mViewSelectCar;// 选择车型外层view

    private TextView mTxtCar;// 车辆信息

    private EditText mETCarVin;//车架号输入框


    private String need_pin;
    private String deviceidstring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acrivity_selectcar);

        try {
            fromName = getIntent().getStringExtra(FROM_NAME);
        } catch (Exception e) {
        }
        initTitle();
        init();

        carName = LoginInfo.getCarname();
        if (carName != null && carName.length() > 0 && !carName.equals("null")) {
            mTxtCar.setText(carName);
        }

        String mBrandid = LoginInfo.getBrandid();
        if (mBrandid != null && mBrandid.length() > 0) {
            brandid = mBrandid;
        }

        String mOptionid = LoginInfo.getOptionid();
        if (mOptionid != null && mOptionid.length() > 0) {
            optionid = mOptionid;
        }

        String mCarid = LoginInfo.getCarid();
        if (mCarid != null && mCarid.length() > 0) {
            carid = mCarid;
        }

        String mNeed_pin = LoginInfo.getNeed_pin();
        if (!TextUtils.isEmpty(mNeed_pin)) {
            need_pin = mNeed_pin;
        }

        // 判断是否绑定设备
        deviceidstring = LoginInfo.getDeviceidstring();

        String standcarno = LoginInfo.getStandcarno();
        if(!StringUtils.isEmpty(standcarno)){
            mETCarVin.setText(standcarno);
        }

    }

    private void initTitle() {
        ImageView back = (ImageView) findViewById(R.id.head_back_img1);
        TextView title = (TextView) findViewById(R.id.head_back_txt1);
        TextView txtRight = (TextView) findViewById(R.id.head_back_txt2);

        back.setImageResource(R.drawable.arrow_back);
        title.setText("绑定盒子");
        txtRight.setVisibility(View.VISIBLE);
        txtRight.setText("发起过户");

        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                back();
            }
        });

        txtRight.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ScanActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }

    private void init() {
        mBtn = (TextView) findViewById(R.id.activity_bind_btnsure);
        mViewSelectCar = findViewById(R.id.activity_bind_selectcar);
        mTxtCar=(TextView)findViewById(R.id.selectcar_txt);
        mETCarVin = (EditText) findViewById(R.id.activity_bind_car_vin);
        mBtn.setOnClickListener(mClickListener);
        mViewSelectCar.setOnClickListener(mClickListener);
    }

    private OnClickListener mClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.activity_bind_btnsure:
                    if(TextUtils.isEmpty(carName)){
                        UUToast.showUUToast(SelectCarBindActivity.this, "爱车信息不能为空!");
                        return;
                    }
//                    if(StringUtils.isEmpty(deviceidstring)){//未绑定
                        String text = mETCarVin.getText().toString();
                        if(TextUtils.isEmpty(text)){
                            UUToast.showUUToast(SelectCarBindActivity.this,"请输入正确车架号");
                        }else if(text.length() != 17){
                            UUToast.showUUToast(SelectCarBindActivity.this,"请输入正确车架号");
                        }else{
                            if (mDialog == null) {
                                mDialog = PopBoxCreat.createDialogWithProgress(SelectCarBindActivity.this, "绑定中...");
                            }
                            mDialog.show();
//                            CPControl.GetCheckVpinResult(listener, text);
                            CPControl.GetCarBindInfoResult(text,listener);
                        }
//                    }else{//已经绑定过了，直接跳转
//                        Intent mIntent = new Intent(SelectCarBindActivity.this, ActivateActivity.class);
//                        mIntent.putExtra(ActivateActivity.FROM_NAME, SelectCarBindActivity.this.getClass().getName());
//                        mIntent.putExtra("need_pin", need_pin);
//                        startActivity(mIntent);
//                        finish();
//                    }

                    break;
                case R.id.activity_bind_selectcar:
                    // 选择车款
                    if (mCarTypeView == null) {
                        mCarTypeView = new SelectCarTypeView(SelectCarBindActivity.this,
                                mOnCarTypeItemClick);
                    }
                    CarModeInfo carModeInfo1 = new CarModeInfo();
                    carModeInfo1.setTitle(SelectCarTypeView.TITLE);
                    carModeInfo1.setId(SelectCarTypeView.OPTIONID);
                    mCarTypeView.pullDataThird(carModeInfo1, SelectCarTypeView.TYPE_CAR);
                    mCarTypeView.showMenu();
                    break;
            }
        }
    };

    /**
     * 选择车型、车系、车款的回调
     */
    private SelectCarTypeView.OnCarTypeItemClick mOnCarTypeItemClick = new SelectCarTypeView.OnCarTypeItemClick() {

        @Override
        public void onClick(CarModeInfo carModeInfo, int type) {
            switch (type) {
                case SelectCarTypeView.TYPE_MODEL:
                    // 车型
                    brandid = carModeInfo.getId();
                    Log.e("info", "brandid_onClick="+brandid);
                    mCarTypeView.pullDataSecond(carModeInfo, SelectCarTypeView.TYPE_SERIES);
                    break;
                case SelectCarTypeView.TYPE_SERIES:
                    // 车系
                    optionid = carModeInfo.getId();
                    Log.e("info", "optionid_onClick="+optionid);
                    break;
                case SelectCarTypeView.TYPE_CAR:
                    // 车款
                    if (mCarTypeView != null) {
                        // 让第三级选车型popwindow消失
                        carid = carModeInfo.getId();
                        carName = carModeInfo.getTitle();
                        Log.e("info", "carid_onClick="+carid);

                        DialogWithTitleClick click = new DialogWithTitleClick() {

                            @Override
                            public void onRightClick() {
                                // 取消

                            }

                            @Override
                            public void onLeftClick() {

                                // 确定
                                if (mCarTypeView != null) {
                                    mCarTypeView.dissmiss();
                                }
                                mTxtCar.setText(carName);
                                CPControl.GetSetCarInfoResult(SelectCarTypeView.BRANDID, SelectCarTypeView.OPTIONID, carid, carName, new GetResultListCallback() {
                                    @Override
                                    public void onFinished(Object o) {
                                        Message msg = new Message();
                                        msg.what = 11;
                                        msg.obj = o;
                                        mHandler.sendMessage(msg);
                                    }
                                    @Override
                                    public void onErro(Object o) {
                                        Message msg = new Message();
                                        msg.what = 12;
                                        msg.obj = o;
                                        mHandler.sendMessage(msg);
                                    }
                                });
                            }
                        };
                        PopBoxCreat.createDialogWithTitle(SelectCarBindActivity.this, "提示",
                                "您选择的车型是\n" + carName, "", "确定", "取消", click);
                    }
                    break;
            }
        }
    };


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

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
            switch (msg.what) {

                case 11://选择车型成功
                    break;
                case 12://选择车型失败
                    break;
                case 0:
                    // 绑定码验证成功
                    UUToast.showUUToast(SelectCarBindActivity.this, "信息提交成功!");
                    BindDeviceInfo info = (BindDeviceInfo) msg.obj;
                    LoginInfo.setStandcarno(mETCarVin.getText().toString());
                    need_pin = info.getNeed_pin();
                    Intent mIntent = new Intent(SelectCarBindActivity.this, ActivateActivity.class);
                    mIntent.putExtra(ActivateActivity.FROM_NAME, SelectCarBindActivity.this.getClass().getName());
                    mIntent.putExtra("need_pin", need_pin);
                    startActivity(mIntent);
                    finish();
                    break;

                case 1:
                    // 绑定盒子失败
                    BaseResponseInfo mInfo = (BaseResponseInfo) msg.obj;
                    if (mInfo != null) {
                        UUToast.showUUToast(SelectCarBindActivity.this, mInfo.getInfo());
                    } else {
                        UUToast.showUUToast(SelectCarBindActivity.this, "绑定设备失败...");
                    }
                    break;
                case 2:
                    //开启轮询
                    mHandler.sendEmptyMessage(4);
                    break;
                case 3:
                    BaseResponseInfo bInfo = (BaseResponseInfo) msg.obj;
                    if (null != bInfo && !TextUtils.isEmpty(bInfo.getInfo())) {
                        UUToast.showUUToast(context, bInfo.getInfo());
                    } else {
                        UUToast.showUUToast(context, "网络原因，请求过户失败！");
                    }
                    mTDialog.dismiss();
                    break;
                case 4:
                    if (!isCancle) {
                        CPControl.GetTransferNewCheckResult(code, transferCheckCallback);
                        isCancle = false;
                    }
                    break;
                case 5:
                    TransferNewCheckInfo tni = (TransferNewCheckInfo) msg.obj;
                    if (tni != null && !TextUtils.isEmpty(tni.getStatus())) {
                        if ("1".equals(tni.getStatus())) {
                            mHandler.sendEmptyMessageDelayed(4, 1000);
                        } else if ("2".equals(tni.getStatus())) {
                            UUToast.showUUToast(context, "过户成功！");
                            //-----这里可能需要调用一次登陆接口
                            UseInfo mUseInfo = UseInfoLocal.getUseInfo();
                            String account = mUseInfo.getAccount();
                            String password = mUseInfo.getPassword();
                            CPControl.GetLogin(account, password, new GetResultListCallback() {

                                @Override
                                public void onFinished(Object o) {
                                    mTDialog.dismiss();
                                    Intent intent = new Intent(context, MainActivity.class);
                                    intent.putExtra("showTransferDialog", true);
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onErro(Object o) {
                                    mTDialog.dismiss();
                                    UUToast.showUUToast(context, "获取账户信息失败，请重新登录！");
                                    back();
                                    finish();
                                }
                            });

                        } else if ("3".equals(tni.getStatus())) {
                            UUToast.showUUToast(context, "过户失败，对方拒绝过户！");
                            mTDialog.dismiss();
                        }
                    } else {
                        UUToast.showUUToast(context, "网络原因，请求过户失败！");
                        mTDialog.dismiss();
                    }
                    break;
                case 6:
//				BaseResponseInfo bInfo2 = (BaseResponseInfo) msg.obj;
//				if(null != bInfo2 && TextUtils.isEmpty(bInfo2.getInfo())){
//					UUToast.showUUToast(context, bInfo2.getInfo());
//				}else{
//					UUToast.showUUToast(context, "网络原因，请求过户失败！");
//				}
                    mHandler.sendEmptyMessageDelayed(4, 1000);
                    break;
            }
        }

    };

    private void back() {
        if (fromName.equals(CLASS_START)) {
            Intent mIntent = new Intent(SelectCarBindActivity.this, LoginActivity.class);
            startActivity(mIntent);
        } else if (fromName.equals(CLASS_LOGIN)) {

        } else if (fromName.equals(CLASS_ACTIVATE)) {
            Intent mIntent = new Intent(SelectCarBindActivity.this, LoginActivity.class);
            startActivity(mIntent);
        } else if (fromName.equals(CLASS_SCAN)) {
            Intent mIntent = new Intent(SelectCarBindActivity.this, LoginActivity.class);
            startActivity(mIntent);
        }

        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            back();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();

        String mobile = LoginInfo.getMobile();
        String vpin = LoginInfo.getVpin(mobile);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && data != null) {
            code = data.getStringExtra("Code");
            if (TextUtils.isEmpty(code)) {
                UUToast.showUUToast(context, "您扫描的二维码有误，请重新扫描");
            } else {
                isCancle = false;
                // --调用过户接口
                mTDialog = PopBoxCreat.createUUTransferDialog(context,
                        "已发出过户请求正在等待处理",
                        "取消",
                        "",
                        new DialogWithTitleClick() {

                            @Override
                            public void onRightClick() {
                            }

                            @Override
                            public void onLeftClick() {
                                if (mTDialog != null && mTDialog.isShowing()) {
                                    mTDialog.dismiss();
                                }
                                isCancle = true;
                                CPControl.TransferNewCancleControl(code, new GetResultListCallback() {

                                    @Override
                                    public void onFinished(Object o) {
                                    }

                                    @Override
                                    public void onErro(Object o) {
                                    }
                                });
                            }
                        });
                mTDialog.show();
                CPControl.GetTransferNewOptResult(code, optTransferCallback);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private GetResultListCallback optTransferCallback = new GetResultListCallback() {

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

    private GetResultListCallback transferCheckCallback = new GetResultListCallback() {

        @Override
        public void onFinished(Object o) {
            Message msg = new Message();
            msg.what = 5;
            msg.obj = o;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onErro(Object o) {
            Message msg = new Message();
            msg.what = 6;
            msg.obj = o;
            mHandler.sendMessage(msg);
        }
    };
}
