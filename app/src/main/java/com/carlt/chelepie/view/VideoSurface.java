package com.carlt.chelepie.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.carlt.chelepie.appsdk.CapBufferData;
import com.carlt.sesame.utility.MyTimeUtil;
import com.hz17car.chelepie.utility.AVIConverter;
import com.orhanobut.logger.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static com.carlt.chelepie.utils.CodecPlayerUtil.TYPE_CUT_SUCCSE;
import static com.carlt.chelepie.utils.CodecPlayerUtil.TYPE_END;
import static com.carlt.chelepie.utils.CodecPlayerUtil.TYPE_PAUSE;
import static com.carlt.chelepie.utils.CodecPlayerUtil.TYPE_PROCESS;
import static com.carlt.chelepie.utils.CodecPlayerUtil.TYPE_START_PLAY;
import static com.carlt.chelepie.utils.CodecPlayerUtil.TYPE_TIME_STRING;

/**
 * @author wsq
 * @time 14:48  2018/10/29/029
 * @describe
 *
 *   自定义SurfaceView ,用于绑定MediaCodec,解码视屏Byte,并用Surface 绘制每一帧
 */
public class VideoSurface extends SurfaceView implements SurfaceHolder.Callback{

    private String filePath ;
    private SurfaceHolder mSurfaceHolder;
    private Thread mDecodeThread;
    private boolean mStopFlag = false;
    private MediaCodec mCodec;
    /**
     *  每次去帧数
     */
    private int FrameRate = 28;
    private Boolean UseSPSandPPS = false;
    private static final int VIDEO_WIDTH = 1920;
    private static final int VIDEO_HEIGHT = 1088;
    private DataInputStream mInputStream;
    private Handler mHandler;
    /**
     * 用于获取,指定视屏文件,指定时间的Bitmap
     */
    MediaMetadataRetriever mmr = new MediaMetadataRetriever();

    /**
     * 是否终止
     */
    public boolean isPoast  =false ;

    /**
     * 截屏时候的 ,视频时间
     */
    private long captuerTime ;

    /**
     * 保存暂停时候的 位置
     */
    private int oldPlayIndex ;
    decodeThread decodeThread ;

    /**
     * 线程锁
     */
    private Object lock = new Object();

    byte[] streamBuffer ;
    ByteBuffer[] inputBuffers;
    public VideoSurface(Context context) {
        super(context);
        init();
    }

