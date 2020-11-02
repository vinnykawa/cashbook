package com.nodem.cashbook;

import com.nodem.cashbook.db.DBReader;
import com.nodem.cashbook.net.SyncToServer;
import com.vmk.cashbook.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewAccount extends Activity implements OnClickListener{

	private EditText edAcName, edEmail, edContact, edDesc;
	private Button btnSave, btnCancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_new_account);

		initViews();
	}

	private void initViews() {
		edAcName = (EditText) findViewById(R.id.ed_acc_name);
		edEmail = (EditText) findViewById(R.id.ed_email);
		edContact = (EditText) findViewById(R.id.ed_contact);
		edDesc = (EditText) findViewById(R.id.ed_desc);

		btnSave = (Button) findViewById(R.id.btn_save);
		btnCancel = (Button) findViewById(R.id.btn_cancel);
		
		btnSave.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		
		String accName = edAcName.getText().toString();
		String email = edEmail.getText().toString();
		String contact = edContact.getText().toString();
		String desc = edDesc.getText().toString();
		
		
		
		if(btnSave == v){
			
			DBReader db = new DBReader(getApplicationContext());
			
			db.insertIntoAccountsTable(accName, contact, email, desc);
			Toast.makeText(this, "account added", Toast.LENGTH_LONG).show();
			onBackPressed();
			//sync to server
			new SyncToServer(this, false).execute();
			
		}else if(btnCancel == v){
			onBackPressed();
		}
		
		
	}
}
