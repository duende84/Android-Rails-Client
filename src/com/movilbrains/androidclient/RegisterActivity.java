package com.movilbrains.androidclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.app.Activity;
import android.content.Intent;

public class RegisterActivity extends Activity implements OnClickListener {

	private final String LOG_TAG = "LOG";
	Button btnRegister;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		btnRegister = (Button) findViewById(R.id.btnCreateAccount);
		btnRegister.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.btnCreateAccount:
			postData();

			intent = new Intent(RegisterActivity.this, ResponseActivity.class);
			startActivity(intent);

			break;

		default:
			break;
		}

	}

	public void postData() {
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(
				"http://nuestrosprecios.herokuapp.com/api/users/new");
		httppost.setHeader("Content-Type", "application/json");
		httppost.setHeader("Accept", "application/json");

		try {
			JSONObject params = new JSONObject();
			try {
				params.put("name", "name");
				params.put("email", "create@mail.com");
				params.put("password", "create123");
				params.put("password_confirmation", "create123");
			} catch (JSONException e1) {
				e1.printStackTrace();
			}

			System.out.println("Params: " + params.toString());

			StringEntity entity = new StringEntity(params.toString());
			httppost.setEntity(entity);

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);

			int status = response.getStatusLine().getStatusCode();

			System.out.println("Status: " + status);

			if (status == 200) {

				HttpEntity e = response.getEntity();

				StringBuilder sb = new StringBuilder();
				try {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(e.getContent()), 65728);
					String line = null;

					while ((line = reader.readLine()) != null) {
						sb.append(line);
					}
				} catch (IOException ex) {
					ex.printStackTrace();
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				System.out.println("finalResult " + sb.toString());

			}

		} catch (ClientProtocolException e) {
			Log.v(LOG_TAG, e.toString());
		} catch (IOException e) {
			Log.v(LOG_TAG, e.toString());
		}
	}
}
