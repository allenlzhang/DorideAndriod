package com.carlt.sesame.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.text.InputFilter;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.text.method.PasswordTransformationMethod;
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
import com.carlt.sesame.R;
import com.carlt.sesame.utility.DisplayUtil;

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
	public static final int TYPE_CODE = 9;// --验证码
	public static final int TYPE_NAME = 10;// --昵称、姓名

	public ValidateEditText(Context context) {
		this(context, null);
	}

	public ValidateEditText(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ValidateEditText(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.mContext = context;
		init();
	}

	public void init() {
		setOrientation(VERTICAL);
		mContentEdit = new EditText(mContext);
		mContentEdit.setTextColor(mContext.getResources().getColor(
				R.color.text_color_gray3));
		mContentEdit.setTextSize(/* DisplayUtil.sp2px(mContext, 16) */16);
		mContentEdit.setBackgroundResource(R.drawable.edittext_bg);
		mContentEdit.setHintTextColor(mContext.getResources().getColor(
				R.color.text_color_gray1));
		mContentEdit.setFocusable(true);
		mContentEdit.setPadding(DisplayUtil.dip2px(mContext, 9),
				DisplayUtil.dip2px(mContext, 15),
				DisplayUtil.dip2px(mContext, 9),
				DisplayUtil.dip2px(mContext, 15));
		mContentEdit.setTextAppearance(mContext, R.style.safety_edt);
		mContentEdit.setSingleLine(true);
		mContentEdit.setImeOptions(EditorInfo.IME_ACTION_NEXT);

		LayoutParams params1 = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		params1.gravity = Gravity.CENTER_VERTICAL;
		addView(mContentEdit, params1);

		LayoutParams params2 = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
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
				if (mContentEdit.hasFocus()
						&& mTv.getVisibility() == View.VISIBLE) {
					mTv.setVisibility(View.INVISIBLE);
					mContentEdit.setBackgroundResource(R.drawable.edittext_bg);
				}
			}
		});

		mContentEdit.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {

				if (actionId == EditorInfo.IME_ACTION_NEXT) {
					if (mNextVEditTxt != null
							&& mNextVEditTxt.getmEditText() != null) {
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
		content = content.replaceAll("\\s*", "");
		mContentEdit.setText(content);
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
			if (mConfirmEdit == null
					|| TextUtils.isEmpty(mConfirmEdit.getText())
					|| !content.equals(mConfirmEdit.getText().toString())) {

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
		mContentEdit.setPadding(DisplayUtil.dip2px(mContext, 9),
				DisplayUtil.dip2px(mContext, 15),
				DisplayUtil.dip2px(mContext, right),
				DisplayUtil.dip2px(mContext, 15));
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

	public boolean getIsEmoji(char codePoint) {
		if ((codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA)
				|| (codePoint == 0xD)
				|| ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
				|| ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
				|| ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF)))
			return false;
		return true;
	}

	public void setmType(int mType) {
		this.mType = mType;
		InputFilter mInputFilter = new InputFilter() {

			@Override
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {
				if (source.equals(" ")) {
					return "";
				} else {
					return null;
				}
			}
		};

		final String ET4_DIGITS = "0123456789abcdefghigklmnopqrstuvwxyz";

		InputFilter mInputFilter2 = new InputFilter() {

			@Override
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < source.length(); i++) {
					if (ET4_DIGITS.indexOf(source.charAt(i)) >= 0) {
						sb.append(source.charAt(i));
					}
				}
				return sb;
			}
		};

		InputFilter mInputFilter3 = new InputFilter() {

			@Override
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {
				if (source.equals(" ")) {
					return "";
				} else {
					return null;
				}
			}
		};

		InputFilter mInputFilter4 = new InputFilter() {
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {
				for (int i = start; i < end; i++) {
					if (!Character.isLetterOrDigit(source.charAt(i))
							&& !Character.toString(source.charAt(i))
									.equals("_")
							&& !Character.toString(source.charAt(i))
									.equals("-")) {
						return "";
					}
				}
				return null;
			}
		};

		InputFilter mInputFilter5 = new InputFilter() {
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {
				StringBuffer buffer = new StringBuffer();
				for (int i = start; i < end; i++) {
					char codePoint = source.charAt(i);
					if (!getIsEmoji(codePoint)) {
						buffer.append(codePoint);
					} else {
						i++;
						continue;
					}
				}
				if (source instanceof Spanned) {
					SpannableString sp = new SpannableString(buffer);
					TextUtils.copySpansFrom((Spanned) source, start, end, null,
							sp, 0);
					return sp;
				} else {
					return buffer;
				}
			}
		};
		switch (mType) {
		case TYPE_PDT:
			mMinLength = 6;
			mMaxLength = 16;
			mContentEdit.setFilters(new InputFilter[] {
					new InputFilter.LengthFilter(16), mInputFilter2,
					mInputFilter3 });
			mContentEdit.setKeyListener(DigitsKeyListener
					.getInstance("0123456789abcdefghigklmnopqrstuvwxyz"));
			mContentEdit.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
			mContentEdit.setTransformationMethod(PasswordTransformationMethod
					.getInstance());
			break;
		case TYPE_NUM:
			mContentEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
			mContentEdit.setFilters(new InputFilter[] { mInputFilter3 });
			break;
		case TYPE_TXT:
			mContentEdit.setFilters(new InputFilter[] { mInputFilter3 });
			break;
		case TYPE_COFT:
			mContentEdit.setInputType(InputType.TYPE_CLASS_TEXT);
			mContentEdit.setFilters(new InputFilter[] {
					new InputFilter.LengthFilter(16), mInputFilter,
					mInputFilter3 });
			mContentEdit.setTransformationMethod(PasswordTransformationMethod
					.getInstance());
			break;
		case TYPE_COFN:
			mContentEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
			mContentEdit.setTransformationMethod(PasswordTransformationMethod
					.getInstance());
			mContentEdit.setFilters(new InputFilter[] { mInputFilter3 });
			break;
		case TYPE_PON:
			mContentEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
			mLength = 11;
			mContentEdit.setFilters(new InputFilter[] {
					new InputFilter.LengthFilter(mLength), mInputFilter3 });
			break;
		case TYPE_PDN:
			mContentEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
			mContentEdit.setTransformationMethod(PasswordTransformationMethod
					.getInstance());
			mContentEdit.setFilters(new InputFilter[] { mInputFilter3 });
			break;
		case TYPE_IDN:
			mContentEdit.setInputType(InputType.TYPE_CLASS_TEXT);
			mLength = 18;
			mContentEdit.setFilters(new InputFilter[] {
					new InputFilter.LengthFilter(18), mInputFilter3 });
			mContentEdit.setKeyListener(new DigitsKeyListener() {
				@Override
				protected char[] getAcceptedChars() {
					return new char[] { '0', '1', '2', '3', '4', '5', '6', '7',
							'8', '9', 'x','X' };
				}
			});
			break;
		case TYPE_CODE:
			mContentEdit.setFilters(new InputFilter[] { mInputFilter4 });
			break;
		case TYPE_NAME:
			mContentEdit.setFilters(new InputFilter[] { mInputFilter3,
					mInputFilter5 });
			break;
		}
	}
}
