package com.carlt.doride.ui.activity.carstate;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.Inputtips.InputtipsListener;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.carlt.doride.R;
import com.carlt.doride.base.BaseActivity;
import com.carlt.doride.ui.adapter.AddressListAdapter;
import com.carlt.doride.ui.view.UUToast;
import com.carlt.doride.utils.map.InputTipTask;
import com.carlt.doride.utils.map.RouteTask;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SearchAddrActivity extends BaseActivity implements OnClickListener, TextWatcher, OnItemClickListener {

    private ImageView back;// 返回键
    private EditText  mEdtAddr;// 地址输入框
    private ImageView mImgCha;// 清除框
    private TextView  mTxtSearch;// 搜索
    private ListView  mList;// 搜索结果
    private View      mLoadingLay;

    private AddressListAdapter mAdapter;
    private RouteTask          mRouteTask;
    private AMapLocation       current;
    private String             cityCode;
    private TextView           errTxt;
    private View               noNetworkView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchaddr);
        String latlng = getIntent().getStringExtra("latlng");
        if (TextUtils.isEmpty(latlng)) {
            UUToast.showUUToast(this, "为获取到当前位置");
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
        mEdtAddr.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == event.KEYCODE_ENTER) {
                    searchLoc(mEdtAddr.getText().toString() + "", "");
                }
                return false;
            }
        });
    }

    private void init() {
        mList = (ListView) findViewById(R.id.searchaddr_list);
        mLoadingLay = findViewById(R.id.search_loading_lay);
        noNetworkView = findViewById(R.id.no_network_lay_main);
        Button retryBtn = findViewById(R.id.error_txt_retry);
        errTxt = findViewById(R.id.error_txt_des_sub);
        mAdapter = new AddressListAdapter(this, null, current);
        mList.setAdapter(mAdapter);
        mList.setOnItemClickListener(this);
        mRouteTask = RouteTask.getInstance(getApplicationContext());
        retryBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                searchLoc(mEdtAddr.getText().toString() + "", "");
            }
        });
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
     * @param addr
     * @param city
     */
    public void searchLoc(final String addr, String city) {
        if (TextUtils.isEmpty(addr)) {
            UUToast.showUUToast(this, "请输入地点");
            return;
        }
        showLoading();
        InputtipsQuery inputquery = new InputtipsQuery(addr, city);
        inputquery.setCityLimit(true);
        Inputtips inputTips = new Inputtips(SearchAddrActivity.this, inputquery);
        inputTips.setInputtipsListener(new InputtipsListener() {
            @Override
            public void onGetInputtips(List<Tip> arg0, int arg1) {
                if (arg1 == 1000) {
                    if (arg0.size() > 0) {
                        dissMissLoading();
                        Collections.sort(arg0, new Comparator<Tip>() {
                            @Override
                            public int compare(Tip o1, Tip o2) {
                                LatLonPoint llp1 = o1.getPoint();
                                LatLonPoint llp2 = o2.getPoint();
                                float[] ff1 = new float[4];
                                float[] ff2 = new float[4];
                                AMapLocation.distanceBetween(current.getLatitude(), current.getLongitude(), llp1.getLatitude(), llp1.getLongitude(), ff1);
                                AMapLocation.distanceBetween(current.getLatitude(), current.getLongitude(), llp2.getLatitude(), llp2.getLongitude(), ff2);
                                float dis1 = ff1[0] / 1000;
                                float dis2 = ff2[0] / 1000;
                                if (dis1 > dis2) {
                                    return 1;
                                }
                                if (dis1 == dis2) {
                                    return 0;
                                }
                                return -1;
                            }
                        });
                        if (mAdapter == null) {
                            mAdapter = new AddressListAdapter(SearchAddrActivity.this, arg0, current);
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
                    if (arg1 == 1804 || arg1 == 1806) {
                        loadError("网络不稳定，请稍后再试");
                    } else {
                        loadError("没有结果");
                    }
                }
            }
        });
        inputTips.requestInputtipsAsyn();
    }

    void showLoading() {
        noNetworkView.setVisibility(View.GONE);
        mLoadingLay.setVisibility(View.VISIBLE);
        mList.setVisibility(View.GONE);
    }

    void dissMissLoading() {
        noNetworkView.setVisibility(View.GONE);
        mList.setVisibility(View.VISIBLE);
        mLoadingLay.setVisibility(View.GONE);
    }

    void loadError(String s) {
        mList.setVisibility(View.GONE);
        noNetworkView.setVisibility(View.VISIBLE);
        errTxt.setVisibility(View.VISIBLE);
        errTxt.setText(s);
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
