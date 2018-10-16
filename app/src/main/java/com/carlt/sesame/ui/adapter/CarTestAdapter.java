package com.carlt.sesame.ui.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.sesame.data.car.CheckFaultInfo;

import java.util.ArrayList;

/**
 * 爱车体检Adapter
 * 
 * @author daisy
 */
public class CarTestAdapter extends BaseAdapter {
	private LayoutInflater mInflater;

	private int listSizeP;// 动态大小

	private int listSizeC;

	private int listSizeB;

	private int listSizeU;

	private int listSizeAllP;// 总大小

	private int listSizeAllC;

	private int listSizeAllB;

	private int listSizeAllU;

	private int titleState1 = TITLE_STATE3;// 0 检测中 1正确 2错误 3检测未开始

	private int titleState2 = TITLE_STATE3;

	private int titleState3 = TITLE_STATE3;

	private int titleState4 = TITLE_STATE3;

	private boolean isTestFinishAll = false;// 所有检测是否完成

	public final static int TITLE_STATE0 = 0;

	public final static int TITLE_STATE1 = 1;

	public final static int TITLE_STATE2 = 2;

	public final static int TITLE_STATE3 = 3;

	private ArrayList<CheckFaultInfo> listP;

	private ArrayList<CheckFaultInfo> listC;

	private ArrayList<CheckFaultInfo> listB;

	private ArrayList<CheckFaultInfo> listU;

	public void setTitleState1(int titleState1) {
		this.titleState1 = titleState1;
	}

	public void setTitleState2(int titleState2) {
		this.titleState2 = titleState2;
	}

	public void setTitleState3(int titleState3) {
		this.titleState3 = titleState3;
	}

	public void setTitleState4(int titleState4) {
		this.titleState4 = titleState4;
	}

	// 设置listP的数据
	public void AddListP(CheckFaultInfo info) {
		listP.add(info);
	}

	public void RemoveListP() {
		listP.remove(0);
	}

	public void setStateListP(int index) {

		if (index < listP.size()) {
			listP.get(index).setFlag(CheckFaultInfo.STATE1);
		}
	}

	public void setTestNumListP(int testNum) {
		// 设置已检测个数
		listSizeAllP = testNum;
	}

	public void clearListP() {
		listP.clear();
	}

	// 设置listC的数据
	public void AddListC(CheckFaultInfo info) {
		listC.add(info);
	}

	public void RemoveListC() {
		listC.remove(0);
	}

	public void setStateListC(int index) {

		if (index < listC.size()) {
			listC.get(index).setFlag(CheckFaultInfo.STATE1);
		}
	}

	public void setTestNumListC(int testNum) {
		// 设置已检测个数
		listSizeAllC = testNum;
	}

	public void clearListC() {
		listC.clear();
	}

	// 设置listB的数据
	public void AddListB(CheckFaultInfo info) {
		listB.add(info);
	}

	public void RemoveListB() {
		listB.remove(0);
	}

	public void setStateListB(int index) {

		if (index < listB.size()) {
			listB.get(index).setFlag(CheckFaultInfo.STATE1);
		}
	}

	public void setTestNumListB(int testNum) {
		// 设置已检测个数
		listSizeAllB = testNum;
	}

	public void clearListB() {
		listB.clear();
	}

	// 设置listU的数据
	public void AddListU(CheckFaultInfo info) {
		listU.add(info);
	}

	public void RemoveListU() {
		listU.remove(0);
	}

	public void setStateListU(int index) {
		if (index < listU.size()) {
			listU.get(index).setFlag(CheckFaultInfo.STATE1);
		}

	}

	public void setTestNumListU(int testNum) {
		// 设置已检测个数
		listSizeAllU = testNum;
	}

	public void clearListU() {
		listU.clear();
	}

	// 全部检测是否完成
	public void setTestFinishAll(boolean isTestFinishAll) {
		this.isTestFinishAll = isTestFinishAll;
	}

	private Resources mResources;

	public CarTestAdapter(Context context, ArrayList<CheckFaultInfo> listP) {
		mInflater = LayoutInflater.from(context);

		this.listP = listP;
		listC = new ArrayList<CheckFaultInfo>();
		listB = new ArrayList<CheckFaultInfo>();
		listU = new ArrayList<CheckFaultInfo>();

		mResources = context.getResources();
	}

	public int getCount() {
		listSizeP = listP.size();
		listSizeC = listC.size();
		listSizeB = listB.size();
		listSizeU = listU.size();

		return listSizeP + listSizeC + listSizeB + listSizeU + 4;
	}

