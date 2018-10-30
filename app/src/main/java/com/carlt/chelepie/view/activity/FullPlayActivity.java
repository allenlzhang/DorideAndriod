package com.carlt.chelepie.view.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carlt.chelepie.appsdk.AppsdkUtils;
import com.carlt.chelepie.control.DaoPieDownloadControl;
import com.carlt.chelepie.control.DeviceConnControl;
import com.carlt.chelepie.control.DeviceConnListener;
import com.carlt.chelepie.control.RecorderControl;
import com.carlt.chelepie.data.recorder.PieAttrInfo;
import com.carlt.chelepie.data.recorder.PieDownloadInfo;
import com.carlt.chelepie.data.recorder.PieInfo;
import com.carlt.chelepie.download.PieDownloadControl;
import com.carlt.chelepie.manager.DeviceConnectManager;
import com.carlt.chelepie.systemconfig.ActionConfig;
import com.carlt.chelepie.utils.PlayListener;
import com.carlt.chelepie.utils.PrefUtils;
import com.carlt.chelepie.utils.video.CodecMode;
import com.carlt.chelepie.view.PopShow;
import com.carlt.chelepie.view.UUDialogCapture;
import com.carlt.chelepie.view.gl.IVideoView;
import com.carlt.doride.DorideApplication;
import com.carlt.doride.R;
import com.carlt.doride.base.BaseActivity;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.preference.UseInfoLocal;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.ui.view.UUPopupWindow;
import com.carlt.doride.ui.view.UUToast;
import com.carlt.doride.utils.FileUtil;
import com.carlt.doride.utils.LocalConfig;
import com.carlt.doride.utils.StringUtils;
import com.carlt.sesame.utility.MyTimeUtil;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;

/**
 * 回放页面item点击后的全屏播放
 *
 * @author Administrator
 */
public class FullPlayActivity extends BaseActivity implements OnClickListener, DeviceConnListener {
    private TextView back;// 头部返回键

    private IVideoView mVideoView;

    private View mViewLoading;// 加载View

    private View mWaitLoading;// 等待加载Loading

    private String startTime = "1970-01-01 00:00:00";// 回放开始时间

    private TextView mTxtWifi;// 网速

    private ImageView mImgConfig;// 配置

    private ImageView mImgQuality;// 画质

    private ImageView mImgSilence;// 静音模式

    private ImageView mImgSetting;// 设置

    private ImageView mImgBack;// 回放

    private ImageView mImgPause;

    private ImageView mImgCapture;// 抓拍

    private ImageView mImgClip;// 剪裁

    private ImageView mImgDb;// 图库

    private String[] mNames = {"亮度", "饱和度", "对比度"};

    private int[] values = new int[3];

    private int quality = 0;// 画面质量

    private int type;

    private ArrayList<PieAttrInfo> mDataList;

    private PieInfo mPieInfo;

    private PopShow mPopShow;

    private View mImgLay;

    private ImageView mCapture;

    private View mFullBG;

    private ImageView mToPlay;

    DeviceConnControl mConnControl;

    private final static int TYPE_IMGCONFIG = 1001;// 图片亮度相关设置

    private final static int TYPE_QUALITY = 1002;// 画面质量

    private final static int TYPE_LINKED = 1003;// 拍照关联10s短视频

    UUDialogCapture mDialog = null;

    boolean pause = false;
    boolean isLastOver = true;

    public final static String Time = "time";

    public final static String TITLE = "title";

    /**
     * 全屏时头布局
     */
    private View mFullHead;

    /**
     * 全屏时底部布局
     */
    private View mFullBottom;


    private RelativeLayout mvideoLayout;

    /**
     * 重新加载背景
     */
    private View retryBg;
    /**
     * 重新加载按钮
     */
    private Button retryBtn;
    /**
     * 手势控制器
     */
    private GestureDetector mGestureDetector;

    /**
     * 设置全屏时的头布局和脚布局在显示7秒后隐藏的handler
     */
    private Handler bhHandler = new Handler();

    public static final String BROADCAST_ACTION = "com.example.loading";

