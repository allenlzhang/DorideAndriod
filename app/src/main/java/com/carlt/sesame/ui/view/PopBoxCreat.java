
package com.carlt.sesame.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Bitmap;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.doride.DorideApplication;
import com.carlt.sesame.R;
import com.carlt.sesame.data.remote.CarStateInfo;
import com.carlt.sesame.ui.view.UUAuthorDialog.OnTimeOutListener;
import com.carlt.sesame.ui.view.UUUpdateDialog.DialogUpdateListener;

import java.util.ArrayList;

public class PopBoxCreat {
    private static UUUpdateDialog mUUDialog;

    /**
     * 带标题弹出框 title_msg="";则无标题
     */
    public interface DialogWithTitleClick {

        public void onLeftClick();

        public void onRightClick();
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
        ImageView div = (ImageView) view.findViewById(R.id.dialog_withtitle_title_div);
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
            div.setVisibility(View.VISIBLE);
            title.setText(title_msg);
        } else {
            title.setVisibility(View.GONE);
            div.setVisibility(View.GONE);
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
        LayoutParams parm = new LayoutParams(w, LayoutParams.WRAP_CONTENT);
        dialogI.setContentView(view, parm);

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
                                           final DialogWithTitleClick click) {

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
        LayoutParams parm = new LayoutParams(w, LayoutParams.WRAP_CONTENT);
        dialogI.setContentView(view, parm);
        dialogI.show();

    }

    /**
     * 无标题dialog,需要给出是否点击外部消失
     * @param context
     * @param content1_msg
     * @param content2_msg
     * @param btnL_msg
     * @param btnR_msg
     * @param click
     * @param isNodismiss
     */
    public static void createDialogNotitle(final Context context, String content1_msg,
                                           String content2_msg, final String btnL_msg, final String btnR_msg,
                                           final DialogWithTitleClick click, final boolean isNodismiss) {

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
        LayoutParams parm = new LayoutParams(w, LayoutParams.WRAP_CONTENT);
        dialogI.setContentView(view, parm);
        if (isNodismiss) {
            dialogI.setCanceledOnTouchOutside(false);
        }
        dialogI.show();

    }

    public static void createDialogNotitleOneBtn(final Context context, String content1_msg,
                                                 String content2_msg, String btn_msg, final DialogWithTitleClick click) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_notitle_one_btn, null);
        final Dialog dialogI = new Dialog(context, R.style.dialog);
        TextView content1 = (TextView) view.findViewById(R.id.dialog_notitle_text_content1);
        TextView content2 = (TextView) view.findViewById(R.id.dialog_notitle_text_content2);
        TextView btn = (TextView) view.findViewById(R.id.dialog_notitle_text_btnright);
        if (btn_msg != null && !btn_msg.equals("")) {
            btn.setText(btn_msg);
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

        btn.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                click.onLeftClick();
                dialogI.dismiss();

            }
        });

        int w = (int) (DorideApplication.ScreenDensity * 300);
        LayoutParams parm = new LayoutParams(w, LayoutParams.WRAP_CONTENT);
        dialogI.setContentView(view, parm);

        dialogI.show();

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
        ImageView div = (ImageView) view.findViewById(R.id.dialog_withtitle_title_div);
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
            div.setVisibility(View.VISIBLE);
            title.setText(title_msg);
        } else {
            title.setVisibility(View.GONE);
            div.setVisibility(View.GONE);
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
        LayoutParams parm = new LayoutParams(w, LayoutParams.WRAP_CONTENT);
        dialogI.setContentView(view, parm);
        dialogI.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                return true;
            }
        });
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

    public static UUTwoCodeDialog showTwoCodeDialog(Context context, Bitmap bit, OnClickListener mClick) {
        UUTwoCodeDialog mDialog = new UUTwoCodeDialog(context, mClick);
        mDialog.setTwoCode(bit);
        mDialog.show();
        return mDialog;
    }

    public static UUAuthorDialog createUUAuthorDialog(final Context context, String content1,
                                                      String content2, String btnLeft, String btnRight,
                                                      DialogWithTitleClick mDialogWithTitleClick, OnTimeOutListener mTimeOutListener) {
        UUAuthorDialog mUUDialog = new UUAuthorDialog(context);
        mUUDialog.setContentText(content1, content2);
        mUUDialog.setBtnText(btnLeft, btnRight);
        mUUDialog.setmDialogWithTitleClick(mDialogWithTitleClick);
        mUUDialog.setmTimeOutListener(mTimeOutListener);
        return mUUDialog;
    }

    public static UUTransferDialog createUUTransferDialog(final Context context, String content1,
                                                          String btnLeft, String btnRight, DialogWithTitleClick mDialogWithTitleClick) {
        UUTransferDialog mUUDialog = new UUTransferDialog(context);
        mUUDialog.setContentText(content1);
        mUUDialog.setBtnText(btnLeft, btnRight);
        mUUDialog.setmDialogWithTitleClick(mDialogWithTitleClick);
        return mUUDialog;
    }

    public static UUImgInfoDialog createUUImgInfoDialog(final Context context, String content1,
                                                        String content2, String btnLeft, final OnClickListener listener) {
        UUImgInfoDialog mUUDialog = new UUImgInfoDialog(context);
        mUUDialog.setContentText(content1, content2);
        mUUDialog.setBtnText(btnLeft);
        mUUDialog.setClickListener(listener);
        return mUUDialog;
    }

    /**
     * 硬件升级提示框
     * @param context
     * @param content1
     * @param btnLeft
     * @param btnRight
     * @param mDialogWithTitleClick
     * @return
     */
    public static UUUpdateDialog showUUUpdateDialog(final Context context, final DialogUpdateListener mListener) {
        if (mUUDialog != null && mUUDialog.isShowing()) {
            return mUUDialog;
        }

        mUUDialog = new UUUpdateDialog(context, mListener);
        mUUDialog.show();
        return mUUDialog;
    }


    /**
     * 显示车辆状态
     * @param context
     * @param datas
     * @return
     */
    public static UUCarStateDialog showUUCarStateDialog(final Context context, final ArrayList<CarStateInfo> datas) {
        UUCarStateDialog mUUDialog = new UUCarStateDialog(context, datas);
        mUUDialog.show();
        return mUUDialog;
    }

    /**
     * 输入密码Dialog
     */
    public static void showEditDialog(final Context context, DialogEditClick clickListener,
                                      final OnClickListener cancleListener, InputFilter[] inputFilters) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_withedit, null);
        final Dialog dialogI = new Dialog(context, R.style.dialog);
        final EditText editPassword = (EditText) view.findViewById(R.id.dialog_withedit_edt);
        if (inputFilters != null) {
            editPassword.setFilters(inputFilters);
        }
        TextView btn1 = (TextView) view.findViewById(R.id.dialog_withedit_btn1);
        TextView btn2 = (TextView) view.findViewById(R.id.dialog_withedit_btn2);
        clickListener.setCompounts(dialogI, editPassword);
        btn1.setOnClickListener(clickListener);
        dialogI.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                if (cancleListener != null) {
                    cancleListener.onClick(null);
                }
            }
        });

        dialogI.setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                if (cancleListener != null) {
                    cancleListener.onClick(null);
                }
            }
        });
        btn2.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                dialogI.dismiss();
            }
        });

        int w = (int) (DorideApplication.ScreenDensity * 300);
        LayoutParams parm = new LayoutParams(w, LayoutParams.WRAP_CONTENT);
        dialogI.setContentView(view, parm);
        dialogI.show();
    }

}
