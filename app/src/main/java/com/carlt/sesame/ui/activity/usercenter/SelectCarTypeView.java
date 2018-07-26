
package com.carlt.sesame.ui.activity.usercenter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.LoginInfo;
import com.carlt.sesame.data.car.CarModeInfo;
import com.carlt.sesame.ui.adapter.CarTypeAdapter;
import com.carlt.sesame.ui.view.IndexListView;
import com.carlt.sesame.ui.view.SelectView;

import java.util.ArrayList;

/**
 * 选择车型页面
 * 
 * @author daisy
 */
public class SelectCarTypeView extends SelectView {

    private View mLoadingLayout;

    private TextView mLoadingTextView;

    private View mLoadingBar;

    private IndexListView mIndexListView;

    private ListView mListView;

    private View mViewData;

    private ArrayList<CarModeInfo> mArrayList1;

    private ArrayList<CarModeInfo> mArrayList2;

    private ArrayList<CarModeInfo> mArrayList3;

    private CarTypeAdapter mAdapter;

    private OnCarTypeItemClick mOnCarTypeItemClick;

    private CarModeInfo mCarModeInfo = null;

    private Context mContext;

    private int type = TYPE_MODEL;// 列表类型(初始值为1)

    public final static int TYPE_MODEL = 1;// 车型

    public final static int TYPE_SERIES = 2;// 车系

    public final static int TYPE_CAR = 3;// 车款

    public final static String BRANDID = "112";// 大迈芝麻固定品牌id

    public final static String OPTIONID = "2550";// 大迈芝麻固定车系id

    public final static String TITLE = "芝麻 E30";// 大迈芝麻固定车系标题

    private String titleSecond;// 二级列表的title

