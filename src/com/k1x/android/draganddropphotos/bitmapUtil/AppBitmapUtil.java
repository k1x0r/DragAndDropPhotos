package com.k1x.android.draganddropphotos.bitmapUtil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

public class AppBitmapUtil {

    public static Bitmap getDecodedBitmap(String path, float target_width, float target_height) {
    	Options decode_options = new Options();
    	decode_options.inJustDecodeBounds = true;
    	BitmapFactory.decodeFile(path,decode_options);  //This will just fill the output parameters
    	int inSampleSize = calculateInSampleSize(decode_options, target_width, target_height);
    	
    	Options outOptions = new Options();
    	outOptions.inJustDecodeBounds = false;
    	outOptions.inSampleSize = inSampleSize;
    	outOptions.inPreferredConfig =  Bitmap.Config.ARGB_8888;

    	Bitmap decodedBitmap = BitmapFactory.decodeFile(path,outOptions);
    	Bitmap outBitmap = Bitmap.createScaledBitmap(decodedBitmap, 
    			(int)((float)decodedBitmap.getWidth() / inSampleSize),
    			(int)((float)decodedBitmap.getHeight() / inSampleSize), true);
    	System.out.println("Decoded Bitmap: Width "  + outBitmap.getWidth() + " Height = " + outBitmap.getHeight() + " inSampleSize = " + inSampleSize);
    	
    	return outBitmap;
    }

	public static int calculateInSampleSize(Options options, float reqWidth, float reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}
    
}