    private BroadcastReceiver mBroadcastReceiver;

    /**
     * 设置全屏时的头布局和脚布局在显示7秒后隐藏的Runnable 对象
     */
    private Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            if (mFullHead.getVisibility() == View.VISIBLE) {
                hideBottomHead();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_fullplay);

        try {
            startTime = getIntent().getStringExtra(Time);
        } catch (Exception e) {
        }
//		startTime = "1970-01-01 00:00:00";// 回放开始时间
        mConnControl = new DeviceConnControl(this, this);
        init();
        initTitle();

        mPieInfo = PieInfo.getInstance();
        mDataList = new ArrayList<PieAttrInfo>();
        values[0] = mPieInfo.getBrightness();
        values[1] = mPieInfo.getSaturation();
        values[2] = mPieInfo.getConstrast();
        for (int i = 0; i < mNames.length; i++) {
            PieAttrInfo mInfo = new PieAttrInfo();
            mInfo.setName(mNames[i]);
            mInfo.setValue(values[i]);
            mDataList.add(mInfo);
        }

        connOk();
        DorideApplication.getInstanse().setPlayStringtime("");//清空上次播放的时间
        mWaitLoading.setVisibility(View.GONE);
        mGestureDetector = new GestureDetector(this,
                new BookOnGestureListener());
        bhHandler.postDelayed(myRunnable, 8000);
        Log.e("FullActivity", "FullPlayActivity" + "onCreat");
        mBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST_ACTION);
        registerReceiver(mBroadcastReceiver, intentFilter);
    }

    private void initTitle() {
        back = (TextView) findViewById(R.id.live_back_txt_back);
        back.setOnClickListener(this);
    }

    private void init() {
        mViewLoading = findViewById(R.id.fullplay_lay_loading);
        mWaitLoading = findViewById(R.id.play_progress_loading2);
        mvideoLayout = (RelativeLayout) findViewById(R.id.fullplay_videoview);
        mTxtWifi = (TextView) findViewById(R.id.live_back_txt_wifi);
        mImgConfig = (ImageView) findViewById(R.id.live_back_img_config);
        mImgQuality = (ImageView) findViewById(R.id.live_back_img_quality);
        mImgSilence = (ImageView) findViewById(R.id.live_back_img_silence);
        mImgSilence.setVisibility(View.VISIBLE);
        mImgSetting = (ImageView) findViewById(R.id.live_back_img_setting);
        mImgBack = (ImageView) findViewById(R.id.recorder_play_img_back);
        mImgBack.setVisibility(View.GONE);
        mImgPause = (ImageView) findViewById(R.id.recorder_play_img_pause);
        mImgPause.setVisibility(View.VISIBLE);
        mImgCapture = (ImageView) findViewById(R.id.recorder_play_img_capture);
        mImgClip = (ImageView) findViewById(R.id.recorder_play_img_clip);
        mImgDb = (ImageView) findViewById(R.id.recorder_play_img_db);

        mImgLay = findViewById(R.id.imgLay);
        mCapture = (ImageView) findViewById(R.id.recorder_img_capture);

        mToPlay = (ImageView) findViewById(R.id.readyToPlay);
        mFullBG = findViewById(R.id.fullplay_lay_img);
        mImgDb.setVisibility(View.GONE);

        mFullHead = findViewById(R.id.fullplay_head);
        mFullBottom = findViewById(R.id.fullplay_play_bottom);
        retryBg = findViewById(R.id.fullplay_retry_img);
        retryBtn = (Button) findViewById(R.id.bt_retry);

        mFullHead.setVisibility(View.VISIBLE);
        mFullBottom.setVisibility(View.VISIBLE);
        retryBg.setVisibility(View.INVISIBLE);
        mFullBG.setOnClickListener(this);
        mToPlay.setOnClickListener(this);
        mImgConfig.setOnClickListener(this);
        mImgQuality.setOnClickListener(this);
        mImgSilence.setOnClickListener(this);
        mImgSetting.setOnClickListener(this);

        mImgPause.setOnClickListener(this);
        mImgCapture.setOnClickListener(this);
        mImgClip.setOnClickListener(this);
        mImgDb.setOnClickListener(this);
        retryBtn.setOnClickListener(this);

        mImgCapture.setImageResource(R.drawable.player_cut_selector);
    }

    /**
     * 页面显示的时候检查下 设置项
     */
    public void checkPaiSetting() {

        SharedPreferences sharePref = PrefUtils.getPlayBack();
        boolean silence = sharePref.getBoolean(PrefUtils.PlayBack_Silence, false);
        if (silence) {
            mImgSilence.setImageResource(R.drawable.pie_silence_close);
        } else {
            mImgSilence.setImageResource(R.drawable.pie_silence_open);
        }
        mVideoView.setSilence(silence);

        if (mPieInfo == null) {
            return;
        }

        if (mPieInfo.getResolution() == 1) {
            mImgQuality.setImageResource(R.drawable.pie_hd);
        } else {
            mImgQuality.setImageResource(R.drawable.pie_fhd);
        }

    }

    /**
     * @author liuxiangfei
     * Description 手势的监听器
     */
    class BookOnGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent e) {

            if (mFullHead.getVisibility() == View.VISIBLE) {
                hideBottomHead();
            } else if (mFullHead.getVisibility() == View.GONE) {
                bhHandler.removeCallbacks(myRunnable);
                bhHandler.postDelayed(myRunnable, 8000);
                showBottomHead();
            }
            return true;
        }
    }

    private UUPopupWindow.OnDismissListener mOnDismissListener = new UUPopupWindow.OnDismissListener() {

        @Override
        public void onDismiss() {

            switch (type) {
                case TYPE_IMGCONFIG:

                    int brightness = mDataList.get(0).getValue();
                    int contrast = mDataList.get(2).getValue();
                    int saturation = mDataList.get(1).getValue();

                    int brightness_history = mPieInfo.getBrightness();
                    int contrast_history = mPieInfo.getConstrast();
                    int saturation_history = mPieInfo.getSaturation();

                    if (brightness == brightness_history && contrast == contrast_history && saturation == saturation_history) {
                    } else {
                        RecorderControl.setVideoColor(brightness, contrast, saturation, listener_imgconfig);
                    }
                    break;

                case TYPE_QUALITY:
                    int quality_history = mPieInfo.getResolution();
                    if (quality == quality_history) {
                    } else {
                        RecorderControl.setVideoSize(quality, listener_quality);
                    }
                    break;
                case TYPE_LINKED:
                    // 抓拍是否带视频
                    break;
            }
        }
    };

    private BaseParser.ResultCallback listener_imgconfig = new BaseParser.ResultCallback() {

        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            Message msg = new Message();
            msg.what = 7;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onError(BaseResponseInfo o) {
            Message msg = new Message();
            msg.what = 8;
            msg.obj = o;
            mHandler.sendMessage(msg);
        }

    };

    private BaseParser.ResultCallback listener_quality = new BaseParser.ResultCallback() {

        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            Message msg = new Message();
            msg.what = 9;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onError(BaseResponseInfo o) {
            Message msg = new Message();
            msg.what = 10;
            msg.obj = o;
            mHandler.sendMessage(msg);
        }
    };

    private BaseParser.ResultCallback listener_playback = new BaseParser.ResultCallback() {

        @Override
        public void onSuccess(BaseResponseInfo o) {
            Message msg = new Message();
            msg.what = 13;
            msg.obj = o;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onError(BaseResponseInfo o) {
            Message msg = new Message();
            msg.what = 14;
            msg.obj = o;
            mHandler.sendMessage(msg);
        }
    };

    private PopShow.OnPopshowCheckedChangedListener mCheckedChangedListener = new PopShow.OnPopshowCheckedChangedListener() {

        @Override
        public void onChecked(int imgQuality) {
            quality = imgQuality;
        }
    };

    int speedCount = 0;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mHandler.sendEmptyMessageDelayed(1, 1000);
                    String s = StringUtils.caculateSpeed();
                    mTxtWifi.setText(s);
//				//循环检查速度
//				long speed = StringUtils.caculateSpeedLong();
//				if( speed < (100 * 1024)){
//					speedCount ++;
//				}else if(speed > (300 * 1024)){
//					speedCount = 0;
//				}
//				if(speedCount > 20){
//					// 重新加载数据
//					reloadingShow();
//					speedCount = 0;
//				}
                    break;
                case 2:// 暂停成功
                    pause = true;
                    UUToast.showUUToast(FullPlayActivity.this, "暂停成功");
                    mImgPause.setImageResource(R.drawable.main_btn_play_selector);
                    break;
                case 3:// 暂停失败
                    UUToast.showUUToast(FullPlayActivity.this, "暂停失败");
                    break;
                case 4:// 继续成功
                    pause = false;
                    UUToast.showUUToast(FullPlayActivity.this, "继续成功");
                    mImgPause.setImageResource(R.drawable.pie_pause);
                    break;
                case 5:// 继续失败
                    UUToast.showUUToast(FullPlayActivity.this, "继续失败");
                    break;
                case 6:
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    break;
                case 7:
                    // 设置图片饱和度等信息成功
                    UUToast.showUUToast(FullPlayActivity.this, "设置图片信息成功！");
                    break;
                case 8:
                    // 设置图片饱和度等信息失败
                    UUToast.showUUToast(FullPlayActivity.this, "设置图片信息失败。。。");
                    break;
                case 9:
                    // 设置画面质量成功
                    UUToast.showUUToast(FullPlayActivity.this, "设置画面质量成功！");
                    if (mPieInfo.getResolution() == 1) {
                        mImgQuality.setImageResource(R.drawable.pie_hd);
                    } else {
                        mImgQuality.setImageResource(R.drawable.pie_fhd);
                    }
                    break;
                case 10:
                    // 设置画面质量失败
                    UUToast.showUUToast(FullPlayActivity.this, "设置画面质量失败。。。");
                    break;
                case 13:
                    UUToast.showUUToast(FullPlayActivity.this, "开启回放成功");
                    mVideoView.play();
                    break;
                case 14:
                    UUToast.showUUToast(FullPlayActivity.this, "开启回放失败");
                    mViewLoading.setVisibility(View.GONE);
                    showAllOthersView(false);
                    mFullBG.setVisibility(View.VISIBLE);
                    retryBg.setVisibility(View.GONE);
                    mWaitLoading.setVisibility(View.GONE);
                    DeviceConnectManager.Exit();
                    break;
                case 20:
                    mImgLay.setVisibility(View.GONE);
                    break;
                case 1014:
                    mViewLoading.setVisibility(View.GONE);
                    showAllOthersView(true);
                    UUToast.showUUToast(FullPlayActivity.this, "播放出现问题");
                    mFullBG.setVisibility(View.VISIBLE);
                    retryBg.setVisibility(View.GONE);
                    mWaitLoading.setVisibility(View.GONE);
                    break;
                case 1015:
                    mViewLoading.setVisibility(View.GONE);
                    showAllOthersView(true);
                    break;
                case 15:
                    Bitmap bit = (Bitmap) msg.obj;
                    if (bit != null) {
                        mHandler.removeMessages(20);
                        mImgLay.setVisibility(View.VISIBLE);
                        mCapture.setImageBitmap(bit);
                        String time = MyTimeUtil.getFormatTime(new Date());
                        String fileName = MyTimeUtil.getTimeFileName("H", time) + ".jpg";
                        String account = UseInfoLocal.getUseInfo().getAccount();
                        String deviceName = PieInfo.getInstance().getDeviceName();
                        String path = LocalConfig.GetMediaPath(account, deviceName, LocalConfig.DIR_CAPTURE) + fileName;
                        PieDownloadInfo mInfo = new PieDownloadInfo();
                        mInfo.setAccout(account);
                        mInfo.setDeviceName(deviceName);
                        mInfo.setType(PieDownloadInfo.TYPE_JPG);
                        mInfo.setStoreType(PieDownloadInfo.STORE_CAPTURE);
                        mInfo.setStatus(PieDownloadInfo.STATUS_FINISHED);
                        mInfo.setFileSrcName(fileName);
                        mInfo.setFileName(fileName);
                        mInfo.setStartTime(time);
                        mInfo.setEndTime(time);
                        mInfo.setCreateTime(time);
                        mInfo.setLocalPath(path);
                        int width = bit.getWidth();
                        if (width == 1080 || width == 1920) {
                            mInfo.setResolution(0);
                        } else {
                            mInfo.setResolution(1);
                        }
                        try {
                            bit.compress(CompressFormat.JPEG, 100, new FileOutputStream(path));
                            mInfo.setTotalLen((int) (FileUtil.getFileLength(path)));
                            DaoPieDownloadControl.getInstance().insert(mInfo);
                        } catch (FileNotFoundException e) {
                            Log.e("DEBUG", "保存图片失败" + e.getMessage());
                        }
                        mHandler.sendEmptyMessageDelayed(20, 1000 * 2);
                    } else {
                        UUToast.showUUToast(FullPlayActivity.this, "抓拍失败!");
                    }
                    break;
                case 16:
                    UUToast.showUUToast(FullPlayActivity.this, "抓拍失败!");
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.readyToPlay:
//			if(RuningConfig.ISCropRuning){
//				UUToast.showUUToast(FullPlayActivity.this, "正在剪裁，请等待剪裁完成");
//			}else{
                if (!DeviceConnectManager.isDeviceConnect()) {
                    mConnControl.goConnect();
                } else {
                    connOk();
                }
//			}
                break;
            case R.id.recorder_back_txt_back:
                finish();
                break;
            case R.id.live_back_txt_back:
                finish();
                break;
            case R.id.live_back_img_config:
                type = TYPE_IMGCONFIG;
                mPopShow = new PopShow(FullPlayActivity.this);
                mPopShow.SetOnDismissListener(mOnDismissListener);
                mPopShow.showList(mImgConfig, mDataList);
                break;
            case R.id.live_back_img_quality:
                // 画质
                type = TYPE_QUALITY;
                mPopShow = new PopShow(FullPlayActivity.this);
                mPopShow.SetOnDismissListener(mOnDismissListener);
                mPopShow.setmCheckedChangedListener(mCheckedChangedListener);
                mPopShow.showRg(mImgQuality, mPieInfo.getResolution());
                break;
            case R.id.live_back_img_silence:
                // 静音
                SharedPreferences sharePref = PrefUtils.getPlayBack();
                boolean silence = sharePref.getBoolean(PrefUtils.PlayBack_Silence, false);
                silence = !silence;
                sharePref.edit().putBoolean(PrefUtils.PlayBack_Silence, silence).apply();
                mVideoView.setSilence(silence);
                if (silence) {
                    mImgSilence.setImageResource(R.drawable.pie_silence_close);
                } else {
                    mImgSilence.setImageResource(R.drawable.pie_silence_open);
                }
                break;
            case R.id.live_back_img_setting:
                // 设置
                Intent mIntent1 = new Intent(FullPlayActivity.this, ManagePieActivity.class);
                startActivity(mIntent1);
                break;
            case R.id.recorder_play_img_pause:
                if (isLastOver) {
                    isLastOver = false;
                    if (pause) {
                        mVideoView.continuePlay();
                        RecorderControl.ContinuePlayBack(continueCallback);
                    } else {
                        mVideoView.pause(pauseCallback);
                    }
                } else {
                    UUToast.showUUToast(FullPlayActivity.this, "请等待上次操作完成");
                }
                break;
            case R.id.recorder_play_img_capture:
                // 抓拍
                mVideoView.getCapture(new CodecMode.ICaptureListener() {

                    @Override
                    public void CaptureOK(Bitmap bit) {

                        Message msg = new Message();
                        msg.what = 15;
                        msg.obj = bit;
                        mHandler.sendMessage(msg);
                    }

                    @Override
                    public void CaptureERR() {
                        Message msg = new Message();
                        msg.what = 16;
                        mHandler.sendMessage(msg);

                    }
                });

                break;
            case R.id.recorder_play_img_clip:
                // 剪裁
                Intent mIntent3 = new Intent(FullPlayActivity.this, PlaybackActivity.class);
                mIntent3.putExtra("is_cut", true);
                mIntent3.putExtra("startTime", startTime);
                startActivity(mIntent3);
                finish();
                break;
            case R.id.recorder_play_img_db:
//			// 跳转到车乐拍媒体库
//			Intent mIntent4 = new Intent(FullPlayActivity.this, MediaDBActivity.class);
//			startActivity(mIntent4);
                break;
            case R.id.bt_retry:
                // 重新建立连接并播放
                retryBg.setVisibility(View.GONE);
                mWaitLoading.setVisibility(View.GONE);
                Log.e("FullActivity", startTime);
                connOk();
                DorideApplication.getInstanse().setPlayStringtime("");//清空上次播放的时间
                mHandler.sendEmptyMessageDelayed(1, 1000);
                break;
        }
    }
