package com.movilbrains.androidclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

public class ResponseActivity extends Activity {

	public static JSONArray info = null;
	final static String URL = "http://nuestrosprecios.herokuapp.com/users.json";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_response);
		
		new DownloadJSONFile().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.response, menu);
		return true;
	}

	public String getStringFromURL(String url) {
		StringBuilder strB = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url.toString());

		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusline = response.getStatusLine();
			int statusCode = statusline.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content), 1);
				String line;
				while ((line = reader.readLine()) != null) {
					strB.append(line);
				}
			} else {
				Log.e("JSON", "Error: Failed to download file");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return strB.toString();
	}

	public class DownloadJSONFile extends AsyncTask<String, Float, Integer> {

		@Override
		protected Integer doInBackground(String... url) {
			String text = getStringFromURL(URL);
			Log.d("INFO", text);
			try {
				info = new JSONArray(text);
			} catch (JSONException e) {
				info = null;
				Log.e("JSON", "Error: Failed to parse file");
			}
			return 0;
		}

		@Override
		protected void onPostExecute(Integer bytes) {

		}
	}

}
