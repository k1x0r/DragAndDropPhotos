package com.k1x.android.draganddropphotos;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Random;
import java.io.RandomAccessFile;

import com.k1x.android.draganddropphotos.R;
import com.k1x.android.draganddropphotos.bitmapUtil.AppBitmapUtil;
import com.k1x.android.draganddropphotos.views.ImageDraggableView;
import com.k1x.android.draganddropphotos.views.DraggableLayout;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

    private ImageDraggableView imageView;
	private DraggableLayout draggableLayout;
	private Button button;
	private String selectedImagePath;
	private Button saveButton;


    private static final int SELECT_PICTURE = 1;

	private static final float TARGET_SIZE = 800;
    
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
        
        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Bitmap outBitmap = draggableLayout.drawOutBitmap(2.25f);
				saveBitmap(outBitmap);
				outBitmap.recycle();
			}
		});
	}

    private void saveBitmap(Bitmap bitmap) {


		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		
		String filePath = Environment.getExternalStorageDirectory()
				+ File.separator + "test.jpg";
		System.out.println(filePath);
		File f = new File(filePath);
		try {
	    	File outFile = new File(filePath);
			f.createNewFile();
	        RandomAccessFile randomAccessFile = new RandomAccessFile(outFile, "rw");
	        FileChannel  wChannel = randomAccessFile.getChannel();
	        OutputStream os = Channels.newOutputStream(wChannel);
			os.write(bytes.toByteArray());
			wChannel.close();
			randomAccessFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    private void addImageView(String path) {
    /*    displaymetrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float display_height = displaymetrics.heightPixels;
        float display_width = displaymetrics.widthPixels;
     */
    	Bitmap bitmap = AppBitmapUtil.getDecodedBitmap(path, TARGET_SIZE, TARGET_SIZE);
       	       	
    	float bitmapSize = bitmap.getWidth() > bitmap.getHeight() ? bitmap.getWidth() : bitmap.getHeight();
    	System.out.println("Bitmap Size: " + bitmapSize);
    	float scaleFactor = TARGET_SIZE / bitmapSize  ; 
    	System.out.println("ScaleFactor: " + scaleFactor);

        ImageDraggableView imageTView = new ImageDraggableView(this, null);
        imageTView.setImageBitmap(bitmap);
        imageTView.setImagePath(path);
        
        draggableLayout.addView(imageTView);
        imageTView.setParentLayout(draggableLayout);

        Random R = new Random();
        imageTView.animate().rotation(R.nextFloat() * 90 - 45);
        imageTView.animate().scaleX(scaleFactor);
        imageTView.animate().scaleY(scaleFactor);
        
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
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
	
	
    
}