//       DorideApplication.getInstanse().setToFullFlag(false);
//        AppsdkUtils.CMStop(ActionConfig.getSeqNum());
//        mVideoView.stop();
//        videoLayout.removeAllViews();
//        mConnControl.onPause();
//    loading(false);
//        mFullBG.setVisibility(View.VISIBLE);
//        mHandler.removeMessages(1);
//        Log.e("FullActivity", "FullLiveActivity" + "onPause");
    @Override
    protected void onPause() {
        super.onPause();
        AppsdkUtils.CPStop(ActionConfig.getSeqNum());
        mVideoView.stop();
        mvideoLayout.removeAllViews();
        mConnControl.onPause();
        mViewLoading.setVisibility(View.GONE);
        showAllOthersView(true);
        mFullBG.setVisibility(View.VISIBLE);
        retryBg.setVisibility(View.GONE);
        mWaitLoading.setVisibility(View.GONE);
        mHandler.removeMessages(1);
        Log.e("FullActivity", "FullPlayActivity" + "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        mConnControl.onStop();
        mFullBG.setVisibility(View.VISIBLE);
        retryBg.setVisibility(View.GONE);
        mWaitLoading.setVisibility(View.GONE);
        Log.e("FullActivity", "FullPlayActivity" + "onStop");
    }

    @Override
    protected void onDestroy() {
        mConnControl.onDestory();
        PieDownloadControl.media_ResumeMediaDownloading();
        super.onDestroy();
        Log.e("FullActivity", "FullPlayActivity" + "onDestroy");
        unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mvideoLayout.removeAllViews();
        DorideApplication.getInstanse().setMonitor(false);
        mVideoView = DorideApplication.getInstanse().getVideoView();
        mvideoLayout.addView((View) mVideoView, new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mVideoView.setIsMonitor(false);
        ((View) mVideoView).setKeepScreenOn(true);
        mVideoView.setmListener(mPlayListener);
        mConnControl.onResume();
        checkPaiSetting();
        mHandler.sendEmptyMessage(1);
        Log.e("FullActivity", "FullPlayActivity" + "onResume");
    }

    BaseParser.ResultCallback pauseCallback = new BaseParser.ResultCallback() {

        @Override
        public void onSuccess(BaseResponseInfo o) {
            Message msg = new Message();
            msg.what = 2;
            msg.obj = o;
            mHandler.sendMessage(msg);
            isLastOver = true;
        }

        @Override
        public void onError(BaseResponseInfo o) {
            Message msg = new Message();
            msg.what = 3;
            msg.obj = o;
            mHandler.sendMessage(msg);
            isLastOver = true;
        }

    };

    BaseParser.ResultCallback continueCallback = new BaseParser.ResultCallback() {

        @Override
        public void onSuccess(BaseResponseInfo o) {
            Message msg = new Message();
            msg.what = 4;
            msg.obj = o;
            mHandler.sendMessage(msg);
            isLastOver = true;
        }

        @Override
        public void onError(BaseResponseInfo o) {
            Message msg = new Message();
            msg.what = 5;
            msg.obj = o;
            mHandler.sendMessage(msg);
            isLastOver = true;
        }
    };

    @Override
    public void connOk() {
        mFullBG.setVisibility(View.GONE);
        PieDownloadControl.media_PauseMediaDownloading();
        RecorderControl.startPlayback(listener_playback, startTime);
        mViewLoading.setVisibility(View.VISIBLE);
        showAllOthersView(false);

    }

    @Override
    public void connError() {
        mFullBG.setVisibility(View.VISIBLE);
        retryBg.setVisibility(View.GONE);
        mWaitLoading.setVisibility(View.GONE);
        mViewLoading.setVisibility(View.GONE);
    }

    PlayListener mPlayListener = new PlayListener() {

        @Override
        public void onError() {
            // --播放期间出现问题
            mHandler.sendEmptyMessage(1014);
        }

        @Override
        public void onReady() {
            // ---已经准备好了
            mHandler.sendEmptyMessage(1015);
        }
    };

    /**
     * 隐藏底部和顶部
     */
    private void showBottomHead() {
        mFullHead.setVisibility(View.VISIBLE);
        mFullBottom.setVisibility(View.VISIBLE);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(500);
        mFullHead.startAnimation(alphaAnimation);
        mFullBottom.startAnimation(alphaAnimation);
    }

    /**
     * 隐藏底部和顶部
     */
    private void hideBottomHead() {
        mFullHead.setVisibility(View.GONE);
        mFullBottom.setVisibility(View.GONE);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(500);
        mFullHead.startAnimation(alphaAnimation);
        mFullBottom.startAnimation(alphaAnimation);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    /**
     * 重新加载数据
     */
    private void reloadingShow() {
        Log.e("playtime", "reloadingShow");
        if (mFullBG.getVisibility() != View.VISIBLE) {
            retryBg.setVisibility(View.VISIBLE);
            mWaitLoading.setVisibility(View.GONE);
            showAllOthersView(false);
        }
        mViewLoading.setVisibility(View.GONE);
        mHandler.removeMessages(1);
        if (!TextUtils.isEmpty(mVideoView.getLastPlayTime())) {
            startTime = mVideoView.getLastPlayTime();
        }
        AppsdkUtils.CPStop(ActionConfig.getSeqNum());
    }

    /**
     * 回放重新加载按钮显示的广播
     *
     * @author liu
     */
    public class MyBroadcastReceiver extends BroadcastReceiver {
        public final String TAG = "MyBroadcastReceiver";

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.w(TAG, "intent:" + intent);
            boolean mIscecheOK = intent.getBooleanExtra("mIscecheOK", true);
            if (!mIscecheOK) {
                if (mViewLoading.getVisibility() != View.VISIBLE && mFullBG.getVisibility() != View.VISIBLE && retryBg.getVisibility() != View.VISIBLE) {
                    mWaitLoading.setVisibility(View.VISIBLE);
                    showAllOthersView(false);
                }
            } else {
                mWaitLoading.setVisibility(View.GONE);
                showAllOthersView(true);
            }
            if (1 == intent.getIntExtra("needReload", 0)) {
//	        	reloadingShow();
            }
        }
    }

    /**
     * true 显示，  false 不显示
     *
     * @param flag
     */
    private void showAllOthersView(boolean flag) {
        if (flag) {
            mImgConfig.setVisibility(View.VISIBLE);
            mImgQuality.setVisibility(View.VISIBLE);
            mImgSilence.setVisibility(View.VISIBLE);
            mImgSetting.setVisibility(View.VISIBLE);
            mImgPause.setVisibility(View.VISIBLE);
            mImgCapture.setVisibility(View.VISIBLE);
            mImgClip.setVisibility(View.VISIBLE);
        } else {
            mImgConfig.setVisibility(View.GONE);
            mImgQuality.setVisibility(View.GONE);
            mImgSilence.setVisibility(View.GONE);
            mImgSetting.setVisibility(View.GONE);
            mImgPause.setVisibility(View.GONE);
            mImgCapture.setVisibility(View.GONE);
            mImgClip.setVisibility(View.GONE);
        }

    }
}
