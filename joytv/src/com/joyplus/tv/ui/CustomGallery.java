package com.joyplus.tv.ui;

import com.joyplus.tv.utils.Log;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Scroller;

public class CustomGallery extends HorizontalScrollView {

	private OnItemClickListener itmeClickListener;
	private OnItemSelectedListener itmeSelectedListener;
	private BaseAdapter adapter;
	private int selectedIndex;
	private Scroller mScroller;
	private int currentLeft;
	private static final String TAG = "CustomGallery";
	private LinearLayout layout;
	private TranslateAnimation leftAnim;
	private TranslateAnimation rightAnim;
	private int itemWidth = 0;
	private Handler handler = new Handler();

	// public CustomGallery(Context context, AttributeSet attrs, int defStyle) {
	// super(context, attrs, defStyle);
	// // TODO Auto-generated constructor stub
	// }

	public CustomGallery(Context context, AttributeSet attrs) {
		super(context, attrs);
		mScroller = new Scroller(context);
		currentLeft = 0;
		layout = new LinearLayout(context);
		this.setVerticalScrollBarEnabled(false); // 禁用垂直滚动
		this.setHorizontalScrollBarEnabled(false); // 禁用水平滚动
		// setSc
		// TODO Auto-generated constructor stub
	}

	public CustomGallery(Context context) {
		super(context);
		mScroller = new Scroller(context);
		currentLeft = 0;
		layout = new LinearLayout(context);
		this.setVerticalScrollBarEnabled(false); // 禁用垂直滚动
		this.setHorizontalScrollBarEnabled(false); // 禁用水平滚动
		// TODO Auto-generated constructor stub
	}

