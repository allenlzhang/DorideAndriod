package com.carlt.doride.ui.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.carlt.doride.R;

import java.util.Date;

public class CalendarDay extends MenuCalendar {
	public CalendarDay(Context mContext,
                       final OnCalendarDayClick mOnCalendarDayClick) {
		super(mContext);
		View child = LayoutInflater.from(mContext).inflate(
				R.layout.report_rili_main, null);
		init(child);

		setTitle("选择日报日期");
		mDateView = (ReportDateView) child
				.findViewById(R.id.report_rili_dateview);
		ReportDateView.OnItemClick mOnItemClick = new ReportDateView.OnItemClick() {

			@Override
			public void onClick(String date) {
				mOnCalendarDayClick.onClick(date);
				dissmiss();

			}

			@Override
			public void onTextChange(String title) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTitleStateChange(int year, int month, int day) {
				// TODO Auto-generated method stub

			}
		};
		mDateView.setmOnItemClick(mOnItemClick);
	}

	private ReportDateView mDateView;

	@Override
	protected void onPopCreat() {
		Date mDate = new Date();
		mDateView.load(mDate.getYear() + 1900, mDate.getMonth() + 1);

	}

	public interface OnCalendarDayClick {
		void onClick(String date);
	};

}
