package com.cysnake.syncapp.tools;

import java.io.ByteArrayOutputStream;

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
}
