package com.nodem.cashbook;

import com.nodem.cashbook.adapters.LedgerAdapter;
import com.nodem.cashbook.db.DBReader;
import com.nodem.cashbook.db.DBReader.TransactionsTable;
import com.vmk.cashbook.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

public class Ledger extends Activity{

	private ListView list;
	private TextView name,phone,bal;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ledger);
		
		list=(ListView)findViewById(R.id.list_ledger);
		name=(TextView)findViewById(R.id.txt_lName);
		phone=(TextView)findViewById(R.id.txt_lphone);
		bal =(TextView)findViewById(R.id.txt_lBal);
		
		bal.setText(getIntent().getExtras().getString("amount"));
		name.setText(getIntent().getExtras().getString("name"));
		phone.setText(getIntent().getExtras().getString("phone"));
		
		
		DBReader db = new DBReader(this);
		DBReader.TransactionsTable transactions = db.new TransactionsTable(getIntent().getExtras().getString("name"));
		
		LedgerAdapter adapter = new LedgerAdapter(this, transactions.getTransactionDates(), transactions.getTransactionAmounts(), transactions.getTransactionDueDates());
		
		list.setAdapter(adapter);
		
		
		
		
	}
}
