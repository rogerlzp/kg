package com.abc.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import android.util.Log;

public class CellSiteHttpClient {

	public static String TAG = "CellSiteHttpClient";
	/** The time it takes for our client to timeout */
	public static final int HTTP_TIMEOUT = 30 * 1000; // milliseconds
	/** Single instance of our HttpClient */
	private static HttpClient mHttpClient;

	// to avoid Invalid use of SinleConnectionConnMagager Error
	public static HttpClient getThreadSafeClient() {
		if (mHttpClient == null) {
			// TODO:
		}
		return mHttpClient;
	}

	private static HttpClient getHttpClient() {
		if (mHttpClient == null) {
			mHttpClient = new DefaultHttpClient();
			final HttpParams params = mHttpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, HTTP_TIMEOUT);
			HttpConnectionParams.setSoTimeout(params, HTTP_TIMEOUT);
			ConnManagerParams.setTimeout(params, HTTP_TIMEOUT);
		}

		return mHttpClient;
	}

	public synchronized static JSONObject executeHttpPost(String url,
			ArrayList<NameValuePair> postParameters) throws Exception {

		JSONObject result = null;
		HttpEntity entity = null;
		HttpResponse response = null;
		try {
			mHttpClient = getHttpClient();
			HttpPost request = new HttpPost(url);
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
					postParameters, "UTF-8");
			request.setEntity(formEntity);
			response = mHttpClient.execute(request);
			int resultCode = response.getStatusLine().getStatusCode();
			Log.d(TAG, "response code =" + resultCode);
			entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				String tmp = convertStreamToString(instream);
				Log.d(TAG, "result =" + tmp);
				result = new JSONObject(tmp);
				instream.close();

				//
			}
			Log.d(TAG, result.toString());
			return result;
		} catch (HttpResponseException e) {
			Log.d(TAG, "HttpResponse Exception :" + e.getMessage());

		} finally {
			try {
				entity.consumeContent();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;

	}

	public synchronized static JSONObject executeHttpGet(String url) {
		JSONObject result = null;
		try {
			mHttpClient = getHttpClient();
			HttpGet request = new HttpGet();
			request.setURI(new URI(url));
			HttpResponse response = mHttpClient.execute(request);
			int resultCode = response.getStatusLine().getStatusCode();
			Log.d(TAG, "response code =" + resultCode);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				String tmp = convertStreamToString(instream);
				result = new JSONObject(tmp);
				instream.close();

			}
			Log.d(TAG, result.toString());
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			// mHttpClient.getConnectionManager().shutdown();
		}
		return null;

	}

	public static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();

	}

}
