package com.nodem.cashbook;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.nodem.cashbook.net.SyncFromServer;
import com.nodem.cashbook.net.SyncToServer;
import com.vmk.cashbook.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Backup extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.backup);
		
		final TextView last = (TextView)findViewById(R.id.txt_backup);
		
		findViewById(R.id.btn_backup).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				new SyncToServer(Backup.this).execute();
				String date ="Last Back up date :"+new SimpleDateFormat("yyyy-MMM-dd").format(new Date());
				String saved =getSharedPreferences("PREFS", MODE_PRIVATE).getString("backup", "No Backup yet");
				getSharedPreferences("PREFS", MODE_PRIVATE).edit().putString("backup", date).commit();
				
				last.setText(saved);
			}
		});
		
		findViewById(R.id.btn_restore).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				new SyncFromServer(Backup.this).execute();
			
			}
		});
		
		
		
	}
}
