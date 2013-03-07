package com.k1x.android.draganddropphotos.views;


import com.k1x.android.draganddropphotos.views.DraggableLayout.OnInterceptToutchEventListener;
import com.k1x.android.draganddropphotos.views.RotationGestureDetector.OnRotationGestureListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.view.View.OnLongClickListener;

public class ImageDraggableView extends ImageView implements OnRotationGestureListener, OnLongClickListener {


	private ScaleGestureDetector mScaleDetector;
	private RotationGestureDetector rotationDetector;
	private float angle = 0;

	private DraggableLayout parentLayout;
	private MotionEvent parentEvent;
	private String imagePath;
	private float mScaleFactor = 1.f;
	private float imgX;
	private float imgY;
	private float iX;
	private float iY;
	private float origScaleFactor;
	private float origImgX;
	private float origImgY;	
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
	private int bitmapWidth;
	private int bitmapHeight;
	

	public ImageDraggableView(Context context, AttributeSet attrs) {
		super(context, attrs);
	    mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
	    rotationDetector = new RotationGestureDetector(this);
	    setOnLongClickListener(this);
	    
	}
	
	@Override
	public boolean onLongClick(View arg0) {
		parentLayout.bringChildToFront(this);
		parentLayout.invalidate();
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
//		printMatrix();
		if (checkActiveView(parentEvent)) {
			mScaleDetector.onTouchEvent(parentEvent);
			rotationDetector.onTouchEvent(parentEvent);
			onDragging(parentEvent);
		}
		
		return super.onTouchEvent(e);
	}
	
	private void printMatrix() {
		Matrix matrix = getMatrix();
		float[] values = new float[] {1,2,3,4,5,6,7,8,9};
		System.out.println("---------------NEW MATRIX");
		matrix.getValues(values);
		for(int i = 0; i< 9; i++) {
			System.out.print(values[i] + ",");
		}
		System.out.println();
		System.out.println("scaleFactor = " + mScaleFactor + " x = " + getX()  + " y = "+ getY());
		
		
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
				iX = imgX + x - getSizedX(dx);
				iY = imgY + y - getSizedY(dy);
				setX(iX);
				setY(iY);
				break;
		case MotionEvent.ACTION_UP:
			imgX = getSizedX(getX());
			imgY = getSizedY(getY());
			break;
		}
		}
		
		
	}
	
	@Override
	public void setImageBitmap(Bitmap bm) {
		super.setImageBitmap(bm);
		bitmapWidth = bm.getWidth();
		bitmapHeight = bm.getHeight();
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

	public void scaleView(float scaleFactor) {
		origImgX = iX;
		origImgY = iY;
		origScaleFactor = mScaleFactor;
		
		setX(iX * scaleFactor);
		setY(iY * scaleFactor);
		setScaleX(mScaleFactor * scaleFactor);
		setScaleY(mScaleFactor * scaleFactor);
		
	}
	
	public void restoreView() {
		iX = origImgX;
		iY = origImgY;
		mScaleFactor = origScaleFactor;
		
		setX(iX);
		setY(iY);
		setScaleX(mScaleFactor);
		setScaleY(mScaleFactor);
	}

	@Override
	public boolean onRotation(RotationGestureDetector rotationDetector) {
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

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	

	public float getAngle() {
		return angle;
	}

	public float getiX() {
		return iX;
	}

	public float getiY() {
		return iY;
	}

	public float getmScaleFactor() {
		return mScaleFactor;
	}
	
	public int getBitmapWidth() {
		return bitmapWidth;
	}

	public int getBitmapHeight() {
		return bitmapHeight;
	}
}
