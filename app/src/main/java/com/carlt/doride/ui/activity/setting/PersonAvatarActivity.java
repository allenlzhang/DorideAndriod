package com.carlt.doride.ui.activity.setting;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.carlt.doride.R;
import com.carlt.doride.base.LoadingActivity;
import com.carlt.doride.data.set.AvatarInfo;
import com.carlt.doride.http.HttpLinker;
import com.carlt.doride.model.LoginInfo;
import com.carlt.doride.protocolparser.user.AvatarParser;
import com.carlt.doride.systemconfig.URLConfig;
import com.carlt.doride.ui.view.PopBoxCreat;
import com.carlt.doride.ui.view.UUToast;
import com.carlt.doride.utils.FileUtil;
import com.carlt.doride.utils.PhotoUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import okhttp3.Response;

public class PersonAvatarActivity extends LoadingActivity implements OnClickListener {

    private static final String TAG = "PersonAvatarActivity";

    public static final String IMAGE_UNSPECIFIED = "image/*";//系统相册mime

    private ImageView image_display;//头像展示
    private TextView avatar_photograph;//调用系统相机
    private TextView avatar_album;//调用系统相册
    private TextView avatar_cancel;//取消按钮
    private View view;

    private Intent intent;

    private String avatarName;
    private String avatarPath;

    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg");
    private Uri imageUri;
    private Uri cropImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar_display);
        intent = getIntent();
        initTitle("修改资料");
        optRight.setVisibility(View.VISIBLE);
        optRight.setText("修改");
        optRight.setOnClickListener(this);
        initComponent();
//        loadSuccessUI();
        if (!TextUtils.isEmpty(LoginInfo.getAvatar_img())) {
            Glide.with(this).load(LoginInfo.getAvatar_img()).into(image_display);
        } else {
            image_display.setImageResource(R.mipmap.default_avater);
        }
    }

    private void initComponent() {
        image_display = $ViewByID(R.id.avatar_display);
        view = $ViewByID(R.id.avatar_image_opt);
        avatar_photograph = $ViewByID(R.id.avatar_photograph);
        avatar_photograph.setOnClickListener(this);
        avatar_album = $ViewByID(R.id.avatar_album);
        avatar_album.setOnClickListener(this);
        avatar_cancel = $ViewByID(R.id.avatar_cancel);
        avatar_cancel.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        if ( !TextUtils.isEmpty(LoginInfo.getAvatar_img())) {
            Glide.with(this).load(LoginInfo.getAvatar_img()).into(image_display);
        } else {
            image_display.setImageResource(R.mipmap.default_avater);
        }
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnOpt:
                if (this.view.getVisibility() == View.GONE) {
                    this.view.setVisibility(View.VISIBLE);
                } else {
                    this.view.setVisibility(View.GONE);
                }
                break;
            case R.id.avatar_photograph:
                requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, new RequestPermissionCallBack() {
                    @Override
                    public void granted() {
                            imageUri = Uri.fromFile(fileUri);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                                //通过FileProvider创建一个content类型的Uri
                                imageUri = FileProvider.getUriForFile(PersonAvatarActivity.this, "com.carlt.doride.fileprovider", fileUri);
                        PhotoUtils.takePicture(PersonAvatarActivity.this, imageUri, CODE_CAMERA_REQUEST);
                    }

                    @Override
                    public void denied() {
                        UUToast.showUUToast(PersonAvatarActivity.this,"部分权限获取失败，正常功能受到影响");
                    }
                });
                break;
            case R.id.avatar_album:
                requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, new RequestPermissionCallBack() {
                    @Override
                    public void granted() {
                        PhotoUtils.openPic(PersonAvatarActivity.this, CODE_GALLERY_REQUEST);
                    }

                    @Override
                    public void denied() {
                        UUToast.showUUToast(PersonAvatarActivity.this,"部分权限获取失败，正常功能受到影响");
                    }
                });
                break;
            case R.id.avatar_cancel:
                this.view.setVisibility(View.GONE);
                break;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            AvatarInfo info = (AvatarInfo) msg.obj;
            if (mDialog != null) {
                mDialog.dismiss();
            }
            Intent backIntent = new Intent(PersonAvatarActivity.this, PersonInfoActivity.class);
            switch (msg.what) {
                case 0:
                    UUToast.showUUToast(PersonAvatarActivity.this, "头像上传成功");
                    backIntent.putExtra("imageId", info.getId());
                    LoginInfo.setAvatar_img(info.getFilePath());
                    break;
                case 1:
                    backIntent.putExtra("imageId", "-1");
                    UUToast.showUUToast(PersonAvatarActivity.this, "头像上传失败");
                    break;

            }
            PersonAvatarActivity.this.setResult(RESULT_OK, backIntent);
            finish();
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        int output_X = 480, output_Y = 480;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODE_CAMERA_REQUEST://拍照完成回调
                    cropImageUri = Uri.fromFile(fileCropUri);
                    PhotoUtils.cropImageUri(this, imageUri, cropImageUri, 1, 1, output_X, output_Y, CODE_RESULT_REQUEST);
                    break;
                case CODE_GALLERY_REQUEST://访问相册完成回调
                        cropImageUri = Uri.fromFile(fileCropUri);
                        Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            newUri = FileProvider.getUriForFile(this, "com.carlt.doride.fileprovider", new File(newUri.getPath()));
                        PhotoUtils.cropImageUri(this, newUri, cropImageUri, 1, 1, output_X, output_Y, CODE_RESULT_REQUEST);
                    break;
                case CODE_RESULT_REQUEST:
                    Bitmap bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, this);
                    setPicToView(bitmap);
                    break;
            }
        }

            super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     */
    private void setPicToView(Bitmap bitmap) {

            //图片路径
            avatarPath = FileUtil.saveFile(PersonAvatarActivity.this, "temphead.jpg", bitmap);
            System.out.println("----------路径----------" + avatarPath);
            final File avatar = new File(avatarPath);
            Glide.with(this).load(avatar).into(image_display);
            mDialog = PopBoxCreat.createDialogWithProgress(PersonAvatarActivity.this, "正在上传...");
            mDialog.show();
            new Thread() {
                @Override
                public void run() {
                    Response response = uploadAvatarRequest(avatar);
                    if (response != null && response.isSuccessful()) {
                        JsonParser jp = new JsonParser();
                        AvatarInfo avatarInfo = null;
                        try {
                            JsonElement element = jp.parse(response.body().string());
                            JsonObject mJson = element.getAsJsonObject();
                            int flag = mJson.get("code").getAsInt();
                            String info = mJson.get("msg").getAsString();
                            Log.d(TAG, "upload flag--------------------------------->" + flag);
                            Log.d(TAG, "upload msg--------------------------------->" + info);
                            if (flag == 200) {
                                avatarInfo = AvatarParser.parser(mJson);
                                Message msg = Message.obtain();
                                msg.what = 0;
                                msg.obj = avatarInfo;
                                handler.sendMessage(msg);
                            } else {
                                handler.sendEmptyMessage(1);
                            }
//                            String url=mJson.get("url").getAsString();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        handler.sendEmptyMessage(1);
                    }
                    super.run();
                }
            }.start();
    }

    private Dialog mDialog;

    private Response uploadAvatarRequest(File image) {
        Response response = null;
        HashMap<String, Object> params = new HashMap<>();
        params.put("fileOwner", "avatar");
        try {
            response = HttpLinker.uploadImage(URLConfig.getM_OSS_UPLOAD_URL(), params, image);
            Log.d(TAG, "uploadAvatar-------->" + response.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }


}
