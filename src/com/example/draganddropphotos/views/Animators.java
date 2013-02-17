package com.example.draganddropphotos.views;

import java.util.LinkedList;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.database.CursorJoiner.Result;
import android.view.View;

public class Animators {

	private View view;
	private ObjectAnimator scaleXOut;
	private ObjectAnimator scaleYOut;
	private AnimatorSet set;
	private LinkedLimitedList<Float> angles;
	private LinkedLimitedList<Float> scales;
	private Animator rotationAnimation;

	public Animators (View view) {
		this.view = view;
		set = new AnimatorSet();
		scales = new LinkedLimitedList<Float>();
		angles = new LinkedLimitedList<Float>();
	}

	public void rotate(float angle) {
		
		float oldAngle = getAverage(angles);
		angles.add(angle);
		float newAngle = getAverage(angles);
		
		rotationAnimation = ObjectAnimator.ofFloat(view, "rotation", -oldAngle, -newAngle);
		set.end();
		set.play(rotationAnimation);
		set.setDuration(100);
		set.start();		
	}

	public void scale(float mScaleFactor) {
		
		
		float oldScale = getAverage(scales);
		scales.add(mScaleFactor);
		float newScale = getAverage(scales);
		
		scaleXOut = ObjectAnimator.ofFloat(view, "scaleX", oldScale, newScale);
		scaleYOut = ObjectAnimator.ofFloat(view, "scaleY", oldScale, newScale);
	//	System.out.println(mScaleFactor);
		
		set.end();
		set.play(scaleYOut).with(scaleXOut);
		set.setDuration(100);
		set.start();
	}
	
	private float getAverage(LinkedLimitedList<Float> list) {
		if (list.size() > 0) {
			float result = 0;
			for (Float val : list) {
				result += val;
			}
			return result / list.size();
		} else {
			return 1;
		}	
	}
}
