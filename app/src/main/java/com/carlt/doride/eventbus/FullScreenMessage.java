package com.carlt.doride.eventbus;

/**
 * @author wsq
 * @time 11:08  2018/10/25/025
 * @describe  记录仪由全屏返回 时候的回调
 */
public class FullScreenMessage {
   public   int message;

    public FullScreenMessage(int message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "FullScreenMessage{" +
                "message=" + message +
                '}';
    }
}
