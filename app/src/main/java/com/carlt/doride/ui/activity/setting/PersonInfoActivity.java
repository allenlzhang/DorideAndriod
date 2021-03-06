package com.carlt.doride.ui.activity.setting;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.lib.WheelView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.blankj.utilcode.util.LogUtils;
import com.carlt.doride.R;
import com.carlt.doride.base.LoadingActivity;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.http.retrofitnet.model.UserInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.protocolparser.DefaultStringParser;
import com.carlt.doride.systemconfig.URLConfig;
import com.carlt.doride.ui.view.UUToast;
import com.carlt.doride.utils.LoadLocalImageUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by marller on 2018\3\17 0017.
 */

public class PersonInfoActivity extends LoadingActivity implements View.OnClickListener {
    private static final String TAG = "PersonInfoActivity";

    private View edit_person_avatar;
    private View edit_person_nickname;
    private View edit_person_sex;

    private TextView  person_nickname_txt;
    private TextView  person_sex_txt;
    private ImageView usr_avatar;

    private        OptionsPickerView mSexOptions;//性别选择
    private static String[]          sexItems = {"男", "女", "保密"};
    private        List<String>      sexList;
    private        String            gender;
    int sexFlag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        initTitle("修改资料");
        initComponent();
    }

    @Override
    protected void onResume() {
        if (UserInfo.getInstance().gender != 0) {
            if (UserInfo.getInstance().gender == 1) {
                person_sex_txt.setText("男");
            } else if (UserInfo.getInstance().gender == 2) {
                person_sex_txt.setText("女");
            } else {
                person_sex_txt.setText("保密");
            }

        }
        LoadLocalImageUtil.getInstance().displayCircleFromWeb(UserInfo.getInstance().avatarFile, usr_avatar, R.mipmap.default_avater);
        //        if (!TextUtils.isEmpty(UserInfo.getInstance().realName)) {
        //            person_nickname_txt.setText(UserInfo.getInstance().realName);
        //        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initComponent() {
        edit_person_avatar = findViewById(R.id.edit_person_avatar);
        edit_person_avatar.setOnClickListener(this);
        edit_person_nickname = findViewById(R.id.edit_person_nickname);
        edit_person_nickname.setOnClickListener(this);
        edit_person_sex = findViewById(R.id.edit_person_sex);
        edit_person_sex.setOnClickListener(this);

        edit_person_nickname = findViewById(R.id.edit_person_nickname);
        usr_avatar = findViewById(R.id.usr_avatar);
        person_sex_txt = findViewById(R.id.person_sex_txt);
        person_nickname_txt = findViewById(R.id.person_nickname_txt);
        if (!TextUtils.isEmpty(UserInfo.getInstance().realName)) {
            person_nickname_txt.setText(UserInfo.getInstance().realName);
        }

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_person_avatar:
                Intent avatarIntent = new Intent(this, PersonAvatarActivity.class);
                //avatarIntent.putExtra("avatar",avatarPath);
                startActivityForResult(avatarIntent, 1);
                break;
            case R.id.edit_person_nickname:
                Intent nicknameEdit = new Intent(this, NicknameEditActivity.class);
                nicknameEdit.putExtra("nickname", person_nickname_txt.getText().toString());
                startActivityForResult(nicknameEdit, 0);
                break;
            case R.id.edit_person_sex:
                initSexSelector(person_sex_txt.getText().toString());
                mSexOptions.show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null)
            return;

        if (requestCode == 0) {
            String nickName = data.getStringExtra("nickName");
            LogUtils.e(nickName);
            if (!TextUtils.isEmpty(nickName)) {
                person_nickname_txt.setText(nickName);
            }

        } else {
            if (!TextUtils.isEmpty(data.getStringExtra("imageId"))) {
                HashMap<String, String> params = new HashMap<>();
                params.put("avatar", data.getStringExtra("imageId"));
                chanageInfoRequest(params);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initSexSelector(String sex) {
        sexList = Arrays.asList(sexItems);
        int selectOptions = 0;
        for (int i = 0; i < sexList.size(); i++) {
            if (TextUtils.equals(sex, sexList.get(i))) {
                selectOptions = i;
            }
        }
        mSexOptions = new OptionsPickerView.Builder(this, (options1, option2, options3, v) -> {
            //返回的分别是三个级别的选中位置
            String tx = sexList.get(options1);

            if (tx.equals("男"))
                sexFlag = 1;
            else if (tx.equals("女"))
                sexFlag = 2;
            else
                sexFlag = 3;
            HashMap<String, String> params = new HashMap<>();
            params.put("gender", sexFlag + "");
            chanageInfoRequest(params);
            gender = tx;
        })

                .setLayoutRes(R.layout.sex_edit_dialog, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView sex_change_OK = v.findViewById(R.id.sex_change_OK);
                        final TextView sex_change_cancel = v.findViewById(R.id.sex_change_cancel);
                        sex_change_OK.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mSexOptions.returnData();
                                mSexOptions.dismiss();
                            }
                        });

                        sex_change_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mSexOptions.dismiss();
                            }
                        });

                    }
                })

                //                .setBgColor(Color.parseColor("#95161922"))

                .setSelectOptions(selectOptions)
                .setTextColorCenter(Color.BLUE)
                .setContentTextSize(20)
                .setDividerType(WheelView.DividerType.FILL)
                .setLineSpacingMultiplier(0.5f)
                .isDialog(false)
                .build();
        mSexOptions.setPicker(sexList);//添加数据

    }

    private void chanageInfoRequest(HashMap<String, String> params) {
        DefaultStringParser parser = new DefaultStringParser(callback);
        parser.executePost(URLConfig.getM_USER_EDIT_INFO(), params);
    }

    private BaseParser.ResultCallback callback = new BaseParser.ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            UUToast.showUUToast(PersonInfoActivity.this, "资料修改成功");
            UserInfo.getInstance().gender = sexFlag;
            if (!TextUtils.isEmpty(gender)) {
                person_sex_txt.setText(gender);
            }

            //            parseAvatarUrl(bInfo);
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            if (bInfo != null && !TextUtils.isEmpty(bInfo.getInfo())) {
                UUToast.showUUToast(PersonInfoActivity.this, bInfo.getInfo());
            }
        }
    };

    private void parseAvatarUrl(BaseResponseInfo bInfo) {
        String data = bInfo.getValue().toString();
        try {
            JSONObject json = new JSONObject(data);
            if (json != null) {
                //                String avatar_url = json.optString("avatar_img");
                String avatar_url = UserInfo.getInstance().avatarFile;
                if (!TextUtils.isEmpty(avatar_url)) {
                    UserInfo.getInstance().avatarFile = avatar_url;
                    LoadLocalImageUtil.getInstance().displayCircleFromWeb(UserInfo.getInstance().avatarFile, usr_avatar, R.mipmap.default_avater);

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
