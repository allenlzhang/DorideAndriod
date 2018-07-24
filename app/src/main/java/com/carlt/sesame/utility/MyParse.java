package com.carlt.sesame.utility;

import android.graphics.Color;

public class MyParse {

	public static float parseFloat(String f) {
		float f1 = 0;
		try {
			f1 = Float.parseFloat(f);
		} catch (Exception e) {
			Log.e("ero", "parseFloat转换错误");
		}

		return f1;
	}

	public static int parseInt(String i) {
		int i1 = 0;
		try {
			i1 = Integer.parseInt(i);
		} catch (Exception e) {
			Log.e("ero", "parseInt转换错误");
		}

		return i1;
	}

	public static int getColorByPoint(int point) {
		int color = Color.parseColor("#E94B3A");
		if (point >= 80) {
			color = Color.parseColor("#3FC299");
		} else if (point >= 60) {
			color = Color.parseColor("#F39800");
		}

		return color;
	}

	public static int getColorByPoint(String point) {

	    if(point==null||point.length()<1){
	        return getColorByPoint(0);

	    }else{
	        return getColorByPoint(parseInt(point));
	    }
	}
	
	public static int getColorByPersent(int persent) {
        int color = Color.parseColor("#31bd80");
        if(persent<=20){
            color=Color.parseColor("#f1523d");
        }else if(persent>20&&persent<=50){
            color=Color.parseColor("#fda11e");
        }else if(persent>50){
            color=Color.parseColor("#31bd80");
        }
        return color;
    }

    public static int getColorByPersent(String persent) {

        if(persent==null||persent.length()<1){
            return getColorByPoint(0);

        }else{
            return getColorByPoint(parseInt(persent));
        }
    }
}
