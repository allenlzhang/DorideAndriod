package com.carlt.sesame.data;

//广告信息
public class AdvertiseInfo extends BaseResponseInfo{
    String id;
    String filePath;//图片地址
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getFilePath() {
        return filePath;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
}
