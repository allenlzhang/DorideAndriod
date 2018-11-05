
package com.carlt.chelepie.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.carlt.chelepie.data.recorder.PieAttrInfo;
import com.carlt.chelepie.data.recorder.PieInfo;
import com.carlt.chelepie.view.adapter.PieAttrAdapter;
import com.carlt.doride.DorideApplication;
import com.carlt.doride.R;
import com.carlt.doride.eventbus.FullActivityProssEvent;
import com.carlt.doride.ui.view.UUPopupWindow;
import com.carlt.sesame.control.CPControl;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * 点击弹出列表
 * 
 * @author Administrator
 */
public class PopShow {
	private LayoutInflater inflater;

	private UUPopupWindow menuPop;

	private View menuView_main;

	private Animation ani1;

	private Animation ani2;

	private View menu_bg;

	private ListView mListView;

	private RadioGroup mRadioGroup;

	private TextView mTxtTip;

	private PieAttrAdapter mAdapter;

	private ArrayList<PieAttrInfo> mDataList;

	private Context mContext;

	private OnPopshowCheckedChangedListener mCheckedChangedListener;// 选择改变listener

	public void SetOnDismissListener(UUPopupWindow.OnDismissListener mOnDismissListener) {
		menuPop.setOnDismissListener(mOnDismissListener);
	}

	public void setmCheckedChangedListener(OnPopshowCheckedChangedListener mCheckedChangedListener) {
		this.mCheckedChangedListener = mCheckedChangedListener;
	}

	public PopShow(Context context) {
		mContext = context;
		inflater = LayoutInflater.from(mContext);
		menuView_main = inflater.inflate(R.layout.layout_pop_show, null);

		menuPop = new UUPopupWindow(menuView_main, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		menu_bg = menuView_main.findViewById(R.id.layout_list_show_background);

		mListView = (ListView) menuView_main.findViewById(R.id.layout_pop_show_list);
		mRadioGroup = (RadioGroup) menuView_main.findViewById(R.id.layout_pop_show_rg);
		mTxtTip = (TextView) menuView_main.findViewById(R.id.layout_pop_show_txt);

		// menu_bg.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// Log.e("info", "饱和度背景点击");
		// switch (type) {
		// case TYPE_LIST:
		// String brightness = mDataList.get(0).getValue();
		// String contrast = mDataList.get(2).getValue();
		// String saturation = mDataList.get(1).getValue();
		//
		// // 测试数据
		// String width = "320";
		// String height = "240";
		// // 测试数据
		// RecorderControl.setImg(width, height, brightness, contrast,
		// saturation, listener);
		// break;
		//
		// case TYPE_RG:
		// break;
		// }
		// dissmiss();
		// }
		// });
		initAnimation(mContext);
		menuView_main.setFocusableInTouchMode(true);
		// menuView_main.setOnKeyListener(new OnKeyListener() {
		//
		// public boolean onKey(View v, int keyCode, KeyEvent event) {
		//
		// if (keyCode == KeyEvent.KEYCODE_MENU && menuPop.isShowing()
		// && event.getAction() == KeyEvent.ACTION_UP) {
		// menuPop.dismiss();
		//
		// } else if (keyCode == KeyEvent.KEYCODE_BACK && menuPop.isShowing()
		// && event.getAction() == KeyEvent.ACTION_UP) {
		// menuPop.dismiss();
		//
		// }
		// return true;
		// }
		// });
		menuPop.setBackgroundDrawable(new BitmapDrawable());
		menuPop.initAni(menuView_main, ani2);
		menuPop.setFocusable(true);
		menuPop.update();
		menuPop.setOutsideTouchable(false);

		mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				int imgQuality = 0;
				switch (checkedId) {
				case R.id.layout_pop_show_rb_fhd:
					imgQuality = PieInfo.SIZE_1080P;
					break;
				case R.id.layout_pop_show_rb_hd:
					imgQuality = PieInfo.SIZE_720P;
					break;
				case R.id.layout_pop_show_rb_sd:
					imgQuality = PieInfo.SIZE_720P;
					break;
				}
				EventBus.getDefault().post(new FullActivityProssEvent(1,imgQuality));
				mCheckedChangedListener.onChecked(imgQuality);
			}
		});

	}

	private CPControl.GetResultListCallback listener = new CPControl.GetResultListCallback() {

		@Override
		public void onFinished(Object o) {

		}

		@Override
		public void onErro(Object o) {

		}
	};

	private void initAnimation(Context mContext) {
		ani1 = AnimationUtils.loadAnimation(mContext, R.anim.enter_menu_personevaluation);
		ani2 = AnimationUtils.loadAnimation(mContext, R.anim.exit_menu_personevaluation);
	}

	/**
	 * 列表
	 * 
	 * @param view
	 *            显示在该view的下方
	 */
	public void showList(View view, ArrayList<PieAttrInfo> dataList) {
		mDataList = dataList;
		if (mAdapter == null) {
			mAdapter = new PieAttrAdapter(mContext, dataList);
			mListView.setAdapter(mAdapter);
		} else {
			mAdapter.notifyDataSetChanged();
		}
		mListView.setVisibility(View.VISIBLE);
		mRadioGroup.setVisibility(View.GONE);
		mTxtTip.setVisibility(View.GONE);
		show(view, mListView);
	}

	/**
	 * 单选框
	 * 
	 * @param view
	 *            显示在该view的下方
	 */
	public void showRg(View view, int quality) {
		int index = 1;
		if (quality == PieInfo.SIZE_1080P) {
			index = 0;
		} else if (quality == PieInfo.SIZE_720P) {
			index = 1;
		}

		mListView.setVisibility(View.GONE);
		mRadioGroup.setVisibility(View.VISIBLE);

		RadioButton rbSelected = (RadioButton) mRadioGroup.getChildAt(index);
		mRadioGroup.check(rbSelected.getId());
		mTxtTip.setVisibility(View.GONE);

		show(view, mRadioGroup);
	}

	/**
	 * 文字
	 * 
	 * @param view
	 *            显示在该view的下方
	 */
	public void showTxt(View view, String txt) {
		mListView.setVisibility(View.GONE);
		mRadioGroup.setVisibility(View.GONE);
		mTxtTip.setVisibility(View.VISIBLE);
		mTxtTip.setText(txt);
		show(view, mTxtTip);
	}

	private void show(View view, final View showView) {
		int width = 0;
		switch (showView.getId()) {
		case R.id.layout_pop_show_list:
			width = (int) mContext.getResources().getDimension(R.dimen.pop_width_list);
			break;

		case R.id.layout_pop_show_rg:
			width = (int) mContext.getResources().getDimension(R.dimen.pop_width_rg);
			break;
		case R.id.layout_pop_show_txt:
			width = (int) mContext.getResources().getDimension(R.dimen.pop_width_txt);
			break;
		}

		int x = (int) (view.getX() + 15 * DorideApplication.ScreenDensity - width / 2);
		int y = (int) (mContext.getResources().getDimension(R.dimen.head_height) + 35 * DorideApplication.ScreenDensity);
		menuPop.showAtLocation(menuView_main, Gravity.LEFT | Gravity.TOP, x, y);
		menuView_main.startAnimation(ani1);

	}

	private void dissmiss() {
		menuPop.dismiss();
	}

	public interface OnPopshowCheckedChangedListener {
		void onChecked(int imgQuality);
	};
}
