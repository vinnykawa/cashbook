package com.nodem.cashbook.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.HttpStatus;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.StatusLine;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class UpdatePassword extends AsyncTask<Void, String, String> {

	private String password;
	private Context context;
	private ProgressDialog pg;

	public UpdatePassword(Context context, String pass) {
		password = pass;
		this.context = context;
	}

	public String getTableName() {

		String username = context.getSharedPreferences("PREFS",
				Context.MODE_PRIVATE).getString("username", "");
		String pass = context.getSharedPreferences("PREFS",
				Context.MODE_PRIVATE).getString("password", "");

		return username + "_" + pass;
	}
	
	public String getNewTableName()
	{
		String username = context.getSharedPreferences("PREFS",
				Context.MODE_PRIVATE).getString("username", "");
		return username+ "_" +password;
	}
			
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		pg=new ProgressDialog(context);
		pg.setTitle("Updating Password...");
		pg.setMessage("Synchronising password to server please wait...");
		pg.setCancelable(false);
		pg.show();
	}	

	@Override
	protected String doInBackground(Void... params) {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(
				"http://mmadandroid.com/cashbook/changepassword.php");
		StatusLine status = null;
		try {
			// add values to array
			List<NameValuePair> values = new ArrayList<NameValuePair>();

			// send table name
			values.add(new BasicNameValuePair("tablename", getTableName()));
			values.add(new BasicNameValuePair("new_tablename",getNewTableName()));

			post.setEntity(new UrlEncodedFormEntity(values));

			String responseString;
			// execute
			HttpResponse response = client.execute(post);
			status = response.getStatusLine();
			if (status.getStatusCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				responseString = out.toString();

			} else {
				// Closes the connection.
				response.getEntity().getContent().close();
				throw new IOException(status.getReasonPhrase());
			}

			return responseString;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		Log.i("PHP-UPDATE-PASSWORD", result);

		if(pg!=null)
			pg.dismiss();
		
	
		// save new password
		context.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
				.putString("password", password).commit();
		
		((Activity) context).finish();
	}
}
