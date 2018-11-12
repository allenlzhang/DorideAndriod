package com.carlt.chelepie.view.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.GridLayoutManager.SpanSizeLookup;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.chelepie.data.recorder.PieDownloadInfo;
import com.carlt.chelepie.protocolstack.recorder.RecorderDownloadVideoThumbnailParser;
import com.carlt.chelepie.view.activity.PicViewPagerActivity;
import com.carlt.chelepie.view.activity.PlayerActivity;
import com.carlt.chelepie.view.adapter.MedialistAdapter;
import com.carlt.doride.R;
import com.carlt.doride.base.BaseFragment;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.utility.MyTimeUtil;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;


public class MediaAllFragment extends BaseFragment {

	private RecyclerView mRecyclerView;
	private MedialistAdapter mAdapter;
	private String startTime = null;
	private SwipeRefreshLayout mSwipeRefreshLayout;
	
	private ArrayList<PieDownloadInfo> pieDownloadInfosNew;
	
	private List<PieDownloadInfo> infoLists;
	/**
	 * 加载没有数据时候
	 */
	private View layoutNodata;

	@Override
	protected View inflateView(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.fragment_medialist, null);
		mRecyclerView = (RecyclerView) view
				.findViewById(R.id.medialist_recyclerview);
		mSwipeRefreshLayout = (SwipeRefreshLayout) view
				.findViewById(R.id.medialist_swiperefreshlayout);
		layoutNodata = view.findViewById(R.id.nodata_lay_main);
		hideView();
		initRecyclerView();
		return view;
	}

	public void setData(List<PieDownloadInfo> capInfoList) {
		this.infoLists = capInfoList;
	}
	GridLayoutManager gridLayoutManager;

	private void initRecyclerView() {

		// 设置Item增加、移除动画
		mRecyclerView.setItemAnimator(new DefaultItemAnimator());

		mSwipeRefreshLayout.setColorSchemeColors(mCtx.getResources().getColor(
				R.color.blue_light));
		mSwipeRefreshLayout
				.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
					@Override
					public void onRefresh() {
						// TODO
						mHAnHandler.sendEmptyMessageAtTime(5, 5 * 1000);
						// RecorderControl.getCaptureFilelist(listener_refresh,
						// startTime,
						// false);
					}
				});
		
		gridLayoutManager = new GridLayoutManager(mCtx, 5);
		mRecyclerView.setLayoutManager(gridLayoutManager);
