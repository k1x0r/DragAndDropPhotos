package com.k1x.android.draganddropphotos.views;

import java.util.HashSet;

import com.k1x.android.draganddropphotos.bitmapUtil.AppBitmapUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class DraggableLayout extends RelativeLayout {

	
	private static final int OUT_IMAGE_SIZE = 1200;

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
		int targetWidth = (int)(getWidth() * scaleFactor);
		int targetHeight =  (int)(getHeight() * scaleFactor);
		Bitmap outBitmap = Bitmap.createBitmap(targetWidth, targetHeight, conf);
		Canvas canvas = new Canvas(outBitmap);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		
		canvas.drawColor(Color.WHITE);

		for (int i = 0; i < getChildCount(); i++) {
			ImageDraggableView iView = (ImageDraggableView) getChildAt(i);
			if (iView.getImagePath() != null) {
				Bitmap imageBitmap = AppBitmapUtil.getDecodedBitmap(iView.getImagePath(), OUT_IMAGE_SIZE, OUT_IMAGE_SIZE);
				Matrix transformMatrix = new Matrix(iView.getMatrix());
				transformMatrix.preConcat(iView.getImageMatrix());
				transformMatrix.postScale(scaleFactor, scaleFactor);
				
				float[] values = new float[9];
				transformMatrix.getValues(values);

				float scaleFactorQ = ((float)((BitmapDrawable)iView.getDrawable()).getBitmap().getWidth() / (float)imageBitmap.getWidth());
				values[0] *= scaleFactorQ;
				values[4] *= scaleFactorQ;
				values[1] *= scaleFactorQ;
				values[3] *= scaleFactorQ;
				transformMatrix.setValues(values);
				canvas.drawBitmap(imageBitmap, transformMatrix, paint);	
				imageBitmap.recycle();
			}
		}

		return outBitmap;
	}

}
