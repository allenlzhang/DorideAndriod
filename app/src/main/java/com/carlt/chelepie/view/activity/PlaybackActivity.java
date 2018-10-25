package com.carlt.chelepie.view.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.chelepie.control.DeviceConnControl;
import com.carlt.chelepie.control.DeviceConnListener;
import com.carlt.chelepie.control.RecorderControl;
import com.carlt.chelepie.data.recorder.PieDownloadInfo;
import com.carlt.chelepie.download.PieDownloadControl;
import com.carlt.chelepie.manager.DeviceConnectManager;
import com.carlt.chelepie.manager.ThumbnailManager;
import com.carlt.chelepie.protocolstack.recorder.RecorderDownloadVideoThumbnailParser;
import com.carlt.chelepie.view.CropDialog;
import com.carlt.chelepie.view.ThumbnailView;
import com.carlt.doride.R;
import com.carlt.doride.base.BaseActivity;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.systemconfig.RuningConfig;
import com.carlt.doride.ui.view.PopBoxCreat;
import com.carlt.doride.ui.view.UUToast;
import com.carlt.sesame.utility.MyTimeUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 时间：2015-8-3
 * 描述：回放选择界面
 */
public class PlaybackActivity extends BaseActivity implements OnClickListener, DeviceConnListener {
    private TextView back;// 头部返回键

    private TextView          textview_Day;// 日期
    private ImageView         playback_nextday;// 后一天
    private ImageView         playback_preday;// 前一天
    private ImageView         img_handle;// 处理按钮
    private ThumbnailView     playback_thumbnailview;
    private TextView          mTxtDownlaoding;
    private ImageView         mImgRight;
    private DeviceConnControl mConnControl;

