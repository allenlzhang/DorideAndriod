package com.carlt.doride.ui.activity.login;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.doride.DorideApplication;
import com.carlt.doride.R;
import com.carlt.doride.base.BaseActivity;
import com.carlt.doride.control.ActivityControl;
import com.carlt.doride.control.LoginControl;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.data.UseInfo;
import com.carlt.doride.data.login.UserRegisterParams;
import com.carlt.doride.http.retrofitnet.BaseMvcObserver;
import com.carlt.doride.http.retrofitnet.model.BaseErr;
import com.carlt.doride.http.retrofitnet.model.SmsToken;
import com.carlt.doride.http.retrofitnet.model.User;
import com.carlt.doride.http.retrofitnet.model.UserInfo;
import com.carlt.doride.preference.UseInfoLocal;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.systemconfig.URLConfig;
import com.carlt.doride.ui.activity.setting.TermsDeclareActivity;
import com.carlt.doride.ui.view.PopBoxCreat;
import com.carlt.doride.ui.view.UUToast;
import com.carlt.doride.utils.CipherUtils;
import com.carlt.doride.utils.SharepUtil;
import com.carlt.doride.utils.StringUtils;
import com.carlt.sesame.preference.TokenInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class UserRegisterActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private ImageView back;//返回按钮

    private EditText register_phone_input;//手机号码
    private EditText register_vcode_input;//验证码
    private EditText register_passwd_et;//输入修改密码
    private EditText register_passwd_again_et;//再次输入

    private ImageView register_passwd_toggle;//显示&隐藏密码按钮
    private ImageView register_passwd_again_toggle;//显示&隐藏密码按钮

    private TextView titleText;//页面标题
    private TextView register_txt_declaration;//页面标题
    private TextView register_verification_send;//发送验证码按钮
    private TextView register_commit;//确认修改

    private CheckBox register_check;

    private Dialog mDialog;

    private UserRegisterParams registerParams = new UserRegisterParams();

    private final static String URL_PROVISION = "http://m.cheler.com/doride.html";// 服务条款URL

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        initComponent();
    }

    private void initComponent() {
        back = findViewById(R.id.back);
        back.setOnClickListener(this);
        titleText = findViewById(R.id.title);
        titleText.setText("注册");

        register_phone_input = findViewById(R.id.register_phone_input);
        register_vcode_input = findViewById(R.id.register_vcode_input);
        register_passwd_et = findViewById(R.id.register_passwd_et);
        register_passwd_again_et = findViewById(R.id.register_passwd_again_et);

        register_passwd_toggle = findViewById(R.id.register_passwd_toggle);
        register_passwd_toggle.setOnClickListener(this);
        register_passwd_again_toggle = findViewById(R.id.register_passwd_again_toggle);
        register_passwd_again_toggle.setOnClickListener(this);

        register_verification_send = findViewById(R.id.register_verification_send);
        register_verification_send.setOnClickListener(this);

        register_check = findViewById(R.id.register_check);
        register_check.setOnCheckedChangeListener(this);

        register_txt_declaration = findViewById(R.id.register_txt_declaration);
        register_txt_declaration.setOnClickListener(this);

        register_commit = findViewById(R.id.register_commit);
        register_commit.setOnClickListener(this);

    }

    private void getSmsToken(final String phoneNum, final int type) {
        final Map<String, String> param = new HashMap<>();
        param.put("mobile", phoneNum);
        addDisposable(mApiService.getSmsToken(param), new BaseMvcObserver<SmsToken>() {
            @Override
            public void onSuccess(SmsToken result) {
                if (result.err != null) {
                    showToast(result.err.msg);
                } else {
                    sendSmsCode(phoneNum, type, result.token);

                }
            }

            @Override
            public void onError(String msg) {
                showToast(msg);
            }
        });
    }

    private void sendSmsCode(String phoneNum, int type, String token) {

        Map<String, Object> map = new HashMap<>();
        map.put("mobile", phoneNum);
        map.put("type", type);
        map.put("smsToken", token);
        addDisposable(mApiService.SendSmsCode(map), new BaseMvcObserver<BaseErr>() {
            @Override
            public void onSuccess(BaseErr result) {
                //                Message msg = Message.obtain();
                //                msg.what = 0;
                //                msg.obj = result;
                //                mHandler.sendMessage(msg);
                if (result.code == 0) {
                    showToast("发送成功");
                    count = 60;
                    register_verification_send.setText(count + "秒后重发");
                    register_verification_send.setEnabled(false);

                    task = new TimerTask() {

                        @Override
                        public void run() {
                            Message msg = new Message();
                            msg.what = 10;
                            mHandler.sendMessage(msg);

                        }
                    };
                    timer.schedule(task, 1000, 1000);
                } else {
                    showToast(result.msg);
                }
            }

            @Override
            public void onError(String msg) {
                showToast(msg);
                //                Message msg1 = Message.obtain();
                //                msg1.what = 1;
                //                msg1.obj = msg;
                //                mHandler.sendMessage(msg1);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_passwd_toggle:
                ActivityControl.passwdToggle(this, register_passwd_et, register_passwd_toggle, view.getTag().toString());
                if (!TextUtils.isEmpty(register_passwd_et.getText().toString())) {
                    register_passwd_et.setSelection(register_passwd_et.getText().toString().length());
                }
                break;
            case R.id.register_passwd_again_toggle:
                ActivityControl.passwdToggle(this, register_passwd_again_et, register_passwd_again_toggle, view.getTag().toString());
                if (!TextUtils.isEmpty(register_passwd_again_et.getText().toString())) {
                    register_passwd_again_et.setSelection(register_passwd_again_et.getText().toString().length());
                }
                break;
            case R.id.register_verification_send:
                String cellPhone = register_phone_input.getText().toString();
                if (TextUtils.isEmpty(cellPhone) || !StringUtils.checkCellphone(cellPhone)) {
                    UUToast.showUUToast(this, getResources().getString(R.string.cell_phone_error));
                } else {
                    //                    CPControl.GetMessageValidateResult("1", cellPhone, validateCodeListener);
                    getSmsToken(cellPhone, 1);

                }
                break;
            case R.id.register_txt_declaration:
                Intent termsDeclare = new Intent(this, TermsDeclareActivity.class);
                termsDeclare.putExtra(TermsDeclareActivity.URL_INFO, URL_PROVISION);
                startActivity(termsDeclare);
                break;
            case R.id.register_commit:
                String commitPhone = register_phone_input.getText().toString();
                String commitVCode = register_vcode_input.getText().toString();
                String passwd = register_passwd_et.getText().toString();
                String passwdAgain = register_passwd_again_et.getText().toString();

                if (!isCommitInvalid(commitPhone, commitVCode, passwd, passwdAgain))
                    return;

                registerParams.setMobile(commitPhone);
                registerParams.setValidate(commitVCode);
                registerParams.setPassword(passwdAgain);

                mDialog = PopBoxCreat
                        .createDialogWithProgress(UserRegisterActivity.this, "正在加载");
                mDialog.show();
                //                CPControl.GetRegisteResult(registerParams, listener_register);
                register(commitPhone, passwdAgain, commitVCode, 1);
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    //RegStateByPWd 注册类型
    private void register(String phoneNum, String pwd, String code, int RegStateByPWd) {
        Map<String, Object> params = new HashMap<>();
        params.put("mobile", phoneNum);
        params.put("password", CipherUtils.md5(pwd));
        params.put("validate", code);
        params.put("regType", RegStateByPWd);
        addDisposable(mApiService.commonReg(params), new BaseMvcObserver<BaseErr>() {
            @Override
            public void onSuccess(BaseErr result) {
                Message msg = new Message();
                msg.what = 2;
                mHandler.sendMessage(msg);
            }

            @Override
            public void onError(String msg) {
                Message msg1 = new Message();
                msg1.what = 3;
                msg1.obj = msg;
                mHandler.sendMessage(msg1);
            }
        });
    }

    /**
     * 倒计时
     */
    private int count = 60;

    private Timer timer = new Timer();

    private TimerTask task;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            BaseResponseInfo mBaseResponseInfo = null;
            switch (msg.what) {
                case 0:
                    //                    UUToast.showUUToast(UserRegisterActivity.this, msg.obj.toString());

                    break;
                case 1:
                    // 停止计时
                    if (timer != null) {
                        if (task != null) {
                            task.cancel();
                        }
                    }
                    register_verification_send.setEnabled(true);
                    register_verification_send.setText(R.string.usercenter_push_validate1);
                    showToast((String) msg.obj);
                    //                    mBaseResponseInfo = (BaseResponseInfo) msg.obj;
                    //                    int flag = mBaseResponseInfo.getFlag();
                    //                    if (flag == BaseResponseInfo.PHONE_REGISTERED) {
                    //                        UUToast.showUUToast(UserRegisterActivity.this,
                    //                                 mBaseResponseInfo.getInfo());
                    //                    } else {
                    //                        UUToast.showUUToast(UserRegisterActivity.this,
                    //                                 mBaseResponseInfo.getInfo());
                    //                    }
                    break;
                case 2:
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    UseInfo mUseInfo = UseInfoLocal.getUseInfo();

                    String commitPhone = register_phone_input.getText().toString();
                    String passwd = register_passwd_et.getText().toString();

                    mUseInfo.setAccount(commitPhone);
                    mUseInfo.setPassword(registerParams.getPassword());
                    UseInfoLocal.setUseInfo(mUseInfo);

                    UUToast.showUUToast(UserRegisterActivity.this, "注册成功！");
                    Login(register_phone_input.getText().toString(), register_passwd_again_et.getText().toString());

                    break;
                case 3:
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    String txt = (String) msg.obj;
                    showToast(txt);
                    //                    mBaseResponseInfo = (BaseResponseInfo)msg.obj;
                    //                    UUToast.showUUToast(UserRegisterActivity.this, "不好意思，注册失败，请稍候再试:"
                    //                            + mBaseResponseInfo.getInfo());
                    break;
                case 10:
                    count--;
                    if (count > 0) {
                        register_verification_send.setText(count + "秒后重发");
                    } else {
                        if (timer != null) {
                            if (task != null) {
                                task.cancel();
                            }
                        }
                        register_verification_send.setEnabled(true);
                        register_verification_send.setText(R.string.usercenter_push_validate1);
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void Login(String account, String password) {
        HashMap<String, Object> mMap = new HashMap<>();
        mMap.put("version", DorideApplication.Version);
        mMap.put("moveDeviceName", DorideApplication.MODEL_NAME);
        mMap.put("loginModel", DorideApplication.MODEL);
        mMap.put("loginSoftType", "Android");
        mMap.put("moveDeviceid", DorideApplication.NIMEI);
        mMap.put("mobile", account);
        mMap.put("password", CipherUtils.md5(password));
        mMap.put("loginType", 1);
        mMap.put("pwdReally", password);
        addDisposable(mApiService.commonLogin(mMap), new BaseMvcObserver<User>() {
            @Override
            public void onSuccess(User result) {
                if (result.err != null) {
                    showToast("登录失败");
                    Intent intent = new Intent(UserRegisterActivity.this, UserLoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    TokenInfo.setToken(result.token);
//                    ActivityControl.initXG();
                    HashMap<String, Object> prams = new HashMap<>();
                    prams.put("token", result.token);
                    getUserInfo(prams);
                }
            }

            @Override
            public void onError(String msg) {
                showToast("登录失败");
                Intent intent = new Intent(UserRegisterActivity.this, UserLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void getUserInfo(HashMap<String, Object> prams) {
        addDisposable(mApiService.getUserInfo(prams), new BaseMvcObserver<UserInfo>() {
            @Override
            public void onSuccess(UserInfo result) {
                if (result.err != null) {
                    showToast("登录失败");
                    Intent intent = new Intent(UserRegisterActivity.this, UserLoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.e("userInfo", result.toString());
                    UserInfo.getInstance().setUserInfo(result);
                    SharepUtil.putByBean(URLConfig.USER_INFO, result);
                    LoginControl.logic(UserRegisterActivity.this);
                    finish();
                }

            }

            @Override
            public void onError(String msg) {
                showToast("登录失败");
                Intent intent = new Intent(UserRegisterActivity.this, UserLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private BaseParser.ResultCallback validateCodeListener = new BaseParser.ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            //            Message msg = Message.obtain();
            //            msg.what = 0;
            //            msg.obj = UserRegisterActivity.this.getResources().getString(R.string.vcode_send_success);
            //            mHandler.sendMessage(msg);
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            Message msg = Message.obtain();
            msg.what = 1;
            msg.obj = bInfo;
            mHandler.sendMessage(msg);
        }
    };

    private BaseParser.ResultCallback listener_register = new BaseParser.ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            Message msg = new Message();
            msg.what = 2;
            msg.obj = bInfo;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            Message msg = new Message();
            msg.what = 3;
            msg.obj = bInfo;
            mHandler.sendMessage(msg);
        }
    };

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        register_commit.setClickable(isChecked);
        if (isChecked) {
            register_commit.setEnabled(true);
        } else {
            register_commit.setEnabled(false);
        }

    }

    private boolean isCommitInvalid(String phone, String vcode, String passwd, String passwdAgain) {
        if (TextUtils.isEmpty(phone) || !StringUtils.checkCellphone(phone)) {
            UUToast.showUUToast(this, getResources().getString(R.string.cell_phone_error));
            return false;
        } else if (TextUtils.isEmpty(vcode)) {
            UUToast.showUUToast(this, "验证码不能为空");
            return false;
        } else if (TextUtils.isEmpty(passwd) || passwd.length() < 6) {
            UUToast.showUUToast(this, "密码至少为6位");
            return false;
        } else if (TextUtils.isEmpty(passwdAgain) || !passwd.equals(passwdAgain)) {
            UUToast.showUUToast(this, "两次输入密码不一致");
            return false;
        } else {
            return true;
        }

    }

}
