
package com.carlt.sesame.data.remote;

import com.carlt.sesame.data.BaseResponseInfo;

import java.util.ArrayList;

/**
 * 远程记录列表info
 * @author Administrator
 *
 */
public class RemoteLogListInfo extends BaseResponseInfo{

    private int limit;

    private int offset;
    
    private boolean hasMore;//是否有下一页

    private ArrayList<RemoteLogInfo> mRemoteLogInfoList= new ArrayList<RemoteLogInfo>();

    public ArrayList<RemoteLogInfo> getmRemoteLogInfoList() {
        return mRemoteLogInfoList;
    }

    public void addmRemoteLogInfoList(RemoteLogInfo mRemoteLogInfo) {
        mRemoteLogInfoList.add(mRemoteLogInfo);
    }

    public void addmRemoteLogInfoList(ArrayList<RemoteLogInfo> mList) {
        mRemoteLogInfoList.addAll(mList);
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
