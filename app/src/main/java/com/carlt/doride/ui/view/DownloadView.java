
package com.carlt.doride.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.doride.control.ActivityControl;
import com.carlt.doride.http.FileDownloadThread;
import com.carlt.doride.http.FileDownloadThread.OnDownloadListner;
import com.carlt.doride.utils.FileUtil;
import com.carlt.doride.utils.LocalConfig;

import java.io.File;

public class DownloadView {
    private LayoutInflater inflater;

    private UUPopupWindow menuPop;

    private View menuView_main;

    private View menuView_bg;

    private Animation ani1;

    private Animation ani2;

    private FileDownloadThread mDownloadThread;

    private Thread mThread;

    private Context mContext;

    public DownloadView(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        menuView_main = inflater.inflate(R.layout.dialog_download, null);
        menuPop = new UUPopupWindow(menuView_main, LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT);
        menuView_bg = menuView_main.findViewById(R.id.dialog_download_bg);

        menuView_bg.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
            }
        });
        initAnimation(mContext);
        menuView_main.setFocusableInTouchMode(true);
        menuView_main.setOnKeyListener(new OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK && menuPop.isShowing()
                        && event.getAction() == KeyEvent.ACTION_UP) {
                    mDownloadThread.setStop(true);
                    ActivityControl.onExit();
                }
                return true;
            }
        });
        menuPop.setBackgroundDrawable(new BitmapDrawable());
        menuPop.initAni(menuView_main, ani2);
        menuPop.setFocusable(true);
        menuPop.update();
        menuPop.setOutsideTouchable(false);

    }

    private void initAnimation(Context mContext) {
        ani1 = AnimationUtils.loadAnimation(mContext, R.anim.enter_menu_personevaluation);
        ani2 = AnimationUtils.loadAnimation(mContext, R.anim.exit_menu_personevaluation);
    }

    private TextView mTxtContent;

    private TextView mTxtBtn1;

    private TextView mTxtBtn2;

    private ProgressBar mProgressBar;

    public void showView(String apkUrl) {
        mTxtContent = (TextView) menuView_main.findViewById(R.id.dialog_download_content);

        mProgressBar = (ProgressBar) menuView_main.findViewById(R.id.dialog_download_progress);
        mTxtBtn1 = (TextView) menuView_main.findViewById(R.id.dialog_download_btn1);
        mTxtBtn2 = (TextView) menuView_main.findViewById(R.id.dialog_download_btn2);

        mTxtBtn1.setOnClickListener(mClickListener);
        mTxtBtn2.setOnClickListener(mClickListener);

        File mFile = null;
        int tmp = apkUrl.lastIndexOf("/");
        if (tmp >= 0) {
            String apkFileURL_sdcard = LocalConfig.mDownLoadFileSavePath_SD
                    + apkUrl.substring(tmp + 1);

            String apkFileURL_internal = LocalConfig.mDownLoadFileSavePath_Absolute
                    + apkUrl.substring(tmp + 1);

            if (FileUtil.isExist(apkFileURL_sdcard)) {
                mFile = new File(apkFileURL_sdcard);
            } else if (FileUtil.isExist(apkFileURL_internal)) {
                mFile = new File(apkFileURL_sdcard);
            }
        }

        if (mFile != null && mFile.length() > 0) {
            // 本地已存在完整文件
            Uri uri = Uri.fromFile(mFile);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(mContext, "com.carlt.doride.fileprovider", mFile);
                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            } else {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 启动新的activity
                intent.setDataAndType(uri, "application/vnd.android.package-archive");
            }
            mContext.startActivity(intent);
            ActivityControl.onExit();

        } else {
            // 本地不存在完整文件
            mDownloadThread = new FileDownloadThread(apkUrl, mDownloadListner);
            mDownloadThread.setStop(false);
            mThread = new Thread(mDownloadThread);
            mThread.start();

            menuPop.showAtLocation(menuView_main, 0, 0, 0);
            menuView_main.startAnimation(ani1);
        }

    }

    private OnDownloadListner mDownloadListner = new OnDownloadListner() {

        @Override
        public void onStarted(int defaultPro) {
            Message msg = new Message();
            msg.what = 0;
            msg.obj = defaultPro;
            mHandler.sendMessage(msg);

        }

        @Override
        public void onProgress(int progress) {
            Message msg = new Message();
            msg.what = 1;
            msg.obj = progress;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onFinished(File file) {
            Message msg = new Message();
            msg.what = 2;
            msg.obj = file;
            mHandler.sendMessage(msg);

        }

        @Override
        public void onFailed(String error) {
            Message msg = new Message();
            msg.what = 3;
            msg.obj = error;
            mHandler.sendMessage(msg);

        }
    };

    private OnClickListener mClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.dialog_download_btn1:
                    // 暂停or下载
                    boolean isStop = mDownloadThread.isStop();
                    if (isStop) {
                        mDownloadThread.setStop(false);
                        mThread = new Thread(mDownloadThread);
                        mThread.start();
                        mTxtBtn1.setText("暂停");
                    } else {
                        mDownloadThread.setStop(true);
                        mTxtBtn1.setText("继续");
                    }
                    break;

                case R.id.dialog_download_btn2:
                    // 退出
                    mDownloadThread.setStop(true);
                    ActivityControl.onExit();
                    break;
            }
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    // 开始下载
                    int defaultPro = (Integer) msg.obj;
                    mProgressBar.setProgress(defaultPro);
                    break;

                case 1:
                    // 更新下载进度
                    int progress = (Integer) msg.obj;
                    mProgressBar.setProgress(progress);
                    mTxtContent.setText("已下载：" + progress + "%");
                    break;
                case 2:
                    // 下载完成
                    File mFile = (File) msg.obj;
                    Uri uri = Uri.fromFile(mFile);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 启动新的activity
                    intent.setDataAndType(uri, "application/vnd.android.package-archive");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        Uri contentUri = FileProvider.getUriForFile(mContext, "com.carlt.doride.fileprovider", mFile);
                        intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                    } else {
                        intent.setDataAndType(Uri.fromFile(mFile), "application/vnd.android.package-archive");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    }
                    mContext.startActivity(intent);
                    //                    ActivityControl.onExit();
                    ActivityControl.clearAllActivity();
                    break;
                case 3:
                    // 下载失败
                    String error = (String) msg.obj;
                    mTxtContent.setText(error);
                    mTxtBtn1.setText("升级");
                    break;
            }
        }

    };

    public void dissmiss() {
        menuPop.dismiss();
    }
}
