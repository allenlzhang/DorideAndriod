package com.carlt.sesame.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.AttributeSet;
import android.widget.EditText;

@SuppressLint("AppCompatCustomView")
public class NoEmojiEditText extends EditText {
	private int cursorPos; // 输入表情前EditText中的文本
	private String inputAfterText; // 是否重置了EditText的内容
	private boolean resetText;
	private Context mContext;

	public NoEmojiEditText(Context context) {
		super(context);
	}

	public NoEmojiEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		initEditText();
	}

	private void initEditText() {
		InputFilter filter=new InputFilter() {

			@Override
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {
				if(source.equals(" ") ){return "";}
                else {return null;}
			}
        };
        setFilters(new InputFilter[]{filter});
		
//		addTextChangedListener(new TextWatcher() {
//
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before,
//					int count) {
//				if (!resetText) {
//                    if (count >= 2) {// 表情符号的字符长度最小为2
//                        CharSequence input = s.subSequence(cursorPos, cursorPos
//                                + count);
//                        if (containsEmoji(input.toString())) {
//                            resetText = true;
//                            //暂不支持输入Emoji表情符号
//                            setText(inputAfterText);
//                            CharSequence text = getText();
//                            if (text instanceof Spannable) {
//                                Spannable spanText = (Spannable) text;
//                                Selection.setSelection(spanText, text.length());
//                            }
//                        }
//                    }
//                } else {
//                    resetText = false;
//                }
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count,
//					int after) {
//				if (!resetText) {
//					cursorPos = getSelectionEnd(); // 这里用s.toString()而不直接用s是因为如果用s，
//					// 那么，inputAfterText和s在内存中指向的是同一个地址，s改变了，
//					// inputAfterText也就改变了，那么表情过滤就失败了
//					inputAfterText = s.toString();
//				}
//
//			}
//
//			@Override
//			public void afterTextChanged(Editable s) {
//				// TODO Auto-generated method stub
//
//			}
//		});
	}

	/**
	 * 检测是否有emoji表情
	 * 
	 * @param source
	 * @return
	 */
	public static boolean containsEmoji(String source) {
		int len = source.length();
		for (int i = 0; i < len; i++) {
			char codePoint = source.charAt(i);
			if (!isEmojiCharacter(codePoint)) { // 如果不能匹配,则该字符是Emoji表情
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断是否是Emoji
	 * 
	 * @param codePoint
	 *            比较的单个字符
	 * @return
	 */
	private static boolean isEmojiCharacter(char codePoint) {
		return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA)
				|| (codePoint == 0xD)
				|| ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
				|| ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
				|| ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
	}

}
