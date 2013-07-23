package com.joyplus.tv.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;
import android.widget.TextView;

import com.joyplus.tv.R;

public class MyScrollLayout extends ViewGroup {

	private static final String TAG = "MyScrollLayout";
//	private VelocityTracker mVelocityTracker; 
//	private static final int SNAP_VELOCITY = 600;   
	private Scroller mScroller; 
	private int mCurTitle;   
	private TextView mSelectedView;
	private boolean isInit = false;
//	private float mLastMotionX;


	private OnViewChangeListener mOnViewChangeListener;

	public MyScrollLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public MyScrollLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context);
	}

	private void init(Context context) {
		Log.i(TAG, "init--->");
		mCurTitle = 0;
		mScroller = new Scroller(context);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		
		if (changed) {
			int childBottom = 0;
			final int childCount = getChildCount();
			for (int i = 0; i < childCount; i++) {
				final View childView = getChildAt(i);
				if (childView.getVisibility() != View.GONE) {
					final int childHeight = childView.getMeasuredHeight();
					childView.layout(0, childBottom, childView.getMeasuredWidth(),
							childBottom+childHeight);
					childBottom += childHeight;
					
					
					final int position = getChildCount()-i; 
					//添加鼠标点击事件
					childView.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							setSelectedTitleIndex(position);
						}
					});
				}
			}
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

//		final int width = MeasureSpec.getSize(widthMeasureSpec);
//
//		final int count = getChildCount();
//		for (int i = 0; i < count; i++) {
//			getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
//		}
//		
//		scrollTo(mCurTitle * width, 0);
		
		
		int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
		int measureHeigth = MeasureSpec.getSize(heightMeasureSpec);
		setMeasuredDimension(measureWidth, measureHeigth);
		// TODO Auto-generated method stub
		for(int i= 0;i<getChildCount();i++){
			View v = getChildAt(i);
			int widthSpec = 0;
			int heightSpec = 0;
			LayoutParams params = v.getLayoutParams();
			if(params.width > 0){
				widthSpec = MeasureSpec.makeMeasureSpec(params.width, MeasureSpec.EXACTLY);
			}else if (params.width == -1) {
				widthSpec = MeasureSpec.makeMeasureSpec(measureWidth, MeasureSpec.EXACTLY);
			} else if (params.width == -2) {
				widthSpec = MeasureSpec.makeMeasureSpec(measureWidth, MeasureSpec.AT_MOST);
			}
			
			if(params.height > 0){
				heightSpec = MeasureSpec.makeMeasureSpec(params.height, MeasureSpec.EXACTLY);
			}else if (params.height == -1) {
				heightSpec = MeasureSpec.makeMeasureSpec(measureHeigth, MeasureSpec.EXACTLY);
			} else if (params.height == -2) {
				heightSpec = MeasureSpec.makeMeasureSpec(measureWidth, MeasureSpec.AT_MOST);
			}
			v.measure(widthSpec, heightSpec);
		}
		final int height = getChildAt(0).getMeasuredHeight();
		
		if(!isInit){
			scrollTo(0, (getChildCount()-3)  * height);
			mCurTitle = getChildCount()-3;
			mSelectedView = (TextView) getChildAt(mCurTitle+2);
			mSelectedView.setTextColor(getContext().getResources().getColor(R.color.common_title_selected));
			isInit = true;
		}
		invalidate();
	}

	public void snapToDestination() {
		final int TitleHeight = getChildAt(0).getMeasuredHeight();

		final int destScreen = (getScrollY() + TitleHeight / 2) / TitleHeight;
		snapToTitle(destScreen);
	}

	//使屏幕移动到第whichScreen+1�?
	private void snapToTitle(int index) {
		
		Log.i(TAG, "snapToDestination--->" + index);
		
		if(index<-2||index>(getChildCount()-3)){
			return ;
		}

		if (getScrollY() != (index * getChildAt(0).getMeasuredHeight())) {
			final int delta = index * getChildAt(0).getMeasuredHeight() - getScrollY();
			mScroller.startScroll(0, getScrollY(), 0, delta,
					800);
			mSelectedView.setTextColor(getContext().getResources().getColor(R.color.common_title_unselected));
			mCurTitle = index;
			mSelectedView = (TextView) getChildAt(mCurTitle+2);
			mSelectedView.setTextColor(getContext().getResources().getColor(R.color.common_title_selected));
			invalidate(); 

			if (mOnViewChangeListener != null) {
				mOnViewChangeListener.OnViewChange(2-mCurTitle);
			}
		}
	}

	@Override
	public void computeScroll() {
		// TODO Auto-generated method stub
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		}
	}

//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		// TODO Auto-generated method stub
//
//		final int action = event.getAction();
//		final float x = event.getX();
//		final float y = event.getY();
//
//		switch (action) {
//		case MotionEvent.ACTION_DOWN:
//
//			if (mVelocityTracker == null) {
//				mVelocityTracker = VelocityTracker.obtain();
//				mVelocityTracker.addMovement(event);
//			}
//			if (!mScroller.isFinished()) {
//				mScroller.abortAnimation();
//			}
//			mLastMotionX = y;
//			break;
//
//		case MotionEvent.ACTION_MOVE:
//			
//			int deltaY = (int) (mLastMotionX - y);
//			if (IsCanMove(deltaY)) {
//				if (mVelocityTracker != null) {
//					mVelocityTracker.addMovement(event);
//				}
//				mLastMotionX = y;
//				//正向或�?负向移动，屏幕跟随手指移�?
//				scrollBy(0, deltaY);
//			}
//			break;
//
//		case MotionEvent.ACTION_UP:
//
//			int velocityY = 0;
//			if (mVelocityTracker != null) {
//				mVelocityTracker.addMovement(event);
//				mVelocityTracker.computeCurrentVelocity(1000);
//				//得到X轴方向手指移动�?�?
//				velocityY = (int) mVelocityTracker.getYVelocity();
//			}
//			//velocityX为正值说明手指向右滑动，为负值说明手指向左滑�?
//			if (velocityY > SNAP_VELOCITY && mCurTitle > 0) {
//				// Fling enough to move left
//				snapToTitle(mCurTitle - 1);
//			} else if (velocityY < -SNAP_VELOCITY
//					&& mCurTitle < getChildCount() - 1) {
//				// Fling enough to move right
//				snapToTitle(mCurTitle + 1);
//			} else {
//				snapToDestination();
//			}
//
//			if (mVelocityTracker != null) {
//				mVelocityTracker.recycle();
//				mVelocityTracker = null;
//			}
//
//			break;
//		}
//		return true;
//	}
//
//
//	private boolean IsCanMove(int deltaY) {
//		//deltaX<0说明手指向右�?
//		if (getScrollY() <= 0 && deltaY < 0) {
//			return false;
//		}
//		//deltaX>0说明手指向左�?
//		if (getScrollY() >= (getChildCount() - 1) * getChildAt(0).getMeasuredHeight() && deltaY > 0) {
//			return false;
//		}
//		return true;
//	}

	public void SetOnViewChangeListener(OnViewChangeListener listener) {
		mOnViewChangeListener = listener;
	}

	public void selectPreTitle(){
		snapToTitle(mCurTitle - 1);
	}
	
	public void selectNextTitle(){
		snapToTitle(mCurTitle + 1);
	}
	
	public int getSelectedTitleIndex(){
		return 2-mCurTitle;
	}
	
	public void setSelectedTitleIndex(int index){
		snapToTitle(2-index);
	}
	
	public interface OnViewChangeListener {
		public void OnViewChange(int index);
	}
}
