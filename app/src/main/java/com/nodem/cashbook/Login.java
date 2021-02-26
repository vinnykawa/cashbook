package com.nodem.cashbook;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.vmk.cashbook.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class Login extends AppCompatActivity {

	private String s_username, s_pass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		s_username = getSharedPreferences("PREFS", MODE_PRIVATE).getString(
				"username", "not set");
		s_pass = getSharedPreferences("PREFS", MODE_PRIVATE).getString(
				"password", "not set");

		
		 if (s_username.equals("not set") && s_pass.equals("not set"))
			initSignUp();
		else{
			
			if(getSharedPreferences("PREFS", MODE_PRIVATE).getBoolean("isLoggedIn", false)){
				String username = getSharedPreferences("PREFS", MODE_PRIVATE).getString("temp_username", "");
				String pass = getSharedPreferences("PREFS", MODE_PRIVATE).getString("temp_password", "");
				
				startActivity(new Intent(getApplicationContext(),MainActivity.class)
				.putExtra("username", username)
				.putExtra("password", pass));
			}else
			initLogin();
		}
	}

	private void initLogin(){
		setContentView(R.layout.login);
		final EditText edUser = (EditText) findViewById(R.id.ed_username);
		final EditText edpass = (EditText) findViewById(R.id.ed_pass);
		
		findViewById(R.id.btnlogin).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String username = edUser.getText().toString().trim();
				String pass = edpass.getText().toString().trim();
				
				
				
				if(s_username.equals(username)&&s_pass.equals(pass) ||  getSharedPreferences("PREFS", MODE_PRIVATE).getBoolean("bypass", false)){
					
					getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
					.putString("temp_username", username).commit();
					getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
					.putString("temp_password", pass).commit();
					
					getSharedPreferences("PREFS", MODE_PRIVATE).edit().putBoolean("isLoggedIn", true).commit();



					startActivity(new Intent(getApplicationContext(),MainActivity.class)
					.putExtra("username", username)
					.putExtra("password", pass));
					
					finish();
				}else{
					if(!s_username.equals(username))
						edUser.setError("Wrong username");
					else
						edpass.setError("Wrong password");
					
				//	new SyncFromServer(getApplicationContext()).execute();
				}
				
			}
		});

		findViewById(R.id.txt_signup).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				initSignUp();
			}
		});
	}

	private void initSignUp() {
		setContentView(R.layout.signup);

		final EditText edUser = (EditText) findViewById(R.id.ed_username);
		final EditText edpass = (EditText) findViewById(R.id.ed_pass);
		final EditText edpassconf = (EditText) findViewById(R.id.ed_pass1);

		findViewById(R.id.btn_signup).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {

						final String username = edUser.getText().toString().trim();
						final String pass = edpass.getText().toString().trim();
						String pass2 = edpassconf.getText().toString().trim();

						if ((pass.equals(pass2) && !username.equals("")
								|| username == null) ) {
							getSharedPreferences("PREFS", MODE_PRIVATE).edit()
									.putString("username", username).commit();
							getSharedPreferences("PREFS", MODE_PRIVATE).edit()
									.putString("password", pass).commit();
							getSharedPreferences("PREFS", MODE_PRIVATE).edit().putBoolean("isLoggedIn", true).commit();

							//register user in db
							new AsyncTask<String,String,String>(){
								@Override
								protected String doInBackground(String... strings) {
									HttpClient client = new DefaultHttpClient();
									HttpPost post = new HttpPost(
											"http://cashbook.nodemtech.com/register.php");
									StatusLine status = null;
									try {
										// add values to array
										List<NameValuePair> values = new ArrayList<NameValuePair>();

										//send table name
										values.add(new BasicNameValuePair("username", username));
										values.add(new BasicNameValuePair("password", pass));

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
								protected void onPostExecute(String s) {
									super.onPostExecute(s);


									try {
										JSONObject jsonObj = new JSONObject(s);

										int user_id = jsonObj.getInt("user_id");
										//save to prefs
										getSharedPreferences("PREFS", MODE_PRIVATE).edit().putInt("user_id",user_id).commit();


									} catch (JSONException e) {
										e.printStackTrace();
									}

									Log.e("PHP", s);


								}
							}.execute();


							startActivity(new Intent(getApplicationContext(),MainActivity.class));
							finish();

						} else {
							if (username.equals("") || username == null)
								edUser.setError("please choose a username");
							else
								edpass.setError("Passwords do not match");
						}

					}
				});
		
		findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			initLogin();
			getSharedPreferences("PREFS", MODE_PRIVATE).edit().putBoolean("bypass", true).commit();
				//recreate();
			}
		});
			
		

	}
}
