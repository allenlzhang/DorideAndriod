
package com.carlt.sesame.ui.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.sesame.data.car.CarModeInfo;
import com.carlt.sesame.ui.activity.usercenter.SelectCarTypeView;
import com.carlt.sesame.ui.activity.usercenter.SelectCarTypeView.OnCarTypeItemClick;
import com.carlt.sesame.ui.adapter.CarTypeAdapter;

import java.util.ArrayList;

/**
 * 车型索引View
 * 
 * @author daisy
 */
public class IndexListView extends LinearLayout {

    private ListView mListView;

    private LinearLayout mLinearLayout;//

    private LayoutInflater mInflater;

    private TextView mTextView;// 悬浮TextView

    private CarTypeAdapter mAdapter;

    private ArrayList<CarModeInfo> mArrayList;

    private int mScrollState; // ListView滚动状态

    private Context mContext;

    public IndexListView(Context context) {
        super(context);
        mContext = context;
    }

    public IndexListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mInflater.inflate(R.layout.layout_index_car_type, this, true);
        mListView = (ListView)findViewById(R.id.layout_index_car_type_list);
        mLinearLayout = (LinearLayout)findViewById(R.id.layout_index_car_type_layout1);
        mTextView = (TextView)findViewById(R.id.layout_index_car_type_txt);

        initAlphabet();
    }

    private String[] alphabets = {
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q",
            "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
    };

    // 初始化右侧的字母表
    private void initAlphabet() {

        for (int i = 0; i < alphabets.length; i++) {
            View itemView = mInflater.inflate(R.layout.layout_index_alphabet, null);
            itemView.setTag(i);
            itemView.setOnTouchListener(mOnTouchListener);
            TextView textView = (TextView)itemView.findViewById(R.id.layout_index_alphabet_txt);
            textView.setText(alphabets[i]);
            mLinearLayout.addView(itemView);
        }
    }

    private OnScrollListener mScrollListener = new OnScrollListener() {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            mScrollState = scrollState;

            if (mScrollState == OnScrollListener.SCROLL_STATE_IDLE) {
                mHandler.sendEmptyMessageDelayed(0, 1500);
            } else {
                mTextView.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                int totalItemCount) {
            mTextView.setText(String.valueOf(
                    mArrayList.get(firstVisibleItem).getTitle_py().charAt(0)).toUpperCase());

        }
    };

    private OnTouchListener mOnTouchListener = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                int tag = (Integer)v.getTag();
                for (int i = 0; i < mArrayList.size(); i++) {
                    String s1 = "" + mArrayList.get(i).getTitle_py().charAt(0);
                    String s2 = alphabets[tag];
                    if (s1.equalsIgnoreCase(s2)) {
                        mListView.setSelection(i);
                        break;
                    }
                }
            }
            return false;
        }
    };

    Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            mTextView.setVisibility(View.INVISIBLE);
        };
    };

    private OnItemClickListener mItemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (mOnCarTypeItemClick != null) {
                TextView mTextName = (TextView)view
                        .findViewById(R.id.list_item_index_car_type_txt2);
                CarModeInfo mInfo = (CarModeInfo)mTextName.getTag();
                mOnCarTypeItemClick.onClick(mInfo, SelectCarTypeView.TYPE_MODEL);
            }

        }
    };

    /**
     * 车型数据
     * 
     * @param dataList
     */
    public void setDataList(ArrayList<CarModeInfo> dataList) {
        mArrayList = dataList;
        mAdapter = new CarTypeAdapter(mContext, dataList);
        mListView.setAdapter(mAdapter);
        mListView.setOnScrollListener(mScrollListener);
    }
    
    public void reFreshCarlogo(){
        if(mAdapter!=null){
            mAdapter.notifyDataSetChanged();
        }
    }

    private OnCarTypeItemClick mOnCarTypeItemClick;

    public void setOnCarTypeItemClick(OnCarTypeItemClick onCarTypeItemClick) {
        this.mOnCarTypeItemClick = onCarTypeItemClick;
        mListView.setOnItemClickListener(mItemClickListener);
    }

}
