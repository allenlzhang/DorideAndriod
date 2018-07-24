
package com.carlt.sesame.control;

import android.app.Activity;
import android.content.Intent;

import com.carlt.sesame.data.LoginInfo;
import com.carlt.sesame.ui.MainActivity;
import com.carlt.sesame.ui.activity.safety.AuthorActivity;
import com.carlt.sesame.ui.activity.safety.FreezeActivity;
import com.carlt.sesame.ui.activity.usercenter.SelectCarBindActivity;
import com.carlt.sesame.ui.activity.usercenter.login.ActivateActivity;
import com.carlt.sesame.ui.view.PopBoxCreat;
import com.carlt.sesame.ui.view.UUUpdateDialog.DialogUpdateListener;
import com.carlt.sesame.utility.Log;

/**
 * 登录控制
 * 
 * @author daisy
 */
public class LoginControl {
	public static Activity mCtx;
	public static DialogUpdateListener mDialogUpdateListener;

	public static void logic(final Activity mContext) {
		mCtx = mContext;
		String className = mContext.getClass().getName();
		// 判断是否绑定设备，
		String s = LoginInfo.getDeviceidstring();
		Log.e("info", "deviceidstring==" + s);
		// 测试代码开始
//		 s="1234567890123456";
//		 s="";
		// 测试代码结束
		if (s != null && s.length() > 0 && !s.equals("null")) {
			// 已绑定设备,判断是否激活设备
			boolean isDeviceActivate = LoginInfo.isDeviceActivate();
			Log.e("info", "isDeviceActivate==" + isDeviceActivate);
			//测试代码
//			isDeviceActivate = false;
			//测试代码结束
			if (isDeviceActivate) {
				// 已激活设备，
				if (LoginInfo.isFreezing()) {
					// 处在冻结状态
					Intent mIntent4 = new Intent(mContext, FreezeActivity.class);
					mIntent4.putExtra(FreezeActivity.FROM_NAME, mContext.getClass().getName());
					mContext.startActivity(mIntent4);
				} else {
					// 处在解冻状态
					if (LoginInfo.isHasAuthorize()) {
						// 有授权请求，跳转至授权页面
						Intent mIntent4 = new Intent(mContext, AuthorActivity.class);
						mContext.startActivity(mIntent4);
					} else {
						// 没有授权请求，跳转至主页面
						Intent mIntent4 = new Intent(mContext, MainActivity.class);
						mContext.startActivity(mIntent4);
						mContext.finish();
					}
				}
			} else {
				// 未激活设备
				boolean isUpdating = LoginInfo.isUpgradeing();
				// 是否需要升级
				if (isUpdating) {
					// 设备正在升级，跳转至升级页面
					PopBoxCreat.showUUUpdateDialog(mContext, new DialogUpdateListener() {
						
						@Override
						public void onSuccess() {
							LoginInfo.setUpgradeing(false);
							LoginControl.logic(mCtx);
							if(mDialogUpdateListener != null){
								mDialogUpdateListener.onSuccess();
							}
						}
						
						@Override
						public void onFailed() {
							if(mDialogUpdateListener != null){
								mDialogUpdateListener.onFailed();
							}
						}
					});
				} else {
					// 设备不需要升级，跳转至激活盒子
					Intent mIntent3 = new Intent(mContext, SelectCarBindActivity.class);
					mIntent3.putExtra(SelectCarBindActivity.FROM_NAME, className);
					mContext.startActivity(mIntent3);
				}

			}
		} else {
			// 未绑定设备
			String mobile = LoginInfo.getMobile();
			String vpin = LoginInfo.getVpin(mobile);
			Log.e("info", "mobile_logincontrol==" + mobile);
			Log.e("info", "vpin_logincontrol==" + vpin);
			Intent mIntent2 = new Intent(mContext, SelectCarBindActivity.class);
			mIntent2.putExtra(ActivateActivity.FROM_NAME, className);
			mContext.startActivity(mIntent2);
		}
	}

}
