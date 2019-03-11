
package com.carlt.doride.ui.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carlt.chelepie.view.EditDialog2;
import com.carlt.chelepie.view.EditDialog3;
import com.carlt.chelepie.view.WifiListDialog;
import com.carlt.doride.DorideApplication;
import com.carlt.doride.R;
import com.carlt.doride.control.ActivityControl;
import com.carlt.doride.ui.activity.setting.FlowPackageRechargeActivity;
import com.carlt.sesame.ui.view.UUImgInfoDialog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


public class PopBoxCreat {
    private static UUUpdateDialog mUUDialog;
    private static ImageView      ivIcon1;
    private static ImageView      ivIcon2;
    //    private static UUUpdateChangeDialog mUUDialogChange;

    private static EditDialog2 mEditDialog;

    /**
     * 带标题弹出框 title_msg="";则无标题
     */
    public interface DialogWithTitleClick {

        public void onLeftClick();

        public void onRightClick();
    }

    public interface DialogWithEditClick {

        public void onLeftClick(String editContent);

        public void onRightClick(String editContent);
    }

    public interface onDialogRemoteClick {

        void onItemOneClick(View v);

        void onItemTwoClick(View v);
    }

    /**
     * 流量提醒dialog,需要给出是否点击外部消失
     * @param context
     * @param isNodismiss
     */
    public static void createTrafficDialogNotitle(final Context context,
                                                  final boolean isNodismiss) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.traffic_dialog_notitle, null);
        final Dialog dialogI = new Dialog(context, R.style.dialog);
        TextView btnL = (TextView) view.findViewById(R.id.btn_traffic_buy_now);
        TextView btnR = (TextView) view.findViewById(R.id.btn_traffic_never_remind);
        CheckBox isSelect = (CheckBox) view.findViewById(R.id.traffic_never_remind);

        isSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DorideApplication.isTrafficTipsShow = !isChecked;
            }
        });

        btnL.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DorideApplication.isFirstLogin = false;
                ActivityControl.saveExitTime();
                Intent intent = new Intent(context, FlowPackageRechargeActivity.class);
                intent.putExtra("title", "T-box流量充值");
                context.startActivity(intent);
            }
        });

        btnR.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                DorideApplication.isFirstLogin = false;
                dialogI.dismiss();
                ActivityControl.saveExitTime();
            }
        });


        int w = (int) (DorideApplication.ScreenDensity * 300);
        ViewGroup.LayoutParams parm = new ViewGroup.LayoutParams(w, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialogI.setContentView(view, parm);
        if (isNodismiss) {
            dialogI.setCanceledOnTouchOutside(false);
            dialogI.setCancelable(false);
        }
        dialogI.show();

    }

    /**
     * 三个编辑框
     * @author liu
     */
    public interface DialogWithEditClick2 {

        public void onLeftClick(String editContent);

        public void onRightClick(String editContent, String editContent2, String editContent3);
    }

    public abstract static class DialogEditClick implements OnClickListener {
        protected Dialog dialog;

        protected EditText mEditText;

        public void setCompounts(Dialog diaolg, EditText editText) {
            this.dialog = diaolg;
            this.mEditText = editText;
        }

        public void dismiss() {
            if (this.dialog != null) {
                this.dialog.dismiss();
            }
        }

        public void cancle() {
            if (this.dialog != null) {
                this.dialog.cancel();
            }
        }

        public abstract void onClick(View v);
    }

    public static void createDialogWithTitle(final Context context, String title_msg,
                                             String content1_msg, String content2_msg, String bt1_msg, String bt2_msg,
                                             final DialogWithTitleClick click) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_withtitle, null);
        final Dialog dialogI = new Dialog(context, R.style.dialog);
        TextView title = (TextView) view.findViewById(R.id.dialog_withtitle_title);
        TextView content1 = (TextView) view.findViewById(R.id.dialog_withtitle_content1);
        TextView content2 = (TextView) view.findViewById(R.id.dialog_withtitle_content2);
        TextView btn1 = (TextView) view.findViewById(R.id.dialog_withtitle_btn1);
        TextView btn2 = (TextView) view.findViewById(R.id.dialog_withtitle_btn2);
        if (bt1_msg != null && !bt1_msg.equals("")) {
            btn1.setText(bt1_msg);
        }
        if (bt2_msg != null && !bt2_msg.equals("")) {
            btn2.setText(bt2_msg);
        } else {
            btn2.setVisibility(View.GONE);
        }
        if (title_msg != null && !title_msg.equals("")) {
            title.setVisibility(View.VISIBLE);
            title.setText(title_msg);
        } else {
            title.setVisibility(View.GONE);
        }
        if (content1_msg != null) {
            content1.setText(content1_msg);
        } else {
            content1.setText("");
        }
        if (content2_msg != null && !content2_msg.equals("")) {
            content2.setText(content2_msg);
            content2.setVisibility(View.VISIBLE);
            content1.setGravity(Gravity.NO_GRAVITY);
        } else {
            content2.setText("");
            content2.setVisibility(View.GONE);
            content1.setGravity(Gravity.CENTER);
        }

        btn1.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                click.onLeftClick();
                dialogI.dismiss();

            }
        });

        btn2.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                click.onRightClick();
                dialogI.dismiss();

            }
        });

        int w = (int) (DorideApplication.ScreenDensity * 300);
        ViewGroup.LayoutParams parm = new ViewGroup.LayoutParams(w, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogI.setContentView(view, parm);

        dialogI.show();

    }

    public static UUImgInfoDialog createUUImgInfoDialog(final Context context, String content1,
                                                        String content2, String btnLeft, final OnClickListener listener) {
        UUImgInfoDialog mUUDialog = new UUImgInfoDialog(context);
        mUUDialog.setContentText(content1, content2);
        mUUDialog.setBtnText(btnLeft);
        mUUDialog.setClickListener(listener);
        return mUUDialog;
    }

    public static void createDialogNotitleWithTel(Context context, String tel1,
                                                  String tel2, final DialogWithTitleClick click) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_tel, null);
        final Dialog dialogI = new Dialog(context, R.style.dialog);
        TextView tvTel = view.findViewById(R.id.tvTel);
        TextView tvTel1 = view.findViewById(R.id.tvTel1);
        LinearLayout ll1 = view.findViewById(R.id.ll1);
        LinearLayout ll2 = view.findViewById(R.id.ll2);
        TextView btnL = view.findViewById(R.id.dialog_notitle_text_btnleft);

        tvTel.setText(tel1);
        tvTel1.setText(tel2);
        btnL.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                dialogI.dismiss();

            }
        });
        ll1.setOnClickListener(new DialogEditClick() {
            @Override
            public void onClick(View v) {
                dialogI.dismiss();
                click.onLeftClick();
            }
        });
        ll2.setOnClickListener(new DialogEditClick() {
            @Override
            public void onClick(View v) {
                dialogI.dismiss();
                click.onRightClick();
            }
        });


        dialogI.setContentView(view);
        dialogI.show();

    }

    /**
     * 无标题dialog
     * @param context
     * @param content1_msg
     * @param content2_msg
     * @param btnL_msg
     * @param btnR_msg
     * @param click
     */
    public static void createDialogNotitle(final Context context, String content1_msg,
                                           String content2_msg, final String btnL_msg, final String btnR_msg,
                                           final DialogWithTitleClick click, boolean hideView) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_notitle, null);
        final Dialog dialogI = new Dialog(context, R.style.dialog);
        TextView content1 = (TextView) view.findViewById(R.id.dialog_notitle_text_content1);
        TextView content2 = (TextView) view.findViewById(R.id.dialog_notitle_text_content2);
        TextView btnL = (TextView) view.findViewById(R.id.dialog_notitle_text_btnleft);
        TextView btnR = (TextView) view.findViewById(R.id.dialog_notitle_text_btnright);
        View viewTwo = view.findViewById(R.id.dialog_notitle_lay_bottom);
        TextView btnOne = (TextView) view.findViewById(R.id.dialog_notitle_text_btnone);

        if (btnL_msg == null || btnL_msg.equals("")) {
            if (btnR_msg == null || btnR_msg.equals("")) {
                viewTwo.setVisibility(View.GONE);
                btnOne.setVisibility(View.GONE);

            } else {
                viewTwo.setVisibility(View.GONE);
                btnOne.setVisibility(View.VISIBLE);
                btnOne.setText(btnR_msg);
            }
        } else {
            if (btnR_msg == null || btnR_msg.equals("")) {
                viewTwo.setVisibility(View.GONE);
                btnOne.setVisibility(View.VISIBLE);
                btnOne.setText(btnL_msg);
            } else {
                viewTwo.setVisibility(View.VISIBLE);
                btnOne.setVisibility(View.GONE);
                btnL.setText(btnL_msg);
                btnR.setText(btnR_msg);
            }
        }

        if (content1_msg != null) {
            content1.setText(content1_msg);
        } else {
            content1.setText("");
        }
        if (content2_msg != null && !content2_msg.equals("")) {
            content2.setText(content2_msg);
            content2.setVisibility(View.VISIBLE);
            content1.setGravity(Gravity.NO_GRAVITY);
        } else {
            content2.setText("");
            content2.setVisibility(View.GONE);
            content1.setGravity(Gravity.CENTER);
        }

        //让第一个 content1 居中
        if (hideView) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            content1.setLayoutParams(params);
        }
        btnL.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                click.onLeftClick();
                dialogI.dismiss();

            }
        });

        btnR.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                click.onRightClick();
                dialogI.dismiss();

            }
        });

        btnOne.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (btnL_msg != null && !btnL_msg.equals("")) {
                    click.onLeftClick();
                } else if (btnR_msg != null && !btnR_msg.equals("")) {
                    click.onRightClick();
                }
                dialogI.dismiss();
            }
        });


        int w = (int) (DorideApplication.ScreenDensity * 300);
        ViewGroup.LayoutParams parm = new ViewGroup.LayoutParams(w, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogI.setContentView(view, parm);
        dialogI.show();

    }

    public static void createDialogNotitle(final Context context, String content1_msg,
                                           String content2_msg, final String btnL_msg, final String btnR_msg,
                                           final DialogWithTitleClick click) {
        createDialogNotitle(context, content1_msg, content2_msg, btnL_msg, btnR_msg, click, false);

    }


    public static void createDialogRemote(Activity context, String title, String item1, String item2, int res1, int res2, final onDialogRemoteClick dialogRemoteClick) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_remote_no_title, null);
        LinearLayout llItem1 = view.findViewById(R.id.llItem1);
        LinearLayout llItem2 = view.findViewById(R.id.llItem2);
        ivIcon1 = view.findViewById(R.id.ivIcon1);
        ivIcon2 = view.findViewById(R.id.ivIcon2);
        TextView tvName1 = view.findViewById(R.id.tvName1);
        TextView tvName2 = view.findViewById(R.id.tvName2);
        TextView tvClose = view.findViewById(R.id.tvClose);
        TextView tvDialogTitle = view.findViewById(R.id.tvDialogTitle);
        ivIcon1.setBackgroundResource(res1);
        ivIcon2.setBackgroundResource(res2);
        tvName1.setText(item1);
        tvName2.setText(item2);
        tvDialogTitle.setText(title);
        //        if (carStateInfo != null) {
        //            if (carStateInfo.getName().equals("车锁")) {
        //                if (carStateInfo.getState().equals("1")) {
        //                    ivIcon2.setSelected(true);
        //                } else if (carStateInfo.getState().equals("0")) {
        //                    ivIcon1.setSelected(true);
        //                }
        //            } else {
        //                if (carStateInfo.getState().equals("1")) {
        //                    ivIcon1.setSelected(true);
        //                } else if (carStateInfo.getState().equals("0")) {
        //                    ivIcon2.setSelected(true);
        //                }
        //            }
        //
        //        }


        final Dialog dialogI = new Dialog(context, R.style.dialog);
        int w = (int) (DorideApplication.ScreenDensity * 300);
        ViewGroup.LayoutParams parm = new ViewGroup.LayoutParams(w, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogI.setContentView(view, parm);
        dialogI.show();
        tvClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogI.dismiss();
            }
        });
        ivIcon1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogRemoteClick.onItemOneClick(v);
                dialogI.dismiss();
            }
        });
        ivIcon2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogRemoteClick.onItemTwoClick(v);
                dialogI.dismiss();
            }
        });

    }

    /**
     * 点击对话框以外的部分不会消失
     * @param context
     * @param title_msg
     * @param content1_msg
     * @param content2_msg
     * @param bt1_msg
     * @param bt2_msg
     * @param click
     */
    public static void createDialogWithNodismiss(final Context context, String title_msg,
                                                 String content1_msg, String content2_msg, String bt1_msg, String bt2_msg,
                                                 final DialogWithTitleClick click) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_withtitle, null);
        final Dialog dialogI = new Dialog(context, R.style.dialog);
        TextView title = (TextView) view.findViewById(R.id.dialog_withtitle_title);
        TextView content1 = (TextView) view.findViewById(R.id.dialog_withtitle_content1);
        TextView content2 = (TextView) view.findViewById(R.id.dialog_withtitle_content2);
        TextView btn1 = (TextView) view.findViewById(R.id.dialog_withtitle_btn1);
        TextView btn2 = (TextView) view.findViewById(R.id.dialog_withtitle_btn2);
        if (bt1_msg != null && !bt1_msg.equals("")) {
            btn1.setText(bt1_msg);
        }
        if (bt2_msg != null && !bt2_msg.equals("")) {
            btn2.setText(bt2_msg);
        } else {
            btn2.setVisibility(View.GONE);
        }
        if (title_msg != null && !title_msg.equals("")) {
            title.setVisibility(View.VISIBLE);
            title.setText(title_msg);
        } else {
            title.setVisibility(View.GONE);
        }
        if (content1_msg != null) {
            content1.setText(content1_msg);
            content1.setTextSize(13);
        } else {
            content1.setText("");
        }
        if (content2_msg != null && !content2_msg.equals("")) {
            content2.setText(content2_msg);
            content2.setVisibility(View.VISIBLE);
        } else {
            content2.setText("");
            content2.setVisibility(View.GONE);
        }

        btn1.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                click.onLeftClick();
                dialogI.dismiss();

            }
        });

        btn2.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                click.onRightClick();
                dialogI.dismiss();

            }
        });

        int w = (int) (DorideApplication.ScreenDensity * 300);
        dialogI.setCanceledOnTouchOutside(false);
        ViewGroup.LayoutParams parm = new ViewGroup.LayoutParams(w, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogI.setContentView(view, parm);
        dialogI.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                return true;
            }
        });
        dialogI.show();

    }

    /**
     * 没有按钮的对话框
     * @param context
     * @param content1_msg
     * @param content2_msg
     */
    public static void createDialogNobtn(final Context context, String content1_msg,
                                         String content2_msg) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_notitle, null);
        final Dialog dialogI = new Dialog(context, R.style.dialog);
        TextView content1 = (TextView) view.findViewById(R.id.dialog_notitle_text_content1);
        TextView content2 = (TextView) view.findViewById(R.id.dialog_notitle_text_content2);
        View viewTwo = view.findViewById(R.id.dialog_notitle_lay_bottom);
        View line = view.findViewById(R.id.dialog_notitle_lay_line);
        viewTwo.setVisibility(View.GONE);
        line.setVisibility(View.GONE);

        if (content1_msg != null) {
            content1.setText(content1_msg);
        } else {
            content1.setText("");
        }
        if (content2_msg != null && !content2_msg.equals("")) {
            content2.setText(content2_msg);
            content2.setVisibility(View.VISIBLE);
            content1.setGravity(Gravity.NO_GRAVITY);
        } else {
            content2.setText("");
            content2.setVisibility(View.GONE);
            content1.setGravity(Gravity.CENTER);
        }

        int w = (int) (DorideApplication.ScreenDensity * 300);
        ViewGroup.LayoutParams parm = new ViewGroup.LayoutParams(w, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogI.setContentView(view, parm);
        dialogI.show();
    }

    public static UUDialog createDialogWithProgress(final Context context, String title_msg) {
        UUDialog mUUDialog = new UUDialog(context);
        if (title_msg != null && !title_msg.equals("")) {
            mUUDialog.setContentText(title_msg);
        }
        return mUUDialog;
    }

    public static UUTimerDialog createUUTimerDialog(final Context context, String title_msg) {
        UUTimerDialog mUUDialog = new UUTimerDialog(context);
        if (title_msg != null && !title_msg.equals("")) {
            mUUDialog.setContentText(title_msg);
        }
        return mUUDialog;
    }

    public static UUAuthorDialog createUUAuthorDialog(final Context context, String content1,
                                                      String content2, String btnLeft, String btnRight,
                                                      DialogWithTitleClick mDialogWithTitleClick, UUAuthorDialog.OnTimeOutListener mTimeOutListener) {
        UUAuthorDialog mUUDialog = new UUAuthorDialog(context);
        mUUDialog.setContentText(content1, content2);
        mUUDialog.setBtnText(btnLeft, btnRight);
        mUUDialog.setmDialogWithTitleClick(mDialogWithTitleClick);
        mUUDialog.setmTimeOutListener(mTimeOutListener);
        return mUUDialog;
    }


    /**
     * 硬件升级提示框
     * @param context
     * @param mListener
     * @return
     */
    public static UUUpdateDialog showUUUpdateDialog(final Context context, final UUUpdateDialog.DialogUpdateListener mListener) {
        if (mUUDialog != null && mUUDialog.isShowing()) {
            return mUUDialog;
        }

        mUUDialog = new UUUpdateDialog(context, mListener);
        mUUDialog.show();
        return mUUDialog;
    }


    private static EditDialog3 mEditDialog3;

    /**
     * @param context
     * @param title_msg
     * @param hint
     * @param inputType
     * @param btnL
     * @param btnR
     * @param dialogWithEditClick
     * @return
     */
    public static EditDialog2 createDialogWithedit2(final Context context, String title_msg,
                                                    String hint, int inputType, String btnL, String btnR, DialogWithEditClick dialogWithEditClick) {
        if (mEditDialog != null) {
            mEditDialog.dismiss();
        }

        mEditDialog = new EditDialog2(context);
        mEditDialog.setTitleString(title_msg);
        mEditDialog.setEditType(inputType);
        mEditDialog.setHintTxt(hint);
        mEditDialog.setBtnL(btnL);
        mEditDialog.setBtnR(btnR);
        mEditDialog.setDialogWithTitleClick(dialogWithEditClick);
        final EditText editT = mEditDialog.getEditT();
        editT.setFilters(new InputFilter[]{new InputFilter.LengthFilter(25)});
        editT.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String editable = editT.getText().toString();
                String str = stringFilter(editable.toString());
                if (!editable.equals(str)) {
                    editT.setText(str);
                    //设置新的光标所在位置
                    editT.setSelection(str.length());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return mEditDialog;
    }

    public static EditDialog2 createDialogWithedit2_16lenth(final Context context, String title_msg,
                                                            String hint, int inputType, String btnL, String btnR, DialogWithEditClick dialogWithEditClick) {
        if (mEditDialog != null) {
            mEditDialog.dismiss();
        }

        mEditDialog = new EditDialog2(context);
        mEditDialog.setTitleString(title_msg);
        mEditDialog.setEditType(inputType);
        mEditDialog.setHintTxt(hint);
        mEditDialog.setBtnL(btnL);
        mEditDialog.setBtnR(btnR);
        mEditDialog.setDialogWithTitleClick(dialogWithEditClick);
        final EditText editT = mEditDialog.getEditT();
        editT.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
        editT.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String editable = editT.getText().toString();
                String str = stringFilter(editable.toString());
                if (!editable.equals(str)) {
                    editT.setText(str);
                    //设置新的光标所在位置
                    editT.setSelection(str.length());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return mEditDialog;
    }

    public static WifiListDialog createDialogWifilist(final Context context,
                                                      DialogWithEditClick dialogWithEditClick) {
        WifiListDialog mWifiListDialog = new WifiListDialog(context);
        mWifiListDialog.setmDialogWithTitleClick(dialogWithEditClick);
        mWifiListDialog.setCancelable(true);
        mWifiListDialog.setCanceledOnTouchOutside(true);
        mWifiListDialog.doItemClick();
        return mWifiListDialog;
    }

    public static String stringFilter(String str) throws PatternSyntaxException {
        // 仅仅同意字母、数字和汉字
        //        String   regEx  =  "[^a-zA-Z0-9\u4E00-\u9FA5]";
        // 仅仅同意字母、数字和汉字
        String regEx = "[^a-zA-Z0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 带三个编辑款
     * @param context
     * @param title_msg
     * @param hint
     * @param hint2
     * @param hint3
     * @param inputType
     * @param btnL
     * @param btnR
     * @param dialogWithEditClick
     * @return
     */
    public static EditDialog3 createDialogWithedit3(final Context context, String title_msg,
                                                    String hint, String hint2, String hint3, int inputType, String btnL, String btnR, DialogWithEditClick2 dialogWithEditClick) {
        if (mEditDialog != null) {
            mEditDialog.dismiss();
        }

        mEditDialog3 = new EditDialog3(context);
        mEditDialog3.setTitleString(title_msg);
        mEditDialog3.setEditType(inputType);
        mEditDialog3.setHintTxt(hint, hint2, hint3);
        mEditDialog3.setBtnL(btnL);
        mEditDialog3.setBtnR(btnR);
        mEditDialog3.setDialogWithTitleClick(dialogWithEditClick);
        return mEditDialog3;
    }
}
