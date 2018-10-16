package com.carlt.doride.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.doride.data.flow.TrafficPackagePurchaseLogInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 续费记录Adapter
 * 
 * @author daisy
 */
public class TrafficPurchaseLogAdapter extends BaseAdapter {
	private LayoutInflater mInflater;

	private static final int BUY_TIME = 0;

	private static final int BUY_DETAIL = 1;

	private ArrayList<TrafficPackagePurchaseLogInfo> mDataList;

	private Context context;
	private int package_type;

	public TrafficPurchaseLogAdapter(Context context,
			ArrayList<TrafficPackagePurchaseLogInfo> listInfo) {
		this.context = context;
		mInflater = LayoutInflater.from(context);
		this.mDataList = listInfo;
		// this.package_type=package_type;
	}

	public void setmList(ArrayList<TrafficPackagePurchaseLogInfo> mList) {
		this.mDataList = mList;
	}

	public void setPackType(int type) {
		this.package_type = type;
	}

	@Override
	public int getCount() {
		if (mDataList != null) {
			return mDataList.size();
		}
		return 0;
	}

	@Override
	public TrafficPackagePurchaseLogInfo getItem(int position) {
		return mDataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		TrafficPackagePurchaseLogInfo purchaseLogInfo = getItem(position);
		if (!TextUtils.isEmpty(purchaseLogInfo.getBuy_time())) {
			return BUY_TIME;
		} else {
			return BUY_DETAIL;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		TrafficPackagePurchaseLogInfo mPurchaseLogInfo = getItem(position);
		ViewHolder mViewHolder = null;
		ViewHolderMonth mViewHolderMonth = null;
		int type = getItemViewType(position);
		if (null == convertView) {
			switch (type) {
			case BUY_TIME:
				convertView = mInflater.inflate(
						R.layout.list_item_traffic_purchase_month, null);
				mViewHolderMonth = new ViewHolderMonth();
				mViewHolderMonth.traffic_buy_month = (TextView) convertView
						.findViewById(R.id.traffic_buy_month);
				convertView.setTag(mViewHolderMonth);
				break;
			case BUY_DETAIL:
				convertView = mInflater.inflate(
						R.layout.list_item_traffic_purchase, null);
				mViewHolder = new ViewHolder();
				mViewHolder.traffic_buy_value = (TextView) convertView
						.findViewById(R.id.traffic_buy_value);
				mViewHolder.traffic_buy_amount = (TextView) convertView
						.findViewById(R.id.traffic_buy_amount);
				mViewHolder.traffic_buy_time = (TextView) convertView
						.findViewById(R.id.traffic_buy_time);
				mViewHolder.tvFlowType = (TextView) convertView
						.findViewById(R.id.tvFlowType);
				mViewHolder.tvFlowEndDate = (TextView) convertView
						.findViewById(R.id.tvFlowEndDate);
				mViewHolder.traffic_buy_addtime = (TextView)convertView.findViewById(R.id.traffic_buy_addtime);
				convertView.setTag(mViewHolder);
				break;
			}
		} else {
			switch (type) {
			case BUY_TIME:
				mViewHolderMonth = (ViewHolderMonth) convertView.getTag();
				break;
			case BUY_DETAIL:
				mViewHolder = (ViewHolder) convertView.getTag();
				break;
			}
		}
		switch (type) {
		case BUY_TIME:
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
				Date date = sdf.parse(mPurchaseLogInfo.getBuy_time());
				Date date1 = new Date(date.getTime());
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月");
				mViewHolderMonth.traffic_buy_month.setText(sdf1.format(date1));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			break;
		case BUY_DETAIL:
			mViewHolder.traffic_buy_value.setText(String.format(context
					.getResources().getString(R.string.traffic_log_buy_value),
					mPurchaseLogInfo.getPackage_name().trim()));
			mViewHolder.traffic_buy_amount.setText("￥"
					+ mPurchaseLogInfo.getPackage_cost());
			
				SimpleDateFormat sdf2 = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
			Date date2 = null;
			try {
				date2 = sdf2.parse(mPurchaseLogInfo.getAddtime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				Date date3 = new Date(date2.getTime());
				SimpleDateFormat sdf3 = new SimpleDateFormat("MM-dd HH:mm");
				
			switch (package_type) {
			case 2:
				mViewHolder.tvFlowType.setText("流量充值");
				mViewHolder.tvFlowEndDate.setVisibility(View.GONE);
				mViewHolder.traffic_buy_time.setText(sdf3.format(date3));
				mViewHolder.traffic_buy_addtime.setVisibility(View.GONE);
				break;
			case 3:
				mViewHolder.tvFlowType.setText("续套餐");
				mViewHolder.tvFlowEndDate.setVisibility(View.VISIBLE);
				mViewHolder.tvFlowEndDate.setText("结束日期："+mPurchaseLogInfo.service_data_end);
				mViewHolder.traffic_buy_time.setText("生效日期："+mPurchaseLogInfo.service_data_start);
				String package_month = mPurchaseLogInfo.package_month;
				if (!TextUtils.isEmpty(package_month)&&!TextUtils.equals(package_month, "0")) {
					if (Integer.parseInt(package_month)%12 == 0) {
						package_month = "/"+Integer.parseInt(package_month)/12+"年";
					}else{
						package_month = "/"+package_month+"月";
					}
				}else{
					package_month = "";
				}
				mViewHolder.traffic_buy_value.setText("["+
						mPurchaseLogInfo.getPackage_name().trim()+package_month+"]");
				mViewHolder.traffic_buy_addtime.setVisibility(View.VISIBLE);
				mViewHolder.traffic_buy_addtime.setText(sdf3.format(date3));
				break;
			case 4:
				mViewHolder.tvFlowType.setText("更改套餐");
				mViewHolder.tvFlowEndDate.setVisibility(View.VISIBLE);
//				mViewHolder.traffic_buy_time.setText(sdf3.format(date3));
				mViewHolder.traffic_buy_addtime.setVisibility(View.VISIBLE);
				mViewHolder.traffic_buy_addtime.setText(sdf3.format(date3));
				mViewHolder.tvFlowEndDate.setText("结束日期："+mPurchaseLogInfo.service_data_end);
				mViewHolder.traffic_buy_time.setText("生效日期："+mPurchaseLogInfo.service_data_start);
				break;

			default:
				break;
			}
			
			break;
		}
		return convertView;
	}

	static class ViewHolder {
		TextView traffic_buy_value;// 购买流量数

		TextView traffic_buy_amount;// 购买金额
		TextView traffic_buy_addtime;	//续费购买时间
		TextView traffic_buy_time;// 购买时间
		TextView tvFlowType;// 充值类型
		TextView tvFlowEndDate;// 流量结束时间

	}

	static class ViewHolderMonth {
		TextView traffic_buy_month;// 购买流量数
	}

}
