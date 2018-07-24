
package com.carlt.sesame.ui.activity.car;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.carlt.sesame.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.DaoControl;
import com.carlt.sesame.data.LoginInfo;
import com.carlt.sesame.data.car.PostViolationInfo;
import com.carlt.sesame.data.car.ViolationInfo;
import com.carlt.sesame.ui.activity.base.LoadingActivityWithTitle;
import com.carlt.sesame.ui.adapter.QueryIllegalAdapter;
import com.carlt.sesame.ui.adapter.QueryIllegalAdapter.OnBtnClickListener;
import com.carlt.sesame.utility.MyParse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 座驾-违章查询页面
 * 
 * @author daisy
 */
public class CarQueryIllegalActivity extends LoadingActivityWithTitle implements OnClickListener {
	private ImageView back;// 头部返回键

	private TextView title;// 标题文字

	private TextView txtRight;// 头部右侧文字

	private ImageView mImageViewSecretary;// 车秘书头像

	private TextView mTextViewSecretary;// 车秘书提醒消息

	private ListView mListView;// 安防提醒列表

	private QueryIllegalAdapter mAdapter;

	private ArrayList<ViolationInfo> mViolationInfos;// 违章信息列表

	private PostViolationInfo mInfo;

	boolean mIsOther = false;

	public static final String POST_VIOLATION_INFO = "post_violation_info";
	public static final String VIOLATION_PREF = "violation_pref";
	public static final String KEY_TIME = "key_time";
	public static final String KEY_VIOLATION = "key_violation";
	public static final long DAY_3 = 1000 * 60 * 60 * 24 * 3;// 三天

