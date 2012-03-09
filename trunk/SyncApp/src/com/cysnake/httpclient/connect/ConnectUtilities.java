package com.cysnake.httpclient.connect;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cysnake.httpclient.exception.HostAddressException;
import com.cysnake.httpclient.resource.ResourceUtil;

import android.util.Log;

/**
 * Provides utility methods that can be used by android system to perform http
 * connection.
 * */
public class ConnectUtilities {

	// singleton ojbect
	private static ConnectUtilities connectUtilities = new ConnectUtilities();

	// the entity used for store request value
	private static HttpEntity requestEntity;

	// the entity used for catch the responseValue
	private static HttpEntity responseEntity;

	private String HOST_ADDRESS = "";

	private String PATH = "";

	private String CHARSET = null;

	private static final String TEMPCHARSET = "UTF-8";

	private ConnectUtilities() {
		try {
			HOST_ADDRESS = ResourceUtil.getHostAddress();
		} catch (HostAddressException e) {
			Log.e("ConnectUtilities", "Can't get The Host Address");
		}
		CHARSET = ResourceUtil.getCharset();
		if ("".equals(CHARSET) || null == CHARSET) {
			CHARSET = TEMPCHARSET;
			Log.w("ConnectUtilities", "Unable to get the Charset!");
		}
	}

	/**
	 * Get the ConnectUtilities singleton object
	 * 
	 * @return the singleton object.
	 * */
	public static ConnectUtilities getInstance() {
		cleanUp();
		return connectUtilities;
	}

	/**
	 * Set the Host address. Default value is modified by the properties file.
	 * 
	 * @param value
	 *            the HostAddress. mostly like "http://www.****.com"
	 * @return the object self.
	 * */
	public ConnectUtilities setHostAddress(String value) {
		HOST_ADDRESS = value;
		return this;
	}

	/**
	 * Get current host address value.
	 * 
	 * @return current host address.
	 * */
	public String getHostAddress() {
		return HOST_ADDRESS;
	}

	/**
	 * Set the request Path
	 * 
	 * @param value
	 *            the path. mostly like ***.do or /***.do
	 * @return the object self.
	 * */
	public ConnectUtilities setPath(String value) {
		if (value.startsWith("/")) {
			PATH = value.substring(1, value.length());
		}
		return this;
	}

	/**
	 * Get the Path
	 * 
	 * @return path
	 * */
	public String getPath() {
		return PATH;
	}

	/**
	 * Set the charset.
	 * 
	 * @param charset
	 *            {@link HTTP}
	 * */
	public void setCharset(String charset) {
		this.CHARSET = charset;
	}

	/**
	 * get the current charset
	 * 
	 * @return charset
	 * */
	public String getCharset() {
		return CHARSET;
	}

	/**
	 * set the request entity with jsonObject
	 * 
	 * @param jsonObject
	 *            the ojbect which want be sent
	 * @return self
	 * */
	public ConnectUtilities setEntity(JSONObject jsonObject)
			throws UnsupportedEncodingException {
		try {
			requestEntity = new StringEntity(jsonObject.toString(), CHARSET);
		} catch (UnsupportedEncodingException e) {
			Log.e("ConnectUtilities",
					"unable to set the StringEntity inital charset:" + CHARSET,
					e);
			throw e;
		}
		return this;
	}

	/**
	 * set the request Entity with list<NameValuePair>.
	 * 
	 * @param list
	 *            the list which which want be sent return self
	 * */
	public ConnectUtilities setEntity(List<NameValuePair> list)
			throws UnsupportedEncodingException {
		try {
			requestEntity = new UrlEncodedFormEntity(list, CHARSET);
		} catch (UnsupportedEncodingException e) {
			Log.e("ConnectUtilities",
					"Unable to set the UrlEncodedFormEntity inital charset:"
							+ CHARSET, e);
			throw e;
		}

		return this;
	}

	/**
	 * Send the request in post way.
	 * 
	 * @return self
	 * @throws ClientProtocolException
	 *             , IOException
	 * */
	public ConnectUtilities post() throws ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost(HOST_ADDRESS + PATH);
		if (null != requestEntity) {
			request.setEntity(requestEntity);
		}else{
			Log.e("ConnectUtilities", "requestEntity is null ,maybe you haven't run post() yet. please check you code");
		}
		HttpResponse response;
		try {
			response = client.execute(request);
			responseEntity = response.getEntity();
		} catch (ClientProtocolException e) {
			Log.e("ConnectUtilities",
					"can't get connection to the server! the whole path is "
							+ HOST_ADDRESS + PATH, e);
			throw e;
		} catch (IOException e) {
			Log.e("ConnectUtilities",
					"can't get connection to the server! the whole path is "
							+ HOST_ADDRESS + PATH, e);
			throw e;
		}
		return this;
	}

	public String getResultByString() throws IOException, ParseException {
		if (responseEntity != null) {
			String resultString = null;
			try {
				resultString = EntityUtils.toString(responseEntity, CHARSET);
			} catch (ParseException e) {
				Log.e("ConnectUtilities",
						"Unable to transform entity to String", e);
				throw e;
			} catch (IOException e) {
				Log.e("ConnectUtilities",
						"Unable to transform entity to String", e);
				throw e;
			}
			return resultString;
		} else {
			return null;
		}

	}

	public JSONObject getResultByJSONObject() throws ParseException,
			IOException, JSONException {
		JSONObject jsonObject = null;
		String resultString = getResultByString();
		if (resultString != null) {
			try {
				jsonObject = new JSONObject(resultString);
			} catch (JSONException e) {
				Log.e("ConnectUtilities", "Unable to change string: "
						+ resultString + " to JsonOjbect", e);
				throw e;
			}

		}
		return jsonObject;
	}

	public JSONArray getResultByJSONArray() throws ParseException, IOException,
			JSONException {
		String resultString = getResultByString();
		JSONArray jsonArray = null;
		if (resultString != null) {
			try {
				jsonArray = new JSONArray(resultString);
			} catch (JSONException e) {
				Log.e("ConnectUtilities", "unable change the string : "
						+ resultString + " to JsonArray", e);
				throw e;
			}
		}
		return jsonArray;
	}

	private static void cleanUp() {
		requestEntity = null;
		responseEntity = null;
	}

}
