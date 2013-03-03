package com.k1x.android.draganddropphotos.views;

import java.util.HashSet;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

public class DraggableLayout extends RelativeLayout {

	private HashSet<OnInterceptToutchEventListener> listeners;
	
	private ImageDraggableView activeView;
	private boolean dragging;
	
	public DraggableLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		listeners = new HashSet<OnInterceptToutchEventListener>();
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		

		for(OnInterceptToutchEventListener listener: listeners) {
		listener.onTouchEvent(ev); 
		}
		return super.onInterceptTouchEvent(ev);
	}
	
	public interface OnInterceptToutchEventListener	{
		public void onTouchEvent(MotionEvent e);
	};
	
	public boolean addOnInterceptToutchEventListener(
			OnInterceptToutchEventListener onInterceptToutchEventListener) {
		return listeners.add(onInterceptToutchEventListener);
	}
	
	public boolean removeOnInterceptToutchEventListener(
			OnInterceptToutchEventListener onInterceptToutchEventListener) {
		return listeners.remove(onInterceptToutchEventListener);
	}

	public ImageDraggableView getActiveView() {
		return activeView;
	}

	public void setActiveView(ImageDraggableView activeView) {
		this.activeView = activeView;
	}

	public boolean isDragging() {
		return dragging;
	}

	public void setDragging(boolean dragging) {
		this.dragging = dragging;
	}

	public Bitmap drawOutBitmap(float scaleFactor) {
		Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
		Bitmap outBitmap = Bitmap.createBitmap((int)(getWidth() * scaleFactor), (int)(getHeight() * scaleFactor), conf);
		Canvas canvas = new Canvas(outBitmap);
		
		for(int i = 0; i<getChildCount(); i++ ) {
			ImageDraggableView iView = (ImageDraggableView)getChildAt(i);
			iView.scaleView(scaleFactor);
		}
		
		draw(canvas);
		
		for(int i = 0; i<getChildCount(); i++ ) {
			ImageDraggableView iView = (ImageDraggableView)getChildAt(i);
			iView.restoreView();
		}
		
//		ImageDraggableView view = (ImageDraggableView) getChildAt(0);
//		view.draw(canvas);
		
		
		return outBitmap;
	}

}
