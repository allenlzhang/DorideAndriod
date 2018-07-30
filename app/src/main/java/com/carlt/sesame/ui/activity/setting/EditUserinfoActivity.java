
package com.carlt.sesame.ui.activity.setting;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.doride.utils.LocalConfig;
import com.carlt.sesame.control.CPControl;
import com.carlt.sesame.control.CPControl.GetResultListCallback;
import com.carlt.sesame.data.BaseResponseInfo;
import com.carlt.sesame.data.SesameLoginInfo;
import com.carlt.sesame.data.set.TransferOldCheckInfo;
import com.carlt.sesame.http.AsyncImageLoader;
import com.carlt.sesame.ui.activity.base.BaseActivity;
import com.carlt.sesame.ui.activity.transfer.TransferCheckActivity;
import com.carlt.sesame.ui.activity.transfer.TransferHandleActivity;
import com.carlt.sesame.ui.view.PopBoxCreat;
import com.carlt.sesame.ui.view.UUPopupWindow;
import com.carlt.sesame.ui.view.UUTwoCodeDialog;
import com.carlt.sesame.utility.FileUtil;
import com.carlt.sesame.utility.ImageUtils;
import com.carlt.sesame.utility.Log;
import com.carlt.sesame.utility.PhotoUtils;
import com.carlt.sesame.utility.UUToast;

import java.io.File;

/**
 * 设置-修改资料主页面
 * @author daisy
 */
public class EditUserinfoActivity extends BaseActivity {
    private ImageView back;// 头部返回键

    private TextView title;// 标题文字

    private TextView txtRight;// 头部右侧文字

    private View mView1;// 修改头像

    private View mView2;// 修改手机号

    private View mView3;// 修改密码

    private View mView4;// 修改姓名/昵称

    private View mView5;// 修改性别

    private View mView6;//生成二维码

    private ImageView mImageView;// 用户头像

    private TextView mTextView1;// 手机号

    private TextView mTextView2;// 密码

    private TextView mTextView3;// 用户名

    private TextView mTextView4;// 性别


    private UUPopupWindow mPopupWindow;// 修改头像时底部弹出的popupWindow

    private AsyncImageLoader mAsyncImageLoader = AsyncImageLoader.getInstance();
    //
    private UUTwoCodeDialog mQRCodeDialog;

    public final static int REQUESTCODE = 2222;

