package com.nodem.cashbook.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nodem.cashbook.db.DBReader;
import com.nodem.cashbook.db.Database;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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

public class SyncFromServer extends AsyncTask<Void, String, String> {

	private Context context;
	private ProgressDialog pg;
	private String username = null, password = null;

	public SyncFromServer(Context context) {
		this.context = context;
	}

	public SyncFromServer(Context context, String username, String password) {
		this.context = context;
		this.username = username;
		this.password = password;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		pg = new ProgressDialog(context);
		pg.setTitle("Downloading Records...");
		pg.setMessage("Synchronising your records from the cloud please wait...");
		pg.setCancelable(true);
		pg.show();
	}

	public String getTableName() {

		String username = context.getSharedPreferences("PREFS",
				Context.MODE_PRIVATE).getString("username", "not set");
		String pass = context.getSharedPreferences("PREFS",
				Context.MODE_PRIVATE).getString("password", "not set");

		String temp_username = context.getSharedPreferences("PREFS",
				Context.MODE_PRIVATE).getString("temp_username", "not set");
		String temp_pass = context.getSharedPreferences("PREFS",
				Context.MODE_PRIVATE).getString("temp_password", "not set");

		if (username.equals("not set") || pass.equals("not set"))
			return temp_username + "_" + temp_pass;
		else
			return username + "_" + pass;
	}

	@Override
	protected String doInBackground(Void... params) {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(
				"http://mmadandroid.com/cashbook/download.php");
		StatusLine status = null;

		try {
			// add values to array
			List<NameValuePair> values = new ArrayList<NameValuePair>();

			// send table name
			values.add(new BasicNameValuePair("tablename", getTableName()));

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

		Log.i("JSON DATA", result);
		if (pg != null)
			pg.dismiss();

		if (!TextUtils.isEmpty(result) && result != null) {
			parseJSON(result);
			restoreAccountsTable();
			restoreTransactionsTable();
		}
	}

	private List<JSONObject> accountsJObjs, transactionsJObjs;

	private void parseJSON(String result) {
		JSONArray jArray = null;
		try {
			jArray = new JSONArray(result);

			// save login account details
			context.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
					.putString("username", username).commit();
			context.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
					.putString("password", password).commit();
			context.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
					.putBoolean("bypass", false).commit();
			// auto sync the first time
			context.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
					.putBoolean("isSynced", true).commit();

			// things are going good ,flush db now
			if (username == null && password == null) {
				DBReader db = new DBReader(context);
				db.flushDb();
			}

		} catch (JSONException e) {
			if (username != null && password != null)
				showInvalidLoginDialog();

			//
			e.printStackTrace();

		}

		accountsJObjs = new ArrayList<JSONObject>();
		transactionsJObjs = new ArrayList<JSONObject>();

		// get accounts table data
		if (jArray != null)
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject obj = null;
				try {
					obj = jArray.getJSONObject(i);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// accountsJObjs[i] = obj;
				if (obj.length() == 8)
					accountsJObjs.add(obj);

				if (obj.length() == 12)
					transactionsJObjs.add(obj);

			}

	}

	private void showInvalidLoginDialog() {
		AlertDialog.Builder bd = new AlertDialog.Builder(context);
		bd.setTitle("Login Error");
		bd.setCancelable(false);
		bd.setMessage("Wrong password or username . Please sign up if your are new.");
		bd.setNeutralButton("Ok", new Dialog.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				((Activity) context).finish();

			}
		});
		bd.show();
	}

	private void restoreAccountsTable() {
		DBReader db = new DBReader(context);

		try {

			for (int i = 0; i < accountsJObjs.size(); i++) {
				String name = accountsJObjs.get(i).getString(
						Database.COLUMN_ACC_NAME);
				String contact = accountsJObjs.get(i).getString(
						Database.COLUMN_ACC_CONTACT);
				String email = accountsJObjs.get(i).getString(
						Database.COLUMN_ACC_EMAIL);
				String desc = accountsJObjs.get(i).getString(
						Database.COLUMN_ACC_DESC);

				db.insertIntoAccountsTable(name, contact, email, desc);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void restoreTransactionsTable() {
		DBReader db = new DBReader(context);

		for (int i = 0; i < transactionsJObjs.size(); i++) {
			try {
				String name = transactionsJObjs.get(i).getString(
						Database.COLUMN_ACC_NAME);
				String date = transactionsJObjs.get(i).getString(
						Database.COLUMN_TRANS_DATE);
				String due_reminder = transactionsJObjs.get(i).getString(
						Database.COLUMN_DUE_REMINDER);
				String note = transactionsJObjs.get(i).getString(
						Database.COLUMN_TRANS_NOTE);
				String debit = transactionsJObjs.get(i).getString(
						Database.COLUMN_DEBIT);
				String credit = transactionsJObjs.get(i).getString(
						Database.COLUMN_CREDIT);

				db.insertIntoTransactionTable(name, date, due_reminder, note,
						Integer.parseInt(debit), Integer.parseInt(credit));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	void showMessage(String text) {
		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}

}
