package com.carlt.sesame.control;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout.LayoutParams;

import com.carlt.doride.DorideApplication;
import com.carlt.doride.R;
import com.carlt.doride.ui.activity.login.UserLoginActivity;
import com.carlt.sesame.data.SesameLoginInfo;
import com.carlt.sesame.ui.activity.car.CarMainActivity;
import com.carlt.sesame.ui.activity.career.CareerMainActivity;
import com.carlt.sesame.ui.activity.career.report.newui.DayActivity;
import com.carlt.sesame.ui.activity.remote.RemoteMainNewActivity;
import com.carlt.sesame.ui.activity.safety.AuthorActivity;
import com.carlt.sesame.ui.activity.setting.SettingMainActivity;
import com.carlt.sesame.ui.view.PopBoxCreat;
import com.carlt.sesame.ui.view.PopBoxCreat.DialogWithTitleClick;
import com.carlt.sesame.ui.view.UUCallDialog;
import com.carlt.sesame.ui.view.UUCallDialog.CallDialogClick;
import com.carlt.sesame.utility.DisplayUtil;
import com.carlt.sesame.utility.Log;

/**
 *
 * @author liuxiangfei Description: 显示悬浮框的控制类
 */
public class DragViewCtr {

	private static final int MOVE_LENGH = 150;

	private int screenHeight;

	private static final String TAG = DragViewCtr.class.getSimpleName();

	private int screenWidth;
	/**
	 * 被拖动的图
	 */
	private ImageButton iv_drag;
	private SharedPreferences sp;
	private Activity activity;

	private ViewGroup decorView;

	private View layout;

	/**
	 * 设置页面设置是否应该显示
	 */
	private boolean showFlag;

	/**
	 * @return 获取是否显示拖动滑块
	 */
	public boolean isShowFlag() {
		return showFlag;
	}

	/**
	 * BaseActivityGroup 底部控件栏的高度
	 */
	private int bootHigh = 0;

	private int initHigh;

	private int initLift;

	/**
	 * 对应的挂在GroupActivity或者Activity 状态栏的高度
	 */
	private int statusBarHeight1 = 0;

	/**
	 * 系统的状态栏高度
	 */
	private static int STATUSBARHEIGHT;

	public DragViewCtr(Activity activity) {
		this.activity = activity;
		DisplayMetrics metric = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
		this.screenHeight = metric.heightPixels;
		this.screenWidth = metric.widthPixels;

		this.sp = activity.getSharedPreferences("config", Context.MODE_PRIVATE);
		//获取status_bar_height资源的ID  
		int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
		//根据资源ID获取响应的尺寸值
		STATUSBARHEIGHT  = activity.getResources().getDimensionPixelSize(resourceId);
		statusBarHeight1 = activity.getResources().getDimensionPixelSize(resourceId);
		Log.e(TAG, "状态栏:" + statusBarHeight1);
		}
		if (checkLinkGroupActivity()) {
			bootHigh = DisplayUtil.dip2px(activity, 80);
			statusBarHeight1 = 0;
		}

		initHigh = DisplayUtil.dip2px(activity, 180);

