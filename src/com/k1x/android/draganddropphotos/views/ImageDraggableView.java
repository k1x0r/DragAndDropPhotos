package com.k1x.android.draganddropphotos.views;


import com.k1x.android.draganddropphotos.views.DraggableLayout.OnInterceptToutchEventListener;
import com.k1x.android.draganddropphotos.views.RotationGestureDetector.OnRotationGestureListener;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.view.View.OnLongClickListener;

public class ImageDraggableView extends ImageView implements OnRotationGestureListener, OnLongClickListener {

	private float mScaleFactor = 1.f;

	private ScaleGestureDetector mScaleDetector;
	private RotationGestureDetector rotationDetector;
	private float angle = 0;

	private DraggableLayout parentLayout;
	private MotionEvent parentEvent;

	private float imgX;
	private float imgY;
	private float dx;
	private float dy;
	private int pointerCount;
	private int prevCount;
	
	private OnInterceptToutchEventListener onInterceptToutchEventListener = new OnInterceptToutchEventListener() {
		
		@Override
		public void onTouchEvent(MotionEvent e) {
			parentEvent = e;
		}
	};
	

	public ImageDraggableView(Context context, AttributeSet attrs) {
		super(context, attrs);
	    mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
	    rotationDetector = new RotationGestureDetector(this);
	    setOnLongClickListener(this);
	    
	}
	
	@Override
	public boolean onLongClick(View arg0) {
		System.out.println("Long click happened! ");
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		if (checkActiveView(parentEvent)) {
			mScaleDetector.onTouchEvent(parentEvent);
			rotationDetector.onTouchEvent(parentEvent);
			onDragging(parentEvent);
		}
		
		return super.onTouchEvent(e);
	}
	
	private boolean checkActiveView(MotionEvent e) {
		if (e.getAction() == MotionEvent.ACTION_DOWN && !parentLayout.isDragging()) {
			parentLayout.setActiveView(this);
			parentLayout.setDragging(true);
		} else if (e.getAction() == MotionEvent.ACTION_UP && e.getPointerCount() == 1) {
			parentLayout.setDragging(false);
		}
		return parentLayout.getActiveView() == this;		
	}
	
	private void onDragging(MotionEvent event) {
		
		pointerCount = event.getPointerCount(); 
		if(prevCount>1 && pointerCount == 1) {
			imgX = getSizedX(getX());
			imgY = getSizedY(getY());
			dx = event.getX();
			dy = event.getY();
		}
		prevCount = pointerCount;
		
		if (pointerCount == 1) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			dx = event.getX();
			dy = event.getY();
			break;

		case MotionEvent.ACTION_MOVE:
				float x = event.getX();
				float y = event.getY();
				setX(imgX + x - getSizedX(dx));
				setY(imgY + y - getSizedY(dy));
				break;
		case MotionEvent.ACTION_UP:
			imgX = getSizedX(getX());
			imgY = getSizedY(getY());
			break;
		}
		}
		
		
	}
	
	private float getSizedX(float x) {
		
		return x + (mScaleFactor *getWidth() -  getWidth()) / 2;
	}
	
	private float getSizedY(float y) {
		return y + (mScaleFactor * getHeight() - getHeight()) / 2;
	}
	



	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

		
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

	public DraggableLayout getParentLayout() {
		return parentLayout;
	}

	public void setParentLayout(DraggableLayout parentLayout) {
		this.parentLayout = parentLayout;
		parentLayout.addOnInterceptToutchEventListener(onInterceptToutchEventListener);
	}
	
	public OnInterceptToutchEventListener getOnInterceptToutchEventListener() {
		return onInterceptToutchEventListener;
	}

}