    public SelectCarTypeView(Context mContext, OnCarTypeItemClick onCarTypeItemClick) {
        super(mContext);
        View child = LayoutInflater.from(mContext).inflate(R.layout.layout_select_car_type, null);
        init(child);
        setTitle("选择爱车品牌");
        setOnBackClickListner();

        this.mContext = mContext;
        mOnCarTypeItemClick = onCarTypeItemClick;

        mIndexListView = (IndexListView)child.findViewById(R.id.layout_select_car_type_list1);
        mListView = (ListView)child.findViewById(R.id.layout_select_car_type_list2);

        mViewData = child.findViewById(R.id.select_car_layout_data);

        mIndexListView.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.GONE);
        // Loading控件
        mLoadingLayout = child.findViewById(R.id.loading_activity_mainlayout);
        mLoadingTextView = (TextView)child.findViewById(R.id.loading_activity_loading_text);
        mLoadingBar = child.findViewById(R.id.loading_activity_loading_bar);
    }

    @Override
    protected void onPopCreat() {

    }

    @Override
    protected void onBackClick() {
        switch (type) {
            case TYPE_MODEL:
                dissmiss();
                break;

            case TYPE_SERIES:
                if (mArrayList1 != null) {
                    mListView.setVisibility(View.GONE);
                    mIndexListView.setVisibility(View.VISIBLE);
                    mIndexListView.setDataList(mArrayList1);
                }
                setTitle("选择爱车品牌");
                type = TYPE_MODEL;
                break;
            case TYPE_CAR:
                if (titleSecond == null || titleSecond.length() < 0) {
                    dissmiss();
                } else {
                    if (mArrayList2 != null && mAdapter != null) {
                        mAdapter.setmDataList(mArrayList2);
                        mAdapter.notifyDataSetChanged();
                    }
                    setTitle(titleSecond);
                    type = TYPE_SERIES;
                }
                break;
        }

    }

    /**
     * 获取一级列表数据
     */
    private void loadDataFirst() {
        CPControl.GetDealerModelListResult(1, null, listener1);
        showLoading();
    }

    /**
     * 获取二级列表数据
     */
    private void loadDataSecond() {
        if (mCarModeInfo != null) {
            CPControl.GetDealerModelListV1Result(mCarModeInfo.getId(), listener2);
        }
        showLoading();
    }

    /**
     * 获取三级列表数据
     */
    private void loadDataThird() {
        if (mCarModeInfo != null) {
            String is_before;
            boolean isInstallorder=LoginInfo.isInstallorder();
            
            if (isInstallorder) {
                is_before = "1";
            } else {
                is_before = "0";
            }
            //测试用数据
            //is_before="1";
            //测试结束
            CPControl.GetDealerModelListV2Result(mCarModeInfo.getId(), is_before, listener3);
        }
        showLoading();
    }

    private GetResultListCallback listener1 = new GetResultListCallback() {

        @Override
        public void onFinished(Object o) {
            mArrayList1 = (ArrayList<CarModeInfo>)o;
            Message msg = new Message();
            msg.what = 0;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onErro(Object o) {
            mHandler.sendEmptyMessage(1);
        }
    };

    private GetResultListCallback listener2 = new GetResultListCallback() {

        @Override
        public void onFinished(Object o) {
            mArrayList2 = (ArrayList<CarModeInfo>)o;
            Message msg = new Message();
            msg.what = 2;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onErro(Object o) {
            mHandler.sendEmptyMessage(3);
        }
    };

    private GetResultListCallback listener3 = new GetResultListCallback() {

        @Override
        public void onFinished(Object o) {
            mArrayList3 = (ArrayList<CarModeInfo>)o;
            Message msg = new Message();
            msg.what = 4;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onErro(Object o) {
            mHandler.sendEmptyMessage(5);
        }
    };

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 0:
                    // 拉取一级列表数据成功
                    mIndexListView.setDataList(mArrayList1);
                    mIndexListView.setOnCarTypeItemClick(mOnCarTypeItemClick);
                    setTitle("选择爱车品牌");
                    dissmissLoading();
                    break;

                case 1:
                    // 拉取一级列表数据失败
                    erroLoading();
                    break;

                case 2:
                    // 拉取二级列表数据成功
                    String carlogo1 = mCarModeInfo.getCarlogo();
                    int size1 = mArrayList2.size();
                    for (int i = 0; i < size1; i++) {
                        CarModeInfo carModeInfo = mArrayList2.get(i);
                        carModeInfo.setCarlogo(carlogo1);
                    }
                    if (mAdapter == null) {
                        mAdapter = new CarTypeAdapter(mContext, mArrayList2);
                        mListView.setAdapter(mAdapter);
                    } else {
                        mAdapter.setmDataList(mArrayList2);
                        mAdapter.notifyDataSetChanged();
                    }
                    titleSecond = mCarModeInfo.getTitle();
                    setTitle(titleSecond);
                    dissmissLoading();
                    break;

                case 3:
                    // 拉取二级列表数据失败
                    erroLoading();
                    break;

                case 4:
                    // 拉取三级列表数据成功
                    String carlogo2 = mCarModeInfo.getCarlogo();
                    int size2 = mArrayList3.size();
                    for (int i = 0; i < size2; i++) {
                        CarModeInfo carModeInfo = mArrayList3.get(i);
                        carModeInfo.setCarlogo(carlogo2);
                    }
                    if (mAdapter == null) {
                        mAdapter = new CarTypeAdapter(mContext, mArrayList3);
                        mListView.setAdapter(mAdapter);
                    } else {
                        mAdapter.setmDataList(mArrayList3);
                        mAdapter.notifyDataSetChanged();
                    }
                    String titleThird = mCarModeInfo.getTitle();
                    setTitle(titleThird);
                    dissmissLoading();
                    break;

                case 5:
                    // 拉取三级列表数据失败
                    erroLoading();
                    break;
            }

        }
    };

    private void showLoading() {
        mViewData.setVisibility(View.GONE);
        mLoadingLayout.setVisibility(View.VISIBLE);
        mLoadingBar.setVisibility(View.VISIBLE);
        mLoadingTextView.setText("等待中");
    }

    private void dissmissLoading() {
        mLoadingLayout.setVisibility(View.GONE);
        mViewData.setVisibility(View.VISIBLE);
    }

    private void erroLoading() {
        mViewData.setVisibility(View.GONE);
        mLoadingLayout.setVisibility(View.VISIBLE);
        mLoadingTextView.setText("获取数据失败");
        mLoadingBar.setVisibility(View.GONE);
    }

    private OnItemClickListener mItemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (parent.getId()) {
                case R.id.layout_select_car_type_list2:
                    TextView mTextView = (TextView)view
                            .findViewById(R.id.list_item_index_car_type_txt2);
                    // 二、三级列表的list
                    mCarModeInfo = (CarModeInfo)mTextView.getTag();
                    boolean isSubTitle = mCarModeInfo.isTitleSub();
                    if (!isSubTitle) {
                        mOnCarTypeItemClick.onClick(mCarModeInfo, type);
                        if (type == TYPE_SERIES) {
                            pullDataThird(mCarModeInfo, TYPE_CAR);
                        } else if (type == TYPE_CAR) {
                            boolean flag = mCarModeInfo.isTitleSub();
                            if (flag) {

                            } else {
                                // 让第三级选车型popwindow消失
                                // dissmiss();
                            }

                        }
                    }
                    break;

            }

        }
    };

    /**
     * 拉取一级列表数据
     */
    public void pullDataFirst(int type) {
        this.type = type;
        mListView.setVisibility(View.GONE);
        mIndexListView.setVisibility(View.VISIBLE);

        loadDataFirst();
        mListView.setOnItemClickListener(mItemClickListener);
    }

    /**
     * 拉取二级列表数据
     */
    public void pullDataSecond(CarModeInfo carModeInfo, int type) {
        mCarModeInfo = carModeInfo;
        this.type = type;
        mIndexListView.setVisibility(View.GONE);
        mListView.setVisibility(View.VISIBLE);
        loadDataSecond();
        mListView.setOnItemClickListener(mItemClickListener);
    }

    /**
     * 拉取三级列表数据
     */
    public void pullDataThird(CarModeInfo carModeInfo, int type) {
        mCarModeInfo = carModeInfo;
        this.type = type;
        mIndexListView.setVisibility(View.GONE);
        mListView.setVisibility(View.VISIBLE);
        loadDataThird();
        mListView.setOnItemClickListener(mItemClickListener);
    }

    public interface OnCarTypeItemClick {
        /**
         * @param carModeInfo 车型数据结构
         * @param type 类型：一级列表-车型，二级列表-车系，三级列表-车款
         */
        void onClick(CarModeInfo carModeInfo, int type);
    };

    public void refreshCarlogo() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
        if (mIndexListView != null) {
            mIndexListView.reFreshCarlogo();
        }
    }
}
