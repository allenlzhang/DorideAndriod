package com.carlt.sesame.data.usercenter;

import com.carlt.sesame.data.BaseResponseInfo;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Created by liu on 2018/4/8.
 */

public class VersionLog extends BaseResponseInfo implements Serializable{

   private String version;

    private ArrayList<String> infos;

    private String createdate;

    private String createdateString;


    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public ArrayList<String> getInfos() {
        return infos;
    }

    public void setInfos(ArrayList<String> infos) {
        this.infos = infos;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getCreatedateString() {
        return createdateString;
    }

    public void setCreatedateString(String createdateString) {
        this.createdateString = createdateString;
    }
}
