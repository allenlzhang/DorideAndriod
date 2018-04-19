/**  
 * Project Name:Android_Car_Example  
 * File Name:RouteTask.java  
 * Package Name:com.amap.api.car.example  
 * Date:2015年4月3日下午2:38:10  
 *  
 */

package com.carlt.doride.utils.map;

import android.content.Context;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.RouteSearch.FromAndTo;
import com.amap.api.services.route.RouteSearch.OnRouteSearchListener;
import com.amap.api.services.route.RouteSearch.WalkRouteQuery;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName:RouteTask <br/>
 * Function: 封装的驾车路径规划 <br/>
 * Date: 2015年4月3日 下午2:38:10 <br/>
 * 
 * @author yiyi.qi
 * @version
 * @since JDK 1.6
 * @see
 */
public class RouteTask implements OnRouteSearchListener {

	private static RouteTask mRouteTask;

	private RouteSearch mRouteSearch;

	private PositionEntity mFromPoint;

	private PositionEntity mToPoint;

	private List<OnRouteCalculateListener> mListeners = new ArrayList<OnRouteCalculateListener>();
	private List<OnRouteResult> mWalkListeners = new ArrayList<OnRouteResult>();

	public interface OnRouteCalculateListener {
		public void onRouteCalculate(float cost, float distance, int duration);

	}

	public interface OnRouteResult {
		public void onRoutePath(WalkPath path, WalkRouteResult result);
	}

	public static RouteTask getInstance(Context context) {
		if (mRouteTask == null) {
			mRouteTask = new RouteTask(context);
		}
		return mRouteTask;
	}

	public PositionEntity getStartPoint() {
		return mFromPoint;
	}

	public void setStartPoint(PositionEntity fromPoint) {
		mFromPoint = fromPoint;
	}

	public PositionEntity getEndPoint() {
		return mToPoint;
	}

	public void setEndPoint(PositionEntity toPoint) {
		mToPoint = toPoint;
	}

	private RouteTask(Context context) {
		mRouteSearch = new RouteSearch(context);
		mRouteSearch.setRouteSearchListener(this);
	}

	public void search() {
		if (mFromPoint == null || mToPoint == null) {
			return;
		}

		FromAndTo fromAndTo = new FromAndTo(new LatLonPoint(mFromPoint.latitue, mFromPoint.longitude), new LatLonPoint(mToPoint.latitue, mToPoint.longitude));
		WalkRouteQuery driveRouteQuery = new WalkRouteQuery(fromAndTo, RouteSearch.WalkDefault);

		// mRouteSearch.calculateDriveRouteAsyn(driveRouteQuery);
		mRouteSearch.calculateWalkRouteAsyn(driveRouteQuery);
	}

	public void search(PositionEntity fromPoint, PositionEntity toPoint) {

		mFromPoint = fromPoint;
		mToPoint = toPoint;
		search();

	}

	public void addRouteCalculateListener(OnRouteCalculateListener listener) {
		synchronized (this) {
			if (mListeners.contains(listener))
				return;
			mListeners.add(listener);
		}
	}

	public void removeRouteCalculateListener(OnRouteCalculateListener listener) {
		synchronized (this) {
			mListeners.remove(listener);
		}
	}

	public void addRouteResultListener(OnRouteResult listener) {
		synchronized (this) {
			if (mWalkListeners.contains(listener))
				return;
			mWalkListeners.add(listener);
		}
	}
	
	public void removeAllResultListener(){
		if(mWalkListeners != null && mWalkListeners.size() > 0){
			mWalkListeners.clear();
		}
	}

	public void removeRouteResultListener(OnRouteResult listener) {
		synchronized (this) {
			mWalkListeners.remove(listener);
		}
	}

	@Override
	public void onBusRouteSearched(BusRouteResult arg0, int arg1) {

		// TODO Auto-generated method stub

	}

	@Override
	public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int resultCode) {
		if (resultCode == 1000 && driveRouteResult != null) {
			synchronized (this) {
				for (OnRouteCalculateListener listener : mListeners) {
					List<DrivePath> drivepaths = driveRouteResult.getPaths();
					float distance = 0;
					int duration = 0;
					if (drivepaths.size() > 0) {
						DrivePath drivepath = drivepaths.get(0);

						distance = drivepath.getDistance() / 1000;

						duration = (int) (drivepath.getDuration() / 60);
					}

					float cost = driveRouteResult.getTaxiCost();

					listener.onRouteCalculate(cost, distance, duration);
				}

			}
		} else {

		}
		// TODO 可以根据app自身需求对查询错误情况进行相应的提示或者逻辑处理
	}

	@Override
	public void onWalkRouteSearched(WalkRouteResult arg0, int arg1) {
		if (arg1 == 1000 && arg0 != null) {
			synchronized (this) {
				for (OnRouteResult listener : mWalkListeners) {
					List<WalkPath> drivepaths = arg0.getPaths();
					if (drivepaths.size() > 0) {
						WalkPath drivepath = drivepaths.get(0);
						listener.onRoutePath(drivepath, arg0);
					} else {
						listener.onRoutePath(null, null);
					}
				}
			}
		} else {
			synchronized (this) {
				for (OnRouteResult listener : mWalkListeners) {
					listener.onRoutePath(null, null);
				}
			}
		}
	}

	@Override
	public void onRideRouteSearched(RideRouteResult arg0, int arg1) {
		// TODO Auto-generated method stub
	}
}
