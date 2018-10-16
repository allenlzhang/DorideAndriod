
package com.carlt.sesame.data;

public class BaseResponseInfo {

    /**
     * 作者：秋良 描述：所有Http请求返回数据的基类
     */
    public final static int SUCCESS = 200;

    public final static int ERRO = 0;

    public final static int NO_TOKEN = 1002;

    public final static int TOKEN_DISABLE = 1003;

    public final static int DEVICE_UPDATE = 1020;// 硬件升级中

    public final static int VALIDATE_LIMIT = 110000;// 获取验证码次数达到上限,调用语音接口

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

    @Override
    public String toString() {
        return "BaseResponseInfo{" +
                "flag=" + flag +
                ", info='" + info + '\'' +
                '}';
    }
}
