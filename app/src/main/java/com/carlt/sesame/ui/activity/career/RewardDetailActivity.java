package com.carlt.sesame.ui.activity.career;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.data.career.PrizeInfo;
import com.carlt.sesame.http.AsyncImageLoader;
import com.carlt.sesame.ui.activity.base.LoadingActivityWithTitle;
import com.carlt.sesame.utility.Log;
import com.carlt.sesame.utility.UUToast;

/**
 * 奖品详情页面
 * 
 * @author daisy
 */
public class RewardDetailActivity extends LoadingActivityWithTitle implements
		OnClickListener {
	private ImageView back;// 头部返回键

	private TextView title;// 标题文字

	private TextView txtRight;// 头部右侧文字

	private TextView mTextView1; // 奖品名称

	private TextView mTextView2; // 奖品描述

	private TextView mTextView3; // 优惠券密码

	private TextView mTextView4; // 优惠券密码说明框

	private TextView mTextView5; // 4s店铺名称

	private WebView mWeb1; // 使用说明描述

	private WebView mWeb2; // 4s店铺描述

	private TextView mTextView8;// 4s店铺电话

	private ImageView mImageView1;// 奖券图片

	private View mView;// 底部拨打电话按钮

	private Intent mIntent;

	private String rewardId; // 奖品id

	private PrizeInfo mPrizeInfo;

	public final static String REWARDID = "rewardId";

	private AsyncImageLoader mImageLoader = AsyncImageLoader.getInstance();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_career_reward_detail);
		setTitleView(R.layout.head_back);
		mIntent = getIntent();
		rewardId = mIntent.getStringExtra(REWARDID);
		initTitle();
		init();
		LoadData();

	}

	private void initTitle() {
		back = (ImageView) findViewById(R.id.head_back_img1);
		title = (TextView) findViewById(R.id.head_back_txt1);
		txtRight = (TextView) findViewById(R.id.head_back_txt2);

		back.setImageResource(R.drawable.arrow_back);
		txtRight.setText("晒一晒");
		txtRight.setVisibility(View.GONE);

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		txtRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (mBitmap != null) {
//					ShareControl.share(RewardDetailActivity.class,
//							RewardDetailActivity.this,
//							mPrizeInfo.getShareTitle(),
//							mPrizeInfo.getShareText(),
//							mPrizeInfo.getShareLink(), mBitmap);
				}

			}
		});
	}

	private void init() {

		mTextView1 = (TextView) findViewById(R.id.activity_career_reward_detail_text1);
		mTextView2 = (TextView) findViewById(R.id.activity_career_reward_detail_text2);
		mTextView3 = (TextView) findViewById(R.id.activity_career_reward_detail_text3);
		mTextView4 = (TextView) findViewById(R.id.activity_career_reward_detail_text3_des);

		mTextView5 = (TextView) findViewById(R.id.activity_career_reward_detail_text5);
		mTextView8 = (TextView) findViewById(R.id.activity_career_reward_detail_text8);
		mWeb1 = (WebView) findViewById(R.id.activity_career_reward_detail_webview1);
		mWeb2 = (WebView) findViewById(R.id.activity_career_reward_detail_webview2);
		mImageView1 = (ImageView) findViewById(R.id.activity_career_reward_detail_img1);

		mView = findViewById(R.id.activity_career_reward_detail_layout);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.activity_career_reward_detail_layout:
			// 调用系统拨号功能
			String phoneNum = mPrizeInfo.getDealer_tel();
			if (phoneNum != null && !phoneNum.equals("")) {

				// 调用系统的拨号服务实现电话拨打功能
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
						+ phoneNum.trim()));
				try {
					RewardDetailActivity.this.startActivity(intent);
				} catch (Exception e) {
					Log.e("info", e.getMessage());
					if (e instanceof SecurityException) {
						UUToast.showUUToast(RewardDetailActivity.this, "请到设置页面打开芝麻乐园的通话权限");
					}
				}
			}
			break;
		default:
			break;
		}
	}

	Bitmap mBitmap;

	@Override
	protected void LoadSuccess(Object data) {
		mPrizeInfo = (PrizeInfo) data;
		if (mPrizeInfo != null) {

			// mWeb1.getSettings().setJavaScriptEnabled(true);
			// mWeb1.setWebChromeClient(new WebChromeClient());
			mWeb1.loadDataWithBaseURL(null, mPrizeInfo.getUsedescription()
					.toString(), "text/html", "utf-8", null);

			// mWeb2.getSettings().setJavaScriptEnabled(true);
			// mWeb2.setWebChromeClient(new WebChromeClient());
			mWeb2.loadDataWithBaseURL(null, mPrizeInfo.getDealer_info(),
					"text/html", "utf-8", null);

			title.setText(mPrizeInfo.getName());
			mTextView1.setText(mPrizeInfo.getName());

			mTextView2.setText(mPrizeInfo.getDescription());
			String code = mPrizeInfo.getCode();
			if (code == null || code.equals("")) {
				mTextView4.setVisibility(View.GONE);
				mTextView3.setVisibility(View.GONE);
			} else {
				mTextView4.setVisibility(View.VISIBLE);
				mTextView3.setVisibility(View.VISIBLE);
				mTextView3.setText(code);
			}

			mView.setOnClickListener(this);

			String urlImg = mPrizeInfo.getIconUrl();

			if (urlImg != null && urlImg.length() > 0) {
				mBitmap = mImageLoader.getBitmapByUrl(urlImg);
				if (mBitmap != null) {
					mImageView1.setImageBitmap(mBitmap);
				}
			} else {
				mImageView1.setImageResource(R.drawable.icon_default_reward);
			}

			mTextView5.setText(mPrizeInfo.getDealer_name());

			mTextView8.setText(mPrizeInfo.getDealer_tel());
		}

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				ManualLoadSuccess();
			}
		}, 3000);
	}

	final String digits = "0123456789ABCDEF";

	public String encode(String s) {
		// Guess a bit bigger for encoded form
		StringBuilder buf = new StringBuilder(s.length() + 16);
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')
					|| (ch >= '0' && ch <= '9') || ".-*_".indexOf(ch) > -1) { //$NON-NLS-1$  
				buf.append(ch);
			} else {
				byte[] bytes = new String(new char[] { ch }).getBytes();
				for (int j = 0; j < bytes.length; j++) {
					buf.append('%');
					buf.append(digits.charAt((bytes[j] & 0xf0) >> 4));
					buf.append(digits.charAt(bytes[j] & 0xf));
				}
			}
		}
		return buf.toString();
	}

	@Override
	protected void LoadErro(Object erro) {

		super.LoadErro(erro);
	}

	@Override
	protected void LoadData() {
		super.LoadData();
		CPControl.GetPrizeDetailResult(rewardId, listener);
	}

	@Override
	public void OnImgLoadFinished(String url, Bitmap mBitmap) {
		super.OnImgLoadFinished(url, mBitmap);

		if (url != null && url.equals(mPrizeInfo.getIconUrl())) {
			this.mBitmap = mBitmap;
			mImageView1.setImageBitmap(mBitmap);
		}
	}

}
