package com.carlt.yema.ui.view;

import android.graphics.Color;

import com.carlt.yema.YemaApplication;

public class ReportDateConfig {

	// 日历方格宽度
	private static int Cell_Width = 0;

	private static int Cell_Height = 0;

	private static int Head_Height = 0;

	private static int Div_Height = 0;

	public final static int Div_Color = Color.rgb(224, 224, 224);
	public static int getCell_Width() {
		if (Cell_Width == 0) {
			Cell_Width = YemaApplication.ScreenWith / 7;
		}
		return Cell_Width;
	}

	public static int getCell_Height() {
		if (Cell_Height == 0) {
			Cell_Height = YemaApplication.ScreenWith * 4 / (7 * 3);
		}
		return Cell_Height;
	}

	public static int getHead_Height() {
		if (Head_Height == 0) {
			Head_Height = YemaApplication.ScreenWith * 7 / (7 * 13);
		}
		return Head_Height;
	}

	public static int getDiv_Height() {
		if (Div_Height == 0) {
			Div_Height = (int) (YemaApplication.ScreenDensity * 1);
		}
		return Div_Height;
	}

}
