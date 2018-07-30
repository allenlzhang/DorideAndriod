
package com.carlt.sesame.utility;

import android.os.Handler;
import android.os.Message;

import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.SesameLoginInfo;
import com.carlt.sesame.data.UseInfo;
import com.carlt.sesame.preference.UseInfoLocal;
import com.carlt.sesame.ui.SesameMainActivity;

/**
 * @author Y.yun
 */
public class LoginChecker extends Thread {
    public static boolean isCheck = false;

    private LoginChecker() {
    }

    public static void startCheck() {
        if (!isCheck) {
            isCheck = true;
            new LoginChecker().start();
        }
    }

    public static void stopCheck() {
        isCheck = false;
    }

    @Override
    public void run() {
        Log.e("info", "LoginChecker--------");
        while (isCheck) {

            int ca = 0;
            if (SesameLoginInfo.Last_Login_Time < 0) {
                SesameLoginInfo.Last_Login_Time = System.currentTimeMillis();
            } else {
                int dayL = (int)(SesameLoginInfo.Last_Login_Time / 1000 / 60 / 60 / 24);// 天数取整
                int dayN = (int)(System.currentTimeMillis() / 1000 / 60 / 60 / 24);// 天数取整
                ca = dayN - dayL;
            }

            if (ca > 0) {

                UseInfo mUseInfo = UseInfoLocal.getUseInfo();
                String account = mUseInfo.getAccount();
                String password = mUseInfo.getPassword();
                if (account != null && account.length() > 0 && password != null
                        && password.length() > 0) {
                    Log.e("info", "LoginChecker--------login-------");
                    // 跨天拉取数据
                    CPControl.GetLogin(account, password, new GetResultListCallback() {

                        @Override
                        public void onFinished(Object o) {
                            mHandler.sendEmptyMessage(0);
                        }

                        @Override
                        public void onErro(Object o) {
                            mHandler.sendEmptyMessage(1);
                        }
                    });
                }

            }

            try {
                Thread.sleep(1000 * 60 * 60);
            } catch (InterruptedException e) {
            }
        }
    }
    
    private GetResultListCallback listener_extInfo=new GetResultListCallback() {
        
        @Override
        public void onFinished(Object o) {
            mHandler.sendEmptyMessage(2);
            
        }
        
        @Override
        public void onErro(Object o) {
            mHandler.sendEmptyMessage(3);
            
        }
    };

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //登录成功
                    CPControl.GetExtInfoResult(listener_extInfo);
                    break;
                case 1:
                  //登录成功
                    break;
                case 2:
                  //获取用户相关信息成功
                    SesameMainActivity.setDotVisiable();
                    break;
                case 3:
                    //获取用户相关信息失败
                    break;
            }
        }

    };

}
