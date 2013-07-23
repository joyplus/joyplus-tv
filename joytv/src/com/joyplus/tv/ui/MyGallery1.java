package com.joyplus.tv.ui;

import com.joyplus.tv.utils.Log;

import android.content.Context;
import android.content.Intent;
import android.inputmethodservice.Keyboard.Key;
import android.util.AttributeSet;
import android.view.KeyEvent;

public class MyGallery1 extends android.widget.Gallery{

	public MyGallery1(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public MyGallery1(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyGallery1(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.d("sss", "key code = " + keyCode);
		Intent intent;
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_CENTER:
		case KeyEvent.KEYCODE_ENTER:
			intent = new  Intent("KEY_EVENT_KEYCODE_DPAD_CENTER");
			intent.putExtra("isBack", false);
			getContext().sendBroadcast(intent);
			return true;
		case KeyEvent.KEYCODE_BACK:
			intent = new  Intent("KEY_EVENT_KEYCODE_BACK");
			intent.putExtra("isBack", true);
			getContext().sendBroadcast(intent);
			return true;
		case KeyEvent.KEYCODE_DPAD_UP:
			intent = new  Intent("KEY_EVENT_KEYCODE_DPAD_UP");
			intent.putExtra("isBack", false);
			getContext().sendBroadcast(intent);
			return true;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			intent = new  Intent("KEY_EVENT_KEYCODE_DPAD_DOWN");
			intent.putExtra("isBack", false);
			getContext().sendBroadcast(intent);
			return true;
		default:
			return super.onKeyDown(keyCode, event);
		}
//		return false;
	}
	
	

}
