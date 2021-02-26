package com.nodem.cashbook.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


import com.nodem.cashbook.db.DBReader;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
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

public class SyncToServer extends AsyncTask<String, String, String> {

	private Context context;
	private DBReader reader;
	private DBReader.AccountsTable accountsTable;
	private DBReader.TransactionsTable transactions;
	private ProgressDialog pg;
	private boolean isVisible = true;

	public SyncToServer(Context context) {
		this.context = context;
		reader = new DBReader(context);
		accountsTable = reader.new AccountsTable();
		transactions = reader.new TransactionsTable();
	}
	
	public SyncToServer(Context context,boolean isVisible) {
		this.context = context;
		reader = new DBReader(context);
		accountsTable = reader.new AccountsTable();
		transactions = reader.new TransactionsTable();
		this.isVisible = isVisible;
	}

	public String getTableName() {

		String username = context.getSharedPreferences("PREFS",
				Context.MODE_PRIVATE).getString("username", "");
		String pass = context.getSharedPreferences("PREFS",
				Context.MODE_PRIVATE).getString("password", "");


		return username + "_" + pass;
	}
	
	void showMessage(String text){
		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		pg=new ProgressDialog(context);
		pg.setTitle("Uploading Records...");
		pg.setMessage("Synchronising records to server please wait...");
		pg.setCancelable(true);
		if(isVisible)
			pg.show();
	}

	@Override
	protected String doInBackground(String... params) {

		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(
				"http://cashbook.nodemtech.com/upload.php");
		StatusLine status = null;
		try {
			// add values to array
			List<NameValuePair> values = new ArrayList<NameValuePair>();

			int userId = context.getSharedPreferences("PREFS",
					Context.MODE_PRIVATE).getInt("user_id", 0);
			
			//send table name
			values.add(new BasicNameValuePair("user_id",""+userId));

			// get names
			for (String s : accountsTable.getAccNames())
				values.add(new BasicNameValuePair("accountNames[]", s));

			// get contacts
			for (String con : accountsTable.getAccContacts())
				values.add(new BasicNameValuePair("accountContacts[]", con));
			
			//emails
			for (String email : accountsTable.getAccountEmails())
				values.add(new BasicNameValuePair("accountEmails[]", email));
			
			//descriptions
			for (String desc : accountsTable.getAccountDescriptions())
				values.add(new BasicNameValuePair("accountDescriptions[]", desc));
			
			
			/**Transactions Table**/
			//names
			for (String n : transactions.getTransactionAccountNames())
				values.add(new BasicNameValuePair("transactionNames[]", n));
			
			//dates
			for (String date : transactions.getTransactionDates())
				values.add(new BasicNameValuePair("transactionDates[]", date));
			
			//due reminders
			for (String rem : transactions.getTransactionDueDates())
				values.add(new BasicNameValuePair("transactionDueDates[]", rem));
			
			//notes
			for (String note : transactions.getTransactionNotes())
				values.add(new BasicNameValuePair("transactionNotes[]", note));
			
			//credits
			for (int cr : transactions.getCreditTransactions())
				values.add(new BasicNameValuePair("transactionCredits[]", ""+cr));
			
			//debits
			for (int db : transactions.getDebitTransactions())
				values.add(new BasicNameValuePair("transactionDebits[]", ""+db));
			
			

			post.setEntity(new UrlEncodedFormEntity(values));

			String responseString;
			// execute
			HttpResponse response = client.execute(post);
			status = response.getStatusLine();
			  if(status.getStatusCode() == HttpStatus.SC_OK){
	                ByteArrayOutputStream out = new ByteArrayOutputStream();
	                response.getEntity().writeTo(out);
	                out.close();
	                responseString = out.toString();
	                
	            } else{
	                //Closes the connection.
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

		if(pg!=null && pg.isShowing())
			pg.dismiss();

		Log.e("PHP", result);
			//pg.dismiss();



	}
	
	

}
