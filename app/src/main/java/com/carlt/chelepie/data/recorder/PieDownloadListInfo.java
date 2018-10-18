package com.carlt.chelepie.data.recorder;


import com.carlt.doride.data.BaseResponseInfo;

import java.util.ArrayList;

/**
 * 
 * 缩略图信息
 * @author @Y.yun
 * 
 */
public class PieDownloadListInfo extends BaseResponseInfo {
	private ArrayList<PieDownloadInfo> arrays = new ArrayList<PieDownloadInfo>();
	private boolean hasMore = false;

	public ArrayList<PieDownloadInfo> getArrays() {
		return arrays;
	}

	public void setArrays(ArrayList<PieDownloadInfo> arrays) {
		this.arrays = arrays;
	}

	public boolean isHasMore() {
		return hasMore;
	}

	public void setHasMore(boolean hasMore) {
		this.hasMore = hasMore;
	}

}