	public void setAdapter(BaseAdapter adapter) {
		removeAllViews();
		layout.removeAllViews();
		this.adapter = adapter;
		if (this.adapter == null) {
			return;
		}
		for (int i = 0; i < adapter.getCount(); i++) {
			// final Map<String,Object> map=adapter.getItem(i);
			View view = adapter.getView(i, null, this);
			final int index = i;
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// Toast.makeText(context, "您点击了"+map.get("text"),
					// Toast.LENGTH_SHORT).show();
					if (itmeClickListener != null) {
						itmeClickListener.onItemClick(null, v, index, 0);
					}
					//鼠标点击view，高亮不切换
//					if (itmeSelectedListener != null) {
//						itmeSelectedListener.onItemSelected(null, v, index, 0);
//					}
				}
			});
			view.setOnTouchListener(new View.OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					
					requestDisallowInterceptTouchEvent(true);
					return false;
				}
			});
			layout.setOrientation(LinearLayout.HORIZONTAL);
			if(itemWidth ==0 &&view.getLayoutParams().width!=0){
				itemWidth = view.getLayoutParams().width;
			}
			layout.addView(view, view.getLayoutParams());
		}
		if (layout.getChildAt(0) != null) {
			View v = new View(getContext());
			v.setLayoutParams(layout.getChildAt(0).getLayoutParams());
			layout.addView(v, 0);
		}
		this.addView(layout, new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		// MarginLayoutParams mlp2 = (MarginLayoutParams)
		// layout.getLayoutParams();
		// mlp2.setMargins(getChildAt(0).getMeasuredWidth(),
		// mlp2.topMargin,
		// mlp2.rightMargin,
		// mlp2.bottomMargin);
		// requestLayout();
		// scrollTo(layout.getChildAt(0).getWidth()*(selectedIndex+1),0);
		// currentLeft = layout.getChildAt(0).getWidth()*(selectedIndex+1);
		// if(itmeSelectedListener!=null){
		// itmeSelectedListener.onItemSelected(null,
		// layout.getChildAt((selectedIndex+1)), (selectedIndex+1), 0);
		// }

	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		
//		switch (ev.getAction()) {
//		case MotionEvent.ACTION_DOWN:
//			
//			break;
//		case MotionEvent.ACTION_MOVE:
//			
//			return true;
//		default:
//			break;
//		}
		
		return super.onTouchEvent(ev);
	}

	public void setOnItemClickListener(OnItemClickListener listener) {
		this.itmeClickListener = listener;
	}

	public void setOnItemSelectedListener(OnItemSelectedListener listener) {
		this.itmeSelectedListener = listener;
	}

	public BaseAdapter getAdapter() {
		return this.adapter;
	}

	public void setSelection(int index) {
		this.selectedIndex = index - 1;
		layout.setVisibility(View.INVISIBLE);
		if (layout.getChildAt(0) != null) {
					// TODO Auto-generated method stub
			Log.d(TAG, "selectedIndex ------>" + selectedIndex);
			Log.d(TAG, "getWidth ------>" + itemWidth);
			layout.scrollTo(itemWidth*(selectedIndex + 1), 0);
			layout.setVisibility(View.VISIBLE);
			// currentLeft = layout.getChildAt(0).getWidth()*(selectedIndex+1);
			if (itmeSelectedListener != null) {
				itmeSelectedListener.onItemSelected(null,
						layout.getChildAt((selectedIndex + 1)), index, 0);
			}
		} else {
			selectedIndex = 0;
		}

	}

	public int getSelectedItemPosition() {
		return (selectedIndex + 1);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		// if(layout.is`)
		if (layout.getChildCount() < 2) {
			return true;
		}
		if (leftAnim != null && leftAnim.hasStarted() && !leftAnim.hasEnded()) {
			return true;
		}
		if (rightAnim != null && rightAnim.hasStarted()
				&& !rightAnim.hasEnded()) {
			return true;
		}
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_LEFT:
			if (selectedIndex >= 0) {
				selectedIndex -= 1;
				// mScroller.startScroll(currentLeft, 0,
				// -getChildAt((selectedIndex+1)).getWidth(), 0, 500);
				layout.scrollTo(layout.getChildAt(0).getWidth()
						* (selectedIndex + 1), 0);
				// TranslateAnimation anim = new
				// TranslateAnimation(-layout.getChildAt(0).getWidth(),0,0,0);
				if (leftAnim == null) {
					leftAnim = new TranslateAnimation(-layout.getChildAt(0)
							.getWidth(), 0, 0, 0);
					leftAnim.setDuration(250);
					// leftAnim.ha
				}
				layout.startAnimation(leftAnim);
				if (itmeSelectedListener != null) {
					Log.d(TAG, "selectedIndex = " + selectedIndex);
					itmeSelectedListener.onItemSelected(null,
							layout.getChildAt((selectedIndex + 1)),
							selectedIndex + 1, 0);
				}
			}
			return true;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			if (selectedIndex < adapter.getCount() - 2) {
				selectedIndex += 1;
				layout.scrollTo(layout.getChildAt(0).getWidth()
						* (selectedIndex + 1), 0);
				if (rightAnim == null) {
					rightAnim = new TranslateAnimation(layout.getChildAt(0)
							.getWidth(), 0, 0, 0);
					rightAnim.setDuration(250);
				}
				layout.startAnimation(rightAnim);
				// TranslateAnimation anim = new
				// TranslateAnimation(layout.getChildAt(0).getWidth(),0,0,0);
				// TranslateAnimation anim = new
				// TranslateAnimation(0,-layout.getChildAt(0).getWidth(),0,0);
				// mScroller.startScroll(currentLeft, 0,
				// getChildAt((selectedIndex+1)).getWidth(), 0, 500);
				// getChildAt((selectedIndex+1)-1).setVisibility(View.VISIBLE);
				// anim.setAnimationListener(new AnimationListener() {
				//
				// @Override
				// public void onAnimationStart(Animation animation) {
				// // TODO Auto-generated method stub
				//
				// }
				//
				// @Override
				// public void onAnimationRepeat(Animation animation) {
				// // TODO Auto-generated method stub
				//
				// }
				//
				// @Override
				// public void onAnimationEnd(Animation animation) {
				// // TODO Auto-generated method stub
				// layout.scrollTo(layout.getChildAt(0).getWidth()*(selectedIndex+1),0);
				// }
				// });

				if (itmeSelectedListener != null) {
					Log.d(TAG, "selectedIndex = " + selectedIndex);
					itmeSelectedListener.onItemSelected(null,
							layout.getChildAt((selectedIndex + 1)),
							selectedIndex + 1, 0);
				}
			}
			return true;
		case KeyEvent.KEYCODE_DPAD_CENTER:
		case KeyEvent.KEYCODE_ENTER:
			if (itmeClickListener != null) {
				itmeClickListener.onItemClick(null,
						layout.getChildAt((selectedIndex + 1)),
						selectedIndex + 1, 0);
			}
			return true;
		default:
			super.onKeyDown(keyCode, event);
			break;
		}
		return false;
	}

	public void showPre() {
		if (selectedIndex >= 0) {
			selectedIndex -= 1;
			// mScroller.startScroll(currentLeft, 0,
			// -getChildAt((selectedIndex+1)).getWidth(), 0, 500);
			layout.scrollTo(layout.getChildAt(0).getWidth()
					* (selectedIndex + 1), 0);
			// TranslateAnimation anim = new
			// TranslateAnimation(-layout.getChildAt(0).getWidth(),0,0,0);
			if (leftAnim == null) {
				leftAnim = new TranslateAnimation(-layout.getChildAt(0)
						.getWidth(), 0, 0, 0);
				leftAnim.setDuration(250);
				// leftAnim.ha
			}
			layout.startAnimation(leftAnim);
			if (itmeSelectedListener != null) {
				Log.d(TAG, "selectedIndex = " + selectedIndex);
				itmeSelectedListener.onItemSelected(null,
						layout.getChildAt((selectedIndex + 1)),
						selectedIndex + 1, 0);
			}
		}
	}

	public void showNext() {
		if (selectedIndex < adapter.getCount() - 2) {
			selectedIndex += 1;
			layout.scrollTo(layout.getChildAt(0).getWidth()
					* (selectedIndex + 1), 0);
			if (rightAnim == null) {
				rightAnim = new TranslateAnimation(layout.getChildAt(0)
						.getWidth(), 0, 0, 0);
				rightAnim.setDuration(250);
			}
			layout.startAnimation(rightAnim);
			// TranslateAnimation anim = new
			// TranslateAnimation(layout.getChildAt(0).getWidth(),0,0,0);
			// TranslateAnimation anim = new
			// TranslateAnimation(0,-layout.getChildAt(0).getWidth(),0,0);
			// mScroller.startScroll(currentLeft, 0,
			// getChildAt((selectedIndex+1)).getWidth(), 0, 500);
			// getChildAt((selectedIndex+1)-1).setVisibility(View.VISIBLE);
			// anim.setAnimationListener(new AnimationListener() {
			//
			// @Override
			// public void onAnimationStart(Animation animation) {
			// // TODO Auto-generated method stub
			//
			// }
			//
			// @Override
			// public void onAnimationRepeat(Animation animation) {
			// // TODO Auto-generated method stub
			//
			// }
			//
			// @Override
			// public void onAnimationEnd(Animation animation) {
			// // TODO Auto-generated method stub
			// layout.scrollTo(layout.getChildAt(0).getWidth()*(selectedIndex+1),0);
			// }
			// });

			if (itmeSelectedListener != null) {
				Log.d(TAG, "selectedIndex = " + selectedIndex);
				itmeSelectedListener.onItemSelected(null,
						layout.getChildAt((selectedIndex + 1)),
						selectedIndex + 1, 0);
			}
		}
	}

	// private void showPre(){
	// if((selectedIndex+1)>0){
	// (selectedIndex+1) -= 1;
	// if(itmeSelectedListener!=null){
	// itmeSelectedListener.onItemSelected(null,
	// layout.getChildAt((selectedIndex+1)), (selectedIndex+1), 0);
	// }
	// mScroller.startScroll(currentLeft, 0,
	// -layout.getChildAt((selectedIndex+1)).getWidth(), 0, 500);
	// // scrollTo(getChildAt(0).getWidth()*(selectedIndex+1),0);
	// }
	// }
	//
	// private void showNext(){
	// if((selectedIndex+1)<adapter.getCount()-1){
	// (selectedIndex+1) += 1;
	// if(itmeSelectedListener!=null){
	// itmeSelectedListener.onItemSelected(null,
	// layout.getChildAt((selectedIndex+1)), (selectedIndex+1), 0);
	// }
	// mScroller.startScroll(currentLeft, 0,
	// layout.getChildAt((selectedIndex+1)).getWidth(), 0, 500);
	// scrollTo(layout.getChildAt(0).getWidth()*(selectedIndex+1),0);
	// }
	// }
}
