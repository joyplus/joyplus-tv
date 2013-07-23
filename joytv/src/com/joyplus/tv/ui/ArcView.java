package com.joyplus.tv.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class ArcView extends View {

//	 int r1 = 20;
	int r1 = 12;
	private int mAngle = 180;
	private Paint mPaints;
    private boolean mUseCenters;
    private RectF mBigOval;
    private float mStart;

	public ArcView(Context context) {
		super(context);
		start();
		// TODO Auto-generated constructor stub
	}

	public ArcView(Context context, AttributeSet attrs) {
		super(context, attrs);
		start();
		// TODO Auto-generated constructor stub
	}
	
	public ArcView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		start();
		// TODO Auto-generated constructor stub
	}
	
	private void start(){
              
       mPaints = new Paint();
       mPaints.setAntiAlias(true);
       mPaints.setStyle(Paint.Style.STROKE);
       mPaints.setStrokeWidth(r1);
       mPaints.setColor(0xFFF18C21);
       mUseCenters = true;

	}
    private void drawArcs(Canvas canvas, RectF oval, boolean useCenter,
                          Paint paint) {
//        canvas.drawRect(oval, mFramePaint);
        canvas.drawArc(oval, mStart, mAngle, useCenter, paint);
    
    }
    public void SetAngle(int mAngle) {
   	 this.mAngle = mAngle;
   	 invalidate();
		
	}
    public void SetRingWidth(int ringWidth) {
   	 this.r1 = ringWidth;
   	 invalidate();
		
	}
    @Override protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);

//        float left = 0+r1-1;
//        float top = 0+r1-2;
//        float right =this.getWidth()-r1+1;
//        float bottom = this.getHeight()-r1;
        float left = 0+r1-6;
        float top = 0+r1-6;
        float right =this.getWidth()-r1+6;
        float bottom = this.getHeight()-r1+6;
           
        mBigOval = new RectF(left, top, right, bottom);
             
        mStart = -90;
        canvas.drawArc(mBigOval, mStart, mAngle, false, mPaints);

    }
}
