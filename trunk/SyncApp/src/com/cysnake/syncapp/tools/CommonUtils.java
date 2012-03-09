package com.cysnake.syncapp.tools;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;

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
}
