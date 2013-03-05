package com.k1x.android.draganddropphotos.bitmapUtil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

public class AppBitmapUtil {

    public static Bitmap getDecodedBitmap(String path, float display_width, float display_height) {
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
	
}
