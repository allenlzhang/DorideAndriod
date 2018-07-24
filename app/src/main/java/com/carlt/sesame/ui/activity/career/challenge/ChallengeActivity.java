
package com.carlt.sesame.ui.activity.career.challenge;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.sesame.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.data.LoginInfo;
import com.carlt.sesame.data.career.ChallengeInfo;
import com.carlt.sesame.map.ChallengeMapActivity;
import com.carlt.sesame.ui.activity.base.LoadingActivityWithTitle;
import com.carlt.sesame.ui.activity.career.ChallengeHistoryBestActivity;
import com.carlt.sesame.ui.adapter.ChallengePassesAdapterNew;
import com.carlt.sesame.ui.adapter.ChallengePassesAdapterNew.OnChallengeItemListener;

import java.util.ArrayList;

/**
 * 挑战关卡（新页面）-九宫格
 * 
 * @author Administrator
 */
public class ChallengeActivity extends LoadingActivityWithTitle {
    private ImageView back;// 头部返回键

    private TextView title;// 标题文字

    private TextView txtRight;// 头部右侧文字

    private ImageView mImgSecretary;// 车秘书图标

    private TextView mTxtTotal;// 关卡总数

    private TextView mTxtCup;// 获得奖杯数

    private TextView mTxtUnlock;// 未解锁关卡

    private GridView mGrid;// 关卡列表

    private ChallengePassesAdapterNew mAdapter;

    private ArrayList<ChallengeInfo> mDataList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_grid);
        setTitleView(R.layout.head_back);

        initTitle();
        init();
    }

    private void initTitle() {
        back = (ImageView)findViewById(R.id.head_back_img1);
        title = (TextView)findViewById(R.id.head_back_txt1);
        txtRight = (TextView)findViewById(R.id.head_back_txt2);

        back.setImageResource(R.drawable.arrow_back);
        title.setText("挑战模式");
        txtRight.setVisibility(View.GONE);

        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void init() {
        mImgSecretary = (ImageView)findViewById(R.id.challenge_img_secretary);

        mTxtTotal = (TextView)findViewById(R.id.challenge_txt_total);
        mTxtCup = (TextView)findViewById(R.id.challenge_txt_cup);
        mTxtUnlock = (TextView)findViewById(R.id.challenge_txt_unlock);

        mGrid = (GridView)findViewById(R.id.challenge_grid);
    }

    @Override
    protected void LoadSuccess(Object data) {
        mDataList = (ArrayList<ChallengeInfo>)data;
        if (mDataList != null) {
            int total = mDataList.size();

            SpannableStringBuilder mSpannable = new SpannableStringBuilder("共");
            mSpannable.append(total + "");
            mSpannable.append("关");
            mSpannable.setSpan(new ForegroundColorSpan(Color.rgb(62, 192, 234)), 1,
                    mSpannable.length() - 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            mTxtTotal.setText(mSpannable);

            int finished = 0;
            int unlock = 0;
            for (int i = 0; i < total; i++) {
                ChallengeInfo mChallengeInfo = mDataList.get(i);
                String state = mChallengeInfo.getStatus();
                if (state.equals(ChallengeInfo.STATUS_FINISHED)) {
                    // 已完成
                    finished++;
                } else if (state.equals(ChallengeInfo.STATUS_UNLOCKED)) {
                    // 未解锁
                    unlock++;
                }
            }

            mSpannable = new SpannableStringBuilder("已获得");
            mSpannable.append(finished + "");
            mSpannable.append("座");
            mSpannable.setSpan(new ForegroundColorSpan(Color.rgb(241, 61, 109)), 3,
                    mSpannable.length() - 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            mTxtCup.setText(mSpannable);

            mSpannable = new SpannableStringBuilder("还有");
            mSpannable.append(unlock + "");
            mSpannable.append("关未解锁");
            mSpannable.setSpan(new ForegroundColorSpan(Color.rgb(128, 128, 128)), 2,
                    mSpannable.length() - 4, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            mTxtUnlock.setText(mSpannable);

            int remainder = total % 3;
            if (remainder == 1) {
                ChallengeInfo mInfo1 = new ChallengeInfo();
                mInfo1.setNull(true);
                mDataList.add(mInfo1);
                ChallengeInfo mInfo2 = new ChallengeInfo();
                mInfo2.setNull(true);
                mDataList.add(mInfo2);
            } else if (remainder == 2) {
                ChallengeInfo mInfo = new ChallengeInfo();
                mInfo.setNull(true);
                mDataList.add(mInfo);
            }
            if (mAdapter == null) {
                mAdapter = new ChallengePassesAdapterNew(ChallengeActivity.this, mItemListener);
            }
            mAdapter.setmDataList(mDataList);
            mGrid.setAdapter(mAdapter);
            mGrid.setOnItemClickListener(null);
        }

        int resId = LoginInfo.getSecretaryImg();
        mImgSecretary.setImageResource(resId);
        super.LoadSuccess(data);
    }

    private OnChallengeItemListener mItemListener = new OnChallengeItemListener() {

        @Override
        public void onItemClick(ChallengeInfo info) {
            ChallengeInfo mChallengeInfo = info;
            if (mChallengeInfo != null) {
                String status = mChallengeInfo.getStatus();
                if (status.equals(ChallengeInfo.STATUS_FINISHED)) {
                    // 1.已完成挑战，跳转至挑战结果（最佳纪录）
                    Intent mIntent1 = new Intent(ChallengeActivity.this,
                            ChallengeHistoryBestActivity.class);
                    mIntent1.putExtra("ChallengeInfo", mChallengeInfo);
                    startActivity(mIntent1);

                } else if (status.equals(ChallengeInfo.STATUS_UNFINISHED)) {
                    // 2.未完成挑战，跳转至挑战页面
                    Intent mIntent2 = new Intent(ChallengeActivity.this, ChallengeMapActivity.class);
                    mIntent2.putExtra("ChallengeInfo", mChallengeInfo);
                    startActivity(mIntent2);
                }
            }
        }
    };

    @Override
    protected void LoadErro(Object erro) {
        // TODO Auto-generated method stub
        super.LoadErro(erro);
    }

    @Override
    protected void LoadData() {
        super.LoadData();
        // 调用获取挑战信息接口-todo
        CPControl.GetChallengeListResult(listener);
    }

    @Override
    protected void onStart() {
        LoadData();
        super.onStart();
    }

}
