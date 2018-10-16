
package com.carlt.sesame.data.safety;

import java.util.ArrayList;

/**
 * 授权设备列表info
 * @author Administrator
 *
 */
public class MobileListInfo {

    private int limit;

    private int offset;
    
    private boolean hasMore;//是否有下一页

    private ArrayList<MobileInfo> mMobileInfoList = new ArrayList<MobileInfo>();

    public ArrayList<MobileInfo> getmMobileInfoList() {
        return mMobileInfoList;
    }

    public void addmMobileInfoList(MobileInfo mMobileInfo) {
        mMobileInfoList.add(mMobileInfo);
    }

    public void addmMobileInfoList(ArrayList<MobileInfo> mList) {
        mMobileInfoList.addAll(mList);
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
