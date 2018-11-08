package com.carlt.doride.ui.activity.setting;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.doride.data.flow.TrafficPackagePurchaseLogInfo;
import com.carlt.doride.data.flow.TrafficPackagePurchaseLogListInfo;
import com.carlt.doride.ui.adapter.TrafficPurchaseLogAdapter;
import com.carlt.doride.ui.pull.PullToRefreshBase;
import com.carlt.doride.ui.pull.PullToRefreshListView;
import com.carlt.doride.ui.view.PopBoxCreat;
import com.carlt.doride.ui.view.SegmentControl;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.ui.activity.base.LoadingActivityWithTitle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * 充值服务记录
 * 
 * @author Administrator
 */

public class TrafficPackagePurchaseLogActivity extends LoadingActivityWithTitle {
	private ImageView back;// 头部返回键

	private TextView title;// 标题文字

	private TextView txtRight;// 头部右侧文字

	private PullToRefreshListView mPullListView;

	private ListView mListView;

	private TextView mTxtEmpty;// 没有消息时的提示文字

	private final static int LIMIT = 20;
	private SegmentControl segment_control;
	private Dialog         mDialog;

	private ArrayList<TrafficPackagePurchaseLogInfo> mPurchaseLogList = new ArrayList<>();

	private TrafficPackagePurchaseLogListInfo mInfoLists;

	private TrafficPurchaseLogAdapter mAdapter;

	private TrafficPackagePurchaseLogInfo flagPackagePurchaseLogInfo;// 记录offset中最后一个月份

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_traffic_log);
		setTitleView(R.layout.head_back);
		if (mDialog==null) {
			  mDialog = PopBoxCreat.createDialogWithProgress(this,
                    "数据提交中...");
		}
		initTitle();
		init();
		LoadData();
	}

	private void initTitle() {
		back = (ImageView) findViewById(R.id.head_back_img1);
		title = (TextView) findViewById(R.id.head_back_txt1);
		txtRight = (TextView) findViewById(R.id.head_back_txt2);

		back.setImageResource(R.drawable.arrow_back);
		title.setText("充值记录");
		txtRight.setVisibility(View.GONE);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	private int package_type=2;
	private void init() {
		mPullListView = (PullToRefreshListView) findViewById(R.id.traffic_log_list);
		mTxtEmpty = (TextView) findViewById(R.id.traffic_log_txt_empty);
		segment_control = (SegmentControl) findViewById(R.id.segment_control);
		mListView = mPullListView.getRefreshableView();
		mListView.setVerticalScrollBarEnabled(false);
		mListView.setSelector(getResources().getDrawable(
				R.drawable.list_divider_bg));
		segment_control
		.setOnSegmentControlClickListener(new SegmentControl.OnSegmentControlClickListener() {

			@Override
			public void onSegmentControlClick(int index) {
				switch (index) {
				case 0:
					// 加油包
					package_type=2;
//					CPControl.GetTrafficPackageLogResult(LIMIT, 0,package_type, listener);
					LoadData();
					break;
				case 1:
					// 改套餐
					package_type=4;
					LoadData();
					break;
				case 2:
					// 续套餐
					package_type=3;
					LoadData();
					break;
				default:
					break;
				}
			}
			
		});
		mPullListView.setPullLoadEnabled(true);
		mPullListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// 下拉刷新
				PullDown();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// 上拉刷新
				PullUp();
			}
		});
	}

	/**
	 * 下拉刷新
	 */
	private void PullDown() {
		// 调用接口
		CPControl.GetTrafficPackageLogResult(LIMIT, 0,package_type, listener);
	}

	/**
	 * 上拉获取更多数据
	 */
	private void PullUp() {
		// 调用接口

		int ratio = mAdapter.getCount() % 21 == 0 ? mAdapter.getCount() / 21
				: mAdapter.getCount() / 21 + 1;
		int offset = mInfoLists.getOffset();
		flagPackagePurchaseLogInfo = mAdapter.getItem((ratio - 1) * 21);
		CPControl.GetTrafficPackageLogResult(LIMIT, ratio * 20,package_type,
				listener_loadmore);
	}

	private void setLastUpdateTime() {
		SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
		String text = mDateFormat.format(new Date(System.currentTimeMillis()));
		mPullListView.setLastUpdatedLabel(text);
	}

	@Override
	protected void LoadSuccess(Object data) {
		super.LoadSuccess(data);
		mInfoLists = (TrafficPackagePurchaseLogListInfo) data;
		if (mInfoLists != null) {
			mPurchaseLogList = mInfoLists
					.getmTrafficPackagePurchaseLogInfoList();
			if (mAdapter == null) {
				mAdapter = new TrafficPurchaseLogAdapter(
						TrafficPackagePurchaseLogActivity.this,
						mPurchaseLogList);
				mListView.setAdapter(mAdapter);
			} else {
				mAdapter.setmList(mPurchaseLogList);
				mAdapter.notifyDataSetChanged();
			}
			mAdapter.setPackType(package_type);
			if (mPurchaseLogList.size() == 0) {
				mPullListView.setVisibility(View.GONE);
				mTxtEmpty.setVisibility(View.VISIBLE);
			} else {
				mPullListView.setVisibility(View.VISIBLE);
				mTxtEmpty.setVisibility(View.GONE);
			}

			mPullListView.onPullDownRefreshComplete();
			mPullListView.onPullUpRefreshComplete();
			setLastUpdateTime();
		}
	}

	@Override
	protected void LoadErro(Object erro) {
		// TODO Auto-generated method stub
		super.LoadErro(erro);
	}

	@Override
	protected void LoadData() {
		super.LoadData();
		CPControl.GetTrafficPackageLogResult(LIMIT, 0,package_type, listener);
	}

	// 拉取更多数据
	CPControl.GetResultListCallback listener_loadmore = new CPControl.GetResultListCallback() {

		@Override
		public void onFinished(Object o) {
			Message msg = new Message();
			msg.what = 0;
			msg.obj = o;
			mHandler.sendMessage(msg);

		}

		@Override
		public void onErro(Object o) {
			Message msg = new Message();
			msg.what = 1;
			msg.obj = o;
			mHandler.sendMessage(msg);

		}
	};

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				TrafficPackagePurchaseLogListInfo mMore = (TrafficPackagePurchaseLogListInfo) msg.obj;
				if (mInfoLists != null
						&& mMore.getmTrafficPackagePurchaseLogInfoList().size() > 0) {
					if (TextUtils.equals(
							flagPackagePurchaseLogInfo.getBuy_time(), mMore
									.getmTrafficPackagePurchaseLogInfoList()
									.get(0).getBuy_time())) {
						mMore.getmTrafficPackagePurchaseLogInfoList().remove(0);
					}
					mInfoLists.addAllTrafficPackagePurchaseLogList(mMore
							.getmTrafficPackagePurchaseLogInfoList());
					if (mMore.getmTrafficPackagePurchaseLogInfoList().size() == 0) {
						mPullListView.setPullLoadEnabled(false);
					}
				}

				LoadSuccess(mInfoLists);
				break;

			case 1:
				LoadErro(msg.obj);
				break;
			}
		}

	};



	@Override
	protected void onResume() {
		super.onResume();
	}

}
