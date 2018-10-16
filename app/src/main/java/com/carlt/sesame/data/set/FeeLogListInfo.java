
package com.carlt.sesame.data.set;

import java.util.ArrayList;

/**
 * 近期登录记录列表info
 * @author Administrator
 *
 */
public class FeeLogListInfo {

    private int limit;

    private int offset;
    
    private boolean hasMore;//是否有下一页

    private ArrayList<FeeLogInfo> mFeeLogInfoList = new ArrayList<FeeLogInfo>();

    public ArrayList<FeeLogInfo> getmFeeLogInfoList() {
        return mFeeLogInfoList;
    }

    public void addmFeeLogInfoList(FeeLogInfo mFeeLogInfo) {
        mFeeLogInfoList.add(mFeeLogInfo);
    }

    public void addmFeeLogInfoList(ArrayList<FeeLogInfo> mList) {
        mFeeLogInfoList.addAll(mList);
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
