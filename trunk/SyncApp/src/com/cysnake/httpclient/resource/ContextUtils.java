package com.cysnake.httpclient.resource;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class ContextUtils extends Application {

	private static Context ctx;

	@Override
	public void onCreate() {
		super.onCreate();
		ctx = this.getApplicationContext();
	}

	/**
	 * get the Application Context object
	 * 
	 * @return the Context object
	 * */
	public static Context getContext() {
		if (ctx == null) {
			Log.e("ContextUtils", "unable to get application context");
		}
		return ctx;
	}
}
