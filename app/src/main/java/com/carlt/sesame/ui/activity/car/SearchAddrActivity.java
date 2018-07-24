package com.carlt.sesame.ui.activity.car;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.Inputtips.InputtipsListener;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.carlt.sesame.R;
import com.carlt.sesame.ui.activity.base.BaseActivity;
import com.carlt.sesame.ui.activity.car.map.InputTipTask;
import com.carlt.sesame.ui.activity.car.map.RouteTask;
import com.carlt.sesame.ui.adapter.AddressListAdapter;
import com.carlt.sesame.utility.UUToast;

import java.util.List;

public class SearchAddrActivity extends BaseActivity implements OnClickListener, TextWatcher, OnItemClickListener {

	private ImageView back;// 返回键
	private EditText mEdtAddr;// 地址输入框
	private ImageView mImgCha;// 清除框
	private TextView mTxtSearch;// 搜索
	private ListView mList;// 搜索结果
	private View mLoadingLay;
	private TextView mErrorTxt;

	private AddressListAdapter mAdapter;
	private RouteTask mRouteTask;
	private AMapLocation current;
	private String cityCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_searchaddr);
		String latlng = getIntent().getStringExtra("latlng");
		if (TextUtils.isEmpty(latlng)) {
			UUToast.showUUToast(context, "为获取到当前位置");
			return;
		}
		cityCode = getIntent().getStringExtra("cityCode");
		current = new AMapLocation("");
		current.setLatitude(Double.parseDouble(latlng.split(",")[0]));
		current.setLongitude(Double.parseDouble(latlng.split(",")[1]));

		initTitle();
		init();
		mEdtAddr.requestFocus();
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
	}

	private void initTitle() {
		back = (ImageView) findViewById(R.id.searchaddr_img_back);
		mEdtAddr = (EditText) findViewById(R.id.searchaddr_edt_addr);
		mImgCha = (ImageView) findViewById(R.id.searchaddr_img_cha);
		mTxtSearch = (TextView) findViewById(R.id.searchaddr_txt_search);

		back.setOnClickListener(this);
		// mEdtAddr.addTextChangedListener(this);
		mImgCha.setOnClickListener(this);
		mTxtSearch.setOnClickListener(this);

		mEdtAddr.setFocusable(true);
		mEdtAddr.setFocusableInTouchMode(true);
	}

	private void init() {
		mList = (ListView) findViewById(R.id.searchaddr_list);
		mLoadingLay = findViewById(R.id.search_loading_lay);
		mErrorTxt = (TextView) findViewById(R.id.search_error_text);
		mAdapter = new AddressListAdapter(context, null, current);
		mList.setAdapter(mAdapter);
		mList.setOnItemClickListener(this);
		mRouteTask = RouteTask.getInstance(getApplicationContext());

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mEdtAddr.requestFocus();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
		Intent intent = new Intent();
		Tip tip = (Tip) mAdapter.getItem(position);
		intent.putExtra("latlng", tip.getPoint().getLatitude() + "," + tip.getPoint().getLongitude());
		intent.putExtra("name", tip.getName());
		intent.putExtra("address", tip.getAddress());
		setResult(1, intent);
		finish();
	}

	@Override
	public void afterTextChanged(Editable arg0) {

	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (RouteTask.getInstance(getApplicationContext()).getStartPoint() == null) {
			Toast.makeText(getApplicationContext(), "检查网络，Key等问题", Toast.LENGTH_SHORT).show();
			return;
		}
		InputTipTask.getInstance(mAdapter).searchTips(getApplicationContext(), s.toString(), RouteTask.getInstance(getApplicationContext()).getStartPoint().city);

	}

	/**
	 * 传入 用户输入地址，城市，返回可能的地址列表
	 * 
	 * @param addr
	 * @param city
	 */
	public void searchLoc(final String addr, String city) {
		showLoading();
		InputtipsQuery inputquery = new InputtipsQuery(addr, city);
		inputquery.setCityLimit(true);
		Inputtips inputTips = new Inputtips(context, inputquery);
		inputTips.setInputtipsListener(new InputtipsListener() {

			@Override
			public void onGetInputtips(List<Tip> arg0, int arg1) {
				if (arg1 == 1000) {
					if (arg0.size() > 0) {
						dissMissLoading();
						if (mAdapter == null) {
							mAdapter = new AddressListAdapter(context, arg0, current);
							mAdapter.setText(addr);
							mList.setAdapter(mAdapter);
							mAdapter.notifyDataSetChanged();
						} else {
							mAdapter.setmList(arg0);
							mAdapter.setText(addr);
							mAdapter.notifyDataSetChanged();
						}
					} else {
						loadError("没有结果");
					}
				} else {
					if(arg1 == 1804||arg1 ==1806){
						loadError("网络不稳定，请稍后再试");
					}else{
						loadError("没有结果");
					}
				}
			}
		});
		inputTips.requestInputtipsAsyn();
	}

	void showLoading() {
		mErrorTxt.setVisibility(View.GONE);
		mLoadingLay.setVisibility(View.VISIBLE);
		mList.setVisibility(View.GONE);
	}

	void dissMissLoading() {
		mList.setVisibility(View.VISIBLE);
		mErrorTxt.setVisibility(View.GONE);
		mLoadingLay.setVisibility(View.GONE);
	}

	void loadError(String s) {
		mList.setVisibility(View.GONE);
		mErrorTxt.setVisibility(View.VISIBLE);
		mErrorTxt.setText(s);
		mLoadingLay.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.searchaddr_img_back:
			// 返回键
			finish();
			break;
		case R.id.searchaddr_img_cha:
			// 清除
			mEdtAddr.setText("");
			if (mAdapter != null) {
				mAdapter.clear();
			}
			break;
		case R.id.searchaddr_txt_search:
			// 搜索
			//searchLoc(mEdtAddr.getText().toString() + "", cityCode);
			searchLoc(mEdtAddr.getText().toString() + "", "");
			break;
		}
	}

}
