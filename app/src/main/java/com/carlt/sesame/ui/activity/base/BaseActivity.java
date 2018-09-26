package com.carlt.sesame.ui.activity.base;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.carlt.sesame.control.ActivityControl;
import com.carlt.sesame.control.DragViewCtr;
import com.carlt.sesame.http.AsyncImageLoader;
import com.carlt.sesame.http.AsyncImageLoader.AsyncImageLoaderListener;
import com.carlt.sesame.utility.Log;

import java.util.ArrayList;

public class BaseActivity extends Activity implements AsyncImageLoaderListener, BeforeGoToBackground {

    /**
     * 标识是否还有页面显示
     */
    protected        boolean                         mIsShowing  = false;
    /**
     * 进入后台之前 要干的事的集合
     */
    protected static ArrayList<BeforeGoToBackground> mBackDoList = new ArrayList<BeforeGoToBackground>();

    protected Context                   context;
    private   RequestPermissionCallBack mRequestPermissionCallBack;
    private static final int PERMISSION_REQUEST_CODE = 1024;
    /**
     * 可拖动的打电话按钮控制器
     */
    protected DragViewCtr dragViewCtr;

    public boolean IsShowing() {
        return mIsShowing;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        context = this;
        ActivityControl.addActivity(this);
        AsyncImageLoader mAsyncImageLoader = AsyncImageLoader.getInstance();
        mAsyncImageLoader.setmListener(this);
        Log.e("BaseActivity", this.getClass().getName() + "--onCreate");
    }

    @Override
    protected void onDestroy() {
        ActivityControl.removeActivity(this);
        super.onDestroy();
    }
    protected String[] needPermissions = {
            Manifest.permission.CALL_PHONE,
            //            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            //            Manifest.permission.READ_EXTERNAL_STORAGE,
            //            Manifest.permission.READ_PHONE_STATE
    };
    /**
     * 显示打电话的按钮，必须在onResume方法里调用
     */
    public void addDragCallView() {
        if (dragViewCtr == null) {
            dragViewCtr = new DragViewCtr(this);
        }
        dragViewCtr.showDragCallView();


    }

    @Override
    protected void onResume() {
        addDragCallView();
        mIsShowing = true;
        AsyncImageLoader mAsyncImageLoader = AsyncImageLoader.getInstance();
        mAsyncImageLoader.setmListener(this);
        Resources res = getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());

        super.onResume();
        //		MobclickAgent.onPageStart(this.getClass().getName());
        //		MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (dragViewCtr != null) {
            dragViewCtr.hideDragCallView();
        }
        dragViewCtr = null;
        //		MobclickAgent.onPageEnd(this.getClass().getName());
        //		MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        mIsShowing = false;
        if (!ActivityControl.anyActivtyShowing()) {
            HandleBeforeGoToBackGround();
        }
        super.onStop();
    }

    @Override
    public void OnImgLoadFinished(String url, Bitmap mBitmap) {
        // TODO Auto-generated method stub

    }

    // @Override
    // public boolean onKeyDown(int keyCode, KeyEvent event) {
    //
    // if (keyCode == KeyEvent.KEYCODE_BACK) {
    // Log.e("info", "KYActivity");
    // ActivityControl.exit(this);
    // return true;
    // }
    // return super.onKeyDown(keyCode, event);
    // }

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
            int[] leftTop = {0, 0};
            // 获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    protected void registerBeforeGoToBackGround(BeforeGoToBackground listener) {
        if (!mBackDoList.contains(listener)) {
            mBackDoList.add(listener);
        }
    }

    protected void unRegisterBeforeGoToBackGround(BeforeGoToBackground listener) {
        mBackDoList.remove(listener);
    }

    public void doBeforeGoToBackground() {
    }

    private static void HandleBeforeGoToBackGround() {
        Log.e("info", "baseactivity____doBeforeGoToBackGround()>>>>>>>>>>>>>>>没有页面显示了");
        for (int i = 0; i < mBackDoList.size(); i++) {
            try {
                mBackDoList.get(i).doBeforeGoToBackground();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 权限请求结果回调
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean hasAllGranted = true;
        StringBuilder permissionName = new StringBuilder();
        for (String s : permissions) {
            permissionName = permissionName.append(s + "\r\n");
        }
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                for (int i = 0; i < grantResults.length; ++i) {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        hasAllGranted = false;
                        //在用户已经拒绝授权的情况下，如果shouldShowRequestPermissionRationale返回false则
                        // 可以推断出用户选择了“不在提示”选项，在这种情况下需要引导用户至设置页手动授权
                        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) {
                            new AlertDialog.Builder(BaseActivity.this).setTitle("权限申请")//设置对话框标题
                                    .setMessage("大乘智享获取相关权限失败:" + permissionName +
                                            "将导致部分功能无法正常使用，需要到设置页面手动授权")//设置显示的内容
                                    .setPositiveButton("去授权", new DialogInterface.OnClickListener() {//添加确定按钮
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                            //TODO Auto-generated method stub
                                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            Uri uri = Uri.fromParts("package", getApplicationContext().getPackageName(), null);
                                            intent.setData(uri);
                                            startActivity(intent);
                                            finish();
                                            dialog.dismiss();
                                        }
                                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
                                @Override
                                public void onClick(DialogInterface dialog, int which) {//响应事件
                                    // TODO Auto-generated method stub
                                    finish();
                                    System.exit(0);
                                    dialog.dismiss();
                                }

                            }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    mRequestPermissionCallBack.denied();
                                }
                            }).show();//在按键响应事件中显示此对话框
                        } else {
                            //用户拒绝权限请求，但未选中“不再提示”选项
                            mRequestPermissionCallBack.denied();
                        }
                        break;
                    }
                }
                if (hasAllGranted) {
                    mRequestPermissionCallBack.granted();
                }
            }
        }
    }

    /**
     * 发起权限请求
     * @param context
     * @param permissions
     * @param callback
     */
    public void requestPermissions(final Context context, final String[] permissions,
                                   RequestPermissionCallBack callback) {
        this.mRequestPermissionCallBack = callback;
        StringBuilder permissionNames = new StringBuilder();
        for (String s : permissions) {
            permissionNames = permissionNames.append(s + "\r\n");
        }
        //如果所有权限都已授权，则直接返回授权成功,只要有一项未授权，则发起权限请求
        boolean isAllGranted = true;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
                isAllGranted = false;
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission)) {
                    new AlertDialog.Builder(BaseActivity.this).setTitle("权限申请")//设置对话框标题
                            .setMessage("您好，大乘智享部分功能需要如下权限：" + permissionNames +
                                    " 请允许，否则将影响部分功能的正常使用。")//设置显示的内容
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                                @Override
                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                    //TODO Auto-generated method stub
                                    ActivityCompat.requestPermissions(((Activity) context), permissions, PERMISSION_REQUEST_CODE);
                                }
                            }).show();//在按键响应事件中显示此对话框
                } else {
                    ActivityCompat.requestPermissions(((Activity) context), permissions, PERMISSION_REQUEST_CODE);
                }
                break;
            }
        }
        if (isAllGranted) {
            mRequestPermissionCallBack.granted();
            return;
        }
    }

    /**
     * 权限请求结果回调接口
     */
    public interface RequestPermissionCallBack {
        /**
         * 同意授权
         */
        void granted();

        /**
         * 取消授权
         */
        void denied();
    }
}
