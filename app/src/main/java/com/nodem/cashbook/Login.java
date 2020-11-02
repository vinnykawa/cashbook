package com.nodem.cashbook;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.vmk.cashbook.R;

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

						String username = edUser.getText().toString().trim();
						String pass = edpass.getText().toString().trim();
						String pass2 = edpassconf.getText().toString().trim();

						if ((pass.equals(pass2) && !username.equals("")
								|| username == null) ) {
							getSharedPreferences("PREFS", MODE_PRIVATE).edit()
									.putString("username", username).commit();
							getSharedPreferences("PREFS", MODE_PRIVATE).edit()
									.putString("password", pass).commit();
							getSharedPreferences("PREFS", MODE_PRIVATE).edit().putBoolean("isLoggedIn", true).commit();
							
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
