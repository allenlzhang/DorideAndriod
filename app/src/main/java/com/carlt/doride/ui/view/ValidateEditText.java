package com.carlt.doride.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TextKeyListener;
import android.text.method.TextKeyListener.Capitalize;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.carlt.doride.DorideApplication;
import com.carlt.doride.R;
import com.carlt.doride.utils.DisplayUtil;


/**
 * @author Yyun 添加验证框的 EditText
 */
public class ValidateEditText extends LinearLayout {

	// --输入框
	private EditText mContentEdit;
	// -- 如果是确认密码的话，这为与之关联的密码框
	private EditText mConfirmEdit;
	// --
	private Context mContext;
	// --错误框，默认隐藏
	private TextView mTv;
	// --最小长度
	private int mMinLength = LENGTH_NO;

	// --最大长度
	private int mMaxLength = LENGTH_NO;
	// --固定长度
	private int mLength = LENGTH_NO;
	// --是否必填
	private boolean isRequired = true;
	// --表示当前什么类型
	private int mType = TYPE_TXT;
	// -- 错误信息
	private String mErrorMsg = "";

	// 软键盘回车按下之后要相应的动作
	private int action;
	//
	private ValidateEditText mNextVEditTxt;

	// 错误字体颜色
	public static final int ERROR_COLOR = Color.parseColor("#FA7C44");
	public static final int LENGTH_NO = -1;
	public static final int TYPE_PDT = 1;// --密码 组合
	public static final int TYPE_PDN = 2;// --密码 数字
	public static final int TYPE_TXT = 3;// --Txt
	public static final int TYPE_NUM = 4;// --数字
	public static final int TYPE_COFT = 5;// --确认密码 TEXT
	public static final int TYPE_COFN = 6;// --确认密码 NUM
	public static final int TYPE_PON = 7;// --电话
	public static final int TYPE_IDN = 8;// --身份证号码

