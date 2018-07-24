
package com.carlt.sesame.ui.view.general;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.carlt.sesame.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.data.LoginInfo;
import com.carlt.sesame.ui.view.PopBoxCreat;
import com.carlt.sesame.utility.UUToast;

/**
 * 通用座驾-自动升窗功能View
 * 
 * @author daisy
 */
public class GeneralWindowView extends GeneralBaseView {

    private Dialog mDialog;

    private long click_time;// 点击View时的时间

    private final static long DURATION = 60 * 1000;// 超时判断时长

    private final static long DURATION_EVERY = 5 * 1000;// 轮询间隔

    public GeneralWindowView(Context context) {
        super(context);
        int[] imgIds = {
                R.drawable.general_window_gray, R.drawable.general_window_light,
                R.drawable.general_window_light, R.drawable.general_window_dark
        };
        setImgIds(imgIds);
        String[] stateStrings = {
                "自动升窗/暂不支持", "自动升窗", "自动升窗/ON", "自动升窗/OFF"
        };
        setStateStrings(stateStrings);
        setState(LoginInfo.getAutoCloseWinSw());
    }

    @Override
    public void setState(int state) {
        if (state == LoginInfo.WIN_NONE) {
            state = GeneralBaseView.STATE_UNAVAILABLE;
        } else if (state == LoginInfo.WIN_ON) {
            state = GeneralBaseView.STATE_AVAILABLE_ON;
        } else if (state == LoginInfo.WIN_OFF) {
            state = GeneralBaseView.STATE_AVAILABLE_OFF;
        }
        super.setState(state);
    }

    @Override
    protected void onViewClick() {
        if (mDialog == null) {
            mDialog = PopBoxCreat.createDialogWithProgress(context, "正在连接爱车...");
        }

        mDialog.show();
        // 调用接口
        click_time = System.currentTimeMillis();
        mHandler.sendEmptyMessage(2);
    }

    private GetResultListCallback listener = new GetResultListCallback() {

        @Override
        public void onFinished(Object o) {
            mHandler.sendEmptyMessage(0);

        }

        @Override
        public void onErro(Object o) {
            Message msg = new Message();
            msg.what = 1;
            msg.obj = o;
            mHandler.sendMessage(msg);
        }
    };

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    // 成功
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    setState(LoginInfo.getAutoCloseWinSw());
                    if (LoginInfo.getAutoCloseWinSw() == LoginInfo.WIN_ON) {
                        // 开启成功
                        UUToast.showUUToast(context,
                                "您的自动升窗功能已成功开启！如果您的车窗没有防夹功能，请在确保安全的情况下使用。您最好在确认车窗关闭后再离开，以防车窗工作不正常导致的安全疏漏。");
                    } else if (LoginInfo.getAutoCloseWinSw() == LoginInfo.WIN_OFF) {
                        // 关闭成功
                        UUToast.showUUToast(context, "自动升窗功能已成功关闭！");
                    }

                    break;
                case 1:
                    // 失败
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
                            // 继续调用接口
                            mHandler.sendEmptyMessageDelayed(2, DURATION_EVERY);
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
                    // 调用接口
                    CPControl.GetAutoCloseWin(listener);
                    break;
            }
        }

    };
}
