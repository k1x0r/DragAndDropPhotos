package com.example.draganddropphotos.views;


import com.example.draganddropphotos.views.MyLayout.OnInterceptToutchEventListener;
import com.example.draganddropphotos.views.RotationGestureDetector.OnRotationGestureListener;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;


public class ImageDraggableView extends ImageView implements OnRotationGestureListener{

	private float mScaleFactor = 1.f;

	private ScaleGestureDetector mScaleDetector;
	private RotationGestureDetector rotationDetector;
	private float angle = 0;

	private MyLayout parentLayout;
	private MotionEvent parentEvent;

	private Context context;
	private float imgX;
	private float imgY;
	private float dx;
	private float dy;

	private boolean enableDragging = true;

	private int pointerCount;

	private int prevCount;
	
	public ImageDraggableView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	    mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
	    rotationDetector = new RotationGestureDetector(this);
	    
	    
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		mScaleDetector.onTouchEvent(parentEvent);
		rotationDetector.onTouchEvent(parentEvent);
		onDragging(parentEvent);
		return true;
		
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
