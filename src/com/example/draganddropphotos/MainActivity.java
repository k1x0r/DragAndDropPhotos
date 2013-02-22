package com.example.draganddropphotos;

import com.example.draganddropphotos.views.ImageDraggableView;
import com.example.draganddropphotos.views.MyLayout;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

    private ImageDraggableView imageView;
	private MyLayout relativeLayout;
	private ImageDraggableView imageView1;
	private ImageDraggableView imageView2;
	private ImageDraggableView imageView3;
	private Button button;
	private String selectedImagePath;

    private static final int SELECT_PICTURE = 1;

	
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
        
        imageView3 = new ImageDraggableView(this, null);
        imageView3.setImageResource(R.drawable.image111);
        
        relativeLayout.addView(imageView3);
        imageView3.setParentLayout(relativeLayout);
        
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), SELECT_PICTURE);				
			}
		});
	}

    private void addImageView(String path) {
    	BitmapDrawable bitDrawable = new BitmapDrawable(getResources(), path);
    	Bitmap bitmap = Bitmap.createScaledBitmap(bitDrawable.getBitmap(), 480, 480, true);
        ImageDraggableView imageTView = new ImageDraggableView(this, null);
        imageTView.setImageBitmap(bitmap);
        relativeLayout.addView(imageTView);
        imageTView.setParentLayout(relativeLayout);
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                addImageView(selectedImagePath);
            }
        }
    }

        
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
	
	
    
}
