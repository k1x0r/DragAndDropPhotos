package com.example.draganddropphotos.views;

import java.util.HashSet;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class MyLayout extends RelativeLayout {

	private HashSet<OnInterceptToutchEventListener> listeners;

	public MyLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		listeners = new HashSet<OnInterceptToutchEventListener>();
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		
		for(OnInterceptToutchEventListener listener: listeners)
		listener.onTouchEvent(ev);
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
}
