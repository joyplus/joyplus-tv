package com.joyplus.tv.ui;

import com.joyplus.tv.utils.MyKeyEventKey;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.GridView;

public class MyMovieGridView extends GridView implements MyKeyEventKey{
	
	public MyMovieGridView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MyMovieGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
//		int aciton = event.getAction();
//		
//		if(aciton == KeyEvent.ACTION_DOWN) {
//			
//			if(keyCode == KEY_UP || keyCode == KEY_DOWN) {
//				int position = getSelectedItemPosition();
//				if(keyCode == KEY_UP) {
//					
//					if(position >=5) {
//						
//						setSelection(position - 5);
//					}
//				} else if(keyCode == KEY_DOWN){
//					
//					if(position + 5 < getChildCount()) {
//						
//						setSelection(position + 5);
//					}
//				}
//				return true;
//				
//			}
//		}
		return super.onKeyDown(keyCode, event);
//		super.onKeyUp(keyCode, event);
//		return true;
//		thi
//		return false;
	}
	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
//		int aciton = event.getAction();
//		
//		if(aciton == KeyEvent.ACTION_DOWN) {
//			
//			if(keyCode == KEY_UP || keyCode == KEY_DOWN) {
//				int position = getSelectedItemPosition();
//				if(keyCode == KEY_UP) {
//					
//					if(position >=5) {
//						
//						setSelection(position - 5);
//					}
//				} else if(keyCode == KEY_DOWN){
//					
//					if(position + 5 < getChildCount()) {
//						
//						setSelection(position + 5);
//					}
//				}
//				return true;
//				
//			}
//		}
		return super.onKeyDown(keyCode, event);
//		return false;
	}
	
	@Override
	protected void onFocusChanged(boolean gainFocus, int direction,
			Rect previouslyFocusedRect) {
		// TODO Auto-generated method stub
//		super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
	}
	
	
}
