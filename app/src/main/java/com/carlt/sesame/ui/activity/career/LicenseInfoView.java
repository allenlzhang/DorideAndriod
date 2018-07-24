
package com.carlt.sesame.ui.activity.career;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.carlt.sesame.R;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.data.LoginInfo;
import com.carlt.sesame.data.career.MedalInfo;
import com.carlt.sesame.data.career.UserLicenseInfo;
import com.carlt.sesame.http.AsyncImageLoader;
import com.carlt.sesame.ui.activity.base.BaseLoadingView;
import com.carlt.sesame.utility.GetTypeFace;

import java.util.ArrayList;

/**
 * 我的驾驶证-tab1页面
 * 
 * @author daisy
 */
public class LicenseInfoView extends BaseLoadingView {
    private TextView mTextView1;// 驾驶证等级-类型

    private TextView mTextView2;// 驾驶证等级-数字

    private TextView mTextView3;// 获得的称号1

    private TextView mTextView4;// 获得的称号2

    private TextView mTextView5;// 获得的称号3

    private TextView mTextView6;// 用户姓名（右侧drawable为性别图标）

    private TextView mTextView7;// 用户车型

    private TextView mTextView8;// 积分

    private TextView mTextView9;// 行驶总里程

    private TextView mTextView10;// 行驶总油耗

    private TextView mTextView11;// 获得的勋章个数

    private TextView mTextView12;// 编号

    private TextView mTextView13;// 发证日期

    private TextView mTextViewMedal1;// 勋章类别

    private TextView mTextViewMedal2;// 勋章类别

    private TextView mTextViewMedal3;// 勋章类别

    private TextView mTextViewMedal4;// 勋章类别

    private ImageView mImageView1;// 驾驶证等级-图腾

    private ImageView mImageView2;// 用户头像

    private ImageView mImageView3;// 用户座驾icon

    private ImageView mImageView4;// 通行证邮戳

    private ImageView mImageViewMedal1;// 勋章图片

    private ImageView mImageViewMedal2;// 勋章图片

    private ImageView mImageViewMedal3;// 勋章图片

    private ImageView mImageViewMedal4;// 勋章图片

    private ProgressBar mProgressBar;// 驾驶证等级条

    private View mView1;// 驾驶证等级整个布局

    private View mView2;// 勋章图片整行

    private Context mContext;

    private ArrayList<TextView> mMedalTextViews;

    private ArrayList<ImageView> mMedalImageViews;

    private ArrayList<MedalInfo> medalInfos;

    private UserLicenseInfo mUserLicenseInfo;

    private Resources mResources;

    public LicenseInfoView(Context context) {
        super(context);
        setContent(R.layout.layout_license_info);

        mContext = context;
        mResources = context.getResources();
        init();
        initMedal();

    }

    private void init() {
        mTextView1 = (TextView)findViewById(R.id.layout_license_info_txt1);
        mTextView2 = (TextView)findViewById(R.id.layout_license_info_txt2);
        mTextView3 = (TextView)findViewById(R.id.layout_license_info_txt3);
        mTextView4 = (TextView)findViewById(R.id.layout_license_info_txt4);
        mTextView5 = (TextView)findViewById(R.id.layout_license_info_txt5);
        mTextView6 = (TextView)findViewById(R.id.layout_license_info_txt6);
        mTextView7 = (TextView)findViewById(R.id.layout_license_info_txt7);
        mTextView8 = (TextView)findViewById(R.id.layout_license_info_txt8);
        mTextView9 = (TextView)findViewById(R.id.layout_license_info_txt9);
        mTextView10 = (TextView)findViewById(R.id.layout_license_info_txt10);
        mTextView11 = (TextView)findViewById(R.id.layout_license_info_txt11);
        mTextView12 = (TextView)findViewById(R.id.layout_license_info_txt12);
        mTextView13 = (TextView)findViewById(R.id.layout_license_info_txt13);

        mImageView1 = (ImageView)findViewById(R.id.layout_license_info_img1);
        mImageView2 = (ImageView)findViewById(R.id.layout_license_info_img2);
        mImageView3 = (ImageView)findViewById(R.id.layout_license_info_img3);
        mImageView4 = (ImageView)findViewById(R.id.layout_license_info_img4);

        mProgressBar = (ProgressBar)findViewById(R.id.layout_license_info_progress);

        mView1 = findViewById(R.id.layout_license_info_layout1);
        mView2 = findViewById(R.id.layout_license_info_layout4);

        mTextView11.setOnClickListener(mListener);
        mView1.setOnClickListener(mListener);
        mView2.setOnClickListener(mListener);

    }