	public static final String IS_OTHER = "is_other";// 是否查询其他车辆

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_car_query_illegal);
		setTitleView(R.layout.head_back);

		if (getIntent().getBundleExtra("Bundle") != null) {
			mInfo = (PostViolationInfo) getIntent().getBundleExtra("Bundle").getSerializable(POST_VIOLATION_INFO);
			mIsOther = getIntent().getBundleExtra("Bundle").getBoolean(IS_OTHER, false);
		}

		initTitle();
		initSubTitle();
		init();
		LoadData();

	}

	private void initTitle() {
		back = (ImageView) findViewById(R.id.head_back_img1);
		title = (TextView) findViewById(R.id.head_back_txt1);
		txtRight = (TextView) findViewById(R.id.head_back_txt2);

		back.setImageResource(R.drawable.arrow_back);
		title.setText("违章信息");
		txtRight.setVisibility(View.GONE);
		txtRight.setBackgroundResource(R.drawable.illegal_chekc_other);

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void init() {

		mListView = (ListView) findViewById(R.id.activity_car_query_illegal_list);
	}

	private void initSubTitle() {
		mImageViewSecretary = (ImageView) findViewById(R.id.layout_sub_head_img);
		mTextViewSecretary = (TextView) findViewById(R.id.layout_sub_head_txt);
	}

	private int count;// 未处理的违章次数

	private int money;// 罚款总数

	private int fen; // 扣分总数

	private StringBuilder mStringBuilder = new StringBuilder();

	@Override
	protected void LoadSuccess(Object data) {
		super.LoadSuccess(data);
		if (!mIsOther) {
			// 聚合查询成功，初始化违章查询信息
			//CPControl.InitViolation(mInfo, null);
		}

		mViolationInfos = (ArrayList<ViolationInfo>) data;
		PostViolationInfo temp = DaoControl.getInstance().getByCarno(mInfo.getCarno());
		String viloations = "";
		if (temp == null) {
			mInfo.setTime(System.currentTimeMillis());
			viloations = generateInfos();
			mInfo.setInfos(viloations);
			DaoControl.getInstance().insertIllegalInfo(mInfo);
		} else {
			if (temp.getInfos() == null || System.currentTimeMillis() - temp.getTime() > DAY_3) {
				mInfo.setTime(System.currentTimeMillis());
				viloations = generateInfos();
				mInfo.setInfos(viloations);
				DaoControl.getInstance().insertIllegalInfo(mInfo);
			}
		}

		mAdapter = new QueryIllegalAdapter(this, mViolationInfos, mBtnClickListener);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(null);

		for (int i = 0; i < mViolationInfos.size(); i++) {
			ViolationInfo mViolationInfo = mViolationInfos.get(i);
			if (mViolationInfo.getHandled().equals(ViolationInfo.HANDLED_NO)) {
				count++;
			}

			money = money + MyParse.parseInt(mViolationInfo.getMoney());
			fen = fen + MyParse.parseInt(mViolationInfo.getFen());

		}
		mStringBuilder.append("车牌号为");
		mStringBuilder.append(mInfo.getCarno().toUpperCase());
		mStringBuilder.append("用户,");
		mStringBuilder.append("有");
		mStringBuilder.append(count);
		mStringBuilder.append("条未处理的违章记录;");
		mStringBuilder.append("您总共违章");
		mStringBuilder.append(mViolationInfos.size());
		mStringBuilder.append("次");
		// mStringBuilder.append("罚款");
		// mStringBuilder.append(money);
		// mStringBuilder.append("元，");
		// mStringBuilder.append("扣分");
		// mStringBuilder.append(fen);
		// mStringBuilder.append("分");
		mTextViewSecretary.setText(mStringBuilder);
	}

	/**
	 * @return
	 */
	private String generateInfos() {
		String viloations;
		try {
			JSONArray jsArry = new JSONArray();
			for (ViolationInfo info : mViolationInfos) {
				JSONObject jobj = new JSONObject();
				jobj.put("date", info.getDate());
				jobj.put("area", info.getArea());
				jobj.put("act", info.getAct());
				jobj.put("code", info.getCode());
				jobj.put("fen", info.getFen());
				jobj.put("money", info.getMoney());
				jobj.put("handled", info.getHandled());
				jobj.put("shareLink", info.getShareLink());
				jobj.put("shareTitle", info.getShareTitle());
				jobj.put("shareText", info.getShareText());
				jsArry.put(jobj);
			}
			viloations = jsArry.toString();
		} catch (JSONException e) {
			e.printStackTrace();
			viloations = "[]";
		}
		return viloations;
	}

	public final static int[] ERRO = { 2900, 2901, 2902, 2903, 2904, 2905, 2906, 2910 };

	public final static int[] ERRO_JUHE = { 202, 203, 204, 205, 206, 207, 208 };

	@Override
	protected void LoadErro(Object erro) {
		DaoControl.getInstance().deletePostViolationInfo(mInfo.getCarno());
		// BaseResponseInfo mInfo = (BaseResponseInfo) erro;
		// 在数组中搜索是否含有5
		// int result = Arrays.binarySearch(ERRO, mInfo.getFlag());
		// if (result >= 0) {
		// LoginInfo.canQueryVio = "0";
		// Intent mIntent = new Intent(CarQueryIllegalActivity.this,
		// CarQueryIllegalFirstActivity.class);
		// startActivity(mIntent);
		// finish();
		// return;
		// } else if (mInfo.getFlag() == 2911) {
		// // 错误码2911显示的内容为：您好，您所查询的城市正在维护或未开通查询！
		// LoginInfo.canQueryVio = "1";
		// }

		// if (mInfo.getFlag() == ERRO_JUHE[6]) {
		// // 您好,你所查询的城市正在维护或未开通查询
		// // LoginInfo.canQueryVio = "1";
		// } else if (mInfo.getFlag() == ERRO_JUHE[0] || mInfo.getFlag() ==
		// ERRO_JUHE[2]
		// || mInfo.getFlag() == ERRO_JUHE[3] || mInfo.getFlag() ==
		// ERRO_JUHE[4]) {
		// mInfo.setInfo("车辆信息错误，请确认输入的信息是否正确");
		// } else {
		// mInfo.setInfo("由于网络原因，查询失败,请稍后重试！");
		// }

		super.LoadErro(erro);
	}

	@Override
	protected void LoadData() {
		super.LoadData();
		if (mInfo == null) {
			mInfo = new PostViolationInfo();
			mInfo.setCarno(LoginInfo.getCarno());
			mInfo.setCityCodeId(LoginInfo.getCity_code());
			mInfo.setEngineno(LoginInfo.getEngineno());
			mInfo.setRegistno(LoginInfo.getRegistno());
			mInfo.setStandcarno(LoginInfo.getShortstandcarno());
		}

		final PostViolationInfo temp = DaoControl.getInstance().getByCarno(mInfo.getCarno());
		boolean isSame = temp != null;
		isSame = isSame && (temp.getCarno() == null ? false : temp.getCarno().equals(mInfo.getCarno()));
		isSame = isSame && (temp.getCityCodeId() == null ? false : temp.getCityCodeId().equals(mInfo.getCityCodeId()));
		isSame = isSame && (temp.getEngineno() == null ? (temp.getEngineno() == mInfo.getEngineno()) : temp.getEngineno().equals(mInfo.getEngineno()));
		isSame = isSame && (temp.getRegistno() == null ? (null == mInfo.getRegistno()) : (temp.getRegistno().equals(mInfo.getRegistno())));
		isSame = isSame && (temp.getStandcarno() == null ? (null == mInfo.getStandcarno()) : temp.getStandcarno().equals(mInfo.getStandcarno()));
		isSame = isSame && (temp.getInfos() != null && temp.getInfos().length() > 0);
		isSame = isSame && System.currentTimeMillis() - temp.getTime() < DAY_3;

		if (isSame) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						ArrayList<ViolationInfo> list = new ArrayList<ViolationInfo>();
						JSONArray jarray = new JSONArray(temp.getInfos());
						for (int i = 0; i < jarray.length(); i++) {
							ViolationInfo vInfo = new ViolationInfo();
							JSONObject obj = jarray.getJSONObject(i);
							vInfo.setDate(obj.getString("date"));
							vInfo.setArea(obj.getString("area"));
							vInfo.setAct(obj.getString("act"));
							vInfo.setCode(obj.getString("code"));
							vInfo.setFen(obj.getString("fen"));
							vInfo.setMoney(obj.getString("money"));
							vInfo.setHandled(obj.getString("handled"));
							vInfo.setShareLink(obj.getString("shareLink"));
							vInfo.setShareTitle(obj.getString("shareTitle"));
							vInfo.setShareText(obj.getString("shareText"));
							list.add(vInfo);
						}
						listener.onFinished(list);
						return;
					} catch (JSONException e) {
						e.printStackTrace();
						listener.onErro(null);
					}
				}
			}).start();
		} else {
			//聚合违章查询
			CPControl.GetViolationListNewResult(mInfo, listener);
			
			//车行易违章查询。。。
//			CPControl.GetViolationList2Result(mInfo, listener);
			
		}
	}

	private OnBtnClickListener mBtnClickListener = new OnBtnClickListener() {

		@Override
		public void onClickInfo(ViolationInfo violationInfo) {
			Intent mIntent = new Intent(CarQueryIllegalActivity.this, CarQueryIllegalDetailActivity.class);
			Bundle mBundle = new Bundle();
			mBundle.putSerializable(CarQueryIllegalDetailActivity.VIOLATION_INFO, violationInfo);
			mIntent.putExtra("Bundle", mBundle);
			startActivity(mIntent);
		}

		@Override
		public void onClickShare(ViolationInfo violationInfo, View shareView) {
//			ShareControl.share(CarQueryIllegalActivity.class, CarQueryIllegalActivity.this, violationInfo.getShareTitle(), violationInfo.getShareText(),
//					violationInfo.getShareLink(), shareView);

		}
	};

	@Override
	public void onClick(View arg0) {
	}
	
	@Override
	public void onBackPressed() {
		
		super.onBackPressed();
	}
}
