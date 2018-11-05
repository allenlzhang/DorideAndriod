package com.carlt.chelepie.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.widget.VideoView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.carlt.sesame.utility.MyTimeUtil;
import com.orhanobut.logger.Logger;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.carlt.chelepie.utils.CodecPlayerUtil.TYPE_CUT_SUCCSE;
import static com.carlt.chelepie.utils.CodecPlayerUtil.TYPE_END;
import static com.carlt.chelepie.utils.CodecPlayerUtil.TYPE_PAUSE;
import static com.carlt.chelepie.utils.CodecPlayerUtil.TYPE_PROCESS;
import static com.carlt.chelepie.utils.CodecPlayerUtil.TYPE_START_PLAY;
import static com.carlt.chelepie.utils.CodecPlayerUtil.TYPE_TIME_STRING;

public class MyVideoView extends VideoView implements MediaPlayer.OnCompletionListener,MediaPlayer.OnPreparedListener,MediaPlayer.OnErrorListener  {

    String filePath ;
    /**
     * 用于获取,指定视屏文件,指定时间的Bitmap
     */
    MediaMetadataRetriever mmr = new MediaMetadataRetriever();

    /**
     * 播放总时间
     */
    private String timeTotal ;
    /**
     * 总时间
     */
    private int totalTime ;

    private Handler mHandler;
    private boolean isCommpeleted =false ;

    private int stopPosition  ;
    private Disposable disposable ;

    public MyVideoView(Context context, String filePath) {
        super(context);
        this.filePath = filePath;
        init();
    }

    @SuppressLint("CheckResult")
    public void setmHandler(Handler mHandler) {
        this.mHandler = mHandler;
        mHandler.sendEmptyMessage(TYPE_START_PLAY);

        disposable =   Observable.interval(400, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        int cuurent = getCurrentPosition();

                        reflshTime(timeTotal, cuurent);
                        reflshProgress(getDuration(),cuurent);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
        start();
    }

    @SuppressLint("CheckResult")
    private void init() {
        this.setVideoPath(filePath);
        mmr.setDataSource(filePath);
        setOnCompletionListener(this);
        setOnPreparedListener(this);

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        ToastUtils.showShort("播放失败");
        return false;
    }


    @Override
    public void onPrepared(MediaPlayer mp) {
        timeTotal = MyTimeUtil.formartTime1(getDuration() )+ "";
        totalTime = getDuration();
        Logger.e("onPrepared=================" + stopPosition);
        if(stopPosition > 0){
            seekTo(stopPosition);
            this.onPause();
        }

    }

    @Override
    public void onCompletion(MediaPlayer mp) {

        Message  end= new Message();
        end.what = TYPE_END;
        mHandler.sendMessage(end);

        isCommpeleted = true ;
        stopPosition = 0;
        reflshProgress(getDuration(),getDuration());

    }

    public void onPause(){
        mHandler.sendEmptyMessage(TYPE_PAUSE);
        this.pause();
        if(isCommpeleted){
            stopPosition = 0 ;
        }
        Logger.e("onPause" + stopPosition);

    }

    public void onStop(){
        mHandler.sendEmptyMessage(TYPE_PAUSE);
        this.pause();

    }

    public void continuePaly(){
        mHandler.sendEmptyMessage(TYPE_START_PLAY);
        isCommpeleted = false ;
        this.start();

    }

    //截屏
    public void cropBitmap() {

        if(mmr != null){
            Bitmap   bitmap =  mmr.getFrameAtTime(getCurrentPosition());
            Message msg = new Message();
            msg.what = TYPE_CUT_SUCCSE;
            msg.obj = bitmap;
            mHandler.sendMessage(msg);
        }

    }

    /** 刷新时间进度
     * @param timeAll
     * @param nextFrameStart
     */
    private void reflshTime(String timeAll, int nextFrameStart) {
        String timePlay = MyTimeUtil.formartTime1(nextFrameStart );
        if(nextFrameStart > 0){
            stopPosition = nextFrameStart ;
            Message msg = new Message();
            msg.what = TYPE_TIME_STRING;
            msg.obj = timePlay + "/" + timeAll;
            mHandler.sendMessage(msg);
        }

    }

    /** 刷新 进度条
     * @param totalLenth
     * @param nextFrameStart
     */
    private void reflshProgress(int totalLenth, int nextFrameStart) {
        int i1 = (int) (((float)(nextFrameStart) / (float)(totalLenth)) * 100);
        if(i1 > 0){
            Message msg2 = new Message();
            msg2.what = TYPE_PROCESS;
            msg2.obj = i1;
            mHandler.sendMessage(msg2);
        }
    }

    public void playSeekTo(int progress){
        int seekPosition = totalTime * progress /100 ;
        seekTo(seekPosition);
        //continuePaly();
    }
    public void onDestroy (){
        this.pause();
        mmr.release();
        if(disposable !=null){
            disposable.dispose();
        }
    }
}
