package com.cysnake.syncapp.tools;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class CommonUtils {
	public static byte[] getBitePhoto(Bitmap photoMap,
			Bitmap.CompressFormat format, int quality) {
		if (photoMap != null) {
			ByteArrayOutputStream streamy = new ByteArrayOutputStream();
			photoMap.compress(format, quality, streamy);
			return streamy.toByteArray();

		}
		return null;
	}

	public static Bitmap getBitmap(byte[] b) {
		if (b.length > 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}

	}

	public static Bitmap getPhotoByNet(String ssurl) {
		try {
			URL url = new URL(ssurl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			InputStream is = conn.getInputStream();
			Bitmap bitmap = BitmapFactory.decodeStream(is);
			return bitmap;

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