    public final static String NEWDATA = "newdata";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_userinfo);
        initTitle();
        init();
    }

    private void initTitle() {
        back = (ImageView) findViewById(R.id.head_back_img1);
        title = (TextView) findViewById(R.id.head_back_txt1);
        txtRight = (TextView) findViewById(R.id.head_back_txt2);

        back.setImageResource(R.drawable.arrow_back);
        title.setText("修改资料");
        txtRight.setVisibility(View.GONE);

        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void init() {

        mView1 = findViewById(R.id.activity_setting_userinfo_layout1);
        mView2 = findViewById(R.id.activity_setting_userinfo_layout2);
        mView3 = findViewById(R.id.activity_setting_userinfo_layout3);
        mView4 = findViewById(R.id.activity_setting_userinfo_layout4);
        mView5 = findViewById(R.id.activity_setting_userinfo_layout5);
        mView6 = findViewById(R.id.activity_setting_userinfo_layout6);

        mImageView = (ImageView) findViewById(R.id.activity_setting_userinfo_img);
        mTextView1 = (TextView) findViewById(R.id.activity_setting_userinfo_txt1);
        mTextView2 = (TextView) findViewById(R.id.activity_setting_userinfo_txt2);
        mTextView3 = (TextView) findViewById(R.id.activity_setting_userinfo_txt3);
        mTextView4 = (TextView) findViewById(R.id.activity_setting_userinfo_txt4);

        OnClickListener l = new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mPopupWindow == null) {
                    initPopwindow();
                }

                mPopupWindow.showAtLocation(mPopupWindowView, Gravity.BOTTOM, 0, 0);
                mPopupWindowView.startAnimation(ani1);
            }
        };
        mView1.setOnClickListener(l);
        mView2.setOnClickListener(mClickListener);
        mView3.setOnClickListener(mClickListener);
        mView4.setOnClickListener(mClickListener);
        mView5.setOnClickListener(mClickListener);
        mView6.setOnClickListener(mClickListener);

        if (!SesameLoginInfo.isMain()) {
            mView6.setVisibility(View.GONE);
        }

    }

    private void loadData() {
        String imgUrl = SesameLoginInfo.getAvatar_img();
        if (imgUrl != null && imgUrl.length() > 0) {
            Bitmap mBitmap = mAsyncImageLoader.getBitmapByUrl(SesameLoginInfo.getAvatar_img());
            if (mBitmap != null) {
                mImageView.setImageBitmap(mBitmap);
            } else {
                mImageView.setImageResource(R.drawable.icon_default_head);
            }
        } else {
            mImageView.setImageResource(R.drawable.icon_default_head);
        }

        String mobile = SesameLoginInfo.getMobile();
        if (mobile != null && mobile.length() == 11) {
            String s1 = mobile.substring(0, 3);
            String s2 = mobile.substring(7);
            StringBuffer mBuffer = new StringBuffer(s1);
            mBuffer.append("****");
            mBuffer.append(s2);
            mTextView1.setText(mBuffer);
        }

        mTextView3.setText(SesameLoginInfo.getRealname());

        if (SesameLoginInfo.getGender().equals(SesameLoginInfo.GENDER_NAN)) {
            mTextView4.setText("男");
        } else if (SesameLoginInfo.getGender().equals(SesameLoginInfo.GENDER_NV)) {
            mTextView4.setText("女");
        } else if (SesameLoginInfo.getGender().equals(SesameLoginInfo.GENDER_MI)) {
            mTextView4.setText("保密");
        }

    }

    private OnClickListener mClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            int type = 0;
            switch (v.getId()) {

                case R.id.activity_setting_userinfo_layout2:
                    // 修改手机号
                    Intent mIntent1 = new Intent(EditUserinfoActivity.this,
                            EditPhoneActivity1.class);
                    startActivityForResult(mIntent1, REQUESTCODE);
                    break;
                case R.id.activity_setting_userinfo_layout3:
                    // 修改密码
                    type = EditUserinfoDetailActivity.TYPE_PASSWORD;
                    Intent mIntent2 = new Intent(EditUserinfoActivity.this,
                            EditUserinfoDetailActivity.class);
                    mIntent2.putExtra(EditUserinfoDetailActivity.EDIT_TYPE, type);
                    startActivityForResult(mIntent2, REQUESTCODE);
                    break;
                case R.id.activity_setting_userinfo_layout4:
                    // 修改用户名
                    type = EditUserinfoDetailActivity.TYPE_NAME;
                    Intent mIntent3 = new Intent(EditUserinfoActivity.this,
                            EditUserinfoDetailActivity.class);
                    mIntent3.putExtra(EditUserinfoDetailActivity.EDIT_TYPE, type);
                    startActivityForResult(mIntent3, REQUESTCODE);
                    break;
                case R.id.activity_setting_userinfo_layout5:
                    // 修改性别
                    type = EditUserinfoDetailActivity.TYPE_SEX;
                    Intent mIntent4 = new Intent(EditUserinfoActivity.this,
                            EditUserinfoDetailActivity.class);
                    mIntent4.putExtra(EditUserinfoDetailActivity.EDIT_TYPE, type);
                    startActivityForResult(mIntent4, REQUESTCODE);
                    break;
                case R.id.activity_setting_userinfo_layout6:
                    //弹出安全验证页面

                    if (!SesameLoginInfo.isAuthen()) {
                        UUToast.showUUToast(context, "您还没有实名认证，请先实名认证");
                        return;
                    }

                    Intent mIntent = new Intent();
                    mIntent.setClass(context, TransferCheckActivity.class);
                    startActivityForResult(mIntent, QRCode);
                    break;
            }
        }
    };

    @Override
    public void OnImgLoadFinished(String url, Bitmap mBitmap) {
        super.OnImgLoadFinished(url, mBitmap);

        if (SesameLoginInfo.getAvatar_img().equals(url) && mBitmap != null) {
            mImageView.setImageBitmap(mBitmap);
        }
    }

    /**
     * 以下部分是用于头像拍照上传
     **/

    public static final int NONE = 0;

    public static final int PHOTOHRAPH = 1;// 拍照

    public static final int PHOTOZOOM = 2; // 缩放

    public static final int PHOTORESOULT = 3;// 结果

    public static final int QRCode = 4;

    public static final String IMAGE_UNSPECIFIED = "image/*";

    private String ImageName;

    // 调用startActivityResult，返回之后的回调函数
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == NONE)
            return;

        if (requestCode == QRCode) {

            if (data != null && !TextUtils.isEmpty(data.getStringExtra("code"))) {
                // 二维码生成成功，显示二维码对话框
                Bitmap bit = ImageUtils.getBarCode(data.getStringExtra("code"), 600, 600);
                if (null != bit) {
                    mQRCodeDialog = PopBoxCreat.showTwoCodeDialog(context, bit, new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            mUploadHandler.removeMessages(5);
                        }
                    });
                    ////---循环调用接口，验证二维码有没有被扫过
                    mUploadHandler.sendEmptyMessage(5);
                }
            }

            return;
        }
        int output_X = 480, output_Y = 480;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODE_CAMERA_REQUEST://拍照完成回调
