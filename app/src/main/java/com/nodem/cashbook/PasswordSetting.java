package com.nodem.cashbook;

import com.google.android.material.textfield.TextInputEditText;
import com.nodem.cashbook.net.UpdatePassword;
import com.vmk.cashbook.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PasswordSetting extends Activity{

	EditText edP,edP1;
	Button change;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.passwordsetting);
		
		
		edP = (EditText)findViewById(R.id.ed_new_pass0);
		edP1 = (EditText)findViewById(R.id.ed_new_pass);
		change = (Button)findViewById(R.id.btnchangepass);

		change.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String pass1= edP.getText().toString().trim();
				String pass2 =edP1.getText().toString().trim();
				
				if(pass1.equals(pass2))
					//sync
					new UpdatePassword(PasswordSetting.this, pass1).execute();
				else
					edP.setError("passwords do not match");
				
			}
		});
		
		
	}
}
