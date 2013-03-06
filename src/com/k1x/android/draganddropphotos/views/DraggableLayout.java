package com.k1x.android.draganddropphotos.views;

import java.util.HashSet;

import com.k1x.android.draganddropphotos.bitmapUtil.AppBitmapUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class DraggableLayout extends RelativeLayout {

	
	private static final int OUT_IMAGE_SIZE = 1200;

	private HashSet<OnInterceptToutchEventListener> listeners;
	
	private ImageDraggableView activeView;
	private boolean dragging;

	private int layoutX;

	private int layoutY;
	
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
		
		getXY();
		
		canvas.drawColor(Color.WHITE);

		for (int i = 0; i < getChildCount(); i++) {
			ImageDraggableView iView = (ImageDraggableView) getChildAt(i);
			if (iView.getImagePath() != null) {
				Bitmap imageBitmap = AppBitmapUtil.getDecodedBitmap(
						iView.getImagePath(), OUT_IMAGE_SIZE, OUT_IMAGE_SIZE);
				
				Matrix transformMatrix = new Matrix();
				float scaleBitmapFactor = ((float)iView.getWidth() / imageBitmap.getWidth()) * iView.getmScaleFactor() * scaleFactor;
				transformMatrix.postScale(scaleBitmapFactor, scaleBitmapFactor);

				float pivotX =  (iView.getWidth() * scaleBitmapFactor) /2 ;
				float pivotY =  (iView.getHeight() * scaleBitmapFactor) /2 ;
				transformMatrix.postRotate(-iView.getAngle(), pivotX, pivotY);

				
				Rect R = new Rect();
				iView.getGlobalVisibleRect(R);
				float viewX = R.centerX() - R.width() / 2;
				float viewY = R.centerY() - R.height() / 2 - layoutY;
				System.out.println("viewX = " + viewX + " viewY = " + viewY);
				transformMatrix.postTranslate(viewX, viewY);
				
				paint.setColor(Color.RED);
				canvas.drawRect(viewX, viewY, viewX + imageBitmap.getWidth() * scaleBitmapFactor, viewY + imageBitmap.getHeight() * scaleBitmapFactor, paint);
				
				canvas.drawBitmap(imageBitmap, transformMatrix, paint);
				
				canvas.drawRect(
						viewX + imageBitmap.getWidth() * scaleBitmapFactor / 2 - 1, 
						viewY + imageBitmap.getHeight() * scaleBitmapFactor / 2 - 1, 
						viewX + imageBitmap.getWidth() * scaleBitmapFactor / 2 + 1, 
						viewY + imageBitmap.getHeight() * scaleBitmapFactor / 2 + 1, 
						paint);

			}
		}

		return outBitmap;
	}

	private void getXY() {
		Rect R = new Rect();
		getGlobalVisibleRect(R);
		layoutX = R.left;
		layoutY = R.top;
	}

}