//                    upload(imageUri.getPath());
                    cropImageUri = Uri.fromFile(fileCropUri);
                    PhotoUtils.cropImageUri(this, imageUri, cropImageUri, 1, 1, output_X, output_Y, CODE_RESULT_REQUEST);
                    break;
                case CODE_RESULT_REQUEST:
                    Bitmap bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, this);
//                    setPicToView(bitmap);
                    uploadAvater(bitmap);
                    break;
                case CODE_GALLERY_REQUEST://访问相册完成回调
                    cropImageUri = Uri.fromFile(fileCropUri);
                    Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                        newUri = FileProvider.getUriForFile(this, "com.carlt.sesame.fileprovider", new File(newUri.getPath()));
                    PhotoUtils.cropImageUri(this, newUri, cropImageUri, 1, 1, output_X, output_Y, CODE_RESULT_REQUEST);
                    break;
            }
        }
        // 拍照
//        if (requestCode == PHOTOHRAPH) {
//            //压缩
//            upload(LocalConfig.mImageCacheSavePath_SD + ImageName);
//            return;
//        }

        if (data == null)
            return;

        // 读取相册
//        if (requestCode == PHOTOZOOM) {
//            String[] proj = {
//                    MediaStore.Images.Media.DATA
//            };
//            // 好像是android多媒体数据库的封装接口，具体的看Android文档
//            Cursor cursor = managedQuery(data.getData(), proj, null, null, null);
//            // 按我个人理解 这个是获得用户选择的图片的索引值
//            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            // 将光标移至开头 ，这个很重要，不小心很容易引起越界
//            cursor.moveToFirst();
//            // 最后根据索引值获取图片路径
//            String path = cursor.getString(column_index);
//            upload(path);
//            return;
//        }

        if (resultCode == REQUESTCODE + 2) {
            // 修改手机号
            String newData = data.getStringExtra(NEWDATA);
            mTextView1.setText(newData);
        }
        if (resultCode == REQUESTCODE + 4) {
            // 修改姓名
            String newData = data.getStringExtra(NEWDATA);
            mTextView3.setText(newData);
        }
        if (resultCode == REQUESTCODE + 5) {
            // 修改性别
            String newData = data.getStringExtra(NEWDATA);
            if (newData.equals("1")) {
                mTextView4.setText("男");
            } else if (newData.equals("2")) {
                mTextView4.setText("女");
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadAvater(Bitmap bitmap) {
        String filePath = FileUtil.saveFile(EditUserinfoActivity.this, "temphead.jpg", bitmap);
        android.util.Log.e("----------路径----------" ,filePath) ;
        upload(filePath);
    }

    // 调用系统的相册
    public void usePhoto() {

        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
        // 调用剪切功能
        startActivityForResult(intent, PHOTOZOOM);
    }



    private Dialog mDialog;

    private void upload(String mFilePath) {

        Log.e("info22222", "path==" + mFilePath);
        mDialog = PopBoxCreat.createDialogWithProgress(EditUserinfoActivity.this, "正在上传...");
        mDialog.show();
        File file = new File(mFilePath);
        Log.e("----",file.exists()+"");
        if (file.exists()) {
            File f = ImageUtils.getSmallImageFile(getApplicationContext(), mFilePath, 200, 200, true);
            CPControl.GetUpadeAvatarResult(f.getAbsolutePath(), listener);
        } else {
            listener.onErro(null);
        }
    }

    private GetResultListCallback listener = new GetResultListCallback() {

        @Override
        public void onFinished(Object o) {
            Message msg = new Message();
            msg.what = 0;
            msg.obj = o;
            mUploadHandler.sendMessage(msg);
        }

        @Override
        public void onErro(Object o) {
            Message msg = new Message();
            msg.what = 1;
            msg.obj = o;
            mUploadHandler.sendMessage(msg);
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler mUploadHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                    Bitmap photo = BitmapFactory.decodeFile(msg.obj.toString());
                    mImageView.setImageBitmap(photo);
                    break;
                case 1:
                    if (mDialog != null) {
                        mDialog.dismiss();
                    }
                    BaseResponseInfo mInfo = (BaseResponseInfo) msg.obj;
                    Log.e("-----",mInfo.toString());
                    if (mInfo != null && mInfo.getInfo() != null && mInfo.getInfo().length() > 0) {
                        UUToast.showUUToast(EditUserinfoActivity.this, mInfo.getInfo());
                    } else {
                        UUToast.showUUToast(EditUserinfoActivity.this, "上传失败");
                    }
                    break;
                case 3:
                    ///--验证二维码调用失败
                    if (mQRCodeDialog != null && mQRCodeDialog.isShowing()) {
                        sendEmptyMessageDelayed(5, 1000);
                    }
                    break;
                case 4:
                    TransferOldCheckInfo toi = (TransferOldCheckInfo) msg.obj;
                    if (null != toi) {
                        String isHave = toi.getIshave();
                        if (!TextUtils.isEmpty(isHave) && isHave.equals("1")) {
                            if (mQRCodeDialog != null && mQRCodeDialog.isShowing()) {
                                mQRCodeDialog.dismiss();
                            }
                            Intent mIntent = new Intent(context, TransferHandleActivity.class);
                            mIntent.putExtra("outtingid", toi.getOuttingid());
                            mIntent.putExtra("mobile", toi.getMobile());
                            startActivity(mIntent);
                            return;
                        }
                    }

                    if (mQRCodeDialog != null && mQRCodeDialog.isShowing()) {
                        sendEmptyMessageDelayed(5, 1000);
                    }

                    break;
                case 5:
                    ////--调用验证二维码是否被扫接口
                    CPControl.GetTransferOldCheckResult(m_tracsfer_check);
                    break;
            }
        }
    };

    private View mPopupWindowView;

    private Animation ani1;
    private File fileUri     = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg");
    private Uri imageUri;
    private Uri cropImageUri;
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST  = 0xa1;
    private static final int CODE_RESULT_REQUEST  = 0xa2;

    private void initPopwindow() {

        Animation ani2;
        ani1 = AnimationUtils.loadAnimation(EditUserinfoActivity.this,
                R.anim.enter_menu_personevaluation);
        ani2 = AnimationUtils.loadAnimation(EditUserinfoActivity.this,
                R.anim.exit_menu_personevaluation);

        LayoutInflater mInflater = LayoutInflater.from(EditUserinfoActivity.this);
        mPopupWindowView = mInflater.inflate(R.layout.layout_edit_head_icon, null);

        mPopupWindow = new UUPopupWindow(mPopupWindowView, LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT);

        View mPopupWindowBg = mPopupWindowView.findViewById(R.id.layout_edit_head_icon);
        mPopupWindowBg.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });

        mPopupWindowView.setFocusableInTouchMode(true);
        mPopupWindowView.setOnKeyListener(new OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK && mPopupWindow.isShowing()
                        && event.getAction() == KeyEvent.ACTION_UP) {
                    mPopupWindow.dismiss();

                }
                return true;
            }
        });
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.initAni(mPopupWindowView, ani2);
        mPopupWindow.setFocusable(true);
        mPopupWindow.update();
        mPopupWindow.setOutsideTouchable(false);

        OnClickListener mListener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.layout_edit_head_icon_txt1:
                        requestPermissions(EditUserinfoActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, new RequestPermissionCallBack() {
                            @Override
                            public void granted() {
//                                ImageName = System.currentTimeMillis() + ".jpg";
                                imageUri = Uri.fromFile(fileUri);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                                    //通过FileProvider创建一个content类型的Uri
                                    imageUri = FileProvider.getUriForFile(EditUserinfoActivity.this, "com.carlt.sesame.fileprovider", fileUri);
                                PhotoUtils.takePicture(EditUserinfoActivity.this, imageUri, CODE_CAMERA_REQUEST);
                            }

                            @Override
                            public void denied() {
                                UUToast.showUUToast(EditUserinfoActivity.this, "部分权限获取失败，正常功能受到影响");
                            }
                        });
                        if (mPopupWindow != null && mPopupWindow.isShowing()) {
                            mPopupWindow.dismiss();
                        }
                        //                        useCamero();
                        break;

                    case R.id.layout_edit_head_icon_txt2:
                        requestPermissions(EditUserinfoActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, new RequestPermissionCallBack() {
                            @Override
                            public void granted() {
                                PhotoUtils.openPic(EditUserinfoActivity.this, CODE_GALLERY_REQUEST);
                            }

                            @Override
                            public void denied() {
                                UUToast.showUUToast(EditUserinfoActivity.this,"部分权限获取失败，正常功能受到影响");
                            }
                        });
