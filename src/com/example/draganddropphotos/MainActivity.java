package com.example.draganddropphotos;

import com.example.draganddropphotos.views.ImageDraggableView;

import android.os.Bundle;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.view.Menu;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

    private ImageDraggableView imageView;
	private RelativeLayout relativeLayout;
	private AnimatorSet set;
	private ObjectAnimator scaleXOut;
	private ObjectAnimator scaleYOut;
	private ObjectAnimator rotateClockWise;
	private ObjectAnimator translation;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpViews();
    }

    private void setUpViews() {
        setContentView(R.layout.activity_main);
		
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        imageView = (ImageDraggableView) findViewById(R.id.imageView);
        



	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
}