		initLift = DisplayUtil.dip2px(activity, 80);


	}

	/**
	 * 显示可拖动的客服电话图标
	 */
	public void showDragCallView() {
		// 判断是否是登录之前的页面,
		showFlag = this.sp.getBoolean("isShow", true);
		if (activity instanceof CareerMainActivity) {
			DorideApplication.getInstance().setShowDragFlag(true);
		}
		if (TextUtils.isEmpty(SesameLoginInfo.getDealerTel())
				&& TextUtils.isEmpty(SesameLoginInfo.getServiceTel())) {
			return;
		}
		if (!showFlag) {
			return;
		}

		if (!DorideApplication.getInstance().isShowDragFlag()) {
			return;
		}
		if (activity instanceof AuthorActivity) {
			return;
		}
		if (activity instanceof UserLoginActivity) {
			return;
		}

		decorView = (ViewGroup) activity.getWindow().getDecorView();
		LayoutInflater factory = LayoutInflater.from(activity);
		layout = factory.inflate(R.layout.draglayout, null);
		decorView.addView(layout);
		this.iv_drag = (ImageButton) layout.findViewById(R.id.imageview_drag);

		this.iv_drag.setVisibility(View.VISIBLE);
		int width = screenWidth; // 屏幕宽度（像素）
		int height = screenHeight; // 屏幕高度（像素）
		int lastx = this.sp.getInt("lastx", width - initLift);
		int lasty = this.sp.getInt("lasty", height - initHigh);

		LayoutParams params = (LayoutParams) this.iv_drag
				.getLayoutParams();
		if (checkLinkGroupActivity()) {
			if(lasty > height - DisplayUtil.dip2px(activity, 132)){
				lasty = height - DisplayUtil.dip2px(activity, 132);
			}else if(lasty < STATUSBARHEIGHT+5){//微调因为activitygroup引起的bug
				lasty = 0;
			}
		}
		else{
			if(lasty < statusBarHeight1){//微调因为activitygroup引起的bug
			lasty = statusBarHeight1;
			}
		}
		params.leftMargin = lastx;
		params.topMargin = lasty;
		this.iv_drag.setLayoutParams(params);

		this.iv_drag.setOnTouchListener(new OnTouchListener() {
			int startX;
			int startY;
			long downTime;
			long upTime;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:// 手指第一次触摸到屏幕
					iv_drag.setBackgroundResource(R.drawable.setting_cusromer_drag_down);
					this.startX = (int) event.getRawX();
					this.startY = (int) event.getRawY();
					downTime = System.currentTimeMillis();
					break;
				case MotionEvent.ACTION_MOVE:// 手指移动
					int newX = (int) event.getRawX();
					int newY = (int) event.getRawY();

					int dx = newX - this.startX;
					int dy = newY - this.startY;

					// 计算出来控件原来的位置
					int l = iv_drag.getLeft();
					int r = iv_drag.getRight();
					int t = iv_drag.getTop();
					int b = iv_drag.getBottom();

					int newt = t + dy;
					int newb = b + dy;
					int newl = l + dx;
					int newr = r + dx;

					if ((newl < 0) || (newt < statusBarHeight1) || (newr > screenWidth)
							|| (newb > (screenHeight - bootHigh))) {
						break;
					}

					// 更新iv在屏幕的位置.
					iv_drag.layout(newl, newt, newr, newb);
					this.startX = (int) event.getRawX();
					this.startY = (int) event.getRawY();

					break;
				case MotionEvent.ACTION_UP: // 手指离开屏幕的一瞬间
					iv_drag.setBackgroundResource(R.drawable.setting_cusromer_drag);
					int lastx = iv_drag.getLeft();
					int lasty = iv_drag.getTop();
					upTime = System.currentTimeMillis();
					if (Math.abs(lastx - startX) < MOVE_LENGH
							&& Math.abs(lasty - startY) < MOVE_LENGH
							&& (upTime - downTime) < 150l) {
						showDialog();
					}
					Editor editor = sp.edit();
					editor.putInt("lastx", lastx);
					editor.putInt("lasty", lasty);
					editor.apply();
					break;
				}
				return true;
			}
		});
	}

	/**
	 * 拨打电话的弹出框
	 */
	public void showDialog() {
		UUCallDialog mCallDialog = new UUCallDialog(activity,
				new CallDialogClick() {

					@Override
					public void clickSoft() {
						String serviceTel = SesameLoginInfo.getServiceTel();
						if (serviceTel != null && !serviceTel.equals("")) {
							call(serviceTel);
						}
					}

					@Override
					public void clickCar() {
						String dealerTel = SesameLoginInfo.getDealerTel();
						if (dealerTel != null && !dealerTel.equals("")) {
							call(dealerTel);
						}
					}
				});
		mCallDialog.show();

	}

	/**
	 * 隐藏并标记已经隐藏
	 */
	public void hideDragCallView() {
		if (null != iv_drag) {
			this.iv_drag.setVisibility(View.GONE);
		}
		if (null != decorView && null != layout) {
			decorView.removeView(layout);
		}
		showFlag = false;
	}

	/**
	 * 仅仅是隐藏
	 */
	public void justhideDragCallView() {
		if (null != iv_drag) {
			this.iv_drag.setVisibility(View.GONE);
		}
		if (null != decorView && null != layout) {
			decorView.removeView(layout);
		}
	}

	/**
	 * 拨打电话的弹出框
	 */
	public void call(final String tel) {
		DialogWithTitleClick click = new DialogWithTitleClick() {

			@Override
			public void onRightClick() {
				// 取消

			}

			@SuppressLint("MissingPermission")
			@Override
			public void onLeftClick() {
				// 确定
				if (tel != null && !tel.equals("")) {
					// 调用系统的拨号服务实现电话拨打功能
					Intent intent = new Intent(Intent.ACTION_CALL,
							Uri.parse("tel:" + tel.trim()));
					try {
						activity.startActivity(intent);
					} catch (Exception e) {
						Log.e("info", e.getMessage());
					}
				}
				;
			}
		};
		PopBoxCreat.createDialogNotitle(activity, "您确定拨打电话 ", tel, "确定", "取消",
				click, true);
	}

	/**
	 * 设置是否可见
	 * 
	 * @param flag
	 *            true表示可见， false表示不可见
	 */
	public void setShow(boolean flag) {
		showFlag = flag;
		Editor editor = sp.edit();
		editor.putBoolean("isShow", flag);
		editor.apply();
	}

	/**
	 * @return 返回true表示 是挂在GroupActivity上的页面
	 */
	private boolean checkLinkGroupActivity() {
		if (activity instanceof CareerMainActivity) {
			return true;
		} else if (activity instanceof CarMainActivity) {
			return true;
		} else if (activity instanceof RemoteMainNewActivity) {
			return true;
		} else if (activity instanceof SettingMainActivity) {
			return true;
		} else if (activity instanceof DayActivity) {
			return true;
		}
		return false;
	}

}
