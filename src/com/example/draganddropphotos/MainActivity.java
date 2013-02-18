package com.example.draganddropphotos;

import com.example.draganddropphotos.views.ImageDraggableView;
import com.example.draganddropphotos.views.MyLayout;

import android.os.Bundle;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {

    private ImageDraggableView imageView;
	private MyLayout relativeLayout;
	private AnimatorSet set;
	private ObjectAnimator scaleXOut;
	private ObjectAnimator scaleYOut;
	private ObjectAnimator rotateClockWise;
	private ObjectAnimator translation;
	private ImageDraggableView imageView1;
	private ImageDraggableView imageView2;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpViews();
    }

    private void setUpViews() {
        setContentView(R.layout.activity_main);
		
        relativeLayout = (MyLayout) findViewById(R.id.relativeLayout);
        imageView = (ImageDraggableView) findViewById(R.id.imageView);
        imageView1 = (ImageDraggableView) findViewById(R.id.imageView1);
        
        imageView.setParentLayout(relativeLayout);
        imageView1.setParentLayout(relativeLayout);
        
        imageView2 = new ImageDraggableView(this, null);
        imageView2.setImageResource(R.drawable.image111);
        
        relativeLayout.addView(imageView2);
        imageView2.setParentLayout(relativeLayout);
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
}
