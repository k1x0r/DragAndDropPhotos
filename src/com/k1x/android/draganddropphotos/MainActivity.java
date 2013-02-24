package com.k1x.android.draganddropphotos;

import java.util.Random;

import com.k1x.android.draganddropphotos.R;
import com.k1x.android.draganddropphotos.views.ImageDraggableView;
import com.k1x.android.draganddropphotos.views.DraggableLayout;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

    private ImageDraggableView imageView;
	private DraggableLayout draggableLayout;
	private Button button;
	private String selectedImagePath;
	private DisplayMetrics displaymetrics;


    private static final int SELECT_PICTURE = 1;

	private static final float TARGET_SIZE = 400;
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpViews();
    }

    private void setUpViews() {
        setContentView(R.layout.activity_main);
		
        draggableLayout = (DraggableLayout) findViewById(R.id.relativeLayout);
        imageView = (ImageDraggableView) findViewById(R.id.imageView);
        imageView.setParentLayout(draggableLayout);
                       
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
    	Bitmap bitmap = getDecodedBitmap(path);
       	       	
    	float bitmapSize = bitmap.getWidth() > bitmap.getHeight() ? bitmap.getWidth() : bitmap.getHeight();
    	System.out.println("Bitmap Size: " + bitmapSize);
    	float scaleFactor = TARGET_SIZE / bitmapSize  ; 
    	System.out.println("ScaleFactor: " + scaleFactor);

        ImageDraggableView imageTView = new ImageDraggableView(this, null);
        imageTView.setImageBitmap(bitmap);
        
        draggableLayout.addView(imageTView);
        imageTView.setParentLayout(draggableLayout);

        Random R = new Random();
        imageTView.animate().rotation(R.nextFloat() * 90 - 45);
        imageTView.animate().scaleX(scaleFactor);
        imageTView.animate().scaleY(scaleFactor);
        
    }
    
    private Bitmap getDecodedBitmap(String path) {
        displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float display_height = displaymetrics.heightPixels;
        float display_width = displaymetrics.widthPixels;
    	float scale_param = 0;

    	Options decode_options = new Options();
    	decode_options.inJustDecodeBounds = true;
    	BitmapFactory.decodeFile(path,decode_options);  //This will just fill the output parameters
		if(decode_options.outWidth > display_width
    	        || decode_options.outHeight > display_height)
    	{
    	    float scale_width,scale_height;

    	    scale_width = ((float)decode_options.outWidth) / display_width;
    	    scale_param = scale_width;
    	    scale_height = ((float)decode_options.outHeight) / display_height;

    	    if(scale_param < scale_height) {
    	        scale_param = scale_height;
    	    }
    	}

    	decode_options.inJustDecodeBounds = false;
    	decode_options.inSampleSize  = (int)(scale_param + 1);
    	decode_options.inPreferredConfig =  Bitmap.Config.ARGB_8888;
    	return BitmapFactory.decodeFile(path,decode_options);
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getRealPathFromURI(selectedImageUri);
                System.out.println(selectedImagePath);
                addImageView(selectedImagePath);
            }
        }
    }

        
    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
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
