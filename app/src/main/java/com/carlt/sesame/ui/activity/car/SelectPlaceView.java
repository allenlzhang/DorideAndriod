
package com.carlt.sesame.ui.activity.car;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.carlt.sesame.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultList2Callback;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.data.car.CityInfo;
import com.carlt.sesame.data.car.ProvinceInfo;
import com.carlt.sesame.ui.adapter.QueryIllegalSelectCityAdapter;
import com.carlt.sesame.ui.adapter.QueryIllegalSelectProvinceAdapter;
import com.carlt.sesame.ui.view.SelectView;

import java.util.ArrayList;

public class SelectPlaceView extends SelectView {

	private View mView;// 第一级列表view

	private GridView mGridView1; // 热门城市

	private GridView mGridView2; // 省份

	private ListView mGridView3;// 城市

	private QueryIllegalSelectCityAdapter mAdapterCity;

	private QueryIllegalSelectProvinceAdapter mAdapterProvince;

	private View mLoadingLayout;

	private TextView mLoadingTextView;

	private View mLoadingBar;

	private ArrayList<ProvinceInfo> mListProvince = new ArrayList<ProvinceInfo>();

	private ArrayList<CityInfo> mListCity = new ArrayList<CityInfo>();

	private Context mContext;

	private OnPlaceNameClick mOnPlaceNameClick;

	private int type;// 列表类型

	public final static int TYPE_PROVINCE = 1;// 省份

	public final static int TYPE_CITY = 2;// 城市

	private ProvinceInfo mProvinceInfo;

	public SelectPlaceView(Context mContext, OnPlaceNameClick onPlaceNameClick) {
		super(mContext);

		this.mContext = mContext;
		mOnPlaceNameClick = onPlaceNameClick;
		setOnBackClickListner();

		type = TYPE_PROVINCE;

		View child = LayoutInflater.from(mContext).inflate(R.layout.layout_query_illegal_place, null);
		init(child);
		setTitle("选择城市");

		mView = child.findViewById(R.id.layout_query_illegal_place_layout);
		mGridView1 = (GridView) child.findViewById(R.id.layout_query_illegal_place_grid1);
		mGridView2 = (GridView) child.findViewById(R.id.layout_query_illegal_place_grid2);
		mGridView3 = (ListView) child.findViewById(R.id.layout_query_illegal_place_grid3);

		mGridView1.setOnItemClickListener(mItemClickListener);
		mGridView2.setOnItemClickListener(mItemClickListener);
		mGridView3.setOnItemClickListener(mItemClickListener);

		// Loading控件
		mLoadingLayout = child.findViewById(R.id.loading_activity_mainlayout);
		mLoadingTextView = (TextView) child.findViewById(R.id.loading_activity_loading_text);
		mLoadingBar = child.findViewById(R.id.loading_activity_loading_bar);

		if (mContext instanceof Activity) {

		}

	}

	@Override
	protected void onPopCreat() {

	}

	@Override
	protected void onBackClick() {
		if (type == TYPE_PROVINCE) {
			dissmiss();
		} else if (type == TYPE_CITY) {
			mGridView3.setVisibility(View.GONE);
			mView.setVisibility(View.VISIBLE);
			setTitle("选择城市");
			type = TYPE_PROVINCE;
			pullDataFirst(TYPE_PROVINCE);
		}

	}

	// 拉取一级列表信息
	private void loadDataFirst() {
		// 车易行
		// CPControl.GetProvinceListResult(listener1);
		// 聚合
		CPControl.GetProvinceListResultJUHE(listener1);
		showLoading();
	}

	// 拉取二级列表信息
	private void loadDataSecond() {
		if (mProvinceInfo != null) {
			String provinceCode = mProvinceInfo.getCode();
			if (provinceCode != null && provinceCode.length() > 0) {
				// 车易行
				// CPControl.GetCityListResult(provinceCode, listener2);
				// 聚合
				CPControl.GetCityListResultJUHE(provinceCode, listener2);
			}
		}
		showLoading();
	}

