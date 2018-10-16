
package com.carlt.doride.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.doride.data.home.InformationMessageInfo;

import java.util.ArrayList;


/**
 * 秘书提醒Adapter
 *
 * @author daisy
 */
public class InformationCentreTipsAdapter extends BaseAdapter {
    private LayoutInflater mInflater;

    private Activity mActivity;

    private ArrayList<InformationMessageInfo> mList;

//    private AsyncImageLoader mAsyncImageLoader = AsyncImageLoader.getInstance();

    private OnBottomClickListner mBottomClickListner;

    public void setmList(ArrayList<InformationMessageInfo> mList) {
        this.mList = mList;
    }

    public InformationCentreTipsAdapter(Activity context, ArrayList<InformationMessageInfo> list,
                                        OnBottomClickListner bottomClickListner) {
        mActivity = context;
        mInflater = LayoutInflater.from(mActivity);
        mBottomClickListner = bottomClickListner;

        mList = list;
    }

    @Override
    public int getCount() {
        if (mList != null)
            return mList.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    private int i;

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {
        Holder mHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_secretary, null);
            mHolder = initHolder(convertView);
            convertView.setTag(mHolder);

        } else {
            mHolder = (Holder) convertView.getTag();
        }

        final InformationMessageInfo mInfo = mList.get(position);

//        if (mInfo.isIstop()) {
//            mHolder.mImageViewTop.setVisibility(View.VISIBLE);
//        } else {
//            mHolder.mImageViewTop.setVisibility(View.GONE);
//        }
        if (mInfo.getCreatedate() != null) {
            mHolder.mTextView3.setText(mInfo.getCreatedate());
        }

        int c1 = mInfo.getClass1();
        int c2 = mInfo.getClass2();

        String minTitle = "";
        switch (c1) {
            case InformationMessageInfo.C1_T1:
                // 用车提醒

                mHolder.mImgDelete.setVisibility(View.VISIBLE);
                mHolder.mView2.setVisibility(View.VISIBLE);
                mHolder.mViewTable.setVisibility(View.GONE);

                mHolder.mTextView4.setVisibility(View.VISIBLE);
                mHolder.mTextView5.setVisibility(View.GONE);

                switch (c2) {
                    case InformationMessageInfo.C1_T1_T4:
                        minTitle = "激活";
                        // mHolder.mTextView4.setText("爱车体检");
                        mHolder.mTextView4.setVisibility(View.GONE);
                        break;
                    case InformationMessageInfo.C1_T1_T5:
                        minTitle = "失连";
                        mHolder.mTextView4.setVisibility(View.GONE);
                        break;
                }

                break;

            case InformationMessageInfo.C1_T4:
                // 行车信息
                mHolder.mImgDelete.setVisibility(View.VISIBLE);

                mHolder.mView2.setVisibility(View.VISIBLE);
                mHolder.mTextView5.setVisibility(View.GONE);

                switch (c2) {
                    case InformationMessageInfo.C1_T4_T1:
                        minTitle = "日报";
                        mHolder.mTextView4.setVisibility(View.VISIBLE);
                        mHolder.mTextView4.setText("查看行车报告");

                        mHolder.mViewTable.setVisibility(View.VISIBLE);
                        if (mInfo.getMiles() != null) {
                            mHolder.mTextViewTable1.setText(mInfo.getMiles());
                        }
                        if (mInfo.getFuel() != null) {
                            mHolder.mTextViewTable2.setText(mInfo.getFuel());
                        }
                        // if (mInfo.getPoint() != null) {
                        // mHolder.mTextViewTable3.setText(mInfo.getPoint());
                        // }
                        if (mInfo.getMaxspeed() != null) {
                            mHolder.mTextViewTable3.setText(mInfo.getMaxspeed());
                        }
                        if (mInfo.getAvgfuel() != null) {
                            mHolder.mTextViewTable4.setText(mInfo.getAvgfuel());
                        }
                        if (mInfo.getSumtime() != null) {
                            mHolder.mTextViewTable5.setText(mInfo.getSumtime());
                        }

                        // if (mInfo.getMaxspeed() != null) {
                        // mHolder.mTextViewTable6.setText(mInfo.getMaxspeed());
                        // }
//                        mHolder.mTextViewTable6.setText("");
                        break;
                    case InformationMessageInfo.C1_T4_T3:
                        minTitle = "月报";
                        mHolder.mTextView4.setVisibility(View.VISIBLE);
                        mHolder.mTextView4.setText("查看行车报告");

                        mHolder.mViewTable.setVisibility(View.VISIBLE);
                        if (mInfo.getMiles() != null) {
                            mHolder.mTextViewTable1.setText(mInfo.getMiles());
                        }
                        if (mInfo.getFuel() != null) {
                            mHolder.mTextViewTable2.setText(mInfo.getFuel());
                        }
                        // if (mInfo.getPoint() != null) {
                        // mHolder.mTextViewTable3.setText(mInfo.getPoint());
                        // }
                        if (mInfo.getMaxspeed() != null) {
                            mHolder.mTextViewTable3.setText(mInfo.getMaxspeed());
                        }
                        if (mInfo.getAvgfuel() != null) {
                            mHolder.mTextViewTable4.setText(mInfo.getAvgfuel());
                        }
                        if (mInfo.getSumtime() != null) {
                            mHolder.mTextViewTable5.setText(mInfo.getSumtime());
                        }
                        // if (mInfo.getMaxspeed() != null) {
                        // mHolder.mTextViewTable6.setText(mInfo.getMaxspeed());
                        // }
//                        mHolder.mTextViewTable6.setText("");
                        break;
                }

                break;

            case InformationMessageInfo.C1_T6:
                // 养护贴士
                mHolder.mImgDelete.setVisibility(View.VISIBLE);
                mHolder.mView2.setVisibility(View.VISIBLE);
                mHolder.mViewTable.setVisibility(View.GONE);

                mHolder.mTextView4.setVisibility(View.GONE);
                mHolder.mTextView5.setVisibility(View.GONE);

                switch (c2) {
                    case InformationMessageInfo.C1_T6_T1:
                        minTitle = "保养";
                        mHolder.mTextView4.setText("");
                        break;
                }

                break;
            case InformationMessageInfo.C1_T7:
                //车主关怀
                mHolder.mImgDelete.setVisibility(View.GONE);
                mHolder.mView2.setVisibility(View.VISIBLE);
                mHolder.mViewTable.setVisibility(View.GONE);

                mHolder.mTextView4.setVisibility(View.GONE);
                mHolder.mTextView5.setVisibility(View.GONE);
//                switch (c2){
//                    case InformationMessageInfo.
//                }
                break;
            case InformationMessageInfo.C1_T2:
                mHolder.mImgDelete.setVisibility(View.VISIBLE);
                mHolder.mView2.setVisibility(View.VISIBLE);
                mHolder.mViewTable.setVisibility(View.GONE);

                mHolder.mTextView4.setVisibility(View.GONE);
                mHolder.mTextView5.setVisibility(View.GONE);
                break;
        }