    private void init() {
        this.mSurfaceHolder = this.getHolder();
        mSurfaceHolder.addCallback(this);

    }


    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setmHandler(Handler mHandler) {
        this.mHandler = mHandler;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        mSurfaceHolder = holder;
        creatMedia(holder);
        startDecodingThread();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void creatMedia(SurfaceHolder holder) {
        try
        {
            //通过多媒体格式名创建一个可用的解码器
            mCodec = MediaCodec.createDecoderByType("video/avc");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //初始化编码器
        final MediaFormat mediaformat = MediaFormat.createVideoFormat("video/avc", VIDEO_WIDTH, VIDEO_HEIGHT);
        //获取h264中的pps及sps数据
        if (UseSPSandPPS) {
            byte[] header_sps = {0, 0, 0, 1, 103, 66, 0, 42, (byte) 149, (byte) 168, 30, 0, (byte) 137, (byte) 249, 102, (byte) 224, 32, 32, 32, 64};
            byte[] header_pps = {0, 0, 0, 1, 104, (byte) 206, 60, (byte) 128, 0, 0, 0, 1, 6, (byte) 229, 1, (byte) 151, (byte) 128};
            mediaformat.setByteBuffer("csd-0", ByteBuffer.wrap(header_sps));
            mediaformat.setByteBuffer("csd-1", ByteBuffer.wrap(header_pps));
        }
        //设置帧率
        mediaformat.setInteger(MediaFormat.KEY_FRAME_RATE, FrameRate);
        mCodec.configure(mediaformat, holder.getSurface(), null, 0);
        mCodec.start();

    }

    private void initSteam() {
        try {
            //获取文件输入流
            mInputStream = new DataInputStream(new FileInputStream(new File(filePath)));
            mmr.setDataSource(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            try {
                mInputStream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if(mCodec != null){
            mCodec.stop();
            mCodec.release();
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void startDecodingThread() {

        if(decodeThread == null){
            decodeThread  =    new decodeThread();
            mDecodeThread = new Thread(decodeThread);
        }
        initSteam();

        if(isPoast && oldPlayIndex > 0){
            isPoast =false ;
            decodeThread.jumpIndex = true;
            inputBuffers = mCodec.getInputBuffers();
            decodeThread.unlock();
        }else {
            inputBuffers = mCodec.getInputBuffers();
            decodeThread.jumpIndex = false;
        }

        if(!mDecodeThread.isAlive()){
            mDecodeThread =new Thread(decodeThread);
            mDecodeThread.start();
        }
    }

    /**
     * @author ldm
     * @description 解码线程
     * @time 2016/12/19 16:36
     */
    private class decodeThread implements Runnable {
        /**
         * 预加载,进度位置,防止再次进入页面黑屏
         */
        public boolean jumpIndex = false ;
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void run() {
            try {
                mHandler.sendEmptyMessage(TYPE_START_PLAY);
                synchronized (lock){
                    decodeLoop();
                }

            } catch (Exception e) {
                Logger.e(e.toString());
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        private void decodeLoop() throws InterruptedException {
            String timeAll = null;
            int totalLenth = 0;
            //存放目标文件的数据
//            ByteBuffer[] inputBuffers = mCodec.getInputBuffers();
            //解码后的数据，包含每一个buffer的元数据信息，例如偏差，在相关解码器中有效的数据大小
            MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
            long startMs = System.currentTimeMillis();
            long timeoutUs = 10000;
            byte[] marker0 = new byte[]{0, 0, 0, 1};
            byte[] dummyFrame = new byte[]{0x00, 0x00, 0x01, 0x20};
            try {
                //总时间 142 byte 为1秒
                timeAll = MyTimeUtil.formartTime1(mInputStream.available()/142);
                totalLenth = mInputStream.available();
                streamBuffer = getBytes(mInputStream);
            } catch (IOException e) {
                e.printStackTrace();
                Logger.e(e.toString()+"=================");
            }
            int bytes_cnt = 0;
            while (mStopFlag == false ) {
                bytes_cnt = streamBuffer.length;
                if (bytes_cnt == 0) {
                    streamBuffer = dummyFrame;
                }
                int startIndex = 0;
                int remaining = bytes_cnt;

                while (true && isPoast == false) {
                    if (remaining == 0 || startIndex >= remaining) {
                        break;
                    }

                    if(oldPlayIndex > 0){
                        startIndex = oldPlayIndex ;
                    }
                    int nextFrameStart = KMPMatch(marker0, streamBuffer, startIndex + 2, remaining);

                    if (nextFrameStart == -1) {
                        nextFrameStart = remaining;
                        captuerTime = remaining ;
                    } else {
                    }

                    int inIndex = mCodec.dequeueInputBuffer(timeoutUs);
                    if (inIndex >= 0) {
                        ByteBuffer byteBuffer = inputBuffers[inIndex];
                        byteBuffer.clear();
                        byteBuffer.put(streamBuffer, startIndex, nextFrameStart - startIndex);
                        //在给指定Index的inputbuffer[]填充数据后，调用这个函数把数据传给解码器
                        mCodec.queueInputBuffer(inIndex, 0, nextFrameStart - startIndex, 0, 0);
                        startIndex = nextFrameStart;
                        oldPlayIndex = startIndex;
                    } else {
                        continue;
                    }

                    int outIndex = mCodec.dequeueOutputBuffer(info, timeoutUs);
                    if (outIndex >= 0) {
                        //帧控制是不在这种情况下工作，因为没有PTS H264是可用的
                        while (info.presentationTimeUs / 1000 > System.currentTimeMillis() - startMs) {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                                Logger.e(e.toString());
                            }
                        }
                        boolean doRender = (info.size != 0);
                        //对outputbuffer的处理完后，调用这个函数把buffer重新返回给codec类。
                        mCodec.releaseOutputBuffer(outIndex, doRender);
                    }

                    if(!jumpIndex){
                        //刷新时间
                        reflshTime(timeAll, nextFrameStart);
                        //计算进度,刷新进度
                        reflshProgress(totalLenth, nextFrameStart);
                    }

                    if(jumpIndex && outIndex > 0 ){
                        jumpIndex = false;
                        isPoast =true ;
                        mHandler.sendEmptyMessage(TYPE_PAUSE);
                    }
                }
                if(isPoast){
                    lock.wait();
                }else {
                    oldPlayIndex = 0 ;
                    Message  end= new Message();
                    end.what = TYPE_END;
                    mHandler.sendMessage(end);
                    lock.wait();
                }
            }
        }

        public void unlock(){
            synchronized (lock){
                lock.notify();
            }
        }
    }

    private void reflshProgress(int totalLenth, int nextFrameStart) {
        int i1 = (int) (((float)(nextFrameStart) / (float)(totalLenth)) * 100);
        if(i1 > 0){
            Message msg2 = new Message();
            msg2.what = TYPE_PROCESS;
            msg2.obj = i1;
            mHandler.sendMessage(msg2);
        }
    }

    private void reflshTime(String timeAll, int nextFrameStart) {
        String timePlay = MyTimeUtil.formartTime1(nextFrameStart /142);
        captuerTime = nextFrameStart/142 * 1000 ;
        Message msg = new Message();
        msg.what = TYPE_TIME_STRING;
        msg.obj = timePlay + "/" + timeAll;
        mHandler.sendMessage(msg);
    }

    public static byte[] getBytes(InputStream is) throws IOException {
        int len;
        int size = 1024;
        byte[] buf;
        if (is instanceof ByteArrayInputStream) {
            size = is.available();
            buf = new byte[size];
            len = is.read(buf, 0, size);
        } else {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            buf = new byte[size];
            while ((len = is.read(buf, 0, size)) != -1) {
                bos.write(buf, 0, len);
            }
            buf = bos.toByteArray();
        }
        return buf;
    }
    // 解码还64 流算法
    int KMPMatch(byte[] pattern, byte[] bytes, int start, int remain) {

        try {
            Thread.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int[] lsp = computeLspTable(pattern);

        int j = 0;  // Number of chars matched in pattern
        for (int i = start; i < remain; i++) {
            while (j > 0 && bytes[i] != pattern[j]) {
                // Fall back in the pattern
                j = lsp[j - 1];  // Strictly decreasing
            }
            if (bytes[i] == pattern[j]) {
                // Next char matched, increment position
                j++;
                if (j == pattern.length) {
                    return i - (j - 1);
                }
            }
        }

        return -1;  // Not found
    }
    // 解码还64 流算法
    int[] computeLspTable(byte[] pattern) {
        int[] lsp = new int[pattern.length];
        lsp[0] = 0;  // Base case
        for (int i = 1; i < pattern.length; i++) {
            // Start by assuming we're extending the previous LSP
            int j = lsp[i - 1];
            while (j > 0 && pattern[i] != pattern[j]) {
                j = lsp[j - 1];
            }
            if (pattern[i] == pattern[j]) {
                j++;
            }
            lsp[i] = j;
        }
        return lsp;
    }

    /**
     * 暂停
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void stop(){
        isPoast =true ;
        mHandler.sendEmptyMessage(TYPE_PAUSE);

    }


    /**
     * 继续开始
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void connetStart(){

        mHandler.sendEmptyMessage(TYPE_START_PLAY);
        isPoast =false ;

        if(decodeThread != null){
            decodeThread.unlock();
        }
    }

    //截屏
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void cropBitmap() {
        Bitmap bitmap = null ;
        if(mmr != null){
             bitmap =  mmr.getFrameAtTime(captuerTime);
        }
        Message msg = new Message();
        msg.what = TYPE_CUT_SUCCSE;
        msg.obj = bitmap;
        mHandler.sendMessage(msg);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void destroy(){
        mStopFlag = true ;
        if(decodeThread != null){
            decodeThread.unlock();
            decodeThread = null ;
        }

        if(mCodec != null){
            mCodec.release();
            mCodec = null ;
        }

        if(mmr !=  null){
            mmr.release();
            mmr  = null ;
        }
        inputBuffers = null ;
        streamBuffer = null ;
    }
}
