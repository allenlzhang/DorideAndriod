
package com.carlt.sesame.utility;

import android.content.res.Resources;
import android.graphics.Typeface;

import com.carlt.doride.DorideApplication;


public class GetTypeFace {
    /**
     * 获取粗字体
     * 
     * @param resources
     * @return
     */
    public static Typeface typefaceBold(Resources resources) {
        if (resources != null) {
            return Typeface.createFromAsset(resources.getAssets(),
                    "fonts/HelveticaNeueLTStd-BlkCn.otf");
        } else {
            return Typeface.createFromAsset(DorideApplication.ApplicationContext.getResources()
                    .getAssets(), "fonts/HelveticaNeueLTStd-BlkCn.otf");
        }

    }

    /**
     * 获取细字体
     * 
     * @param resources
     * @return
     */
    public static Typeface typefaceThin(Resources resources) {
        if (resources != null) {
            return Typeface.createFromAsset(resources.getAssets(),
                    "fonts/HelveticaNeueLTStd-LtCn.otf");
        } else {
            return Typeface.createFromAsset(DorideApplication.ApplicationContext.getResources()
                    .getAssets(), "fonts/HelveticaNeueLTStd-LtCn.otf");
        }

    }
}
