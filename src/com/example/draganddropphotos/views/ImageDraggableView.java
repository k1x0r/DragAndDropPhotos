package com.example.draganddropphotos.views;


import com.example.draganddropphotos.views.RotationGestureDetector.OnRotationGestureListener;

import android.R.anim;
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RotateDrawable;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;


public class ImageDraggableView extends ImageView implements OnRotationGestureListener {




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
	private Animators animator;


	public ImageDraggableView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	    mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
	    rotationDetector = new RotationGestureDetector(this);
	    animSet = new AnimatorSet();
	    animator = new Animators(this);


	}
	
	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
	//	animate().rotationBy(45);

	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent motionEvent) {

		mScaleDetector.onTouchEvent(motionEvent);
		rotationDetector.onTouchEvent(motionEvent);
		
		switch (motionEvent.getAction()) {
		case MotionEvent.ACTION_DOWN:
			dx = motionEvent.getX();
			dy = motionEvent.getY();
			break;

		case MotionEvent.ACTION_MOVE:
			if (!mScaleDetector.isInProgress()) {
				float x = motionEvent.getX();
				float y = motionEvent.getY();
				
				RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) getLayoutParams(); 

				float left = lp.leftMargin + (x - dx);
				float top = lp.topMargin + (y - dy);
				
				lp.leftMargin = (int) left;
				lp.topMargin = (int) top;
				lp.bottomMargin = -200;
				lp.rightMargin = -200;
				setLayoutParams(lp);
			/*	setX(x - dx);
				setY(y - dy);
			*/
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
			
			animator.scale(mScaleFactor);
			
			
	//		setScaleX(mScaleFactor);
	//		setScaleY(mScaleFactor);
//			System.out.println(mScaleFactor + " Span " + detector.getCurrentSpan());
	/*		animate().scaleX(mScaleFactor);
			animate().scaleY(mScaleFactor);
	*/
	//		animate().setDuration(0);
	/*		if (!set.isRunning() ) {
				
				
				scaleXOut = ObjectAnimator.ofFloat(ImageDraggableView.this,
						"scaleX", mOldScale, mScaleFactor);
				scaleYOut = ObjectAnimator.ofFloat(ImageDraggableView.this,
						"scaleY", mOldScale, mScaleFactor);
				
				System.out.println("Scaling image " + mOldScale + " " + mScaleFactor);
				
				mOldScale = mScaleFactor;
				set.play(scaleYOut).with(scaleXOut);
				set.setDuration(100);
				set.start();

			}*/
			return true;
		}
	}



	@Override
	public boolean OnRotation(RotationGestureDetector rotationDetector) {
		angle += rotationDetector.getAngle();
		
		animator.rotate(angle);
		
	//	setRotation(-angle);

/*		animate().rotation(-angle);
		System.out.println(-angle);
		animate().setDuration(0);
*/	
		return true;
	}




}
