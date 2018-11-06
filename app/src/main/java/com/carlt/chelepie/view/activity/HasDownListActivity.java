package com.carlt.chelepie.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;


import com.carlt.chelepie.control.DaoPieDownloadControl;
import com.carlt.chelepie.data.recorder.PieDownloadInfo;
import com.carlt.chelepie.data.recorder.PieDownloadListInfo;
import com.carlt.chelepie.view.adapter.HasdownlistAdapter;
import com.carlt.chelepie.view.adapter.MedialistAdapter;
import com.carlt.doride.R;
import com.carlt.doride.base.LoadingActivity;
import com.carlt.doride.control.CPControl;
import com.carlt.doride.ui.view.PopBoxCreat;
import com.carlt.doride.utils.FileUtil;
import com.carlt.sesame.ui.activity.base.LoadingActivityWithTitle;

import java.io.File;
import java.util.ArrayList;

/**
 * 已下载列表
 * 
 * @author Daisy
 * 
 */
public class HasDownListActivity extends LoadingActivity implements
        OnClickListener {
//	private ImageView back;// 头部返回键
//	private TextView title;// 标题文字
//	private TextView txtRight;// 头部右侧文字

	private TextView mTxtAll;// 全选
	private TextView mTxtDelete;// 删除
	private View mViewBottom;// 底部view
	private RecyclerView mRecyclerView;
	private SwipeRefreshLayout mSwipeRefreshLayout;
	private HasdownlistAdapter mAdapter;
	private ArrayList<PieDownloadInfo> pieDownloadInfos = new ArrayList<PieDownloadInfo>();

	private boolean isEditale;// 是否是在可编辑状态
	private DaoPieDownloadControl mDaoControl = DaoPieDownloadControl
			.getInstance();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hasdown_list);
		initTitle("已下载");
		init();
		LoadData();
	}

	private void init() {
		mTxtAll = (TextView) findViewById(R.id.hasdown_list_txt_all);
		mTxtDelete = (TextView) findViewById(R.id.hasdown_list_txt_delete);
		mViewBottom = findViewById(R.id.hasdown_list_lay_bottom);

		mRecyclerView = (RecyclerView) findViewById(R.id.hasdown_list_recyclerview);
		mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.hasdown_list_swiperefreshlayout);

		LinearLayoutManager mManager = new LinearLayoutManager(
				HasDownListActivity.this);

		mRecyclerView.setLayoutManager(mManager);
		// 添加分割线
		mRecyclerView.addItemDecoration(new DividerItemDecoration(
				HasDownListActivity.this));

		mSwipeRefreshLayout.setColorSchemeColors(this.getResources().getColor(
				R.color.blue_light));
		mSwipeRefreshLayout
				.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
					@Override
					public void onRefresh() {
						// 下拉刷新
						mSwipeRefreshLayout.setRefreshing(false);
						if(!isEditale){
							CPControl.GetHasDownListResult(mCallback);
						}
					}
				});

		mTxtAll.setOnClickListener(this);
		mTxtDelete.setOnClickListener(this);
		tvRight2.setText("选择");
		tvRight2.setOnClickListener(this);
	}

	protected void LoadData() {
		CPControl.GetHasDownListResult(mCallback);
	}

	@Override
	public void loadDataSuccess(Object data) {
		pieDownloadInfos.clear();
		if (data != null) {
			pieDownloadInfos=((PieDownloadListInfo)data).getArrays();
			mAdapter = new HasdownlistAdapter(HasDownListActivity.this,
					pieDownloadInfos);
			mAdapter.setOnItemClickListener(mMyItemClickListener);
			mAdapter.setOnDeleteListener(mOnDeleteListener);
			if (pieDownloadInfos.size() > 0) {
				pieDownloadInfos.add(new PieDownloadInfo());
				tvRight2.setVisibility(View.VISIBLE);
			} else {
				loadNodataUI();
				tvRight2.setVisibility(View.INVISIBLE);
			}
		} else {
			loadNodataUI();
			tvRight2.setVisibility(View.INVISIBLE);
		}
		mRecyclerView.setAdapter(mAdapter);
		super.loadDataSuccess(data);
	}

	@Override
	public void loadNodataUI() {
		super.loadNodataUI();
	}

	// 列表item点击回调
	private MedialistAdapter.MyItemClickListener mMyItemClickListener = new MedialistAdapter.MyItemClickListener() {

		@Override
		public void onItemClick(View view, int postion) {
			PieDownloadInfo mInfo = pieDownloadInfos.get(postion);
			int type = mInfo.getType();
			switch (type) {
			case PieDownloadInfo.TYPE_JPG:
				// 跳转至图像展示
				Intent mIntent1 = new Intent(HasDownListActivity.this, PicViewPagerActivity.class);
				mIntent1.putExtra(PicViewPagerActivity.FILEPIC, mInfo);
				startActivity(mIntent1);
				break;

			case PieDownloadInfo.TYPE_H264:
				// 跳转至视频播放页面
				Intent mIntent2 = new Intent(HasDownListActivity.this, PlayerActivity.class);
				mIntent2.putExtra("pieDownloadInfo", mInfo);
				startActivity(mIntent2);
				break;
			}
		}
	};

	// 列表item中的删除按钮回调
	private HasdownlistAdapter.OnDeleteListener mOnDeleteListener = new HasdownlistAdapter.OnDeleteListener() {

		@Override
		public void onDelete(final PieDownloadInfo pieDownloadInfo) {
			PopBoxCreat.createDialogNotitle(HasDownListActivity.this, "是否删除文件",
					"", "取消", "删除", new PopBoxCreat.DialogWithTitleClick() {

						@Override
						public void onRightClick() {
							// 删除
							String localPath = pieDownloadInfo.getLocalPath();
							FileUtil.deleteFile(new File(localPath));
							mDaoControl.delete(pieDownloadInfo);
							pieDownloadInfos.remove(pieDownloadInfo);
							resetUI();
							mAdapter.notifyDataSetChanged();
						}

						@Override
						public void onLeftClick() {
							// 取消

						}
					});
		}
	};
	
	/**
	 * 重置UI
	 */
	private void resetUI(){
		int size =pieDownloadInfos.size();
		for (int i = 0; i < size; i++) {
			mAdapter.setItemChecked(i, false);
			mAdapter.setItemDeleteShow(i, false);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.head_back_img1:
//			// 返回键
//			finish();
//			break;
//
//		case R.id.head_back_txt2:
//
//			break;
			case R.id.layout_title_back_text4:
				// 右侧编辑按钮
				if (isEditale) {
					mViewBottom.setVisibility(View.GONE);
					tvRight2.setText("选择");
					isEditale = false;
					resetUI();
					mAdapter.setEditable(false);
				} else {
					mViewBottom.setVisibility(View.VISIBLE);
					tvRight2.setText("取消");
					isEditale = true;
					mAdapter.setEditable(true);
					resetUI();
				}
				mAdapter.notifyDataSetChanged();
				break;
		case R.id.hasdown_list_txt_all:
			// 全选
			for (int i = 0; i < pieDownloadInfos.size(); i++) {
				mAdapter.setItemChecked(i, true);
			}
			mAdapter.notifyDataSetChanged();
			break;
		case R.id.hasdown_list_txt_delete:
			// 删除
			PopBoxCreat.createDialogNotitle(HasDownListActivity.this, "是否删除文件",
					"", "取消", "删除", new PopBoxCreat.DialogWithTitleClick() {

						@Override
						public void onRightClick() {
							// 删除
							ArrayList<PieDownloadInfo> deleteInfos = mAdapter.getSelectedItem();
							int size = deleteInfos.size();
							for (int i = 0; i < size; i++) {
								PieDownloadInfo deleteInfo = deleteInfos.get(i);
								String localPath = deleteInfo.getLocalPath();
								FileUtil.deleteFile(new File(localPath));
								mDaoControl.delete(deleteInfo);
								pieDownloadInfos.remove(deleteInfo);
							}
							mViewBottom.setVisibility(View.GONE);
							tvRight2.setText("选择");
							isEditale = false;
							resetUI();
							CPControl.GetHasDownListResult(mCallback);
							mAdapter.setEditable(false);
							mAdapter.notifyDataSetChanged();
						}

						@Override
						public void onLeftClick() {
							// 取消

						}
					},true);
			break;
		}
	}

	class DividerItemDecoration extends RecyclerView.ItemDecoration {
		private int dividerHeight;
		private Paint dividerPaint;

		public DividerItemDecoration(Context context) {
			dividerPaint = new Paint();
			dividerPaint
					.setColor(context.getResources().getColor(R.color.gray));
			dividerHeight = context.getResources().getDimensionPixelSize(
					R.dimen.divider_height);
		}

		@Override
		public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
			super.getItemOffsets(outRect, view, parent, state);
			outRect.bottom = dividerHeight;
		}

		@Override
		public void onDraw(Canvas c, RecyclerView parent,
                           RecyclerView.State state) {
			int childCount = parent.getChildCount();
			int left = parent.getPaddingLeft();
			int right = parent.getWidth() - parent.getPaddingRight();

			for (int i = 0; i < childCount - 1; i++) {
				View view = parent.getChildAt(i);
				float top = view.getBottom();
				float bottom = view.getBottom() + dividerHeight;
				c.drawRect(left, top, right, bottom, dividerPaint);
			}
		}
	}

}
