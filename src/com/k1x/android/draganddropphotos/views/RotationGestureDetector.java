package com.k1x.android.draganddropphotos.views;

import android.view.MotionEvent;

public class RotationGestureDetector {
private static final int INVALID_POINTER_ID = -1;
private float fX, fY, sX, sY, focalX, focalY;


private int ptrID1, ptrID2;
private float mAngle;
private boolean firstTouch;
private OnRotationGestureListener mListener;
private boolean isInProgress;

public float getAngle() {
    return mAngle;
}

public RotationGestureDetector(OnRotationGestureListener listener){
    mListener = listener;
    ptrID1 = INVALID_POINTER_ID;
    ptrID2 = INVALID_POINTER_ID;
}


public boolean onTouchEvent(MotionEvent event){
    switch (event.getActionMasked()) {
        case MotionEvent.ACTION_DOWN:
            sX = event.getX();
            sY = event.getY();
            ptrID1 = event.getPointerId(0);
            mAngle = 0;
            firstTouch = true;
            break;
        case MotionEvent.ACTION_POINTER_DOWN:
            fX = event.getX();
            fY = event.getY();
            focalX = getMidpoint(fX, sX);
            focalY = getMidpoint(fY, sY);
            ptrID2 = event.getPointerId(event.getActionIndex());
            mAngle = 0;
            firstTouch = true;
            break;
        case MotionEvent.ACTION_MOVE:

            if(ptrID1 != INVALID_POINTER_ID && ptrID2 != INVALID_POINTER_ID){
                float nfX, nfY, nsX, nsY;
                nsX = event.getX(event.findPointerIndex(ptrID1));
                nsY = event.getY(event.findPointerIndex(ptrID1));
                nfX = event.getX(event.findPointerIndex(ptrID2));
                nfY = event.getY(event.findPointerIndex(ptrID2));
                if (firstTouch) {
                    mAngle = 0;
                    firstTouch = false;
                } else {
                    mAngle = angleBetweenLines(fX, fY, sX, sY, nfX, nfY, nsX, nsY);
                }

                if (mListener != null) {
                    mListener.onRotation(this);
                }
                fX = nfX;
                fY = nfY;
                sX = nsX;
                sY = nsY;
            }
            break;
        case MotionEvent.ACTION_UP:
            ptrID1 = INVALID_POINTER_ID;
            break;
        case MotionEvent.ACTION_POINTER_UP:
            ptrID2 = INVALID_POINTER_ID;
            break;
    }
    return true;
}

private float getMidpoint(float a, float b){
    return (a + b) / 2;
}

float findAngleDelta( float angle1, float angle2 )
{
    float From = ClipAngleTo0_360( angle2 );
    float To   = ClipAngleTo0_360( angle1 );

    float Dist  = To - From;

    if ( Dist < -180.0f )
    {
        Dist += 360.0f;
    }
    else if ( Dist > 180.0f )
    {
        Dist -= 360.0f;
    }

    return Dist;
}

float ClipAngleTo0_360( float Angle ) { 
    return Angle % 360.0f; 
}

public float getFocalX() {
	return focalX;
}

public float getFocalY() {
	return focalY;
}

private float angleBetweenLines (float fx1, float fy1, float fx2, float fy2, float sx1, float sy1, float sx2, float sy2)
{
       float angle1 = (float) Math.atan2( (fy1 - fy2), (fx1 - fx2) );
       float angle2 = (float) Math.atan2( (sy1 - sy2), (sx1 - sx2) );

       return findAngleDelta((float)Math.toDegrees(angle1),(float)Math.toDegrees(angle2));
}

public boolean isInProgress() {
	return isInProgress;
}


public static interface OnRotationGestureListener {
    public boolean onRotation(RotationGestureDetector rotationDetector);
}
}