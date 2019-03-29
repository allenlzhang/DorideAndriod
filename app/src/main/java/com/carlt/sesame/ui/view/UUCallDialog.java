package com.carlt.sesame.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.carlt.doride.DorideApplication;
import com.carlt.doride.R;
import com.carlt.doride.http.retrofitnet.model.ContactsInfo;
import com.carlt.sesame.data.SesameLoginInfo;

/**
 * 拨打电话对话框
 * 
 * @author Daisy
 */
public class UUCallDialog extends Dialog implements
		View.OnClickListener {

	private TextView txtCallSoft;// 软件问题
	private TextView txtCallCar;// 车辆问题
	private TextView txtCancel;// 取消
	private View txtLine;// 分割线

	private final static int w_dip = 340;

	private Context mContext;
	private CallDialogClick mCallDialogClick;

	public UUCallDialog(Context context, CallDialogClick callDialogClick) {
		super(context, R.style.dialog);
		mContext = context;
		mCallDialogClick = callDialogClick;
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.dialog_call, null);
		txtCallSoft = (TextView) view.findViewById(R.id.call_txt_soft);
		txtCallCar = (TextView) view.findViewById(R.id.call_txt_car);
		txtCancel = (TextView) view.findViewById(R.id.call_txt_cancel);
		txtLine = view.findViewById(R.id.call_line2);
		int w = (int) (DorideApplication.ScreenDensity * w_dip);
		setCanceledOnTouchOutside(false);
		LayoutParams parm = new LayoutParams(w,
				LayoutParams.WRAP_CONTENT);
		setContentView(view, parm);
		setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		String softTel = ContactsInfo.getInstance().serviceHotLine;
		if (TextUtils.isEmpty(softTel)) {
			txtCallSoft.setVisibility(View.GONE);
			txtLine.setVisibility(View.GONE);
		}
		String carTel = ContactsInfo.getInstance().salesHotLine;
		if (TextUtils.isEmpty(carTel)) {
			txtCallCar.setVisibility(View.GONE);
			txtLine.setVisibility(View.GONE);
		}
		txtCallSoft.setOnClickListener(this);
		txtCallCar.setOnClickListener(this);
		txtCancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.call_txt_soft:
			// 拨打卡尔特客服电话
			mCallDialogClick.clickSoft();
			dismiss();
			break;

		case R.id.call_txt_car:
			// 拨打Domy客服电话
			mCallDialogClick.clickCar();
			dismiss();
			break;
		case R.id.call_txt_cancel:
			// 取消
			dismiss();
			break;
		}
	}

	public interface CallDialogClick {
		void clickSoft();

		void clickCar();
	}
}