        StringBuffer mStingBuffer = new StringBuffer("");
        if (!minTitle.equals("")) {
            mStingBuffer.append("【");
            mStingBuffer.append(minTitle);
            mStingBuffer.append("】");
        }

        if (mInfo.getTitle() != null && mInfo.getTitle().length() > 0) {
            mStingBuffer.append(mInfo.getTitle());
        }
        mHolder.mTextView1.setText(mStingBuffer.toString());
        if (mInfo.getContent() != null) {
            mHolder.mTextView2.setText(mInfo.getContent());
        }
//        if (mInfo.getDetial_flag() == InformationMessageInfo.FLAG_NONE) {
//            mHolder.mTextBtn.setVisibility(View.GONE);
//            if (mInfo.getContent() != null) {
//                mHolder.mTextView2.setText(mInfo.getContent());
//            }
//        } else if (mInfo.getDetial_flag() == InformationMessageInfo.FLAG_REFERENCE) {
//            if (mInfo.getContentReference() != null) {
//                mHolder.mTextView2.setText(mInfo.getContentReference());
//            }
//            mHolder.mTextBtn.setVisibility(View.VISIBLE);
//            mHolder.mTextBtn.setText("详情");
//            mHolder.mTextBtn
//                    .setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down, 0);
//        } else if (mInfo.getDetial_flag() == InformationMessageInfo.FLAG_ALL) {
//            if (mInfo.getContent() != null) {
//                mHolder.mTextView2.setText(mInfo.getContent());
//            }
//            mHolder.mTextBtn.setVisibility(View.VISIBLE);
//            mHolder.mTextBtn.setText("收起");
//            mHolder.mTextBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_up,
//                    0);
//        }