	private OnItemClickListener mItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			TextView textView = (TextView) view.findViewById(R.id.grid_item_illegal_select_txt);
			if (parent.getId() == R.id.layout_query_illegal_place_grid1) {
				mOnPlaceNameClick.onClick(textView.getTag(), TYPE_CITY);
			} else if (parent.getId() == R.id.layout_query_illegal_place_grid2) {
				mOnPlaceNameClick.onClick(textView.getTag(), TYPE_PROVINCE);
			} else if (parent.getId() == R.id.layout_query_illegal_place_grid3) {
				mOnPlaceNameClick.onClick(textView.getTag(), TYPE_CITY);
			}
		}
	};

	private BaseResponseInfo mBaseResponseInfo;

	private GetResultList2Callback listener1 = new GetResultList2Callback() {
		// o1 省 o2市
		@Override
		public void onFinished(Object o1, Object o2, Object o3) {
			mListProvince = (ArrayList<ProvinceInfo>) o1;
			mListCity = (ArrayList<CityInfo>) o2;

			mHandler.sendEmptyMessage(0);
		}

		@Override
		public void onErro(Object o) {
			mBaseResponseInfo = (BaseResponseInfo) o;

			mHandler.sendEmptyMessage(1);

		}
	};

	private GetResultListCallback listener2 = new GetResultListCallback() {

		@Override
		public void onFinished(Object o) {
			mListCity = (ArrayList<CityInfo>) o;
			mHandler.sendEmptyMessage(2);

		}

		@Override
		public void onErro(Object o) {
			mBaseResponseInfo = (BaseResponseInfo) o;
			mHandler.sendEmptyMessage(3);

		}
	};

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case 0:
				// 拉取热门城市数据成功
				if (mAdapterCity == null) {
					mAdapterCity = new QueryIllegalSelectCityAdapter(mContext);
				}
				mAdapterCity.setmDataList(mListCity);
				mAdapterCity.notifyDataSetChanged();
				mGridView1.setAdapter(mAdapterCity);
				// 拉取省份数据成功
				if (mAdapterProvince == null) {
					mAdapterProvince = new QueryIllegalSelectProvinceAdapter(mContext, mListProvince);
				}
				mGridView2.setAdapter(mAdapterProvince);
				setTitle("选择城市");
				dissmissLoading();
				break;

			case 1:
				// 拉取一级列表数据失败
				erroLoading();
				break;

			case 2:
				if (mAdapterCity == null) {
					mAdapterCity = new QueryIllegalSelectCityAdapter(mContext);
				}
				mAdapterCity.setmDataList(mListCity);
				mAdapterCity.notifyDataSetChanged();
				mGridView3.setAdapter(mAdapterCity);
				if (mProvinceInfo != null && mProvinceInfo.getName() != null) {
					setTitle(mProvinceInfo.getName());
				}
				dissmissLoading();
				break;
			case 3:
				// 拉取二级列表数据失败
				erroLoading();
				break;
			}

		}
	};

	private void showLoading() {
		mLoadingBar.setVisibility(View.VISIBLE);
		mLoadingTextView.setText("等待中");
		mLoadingLayout.setVisibility(View.VISIBLE);
	}

	private void dissmissLoading() {
		mLoadingLayout.setVisibility(View.GONE);
	}

	private void erroLoading() {
		mLoadingTextView.setText("获取数据失败:" + mBaseResponseInfo.getInfo());
		mLoadingBar.setVisibility(View.GONE);
	}

	public void pullDataFirst(int type) {
		this.type = type;
		mGridView3.setVisibility(View.GONE);
		mView.setVisibility(View.VISIBLE);
		loadDataFirst();
	}

	public void pullDataSecond(ProvinceInfo mProvinceInfo, int type) {
		this.mProvinceInfo = mProvinceInfo;
		this.type = type;
		mView.setVisibility(View.GONE);
		mGridView3.setVisibility(View.VISIBLE);
		loadDataSecond();
	}

	public interface OnPlaceNameClick {
		void onClick(Object info, int type);
	}

	@Override
	protected void onBackPressed() {
		onBackClick();
	}
}
