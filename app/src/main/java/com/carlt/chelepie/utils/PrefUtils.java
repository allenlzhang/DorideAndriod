/**
 * 
 */
package com.carlt.chelepie.utils;

import android.content.SharedPreferences;

import com.carlt.doride.DorideApplication;

/**
 *
 * @author @Y.yun
 * 
 */
public class PrefUtils {
	public static final String PlayBack = "playback";
	public static final String PlayBack_Silence = "playback_silence";
	
	public static SharedPreferences getPlayBack(){
		return DorideApplication.ApplicationContext.getSharedPreferences(PlayBack, 0);
	}
}
