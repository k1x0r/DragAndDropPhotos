package com.example.draganddropphotos.views;


import com.example.draganddropphotos.views.MyLayout.OnInterceptToutchEventListener;
import com.example.draganddropphotos.views.RotationGestureDetector.OnRotationGestureListener;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;


public class ImageDraggableView extends ImageView implements OnRotationGestureListener{

	private boolean containsDragable;
	private GestureDetector gestureDetector;
	private Context context;
	private float mScaleFactor = 1.f;
	private float dx;
	private float dy;
	private ScaleGestureDetector mScaleDetector;
	private RotationGestureDetector rotationDetector;
	private float angle = 0;
	private float oldAngle;
	private AnimatorSet animSet;
	private MyLayout parentLayout;
	private MotionEvent parentEvent;
	private float prevX;
	private float prevY;
	private float imgX;
	private float imgY;
	
	public ImageDraggableView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	    mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
	    rotationDetector = new RotationGestureDetector(this);
	    animSet = new AnimatorSet();
	    
	    
	}
	
	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
	//	animate().rotationBy(45);

	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {

		
		mScaleDetector.onTouchEvent(parentEvent);
		rotationDetector.onTouchEvent(parentEvent);
		if (!mScaleDetector.isInProgress()) {
		switch (parentEvent.getAction()) {
		case MotionEvent.ACTION_DOWN:
			dx = parentEvent.getX();
			dy = parentEvent.getY();
			break;

		case MotionEvent.ACTION_MOVE:
				float x = parentEvent.getX();
				float y = parentEvent.getY();
		
				setX(imgX + x - dx);
				setY(imgY + y - dx);
				prevX = x;
				prevY = y;
				break;
		case MotionEvent.ACTION_UP:
			imgX = getX();
			imgY = getY();
			break;
		}
		}
		return true;
		
	}
	



	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

		private AnimatorSet set;
		private ObjectAnimator scaleXOut;
		private ObjectAnimator scaleYOut;
		private float mOldScale = 1.0f;
		private float mScaleMultiplier;

		public ScaleListener() {
	        set = new AnimatorSet();
		}
		
		@Override
		public boolean onScale(ScaleGestureDetector detector) {

			
			mScaleFactor *= detector.getScaleFactor();
			mScaleFactor = Math.max(0.5f, Math.min(mScaleFactor, 5.0f));
			
			setScaleX(mScaleFactor);
			setScaleY(mScaleFactor);			

			return true;
		}
	}



	@Override
	public boolean OnRotation(RotationGestureDetector rotationDetector) {
		angle += rotationDetector.getAngle();
		
		setRotation(-angle);
		return true;
	}

	public MyLayout getParentLayout() {
		return parentLayout;
	}

	public void setParentLayout(MyLayout parentLayout) {
		this.parentLayout = parentLayout;
		parentLayout.addOnInterceptToutchEventListener(new OnInterceptToutchEventListener() {
			
			@Override
			public void onTouchEvent(MotionEvent e) {
				parentEvent = e;
			}
		});
	}







}