//                        调用系统相册
                        if (mPopupWindow != null && mPopupWindow.isShowing()) {
                            mPopupWindow.dismiss();
                        }
//                        usePhoto();
                        break;
                    case R.id.layout_edit_head_icon_txt3:
                        mPopupWindow.dismiss();
                        break;
                }

            }
        };

        TextView mBtn1 = (TextView) mPopupWindowView.findViewById(R.id.layout_edit_head_icon_txt1);
        TextView mBtn2 = (TextView) mPopupWindowView.findViewById(R.id.layout_edit_head_icon_txt2);
        TextView mBtn3 = (TextView) mPopupWindowView.findViewById(R.id.layout_edit_head_icon_txt3);

        mBtn1.setOnClickListener(mListener);
        mBtn2.setOnClickListener(mListener);
        mBtn3.setOnClickListener(mListener);

    }
    public void useCamero() {
        ImageName = System.currentTimeMillis() + ".jpg";
        // 调用系统的拍照功能
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(LocalConfig.mImageCacheSavePath_SD, ImageName)));
        startActivityForResult(intent, PHOTOHRAPH);
    }
    @Override
    protected void onStart() {
        super.onStart();
        loadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    public GetResultListCallback m_tracsfer_check = new GetResultListCallback() {

        @Override
        public void onFinished(Object o) {
            Message msg = new Message();
            msg.what = 4;
            msg.obj = o;
            mUploadHandler.sendMessage(msg);
        }

        @Override
        public void onErro(Object o) {
            mUploadHandler.sendEmptyMessage(3);
        }
    };


}
