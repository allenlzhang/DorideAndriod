
package com.carlt.sesame.ui.activity.car;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.doride.data.car.CarNowStatusInfo;
import com.carlt.doride.http.retrofitnet.BaseMvcObserver;
import com.carlt.doride.http.retrofitnet.model.GetCarInfo;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.ui.activity.base.LoadingActivityWithTitle;
import com.carlt.sesame.ui.adapter.CarConditionListAdapter;
import com.carlt.sesame.ui.pull.PullToRefreshBase;
import com.carlt.sesame.ui.pull.PullToRefreshBase.OnRefreshListener;
import com.carlt.sesame.ui.pull.PullToRefreshListView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 座驾-实时车况页面
 * 
 * @author daisy
 */
public class CarConditionActivity extends LoadingActivityWithTitle {
	private ImageView back;// 头部返回键

	private TextView title;// 标题文字

	private TextView txtRight;// 头部右侧文字

	private ImageView imgRight;// 头部右侧图标

	private ImageView mImageViewSecretary;// 车秘书头像

	private TextView mTextViewSecretary;// 提醒消息

	private ListView mListView;// 安防提醒列表

	private PullToRefreshListView mRefreshListView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.seame_activity_car_condition);
		setTitleView(R.layout.head_back);

		initTitle();
		initSubTitle();
		init();
		LoadData();

	}

	private void initTitle() {
		back = (ImageView) findViewById(R.id.head_back_img1);
		title = (TextView) findViewById(R.id.head_back_txt1);
		txtRight = (TextView) findViewById(R.id.head_back_txt2);
		imgRight = (ImageView) findViewById(R.id.head_back_img2);

		back.setImageResource(R.drawable.arrow_back);
		title.setText("实时车况");
		txtRight.setVisibility(View.GONE);
		imgRight.setVisibility(View.VISIBLE);
		imgRight.setImageResource(R.drawable.icon_refresh);

		back.setOnClickListener(v -> finish());

		imgRight.setOnClickListener(v -> {
			// 刷新
			LoadData();
		});
	}

	private void initSubTitle() {
		mImageViewSecretary = (ImageView) findViewById(R.id.layout_sub_head_img);
		mTextViewSecretary = (TextView) findViewById(R.id.layout_sub_head_txt);
	}

	private void init() {
		mRefreshListView = (PullToRefreshListView) findViewById(R.id.activity_car_condition_list);
		mListView = mRefreshListView.getRefreshableView();
		mListView.setVerticalScrollBarEnabled(false);
		mRefreshListView.setLastUpdatedLabel("------");
		mRefreshListView.setPullRefreshEnabled(true);
		mRefreshListView.setPullLoadEnabled(false);

		mRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				loadOnrefresh();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void setLastUpdateTime() {
		SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
		String text = mDateFormat.format(new Date(System.currentTimeMillis()));
		mRefreshListView.setLastUpdatedLabel(text);
	}

	public void loadOnrefresh() {
//		CPControl.GetCarStatuListResult(mLoadListener);
		getCarStatuList(true);
	}

	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
//			mRefreshListView.onPullDownRefreshComplete();
//			setLastUpdateTime();
//			if (msg.what == 0) {
//				ArrayList<CarStatuInfo> mArrayList = (ArrayList<CarStatuInfo>) msg.obj;
//				mAdapter = new CarConditionListAdapter(CarConditionActivity.this, mArrayList);
//				mListView.setAdapter(mAdapter);
//			} else if (msg.what == 1) {
//				BaseResponseInfo mInfo = (BaseResponseInfo) msg.obj;
//				String info = "";
//				if (mInfo == null || mInfo.getInfo() == null || mInfo.getInfo().length() < 1) {
//					info = "加载失败";
//				}
//				info = mInfo.getInfo();
//				UUToast.showUUToast(CarConditionActivity.this, info);
//			}
		};
	};

	GetResultListCallback mLoadListener = new GetResultListCallback() {

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

	private CarConditionListAdapter mAdapter;

	@Override
	protected void LoadSuccess(Object data) {
		mAdapter = new CarConditionListAdapter(CarConditionActivity.this, ((CarNowStatusInfo)data).getList());
		mListView.setAdapter(mAdapter);

		mTextViewSecretary.setText("已经成功获取车况数据");

		super.LoadSuccess(data);
	}

	@Override
	protected void LoadErro(Object erro) {
		mTextViewSecretary.setText("获取车况数据失败");
		super.LoadErro(erro);
	}

	@Override
	protected void LoadData() {
		super.LoadData();
//		CPControl.GetCarStatuListResult(listener);
		getCarStatuList(false);
	}

	private void getCarStatuList(boolean isLoadMore){
		Map<String,Object> param = new HashMap<>();
		Map<String,Object> commParam = new HashMap<>();
		commParam.put("carId", GetCarInfo.getInstance().id);
		commParam.put("deviceID",GetCarInfo.getInstance().deviceNum);
		param.put("base",commParam);
		addDisposable(mApiService.QueryCurrentData(param), new BaseMvcObserver<CarNowStatusInfo>() {
			@Override
			public void onSuccess(CarNowStatusInfo result) {
				if (isLoadMore){
					mRefreshListView.onPullDownRefreshComplete();
					setLastUpdateTime();
				}
				if (result.err!=null){
					mTextViewSecretary.setText("获取车况数据失败");
					BaseResponseInfo info = new BaseResponseInfo();
					info.setInfo("暂时获取不到车辆状态");
					LoadErro(info);
				}else{
					LoadSuccess(result);
				}
			}

			@Override
			public void onError(String msg) {
				if (isLoadMore){
					mRefreshListView.onPullDownRefreshComplete();
					setLastUpdateTime();
				}

				LoadErro(null);
			}
		});
	}
}
