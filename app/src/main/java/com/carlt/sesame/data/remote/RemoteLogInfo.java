
package com.carlt.sesame.data.remote;

import com.carlt.doride.R;
import com.carlt.sesame.data.BaseResponseInfo;

import java.util.HashMap;

/**
 * 远程操作记录info
 * @author Administrator
 *
 */
public class RemoteLogInfo extends BaseResponseInfo {
    int logtype;//记录类型
    public final static int TYPE_FLASHING=11;//闪灯鸣笛
    public final static int TYPE_UNLOCK=21;//解锁
    public final static int TYPE_LOCK=22;//落锁
    public final static int TYPE_START=31;//启动
    public final static int TYPE_STOP=41;//熄火
    public final static int TYPE_AIROPEN=51;//远程开启空调
    public final static int TYPE_AIRCLOSE=52;//远程关闭空调
    public final static int TYPE_AIRDEFROST=53;//空调/一键除霜
    public final static int TYPE_AIRCOLD=54;//空调/最大制冷
    public final static int TYPE_AIRHOT=55;//空调/最大制热
    public final static int TYPE_AIRANION=56;//空调/负离子
    public final static int TYPE_AIRCLEAN=57;//空调/座舱清洁
    public final static int TYPE_WINOPEN=61;//远程开窗
    public final static int TYPE_WINCLOSE=62;//远程关窗
    public final static int TYPE_TRUNKOPEN=71;//远程开启后备箱
    public final static int TYPE_SEATOPEN=81;//远程开启座椅加热
    public final static int TYPE_SEATCLOSE=82;//远程关闭座椅加热
    public final static int TYPE_PURIFYOPEN=91;//远程开启空气净化
    public final static int TYPE_PURIFYCLOSE=92;//远程关闭空气净化
    public final static int TYPE_CHARGENOW=93;//远程立即充电
    public final static int TYPE_CHARGETIMER=94;//远程定时充电
    public final static int TYPE_CHARGESTOP=95;//远程停止充电
    public final static int TYPE_CHARGECANCEL=96;//取消定时充电
    
    private HashMap<Integer, String> names=new HashMap<Integer, String>();
    
    private HashMap<Integer, Integer> icons=new HashMap<Integer, Integer>();
    
    int id;
    String logName;//记录名称
    int logIcon;//记录图标
    String logtime;//记录时间
    String device_name;//来源（操作的手机型号）
    String result;//操作结果
    public final static String result_success="1";//成功
    
    public int getLogtype() {
        return logtype;
    }
    public void setLogtype(int logtype) {
        this.logtype = logtype;
    }
    public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLogName() {
        return logName;
    }
    public void setLogName(String logName) {
        this.logName = logName;
    }
    public int getLogIcon() {
        return logIcon;
    }
    public void setLogIcon(int logIcon) {
        this.logIcon = logIcon;
    }
    public String getLogtime() {
        return logtime;
    }
    public void setLogtime(String logtime) {
        this.logtime = logtime;
    }
    public String getDevice_name() {
        return device_name;
    }
    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }
    public String getResult() {
        return result;
    }
    public void setResult(String result) {
        this.result = result;
    }
    public HashMap<Integer, String> getNames() {
        names.put(TYPE_FLASHING, "声光寻车");
        names.put(TYPE_UNLOCK, "解锁");
        names.put(TYPE_LOCK, "落锁");
        names.put(TYPE_START, "远程启动");
        names.put(TYPE_STOP, "远程熄火");
        names.put(TYPE_AIROPEN, "开启空调");
        names.put(TYPE_AIRCLOSE, "关闭空调");
        names.put(TYPE_AIRDEFROST, "一键除霜");
        names.put(TYPE_AIRCOLD, "最大制冷");
        names.put(TYPE_AIRHOT, "最大制热");
        names.put(TYPE_AIRANION, "负离子");
        names.put(TYPE_AIRCLEAN, "座舱清洁");
        names.put(TYPE_WINOPEN, "远程开窗");
        names.put(TYPE_WINCLOSE, "远程关窗");
        names.put(TYPE_TRUNKOPEN, "开启后备箱");
        names.put(TYPE_SEATOPEN, "开启座椅加热");
        names.put(TYPE_SEATCLOSE, "关闭座椅加热");
        names.put(TYPE_PURIFYOPEN, "开启空气净化");
        names.put(TYPE_PURIFYCLOSE, "关闭空气净化");
        names.put(TYPE_CHARGENOW, "立即充电");
        names.put(TYPE_CHARGETIMER, "定时充电");
        names.put(TYPE_CHARGESTOP, "停止充电");
        names.put(TYPE_CHARGECANCEL, "取消定时充电");
        return names;
    }
    public HashMap<Integer, Integer> getIcons() {
        icons.put(TYPE_FLASHING, R.drawable.horm_selected_no);
        icons.put(TYPE_UNLOCK, R.drawable.openlock_selected_no);
        icons.put(TYPE_LOCK, R.drawable.closelock_selected_no);
        icons.put(TYPE_START, R.drawable.remote_start_small);
        icons.put(TYPE_STOP, R.drawable.remote_stop_small);
        icons.put(TYPE_AIROPEN, R.drawable.air_selected_no);
        icons.put(TYPE_AIRCLOSE, R.drawable.air_condition_close_selected_no);
        icons.put(TYPE_AIRDEFROST, R.drawable.air_selected_no);
        icons.put(TYPE_AIRCOLD, R.drawable.air_selected_no);
        icons.put(TYPE_AIRHOT, R.drawable.air_selected_no);
        icons.put(TYPE_AIRANION, R.drawable.air_selected_no);
        icons.put(TYPE_AIRCLEAN, R.drawable.air_selected_no);
        icons.put(TYPE_WINOPEN, R.drawable.remote_default);
        icons.put(TYPE_WINCLOSE,R.drawable.remote_default);
        icons.put(TYPE_TRUNKOPEN, R.drawable.trunck_selected_no);
        icons.put(TYPE_SEATOPEN, R.drawable.remote_default);
        icons.put(TYPE_SEATCLOSE, R.drawable.remote_default);
        icons.put(TYPE_PURIFYOPEN, R.drawable.remote_default);
        icons.put(TYPE_PURIFYCLOSE, R.drawable.remote_default);
        
        return icons;
    }
}
