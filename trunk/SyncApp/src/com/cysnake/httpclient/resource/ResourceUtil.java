package com.cysnake.httpclient.resource;

import java.util.Properties;

import com.cysnake.httpclient.exception.HostAddressException;

import android.content.res.AssetManager;
import android.util.Log;

public class ResourceUtil {
	private static Properties config;
	private static Properties configApp;
	private static String fileName;

	private ResourceUtil() {
	}

	private static void getConfigFromFile() {
		AssetManager assets = ContextUtils.getContext().getResources()
				.getAssets();
		config = new Properties();
		try {
			if (null != fileName) {
				config.load(assets.open(fileName));
			} else {
				config.load(assets.open("sourse.properties"));
			}
		} catch (Exception ex) {
			Log.e("MonsterResource", "unable to get the properties file", ex);
		}
	}

	/**
	 * get the info by key
	 * 
	 * @param key
	 *            the key string
	 * @return string info
	 */
	public static String getString(String key) {
		if (config == null) {
			getConfigFromFile();
		}
		String value = config.getProperty(key, "");
		if (value == "") {
			value = configApp.getProperty(key, "");
		}
		return value;
	}

	/**
	 * get the inteter base on key value.
	 * 
	 * @param key
	 *            the key string
	 * @return int info
	 */
	public static int getInt(String key) {
		String value = ResourceUtil.getString(key);
		int result = 0;
		try {
			result = new Integer(value).intValue();
		} catch (NumberFormatException e) {
			System.out.println(e);
		}
		return result;
	}

	public static int stringToInt(String str) {
		int result = -1;
		if (str == null || "".equals(str.trim())) {
			result = -1;
		} else {
			try {
				result = Integer.parseInt(str);
			} catch (Exception e) {
				result = -1;
			}
		}
		return result;
	}

	/**
	 * get the string by the key and dltValue
	 * 
	 * @param key
	 *            the key string
	 * @param dltValue
	 * @return string info
	 */
	public static String getString(String key, String dltValue) {
		if (config == null) {
			getConfigFromFile();
		}
		return config.getProperty(key, dltValue);
	}

	public static String getHostAddress() throws HostAddressException {
		if (config == null) {
			getConfigFromFile();
		}
		if("".equals(config.getProperty("renren.host.address"))){
			throw new HostAddressException();
		}
		return config.getProperty("renren.host.address");
	}

	public static String getCharset() {
		if (config == null) {
			getConfigFromFile();
		}
		return config.getProperty("renren.host.charset");
	}
	
	public static void setFileName(String name){
		fileName=name;
	}
}
