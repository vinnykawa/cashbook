package com.nodem.cashbook;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.util.TimeUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.nodem.cashbook.db.DBReader;
import com.nodem.cashbook.net.SyncToServer;
import com.vmk.cashbook.R;

public class NewTransaction extends Activity implements OnClickListener {

	private EditText edAccName, edAmount, edNote;
	private TextView txtToday, txtReminder;
	private CheckBox reminder;
	private Button btnCancel, btnDebit, btnCredit;
	private ImageButton btnAdd;

	private DBReader db;
	private DBReader.TransactionsTable transactions;
	private DBReader.AccountsTable acTable;
	private Calendar cal;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_transaction);

		initViews();

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		txtToday.setText("" + format.format(new Date()));
		
		 cal = Calendar.getInstance();
		cal.setTime(new Date());
		
		
		
		txtReminder.setText(""+cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+(cal.get(Calendar.DAY_OF_MONTH)+1));

		db = new DBReader(this);
		acTable = db.new AccountsTable();
		transactions = db.new TransactionsTable();
		

	}
	static final int DATE_DIALOG_ID = 0;
	public void initViews() {
		edAccName = (EditText) findViewById(R.id.ed_trans_acc_name);
		edAmount = (EditText) findViewById(R.id.ed_trans_amount);
		edNote = (EditText) findViewById(R.id.ed_trans_note);
		txtToday = (TextView) findViewById(R.id.txt_today);

		txtReminder = (TextView) findViewById(R.id.txt_reminder_date);
		txtReminder.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
		});

		reminder = (CheckBox) findViewById(R.id.check_reminder);

		btnAdd = (ImageButton) findViewById(R.id.btn_add_new_trans);
		btnCancel = (Button) findViewById(R.id.btn_trans_cancel);
		btnDebit = (Button) findViewById(R.id.btn_debit);
		btnCredit = (Button) findViewById(R.id.btn_credit);

		btnAdd.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		btnDebit.setOnClickListener(this);
		btnCredit.setOnClickListener(this);
		reminder.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if(isChecked)
				showDialog(DATE_DIALOG_ID);
				
			}
		});

	}
	
	int mYear,mMonth,mDay;
	
	@Override
	protected Dialog onCreateDialog(int id) {
	   switch (id) {
	   case DATE_DIALOG_ID:
	      return new DatePickerDialog(this,
	                mDateSetListener,
	                mYear, mMonth, mDay);
	   }
	   return null;
	}
	
	private DatePickerDialog.OnDateSetListener mDateSetListener =
		    new DatePickerDialog.OnDateSetListener() {
		        public void onDateSet(DatePicker view, int year, 
		                              int monthOfYear, int dayOfMonth) {
		            mYear = year;
		            mMonth = monthOfYear;
		            mDay = dayOfMonth;
		            txtReminder.setText(year+"-"+monthOfYear+"-"+dayOfMonth);
		        }
		    };

	@Override
	public void onClick(View v) {

		if (btnAdd == v) {
			launchAccSelectDialog();
		} else if (btnCancel == v) {
			onBackPressed();
		} else if (btnDebit == v || btnCredit == v) {

			String name = edAccName.getText().toString();
			String date = txtToday.getText().toString();
			String due_reminder = txtReminder.getText().toString();
			String note = edNote.getText().toString();
			int amount = Integer.parseInt(edAmount.getText().toString());

			if (btnDebit == v){
				db.insertIntoTransactionTable(name, date, reminder.isChecked()?due_reminder:"", note,
						amount, 0);

				Toast.makeText(this,"Amount Debited",Toast.LENGTH_SHORT).show();
			}


			else if (btnCredit == v){
				db.insertIntoTransactionTable(name, date, due_reminder, note,
						0, amount);
				Toast.makeText(this,"Amount Credited",Toast.LENGTH_SHORT).show();

			}
			
			
			 Intent intent = new Intent(this, Mote.class);
			    PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 1253, intent, PendingIntent.FLAG_UPDATE_CURRENT|  Intent.FILL_IN_DATA);

			    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
			    cal.set(mYear, mMonth, mDay);

			    alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),pendingIntent );
			//sync to server
			new SyncToServer(this, false).execute();
			onBackPressed();
		}

	}

	private void launchAccSelectDialog() {

		String[] accNames = new String[acTable.getAccNames().size()];
		for (int i = 0; i < acTable.getAccNames().size(); i++)
			accNames[i] = acTable.getAccNames().get(i);

		AlertDialog.Builder bd = new AlertDialog.Builder(NewTransaction.this);
		bd.setTitle("Select Account");
		bd.setItems(accNames, new Dialog.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				edAccName.setText(acTable.getAccNames().get(which));
				dialog.dismiss();
			}
		});
		bd.show();

	}
}
