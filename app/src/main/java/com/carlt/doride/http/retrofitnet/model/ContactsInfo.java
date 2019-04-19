package com.carlt.doride.http.retrofitnet.model;

import com.blankj.utilcode.util.LogUtils;

import java.io.Serializable;

/**
 * Created by Marlon on 2019/3/28.
 */
public class ContactsInfo implements Serializable {
    private static ContactsInfo contactsInfo = null;
    private ContactsInfo (){}
    public static ContactsInfo getInstance(){
        if (contactsInfo == null){
            synchronized (ContactsInfo.class){
                if (contactsInfo == null){
                    contactsInfo = new ContactsInfo();
                }
            }
        }
        return contactsInfo;
    }

    public String name  = ""; //企业全称
    public String address = ""; //详细地址
    public String map = ""; //百度坐标
    public String salesHotLine = ""; //销售热线
    public String serviceHotLine = ""; //服务电话

    public void setContactsInfo(ContactsInfo contactsInfo){

        LogUtils.e(contactsInfo);
        name = contactsInfo.name;
        address = contactsInfo.address;
        map = contactsInfo.map;
        salesHotLine = contactsInfo.salesHotLine;
        serviceHotLine = contactsInfo.serviceHotLine;
    }

    public void initContactsInfo(){
        name = "";
        address = "";
        map = "";
        salesHotLine = "";
        serviceHotLine = "";
    }

    @Override
    public String toString() {
        return "ContactsInfo{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", map='" + map + '\'' +
                ", salesHotLine='" + salesHotLine + '\'' +
                ", serviceHotLine='" + serviceHotLine + '\'' +
                '}';
    }
}