	@Override
	public Object getItem(int position) {

		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		Holder mHolder;
		if (convertView == null) {
			mHolder = new Holder();
			convertView = mInflater.inflate(R.layout.list_item_car_test, null);
			convertView.setTag(mHolder);
			mHolder.mView1 = convertView
					.findViewById(R.id.list_item_car_test_relative1);
			mHolder.mView2 = convertView
					.findViewById(R.id.list_item_car_test_relative2);
			mHolder.mView3 = convertView
					.findViewById(R.id.list_item_car_test_line);

			mHolder.mTextViewTitle1 = (TextView) convertView
					.findViewById(R.id.list_item_car_test_title_txt1);
			mHolder.mTextViewTitle2 = (TextView) convertView
					.findViewById(R.id.list_item_car_test_title_txt2);
			mHolder.mTextViewTitle3 = (TextView) convertView
					.findViewById(R.id.list_item_car_test_title_txt3);
			mHolder.mImageViewTitle = (ImageView) convertView
					.findViewById(R.id.list_item_car_test_title_img);
			mHolder.mProgressBarTitle = (ProgressBar) convertView
					.findViewById(R.id.list_item_car_test_title_progressbar);
			mHolder.mProgressBarTitleOut = convertView
					.findViewById(R.id.list_item_car_test_title_progressbar_out);

			mHolder.mTextView = (TextView) convertView
					.findViewById(R.id.list_item_car_test_txt);
			mHolder.mImageView = (ImageView) convertView
					.findViewById(R.id.list_item_car_test_img);
			mHolder.mProgressBar = (ProgressBar) convertView
					.findViewById(R.id.list_item_car_test_progressbar);

		} else {
			mHolder = (Holder) convertView.getTag();
		}
		if (position == 0) {
			mHolder.mView1.setVisibility(View.VISIBLE);
			mHolder.mView2.setVisibility(View.GONE);
			mHolder.mView3.setVisibility(View.GONE);

			if (listSizeP > 0) {
				mHolder.mView1
						.setBackgroundResource(R.drawable.car_test_item_bg_white);
			} else {
				mHolder.mView1
						.setBackgroundResource(R.drawable.car_test_item_bg);
			}

			mHolder.mTextViewTitle1.setText("P");
			mHolder.mTextViewTitle2.setText("动力系统");
			mHolder.mProgressBarTitleOut
					.setBackgroundResource(R.drawable.test_loading_bg_p);
			mHolder.mTextViewTitle3.setText("已检测：" + listSizeAllP + "项");

			if (titleState1 == TITLE_STATE0) {
				mHolder.mProgressBarTitleOut.setVisibility(View.VISIBLE);
				mHolder.mImageViewTitle.setVisibility(View.INVISIBLE);
				mHolder.mTextViewTitle3.setTextColor(mResources
						.getColor(R.color.text_color_gray1));
			} else if (titleState1 == TITLE_STATE1) {
				mHolder.mProgressBarTitleOut.setVisibility(View.GONE);
				mHolder.mImageViewTitle.setVisibility(View.VISIBLE);
				mHolder.mImageViewTitle
						.setImageResource(R.drawable.car_test_no_problem_large);
				mHolder.mTextViewTitle3.setTextColor(mResources
						.getColor(R.color.text_color_gray1));

			} else if (titleState1 == TITLE_STATE2) {
				mHolder.mProgressBarTitleOut.setVisibility(View.GONE);
				mHolder.mImageViewTitle.setVisibility(View.VISIBLE);
				mHolder.mImageViewTitle
						.setImageResource(R.drawable.car_test_problem_large);
				mHolder.mTextViewTitle3.setTextColor(mResources
						.getColor(R.color.text_color_yellow));

				if (isTestFinishAll) {
					mHolder.mTextViewTitle3.setText("发现故障：" + listSizeAllP
							+ "项");
				} else {
					mHolder.mTextViewTitle3
							.setText("已检测：" + listSizeAllP + "项");
				}
			} else if (titleState1 == TITLE_STATE3) {
				mHolder.mProgressBarTitleOut.setVisibility(View.GONE);
				mHolder.mImageViewTitle.setVisibility(View.INVISIBLE);
				mHolder.mTextViewTitle3.setTextColor(mResources
						.getColor(R.color.text_color_gray1));
			}
		} else if (position > 0 && position < listSizeP + 1) {

			mHolder.mView1.setVisibility(View.GONE);
			mHolder.mView2.setVisibility(View.VISIBLE);
			mHolder.mView3.setVisibility(View.VISIBLE);

			CheckFaultInfo mCheckFaultInfo = listP.get(position - 1);
			mHolder.mTextView.setText(mCheckFaultInfo.getCode() + ":"
					+ mCheckFaultInfo.getCn());

			if (mCheckFaultInfo.getFlag() == CheckFaultInfo.STATE0) {
				mHolder.mProgressBar.setVisibility(View.VISIBLE);
				mHolder.mImageView.setVisibility(View.GONE);
			} else if (mCheckFaultInfo.getFlag() == CheckFaultInfo.STATE1) {
				mHolder.mProgressBar.setVisibility(View.GONE);
				mHolder.mImageView.setVisibility(View.VISIBLE);
				mHolder.mImageView
						.setImageResource(R.drawable.car_test_no_problem);
			} else if (mCheckFaultInfo.getFlag() == CheckFaultInfo.STATE2) {
				mHolder.mProgressBar.setVisibility(View.GONE);
				mHolder.mImageView.setVisibility(View.VISIBLE);
				mHolder.mImageView
						.setImageResource(R.drawable.car_test_problem);
			}

		} else if (position == listSizeP + 1) {
			mHolder.mView1.setVisibility(View.VISIBLE);
			mHolder.mView2.setVisibility(View.GONE);
			mHolder.mView3.setVisibility(View.GONE);

			if (listC.size() > 0) {
				mHolder.mView1
						.setBackgroundResource(R.drawable.car_test_item_bg_white);
			} else {
				mHolder.mView1
						.setBackgroundResource(R.drawable.car_test_item_bg);
			}

			mHolder.mTextViewTitle1.setText("C");
			mHolder.mTextViewTitle2.setText("底盘系统");
			mHolder.mProgressBarTitleOut
					.setBackgroundResource(R.drawable.test_loading_bg_c);
			mHolder.mTextViewTitle3.setText("已检测：" + listSizeAllC + "项");

			if (titleState2 == TITLE_STATE0) {
				mHolder.mProgressBarTitleOut.setVisibility(View.VISIBLE);
				mHolder.mImageViewTitle.setVisibility(View.INVISIBLE);
				mHolder.mTextViewTitle3.setTextColor(mResources
						.getColor(R.color.text_color_gray1));
			} else if (titleState2 == TITLE_STATE1) {
				mHolder.mProgressBarTitleOut.setVisibility(View.GONE);
				mHolder.mImageViewTitle.setVisibility(View.VISIBLE);
				mHolder.mImageViewTitle
						.setImageResource(R.drawable.car_test_no_problem_large);
				mHolder.mTextViewTitle3.setTextColor(mResources
						.getColor(R.color.text_color_gray1));
			} else if (titleState2 == TITLE_STATE2) {
				mHolder.mProgressBarTitleOut.setVisibility(View.GONE);
				mHolder.mImageViewTitle.setVisibility(View.VISIBLE);
				mHolder.mImageViewTitle
						.setImageResource(R.drawable.car_test_problem_large);
				mHolder.mTextViewTitle3.setTextColor(mResources
						.getColor(R.color.text_color_yellow));
				if (isTestFinishAll) {
					mHolder.mTextViewTitle3.setText("发现故障：" + listSizeAllC
							+ "项");
				} else {
					mHolder.mTextViewTitle3
							.setText("已检测：" + listSizeAllC + "项");
				}
			} else if (titleState2 == TITLE_STATE3) {
				mHolder.mProgressBarTitleOut.setVisibility(View.GONE);
				mHolder.mImageViewTitle.setVisibility(View.INVISIBLE);
				mHolder.mTextViewTitle3.setTextColor(mResources
						.getColor(R.color.text_color_gray1));
			}

		} else if (position > (listSizeP + 1)
				&& position < (listSizeP + listSizeC + 2)) {

			mHolder.mView1.setVisibility(View.GONE);
			mHolder.mView2.setVisibility(View.VISIBLE);
			mHolder.mView3.setVisibility(View.VISIBLE);

			CheckFaultInfo mCheckFaultInfo = listC
					.get(position - listSizeP - 2);
			mHolder.mTextView.setText(mCheckFaultInfo.getCode() + ":"
					+ mCheckFaultInfo.getCn());

			if (mCheckFaultInfo.getFlag() == CheckFaultInfo.STATE0) {
				mHolder.mProgressBar.setVisibility(View.VISIBLE);
				mHolder.mImageView.setVisibility(View.GONE);
			} else if (mCheckFaultInfo.getFlag() == CheckFaultInfo.STATE1) {
				mHolder.mProgressBar.setVisibility(View.GONE);
				mHolder.mImageView.setVisibility(View.VISIBLE);
				mHolder.mImageView
						.setImageResource(R.drawable.car_test_no_problem);
			} else if (mCheckFaultInfo.getFlag() == CheckFaultInfo.STATE2) {
				mHolder.mProgressBar.setVisibility(View.GONE);
				mHolder.mImageView.setVisibility(View.VISIBLE);
				mHolder.mImageView
						.setImageResource(R.drawable.car_test_problem);
			}

		} else if (position == listSizeP + listSizeC + 2) {
			mHolder.mView1.setVisibility(View.VISIBLE);
			mHolder.mView2.setVisibility(View.GONE);
			mHolder.mView3.setVisibility(View.GONE);

			if (listB.size() > 0) {
				mHolder.mView1
						.setBackgroundResource(R.drawable.car_test_item_bg_white);
			} else {
				mHolder.mView1
						.setBackgroundResource(R.drawable.car_test_item_bg);
			}

			mHolder.mTextViewTitle1.setText("B");
			mHolder.mTextViewTitle2.setText("车身系统");
			mHolder.mProgressBarTitleOut
					.setBackgroundResource(R.drawable.test_loading_bg_b);

			mHolder.mTextViewTitle3.setText("已检测：" + listSizeAllB + "项");

			if (titleState3 == TITLE_STATE0) {
				mHolder.mProgressBarTitleOut.setVisibility(View.VISIBLE);
				mHolder.mImageViewTitle.setVisibility(View.INVISIBLE);
				mHolder.mTextViewTitle3.setTextColor(mResources
						.getColor(R.color.text_color_gray1));

			} else if (titleState3 == TITLE_STATE1) {
				mHolder.mProgressBarTitleOut.setVisibility(View.GONE);
				mHolder.mImageViewTitle.setVisibility(View.VISIBLE);
				mHolder.mImageViewTitle
						.setImageResource(R.drawable.car_test_no_problem_large);
				mHolder.mTextViewTitle3.setTextColor(mResources
						.getColor(R.color.text_color_gray1));

			} else if (titleState3 == TITLE_STATE2) {
				mHolder.mProgressBarTitleOut.setVisibility(View.GONE);
				mHolder.mImageViewTitle.setVisibility(View.VISIBLE);
				mHolder.mImageViewTitle
						.setImageResource(R.drawable.car_test_problem_large);
				mHolder.mTextViewTitle3.setTextColor(mResources
						.getColor(R.color.text_color_yellow));
				if (isTestFinishAll) {
					mHolder.mTextViewTitle3.setText("发现故障：" + listSizeAllB
							+ "项");
				} else {
					mHolder.mTextViewTitle3
							.setText("已检测：" + listSizeAllB + "项");
				}
			} else if (titleState3 == TITLE_STATE3) {
				mHolder.mProgressBarTitleOut.setVisibility(View.GONE);
				mHolder.mImageViewTitle.setVisibility(View.INVISIBLE);
				mHolder.mTextViewTitle3.setTextColor(mResources
						.getColor(R.color.text_color_gray1));
			}

		} else if (position > (listSizeP + listSizeC + 2)
				&& position < (listSizeP + listSizeC + listSizeB + 3)) {
			mHolder.mView1.setVisibility(View.GONE);
			mHolder.mView2.setVisibility(View.VISIBLE);
			mHolder.mView3.setVisibility(View.VISIBLE);

			CheckFaultInfo mCheckFaultInfo = listB.get(position - listSizeP
					- listSizeC - 3);
			mHolder.mTextView.setText(mCheckFaultInfo.getCode() + ":"
					+ mCheckFaultInfo.getCn());

			if (mCheckFaultInfo.getFlag() == CheckFaultInfo.STATE0) {
				mHolder.mProgressBar.setVisibility(View.VISIBLE);
				mHolder.mImageView.setVisibility(View.GONE);
			} else if (mCheckFaultInfo.getFlag() == CheckFaultInfo.STATE1) {
				mHolder.mProgressBar.setVisibility(View.GONE);
				mHolder.mImageView.setVisibility(View.VISIBLE);
				mHolder.mImageView
						.setImageResource(R.drawable.car_test_no_problem);
			} else if (mCheckFaultInfo.getFlag() == CheckFaultInfo.STATE2) {
				mHolder.mProgressBar.setVisibility(View.GONE);
				mHolder.mImageView.setVisibility(View.VISIBLE);
				mHolder.mImageView
						.setImageResource(R.drawable.car_test_problem);
			}

		} else if (position == listSizeP + listSizeC + listSizeB + 3) {
			mHolder.mView1.setVisibility(View.VISIBLE);
			mHolder.mView2.setVisibility(View.GONE);
			mHolder.mView3.setVisibility(View.GONE);

			if (listU.size() > 0) {
				mHolder.mView1
						.setBackgroundResource(R.drawable.car_test_item_bg_white);
			} else {
				mHolder.mView1
						.setBackgroundResource(R.drawable.car_test_item_bg);
			}

			mHolder.mTextViewTitle1.setText("U");
			mHolder.mTextViewTitle2.setText("通信系统");
			mHolder.mProgressBarTitleOut
					.setBackgroundResource(R.drawable.test_loading_bg_u);

			mHolder.mTextViewTitle3.setText("已检测：" + listSizeAllU + "项");

			if (titleState4 == TITLE_STATE0) {
				mHolder.mProgressBarTitleOut.setVisibility(View.VISIBLE);
				mHolder.mImageViewTitle.setVisibility(View.INVISIBLE);
				mHolder.mTextViewTitle3.setTextColor(mResources
						.getColor(R.color.text_color_gray1));
			} else if (titleState4 == TITLE_STATE1) {
				mHolder.mProgressBarTitleOut.setVisibility(View.GONE);
				mHolder.mImageViewTitle.setVisibility(View.VISIBLE);
				mHolder.mImageViewTitle
						.setImageResource(R.drawable.car_test_no_problem_large);
				mHolder.mTextViewTitle3.setTextColor(mResources
						.getColor(R.color.text_color_gray1));
			} else if (titleState4 == TITLE_STATE2) {
				mHolder.mProgressBarTitleOut.setVisibility(View.GONE);
				mHolder.mImageViewTitle.setVisibility(View.VISIBLE);
				mHolder.mImageViewTitle
						.setImageResource(R.drawable.car_test_problem_large);
				mHolder.mTextViewTitle3.setTextColor(mResources
						.getColor(R.color.text_color_yellow));

				if (isTestFinishAll) {
					mHolder.mTextViewTitle3.setText("发现故障：" + listSizeAllU
							+ "项");
				} else {
					mHolder.mTextViewTitle3
							.setText("已检测：" + listSizeAllU + "项");
				}
			} else if (titleState4 == TITLE_STATE3) {
				mHolder.mProgressBarTitleOut.setVisibility(View.GONE);
				mHolder.mImageViewTitle.setVisibility(View.INVISIBLE);
				mHolder.mTextViewTitle3.setTextColor(mResources
						.getColor(R.color.text_color_gray1));
			}

		} else if (position > listSizeP + listSizeC + listSizeB + 3
				&& position < listSizeP + listSizeC + listSizeB + listSizeU + 4) {
			mHolder.mView1.setVisibility(View.GONE);
			mHolder.mView2.setVisibility(View.VISIBLE);
			mHolder.mView3.setVisibility(View.VISIBLE);

			CheckFaultInfo mCheckFaultInfo = listU.get(position - listSizeP
					- listSizeC - listSizeB - 4);
			mHolder.mTextView.setText(mCheckFaultInfo.getCode() + ":"
					+ mCheckFaultInfo.getCn());

			if (mCheckFaultInfo.getFlag() == CheckFaultInfo.STATE0) {
				mHolder.mProgressBar.setVisibility(View.VISIBLE);
				mHolder.mImageView.setVisibility(View.GONE);
			} else if (mCheckFaultInfo.getFlag() == CheckFaultInfo.STATE1) {
				mHolder.mProgressBar.setVisibility(View.GONE);
				mHolder.mImageView.setVisibility(View.VISIBLE);
				mHolder.mImageView
						.setImageResource(R.drawable.car_test_no_problem);
			} else if (mCheckFaultInfo.getFlag() == CheckFaultInfo.STATE2) {
				mHolder.mProgressBar.setVisibility(View.GONE);
				mHolder.mImageView.setVisibility(View.VISIBLE);
				mHolder.mImageView
						.setImageResource(R.drawable.car_test_problem);
			}
		}
		return convertView;
	}

	class Holder {
		private TextView mTextViewTitle1;// 故障（标题）类别字母

		private TextView mTextViewTitle2;// 故障（标题）类别描述

		private TextView mTextViewTitle3;// 故障（标题）检测数

		private ImageView mImageViewTitle;// 故障（标题）检测图标

		private ProgressBar mProgressBarTitle;// 故障（标题）检测进度条

		private View mProgressBarTitleOut;// 标题进度条的外层背景

		private TextView mTextView;// 故障类别

		private ImageView mImageView;// 故障检测图标

		private ProgressBar mProgressBar;// 故障检测进度条

		private View mView1;// 标题

		private View mView2;// item

		private View mView3;// 底部横线

	}
}
