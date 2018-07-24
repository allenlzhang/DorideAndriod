
package com.carlt.sesame.ui.view.general;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.carlt.doride.DorideApplication;
import com.carlt.sesame.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.data.LoginInfo;
import com.carlt.sesame.ui.view.PopBoxCreat;
import com.carlt.sesame.utility.CheckInfo;
import com.carlt.sesame.utility.UUToast;

/**
 * 通用座驾-远程启动功能View
 * 
 * @author daisy
 */
public class GeneralStartView extends GeneralBaseView {

    private Dialog mDialog;

    private String password;

    private long click_time;// 点击View时的时间

    private final static long DURATION = 60 * 1000;// 超时判断时长

    private final static long DURATION_EVERY = 5 * 1000;// 轮询间隔

    public GeneralStartView(Context context) {
        super(context);
        int[] imgIds = {
                R.drawable.general_start_gray, R.drawable.general_start_light,
                R.drawable.general_start_light, R.drawable.general_start_dark
        };
        setImgIds(imgIds);
        String[] stateStrings = {
                "远程启动/暂不支持", "远程启动", "远程启动/已启动", "远程启动"
        };
        setStateStrings(stateStrings);

        setState(LoginInfo.getRemoteStart());
    }

    @Override
    public void setState(int state) {
        if (state == LoginInfo.START_NONE) {
            state = GeneralBaseView.STATE_UNAVAILABLE;
        } else if (state == LoginInfo.START_ON) {
            state = GeneralBaseView.STATE_AVAILABLE_ON;
        } else if (state == LoginInfo.START_OFF) {
            state = GeneralBaseView.STATE_AVAILABLE_OFF;
        }
        super.setState(state);
    }

    @Override
    protected void onViewClick() {
        if (LoginInfo.getRemoteStart() == LoginInfo.START_ON) {
            // 当前为开启状态-调用取消接口
            if (mDialog == null) {
                mDialog = PopBoxCreat.createDialogWithProgress(context, "正在连接爱车...");
            }
            mDialog.show();
            // 调用取消接口
            click_time = System.currentTimeMillis();
            CPControl.GetCancelRemoteStart(listener_cancle);
        } else if (LoginInfo.getRemoteStart() == LoginInfo.START_OFF) {
            // 当前为关闭状态-调用开启接口
            // 确认密码
            showEditDialog();
        }
    }

    /**
     * 输入密码Dialog
     */
    private void showEditDialog() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_withedit, null);
        final Dialog dialogI = new Dialog(context, R.style.dialog);
        final EditText editPassword = (EditText)view.findViewById(R.id.dialog_withedit_edt);
        TextView btn1 = (TextView)view.findViewById(R.id.dialog_withedit_btn1);
        TextView btn2 = (TextView)view.findViewById(R.id.dialog_withedit_btn2);

        btn1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                password = editPassword.getText().toString();
                String checkPsw=CheckInfo.checkPassword(password);
                if (!checkPsw.equals(CheckInfo.CORRECT_PSWLENTH)) {
                    UUToast.showUUToast(context, checkPsw);
                } else {
                    if (mDialog == null) {
                        mDialog = PopBoxCreat.createDialogWithProgress(context, "正在连接爱车...");
                    }
                    mDialog.show();
                    // 调用接口
                    click_time = System.currentTimeMillis();
                    mHandler.sendEmptyMessage(10);
                }
                dialogI.dismiss();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                dialogI.dismiss();
            }
        });

        int w = (int)(DorideApplication.ScreenDensity * 300);
        ViewGroup.LayoutParams parm = new ViewGroup.LayoutParams(w, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialogI.setContentView(view, parm);

        dialogI.show();

    }

    private GetResultListCallback listener_start = new GetResultListCallback() {

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

    private GetResultListCallback listener_cancle = new GetResultListCallback() {

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

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    // 启动成功
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    setState(LoginInfo.getRemoteStart());

                    UUToast.showUUToast(context, "操作成功");
                    break;
                case 1:
                    // 启动失败
                    long current_time = System.currentTimeMillis();
                    long d_valuew = current_time - click_time;
                    if (d_valuew < DURATION) {
                        BaseResponseInfo mInfo = (BaseResponseInfo)msg.obj;
                        if (mInfo.getFlag() > 3000) {
                            // 提示失败信息
                            if (mDialog != null && mDialog.isShowing()) {
                                mDialog.dismiss();
                            }
                            if (mInfo != null) {
                                UUToast.showUUToast(context, mInfo.getInfo());
                            }
                        } else {
                            // 继续调用启动接口
                            mHandler.sendEmptyMessageDelayed(10, DURATION_EVERY);
                        }

                    } else {
                        // 提示失败信息
                        if (mDialog != null && mDialog.isShowing()) {
                            mDialog.dismiss();
                        }
                        BaseResponseInfo mInfo = (BaseResponseInfo)msg.obj;
                        if (mInfo != null) {
                            UUToast.showUUToast(context, mInfo.getInfo());
                        }

                    }
                    break;
                case 2:
                    // 取消成功
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    setState(LoginInfo.getRemoteStart());
                    UUToast.showUUToast(context, "操作成功");
                    break;
                case 3:
                    // 取消失败
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    BaseResponseInfo mInfo = (BaseResponseInfo)msg.obj;
                    if (mInfo != null) {
                        UUToast.showUUToast(context, mInfo.getInfo());
                    }
                    break;
                case 10:
                    // 调用启动接口
                    CPControl.GetRemoteStart(listener_start);
                    break;
            }
        }

    };

}