//		loadData();
	}

	@Override
	public void loadData() {
		if(infoLists != null && !infoLists.isEmpty()){
			loadSuccess(infoLists);
		}
		LogUtils.e("=============loadData==============");
//		if(null != picMediaLisenter){
//			picMediaLisenter.onRefreshing();
//		}
	}


	public void loadSuccess(Object data) {

		ArrayList<PieDownloadInfo> pieDownloadInfos = new ArrayList<PieDownloadInfo>();
		pieDownloadInfosNew = new ArrayList<PieDownloadInfo>();

		if (data != null) {
			pieDownloadInfos = (ArrayList<PieDownloadInfo>) data;
			//测试开始
			//pieDownloadInfos=new ArrayList<PieDownloadInfo>();
			//测试结束
			if(pieDownloadInfos.size()>0){
				loadSuccess();
				methodUI(pieDownloadInfos, pieDownloadInfosNew);
				
				if(mAdapter == null){
					mAdapter = new MedialistAdapter(mCtx, pieDownloadInfosNew);
				}else{
					mAdapter.setData(pieDownloadInfosNew);
					mAdapter.notifyDataSetChanged();
				}
				
				mAdapter.setOnItemClickListener(new MedialistAdapter.MyItemClickListener() {

					@Override
					public void onItemClick(View view, int postion) {
						PieDownloadInfo mInfo = pieDownloadInfosNew.get(postion);
						int type = mInfo.getType();
						switch (type) {
						case PieDownloadInfo.TYPE_JPG:
							// 跳转至图像展示
							Intent mIntent1 = new Intent(mCtx, PicViewPagerActivity.class);
							mIntent1.putExtra(PicViewPagerActivity.FILEPIC, mInfo);
							startActivity(mIntent1);
							break;

						case PieDownloadInfo.TYPE_H264:
							// 跳转至视频播放页面
							Intent mIntent2 = new Intent(mCtx, PlayerActivity.class);
							mIntent2.putExtra("pieDownloadInfo", mInfo);
							startActivity(mIntent2);
							break;
						}

					}
				});
				// 设置adapter
				mRecyclerView.setAdapter(mAdapter);
				mAdapter.notifyDataSetChanged();
				gridLayoutManager = new GridLayoutManager(mCtx, 5);
				gridLayoutManager.setSpanSizeLookup(new SpanSizeLookup() {

					@Override
					public int getSpanSize(int position) {
						int type = mAdapter.getItemViewType(position);
						if (type == MedialistAdapter.TYPE_TIME_TOP
								|| type == MedialistAdapter.TYPE_TIME) {
							return 5;
						}
						if (type == MedialistAdapter.TYPE_NORMAL) {
							return 2;
						}
						return 1;
					}
				});
				// 设置布局管理器
				mRecyclerView.setLayoutManager(gridLayoutManager);
			}else{
				loadNodata();
			}
		} else {
			loadNodata();
		}
	}

	/**
	 *  
	 * @param pieDownloadInfos
	 * @param pieDownloadInfosNew
	 */
	private void methodUI(final ArrayList<PieDownloadInfo> pieDownloadInfos,
			final ArrayList<PieDownloadInfo> pieDownloadInfosNew) {
		int count = 0;
		String currentDate = "";
		String currentYear="";
		for (int i = 0; i < pieDownloadInfos.size(); i++) {
			PieDownloadInfo mInfoOld = pieDownloadInfos.get(i);
			String nextDate = MyTimeUtil.parseDate2(mInfoOld.getStartTime());
			String nextYear=nextDate.substring(0, 4);
			if (!TextUtils.equals(nextDate, currentDate)) {
				if(!TextUtils.equals(nextYear, currentYear)){
					PieDownloadInfo mInfoDate = new PieDownloadInfo();
					mInfoDate.setType(PieDownloadInfo.TYPE_DATE_YEAR);
					mInfoDate.setStartTime(mInfoOld.getStartTime());
					pieDownloadInfosNew.add(mInfoDate);
				}else{
					PieDownloadInfo mInfoDate = new PieDownloadInfo();
					mInfoDate.setType(PieDownloadInfo.TYPE_DATE);
					mInfoDate.setStartTime(mInfoOld.getStartTime());
					pieDownloadInfosNew.add(mInfoDate);
				}

				PieDownloadInfo mInfoLine = new PieDownloadInfo();
				mInfoLine.setType(PieDownloadInfo.TYPE_LINE);
				pieDownloadInfosNew.add(mInfoLine);
				count = 0;
			}
			if (count == 2) {
				PieDownloadInfo mInfoLine = new PieDownloadInfo();
				mInfoLine.setType(PieDownloadInfo.TYPE_LINE);
				pieDownloadInfosNew.add(mInfoLine);
				count = 0;
			}
			pieDownloadInfosNew.add(mInfoOld);
			currentDate = nextDate;
			currentYear=nextYear;
			count++;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(null != mMediaThumbnailDownloader){
			mMediaThumbnailDownloader.stopDownLoadVideoThumbnail();
		}
	}
	
	/**
	 * 下载图片
	 */
	private RecorderDownloadVideoThumbnailParser mMediaThumbnailDownloader;

	/**
	 * 下载缩略图
	 */
	public void downloadThumbnail(final List<PieDownloadInfo> mlists) {
		final BaseParser.ResultCallback thumbnailDownloadCallback = new BaseParser.ResultCallback() {

			@Override
			public void onSuccess(BaseResponseInfo o) {
				Message msg = new Message();
				msg.what = 4;
				msg.obj = o;
				mHAnHandler.sendMessage(msg);
			}

			@Override
			public void onError(BaseResponseInfo bInfo) {

			}

		};
		Thread thumbnailThread = new Thread(new Runnable() {
			@Override
			public void run() {
				List<PieDownloadInfo> lists = mlists;
				if (null == mMediaThumbnailDownloader) {
					mMediaThumbnailDownloader = new RecorderDownloadVideoThumbnailParser(
							thumbnailDownloadCallback, lists);
					mMediaThumbnailDownloader.run();
				}
			}
		});
		thumbnailThread.start();
	}

	protected CPControl.GetResultListCallback listener_refresh = new CPControl.GetResultListCallback() {

		@Override
		public void onFinished(Object o) {
			Message msg = new Message();
			msg.what = 5;
			msg.obj = o;
			mHAnHandler.sendMessage(msg);
		}

		@Override
		public void onErro(Object o) {
			Message msg = new Message();
			msg.what = 1;
			msg.obj = 6;
			mHAnHandler.sendMessage(msg);

		}
	};

	@SuppressLint("HandlerLeak")
	private Handler mHAnHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 4:
				// 缓冲图片成功(有了新的图片后刷新页面)
				BaseResponseInfo info = (BaseResponseInfo) msg.obj;
				if (!RecorderDownloadVideoThumbnailParser.ISEXIST.equals(info
						.getInfo())) {
					picMediaLisenter.onPicDownLoadFinished();
				}
				break;
			case 5:
				// 刷新成功
				// 加载完数据设置为不刷新状态，将下拉进度收起来
				mSwipeRefreshLayout.setRefreshing(false);
				if(null != picMediaLisenter){
					// mSwipeRefreshLayout.
					picMediaLisenter.onRefreshing();
				}
				// TODO
				break;
			case 6:
				// 刷新失败
				// 加载完数据设置为不刷新状态，将下拉进度收起来
				mSwipeRefreshLayout.setRefreshing(false);
				// TODO
				break;
			}

		}
	};
	
	public void notifyAdapter(){
		if(mAdapter != null){
			if(null == pieDownloadInfosNew || pieDownloadInfosNew.size() == 0){
				loadNodata();
			}else{
				mAdapter.notifyDataSetChanged();
			}
		}
	}
	
	public OnPicMediaLisenter picMediaLisenter;
	
	public void setOnPicMediaLisenter(OnPicMediaLisenter lisenter){
		picMediaLisenter = lisenter;
	}
	
	public interface OnPicMediaLisenter{
		//图片加载完成
		void onPicDownLoadFinished();
		
		void onRefreshing();
	}

	public void loadNodata(){
		layoutNodata.setVisibility(View.VISIBLE);
		mSwipeRefreshLayout.setVisibility(View.GONE);
	}
	public void loadSuccess(){
		layoutNodata.setVisibility(View.GONE);
		mSwipeRefreshLayout.setVisibility(View.VISIBLE);
	}
	// 橫屏不重新加載activity，調用該方法
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		try {
			Log.e("onConfigurationChanged", "fragment: "+  newConfig.orientation );

			// Checks the orientation of the screen
			if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			//	content.setVisibility(View.INVISIBLE);

			} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
			//	content.setVisibility(View.VISIBLE);

			}
		} catch (Exception ex) {

		}

		super.onConfigurationChanged(newConfig);
		// 如果是橫屏時候


	}
	@Override
	public void onResume() {
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		super.onResume();



	}
}