    private void initMedal() {
        mMedalTextViews = new ArrayList<TextView>();
        mMedalImageViews = new ArrayList<ImageView>();

        mTextViewMedal1 = (TextView)findViewById(R.id.layout_license_info_medal_txt1);
        mTextViewMedal2 = (TextView)findViewById(R.id.layout_license_info_medal_txt2);
        mTextViewMedal3 = (TextView)findViewById(R.id.layout_license_info_medal_txt3);
        mTextViewMedal4 = (TextView)findViewById(R.id.layout_license_info_medal_txt4);

        mImageViewMedal1 = (ImageView)findViewById(R.id.layout_license_info_medal_img1);
        mImageViewMedal2 = (ImageView)findViewById(R.id.layout_license_info_medal_img2);
        mImageViewMedal3 = (ImageView)findViewById(R.id.layout_license_info_medal_img3);
        mImageViewMedal4 = (ImageView)findViewById(R.id.layout_license_info_medal_img4);

        mMedalTextViews.add(mTextViewMedal1);
        mMedalTextViews.add(mTextViewMedal2);
        mMedalTextViews.add(mTextViewMedal3);
        mMedalTextViews.add(mTextViewMedal4);

        mMedalImageViews.add(mImageViewMedal1);
        mMedalImageViews.add(mImageViewMedal2);
        mMedalImageViews.add(mImageViewMedal3);
        mMedalImageViews.add(mImageViewMedal4);
    }

    private AsyncImageLoader mAsyncImageLoader;

