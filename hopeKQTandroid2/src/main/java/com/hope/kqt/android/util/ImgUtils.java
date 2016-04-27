package com.hope.kqt.android.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

public class ImgUtils 
{
	public static void saveBitmap(String path,Bitmap bitmap)
	{
		File file = new File(path);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.flush();
			fos.close();
		}catch (Exception e){
			e.printStackTrace();
		}
		
	}
	
	public static Bitmap readBitMap(InputStream is)
	{
		BitmapFactory.Options options = new BitmapFactory.Options();
		
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		options.inPurgeable = true;
		options.inInputShareable = true;
		
		return BitmapFactory.decodeStream(new BufferedInputStream(is,16*1024),null,options);
	}
	
	public static Bitmap rotaingImageView(int angle,Bitmap bitmap)
	{
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap,0,0,
				bitmap.getWidth(),bitmap.getHeight(),matrix,true);
		
		return resizedBitmap;
	}
	
	public static int readPictureDegree(String path)
	{
		int degree = 0;
		
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			switch(orientation)
			{
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return degree;
	}
}
