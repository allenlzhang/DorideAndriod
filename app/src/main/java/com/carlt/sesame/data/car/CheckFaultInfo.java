
package com.carlt.sesame.data.car;

public class CheckFaultInfo {
    // "code": "P0001",
    // "cn": "燃油量调节器控制电路范围/性能",
    // "en": "Fuel Volume Regulator Control Circuit/Open",
    // "scope": "燃油, 空气或排放控制",
    // "content": "（在喷油嘴被广泛使用的新型汽车中，燃油量调节器已经很少被使用)"
    public String code;

    private String cn;

    private String en;

    private String scope;

    private String content;

    // 0加载 1正确 2错误
    private int flag;

    public final static int STATE0 = 0;

    public final static int STATE1 = 1;

    public final static int STATE2 = 2;
    

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