	private char[] accepts = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'x', 'X' };

	public ValidateEditText(Context context) {
		this(context, null);
	}

	public ValidateEditText(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ValidateEditText(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.mContext = context;
		init();
	}

	public void init() {
		setOrientation(VERTICAL);
		mContentEdit = new EditText(mContext);
		mContentEdit.setTextColor(mContext.getResources().getColor(R.color.text_color_gray3));
		mContentEdit.setTextSize(/* DisplayUtil.sp2px(mContext, 16) */16);
		mContentEdit.setBackgroundResource(R.drawable.edittext_bg);
		mContentEdit.setHintTextColor(mContext.getResources().getColor(R.color.text_color_gray1));
		mContentEdit.setFocusable(true);
		mContentEdit.setPadding(DisplayUtil.dip2px(mContext, 9), DisplayUtil.dip2px(mContext, 15), DisplayUtil.dip2px(mContext, 9), DisplayUtil.dip2px(mContext, 15));
		mContentEdit.setTextAppearance(mContext, R.style.safety_edt);
		mContentEdit.setSingleLine(true);
		mContentEdit.setImeOptions(EditorInfo.IME_ACTION_NEXT);

		LayoutParams params1 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params1.gravity = Gravity.CENTER_VERTICAL;
		addView(mContentEdit, params1);

		LayoutParams params2 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		mTv = new TextView(mContext);
		mTv.setTextSize(/* DisplayUtil.sp2px(mContext, 10) */10);
		mTv.setTextColor(ERROR_COLOR);
		mTv.setVisibility(View.INVISIBLE);
		params2.topMargin = DorideApplication.dpToPx(2);
		params2.gravity = Gravity.CENTER_VERTICAL;
		addView(mTv, params2);

		mContentEdit.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					mTv.setVisibility(View.INVISIBLE);
					mContentEdit.setBackgroundResource(R.drawable.edittext_bg);
				} else {
					validateEdit();
				}
			}

		});

		mContentEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mContentEdit.hasFocus() && mTv.getVisibility() == View.VISIBLE) {
					mTv.setVisibility(View.INVISIBLE);
					mContentEdit.setBackgroundResource(R.drawable.edittext_bg);
				}
			}
		});

		mContentEdit.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

				if (actionId == EditorInfo.IME_ACTION_NEXT) {
					if (mNextVEditTxt != null && mNextVEditTxt.getmEditText() != null) {
						mContentEdit.clearFocus();
						mNextVEditTxt.getmEditText().requestFocus();
						return true;
					}
				}
				return false;
			}
		});

	}

	/**
	 * 
	 */
	public boolean validateEdit() {
		String content = mContentEdit.getText().toString();
		if (isRequired) {
			if (TextUtils.isEmpty(content)) {
				showErrorMsg("必填栏目");
				return false;
			}
		}

		boolean result = true;
		if (mLength != LENGTH_NO) {
			if (content.length() != mLength) {
				showErrorMsg("长度为" + mLength + "位");
				result = false;
			}
		} else if (mMinLength != LENGTH_NO) {
			if (content.length() < mMinLength) {
				showErrorMsg("长度为不能小于" + mMinLength + "位");
				result = false;
			}
		} else if (mMaxLength != LENGTH_NO) {
			if (content.length() > mMaxLength) {
				showErrorMsg("长度为不能大于" + mMaxLength + "位");
				result = false;
			}
		}

		if (mType == TYPE_COFT || mType == TYPE_COFN) {
			if (mConfirmEdit == null || TextUtils.isEmpty(mConfirmEdit.getText()) || !content.equals(mConfirmEdit.getText().toString())) {

				showErrorMsg("");
				result = false;
			}
		}

		return result;
	}

	public void showErrorMsg(String msg) {

		if (mType == TYPE_PDT) {
			msg = "请输入长度为6-16个字符";
		} else if (mType == TYPE_COFT || mType == TYPE_COFN) {
			msg = "密码不一致";
		}

		if (TextUtils.isEmpty(msg)) {
			mTv.setText(mErrorMsg);
		} else {
			mTv.setText(msg);
		}
		mContentEdit.setBackgroundResource(R.drawable.edittext_bg_error);
		mTv.setVisibility(View.VISIBLE);
	}

	public void setEditStyle(int rsid) {
		mConfirmEdit.setTextAppearance(mContext, rsid);
	}

	public void setEditPaddingRightDP(int right) {
		mContentEdit.setPadding(DisplayUtil.dip2px(mContext, 9), DisplayUtil.dip2px(mContext, 15), DisplayUtil.dip2px(mContext, right), DisplayUtil.dip2px(mContext, 15));
	}

	public void setEditHint(String hintMsg) {
		mContentEdit.setHint(hintMsg);
	}

	public int getmMinLength() {
		return mMinLength;
	}

	public void setmMinLength(int mMinLength) {
		this.mMinLength = mMinLength;
	}

	public int getmMaxLength() {
		return mMaxLength;
	}

	public void setmMaxLength(int mMaxLength) {
		this.mMaxLength = mMaxLength;
	}

	public int getmLength() {
		return mLength;
	}

	public void setmLength(int mLength) {
		this.mLength = mLength;
	}

	public boolean isRequired() {
		return isRequired;
	}

	public void setRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}

	public EditText getmEditText() {
		return mContentEdit;
	}

	public void setmEditText(EditText mEditText) {
		this.mContentEdit = mEditText;
	}

	public TextView getmTv() {
		return mTv;
	}

	public void setmTv(TextView mTv) {
		this.mTv = mTv;
	}

	public EditText getmConfirmEdit() {
		return mConfirmEdit;
	}

	public void setmConfirmEdit(EditText mConfirmEdit) {
		this.mConfirmEdit = mConfirmEdit;
	}

	public int getmType() {
		return mType;
	}

	public String getmErrorMsg() {
		return mErrorMsg;
	}

	public void setmErrorMsg(String mErrorMsg) {
		this.mErrorMsg = mErrorMsg;
	}

	public String getText() {
		return mContentEdit.getText().toString();
	}

	public void setSingleLine(boolean singleLine) {
		mContentEdit.setSingleLine(singleLine);
	}

	public void setImeAction(int act) {
		action = act;
		mContentEdit.setImeOptions(action);
	}

	public void setNextEditText(ValidateEditText vEditTxt) {
		this.mNextVEditTxt = vEditTxt;
	}

	public void setmType(int mType) {
		this.mType = mType;
		switch (mType) {
		case TYPE_PDT:
			mMinLength = 6;
			mMaxLength = 16;
			mContentEdit.setFilters(new InputFilter[] { new InputFilter.LengthFilter(16) });
			mContentEdit.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
			mContentEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
			break;
		case TYPE_NUM:
			mContentEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
			break;
		case TYPE_TXT:
			break;
		case TYPE_COFT:
			mContentEdit.setInputType(InputType.TYPE_CLASS_TEXT);
			mContentEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
			break;
		case TYPE_COFN:
			mContentEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
			mContentEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
			break;
		case TYPE_PON:
			mContentEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
			mLength = 11;
			break;
		case TYPE_PDN:
			mContentEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
			mContentEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
			break;
		case TYPE_IDN:
			mContentEdit.setInputType(InputType.TYPE_CLASS_TEXT);
			mLength = 18;
			mContentEdit.setFilters(new InputFilter[] { new InputFilter.LengthFilter(18) });
			mContentEdit.setKeyListener(new TextKeyListener(Capitalize.CHARACTERS,false));
			mContentEdit.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				}

				@Override
				public void afterTextChanged(Editable s) {
					if (TextUtils.isEmpty(s)) {
						return;
					}
					
					for (int i = 0; i < s.length(); i++) {
						char c = s.charAt(i);
						boolean isFound = false;
						for (int k = 0; k < accepts.length; k++) {
							char h = accepts[k];
							if(c == h){
								isFound = true;
								break;
							}
						}
						
						if(!isFound){
							s.delete(i, i+1);
							i=i-1;
						}
					}
					
				}
			});
			break;
		}
	}

}
