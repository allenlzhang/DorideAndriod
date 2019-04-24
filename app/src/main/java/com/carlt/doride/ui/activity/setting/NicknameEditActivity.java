package com.carlt.doride.ui.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.carlt.doride.R;
import com.carlt.doride.base.LoadingActivity;
import com.carlt.doride.data.BaseResponseInfo;
import com.carlt.doride.http.retrofitnet.model.UserInfo;
import com.carlt.doride.protocolparser.BaseParser;
import com.carlt.doride.protocolparser.DefaultStringParser;
import com.carlt.doride.systemconfig.URLConfig;
import com.carlt.doride.ui.view.UUToast;
import com.carlt.doride.utils.SharepUtil;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class NicknameEditActivity extends LoadingActivity implements View.OnClickListener {


    private EditText nickname_input;//里程输入框
    private TextView nickname_input_commit;//确认修改按钮
    private String   oldName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nickname_edit);
        initComponent();
        initTitle("修改昵称");
    }

    private void initComponent() {

        nickname_input = findViewById(R.id.nickname_input);
        nickname_input_commit = findViewById(R.id.nickname_input_commit);
        nickname_input_commit.setOnClickListener(this);
        Intent intent = getIntent();
        oldName = intent.getStringExtra("nickname");
        if (!TextUtils.isEmpty(oldName)) {
            nickname_input.setText(oldName);
            nickname_input.setSelection(oldName.length());
        }
        nickname_input.addTextChangedListener(watcher);
    }

    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String editable = nickname_input.getText().toString();
            String str = stringFilter(editable);
            if (!editable.equals(str)) {
                nickname_input.setText(str);
                //设置新的光标所在位置
                nickname_input.setSelection(str.length());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.nickname_input_commit:
                if (TextUtils.isEmpty(nickname_input.getText().toString())) {
                    UUToast.showUUToast(this, "昵称不能为空");
                    return;
                } else if (nickname_input.getText().toString().length() > 16) {
                    UUToast.showUUToast(this, "最多可输入16位数字、字母、汉字");
                    return;
                } else if (TextUtils.equals(oldName, nickname_input.getText().toString())) {
                    UUToast.showUUToast(this, "修改失败，与原昵称相同");
                    return;
                }
                chanageNickNameRequest(nickname_input.getText().toString());
                break;
        }
    }

    private void chanageNickNameRequest(String info) {
        DefaultStringParser parser = new DefaultStringParser(callback);
        HashMap<String, String> params = new HashMap<>();
        params.put("realname", info);
        parser.executePost(URLConfig.getM_USER_EDIT_INFO(), params);
        loadingDataUI();
    }

    private BaseParser.ResultCallback callback = new BaseParser.ResultCallback() {
        @Override
        public void onSuccess(BaseResponseInfo bInfo) {
            loadSuccessUI();
            UUToast.showUUToast(NicknameEditActivity.this, "修改资料成功");
            Intent intent = new Intent(NicknameEditActivity.this, PersonInfoActivity.class);
            intent.putExtra("nickName", nickname_input.getText().toString());
            setResult(RESULT_OK, intent);
            UserInfo.getInstance().realName = nickname_input.getText().toString();
            UserInfo userInfo = SharepUtil.getBeanFromSp(URLConfig.USER_INFO);
            userInfo.realName = nickname_input.getText().toString();
            SharepUtil.putByBean(URLConfig.USER_INFO, userInfo);
            finish();
        }

        @Override
        public void onError(BaseResponseInfo bInfo) {
            loadSuccessUI();
            UUToast.showUUToast(NicknameEditActivity.this, "修改资料失败");
        }
    };

    public static String stringFilter(String str) throws PatternSyntaxException {
        // 只允许字母、数字和汉字
        String regEx = "[^a-zA-Z0-9\u4E00-\u9FA5]";//正则表达式
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }
}