    private Calendar         currentCalendar = Calendar.getInstance();
    private SimpleDateFormat format          = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
    private CropDialog mCDialog;
    private TextView   tvTitle;
    private boolean isCut      = false;
    private boolean isfromLive = false;//标记是否来自直播页面
    private ImageView pieCutBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_playback);
        mConnControl = new DeviceConnControl(this, this);
        init();
        initTitle();
        isCut = getIntent().getBooleanExtra("is_cut", false);
        isfromLive = getIntent().getBooleanExtra("isfromLive", false);
        playback_thumbnailview.setIsCut(isCut);
        //TODO 根据时间设置ThumbnailView 的进度
        String startTime = getIntent().getStringExtra("startTime");
        tvTitle.setText("回放");
        if (isCut) {
            img_handle.setImageResource(R.drawable.player_download);
            img_handle.setTag(R.drawable.player_download);
            tvTitle.setText("剪裁");
            playback_thumbnailview.changeCutMode();
        }
        playback_thumbnailview.loadData(ThumbnailManager.mDatas);
        playback_thumbnailview.setPickedTime(startTime);
        PieDownloadControl.crop_AddCropListener(progressCallback);
        downloadThumbnail();
    }


    /**
     * 下载缩略图图片
     */
    private RecorderDownloadVideoThumbnailParser mMediaThumbnailDownloader;

    /**
     * 下载缩略图
     */
    private void downloadThumbnail() {
        final BaseParser.ResultCallback thumbnailDownloadCallback = new BaseParser.ResultCallback() {

            @Override
            public void onSuccess(BaseResponseInfo o) {
                Message msg = new Message();
                msg.what = 4;
                msg.obj = o;
                mHAnHandler.sendMessage(msg);
            }

            @Override
            public void onError(BaseResponseInfo o) {
                //				Message msg = new Message();
                //				msg.what = 5;
                //				msg.obj = o;
                //				mHAnHandler.sendMessage(msg);
            }
        };
        Thread thumbnailThread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<PieDownloadInfo> lists = ThumbnailManager.mDatas;
                if (null == mMediaThumbnailDownloader) {
                    mMediaThumbnailDownloader = new RecorderDownloadVideoThumbnailParser(
                            thumbnailDownloadCallback, lists);
                    mMediaThumbnailDownloader.run();
                }
            }
        });
        thumbnailThread.start();

    }

    private void initTitle() {
        back = (TextView) findViewById(R.id.recorder_back_txt_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        mImgRight = (ImageView) findViewById(R.id.imgRight);
        tvTitle.setVisibility(View.VISIBLE);
        mImgRight.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!DeviceConnectManager.isDeviceConnect()) {
                    mConnControl.goConnect();
                } else {
                    UUToast.showUUToast(PlaybackActivity.this, "设备已连接");
                }
            }
        });

        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();//不是剪裁页面变成回放页面
            }
        });
        pieCutBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                int tag = (Integer) img_handle.getTag();
                if (tag == R.drawable.player_download) {
                    img_handle.setImageResource(R.drawable.pie_clip);
                    img_handle.setTag(R.drawable.pie_clip);
                }
                tvTitle.setText("回放");
                pieCutBack.setVisibility(View.GONE);
                playback_thumbnailview.changePlayMode();
            }
        });
    }

    private void init() {
        playback_nextday = (ImageView) findViewById(R.id.playback_nextday);
        playback_preday = (ImageView) findViewById(R.id.playback_preday);
        img_handle = (ImageView) findViewById(R.id.playback_img_handle);
        pieCutBack = (ImageView) findViewById(R.id.pie_cut_back);
        img_handle.setTag(R.drawable.pie_clip);
        textview_Day = (TextView) findViewById(R.id.playback_textday);
        mTxtDownlaoding = (TextView) findViewById(R.id.playback_txt_downloading);
        playback_thumbnailview = (ThumbnailView) findViewById(R.id.playback_thumbnailview);
        playback_nextday.setOnClickListener(this);
        playback_preday.setOnClickListener(this);
        img_handle.setOnClickListener(this);
        playback_thumbnailview.setPlayViewShow(true);
    }

    // 下一日
    public void nextDay() {
        currentCalendar.add(Calendar.DATE, 1);
    }

    // 上一日
    public void preDay() {
        currentCalendar.add(Calendar.DATE, -1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.playback_preday:
                preDay();
                break;
            case R.id.playback_nextday:
                nextDay();
                break;
            case R.id.playback_img_handle:
                tvTitle.setText("剪裁");
                if (isfromLive) {
                    pieCutBack.setVisibility(View.VISIBLE);
                }
                playback_thumbnailview.setPlayViewShow(false);
                double max = playback_thumbnailview.getMax();
                double left = max / 2 - playback_thumbnailview.getProgressLow();
                double right = playback_thumbnailview.getProgressHigh() - max / 2;
                String time = playback_thumbnailview.getTime();
                Date date = null;
                if (time == null || time.length() < 1 || (date = MyTimeUtil.parseDate(time)) == null) {
                    UUToast.showUUToast(PlaybackActivity.this, "时间有问题");
                    return;
                }

                // 总共5分钟 左边 两分半 ， 右边 两分半
                int leftSecond = (int) (left / max * 2 * 2.5 * 60);// Second 秒
                int rightSecond = (int) (right / max * 2 * 2.5 * 60);// Second 秒
                Calendar cs = Calendar.getInstance();
                cs.setTime(date);
                cs.add(Calendar.SECOND, -leftSecond);

                Calendar ce = Calendar.getInstance();
                ce.setTime(date);
                ce.add(Calendar.SECOND, rightSecond);
                final String startTime = MyTimeUtil.getFormatTime(cs.getTime());
                final String endTime = MyTimeUtil.getFormatTime(ce.getTime());

                // 确定
                int tag = (Integer) img_handle.getTag();
                if (tag == R.drawable.pie_clip) {
                    if (RuningConfig.ISCropRuning) {
                        UUToast.showUUToast(PlaybackActivity.this, "请等待本次内容下载完毕");
                    } else {
                        img_handle.setImageResource(R.drawable.player_download);
                        img_handle.setTag(R.drawable.player_download);
                        playback_thumbnailview.changeCutMode();
                    }
                } else {
                    if (RuningConfig.ISCropRuning) {
                        UUToast.showUUToast(PlaybackActivity.this, "请等待本次内容下载完毕");
                        return;
                    }
                    PopBoxCreat.DialogWithTitleClick click = new PopBoxCreat.DialogWithTitleClick() {

                        @Override
                        public void onRightClick() {
                            // 取消
                        }

                        @Override
                        public void onLeftClick() {
                            // 确定
                            img_handle.setImageResource(R.drawable.pie_clip);
                            img_handle.setTag(R.drawable.pie_clip);
                            Log.e("PlaybackActivity", "MAX : " + playback_thumbnailview.getMax() + "|MIN:" + playback_thumbnailview.getMin() + "|LOW:"
                                    + playback_thumbnailview.getProgressLow() + "|HEIGH:" + playback_thumbnailview.getProgressHigh());
                            showCropDialog();
                            PieDownloadControl.crop_DownloadCrop(startTime, endTime);
                            playback_thumbnailview.changeCutMode();
                        }
                    };
                    StringBuilder sMsg = new StringBuilder("你选择了");
                    sMsg.append(startTime).append("--").append(endTime).append("的视频，确定下载吗？");
                    PopBoxCreat.createDialogWithTitle(this, "提示", sMsg.toString(), "", "确定", "取消", click);
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mConnControl.onResume();
        if (null != mMediaThumbnailDownloader) {
            mMediaThumbnailDownloader.setContinue(true);
        }
        // if (RuningConfig.ISCropRuning) {
        // mTxtDownlaoding.setVisibility(View.VISIBLE);
        // } else {
        // mTxtDownlaoding.setVisibility(View.GONE);
        // }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mConnControl.onDestory();
        PieDownloadControl.crop_RemoveCropListener(progressCallback);
        if (null != mMediaThumbnailDownloader) {
            mMediaThumbnailDownloader.stopDownLoadVideoThumbnail();
        }
    }

    @Override
    public void OnImgLoadFinished(String url, Bitmap mBitmap) {
        playback_thumbnailview.ImgLoadFinished(url, mBitmap);
    }

    public void showCropDialog() {
        dismissCropDialog();
        mCDialog = new CropDialog(this);
        mCDialog.setOnClickListener(mDialogClick);
        mCDialog.show();
    }

    public void dismissCropDialog() {
        if (mCDialog != null) {
            mCDialog.dismiss();
        }
    }

    OnClickListener mDialogClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // 删除
            PopBoxCreat.DialogWithTitleClick click = new PopBoxCreat.DialogWithTitleClick() {

                @Override
                public void onRightClick() {
                    // 取消

                }

                @Override
                public void onLeftClick() {
                    // 确定
                    dismissCropDialog();
                    PieDownloadControl.crop_StopCrop();
                }
            };
            PopBoxCreat.createDialogWithTitle(PlaybackActivity.this, "提示", "确定要停止剪裁吗？", "", "确定", "取消", click);
        }
    };

    RecorderControl.GetTranslateProgressCallback progressCallback = new RecorderControl.GetTranslateProgressCallback() {

        @Override
        public void onUpdateProgress(int progress) {
            Message msg = new Message();
            msg.what = 12;
            msg.obj = progress;
            mHAnHandler.sendMessage(msg);
        }

        @Override
        public void onTranslateProgress(Object progress) {

        }

        @Override
        public void onFinished(Object o1) {
            Message msg = new Message();
            msg.what = 2;
            msg.obj = o1;
            mHAnHandler.sendMessage(msg);
        }

        @Override
        public void onErro(Object o) {
            Message msg = new Message();
            msg.what = 3;
            msg.obj = o;
            mHAnHandler.sendMessage(msg);
        }
    };

    private Handler mHAnHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2:
                    dismissCropDialog();
                    // 车乐拍剪裁成功
                    // BaseResponseInfo bInfo = (BaseResponseInfo) msg.obj;
                    UUToast.showUUToast(PlaybackActivity.this, "剪裁完成");
                    // if (mTxtDownlaoding.getVisibility() == View.VISIBLE) {
                    // mTxtDownlaoding.setVisibility(View.GONE);
                    // }
                    break;

                case 3:
                    dismissCropDialog();
                    // 车乐拍剪裁失败
                    UUToast.showUUToast(PlaybackActivity.this, "视频剪裁失败");
                    break;
                case 4:
                    // 缓冲图片成功(有了新的图片后刷新页面)
                    BaseResponseInfo info = (BaseResponseInfo) msg.obj;
                    if (!RecorderDownloadVideoThumbnailParser.ISEXIST.equals(info.getInfo())) {
                        playback_thumbnailview.notifydata();
                    }
                    break;
                case 12:
                    //下载进度
                    try {
                        int len = (Integer) msg.obj;
                        StringBuilder download = new StringBuilder();
                        download.append(len).append("%");
                        //				UUToast.showUUToast(PlaybackActivity.this, "已下载："+download.toString());
                        mCDialog.setTextProcess(download.toString());
                    } catch (Exception e) {
                        //do nothing
                    }
                    break;
            }

        }
    };

    @Override
    public void connOk() {
        playback_thumbnailview.loadData(ThumbnailManager.mDatas);
    }

    @Override
    public void connError() {
    }

    @Override
    protected void onStop() {
        super.onStop();
        PieDownloadControl.crop_StopCrop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != mMediaThumbnailDownloader) {
            mMediaThumbnailDownloader.setContinue(false);
        }
    }

}
