package com.carlt.sesame.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.carlt.doride.DorideApplication;

public class SectorView extends View{
    
    private float r_out;//外圆半径
    
    private float r_in;//内圆半径
    
    private int persent;//半分比
    
    

    public void setPersent(int persent) {
        this.persent = persent;
        invalidate();
    }

    public SectorView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }
    
    public SectorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        r_out=7* DorideApplication.ScaledDensity;
        r_in=6* DorideApplication.ScaledDensity;
                
    }

    public SectorView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        getOutCitcle(canvas);
        getInArc(canvas);
    }
    
    private void getOutCitcle(Canvas canvas){
        Paint mPaint=new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        float strokeWidth=5* DorideApplication.ScreenDensity/10;
        mPaint.setStrokeWidth(strokeWidth);
        mPaint.setAntiAlias(true);
        
        canvas.drawCircle(r_out+strokeWidth, r_out+strokeWidth, r_out, mPaint);
    }
    
    private void getInArc(Canvas canvas){
        Paint mPaint=new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        
        float offset=15* DorideApplication.ScreenDensity/10;
        RectF oval=new RectF(offset, offset, r_in*2+offset, r_in*2+offset);
        float sweepAngle=persent*360/100;
        canvas.drawArc(oval, 270, sweepAngle, true, mPaint);
    }

}