    @Override
    protected void LoadSuccess(Object data) {
        mUserLicenseInfo = (UserLicenseInfo)data;
        mAsyncImageLoader = AsyncImageLoader.getInstance();

        String mString;

        // 驾驶证等级图腾
        mString = mUserLicenseInfo.getLicenceImg();
        setImgResource(mString, mImageView1, 0);

        // 驾驶证名称
        mString = mUserLicenseInfo.getLicenceName();
        if (mString != null && mString.length() > 0) {
            mTextView1.setText(mString);
        } else {
            mTextView1.setText("");
        }

        // 驾驶证等级
        mString = mUserLicenseInfo.getLicenceLevel();
        if (mString != null && mString.length() > 0) {
            mTextView2.setText(mString);
        } else {
            mTextView2.setText("");
        }

        // 进度条
        mProgressBar.setProgress(mUserLicenseInfo.getLicencePercent());

        // 获得的称号
        String tag = mUserLicenseInfo.getLicenceTag();
        if (tag != null && !tag.equals("")) {
            String[] tags = tag.split(",");
            if (tags != null && tags.length > 0) {
                mTextView3.setVisibility(View.VISIBLE);
                mTextView3.setText(tags[0] + "");

                if (tags.length == 2) {
                    mTextView4.setVisibility(View.VISIBLE);
                    mTextView4.setText(tags[1] + "");
                } else if (tags.length >= 3) {
                    mTextView4.setVisibility(View.VISIBLE);
                    mTextView4.setText(tags[1] + "");
                    mTextView5.setVisibility(View.VISIBLE);
                    mTextView5.setText(tags[2] + "");
                }
            }
        }

        // 用户头像
        mString = LoginInfo.getAvatar_img();
        setImgResource(mString, mImageView2, 1);

        // 用户姓名
        mString = LoginInfo.getRealname();
        if (mString != null && mString.length() > 0) {
            mTextView6.setText(mString);
        } else {
            mTextView6.setText("路人甲");
        }

        // 用户性别
        mString = LoginInfo.getGender();
        if (mString == null) {
            mString = LoginInfo.GENDER_MI;
        }
        if (LoginInfo.getGender().equals(LoginInfo.GENDER_NAN)) {
            //男
            mTextView6.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources()
                    .getDrawable(R.drawable.icon_sex_male), null);
        } else if (LoginInfo.getGender().equals(LoginInfo.GENDER_NV)) {
            //女
            mTextView6.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources()
                    .getDrawable(R.drawable.icon_sex_female), null);
        } else{
            //保密
            mTextView6.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources()
                    .getDrawable(R.drawable.icon_sex_secret), null);
        }

        // 汽车名称
        mString = LoginInfo.getCarname();
        if (mString != null && mString.length() > 0) {
            mTextView7.setText(mString);
        } else {
            mTextView7.setText("");
        }

        // 汽车logo
        mString = LoginInfo.getCarlogo();
        if (mString != null && mString.length() > 0) {
            setImgResource(mString, mImageView3, 2);
        }

        // 积分
        mString = mUserLicenseInfo.getCredit();
        if (mString != null && mString.length() > 0 && !mString.equals("null")) {
            mTextView8.setText("积分：" + mString);
        } else {
            mTextView8.setText("积分：0");
        }

        // 总里程
        mString = LoginInfo.getSummileage();
        if (mString != null && mString.length() > 0) {
            mTextView9.setText(mString);
        }
        mTextView9.setTypeface(GetTypeFace.typefaceThin(mResources));

        // 总油耗
        mString = mUserLicenseInfo.getSumfuel();
        if (mString != null && mString.length() > 0) {
            mTextView10.setText(mString);
        }
        mTextView10.setTypeface(GetTypeFace.typefaceThin(mResources));

        // 驾驶证编号
        mString = mUserLicenseInfo.getLicencenumber();
        if (mString != null && mString.length() > 0) {
            mTextView12.setText("编号：" + mString);
        } else {
            mTextView12.setText("编号：");
        }

        // 发证日期
        mString = mUserLicenseInfo.getLicencedate();
        if (mString != null && mString.length() > 0) {
            mTextView13.setText("发证：" + mString);
        } else {
            mTextView13.setText("发证：");
        }

        // 勋章列表

        medalInfos = mUserLicenseInfo.getmUserMediaInfo().getmList();
        setMedalData();

        super.LoadSuccess(data);

    }

    /**
     * 设置用户勋章信息
     * 
     */
    private void setMedalData() {
        int size_get = 0;// 已获得的勋章个数
        int size;// 勋章列表个数
        if (medalInfos != null) {
            size = medalInfos.size();
            for (int i = 0; i < size; i++) {
                MedalInfo mMedalInfo = medalInfos.get(i);
                if (mMedalInfo.isIsgot()) {
                    size_get++;
                }
                if (i < 4) {
                    if (mMedalInfo.isIsgot()) {
                        setImgResource(mMedalInfo.getIconUrl2(), mMedalImageViews.get(i), 3);
                    } else {
                        setImgResource(mMedalInfo.getIconUrl1(), mMedalImageViews.get(i), 3);
                    }
                    mMedalTextViews.get(i).setText(mMedalInfo.getName());
                }
            }
        }

        mTextView11.setText("我解锁了" + size_get + "枚勋章");
    }

    /**
     * 设置图片
     * 
     */
    private void setImgResource(String imgUrl, ImageView imgView, int type) {
        switch (type) {
            case 0:
                // 驾驶证等级图标
                if (mAsyncImageLoader.getBitmapByUrl(imgUrl) != null) {
                    imgView.setImageBitmap(mAsyncImageLoader.getBitmapByUrl(imgUrl));
                } else {
                    imgView.setImageResource(R.drawable.career_level_icon2);
                }
                break;
            case 1:
                // 用户头像图标
                if (mAsyncImageLoader.getBitmapByUrl(imgUrl) != null) {
                    imgView.setImageBitmap(mAsyncImageLoader.getBitmapByUrl(imgUrl));
                } else {
                    imgView.setImageResource(R.drawable.icon_default_head);
                }
                break;
            case 2:
                // 用户座驾icon
                if (mAsyncImageLoader.getBitmapByUrl(imgUrl) != null) {
                    imgView.setImageBitmap(mAsyncImageLoader.getBitmapByUrl(imgUrl));
                } else {
                    imgView.setImageResource(R.drawable.default_car_small);
                }
                break;
            case 3:
                // 勋章图标
                if (mAsyncImageLoader.getBitmapByUrl(imgUrl) != null) {
                    imgView.setImageBitmap(mAsyncImageLoader.getBitmapByUrl(imgUrl));
                } else {
                    imgView.setImageResource(R.drawable.icon_default_medal);
                }
                break;
        }

    }

    @Override
    protected void LoadErro(Object erro) {
        super.LoadErro(erro);
    }

    @Override
    protected void LoadData() {
        super.LoadData();
        CPControl.GetMyLicenseResult(listener);
    }

    private OnClickListener mListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.layout_license_info_layout1:
                    // 跳转至驾驶证等级页面
                    Intent mIntent1 = new Intent(mContext, LicenseLevelActivity.class);
                    mIntent1.putExtra(LicenseLevelActivity.LICENSE_LEVEL,
                            mUserLicenseInfo.getLicenceLevel());
                    mContext.startActivity(mIntent1);
                    break;
                case R.id.layout_license_info_txt11:
                    // 跳转至勋章
                    Intent mIntent2 = new Intent(mContext, MedalActivity.class);
                    mIntent2.putExtra("UserLicenseInfo", mUserLicenseInfo);
                    mContext.startActivity(mIntent2);
                    break;
                case R.id.layout_license_info_layout4:
                    // 跳转至勋章
                    Intent mIntent3 = new Intent(mContext, MedalActivity.class);
                    mIntent3.putExtra("UserLicenseInfo", mUserLicenseInfo);
                    mContext.startActivity(mIntent3);
                    break;
            }

        }
    };

    @Override
    public void refreshImage(String url, Bitmap mBitmap) {
        super.refreshImage(url, mBitmap);
        if (url != null) {

            if (mUserLicenseInfo != null && url.equals(mUserLicenseInfo.getLicenceImg())) {
                mImageView1.setImageBitmap(mBitmap);
                return;
            } else if (url.equals(LoginInfo.getAvatar_img())) {
                mImageView2.setImageBitmap(mBitmap);
                return;
            } else if (url.equals(LoginInfo.getCarlogo())) {
                mImageView3.setImageBitmap(mBitmap);
                return;
            } else if (medalInfos != null) {
                int size = medalInfos.size();
                if (size > 4) {
                    size = 4;
                }
                for (int i = 0; i < size; i++) {
                    if (url.equals(medalInfos.get(i).getIconUrl2())) {
                        mMedalImageViews.get(i).setImageBitmap(mBitmap);
                        return;
                    } else if (url.equals(medalInfos.get(i).getIconUrl1())) {
                        mMedalImageViews.get(i).setImageBitmap(mBitmap);
                    }
                }
            }
        }
    }

    @Override
    public void onShare() {
        super.onShare();
        try {
//            ShareControl.share(LicenseInfoView.class, (Activity)mContext,
//                    mUserLicenseInfo.getShareTitle(), mUserLicenseInfo.getShareText(),
//                    mUserLicenseInfo.getShareLink(), this);
//            CPControl.GetLicenseShareResult(mUserLicenseInfo.getLicenceLevel());
        } catch (Exception e) {
        }

        // ShareControl.share(LicenseInfoView.class, LoginInfo.mediaShareTitle,
        // LoginInfo.mediaShareText, LoginInfo.mediaShareLink, this);
    }

}