        View.OnClickListener mClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {

//                    case R.id.list_item_secretary_btn:
//                        // 展开按钮
//                        if (mInfo.getDetial_flag() == InformationMessageInfo.FLAG_REFERENCE) {
//                            mInfo.setDetial_flag(InformationMessageInfo.FLAG_ALL);
//                        } else if (mInfo.getDetial_flag() == InformationMessageInfo.FLAG_ALL) {
//                            mInfo.setDetial_flag(InformationMessageInfo.FLAG_REFERENCE);
//                        }
//                        notifyDataSetChanged();
//
//                        break;
                    case R.id.list_item_secretary_img_delete:
                        // 删除图标
                        mBottomClickListner.onDelete(mInfo, position);
                        break;
                    case R.id.list_item_secretary_txt4:
                        // 右侧不带箭头按钮
                        mBottomClickListner.onAction(mInfo);
                        break;
                    case R.id.list_item_secretary_txt5:
                        // 右侧带箭头按钮
                        mBottomClickListner.onAction(mInfo);
                        break;

                }

            }
        };

        mHolder.mView1.setOnClickListener(null);
        mHolder.mView2.setOnClickListener(null);
//        mHolder.mTextBtn.setOnClickListener(mClickListener);
        mHolder.mImgDelete.setOnClickListener(mClickListener);
        mHolder.mTextView4.setOnClickListener(mClickListener);
        mHolder.mTextView5.setOnClickListener(mClickListener);

        return convertView;
    }

    private Holder initHolder(View convertView) {
        Holder holder = new Holder();
        holder.mTextView1 = (TextView) convertView.findViewById(R.id.list_item_secretary_txt1);
        holder.mTextView2 = (TextView) convertView.findViewById(R.id.list_item_secretary_txt2);
        holder.mTextView3 = (TextView) convertView.findViewById(R.id.list_item_secretary_txt3);
        holder.mTextView4 = (TextView) convertView.findViewById(R.id.list_item_secretary_txt4);
        holder.mTextView5 = (TextView) convertView.findViewById(R.id.list_item_secretary_txt5);

        holder.mTextViewTable1 = (TextView) convertView
                .findViewById(R.id.layout_secretary_report_table_txt1);
        holder.mTextViewTable2 = (TextView) convertView
                .findViewById(R.id.layout_secretary_report_table_txt2);
        holder.mTextViewTable3 = (TextView) convertView
                .findViewById(R.id.layout_secretary_report_table_txt3);
        holder.mTextViewTable4 = (TextView) convertView
                .findViewById(R.id.layout_secretary_report_table_txt4);
        holder.mTextViewTable5 = (TextView) convertView
                .findViewById(R.id.layout_secretary_report_table_txt5);
        holder.mTextViewTable6 = (TextView) convertView
                .findViewById(R.id.layout_secretary_report_table_txt6);

//        holder.mImageViewTop = (ImageView)convertView
//                .findViewById(R.id.list_item_secretary_img_top);
        holder.mImgDelete = (ImageView) convertView
                .findViewById(R.id.list_item_secretary_img_delete);

//        holder.mTextBtn = (TextView)convertView.findViewById(R.id.list_item_secretary_btn);

        holder.mView1 = convertView.findViewById(R.id.list_item_secretary_layout1);
        holder.mView2 = convertView.findViewById(R.id.list_item_secretary_layout2);
        holder.mViewTable = convertView.findViewById(R.id.list_item_secretary_table);

        return holder;
    }

    class Holder {
        private TextView mTextView1;// 提醒标题

        private TextView mTextView2;// 提醒描述

        private TextView mTextView3;// 提醒时间

        private TextView mTextView4;// 提醒类别

        private TextView mTextView5;// 提醒类别(不带右边箭头)

        private TextView mTextViewTable1;// 表格里程

        private TextView mTextViewTable2;// 表格油耗

        private TextView mTextViewTable3;// 表格得分

        private TextView mTextViewTable4;// 表格平均油耗

        private TextView mTextViewTable5;// 表格行车时间

        private TextView mTextViewTable6;// 表格最高速度

        private ImageView mImageViewTop; // 置顶图标

        private ImageView mImgDelete;// 删除图标

//        private TextView mTextBtn;// 展开按钮

        private View mView1;// item上半部分白底layout

        private View mView2;// item底部layout

        private View mViewTable;// 行车报告的表格布局
    }

    public interface OnBottomClickListner {
        void onDelete(InformationMessageInfo info, int position);// 删除

        void onAction(InformationMessageInfo info);// 底部右侧按钮
    }
}
