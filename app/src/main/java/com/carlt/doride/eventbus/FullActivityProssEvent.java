package com.carlt.doride.eventbus;

/**
 * @author wsq
 * @time 11:31  2018/11/5/005
 * @describe  直播全屏页面,设置饱和度,清晰度事件
 */
public class FullActivityProssEvent {
    // 0= VideoColor  1 = videoSeize ;
    public int status;
    public int value ;

    public FullActivityProssEvent(int status) {
        this.status = status;
    }

    public FullActivityProssEvent(int status, int value) {
        this.status = status;
        this.value = value;
    }
}
