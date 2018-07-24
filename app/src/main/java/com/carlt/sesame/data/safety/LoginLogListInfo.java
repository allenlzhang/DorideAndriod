
package com.carlt.sesame.data.safety;

import java.util.ArrayList;

/**
 * 近期登录记录列表info
 * @author Administrator
 *
 */
public class LoginLogListInfo {

    private int limit;

    private int offset;
    
    private boolean hasMore;//是否有下一页

    private ArrayList<LoginLogInfo> mLoginLogInfoList = new ArrayList<LoginLogInfo>();

    public ArrayList<LoginLogInfo> getmLoginLogInfoList() {
        return mLoginLogInfoList;
    }

    public void addmLoginLogInfoList(LoginLogInfo mLoginLogInfo) {
        mLoginLogInfoList.add(mLoginLogInfo);
    }

    public void addmLoginLogInfoList(ArrayList<LoginLogInfo> mList) {
        mLoginLogInfoList.addAll(mList);
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }
}
