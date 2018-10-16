
package com.carlt.sesame.utility;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.carlt.sesame.data.SesameLoginInfo;
import com.orhanobut.logger.Logger;

import java.io.IOException;

/**
 * 声音播放相关
 * @author Daisy
 *
 */
public class PlayRadio {
    static PlayRadio mPlayRadio;

    Context mContext;

    public PlayRadio(Context mContext) {
        this.mContext = mContext;
    }

    public static PlayRadio getInstance(Context mContext) {
        if (mPlayRadio == null) {
            mPlayRadio = new PlayRadio(mContext);
        }
        return mPlayRadio;
    }

    private static MediaPlayer mp; // 音频播放

    /**
     * 点击播放音效
     * @param sourceId 音频文件Id
     */
    public void playClickVoice(int sourceId) {
        if (SesameLoginInfo.isRemoteSoundOpen()) {
            try {
                if (mp == null) {
                    mp = new MediaPlayer();
                }
                mp.reset();
                AssetFileDescriptor afd = mContext.getResources().openRawResourceFd(sourceId);
                if (afd == null)
                    return;
                mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                afd.close();

                mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mp.prepare();
            } catch (IllegalStateException e) {
                e.printStackTrace();
                Logger.e(e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                Logger.e(e.getMessage());
            }
            mp.start();
        }
    }
}
