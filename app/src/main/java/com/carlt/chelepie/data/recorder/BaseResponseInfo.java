
package com.carlt.chelepie.data.recorder;

public class BaseResponseInfo {

    /**
     * 作者：秋良 描述：所有Http请求返回数据的基类
     */
    public final static int SUCCESS = 200;


    public final static int ERRO = 0;
    
	/** 车乐拍连接断开错误 */
    public final static int ERRO_DISCONNECT = 1;

    public final static int NO_TOKEN = 1002;
    
    public final static int TOKEN_DISABLE = 1003;

    public final static int VALIDATE_LIMIT = 110000;// 获取验证码次数达到上限,调用语音接口

    public final static int RECORD_OK = 100; //返回数据正常
    public final static int RECORD_RESTART = 603;//设备需要重启
    
    private int flag;

    private String info;

    public int getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = Integer.parseInt(flag);
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

}
