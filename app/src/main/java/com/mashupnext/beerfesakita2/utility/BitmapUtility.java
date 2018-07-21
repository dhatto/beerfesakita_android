package com.mashupnext.beerfesakita2.utility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore.Images;
import android.text.format.DateFormat;

public class BitmapUtility {

	public static Bitmap createBitmap(byte[] data, int maxPictureSize, int rotation) {
		BitmapFactory.Options options = queryBitmapSize(data);

		if( maxPictureSize < Math.max(options.outWidth, options.outHeight)){
			int scale = Math.max(options.outWidth, options.outHeight) / maxPictureSize + 1;
			options.inSampleSize = scale;
		}

		Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);

		int orientation = 0;
		switch(rotation) {
	        case 0: orientation = 90; break;
	        case 90: orientation = 0; break;
	        case 180: orientation = 180; break;
	        case 270: orientation = 180; break;
		}

		Matrix matrix = new Matrix();
		matrix.postRotate (orientation);

		return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
	}

	private static BitmapFactory.Options queryBitmapSize(byte[] data) {
		BitmapFactory.Options result = new BitmapFactory.Options();
		result.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(data, 0, data.length, result);
		result.inJustDecodeBounds = false;

		return result;
	}

	public static Uri savePhoto(ContentResolver resolver, Bitmap bitmap) {

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, 100, os);
		byte[] data = os.toByteArray();

        String filename = createName(System.currentTimeMillis()) + ".jpg";
		String filepath = Environment.getExternalStorageDirectory().getPath() + "/" + filename;
		final File OUTPUT_FILE = new File(filepath);
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(OUTPUT_FILE);
			out.write(data);
			out.flush();
			out.close();
		} catch(IOException e) {
			e.printStackTrace();
			return null;
		}

		ContentValues values = new ContentValues();
		values.put(Images.Media.MIME_TYPE,"image/jpeg");
		values.put(Images.Media.DATA, filepath);
		values.put(Images.Media.SIZE, new File(filepath).length());
		values.put(Images.Media.DATE_ADDED, System.currentTimeMillis());
		values.put(Images.Media.DATE_TAKEN, System.currentTimeMillis());
		values.put(Images.Media.DATE_MODIFIED,System.currentTimeMillis());
		Uri result = resolver.insert(Images.Media.EXTERNAL_CONTENT_URI, values);

		return result;
	}

	private static String createName(long dateTaken) {
        return DateFormat.format("yyyy-MM-dd_kk.mm.ss", dateTaken).toString();
    }
}
