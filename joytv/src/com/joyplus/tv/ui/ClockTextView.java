package com.joyplus.tv.ui;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.TextView;

public class ClockTextView extends TextView {
	private static final int MESSAGE_UPDATE = 1;
	private Handler hadler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case MESSAGE_UPDATE:
				updateTime();
				break;

			default:
				break;
			}
		}
	};

	public ClockTextView(Context context) {
		super(context);
		start();
		// TODO Auto-generated constructor stub
	}

	public ClockTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		start();
		// TODO Auto-generated constructor stub
	}
	
	public ClockTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		start();
		// TODO Auto-generated constructor stub
	}
	
	private void start(){
		Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/Roboto-Thin.ttf");
		setTypeface(tf);
		updateTime();
	}
	
	private void updateTime(){
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("H:mm");
		setText(format.format(date));
//		Typeface tf = Typeface.create("serif",Typeface.NORMAL);//The font family for this typeface. Examples include "monospace", "serif", and "sans-serif". 
		hadler.sendEmptyMessageDelayed(MESSAGE_UPDATE,500);
	}
}
