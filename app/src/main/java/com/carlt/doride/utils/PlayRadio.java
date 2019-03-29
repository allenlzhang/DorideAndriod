
package com.carlt.doride.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.carlt.doride.model.LoginInfo;

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

}
